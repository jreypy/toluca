package py.edu.uca.fcyt.toluca.table;

import java.awt.Cursor;
import java.util.Iterator;
import java.util.LinkedList;

import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TableListener;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * Dispara eventos de TableListeners
 */
class TableEventManager {
	protected Table table; // objeto Table

	protected LinkedList tableListeners; // listeners de eventos

	/** Construye un TableEventManager con Table 'table' */
	public TableEventManager(Table table) {
		this.table = table;
		tableListeners = new LinkedList();
	}

	/** Registra un listener de eventos TableListener */
	public void addListener(TableListener t) {
		Util.verifParam(t != null, "Par�metro 't' nulo");
		tableListeners.add(t);
	}

	/**
	 * Dispara el TableListener#playerJoined' de todos los TableListeners
	 * registrados
	 */
	public void firePlayerJoined(TrucoPlayer player) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).playerJoined(player);
		}
	}

	/**
	 * Dispara el chatMessageRequested' de todos los TrucoListeners registrados
	 */
	public void fireChatMessageRequested(TrucoPlayer player, String htmlMessage) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).chatMessageRequested(table, player,
					htmlMessage);
		}
	}

	/**
	 * Dispara el chatMessageSent de todos los TableListeners registrados
	 */
	public void fireChatMessageSent(TrucoPlayer player, String htmlMessage) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).chatMessageSent(table, player,
					htmlMessage);
		}
	}

	/**
	 * Dispara el 'gameStartRequest' de todos los TableListeners registrados
	 */
	public void fireGameStartRequest() {
		table.setCursor(Cursor.WAIT_CURSOR);

		Iterator iter = tableListeners.iterator();
		TableEvent event = new TableEvent();
		event.setPlayer(new TrucoPlayer[] { table.getPlayer() });
		event.setEvent(TableEvent.EVENT_gameStartRequest);
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());

		while (iter.hasNext()) {
			//System.out.println("iterando...");
			((TableListener) iter.next()).gameStartRequest(event);
		}
	}

	/**
	 * Dispara el 'gameStartRequest' de todos los TableListeners registrados
	 */
	public void fireGameStarted() {
		table.setCursor(Cursor.DEFAULT_CURSOR);

		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).gameStarted(new TableEvent(
					TableEvent.EVENT_gameStarted, table, null, null, -1));
		}
	}

	/**
	 * Daispara el 'gameFinished' de todos los TableListeners registrados
	 */
	public void fireGameFinished() {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).gameFinished(new TableEvent(
					TableEvent.EVENT_gameFinished, table, null, null, -1));
		}
	}

	/**
	 * Dispara el 'gameFinished' de todos los TableListeners registrados
	 */
	public void firePlayerStandRequest(int chair) {
		table.setCursor(Cursor.WAIT_CURSOR);
		TableEvent event = new TableEvent();
		event.setEvent(TableEvent.EVENT_playerStandRequest);
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
		event.setPlayer(new TrucoPlayer[] { table.getPlayer(), null });
		event.setValue(chair);
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).playerStandRequest(event);
		}
	}

	/**
	 * Dispara el 'gameFinished' de todos los TableListeners registrados
	 */
	public void firePlayerStanded(TrucoPlayer player) {
		table.setCursor(Cursor.DEFAULT_CURSOR);

		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).playerStanded(new TableEvent(
					TableEvent.EVENT_playerStanded, table, player, null, -1));
		}
	}

	/**
	 * Dispara el 'playerKicked' de todos los TableListeners registrados
	 */
	public void firePlayerKickRequest(TrucoPlayer player) {
		Iterator iter = tableListeners.iterator();
		TableEvent event = new TableEvent();
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
		event.setPlayer(new TrucoPlayer[] { player, null });
		event.setEvent(TableEvent.EVENT_playerKickRequest);

		while (iter.hasNext()) {
			((TableListener) iter.next()).playerKickRequest(event);
		}
	}

	/**
	 * Dispara el 'playerKicked' de todos los TableListeners registrados
	 */
	public void firePlayerKicked(TrucoPlayer player) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).playerKicked(new TableEvent(
					TableEvent.EVENT_playerKicked, table, player, null, -1));
		}
	}

	/**
	 * Dispara el 'playerLeft' de todos los TableListeners registrados
	 */
	public void firePlayerLeft() {
		Iterator iter = tableListeners.iterator();

		TableEvent event = new TableEvent();
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
		event.setPlayer(new TrucoPlayer[] { table.getPlayer(), null });
		event.setEvent(TableEvent.EVENT_playerLeft);

		while (iter.hasNext()) {
			((TableListener) iter.next()).playerLeft(event);
		}
	}

	/**
	 * Dispara el 'sitRequested' de todos los TableListeners registrados
	 */
	public void firePlayerSitRequest(int chair) {
		table.setCursor(Cursor.WAIT_CURSOR);

		Iterator iter = tableListeners.iterator();
		TableEvent event = new TableEvent();
		event.setEvent(TableEvent.EVENT_playerSitRequest);
		event.setValue(chair);
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
		event.setPlayer(new TrucoPlayer[] { table.getPlayer() });

		while (iter.hasNext()) {
			((TableListener) iter.next()).playerSitRequest(event);
		}
	}

	/**
	 * Dispara el 'playerSit' de todos los TableListeners registrados
	 */
	public void firePlayerSit() {
		table.setCursor(Cursor.DEFAULT_CURSOR);

		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).playerSit(new TableEvent(
					TableEvent.EVENT_playerSit, table, null, null, -1));
		}
	}

	/**
	 * Dispara el evento 'sendSign' de todos los TableListeners registrados
	 */
	public void fireSignSendRequest(TrucoPlayer dest, int sign) {
		table.setCursor(Cursor.WAIT_CURSOR);
		Iterator iter = tableListeners.iterator();
		TableEvent event = new TableEvent();
		event.setEvent(TableEvent.EVENT_signSendRequest);
		event.setTableBeanRepresentation(table.getTableBeanRepresentation());
		event.setPlayer(new TrucoPlayer[] { table.getPlayer(), dest });
		event.setValue(sign);

		/*
		 * este era el evento chilista new TableEvent (
		 * TableEvent.EVENT_signSendRequest, table, table.getPlayer(), dest,
		 * sign )
		 */
		while (iter.hasNext()) {
			((TableListener) iter.next()).signSendRequest(event);
		}
	}

	/**
	 * Dispara el evento 'sendSign' de todos los TableListeners registrados
	 */
	public void fireSignSent(TrucoPlayer dest) {
		table.setCursor(Cursor.DEFAULT_CURSOR);
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).signSent(new TableEvent(
					TableEvent.EVENT_signSent, table, table.getPlayer(), dest,
					-1));
		}
	}

	/**
	 * Dispara el evento 'showCards' de todos los TableListeners registrados
	 */
	public void fireShowPlayed(int chair) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).showPlayed(new TableEvent(
					TableEvent.EVENT_showPlayed, table, null, null, chair));
		}
	}

	/**
	 * @param re
	 */
	public void fireInvitationRequest(RoomEvent re) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).invitationRequest(re);
		}
	}

	/**
	 * @param re
	 */
	public void fireInvitationRejected(RoomEvent re) {
		Iterator iter = tableListeners.iterator();
		while (iter.hasNext()) {
			((TableListener) iter.next()).invitationRejected(re);
		}
	}
}