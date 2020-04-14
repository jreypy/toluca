package py.com.roshka.truco.api.request;

public class RoomRequest {
    private String roomId;

    public RoomRequest() {
    }

    public RoomRequest(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomRequest{" +
                "roomId='" + roomId + '\'' +
                '}';
    }
}
