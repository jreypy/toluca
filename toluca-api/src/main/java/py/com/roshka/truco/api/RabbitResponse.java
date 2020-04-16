package py.com.roshka.truco.api;

// TODO Change to Rabbit Message
public class RabbitResponse {
    String eventName;
    private Object data;
    private String channel;

    public RabbitResponse() {
    }


    public RabbitResponse(String eventName, TrucoEvent data) {
        this.eventName = eventName;
        this.data = data;
    }

    public RabbitResponse(String eventName, String channel, TrucoEvent data) {
        this.eventName = eventName;
        this.data = data;
        this.channel = channel;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "RabbitResponse{" +
                "eventName='" + eventName + '\'' +
                ", data=" + data +
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
