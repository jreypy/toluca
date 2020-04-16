package py.com.roshka.toluca.websocket.handler;

import java.io.IOException;

public interface WebSocketSessionListener {
    void messageReceived(String payload) throws IOException;

    void sessionTerminated(WebSocketSessionHandler handler);

    void afterConnectionEstablished(WebSocketSessionHandler handler);
}
