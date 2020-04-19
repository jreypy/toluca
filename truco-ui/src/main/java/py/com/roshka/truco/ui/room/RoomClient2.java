package py.com.roshka.truco.ui.room;

import py.edu.uca.fcyt.game.ChatPanel;
import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.RoomListener;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;
import py.edu.uca.fcyt.toluca.guinicio.TableGame;
import py.edu.uca.fcyt.toluca.guinicio.TableRanking;
import py.edu.uca.fcyt.toluca.table.Table;
import py.edu.uca.fcyt.toluca.table.TableServer;

import java.util.HashMap;

public class RoomClient2 extends RoomClient {

    RoomClient target;

    public RoomClient2(RoomUING rui) {
        super(rui);
        target = new RoomClient();
        target.setRoomUING(rui);
        rui.getChatPanel().setCpc(target);
        target.setChatPanel(rui.getChatPanel());
        target.setMainTable(rui.getTableGame());
        target.setRankTable(rui.getTableRanking());

    }

    public RoomClient2() {
        super();
    }

    @Override
    public void setRoomPlayer(TrucoPlayer player) {
        super.setRoomPlayer(player);
    }

    @Override
    public TrucoPlayer getRoomPlayer() {
        return super.getRoomPlayer();
    }

    @Override
    public void cerrarConexion() {
        super.cerrarConexion();
    }

    @Override
    public void setMainTable(TableGame game) {
        super.setMainTable(game);
    }

    @Override
    public void setChatPanel(ChatPanel chatPanel) {
        super.setChatPanel(chatPanel);
    }

    @Override
    public void addTable(Table table) {
        super.addTable(table);
    }

    @Override
    public void removeTable(Table table) {
        super.removeTable(table);
    }

    @Override
    public void joinTableRequest(int tableNumber) {
        super.joinTableRequest(tableNumber);
    }

    @Override
    public void createTableRequest() {
        super.createTableRequest();
    }

    @Override
    public void createTableRequest(int points) {
        super.createTableRequest(points);
    }

    @Override
    public void addPlayer(TrucoPlayer player) {
        super.addPlayer(player);
    }

    @Override
    public void removePlayer(TrucoPlayer player) {
        super.removePlayer(player);
    }

    @Override
    public void modifyPlayer(TrucoPlayer player) {
        super.modifyPlayer(player);
    }

    @Override
    public void eliminatePlayer() {
        super.eliminatePlayer();
    }

    @Override
    public synchronized void fireEliminatePlayer(String playerName) {
        super.fireEliminatePlayer(playerName);
    }

    @Override
    public synchronized void fireLoginRequested(String username, String password) {
        super.fireLoginRequested(username, password);
    }

    @Override
    public void loginCompleted(TrucoPlayer player) {
        target.loginCompleted(player);
    }

    @Override
    public void actualizarRanking(TrucoPlayer trucoPlayer) {
        super.actualizarRanking(trucoPlayer);
    }

    @Override
    public void joinTable(RoomEvent re) {
        super.joinTable(re);
    }

    @Override
    public void loginFailed(RoomEvent event) {
        super.loginFailed(event);
    }

    @Override
    public TableRanking getRankTable() {
        return super.getRankTable();
    }

    @Override
    public void setRankTable(TableRanking ranking) {
        super.setRankTable(ranking);
    }

    @Override
    public void gameStartRequest(TableEvent event) {
        super.gameStartRequest(event);
    }

    @Override
    public void gameStarted(TableEvent event) {
        super.gameStarted(event);
    }

    @Override
    public void gameFinished(TableEvent event) {
        super.gameFinished(event);
    }

    @Override
    public void playerStandRequest(TableEvent event) {
        super.playerStandRequest(event);
    }

    @Override
    public void playerStanded(TableEvent event) {
        super.playerStanded(event);
    }

    @Override
    public void playerKickRequest(TableEvent event) {
        super.playerKickRequest(event);
    }

    @Override
    public void playerKicked(TableEvent event) {
        super.playerKicked(event);
    }

    @Override
    public void playerLeft(TableEvent event) {
        super.playerLeft(event);
    }

    @Override
    public void playerSitRequest(TableEvent event) {
        super.playerSitRequest(event);
    }

    @Override
    public void playerSit(TableEvent event) {
        super.playerSit(event);
    }

    @Override
    public void setearPlayerTable(TrucoPlayer player, Table tabela, int chair) {
        super.setearPlayerTable(player, tabela, chair);
    }

    @Override
    public void setStandPlayer(int chair, Table tabela) {
        super.setStandPlayer(chair, tabela);
    }

    @Override
    public void borrarPlayerTable(TrucoPlayer player, Table table) {
        super.borrarPlayerTable(player, table);
    }

    @Override
    public void signSendRequest(TableEvent event) {
        super.signSendRequest(event);
    }

    @Override
    public void signSent(TableEvent event) {
        super.signSent(event);
    }

    @Override
    public void showPlayed(TableEvent event) {
        super.showPlayed(event);
    }

    @Override
    public void playerJoined(TrucoPlayer player) {
        super.playerJoined(player);
    }

    @Override
    public void playerLeft(TrucoPlayer player) {
        super.playerLeft(player);
    }

    @Override
    public void chatMessageRequested(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {
        super.chatMessageRequested(cpc, player, htmlMessage);
    }

    @Override
    public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage) {
        super.chatMessageSent(cpc, player, htmlMessage);
    }

    @Override
    public void seAgregoTable(Table t) {
        super.seAgregoTable(t);
    }

    @Override
    public void chatMessageSent(RoomEvent event) {
        super.chatMessageSent(event);
    }

    @Override
    public void tableDestroyed(TableEvent event) {
        super.tableDestroyed(event);
    }

    @Override
    public void tableDestroyed(Table table) {
        super.tableDestroyed(table);
    }

    @Override
    public RoomUING getRoomUING() {
        return super.getRoomUING();
    }

    @Override
    public void setRoomUING(RoomUING uiNG) {
        super.setRoomUING(uiNG);
    }

    @Override
    public void testConexionReceive(long milisegundos) {
        super.testConexionReceive(milisegundos);
    }

    @Override
    public void invitationRequest(RoomEvent event) {
        super.invitationRequest(event);
    }

    @Override
    public void invitationRejected(RoomEvent re) {
        super.invitationRejected(re);
    }

    @Override
    public void showSystemMessage(String string) {
        target.showSystemMessage(string);
    }

    @Override
    public TableServer[] getTablesServers() {
        return super.getTablesServers();
    }

    @Override
    public synchronized void addRoomListener(RoomListener roomListener) {
        super.addRoomListener(roomListener);
    }

    @Override
    public void joinTable(int tableNumber) {
        super.joinTable(tableNumber);
    }

    @Override
    public HashMap getHashPlayers() {
        return super.getHashPlayers();
    }

    @Override
    public void addTable(TableServer table) {
        super.addTable(table);
    }

    @Override
    public TrucoPlayer getPlayer(String keyCode) {
        return super.getPlayer(keyCode);
    }

    @Override
    public String getOrigin() {
        return super.getOrigin();
    }

    @Override
    public void removeTable(TableServer table) {
        super.removeTable(table);
    }

    @Override
    public synchronized int getAvailableKey() {
        return super.getAvailableKey();
    }

    @Override
    public TableServer getTableServer(int id) {
        return super.getTableServer(id);
    }

    @Override
    public Table getTable(int id) {
        return super.getTable(id);
    }

    @Override
    public void sendChatMessage(TrucoPlayer player, String htmlMessage) {

    }

    @Override
    public void sendChatMessage(RoomEvent event) {

    }

    @Override
    public void showChatMessage(TrucoPlayer player, String htmlMessage) {

    }


}
