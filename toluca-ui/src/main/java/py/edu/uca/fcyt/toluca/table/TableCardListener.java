package py.edu.uca.fcyt.toluca.table;

/**
 * Contiene las definiciones de los eventos referentes a 
 * la transicin de estados de un {@link TableCard}
 * @author Grupo Interfaz de Juego
 */
interface TableCardListener
{
	/**
     * Llamado cuando la transicin de un Table card
     * de estado a otro ha sido completada
     * @param tCard		TableCard que complet su transicin.
     */
	public void transitionCompleted(TableCard tCard);
}