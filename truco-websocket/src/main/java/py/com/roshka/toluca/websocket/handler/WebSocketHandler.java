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

import java.util.Arrays;
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


        public TrucoRoomHandler(ObjectMapper objectMapper, String id) {
            super(objectMapper);
            this.id = id;
        }
    }

}
