package py.edu.uca.fcyt.toluca.table;
import py.edu.uca.fcyt.toluca.table.state.State;

/**
 * Clase que maneja el tiempo de un estado de carita
 */
class FaceState extends BasicState
{
	// desplazamiento (x, y) de los objetos 
	// dentro del cuadrado blanco
	public double offX, offY;
	
	/**
     * Crea un nuevo FaceState a partir de otro
     * @param state	origen de los valores del nuevo objeto
     */
    public FaceState(FaceState state)
    {
    	super(state);
    	this.offX = state.offX;
    	this.offY = state.offY;
    }
	
	/**
     * Crea un nuevo FaceState a partir de los parámetros pasados
     * @param x		posición x de la cara
     * @param y		posición y de la cara
     * @param angle	ángulo de la cara
     * @param scale	escala de la cara
     * @param offX	posición x del centro de los objetos internos
     * @param offY	posición y del centro de los objetos internos
     */
	public FaceState
	(
		double x, double y, double offX, double offY, 
		double angle, double scale 
	)
	{
		super(x, y, angle, scale);
		this.offX = offX;
		this.offY = offY;
	}
	
	public void transition(State state, double ratio)
	{
		FaceState fState;
		
		fState = (FaceState) state;
		
		offX = offX * ratio + fState.offX * (1 - ratio);
		offY = offY * ratio + fState.offY * (1 - ratio);
		super.transition(state, ratio);
	}
	
	public State getCopy()
	{
		return new FaceState(x, y, offX, offY, angle, scale);
	}
}
