/*
 * Created on Sep 13, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package py.edu.uca.fcyt.toluca.net;

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
	public void dispatchEvent(RoomEvent event)
	{
	System.out.println("DispatchEvent: Se resive un evento RoomEvent");
		switch(event.getType())
		{
			case RoomEvent.TYPE_CHAT_REQUESTED:break;
			case RoomEvent.TYPE_CHAT_SENT:break;
			case RoomEvent.TYPE_CREATE_TABLE_REQUESTED:break;
			case RoomEvent.TYPE_LOGIN_COMPLETED:loginCompleted(event);break;
			case RoomEvent.TYPE_LOGIN_FAILED:break;
			case RoomEvent.TYPE_LOGIN_REQUESTED: loginRequested(event);break;
			case RoomEvent.TYPE_PLAYER_JOINED:playerJoined(event);break;
			case RoomEvent.TYPE_PLAYER_KICKED:break;
			case RoomEvent.TYPE_PLAYER_LEFT:break;
			case RoomEvent.TYPE_TABLE_CREATED:break;
			case RoomEvent.TYPE_TABLE_CREATED_SERVER:break;
			case RoomEvent.TYPE_TABLE_JOIN_REQUESTED:break;
			case RoomEvent.TYPE_TABLE_JOINED:break;
			
		}
	}
	public void dispatchEvent(TableEvent event)
	{
		
	}
	public void dispatchEvent(TrucoEvent event)
	{
		
	}
	public void dispatchEvent(TrucoPlay event)
	{
		
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
	
	public abstract void loginRequested(RoomEvent event);
	public abstract void loginCompleted(RoomEvent event);
	public abstract void playerJoined(RoomEvent event);
}