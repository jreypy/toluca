package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcher;

import java.util.Map;

public class TrucoClientDispatcher {

    Logger logger = Logger.getLogger(TrucoClientDispatcher.class);

    private ObjectMapper objectMapper;
    private EventDispatcher eventDispatcher;

    public TrucoClientDispatcher(ObjectMapper objectMapper, EventDispatcher eventDispatcher) {
        this.objectMapper = objectMapper;
        this.eventDispatcher = eventDispatcher;
    }

    public void dispatchEvent(Map event) {
        String type = (String) event.get("type");
        logger.debug("Dispatching event [" + type + "]");
        if (Event.ROOM_USER_JOINED.equalsIgnoreCase(type)) {
            dispatchRoomEvent((Map) event.get("data"));
        }
    }

    void dispatchRoomEvent(Map eventData) {
        logger.debug("Dispatching event [" + eventData + "]");
        TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(eventData, TrucoRoomEvent.class);
        dispatchRoomEvent(trucoRoomEvent);
    }

    void dispatchRoomEvent(TrucoRoomEvent trucoRoomEvent) {
        if (TrucoFrame.MAIN_ROOM.equalsIgnoreCase(trucoRoomEvent.getRoom().getId())) {
            if (Event.ROOM_USER_JOINED.equalsIgnoreCase(trucoRoomEvent.getEventName())) {
                RoomEvent roomEvent = new RoomEvent();
                roomEvent.setType(RoomEvent.TYPE_PLAYER_JOINED);
                roomEvent.setPlayer(new TrucoPlayer());
                roomEvent.getPlayer().setName(trucoRoomEvent.getUser().getUsername());
                roomEvent.getPlayer().setId(trucoRoomEvent.getUser().getId());
                eventDispatcher.dispatchEvent(roomEvent);
            }
        }
    }

}
