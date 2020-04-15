package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.TableServer;

public class TrucoGameEventDispatcher {
    Logger logger = Logger.getLogger(TrucoRoomEventDispatcher.class);

    final private WebSocketCommunicatorClient communicatorClient;
    final private EventDispatcherClient target;

    public TrucoGameEventDispatcher(WebSocketCommunicatorClient communicatorClient, EventDispatcherClient target) {
        this.communicatorClient = communicatorClient;
        this.target = target;
    }

    private void dispatchTrucoGameEvent(TrucoGameEvent trucoGameEvent) {
        TrucoEvent trucoEvent = TolucaHelper.trucoEvent(trucoGameEvent);

        if (Event.GAME_STARTED.equalsIgnoreCase(trucoGameEvent.getEventName())) {
            TableEvent tableEvent = new TableEvent();
            tableEvent.setEvent(TableEvent.EVENT_gameStarted);
            tableEvent.setTableServer(new TableServer());
            tableEvent.getTableServer().setTableNumber(trucoEvent.getTableNumber());
            target.dispatchEvent(tableEvent);
        } else {
            logger.debug("Fire Truco Game Event [ " + trucoGameEvent + "]");

            target.dispatchEvent(trucoEvent);
        }

    }

    private void handleGivingCards(TrucoGameEvent trucoGameEvent) {
        TrucoEvent trucoEvent = new TrucoEvent();
        trucoEvent.setType(TrucoEvent.ENVIAR_CARTAS);
        trucoEvent.setPlayer(TolucaHelper.getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setTableNumber(Integer.parseInt(trucoGameEvent.getGame().getId()));
        trucoEvent.setPlayer(TolucaHelper.getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setCards(TolucaHelper.getCards(trucoGameEvent.getCards()));
        target.receiveCards(trucoEvent);
    }



}
