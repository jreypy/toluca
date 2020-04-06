package py.edu.uca.fcyt.toluca.table;

import java.awt.event.MouseEvent;

/** Listener de eventos en PlayTable */
interface PTableListener
{
	/**
     * Invocado cuando se hizo click en la mesa de juego
     * @param x			posición x de la mesa
     * @param y			posición y de la mesa
     * @param button	botón (ver MouseEvent)
     */
	void mouseClicked(int x, int y, MouseEvent e);
	
	void say(byte say);
}	