package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.toluca.guinicio.PanelComandos;

import java.awt.event.ActionEvent;

public class PanelComandos2 extends PanelComandos {
    public final static PanelComandos2 instance = new PanelComandos2();

    public PanelComandos2() {
    }

    protected void botonUnirseActionPerformed(ActionEvent e) {
        Object data = TableGame2.instance.getTableModelGame().getValueAt(TableGame2.instance.getSelectedRow(), 0);
        System.out.println(data);
        // TODO solitar unirse
    }

}
