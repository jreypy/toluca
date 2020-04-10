package py.com.roshka.truco.api;

public class TrucoRoomEvent extends TrucoEvent {
    private String eventName;
    private TrucoUser user;
    private TrucoRoom room;

    public TrucoRoomEvent() {
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public TrucoUser getUser() {
        return user;
    }

    public void setUser(TrucoUser user) {
        this.user = user;
    }

    public TrucoRoom getRoom() {
        return room;
    }

    public void setRoom(TrucoRoom room) {
        this.room = room;
    }
}
