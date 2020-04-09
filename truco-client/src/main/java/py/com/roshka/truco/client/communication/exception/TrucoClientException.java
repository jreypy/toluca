package py.com.roshka.truco.client.communication.exception;

public class TrucoClientException extends Exception {
    public TrucoClientException(String message) {
        super(message);
    }

    public TrucoClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
