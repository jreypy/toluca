package py.edu.uca.fcyt.toluca.game;

public class TrucoGameRuntimeException extends RuntimeException{
    public TrucoGameRuntimeException(String message) {
        super(message);
    }

    public TrucoGameRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
