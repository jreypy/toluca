package py.edu.uca.fcyt.toluca.net;

import org.apache.log4j.Logger;

import py.edu.uca.fcyt.game.ChatMessage;
import py.edu.uca.fcyt.toluca.RoomServer;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.TableBeanRepresentation;
import py.edu.uca.fcyt.toluca.table.TableServer;

/**
 * @author dcricco
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EventDispatcherServer extends EventDispatcher {
    static Logger logger = Logger.getLogger(EventDispatcherServer.class);

    private CommunicatorServer communicatorServer;

    public EventDispatcherServer(CommunicatorServer communicatorServer) {
        this.communicatorServer = communicatorServer;
    }

    public void loginRequested(RoomEvent event) {
        //		logger.debug("Se recibio un login Requested");

        ((RoomServer) room).login(event.getUser(), event.getPassword(),
                communicatorServer);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#loginCompleted(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void loginCompleted(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerJoined(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void playerJoined(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerLeft(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void playerLeft(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#loginFailed(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void loginFailed(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#chatRequested(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void chatRequested(RoomEvent event) {

        ChatMessage chatMsg = event.getChatMessage();
        logger.debug("Llego un chat Requested de :");
        logger.debug(chatMsg.getPlayer());
        logger.debug(chatMsg.getHtmlMessage());
        //	if(chatMsg.getOrigin().equals("room"))
        room.sendChatMessage(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#chatSend(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void chatSend(RoomEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#createTableRequest(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void createTableRequest(RoomEvent event) {
        //		logger.debug("Llego un Create Table");

        TrucoPlayer player = communicatorServer.getTrucoPlayer();
        ((RoomServer) room).createTable(player, event.getGamePoints());

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#tableCreated(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void tableCreated(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#tableJoinRequested(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void tableJoinRequested(RoomEvent event) {

        logger.info(event.getPlayer() + " solicita unirse a la mesa : "
                + event.getTableNumber());
        /*
         * logger.debug("Llego una solicitud para unirse a la mesa" +
         * event.getTableNumber()); logger.debug("La solicitud fue de " +
         * event.getPlayer());
         */

        TableServer tableServer = room.getTableServer(event.getTableNumber());
        if (tableServer.getTrucoGame() == null) {
            RoomEvent e = new RoomEvent();
            e.setType(RoomEvent.TYPE_TABLE_JOIN_REQUESTED);
            e.setPlayer(communicatorServer.getTrucoPlayer());
            e.setTableServer(tableServer);
            ((RoomServer) room).joinTable(e);
        } else {
            logger.info("No se puede unir a una mesa que ya tiene juego");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#tableJoined(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void tableJoined(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerSitRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerSitRequest(TableEvent event) {

        logger.info("Jugador " + event.getPlayer()[0]
                + " quiere sentarse en la silla " + event.getValue()
                + " de la mesa: " + event.getTableBeanRepresentation().getId());
        /*
         * logger.debug("Se recibe un playerSitRequest");
         * logger.debug(event.getPlayer()[0]);
         * logger.debug("TableNumber"+event.getTableBeanRepresentation().getId());
         * logger.debug("Silla "+ event.getValue());
         */

        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TrucoPlayer playerCliente = event.getPlayer()[0];
        int chair = event.getValue();

        TableServer tableServer = room.getTableServer(tableClient.getId());
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());

        tableServer.sitPlayer(playerServidor, chair);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerSit(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerSit(TableEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerStandRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerStandRequest(TableEvent event) {
        logger.debug("Player stand request");
        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TrucoPlayer playerCliente = event.getPlayer()[0];
        int chair = event.getValue();

        TableServer tableServer = room.getTableServer(tableClient.getId());
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());

        TableEvent e = new TableEvent();
        e.setEvent(TableEvent.EVENT_playerStandRequest);
        e.setTableServer(tableServer);
        e.setPlayer(new TrucoPlayer[] { playerServidor, null });
        e.setValue(chair);
        tableServer.standPlayer(e);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerStand(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerStand(TableEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerKickRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerKickRequest(TableEvent event) {

        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TrucoPlayer playerCliente = event.getPlayer()[0];

        TableServer tableServer = room.getTableServer(tableClient.getId());
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());

        tableServer.kickPlayer(playerServidor);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerKicked(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerKicked(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerLeft(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void playerLeft(TableEvent event) {

        logger.info("Se recibe un playerLeft communicator de: ");
        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TrucoPlayer playerCliente = event.getPlayer()[0];

        TableServer tableServer = room.getTableServer(tableClient.getId());
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());

        logger.info("Player : " + playerServidor.getName());
        logger.info("TableServer: " + tableServer);
        logger.info("Table :  " + tableServer.getTableNumber());
        tableServer.kickPlayer(playerServidor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#gameStartRequest(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void gameStartRequest(TableEvent event) {

        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TableServer tableServer = room.getTableServer(tableClient.getId());

        logger.info("solicitud para iniciar el juego en la mesa "
                + tableServer.getTableNumber() + " echa por el jugador: "
                + event.getPlayer()[0]);

        tableServer.startGame(tableClient.getGamePoints());
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#gameStarted(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void gameStarted(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#receiveCards(py.edu.uca.fcyt.toluca.event.TrucoEvent)
     */
    public void signSend(TableEvent event) {

    }

    public void receiveCards(TrucoEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#infoGame(py.edu.uca.fcyt.toluca.event.TrucoEvent)
     */
    public void infoGame(TrucoEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#play(py.edu.uca.fcyt.toluca.game.TrucoPlay)
     */
    public void signSendRequest(TableEvent event) {

        TableBeanRepresentation tableClient = event
                .getTableBeanRepresentation();
        TableServer tableServer = room.getTableServer(tableClient.getId());

        TrucoPlayer playerClienteSenhador = event.getPlayer(0);
        TrucoPlayer playerClienteSenhado = event.getPlayer(1);

        TrucoPlayer playerServerSenhador = room.getPlayer(playerClienteSenhador
                .getName());
        TrucoPlayer playerServerSenhado = room.getPlayer(playerClienteSenhado
                .getName());

        /*
         * System.out.println(" *********************************llego una
         * senha**********************"); System.out.println(" El que hizo la
         * senha = "+playerServerSenhador.getName()); System.out.println(" el
         * que resive la senha = "+playerServerSenhado.getName());
         * System.out.println(" value = "+event.getValue());
         * System.out.println(" table "+tableServer.getTableNumber());
         * System.out.println("*********************************************************************");
         */

        TableEvent te = new TableEvent(TableEvent.EVENT_signSent, tableServer,
                playerServerSenhador, playerServerSenhado, event.getValue());
        tableServer.showSign(te);

    }

    public void play(TrucoPlay event) {

        /*
         * logger.debug("SE recibe un play de "+event.getPlayer().getName());
         * logger.debug("TAbla : "+event.getTableNumber()); logger.debug("type :
         * "+event.getType()); if(event.getCard()!=null) logger.debug("carta
         * Palo: "+event.getCard().getKind() +" val
         * "+event.getCard().getValue()); logger.debug("value >
         * "+event.getValue());
         */

        TableServer tableServer = room.getTableServer(event.getTableNumber());
        TrucoGame trucoGame = tableServer.getTrucoGame();
        TrucoPlayer playerCliente = event.getPlayer();
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());
        event.setPlayer(playerServidor);//es indispensable la traduccion de
        // referencias
        trucoGame.play(event);

    }

    public void tirarCarta(TrucoEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#playerConfirmado(py.edu.uca.fcyt.toluca.game.TrucoPlay)
     */
    public void playerConfirmado(TrucoPlay event) {

        TableServer tableServer = room.getTableServer(event.getTableNumber());
        TrucoGame trucoGame = tableServer.getTrucoGame();
        TrucoPlayer playerCliente = event.getPlayer();
        TrucoPlayer playerServidor = room.getPlayer(playerCliente.getName());
        trucoGame.startHand(playerServidor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#canto(py.edu.uca.fcyt.toluca.event.TrucoEvent)
     */
    public void canto(TrucoEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#cantarTanto(py.edu.uca.fcyt.toluca.event.TrucoEvent)
     */
    public void cantarTanto(TrucoEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#tableDestroyed(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void tableDestroyed(TableEvent event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#rankingChanged(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void rankingChanged(RoomEvent event) {
        // TODO Auto-generated method stub

    }

    public void testConexion(RoomEvent event) {
        //        logger.debug("Se resive un test de conexion");
        communicatorServer.sendXmlPackage(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#invitacion(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitacion(RoomEvent event) {

        Communicator commInvitado = ((RoomServer) room).getCommunicator(event
                .getPlayer());

        logger.debug(getCommunicatorServer().getTrucoPlayer()
                + " invitacion  a la mesa" + event.getTableNumber() + " para: "
                + event.getPlayer());
        
        event.setPlayer(getCommunicatorServer().getTrucoPlayer());
        commInvitado.invitationRequest(event);
    }

    public CommunicatorServer getCommunicatorServer() {
        return communicatorServer;
    }

    public void setCommunicatorServer(CommunicatorServer communicatorServer) {
        this.communicatorServer = communicatorServer;
    }

    /* (non-Javadoc)
     * @see py.edu.uca.fcyt.toluca.net.EventDispatcher#invitacionRejected(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitacionRejected(RoomEvent event) {

        Communicator commHost = ((RoomServer) room).getCommunicator(event
                .getPlayer());

        logger.debug(getCommunicatorServer().getTrucoPlayer()
                + " rechazó la invitación a la mesa" + event.getTableNumber() + " MOTIVO: "
                + event.getErrorMsg());        
        event.setPlayer(getCommunicatorServer().getTrucoPlayer());
        commHost.invitationRejected(event);   
    }
}