package py.com.roshka.truco.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrucoRoom {
    private String id;
    private String name;
    private Set<TrucoUser> users = new HashSet<TrucoUser>();

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

    public Set<TrucoUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TrucoUser> users) {
        this.users = users;
    }
}
