package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.EventProcessor;

import java.util.HashMap;
import java.util.Map;

public abstract class WebSocketHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);


    private Map<String, WebSocketSession> sessions = new HashMap<>();
    int count;

    protected  ObjectMapper objectMapper;
    protected  EventProcessor eventProcessor;
    protected CommandProcessor commandProcessor;

    public WebSocketHandler(ObjectMapper objectMapper, EventProcessor eventProcessor, CommandProcessor commandProcessor) {
        this.objectMapper = objectMapper;
        this.eventProcessor = eventProcessor;
        this.commandProcessor = commandProcessor;
    }

    public WebSocketHandler(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        count++;
        logger.debug("afterConnectionEstablished [" + session.getId() + "][" + count + "]");
        super.afterConnectionEstablished(session);
        // roomService.connect(auth.split("-")[0]);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        count--;
        sessions.remove(session.getId());
        logger.debug("afterConnectionClosed [" + session.getId() + "][" + count + "]");
        super.afterConnectionClosed(session, status);
    }

    protected void sendEvent(Event event) {
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
