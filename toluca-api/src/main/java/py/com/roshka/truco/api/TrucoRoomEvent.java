package py.com.roshka.truco.api;

import java.util.Collection;
import java.util.Set;

public class TrucoRoomEvent extends TrucoEvent {

    private TrucoUser user;
    private TrucoRoomDescriptor room;
    private TrucoRoomTableDescriptor table;
    private Collection<TrucoRoom> rooms;

    public TrucoRoomEvent() {
    }

    private TrucoRoomEvent(String eventName, String message, TrucoUser user, TrucoRoomDescriptor room, TrucoRoomTableDescriptor table, Collection<TrucoRoom> rooms) {
        super(eventName, message);
        this.user = user;
        this.room = room;
        this.table = table;
        this.rooms = rooms;
    }

    public TrucoUser getUser() {
        return user;
    }

    public void setUser(TrucoUser user) {
        this.user = user;
    }

    public TrucoRoomDescriptor getRoom() {
        return room;
    }

    public void setRoom(TrucoRoomDescriptor room) {
        this.room = room;
    }

    public TrucoRoomTableDescriptor getTable() {
        return table;
    }

    public void setTable(TrucoRoomTableDescriptor table) {
        this.table = table;
    }

    public Collection<TrucoRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<TrucoRoom> rooms) {
        this.rooms = rooms;
    }

    static public TrucoRoomEventBuilder builder(String eventName) {
        return new TrucoRoomEventBuilder(eventName);
    }


    public static class TrucoRoomEventBuilder {
        private String eventName;
        private TrucoUser user;
        private TrucoRoomDescriptor room;
        private TrucoRoomTableDescriptor table;
        private String message;
        private Collection<TrucoRoom> rooms;

        private TrucoRoomEventBuilder(String eventName) {
            this.eventName = eventName;
        }

        public TrucoRoomEventBuilder message(String message) {
            this.message = message;
            return this;
        }


        public TrucoRoomEventBuilder user(TrucoUser user) {
            this.user = user;
            return this;
        }

        public TrucoRoomEventBuilder room(TrucoRoomDescriptor room) {
            this.room = room;
            return this;
        }

        public TrucoRoomEventBuilder table(TrucoRoomTableDescriptor table) {
            this.table = table;
            return this;
        }


        public TrucoRoomEventBuilder rooms(Collection<TrucoRoom> rooms) {
            this.rooms = rooms;
            return this;
        }

        public TrucoRoomEvent build() {
            return new TrucoRoomEvent(eventName, message, user, room, table, rooms);
        }
    }


}
