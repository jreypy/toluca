package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

import java.util.Map;

public class TrucoClientDispatcher {

    ObjectMapper objectMapper;

    public TrucoClientDispatcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    RoomEvent convertToRoomEvent(Map event) {
        TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(event, TrucoRoomEvent.class);
        if (trucoRoomEvent.getRoom().getId().equalsIgnoreCase(TrucoFrame.MAIN_ROOM)) {
            if (trucoRoomEvent.equals(Event.ROOM_USER_JOINED)) {
                RoomEvent joinUser = new RoomEvent();
                joinUser.setType(RoomEvent.TYPE_PLAYER_JOINED);
                joinUser.setPlayer(new TrucoPlayer());
                joinUser.getPlayer().setName("sricco");
                joinUser.getPlayer().setId("sricco");
                getEventDispatcher().dispatchEvent(joinUser);
            }
        }
    }
}
