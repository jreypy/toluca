package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.api.constants.Commands;
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
        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
            dispatchCommandResponse((String) event.get("command"), (String) event.get("id"), (Map) event.get("data"));
        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(type)) {
            dispatchRoomEvent((Map) event.get("data"));
        }
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


}
