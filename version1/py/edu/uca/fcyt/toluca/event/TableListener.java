package py.edu.uca.fcyt.toluca.event;

 

/**
 * Listener de eveos de mesa
 * @author Grupo Interfaz de Juego
 */
public interface TableListener extends SpaceListener 
{
	public void gameStartRequest(TableEvent event);
	public void gameStarted(TableEvent event);
	public void gameFinished(TableEvent event);
	public void invitationRequest(RoomEvent event);
    public void invitationRejected(RoomEvent re);
//	public void tableLocked(TableEvent event);
//	public void tableUnlocked (TableEvent event);
	public void playerStandRequest(TableEvent event);
	public void playerStanded(TableEvent event);
	public void playerKickRequest(TableEvent event);
	public void playerKicked(TableEvent event);
	public void playerLeft(TableEvent event);
    public void playerSitRequest(TableEvent event);
    public void playerSit(TableEvent event);
    public void signSendRequest(TableEvent event);
    public void signSent(TableEvent event);
    public void showPlayed(TableEvent event);
    public void tableDestroyed(TableEvent event);

} 





