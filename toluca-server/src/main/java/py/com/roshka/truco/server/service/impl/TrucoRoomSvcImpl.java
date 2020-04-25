package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.beans.holder.TrucoRoomHolder;
import py.com.roshka.truco.server.beans.holder.TrucoTableHolder;
import py.com.roshka.truco.server.service.AMQPSender;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {


    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TrucoRoomSvcImpl.class);

    ObjectMapper objectMapper;
    private TrucoUserService trucoUserService;
    Map<String, TrucoRoomHolder> roomsHolder = new LinkedHashMap<>();
    Map<String, TrucoRoom> rooms = new LinkedHashMap<>();
    int _roomId = 0;
    int _tableId = 0;
    final AMQPSender amqpSender;

    public TrucoRoomSvcImpl(ObjectMapper objectMapper, TrucoUserService trucoUserService, AMQPSender amqpSender) {

        this.objectMapper = objectMapper;
        this.trucoUserService = trucoUserService;
        this.amqpSender = amqpSender;
        TrucoRoom trucoRoom = new TrucoRoom();
        trucoRoom.setId(Integer.toString(++_roomId));
        trucoRoom.setName("Principal");
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom, amqpSender, objectMapper));
    }


    @Scheduled(cron = "0 0/5 * * * *")
    protected void cleanTables() {
        TrucoUser trucoUser = new TrucoUser("null", "** Admin **");
        logger.info("Clean Tables!!");
        for (TrucoRoomHolder trucoRoomHolder : roomsHolder.values()) {
            for (TrucoRoomTableDescriptor roomTable : new HashSet<>(trucoRoomHolder.getTables())) {
                TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(roomTable.getId());
                if (System.currentTimeMillis() - trucoTableHolder.getUpdated() > 600_000) {
                    // 30 seconds
                    trucoRoomHolder.removeTable(roomTable.getId(), null);
                    TrucoRoomEvent destroyTableEvent = TrucoRoomEvent.builder(Event.ROOM_TABLE_DESTROYED).user(trucoUser).room(trucoRoomHolder.descriptor()).table(trucoTableHolder.descriptor()).build();
                    amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + trucoRoomHolder.getId(), destroyTableEvent);
                }
            }
        }
    }

    @Override
    public TrucoRoomEvent findAllRooms() {
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOMS_FOUND).user(trucoUserService.getTrucoUser()).rooms(rooms.values()).build();
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent findRoomById(String roomId) {
        TrucoRoomHolder trucoRoom = getTrucoRoomHolder(roomId);
        TrucoRoomDescriptor descriptor = trucoRoom.descriptor();

        descriptor.setUsers(trucoRoom.getUsers());

        descriptor.setTables(trucoRoom.getTables().stream().map(s -> {
            TrucoRoomTableDescriptor t = s.descriptor();
            t.setOwner(s.getOwner());
            t.setUsers(s.getUsers());
            t.setPositions(s.getPositions());
            if (t.getPositions() == null) {
                t.setPositions(new TrucoUser[TrucoRoomTable.TABLE_SIZE]);
            }
            if (t.getUsers() == null) {
                t.setUsers(new HashSet<>());
            }
            return t;

        }).collect(Collectors.toSet()));

        if (trucoRoom == null)
            throw new IllegalArgumentException("Room not found [" + roomId + "]");

        return TrucoRoomEvent.builder(Event.ROOM_FOUND).message("Truco Room [" + roomId + " was found").user(trucoUserService.getTrucoUser()).room(descriptor).build();
    }

    public TrucoRoomEvent create(TrucoRoom trucoRoom) {
        trucoRoom.setId(Integer.toString(++_roomId));
        this.rooms.put(trucoRoom.getId(), trucoRoom);
        this.roomsHolder.put(trucoRoom.getId(), new TrucoRoomHolder(trucoRoom, amqpSender, objectMapper));
        // Notify was created
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_CREATED).message("Truco Room [" + trucoRoom.getId() + " was created").user(trucoUserService.getTrucoUser()).room(trucoRoom).build();
        amqpSender.convertAndSend(trucoRoomEvent);
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent joinRoom(String roomId) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] joining to room [" + roomId + "]");
        TrucoRoom trucoRoom = getTrucoRoomHolder(roomId).getTrucoRoom();
        trucoRoom.getUsers().add(new TrucoRoomUser(user, true));
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_USER_JOINED).message("User joined to the Room [" + roomId + "]").user(user).room(trucoRoom).build();
        amqpSender.joinToChannel(AMQPSenderImpl.CHANNEL_ROOM_ID + roomId, user, trucoRoomEvent, AMQPSenderImpl.CHANNEL_ROOM_ID + roomId);
        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomTableEvent setTablePosition(String roomId, String tableId, Integer index) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "]  wants to sit at [" + roomId + "][" + index + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);

        TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(tableId);
        trucoTableHolder.sitDownPlayer(user, index);
        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
        trucoRoomTableEvent.setEventName(Event.TABLE_POSITION_SETTED);
        trucoRoomTableEvent.setMessage("User sat down to the Table [" + roomId + "][" + index + "]");
        trucoRoomTableEvent.setUser(user);
        trucoRoomTableEvent.setRoomId(roomId);
        trucoRoomTableEvent.setTableId(tableId);
        trucoRoomTableEvent.setPosition(index);
        TrucoRoomTableDescriptor tableDescriptor = trucoTableHolder.descriptor();
        tableDescriptor.setPositions(trucoTableHolder.getPositions());
        trucoRoomTableEvent.setTable(tableDescriptor);
        amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + roomId, trucoRoomTableEvent);
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
        amqpSender.joinToChannel(AMQPSenderImpl.CHANNEL_ROOM_ID + roomId, user, trucoRoomEvent, AMQPSenderImpl.CHANNEL_ROOM_ID + roomId + AMQPSenderImpl.CHANNEL_TABLE_ID + tableId);
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent joinRoomTable(String roomId, String tableId) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] is joining to table [" + roomId + "].[" + tableId + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(tableId);
        trucoRoomHolder.getTrucoTableHolder(tableId).joinUser(user);
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.ROOM_TABLE_USER_JOINED).user(user).room(trucoRoomHolder.descriptor()).table(trucoTableHolder.descriptor()).build();

        // Add Positions in the Table
        trucoRoomEvent.getTable().setPositions(trucoTableHolder.getPositions());

        amqpSender.joinToChannel(AMQPSenderImpl.CHANNEL_ROOM_ID + roomId, user, trucoRoomEvent, AMQPSenderImpl.CHANNEL_ROOM_ID + roomId + AMQPSenderImpl.CHANNEL_TABLE_ID + tableId);

        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return trucoRoomEvent;
    }

    @Override
    public TrucoRoomEvent leaveRoomTable(String roomId, String tableId) {
        TrucoUser user = trucoUserService.getTrucoUser();
        logger.debug("User [" + user.getUsername() + "] is leaving the table [" + roomId + "].[" + tableId + "]");
        TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(roomId);
        logger.debug("User [" + user.getUsername() + "] joined to the room [" + roomId + "]");
        return leaveTable(trucoRoomHolder, user, tableId);
    }

    private TrucoRoomEvent leaveTable(TrucoRoomHolder trucoRoomHolder, TrucoUser user, String tableId) {
        TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(tableId);
        TrucoRoomEvent trucoRoomEvent = TrucoRoomEvent.builder(Event.USER_LEFT_TABLE).user(user).room(trucoRoomHolder.descriptor()).table(trucoTableHolder.descriptor()).build();
        trucoRoomEvent.getTable().setPositions(trucoTableHolder.getPositions());

        if (trucoRoomHolder.getTrucoTableHolder(tableId).leaveUser(user)) {
            // Destroy Table
            trucoRoomHolder.removeTable(tableId, user);
            TrucoRoomEvent destroyTableEvent = TrucoRoomEvent.builder(Event.ROOM_TABLE_DESTROYED).user(user).room(trucoRoomHolder.descriptor()).table(trucoTableHolder.descriptor()).build();
            amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + trucoRoomHolder.getId(), destroyTableEvent);
        } else {
            amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + trucoRoomHolder.getId(), trucoRoomEvent);
        }

        return trucoRoomEvent;

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
                TrucoRoomHolder trucoRoomHolder = getTrucoRoomHolder(room.getId());

                // notify
                room.getUsers().remove(trucoRoomUser);
                TrucoRoomEvent trucoRoomEvent = new TrucoRoomEvent();
                trucoRoomEvent.setEventName(Event.ROOM_USER_LEFT);
                trucoRoomEvent.setMessage("User left the Room [" + room.getId() + "]");
                trucoRoomEvent.setUser(trucoRoomUser.getUser());
                trucoRoomEvent.setRoom(room);
                //amqpSender.convertAndSend(TRUCO_ROOM_EVENT, ROOM_LOGOUT_ROUTING_KEY, new RabbitResponse(Event.ROOM_USER_JOINED, trucoRoomEvent.getClass().getCanonicalName(), trucoRoomEvent));


                TrucoRoomEvent trucoRoomTableEvent = new TrucoRoomEvent();
                trucoRoomTableEvent.setEventName(Event.ROOM_USER_LEFT);
                trucoRoomTableEvent.setMessage("User left the room [" + room + "]");
                trucoRoomTableEvent.setUser(trucoRoomUser.getUser());
                trucoRoomTableEvent.setRoom(room.descriptor());
                Iterator<TrucoRoomTableDescriptor> tables = new HashSet<>(room.getTables()).iterator();

                while (tables.hasNext()) {
                    TrucoRoomTableDescriptor table = tables.next();
                    TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(table.getId());
                    if (trucoTableHolder.getUsers().contains(trucoRoomUser.getUser())) {
                        try {
                            leaveTable(trucoRoomHolder, trucoRoomUser.getUser(), table.getId());
                        } catch (Exception e) {
                            logger.warn(e.getMessage(), e);
                        }
                    }
                }

                amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + room.getId(), trucoRoomTableEvent);
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
