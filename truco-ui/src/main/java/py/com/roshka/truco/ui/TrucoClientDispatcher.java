package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.edu.uca.fcyt.net.CommunicatorClient;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.util.LinkedHashMap;
import java.util.Map;

import static py.edu.uca.fcyt.toluca.event.TableEvent.EVENT_playerSit;

public class TrucoClientDispatcher extends CommunicatorClient {

    Logger logger = Logger.getLogger(TrucoClientDispatcher.class);

    private ObjectMapper objectMapper;
    private EventDispatcherClient target;
    private TrucoListener trucoListener;
    private TrucoUser trucoUser;
    WebSocketCommunicatorClient communicatorClient;

    public TrucoClientDispatcher(WebSocketCommunicatorClient webSocketCommunicatorClient, ObjectMapper objectMapper, EventDispatcherClient eventDispatcher, RoomClient client, TrucoListener trucoListener) {
        this.communicatorClient = webSocketCommunicatorClient;
        this.objectMapper = objectMapper;
        this.target = eventDispatcher;
        this.target.setRoom(client);
        this.trucoListener = trucoListener;
    }

    public void dispatchEvent(Map event) {
        String type = (String) event.get("type");
        logger.debug("Dispatching event [" + type + "]");

        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)){
            dispatchCommandResponse((String) event.get("command"), (String) event.get("id"), (Map) event.get("data"));
        }
        else if (Event.TRUCO_ROOM_EVENT.equalsIgnoreCase(type)){
            TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(event.get("data"), TrucoRoomEvent.class);
            dispatchRoomEvent(trucoRoomEvent);
        }


//        if (Event.TRUCO_GAME_EVENT.equalsIgnoreCase(type)) {
//            TrucoGameEvent trucoGameEvent = objectMapper.convertValue((Map) event.get("data"), TrucoGameEvent.class);
//            dispatchTrucoGameEvent(trucoGameEvent);
//        } else if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
//            dispatchCommandResponse((String) event.get("command"), (String) event.get("id"), (Map) event.get("data"));
//        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(type)) {
//            dispatchRoomEvent((Map) event.get("data"));
//        } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(type)) {
//            dispatchRoomEvent((Map) event.get("data"));
//        } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(type)) {
//            dispatchRoomTableCreated((Map) event.get("data"));
//        } else if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(type)) {
//            dispatchRoomTableEvent(EVENT_playerSit, (Map) event.get("data"));
//        } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(type)) {
//            TrucoRoomTableEvent trucoRoomTableEvent = objectMapper.convertValue((Map) event.get("data"), TrucoRoomTableEvent.class);
//            dispatchRoomEvent(RoomEvent.TYPE_TABLE_JOINED, trucoRoomTableEvent);
//        }

    }


    private void handleGivingCards(TrucoGameEvent trucoGameEvent) {

        TrucoEvent trucoEvent = new TrucoEvent();
        trucoEvent.setType(TrucoEvent.ENVIAR_CARTAS);
        trucoEvent.setPlayer(TolucaHelper.getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setTableNumber(Integer.parseInt(trucoGameEvent.getGame().getId()));
        trucoEvent.setPlayer(TolucaHelper.getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setCards(TolucaHelper.getCards(trucoGameEvent.getCards()));
        target.receiveCards(trucoEvent);

    }


    private void dispatchTrucoGameEvent(TrucoGameEvent trucoGameEvent) {
        TrucoEvent trucoEvent = TolucaHelper.trucoEvent(trucoGameEvent);

        if (Event.GAME_STARTED.equalsIgnoreCase(trucoGameEvent.getEventName())) {
            TableEvent tableEvent = new TableEvent();
            tableEvent.setEvent(TableEvent.EVENT_gameStarted);
            tableEvent.setTableServer(new TableServer());
            tableEvent.getTableServer().setTableNumber(trucoEvent.getTableNumber());
            target.dispatchEvent(tableEvent);
        }
        else {
            logger.debug("Fire Truco Game Event [ " + trucoGameEvent + "]");

            target.dispatchEvent(trucoEvent);
        }

    }




    private void dispatchCommandResponse(String command, String id, Map data) {
        if (Commands.JOIN_ROOM.equalsIgnoreCase(command)) {
            logger.debug("Login Completed!! ");
            loginCompleted(this.communicatorClient.getTrucoClient().getTrucoPrincipal());
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
                if (trucoRoomEvent.getUser().getUsername().equals(communicatorClient.getTrucoClient().getTrucoPrincipal().getUsername())) {
                    trucoRoomEvent.getRoom().getUsers().stream().forEach(s -> {
                        RoomEvent roomEvent = new RoomEvent();
                        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
                        roomEvent.setPlayer(TolucaHelper.getPlayer(s.getUser()));
                        target.dispatchEvent(roomEvent);
                    });
                } else {
                    RoomEvent roomEvent = new RoomEvent();
                    roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
                    roomEvent.setPlayer(TolucaHelper.getPlayer(trucoRoomEvent.getUser()));
                    target.dispatchEvent(roomEvent);
                }

            }
            else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(trucoRoomEvent.getEventName())){
                RoomEvent table = new RoomEvent();
                table.setTableServer(new TableServer());
                table.getTableServer().setHost(new TrucoPlayer());
                table.getTableServer().getHost().setId(trucoRoomEvent.getUser().getId());
                table.getTableServer().getHost().setName(trucoRoomEvent.getUser().getUsername());
                table.setType(RoomEvent.TYPE_TABLE_CREATED);
                table.setGamePoints(trucoRoomEvent.getTable().getPoints());
                table.setPlayers(new LinkedHashMap());
                table.getTableServer().setTableNumber(Integer.parseInt(trucoRoomEvent.getTable().getId()));
                table.setTableNumber(table.getTableServer().getTableNumber());
                target.dispatchEvent(table);
            }
            else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                trucoUserLeft(trucoRoomEvent.getUser());
            }

        }
    }


    void trucoUserLeft(TrucoUser trucoUser) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_LEFT);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(trucoUser.getUsername());
        roomEvent.getPlayer().setId(trucoUser.getId());
        target.dispatchEvent(roomEvent);
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
        target.dispatchEvent(tableEvent);

    }

    public void dispatchRoomEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(eventType);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(eventData.getUser().getUsername());
        roomEvent.getPlayer().setId(eventData.getUser().getId());

        roomEvent.setTableServer(new TableServer());
        roomEvent.getTableServer().setTableNumber(Integer.parseInt(eventData.getTableId()));


        target.dispatchEvent(roomEvent);

    }


    @Override
    public void setLoggedIn(boolean loggedIn) {

    }

    @Override
    public void connectionFailed(String mensaje) {

    }

    @Override
    public void rankingChanged(RoomEvent ev) {

    }

    @Override
    public void invitationRequest(RoomEvent event) {

    }

    @Override
    public void invitationRejected(RoomEvent re) {

    }

    @Override
    public void tableDestroyed(TableEvent event) {

    }

    @Override
    public void chatMessageSent(RoomEvent event) {

    }


    public void loginCompleted(TrucoPrincipal trucoPrincipal) {
        //        RoomEvent roomEvent = new RoomEvent();
//        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
//        roomEvent.setTablesServers(new TableServer[0]);
//        roomEvent.setPlayer(TrucoConverter.getPlayer(new Player(trucoUser.getId(), trucoUser.getUsername())));
//        roomEvent.getPlayer().setName(trucoUser.getUsername());
//        roomEvent.getPlayer().setId(trucoUser.getId());
//        eventDispatcher.dispatchEvent(roomEvent);

        // Players...
//        roomEvent.setPlayers(new LinkedHashMap());
//        roomEvent.getPlayers().put(trucoUser.getUsername(), TrucoConverter.getPlayer(new Player(trucoUser.getUsername(), trucoUser.getUsername())));
        this.trucoUser = new TrucoUser(trucoPrincipal.getUsername(), trucoPrincipal.getUsername());
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
        //TODO Tables
        roomEvent.setTablesServers(new TableServer[0]);
        roomEvent.setPlayer(TolucaHelper.getPlayer(new Player(trucoUser.getId(), trucoUser.getUsername())));
        roomEvent.setPlayers(new LinkedHashMap());
        roomEvent.getPlayers().put(roomEvent.getPlayer().getId(), roomEvent.getPlayer());
        target.loginCompleted(roomEvent);

    }

    public void setTrucoUser(TrucoUser trucoUser) {
        this.trucoUser = trucoUser;
    }
}
