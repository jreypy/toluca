package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.toluca.guinicio.RowGame;
import py.edu.uca.fcyt.toluca.guinicio.TableGame;
import py.edu.uca.fcyt.toluca.guinicio.TableModelGame;
import py.edu.uca.fcyt.toluca.table.Table;

import javax.swing.table.TableModel;

public class TableGame2 extends TableGame {

    public final static TableGame2 instance = new TableGame2();


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
