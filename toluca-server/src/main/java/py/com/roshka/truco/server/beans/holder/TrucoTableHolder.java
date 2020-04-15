package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.api.TrucoUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrucoTableHolder extends TrucoRoomTableDescriptor {
    private TrucoRoomTable target;

    private TrucoGameHolder trucoGameHolder = null;
    private ObjectMapper objectMapper;

    public TrucoTableHolder(TrucoRoomTable table, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        super(table);
        if (table.getOwner() == null)
            throw new IllegalArgumentException("TrucoUser owner is required");
        this.target = table;
        this.objectMapper = objectMapper;
        this.target.setUsers(new HashSet<>());
        this.target.getUsers().add(table.getOwner());
        this.trucoGameHolder = new TrucoGameHolder(this, objectMapper, rabbitTemplate);
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
}
