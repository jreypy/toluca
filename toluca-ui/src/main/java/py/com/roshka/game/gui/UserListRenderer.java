/* UserListRenderer.java
 * Created on May 6, 2005
 *
 * Last modified: $Date: 2005/05/06 05:34:19 $
 * @version $Revision: 1.1 $ 
 * @author afeltes
 */
package py.com.roshka.game.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.guinicio.RowRanking;

/**
 * 
 * @author afeltes
 *
 */

public class UserListRenderer implements ListCellRenderer {
    
    private HashMap colores;

    /**
     * @param coloresRanking
     */
    public UserListRenderer(HashMap coloresRanking) {
        colores = coloresRanking;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        TrucoPlayer tp = null;
        JLabel jl = new JLabel(); 
        if(value != null)
        {
            jl = new JLabel(value.toString());
            tp = (TrucoPlayer) value;
            ImageIcon icon = (ImageIcon) colores.get(RowRanking
                    .getRankingStatus(tp.getRating()));
            jl.setIcon(icon);
            jl.setToolTipText(tp.getName() + ": "
                    + tp.getRating());
        }
        jl.setOpaque(true);
        jl.setFont(jl.getFont().deriveFont(Font.PLAIN));
        if(isSelected)
        {
            jl.setForeground(Color.WHITE);
            jl.setBackground(Color.BLACK);
        } else
        {
            jl.setForeground(Color.BLACK);
            jl.setBackground(Color.WHITE);            
        }
        return jl;
    }

    public HashMap getColores() {
        return colores;
    }
    public void setColores(HashMap colores) {
        this.colores = colores;
    }
}
