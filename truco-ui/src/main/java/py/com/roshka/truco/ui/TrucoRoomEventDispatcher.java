package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.ui.room.TableGame2;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.util.LinkedHashMap;

public class TrucoRoomEventDispatcher {
    Logger logger = Logger.getLogger(TrucoRoomEventDispatcher.class);


    final private WebSocketCommunicatorClient communicatorClient;
    final private TrucoRoomTableEventDispatcher roomTableEventDispatcher;
    final private EventDispatcherClient target;


    final ObjectMapper objectMapper;


    public TrucoRoomEventDispatcher(WebSocketCommunicatorClient communicatorClient, TrucoRoomTableEventDispatcher roomTableEventDispatcher, EventDispatcherClient target) {
        this.communicatorClient = communicatorClient;
        this.roomTableEventDispatcher = roomTableEventDispatcher;
        this.target = target;
        this.objectMapper = communicatorClient.getObjectMapper();
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
            } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                userTableJoined(trucoRoomEvent);
            } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                tableCreated(trucoRoomEvent);
            } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                trucoUserLeft(trucoRoomEvent.getUser());
            } else if (Event.ROOM_FOUND.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                updateRoom(trucoRoomEvent);
            }
        }
    }

//    public void dispatchRoomTableEvent(Integer eventType, Map eventData) {
//        TrucoRoomTableEvent trucoRoomTableEvent = objectMapper.convertValue(eventData, TrucoRoomTableEvent.class);
//        TableEvent tableEvent = new TableEvent();
//        tableEvent.setEvent(eventType);
//        tableEvent.setTableServer(new TableServer());
//        tableEvent.getTableServer().setTableNumber(Integer.parseInt(trucoRoomTableEvent.getTableId()));
//        TrucoPlayer trucoPlayer = new TrucoPlayer();
//        trucoPlayer.setId(trucoRoomTableEvent.getUser().getId());
//        trucoPlayer.setName(trucoRoomTableEvent.getUser().getUsername());
//        tableEvent.setPlayer(new TrucoPlayer[]{trucoPlayer});
//        tableEvent.setValue(trucoRoomTableEvent.getPosition());
//        target.dispatchEvent(tableEvent);
//
//    }


    private void updateRoom(TrucoRoomEvent trucoRoomEvent) {
        final String roomId = trucoRoomEvent.getRoom().getId();
        for (TrucoRoomTableDescriptor table : trucoRoomEvent.getRoom().getTables()) {
            final String tableId = table.getId();
            createTable(table);
            for (TrucoUser user : table.getUsers()) {
                userTableJoined(roomId, tableId, user);
            }
            for (int i = 0; i < table.getPositions().length; i++) {
                TrucoUser user = table.getPositions()[i];
                if (user != null) {
                    roomTableEventDispatcher.tablePositionSetted(roomId, tableId, user, i);
                }
            }
        }
    }

    private void createTable(TrucoRoomTableDescriptor table) {
        TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
        trucoRoomEvent.setTable(table);
        trucoRoomEvent.setUser(table.getOwner());
        trucoRoomEvent.setTable(table);
        tableCreated(trucoRoomEvent);

    }


    private void tableCreated(TrucoRoomEvent trucoRoomEvent) {
        RoomEvent table = new RoomEvent();
        table.setTableServer(new TableServer());

        //Owner
        if (trucoRoomEvent.getUser() != null) {
            table.getTableServer().setHost(TolucaHelper.getPlayer(trucoRoomEvent.getUser()));
        }
        table.setType(RoomEvent.TYPE_TABLE_CREATED);
        //Players
        table.setPlayers(new LinkedHashMap());
        //Table
        table.getTableServer().setTableNumber(Integer.parseInt(trucoRoomEvent.getTable().getId()));
        table.setTableNumber(table.getTableServer().getTableNumber());
        table.setGamePoints(trucoRoomEvent.getTable().getPoints());

        if (trucoRoomEvent.getTable().getOwner().getUsername().equals(TrucoGameClient2.principal.getUsername())) {
            TrucoGameClient2.getTrucoGameClient2(trucoRoomEvent, true);
        }

        //target.dispatchEvent(table);
    }

    private void userTableJoined(TrucoRoomEvent trucoRoomEvent) {
        if (trucoRoomEvent.getUser().getUsername().equals(TrucoGameClient2.principal.getUsername())) {
            logger.warn("*** Ingresar a Tabla ****");
            TrucoGameClient2.getTrucoGameClient2(trucoRoomEvent, false);
        }
        userTableJoined(trucoRoomEvent.getRoom().getId(), trucoRoomEvent.getTable().getId(), trucoRoomEvent.getUser());
    }

    private void userTableJoined(String roomId, String tableId, TrucoUser user) {
        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
        trucoRoomTableEvent.setEventName(Event.ROOM_TABLE_USER_JOINED);
        trucoRoomTableEvent.setRoomId(roomId);
        trucoRoomTableEvent.setTableId(tableId);
        trucoRoomTableEvent.setUser(user);

        roomTableEventDispatcher.dispatchRoomTableEvent(trucoRoomTableEvent);

    }


    void trucoUserLeft(TrucoUser trucoUser) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_PLAYER_LEFT);
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(trucoUser.getUsername());
        roomEvent.getPlayer().setId(trucoUser.getId());
        target.dispatchEvent(roomEvent);
    }
}
