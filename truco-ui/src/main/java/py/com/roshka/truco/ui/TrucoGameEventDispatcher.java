package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.helper.TolucaHelper;
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
        TrucoEvent trucoEvent = TolucaHelper.trucoEvent(trucoGameEvent);

        if (Event.GAME_STARTED.equals(trucoGameEvent.getEventName())) {
            handleGameStarted(trucoGameEvent);
        } else if (Event.GIVING_CARDS.equals(trucoGameEvent.getEventName())) {
            handleGivingCards(trucoGameEvent);
        } else{
            target.dispatchEvent(trucoEvent);
        }
    }
    private void handleGameStarted(TrucoGameEvent trucoGameEvent) {
        TableEvent tableEvent = new TableEvent();
        /*logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,
                "Llego un gameStarted de TableEvent ");*/
        TableServer tableServer = new TableServer();
        // TODO CHECK

        Table table = target.getRoom().getTable(Integer.parseInt(trucoGameEvent.getTableId()));
        TrucoTeam[] trucoTeams = table.createTeams();

//        TrucoTeam[] trucoTeam = table.createTeams();
        TrucoGameClient trucoGameClient = new TrucoGameClient(trucoTeams[0],
                trucoTeams[1],
                trucoGameEvent.getGame().getPoints());

        trucoGameClient.setTableNumber(tableServer.getTableNumber());
        trucoGameClient.addTrucoListener(communicatorClient);

        //es muy importante el orden de los faroles altera el valor del
        // alumbrado
        //esto me hizo perder 2 horas
        table.startGame(trucoGameClient);//atender esta linea primero que la
        trucoGameClient.startGameClient();


    }

    private void handleHandStarted(TrucoGameEvent trucoGameEvent) {
//        TrucoRoomTable trucoRoomTable = objectMapper.convertValue(eventData, TrucoRoomTable.class);
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
