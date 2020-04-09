/*
 * PopupTrucoPlays.java
 *
 * Created on March 27, 2003, 10:22 PM
 */

package py.edu.uca.fcyt.toluca.table;

import java.util.Vector;

import py.edu.uca.fcyt.toluca.game.TrucoPlay;

/**
 *
 * @author  Owner
 */
class PopupTrucoPlays extends BasePopupMenu 
{
    /** 
     * Crea una nueva instancia de PopupTrucoPlays
     * @param ptListener 	Listener de eventos de mesa de juego
     * @param aPlays		Vector de Bytes de las jugadas habilitadas
     */
    public PopupTrucoPlays
    (
    	PTableListener ptListener, Vector aPlays, int envidoPoints
    )
    {
    	byte play;
    	String name;
    	
    	for (int i = 0; i < aPlays.size(); i++)
    	{
    		play = ((Byte) aPlays.get(i)).byteValue();
    		name = Util.getPlayName(play);
    		
    		if 
    		(
    			envidoPoints != -1 
    			&& play == TrucoPlay.QUIERO 
    			|| play == TrucoPlay.CANTO_ENVIDO
    		)
    		name = name + " " + envidoPoints;
    					
    		add(new PopupActions(ptListener, play, name));
    	}
    }
}




