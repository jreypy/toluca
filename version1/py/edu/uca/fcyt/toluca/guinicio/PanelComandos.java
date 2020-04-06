package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.TolucaConstants;

/**
 * @author dani
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PanelComandos extends JPanel {

    private RoomClient roomClient = null;

    private TableGame tableGame = null;

    private JButton botonJugar;

    private JButton botonUnirse;

    private JButton botonSalir;

    private JButton botonAyuda;

    private JLabel puntaje;

    public static final Color COLOR_DE_FONDO = new Color(235, 238, 183);

    private JLabel version = null;

    private JApplet applet;

    private JPanel panelPuntaje;

	private JLabel jLmaximo = null;
	private JLabel jLsegundo = null;
	private JLabel jLtercero = null;
	private JLabel jLcuarto = null;
	private JLabel jLquinto = null;
	private JLabel jLsexto = null;

    public PanelComandos() {
        initComponents();
    }

    /**
     * @deprecated Se usa como bean ahora
     */
    public PanelComandos(ImageIcon iconJugar, ImageIcon control,
            ImageIcon salir, ImageIcon o) {

        setPanel();
    }

    /**
     * @param e
     */
    protected void botonAyudaActionPerformed(ActionEvent e) {
        try {
            TolucaConstants.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,
                    "A mostrar la ayuda...");
            getApplet().getAppletContext().showDocument(
                    new URL("http://www.truco.com.py/ayuda.html"), "_blank");
        } catch (MalformedURLException e1) {
            TolucaConstants.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, e1
                    .getMessage());
        }
    }

    /**
     * @param e
     */
    protected void botonSalirActionPerformed(ActionEvent e) {
        getApplet().setVisible(false);
        try {
            System.out.println("Saliendo del applet mmmmmmmmmm");
            getApplet().getAppletContext().showDocument(
                    new URL("http://www.truco.com.py/close.html"));
        } catch (MalformedURLException e1) {
            System.out.println(e1.getMessage());
        }
    }

    private JPanel getPanelBoton(JButton button, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(PanelComandos.COLOR_DE_FONDO);
        panel.add(button);
        //panel.add(new JLabel(text));
        return panel;
    }

    private JPanel getPanelBotonesJugar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(version);
        panel.add(getPanelBoton(botonJugar, "Crear mesa"));
        panel.add(getPanelBoton(botonUnirse, "Unirse"));
        return panel;
    }

    private JPanel getPanelControlGral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(getPanelBoton(botonAyuda, "Ayuda"));
        panel.add(getPanelBoton(botonSalir, "Salir"));
        return panel;
    }

    private JPanel getPuntaje() {
        if (panelPuntaje == null) {
            panelPuntaje = new JPanel();
            jLmaximo = new JLabel();
            jLsegundo = new JLabel();
        	jLtercero = new JLabel();
        	jLcuarto = new JLabel();
        	jLquinto = new JLabel();
        	jLsexto = new JLabel();
        	panelPuntaje.setLayout(new BoxLayout(panelPuntaje, BoxLayout.Y_AXIS));
            panelPuntaje.setForeground(java.awt.Color.white);
            panelPuntaje.setOpaque(true);
            panelPuntaje.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2,2,2,2,java.awt.Color.lightGray), "Trucometro", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.blue));
            panelPuntaje.setBackground(java.awt.Color.white);
            jLmaximo.setText("2000+");
            jLmaximo.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rRojo.gif")));
            jLmaximo.setBackground(java.awt.Color.white);
            jLmaximo.setOpaque(true);
            jLsegundo.setText("1999-1850");
            jLsegundo.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rLila.gif")));
            jLsegundo.setBackground(java.awt.Color.white);
            jLsegundo.setOpaque(true);
            jLtercero.setText("1849-1750");
            jLtercero.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rNaranja.gif")));
            jLtercero.setBackground(java.awt.Color.white);
            jLtercero.setOpaque(true);
            jLcuarto.setText("1749-1650");
            jLcuarto.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rVerde.gif")));
            jLcuarto.setBackground(java.awt.Color.white);
            jLcuarto.setOpaque(true);
            jLquinto.setText("1649-1550");
            jLquinto.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rAzul.gif")));
            jLquinto.setBackground(java.awt.Color.white);
            jLquinto.setOpaque(true);
            jLsexto.setText("1549-");
            jLsexto.setIcon(new ImageIcon(getClass().getResource("/py/edu/uca/fcyt/toluca/images/inicio/rGris.gif")));
            jLsexto.setBackground(java.awt.Color.white);
            jLsexto.setOpaque(true);
            panelPuntaje.add(jLmaximo, null);
            panelPuntaje.add(jLsegundo, null);
            panelPuntaje.add(jLtercero, null);
            panelPuntaje.add(jLcuarto, null);
            panelPuntaje.add(jLquinto, null);
            panelPuntaje.add(jLsexto, null);            
    
        }
        return panelPuntaje;

    }

    private void setPanel() {
        setBackground(PanelComandos.COLOR_DE_FONDO);
        setOpaque(true);
        setLayout(new BorderLayout());

        add(getPanelBotonesJugar(), BorderLayout.NORTH);

        add(getPuntaje(), BorderLayout.CENTER);
        add(getPanelControlGral(), BorderLayout.SOUTH);

    }

    private void initComponents() {
        version = new JLabel(RoomUING.VERSION);
        version.setHorizontalTextPosition(JLabel.LEFT);
        version.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon(getClass().getResource(
                RoomUING.IMAGE_DIR + "btnCrearMesa.gif"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource(
                RoomUING.IMAGE_DIR + "btnUnirse.gif"));
        ImageIcon controlIcon = new ImageIcon(getClass().getResource(
                RoomUING.IMAGE_DIR + "btnAyuda.gif"));
        ImageIcon salirIcon = new ImageIcon(getClass().getResource(
                RoomUING.IMAGE_DIR + "btnSalir.gif"));
//        ImageIcon puntajeIcon = new ImageIcon(getClass().getResource(
//                RoomUING.IMAGE_DIR + "puntaje.gif"));
        botonJugar = new JButton(icon);
        botonUnirse = new JButton(icon2);
        botonSalir = new JButton(salirIcon);
        botonAyuda = new JButton(controlIcon);
        //this.puntaje = new JLabel(puntajeIcon);
        botonJugar.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                botonJugarActionPerformed(e);
            }
        });
        botonUnirse.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonSalir.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonAyuda.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonUnirse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                botonUnirseActionPerformed(e);
            }
        });
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                botonSalirActionPerformed(e);
            }
        });

        botonAyuda.setToolTipText("Ayuda sobre las reglas y el uso en general");
        botonJugar.setToolTipText("Crea una nueva mesa para iniciar un juego");
        botonUnirse
                .setToolTipText("Seleccione una mesa de la lista de la derecha");
        botonSalir.setToolTipText("Sale del servidor y cierra la ventana");
        botonJugar.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonUnirse.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonSalir.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonAyuda.setBackground(PanelComandos.COLOR_DE_FONDO);
        botonAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                botonAyudaActionPerformed(e);
            }
        });
        setPanel();
    }

    /**
     * @param e
     */
    protected void botonJugarActionPerformed(ActionEvent e) {
        String[] opciones = { "18", "30" };
        int opcion = JOptionPane.showOptionDialog(getApplet(),
                "Escoja la cantidad de Puntos para el Juego", "Nuevo Juego",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                opciones, opciones[0]);

        if (opcion == 0) {
            getRoomClient().createTableRequest(18);
        } else
            getRoomClient().createTableRequest(30);

    }

    /**
     * @param e
     */
    protected void botonUnirseActionPerformed(ActionEvent e) {
        int numeroDeFila = getTableGame().getSelectedRow();
        //System.out.println("numeroDeFila=" + numeroDeFila);
        if (numeroDeFila >= 0) {
            numeroDeFila = getTableGame().getNumeroDeMesa(numeroDeFila);
            // System.out.println("////////////// JOINNN /////////////");
            //System.out.println("Se presiono el join..." + numeroDeFila);
            roomClient.joinTableRequest(numeroDeFila);
        }
    }

    /**
     * @return Returns the roomClient.
     */
    public RoomClient getRoomClient() {
        return roomClient;
    }

    /**
     * @param roomClient
     *            The roomClient to set.
     */
    public void setRoomClient(RoomClient roomClient) {
        this.roomClient = roomClient;
    }

    /**
     * @return Returns the tableGame.
     */
    public TableGame getTableGame() {
        return tableGame;
    }

    /**
     * @param tableGame
     *            The tableGame to set.
     */
    public void setTableGame(TableGame tableGame) {
        this.tableGame = tableGame;
    }

    /**
     * @param roomUI
     */
    public void setApplet(JApplet roomUI) {
        applet = roomUI;
    }

    /**
     * @return Returns the applet.
     */
    public JApplet getApplet() {
        return applet;
    }
}