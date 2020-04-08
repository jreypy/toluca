package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

import java.util.*;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TrucoRoomSvcImpl.class);

    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;
    Map<String, TrucoRoom> rooms = new LinkedHashMap<>();
    int roomId = 0;

    public TrucoRoomSvcImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        TrucoRoom trucoRoom = new TrucoRoom();
        trucoRoom.setId(Integer.toString(++roomId));
        trucoRoom.setName("Principal");
        this.rooms.put(trucoRoom.getId(), trucoRoom);
    }


    @Override
    public List<TrucoRoom> findAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public TrucoRoom create(TrucoRoom trucoRoom) {
        trucoRoom.setId(Integer.toString(++roomId));
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        // Notify was created
        rabbitTemplate.convertAndSend("truco_event", "room", new RabbitResponse(Event.ROOM_CREATED, trucoRoom.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoom, HashMap.class)));
        return trucoRoom;
    }

    @Override
    public TrucoRoomEvent joinRoom(String roomId, TrucoUser user) {
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoom trucoRoom = rooms.get(roomId);
        if (trucoRoom != null) {
            // User added to the ROOM
            trucoRoom.getUsers().add(user);
            TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
            trucoRoomEvent.setEventName(Event.ROOM_USER_JOINED);
            trucoRoomEvent.setMessage("User joined to the Room [" + roomId + "]");
            trucoRoomEvent.setUser(user);
            rabbitTemplate.convertAndSend("truco_room_event", roomId, new RabbitResponse(Event.ROOM_CREATED, trucoRoom.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoom, HashMap.class)));
            logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
            return trucoRoomEvent;
        }
        return null;
    }

    public TrucoRoom delete(TrucoRoom trucoRoom) {
        return null;
    }

    public TrucoRoomTable addTable(String roomId, TrucoRoomTable trucoRoomTable) {
        return null;
    }

    public TrucoRoomTable deleteTable(String roomId, String tableId) {
        return null;
    }

    public void addChair(String tableId, Chair chair) {

    }

    public void removeChair(String tableId, Integer chairPosition) {

    }
}
