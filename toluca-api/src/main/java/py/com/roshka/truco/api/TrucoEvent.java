package py.com.roshka.truco.api;

public class TrucoEvent {
    private String eventName;
    private String message;

    public TrucoEvent() {
    }

    protected TrucoEvent(String eventName, String message) {
        this.eventName = eventName;
        this.message = message;
    }

    public TrucoEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    @Override
    public String toString() {
        return "TrucoEvent{" +
                "eventName='" + eventName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
