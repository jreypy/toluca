package py.edu.uca.fcyt.toluca.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTMLEditorKit;

import py.com.roshka.game.gui.InvitationPanel;
import py.edu.uca.fcyt.game.ChatPanel;
import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;
import py.edu.uca.fcyt.toluca.guinicio.ConexionTestPanel;
import py.edu.uca.fcyt.toluca.guinicio.TableRanking;

import javax.swing.JEditorPane;
/** Panel principal de juego */
public class TrucoTable extends JPanel implements ComponentListener {
    
    private SimpleAttributeSet style = new SimpleAttributeSet();
    public static final int BUTTON_INICIAR_OK = 0;

    public static final int BUTTON_HECHAR = 1;

    public static final int BUTTON_AYUDA = 2;

    protected Score score; // panel de puntajes

    //	protected PlayTable pTable;

    protected JButton[] buttons = new JButton[] { new JButton("Iniciar"),
            new JButton("Ayuda") };

    protected JLabel jlSaying;

    protected Actions actions;

    private Table table;

    private PlayTable playTable = null; //  @jve:decl-index=0:visual-constraint="387,56"

    private ChatPanel chatPanel = null;

    private JPanel jpLeftPanel = null;

    private JPanel jpCantos = null;

    private JScrollPane jScrollPane = null;

    private TableRanking tableRanking = null;

    private JPanel jPmedio = null;

    private JPanel jPbotonesJugadas = null;

    private JPanel jPanel = null;

    private ConexionTestPanel conexionTestPanel = null;

    private JButton jBinvitar;

    private JPanel logChatPanel;

	private JScrollPane jScrollPane1 = null;
	private JEditorPane jEditorPane = null;  //  @jve:decl-index=0:
    /**
     * Construye un TrucoTable con ptListener como listener de eventos de la
     * mesa
     */
    public TrucoTable() {
        initialize();
        inicializar();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        //this.add(getChatPanel(), java.awt.BorderLayout.SOUTH);
        this.add(getLogChatPanel(), java.awt.BorderLayout.SOUTH);
        this.add(getJpLeftPanel(), java.awt.BorderLayout.WEST);
        this.add(getJpCantos(), java.awt.BorderLayout.NORTH);
        this.add(getJPmedio(), java.awt.BorderLayout.CENTER);
        this.add(getJPanel(), java.awt.BorderLayout.EAST);
    }

    /**
     * @return
     */
    private JPanel getLogChatPanel() {
        if(logChatPanel == null)
        {
            Dimension dim = new Dimension(240,100);
            logChatPanel = new JPanel();
            logChatPanel.setMaximumSize(dim);
            logChatPanel.setMinimumSize(dim);
            logChatPanel.setPreferredSize(dim);
            logChatPanel.setLayout(new BoxLayout(logChatPanel, BoxLayout.X_AXIS));
            logChatPanel.add(getJScrollPane1(), null);
            logChatPanel.add(getChatPanel(), null);            
        }
        return logChatPanel;
    }

    /**
     * @return
     */
    private Score getScore() {
        if (score == null) {
            score = new Score();
            score.setLayout(new BoxLayout(score, BoxLayout.Y_AXIS));
            score.add(new JLabel(" ------ Puntaje ------ "));
            score.add(new JLabel("    Rojo        "
                    + "   Azul   "));
            score.setBorder(new EtchedBorder());
        }
        return score;
    }

    /**
     *  
     */
    void inicializar() {
        //      crea los componentes
        //		pTable = new PlayTable(getTable());
        //		jpLeftPanel = new JPanel();
        //		jpCantos = new JPanel();
        //		jpChat = new ChatPanel(getTable(), getTable().getPlayer());
        //		score = new Score(30);
        //		jpWatchers = new JPlayers();

        // agrega
        //		jpChat.add(new JLabel("Chat"));

        //		setLayout(new BorderLayout());

        //		add(pTable, BorderLayout.CENTER);
        //		add(jpLeftPanel, BorderLayout.WEST);
        //		add(jpCantos, BorderLayout.NORTH);
        //		add(jpChat, BorderLayout.SOUTH);
        //		add(score, BorderLayout.EAST);

        /*
         * if (getTable().isHost()) { buttons = new JButton[] { new
         * JButton("Iniciar"), new JButton("Echar"), new JButton("Ayuda") }; }
         * else { buttons = new JButton[] { new JButton("Iniciar"), new
         * JButton("Ayuda") }; }
         */
        buttons[0].setEnabled(false);

        //		score.setBorder(new EtchedBorder());
        //		resizeComponents();

        JPanel aux;
        addComponentListener(this);

    }

    public void componentResized(ComponentEvent e) {
        resizeComponents();
    }

    public void componentMoved(ComponentEvent e) {
        // TODO: Add your code here
    }

    public void componentShown(ComponentEvent e) {
        //		resizeComponents();
    }

    public void componentHidden(ComponentEvent e) {
        // TODO: Add your code here
    }

    private void resizeComponents() {
    }

    public JButton getJButton(String text) {
        for (int i = 0; i < buttons.length; i++)
            if (buttons[i].getText().equals(text))
                return buttons[i];

        return null;
    }

    /**
     * @param manager
     */
    public void enableAction(PlayerManager manager, TrucoPlayer actualPlayer) {
        boolean ena = manager.evenTeams() && (manager.getLocalChair() == 0)
                && manager.isSitted(actualPlayer);
        buttons[TrucoTable.BUTTON_INICIAR_OK].setEnabled(ena);
    }

    /**
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
        getScore().setGamePoints(table.getGamePoints());
        getPlayTable().setPtListener(getTable());
        getPlayTable().inicializar();
        getChatPanel().setCpc(table);
        getChatPanel().setPlayer(table.getPlayer());
        getJpLeftPanel().add(getJBinvitar());
        getJpLeftPanel().add(actions = new Actions(buttons, table, table));        
        //        ((TableRanking)getTableRanking()).setTable(table);
    }

    /**
     * @return
     */
    JButton getJBinvitar() {
        if (jBinvitar == null) {            
            jBinvitar = new JButton();
            jBinvitar.setText("Invitar");
            jBinvitar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jBinvitarActionPerformed(e);

                }
            });
            jBinvitar.setPreferredSize(Actions.DEFAULT_BUTTON_SIZE);
            jBinvitar.setMaximumSize(Actions.DEFAULT_BUTTON_SIZE);
            jBinvitar.setMinimumSize(Actions.DEFAULT_BUTTON_SIZE);
        }
        return jBinvitar;
    }

    /**
     * @param e
     */
    protected void jBinvitarActionPerformed(ActionEvent e) {
        TrucoPlayer tp = getTable().getRoom().getRoomUING().selectUserFromList();
        if(tp != null)
            sendInvitation(tp);
    }

    /**
     * @param tp
     * 
     */
    private void sendInvitation(TrucoPlayer tp) {
        //TODO enviar el pacochi. Debe contener el player que invita
        // a quien se invita y el nro. de mesa
        TolucaConstants.log(getTable().getPlayer() + " de la mesa #" + getTable().getTableNumber() + " esta invitando al jugador " + tp);
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_INVITACION);
        re.setPlayer(tp);
        re.setTableNumber(getTable().getTableNumber());
        getTable().fireInvitationRequest(re);
    }

    public Table getTable() {
        return table;
    }

    /**
     * This method initializes playTable
     * 
     * @return py.edu.uca.fcyt.toluca.table.PlayTable
     */
    public PlayTable getPlayTable() {
        if (playTable == null) {
            playTable = new PlayTable();
        }
        return playTable;
    }

    /**
     * This method initializes chatPanel
     * 
     * @return py.edu.uca.fcyt.game.ChatPanel
     */
    public ChatPanel getChatPanel() {
        if (chatPanel == null) {
            chatPanel = new ChatPanel();
            chatPanel.setBorder(new EtchedBorder());
        }
        return chatPanel;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJpLeftPanel() {
        if (jpLeftPanel == null) {
            jpLeftPanel = new JPanel();
            jpLeftPanel.setLayout(new BoxLayout(jpLeftPanel, BoxLayout.Y_AXIS));
            jpLeftPanel.setBorder(new EtchedBorder());
            //			jpLeftPanel.add(getJpWatchers(), null);
            jpLeftPanel.setPreferredSize(new Dimension(125, 200));
            jpLeftPanel.add(getJScrollPane(), null);
        }
        return jpLeftPanel;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJpCantos() {
        if (jpCantos == null) {
            Dimension dim = new Dimension(300, 50);
            jpCantos = new JPanel();
            jpCantos.setMinimumSize(dim);
            jpCantos.setPreferredSize(dim);
            jpCantos.setMaximumSize(new Dimension(1024, 50));
            jpCantos.add(jlSaying = new JLabel("Canto: "));
            jpCantos.setBorder(new EtchedBorder());
        }
        return jpCantos;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getTableRanking());
        }
        return jScrollPane;
    }

    /**
     * This method initializes tableRanking
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.TableRanking
     */
    TableRanking getTableRanking() {
        if (tableRanking == null) {
            tableRanking = new TableRanking();
        }
        return tableRanking;
    }

    /**
     * @param player
     */
    public void addPlayer(TrucoPlayer player) {
        //getJpWatchers().addPlayer(player.getName());
        getTableRanking().addPlayer(player);
    }

    /**
     * @param player
     */
    public void removePlayer(TrucoPlayer player) {
        //getJpWatchers().removePlayer(player.getName());
        getTableRanking().removeplayer(player);
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPmedio() {
        if (jPmedio == null) {
            jPmedio = new JPanel();
            jPmedio.setLayout(new BoxLayout(jPmedio, BoxLayout.Y_AXIS));
            jPmedio.add(getPlayTable());
            //jPmedio.add(getJSPbotonesJugadas(), java.awt.BorderLayout.SOUTH);
            jPmedio.add(getJPbotonesJugadas());
        }
        return jPmedio;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    public JPanel getJPbotonesJugadas() {
        if (jPbotonesJugadas == null) {
            Dimension dim = new Dimension(300, 50);
            java.awt.GridLayout gridLayout1 = new GridLayout();
            jPbotonesJugadas = new JPanel() {
                /*
                 * (non-Javadoc)
                 * 
                 * @see java.awt.Component#repaint()
                 */
                public void repaint() {
                    // TODO Auto-generated method stub
                    super.repaint();
                    validateTree();
                }
            };
            jPbotonesJugadas.setLayout(gridLayout1);
            jPbotonesJugadas.setMinimumSize(dim);
            jPbotonesJugadas.setPreferredSize(dim);
            jPbotonesJugadas.setMaximumSize(new Dimension(1024, 50));
            jPbotonesJugadas
                    .setBorder(javax.swing.BorderFactory
                            .createTitledBorder(
                                    null,
                                    "Jugadas",
                                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                    new java.awt.Font("Dialog",
                                            java.awt.Font.PLAIN, 12),
                                    java.awt.Color.blue));
            gridLayout1.setRows(1);
        }
        return jPbotonesJugadas;
    }

    public JLabel getJlSaying() {
        return jlSaying;
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
            jPanel.add(getScore(), java.awt.BorderLayout.CENTER);
            jPanel.add(getConexionTestPanel(), java.awt.BorderLayout.SOUTH);
        }
        return jPanel;
    }

    /**
     * This method initializes conexionTestPanel
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.ConexionTestPanel
     */
    private ConexionTestPanel getConexionTestPanel() {
        if (conexionTestPanel == null) {
            conexionTestPanel = new ConexionTestPanel();
            conexionTestPanel.setBorder(BorderFactory.createEtchedBorder());
        }
        return conexionTestPanel;
    }

    public void actualizarConexionStatus(float ms) {
        getConexionTestPanel().actualizar(ms);
    }

    /**
     * @param event
     */
    public void showInvitation(RoomEvent event) {
        InvitationPanel ip = new InvitationPanel();
        ip.setTrucoTable(this);
        ip.setPlayerHost(event.getPlayer().getName());
        ip.setRankingHost(event.getPlayer().getRating());
        ip.setPlayerInvited(getTable().getPlayer().getName());
        ip.setTableHostNumber(event.getTableNumber());
        ip.setTrucoPlayer(event.getPlayer());
        JDialog jf = new JDialog();
        jf.setTitle("Invitacion");
        jf.getContentPane().add(ip);
        ip.setDialog(jf);
        jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jf.setSize(300,200);
        jf.setVisible(true);
    }
	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJEditorPane());
			jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informaciones del Juego", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
	}
		return jScrollPane1;
	}
	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */    
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
//			jEditorPane.setEditorKit(new HTMLEditorKit());
			jEditorPane.setPreferredSize(new java.awt.Dimension(240,100));
			jEditorPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
		}
		return jEditorPane;
	}
	public void addLog(TrucoPlayer player, int team, String htmlMessage)
	{
	    String msg = player.getName() + "(" + (team == TrucoTeam.ROJO ? "Rojo): " : "Azul): ") + htmlMessage + "\n";
		appendLog(msg);
	}

    /**
     * @param msg
     */
    private void appendLog(String msg) {
        try
		{
			String s = getJEditorPane().getDocument().getText(0,getJEditorPane().getDocument().getLength());
			int d1 = s.length();
			getJEditorPane().getDocument().insertString(getJEditorPane().getDocument().getLength(), msg, style);
			getJEditorPane().setEditable(true);
			
			getJEditorPane().setEditable(false);
			// Temporary patch code.
			getJEditorPane().getDocument().insertString(getJEditorPane().getDocument().getLength(), "", style);
			getJEditorPane().setCaretPosition(getJEditorPane().getDocument().getLength());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }

    /**
     * 
     */
    public void startGame() {
        appendLog("------------------------\n");
        appendLog(new Date().toString() + ": Nuevo Juego\n" );
    }
    
    public void endOfGame() {        
        appendLog(new Date().toString() + ": Fin del Juego\n" );
    }

    /**
     * @param string
     */
    public void addSystemLog(String string) {
        appendLog(string);
    }
		
  }
