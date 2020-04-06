package py.edu.uca.fcyt.toluca.guinicio;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PanelGradiente extends JLabel {

    private Color startColor;

    private int inc;

    private ImageIcon logo;// = RoomUING.loadImage("LogoSinFondo.gif");;

    private String nombre;

    private double largo;

    /**
     * @return Returns the nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre
     *            The nombre to set.
     */
    public void setNombre(String nombre) {

/*        this.nombre = "Bienvenido " + nombre;
        repaint();*/
        setText("Bienvenido " + nombre);
    }

    /**
     *  
     */
    public PanelGradiente() {
        initialize();
    }

    /**
     *  
     */
    private void initialize() {
        setStartColor(new Color(50, 255, 50));
        setLargo(50);
        setNombre("Dani Cricco");
        setInc(50);
        setBackground(Color.BLACK);
        setForeground(Color.GREEN);
        setOpaque(true);
        setHorizontalTextPosition(JLabel.RIGHT);
        setFont(new Font("Arial", Font.ITALIC, 18));
    }

    /*
     * public Dimension getMinimumSize() { return new Dimension(400,
     * logo.getIconHeight()); }
     * 
     * public Dimension getPreferredSize() { return new Dimension((int) largo,
     * logo.getIconHeight()); }
     */
    protected void paintComponent_(Graphics g) {

        int margen = 5;

        //Rectangle r = getBounds(null);

        Rectangle r = g.getClipBounds();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, r.width, r.height);
        Font font = new Font(null, Font.ITALIC, 18);
        g.setFont(font);

        Color c = startColor;
        int x = 0;
        if (logo != null)
            x += logo.getIconWidth();
        //System.out.println(" Alto = "+r.height + " ancho "+r.width);

        //int
        // xComienzo=r.width-g.getFontMetrics().charsWidth(nombre.toCharArray(),0,nombre.length());
        int xComienzo = r.width - inc - margen;
        int ancho = g.getFontMetrics().getHeight();

        x = xComienzo;
        int yLinea = r.height / 2 + logo.getIconHeight() / 2 - ancho;
        while (x >= logo.getIconWidth()) {
            g.setColor(c);
            // System.out.println(x);
            g.fillRect(x, yLinea, inc, ancho);
            c = c.darker();

            x -= inc;
        }

        g.setColor(startColor);
        g.drawLine(0, yLinea + ancho, r.width - margen, yLinea + ancho);

        //  g.fillRect(xComienzo+inc,r.height/2-ancho/2,r.width-(xComienzo+inc),ancho);

        g.setColor(new Color(255, 255, 255));
        g.drawString(nombre, r.width
                - margen
                - g.getFontMetrics().charsWidth(nombre.toCharArray(), 0,
                        nombre.length()), yLinea + ancho);
        if (logo != null)
            g.drawImage(logo.getImage(), 0, r.height / 2 - logo.getIconHeight()
                    / 2, new Color(0, 0, 0), this);
    }

    /**
     * @return Returns the inc.
     */
    public int getInc() {
        return inc;
    }

    /**
     * @param inc
     *            The inc to set.
     */
    public void setInc(int inc) {
        this.inc = inc;
    }

    /**
     * @return Returns the largo.
     */
    public double getLargo() {
        return largo;
    }

    /**
     * @param largo
     *            The largo to set.
     */
    public void setLargo(double largo) {
        this.largo = largo;
    }

    /**
     * @return Returns the logo.
     */
    public ImageIcon getLogo() {
        return logo;
    }

    /**
     * @param logo
     *            The logo to set.
     */
    public void setLogo(ImageIcon logo) {
        //        this.logo = logo;
        //        setMinimumSize(new Dimension(logo.getIconWidth(),
        // logo.getIconHeight()));
        setIcon(logo);
    }

    /**
     * @return Returns the startColor.
     */
    public Color getStartColor() {
        return startColor;
    }

    /**
     * @param startColor
     *            The startColor to set.
     */
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }
}