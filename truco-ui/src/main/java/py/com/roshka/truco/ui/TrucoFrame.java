package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.edu.uca.fcyt.net.CommunicatorClient;
import py.edu.uca.fcyt.net.CommunicatorProvider;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;

public class TrucoFrame extends RoomUING {

    static Logger logger = Logger.getLogger(TrucoFrame.class);

    public TrucoFrame() {
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
                logger.info("Login!! Here");
            }
        });

        init();
        setContentPane(getLoginPanel());
    }

    public String getParameter(String name) {
        return "";
    }
}
