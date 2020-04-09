package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;
import py.edu.uca.fcyt.net.CommunicatorClient;
import py.edu.uca.fcyt.net.CommunicatorProvider;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;

import java.util.LinkedHashMap;
import java.util.Map;

public class TrucoFrame extends RoomUING implements TrucoClientHandler {

    static String MAIN_ROOM = "1";

    static Logger logger = Logger.getLogger(TrucoFrame.class);

    TrucoClient trucoClient = null;

    public TrucoFrame() {
        trucoClient = new TrucoClientImpl("http://localhost:8091", "ws://localhost:8050");
        final TrucoClientHandler trucoClientHandler = this;
        CommunicatorProvider.setInstance(new CommunicatorClient() {
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
                logger.debug("loginRequested [" + ev + "]");
                try {
                    trucoClient.login(ev.getUser(), ev.getPassword(), trucoClientHandler);
                } catch (TrucoClientException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

        init();
        setContentPane(getLoginPanel());
    }

    public String getParameter(String name) {
        return "";
    }

    @Override
    public void loginFailed() {
        logger.debug("Login Failed");
    }

    @Override
    public void afterLogin(TrucoPrincipal trucoPrincipal) throws TrucoClientException {
        logger.debug("After Login [" + trucoPrincipal + "]");
        trucoClient.connect();
        logger.debug("Client connected to Websocket");
    }

    @Override
    public void receiveMessage(Object object) {
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
