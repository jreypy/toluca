package py.com.roshka.toluca.websocket.handler;

import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.global.Events;
import py.com.roshka.toluca.websocket.service.EventProcessor;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;

@Component
public class TrucoRoomListenerImpl implements TrucoRoomListener {

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
        roomHandler.sendRoomEvent(roomId, trucoRoomEvent.getUser(), eventProcessor.sendEvent(Events.JOINED_TO_ROOM, trucoRoomEvent));
    }
}