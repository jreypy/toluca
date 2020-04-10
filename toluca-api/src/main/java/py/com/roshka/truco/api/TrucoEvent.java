package py.com.roshka.truco.api;

public class TrucoEvent {
    private String eventName;
    private String message;

    public TrucoEvent() {
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
}
