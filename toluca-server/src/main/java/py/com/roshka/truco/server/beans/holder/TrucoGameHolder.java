package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.*;
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
            trucoGameData  = new TrucoGameData();
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

    }

    @Override
    public Vector getDetallesDeLaMano() {
        return null;
    }

    @Override
    public void play(TrucoEvent event) {

    }

    @Override
    public void playResponse(TrucoEvent event) {
        logger.debug("Play Response [" + trucoTableHolder.getTable().getId() + "]");
    }

    @Override
    public void turn(TrucoEvent event) {
        logger.debug("Turn Event [" + trucoTableHolder.getTable().getId() + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setRequest(getRequestType(event));
        convertAndSend(Event.PLAY_REQUEST, trucoGameEvent);
    }

    String getRequestType(TrucoEvent event) {
        Integer type = (int) event.getType();
        if (type == TrucoEvent.TURNO_JUGAR_CARTA) {
            return Event.PLAY_REQUEST_CARD;
        } else if (type == TrucoEvent.TURNO_CANTAR_ENVIDO) {
            return Event.PLAY_REQUEST_ENVIDO_VALUE;
        } else if (type == TrucoEvent.TURNO_CANTAR_FLOR) {
            return Event.PLAY_REQUEST_FLOR_VALUE;
        } else if (type == TrucoEvent.TURNO_RESPONDER_TRUCO) {
            return Event.PLAY_REQUEST_TRUCO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_RETRUCO) {
            return Event.PLAY_REQUEST_RETRUCO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_VALECUATRO) {
            return Event.PLAY_REQUEST_VALE_CUATRO;
        } else if (type == TrucoEvent.TURNO_CANTAR_ENVIDO) {
            return Event.PLAY_REQUEST_ENVIDO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_FALTAENVIDO) {
            return Event.PLAY_REQUEST_FALTA_ENVIDO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_REALENVIDO) {
            return Event.PLAY_REQUEST_REAL_ENVIDO;
        } else if (type == TrucoEvent.TURNO_CANTAR_FLOR) {
            return Event.PLAY_REQUEST_FLOR;
        } else if (type == TrucoEvent.TURNO_RESPONDER_CONTRAFLOR) {
            return Event.PLAY_REQUEST_FLOR;
        } else if (type == TrucoEvent.TURNO_RESPONDER_CONTRAFLORALRESTO) {
            return Event.PLAY_REQUEST_CONTRAFLOR;
        }
        throw new IllegalArgumentException("Play Type is not valid [" + type + "]");
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
        trucoGameEvent.setCards(getSpanishCards(event.getCards()));
        convertAndSend(Event.GIVING_CARDS, trucoGameEvent);
    }

    List<SpanishCard> getSpanishCards(TrucoCard[] trucoCards) {
        List spanishCards = new ArrayList();
        for (TrucoCard trucoCard : trucoCards) {
            if (trucoCard != null) {
                SpanishCard spanishCard = getSpanishCard(trucoCard);
                spanishCards.add(spanishCard);
            }
        }
        return spanishCards;

    }

    private SpanishCard getSpanishCard(TrucoCard trucoCard) {
        SpanishCard spanishCard = new SpanishCard();
        spanishCard.setType(getType(trucoCard));
        spanishCard.setValue(getValue(trucoCard));
        return spanishCard;
    }

    String getType(TrucoCard trucoCard) {
        byte type = trucoCard.getKind();
        if (TrucoCard.ORO == type)
            return SpanishCard.ORO;
        if (TrucoCard.COPA == type)
            return SpanishCard.COPA;
        if (TrucoCard.ESPADA == type)
            return SpanishCard.ESPADA;
        if (TrucoCard.BASTO == type)
            return SpanishCard.ESPADA;

        return null;
    }

    Integer getValue(TrucoCard trucoCard) {
        return (int) trucoCard.getValue();
    }


    @Override
    public void handStarted(TrucoEvent event) {
        logger.debug("hand Started  [" + event + "]");
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setPlayer(new Player(event.getPlayer().getId(), event.getPlayer().getName()));
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
        rabbitTemplate.convertAndSend(TRUCO_GAME_EVENT, trucoTableHolder.getTable().getRoomId(), new RabbitResponse(Event.TRUCO_GAME_EVENT, trucoGameEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoGameEvent, HashMap.class)));
    }
}
