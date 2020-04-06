/*
 * Created on 31/08/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.state.GeneralState;
import py.edu.uca.fcyt.toluca.table.state.StateListener;
import py.edu.uca.fcyt.toluca.table.state.StatesTransitioner;


/**
 * @author Roberto
 *
 * Maneja el texto animado
 */
class TableText implements Animable, StateListener
{
	private StatesTransitioner tStates;
	private String text;
	private Font font;
	private Graphics2D grOut[];
	private int offX, offY;
	private Shape outLine;  
	private int width, height;
	private float penWidth; 
	private double fillAngle;
	private int fillX, fillY;
	private Vector sListeners;
	private static int indX = 0;
	private static int indY = 1;
	private static int indAngle = 2;
	private static int indScale = 3;
	private static int indColors = 4;
	
	/** Crea un nuevo objeto TableText con string </code>text</code> */
	public TableText
	(
		String text, Font font, float penWidth, double fillAngle
	)
	{
		tStates = new StatesTransitioner();
		this.text = text;
		this.font = font;
		this.penWidth = penWidth;
		this.fillAngle = fillAngle;

		fillX = (int) (.3 * font.getSize() * Math.cos(fillAngle));
		fillY = (int) (.3 * font.getSize() * Math.sin(fillAngle));
		
		tStates.addListener(this);
		sListeners = new Vector();
	}

	/** Crea un nuevo objeto TableText con string </code>text</code> */
	public TableText
	(
		char c, Font font, int penWidth, double fillAngle
	)
	{
		this(new String(new char[]{c}), font, penWidth, fillAngle);
	}	
	
	/** Registra un nuevo {@link StateLstener} */
	public void addStateListener(StateListener sListener)
	{
		sListeners.add(sListener);
	}
	
	/** Agrega un nuevo estado a la cola de estados */
	public void pushState
	(
		int x, int y, double angle, double scale, long duration,
		Color[] colors
	)
	{
		double[] values;
		float[] colValues;
		int index = 0, i, j;
		
		values = new double[4 + 4 * colors.length];
		
		values[index++] = (double) x;
		values[index++] = (double) y;
		values[index++] = angle;
		values[index++] = scale;
		
		for (i = 0; i < colors.length; i++)
		{
			colValues = colors[i].getComponents(null);
			for (j = 0; j < 4; j++)
				values[index++] = (double) colValues[j];
		}
		
		tStates.pushState
		(
			new GeneralState(null, values),	duration
		);
	}
	
	/** Agrega una pausa */
	public void pushPause(long duration)
	{
		tStates.pushPause(duration);
	}

	public void advance() 
	{
		tStates.advance();
	}

	synchronized public void paint(int buffIndex) 
	{
		Graphics2D grOut2;
		GeneralState aState;
		Color[] colors;
		
		if (grOut == null) return;

		// crea una copia de la salida
		grOut2 = (Graphics2D) grOut[buffIndex].create();
		
		// obtiene el estado actual
		aState = (GeneralState) tStates.getCurrState();
		
		if (aState == null) return;

		// transforma la salida
		grOut2.translate
		(
			aState.getValue(indX), 
			aState.getValue(indY)
		);
		grOut2.scale
		(
			aState.getValue(indScale), 
			aState.getValue(indScale)
		);
		grOut2.rotate(aState.getValue(indAngle));

		// establece el ancho del borde de pintado a 3
		grOut2.setStroke(new BasicStroke(penWidth));
		
		colors = new Color[(aState.getValCount() - 4) / 4];
		for (int i = 0; i < colors.length; i++)
		{
			colors[i] = new Color
			(
				(float) aState.getValue(indColors + 4 * i),
				(float) aState.getValue(indColors + 4 * i + 1),
				(float) aState.getValue(indColors + 4 * i + 2),
				(float) aState.getValue(indColors + 4 * i + 3)
			);
		}
		
		// si hay sólo dos colores en el estado, es un sólido
		if (colors.length == 2)
			grOut2.setColor(colors[1]);
		// si hay 3, es un gradiente
		else if (colors.length == 3)
		{
			grOut2.setPaint
			(
				new GradientPaint
				(
					-fillX, -fillY,	colors[1], 
					fillX, fillY, colors[2]
				)
			);
		}
		else
			throw new IllegalStateException
			(
				"Cantidad de colores inválida"
			);
		
		// pinta el relleno del texto
		grOut2.fill(outLine);
		
		// pinta el borde del texto
		grOut2.setColor(colors[0]);
		grOut2.draw(outLine);
//		grOut2.fillOval(-5, -5, 11,11);
		
		// libera el objeto Graphics2D
		grOut2.dispose();
	}

	synchronized public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		FontMetrics fMetrics;
		int txtAsc, txtDesc;
		
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
			
			grOut[i].setFont(font);
		}
			
		// obtiene las medidas de la fuente
		fMetrics = grOut[0].getFontMetrics();
		txtAsc = fMetrics.getAscent();
		txtDesc = fMetrics.getDescent();
		
		// carga el ancho horizontal del texto
		width = (int) fMetrics.getStringBounds
		(
			text, grOut[0]
		).getWidth();
		
		// carga el ancho vertical del texto
		height = txtAsc + txtDesc; 
 
		// calcula la posición x, y del texto
		offX = -width / 2;
		offY = (int) ((txtAsc - txtDesc) / 2.0);

		// obtiene el outline de la fuente
		outLine = font.createGlyphVector
		(
			grOut[0].getFontRenderContext(), text
		).getOutline(offX, offY);
	}

	/**
	 * @return El texto a pintar.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @return El ancho vertical del texto.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @return El ancho horizontal del texto.
	 */
	public int getWidth()
	{
		return width;
	}

	public void transitionCompleted(Object o)
	{
		for (int i = 0; i < sListeners.size(); i++)
			((StateListener) sListeners.get(i)).transitionCompleted(this);
	}	
	
	public void animationCompleted(Object o)
	{
		for (int i = 0; i < sListeners.size(); i++)
			((StateListener) sListeners.get(i)).animationCompleted(this);
	}

	/** Retorna el tiempo que falta para que termine 
	 * la animación */ 
	public long getRemainingTime()
	{
		return tStates.getRemainingTime();
	}
}
