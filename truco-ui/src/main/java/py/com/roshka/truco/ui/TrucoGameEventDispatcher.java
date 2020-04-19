package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.ui.event.TableEvent2;
import py.com.roshka.truco.ui.event.TrucoEvent2;
import py.com.roshka.truco.ui.net.EventDispatcherClient2;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoGameClient;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.util.LinkedHashMap;

public class TrucoGameEventDispatcher {
    Logger logger = Logger.getLogger(TrucoRoomEventDispatcher.class);

    final private WebSocketCommunicatorClient communicatorClient;
    final private EventDispatcherClient target;

    public TrucoGameEventDispatcher(WebSocketCommunicatorClient communicatorClient, EventDispatcherClient target) {
        this.communicatorClient = communicatorClient;
        this.target = target;
    }


    public void dispatchGameEvent(TrucoGameEvent trucoGameEvent) {

        if (Event.GAME_STARTED.equals(trucoGameEvent.getEventName())) {
            TableEvent2 tableEvent2 = new TableEvent2(trucoGameEvent);
            target.gameStarted(tableEvent2);
        } else {
            TrucoEvent2 trucoEvent = new TrucoEvent2(trucoGameEvent);
            if (Event.HAND_STARTED.equals(trucoGameEvent.getEventName())) {
                target.infoGame(trucoEvent);
            } else if (Event.GIVING_CARDS.equals(trucoGameEvent.getEventName())) {
                target.receiveCards(trucoEvent);
            } else if (Event.PLAY_REQUEST.equalsIgnoreCase(trucoGameEvent.getEventName())) {
                ((EventDispatcherClient2) target).receivePlayRequest(trucoGameEvent);
            }
            else if (Event.PLAY_CARD.equalsIgnoreCase(trucoGameEvent.getEventName())){
                ((EventDispatcherClient2) target).playResponse(trucoGameEvent);
            }
            else if (Event.HAND_ENDED.equalsIgnoreCase(trucoGameEvent.getEventName())){
                ((EventDispatcherClient2)target).handEnded(trucoGameEvent);
            }
            else {
                target.dispatchEvent(trucoEvent);
            }
        }

    }

    private void handleHandStarted(TrucoGameEvent trucoGameEvent) {
//        TrucoRoomTable trucoRoomTable = objectMapper.convertValue(eventData, TrucoRoomTable.class);
    }


}
