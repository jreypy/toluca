package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TableRankingRender implements TableCellRenderer {

    private HashMap imagenes;

    private Color colorBack;

    private Color colorSelected;

    public TableRankingRender(HashMap imagenes, Color colorBack, Color selected) {
        this.imagenes = imagenes;
        this.colorBack = colorBack;
        this.colorSelected = selected;
    }

    public Component getTableCellRendererComponent(JTable jTable, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (value != null) {
            JLabel l = new JLabel(value.toString());
            RowRanking rowRanking = (RowRanking) value;
            /*
             * JPanel panel=new JPanel(); panel.setLayout(new
             * FlowLayout(FlowLayout.LEFT)); panel.setBackground(colorBack);
             * panel.setOpaque(true); if(isSelected)
             * panel.setBackground(colorSelected);
             */

            if (col == 0) {

                //          siguiendo con los principios de la PP
                /*if (jTable instanceof TableRanking
                 && ((TableRanking) jTable).getTable() != null
                 && !((TableRanking) jTable).getTable().isInside(
                 rowRanking.getUser())) {
                 //                TableRankingRender.setCellVisible(jTable, row, col);
                 makeInvisible(l);
                 } else {*/

                ImageIcon icon = (ImageIcon) imagenes.get(rowRanking
                        .getRankingStatus());
                l = new JLabel(icon);
                l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                l.setHorizontalAlignment(JLabel.LEFT);
                l.setText(rowRanking.getUser());
                l.setToolTipText(rowRanking.getUser() + ": "
                        + rowRanking.getRanking());
                //			panel.add(l);

                //            }

            } else {
                //          siguiendo con los principios de la PP
                /*if (jTable instanceof TableRanking
                 && ((TableRanking) jTable).getTable() != null
                 && !((TableRanking) jTable).getTable().isInside(
                 rowRanking.getUser())) {
                 //                TableRankingRender.setCellVisible(jTable, row, col);
                 makeInvisible(l);
                 } else*/
                l = new JLabel(value.toString());

            }

            return l;
        } else
        {
            return new JLabel("Era nulo :(");
        }
    }

    /**
     * @param l
     */
    private void makeInvisible(JLabel l) {
        Dimension nulo = new Dimension(10,10);
        l.setPreferredSize(nulo);
        l.setMaximumSize(nulo);
        l.setMinimumSize(nulo);
    }
    
}