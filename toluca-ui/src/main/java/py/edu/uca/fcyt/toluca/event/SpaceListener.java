package py.edu.uca.fcyt.toluca.event;

import py.edu.uca.fcyt.game.ChatPanelContainer;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

 

/**
* <p>
*
* @author Interfaz de Inicio
* </p>
*/
public interface SpaceListener 
{
    public void playerJoined(TrucoPlayer player);
    public void playerLeft(TrucoPlayer player);
    public void chatMessageRequested(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage);
    public void chatMessageSent(ChatPanelContainer cpc, TrucoPlayer player, String htmlMessage);
    public void chatMessageSent(RoomEvent event);
} 







