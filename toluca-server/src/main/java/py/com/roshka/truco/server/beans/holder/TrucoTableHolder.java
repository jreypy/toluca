package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.api.TrucoUser;

import java.util.*;

public class TrucoTableHolder extends TrucoRoomTableDescriptor {
    private TrucoRoomTable table;
    private Set<TrucoUser> users = new HashSet<>();


    private TrucoGameHolder trucoGameHolder = null;
    private ObjectMapper objectMapper;

    public TrucoTableHolder(TrucoRoomTable table, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        super(table);
        if (table.getOwner() == null)
            throw new IllegalArgumentException("TrucoUser owner is required");
        this.table = table;
        this.objectMapper = objectMapper;
        this.users.add(table.getOwner());

        this.trucoGameHolder = new TrucoGameHolder(this, objectMapper, rabbitTemplate);
    }

    public TrucoRoomTable getTable() {
        return table;
    }

    public void setTable(TrucoRoomTable table) {
        this.table = table;
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
        users.add(trucoUser);
        return trucoUser;
    }

    public void play(TrucoGamePlay trucoGamePlay) {
        trucoGameHolder.play(trucoGamePlay);
    }

    public void joinUser(TrucoUser user) {
        this.users.add(user);
    }

    public TrucoGameHolder getTrucoGameHolder() {
        return trucoGameHolder;
    }

    public void setTrucoGameHolder(TrucoGameHolder trucoGameHolder) {
        this.trucoGameHolder = trucoGameHolder;
    }


}
