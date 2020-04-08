package py.com.roshka.toluca.websocket.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.toluca.websocket.service.WebSocketSessionManager;
import py.com.roshka.truco.api.TrucoRoom;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketSessionManagerImpl implements WebSocketSessionManager, TrucoRoomListener {
    Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    Map<String, WebSocketSession> sessions = new HashMap<>();


    @Override
    public void sessionConnected(WebSocketSession webSocketSession) {
        logger.debug("Session Connected [" + webSocketSession.getId() + "]");
        sessions.put(webSocketSession.getId(), webSocketSession);
    }

    @Override
    public void sessionDisconnected(WebSocketSession webSocketSession) {
        //deregister
        logger.debug("Session Disconnected [" + webSocketSession.getId() + "]");
        sessions.remove(webSocketSession.getId());
    }

    @Override
    public void roomCreated(TrucoRoom trucoRoom) {
        // Iterate Over sessions
    }

    private void sendMessage() {
        // Command Processor
    }
}
