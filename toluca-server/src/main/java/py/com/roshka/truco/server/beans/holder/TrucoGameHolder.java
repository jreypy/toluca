package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.game.TrucoGameRequest;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.server.service.AMQPSender;
import py.com.roshka.truco.server.service.impl.AMQPSenderImpl;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.*;

import java.util.*;


public class TrucoGameHolder implements TrucoListener {


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

    public void startGame() {
        TrucoTeam[] teams = teams();

        TrucoTeam trucoGameTeam1 = teams[TEAM_1];
        TrucoTeam trucoGameTeam2 = teams[TEAM_2];

        if (trucoGameTeam1 == null || trucoGameTeam1.getPlayers().isEmpty()) {
            throw new IllegalArgumentException("Team 1 is empty");
        }

        if (trucoGameTeam2 == null || trucoGameTeam2.getPlayers().isEmpty()) {
            throw new IllegalArgumentException("Team 2 is empty");
        }

        if (trucoGameTeam1.getPlayers().size() != trucoGameTeam2.getPlayers().size()) {
            throw new IllegalArgumentException("Team 1 and Team 2 dont have the same quantity of players");
        }

        logger.debug("Starting Game [" + trucoTableHolder.getTable().getId() + "]");

        if (target == null) {

            target = new TrucoGameImpl(teams[0], teams[1], trucoTableHolder.getTable().getPoints());
            target.addTrucoListener(this);
            target.setTableNumber(Integer.parseInt(trucoTableHolder.getTable().getId()));
            trucoGameData = new TrucoGameData();
            trucoGameData.setId(trucoTableHolder.getTable().getId());
            trucoGameData.setTeam1(getTrucoGameTeam(TEAM_1));
            trucoGameData.setTeam2(getTrucoGameTeam(TEAM_2));
            trucoGameData.setPoints(trucoTableHolder.getTable().getPoints());

            List<TrucoUser> users = new ArrayList<>();

            for (TrucoUser user : positions) {
                if (user != null) {
                    users.add(user);
                }
            }
            trucoGameData.setPositions(users.toArray(new TrucoUser[users.size()]));
            trucoGameData.setSize(trucoGameTeam1.getPlayers().size() + trucoGameTeam2.getPlayers().size());
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


    public void startHand(TrucoUser trucoUser) {
        logger.debug("Start Hand [" + trucoUser.getId() + "][" + trucoTableHolder.getTable().getId() + "]");
        //TrucoPlayer tPlayer
        target.startHand(getPlayer(trucoUser.getId()));
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
            target.play(trucoPlay);
        } catch (InvalidPlayExcepcion invalidPlayExcepcion) {
            invalidPlayExcepcion.printStackTrace();

            throw new IllegalArgumentException("Invalida Play Exception [" + trucoGamePlay + "]", invalidPlayExcepcion);
        }
        logger.debug("=========== Play Finished ===========");
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

        ///Get cards
        trucoGameEvent.setOptions(OptionsHelper.getOptions(target, event));
        // Options


        trucoGameEvent.setPlayer(new Player(event.getPlayer().getId(), event.getPlayer().getName()));
        convertAndSend(Event.PLAY_REQUEST, trucoGameEvent);
        //Add can play
    }


    @Override
    public void endOfHand(TrucoEvent event) {
        logger.debug("End Of Hand [" + trucoTableHolder.getTable().getId() + "]");
        trucoGameData.setHandNumber(target.getNumberOfHand());

        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameData.setHandNumber(event.getNumberOfHand());
        trucoGameData.getTeam1().setPoints(target.getTeam(TEAM_1).getPoints());
        trucoGameData.getTeam2().setPoints(target.getTeam(TEAM_2).getPoints());

        TrucoPlayer tp = target.getTeams()[(trucoGameData.getHandNumber() + 1) % 2].getTrucoPlayerNumber((trucoGameData.getHandNumber() - 1) % target.getNumberOfPlayers() / 2);
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


    static class OptionsHelper {
        public OptionsHelper() {

        }

        static public List<TrucoGameRequest> getOptions(TrucoGame trucoGame, TrucoEvent trucoEvent) {
            List<TrucoGameRequest> options = new ArrayList<>();

            TrucoCard trucoCard = trucoGame.getCardNoPlayed(trucoEvent.getTrucoPlayer());

            for (TrucoGameRequestPair pair : getTrucoPlay(trucoEvent, trucoCard, trucoGame.getValorEnvido(trucoEvent.getTrucoPlayer()))) {
                if (trucoGame.esPosibleJugar(pair.getTrucoPlay())) {
                    options.add(pair.getTrucoGameRequest());
                }
            }
            return options;
        }


        static List<TrucoGameRequestPair> getTrucoPlay(TrucoEvent trucoEvent, TrucoCard trucoCard, int envido) {
            List<TrucoGameRequestPair> playList = new ArrayList<>();
            // envido
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.ENVIDO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_ENVIDO, "Envido")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.REAL_ENVIDO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_REAL_ENVIDO, "Real Envido")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.FALTA_ENVIDO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_FALTA_ENVIDO, "Falta Envido")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.TRUCO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_TRUCO, "Truco")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.RETRUCO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_RETRUCO, "Quiero Retruco")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.VALE_CUATRO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_VALECUATRO, "Quiero ValeCuatro")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.QUIERO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_QUIERO, "Quiero")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.NO_QUIERO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_NO_QUIERO, "No Quiero")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.ME_VOY_AL_MAZO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.PLAY_ME_VOY_AL_MAZO, "Me voy al Mazo")));
            }
            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.JUGAR_CARTA);
                trucoPlay.setCard(trucoCard);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.PLAY_CARD, null)));
            }

            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.PASO_ENVIDO);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_PASO_ENVIDO, "Paso")));
            }

            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.PASO_FLOR);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_PASO_FLOR, "Paso")));
            }

            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.CERRARSE);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.CLOSE_CARDS, "Me Cierro")));
            }

            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.CANTO_ENVIDO);
                trucoPlay.setValue(envido);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_ENVIDO_VALUE, Integer.toString(envido), envido)));
            }

            {
                TrucoPlay trucoPlay = getTrucoPlay(trucoEvent.getTrucoPlayer());
                trucoPlay.setType(TrucoPlay.FLOR);
                playList.add(new TrucoGameRequestPair(trucoPlay, getTrucoGameRequest(TrucoGamePlay.SAY_FLOR, "Flor")));
            }

            return playList;
        }


        static TrucoPlay getTrucoPlay(TrucoPlayer trucoPlayer) {
            TrucoPlay trucoPlay = new TrucoPlay();
            trucoPlay.setPlayer(trucoPlayer);
            return trucoPlay;
        }

        static TrucoGameRequest getTrucoGameRequest(String type, String text) {
            TrucoGameRequest trucoGameRequest = new TrucoGameRequest();
            trucoGameRequest.setType(type);
            trucoGameRequest.setText(text);
            trucoGameRequest.setEnvido(null);
            return trucoGameRequest;
        }

        static TrucoGameRequest getTrucoGameRequest(String type, String text, Integer envido) {
            TrucoGameRequest trucoGameRequest = getTrucoGameRequest(type, text);
            trucoGameRequest.setEnvido(envido);
            return trucoGameRequest;
        }


    }

    static class TrucoGameRequestPair {
        TrucoPlay trucoPlay;
        TrucoGameRequest trucoGameRequest;

        public TrucoGameRequestPair(TrucoPlay trucoPlay, TrucoGameRequest trucoGameRequest) {
            this.trucoPlay = trucoPlay;
            this.trucoGameRequest = trucoGameRequest;
        }

        public TrucoPlay getTrucoPlay() {
            return trucoPlay;
        }

        public void setTrucoPlay(TrucoPlay trucoPlay) {
            this.trucoPlay = trucoPlay;
        }

        public TrucoGameRequest getTrucoGameRequest() {
            return trucoGameRequest;
        }

        public void setTrucoGameRequest(TrucoGameRequest trucoGameRequest) {
            this.trucoGameRequest = trucoGameRequest;
        }
    }
}
