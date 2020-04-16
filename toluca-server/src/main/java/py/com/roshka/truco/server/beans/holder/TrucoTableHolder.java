package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.server.service.AMQPSender;

import java.util.HashSet;
import java.util.Set;

public class TrucoTableHolder extends TrucoRoomTableDescriptor {
    private TrucoRoomTable target;

    private TrucoGameHolder trucoGameHolder = null;
    private ObjectMapper objectMapper;
    final private AMQPSender amqpSender;

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
        if (index < 0 || index >= 6)
            throw new IllegalArgumentException("Position is invalid [" + index + "] [0-6]");
        TrucoUser[] positions = trucoGameHolder.getPositions();

        for (int i = 0; i < positions.length; i++) {
            if (positions[i] != null) {
                if (trucoUser.getId().equalsIgnoreCase(positions[i].getId())) {
                    // Position available again
                    positions[i] = null;
                }
            }

        }
        trucoGameHolder.getPositions()[index] = trucoUser;
        getUsers().add(trucoUser);
        target.setPositions(trucoGameHolder.getPositions());

        return trucoUser;
    }

    public void play(TrucoGamePlay trucoGamePlay) {
        trucoGameHolder.play(trucoGamePlay);
    }

    public void joinUser(TrucoUser user) {
        getUsers().add(user);
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
}
