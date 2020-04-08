package py.com.roshka.truco.api;

import java.util.HashSet;
import java.util.Set;

public class TrucoRoom {
    private String id;
    private String name;
    private Set<TrucoRoomUser> users = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TrucoRoomUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TrucoRoomUser> users) {
        this.users = users;
    }
}
