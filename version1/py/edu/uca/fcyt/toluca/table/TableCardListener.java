package py.edu.uca.fcyt.toluca.table;

/**
 * Contiene las definiciones de los eventos referentes a 
 * la transición de estados de un {@link TableCard}
 * @author Grupo Interfaz de Juego
 */
interface TableCardListener
{
	/**
     * Llamado cuando la transición de un Table card 
     * de estado a otro ha sido completada
     * @param tCard		TableCard que completó su transición.
     */
	public void transitionCompleted(TableCard tCard);
}