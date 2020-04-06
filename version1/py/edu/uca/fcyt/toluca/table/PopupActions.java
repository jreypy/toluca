/*
 * ActEnvido.java
 *
 * Created on 27 de junio de 2003, 22:28
 */

package py.edu.uca.fcyt.toluca.table;
import java.awt.event.ActionEvent;

/**
 *
 * @author  ale
 */
class PopupActions extends javax.swing.AbstractAction 
{
    protected PTableListener ptListener;
    byte action;
    
    /** Creates a new instance of PopupActions */
    public PopupActions
    (
    	PTableListener ptListener, byte action, String title
    ) 
    {
    	super(title);
    	this.action = action;
        this.ptListener = ptListener;
    }
    
    public void actionPerformed(ActionEvent actionEvent) 
    {
    	ptListener.say(action);
    }
}
