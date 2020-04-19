package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoRoomTableEvent;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.ui.event.TableEvent2;
import py.com.roshka.truco.ui.net.EventDispatcherClient2;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.TableServer;

public class TrucoRoomTableEventDispatcher {
    Logger logger = Logger.getLogger(TrucoRoomTableEventDispatcher.class);

    final private WebSocketCommunicatorClient communicatorClient;
    private EventDispatcherClient target;


    public TrucoRoomTableEventDispatcher(WebSocketCommunicatorClient communicatorClient, EventDispatcherClient target) {
        this.communicatorClient = communicatorClient;
        this.target = target;
    }

    public void dispatchRoomEvent(TrucoRoomEvent trucoRoomEvent) {

        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
        trucoRoomTableEvent.setEventName(trucoRoomEvent.getEventName());
        trucoRoomTableEvent.setRoomId(trucoRoomEvent.getRoom().getId());
        trucoRoomTableEvent.setTableId(trucoRoomEvent.getTable().getId());
        trucoRoomTableEvent.setUser(trucoRoomEvent.getUser());


    }

    public void dispatchRoomTableEvent(TrucoRoomTableEvent trucoRoomTableEvent) {
        TableEvent2 tableEvent2 = new TableEvent2(trucoRoomTableEvent);
        if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(trucoRoomTableEvent.getEventName())) {
            target.playerSit(tableEvent2);
        } else {
//            target.dispatchEvent(new RoomEvent2(trucoRoomEvent));
            logger.info("Event [" + trucoRoomTableEvent.getEventName() + "] not implemented");
        }
    }

    public void dispatchRoomTableEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        target.dispatchEvent(getRoomEvent(eventType, eventData));

    }

    public void dispatchPlayerSitEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        TableEvent tableEvent = getTableEvent(eventType, eventData);
        tableEvent.setValue(eventData.getPosition());
    }

    private TableEvent getTableEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        TableEvent tableEvent = new TableEvent();
        tableEvent.setEvent(eventType);
        tableEvent.setPlayer(new TrucoPlayer[]{TolucaHelper.getPlayer(eventData.getUser())});
        tableEvent.setTableServer(new TableServer());
        tableEvent.getTableServer().setTableNumber(Integer.parseInt(eventData.getTableId()));
        return tableEvent;
    }

    private RoomEvent getRoomEvent(Integer eventType, TrucoRoomTableEvent eventData) {
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setType(eventType);
        roomEvent.setPlayer(TolucaHelper.getPlayer(eventData.getUser()));
        roomEvent.setTableServer(new TableServer());
        roomEvent.getTableServer().setTableNumber(Integer.parseInt(eventData.getTableId()));
        return roomEvent;
    }

    public void tablePositionSetted(String roomId, String tableId, TrucoUser user, int position) {
        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
        trucoRoomTableEvent.setEventName(Event.TABLE_POSITION_SETTED);
        trucoRoomTableEvent.setRoomId(roomId);
        trucoRoomTableEvent.setTableId(tableId);
        trucoRoomTableEvent.setUser(user);
        trucoRoomTableEvent.setPosition(position);
        dispatchRoomTableEvent(trucoRoomTableEvent);
    }

}
