package py.com.roshka.truco.api;

public class TrucoRoomTableDescriptor {
    private String id;
    private String status = TrucoRoomTable.NEW;
    private String roomId;
    private Integer points;

    private TrucoRoomTableDescriptor(String id, String status, String roomId, Integer points) {
        this.id = id;
        this.status = status;
        this.roomId = roomId;
        this.points = points;
    }

    protected TrucoRoomTableDescriptor descriptor(TrucoRoomTableDescriptor table) {
        return new TrucoRoomTableDescriptor(table.getId(), table.getStatus(), table.getRoomId(), table.getPoints());
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

    @Override
    public String toString() {
        return "TrucoRoomTableDescriptor{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", roomId='" + roomId + '\'' +
                ", points=" + points +
                '}';
    }
}
