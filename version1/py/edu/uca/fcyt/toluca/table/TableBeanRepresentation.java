
package py.edu.uca.fcyt.toluca.table;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;


public class TableBeanRepresentation {

	private TrucoPlayer player;
	private int gamePoints;
	private TrucoPlayer hostPlayer;
	
	public TableBeanRepresentation()
	{
		
	}
	public TableBeanRepresentation(int id,TrucoPlayer player)
	{
		this.id=id;
		this.player=player;
	}
	private int id;
	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public TrucoPlayer getPlayer() {
		return player;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param player
	 */
	public void setPlayer(TrucoPlayer player) {
		this.player = player;
	}

    public TrucoPlayer getHostPlayer() {
        return hostPlayer;
    }
    public void setHostPlayer(TrucoPlayer hostPlayer) {
        this.hostPlayer = hostPlayer;
    }
	public int getGamePoints() {
		return gamePoints;
	}
	public void setGamePoints(int gamePoints) {
		this.gamePoints = gamePoints;
	}
}
