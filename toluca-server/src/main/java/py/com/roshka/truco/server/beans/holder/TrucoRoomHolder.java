package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.*;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TrucoRoomHolder extends TrucoRoomDescriptor {
    private TrucoRoom target;
    private Map<String, TrucoTableHolder> tableDescriptorList = new LinkedHashMap<>();

    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    public TrucoRoomHolder(TrucoRoom trucoRoom, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        super(trucoRoom);
        this.target = trucoRoom;

        this.target.setTables(new HashSet<>());
        this.target.setUsers(new HashSet<>());

        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public TrucoRoom getTrucoRoom() {
        return target;
    }

    public void setTrucoRoom(TrucoRoom trucoRoom) {
        this.target = trucoRoom;
    }

    public TrucoRoomTable addTable(String tableId, TrucoUser user, TrucoRoomTable trucoRoomTable) {
        // Check Permisions and size limit
        trucoRoomTable.setId(tableId);
        trucoRoomTable.setOwner(user);
        trucoRoomTable.setRoomId(getId());
        tableDescriptorList.put(tableId, new TrucoTableHolder(trucoRoomTable, objectMapper, rabbitTemplate));
        getTables().add(trucoRoomTable);
        return trucoRoomTable;
    }


    public TrucoTableHolder getTrucoTableHolder(String tableId) {
        TrucoTableHolder trucoTableHolder = tableDescriptorList.get(tableId);
        if (trucoTableHolder == null)
            throw new IllegalArgumentException("Truco Table not found [" + tableId + "]");
        return trucoTableHolder;
    }

    @Override
    public String getId() {
        return target.getId();
    }


    @Override
    public Set<TrucoRoomUser> getUsers() {
        return target.getUsers();
    }

    @Override
    public void setUsers(Set<TrucoRoomUser> users) {
        throw new IllegalArgumentException("Users cannot be setted here");
    }

    @Override
    public Set<TrucoRoomTableDescriptor> getTables() {
        return target.getTables();
    }

    @Override
    public void setTables(Set<TrucoRoomTableDescriptor> tables) {
        throw new IllegalArgumentException("Tables cannot be setted here");
    }
}
