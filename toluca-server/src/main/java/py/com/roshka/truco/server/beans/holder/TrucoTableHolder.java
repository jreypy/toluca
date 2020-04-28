package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.service.AMQPSender;
import py.com.roshka.truco.server.service.impl.AMQPSenderImpl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TrucoTableHolder extends TrucoRoomTableDescriptor {
    Logger logger = LoggerFactory.getLogger(TrucoTableHolder.class);

    private TrucoRoomTable target;

    private TrucoGameHolder trucoGameHolder = null;
    private ObjectMapper objectMapper;
    final private AMQPSender amqpSender;

    long updated = System.currentTimeMillis();

    public TrucoTableHolder(TrucoRoomTable table, ObjectMapper objectMapper, AMQPSender amqpSender) {
        super(table);
        this.objectMapper = objectMapper;
        this.amqpSender = amqpSender;
        if (table.getOwner() == null)
            throw new IllegalArgumentException("TrucoUser owner is required");
        this.target = table;
        this.target.setUsers(new HashSet<>());
        this.target.getUsers().add(table.getOwner());
        this.trucoGameHolder = new TrucoGameHolder(this, amqpSender, objectMapper);
    }

    public TrucoRoomTable getTable() {
        return target;
    }

    public void setTable(TrucoRoomTable table) {
        this.target = table;
    }


    public TrucoUser sitDownPlayer(TrucoUser trucoUser, Integer index) {
        updated = System.currentTimeMillis();
        if (index != null && (index < 0 || index >= 6)) {
            throw new IllegalArgumentException("Position is invalid [" + index + "] [0-6]");
        }

        if (TrucoRoomTable.IN_PROGRESS.equalsIgnoreCase(getStatus())){
            throw new IllegalArgumentException("Status invalid to change the positions in the table");
        }

        TrucoUser[] positions = trucoGameHolder.getPositions();

        for (int i = 0; i < positions.length; i++) {
            if (positions[i] != null) {
                if (trucoUser.getId().equalsIgnoreCase(positions[i].getId())) {
                    // Position available again
                    positions[i] = null;
                }
            }

        }

        if (index != null) {
            trucoGameHolder.getPositions()[index] = trucoUser;
            getUsers().add(trucoUser);
            target.setPositions(trucoGameHolder.getPositions());
        }

        return trucoUser;
    }

    public void play(TrucoGamePlay trucoGamePlay) {
        updated = System.currentTimeMillis();
        trucoGameHolder.play(trucoGamePlay);
    }


    public boolean joinUser(TrucoUser user) {
        updated = System.currentTimeMillis();
        // Check If user is player
        getUsers().add(user);
        if (TrucoRoomTable.IN_PROGRESS.equalsIgnoreCase(target.getStatus())) {
            for (TrucoUser player : trucoGameHolder.getPositions()) {
                if (player != null && user.getUsername().equalsIgnoreCase(player.getId())) {
                    // Require reconnect
                    logger.debug("Requiere reconnect [" + player.getUsername() + "]");
                    return true;
                }
            }
        }
        return false;
    }

    public void reconnect(TrucoUser user) {
        trucoGameHolder.reconnect(user);

    }

    public boolean leaveUser(TrucoUser user) {
        if (TrucoRoomTable.IN_PROGRESS.equalsIgnoreCase(getStatus())) {
            throw new IllegalArgumentException("Game is in Progress");
        } else {
            sitDownPlayer(user, null);
        }
        getUsers().remove(user);

        if (target.getOwner().equals(user)) {
            return true;
        }
        return false;
    }

    @Override
    public Set<TrucoUser> getUsers() {
        return target.getUsers();
    }

    @Override
    public void setUsers(Set<TrucoUser> users) {
        target.setUsers(users);
    }

    public TrucoGameHolder getTrucoGameHolder() {
        return trucoGameHolder;
    }

    public void setTrucoGameHolder(TrucoGameHolder trucoGameHolder) {
        this.trucoGameHolder = trucoGameHolder;
    }


    @Override
    public TrucoRoomTableDescriptor descriptor() {
        return descriptor(target);
    }

    @Override
    public String getRoomId() {
        return target.getRoomId();
    }


    @Override
    public void setStatus(String status) {
        target.setStatus(status);
        try {
            TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
            trucoRoomTableEvent.setEventName(Event.ROOM_TABLE_STATUS_UPDATED);
            trucoRoomTableEvent.setMessage("Room Table Status [" + target.getId() + "] updated [" + target.getStatus() + "]");
            trucoRoomTableEvent.setTableId(target.getId());
            TrucoRoomTableDescriptor tableDescriptor = descriptor();
            tableDescriptor.setPositions(getPositions());
            trucoRoomTableEvent.setTable(tableDescriptor);
            amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + target.getRoomId(), trucoRoomTableEvent);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


    }

    @Override
    public String getStatus() {
        return target.getStatus();
    }


    @Override
    public void setPositions(TrucoUser[] positions) {
        target.setPositions(positions);
    }

    @Override
    public TrucoUser[] getPositions() {
        return target.getPositions();
    }


    public void startGame() {
        updated = System.currentTimeMillis();
        try {
            trucoGameHolder.startGame();
            target.setPositions(trucoGameHolder.getPositions());
            setStatus(TrucoRoomTable.IN_PROGRESS);
        } catch (Exception e) {
            // TODO Cannot be started
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrucoTableHolder that = (TrucoTableHolder) o;
        return Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), target);
    }

    public long getUpdated() {
        return updated;
    }
}
