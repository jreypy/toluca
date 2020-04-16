package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.socket.handler.TextWebSocketHandler;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.global.Events;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.impl.ChannelSvcImpl;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.api.event.LogoutEvent;

import java.io.IOException;
import java.util.*;

@Component
public class WebSocketSessionManager extends TextWebSocketHandler {
    Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    protected Map<String, WebSocketSessionHandler> sessions = new HashMap<>();
    protected ObjectMapper objectMapper;
    final CommandProcessor commandProcessor;
    int count;
    WebSocketSessionListener webSocketSessionListener;

    public WebSocketSessionManager(ObjectMapper objectMapper, CommandProcessor commandProcessor) {
        this.objectMapper = objectMapper;
        this.commandProcessor = commandProcessor;
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
                    WebSocketPrincipal webSocketPrincipal = WebsocketHelper.getTrucoPrincipal(session);
                    webSocketPrincipal.setHandler(sessions.get(session.getId()));
                    return webSocketPrincipal;
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
            String payload = message.getPayload();

            if ("QUIT".equalsIgnoreCase(payload)) {
                logger.debug("Close Session by Client Request");
                session.close();
            } else {
                webSocketSessionListener.messageReceived(payload);
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
        WebSocketPrincipal trucoPrincipal = WebsocketHelper.getTrucoPrincipal(session);
        WebSocketSessionHandler sessionHandler = new WebSocketSessionHandler(trucoPrincipal.getUsername(), trucoPrincipal.getUsername(), session);
        trucoPrincipal.setHandler(sessionHandler);
        String query = session.getUri().getQuery();
        String message = "Hello! " + trucoPrincipal.getUsername() + " is connected [" + query + "]";
        {
            Map map = new LinkedHashMap<>();
            map.put("message", message);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(map)));
        }
        // Remove others connections
        //Iterator it = map.entrySet().iterator();
        Iterator<Map.Entry<String, WebSocketSessionHandler>> it = sessions.entrySet().iterator();
        while (it.hasNext()) {
            WebSocketSessionHandler s = it.next().getValue();
            if (s.getUsername().equals(trucoPrincipal.getUsername())) {
                if (s.isOpen()) {
                    // Disconnect
                    logger.debug("Disconect because is already connected");
                    Map goodBye = new LinkedHashMap<>();
                    s.setFired(true);
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
        //super.afterConnectionEstablished(session);
        addSession(sessionHandler);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSessionHandler sessionHandler = sessions.get(session.getId());
        if (!sessionHandler.isFired()) {
            String username = sessionHandler.getUsername();
            logger.debug("Users was disconnected [" + username + "]");
            Map map = new LinkedHashMap();
            map.put("username", username);
        }
        removeSession(sessionHandler);
    }

    public void addSession(WebSocketSessionHandler handler) {
        count++;
        logger.debug("Adding Session [" + handler + "][" + count + "]");
        sessions.put(handler.getId(), handler);
        webSocketSessionListener.afterConnectionEstablished(handler);

    }

    //
    public void removeSession(WebSocketSessionHandler session) {
        count--;
        logger.debug("Removing Session [" + session + "][" + count + "]");
        sessions.remove(session.getId());
        webSocketSessionListener.sessionTerminated(session);

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

    public void addWebSessionSocketListener(WebSocketSessionListener webSocketSessionListener) {
        this.webSocketSessionListener = webSocketSessionListener;
    }

    public void sendCommandResponse(CommandResponse commandResponse) throws IOException {
        SecurityContext sc = SecurityContextHolder.getContext();
        WebSocketPrincipal webSocketPrincipal = ((WebSocketPrincipal) sc.getAuthentication().getPrincipal());
        webSocketPrincipal.getHandler().sendMessage(new TextMessage(objectMapper.writeValueAsString(commandResponse)));
    }

    public void addToChannel(ChannelHolder channelHolder, String username) {
        logger.info("Adding User [" + username + "]  - Sessions: (" + sessions + ")");
        sessions.values().stream().parallel().forEach(s -> {
            if (username.equalsIgnoreCase(s.getUsername())) {
                channelHolder.addToChannel(s);
            }
        });
    }
}
