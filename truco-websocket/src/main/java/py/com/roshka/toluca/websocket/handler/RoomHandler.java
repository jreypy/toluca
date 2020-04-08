package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;

@Component
public class RoomHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    ObjectMapper objectMapper;
    CommandProcessor commandProcessor;

    public RoomHandler(ObjectMapper objectMapper, CommandProcessor commandProcessor) {
        this.objectMapper = objectMapper;
        this.commandProcessor = commandProcessor;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        logger.debug("HandleTextMessage", message);
        super.handleTextMessage(session, message);
        Event event = commandProcessor.processCommand(auth, objectMapper.readValue(message.getPayload(), Command.class));
        if (event != null) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
        }
        //
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        String query = session.getUri().getQuery();
        String message = "Hello! " + auth.split("-")[0] + " requesting [" + query + "]";
        session.sendMessage(new TextMessage(message));
        logger.debug("afterConnectionEstablished [" + session.getId() + "]");
        super.afterConnectionEstablished(session);
        // roomService.connect(auth.split("-")[0]);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("afterConnectionClosed [" + session.getId() + "]");
        super.afterConnectionClosed(session, status);
    }

}
