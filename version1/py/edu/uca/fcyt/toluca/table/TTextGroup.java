/*
 * Created on 02/09/2003
 *
 * Maneja una línea de texto.
 */
package py.edu.uca.fcyt.toluca.table;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.state.StateListener;

/**
 * @author Roberto
 *
 * Maneja conjunto de de {@link TableText}s.
 */
class TTextGroup implements Animable, StateListener
{
	// TableTexts a manejar
	private Vector tTexts;
	private Vector finished;
	private Vector sListeners;
	private int x, y;
	private static int count = 0;
	
	
	/** Crea un nuevo objeto TTextGroup */
	public TTextGroup(int x, int y)
	{
		tTexts = new Vector();
		finished = new Vector();
		sListeners = new Vector();
		this.x = x;
		this.y = y;
	}
	
	/** Agrega un nuevo {@link StateListener} */
	public void addStateListener(StateListener sListener)
	{
		sListeners.add(sListener);
	}
	
	/** Agrega un texto a la línea de texto */
	public void addTText(TableText tText)
	{
		tTexts.add(tText);
		tText.addStateListener(this);
	}

	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		if (biOut == null) return;
		afTrans = new AffineTransform(afTrans);
		afTrans.translate(x, y);
		for (int i = 0; i < tTexts.size(); i++)
			((TableText) tTexts.get(i)).setOut(biOut, afTrans);
	}

	public void paint(int buffIndex)
	{
		for (int i = 0; i < tTexts.size(); i++)
			((TableText) tTexts.get(i)).paint(buffIndex);
	}

	public void advance()
	{
		for (int i = 0; i < tTexts.size(); i++)
			((TableText) tTexts.get(i)).advance();
	}
	public void animationCompleted(Object o)
	{
		finished.add(o); 
		if (finished.size() == tTexts.size()) 
			for (int i = 0; i < sListeners.size(); i++)
				((StateListener) sListeners.get(i)).animationCompleted(this);
	}

	public void transitionCompleted(Object o)
	{
		// TODO Auto-generated method stub

	}
	
	/** Retorna el tiempo que falta para que termine 
	 * la animación */ 
	public long getRemainingTime()
	{
		long maxTime = 0, aTime;
		
		for (int i = 0; i < tTexts.size(); i++)
		{
			aTime = ((TableText) tTexts.get(i)).getRemainingTime();
			if (maxTime < aTime) maxTime = aTime;
		}
		
		return maxTime;
	}
	
	/** Retorna el 'index'-ésimo {@link TableText} */
	public TableText getTText(int index)
	{
		return (TableText) tTexts.get(index);
	}
	
	/** Retorna la cantidad de {@link TableText} agregados */
	public long getTTCount()
	{
		return tTexts.size();
	}

	/** Normaliza la duración de todos los TableText
	 * y agrega una pausa extra.
	 * Agrega una pausa a todos los TableTexts de tal
	 * manera que todas terminen al mismo tiempo. El
	 * TableText de mayor duración no tiene pausa agregada.
	 * Finalmente agrega una pausa de duración 
	 * <code>duration</code>  
	 */
	public void pushNormalizedPause(long duration)
	{
		long remTime;
		TableText tText;
		
		remTime = getRemainingTime();
		
		for (int i = 0; i < tTexts.size(); i++)
		{
			tText = (TableText) tTexts.get(i);
			tText.pushPause
			(
				remTime - tText.getRemainingTime() + duration
			);
		}
	}

	/** Agrega una pausa a todos los {@link TableText}s */
	public void pushPause(long duration)
	{
		for (int i = 0; i < tTexts.size(); i++)
		{
			((TableText) tTexts.get(i)).pushPause(duration);
		}
	}
}
