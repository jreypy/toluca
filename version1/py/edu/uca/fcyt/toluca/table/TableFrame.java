/*
 * Created on 08/09/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.table;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.state.GeneralState;
import py.edu.uca.fcyt.toluca.table.state.StateListener;
import py.edu.uca.fcyt.toluca.table.state.StatesTransitioner;


/**
 * @author Roberto
 *
 * Pinta un borde sobre la mesa. 
 */
class TableFrame implements Animable, StateListener
{
	public int highlight = 2;
	BufferedImage[] biOut;
	AffineTransform afTrans;
	StatesTransitioner sTrans;
	double dColors[][];
	
	public TableFrame()
	{
		sTrans = new StatesTransitioner();
		
		dColors = new double[][]
		{
			// colores opacos
			new double[]
			{
				.17, 1, .5,
				0, 0, .25
			},

			// colores brillantes
			new double[]
			{
				.17, .25, 1,
				0, 0, .5
			},
			
			// colores predeterminados
			new double[]
			{
				.17, 1, 1,
				0, 0, .25
			},
		};

		sTrans.pushState(new GeneralState(getCopy(dColors[2])), 0);
		sTrans.addListener(this);
	}
	
	public void flash(boolean on)
	{
		if (highlight == 2 && !on) return;
		 
		if (on)
		{
			highlight = 0;
			transitionCompleted(sTrans);
		}
		else
		{
			highlight = 2;
			sTrans.pushState
			(
				new GeneralState(getCopy(dColors[2])), 
				500
			);
		}
	}

	public void paint(int buffIndex)
	{
		Graphics2D gr2D;
		Color[] colors;
		GeneralState aState;
		int w, h;
		
		w = 676;
		h = 489;
		
		// obtiene el estado actual
		aState = (GeneralState) sTrans.getCurrState();
		
		// si la salida o el estado es nulo, salir
		if (biOut == null || aState == null) return;
		
		// obtiene el BufferedImage y el Graphics2D búfer
		gr2D = biOut[buffIndex].createGraphics();
		
		colors = new Color[2];
		for (int i = 0; i < 2; i++)
		{
			colors[i] = Color.getHSBColor
			(
				(float) aState.getValue(i * 3),
				(float) aState.getValue(i * 3 + 1),
				(float) aState.getValue(i * 3 + 2)
			); 
		}
		
//		System.out.println("col: " + aState.getValue(1));
		
		
		gr2D.setTransform(afTrans);

		setGradient(gr2D, 0, 5, colors);
		gr2D.translate(0, -h / 2 + 4);
		drawGradientBorder(gr2D, w, false);

		setGradient(gr2D, 0, -5, colors);
		gr2D.translate(0, h - 8);
		gr2D.rotate(Math.PI);
		drawGradientBorder(gr2D, w, false);

		setGradient(gr2D, 0, 5, colors);
		gr2D.translate(w/2 - 3, h/2 - 4);
		gr2D.rotate(Math.PI / 2);
		drawGradientBorder(gr2D, h+2, true);

		setGradient(gr2D, 0, -5, colors);
		gr2D.translate(2, w - 6);
		gr2D.rotate(Math.PI);
		drawGradientBorder(gr2D, h+2, true);
		
		gr2D.dispose();
	}

	private void setGradient
	(
		Graphics2D grOut, int x, int y, Color[] colors
	)
	{
		grOut.setPaint
		(
			new GradientPaint
			(
				-x, -y, colors[0], 
				x, y, colors[1]
			)
		);
	}
	
	/** Dibuja un borde con gradiente */
	protected void drawGradientBorder
	(
		Graphics2D grOut, int lenght, boolean narrow
	)
	{
		int x[], y[];
		int l, w;
		
		l = lenght / 2;
		w = narrow ? 8 : 0;
		
		x = new int[]{-l, l, l - w, -l + w};
		y = new int[]{-4, -4, 4, 4};
		
		grOut.fillPolygon(x, y, 4);
	}

	public void advance()
	{
		sTrans.advance();
	}

	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		this.biOut = biOut;
		this.afTrans = afTrans;
	}
	
	// retorna una copia de 'in'
	private double[] getCopy(double[] in)
	{
		double[] ret;
		
		ret = new double[in.length];
		
		System.arraycopy(in, 0, ret, 0, in.length);
		return ret;
	}

	public void animationCompleted(Object o)
	{
	}

	public void transitionCompleted(Object o)
	{
		if (highlight != 2) 
		{
			sTrans.pushState
			(
				new GeneralState
				(
					getCopy(dColors[highlight % 2])
				), 500
			);
			highlight = (highlight + 1) % 2;
		}
	}
}
