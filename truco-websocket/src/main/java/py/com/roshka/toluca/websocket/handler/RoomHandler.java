package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.global.Events;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.EventProcessor;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoRoom;

import java.util.Collection;

@Component
public class RoomHandler extends WebSocketHandler implements TrucoRoomListener {
    Logger logger = LoggerFactory.getLogger(RoomHandler.class);


    public RoomHandler(ObjectMapper objectMapper, EventProcessor eventProcessor, CommandProcessor commandProcessor) {
        super(objectMapper, eventProcessor, commandProcessor);
        this.logger = logger;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return new TrucoPrincipal(auth);
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public void setAuthenticated(boolean b) throws IllegalArgumentException {

                }

                @Override
                public String getName() {
                    return null;
                }
            });
            logger.debug("Security Context Started [" + SecurityContextHolder.getContext() + "]");
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // TODO
        } finally {
            SecurityContextHolder.clearContext();
            logger.debug("Security Context finished [" + SecurityContextHolder.getContext() + "]");

        }

        //
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        String query = session.getUri().getQuery();
        String message = "Hello! " + auth.split("-")[0] + " requesting [" + query + "]";
        session.sendMessage(new TextMessage(message));
        super.afterConnectionEstablished(session);

        // roomService.connect(auth.split("-")[0]);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

    }

    @Override
    public void roomCreated(TrucoRoom trucoRoom) {
        super.sendEvent(eventProcessor.sendEvent(Events.ROOM_CREATED, trucoRoom));
    }
}
