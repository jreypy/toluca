package py.edu.uca.fcyt.toluca.event;



import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableBeanRepresentation;
import py.edu.uca.fcyt.toluca.table.TableServer;
import py.edu.uca.fcyt.toluca.table.Util;

/**
 * Contiene los datos necesarios para manejar un evento de mesa
 * @author Grupo Interfaz de Juego
 */
public class TableEvent {
	public static final int EVENT_gameStartRequest = 0;
	public static final int EVENT_gameStarted = 1;
	public static final int EVENT_gameFinished = 2;
	public static final int EVENT_tableLocked = 3;
	public static final int EVENT_tableUnlocked = 4;
	public static final int EVENT_playerStandRequest = 5;
	public static final int EVENT_playerStanded = 6;
	public static final int EVENT_playerKickRequest = 7;
	public static final int EVENT_playerKicked = 8;
	public static final int EVENT_playerSitRequest = 9;
	public static final int EVENT_playerSit = 10;
	public static final int EVENT_signSendRequest = 11;
	public static final int EVENT_signSent = 12;
	public static final int EVENT_showPlayed = 13;
	public static final int EVENT_playerLeft=14;
	public static final int EVENT_TABLE_DESTROYED=15;
	private static int PLAYER_MASK = 0x4000;
	private static int VALUE_MASK = 0x8000;
    
	private int event;
	private Table table;
	private TableServer tableServer;
	private TrucoPlayer[] player;
	private TableBeanRepresentation tableBeanRepresentation;
	private int value;
    
	/**
	 * Crea una nueva instancia de TableEvent para el
	 * env�o de una se�a.
	 * @param table		mesa asociada
	 * @param player	jugador asociado
	 * @param value		valor asociado
	 */
	public TableEvent
	(
		int event, Table table, TrucoPlayer player1, 
		TrucoPlayer player2, int value
	) 
	{
		// verificaciones ("comentarizado" para el ejemplo)
		Util.verifParam(table != null, "Par�metro 'table' nulo");
        
		this.event = event;
		this.table = table;
		this.value = value;
		this.player = new TrucoPlayer[]
		{
				player1, player2
		};
	}
	public TableEvent
	(
		int event, TableServer table, 
		TrucoPlayer player1, TrucoPlayer player2, 
		int value
	) {
		// verificaciones ("comentarizado" para el ejemplo)
		Util.verifParam(table != null, "Par�metro 'table' nulo");
        
		this.event = event;
		this.tableServer = table;
		this.value = value;
		this.player = new TrucoPlayer[]
		{
				player1, player2
		};
	}
	public TableEvent()
	{
	}
    
	/** Retorna el jugador asociado */
	public TrucoPlayer getPlayer(int index) 
	{ 
		return player[index]; 
	}
    
	/** Retorna el valor asociado */
	public int getValue() { return value; }
    
	/** Retorna la mesa asociada */
	public Table getTable() { return table; }
    
	/**
	 * Retorna una identificaci�n que contiene informaci�n sobre
	 * los par�metros activos de este TableEvent y el evento
	 * que se gener�.
	 */
	public int getID() {
		int p, v;
        
		p = player == null ? 0 : PLAYER_MASK;
		v = value == -1 ? 0 : VALUE_MASK;
        
		return p | v | event;
	}
    
	/**
	 * Retorna verdadero el ID pasado indica que el campo
	 * <code>player</code> est� especificado.
	 * @param id		ID de un TableEvent
	 */
	public static boolean isValueSpecified(int id) {
		return (id & VALUE_MASK) != 0;
	}
    
	/**
	 * Retorna verdadero si el campo ID pasado indica que el
	 * campo <code>value</code> est� especificado.
	 * @param id		ID de un TableEvent
	 */
	public static boolean isPlayerSpecified(int id) {
		return (id & PLAYER_MASK) != 0;
	}
    
	/**
	 * Obtiene el evento especificado en un ID de un TableEvent
	 * @param id	ID de un TableEvent
	 */
	public static int getEventFromID(int id) {
		return id & ~(PLAYER_MASK | VALUE_MASK);
	}
	
	public TableServer getTableServer() {
		return tableServer;
	}
   
   
	
	
        
		/**
		 * Ejemplo de uso. La m�scara juega el rol de tipo de
		 * evento y las 'isXSpecified' el rol de determinadores
		 * de existencia o inexistencia de un cierto par�metro
		 * para un cierto tipo (m�scara).
		 * El par�metro Table siempre est� especificado.
		 */
  /*  public static void main(String[] args) {
		TableEvent[] tEvents;
		int id;
   
		// crea 4 TableEvents con todas las
		// combinaciones de par�metros activos
		tEvents = new TableEvent[] {
			new TableEvent(TableEvent.EVENT_gameStarted, null, null, -1),
			new TableEvent(TableEvent.EVENT_gameFinished, null, null, 1),
			new TableEvent(TableEvent.EVENT_playerSit, null, null, -1),
			new TableEvent(TableEvent.EVENT_tableLocked, null, null, 2)
		};
   
		// imprime la existencia o no de los
		// par�metros de cada TableEvent
		for (int i = 0; i < 4; i++) {
			// obtiene la m�sca del 'i'-�simo TableEvent
			id = tEvents[i].getID();
   
			// imprime la existencia o inexistencia
			// de c/u de los par�metros
			System.out.println("TableEvent numero " + i + ":");
			System.out.println("- Evento: " + TableEvent.getEventFromID(id));
			System.out.println("- Especifica 'player': " + TableEvent.isPlayerSpecified(id));
			System.out.println("- Especifica 'value': " + TableEvent.isValueSpecified(id));
		}
	}*/
	public static void main(String[] args) {
		TableEvent event=new TableEvent();
		
		event.setTable(new Table(new TrucoPlayer("DaniC",222),true));
		
		TableBeanRepresentation tableBean=new TableBeanRepresentation(33,new TrucoPlayer("ddd",33));
		event.setTableBeanRepresentation(tableBean);
		XMLEncoder e=	new XMLEncoder(		new BufferedOutputStream(System.out));
		e.writeObject(event);
		e.close();
		}
	
	public int getEvent() {
		return event;
	}

	public TrucoPlayer[] getPlayer() {
		return player;
	}

	
	public void setEvent(int i) {
		event = i;
	}

	
	public void setPlayer(TrucoPlayer[] players) {
		player = players;
	}

	
	public void setTable(Table table) {
		this.table = table;
	}

	
	public void setTableServer(TableServer server) {
		tableServer = server;
	}

	
	public void setValue(int i) {
		value = i;
	}
	


	public TableBeanRepresentation getTableBeanRepresentation() {
		return tableBeanRepresentation;
	}

	
	public void setTableBeanRepresentation(TableBeanRepresentation representation) {
		tableBeanRepresentation = representation;
	}

	}