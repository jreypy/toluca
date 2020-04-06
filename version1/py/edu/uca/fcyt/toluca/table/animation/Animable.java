
package py.edu.uca.fcyt.toluca.table.animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


/**
 * Esta interfaz representa a un objeto animable en la mesa
 * @author Grupo Interfaz de Juego
 */
public interface Animable
{
	/** Establece la salida y su transformación */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans);
	
	/** 
	 * Pinta el objeto.
	 * 
	 * El pintado se realiza sobre el estado actual del objeto.
	 * @param index		Índice del bufer en el cual pintar.
	 * 
	 * @see #advance() advance
	 */
	public void paint(int buffIndex);
	
    /**
     * Avanza el estado actual del objeto. Antes de avanzar
     * se guarda el estado anterior del objeto, el cual
     * es utilizado por {@link #clear(Graphics2D)}. El nuevo
     * estado es utilizado por 
     * {@link #paint(BufferedImage biOut, AffineTransform afTrans)}
     */
	public void advance();
}