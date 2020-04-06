/*
 * Created on Sep 13, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package py.edu.uca.fcyt.toluca.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import py.edu.uca.fcyt.toluca.Room;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;

/**
 * @author dcricco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class EventDispatcher {

	protected Room room;
	private Logger log=Logger.getLogger(EventDispatcher.class.getName());
	
	public void dispatchEvent(RoomEvent event)
	{
		
	//log.log(Level.WARNING,"DispatchEvent: Se recive un evento RoomEvent " + event.getClass() + " tipo " + event.getType());
		switch(event.getType())
		{
			case RoomEvent.TYPE_CHAT_REQUESTED:chatRequested(event);break;
			case RoomEvent.TYPE_CHAT_SENT:chatSend(event);break;
			case RoomEvent.TYPE_CREATE_TABLE_REQUESTED:createTableRequest(event);break;
			case RoomEvent.TYPE_LOGIN_COMPLETED:loginCompleted(event);break;
			case RoomEvent.TYPE_LOGIN_FAILED:loginFailed(event);break;
			case RoomEvent.TYPE_LOGIN_REQUESTED: loginRequested(event);break;
			case RoomEvent.TYPE_PLAYER_JOINED:playerJoined(event);break;
			case RoomEvent.TYPE_PLAYER_KICKED:break;
			case RoomEvent.TYPE_PLAYER_LEFT: playerLeft(event);break;
			case RoomEvent.TYPE_TABLE_CREATED: tableCreated(event);break;
			case RoomEvent.TYPE_TABLE_CREATED_SERVER:break;
			case RoomEvent.TYPE_TABLE_JOIN_REQUESTED:tableJoinRequested(event);break;
			case RoomEvent.TYPE_TABLE_JOINED:tableJoined(event);break;
			case RoomEvent.TYPE_RANKING_CHANGED:rankingChanged(event);break;
			case RoomEvent.TYPE_TEST_CONEXION:testConexion(event);break;
			case RoomEvent.TYPE_INVITACION:invitacion(event);break;
			case RoomEvent.TYPE_INVITACION_REJECTED:invitacionRejected(event);break;
		}
	}
    public void dispatchEvent(TableEvent event)
	{
		//log.log(Level.WARNING,"se recibe un tableevent");
		switch(event.getEvent())
		{
			case TableEvent.EVENT_gameFinished:break;
			case TableEvent.EVENT_gameStarted:gameStarted(event);break;
			case TableEvent.EVENT_gameStartRequest:gameStartRequest(event);break;
			case TableEvent.EVENT_playerKicked:playerKicked(event);break;
			case TableEvent.EVENT_playerKickRequest:playerKickRequest(event);break;
			case TableEvent.EVENT_playerLeft:playerLeft(event);break;
			case TableEvent.EVENT_playerSit:playerSit(event);break;
			case TableEvent.EVENT_playerSitRequest: playerSitRequest(event);break;
			case TableEvent.EVENT_playerStanded:playerStand(event);break;
			case TableEvent.EVENT_playerStandRequest:playerStandRequest(event);break;
			case TableEvent.EVENT_showPlayed:break;
			case TableEvent.EVENT_signSendRequest:signSendRequest(event);break;
			case TableEvent.EVENT_signSent:signSend(event);break;
			case TableEvent.EVENT_tableLocked:break;
			case TableEvent.EVENT_tableUnlocked:break;
			case TableEvent.EVENT_TABLE_DESTROYED:tableDestroyed(event);log.log(Level.WARNING,"table destroyed"); break;
			
		}
	}
	public void dispatchEvent(TrucoEvent event)
	{
		//log.log(Level.WARNING," se recibe un trucoEvent de tipo "+event.getType());
		switch(event.getType())
		{
			case TrucoEvent.CANTO_ENVIDO:cantarTanto(event);break;
			
			case TrucoEvent.CANTO_FLOR:canto(event);break;
			case TrucoEvent.CARTAS_REPARTIDAS:break;
			case TrucoEvent.CERRARSE:canto(event);break;
			case TrucoEvent.CON_FLOR_ME_ACHICO:canto(event);break;
			case TrucoEvent.CONTRA_FLOR:canto(event);break;
			case TrucoEvent.CONTRA_FLOR_AL_RESTO:canto(event);break;
			case TrucoEvent.ENVIAR_CARTAS:receiveCards(event);break;
			case TrucoEvent.ENVIDO:canto(event);break;
			case TrucoEvent.FALTA_ENVIDO:canto(event);break;
			case TrucoEvent.FIN_DE_JUEGO:infoGame(event);break;
			case TrucoEvent.FIN_DE_MANO:infoGame(event);break;
			
			case TrucoEvent.FIN_DE_RONDA:break;
			
			case TrucoEvent.FLOR:canto(event);break;
			case TrucoEvent.INICIO_DE_JUEGO:infoGame(event);break;
			case TrucoEvent.INICIO_DE_MANO:infoGame(event);break;
			case TrucoEvent.JUGAR_CARTA:tirarCarta(event);break;
			case TrucoEvent.ME_VOY_AL_MAZO:canto(event);break;
			case TrucoEvent.NO_QUIERO:canto(event);break;
			case TrucoEvent.PASO_ENVIDO:canto(event);break;
			case TrucoEvent.PASO_FLOR:canto(event);break;
			
			case TrucoEvent.PLAYER_CONFIRMADO:break;
			
			case TrucoEvent.QUIERO:canto(event);break;
			case TrucoEvent.REAL_ENVIDO:canto(event);break;
			case TrucoEvent.RETRUCO:canto(event);break;
		
			case TrucoEvent.SIN_MENSAJES:break;
			
			case TrucoEvent.TRUCO:canto(event);break;
		
			//los turnos no se envian segun lo que dice jrey
			case TrucoEvent.TURNO_CANTAR_ENVIDO:break;
			case TrucoEvent.TURNO_CANTAR_FLOR:break;
			case TrucoEvent.TURNO_JUGAR_CARTA:break;
			case TrucoEvent.TURNO_RESPONDER_CONTRAFLOR:break;
			case TrucoEvent.TURNO_RESPONDER_CONTRAFLORALRESTO:break;
			case TrucoEvent.TURNO_RESPONDER_ENVIDO:break;
			case TrucoEvent.TURNO_RESPONDER_FALTAENVIDO:break;
			case TrucoEvent.TURNO_RESPONDER_REALENVIDO:break;
			case TrucoEvent.TURNO_RESPONDER_RETRUCO:break;
			case TrucoEvent.TURNO_RESPONDER_TRUCO:break;
			case TrucoEvent.TURNO_RESPONDER_VALECUATRO:break;
			
			case TrucoEvent.VALE_CUATRO:canto(event);break;
			
			
		}
	}
	public void dispatchEvent(TrucoPlay event)
	{
		//log.log(Level.WARNING,"Se recibe un trucoPlay de tipo "+event.getType());
		switch(event.getType())
		{
			case TrucoPlay.CANTO_ENVIDO:play(event);break;
			case TrucoPlay.CANTO_FLOR:play(event);break;
			case TrucoPlay.CARTAS_REPARTIDAS:play(event);break;
			case TrucoPlay.CERRARSE:play(event);break;
			case TrucoPlay.CON_FLOR_ME_ACHICO:play(event);break;
			case TrucoPlay.CONTRA_FLOR:play(event);break;
			case TrucoPlay.CONTRA_FLOR_ALRESTO:play(event);break;
			case TrucoPlay.ENVIDO:play(event);break;
			case TrucoPlay.FALTA_ENVIDO:play(event);break;
			case TrucoPlay.FIN_DE_JUEGO:play(event);break;
			case TrucoPlay.FIN_DE_MANO:play(event);break;
			case TrucoPlay.FLOR:play(event);break;
			case TrucoPlay.INICIO_DE_JUEGO:play(event);break;
			case TrucoPlay.INICIO_DE_MANO:play(event);break;
			case TrucoPlay.JUGAR_CARTA:play(event);break;
			case TrucoPlay.ME_VOY_AL_MAZO:play(event);break;
			case TrucoPlay.NO_QUIERO:play(event);break;
			case TrucoPlay.PASO_ENVIDO:play(event);break;
			case TrucoPlay.PASO_FLOR:play(event);break;
			case TrucoPlay.PLAYER_CONFIRMADO:playerConfirmado(event);break;
			case TrucoPlay.QUIERO:play(event);break;
			case TrucoPlay.REAL_ENVIDO:play(event);break;
			case TrucoPlay.RETRUCO:play(event);break;
			case TrucoPlay.SIN_MENSAJES:play(event);break;
			case TrucoPlay.TRUCO:play(event);break;
			case TrucoPlay.VALE_CUATRO:play(event);break;
		}
	}
	
	/**
	 * @return Returns the room.
	 */
	public Room getRoom() {
		return room;
	}
	/**
	 * @param room The room to set.
	 */
	public void setRoom(Room room) {
		this.room = room;
	}
	
	
	//ACA VIENENLOS METODOS QUE SE TIENEN QUE IMPLEMENTAR EN LOS HIJOS PARA DESPACHAR LOS EVENTOS
	
	
	//METODOS CORRESPONDIENTES AL ROOM_EVENT
	public abstract void loginRequested(RoomEvent event);
	public abstract void loginCompleted(RoomEvent event);
	public abstract void playerJoined(RoomEvent event);
	public abstract void loginFailed(RoomEvent event);
	
	public abstract void playerLeft(RoomEvent event);
	
	public abstract void chatRequested(RoomEvent event);
	public abstract void chatSend(RoomEvent event);
	
	public abstract void createTableRequest(RoomEvent event);
	public abstract void tableCreated(RoomEvent event);
	
	public abstract void tableJoinRequested(RoomEvent event);
	public abstract void tableJoined(RoomEvent event);
	public abstract void rankingChanged(RoomEvent event);
	
	public abstract void testConexion(RoomEvent event);
	public abstract void invitacion(RoomEvent event);
    public abstract void invitacionRejected(RoomEvent event);
    
	//METODOS CORRESPONDIENTES A LA TABLA
	public abstract void playerSitRequest(TableEvent event);
	public abstract void playerSit(TableEvent event);
	
	public abstract void playerStandRequest(TableEvent event);
	public abstract void playerStand(TableEvent event);
	
	public abstract void playerKickRequest(TableEvent event);
	public abstract void playerKicked(TableEvent event);
	
	public abstract void playerLeft(TableEvent event);
	
	public abstract void gameStartRequest(TableEvent event);
	public abstract void gameStarted(TableEvent event);
	
	public abstract void signSendRequest(TableEvent event);
	public abstract void signSend(TableEvent event);
	
	public abstract void tableDestroyed(TableEvent event);
	//METODOS DEL TRUCO GAME
	public abstract void receiveCards(TrucoEvent event);
	
	public abstract void infoGame(TrucoEvent event);
	public abstract void tirarCarta(TrucoEvent event);
	public abstract void play(TrucoPlay event);
	public abstract void playerConfirmado(TrucoPlay event);
	
	public abstract void canto(TrucoEvent event);
	public abstract void cantarTanto(TrucoEvent event);
	
	
}
