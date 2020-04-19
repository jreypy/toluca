package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.toluca.guinicio.PanelComandos;

import java.awt.event.ActionEvent;

public class PanelComandos2 extends PanelComandos {
    RoomHandler roomHandler;

    public PanelComandos2(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }



    protected void botonUnirseActionPerformed(ActionEvent e) {
        TableGame2 tableGame2 = roomHandler.getTableGame2();
        String tableId = (String) (tableGame2.getTableModelGame().getValueAt(tableGame2.getSelectedRow(), 0));
        roomClient.joinTableRequest(Integer.parseInt(tableId));
    }

}
