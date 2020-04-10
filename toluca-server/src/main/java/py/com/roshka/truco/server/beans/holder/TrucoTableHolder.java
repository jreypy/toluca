package py.com.roshka.truco.server.beans.holder;

import py.com.roshka.truco.api.TrucoRoomTable;

public class TrucoTableHolder {
    private TrucoRoomTable table;

    public TrucoTableHolder(TrucoRoomTable table) {
        this.table = table;
    }

    public TrucoRoomTable getTable() {
        return table;
    }

    public void setTable(TrucoRoomTable table) {
        this.table = table;
    }
}
