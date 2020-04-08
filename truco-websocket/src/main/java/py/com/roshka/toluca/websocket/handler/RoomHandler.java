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
import py.com.roshka.toluca.websocket.service.WebSocketSessionManager;

@Component
public class RoomHandler extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(RoomHandler.class);

    ObjectMapper objectMapper;
    CommandProcessor commandProcessor;
    WebSocketSessionManager webSocketSessionManager;

    int count;

    public RoomHandler(ObjectMapper objectMapper, CommandProcessor commandProcessor, WebSocketSessionManager webSocketSessionManager) {
        this.objectMapper = objectMapper;
        this.commandProcessor = commandProcessor;
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        logger.debug("HandleTextMessage", message);
        super.handleTextMessage(session, message);
        String payload = message.getPayload();

        if ("QUIT".equalsIgnoreCase(payload)) {
            logger.debug("Close Session by Client Request");
            session.close();
        } else {
            Event event = commandProcessor.processCommand(auth, objectMapper.readValue(payload, Command.class));
            if (event != null) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
            }
        }

        //
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        count++;
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        String query = session.getUri().getQuery();
        String message = "Hello! " + auth.split("-")[0] + " requesting [" + query + "]";
        session.sendMessage(new TextMessage(message));
        logger.debug("afterConnectionEstablished [" + session.getId() + "][" + count + "]");
        super.afterConnectionEstablished(session);

        // roomService.connect(auth.split("-")[0]);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        count--;
        logger.debug("afterConnectionClosed [" + session.getId() + "][" + count + "]");
        super.afterConnectionClosed(session, status);

    }

}
