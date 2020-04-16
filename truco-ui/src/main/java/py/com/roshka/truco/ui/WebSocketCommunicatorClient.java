package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.RoomRequest;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;
import py.edu.uca.fcyt.net.CommunicatorClient;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;

import java.io.IOException;
import java.util.Map;

public class WebSocketCommunicatorClient extends CommunicatorClient implements TrucoClientHandler {


    static Logger logger = Logger.getLogger(WebSocketCommunicatorClient.class);

    TrucoClient trucoClient = null;

    private RoomClient roomClient;

    ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    TrucoClientDispatcher trucoClientDispatcher = null;
    TrucoListener trucoListener = new WebSocketTrucoListener(this);

    public WebSocketCommunicatorClient(RoomClient client, String serverString, Integer portNumber) throws IOException {
        super();
        eventDispatcher.setRoom(client);
        roomClient = client;
        EventDispatcherClient eventDispatcherClient = new EventDispatcherClient();
        eventDispatcherClient.setCommClient(this);

//        EventDispatcherClient eventDispatcherClient = new EventDispatcherClient();
        trucoClientDispatcher = new TrucoClientDispatcher(this, objectMapper, eventDispatcherClient, roomClient, trucoListener);
    }


    @Override
    public void chatMessageSent(RoomEvent event) {
        logger.warn("chatMessageSent [" + event + "]");
    }

    @Override
    public void invitationRequest(RoomEvent event) {
        logger.warn("invitationRequest [" + event + "]");
    }

    @Override
    public void invitationRejected(RoomEvent re) {
        logger.warn("invitationRejected [" + re + "]");
    }


    @Override
    public void tableDestroyed(TableEvent event) {
        logger.warn("tableDestroyed [" + event + "]");
    }

    @Override
    public void rankingChanged(RoomEvent ev) {
        logger.warn("Ranking Changed [" + ev + "]");
    }

    @Override
    public void connectionFailed(String mensaje) {
        logger.warn("Connection Failed [" + mensaje + "]");
    }

    @Override
    public void setLoggedIn(boolean loggedIn) {
        logger.debug("Set logged in [" + loggedIn + "]");
    }

    @Override
    public void loginRequested(RoomEvent ev) {
        logger.debug("Request Authentication [" + ev.getUser() + "]");
//        String host = "ec2-184-73-89-227.compute-1.amazonaws.com";
        String host = "localhost";

        trucoClient = new TrucoClientImpl("http://" + host + ":8091", "ws://" + host + ":8050");
        logger.debug("loginRequested [" + ev + "]");
        try {
            trucoClient.login(ev.getUser(), ev.getPassword(), this);
        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createTableRequested(RoomEvent ev) {
        logger.debug("Create Table Requested [" + ev + "]");
        TrucoRoomTable trucoRoomTable = new TrucoRoomTable();
        trucoRoomTable.setRoomId(TrucoFrame.MAIN_ROOM_ID);
        trucoRoomTable.setPoints(ev.getGamePoints());
        try {
            executeCommand(Commands.CREATE_ROOM_TABLE, trucoRoomTable);
        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void tableJoinRequested(RoomEvent ev) {
        logger.debug("Send Join Requested [" + ev + "] Command");
        JoinRoomTableRequest joinRoomTable = new JoinRoomTableRequest();
        joinRoomTable.setRoomId(TrucoFrame.MAIN_ROOM_ID);
        joinRoomTable.setTableId(Integer.toString(ev.getTableNumber()));
        try {
            executeCommand(Commands.JOIN_ROOM_TABLE, joinRoomTable);
        } catch (TrucoClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playerSitRequest(TableEvent ev) {
        /**
         * TableEvent event = new TableEvent();
         * 		event.setEvent(TableEvent.EVENT_playerSitRequest);
         * 		event.setValue(chair);
         * 		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
         * 		event.setPlayer(new TrucoPlayer[] { table.getPlayer() });
         */

        logger.debug("Set table position [" + ev + "]");
        try {
            TablePositionRequest tablePositionRequest = new TablePositionRequest();
            tablePositionRequest.setRoomId(TrucoFrame.MAIN_ROOM_ID);
            tablePositionRequest.setTableId(Integer.toString(ev.getTableBeanRepresentation().getId()));
            tablePositionRequest.setPosition(ev.getValue());
            executeCommand(Commands.SET_TABLE_POSITION, tablePositionRequest);

        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void gameStartRequest(TableEvent ev) {
        logger.debug("GameStartedRequest [" + ev + "]");
        logger.debug("Call GameStarted Command");
        logger.debug("Set table position [" + ev + "]");
        try {
            StartGameRequest requestCommand = new StartGameRequest();
            requestCommand.setRoomId(TrucoFrame.MAIN_ROOM_ID);
            requestCommand.setTableId(Integer.toString(ev.getTableBeanRepresentation().getId()));
            executeCommand(Commands.START_GAME, requestCommand);

        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void loginFailed() {
        logger.debug("Login Failed");
    }

    @Override
    public void afterLogin(TrucoPrincipal trucoPrincipal) throws TrucoClientException {
        logger.debug("After Login [" + trucoPrincipal + "]");
        trucoClient.connect();
        TrucoGameClient2.principal = trucoPrincipal;
        /// addTable
//        {
//            RoomEvent table = new RoomEvent();
//            table.setTableServer(new TableServer());
//            table.getTableServer().setHost(new TrucoPlayer());
//            table.getTableServer().getHost().setName("sricco");
//            table.setType(RoomEvent.TYPE_TABLE_CREATED);
//            table.setGamePoints(30);
//            table.setPlayers(new LinkedHashMap());
//            getEventDispatcher().dispatchEvent(table);
//        }
        // get room


        logger.debug("Client connected to Websocket");
        // Notify

    }

    @Override
    public void receiveMessage(Object object) {
        //objectMapper
        logger.debug("receiveMessage [" + object + "]");
        trucoClientDispatcher.dispatchEvent((Map) object);

    }

    @Override
    public void ready() {
        try {
            executeCommand(Commands.JOIN_ROOM, new RoomRequest(TrucoFrame.MAIN_ROOM_ID));
        } catch (TrucoClientException e) {
            logger.error("Error on Client Message sending", e);
        }
    }

    void executeCommand(String commandName, Object data) throws TrucoClientException {
        trucoClient.send(commandName, data);
    }

    TrucoClient getTrucoClient() {
        return trucoClient;
    }

    @Override
    public void play(TrucoEvent event) {
        if (event.getPlayer().getName().equals(getTrucoPlayer().getName())) {
            TrucoPlay trucoPlay = event.toTrucoPlay();
            TrucoGamePlay trucoGamePlay = TolucaHelper.getPlay(trucoPlay);
            try {
                trucoGamePlay.setRoomId(TrucoFrame.MAIN_ROOM_ID);
                trucoGamePlay.setTableId(Integer.toString(event.getTableNumber()));
                executeCommand(Commands.PLAY, trucoGamePlay);
            } catch (TrucoClientException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public TrucoPlayer getTrucoPlayer() {
        return new TrucoPlayer(trucoClient.getTrucoPrincipal().getUsername());
    }

    ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}