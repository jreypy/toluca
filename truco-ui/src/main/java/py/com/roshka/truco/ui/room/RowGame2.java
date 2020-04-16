package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.toluca.guinicio.RowGame;

public class RowGame2 extends RowGame {
    String id;

    public RowGame2(String id, int tableNumber) {
        super(tableNumber);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
