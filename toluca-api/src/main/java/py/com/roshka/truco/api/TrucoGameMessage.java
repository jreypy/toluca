package py.com.roshka.truco.api;

public class TrucoGameMessage {
    private String text;

    public TrucoGameMessage() {
    }

    public TrucoGameMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
