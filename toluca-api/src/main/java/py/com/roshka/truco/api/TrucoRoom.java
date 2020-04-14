package py.com.roshka.truco.api;

import java.util.HashSet;
import java.util.Set;

public class TrucoRoom extends TrucoRoomDescriptor {
    private Set<TrucoRoomUser> users = new HashSet<>();

    public TrucoRoom() {
    }

    public TrucoRoom(String id, String name) {
        super(id, name);
    }

    public Set<TrucoRoomUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TrucoRoomUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "TrucoRoom{" +
                "users=" + users +
                "} " + super.toString();
    }
}
