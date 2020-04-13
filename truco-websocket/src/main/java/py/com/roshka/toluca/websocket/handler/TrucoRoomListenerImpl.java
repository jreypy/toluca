package py.com.roshka.toluca.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.global.Events;
import py.com.roshka.toluca.websocket.service.EventProcessor;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.*;

import java.util.Map;

@Component
public class TrucoRoomListenerImpl implements TrucoRoomListener {
    Logger logger = LoggerFactory.getLogger(TrucoRoomListenerImpl.class);

    RoomHandler roomHandler;
    EventProcessor eventProcessor;

    public TrucoRoomListenerImpl(RoomHandler roomHandler, EventProcessor eventProcessor) {
        this.roomHandler = roomHandler;
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void roomCreated(TrucoRoom trucoRoom) {
        roomHandler.sendEvent(eventProcessor.sendEvent(Events.ROOM_CREATED, trucoRoom));
    }

    @Override
    public void joinedToRoom(String roomId, TrucoRoomEvent trucoRoomEvent) {
        roomHandler.sendRoomEvent(roomId, trucoRoomEvent.getUser(), eventProcessor.sendEvent(Event.ROOM_USER_JOINED, trucoRoomEvent));
    }

    @Override
    public void userLeftTheRoom(String roomId, TrucoRoomEvent trucoRoomEvent) {
        roomHandler.sendRoomEvent(roomId, trucoRoomEvent.getUser(), eventProcessor.sendEvent(Event.ROOM_USER_LEFT, trucoRoomEvent));
    }

    @Override
    public void roomTableCreated(String roomId, TrucoRoomTable trucoRoomTable) {
        roomHandler.sendRoomEvent(roomId, eventProcessor.sendEvent(Event.ROOM_TABLE_CREATED, trucoRoomTable));
    }

    @Override
    public void trucoRoomTableEventReceived(String roomId, TrucoRoomTableEvent trucoRoomTableEvent) {
        roomHandler.sendRoomEvent(roomId, eventProcessor.sendEvent(trucoRoomTableEvent.getEventName(), trucoRoomTableEvent));
    }

    @Override
    public void trucoGameEvent(String roomId, Map gameEvent) {
        // TODO Send to Table
        roomHandler.sendRoomEvent(roomId, eventProcessor.sendEvent(Event.TRUCO_GAME_EVENT, gameEvent));
    }

}
