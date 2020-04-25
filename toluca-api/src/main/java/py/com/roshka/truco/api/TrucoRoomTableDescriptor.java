package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrucoRoomTableDescriptor {
    static public final int TABLE_SIZE = 6;

    final static public String NEW = "NEW";
    final static public String IN_PROGRESS = "IN_PROGRESS";
    final static public String FINISHED = "FINISHED";

    private String id;
    private String status = TrucoRoomTableDescriptor.NEW;
    private String roomId;
    private Integer points;
    private TrucoUser owner;
    private boolean privateTable;
    private Set<TrucoUser> users = null;
    private TrucoUser[] positions = null;

    private TrucoRoomTableDescriptor(String id, String status, String roomId, Integer points, TrucoUser owner) {
        this.id = id;
        this.status = status;
        this.roomId = roomId;
        this.points = points;
        this.owner = owner;
    }

    protected TrucoRoomTableDescriptor descriptor(TrucoRoomTableDescriptor table) {
        return new TrucoRoomTableDescriptor(table.getId(), table.getStatus(), table.getRoomId(), table.getPoints(), table.getOwner());
    }

    protected TrucoRoomTableDescriptor(TrucoRoomTableDescriptor descriptor) {
        this.id = descriptor.id;
        this.status = descriptor.status;
        this.roomId = descriptor.roomId;
        this.points = descriptor.points;
    }

    public TrucoRoomTableDescriptor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public TrucoRoomTableDescriptor descriptor() {
        return descriptor(this);
    }

    public TrucoUser getOwner() {
        return owner;
    }

    public void setOwner(TrucoUser owner) {
        this.owner = owner;
    }

    public boolean isPrivateTable() {
        return privateTable;
    }

    public void setPrivateTable(boolean privateTable) {
        this.privateTable = privateTable;
    }

    public TrucoUser[] getPositions() {
        return positions;
    }

    public void setPositions(TrucoUser[] positions) {
        this.positions = positions;
    }

    public Set<TrucoUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TrucoUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "TrucoRoomTableDescriptor{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", roomId='" + roomId + '\'' +
                ", points=" + points +
                ", owner=" + owner +
                ", privateTable=" + privateTable +
                ", users=" + users +
                ", positions=" + positions +
                '}';
    }
}
