package py.com.roshka.truco.server.beans.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoUser;

import java.util.LinkedHashMap;
import java.util.Map;

public class TrucoRoomHolder {
    private TrucoRoom trucoRoom;
    private Map<String, TrucoTableHolder> tables = new LinkedHashMap<>();
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    public TrucoRoomHolder(TrucoRoom trucoRoom, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.trucoRoom = trucoRoom;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public TrucoRoom getTrucoRoom() {
        return trucoRoom;
    }

    public void setTrucoRoom(TrucoRoom trucoRoom) {
        this.trucoRoom = trucoRoom;
    }

    public TrucoRoomTable addTable(String tableId, TrucoUser user, TrucoRoomTable trucoRoomTable) {
        // Check Permisions and size limit
        trucoRoomTable.setId(tableId);
        trucoRoomTable.setOwner(user);
        trucoRoomTable.setRoomId(trucoRoom.getId());
        tables.put(tableId, new TrucoTableHolder(trucoRoomTable, objectMapper, rabbitTemplate));
        return trucoRoomTable;
    }

    public TrucoTableHolder getTrucoTableHolder(String tableId) {
        TrucoTableHolder trucoTableHolder = tables.get(tableId);
        if (trucoTableHolder == null)
            throw new IllegalArgumentException("Truco Table not found [" + tableId + "]");
        return trucoTableHolder;
    }
}
