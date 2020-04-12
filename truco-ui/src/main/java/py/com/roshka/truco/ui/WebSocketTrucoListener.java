package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

public class WebSocketTrucoListener implements TrucoListener {
    static Logger logger = Logger.getLogger(TrucoFrame.class);

    WebSocketCommunicatorClient webSocketCommunicatorClient;

    public WebSocketTrucoListener(WebSocketCommunicatorClient webSocketCommunicatorClient) {
        this.webSocketCommunicatorClient = webSocketCommunicatorClient;
    }

    @Override
    public void play(TrucoEvent event) {
        logger.debug("Send Play to Server");
    }

    @Override
    public void playResponse(TrucoEvent event) {
        logger.debug("Play Response ");
    }

    @Override
    public void turn(TrucoEvent event) {
        logger.debug("turn ");
    }

    @Override
    public void endOfHand(TrucoEvent event) {
        logger.debug(" end of hand ");
    }

    @Override
    public void cardsDeal(TrucoEvent event) {
        logger.debug(" cards ");
    }

    @Override
    public void handStarted(TrucoEvent event) {
        logger.debug(" hand started ");
    }

    @Override
    public void gameStarted(TrucoEvent event) {
        logger.debug(" gamestarted ");
    }

    @Override
    public void endOfGame(TrucoEvent event) {
        logger.debug(" endofgame ");
    }

    @Override
    public TrucoPlayer getAssociatedPlayer() {
        logger.warn(" getAssociatedPlayer ");
        return null;
    }
}
