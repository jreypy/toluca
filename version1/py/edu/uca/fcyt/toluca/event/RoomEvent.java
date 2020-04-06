package py.edu.uca.fcyt.toluca.event;

/** Java class "RoomEvent.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */


import java.util.HashMap;

import py.edu.uca.fcyt.game.ChatMessage;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;

 
/**
 * <p>
 * El RoomEvent es una clase que se utiliza para informar a los listeners
 * registrados de la clase Room de los eventos que ocurren en el mismo.
 * </p>
 * <p>
 * El atributo "type" indica el tipo de evento y puede ser cualquiera de
 * los eventos tipificados como variables publicas finales que empiezan con
 * el prefijo TYPE_.
 * </p>
 * <p>
 * Dependiendo del tipo de evento, se cargan las demas propiedades de la
 * clase cuando se quiere informar de un evento:
 * </p>
 * <p>
 * En el lado del cliente:
 * </p>
 * <p>
 * TYPE_CHAT_REQUESTED - se carga el atributo ChatMessage
 * </p>
 * <p>
 * TYPE_CREATE_TABLE_REQUESTED - no se carga nada
 * </p>
 * <p>
 * TYPE_TABLE_JOIN_REQUESTED - se carga el atributo tableNumber con el
 * numero de tabla a la que se quiere unir
 * </p>
 * <p>
 * Los demas tipos son del lado del servidor y por ahora podemos dejarlos
 * vacios puesto que seran utilizados por el RoomServer que no esta a
 * nuestro cargo.
 * </p>
 *
 */
public class RoomEvent {
    

    
	/**
	 * <p>
	 * Representa el numero de tabla a la que el usuario se intenta unir en el
	 * RoomClient
	 * </p>
	 *
	 */
	private int tableNumber;
	private String username;
	private String password;
	private String errorMsg;
    private TableServer [] tablesServers;
	/**
	 * @return Returns the errorMsg.
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	/**
	 * @param errorMsg The errorMsg to set.
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	/**
	 * <p>
	 * Representa el mensaje de chat que el usuario envia o recibe en el Room
	 *
	 *</p>
	 *
	 */
	//constructor con param int para el tipo
	// contructor con un chatmessage
	private ChatMessage chatMessage;
    
	/**
	 * <p>
	 * Representa el tipo de evento que ocurrio en el Room
	 * </p>
	 *
	 */
	private int type;
    
	/**
	 * <p>
	 * Representa un vector que contiene objetos de tipo Table para los eventos
	 * que involucran manejo de tablas
	 * </p>
	 *
	 */
	private HashMap tables; // of type Table
    
	/**
	 * <p>
	 * Representa un vector que contiene objetos de tipo Player para los
	 * eventos que involucran manejo de jugadores
	 * </p>
	 *
	 */
	private HashMap players;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se quiere enviar un
	 * mensaje de chat.
	 * </p>
	 *
	 */
	public static final int TYPE_CHAT_REQUESTED=1;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se esta enviando un
	 * mensaje de chat.
	 * </p>
	 *
	 */
	public static final int TYPE_CHAT_SENT=2;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se intenta crear
	 * una tabla.
	 * </p>
	 *
	 */
	public static final int TYPE_CREATE_TABLE_REQUESTED=3;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se quiere confirma
	 * la creacion de una tabla
	 * </p>
	 *
	 */
	public static final int TYPE_TABLE_CREATED=4;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se intenta unir a
	 * una tabla
	 * </p>
	 *
	 */
	public static final int TYPE_TABLE_JOIN_REQUESTED=5;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se confirma la
	 * union de un usuario a una tabla
	 * </p>
	 *
	 */
	public static final int TYPE_TABLE_JOINED=6;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que se intenta unir un
	 * jugador a una tabla
	 * </p>
	 *
	 */
	public static final int TYPE_PLAYER_JOINED=7;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los que un jugador abandona
	 * el Room
	 * </p>
	 *
	 */
	public static final int TYPE_PLAYER_LEFT=8;
    
	/**
	 * <p>
	 * Representa una constante para los eventos en los un jugador es echado de
	 * un Room
	 * </p>
	 *
	 */
	public static final int TYPE_PLAYER_KICKED=9;
    

	public static final int TYPE_LOGIN_REQUESTED=10; 


	public static final int TYPE_LOGIN_COMPLETED=11; 


	public static final int TYPE_LOGIN_FAILED=12; 
    
	public static final int TYPE_TABLE_CREATED_SERVER = 13;
	public static final int TYPE_TABLE_DESTROYED=14;
	public static final int TYPE_RANKING_CHANGED=15;
    public static final int TYPE_TEST_CONEXION=16;
    public static final int TYPE_INVITACION=17;
    public static final int TYPE_INVITACION_REJECTED = 18;
	/** Holds value of property player. */
	private TrucoPlayer player;
    
	/** Holds value of property tableServer. */
	private TableServer tableServer;
    private long msSend;
	private int gamePoints;    
	public RoomEvent(){
		//codigo agregado por el CIT MASTER
	   tables = new HashMap();
	   this.players = new HashMap();
	}
    
	
	public int getType() {       
		return type;
	} 
    
	
	
	public void setType(int _type) {   
		type = _type;
	}
    
	public void setPassword(String password) {
		this.password=new String(password);
	}
	public String getPassword() {
		return password;
	}
	public void setUser(String user) {
		this.username=user;
	}
	public String getUser() {
		return username;
	}
	public HashMap getPlayers() {       
		return players;
	} 
    
    
	

	public void setPlayers(HashMap _players) {       
		players = _players;
	} 
    
    
	
	public void addPlayers(TrucoPlayer player) {        /** lock-end */
        
		try {
			players.put(player.getName(),player);           
		} catch (java.lang.NullPointerException npe) {
			System.out.println("El collection de players no esta instaciado");
			throw npe;
		}
	}
    
	public void removePlayers(TrucoPlayer _Players) {      
		players.remove(_Players);
	}
    
	
	public ChatMessage getChatMessage() {        
		return chatMessage;
	}
    
	
	public void setChatMessage(ChatMessage _chatMessage) {        
		chatMessage = _chatMessage;
	}
    
	
	public HashMap getTabless() {       
		return tables;
	}
    
	
	public void setTabless(HashMap _tables) {        
		tables = _tables;
	}
    
	public void addTables(Table table) {       
		tables.put(new Integer(table.getTableNumber()),table);
	}
	public void addTables(TableServer table) {       
		tables.put(new Integer(table.getTableNumber()),table);
	}
    
	/*public void addTables(TableServer _tables) { 
        
	
		try {
		if (! tables.contains(_tables))
			tables.add(_tables);
        
		} catch (Exception e ) {
			System.out.println("Error en el ROOM EVENT.  PQNTSC.  table es nulo al parecer");
			e.printStackTrace(System.out);
           
		}
        
	} */

	
	public void removeTables(Table _tables) {      
		tables.remove(_tables);
	}
    
	
	public int getTableNumber() {
		return tableNumber;
	}
    
	
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
   
	public TrucoPlayer getPlayer() {
		return this.player;
	}
    
	
	public void setPlayer(TrucoPlayer player) {
		this.player = player;
	}
    
	
	public TableServer getTableServer() {
		return this.tableServer;
	}
    

	public void setTableServer(TableServer tableServer) {
		this.tableServer = tableServer;
	}
    
	
//	  public static void main(String[] args) {
//		XMLEncoder e=	new XMLEncoder(		new BufferedOutputStream(System.out));
//			
//	RoomEvent event=new RoomEvent();
//	event.setType(RoomEvent.TYPE_CHAT_REQUESTED);
//	TrucoPlayer player=new TrucoPlayer("user",33);
//	
//	TableBeanRepresentation table1=new TableBeanRepresentation();
//	table1.setId(2);
//	table1.setPlayer(new TrucoPlayer("ddd",22));
//	TableBeanRepresentation table2=new TableBeanRepresentation();
//	table2.setId(1);
//	table2.setPlayer(new TrucoPlayer("juan",55));
//	
//	ArrayList tablas=new ArrayList();
//	tablas.add(table1);
//	tablas.add(table2);
//	//ent.setTabless(tablas);
//	event.setChatMessage(new ChatMessage(player,"hola mundo "));
//	TableServer server=new TableServer(new TrucoPlayer("juan",55));
//	event.setTableServer(server);
//	
//	
//	e.writeObject(event);
//	e.close();
//		
//	}
	
	/**
	 * @return Returns the tablesServers.
	 */
	public TableServer[] getTablesServers() {
		return tablesServers;
	}
	/**
	 * @param tablesServers The tablesServers to set.
	 */
	public void setTablesServers(TableServer[] tablesServers) {
		this.tablesServers = tablesServers;
	}
    public long getMsSend() {
        return msSend;
    }
    public void setMsSend(long msSend) {
        this.msSend = msSend;
    }

	public int getGamePoints() {
		return gamePoints;
	}
	public void setGamePoints(int points) {
		this.gamePoints = points;
	}
} 



