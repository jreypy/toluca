package py.edu.uca.fcyt.toluca.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.state.State;
import py.edu.uca.fcyt.toluca.table.state.StateListener;
import py.edu.uca.fcyt.toluca.table.state.StatesTransitioner;

// representa a una carta en la mesa
class TableCard implements Animable, StateListener
{
	// tamaño de la carta
	final public static int CARD_WIDTH = 88;
	final public static int CARD_HEIGHT = 127;
	final public static int CARD_RADIUS = 78;
	final public static int CARD_RADIUS2 = (int) Math.pow(CARD_RADIUS, 2);
	final public static int CARD_DIAMETER = CARD_RADIUS * 2;
	
	// BufferedImage para el dorso
	final private BufferedImage biBack;
	final private static Hashtable images = getImages();
	
	private BufferedImage biCard;		// BufferedImage para la carta
	private StatesTransitioner states;	// cola de estados de la carta
	private LinkedList stateListeners;	// listeners de eventos
	private BufferedImage[] biOut;
	private AffineTransform afTrans;
	private Graphics2D[] grOut;
	private boolean blackOutline = false;

	/**
     * Construye un TableCard sin carta en él
     */
	public TableCard()
	{
		biBack = createBack();
		states = new StatesTransitioner();
		stateListeners = new LinkedList();
		states.addListener(this);
	}
	
	/** 
	 * Agrega un nuevo estado a la cola de estados a animar,
	 * con duración de transición igual a 'duration'
	 */
	synchronized public void pushState
	(
		int x, int y, double angle, 
		double scale, Card card, long duration
	)
	{
		State state;

		// crea el nuevo estado
		state = new TCardState
		(
			x, y, angle, scale, card
		);
		
		states.pushState(state, duration);
	}
	
	/** 
	 * Crea la imagen del dorso y la retorna
	 */
	private BufferedImage createBack()
	{
		BufferedImage back;

		back = new BufferedImage
		(
			CARD_WIDTH, CARD_HEIGHT,
			BufferedImage.TYPE_3BYTE_BGR
		);

		//Util.copyImage(new ImageIcon(Util.getImagesDir() + "dorso.gif"), back);
		Util.copyImage(new ImageIcon(
		getClass().getResource("/py/edu/uca/fcyt/toluca/images/" + "dorso.gif")), back);

		return back;
	}
	
	public void paint(int buffIndex)
	{
		int centX, centY;
		BufferedImage biCard;
		TCardState cState;
		AffineTransform afTrans, oldAfTrans;
		
		if (this.afTrans == null) return;
		if (this.biOut == null) return; 
		
		// crea una copia de 'afTrans'
		afTrans = new AffineTransform(this.afTrans);
		
		// obtiene el estado actual
		cState = (TCardState) states.getCurrState();
		
		if (cState == null) return;
		
		// obtiene la imagen de la carta
		try
		{
			biCard = (BufferedImage) images.get(cState.card);
		}
		catch (NullPointerException ex)
		{
			biCard = biBack;
		}
		
		// obtiene el centro de 'bfIn'
		centX = (int) CARD_WIDTH / 2;
		centY =	(int) CARD_HEIGHT / 2;

		// escala, translada y rota la imagen
		afTrans.translate
		(
			(int) cState.x,
			(int) cState.y
		);
		afTrans.scale(cState.scale, cState.scale);
		afTrans.rotate(cState.angle);
		afTrans.translate
		(
			(int) - centX,
			(int) - centY
		);
		
		
		if (blackOutline)
		{
			oldAfTrans = grOut[buffIndex].getTransform();
			grOut[buffIndex].setTransform(afTrans); 
	
			grOut[buffIndex].drawRoundRect
			(
				3, 3, CARD_WIDTH-6, CARD_HEIGHT-6, 2, 2 			
			);
			grOut[buffIndex].setTransform(oldAfTrans);
		}
		else
			// dibuja la imagen 'bfIn' en 'bfOut
			new AffineTransformOp
			(
				afTrans, AffineTransformOp.TYPE_BILINEAR
			).filter(biCard, biOut[buffIndex]);
	}
	
	/**
     * Agrega una pausa a la cola de estados
     * @param duration	duración de la pausa
     */
    synchronized public void pushPause(long duration)
    {
    	states.pushPause(duration);
    }
    
    /**
     * Avanza la animación de esta carta
     */
    public void advance()
    {
    	states.advance();
    }
    
    /**
     * Retorna el tiempo restante de animación de la carta
     */
    public long getRemainingTime()
    {
    	return states.getRemainingTime();
    }
	
	/**
     * Retorna el estado actual.
     */
    public TCardState getCurrState() 
    {
    	return (TCardState) states.getCurrState();
    }
    
    /**
     * Retorna un Hashtable con los pares (Card, BufferedImage),
     * donde el BufferedImage la imagen del {@link Card}.
     */
    private static Hashtable getImages()
    {
    	int num;
    	Card card;
    	BufferedImage biCard;
    	ImageIcon iiCard;
    	Hashtable ret;
    	
    	ret = new Hashtable();
    	
    	// genera el Hashtable
    	for (int i = 0; i < 40; i++)
    	{
    		num = i % 10;
    		num += num > 6 ? 2: 0;
    		card = new Card(i / 10 + 1, num + 1);
    		iiCard = card.getImageIcon();
    		biCard = new BufferedImage
    		(
    			iiCard.getIconWidth(),
    			iiCard.getIconHeight(),
    			BufferedImage.TYPE_3BYTE_BGR
    		);
    		Util.copyImage(iiCard, biCard);
    		ret.put(card, biCard);
//			System.out.println("Card added: " + card.hashCode());
    	}
    	
    	return ret;
    }
    
    /**
     * Retorna la carta contenida
     */
    synchronized public Card getCard()
    {
    	Card card;
    	
		try { card = ((TCardState) states.getCurrState()).card; }
		catch (NullPointerException ex) { card = null; } 
		
		return card;
	}
	
	/**
     * Retorna el último estado de este TableCard
     */
    synchronized public TCardState getLastState()
    {
    	return (TCardState) states.getLastState();
    }


	/**
     * Agrega un nuevo listener de eventos.
     * @param obj	Listener de eventos a agregar.
     */
    public void addListener(TableCardListener obj)
    {
    	stateListeners.add(obj);
    }
    
    /**
     * Dispara el evento 
     * {@link StateListener#transitionCompleted()}
     * de cada uno de los listeners registrados.
     */
    private void fireTransitionCompleted(TableCard o)
    {
    	Iterator slIter;
    	
    	slIter = stateListeners.iterator();
    	while (slIter.hasNext())
    		((TableCardListener) slIter.next()).transitionCompleted(this);
    }

	public void transitionCompleted(Object o) 
	{
		fireTransitionCompleted(this);
	}
	
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.animation.Animable#setOut(java.awt.image.BufferedImage, java.awt.geom.AffineTransform)
	 */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		this.biOut = biOut;
		this.afTrans = afTrans;
		grOut = new Graphics2D[biOut.length];
		for (int i = 0; i < biOut.length; i++)
		{
			grOut[i] = biOut[i].createGraphics();
			grOut[i].setStroke(new BasicStroke(5)); 
			grOut[i].setColor(Color.BLACK);
		}
	}

	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.state.StateListener#animationCompleted()
	 */
	public void animationCompleted(Object o)
	{
		// TODO Auto-generated method stub

	}
	
	/** Establece si la carta se muestra o se muestra un borde negro */
	public void setBlackOutline(boolean on)
	{
		blackOutline = on;
	}

}
