package py.edu.uca.fcyt.toluca.net;





import java.io.IOException;
import java.util.logging.Logger;

import py.edu.uca.fcyt.game.ChatMessage;
import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.net.XmlPackagesSession;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;


/**
 * @author CYT UCA
 *
 * 
 */
public abstract class  Communicator extends XmlPackagesSession
implements RoomListener,TrucoListener,TableListener
{
    private Logger logger=Logger.getLogger(Communicator.class.getName());
	private TrucoPlayer trucoPlayer;
	/**
	 * @return Returns the trucoPlayer.
	 */
	public TrucoPlayer getTrucoPlayer() {
		return trucoPlayer;
	}
	/**
	 * @param trucoPlayer The trucoPlayer to set.
	 */
	public void setTrucoPlayer(TrucoPlayer trucoPlayer) {
		this.trucoPlayer = trucoPlayer;
	}
	public Communicator(EventDispatcher eventDispatcher)
	{
		this();
		this.eventDispatcher=eventDispatcher;
	}
	public Communicator()
	{
		super();
	}
	/**
	 * @return Returns the eventDispatcher.
	 */
	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}
	/**
	 * @param eventDispatcher The eventDispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
//	static Logger logger = Logger.getLogger(Communicator.class);
	
	protected EventDispatcher eventDispatcher;

	
	public String getInitErrorMessage(int errcode) {
		
		return "Error CODE : "  + errcode;
	}

	
	
	/* (non-Javadoc)
     * @see py.edu.uca.fcyt.net.XmlPackagesSession#init()
     */
    public int init() throws IOException {
    	return XmlPackagesSession.XML_PACKAGE_SESSION_INIT_OK;
    }

	
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.net.XmlPackagesSession#connectionFailed()
	 */
	public void connectionFailed() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.net.XmlPackagesSession#receiveObject(java.lang.Object)
	 */
	public void receiveObject(Object bean) {
			
/*	    try
	    {
		logger.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"El Communicator de: "+trucoPlayer.getName());
	    }
	    catch(NullPointerException e)
	    {
	        logger.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"Truco Player "+trucoPlayer);
	    }*/
		if(bean instanceof RoomEvent)
		{
			eventDispatcher.dispatchEvent((RoomEvent)bean);
		}
		if(bean instanceof TableEvent)
		{
			eventDispatcher.dispatchEvent((TableEvent)bean);
		}
		if(bean instanceof TrucoEvent)
		{
			eventDispatcher.dispatchEvent((TrucoEvent)bean);
		}
		if(bean instanceof TrucoPlay)
		{
			eventDispatcher.dispatchEvent((TrucoPlay)bean);
		}
		
	}
	
	
	/**
     * @return
     */
    private Object getBolemObject() {
        RoomEvent ret = new RoomEvent();
        TableServer ts = new TableServer();
        TrucoPlayer tp = new TrucoPlayer();
        tp.setName("aa");
        tp.setRating(606);
        ts.setHost(tp);
        ret.setTableServer(ts);
        return ret;
        
    }
    /* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#tableCreated(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void tableCreated(RoomEvent ev) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#tableRemoved(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void tableRemoved(RoomEvent re) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#tableModified(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void tableModified(RoomEvent ev) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#tableJoined(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void tableJoined(RoomEvent ev) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#createTableRequested(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void createTableRequested(RoomEvent ev) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#tableJoinRequested(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void tableJoinRequested(RoomEvent ev) {
	    
			//System.out.println("Se dispara un tableJoinRequest");
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#loginCompleted(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void loginCompleted(RoomEvent ev) {

		
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#loginRequested(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void loginRequested(RoomEvent ev) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#loginFailed(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void loginFailed(RoomEvent ev) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#playerLeft(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void playerLeft(RoomEvent ev) {
	    
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.SpaceListener#playerJoined(py.edu.uca.fcyt.toluca.game.TrucoPlayer)
	 */
	public void playerJoined(TrucoPlayer player) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.SpaceListener#playerLeft(py.edu.uca.fcyt.toluca.game.TrucoPlayer)
	 */
	public void playerLeft(TrucoPlayer player) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.SpaceListener#chatMessageRequested(py.edu.uca.fcyt.game.ChatPanelContainer, py.edu.uca.fcyt.toluca.game.TrucoPlayer, java.lang.String)
	 */
	public void chatMessageRequested(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {
			
			
				
			
			RoomEvent event=new RoomEvent();
			event.setType(RoomEvent.TYPE_CHAT_REQUESTED);
			ChatMessage chatMsg=new ChatMessage(player,htmlMessage);
			chatMsg.setOrigin(cpc.getOrigin());
			event.setChatMessage(chatMsg);
			
			if(cpc instanceof Table)
				event.setTableNumber( ((Table)cpc).getTableNumber());
			super.sendXmlPackage(event);
			
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.SpaceListener#chatMessageSent(py.edu.uca.fcyt.game.ChatPanelContainer, py.edu.uca.fcyt.toluca.game.TrucoPlayer, java.lang.String)
	 */
	public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {

		RoomEvent event=new RoomEvent();
		event.setType(RoomEvent.TYPE_CHAT_SENT);
		ChatMessage chatMsg=new ChatMessage(player,htmlMessage);
		chatMsg.setOrigin(cpc.getOrigin());
		event.setChatMessage(chatMsg);
		super.sendXmlPackage(event);
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#play(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void play(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#playResponse(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void playResponse(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#turn(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void turn(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#endOfHand(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void endOfHand(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#cardsDeal(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void cardsDeal(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#handStarted(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void handStarted(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#gameStarted(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void gameStarted(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#endOfGame(py.edu.uca.fcyt.toluca.event.TrucoEvent)
	 */
	public void endOfGame(TrucoEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TrucoListener#getAssociatedPlayer()
	 */
	public TrucoPlayer getAssociatedPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#gameStartRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void gameStartRequest(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#gameStarted(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void gameStarted(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#gameFinished(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void gameFinished(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerStandRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerStandRequest(TableEvent event) {
		
		//System.out.println("player stand request");
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerStanded(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerStanded(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerKickRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerKickRequest(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerKicked(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerKicked(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerLeft(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerLeft(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerSitRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerSitRequest(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#playerSit(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void playerSit(TableEvent event) {
		// TODO Auto-generated method stub
	    //
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#signSendRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void signSendRequest(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#signSent(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void signSent(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#showPlayed(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void showPlayed(TableEvent event) {
		// TODO Auto-generated method stub
	    
	}

	public String toString() {
		try {
			return getTrucoPlayer().getName()+ " hash code:  " + hashCode() + " socket " + getSocket();			
		}catch (NullPointerException e) {
			return getClass().getName() + " Truco Player NULO" ;
		}

	}	

}
