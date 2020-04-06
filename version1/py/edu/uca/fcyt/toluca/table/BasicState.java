package py.edu.uca.fcyt.toluca.table;

import py.edu.uca.fcyt.toluca.table.state.State;

/**
 * Aloja el estado de un objeto general
 * @author Grupo Interfaz de Juego
 */
public class BasicState implements State
{
	public double x, y;		// posición
	public double angle;		// ángulo de rotación
	public double scale = 1;	// escala
	public double elapsedTime;
	public double remainingTime;

	/**
     * Construye un nuevo BasicState con los parámetros pasados
     */	
	public BasicState(double x, double y, double angle, double scale)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.scale = scale;
	}
	
	/**
     * Construye un nuevo State a partir de otro 
     */
    public BasicState(BasicState state)
    {
		this.x = state.x;
		this.y = state.y;
		this.angle = state.angle;
		this.scale = state.scale;
    }
    
    public void transition(State state, double ratio)
    {
    	BasicState tcState;
    	
    	tcState = (BasicState) state;
    	
		x = x * (1 - ratio) + tcState.x * ratio;
		y = y * (1 - ratio) + tcState.y * ratio;
		angle = angle * (1 - ratio) + tcState.angle * ratio;
		scale = scale * (1 - ratio) + tcState.scale * ratio;
    }
	/** Retorna una copia del objeto
	 */
	public State getCopy()
	{
		if (!(this instanceof BasicState)) 
			throw new IllegalStateException
			(
				"Este método debe ser sobreescrito"
			); 
		 
		return new BasicState(this);
	}

} 
