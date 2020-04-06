/* LoginPanel.java
 * Created on Jan 7, 2005
 *
 * Last modified: $Date: 2005/01/07 20:32:01 $
 * @version $Revision: 1.1 $ 3
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author afeltes
 *  
 */
public class LoginPanel extends JPanel {

    private RoomUING roomUING = null;

    private JLabel jLabel = null;

    private JPasswordField jPasswordField;

    private JPanel jPpassword = null; //  @jve:decl-index=0:visual-constraint="30,57"

    private JPanel jPbuttones;

    private JButton jBok;

    private JButton jBcancel;

    private JTextField jTextField = null;

    private JPanel jPanel1 = null;

    private JLabel jLabel1 = null;

    private JLabel jLestado = null; //  @jve:decl-index=0:visual-constraint="197,151"

    /**
     * This method initializes
     *  
     */
    public LoginPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        jLabel = new JLabel();
        this.setLayout(new BorderLayout());
        jLabel.setText("Contraseña ");
        jLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        this.setSize(318, 88);
        this.add(getJPanel1(), java.awt.BorderLayout.CENTER);
        this.add(getJPbuttones(), java.awt.BorderLayout.SOUTH);
        //        this.add(getJPpassword(), java.awt.BorderLayout.CENTER);
        this.add(getJLestado(), java.awt.BorderLayout.NORTH);
    }

    /**
     * @param roomUING
     */
    public void setRoomUING(RoomUING roomUING) {
        this.roomUING = roomUING;
    }

    /**
     * @return Returns the roomUING.
     */
    public RoomUING getRoomUING() {
        return roomUING;
    }

    /**
     * This method initializes jPasswordField
     * 
     * @return javax.swing.JPasswordField
     */
    private javax.swing.JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new javax.swing.JPasswordField();
            jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    jpKeyPressed(e);
                }
            });
            jPasswordField
                    .setBorder(javax.swing.BorderFactory
                            .createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        }
        return jPasswordField;
    }

    /**
     * @param e
     */
    protected void jpKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            getJBok().doClick();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            getJBcancel().doClick();
        }
    }

    /**
     * This method initializes jPpassword
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPpassword() {
        if (jPpassword == null) {
            jPpassword = new JPanel();
            jPpassword.setLayout(new BorderLayout());
            jPpassword.add(getJPasswordField(), java.awt.BorderLayout.CENTER);
            jPpassword.add(getJPbuttones(), java.awt.BorderLayout.SOUTH);
        }
        return jPpassword;
    }

    /**
     * This method initializes jPbuttones
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPbuttones() {
        if (jPbuttones == null) {
            jPbuttones = new javax.swing.JPanel();
            jPbuttones.add(getJBok(), null);
            jPbuttones.add(getJBcancel(), null);
            jPbuttones
                    .setBorder(javax.swing.BorderFactory
                            .createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        }
        return jPbuttones;
    }

    /**
     * This method initializes jBok
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJBok() {
        if (jBok == null) {
            jBok = new javax.swing.JButton();
            jBok.setText("Ok");
            jBok.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jBokActionPerformed(e);
                }
            });
        }
        return jBok;
    }

    /**
     * @param e
     */
    protected void jBokActionPerformed(ActionEvent e) {
        doLogin();
        //    	dispose();
    }

    /**
     *  
     */
    private void doLogin() {
        String uname = getJTextField().getText();
        String pass = new String(getJPasswordField().getPassword());
        if (uname != null && uname.trim().length() >= 0 && pass != null
                && pass.trim().length() >= 0) {
            getRoomUING().getRoomClient().fireLoginRequested(uname, pass);
        }
    }

    /**
     * This method initializes jBcancel
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJBcancel() {
        if (jBcancel == null) {
            jBcancel = new javax.swing.JButton();
            jBcancel.setText("Cancel");
            jBcancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jBcancelActionPerformed(e);
                }
            });
        }
        return jBcancel;
    }

    /**
     * @param e
     */
    protected void jBcancelActionPerformed(ActionEvent e) {
        getJPasswordField().setText(null);
        setVisible(false);
        //    	dispose();
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setText("");
        }
        return jTextField;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jLabel1 = new JLabel();
            java.awt.GridLayout gridLayout1 = new GridLayout();
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout1);
            gridLayout1.setRows(2);
            jLabel1.setText("Usuario ");
            jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jPanel1.add(jLabel1, null);
            jPanel1.add(getJTextField(), null);
            jPanel1.add(jLabel, null);
            jPanel1.add(getJPpassword(), null);
            ;
        }
        return jPanel1;
    }

    /**
     * This method initializes jLestado
     * 
     * @return javax.swing.JLabel
     */
    public JLabel getJLestado() {
        if (jLestado == null) {
            jLestado = new JLabel();
            jLestado.setText("Estado:");
        }
        return jLestado;
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
