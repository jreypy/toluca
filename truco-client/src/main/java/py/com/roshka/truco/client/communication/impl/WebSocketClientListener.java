package py.com.roshka.truco.client.communication.impl;

import java.util.Map;

public interface WebSocketClientListener {
    void onOpen(WebSocketClient webSocketClient);

    void onClose(WebSocketClient webSocketClient);

    boolean onMessage(WebSocketClient webSocketClient, Map map);

    void onError(Exception ex);
}