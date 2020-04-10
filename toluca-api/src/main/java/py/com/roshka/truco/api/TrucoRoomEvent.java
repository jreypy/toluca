package py.com.roshka.truco.api;

public class TrucoRoomEvent extends TrucoEvent {

    private TrucoUser user;
    private TrucoRoom room;

    public TrucoRoomEvent() {
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
