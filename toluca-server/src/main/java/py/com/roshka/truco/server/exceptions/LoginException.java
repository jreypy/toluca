package py.com.roshka.truco.server.exceptions;

public class LoginException extends TrucoServerException {

    public LoginException(String message) {
        super(message, null);
    }
}
