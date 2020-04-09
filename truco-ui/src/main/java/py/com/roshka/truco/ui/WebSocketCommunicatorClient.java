package py.com.roshka.truco.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;
import py.edu.uca.fcyt.net.CommunicatorClient;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebSocketCommunicatorClient extends CommunicatorClient implements TrucoClientHandler {

    static String MAIN_ROOM = "1";

    static Logger logger = Logger.getLogger(TrucoFrame.class);

    TrucoClient trucoClient = null;
    private RoomClient roomClient;

    ObjectMapper objectMapper = new ObjectMapper();


    public WebSocketCommunicatorClient(RoomClient client, String serverString, Integer portNumber) throws IOException {
        super(new EventDispatcherClient());
        eventDispatcher.setRoom(client);
        roomClient = client;
        ((EventDispatcherClient) getEventDispatcher()).setCommClient(this);
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
        trucoClient = new TrucoClientImpl("http://localhost:8091", "ws://localhost:8050");
        logger.debug("loginRequested [" + ev + "]");
        try {
            trucoClient.login(ev.getUser(), ev.getPassword(), this);
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
        // TODO Notify Login Success
        RoomEvent roomEvent = new RoomEvent();
        roomEvent.setPlayer(new TrucoPlayer());
        roomEvent.getPlayer().setName(trucoPrincipal.getUsername());
        roomEvent.getPlayer().setFullName(trucoPrincipal.getUsername());
        roomEvent.getPlayer().setId(trucoPrincipal.getUsername());
        roomEvent.setTablesServers(new TableServer[0]);
        getEventDispatcher().loginCompleted(roomEvent);
        logger.debug("Client connected to Websocket");
        // Notify

    }

    @Override
    public void receiveMessage(Object object) {
        //objectMapper
        logger.debug("receiveMessage [" + object + "]");
    }

    @Override
    public void ready() {
        Map map = new LinkedHashMap();
        map.put("id", MAIN_ROOM);
        try {
            trucoClient.send("join_room", map);
        } catch (TrucoClientException e) {
            logger.error("Error on Client Message sending", e);
        }
    }
}