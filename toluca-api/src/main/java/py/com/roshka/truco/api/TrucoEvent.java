package py.com.roshka.truco.api;

public class TrucoEvent {
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
}
