package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;

public class TrucoRoomTable {

    final static public String NEW = "NEW";
    final static public String PROGRESS = "PROGRESS";
    final static public String FINISHED = "FINISHED";

    private String id;
    private String roomId;
    private TrucoUser owner;
    private boolean privateTable;
    private List<Chair> chairs = new ArrayList<Chair>();
    private String status = NEW;
    private Integer points;

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

    public TrucoUser getOwner() {
        return owner;
    }

    public void setOwner(TrucoUser owner) {
        this.owner = owner;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
