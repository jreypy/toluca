/*
 * TTruco.java
 *
 * Created on 8 de abril de 2002, 10:42 PM
 */

package py.edu.uca.fcyt.toluca.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *
 * @author  Julio Rey
 */
public class TTruco implements MouseListener{
    
    /** Creates a new instance of TTruco */
    TPlayer tp;
    byte type;
    public TTruco(TPlayer tp, byte type) {
        this.tp = tp;
        this.type = type;
    }
    public void 	mouseClicked(MouseEvent e){
        tp.cantar((byte)type);
    }
    public void 	mouseEntered(MouseEvent e){
    }
    public void 	mouseExited(MouseEvent e){
    }
    public void 	mousePressed(MouseEvent e){
    }
    public  void mouseReleased(MouseEvent e) {
    }
}
