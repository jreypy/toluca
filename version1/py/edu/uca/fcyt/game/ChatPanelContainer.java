package py.edu.uca.fcyt.game;

import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * <p>
 * 
 * @author Zuni
 * </p>
 */
public interface ChatPanelContainer {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * @param player ...
 * </p><p>
 * @param htmlMessage ...
 * </p><p>
 * 
 * </p>
 */
    public void sendChatMessage(TrucoPlayer player, String htmlMessage);
    public void sendChatMessage(RoomEvent event);
    public void showChatMessage(TrucoPlayer player, String htmlMessage);
    
    public String getOrigin();
} // end ChatPanelContainer







