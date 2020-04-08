package py.com.roshka.truco.api;

import java.util.Map;

public class RabbitResponse {
    String eventName;
    private String type;
    private Map data;

    public RabbitResponse(String eventName, String type, Map data) {
        this.eventName = eventName;
        this.type = type;
        this.data = data;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
}
