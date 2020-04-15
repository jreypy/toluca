package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.beans.holder.TrucoRoomHolder;
import py.com.roshka.truco.server.beans.holder.TrucoTableHolder;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {
    static String TRUCO_ROOM_EVENT = "truco_room_event";

    static String ROOM_ALL_ROUTING_KEY = "room_all";
    static String ROOM_ID_ROUTING_KEY = "room_id";
    static String ROOM_JOIN_ROUTING_KEY = "room_join";
    static String ROOM_LOGOUT_ROUTING_KEY = "room_logout";


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
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom, objectMapper, rabbitTemplate));
    }


    @Override
    public TrucoRoomEvent findAllRooms() {
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_FOUND).user(trucoUserService.getTrucoUser()).rooms(rooms.values()).build();
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent findRoomById(String roomId) {
        TrucoRoom trucoRoom = rooms.get(roomId);

        if (trucoRoom == null)
            throw new IllegalArgumentException("Room not found [" + roomId + "]");

        return TrucoRoomEvent.builder(Event.ROOM_FOUND).message("Truco Room [" + roomId + " was found").user(trucoUserService.getTrucoUser()).room(trucoRoom).build();
    }

    public TrucoRoomEvent create(TrucoRoom trucoRoom) {
        trucoRoom.setId(Integer.toString(++_roomId));
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom, objectMapper, rabbitTemplate));
        // Notify was created
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_CREATED).message("Truco Room [" + trucoRoom.getId() + " was created").user(trucoUserService.getTrucoUser()).room(trucoRoom).build();
        convertAndSend(trucoRoomEvent);
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent joinRoom(String roomId) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoom trucoRoom = getTrucoRoomHolder(roomId).getTrucoRoom();
        trucoRoom.getUsers().add(new TrucoRoomUser(user, true));
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_USER_JOINED).message("User joined to the Room [" + roomId + "]").user(user).room(trucoRoom).build();

        convertAndSend(roomId, user, trucoRoomEvent);
        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return trucoRoomEvent;

    }

    @Override
    public TrucoRoomTableEvent setTablePosition(String roomId, String tableId, Integer index) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "]  wants to sit at [" + roomId + "][" + index + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);

        trucoRoomHolder.getTrucoTableHolder(tableId).sitDownPlayer(user, index);
        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
        trucoRoomTableEvent.setEventName(Event.TABLE_POSITION_SETTED);
        trucoRoomTableEvent.setMessage("User sat down to the Table [" + roomId + "][" + index + "]");
        trucoRoomTableEvent.setUser(user);
        trucoRoomTableEvent.setRoomId(roomId);
        trucoRoomTableEvent.setTableId(tableId);
        trucoRoomTableEvent.setChair(index);
        convertAndSend(roomId, trucoRoomTableEvent);
        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return trucoRoomTableEvent;
    }

    public TrucoRoomEvent delete(TrucoRoom trucoRoom) {
        return null;
    }

    public TrucoRoomEvent addTable(String roomId, TrucoRoomTable trucoRoomTable) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        final int tableId = ++_tableId;
        trucoRoomHolder.addTable(Integer.toString(tableId), user, trucoRoomTable);
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_TABLE_CREATED).table(trucoRoomTable).room(trucoRoomHolder.descriptor()).user(user).build();
        convertAndSend(roomId, trucoRoomEvent);
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent joinRoomTable(String roomId, String tableId) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(tableId);
        trucoRoomHolder.getTrucoTableHolder(tableId).joinUser(user);
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_TABLE_USER_JOINED).user(user).room(trucoRoomHolder.descriptor()).table(trucoTableHolder.descriptor()).build();
        // TODO JOIN TO TABLE LISTENER
//        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ID_ROUTING_KEY, new RabbitResponse(Event.TRUCO_ROOM_EVENT, roomId, trucoRoomEvent));
        convertAndSend(roomId, trucoRoomEvent);
        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return trucoRoomEvent;
    }

    private void convertAndSend(TrucoEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ALL_ROUTING_KEY, new RabbitResponse(Event.TRUCO_ROOM_EVENT, trucoEvent));
    }

    private void convertAndSend(String roomId, TrucoUser trucoUser, TrucoEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_JOIN_ROUTING_KEY, new JoinRabbitResponse(roomId, trucoUser, new RabbitResponse(Event.TRUCO_ROOM_EVENT, roomId, trucoEvent)));
    }

    private void convertAndSend(String roomId, TrucoEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ID_ROUTING_KEY, new RabbitResponse(Event.TRUCO_ROOM_EVENT, roomId, trucoEvent));
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
                rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_LOGOUT_ROUTING_KEY, new RabbitResponse(Event.ROOM_USER_JOINED, trucoRoomEvent.getClass().getCanonicalName(), trucoRoomEvent));
                logger.debug("User [" + trucoRoomUser.getUser().getUsername() + "] left the room [" + room.getId() + "]");
            }
        });
    }

    public TrucoRoomHolder getTrucoRoomHolder(String roomId) {
        TrucoRoomHolder trucoRoomHolder = roomsHolder.get(roomId);
        if (trucoRoomHolder != null) {
            return trucoRoomHolder;
        }
        logger.warn("Truco Room not found [" + roomId + "]");
        throw new IllegalArgumentException("TrucoRoom not found [" + roomId + "]");
    }
}
