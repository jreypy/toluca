package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.ui.event.RoomEvent2;
import py.com.roshka.truco.ui.net.EventDispatcherClient2;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;

public class TrucoRoomEventDispatcher {
    Logger logger = Logger.getLogger(TrucoRoomEventDispatcher.class);


    final private WebSocketCommunicatorClient communicatorClient;
    final private TrucoRoomTableEventDispatcher roomTableEventDispatcher;
    final private EventDispatcherClient2 target;


    final ObjectMapper objectMapper;


    public TrucoRoomEventDispatcher(WebSocketCommunicatorClient communicatorClient, TrucoRoomTableEventDispatcher roomTableEventDispatcher, EventDispatcherClient target) {
        this.communicatorClient = communicatorClient;
        this.roomTableEventDispatcher = roomTableEventDispatcher;
        this.target = (EventDispatcherClient2) target;
        this.objectMapper = communicatorClient.getObjectMapper();
    }

    void dispatchRoomEvent(TrucoRoomEvent trucoRoomEvent) {

        if (Event.ROOM_FOUND.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
            updateRoom(trucoRoomEvent);
        } else {
            RoomEvent2 roomEvent2 = new RoomEvent2(trucoRoomEvent);

            if (Event.ROOM_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                target.playerJoined(roomEvent2);
            } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                target.tableCreated(roomEvent2);
            }
            else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())){
                target.tableJoined(roomEvent2);
            }
            else {
//            target.dispatchEvent(new RoomEvent2(trucoRoomEvent));
                logger.info("Event [" + trucoRoomEvent.getEventName() + "] not implemented");
            }
        }


//        if (TrucoFrame.MAIN_ROOM_ID.equalsIgnoreCase(trucoRoomEvent.getRoom().getId())) {
//
//            if (Event.ROOM_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
//                if (trucoRoomEvent.getUser().getUsername().equals(communicatorClient.getTrucoClient().getTrucoPrincipal().getUsername())) {
//                    trucoRoomEvent.getRoom().getUsers().stream().forEach(s -> {
//                        RoomEvent roomEvent = new RoomEvent();
//                        roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
//                        roomEvent.setPlayer(TolucaHelper.getPlayer(s.getUser()));
//                        target.dispatchEvent(roomEvent);
//                    });
//                } else {
//                    RoomEvent roomEvent = new RoomEvent();
//                    roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
//                    roomEvent.setPlayer(TolucaHelper.getPlayer(trucoRoomEvent.getUser()));
//                    target.dispatchEvent(roomEvent);
//                }
//            } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
//
//            } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
//                tableCreated(trucoRoomEvent);
//            } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
//                trucoUserLeft(trucoRoomEvent.getUser());
//            } else if (Event.ROOM_FOUND.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
//                updateRoom(trucoRoomEvent);
//            }
//        }
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
        for (TrucoRoomUser trucoRoomUser : trucoRoomEvent.getRoom().getUsers()) {
            TrucoRoomEvent trucoRoomEvent2 = new TrucoRoomEvent();
            trucoRoomEvent2.setRoom(trucoRoomEvent.getRoom());
            trucoRoomEvent2.setUser(trucoRoomUser.getUser());
            target.playerJoined(new RoomEvent2(trucoRoomEvent2));
        }
        for (TrucoRoomTableDescriptor table : trucoRoomEvent.getRoom().getTables()) {
            TrucoRoomEvent trucoRoomEvent2 = new TrucoRoomEvent();
            trucoRoomEvent2.setRoom(trucoRoomEvent.getRoom());
            trucoRoomEvent2.setTable(table);
            target.tableCreated(new RoomEvent2(trucoRoomEvent2));
        }


//            final String tableId = table.getId();
//            createTable(trucoRoomEvent.getRoom(), table);
////                for (TrucoUser user : table.getUsers()) {
//////                userTableJoined(roomId, tableId, user);
////                }
//            for (int i = 0; i < table.getPositions().length; i++) {
//                TrucoUser user = table.getPositions()[i];
//                if (user != null) {
//                    roomTableEventDispatcher.tablePositionSetted(trucoRoomEvent.getRoom().getId(), tableId, user, i);
//                }
//            }
//        }
        logger.debug("========  ROOM UPDATED =======");
    }

    private void createTable(TrucoRoomDescriptor room, TrucoRoomTableDescriptor table) {
        TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
        trucoRoomEvent.setRoom(room);
        trucoRoomEvent.setTable(table);
        trucoRoomEvent.setUser(table.getOwner());
        trucoRoomEvent.setTable(table);
        tableCreated(trucoRoomEvent);

    }


    private void tableCreated(TrucoRoomEvent trucoRoomEvent) {
        target.tableCreated(new RoomEvent2(trucoRoomEvent));
        //Agregar

        //
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
