/*
 * BasePopupMenu.java
 *
 * Created on March 27, 2003, 10:15 PM
 * Extension del JPopupMenu que muestra siempre el menu completo dentro de
 * la pantalla disponible del usuario
 */

package py.edu.uca.fcyt.toluca.table;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.JComponent;

/**
 *
 * @author  Owner
 */
class BasePopupMenu extends javax.swing.JPopupMenu 
{
    
    public BasePopupMenu() {
        super();
    }
    public BasePopupMenu(String s) {
        super(s);
    }
    
    public void show(JComponent who,int evtX,int evtY,boolean b) {
        int popW=(int)getPreferredSize().getWidth();
        int popH=(int)getPreferredSize().getHeight();
        
        int whoAbsX=(int)who.getLocationOnScreen().getX();
        int whoAbsY=(int)who.getLocationOnScreen().getY();
        
        int screenW=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenH=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        
        int farestX=whoAbsX+evtX+popW;
        int farestY=whoAbsY+evtY+popH;
        
        if(farestX>screenW) evtX=evtX-popW;
        if(farestY>screenH) evtY=evtY-popH;
        
        super.show(who,evtX,evtY);
    }
    
    public void show(Component who,int evtX,int evtY) {
        int popW=(int)getPreferredSize().getWidth();
        int popH=(int)getPreferredSize().getHeight();
        
        int whoAbsX=(int)who.getLocationOnScreen().getX();
        int whoAbsY=(int)who.getLocationOnScreen().getY();
        
        int screenW=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int screenH=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        
        int farestX=whoAbsX+evtX+popW;
        int farestY=whoAbsY+evtY+popH;
        
        if(farestX>screenW) evtX=evtX-popW;
        if(farestY>screenH) evtY=evtY-popH;
        
        super.show(who,evtX,evtY);
    }
    
}
