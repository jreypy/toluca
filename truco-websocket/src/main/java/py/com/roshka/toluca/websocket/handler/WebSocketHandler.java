package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public abstract class WebSocketHandler<T> extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    ObjectMapper objectMapper;


    public WebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.debug("HandleTextMessage", message);
        super.handleTextMessage(session, message);
        session.sendMessage(new TextMessage("Hello!"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("afterConnectionEstablished [" + session.getId() + "]");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("afterConnectionClosed [" + session.getId() + "]");
        super.afterConnectionClosed(session, status);
    }


}
