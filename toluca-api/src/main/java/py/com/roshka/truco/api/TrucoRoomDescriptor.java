package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrucoRoomDescriptor {
    protected String id;
    protected String name;
    protected Set<TrucoRoomUser> users = null;
    protected Set<TrucoRoomTableDescriptor> tables;





    public TrucoRoomDescriptor() {
    }

    protected TrucoRoomDescriptor(TrucoRoomDescriptor trucoRoomDescriptor) {
        this.id = trucoRoomDescriptor.id;
        this.name = trucoRoomDescriptor.name;
    }

    public TrucoRoomDescriptor(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public TrucoRoomDescriptor descriptor() {
        return new TrucoRoomDescriptor(this.id, this.name);
    }

    public Set<TrucoRoomUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TrucoRoomUser> users) {
        this.users = users;
    }

    public Set<TrucoRoomTableDescriptor> getTables() {
        return tables;
    }

    public void setTables(Set<TrucoRoomTableDescriptor> tables) {
        this.tables = tables;
    }



    @Override
    public String toString() {
        return "TrucoRoomDescriptor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
