package py.com.roshka.truco.api;

public class TrucoRoomTableEvent extends TrucoEvent {
    private String roomId;
    private TrucoUser user;
    private String tableId;
    private Integer chair;

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

    public Integer getChair() {
        return chair;
    }

    public void setChair(Integer chair) {
        this.chair = chair;
    }

    public TrucoUser getUser() {
        return user;
    }

    public void setUser(TrucoUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TrucoRoomTableEvent{" +
                "roomId='" + roomId + '\'' +
                ", user=" + user +
                ", tableId='" + tableId + '\'' +
                ", chair=" + chair +
                "} " + super.toString();
    }
}
