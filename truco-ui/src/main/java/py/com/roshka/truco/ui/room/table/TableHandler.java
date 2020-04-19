package py.com.roshka.truco.ui.room.table;

import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.ui.Toluca;
import py.com.roshka.truco.ui.room.game.TrucoGameClient2;
import py.com.roshka.truco.ui.table.Table2;

public class TableHandler {
    final TrucoGameClient2 game;
    final Table2 table;

    public TableHandler(TrucoRoomTableDescriptor tableDescriptor) {
        game = new TrucoGameClient2(tableDescriptor);
        table = game.getTable();
    }

    public void show() {
        game.show();
    }

    public boolean isInside(TrucoUser user) {
        return table.isInside(user.getUsername());
    }

    public void addPlayer(TrucoUser user) {
        table.addPlayer(Toluca.getTrucoPlayer(user));
    }

    public void sitPlayer(TrucoUser user, Integer position) {
        game.getTable().sitPlayer(Toluca.getTrucoPlayer(user), position);
    }

    public TrucoGameClient2 getGame() {
        return game;
    }
}
