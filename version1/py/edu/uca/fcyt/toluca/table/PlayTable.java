/* PlayTablee.java
 * Created on Feb 9, 2005
 *
 * Last modified: $Date: 2005/02/23 19:32:57 $
 * @version $Revision: 1.10 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.table;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import py.edu.uca.fcyt.toluca.table.animation.Animator;
import py.edu.uca.fcyt.toluca.table.animation.ObjectsPainter;

/**
 * 
 * @author afeltes
 *  
 */
public class PlayTable extends JPanel {

    private boolean paintIt = false;

    private BufferedImage[] biBuff = null; // Búfer de la mesa a pintar

    private AffineTransform afTrans = null; // Transformación a aplicar

    private Image img = null;

    private Graphics2D grImg = null;

    private Vector oPainter = null;

    private int currBuff = 0;

    /** dimensiones de la mesa */
    final public static int TABLE_WIDTH = 676;

    final public static int TABLE_HEIGHT = 489;

    private int centerX = TABLE_WIDTH / 2;

    private int centerY = TABLE_HEIGHT / 2;

    // desplazamiento de la salida
    public int offsetX, offsetY;

    private PTableListener ptListener = null; // list. de eventos

    private Animator animator = null; // animador de cartas

    private Toolkit toolkit = null;

    private Object waitFor = null;

    public static void main(String[] args) {
    }

    /**
     * This is the default constructor
     */
    public PlayTable() {
        super();
        initialize();
    }

    /**
     * @param table
     */
    public PlayTable(Table table) {

        // TODO Auto-generated constructor stub
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
    }

    public int getBuffIndex() {
        return (currBuff + 1) % biBuff.length;
    }

    public PTableListener getPtListener() {
        return ptListener;
    }

    public void setPtListener(PTableListener ptListener) {
        this.ptListener = ptListener;
    }

    /**
     *  
     */
    public void inicializar() {
        setDoubleBuffered(false);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                pTablecomponentResized(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
             */
            public void mousePressed(MouseEvent e) {
                pTableMousePressed(e);
            }
        });
        //		this.ptListener = ptListener;
        toolkit = getToolkit();
        biBuff = new BufferedImage[2];
        afTrans = new AffineTransform();
        animator = new Animator(this);
        oPainter = new Vector();
        addListener(animator);
    }

    /**
     * Captura el evento click del mouse y llama a mouseClicked de 'ptListener'
     * con las coordenadas transformadas y el MouseEvent 'e'
     */
    protected void pTableMousePressed(MouseEvent e) {
        Point2D p;

        try {
            p = afTrans.createInverse().transform(
                    new Point(e.getX() - offsetX, e.getY() - offsetY), null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        getPtListener().mouseClicked((int) p.getX(), (int) p.getY(), e);
    }

    /**
     * Evento de cambio de tamaño del componente
     * 
     * @param e
     */
    protected void pTablecomponentResized(ComponentEvent e) {
        Rectangle bounds;
        double scale;

        //    		img = this.createImage(1000, 1000);
        //    		grImg = (Graphics2D) img.getGraphics();
        //    		
        //    		grImg.fillOval(0, 0, 100, 100);

        // obtiene las coordenadas del área de pintado
        bounds = getBounds();

        // establece la escala y el desplazamiento de la mesa
        // con respecto al Graphic donde se la dibujará
        if (bounds.getWidth() / bounds.getHeight() < (double) TABLE_WIDTH
                / TABLE_HEIGHT) {
            scale = bounds.getWidth() / TABLE_WIDTH;
            offsetX = 0;
            offsetY = (int) (bounds.getHeight() - (TABLE_HEIGHT * scale)) / 2;
        } else {
            scale = bounds.getHeight() / TABLE_HEIGHT;
            offsetX = (int) (bounds.getWidth() - (TABLE_WIDTH * scale)) / 2;
            offsetY = 0;
        }

        // crea los BufferedImages
        for (int i = 0; i < biBuff.length; i++)
            biBuff[i] = new BufferedImage((int) (TABLE_WIDTH * scale),
                    (int) (TABLE_HEIGHT * scale), BufferedImage.TYPE_3BYTE_BGR);

        // crea la transformación y la carga
        afTrans = new AffineTransform();
        afTrans.scale(scale, scale);
        afTrans.translate(TABLE_WIDTH / 2, TABLE_HEIGHT / 2);

        fireSetOut();
    }

    /**
     * @return
     */
    public Animator getAnimator() {
        return animator;
    }

    /**
     * @param type
     */
    public void setCursor(int type) {
        setCursor(new Cursor(type));
    }

    public void addListener(ObjectsPainter obj) {
        oPainter.add(obj);
    }

    /** rutina de pintado */
    public void paintComponent(Graphics g) {
        synchronized (animator) {
            super.paintComponent(g);
            if (animator.drawComplete)
                switchImages();
            g.drawImage(biBuff[currBuff], offsetX, offsetY, this);
        }
    }

    private void switchImages() {
        if (animator.drawComplete)
            currBuff = (currBuff + 1) % biBuff.length;
    }

    /**
     * Dispara el evento {@see ObjectsPainter.setOut(BufferedImage biOut,
     * AffineTransform afTrans)} de todos los {@see ObjectsPainter}s
     * registrados.
     */
    private void fireSetOut() {
        for (int i = 0; i < oPainter.size(); i++)
            ((ObjectsPainter) oPainter.get(i)).setOut(biBuff, afTrans);
    }
    
}