package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;

public class TrucoRoomTable {

    final static public String NEW = "NEW";
    final static public String PROGRESS = "PROGRESS";
    final static public String FINISHED = "FINISHED";

    private String id;
    private String roomId;
    private String ownerId;
    private boolean privateTable;
    private List<Chair> chairs = new ArrayList<Chair>();
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
}
