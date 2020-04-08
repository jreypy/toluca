package py.com.roshka.truco.api;

import java.util.Map;

public class RabbitRequest {
    private String eventName;
    private String type;
    private Map data;

    public RabbitRequest() {
    }

    public RabbitRequest(String type, Map data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RabbitRequest{" +
                "eventName='" + eventName + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
