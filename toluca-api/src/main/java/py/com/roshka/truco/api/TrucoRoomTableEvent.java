package py.com.roshka.truco.api;

public class TrucoRoomTableEvent extends TrucoEvent {
    private String roomId;
    private TrucoUser user;
    private String tableId;
    private Integer position;
    private TrucoRoomTableDescriptor table;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public TrucoUser getUser() {
        return user;
    }

    public void setUser(TrucoUser user) {
        this.user = user;
    }

    public TrucoRoomTableDescriptor getTable() {
        return table;
    }

    public void setTable(TrucoRoomTableDescriptor table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "TrucoRoomTableEvent{" +
                "roomId='" + roomId + '\'' +
                ", user=" + user +
                ", tableId='" + tableId + '\'' +
                ", position=" + position +
                ", table=" + table +
                "} " + super.toString();
    }
}
