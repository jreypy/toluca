package py.edu.uca.fcyt.toluca;

/** Java class "RoomClient.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import py.edu.uca.fcyt.game.ChatPanel;
import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;
import py.edu.uca.fcyt.toluca.guinicio.TableGame;
import py.edu.uca.fcyt.toluca.guinicio.TableRanking;
import py.edu.uca.fcyt.toluca.net.CommunicatorClient;
import py.edu.uca.fcyt.toluca.net.ConexionTester;
import py.edu.uca.fcyt.toluca.table.Table;

/**
 * 
 * @author Interfaz de Inicio
 */
public class RoomClient extends Room implements ChatPanelContainer,
        TableListener {

    protected Logger logeador = Logger.getLogger(RoomClient.class.getName());

    private ChatPanel chatPanel;

    private TableGame mainTable;

    private TableRanking rankTable;

    private RoomUING roomUING;

    private CommunicatorClient cc;
    private ConexionTester ct;
    private TrucoPlayer roomPlayer;

    ///////////////////////////////////////
    // operations

    public RoomClient(RoomUING rui) {
        super();
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Se crea el
        // roomClient");
        String serverString = rui.getParameter("serverString");
        String portNumberString = rui.getParameter("portNumber");
        String intervaloTestString=rui.getParameter("intervaloTest");
        int portNumber;
        long intervaloTest;
        try {
            portNumber = Integer.parseInt(portNumberString);
        } catch (java.lang.NumberFormatException e) {
            portNumber = 6767;
        }
        try
        {
           intervaloTest=Long.parseLong(intervaloTestString);
        }
        catch(NumberFormatException e)
        {
            intervaloTest=5000;
        }
        try {
            cc = new CommunicatorClient(this, serverString, portNumber);
            ct=new ConexionTester(cc,intervaloTest);
            setRoomUING(rui);
            addRoomListener(cc);
            //SwingUtilities.invokeLater(cc);
            Thread cct = new Thread(cc, "comm-client");
            Thread ctt = new Thread(ct,"conn-tester");
            cct.setPriority(Thread.MAX_PRIORITY);
            ctt.setPriority(Thread.MIN_PRIORITY);
            cct.start();
            ctt.start();
        } catch (IOException e) {
            rui.getLoginPanel().getJLestado().setText("<html><font color=\"ff0000\">Problemas al iniciar la conexión: " + e.getMessage() + "</font>");
            rui.getLoginPanel().getJLestado().setToolTipText("<html><font color=\"ff0000\">Problemas al iniciar la conexión: " + e.getMessage() + "</font>");
            rui.getLoginPanel().setToolTipText("<html><font color=\"ff0000\">Problemas al iniciar la conexión: " + e.getMessage() + "</font>");
            rui.getJEPanuncios().setToolTipText("<html><font color=\"ff0000\">Problemas al iniciar la conexión: " + e.getMessage() + "</font>");
            e.printStackTrace(System.out);
        }
        //init();
    }

    public void setRoomPlayer(TrucoPlayer player) {
        roomPlayer = player;
    }

    public TrucoPlayer getRoomPlayer() {
        return roomPlayer;
    }

    public void cerrarConexion() {
        try {
            cc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainTable(TableGame game) {
        this.mainTable = game;
    }

    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    public void addTable(Table table) {
        /** lock-end */
        // agrega la mesa a la lista de mesas de juego
        super.addTable(table);
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "desde el
        // roomClient se inserta mesa.");

        // Agregamos una fila a la Tabla Principal
        mainTable.insertarFila(table);

    } // end addTable /** lock-begin */

    public void removeTable(Table table) {
        /*
         * Linea nueva
         */
        // remueve la mesa de la Lista de Mesas de juego
        super.removeTable(table);

        //        // remueve la mesa de la Tabla Principal
        //        mainTable.eliminarFila(table.getTableNumber());
    }

    /*
     * Este metodo se ejecuta cuando se presiona el Join
     */
    public void joinTableRequest(int tableNumber) {
        fireTableJoinRequested(tableNumber);
    }

    /**
     * <p>
     * Informa a todos los <i>listeners </i> registrados que se esta intentando
     * ingresar a una tabla.
     * 
     * @param tableNumber
     *            El numero de tabla a la que queremos unirnos
     *            </p>
     */
    private synchronized void fireTableJoinRequested(int tableNumber) {
        /** lock-end */
        /*System.out
                .println("Voy a disparar el tableJoinRequest sobre la tabla: "
                        + tableNumber);*/
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_TABLE_JOIN_REQUESTED);
        re.setTableNumber(tableNumber);
        re.setPlayer(getRoomPlayer());
        Iterator iter = roomListeners.listIterator();
        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.tableJoinRequested(re);
        }
    } // end fireTableJoinRequested /** lock-begin */

    /**
     * @deprecated
     */
    public void createTableRequest() {
        createTableRequest(30);
    }
    
    public void createTableRequest(int points) {
        fireTableCreateRequested(points);
    }

    /**
	 * @param points
	 */
	private void fireTableCreateRequested() {
		fireTableCreateRequested(30);
	}

	/**
     * <p>
     * Informa a todos los <i>listeners </i> registrados que se esta intentando
     * crear una tabla nueva en el Room.
     */
    private synchronized void fireTableCreateRequested(int points) {
        /** lock-end */
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_CREATE_TABLE_REQUESTED);
        re.setPlayer(roomPlayer);
        re.setGamePoints(points);

        Iterator iter = roomListeners.listIterator();
        while (iter.hasNext()) {

            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.createTableRequested(re);
        }
    } // end fireTableCreateRequested /** lock-begin */

    public void sendChatMessage(TrucoPlayer player, String htmlMessage) {
        fireChatMessageRequested(player, htmlMessage, getOrigin());
    }

    /**
     * <p>
     * Informa a todos los <i>listeners </i> registrados que se esta intentando
     * enviar un mensaje de chat
     * </p>
     * <p>
     * crear un objeto chatMessage, roomEvent de tipo ChatResquested
     * </p>
     * <p>
     * 
     * @param player
     *            El jugador que esta intentando enviar el mensaje
     *            </p>
     *            <p>
     * @param htmlMessage
     *            El mensaje que se esta enviando
     *            </p>
     */
    private synchronized void fireChatMessageRequested(TrucoPlayer player,
            String htmlMessage, String origin) {
        /** lock-end */

        Iterator iter = roomListeners.listIterator();
        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.chatMessageRequested(this, player, htmlMessage);
        }
    } // end fireChatMessageRequested /** lock-begin */

    public void showChatMessage(TrucoPlayer player, String htmlMessage) {
        /** lock-end */
        chatPanel.showChatMessage(player, htmlMessage);
    } // end showChatMessage /** lock-begin */

    /*
     * Este metodo ingresa a un Player al Room, es decir se le muestra en la
     * Tabla del Ranking
     */
    public void addPlayer(TrucoPlayer player) {
        // agrega al player a la lista de players conectados
        super.addPlayer(player);
        // logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Gooool!!
        // Carajo");

        // agrega al player a la Tabla del Ranking
        if (getRankTable() == null)
            logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Nulooooo!!!");
        getRankTable().addPlayer(player);
    }

    /*
     * Remueve al player de las mesas de la tabla principal y del Ranking
     */
    public void removePlayer(TrucoPlayer player) {
        /** lock-end */

        // elimina al player de la lista de players conectados
        /*
         * for(Enumeration e=getHashTable().elements();e.hasMoreElements();) {
         * Table tabela=(Table)e.nextElement(); Vector
         * jugadores=tabela.getPlayers(); if(jugadores.contains(player)) {
         * tabela.kickPlayer(player); } }
         */
        super.removePlayer(player);

        // elimina al player de la Tabla del Ranking
        getRankTable().removeplayer(player);

        /*
         * linea nueva
         */
        // elimina al player de las mesas de la Tabla Principal
        mainTable.removeplayer(player);

    } // end removePlayer /** lock-begin */

    /*
     * Modifica al player en las mesas de la tabla principal y del Ranking
     */
    public void modifyPlayer(TrucoPlayer player) {
        /** lock-end */
        // modifica el player en la lista de players conectados
        super.modifyPlayer(player);

        // modifica el player de la Tabla del Ranking
        getRankTable().modifyplayer(player);

        /*
         * linea nueva
         */
        // modifica al player en las mesas de la Tabla Principal
        mainTable.removeplayer(player);

    }

    /*
     * Metodo nuevo para cuando al player se le cierra la ventana
     */
    public void eliminatePlayer() {
        fireEliminatePlayer(roomPlayer.getName());
    }

    /*
     * Metodo nuevo, avisa a todos los listeners q se cayo alguien...
     */
    public synchronized void fireEliminatePlayer(String playerName) {
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_PLAYER_LEFT);
        re.setUser(playerName);
        Iterator iter = roomListeners.listIterator();
        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.playerLeft(re);
        }
    }

    public synchronized void fireLoginRequested(String username, String password) {
        /** lock-end */
        RoomEvent re = new RoomEvent();
        re.setType(RoomEvent.TYPE_LOGIN_REQUESTED);
        re.setUser(username);
        re.setPassword(password);
        Iterator iter = roomListeners.listIterator();

        while (iter.hasNext()) {
            RoomListener ltmp = (RoomListener) iter.next();
            ltmp.loginRequested(re);
        }
    } // end fireLoginRequested /** lock-begin */

    public void loginCompleted(TrucoPlayer player) {
        /** lock-end */

        //chatPanel = new ChatPanel(this, player);
        //rui.addChatPanel(chatPanel);
        chatPanel.setPlayer(player);
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "el chatpanel
        // del room es "+chatPanel);
        setRoomPlayer(player);
//        player.setFullName(player.getName());        
        getRoomUING().loginCompleted(player);
    } // end loginCompleted /** lock-begin */
    public void actualizarRanking(TrucoPlayer trucoPlayer)
    {
    	getRoomUING().actualzarRanking(trucoPlayer);
    }
    /*
     * Ingresa al Player en la Tabla Principal como Observador
     */
    public void joinTable(RoomEvent re) {
        //Vector col = new Vector();
        //col = (Vector) re.getPlayerss();
        int tableNumber = re.getTableNumber();

        mainTable.addObserver(re.getPlayer(), re.getTableNumber());

        // mainTable.addPlayer( (TrucoPlayer) col.elementAt(0),tableNumber);
    }

    public void loginFailed(RoomEvent event) {
        getRoomUING().loginFailed(event);
    }

    /**
     * Getter for property rankTable.
     * 
     * @return Value of property rankTable.
     *  
     */
    public TableRanking getRankTable() {
        if (rankTable == null) {
            setRankTable(roomUING.getTableRanking());
        }
        return rankTable;
    }

    /**
     * Setter for property rankTable.
     * 
     * @param ranking
     *            New value of property rankTable.
     *  
     */
    public void setRankTable(TableRanking ranking) {
        /*
         * logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Se settea el
         * rank table -> " + ranking);
         */
        this.rankTable = ranking;
    }

    public void gameStartRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * Se registra cuando empieza el juego
     */
    public void gameStarted(TableEvent event) {
        // TODO Auto-generated method stub

        Table table = event.getTable();
        mainTable.setGameStatus(table.getTableNumber(), true);
    }

    /*
     * Se registra cuando finaliza el juego
     */
    public void gameFinished(TableEvent event) {
        // TODO Auto-generated method stub

        Table table = event.getTable();
        mainTable.setGameStatus(table.getTableNumber(), false);
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
        //Comentado por Cricco Dani nomas va a disparar
        // mainTable.removeplayer(event.getPlayer(),
        // event.getTable().getTableNumber());
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
     * Cuando el player se sento en una mesa, se le carga en la Tabla Principal.
     */
    public void playerSit(TableEvent event) {

        Table tabela = event.getTable();
        TrucoPlayer jug = tabela.getPlayer();
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "El player que
        // se sienta es" + jug.getName());
        //mainTable.pl
        // tabela.getChair(jug));

    }

    public void setearPlayerTable(TrucoPlayer player, Table tabela, int chair) {
        /*System.out
                .println("Se va a sentar al player Parche de Redes el CommunicatorClient le llama a este");*/

        mainTable.addPlayer(player, tabela.getTableNumber(), chair);
    }

    public void setStandPlayer(int chair, Table tabela) {
        mainTable.remPlayer(tabela.getTableNumber(), chair);
    }

    public void borrarPlayerTable(TrucoPlayer player, Table table) {

        mainTable.removeplayer(player, table.getTableNumber());

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#signSendRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void signSendRequest(TableEvent event) {
        // TODO Auto-generated method stub

    }

    public void signSent(TableEvent event) {
        // TODO Auto-generated method stub

    }

    public void showPlayed(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * Se le mete al player de observador
     */
    public void playerJoined(TrucoPlayer player) {
        // TODO Auto-generated method stub

        //mainTable.addObserver( player.getPlayer(),
        // player.getTable().getTableNumber());
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
     * @see py.edu.uca.fcyt.toluca.event.SpaceListener#chatMessageSent(py.edu.uca.fcyt.game.ChatPanelContainer,
     *      py.edu.uca.fcyt.toluca.game.TrucoPlayer, java.lang.String)
     */
    public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player,
            String htmlMessage) {
        // TODO Auto-generated method stub

    }

    public void seAgregoTable(Table t) {
        t.addTableListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.game.ChatPanelContainer#sendChatMessage(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void sendChatMessage(RoomEvent event) {
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

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#tableDestroyed(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void tableDestroyed(TableEvent event) {

    }

    public void tableDestroyed(Table table) {
        removeTable(table);
        roomUING.removeTable(table);
    }

    /**
     * @return Returns the uiNG.
     */
    public RoomUING getRoomUING() {
        return roomUING;
    }

    /**
     * @param uiNG
     *            The uiNG to set.
     */
    public void setRoomUING(RoomUING uiNG) {
        this.roomUING = uiNG;
    }
    public void testConexionReceive(long milisegundos)
    {
        getRoomUING().actualizarTestConexion(milisegundos);
        
        for (int i = 0; i < tables.length; i++) {
            
            if(tables[i]!=null)
            {
                tables[i].getJTrucoTable().actualizarConexionStatus(milisegundos);
            }
        }
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

    /**
     * @param string
     */
    public void showSystemMessage(String string) {
        getRoomUING().getChatPanel().showSystemMessage(string, null);
    }
} // end RoomClient
