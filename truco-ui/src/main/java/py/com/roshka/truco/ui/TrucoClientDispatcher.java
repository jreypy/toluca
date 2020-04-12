package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.constants.Commands;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoGameClient;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;
import py.edu.uca.fcyt.toluca.net.EventDispatcher;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.util.LinkedHashMap;
import java.util.Map;

import static py.edu.uca.fcyt.toluca.event.TableEvent.EVENT_playerSit;

public class TrucoClientDispatcher {

    Logger logger = Logger.getLogger(TrucoClientDispatcher.class);

    private ObjectMapper objectMapper;
    private EventDispatcher eventDispatcher;
    private TrucoListener trucoListener;

    public TrucoClientDispatcher(ObjectMapper objectMapper, EventDispatcher eventDispatcher, RoomClient client, TrucoListener trucoListener) {
        this.objectMapper = objectMapper;
        this.eventDispatcher = eventDispatcher;
        eventDispatcher.setRoom(client);
        this.trucoListener = trucoListener;
    }

    public void dispatchEvent(Map event) {
        String type = (String) event.get("type");
        logger.debug("Dispatching event [" + type + "]");
        if (Event.TRUCO_GAME_EVENT.equalsIgnoreCase(type)) {
            dispatchTrucoGameEvent((Map) event.get("data"));
        }
        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
            dispatchCommandResponse((String) event.get("command"), (String) event.get("id"), (Map) event.get("data"));
        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(type)) {
            dispatchRoomEvent((Map) event.get("data"));
        } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(type)) {
            dispatchRoomEvent((Map) event.get("data"));
        } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(type)) {
            dispatchRoomCreated((Map) event.get("data"));
        } else if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(type)) {
            dispatchRoomTableEvent(EVENT_playerSit, (Map) event.get("data"));
        } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(type)) {
            TrucoRoomTableEvent trucoRoomTableEvent = objectMapper.convertValue((Map) event.get("data"), TrucoRoomTableEvent.class);
            dispatchRoomEvent(RoomEvent.TYPE_TABLE_JOINED, trucoRoomTableEvent);
        }


    }

    private void dispatchTrucoGameEvent(Map data) {
        TrucoGameEvent trucoGameEvent = objectMapper.convertValue(data, TrucoGameEvent.class);
        logger.info("*** TODO **** [" + data + "]");
        if (Event.GAME_STARTED.equals(trucoGameEvent.getEventName())) {
            handleGameStarted(trucoGameEvent);
        } else if (Event.HAND_STARTED.equals(trucoGameEvent.getEventName())) {
            handleHandStarted(trucoGameEvent);
        } else if (Event.GIVING_CARDS.equals(trucoGameEvent.getEventName())) {
            handleGivingCards(trucoGameEvent);
        } else if (Event.PLAY_REQUEST.equals(trucoGameEvent.getEventName())) {

        }
    }

    private void handleGivingCards(TrucoGameEvent trucoGameEvent) {

        TrucoEvent trucoEvent = new TrucoEvent();
        trucoEvent.setType(TrucoEvent.ENVIAR_CARTAS);
        trucoEvent.setTableNumber(Integer.parseInt(trucoGameEvent.getGame().getId()));
        trucoEvent.setPlayer(TrucoConverter.getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setCards(TrucoConverter.getCards(trucoGameEvent.getCards()));
        eventDispatcher.receiveCards(trucoEvent);

    }


    private void handleGameStarted(TrucoGameEvent trucoGameEvent) {
        TableEvent tableEvent = new TableEvent();
        /*logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,
                "Llego un gameStarted de TableEvent ");*/
        TableServer tableServer = new TableServer();
        // TODO CHECK

        Table table = eventDispatcher.getRoom().getTable(Integer.parseInt(trucoGameEvent.getTableId()));
        TrucoTeam[] trucoTeams = table.createTeams();

//        TrucoTeam[] trucoTeam = table.createTeams();
        TrucoGameClient trucoGameClient = new TrucoGameClient(trucoTeams[0],
                trucoTeams[1],
                trucoGameEvent.getGame().getPoints());

        trucoGameClient.setTableNumber(tableServer.getTableNumber());
        trucoGameClient.addTrucoListener(trucoListener);

        //es muy importante el orden de los faroles altera el valor del
        // alumbrado
        //esto me hizo perder 2 horas
        table.startGame(trucoGameClient);//atender esta linea primero que la
        trucoGameClient.startGameClient();


    }
    private void handleHandStarted(TrucoGameEvent trucoGameEvent) {

    }


    private void dispatchRoomCreated(Map eventData) {
        TrucoRoomTable trucoRoomTable = objectMapper.convertValue(eventData, TrucoRoomTable.class);
        RoomEvent table = new RoomEvent();
        table.setTableServer(new TableServer());
        table.getTableServer().setHost(new TrucoPlayer());
        table.getTableServer().getHost().setId(trucoRoomTable.getOwner().getId());
        table.getTableServer().getHost().setName(trucoRoomTable.getOwner().getUsername());
        table.setType(RoomEvent.TYPE_TABLE_CREATED);
        table.setGamePoints(trucoRoomTable.getPoints());
        table.setPlayers(new LinkedHashMap());
        table.getTableServer().setTableNumber(Integer.parseInt(trucoRoomTable.getId()));
        table.setTableNumber(table.getTableServer().getTableNumber());
        eventDispatcher.dispatchEvent(table);
    }

    private void dispatchCommandResponse(String command, String id, Map data) {
        if (Commands.GET_ROOM.equalsIgnoreCase(command)) {
            TrucoRoom trucoRoom = objectMapper.convertValue(data, TrucoRoom.class);
            if (trucoRoom.getUsers() != null) {
                trucoRoom.getUsers().stream().parallel().forEach(user -> {
                    trucoUserJoined(user.getUser());
                });
            }
        }
    }

    void dispatchRoomEvent(Map eventData) {
        logger.debug("Dispatching event [" + eventData + "]");
        TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(eventData, TrucoRoomEvent.class);
        dispatchRoomEvent(trucoRoomEvent);
    }

    void dispatchRoomEvent(TrucoRoomEvent trucoRoomEvent) {
        if (TrucoFrame.MAIN_ROOM_ID.equalsIgnoreCase(trucoRoomEvent.getRoom().getId())) {
            if (Event.ROOM_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                trucoUserJoined(trucoRoomEvent.getUser());
            } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                trucoUserLeft(trucoRoomEvent.getUser());
            }

        }
    }

    void trucoUserJoined(TrucoUser trucoUser) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(trucoUser.getUsername());
        roomEvent.getPlayer().setId(trucoUser.getId());
        eventDispatcher.dispatchEvent(roomEvent);
    }

    void trucoUserLeft(TrucoUser trucoUser) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_LEFT);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(trucoUser.getUsername());
        roomEvent.getPlayer().setId(trucoUser.getId());
        eventDispatcher.dispatchEvent(roomEvent);
    }


    public void dispatchRoomTableEvent(Integer eventType, Map eventData) {
        TrucoRoomTableEvent trucoRoomTableEvent = objectMapper.convertValue(eventData, TrucoRoomTableEvent.class);
        TableEvent tableEvent = new TableEvent();
        tableEvent.setEvent(eventType);
        tableEvent.setTableServer(new TableServer());
        tableEvent.getTableServer().setTableNumber(Integer.parseInt(trucoRoomTableEvent.getTableId()));
        TrucoPlayer trucoPlayer = new TrucoPlayer();
        trucoPlayer.setId(trucoRoomTableEvent.getUser().getId());
        trucoPlayer.setName(trucoRoomTableEvent.getUser().getUsername());
        tableEvent.setPlayer(new TrucoPlayer[]{trucoPlayer});
        tableEvent.setValue(trucoRoomTableEvent.getChair());
        eventDispatcher.dispatchEvent(tableEvent);

    }

    public void dispatchRoomEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(eventType);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(eventData.getUser().getUsername());
        roomEvent.getPlayer().setId(eventData.getUser().getId());

        roomEvent.setTableServer(new TableServer());
        roomEvent.getTableServer().setTableNumber(Integer.parseInt(eventData.getTableId()));


        eventDispatcher.dispatchEvent(roomEvent);

    }
}
