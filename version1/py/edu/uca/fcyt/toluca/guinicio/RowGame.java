
package py.edu.uca.fcyt.toluca.guinicio;

/**
 * @author dani
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RowGame {

	private int estado;
	private int tableNumber;

	/**
	 * @param tableNumber The tableNumber to set.
	 */
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	private final static String sinUser=new String("vacio");
	/**
	 * @return Returns the tableNumber.
	 */
	public int getTableNumber() {
		return tableNumber;
	}
	private String [] jugadores;
	public RowGame(int tableNumber)
	{
		jugadores=new String[8];
		this.tableNumber=tableNumber;
	}
	public void setJugador(int num,String name)
	{
		jugadores[num]=name;
	}
	public String getJugador(int num)
	{
		if(jugadores[num]!=null)
			return jugadores[num];
		else
			return sinUser; 
		
	}
	public int getEstado()
	{
		return estado;
	}
	
}
