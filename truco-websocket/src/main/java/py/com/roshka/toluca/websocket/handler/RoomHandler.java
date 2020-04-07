package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class RoomHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(RoomHandler.class);


    public RoomHandler(ObjectMapper objectMapper) {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.debug("HandleTextMessage", message);
        super.handleTextMessage(session, message);
        session.sendMessage(new TextMessage("Hello! " + session.getUri()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        String query = session.getUri().getQuery();
        String message = "Hello! " + auth.split("-")[0] + " requesting [" + query + "]";
        session.sendMessage(new TextMessage(message));
        logger.debug("afterConnectionEstablished [" + session.getId() + "]");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("afterConnectionClosed [" + session.getId() + "]");
        super.afterConnectionClosed(session, status);
    }

}
