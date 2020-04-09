package py.edu.uca.fcyt.toluca;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;



 
/**
 *
 * @author  Interfaz de Inicio
 */
public abstract class Room 
implements ChatPanelContainer {
    
    private final static int MAX_TABLES=20;
    protected String name;
    protected HashMap players; // of type Player??
	/**
	 * @return Returns the tablesServers.
	 */
	public TableServer[] getTablesServers() {
		return tablesServers;
	}
    protected Vector roomListeners; // of type Vector
    //Unlike the new collection implementations, Vector is synchronized.
    // throws Concurrent modification exception.  
    // Blocks synchronized by aa
    
    protected Table [] tables; // of type Table
    protected TableServer [] tablesServers;
    
    public Room() {
        players = new HashMap();
        tables = new Table[MAX_TABLES];
        tablesServers=new TableServer[MAX_TABLES];
        roomListeners = new Vector();
    }
    
    ///////////////////////////////////////
    // operations
    
    
    /**
     * <p>
     * Este metodo es llamado, cuando se pulsa el boton de Crear tabla;
     * en la pieza. Lo que hace es ejecutar el metodo fireTableCreated().
     *
     * En el servidor llama al constructor de un table y
     * despues llama a addTable.
     *
     */
    public void createTableRequest() {
        fireTableCreated();
    }
    
    
    /**
     * <p>
     * Recorre el vector de listeners y ejecuta en cada uno de los objetos del
     * mismo, el metodo tableCreated.
     * </p>
     *
     */
    private synchronized void fireTableCreated() {
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_TABLE_CREATED);
    	synchronized (roomListeners) {
			Iterator iter = roomListeners.listIterator();
			while (iter.hasNext()) {
				RoomListener ltmp = (RoomListener) iter.next();
				ltmp.tableCreated(re);
			}
		}
    }
    
    /**
	 * 
	 * Recorre el vector de listeners y ejecuta en cada uno de los objetos del
	 * mismo, el metodo chatMessageRequested
	 * 
	 * @param player
	 *            El jugador que intenta enviar el mensaje
	 * 
	 * @param htmlMessage
	 *            El mensaje que se intenta enviar
	 *  
	 */
    private synchronized void fireChatMessageSent(TrucoPlayer player, String htmlMessage) {
        /** lock-end */
        synchronized(roomListeners) {
            Iterator iter = roomListeners.listIterator();
            while(iter.hasNext()) {
                RoomListener ltmp = (RoomListener)iter.next();
                ltmp.chatMessageRequested(this, player, htmlMessage);
            }
        }
    } // end fireChatMessageSent        /** lock-begin */
    
    /**
     * <p>
     * Llamar al metodo showChatMessage del componente del Chat
     * del room para que este muestre los mensajes.
     * </p>
     * <p>
     *
     * @param player El jugador que envi&#243; el mensaje de chat
     * </p>
     * <p>
     * @param htmlMessage El mensaje de chat
     * </p>
     */
    public abstract void showChatMessage(TrucoPlayer player, String htmlMessage);
    
    /**
     * <p>
     * Agrega un listener al vector de listeners en el Room.
     * Cuando se ejecuta este metodo, lo unico que hay que hacer es:
     * </p>
     * <p>
     * roomListeners.add(parametro);
     * </p>
     * <p>
     *
     * @param roomListener es el listener que se va a agregar al vector.
     * </p>
     */
    public synchronized void addRoomListener(RoomListener roomListener) {        /**
     * lock-end */
        synchronized(roomListeners) {
        	roomListeners.add(roomListener);
        }
    } // end addRoomListener        /** lock-begin */
    
    /**
     * @deprecated
     * <p>
     * Se ejecuta cuando un jugador trata de unirse a la tabla.
     * Informa de esto a los listeners registrados
     * a traves de la ejecucion de fireTableJoined.
     * </p>
     * <p>
     *
     * @param tableNumber ...
     * </p>
     */
    public void joinTable(int tableNumber) {        /** lock-end */
        fireTableJoined();
    } // end joinTable        /** lock-begin */
    
    /**
     * @deprecated
     * <p>
     * Recorre el vector de listeners y ejecuta en cada uno
     * de los objetos del mismo, el metodo tableJoined.
     * </p>
     *
     */
    private synchronized void fireTableJoined() {        /** lock-end */
        
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_TABLE_JOINED);
        synchronized(roomListeners) {
        	Iterator iter = roomListeners.listIterator();
        	while(iter.hasNext()) {
        		RoomListener ltmp = (RoomListener)iter.next();
        		ltmp.tableJoined(re);
        	}
        }
    } // end fireTableJoined
    
    /**
     * <p>
     * Cuando se ejecuta, agregar al jugador al Room. Actualizar la
     * tabla en la que se muestran los jugadores asi como el vector
     * subyacente.
     * </p>
     * <p>
     *
     * @param player El jugador que esta siendo agregado
     * </p>
     */
    public void addPlayer(TrucoPlayer player) {
      /*  if (this instanceof RoomClient) {
            System.out.println("Voy a agregar en el cliente: " + player.getName());
        } else {
            System.out.println("Voy a agregar en el servidor: " + player.getName());            
        }*/
        players.put(player.getName(), player); //se carga al vector de jugadores
        //System.out.println("Va a imprimr el hash de players by Cricco");
        //py.edu.uca.fcyt.util.HashUtils.imprimirHash(players);
    }
    
    public HashMap getHashPlayers(){return players;}
    
    //public HashMap getHashTable(){return tables;}
        
    public void addTable(Table table) {        /** lock-end */
        /*if (this instanceof RoomClient) {
            System.out.println("Voy a agregar en el cliente la tabela: " + table.getOrigin());
        } else {
            System.out.println("Voy a agregar en el servidor la tabela: " + table.getOrigin());            
        }*/
        //tables.put(table.getTableNumber()), table); //se carga la tabla nueva pow
        tables[table.getTableNumber()]=table;
       // System.out.println("Imprimir los tables");
        //py.edu.uca.fcyt.util.HashUtils.imprimirHash(tables);
    }
    public void addTable(TableServer table)
    {
    	//tables.put(new Integer(table.getTableNumber()), table); //se carga la tabla nueva pow
    	
    	tablesServers[table.getTableNumber()]=table;
    }
    public TrucoPlayer getPlayer(String keyCode) {
       // System.out.println("Voy a buscar al player: " + keyCode);
        return ((TrucoPlayer)players.get(keyCode));
    }
    
    /**
     * <p>
     * Cuando se ejecuta, sacar al jugador del Room.
     * Actualizar la tabla en la que se muestra el jugador
     * asi como el vector subyacente.
     * </p>
     * <p>
     *
     * @param player El jugador que hay que sacar
     * </p>
     */
// end removePlayer        /** lock-begin */
   public void removePlayer(TrucoPlayer player) {        /** lock-end */
	   //le quita del vector de players
	 
	   players.remove(player);
	 //  firePlayerLeft();
   }     
    /**
     * <p>
     * Modifcar los datos del jugador que esta en el Room
     * </p>
     * <p>
     *
     * @param player El jugador cuyos datos hay que modificar.
     * </p>
     */
    public void modifyPlayer(TrucoPlayer player) {        /** lock-end */
        /*for(int i=0; i<players.size(); i++){
            if( players.get(i) == player){
                players.setElementAt(player, i);
                i = players.size()+10;
            }
        } */
    } // end modifyPlayer        /** lock-begin */

    public String getOrigin() {
        return "room";
    }
    
    /*
     * Metodo nuevo.
     * Remueve una mesa de la Lista de Mesas en juego
     */
    public void removeTable(Table table) {        /** lock-end */
        tables[table.getTableNumber()]=null;
    } 
    public void removeTable(TableServer table)
    {
    	tablesServers[table.getTableNumber()]=null;
    }
    public synchronized int getAvailableKey()
    {
    	
    	for(int key=0;key<MAX_TABLES;key++)
    	{
    		if(tablesServers[key]==null)
    			return key;
    	}
    	return -1;
    }
    public TableServer getTableServer(int id)
    {
    	return tablesServers[id];
    }
    public Table getTable(int id)
    {
    	return tables[id];
    }
} // end Room




