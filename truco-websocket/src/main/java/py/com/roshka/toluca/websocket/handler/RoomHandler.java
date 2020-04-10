package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.global.Events;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.truco.api.RabbitResponse;
import py.com.roshka.truco.api.TrucoEvent;
import py.com.roshka.truco.api.TrucoPrincipal;

import java.io.IOException;
import java.util.*;

@Component
public class RoomHandler extends WebSocketHandler {
    Logger logger = LoggerFactory.getLogger(RoomHandler.class);


    AMQPDispatcher amqpDispatcher;

    public RoomHandler(AMQPDispatcher amqpDispatcher, ObjectMapper objectMapper, CommandProcessor commandProcessor) {
        super(objectMapper, commandProcessor);
        this.amqpDispatcher = amqpDispatcher;
    }

    public TrucoPrincipal getTrucoPrincipal(WebSocketSession session) {
        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        return new TrucoPrincipal(auth);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


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
                    return getTrucoPrincipal(session);
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
                CommandResponse commandResponse = commandProcessor.processCommand(objectMapper.readValue(payload, Command.class));
                if (commandResponse != null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(commandResponse)));
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
        TrucoPrincipal trucoPrincipal = getTrucoPrincipal(session);
        session.getAttributes().put("username", trucoPrincipal.getUsername());
        String query = session.getUri().getQuery();
        String message = "Hello! " + trucoPrincipal.getUsername() + " is connected [" + query + "]";
        {
            Map map = new LinkedHashMap<>();
            map.put("message", message);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(map)));
        }

        // Remove others connections
        //Iterator it = map.entrySet().iterator();
        Iterator<Map.Entry<String, WebSocketSession>> it = sessions.entrySet().iterator();
        while (it.hasNext()) {
            WebSocketSession s = it.next().getValue();
            if (s.getAttributes().get("username").equals(trucoPrincipal.getUsername())) {
                if (s.isOpen()) {
                    // Disconnect
                    logger.debug("Disconect because is already connected");
                    Map goodBye = new LinkedHashMap<>();
                    s.getAttributes().put(Events.USER_FIRED_BY_SERVER, Events.USER_FIRED_BY_SERVER);
                    goodBye.put("message", "Usuario se conectó utilizando otra aplicación");
                    Event event = new Event(Events.USER_FIRED_BY_SERVER, goodBye);
                    sendEvent(event);
                    try {
                        s.close();
                    } catch (IOException e) {
                        logger.error("Error firing Connection [" + s.getId() + "]", e);
                    }
                }
            }
        }
        super.afterConnectionEstablished(session);
        // roomService.connect(auth.split("-")[0]);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (!Events.USER_FIRED_BY_SERVER.equals(session.getAttributes().get(Events.USER_FIRED_BY_SERVER))) {
            String username = (String) session.getAttributes().get("username");
            logger.debug("Users was disconnected [" + username + "]");
            Map map = new LinkedHashMap();
            map.put("username", username);
            amqpDispatcher.send("truco_user_event", "logout", new RabbitResponse(py.com.roshka.truco.api.Event.LOGOUT, Map.class.getCanonicalName(), map));
        }
        super.afterConnectionClosed(session, status);

    }


}
