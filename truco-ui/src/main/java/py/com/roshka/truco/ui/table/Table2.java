package py.com.roshka.truco.ui.table;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;

public class Table2 extends Table {
    private String id;

    public Table2(TrucoPlayer player, boolean host, int points) {
        super(player, host, points);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
