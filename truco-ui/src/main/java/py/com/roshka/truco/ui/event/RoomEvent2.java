package py.com.roshka.truco.ui.event;

import py.com.roshka.truco.api.TrucoRoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomEvent;

public class RoomEvent2 extends RoomEvent {
    TrucoRoomEvent source;

    public RoomEvent2(TrucoRoomEvent source) {
        this.source = source;
    }

    public TrucoRoomEvent getSource() {
        return source;
    }
}
