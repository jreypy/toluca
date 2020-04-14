package py.com.roshka.truco.api;

public class JoinRabbitResponse  {
    private String roomId;
    private TrucoUser trucoUser;
    private RabbitResponse rabbitResponse = null;

    public JoinRabbitResponse(String roomId, TrucoUser trucoUser, RabbitResponse rabbitResponse) {
        this.roomId = roomId;
        this.trucoUser = trucoUser;
        this.rabbitResponse = rabbitResponse;
    }

    public JoinRabbitResponse() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public TrucoUser getTrucoUser() {
        return trucoUser;
    }

    public void setTrucoUser(TrucoUser trucoUser) {
        this.trucoUser = trucoUser;
    }

    public RabbitResponse getRabbitResponse() {
        return rabbitResponse;
    }

    public void setRabbitResponse(RabbitResponse rabbitResponse) {
        this.rabbitResponse = rabbitResponse;
    }
}
