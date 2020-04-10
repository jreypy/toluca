package py.com.roshka.truco.server.beans.holder;

import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrucoRoomHolder {
    private TrucoRoom trucoRoom;
    private Map<String, TrucoTableHolder> tables = new LinkedHashMap<>();

    public TrucoRoomHolder(TrucoRoom trucoRoom) {
        this.trucoRoom = trucoRoom;
    }

    public TrucoRoom getTrucoRoom() {
        return trucoRoom;
    }

    public void setTrucoRoom(TrucoRoom trucoRoom) {
        this.trucoRoom = trucoRoom;
    }

    public TrucoRoomTable addTable(String tableId, TrucoUser user, TrucoRoomTable trucoRoomTable) {
        // Check Permisions and size limit
        trucoRoomTable.setOwner(user);
        tables.put(tableId, new TrucoTableHolder(trucoRoomTable));
        return trucoRoomTable;
    }
}
