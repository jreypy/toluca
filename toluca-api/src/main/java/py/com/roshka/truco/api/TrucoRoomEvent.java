package py.com.roshka.truco.api;

public class TrucoRoomEvent {
    private String eventName;
    private String message;
    private TrucoUser user;
    private TrucoRoom room;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
