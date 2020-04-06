package py.edu.uca.fcyt.toluca.table;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.table.state.State;

/**
 * Aloja el estado de una carta
 * @author Grupo Interfaz de Juego
 */
class TCardState extends BasicState
{
	public Card card;	// carta contenida

	/**
     * Construye un nuevo TCardState con los parámetros pasados
     */	
	public TCardState
	(
		double x, double y, double angle, 
		double scale, Card card
	)
	{
		super(x, y, angle, scale);
		this.card = card;
	}
	
	/**
     * Construye un nuevo TCardState a partir de otro 
     */
    public TCardState(TCardState state)
    {
    	super(state);
    	card = state.card;
    }
    
    public State getCopy()
    {
    	return (State) new TCardState(this);
    }

    public void transition(State state, double ratio)
    {
    	super.transition(state, ratio);
    	this.card = ((TCardState) state).card;
    }
} 
