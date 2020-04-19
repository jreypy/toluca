package py.com.roshka.truco.ui.room.game;


import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Player;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoGameMessage;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.ui.Toluca;
import py.com.roshka.truco.ui.WebSocketCommunicatorClient;
import py.com.roshka.truco.ui.event.TrucoEvent2;
import py.com.roshka.truco.ui.room.RoomHandler;
import py.com.roshka.truco.ui.table.Table2;
import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.net.CommunicatorProvider;
import py.edu.uca.fcyt.toluca.event.*;
import py.edu.uca.fcyt.toluca.game.*;

import java.util.Vector;

public class TrucoGameClient2 extends TrucoGameClient implements TableListener, TrucoListener {
    static Logger logger = Logger.getLogger(TrucoGameClient2.class);

    final String roomId;
    final String id;
    final boolean host;
    Table2 table;
    final TrucoRoomTableDescriptor tableDescriptor;
    private boolean turno = false;
    final TrucoPlayer trucoPlayer;

    public TrucoGameClient2(TrucoRoomTableDescriptor tableDescriptor) {
        super(null, null);
        trucoPlayer = Toluca.getTrucoPlayer(new Player(RoomHandler.principal.getId(), RoomHandler.principal.getUsername()));
        this.tableDescriptor = tableDescriptor;
        id = tableDescriptor.getId();
        roomId = tableDescriptor.getRoomId();
        host = tableDescriptor.getOwner().getId().equalsIgnoreCase(trucoPlayer.getId());
        // add players
        table = new Table2(trucoPlayer, host, tableDescriptor.getPoints());
        tableNumber = Integer.parseInt(id);
    }

    public void show() {
        logger.debug("************************** SHOW TABLE ***********************");
        table.initResources();
        table.setId(id);
        table.getJFrame().setVisible(true);
        table.getJTrucoTable().addSystemLog("Para sentarte, haz Click en uno de los cuadrados vacios");
        table.setTableNumber(Integer.parseInt(this.id));
        table.addTableListener(this);
        table.addPlayer(Toluca.getTrucoPlayer(tableDescriptor.getOwner()));
        table.getJFrame().setVisible(true);
        table.getJTrucoTable().addSystemLog("Para sentarte, haz Click en uno de los cuadrados vacios");
    }

    public Table2 getTable() {
        return table;
    }

    @Override
    public void startGame() {
        logger.debug("===== Start GAME ====");
    }

    @Override
    public void startHand(TrucoPlayer tPlayer) {
        logger.debug("===== Start HAND ====");
    }

    @Override
    public boolean esPosibleJugar(TrucoPlay tp) {
        logger.debug("===== esPosibleJugar ====");
        // TODO Validate
        return turno && true;
    }

    @Override
    synchronized public void play(TrucoPlay tp) throws InvalidPlayExcepcion {
        logger.debug("===== play ====");
        if (turno) {
            turno = false;
            communicatorClient().play(roomId, id, tp);
        } else {
            throw new InvalidPlayExcepcion("No es turno del Jugador [" + trucoPlayer.getName() + "]");
        }
    }

    @Override
    public Vector getDetallesDeLaMano() {
        logger.debug("===== getDetallesDeLaMano ====");
        logger.warn("===== TODO Detalles de la mano ====");
        // TODO Detalles de la mano

        Vector vector = new Vector();
        for (Object o : detalleDelPuntaje) {
            vector.add(new PointsDetail2(o.toString()));
        }
        return vector;
    }

    public class PointsDetail2 extends PointsDetail {

        String message;

        public PointsDetail2(TrucoTeam tm, int gp, int pg) {
            super(tm, gp, pg);
        }

        public PointsDetail2(String message) {
            super(null, 0, 0);
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    @Override
    public void gameStartRequest(TableEvent ev) {
        logger.debug("===== gameStartRequest ====");
        logger.debug("Set table position [" + ev + "]");
        logger.debug("GameStartedRequest [" + ev + "]");
        logger.debug("Call GameStarted Command");
        logger.debug("Set table position [" + ev + "]");
        try {
            StartGameRequest requestCommand = new StartGameRequest();
            requestCommand.setRoomId(roomId);
            requestCommand.setTableId(id);
            communicatorClient().executeCommand(Commands.START_GAME, requestCommand);
        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public void gameStarted(TableEvent event) {
    }

    @Override
    public void startGameClient() {
        teams = table.createTeams();
        super.startGameClient();
    }

    @Override
    public void gameFinished(TableEvent event) {
        logger.debug("===== gameFinished ====");
    }

    @Override
    public void invitationRequest(RoomEvent event) {
        logger.debug("===== invitationRequest ====");
    }

    @Override
    public void invitationRejected(RoomEvent re) {
        logger.debug("===== invitationRejected ====");
    }

    @Override
    public void playerStandRequest(TableEvent event) {
        logger.debug("===== playerStandRequest ====");
    }

    @Override
    public void playerStanded(TableEvent event) {
        logger.debug("===== playerStanded ====");
    }

    @Override
    public void playerKickRequest(TableEvent event) {
        logger.debug("===== playerKickRequest ====");
    }

    @Override
    public void playerKicked(TableEvent event) {
        logger.debug("===== playerKicked ====");
    }

    @Override
    public void playerLeft(TableEvent event) {
        logger.debug("===== playerLeft ====");
    }

    @Override
    public void playerSitRequest(TableEvent event) {
        logger.debug("Set table position [" + event + "]");
        try {
            TablePositionRequest tablePositionRequest = new TablePositionRequest();
            tablePositionRequest.setRoomId(roomId);
            tablePositionRequest.setTableId(id);
            tablePositionRequest.setPosition(event.getValue());
            communicatorClient().executeCommand(Commands.SET_TABLE_POSITION, tablePositionRequest);
        } catch (TrucoClientException e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public void playerSit(TableEvent event) {
        logger.debug("===== playerSit ====");
    }

    @Override
    public void signSendRequest(TableEvent event) {
        logger.debug("===== signSendRequest ====");
    }

    @Override
    public void signSent(TableEvent event) {
        logger.debug("===== signSent ====");
    }

    @Override
    public void showPlayed(TableEvent event) {
        logger.debug("===== showPlayed ====");
    }

    @Override
    public void tableDestroyed(TableEvent event) {
        logger.debug("===== tableDestroyed ====");
    }

    @Override
    public void playerJoined(TrucoPlayer player) {
        logger.debug("===== playerJoined ====");
    }

    @Override
    public void playerLeft(TrucoPlayer player) {
        logger.debug("===== playerLeft ====");
    }

    @Override
    public void chatMessageRequested(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {
        logger.debug("===== chatMessageRequested ====");
    }

    @Override
    public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {
        logger.debug("===== chatMessageSent ====");
    }

    @Override
    public void chatMessageSent(RoomEvent event) {
        logger.debug("===== chatMessageSent ====");
    }


    private WebSocketCommunicatorClient communicatorClient() {
        return (WebSocketCommunicatorClient) CommunicatorProvider.getInstance();
    }


    @Override
    public void playResponse(TrucoEvent event) {
        logger.debug("===== Play Response [" + event + "] =====");
        firePlayResponseEvent(event);
    }


    @Override
    public void fireHandStarted() {
        logger.info(" fireHandStarted " + getNumberOfHand());
        logger.info(" el equipo[0] es: " + teams[0]);
        logger.info(" el equipo[1] es: " + teams[1]);
        logger.info(teams[1] == null);
        logger.info(" numero de players de los equipos = " + teams[0].getNumberOfPlayers() + "y" + teams[0].getNumberOfPlayers());

        TrucoPlayer tp = teams[(numberOfHand + 1) % 2].getTrucoPlayerNumber((numberOfHand - 1) % numberOfPlayers / 2);
        TrucoEvent event = new TrucoEvent(this, numberOfHand, tp, TrucoEvent.INICIO_DE_MANO);
        event.setTableNumber(getTableNumber());

        getListenerlist().stream().forEach(s -> {
            logger.debug("Fire HandStarted!!!");
            ((TrucoListener) s).handStarted(event);
        });
    }

    @Override
    public void firePlayResponseEvent(TrucoEvent event) {
        super.firePlayResponseEvent(event);
    }

    @Override
    public void fireTurnEvent(TrucoPlayer pl, byte type) {
        turno = trucoPlayer.getId().equalsIgnoreCase(pl.getId());
        logger.debug("Turno de [" + pl + "] turno [" + trucoPlayer + "][" + turno + "]");
        super.fireTurnEvent(pl, type);
    }

    @Override
    public void fireTurnEvent(TrucoPlayer pl, byte type, int value) {
        turno = trucoPlayer.getId().equalsIgnoreCase(pl.getId());
        logger.debug("Turno de [" + pl + "] turno [" + trucoPlayer + "][" + turno + "]");
        super.fireTurnEvent(pl, type, value);
    }


    public void andEnded(TrucoGameEvent trucoGameEvent) {
        detalleDelPuntaje = new Vector();
        for (TrucoGameMessage message : trucoGameEvent.getMessages()) {
            detalleDelPuntaje.add(message.getText());
        }
        fireEndOfHandEvent(new TrucoEvent2(trucoGameEvent));
    }


}
