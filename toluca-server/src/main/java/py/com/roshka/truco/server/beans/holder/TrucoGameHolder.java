package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.*;

import java.util.*;

public class TrucoGameHolder extends TrucoGame implements TrucoListener {
    static String TRUCO_GAME_EVENT = "truco_game_event";

    static final int TEAM_1 = 0;
    static final int TEAM_2 = 1;

    Logger logger = LoggerFactory.getLogger(TrucoGameHolder.class);
    int MAX = 6;
    private Map<String, TrucoPlayer> players = new LinkedHashMap<>();
    private TrucoUser[] positions = new TrucoUser[MAX];
    private TrucoTableHolder trucoTableHolder;
    TrucoGame target = null;
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;
    private TrucoGameData trucoGameData = null;

    public TrucoGameHolder(TrucoTableHolder trucoTableHolder, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.trucoTableHolder = trucoTableHolder;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
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
        logger.debug("PLay!! [" + tp + "]");

    }

    public void play(TrucoGamePlay trucoGamePlay) {
        TrucoPlay trucoPlay = TolucaHelper.getPlay(trucoGamePlay);
        trucoPlay.setPlayer(getPlayer(trucoGamePlay.getPlayer().getId()));

        if (trucoPlay.getPlayer() == null){
            throw new IllegalArgumentException("Player is required to play");
        }

        logger.debug(trucoPlay.getPlayer() + "plays " + trucoPlay);
        target.play(trucoPlay);
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
        convertAndSend(Event.PLAY_REQUEST, trucoGameEvent);
    }


    @Override
    public void endOfHand(TrucoEvent event) {
        logger.debug("End Of Hand [" + trucoTableHolder.getTable().getId() + "]");
    }

    @Override
    public void cardsDeal(TrucoEvent event) {
        logger.debug("Cars  [" + event + "]");
        logger.debug("hand Started  [" + event + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setPlayer(new Player(event.getPlayer().getId(), event.getPlayer().getName()));
        trucoGameEvent.setCards(TolucaHelper.getSpanishCards(event.getCards()));
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
            trucoGameTeam.getPlayers().add(new Player(player.getId(), player.getName()));
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
        String routing = Integer.toString(target.getTableNumber());

        trucoGameEvent.setEventName(eventName);
        trucoGameEvent.setTableId(Integer.toString(target.getTableNumber()));
        trucoGameEvent.setGame(trucoGameData);

        // TODO Change to Table
        logger.debug("firing TrucoGame Event  [" + trucoGameEvent + "]");
        rabbitTemplate.convertAndSend(TRUCO_GAME_EVENT, trucoTableHolder.getTable().getRoomId(), new RabbitResponse(Event.TRUCO_GAME_EVENT,
                trucoGameEvent.getClass().getCanonicalName(),
                objectMapper.convertValue(trucoGameEvent, HashMap.class)));


    }
}