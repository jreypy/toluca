/* ButtonPlayAction.java
 * Created on Feb 14, 2005
 *
 * Last modified: $Date: 2005/02/15 10:55:51 $
 * @version $Revision: 1.1 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.table;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * 
 * @author afeltes
 *
 */
public class ButtonPlayAction extends JButton implements ActionListener {

    private byte action;
    private PTableListener ptListener;
    Font df = new Font("Dialog", java.awt.Font.PLAIN, 10);

    /**
     * @param table
     * @param play
     * @param name
     */
    public ButtonPlayAction(PTableListener table, byte play, String name) {
        //TODO la verdad, que salvo las jugadas que dependen de un Nro.
        //TODO cada jugada puede ser creada estáticamente nomás para ahorrar memoria
        super(name);
        setToolTipText(name);
        setFont(df);
    	this.action = play;
        this.ptListener = table;
        addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent actionEvent) 
    {
    	ptListener.say(action);
    }
    

}
