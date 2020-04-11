package py.com.roshka.truco.server.beans.holder;

import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoUser;

import java.util.*;

public class TrucoTableHolder {
    private TrucoRoomTable table;
    private Set<TrucoUser> users = new HashSet<>();
    private Map<Integer, TrucoUser> positions = new LinkedHashMap<>();

    public TrucoTableHolder(TrucoRoomTable table) {
        if (table.getOwner() == null)
            throw new IllegalArgumentException("TrucoUser owner is required");
        this.table = table;
        this.users.add(table.getOwner());
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
        positions.put(index, trucoUser);
        users.add(trucoUser);
        return trucoUser;
    }

    public void joinUser(TrucoUser user) {
        this.users.add(user);
    }
}
