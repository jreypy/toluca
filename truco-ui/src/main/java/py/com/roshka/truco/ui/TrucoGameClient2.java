package py.com.roshka.truco.ui;


import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.ui.room.TableGame2;
import py.com.roshka.truco.ui.table.Table2;
import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.game.InvalidPlayExcepcion;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TrucoGameClient2 extends TrucoGame implements TableListener {
    static Logger logger = Logger.getLogger(WebSocketCommunicatorClient.class);

    final String id;
    final Table2 table;
    static TrucoPrincipal principal = null;


    public TrucoGameClient2(TrucoRoomEvent trucoRoomEvent, boolean host) {
        this.id = trucoRoomEvent.getTable().getId();
        this.table = new Table2(getTrucoPlayer(trucoRoomEvent.getUser()), host, trucoRoomEvent.getTable().getPoints());
        table.setId(this.id);
        table.setTableNumber(0);
        table.addTableListener(this);
        //PP total - Ale
//        table.setRoom((RoomClient) room);
        //room.addTable(table);
        table.initResources();
        table.addPlayer(getTrucoPlayer(trucoRoomEvent.getUser()));
        table.getJFrame().setVisible(true);
        table.getJTrucoTable().addSystemLog("Para sentarte, haz Click en uno de los cuadrados vacios");
        TableGame2.instance.insertarFila(table.getId());
        // add players
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
        return false;
    }

    @Override
    public void play(TrucoPlay tp) throws InvalidPlayExcepcion {
        logger.debug("===== play ====");
    }

    @Override
    public Vector getDetallesDeLaMano() {
        logger.debug("===== getDetallesDeLaMano ====");
        return null;
    }


    @Override
    public void gameStartRequest(TableEvent event) {
        logger.debug("===== gameStartRequest ====");
    }

    @Override
    public void gameStarted(TableEvent event) {
        logger.debug("===== gameStarted ====");
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
        logger.debug("===== playerSitRequest ====");
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


    static private Map<String, TrucoPlayer> players = new HashMap<>();

    static synchronized TrucoPlayer getTrucoPlayer(TrucoUser user) {
        TrucoPlayer trucoPlayer = players.get(user.getUsername());
        if (trucoPlayer == null) {
            trucoPlayer = new TrucoPlayer(user.getUsername(), 0);
            return trucoPlayer;
        }
        return trucoPlayer;

    }

    private static Map<String, TrucoGameClient2> games = new HashMap<>();

    public static TrucoGameClient2 getTrucoGameClient2(TrucoRoomEvent trucoRoomEvent, boolean host) {
        TrucoGameClient2 game = games.get(trucoRoomEvent.getTable().getId());
        if (game == null) {
            game = new TrucoGameClient2(trucoRoomEvent, host);
            games.put(trucoRoomEvent.getTable().getId(), game);

        }
        return game;
    }
}
