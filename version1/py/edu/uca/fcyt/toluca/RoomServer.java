package py.edu.uca.fcyt.toluca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.apache.log4j.xml.DOMConfigurator;

import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.db.DbOperations;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.Communicator;
import py.edu.uca.fcyt.toluca.net.CommunicatorServer;
import py.edu.uca.fcyt.toluca.net.ConnectionManager;
import py.edu.uca.fcyt.toluca.table.TableServer;

/**
 * <p>
 * 
 * </p>
 */
public class RoomServer extends Room

implements ChatPanelContainer, TableListener {
    static Logger logger = Logger.getLogger(RoomServer.class);

    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private java.util.Properties properties;

    //public final static int TIME_OUT = 10000;

    /**
     * <p>
     * Representa a las tablas que estan activas en el servidor.
     * </p>
     */

    ///////////////////////////////////////
    // associations
    /**
     * <p>
     * Maneja las operaciones de base de datos del servidor.
     * </p>
     */
    private DbOperations dbOperations;

    /**
     * <p>
     * Servidor de Conexiones. Crea un CommunicatorServer por cada conexion que
     * se recibe
     * </p>
     */

    protected ConnectionManager connManager;

    //private Map pendingConnections = new Collections.synchronizedMap( new
    // HashMap() );
    private Map pendingConnections = Collections
            .synchronizedMap((Map) (new HashMap()));

    private static String props = "toluca.properties";

    ///////////////////////////////////////
    // operations

    /**
     * <p>
     * Does ...
     * </p>
     */
    public RoomServer() {
        // your code here
        logger.info("Se creo el RoomServer");
        System.out.println("RoomServer: Alive and kicking!");

    } // end RoomServer

    /**
     * @throws ClassNotFoundException
     * @throws SQLException
     *  
     */
    private void init() throws SQLException, ClassNotFoundException {
        logger.info("Instanciando el ConnectionManager");

        connManager = new ConnectionManager(this);
        dbOperations = new DbOperations(properties, this);
        //vTables = new Vector();
        //vPlayers = new Vector();
    }

    /**
     * 
     * @param player
     * @deprecated
     */
    public void createTable(TrucoPlayer player) {
    	createTable(player, 30);
    	
    }
    
    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     */
    public void createTable(TrucoPlayer player, int points) {
        // your code here
        /*logger.debug("Dentor del create table del room server: "
                + player.getName());*/
        TableServer tableServer = new TableServer(player, points);
        tableServer.guardarRoomServer(this);
        //tableServer.addPlayer(player);Comentado porque en el constructor del
        // TableServer
        //ya se esta haciendo un addPlayer, osea esto esta alpedo
        //Table table = new Table(player, true);
        //vTables.add(tableServer);
        
        
        int key = getAvailableKey();
        
        if (key >= 0) {
            tableServer.setTableNumber(key);
            addTable(tableServer);
            fireTableCreated(tableServer);
            tableServer.addTableServerListener(this);
        } else {
            logger.info("No se pueden crear mas tablas");
        }
    } // end createTable

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param table
     *            ...
     *            </p>
     */
    protected synchronized void fireTableCreated(TableServer table) {
        //
        //tHashTable().put(new
        // Integer(table.getTableNumber()),table);//Agregado por Cricco        
        logger.debug("Creado table (" + table.getTableNumber()+ "), creador: "  + table.getHost());
        //Vector players = table.getPlayers();
        //ector playerstmp = new Vector();
        //ayerstmp.add(table.getHost());

        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_TABLE_CREATED);
        //re.setTableNumber(-108);
        //re.addTables(table);
        re.setTableServer(table);
        re.setGamePoints(table.getGamePoints());
        //re.setPlayers(playerstmp);

        re.setTableNumber(table.getTableNumber());
        synchronized(roomListeners) {
	        Iterator iter = roomListeners.listIterator();
	        while (iter.hasNext()) {
	            RoomListener ltmp = (RoomListener) iter.next();
	            ltmp.tableCreated(re);
	        }
        }
    } // end fireTableCreated

    public void joinTable(RoomEvent re) {
        TableServer ts = re.getTableServer();
        if (!ts.getPlayers().contains(re.getPlayer())) {//si no esta unido
            ts.addPlayer(re.getPlayer());
            re.setType(RoomEvent.TYPE_TABLE_JOINED);
            fireTableJoined(re);
        } else {
            logger.info("El player ya esta dentro de la tabla");
        }

    }

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param table
     *            ...
     *            </p>
     */
    private synchronized void fireTableJoined(RoomEvent re) {
        synchronized(roomListeners) {
	        Iterator iter = roomListeners.listIterator();
	        while (iter.hasNext()) {
	            RoomListener ltmp = (RoomListener) iter.next();
	            ltmp.tableJoined(re);
	        }
        }
    } // end fireTableJoined

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param player
     *            ...
     *            </p>
     *            <p>
     * @param htmlMessage
     *            ...
     *            </p>
     */
    public void fireChatSent(TrucoPlayer player, String htmlMessage) {
        // your code here
    } // end fireChatSent

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param player
     *            ...
     *            </p>
     */
    //    public void firePlayerJoined(Player player) {
    //        // your code here
    //    } // end firePlayerJoined
    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param player
     *            ...
     *            </p>
     */
    public void removePlayer(TrucoPlayer player) {
        /** lock-end */
        //le quita del vector de players
        TableServer[] tablasServers = getTablesServers();

        for (int i = 0; i < tablasServers.length; i++) {

            TableServer tabela = tablasServers[i];
            if (tabela != null) {

                Vector jugadores = tabela.getPlayers();
                if (jugadores.contains(player)) {
                    //se le hecha de la tabla
                    tabela.kickPlayer(player);

                }
            }
        }

        try {
            players.remove(player.getName());//quitar del almacenaje de players
            //vPlayers.remove(player);
            firePlayerLeft(player);
        } catch (NullPointerException e) {
            logger.debug("Se elimino un player que era nulo" + player);
            new Exception().printStackTrace();
        }

    }

    protected synchronized void firePlayerLeft(TrucoPlayer player) {


        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_PLAYER_LEFT);
        re.setPlayer(player);
        synchronized(roomListeners) {
            Iterator iter = roomListeners.listIterator();        
	        while (iter.hasNext()) {
	            RoomListener ltmp = (RoomListener) iter.next();
	            ltmp.playerLeft(re);
	        }
        }

        // your code here
    } // end firePlayerLeft

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param player
     *            ...
     *            </p>
     */
    public void firePlayerKicked(TrucoPlayer player) {
        // your code here
    } // end firePlayerKicked

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param username
     *            ...
     *            </p>
     *            <p>
     * @param password
     *            ...
     *            </p>
     * @throws SQLException
     */
    synchronized public void login(String username, String password, CommunicatorServer cs)
    // py.edu.uca.fcyt.toluca.LoginFailedException
    {
        // your code here
        TrucoPlayer jogador = null;
        try {
            if (players.get(username) != null)
                throw new LoginFailedException(
                        "Ya existe un usuario conectado con ese username");
            pendingConnections.put(username, cs);

            //jogador = new Player("CIT", 108);
            jogador = dbOperations.authenticatePlayer(username, password);

            //logger.debug("Se creo el jugador: " + jogador.getName());

            cs.setTrucoPlayer(jogador);

            fireLoginCompleted(jogador);
            firePlayerJoined(jogador);            
        } catch (LoginFailedException le) {
            logger.info("Fallo el intento de logearse de " + username);
            RoomEvent event = new RoomEvent();
            event.setType(RoomEvent.TYPE_LOGIN_FAILED);
            event.setUser(username);
            event.setErrorMsg(le.getMessage());
            cs.loginFailed(event);
        } catch (SQLException e) {
            logger.info("Fallo el intento de logearse de " + username);
            RoomEvent event = new RoomEvent();
            event.setType(RoomEvent.TYPE_LOGIN_FAILED);
            event.setUser(username);
            event.setErrorMsg(e.getMessage());
            cs.loginFailed(event);
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
     * @param args
     *            Actualmente ninguno. Pasar como argumento el nombre del
     *            archivo de configuraci&#243;n?
     *            </p>
     */
    public static void main(String[] args) {
        DOMConfigurator.configure(System.getProperty("user.dir")
                + System.getProperty("file.separator") + "log.xml");
       // PropertyConfigurator.configure("logConfigure.txt");
        RoomServer rs = new RoomServer();
        List params = Arrays.asList(args);
        if (!params.isEmpty()) {
            props = (String) params.get(0);
        }
        if (new File(props).exists()) {
            Properties ret = new Properties();
            //TODO: NO SE LO QUE FELTES QUISO HACER ACA. PERO ESTO ES PARA QUE ANDE PROVISORIO, MMM
//			ret.put(DbOperations.DBURL,"jdbc:firebirdsql:localhost/3050:/opt/firebird/data/TOLUCA.GDB");
//			ret.put(DbOperations.USER_NAME, "sysdba");
//			ret.put(DbOperations.PASSWORD, "asdf");

            FileInputStream fis;
            try {
                fis = new FileInputStream(props);
                ret.load(fis);
                rs.setProperties(ret);
                rs.init();
                if(params.contains("--console"))
                    rs.leerComandos();
            } catch (Exception e) {
                logger.error("no hya beleza", e);
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            String tmp = "Se necesita la ubicación del archivo: " + props;
            logger.error(tmp);
            System.err.println(tmp);
            System.exit(1);
        }

    } // end main

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * @return a Vector with ...
     *         </p>
     */

    /**
     * Getter for property dbOperations.
     * 
     * @return Value of property dbOperations.
     */
    public DbOperations getDbOperations() {
        return this.dbOperations;
    }

    /**
     * Setter for property dbOperations.
     * 
     * @param dbOperations
     *            New value of property dbOperations.
     */
    public void setDbOperations(DbOperations dbOperations) {
        this.dbOperations = dbOperations;
    }

    /**
     * Getter for property connManager.
     * 
     * @return Value of property connManager.
     */
    public ConnectionManager getConnManager() {
        return this.connManager;
    }

    /**
     * Setter for property connManager.
     * 
     * @param connManager
     *            New value of property connManager.
     */
    public void setConnManager(ConnectionManager connManager) {
        this.connManager = connManager;
    }

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * @param player
     *            ...
     *            </p>
     *            <p>
     * @param htmlMessage
     *            ...
     *            </p>
     *            <p>
     * 
     * </p>
     */
    public void sendChatMessage(TrucoPlayer player, String htmlMessage) {
        fireChatMessageSent(player, htmlMessage);
    }

    public void showChatMessage(TrucoPlayer player, String htmlMessage) {
    }

    // end setVTables

    /**
     * <p>
     * Recorre el vector de listeners y ejecuta en cada uno de los objetos del
     * mismo, el metodo fireTableCreated.
     * </p>
     *  
     */
    protected synchronized void firePlayerJoined(final TrucoPlayer jogador) {
        //la gran avestruz, deberia ser asi con RoomEvent que extiende de la
        // inexistente SpaceEvent
        /*
         * RoomEvent re = new RoomEvent();
         * re.setType(RoomEvent.TYPE_PLAYER_JOINED); re.addPlayers(jogador);
         * Iterator iter = roomListeners.listIterator(); while(iter.hasNext()) {
         * RoomListener ltmp = (py.edu.uca.fcyt.toluca.RoomListener)iter.next();
         * ltmp.playerJoined(jogador); ltmp.loginCompleted(re); }
         */
        logger.info("Jugador se unio al Room: "+  jogador);
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_PLAYER_JOINED);
        if (jogador == null)
            logger.debug("jogador es null carajo");

        //Vector v = new Vector();
        //getVPlayers().add(jogador);
        //re.setPlayers(getVPlayers());

        Iterator iter = roomListeners.listIterator();
        int i = 0;

        while (iter.hasNext()) {
            //System.out.println("Player join iterando: " + i++);
            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.playerJoined(jogador);
        }

    }

    /**
     * <p>
     * Se ejecuta cuando un Jugador se autenticï¿½ correctameente. Recorre el
     * vector de listeners y ejecuta en cada uno de los objetos del mismo, el
     * metodo fireTableCreated.
     * </p>
     *  
     */
    protected void fireLoginCompleted(final TrucoPlayer jogador) {
        //la gran avestruz, deberia ser asi con RoomEvent que extiende de la
        // inexistente SpaceEvent

        try {
            /* Agrego el jugador a la lista de jugadores. */
            addPlayer(jogador);
        	
        	
            logger.info("Incio de sesion: " + jogador);
            RoomEvent re = new RoomEvent();
            re.setType(RoomEvent.TYPE_LOGIN_COMPLETED);
            re.setPlayer(jogador);
            re.setPlayers(getHashPlayers());
            re.setTablesServers(getTablesServers());

            RoomListener ltmp = (RoomListener) pendingConnections.get(jogador
                    .getName());

            try {
                ltmp.loginCompleted(re);
            } catch (NullPointerException e) {
                logger.debug("No hay listener adherido");
                throw e;
            }

//            logger.debug("Se disparo login Completed");
            pendingConnections.remove(jogador.getName());

        } catch (java.lang.NullPointerException npe) {
            logger.debug("Null pointer exceptiooooon en room server");
            if (jogador == null)
                logger.debug("jogador es nulo!");
            else
                logger.debug("nombre del jogador:" + jogador.getName());
            npe.printStackTrace(System.out);
        }
        /*
         * Iterator iter = roomListeners.listIterator(); while(iter.hasNext()) {
         * RoomListener ltmp = (py.edu.uca.fcyt.toluca.RoomListener)iter.next();
         * 
         * String nombre = ((CommunicatorServer)ltmp).player.getName();
         * System.out.println("Verificando el jugador: " + nombre);
         * 
         * if ( jogador.getName().compareTo(nombre) == 0) { //
         * System.out.println("dentro del roomserver, encontre el player que se
         * logeuo"); ltmp.loginCompleted(re); break; } }
         */
    }

    /**
     * Dispara el evento de chatMessageSent
     */
    protected synchronized void fireChatMessageSent(TrucoPlayer jogador,
            String htmlMessage) {
        Iterator iter = roomListeners.listIterator();
        int i = 0;
        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            //System.out.println("Jogador vale "+jogador+ " el mensaje
            // es"+htmlMessage);
            logger.debug(jogador.getName()
                    + " enviando message sent al listener #" + (i++)
                    + " clase:" + ltmp.getClass().getName());
            ltmp.chatMessageSent(this, jogador, htmlMessage);
        }
    }
    public void fireRankingChanged(RoomEvent event)
    {
    	Iterator it=roomListeners.listIterator();
    	while(it.hasNext())
    		((RoomListener)it.next()).rankingChanged(event);
    }
    public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player,
            String htmlMessage) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.game.ChatPanelContainer#sendChatMessage(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void sendChatMessage(RoomEvent event) {

        event.setType(RoomEvent.TYPE_CHAT_SENT);
        fireChatMessageSend(event);
    }

    /**
     * 
     *  
     */
    public synchronized void fireChatMessageSend(RoomEvent event) {
        Iterator iter = roomListeners.listIterator();
        int i = 0;
        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            //System.out.println("Jogador vale "+jogador+ " el mensaje
            // es"+htmlMessage);
            //logger.debug(jogador.getName() + " enviando message sent al
            // listener #" + (i++) + " clase:" + ltmp.getClass().getName());

            ltmp.chatMessageSent(event);
        }
    }

    public void removeCommunicator(CommunicatorServer comm) {
        connManager.removeCommunicator(comm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#gameStartRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void gameStartRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#gameStarted(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void gameStarted(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#gameFinished(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void gameFinished(TableEvent event) {
        // TODO Auto-generated method stub

    }
    public void rankingChanged(TrucoPlayer trucoPlayer)
    {
    	RoomEvent event=new RoomEvent();
    	event.setType(RoomEvent.TYPE_RANKING_CHANGED);
    	event.setPlayer(trucoPlayer);
    	fireRankingChanged(event);
    }
    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerStandRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerStandRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerStanded(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerStanded(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerKickRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerKickRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerKicked(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerKicked(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerLeft(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerLeft(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerSitRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerSitRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#playerSit(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerSit(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#signSendRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void signSendRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#signSent(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void signSent(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#showPlayed(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void showPlayed(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#tableDestroyed(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void tableDestroyed(TableEvent event) {

        logger.debug(" Table Destroyed "
                + event.getTableServer().getTableNumber());
        removeTable(event.getTableServer());

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.SpaceListener#playerJoined(py.edu.uca.fcyt.toluca.game.TrucoPlayer)
     */
    public void playerJoined(TrucoPlayer player) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.SpaceListener#playerLeft(py.edu.uca.fcyt.toluca.game.TrucoPlayer)
     */
    public void playerLeft(TrucoPlayer player) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.SpaceListener#chatMessageRequested(py.edu.uca.fcyt.game.ChatPanelContainer,
     *      py.edu.uca.fcyt.toluca.game.TrucoPlayer, java.lang.String)
     */
    public void chatMessageRequested(ChatPanelContainer cpc,
            TrucoPlayer player, String htmlMessage) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.SpaceListener#chatMessageSent(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void chatMessageSent(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /**
     * @return Returns the properties.
     */
    public java.util.Properties getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            The properties to set.
     */
    public void setProperties(java.util.Properties properties) {
        this.properties = properties;
    }

    /**
     *  
     */
    private void leerComandos() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            System.out.print("roomServer> ");
            String command = in.readLine().trim();
            while (!command.equals("salir")) {
                if (command.equalsIgnoreCase("showUsers")) {
                    showUsers();
                } else if (command.equalsIgnoreCase("showComm")) {
                    showCommunicators();
                } else if (command.equalsIgnoreCase("showTables")) {
                    showTables();
                } else if (command.trim().length() > 0) {
                    System.out.println("Comando incorrecto");
                }
                System.out.print("roomServer> ");
                command = in.readLine();
            }
            System.out
                    .println("Finalizo la sesion del administrador en el server");
            System.exit(0);

        } catch (IOException ioe) {
            // Communication is broken
        }
    }

    /**
     *  
     */
    private synchronized void showTables() {
        ArrayList list = new ArrayList();
        TableServer ts[] = getTablesServers();
        for (int i = 0; i < ts.length; i++) {
            list.add(ts[i]);
        }

        showList(list);
    }

    /**
     *  
     */
    private synchronized void showUsers() {
        showHashMap(getHashPlayers());
    }

    private synchronized void showCommunicators() {
        showList(getConnManager().getVecSesiones());

    }

    public synchronized void showHashMap(java.util.HashMap ht) {
        java.util.Iterator it = ht.values().iterator();
        int i = 0;
        System.out.println("Toluca:");
        while (it.hasNext()) {
            System.out.println("element #" + (i++) + " -> " + (it.next()));
        }
        System.out.println("terminado");
    }

    public synchronized void showList(java.util.List l) {
        java.util.Iterator it = l.iterator();
        int i = 0;
        System.out.println("Toluca:");
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof TableServer)
                System.out.println("element #" + (i++) + " -> "
                        + ((TableServer) o).toString2());
            else
                System.out.println("element #" + (i++) + " -> " + (o));
        }
        System.out.println("terminado");

    }
    public Communicator getCommunicator(TrucoPlayer player)
    {
    	
    	Iterator it=connManager.getVecSesiones().iterator();
    	
    	while(it.hasNext())
    	{
    		CommunicatorServer comm=(CommunicatorServer) it.next();
    		if(comm.getTrucoPlayer().getName().equals(player.getName()))
    		{
    			return comm;
    		}
    	}
    		
		
    	
    	return null;
    }

    /* (non-Javadoc)
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRequest(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRequest(RoomEvent event) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRejected(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRejected(RoomEvent re) {
        // TODO Auto-generated method stub
        
    }
} // end RoomServer

