package py.edu.uca.fcyt.toluca.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.state.GeneralState;
import py.edu.uca.fcyt.toluca.table.state.StateListener;
import py.edu.uca.fcyt.toluca.table.state.StatesTransitioner;

/**
 * Maneja el dibujo de una carita en el juego
 */
class Face implements Animable, StateListener
{
	public final static int WIDTH = (int) (PlayTable.TABLE_WIDTH * .2);
	public final static int HEIGHT = (int) (PlayTable.TABLE_HEIGHT * .2);

	private StatesTransitioner[] states;
	private BufferedImage faces[];
	private static final int[][][] triPoints = triangPoints();
	private Font[] fonts;
	private Color borderColor;
	private Rectangle txtClearRegion;
	private int clearText;
	private String playerName;
	private double ballAng;
	public boolean highlighted = false;
	private Graphics2D[] grOut;
	private BufferedImage[] biOut;
	private AffineTransformOp afTransOp;
	private AffineTransform afTrans;
	private StatesTransitioner blinker;

	/**
	 * Construye un nuevo Face. 
	 * @param name			nombre del jugador
	 * @param borderColor	color del borde
	 * @param ballAng		ángulo del globo
	 */
	public Face
	(
		String name, Color borderColor, double ballAng
	)
	{ 
		states = new StatesTransitioner[]
		{
			new StatesTransitioner(),
			new StatesTransitioner(),
			new StatesTransitioner()
		};

		this.playerName = name == null ? "": name;
		this.borderColor = borderColor;
		
		this.blinker = new StatesTransitioner();
		blinker.addListener(this);

		animationCompleted(blinker);
		
		fonts = new Font[3];
		fonts[0] = new Font("SansSerif", Font.BOLD, 13);
		fonts[1] = new Font("SansSerif", Font.PLAIN, 13);
		
		this.ballAng = ballAng;
		
		pushSign(Sign.NONE, 0);
		pushText(null, false, 0);
	}
	
	
	/**
     * Retorna el área afectada por el pintado del texto si 
     * debe ser borrado, de lo contrario retorna nulo
     */
    public Rectangle getClearRegion()
    {
    	if (clearText == 1) 
    	{
    		clearText ++;
    		return txtClearRegion;
    	}
    	else return null;
    }
	
	synchronized public void setBorderColor(Color color)
	{
		borderColor = color;
	}
	
	
	/**
     * Agrega una nueva posición a la cola.
     * @param x 	coordenada x de la nueva posición
     * @param y		cooredenada y de la nueva posición
     */
	synchronized public void pushState(FaceState state, long duration)
	{
		states[0].pushState(state, duration);
	}
	
	/**
     * Agrega una pausa a la cola.
     */
	synchronized public void pushPause(long duration)
	{
		states[0].pushPause(duration);
	}
	
	/**
     * Agrega una nueva seña a la cola
     */	
	synchronized public void pushSign(int sign, long duration)
	{
		// agrega la seña a la cola
		states[1].pushState(new SignState(sign), duration);
	}

	/**
     * Agrega una nuevo texto a la cola
     */	
	synchronized public void pushText
	(
		String text, boolean highlighted, long duration
	)
	{
		// agrega la seña a la cola
		states[2].pushState
		(
			new StringState(text, highlighted), duration
		);
	}
	
	synchronized public void paint(int buffIndex)
	{
		int width, height;
		FaceState fState;
		Graphics2D grOut2;

		if (grOut == null) return;
		if (this.afTrans == null) return;

		// obtiene el estado actual
		fState = (FaceState) states[0].getCurrState();
		
		// si no hay estado actual, salir
		if (fState == null) return;
		
		// obtiene una copia del Graphics2D a pintar
		grOut2 = (Graphics2D) grOut[buffIndex].create();
		
		// translada el orígen a la coordenada 
		// (x, y) del cuadradito blanco
		grOut2.translate
		(
			fState.x, fState.y
		);

		// dibuja el cuadradito blanco
		drawChair(grOut2);

		// translada el orígen a la coordenada 
		// (x, y) del centro de los demás objetos
		grOut2.translate
		(
			 fState.offX, fState.offY
		);

		drawName(grOut2);
		drawFace(biOut[buffIndex], grOut2.getTransform());
		drawDialog(grOut2);
		
		grOut2.dispose();
	}


	/* Dibuja el asiento blanquito*/
	synchronized private void drawChair(Graphics2D grOut)
	{
		int x, y;
		float blk;
		
		x = -WIDTH / 2;
		y = -HEIGHT / 2;

		grOut.setStroke(new BasicStroke(6));

		// si la carita está seleccionada, dibujar el 
		// borde amarillo
		if (highlighted)
		{
			try
			{
				blk = (float) ((GeneralState) blinker.getCurrState()).getValue(0);
			}
			catch (NullPointerException ex)
			{
				blk = 1;
			}
			
			grOut.setColor(new Color(1, 1, 0, blk));
			grOut.drawRoundRect
			(
				(int) x - 4, 
				(int) y - 3, 
				(int) WIDTH + 8,
				(int) HEIGHT + 6,
				33, 33
			);	
		}
		
		grOut.setColor(Color.BLACK);
		grOut.drawRoundRect
		(
			(int) x, 
			(int) y, 
			(int) WIDTH, 
			(int) HEIGHT,
			33, 33
		);	

		grOut.setColor(borderColor);
		grOut.drawRoundRect
		(
			(int) (x + 2), 
			(int) (y + 2), 
			(int) (WIDTH - 4), 
			(int) (HEIGHT - 4),
			33, 33
		);	

		grOut.setColor(Color.WHITE);
		grOut.fillRoundRect
		(	
			(int) (x + 3), 
			(int) (y + 3), 
			(int) (WIDTH - 6), 
			(int) (HEIGHT - 6),
			29, 29
		);
	}
	
	/**
     * Dibuja el nombre de la carita
     */
	synchronized private void drawName(Graphics2D grOut)
	{
		int x, y;
		int off;
		String nameShown;
		int shownLen, shownWidth;
		
		// obtiene una copia de grOut
		grOut = (Graphics2D) grOut.create();
		
		grOut.setFont(fonts[0]);
		
		shownLen = Util.getNChars(playerName, 0, WIDTH - 40, grOut);
		
		nameShown = playerName.substring(0, shownLen);
		shownWidth = grOut.getFontMetrics().stringWidth
		(
			nameShown
		);
		
		x = (int) (-shownWidth / 2);
		y = (int) (HEIGHT * .42);
		
		grOut.setColor(Color.BLACK);	
		grOut.drawString(nameShown, x, y);
		
		grOut.dispose();
	}
	
	/**
     * Devuelve verdadero si x e y están dentro del cuadrito.
     * Las coordenadas pasadas deben ser las transformadas por 
     * el AffineTransform del Graphics2D en donde se pintó 
     * la carita.
     * @param x	coordenada x ya transformada
     * @param y coordenada y ya transformada
     */
	synchronized public boolean inside(int x, int y)
	{
		FaceState cState;
		Point2D p;
		
		// obtiene el estado actual, si es nulo sale
		try	{ cState = (FaceState) states[0].getCurrState(); }
		catch (NullPointerException ex) { return false; }

		x -= cState.x;
		y -= cState.y;		
		
		return 
			x >= -WIDTH / 2 &&
			y >= -HEIGHT / 2 &&
			x <=  WIDTH / 2 &&	
			y <= HEIGHT / 2 ;
	}
			
	/**
     * Devuelve las coordenadas de 2 triángulos equilátero, el
     * segundo más chico que el primero.
     * @return 	Una matriz de coordenadas, donde la posición
     *			i = 0, 1 para el primer y segundo triáng. resp.
     * 			j = 0, 1 para x e y respectivamente, y 
     *			k = 0, 1, 2 para el primer, segundo y tercer punto.
     *			Ejemplo: (1, 1, 1) = coordenada y del punto 1 del 
     *			segundo triángulo
     */	
	private static int[][][] triangPoints()
	{
		int[][][] ret;
		
		ret = new int[][][]
		{
			new int[][]{new int[3], new int[3]}, 
			new int[][]{new int[3], new int[3]}
		};
		
		for (int i = 0; i < 3; i++)
		{
			ret[0][0][i] = (int) (20 * Math.cos(Math.PI / 3 * i * 2 + .5));
			ret[0][1][i] = (int) (20 * Math.sin(Math.PI / 3 * i * 2 + .5));

			ret[1][0][i] = (int) (18 * Math.cos(Math.PI / 3 * i * 2 + .5));
			ret[1][1][i] = (int) (18 * Math.sin(Math.PI / 3 * i * 2 + .5));
		}
		
		return ret;
	}
		
	
	/** Este método es llamado cuando un jugador canta alguna jugada
	 *recibe como argumentos un bufferImage, la accion que debe cantar
	 *y el numero de jugador, o sea su posicion en la mesa*/
	 
	synchronized private void drawDialog(Graphics2D grOut)
	{
		int width, height;
		int textWidth = 0, textHeight = 0;
		int[] xPoints, yPoints;
		StringState sState;
		String text;
		String[] lines;
		FontMetrics fMetrics;
		int triAng = 0, triOffset = 0;
		int lineHeight, lineWidth;
		int lineAscent;
		Graphics2D grBall, grTri;
		boolean highlighted;
		
		// obtiene el estado actual
		sState = (StringState) states[2].getCurrState();
		
		// obtiene el estado resaltado
		highlighted = sState.isHighlighted();
		
		// obtiene el texto actual, si no hay texto actual sale
		text = sState.getText();
		if (text == null) return;
		
		// obtiene nuevos Graphics2D basados en 'biOut' y 'afTrans'
		grBall = (Graphics2D) grOut.create();
		grTri = (Graphics2D) grOut.create();

		// resaltar el texto o no
		if (highlighted)
			grBall.setFont(fonts[0]);
		else
			grBall.setFont(fonts[1]);

		// obtiene las líneas de texto a mostrar
		lines = Util.getLines(text, WIDTH, grBall);
		
		// obtiene las medidas de la fuente actual
		fMetrics = grBall.getFontMetrics();
		
		// obtiene el ancho y el alto del globito
		for (int i = 0; i < lines.length; i++)
		{
			lineWidth = fMetrics.stringWidth(lines[i]);
			if (lineWidth > textWidth)
				textWidth = lineWidth;
		}
		
		lineHeight = fMetrics.getHeight();
		textHeight = lineHeight * lines.length;
		lineAscent = fMetrics.getAscent();
			
		// añade 50 puntos al ancho y al alto
		width = textWidth + 50;
		height = textHeight + 50;

		grBall.translate
		(
			(width * .5 + 40) * Math.sin(ballAng), 
			(height * .5 + 40) * Math.cos(ballAng)
		);
		grTri.rotate(-ballAng);
		grTri.translate(0, 40);
		grTri.scale((double) height / width, 1);
		
		// se establece el color negro
		grTri.setColor(Color.BLACK);
		grBall.setColor(Color.BLACK);
		

		// se dibuja la elipse y el triangulito
		grBall.fillOval
		(
			-width / 2,
			-height / 2,
			width, 
			height
		);
		grTri.fillPolygon(triPoints[0][0], triPoints[0][1], 3);

		// se establece el color a amarillo
		grBall.setColor(Color.YELLOW);
		grTri.setColor(Color.YELLOW);

		// se dibuja la elipse y el triangulito
		grTri.fillPolygon(triPoints[1][0], triPoints[1][1], 3);
		grBall.fillOval
		(
			(int) (-width / 2.0 + 1),
			(int) (-height / 2.0 + 1), 
			width - 2, height - 2
		);

		// resaltar el texto o no
		if (highlighted)
			grBall.setColor(Color.RED.darker());	
		else
			grBall.setColor(Color.BLACK);
		
		// se dibujas las líneas de texto
		for (int i = 0; i < lines.length; i++)
			grBall.drawString
			(
				lines[i],
				(int) (-textWidth / 2),
				lineHeight * i - textHeight / 2 + lineAscent
			);
		
		grBall.dispose();
		grTri.dispose();
	}


	
	/** 
	 * Establece el nombre de la carita
	 * @param name	nombre de la carita
	 */
	synchronized public void setName(String name)
	{
		playerName = name;
	}

	synchronized public void advance() 
	{
		states[0].advance();
		states[1].advance();
		states[2].advance();
		
		blinker.advance();
	}
	
	/**
     * Retorna una copia del estado actual
     */
	synchronized private FaceState getCurrState()
	{
		return (FaceState) states[0].getCurrState();
	}
	
	/**
     * Carga el vector de caritas
	 * @deprecated Usar loadFacesFromURL (applet compilant)
     */
	synchronized public void setFacesDir(String dir)
	{
		ImageIcon fIcon;
		
		if (dir == null)
			faces = null;
		else
		{
			faces = new BufferedImage[10];
			
			for (int i = 0; i < faces.length; i++)
			{
				fIcon = new ImageIcon
				(
					dir + i + ".jpg"
				);
	
				faces[i] = new BufferedImage
				(
					fIcon.getIconWidth(), fIcon.getIconHeight(),
					BufferedImage.TYPE_3BYTE_BGR
				);
	
				Util.copyImage(fIcon, faces[i]);
			}
		}
	}
	
	/**
	 * Carga el vector de caritas
	 */
	synchronized public void loadFacesFromURL(String baseURL)
	{
	    ImageIcon fIcon;
	    
	    if (baseURL == null)
		faces = null;
	    else
	    {
		faces = new BufferedImage[10];
		
		for (int i = 0; i < faces.length; i++)
		{
		    fIcon = new ImageIcon
		    (
		    getClass().getResource(baseURL + i + ".jpg")
		    );
		    if(fIcon != null)
		    {			
			faces[i] = new BufferedImage
			(
			fIcon.getIconWidth(), fIcon.getIconHeight(),
			BufferedImage.TYPE_3BYTE_BGR
			);
			
			Util.copyImage(fIcon, faces[i]);
		    } else
		    {
			System.out.println("No se pudo crear el Icon con : " + baseURL);
			System.out.println("El URL intentado fue: " + baseURL + i + ".jpg");
		    }
		}
	    }
	}
	

	/**
     * Dibuja la carita 
     * @param biOut		lugar donde se dibujará la carita
     * @param afTrans	AffineTransform a utizar
     */
	synchronized private void drawFace
	(
		BufferedImage biOut, AffineTransform afTrans
	) 
	{
		int sign;
		SignState sState;
		
		if (faces == null) return;
		
		sState = (SignState) states[1].getCurrState();
		
		if (sState == null) return;
		
		// obtiene la seña
		sign = sState.getSign();

		// escala la imagen;
		afTrans.scale(.8, .8);
		
		// translada la carita a su posición
		afTrans.translate
		(
			-faces[sign].getWidth() / 2,
			-faces[sign].getHeight() / 2
		);
		
		new AffineTransformOp
		(
			afTrans, AffineTransformOp.TYPE_BILINEAR
		).filter(faces[sign], biOut);
	}
	
	/**
     * Retorna el tiempo restante de animación
     */
    synchronized public long getRemainingTime()
    {
    	return states[0].getRemainingTime();
    }
    
    /**
     * Retorna un Graphics2D con transformación 
     * afTrans y antialiasing
     */
    private Graphics2D getGraphics2D
    (
    	BufferedImage biOut, AffineTransform afTrans
    )
    {
    	Graphics2D ret;

		ret = biOut.createGraphics();
		ret.setTransform(afTrans);

		// establece el antialiasing al Graphics2D
		ret.setRenderingHint
		(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		
		return ret;
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.animation.Animable#setOut(java.awt.image.BufferedImage, java.awt.geom.AffineTransform)
	 */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		int width, height;
		FaceState fState;
		
		grOut = new Graphics2D[biOut.length];
		
		for (int i = 0; i < grOut.length; i++)
		{
			// obtiene el graficador
			grOut[i] = (Graphics2D) biOut[i].getGraphics();
			
			// transforma la salida
			grOut[i].setTransform(new AffineTransform(afTrans));
			
			// establece el antialiasing al Graphics2D
			grOut[i].setRenderingHint
			(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
			);
		}
		
		this.biOut = biOut;
		this.afTrans = afTrans; 
	}

	public void animationCompleted(Object o)
	{
		blinker.pushState(new GeneralState(new double[]{1}), 500);
		blinker.pushState(new GeneralState(new double[]{0}), 500);
	}

	public void transitionCompleted(Object o)
	{
	}

}




