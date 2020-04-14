package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;

public class TrucoRoomTable extends TrucoRoomTableDescriptor{

    final static public String NEW = "NEW";
    final static public String PROGRESS = "PROGRESS";
    final static public String FINISHED = "FINISHED";

    private String id;
    private String roomId;
    private TrucoUser owner;
    private boolean privateTable;
    private List<Chair> chairs = new ArrayList<Chair>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRoomId() {
        return roomId;
    }

    @Override
    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public List<Chair> getChairs() {
        return chairs;
    }

    public void setChairs(List<Chair> chairs) {
        this.chairs = chairs;
    }

    @Override
    public String toString() {
        return "TrucoRoomTable{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", owner=" + owner +
                ", privateTable=" + privateTable +
                ", chairs=" + chairs +
                "} " + super.toString();
    }
}
