package py.edu.uca.fcyt.toluca.net;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import py.edu.uca.fcyt.net.XmlPackagesSession;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;




/**
 * @author CYT UCA
 *
 * 
 */
public class CommunicatorClient extends Communicator{

	/**
	 * @param client
	 */
	private RoomClient roomClient;
	protected Logger logeador = Logger.getLogger(CommunicatorClient.class.getName());
	private String serverString;
	private int portNumber;
	private boolean loggedIn = false;
	private static final int SOCKET_TIMEOUT = 60000;
	
	public CommunicatorClient(RoomClient client,String serverString,int portNumber) throws IOException {
		
		this(serverString,portNumber);
		eventDispatcher.setRoom(client);
		roomClient=client;
	}
	//static Logger logger = Logger.getLogger(CommunicatorClient.class);
	public CommunicatorClient(String serverString,int portNumber) throws IOException
	{
		super(new EventDispatcherClient());
		this.serverString=serverString;
		this.portNumber=portNumber;
		((EventDispatcherClient)getEventDispatcher()).setCommClient(this);
		int retinit = init();
	}
	public int init() throws IOException
	{
		int ret = -1;
		
			setSocket(new Socket(getServerString(), portNumber));
			/*Agregado por aa para cortar la comunicación*/
			//getSocket().setSoTimeout(CommunicatorClient.SOCKET_TIMEOUT);
			
			ret = XmlPackagesSession.XML_PACKAGE_SESSION_INIT_OK;
			logeador.log(Level.FINEST,"Se establecio la coneccion con el servidor");		
		return ret;
	}
	public void connectionFailed(String msg)
	{
		logeador.log(TolucaConstants.CLIENT_ERROR_LOG_LEVEL,"Falló la conexión: "+msg);
	}
	
	//REQUEST DEL ROOM
	public void loginRequested(RoomEvent event) {
		super.sendXmlPackage(event);
	}
	public void createTableRequested(RoomEvent event) {
		super.sendXmlPackage(event);
	}
	public void tableJoinRequested(RoomEvent event) {

					
			super.sendXmlPackage(event);
		
	}
	
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.net.Communicator#receiveObject(java.lang.Object)
	 */
	public void receiveObject(Object bean) {

		if (isLoggedIn()) 
			super.receiveObject(bean);			
		else if(bean instanceof RoomEvent)
		{
			RoomEvent event = (RoomEvent)bean;

			if (event.getType() == RoomEvent.TYPE_LOGIN_COMPLETED || event.getType() == RoomEvent.TYPE_LOGIN_FAILED)	
				super.receiveObject(bean);
		}		

	}
	
	/**
	 * @return
	 */
	private boolean isLoggedIn() {
		return loggedIn;
	}
	//REQUEST DE LA TABLA
	public void playerSitRequest(TableEvent event) {
		super.sendXmlPackage(event);
		
	}
	public void playerStandRequest(TableEvent event) {
		super.sendXmlPackage(event);
	}
	public void playerKickRequest(TableEvent event) {
		super.sendXmlPackage(event);
		
	}
	public void playerLeft(TableEvent event) {
	    logeador.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"playerLeft "+event.getPlayer()[0]);
		super.sendXmlPackage(event);
		
	}
	public void gameStartRequest(TableEvent event) {
		super.sendXmlPackage(event);
		
	}
	
	//METODOS CORRESPONDIENTES AL TRUCO_GAME
	
	public void play(TrucoEvent event) {
		/*System.out.println("play");
		System.out.println("El trucoplayer de este comm es  "+getTrucoPlayer());
		System.out.println("El play hizo "+event.getPlayer());
		System.out.println("eS DE TIPO "+event.getType());*/
		if(event.getPlayer().getName().equals(getTrucoPlayer().getName()))
		{
			
		
	//	System.out.println(getClass().getName()+"se va a hacer un play al server");
			
		TrucoPlay trucoPlay= event.toTrucoPlay();
	
//		logger.debug("SE resive un play de "+trucoPlay.getPlayer().getName());
//		logger.debug("TAbla : "+trucoPlay.getTableNumber());
//		logger.debug("type : "+trucoPlay.getType());
//		if(trucoPlay.getCard()!=null)
//			logger.debug("carta Palo: "+trucoPlay.getCard().getKind() +" val "+trucoPlay.getCard().getValue());
//		logger.debug("value > "+trucoPlay.getValue());
		super.sendXmlPackage(trucoPlay);
		}
	}
	
	public void playResponse(TrucoEvent event) {
	
		//logger.debug("playResponse");
	}
	
	public void turn(TrucoEvent event) {
	
		//logger.debug("Turn");
	}
	
	public void endOfHand(TrucoEvent event) {
	
		
	}
	
	public void cardsDeal(TrucoEvent event) {
	
		
	}
	
	public void handStarted(TrucoEvent event) {
		// TODO Auto-generated method stub
		
	}
	public void signSendRequest(TableEvent event) {
		super.sendXmlPackage(event);
	}
	
	
	
	public void chatMessageSent(RoomEvent event) {
		
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.TableListener#tableDestroyed(py.edu.uca.fcyt.toluca.event.TableEvent)
	 */
	public void tableDestroyed(TableEvent event) {
		
	}
	
	
	
	/*public static void main(String[] args) {
	DOMConfigurator.configure(System.getProperty("user.dir")
            + System.getProperty("file.separator") + "log.xml");
	CommunicatorClient comm=new CommunicatorClient();
	
	comm.sendXmlPackage(new JButton("Hello, world"));
	
	
	try {
		comm.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	while(true)
			;
}*/
	public String getServerString() {
		return serverString;
	}
	public void setServerString(String serverString) {
		this.serverString = serverString;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.event.RoomListener#rankingChanged(py.edu.uca.fcyt.toluca.event.RoomEvent)
	 */
	public void rankingChanged(RoomEvent ev) {
		// TODO Auto-generated method stub
		
	}
	
    /* (non-Javadoc)
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRequest(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRequest(RoomEvent event) {
        sendXmlPackage(event);
    }
    /* (non-Javadoc)
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRejected(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRejected(RoomEvent re) {
        sendXmlPackage(re);
    }
}
