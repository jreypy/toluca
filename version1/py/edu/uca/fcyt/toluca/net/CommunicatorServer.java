package py.edu.uca.fcyt.toluca.net;

import java.io.IOException;
import java.net.SocketException;

import org.apache.log4j.Logger;

import py.edu.uca.fcyt.toluca.RoomServer;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.TableServer;

/**
 * @author CYT UCA
 * 
 *  
 */
public class CommunicatorServer extends Communicator {
    public static final int SOCKET_TIMEOUT = 180000;

    static Logger logger = Logger.getLogger(CommunicatorServer.class);

    private RoomServer roomServer;

    public CommunicatorServer() {
        super();
        setEventDispatcher(new EventDispatcherServer(this));

    }

    /**
     * @return Returns the roomServer.
     */
    public RoomServer getRoomServer() {
        return roomServer;
    }

    /**
     * @param roomServer
     *            The roomServer to set.
     */
    public void setRoomServer(RoomServer roomServer) {
        this.roomServer = roomServer;
        eventDispatcher.setRoom(roomServer);
        roomServer.addRoomListener(this);
    }

    public void connectionFailed(String msg) {
        try {
            logger.error("Fallo la coneccion de " + getTrucoPlayer()
                    + " communicator  " + this);
            logger.error("Fallo de conexión: " + msg);
            roomServer.removePlayer(getTrucoPlayer());
            roomServer.removeCommunicator(this);//para que se elimine ya no es
                                                // util

            close();
        } catch (IOException e) {

            logger.error("Fallo a", e);
        }
    }

    public void playerJoined(TrucoPlayer player) {

        //		logger.debug("Player joined");

        RoomEvent event = new RoomEvent();
        event.setType(RoomEvent.TYPE_PLAYER_JOINED);
        event.setPlayer(player);
        super.sendXmlPackage(event);
    }

    public void loginCompleted(RoomEvent event) {
        logger.debug("Login completed");

        try {
            getSocket().setSoTimeout(CommunicatorServer.SOCKET_TIMEOUT);
        } catch (SocketException e) {
            logger.error("Error en el socket en login Completed", e);
            e.printStackTrace();
        }
        super.sendXmlPackage(event);
        TableServer[] tablesServers = event.getTablesServers();
        for (int i = 0; i < tablesServers.length; i++) {//se agrega a todas las
                                                        // tablas como listener
            if (tablesServers[i] != null) {
                tablesServers[i].addTableServerListener(this);
            }
        }
    }

    public void playerLeft(RoomEvent event) {
        super.sendXmlPackage(event);
    }

    public void loginFailed(RoomEvent event) {
        super.sendXmlPackage(event);
    }

    public void tableCreated(RoomEvent event) {

        event.getTableServer().addTableServerListener(this);
        super.sendXmlPackage(event);
    }

    public void tableJoined(RoomEvent event) {
        //if(getTrucoPlayer().getName().equals(event.getPlayer().getName()))
        //	event.getTableServer().addTableServerListener(this);
        //		logger.info("Se unio a la tabla "+event.getTableNumber() + " el
        // player "+event.getPlayer());
        super.sendXmlPackage(event);

    }

    //EVENTOS DE LA TABLA
    public void playerSit(TableEvent event) {
        //		logger.debug("Player sit");
        super.sendXmlPackage(event);
    }

    public void playerStanded(TableEvent event) {
        super.sendXmlPackage(event);

    }

    public void chatMessageSent(RoomEvent event) {
        super.sendXmlPackage(event);

    }

    public void playerKicked(TableEvent event) {
        super.sendXmlPackage(event);

    }

    public void gameStarted(TableEvent event) {

        TrucoGame trucoGame = event.getTableServer().getTrucoGame();
        TableServer tableServer = event.getTableServer();
        trucoGame.setTableNumber(tableServer.getTableNumber());
        /*
         * Envia solamente si el jugador esta en la mesa
         */
        if (tableServer.getPlayers().contains(getTrucoPlayer())) {
            trucoGame.addTrucoListener(this);
            super.sendXmlPackage(event);
        }
    }

    public void signSent(TableEvent event) {
        super.sendXmlPackage(event);

    }

    //METODOS DEL TRUCOGAME

    public void play(TrucoEvent event) {

        try {
            //			logger.info("Play "+getTrucoPlayer().getName());
            TableServer tableServer = roomServer.getTableServer(event
                    .getTableNumber());

            if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                    // solamente
                                                                    // si esta
                                                                    // en la
                                                                    // tabla
                super.sendXmlPackage(event);
        } catch (NullPointerException e) {
            logger
                    .error("El player es nulo en play de Communicator server!",
                            e);
            logger.error("El communicator es: " + this);

        }

    }

    public void playResponse(TrucoEvent event) {
        /*
         * try { logger.info("Play Response "+getTrucoPlayer().getName()); }
         * catch (NullPointerException e ) { logger.error("El player es nulo en
         * play de Communicator server!", e); }
         */
    }

    public void turn(TrucoEvent event) {
        /*
         * try { logger.info("Turn " + getTrucoPlayer().getName() + " event
         * type> " + event.getType()); } catch (NullPointerException e) {
         * logger.error("El player es nulo en play de Communicator server!",e);
         *  }
         */
    }

    public void endOfHand(TrucoEvent event) {

        //		logger.info("End of Hand");
        TableServer tableServer = roomServer.getTableServer(event
                .getTableNumber());

        if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                // solamente si
                                                                // esta en la
                                                                // tabla
            super.sendXmlPackage(event);
    }

    public void cardsDeal(TrucoEvent event) {

        //		logger.info("Se envia Cards Deal");
        TrucoCard[] cards = event.getCards();
        //		logger.info("Las cartas de "+event.getPlayer().getName()+ " son");
        //		for(int i=0;i<cards.length;i++)
        //		{
        //			System.out.println(cards[i].getKind() + " "+cards[i].getValue());
        //		}
        TableServer tableServer = roomServer.getTableServer(event
                .getTableNumber());

        if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                // solamente si
                                                                // esta en la
                                                                // tabla
            super.sendXmlPackage(event);

    }

    public void handStarted(TrucoEvent event) {

        //		logger.debug("************************EMPEZO LA MANO");
        TableServer tableServer = roomServer.getTableServer(event
                .getTableNumber());

        if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                // solamente si
                                                                // esta en la
                                                                // tabla
            super.sendXmlPackage(event);
    }

    public void gameStarted(TrucoEvent event) {

        //		logger.info("*******************************gameStarted");
        TableServer tableServer = roomServer.getTableServer(event
                .getTableNumber());

        if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                // solamente si
                                                                // esta en la
                                                                // tabla
            super.sendXmlPackage(event);
    }

    public void endOfGame(TrucoEvent event) {

        //		logger.info("End of game");
        TableServer tableServer = roomServer.getTableServer(event
                .getTableNumber());

        if (tableServer.getPlayers().contains(getTrucoPlayer()))//envia
                                                                // solamente si
                                                                // esta en la
                                                                // tabla
            super.sendXmlPackage(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#tableDestroyed(py.edu.uca.fcyt.toluca.event.TableEvent)
     */
    public void tableDestroyed(TableEvent event) {
        //		logger.debug(" Se va a informar la destruccion de la tabla
        // "+event.getTableServer().getTableNumber());
        super.sendXmlPackage(event);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.RoomListener#rankingChanged(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void rankingChanged(RoomEvent ev) {
        //		logger.debug("Para "+getTrucoPlayer().getName() + " se actualiza el
        // rating de: "+ev.getPlayer().getName());
        //	    logger.debug(">> se actualiza el rating de:
        // "+ev.getPlayer().getName());
        super.sendXmlPackage(ev);

    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRequest(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRequest(RoomEvent event) {
        sendXmlPackage(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.edu.uca.fcyt.toluca.event.TableListener#invitationRejected(py.edu.uca.fcyt.toluca.event.RoomEvent)
     */
    public void invitationRejected(RoomEvent re) {
        sendXmlPackage(re);
    }

}
