/* UserListSelection.java
 * Created on May 6, 2005
 *
 * Last modified: $Date: 2005/05/17 22:01:48 $
 * @version $Revision: 1.2 $ 
 * @author afeltes
 */
package py.com.roshka.game.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import py.edu.uca.fcyt.toluca.guinicio.TableRanking;

/**
 * 
 * @author afeltes
 *  
 */

public class UserListSelection extends JDialog {

    private javax.swing.JPanel jContentPane = null;

    private JTextField jTextField = null;

    private JScrollPane jScrollPane = null;

    private JList jLplayres = null;

    private JPanel jPbuttons = null;

    private JButton jBinvitar = null;

    private JButton jBcancelar = null;

    /**
     * This is the default constructor
     */
    public UserListSelection() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        setTitle("Lista de usuarios conectados");
        setModal(true);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJTextField(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJPbuttons(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setText("Seleccione el Jugador a invitar");
            jTextField.setEditable(false);
            jTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        }
        return jTextField;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBorder(new EtchedBorder());
            jScrollPane.setViewportView(getJLplayres());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    public JList getJLplayres() {
        if (jLplayres == null) {
            jLplayres = new JList();
            jLplayres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jLplayres.setCellRenderer(new UserListRenderer(
                    TableRanking.coloresRanking));
            jLplayres.addMouseListener(new MouseAdapter() {                
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
                    {
                        getJBinvitar().doClick();
                    }
                }
            });
        }
        return jLplayres;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPbuttons() {
        if (jPbuttons == null) {
            jPbuttons = new JPanel();
            jPbuttons.add(getJBinvitar(), null);
            jPbuttons.add(getJBcancelar(), null);
        }
        return jPbuttons;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJBinvitar() {
        if (jBinvitar == null) {
            jBinvitar = new JButton();
            jBinvitar.setText("Invitar");
            jBinvitar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jBinvitarActionPerformed(e);
                }
            });
        }
        return jBinvitar;
    }

    /**
     * @param e
     */
    protected void jBinvitarActionPerformed(ActionEvent e) {
        if (getJLplayres().getSelectedValue() != null)
            setVisible(false);
        else
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un jugador de la lista",
                    "Error de seleccion", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getJBcancelar() {
        if (jBcancelar == null) {
            jBcancelar = new JButton();
            jBcancelar.setText("Cancelar");
            jBcancelar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jBcancelarActionPerformed(e);
                }
            });
        }
        return jBcancelar;
    }

    /**
     * @param e
     */
    protected void jBcancelarActionPerformed(ActionEvent e) {
        setVisible(false);
    }
}