package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.service.AMQPSender;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TrucoRoomHolder extends TrucoRoomDescriptor {
    private TrucoRoom target;
    private Map<String, TrucoTableHolder> tableDescriptorList = new LinkedHashMap<>();

    final private AMQPSender amqpSender;
    private ObjectMapper objectMapper;

    public TrucoRoomHolder(TrucoRoom trucoRoom, AMQPSender amqpSender, ObjectMapper objectMapper) {
        super(trucoRoom);
        this.target = trucoRoom;
        this.amqpSender = amqpSender;
        this.target.setTables(new HashSet<>());
        this.target.setUsers(new HashSet<>());
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
        tableDescriptorList.put(tableId, new TrucoTableHolder(trucoRoomTable, objectMapper, amqpSender));
        getTables().add(trucoRoomTable);
        return trucoRoomTable;
    }

    public boolean removeTable(String tableId, TrucoUser user) {
        TrucoTableHolder trucoTableHolder = tableDescriptorList.get(tableId);
        tableDescriptorList.remove(tableId);
        boolean value = getTables().remove(trucoTableHolder.getTable());
        return value;
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
