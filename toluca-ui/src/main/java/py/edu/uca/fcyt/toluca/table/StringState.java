package py.edu.uca.fcyt.toluca.table;

import py.edu.uca.fcyt.toluca.table.state.State;

/**
 * Contiene un texto 
 * @author Grupo Interfaz de Juego
 */
class StringState implements State 
{
	private String text;
	private boolean highlighted;
	
	/**
     * Construye un nuevo StringState
     * @param text	texto contenido
     */
	public StringState(String text, boolean highlighted)
	{
		this.text = text;
		this.highlighted = highlighted;
	}
	
	public void transition(State state, double ratio) 
	{
		StringState sState;
		
		sState = (StringState) state;
		text = sState.text;
		highlighted = sState.highlighted;
	}

	public State getCopy() 
	{
		return new StringState(text, highlighted);
	}
	
	/**
     * Retorna el texto contenido
     */
	public String getText()
	{
		return text;
	}	
	
	/**
     * Retorna verdadero si el texto es resaltado
     */
	public boolean isHighlighted()
	{
		return highlighted;
	}	
}