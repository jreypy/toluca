/* SimpleObjectRenderer.java
 * Created on Jan 7, 2005
 *
 * Last modified: $Date: 2005/01/07 20:32:01 $
 * @version $Revision: 1.1 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author afeltes
 *  
 */

public class SimpleObjectRenderer implements TableCellRenderer {

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel ret = new JLabel(value.toString());
        if(isSelected)
        {
            ret.setBackground(table.getSelectionBackground());
            ret.setOpaque(true);
        }
        ret.setFont(ret.getFont().deriveFont(Font.PLAIN));
        ret.setToolTipText(value.toString());
        return ret;
    }

}