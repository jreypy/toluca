package py.com.roshka.toluca.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.truco.api.TrucoRoomUser;
import py.com.roshka.truco.api.TrucoUser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class WebSocketHandler extends WebSocketSessionManager {
    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);


    private Map<String, TrucoRoomHandler> rooms = new LinkedHashMap<>();


    protected CommandProcessor commandProcessor;

    public WebSocketHandler(ObjectMapper objectMapper, CommandProcessor commandProcessor) {
        super(objectMapper);
        this.commandProcessor = commandProcessor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        addSession(session);
        super.afterConnectionEstablished(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        removeSession(session);
        super.afterConnectionClosed(session, status);

    }

    public void sendRoomEvent(String roomId, TrucoUser trucoUser, Event event) {
        TrucoRoomHandler trucoRoomHandler = getTrucoRoomHandler(roomId);
        //
        sessions.values().stream().filter(s -> {
            return s.getAttributes().get("username").equals(trucoUser.getUsername());
        }).map(s -> {
            logger.debug("Add User to Room Listener [" + roomId + "]");
            trucoRoomHandler.addSession(s);
            return s;
        }).count();

        trucoRoomHandler.sendEvent(event);
    }

    public void sendRoomEvent(String roomId, Event event) {
        TrucoRoomHandler trucoRoomHandler = getTrucoRoomHandler(roomId);
        trucoRoomHandler.sendEvent(event);
    }


    public void addListeners(String roomId, TrucoRoomUser... users) {
        TrucoRoomHandler trucoRoomHandler = getTrucoRoomHandler(roomId);

    }

    public TrucoRoomHandler getTrucoRoomHandler(String roomId) {
        TrucoRoomHandler trucoRoomHandler = rooms.get(roomId);
        if (trucoRoomHandler != null) {
            return trucoRoomHandler;
        }
        synchronized (rooms) {
            trucoRoomHandler = rooms.get(roomId);
            if (trucoRoomHandler == null) {
                trucoRoomHandler = new TrucoRoomHandler(objectMapper, roomId);
                rooms.put(roomId, trucoRoomHandler);
            }
        }
        return trucoRoomHandler;
    }


    class TrucoRoomHandler extends WebSocketSessionManager {
        private String id;
        private Map<String, WebSocketSession> sessions = new HashMap<>();

        public TrucoRoomHandler(ObjectMapper objectMapper, String id) {
            super(objectMapper);
            this.id = id;
        }


    }

}
