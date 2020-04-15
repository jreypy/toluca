package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.api.request.RoomRequest;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
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

    final TrucoRoomEventDispatcher roomEventDispatcher;
    final TrucoRoomTableEventDispatcher tableEventDispatcher;
    final TrucoGameEventDispatcher gameEventDispatcher;

    public TrucoClientDispatcher(WebSocketCommunicatorClient webSocketCommunicatorClient, ObjectMapper objectMapper, EventDispatcherClient eventDispatcher, RoomClient client, TrucoListener trucoListener) {
        this.communicatorClient = webSocketCommunicatorClient;
        this.objectMapper = objectMapper;
        this.target = eventDispatcher;
        this.target.setRoom(client);
        this.trucoListener = trucoListener;

        gameEventDispatcher = new TrucoGameEventDispatcher(webSocketCommunicatorClient, target);
        tableEventDispatcher = new TrucoRoomTableEventDispatcher(webSocketCommunicatorClient, target);
        roomEventDispatcher = new TrucoRoomEventDispatcher(webSocketCommunicatorClient, tableEventDispatcher, target);

    }

    public void dispatchEvent(Map event) {
        String type = (String) event.get("type");
        logger.debug("Dispatching event [" + type + "]");

        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
            dispatchCommandResponse((String) event.get("command"), (String) event.get("id"), (Map) event.get("data"));
        } else if (Event.TRUCO_ROOM_EVENT.equalsIgnoreCase(type)) {
            TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(event.get("data"), TrucoRoomEvent.class);
            roomEventDispatcher.dispatchRoomEvent(trucoRoomEvent);
        } else if (Event.TRUCO_TABLE_EVENT.equalsIgnoreCase(type)) {
            TrucoRoomTableEvent trucoRoomTableEvent = objectMapper.convertValue(event.get("data"), TrucoRoomTableEvent.class);
            tableEventDispatcher.dispatchRoomTableEvent(trucoRoomTableEvent);
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


    private void dispatchCommandResponse(String command, String id, Map data) {
        if (Commands.JOIN_ROOM.equalsIgnoreCase(command)) {
            logger.debug("Login Completed!! ");
            loginCompleted(this.communicatorClient.getTrucoClient().getTrucoPrincipal());
            try {
                communicatorClient.executeCommand(Commands.GET_ROOM, new RoomRequest(TrucoFrame.MAIN_ROOM_ID));
            } catch (TrucoClientException e) {
                logger.error("Error executing command", e);
            }
        } else if (Commands.GET_ROOM.equalsIgnoreCase(command)) {
            TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(data, TrucoRoomEvent.class);
            roomEventDispatcher.dispatchRoomEvent(trucoRoomEvent);
        }
    }


    public void loginCompleted(TrucoPrincipal trucoPrincipal) {
        this.trucoUser = new TrucoUser(trucoPrincipal.getUsername(), trucoPrincipal.getUsername());
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
        // Tables are retrieved after Get Room
        roomEvent.setTablesServers(new TableServer[0]);
        roomEvent.setPlayer(TolucaHelper.getPlayer(new Player(trucoUser.getId(), trucoUser.getUsername())));
        roomEvent.setPlayers(new LinkedHashMap());
        roomEvent.getPlayers().put(roomEvent.getPlayer().getId(), roomEvent.getPlayer());
        target.loginCompleted(roomEvent);
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

    public void setTrucoUser(TrucoUser trucoUser) {
        this.trucoUser = trucoUser;
    }
}
