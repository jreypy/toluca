package py.edu.uca.fcyt.toluca;

import py.edu.uca.fcyt.toluca.table.*;
import py.edu.uca.fcyt.toluca.game.*;
import py.edu.uca.fcyt.toluca.event.*;
import py.edu.uca.fcyt.toluca.db.*;
import py.edu.uca.fcyt.toluca.net.*;
import py.edu.uca.fcyt.game.*;

import org.jdom.*;



/** Java class "RoomServer.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
import java.util.*;
import java.util.Vector;
import java.util.Collections.*;

/**
 * <p>
 *
 * </p>
 */
public class RoomServer extends Room
implements ChatPanelContainer {
    
    ///////////////////////////////////////
    // attributes
    
    
    /**
     * <p>
     * Represents ...
     * </p>
     */
    private java.util.Properties properties;
    
    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Vector vPlayers;
    
    /**
     * <p>
     * Representa a las tablas que estan activas en el servidor.
     * </p>
     */
    private Vector vTables;
    
    ///////////////////////////////////////
    // associations
    
    /**
     * <p>
     *  Maneja las operaciones de base de datos del servidor.
     * </p>
     */
    private DbOperations dbOperations;
    
    /**
     * <p>
     *  Servidor de Conexiones.  Crea un CommunicatorServer por cada conexion que se recibe
     * </p>
     */
    
    protected ConnectionManager connManager;
    
    //private Map pendingConnections = new Collections.synchronizedMap( new HashMap() );
    private Map pendingConnections = Collections.synchronizedMap((Map)(new HashMap()));
    
    
    ///////////////////////////////////////
    // operations
    
    /**
     * <p>
     * Does ...
     * </p>
     */
    public  RoomServer() {
        // your code here
        System.out.println("Soy un room server y voy a instanciar un connection manager.");
        connManager = new ConnectionManager(this);
        dbOperations = new DbOperations(null, null, null, this);
        vTables = new Vector();
        vPlayers = new Vector();
    } // end RoomServer
    
    
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p>
     */
    public void createTable(TrucoPlayer player) {
        // your code here
        Table table = new Table(player, true);
        vTables.add(table);
        fireTableCreated(table);
    } // end createTable
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param table ...
     * </p>
     */
    protected void fireTableCreated(Table table) {
        // your code here
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_TABLE_CREATED);
        re.addTables(table);
        Iterator iter = roomListeners.listIterator();
        while(iter.hasNext()) {
            RoomListener ltmp = (RoomListener)iter.next();
            ltmp.tableCreated(re);
        }
    } // end fireTableCreated
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param table ...
     * </p>
     */
    public void fireTableJoined(Table table) {
        // your code here
    } // end fireTableJoined
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param player ...
     * </p><p>
     * @param htmlMessage ...
     * </p>
     */
    public void fireChatSent(Player player, String htmlMessage) {
        // your code here
    } // end fireChatSent
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param player ...
     * </p>
     */
    //    public void firePlayerJoined(Player player) {
    //        // your code here
    //    } // end firePlayerJoined
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param player ...
     * </p>
     */
    public void firePlayerLeft(Player player) {
        // your code here
    } // end firePlayerLeft
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param player ...
     * </p>
     */
    public void firePlayerKicked(Player player) {
        // your code here
    } // end firePlayerKicked
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param username ...
     * </p><p>
     * @param password ...
     * </p>
     */
    public void login(String username, String password, CommunicatorServer cs) throws py.edu.uca.fcyt.toluca.LoginFailedException {
        // your code here
        Player jogador = null;
        try {
            
            pendingConnections.put(username, cs);
            
            //jogador = new Player("CIT", 108);
            jogador = dbOperations.authenticatePlayer(username, password);
            System.out.println("Se creo el jugador: " +  jogador.getName());
            
            firePlayerJoined(jogador);
            fireLoginCompleted(jogador);
            //firePlayerJoined(jogador);
            //  return jogador;
        }
        catch (LoginFailedException le) {
            throw le;
        }
    } // end login
    
    
    /**
     * <p>
     * Inicia el servidor del TOLUCA instanciando un objeto de clase RoomServer
     * </p>
     * <p>
     *
     * </p>
     * <p>
     *
     * @param args Actualmente ninguno. Pasar como argumento el nombre del
     * archivo de configuraci&#243;n?
     * </p>
     */
    public static void main(String[] args) {
        new RoomServer();
    } // end main
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * @return a Vector with ...
     * </p>
     */
    public Vector getVPlayers() {
        return vPlayers;
    } // end getVPlayers
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param _vPlayers ...
     * </p>
     */
    public void setVPlayers(Vector _vPlayers) {
        vPlayers = _vPlayers;
    } // end setVPlayers
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * @return a Vector with ...
     * </p>
     */
    public Vector getVTables() {
        return vTables;
    } // end getVTables
    
    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p><p>
     *
     * @param _vTables ...
     * </p>
     */
    public void setVTables(Vector _vTables) {
        vTables = _vTables;
    }
    
    /** Getter for property dbOperations.
     * @return Value of property dbOperations.
     */
    public DbOperations getDbOperations() {
        return this.dbOperations;
    }
    
    /** Setter for property dbOperations.
     * @param dbOperations New value of property dbOperations.
     */
    public void setDbOperations(DbOperations dbOperations) {
        this.dbOperations = dbOperations;
    }
    
    /** Getter for property connManager.
     * @return Value of property connManager.
     */
    public ConnectionManager getConnManager() {
        return this.connManager;
    }
    
    /** Setter for property connManager.
     * @param connManager New value of property connManager.
     */
    public void setConnManager(ConnectionManager connManager) {
        this.connManager = connManager;
    }
    
    /** <p>
     * Does ...
     * </p><p>
     *
     * @param player ...
     * </p><p>
     * @param htmlMessage ...
     * </p><p>
     *
     * </p>
     */
    public void sendChatMessage(Player player, String htmlMessage) {
        fireChatMessageSent(player, htmlMessage);
    }
    
    public void showChatMessage(Player player, String htmlMessage) {
    }
    
    // end setVTables
    
    /**
     * <p>
     * Recorre el vector de listeners y ejecuta en cada uno de los objetos del
     * mismo, el metodo fireTableCreated.
     * </p>
     *
     */
    protected void firePlayerJoined(final Player jogador) {
        //la gran avestruz, deberia ser asi con RoomEvent que extiende de la inexistente SpaceEvent
        /*RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_PLAYER_JOINED);
        re.addPlayers(jogador);
        Iterator iter = roomListeners.listIterator();
        while(iter.hasNext()) {
            RoomListener ltmp = (py.edu.uca.fcyt.toluca.RoomListener)iter.next();
            ltmp.playerJoined(jogador);
            ltmp.loginCompleted(re);
        }*/
        /*Agrego el jugador a la lista de jugadores.*/
        addPlayer(jogador);
        System.out.println("Dentro de fire user joined (Room Server) , jugador = " + jogador.getName());
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_PLAYER_JOINED);
        if (jogador == null)
            System.out.println("jogador es null carajo");
        
        //Vector v = new Vector();
        getVPlayers().add(jogador);
        re.setPlayers(getVPlayers());
        
        Iterator iter = roomListeners.listIterator();
        while(iter.hasNext()) {
            RoomListener ltmp = (RoomListener)iter.next();
            ltmp.playerJoined(jogador);
        }
        
        
    }
    
    /**
     * <p>
     * Se ejecuta cuando un Jugador se autentic� correctameente.
     * Recorre el vector de listeners y ejecuta en cada uno de los objetos del
     * mismo, el metodo fireTableCreated.
     * </p>
     *
     */
    protected void fireLoginCompleted(final Player jogador) {
        //la gran avestruz, deberia ser asi con RoomEvent que extiende de la inexistente SpaceEvent
        
        try {
            System.out.println("Dentro de fire login completed , jugador = " + jogador.getName());
            RoomEvent re = new RoomEvent();
            re.setType(RoomEvent.TYPE_LOGIN_COMPLETED);
            
            /*El jugador que se logueo.*/
            Vector v = new Vector();
            v.add(jogador);
            re.setPlayers(v);
            System.err.println("antes de obtener el room listener");
            RoomListener ltmp = (RoomListener) pendingConnections.get(jogador.getName());
            System.err.println("despues de obtener el Roomlistener");
            try {
                
                ltmp.loginCompleted(re);
            } catch (NullPointerException e) {
                System.err.println("cagamos, no hay listener");
                throw e;
            }
            
            
            System.err.println("despues de disparar loginCompleted");
            pendingConnections.remove(jogador.getName());
            
        } catch (java.lang.NullPointerException npe) {
            System.err.println("Null pointer exceptiooooon en room server");
            if (jogador == null)
                System.err.println("jogador es nulo!");
            else
                System.err.println("nombre del jogador:" + jogador.getName());
            npe.printStackTrace();
        }
        /*
        Iterator iter = roomListeners.listIterator();
        while(iter.hasNext()) {
            RoomListener ltmp = (py.edu.uca.fcyt.toluca.RoomListener)iter.next();
         
            String nombre = ((CommunicatorServer)ltmp).player.getName();
            System.err.println("Verificando el jugador: " + nombre);
         
            if ( jogador.getName().compareTo(nombre)  == 0) { //
                System.out.println("dentro del roomserver, encontre el player que se logeuo");
                ltmp.loginCompleted(re);
                break;
            }
         
        }*/
    }
    
    
    
    /**
     * Dispara el evento de chatMessageSent
     */
    protected void fireChatMessageSent(Player jogador, String htmlMessage) {
        Iterator iter = roomListeners.listIterator();
        int i =0;
        while(iter.hasNext()) {
            RoomListener ltmp = (RoomListener)iter.next();
            System.out.println(jogador.getName() + " enviando message sent al listener #" + (i++) + " clase:" + ltmp.getClass().getName());            
            ltmp.chatMessageSent(this, jogador, htmlMessage);
        }
    }
    
    public void chatMessageSent(ChatPanelContainer cpc, Player player, String htmlMessage) {
        
    }
    
    
} // end RoomServer


