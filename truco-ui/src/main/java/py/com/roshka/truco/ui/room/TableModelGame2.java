package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.toluca.guinicio.RowGame;
import py.edu.uca.fcyt.toluca.guinicio.TableModelGame;

public class TableModelGame2 extends TableModelGame {

    @Override
    public Object getValueAt(int row, int col) {
        RowGame2 data = (RowGame2) datos.get(row);
        if (col == 0) {
            return data.getId();
        }
        return super.getValueAt(row, col);
    }
}
