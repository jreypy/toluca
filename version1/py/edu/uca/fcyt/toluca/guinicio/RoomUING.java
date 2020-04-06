/* RoomUING.java
 * Created on Sep 10, 2004
 *
 * Last modified: $Date: 2005/09/02 23:24:15 $
 * @version $Revision: 1.44 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import py.com.roshka.game.gui.UserListSelection;
import py.edu.uca.fcyt.game.ChatPanel;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;

/**
 * 
 * @author afeltes
 *  
 */
public class RoomUING extends JApplet {

    //se saca el log4j porque es muy gande para que este en el cliente
    //    protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger
    //            .getLogger(RoomUING.class);

    public static final String VERSION = "20050819-1359";

    public static final String CLAVE_LOGIN = "claveLogin";

    private javax.swing.JPanel jProomPanel = null;

    private JPanel panelPrincipal = null; //  @jve:decl-index=0:visual-constraint="25,16"

    private PanelGradiente panelTitle = null; //  @jve:decl-index=0:visual-constraint="10,220"

    /** el directorio relativo de donde se sacan las imagenes */
    public static final String IMAGE_DIR = "/py/edu/uca/fcyt/toluca/images/inicio/";

    private JPanel panelConMargen = null;

    public static final Color COLOR_DE_FONDO = new Color(80, 120, 60);

    int margen = 10;

    private JPanel panelPaneles = null;

    private JPanel panelControl = null;

    private JPanel panelCentral = null;

    private JPanel scrollRanking = null;

    private PanelComandos panelComandos = null;

    private JScrollPane scrollGame = null;

    private TableGame tableGame = null;

    private JPanel panelSur = null;

    private ChatPanel chatPanel = null;

    private JScrollPane jScrollPane = null;

    private TableRanking tableRanking = null;

    private RoomClient roomClient;

    private LoginPanel loginPanel;

    private JPanel jContenPane;

    private JEditorPane jEPanuncios = null;

    private JPanel jPanel = null;

    private ConexionTestPanel conexionTestPanel = null;

    private JPanel jPanel1 = null;

    private static String URL_ANUNCIOS;

    static {
        //TODO Deberia cargarse de un .properties
        URL_ANUNCIOS = "http://www.truco.com.py/html/anuncios.html";
    }

    /**
     * This method initializes panelPrincipal
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelPrincipal() {
        if (panelPrincipal == null) {
            panelPrincipal = new JPanel();
            panelPrincipal.setLayout(new BorderLayout());
            //            panelPrincipal.add(getPanelConMargen(),
            // java.awt.BorderLayout.CENTER);
            panelPrincipal.add(getPanelTitle(), java.awt.BorderLayout.NORTH);
            panelPrincipal.add(getPanelConMargen(),
                    java.awt.BorderLayout.CENTER);
            //            panelPrincipal.add(getPanelTitle(), java.awt.BorderLayout.NORTH);
        }
        return panelPrincipal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#stop()
     * 
     * Programaci�n porcina aa / af el 23 de Diciembre de 2004 Cerramos el
     * socket al cerrar el applet.
     *  
     */
    public void stop() {
        roomClient.cerrarConexion();
        super.stop();
    }

    /**
     * This method initializes panelTitle
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.PanelGradiente
     */
    private PanelGradiente getPanelTitle() {
        if (panelTitle == null) {
            ImageIcon logo = new ImageIcon(getClass().getResource(
                    RoomUING.IMAGE_DIR + "LogoSinFondo.gif"));//RoomUING.loadImage("LogoSinFondo.gif");
            panelTitle = new PanelGradiente();
            panelTitle.setLogo(logo);
            panelTitle.setStartColor(new Color(50, 255, 50));
            panelTitle.setLargo(50);
            panelTitle.setNombre("desconocido");

            panelTitle.setMinimumSize(new Dimension(logo.getIconWidth(), logo
                    .getIconHeight()));

            panelTitle.setSize(543, 71);
        }
        return panelTitle;
    }

    /**
     * This method initializes panelConMargen
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelConMargen() {
        if (panelConMargen == null) {
            panelConMargen = new JPanel();
            panelConMargen.setLayout(new BorderLayout());
            panelConMargen.setOpaque(true);
            panelConMargen.setBackground(RoomUING.COLOR_DE_FONDO);
            panelConMargen.add(getPanelPaneles(), java.awt.BorderLayout.CENTER);
            panelConMargen.add(Box.createRigidArea(new Dimension(0, margen)),
                    BorderLayout.NORTH);
            panelConMargen.add(Box.createRigidArea(new Dimension(0, margen)),
                    BorderLayout.SOUTH);
            panelConMargen.add(Box.createRigidArea(new Dimension(margen, 0)),
                    BorderLayout.EAST);
            panelConMargen.add(Box.createRigidArea(new Dimension(margen, 0)),
                    BorderLayout.WEST);
        }
        return panelConMargen;
    }

    /**
     * This method initializes panelPaneles
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelPaneles() {
        if (panelPaneles == null) {
            panelPaneles = new JPanel();
            panelPaneles.setLayout(new BorderLayout());
            panelPaneles.add(getPanelControl(), java.awt.BorderLayout.WEST);
            panelPaneles.add(getPanelCentral(), java.awt.BorderLayout.CENTER);
            panelPaneles.add(getJPanel(), java.awt.BorderLayout.EAST);
        }
        return panelPaneles;
    }

    /**
     * This method initializes panelControl
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelControl() {
        if (panelControl == null) {
            panelControl = new JPanel();
            panelControl.setLayout(new BorderLayout());
            panelControl.add(getPanelComandos(), java.awt.BorderLayout.CENTER);
            panelControl.setBackground(RoomUING.COLOR_DE_FONDO);
            panelControl.setOpaque(true);
            panelControl.add(Box.createRigidArea(new Dimension(margen, 0)),
                    BorderLayout.EAST);
        }
        return panelControl;
    }

    /**
     * This method initializes panelCentral
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelCentral() {
        if (panelCentral == null) {
            panelCentral = new JPanel();
            panelCentral.setLayout(new BorderLayout());
            panelCentral.add(getScrollGame(), java.awt.BorderLayout.CENTER);
            panelCentral.add(getPanelSur(), java.awt.BorderLayout.SOUTH);
        }
        return panelCentral;
    }

    /**
     * This method initializes scrollRanking
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getScrollRanking() {
        if (scrollRanking == null) {
            scrollRanking = new JPanel();
            scrollRanking.setLayout(new BorderLayout());
            //scrollRanking.setPreferredSize(new Dimension(150, 2000));
            scrollRanking.add(Box.createRigidArea(new Dimension(margen, 0)),
                    BorderLayout.WEST);
            scrollRanking.setOpaque(true);
            scrollRanking.setBackground(RoomUING.COLOR_DE_FONDO);
            scrollRanking.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return scrollRanking;
    }

    /**
     * This method initializes panelComandos
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.PanelComandos
     */
    private PanelComandos getPanelComandos() {
        if (panelComandos == null) {
            panelComandos = new PanelComandos();
            //  panelComandos.add(Box.createRigidArea(new Dimension(margen, 0)),
            //        BorderLayout.EAST);
            panelComandos.setBackground(RoomUING.COLOR_DE_FONDO);
            panelComandos.setTableGame(getTableGame());
            panelComandos.setApplet(this);
        }
        return panelComandos;
    }

    /**
     * This method initializes scrollGame
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollGame() {
        if (scrollGame == null) {
            scrollGame = new JScrollPane();
            scrollGame.setViewportView(getTableGame());
        }
        return scrollGame;
    }

    /**
     * This method initializes tableGame
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.TableGame
     */
    public TableGame getTableGame() {
        if (tableGame == null) {
            tableGame = new TableGame();
        }
        return tableGame;
    }

    /**
     * This method initializes panelSur
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelSur() {
        if (panelSur == null) {
            Dimension dim = new Dimension(300,180);
            panelSur = new JPanel();
            panelSur.setMinimumSize(dim);
            panelSur.setPreferredSize(dim);
            panelSur.setLayout(new BorderLayout());
            panelSur.setOpaque(true);
            panelSur.setBackground(RoomUING.COLOR_DE_FONDO);
            panelSur.add(getChatPanel(), java.awt.BorderLayout.CENTER);
            panelSur.add(Box.createRigidArea(new Dimension(0, margen)),
                    BorderLayout.NORTH);
            //panelSur.add(getChatPanel(), BorderLayout.CENTER);
        }
        return panelSur;
    }

    /**
     * This method initializes chatPanel
     * 
     * @return py.edu.uca.fcyt.game.ChatPanel
     */
    public ChatPanel getChatPanel() {
        if (chatPanel == null) {
            chatPanel = new ChatPanel();
        }
        return chatPanel;
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
            //jScrollPane.setPreferredSize(new Dimension(150, 2000));
        }
        return jScrollPane;
    }

    /**
     * This method initializes tableRanking
     * 
     * @return py.edu.uca.fcyt.toluca.guinicio.TableRanking
     */
    public TableRanking getTableRanking() {
        if (tableRanking == null) {
            tableRanking = new TableRanking();
        }
        return tableRanking;
    }

    /**
     * This method initializes jEPanuncios
     * 
     * @return javax.swing.JTextArea
     */
    public JEditorPane getJEPanuncios() {
        if (jEPanuncios == null) {
            try {
                if (URL_ANUNCIOS == null)
                    jEPanuncios = new JEditorPane();
                else
                    jEPanuncios = new JEditorPane(URL_ANUNCIOS);
                //jEPanuncios.setEditorKit(new HTMLEditorKit());
            } catch (IOException e) {
                TolucaConstants.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,
                        "error cargando anuncios");
            }

        }
        return jEPanuncios;
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
            jPanel.add(getScrollRanking(), java.awt.BorderLayout.CENTER);
            jPanel.add(getJPanel1(), java.awt.BorderLayout.SOUTH);
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
        }
        return conexionTestPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(getConexionTestPanel(), java.awt.BorderLayout.CENTER);
            jPanel1.add(Box.createRigidArea(new Dimension(margen, 0)),
                    BorderLayout.WEST);
            jPanel1.setOpaque(true);
            jPanel1.setBackground(RoomUING.COLOR_DE_FONDO);
        }
        return jPanel1;
    }

    public static void main(String[] args) {
    }

    /**
     * This is the default constructor
     */
    public RoomUING() {
        super();
        //init();

    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    public void init() {
        //        DOMConfigurator.configure(System.getProperty("user.dir")
        //                + System.getProperty("file.separator") + "log.xml");
        loadAppletParameters();
        setRoomClient(new RoomClient(this));
        this.setSize(750, 470);
        //this.setContentPane(getRoomPanel());
        this.setContentPane(getCcontenPane());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                getChatPanel().getJtInput().requestFocus();

            }
        });
    }

    /**
     * @return
     */
    private JPanel getCcontenPane() {
        if (jContenPane == null) {
            jContenPane = new JPanel();
            jContenPane.setLayout(new BorderLayout());
            jContenPane.add(getLoginPanel(), BorderLayout.NORTH);
            jContenPane.add(getJEPanuncios(), java.awt.BorderLayout.CENTER);
        }
        return jContenPane;
    }

    /**
     * @return
     */
    public LoginPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new LoginPanel();
            loginPanel.setRoomUING(this);
        }
        return loginPanel;
    }

    /**
     * @param client
     */
    private void setRoomClient(RoomClient client) {
        this.roomClient = client;
        getChatPanel().setCpc(roomClient);
        roomClient.setChatPanel(getChatPanel());
        roomClient.setMainTable(getTableGame());
        roomClient.setRankTable(getTableRanking());
        getPanelComandos().setRoomClient(roomClient);
    }

    /**
     * This method initializes jProomPanel
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getRoomPanel() {
        if (jProomPanel == null) {
            jProomPanel = new javax.swing.JPanel();
            jProomPanel.setSize(750, 470);
            jProomPanel.setLayout(new java.awt.BorderLayout());
            jProomPanel.add(getPanelPrincipal(), java.awt.BorderLayout.CENTER);
        }
        return jProomPanel;
    }

    protected void loadAppletParameters() {
        //Get the applet parameters.
        //String at = getParameter("img");
        //  dir = (at != null) ? at : "/images";
        try {
            String imagedir = getParameter("IMAGEDIR");
            URL_ANUNCIOS = getParameter("urlanuncios");
            // logger.debug("La dire de imagenes es " + imagedir);
        } catch (Exception e) {
        }
        if (URL_ANUNCIOS == null) {
            URL_ANUNCIOS = "";
            TolucaConstants.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,
                    "Falta el parametro para el applet: urlanuncios");
        }
    }

    protected static ImageIcon loadImage(String image) {
        /*
         * ImageIcon ret = new ImageIcon(image.getClass().getResource(image));
         * return ret;
         */
        String path = IMAGE_DIR + image;
        int MAX_IMAGE_SIZE = 10000; //Change this to the size of
        //your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = new BufferedInputStream(path.getClass()
                .getResourceAsStream(path));
        if (imgStream != null) {
            byte buf[] = new byte[MAX_IMAGE_SIZE];
            try {
                count = imgStream.read(buf);
                imgStream.close();
            } catch (java.io.IOException ioe) {
                System.out.println("Couldn't read stream from file: " + path);
                return null;
            }
            if (count <= 0) {
                System.out.println("Empty file: " + path);
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void login() {

        // El login lo escribo por n-�sima vez - CVS urgeeeeente!
        py.edu.uca.fcyt.util.LoginDialog ld = new py.edu.uca.fcyt.util.LoginDialog(
                JOptionPane.getRootFrame(), true);
        ld.setVisible(true);
        //roomClient = new RoomClient(this, ld.getUsername(),
        // ld.getPassword());
        getChatPanel().setCpc(roomClient);

        //System.out.println("El chatpanel del roomui es "+chatPanel);
        roomClient.setChatPanel(getChatPanel());
        roomClient.setMainTable(getTableGame());
        roomClient.setRankTable(getTableRanking());
        getPanelComandos().setRoomClient(roomClient);
        // esto es para probar nomas, sacar despues!!
        //Table table = new Table();
        //table.setTableNumber(5);
        //mainTable.insertarFila(table);

        //jpChatRanking.validate();
        roomClient.fireLoginRequested(ld.getUsername(), ld.getPassword());
    }

    /**
     * @param chatPanel2
     */
    public void addChatPanel(ChatPanel chatPanel2) {
        setChatPanel(chatPanel2);
    }

    /**
     * @param chatPanel
     *            The chatPanel to set.
     */
    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    public void setOwner(TrucoPlayer trucoPlayer) {

        panelTitle.setNombre(trucoPlayer.getName());
    }

    public void removeTable(Table table) {
        tableGame.eliminarFila(table.getTableNumber());
    }

    /**
     * @return Returns the roomClient.
     */
    public RoomClient getRoomClient() {
        return roomClient;
    }

    /**
     * @param event
     */
    public void loginFailed(RoomEvent event) {
        getLoginPanel().getJLestado().setForeground(Color.RED);
        getLoginPanel().getJLestado().setText(
                new Date().toString() + " :   " + event.getErrorMsg());
        getLoginPanel().getJLestado().setToolTipText(event.getErrorMsg());
    }

    /**
     * @param player
     */
    public void loginCompleted(TrucoPlayer player) {
        TrucoPlayer tp = new TrucoPlayer("macho");
        getContentPane().removeAll();
        getContentPane().add(getRoomPanel());
        setOwner(player);
        validateTree();
        getChatPanel()
                .showSystemMessage("Para iniciar un juego presiona \"Crear Mesa\" y sientate en un cuadro Libre", null);
        getChatPanel()
                .showSystemMessage("Para unirte a una mesa seleciona una de la tabla del medio y presiona \"Unirse\"", null);
    }

    public void actualzarRanking(TrucoPlayer trucoPlayer) {
        tableRanking.actualizarPuntaje(trucoPlayer);
    }

    public void actualizarTestConexion(long ms) {
        getConexionTestPanel().actualizar(ms);
    }

    public List getPlayers() {
        return getTableRanking().getPlayers();
    }

    /**
     * @return
     */
    public TrucoPlayer selectUserFromList() {
        UserListSelection jd = new UserListSelection();
        DefaultListModel lm = new DefaultListModel();
        Iterator it = getPlayers().iterator();
        while (it.hasNext()) {
            lm.addElement(it.next());
        }
        jd.getJLplayres().setModel(lm);
        jd.setVisible(true);
        return (TrucoPlayer) jd.getJLplayres().getSelectedValue();
    }

} //  @jve:decl-index=0:visual-constraint="10,30"
