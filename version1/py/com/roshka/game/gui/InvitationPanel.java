/* InvitationPanel.java
 * Created on May 5, 2005
 *
 * Last modified: $Date: 2005/05/17 22:01:48 $
 * @version $Revision: 1.3 $ 
 * @author afeltes
 */
package py.com.roshka.game.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.TrucoTable;

/**
 * 
 * @author afeltes
 *  
 */

public class InvitationPanel extends JPanel {

    public static final int RECHAZADO = 99;
    public static final int ACEPTADO = 100;
    
    private String playerHost;

    private int rankingHost;

    private int tableHostNumber;

    private String playerInvited;

    private JPanel jPanel = null;

    private JTextField jTFplayer = null;

    private JTextField jTFmesaNro = null;

    private JTextField jTFmotivo = null;

    private JPanel jPanel1 = null;

    private JButton jButton = null;

    private JButton jButton1 = null;

    private RoomListener roomListener;
    private TrucoTable table;
    private JDialog dialog;
    
    private TrucoPlayer trucoPlayer = null;
    
    public static void main(String[] args) {
        InvitationPanel ip = new InvitationPanel();
        ip.setPlayerHost("Ale");
        ip.setRankingHost(1600);
        ip.setPlayerInvited("Chumpitas");
        JFrame jf = new JFrame();
        jf.getContentPane().add(ip);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(300,200);
        jf.setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public InvitationPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(300, 150);
        this.add(getJPanel(), java.awt.BorderLayout.CENTER);
        this.add(getJTFplayer(), java.awt.BorderLayout.NORTH);
        this.add(getJPanel1(), java.awt.BorderLayout.SOUTH);
        this.setBorder(new EtchedBorder());
    }

    public String getPlayerHost() {
        return playerHost;
    }

    public void setPlayerHost(String playerHost) {
        this.playerHost = playerHost;   
        getJTFplayer().setText(getJTFplayer().getText() + " " + playerHost);
    }

    public String getPlayerInvited() {
        return playerInvited;
    }

    public void setPlayerInvited(String playerInvited) {
        this.playerInvited = playerInvited;
    }

    public int getRankingHost() {
        return rankingHost;
    }

    public void setRankingHost(int rankingHost) {
        this.rankingHost = rankingHost;
        getJTFplayer().setText(getJTFplayer().getText() + " (" + rankingHost + ")");
        getJTFplayer().setToolTipText(getJTFplayer().getText());
    }

    public int getTableHostNumber() {
        return tableHostNumber;
    }

    public void setTableHostNumber(int tableHostNumber) {
        this.tableHostNumber = tableHostNumber;
        getJTFmesaNro().setText("lo invita a unirse a la mesa Nro. : " + tableHostNumber);
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJTFmesaNro(), java.awt.BorderLayout.NORTH);
            jPanel.add(getJTFmotivo(), java.awt.BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
     * This method initializes jTFplayer
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTFplayer() {
        if (jTFplayer == null) {
            jTFplayer = new JTextField();
            jTFplayer.setText("El jugador: ");
            jTFplayer.setEditable(false);
        }
        return jTFplayer;
    }

    /**
     * This method initializes jTFmesaNro
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTFmesaNro() {
        if (jTFmesaNro == null) {
            jTFmesaNro = new JTextField();
            jTFmesaNro.setEditable(false);
            jTFmesaNro.setText("lo invita a unirse a la mesa Nro. : ");
        }
        return jTFmesaNro;
    }

    /**
     * This method initializes jTFmotivo
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTFmotivo() {
        if (jTFmotivo == null) {
            jTFmotivo = new JTextField();
            jTFmotivo.setText("No gracias. Tal vez en otro momento.");
            jTFmotivo.setBorder(new TitledBorder("Motivo del rechazo"));
        }
        return jTFmotivo;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.add(getJButton(), null);
            jPanel1.add(getJButton1(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Aceptar");
            jButton.addActionListener(new java.awt.event.ActionListener() { 
            	public void actionPerformed(java.awt.event.ActionEvent e) {    
            		jButtonAceptarActionPerformed(e);
            	}
            });
        }
        return jButton;
    }

    /**
     * @param e
     */
    protected void jButtonAceptarActionPerformed(ActionEvent e) {
        getTrucoTable().getTable().getRoom().joinTableRequest(getTableHostNumber());
        getDialog().setVisible(false);
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Rechazar");
            jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jButtonCancelActionPerformed(e);
                }
            });
        }
        return jButton1;
    }
	/**
     * @param e
     */
    protected void jButtonCancelActionPerformed(ActionEvent e) {
        RoomEvent re = new RoomEvent();
        re.setPlayer(getTrucoPlayer());
        re.setType(RoomEvent.TYPE_INVITACION_REJECTED);
        re.setErrorMsg(getJTFmotivo().getText());
        getTrucoTable().getTable().sendInvitationRejected(re);
        getDialog().setVisible(false);
    }

    /**
	 * @param roomListener The roomListener to set.
	 */
	public void setRoomListener(RoomListener roomListener) {
		this.roomListener = roomListener;
	}

    /**
     * @param table
     */
    public void setTrucoTable(TrucoTable table) {
        this.table = table;
    }
    
    public TrucoTable getTrucoTable(){
        return this.table;
    }

    /**
     * @param jf
     */
    public void setDialog(JDialog jf) {
        this.dialog = jf;
    }
    public JDialog getDialog() {
        return dialog;
    }
    public TrucoPlayer getTrucoPlayer() {
        return trucoPlayer;
    }
    public void setTrucoPlayer(TrucoPlayer trucoPlayer) {
        this.trucoPlayer = trucoPlayer;
    }
}
