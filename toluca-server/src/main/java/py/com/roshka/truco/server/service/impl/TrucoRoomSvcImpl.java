package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

import java.util.*;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {

    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;
    List<TrucoRoom> rooms = new ArrayList<>();

    public TrucoRoomSvcImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        TrucoRoom trucoRoom = new TrucoRoom();
        trucoRoom.setId("principal");
        trucoRoom.setName("Principal");
        this.rooms.add(trucoRoom);
    }


    @Override
    public List<TrucoRoom> findAllRooms() {
        return rooms;
    }

    public TrucoRoom create(TrucoRoom trucoRoom) {
        trucoRoom.setId(UUID.randomUUID().toString());
        rooms.add(trucoRoom);
        // Notify was created
        rabbitTemplate.convertAndSend("truco_event", "room", new RabbitResponse(Event.ROOM_CREATED, trucoRoom.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoom, HashMap.class)));
        return trucoRoom;
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
