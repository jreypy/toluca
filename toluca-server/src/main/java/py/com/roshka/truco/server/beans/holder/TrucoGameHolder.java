package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.server.service.AMQPSender;
import py.com.roshka.truco.server.service.impl.AMQPSenderImpl;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.*;

import java.util.*;


public class TrucoGameHolder extends TrucoGame implements TrucoListener {


    static final int TEAM_1 = 0;
    static final int TEAM_2 = 1;

    Logger logger = LoggerFactory.getLogger(TrucoGameHolder.class);
    int MAX = 6;

    private Map<String, TrucoPlayer> players = new LinkedHashMap<>();
    private Map<SpanishCard, TrucoCard> cards = new LinkedHashMap<>();

    private TrucoUser[] positions = new TrucoUser[MAX];
    private TrucoTableHolder trucoTableHolder;
    TrucoGameImpl target = null;
    private TrucoGameData trucoGameData = null;
    final private AMQPSender amqpSender;
    final private ObjectMapper objectMapper;

    public TrucoGameHolder(TrucoTableHolder trucoTableHolder, AMQPSender amqpSender, ObjectMapper objectMapper) {
        this.trucoTableHolder = trucoTableHolder;
        this.objectMapper = objectMapper;
        this.amqpSender = amqpSender;
    }

    public TrucoUser[] getPositions() {
        return positions;
    }

    @Override
    public void startGame() {
        logger.debug("Starting Game [" + trucoTableHolder.getTable().getId() + "]");
        if (target == null) {
            TrucoTeam[] teams = teams();
            target = new TrucoGameImpl(teams[0], teams[1], trucoTableHolder.getTable().getPoints());
            target.addTrucoListener(this);
            target.setTableNumber(Integer.parseInt(trucoTableHolder.getTable().getId()));
            trucoGameData = new TrucoGameData();
            trucoGameData.setId(trucoTableHolder.getTable().getId());
            trucoGameData.setTeam1(getTrucoGameTeam(TEAM_1));
            trucoGameData.setTeam2(getTrucoGameTeam(TEAM_2));
            trucoGameData.setPoints(trucoTableHolder.getTable().getPoints());
        }
        target.startGame();
    }


    private TrucoTeam[] teams() {
        TrucoTeam team1 = new TrucoTeam();
        TrucoTeam team2 = new TrucoTeam();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] != null) {
                if (i % 2 == 0) {
                    team1.addPlayer(getPlayer(positions[i].getUsername()));
                } else {
                    team2.addPlayer(getPlayer(positions[i].getUsername()));
                }
            }
        }
        return new TrucoTeam[]{
                team1,
                team2
        };
    }

    private TrucoPlayer getPlayer(String name) {
        if (players.containsKey(name))
            return players.get(name);
        TrucoPlayer trucoPlayer = new TrucoPlayer(name);
        this.players.put(name, trucoPlayer);
        return trucoPlayer;
    }


    private TrucoCard getCard(SpanishCard spanishCard) {
        return cards.get(spanishCard);
    }


    @Override
    public void startHand(TrucoPlayer tPlayer) {
        logger.debug("Start Hand [" + trucoTableHolder.getTable().getId() + "]");
    }

    @Override
    public boolean esPosibleJugar(TrucoPlay tp) {
        logger.debug("Start Hand [" + trucoTableHolder.getTable().getId() + "]");
        return false;
    }

    @Override
    public void play(TrucoPlay tp) throws InvalidPlayExcepcion {
        logger.debug("Playing [" + tp + "]");
        target.play(tp);

    }

    public void play(TrucoGamePlay trucoGamePlay) {
        TrucoPlay trucoPlay = TolucaHelper.getPlay(trucoGamePlay);
        trucoPlay.setPlayer(getPlayer(trucoGamePlay.getPlayer().getId()));

        if (trucoGamePlay.getCard() != null) {
            TrucoCard trucoCard = cards.get(trucoGamePlay.getCard());
            trucoPlay.setCard(trucoCard);
            logger.debug("Player is playing [" + trucoCard + "]");
        }


        if (trucoPlay.getPlayer() == null) {
            throw new IllegalArgumentException("Player is required to play");
        }

        logger.debug("=========== Receiving Play ===========");
        logger.debug("[" + trucoPlay.getPlayer() + "] PLAYS [" + trucoPlay + "]");

        try {
            play(trucoPlay);
        } catch (InvalidPlayExcepcion invalidPlayExcepcion) {
            invalidPlayExcepcion.printStackTrace();

            throw new IllegalArgumentException("Invalida Play Exception [" + trucoGamePlay + "]", invalidPlayExcepcion);
        }
        logger.debug("=========== Play Finished ===========");
    }

    @Override
    public Vector getDetallesDeLaMano() {
        return null;
    }

    @Override
    public void play(TrucoEvent event) {
        TrucoGameEvent trucoGameEvent = TolucaHelper.trucoEvent(event);
        convertAndSend(trucoGameEvent.getEventName(), trucoGameEvent);
    }

    @Override
    public void playResponse(TrucoEvent event) {
        logger.debug("Play Response [" + trucoTableHolder.getTable().getId() + "]");
    }

    @Override
    public void turn(TrucoEvent event) {
        logger.debug("Turn Event [" + trucoTableHolder.getTable().getId() + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setRequest(TolucaHelper.trucoGameEventType(event));
        trucoGameEvent.setPlayer(new Player(event.getPlayer().getId(), event.getPlayer().getName()));
        convertAndSend(Event.PLAY_REQUEST, trucoGameEvent);
    }


    @Override
    public void endOfHand(TrucoEvent event) {
        logger.debug("End Of Hand [" + trucoTableHolder.getTable().getId() + "]");
        numberOfHand = target.getNumberOfHand();

        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameData.setHandNumber(event.getNumberOfHand());
        trucoGameData.getTeam1().setPoints(target.getTeam(TEAM_1).getPoints());
        trucoGameData.getTeam2().setPoints(target.getTeam(TEAM_2).getPoints());

        TrucoPlayer tp = target.getTeams()[(numberOfHand + 1) % 2].getTrucoPlayerNumber((numberOfHand - 1) % target.getNumberOfPlayers() / 2);

        trucoGameEvent.setPlayer(getPlayer(tp));

        //Messages
        trucoGameEvent.setMessages(new ArrayList<>());
        Vector vector = target.getDetallesDeLaMano();
        for (Object m : vector) {
            trucoGameEvent.getMessages().add(new TrucoGameMessage(((PointsDetail) m).aString()));
        }
        convertAndSend(Event.HAND_ENDED, trucoGameEvent);
    }

    @Override
    public void cardsDeal(TrucoEvent event) {
        logger.debug("cardsDeal event  [" + event + "]");
        logger.debug("send Cards to  [" + event.getPlayer() + "][" + event.getCards() + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setPlayer(getPlayer(event.getPlayer()));
        trucoGameEvent.setCards(TolucaHelper.getSpanishCards(event.getCards()));
        logger.debug("send Spanish Cards to  [" + trucoGameEvent.getPlayer() + "][" + trucoGameEvent.getCards() + "]");
        // Same Object
        Arrays.stream(event.getCards()).forEach(c -> {
            cards.put(TolucaHelper.getSpanishCard(c), c);
        });

        convertAndSend(Event.GIVING_CARDS, trucoGameEvent);
    }


    @Override
    public void handStarted(TrucoEvent event) {
        logger.debug("hand Started  [" + event + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setPlayer(new Player(event.getPlayer().getId(), event.getPlayer().getName()));
        trucoGameData.setHandNumber(event.getNumberOfHand());
        convertAndSend(Event.HAND_STARTED, trucoGameEvent);
    }

    @Override
    public void gameStarted(TrucoEvent event) {
        logger.debug("gameStarted  [" + event + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        convertAndSend(Event.GAME_STARTED, trucoGameEvent);
    }

    TrucoGameTeam getTrucoGameTeam(int index) {
        TrucoTeam trucoTeam = target.getTeam(index);
        TrucoGameTeam trucoGameTeam = new TrucoGameTeam();
        for (Object o : trucoTeam.getPlayers()) {
            TrucoPlayer player = (TrucoPlayer) o;
            trucoGameTeam.getPlayers().add(getPlayer(player));
        }
        return trucoGameTeam;
    }

    @Override
    public void endOfGame(TrucoEvent event) {
        logger.debug("endOfGame  [" + event + "]");
    }

    @Override
    public TrucoPlayer getAssociatedPlayer() {
        return null;
    }

    public void convertAndSend(String eventName, TrucoGameEvent trucoGameEvent) {
        trucoGameEvent.setEventName(eventName);
        trucoGameEvent.setRoomId(trucoTableHolder.getRoomId());
        trucoGameEvent.setTableId(Integer.toString(target.getTableNumber()));
        trucoGameEvent.setGame(trucoGameData);

        // TODO Change to Table
        logger.debug("firing TrucoGame Event  [" + trucoGameEvent + "]");
        amqpSender.convertAndSend(trucoGameEvent);


    }

    public void convertAndSendDirectMessage(String eventName, TrucoUser trucoUser, TrucoGameEvent trucoGameEvent) {
        trucoGameEvent.setEventName(eventName);
        trucoGameEvent.setTableId(Integer.toString(target.getTableNumber()));
        trucoGameEvent.setGame(trucoGameData);
        // TODO Change to Table
        logger.debug("firing Direct TrucoGame Event  [" + trucoGameEvent + "]");
        amqpSender.convertAndSendDirectMessage(trucoUser.getId(), trucoGameEvent);


    }

    public Player getPlayer(TrucoPlayer player) {
        return new Player(player.getId(), player.getName());
    }


}
