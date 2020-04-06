package py.edu.uca.fcyt.toluca.table.state;

/**
 * Contiene las definiciones de los eventos 
 * referentes a la transiciónd de estados
 * @author Grupo Interfaz de Juego
 */
public interface StateListener
{
	/**
     * Llamado cuando la transición de <code>o</code> 
     * de un estado a otro ha sido completada.
     */
	public void transitionCompleted(Object o);

	/**
	 * Llamado cuando la animación del objeto <code>o</code> 
	 * ha sido completada.
	 */
	public void animationCompleted(Object o);	
}