package py.com.roshka.truco.api;

import java.util.Map;

// TODO Change to Rabbit Message
public class RabbitResponse {
    String eventName;
    private Object data;

    private String roomId;
    private String tableId;

    public RabbitResponse() {
    }


    public RabbitResponse(String eventName, TrucoEvent data) {
        this.eventName = eventName;
        this.data = data;
    }

    public RabbitResponse(String eventName, String roomId, TrucoEvent data) {
        this.eventName = eventName;
        this.data = data;
        this.roomId = roomId;
    }

    public RabbitResponse(String eventName, String roomId, String tableId, TrucoEvent data) {
        this.eventName = eventName;
        this.data = data;
        this.roomId = roomId;
        this.tableId = tableId;
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

    @Override
    public String toString() {
        return "RabbitResponse{" +
                "eventName='" + eventName + '\'' +
                ", data=" + data +
                '}';
    }
}
