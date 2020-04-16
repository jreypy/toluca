package py.com.roshka.truco.api;

public class JoinRabbitResponse {
    private String channel;
    private TrucoUser trucoUser;
    private RabbitResponse rabbitResponse = null;

    public JoinRabbitResponse(String channel, TrucoUser trucoUser, RabbitResponse rabbitResponse) {
        this.channel = channel;
        this.trucoUser = trucoUser;
        this.rabbitResponse = rabbitResponse;
    }



    public JoinRabbitResponse() {
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


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
