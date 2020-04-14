package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import py.com.roshka.toluca.websocket.beans.Event;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSessionManager extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    protected Map<String, WebSocketSessionHandler> sessions = new HashMap<>();
    protected ObjectMapper objectMapper;
    int count;

    public WebSocketSessionManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public void addSession(WebSocketSessionHandler handler) {
        count++;
        logger.debug("Adding Session [" + handler + "][" + count + "]");
        //
        sessions.put(handler.getId(), handler);

    }

    public void removeSession(WebSocketSessionHandler session) {
        count--;
        logger.debug("Removing Session [" + session + "][" + count + "]");
        sessions.remove(session.getId());
        //remove from rooms

    }

    public void sendEvent(Event event) {
        logger.debug("Firing WS Event [" + event + "]");
        for (WebSocketSession session : sessions.values()) {
            sendEvent(session, event);
        }
    }

    protected void sendEvent(WebSocketSession session, Event event) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
            }
        } catch (Exception e) {
            logger.error("Event could not be sent [" + event + "]", e);
        }
    }

}
