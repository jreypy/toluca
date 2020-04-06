package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import py.edu.uca.fcyt.game.ChatPanel;

/**
 * @author CYT UCA
 * 
 * @deprecated
 *  
 */
public class RoomUI extends JApplet {

    private TableRanking tableRanking;

    private JScrollPane scrollRanking;

    private TableGame tableGame;

    private JScrollPane scrollGame;

    private ChatPanel chatPanel;

    /** el directorio relativo de donde se sacan las imagenes */
    public static final String IMAGE_DIR = "/py/edu/uca/fcyt/toluca/images/inicio/";

    private Color colorFondo = new Color(80, 120, 60);

    int margen = 10;

    /**
     * @return Returns the scrollGame.
     */

    private JScrollPane getScrollGame() {
        if (scrollGame == null) {
            scrollGame = new JScrollPane();
            scrollGame.setViewportView(getTableGame());
        }
        return scrollGame;
    }

    /**
     * @return Returns the tableGame.
     */
    private TableGame getTableGame() {
        if (tableGame == null) {
            tableGame = new TableGame();
        }
        return tableGame;
    }

    private JComponent getPanelTitle() {

        ImageIcon logo = loadImage("LogoSinFondo.gif");

        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        PanelGradiente panel = new PanelGradiente();

        panel.setMinimumSize(new Dimension(logo.getIconWidth(), logo
                .getIconHeight()));

        return panel;
    }

    private JPanel getPanelControl() {
        ImageIcon icon = RoomUI.loadImage("btnCrearMesa.gif");
        ImageIcon controlIcon = RoomUI.loadImage("btnAyuda.gif");
        ImageIcon salirIcon = RoomUI.loadImage("btnSalir.gif");
        //ImageIcon puntajeIcon = RoomUI.loadImage("puntaje.gif");

        PanelComandos panel = new PanelComandos();
        panel.setApplet(this);
        JPanel panelAfuera = new JPanel();
        panelAfuera.setLayout(new BorderLayout());
        panelAfuera.add(panel, BorderLayout.CENTER);
        panelAfuera.add(Box.createRigidArea(new Dimension(margen, 0)),
                BorderLayout.EAST);
        panelAfuera.setBackground(colorFondo);
        return panelAfuera;

    }

    public static ImageIcon loadImage(String image) {
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
                System.err.println("Couldn't read stream from file: " + path);
                return null;
            }
            if (count <= 0) {
                System.err.println("Empty file: " + path);
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void init() {
        loadAppletParameters();
        getContentPane().add(getPanelPrincipal());

    }

    private JPanel getPanelPrincipal() {
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.add(getPanelTitle(), BorderLayout.NORTH);
        /*
         * panel.add(getScrollGame(),BorderLayout.CENTER);
         * panel.add(getScrollRanking(),BorderLayout.EAST);
         * panel.add(getPanelControl(),BorderLayout.WEST);
         */
        panel.add(getPanelConMargen(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getPanelConMargen() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(colorFondo);
        panel.setOpaque(true);

        panel.add(Box.createRigidArea(new Dimension(0, margen)),
                BorderLayout.NORTH);
        panel.add(Box.createRigidArea(new Dimension(0, margen)),
                BorderLayout.SOUTH);
        panel.add(getPanelPaneles(), BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(margen, 0)),
                BorderLayout.EAST);
        panel.add(Box.createRigidArea(new Dimension(margen, 0)),
                BorderLayout.WEST);
        return panel;
    }

    private JPanel getPanelPaneles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getPanelControl(), BorderLayout.WEST);
        panel.add(getPanelCentral(), BorderLayout.CENTER);
        panel.add(getScrollRanking(), BorderLayout.EAST);
        return panel;
    }

    private JPanel getPanelCentral() {
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.add(getScrollGame(), BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setOpaque(true);
        panel2.setBackground(colorFondo);
        panel2.add(Box.createRigidArea(new Dimension(0, margen)),
                BorderLayout.NORTH);
        panel2.add(getChatPanel(), BorderLayout.CENTER);
        panel.add(panel2, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getScrollRanking() {

        scrollRanking = new JScrollPane();
        scrollRanking.setViewportView(getTableRanking());
        scrollRanking.setPreferredSize(new Dimension(150, 2000));

        JPanel panelAfuera = new JPanel();
        panelAfuera.setLayout(new FlowLayout(FlowLayout.CENTER));

        panelAfuera.add(Box.createRigidArea(new Dimension(margen, 0)));
        panelAfuera.add(scrollRanking);
        panelAfuera.setBackground(colorFondo);

        return panelAfuera;

    }

    private TableRanking getTableRanking() {
        if (tableRanking == null) {
            HashMap coloresRanking = new HashMap();

            coloresRanking.put(RowRanking.RANKING_AZUL, loadImage("rAzul.gif"));
            coloresRanking.put(RowRanking.RANKING_GRIS, loadImage("rGris.gif"));
            coloresRanking.put(RowRanking.RANKING_LILA, loadImage("rLila.gif"));
            coloresRanking.put(RowRanking.RANKING_NARANJA,
                    loadImage("rNaranja.gif"));
            coloresRanking.put(RowRanking.RANKING_ROJO, loadImage("rRojo.gif"));
            coloresRanking.put(RowRanking.RANKING_VERDE,
                    loadImage("rVerde.gif"));
            tableRanking = new TableRanking();
            /*tableRanking = new TableRanking(new TableRankingRender(
                    coloresRanking, new Color(255, 255, 255), new Color(240,
                            248, 146)));*/
        }
        return tableRanking;
    }

    protected void loadAppletParameters() {
        //Get the applet parameters.
        //String at = getParameter("img");
        //  dir = (at != null) ? at : "/images";
        String imagedir = getParameter("IMAGEDIR");
        //System.out.println("La dire de imagenes es " + imagedir);
    }

    protected static ImageIcon createAppletImageIcon(String path,
            String description) {
        int MAX_IMAGE_SIZE = 75000; //Change this to the size of
        //your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = new BufferedInputStream(RoomUI.class
                .getResourceAsStream(path));
        if (imgStream != null) {
            byte buf[] = new byte[MAX_IMAGE_SIZE];
            try {
                count = imgStream.read(buf);
            } catch (IOException ieo) {
                System.err.println("Couldn't read stream from file: " + path);
            }

            try {
                imgStream.close();
            } catch (IOException ieo) {
                System.err.println("Can't close file " + path);
            }

            if (count <= 0) {
                System.err.println("Empty file: " + path);
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf),
                    description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * @return Returns the chatPanel.
     */
    public ChatPanel getChatPanel() {
        if (chatPanel == null) {
            chatPanel = new ChatPanel(null);
        }
        return chatPanel;
    }
}

