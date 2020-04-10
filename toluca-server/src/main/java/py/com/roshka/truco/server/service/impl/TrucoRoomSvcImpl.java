package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.beans.holder.TrucoRoomHolder;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

import java.util.*;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {
    static String TRUCO_EVENT = "truco_event";
    static String TRUCO_ROOM_EVENT = "truco_room_event";
    static String ROOM_ROUTING_KEY = "room";

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TrucoRoomSvcImpl.class);

    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;
    private TrucoUserService trucoUserService;
    Map<String, TrucoRoomHolder> roomsHolder = new LinkedHashMap<>();
    Map<String, TrucoRoom> rooms = new LinkedHashMap<>();
    int _roomId = 0;
    int _tableId = 0;

    public TrucoRoomSvcImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, TrucoUserService trucoUserService) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.trucoUserService = trucoUserService;
        TrucoRoom trucoRoom = new TrucoRoom();
        trucoRoom.setId(Integer.toString(++_roomId));
        trucoRoom.setName("Principal");
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom));
    }


    @Override
    public List<TrucoRoom> findAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public TrucoRoom findRoomById(String roomId) {
        TrucoRoom trucoRoom = rooms.get(roomId);
        if (trucoRoom == null)
            throw new IllegalArgumentException("Room not found [" + roomId + "]");

        return trucoRoom;
    }

    public TrucoRoom create(TrucoRoom trucoRoom) {
        trucoRoom.setId(Integer.toString(++_roomId));
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom));
        // Notify was created
        rabbitTemplate.convertAndSend(TRUCO_EVENT, ROOM_ROUTING_KEY, new RabbitResponse(Event.ROOM_CREATED, trucoRoom.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoom, HashMap.class)));
        return trucoRoom;
    }

    @Override
    public TrucoRoomEvent joinRoom(String roomId, TrucoUser user) {
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoom trucoRoom = rooms.get(roomId);
        if (trucoRoom != null) {
            trucoRoom.getUsers().add(new TrucoRoomUser(user, true));
            TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
            trucoRoomEvent.setEventName(Event.ROOM_USER_JOINED);
            trucoRoomEvent.setMessage("User joined to the Room [" + roomId + "]");
            trucoRoomEvent.setUser(user);
            trucoRoomEvent.setRoom(trucoRoom);
            rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, roomId, new RabbitResponse(Event.ROOM_USER_JOINED, trucoRoomEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoomEvent, HashMap.class)));
            logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
            return trucoRoomEvent;
        } else {
            logger.warn("Truco Room not found [" + roomId + "]");
        }
        return null;
    }

    @Override
    public TrucoRoomTableEvent setTablePosition(String roomId, String tableId, Integer index) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "]  wants to sit at [" + roomId + "][" + index + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        if (trucoRoomHolder != null) {
            trucoRoomHolder.getTrucoTableHolder(tableId).sitDownPlayer(user, index);
            TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
            trucoRoomTableEvent.setEventName(Event.TABLE_POSITION_SETTED);
            trucoRoomTableEvent.setMessage("User sat down to the Table [" + roomId + "][" + index + "]");
            trucoRoomTableEvent.setUser(user);
            trucoRoomTableEvent.setRoomId(roomId);
            trucoRoomTableEvent.setChair(index);
            rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, roomId, new RabbitResponse(Event.TABLE_POSITION_SETTED, trucoRoomTableEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoomTableEvent, HashMap.class)));
            logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
            return trucoRoomTableEvent;
        } else {
            logger.warn("Truco Room not found [" + roomId + "]");
        }
        return null;
    }

    public TrucoRoom delete(TrucoRoom trucoRoom) {
        return null;
    }

    public TrucoRoomTable addTable(String roomId, TrucoRoomTable trucoRoomTable) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        final int tableId = ++_tableId;
        trucoRoomHolder.addTable(Integer.toString(tableId), user, trucoRoomTable);
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, roomId, new RabbitResponse(Event.ROOM_TABLE_CREATED, trucoRoomTable.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoomTable, HashMap.class)));
        return trucoRoomTable;
    }


    public TrucoRoomTable deleteTable(String roomId, String tableId) {
        return null;
    }

    public void addChair(String tableId, Chair chair) {

    }

    public void removeChair(String tableId, Integer chairPosition) {

    }

    @Override
    public void logout(String username) {
        TrucoRoomUser trucoRoomUser = new TrucoRoomUser(new TrucoUser(username, username), false);
        rooms.values().stream().parallel().forEach(room -> {
            if (room.getUsers().contains(trucoRoomUser)) {
                // notify
                room.getUsers().remove(trucoRoomUser);
                TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
                trucoRoomEvent.setEventName(Event.ROOM_USER_LEFT);
                trucoRoomEvent.setMessage("User left the Room [" + room.getId() + "]");
                trucoRoomEvent.setUser(trucoRoomUser.getUser());
                trucoRoomEvent.setRoom(room);
                rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, room.getId(), new RabbitResponse(Event.ROOM_USER_JOINED, trucoRoomEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoRoomEvent, HashMap.class)));
                logger.debug("User [" + trucoRoomUser.getUser().getUsername() + "] left the room [" + room.getId() + "]");
            }
        });
    }

    private TrucoRoomHolder getTrucoRoomHolder(String roomId) {
        TrucoRoomHolder trucoRoomHolder = roomsHolder.get(roomId);
        if (trucoRoomHolder != null) {
            return trucoRoomHolder;
        }
        logger.warn("Truco Room not found [" + roomId + "]");
        throw new IllegalArgumentException("TrucoRoom not found [" + roomId + "]");
    }
}
