package py.edu.uca.fcyt.toluca.table;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.RoomServer;
import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;

/**
 *
 * @author  PABLO JAVIER
 */
public class TableServer  implements TrucoListener, ChatPanelContainer {
	
	private RoomServer roomServer;
	static Logger logger = Logger.getLogger(TableServer.class.getName());
    /** Holds value of property host. */
    private TrucoPlayer host;
	private int gamePoints;    
    protected Vector tableListeners; // of type Vector
    // blocks synchronized added by aa 20050902
    protected Vector players;
    
    protected PlayerManager pManager;
    
    /** Holds value of property tableNumber. */
    private int tableNumber;
    private TrucoGame tGame;
    
    private static int nextTableNumber = 0;
    private HashMap asientos;
    /** Creates a new instance of TableServer */
    public TableServer() {        
        tableListeners = new Vector();
        players = new Vector();
        pManager = new PlayerManager(6);        
        setTableNumber(nextTableNumber++);
        //System.out.println("EL TABLE NUMBER SETEADO ES: " + getTableNumber());
        //System.out.println("El HOST de la tabela es: " + getHost().getName());
        asientos=new HashMap();
    }
   
    /**
     * 
     * @param host
     * @deprecated
     */
    public TableServer(TrucoPlayer host)
    {
        this(host, 30);
    }
    
	/**
	 * @param player
	 * @param points
	 */
	public TableServer(TrucoPlayer player, int points) {
		this();
        setHost(player);
        setGamePoints(points);
	}
	/**
	 * @return Returns the pManager.
	 */
	public PlayerManager getPManager() {
		return pManager;
	}
	/**
	 * @param manager The pManager to set.
	 */

    public void addTableServerListener(TableListener tableListener) {        /**
     * lock-end */
    	synchronized(tableListener) {//added by aa
            tableListeners.add(tableListener);    		
    	}

    } // end addRoomListener        /** lock-begin */

    /**
     * 
     * Sincronizamos el metodo para ver si arregla el bug de las sentadas.
     * 
     * @param player
     * @param chair
     */
    public synchronized void sitPlayer(TrucoPlayer player, int chair) {
        try
		{
            
//    	logger.log(Level.WARNING, "Sit player "+player+" chair "+chair);

            //Aca parece que esta el error de las sentadas
            //Despues se vuelve a intentar sentar en el pManager
            //No se xq el tableServer tiene q sentar, siendo q esto es chilista :?
        pManager.sitPlayer(player, chair);
            
    	asientos.put(player.getName(),new Integer(chair));
        firePlayerSat(player, chair);
        
        if (player == getHost())
            pManager.setPlayerLocal(getHost());
        
//        logger.log(Level.WARNING, player.getName() + " sitted in server chair " + chair + " in table of " + getHost().getName());
		}
        catch(TableException e)
		{
        	logger.log(Level.WARNING, "TableException "+e.getMessage());
		}
    }
    
    /**
     * @deprecated
     *
     */
    public void startGame() {
    	startGame(30);
    }
    public void startGame(int points) {
        
        //TrucoGame tGame;
        TrucoTeam tTeams[];
        
        logger.info("Requesting game start...");
        tTeams = createTeams();
        
        // se crea el TrucoGame con los teams creados
        tGame = new TrucoGame(tTeams[0], tTeams[1], points);
        tGame.addTrucoListener(this);  

		//primero disparamos el evento, asi los cc se registran
		// como listeners del tgame
		fireGameStarted(
		new TableEvent(TableEvent.EVENT_gameStarted, this, null, null,-1)
		);

        //empieza realmente el juego y se disparan los eventos correspondientes
        tGame.startGame();
        tGame.setTableNumber(getTableNumber());//esto es necesario para que se sepa de donde sacar el game en el cliente
    }
    
    public TrucoTeam[] createTeams() {
        TrucoTeam tTeams[];
        TrucoPlayer player;
        
		if (tGame != null)
		{ 
			tTeams = new TrucoTeam[]
			{
				tGame.getTeam(0),
				tGame.getTeam(1)
			};
			return tTeams;
		}
        
        // se crean los teams
        tTeams = new TrucoTeam[] {
            new TrucoTeam("Rojo"),
            new TrucoTeam("Azul")
        };
        
        // se agregan los players a los teams
        for (int i = 0; i < pManager.getPlayerCount(); i++) {
            player = pManager.getPlayer(i);
            if (player != null) {
                tTeams[i % 2].addPlayer(player);
            }
        }
        
        return tTeams;
    }
    
    
    public void cardsDeal(TrucoEvent event) {
    }
    
    public void endOfGame(TrucoEvent event) {
		//pManager = new PlayerManager(6);
    	//con el PlayerManager viejo van a seguir sentados en el mismo lugar
    	//esto caga porque no se pueden cambiar de lugar nada mas
    	//TODO Ver la opcion para que los player se puedan parar
    	
    	TrucoGame tg = event.getGame();
    	TrucoTeam team1 = tg.getTeams()[0];
    	TrucoTeam team2 = tg.getTeams()[1];

    	
    	
    	
    	
    	try {
			obtenerRoomServer().getDbOperations().updateGameData(team1, team2, tg.getTeamGanador());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tGame = null;
		
    	RoomServer roomServer=obtenerRoomServer();
    	Iterator it=team1.getPlayers().iterator();
//    	GUARDA EL RANKING ACTUAL EN OLD RANKING
    	//ESO SE USA PARA QUE EL PLAYER SEPA CUANTO TENIA
    	//Y LUEGO CON CUANTO SE QUEDA
    	//LE AVISA AL ROOMSERVER QUE CAMBIO EL RANKING DE TODOS LOS DEL TEAM1
    	while(it.hasNext())
    	{
    		TrucoPlayer tPlayer=(TrucoPlayer) it.next();
    		tPlayer.setOldRating(tPlayer.getRating());
    		try {
				tPlayer.setRating(roomServer.getDbOperations().getTrucoPlayerRanking(tPlayer));
			} catch (SQLException e1) {
				logger.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,"SQLException : "+e1.getMessage());
			}
    		roomServer.rankingChanged(tPlayer);
    	}
    	it=team2.getPlayers().iterator();
//    	LE AVISA AL ROOMSERVER QUE CAMBIO EL RANKING DE TODOS LOS DEL TEAM1
    	while(it.hasNext())
    	{
    		TrucoPlayer tPlayer=(TrucoPlayer) it.next();
    		tPlayer.setOldRating(tPlayer.getRating());
    		try {
				tPlayer.setRating(roomServer.getDbOperations().getTrucoPlayerRanking(tPlayer));
			} catch (SQLException e1) {
				logger.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,"SQLException : "+e1.getMessage());
			}
    		roomServer.rankingChanged(tPlayer);
    		
    	}
		
    }
    
    public void endOfHand(TrucoEvent event) {
    }
    
    public void gameStarted(TrucoEvent event) {
    }
    
    public TrucoPlayer getAssociatedPlayer() {
        return host;
    }
    
    public void handStarted(TrucoEvent event) {
        // IGNORAR en el lado del servidor
    }
    
	public void play(TrucoEvent event) {
		// IGNORAR en el lado del servidor
		// El TableServer no sirve absolutamente para nada
	}

	public void playResponse(TrucoEvent event) {
		// IGNORAR en el lado del servidor
		// El TableServer no sirve absolutamente para nada
	}
    
    public void turn(TrucoEvent event) {
        // IGNORAR en el lado del servidor
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
     *
     */
    public void sendChatMessage(TrucoPlayer player, String htmlMessage) {
        fireChatMessageSent(player, htmlMessage);
    }
    
    /**
     * Dispara el evento de chatMessageSent
     */
    protected void fireChatMessageSent(TrucoPlayer jogador, String htmlMessage) {
    	synchronized(tableListeners) {//added by aa
            Iterator iter = tableListeners.listIterator();
            int i =0;
            while(iter.hasNext()) {
                TableListener ltmp = (TableListener)iter.next();
                //System.out.println(jogador.getName() + " enviando message sent al listener #" + (i++) + " clase:" + ltmp.getClass().getName());
                ltmp.chatMessageSent(this, jogador, htmlMessage);
            }
    	}
    }
    
    protected void fireGameStarted(TableEvent te) {
    	synchronized( tableListeners) {
        	Iterator iter = tableListeners.listIterator();
            
            while(iter.hasNext()) {
                TableListener ltmp = (TableListener)iter.next();
                //System.out.println("VOY A DISPARAR UN GAME STARTED EN EL SERVA. -> " + ltmp.getClass().getName());
                ltmp.gameStarted(te);
            }
    	}
    }
    
    protected void firePlayerSat(TrucoPlayer jogador, int chair ) {
    	synchronized( tableListeners) {
            Iterator iter = tableListeners.listIterator();
            int i =0;
//            logger.log(Level.WARNING, "tableListeners.size() = "+tableListeners.size());
            while(iter.hasNext()) {
                TableListener ltmp = (TableListener)iter.next();
                //System.out.println(jogador.getName() + " enviando message sent al listener #" + (i++) + " clase:" + ltmp.getClass().getName());
                TableEvent te= new TableEvent(TableEvent.EVENT_playerSit,this, jogador, null,chair);
                ltmp.playerSit(te);
            }
    	}
    }
    
    public void showChatMessage(TrucoPlayer player, String htmlMessage) {
    }
    
    /** Getter for property host.
     * @return Value of property host.
     *
     */
    public TrucoPlayer getHost() {
        return this.host;
    }
    
    /** Setter for property host.
     * @param host New value of property host.
     *
     */
    public void setHost(TrucoPlayer host) {
        this.host = host;
        addPlayer(host);        
    }
    
    public void addPlayer(TrucoPlayer player) 
    {
        players.add(player);
    }
    
    /** Getter for property players.
     * @return Value of property players.
     *
     */
    public java.util.Vector getPlayers() {
        return players;
    }
    
    /** Setter for property players.
     * @param players New value of property players.
     *
     */
    public void setPlayers(java.util.Vector players) {
        this.players = players;
    }
    
    /** Getter for property tableNumber.
     * @return Value of property tableNumber.
     *
     */
    public int getTableNumber() {
        return this.tableNumber;
    }
    
    /** Setter for property tableNumber.
     * @param tableNumber New value of property tableNumber.
     *
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
    
    public String getOrigin() {
        return String.valueOf(getTableNumber());
    }
    
    public TrucoGame getTrucoGame() {
        return tGame;
    }

	public void standPlayer(TableEvent event)
	{
		pManager.standPlayer(event.getValue());
		TrucoPlayer player=event.getPlayer(0);
		asientos.remove(player.getName());
		event.setEvent(TableEvent.EVENT_playerStanded);
		// avisa que el player se levant� correctamente
		synchronized (tableListeners) {
			for (int i = 0; i < tableListeners.size(); i++)
				((TableListener) tableListeners.get(i)).playerStanded(event);
		}
	}

	public void showSign(TableEvent event)
	{
		synchronized(tableListeners) {
			for (int i = 0; i < tableListeners.size(); i++)
				((TableListener) tableListeners.get(i)).signSent(event);
		}
	}
	
	public void kickPlayer(TrucoPlayer playerKicked) 
	{

	    TableBeanRepresentation tBean = null;
	    TableEvent tEvent = null;
	    
		//System.out.println("Se fue: " + tptmp.getName());
		int chair=getChair(playerKicked);
		if(chair>=0)
		{//si estaba sentado primero que se pare
			TableEvent e=new TableEvent();
			e.setEvent(TableEvent.EVENT_playerStandRequest);
			e.setTableServer(this);
			e.setPlayer(new TrucoPlayer[]{playerKicked,null});
			e.setValue(chair);
			standPlayer(e);
			
			TrucoGame trucoGame=getTrucoGame();
			if(trucoGame!=null)
			{//que vea si hay juego
			    
			    if(trucoGame.getTeam(0).getPlayer(playerKicked.getName()) != null)
			    {//si el team 0 le tenia tiene que ganar el team 1
			        
			        
			        trucoGame.setTeamGanador(1);
			        
			    }
			    if(trucoGame.getTeam(1).getPlayer(playerKicked.getName()) != null)
			    {//si el team 1 le tenia tiene que ganar el team 0
			        
			        trucoGame.setTeamGanador(0);
			        
			    }
			    
				getTrucoGame().fireEndOfGameEvent();
			}
		}
		getPlayers().remove(playerKicked);
		
//TODO: Agregado por aa, quito el jugador de entre los listeners del trucogame
//		TrucoGame tg = getTrucoGame();
//		tg.removeTrucoListener(tptmp);
		
//		TableEvent te = new TableEvent(TableEvent.EVENT_playerKicked, this, tptmp, null,0);
		//TODO PP total. Cambiamos porque del lado del cliente al recibir el XML se cambiaba
		// el user que debia salir de la mesa. El caso del host saliendo de la mesa 
		// que dejaba su usuario en la mesa de todos los demas
		tEvent = new TableEvent();
		tBean = new TableBeanRepresentation(getTableNumber(), null);
		tBean.setHostPlayer(getHost());
		tEvent.setPlayer(new TrucoPlayer[]{playerKicked,null});
		tEvent.setEvent(TableEvent.EVENT_playerKicked);
		tEvent.setTableBeanRepresentation(tBean);
		firePlayerKicked(tEvent);
		comprobarTable();
	}
	public void comprobarTable()
	{
		if(getPlayers().size()==0)
		{//hay que eliminar la tabla
		    logger.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"Se va a destruir la tabla: "+getTableNumber());
			fireTableDestroyed();
			
		}
			
	}
	private void fireTableDestroyed()
	{
		synchronized(tableListeners) {
			Iterator iter=tableListeners.listIterator();
			TableEvent event=new TableEvent();
			event.setTableServer(this);
			event.setEvent(TableEvent.EVENT_TABLE_DESTROYED);
			while(iter.hasNext())
				((TableListener)iter.next()).tableDestroyed(event);
		}
	}
	private void firePlayerKicked(TableEvent te) {
		synchronized(tableListeners){
			Iterator iter = tableListeners.listIterator();
			int i =0;
			while(iter.hasNext()) {
				TableListener ltmp = (TableListener)iter.next();
				ltmp.playerKicked(te);
			}
		}
	}
	

	
	public int getChair(TrucoPlayer p)
	{
		return pManager.getChair(p);
	}
	
	/**
	 * @return Returns the asientos.
	 */
	public HashMap getAsientos() {
		return asientos;
	}
	/**
	 * @param asientos The asientos to set.
	 */
	public void setAsientos(HashMap asientos) {
		this.asientos = asientos;
	}
	public Integer getAsiento(TrucoPlayer player)
	{
		return (Integer) asientos.get(player.getName());
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.game.ChatPanelContainer#sendChatMessage(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void sendChatMessage(RoomEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString2() {
		String ret = new String();
		
		TrucoGame tg = getTrucoGame();
		try {
			Iterator it = tg.getListaListeners().iterator();
			
			while (it.hasNext()) {
				Object o = it.next();
				ret += (o.equals(this)) ? "\n\tTableServer" : "\n\t" + o.toString();
			}
			return ret;
		} catch (NullPointerException npe) {
			return super.toString()+ " Sin Juego asociado";
		}
		
	}
	
	public RoomServer obtenerRoomServer() {
		return roomServer;
	}
	public void guardarRoomServer(RoomServer roomServer) {
		this.roomServer = roomServer;
	}
	public int getGamePoints() {
		return gamePoints;
	}
	public void setGamePoints(int gamePoints) {
		this.gamePoints = gamePoints;
	}
}
