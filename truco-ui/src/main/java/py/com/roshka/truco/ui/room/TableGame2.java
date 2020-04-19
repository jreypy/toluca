package py.com.roshka.truco.ui.room;

import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.edu.uca.fcyt.toluca.guinicio.TableGame;
import py.edu.uca.fcyt.toluca.guinicio.TableModelGame;

public class TableGame2 extends TableGame {


    TableModelGame tableModel;

    @Override
    public TableModelGame getTableModelGame() {
        if (tableModel == null){
            tableModel = new TableModelGame2();
        }
        return tableModel;
    }

    public void insertarFila(String id) {
        //System.out.println(getClass().getName()+" tableNumber"+table.getTableNumber());
        getTableModelGame().insertRow(new RowGame2(id, Integer.parseInt(id)));
    }


}
