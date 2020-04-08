package py.com.roshka.toluca.websocket.service;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionManager {


    public void sessionConnected(WebSocketSession webSocketSession);

    public void sessionDisconnected(WebSocketSession webSocketSession);
}
