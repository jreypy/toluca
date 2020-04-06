package py.edu.uca.fcyt.toluca.table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * Maneja la posicion de los jugadores en la mesa
 */
class PlayerManager {

	protected TrucoPlayer playerLocal;

	private HashMap asientos = new HashMap();
	
	protected Vector sittedPlayers;/*
							  * Vector de referencia de jugadores cuyo subindice
							  * corresponde al numero de su silla
							  */

	private boolean started = false;

	/** Crea un PlayerManager */
	public PlayerManager(int playerCount) {
		sittedPlayers = new Vector(playerCount);//vector de referencias
		//de los players
		sittedPlayers.setSize(playerCount);
	}

	public PlayerManager() {

	}

	/**
	 * Sienta al jugador en un numero de silla correspondiente
	 */
	public synchronized void sitPlayer(TrucoPlayer p, int chair) {
		if (sittedPlayers.contains(p))
			throw new TableException("Jugador ya está sentado: " + p.getName());

		if (getPlayer(chair) != null)
			throw new TableException("Silla ocupada");

		sittedPlayers.set(chair, p);
		System.out.println("Sentado " + p + ", en silla " + chair);
//		asientos.put(new Integer(chair), p);
	}

	/**
	 * Para al jugador en una silla
	 * 
	 * @param chair
	 *            Silla
	 */
	public TrucoPlayer standPlayer(int chair) {
		TrucoPlayer player;

		player = getPlayer(chair);
//		player = (TrucoPlayer) asientos.get(new Integer(chair));
		if (player == null)
			throw new TableException("Silla vacía");

		if (playerLocal == player)
			playerLocal = null;

		//Este da un AIOBE por la morgueada que se hace al iniciar el juego :( - Ale 200502010
		// de colocar los jugadores al comienzo del vector ?!?!?!?!
		sittedPlayers.set(chair, null);
//		asientos.remove(new Integer(chair));
		return player;
	}

	public void setPlayerLocal(TrucoPlayer p) {
		Util.verifParam(p != null, "Parámetro 'p' nulo");
		Util.verif(sittedPlayers.contains(p), "Jugador " + p.getName()
				+ " no agregado");

		playerLocal = p;
	}

	public int getLocalChair() {
/*		if (playerLocal == null)
			return 0;
		else*/
			return getChair(playerLocal);
	}

	/**
	 * Reubica a los jugadores de tal manera que ocupen todas las sillas (saca
	 * las sillas sobrantes).
	 */
	public void startGame() {
		int[] teamCount; // cantidad de jugadores de cada team
		Vector[] teams; // equipos
		Object player;

		// crea el vector de 2 equipos
		teams = new Vector[] { new Vector(3), new Vector(3) };

		for (int i = 0; i < sittedPlayers.size(); i++) {
			player = sittedPlayers.get(i);
			if (player != null)
				teams[i % 2].add(player);
		}

		if (teams[0].size() == teams[1].size()) {
		    //Como andaba esto antes????? 2004.02.10 - Ale
		    //Con esto se modifica totalmente la sentada de jugadores :(
		    // y da un AIOFE al querer parar al jugador :((
			sittedPlayers.clear();
			for (int i = 0; i < teams[0].size(); i++) {
				sittedPlayers.add(teams[0].get(i));
				sittedPlayers.add(teams[1].get(i));
			}

			started = true;
		} else
			throw new IllegalStateException(
					"Equipos no parejos: Equipo 0 tiene " + teams[0].size()
							+ ", equipo 1 tiene " + teams[1].size());
	}

	/**
	 * Retorna la cantidad de jugadores en cada equipo
	 * 
	 * @return Un vector de 2 enteros, donde el primero representa a la cantidad
	 *         de jugadores del team 0, y el segundo del team 1
	 */
	private int[] getTeamCount() {
		int[] teamCount;

		teamCount = new int[2];

		for (int i = 0; i < getPlayerCount(); i++) {
			teamCount[i % 2] += (getPlayer(i) != null) ? 1 : 0;
		}

		return teamCount;
	}

	/**
	 * Retorna verdadero si los equipos estï¿½n parejos
	 */
	public boolean evenTeams() {
		int[] teamCount;

		teamCount = getTeamCount();
		return teamCount[0] == teamCount[1] && teamCount[0] > 0;
	}

	/*
	 * Te devuelve la referencia de un player de acuerdo al lugar donde esta
	 * sentado, si no hay nadie sentado alli te devuelve null
	 */

	public TrucoPlayer getPlayer(int chair) {
	    TrucoPlayer ret = null;
	    try {
	        ret = (TrucoPlayer) sittedPlayers.get(chair);
        } catch (Exception e) {
            //TODO PP, daba un AIOBE :(
        }	    
		return ret;
	}

	public int getChair(TrucoPlayer player) {
		return sittedPlayers.indexOf(player);
	}

	/*
	 * Devuelve la posicion relativa de un jugador en relacion al jugador que
	 * esta enfrente a la pantalla. Se le manda el lugar de la silla en donde
	 * eligiï¿½ sentarse y de acuerdo a algunos calculos se le coloca en la
	 * posicion correspondiente para que el jugador actual lo vea
	 */
	public int getPos(int chair) {
		if (started)
			return (getPlayerCount() + chair - getLocalChair())
					% getPlayerCount();
		else
			return chair;
	}

	/**
	 * Retorna la posiciï¿½n en la cual estï¿½ <code>player</code>. Si no
	 * estï¿½ sentado, retorna -1
	 */
	public int getPos(TrucoPlayer player) {
		return getPos(getChair(player));
	}

	public int getChair(int pos) {
		if (started)
			return (getLocalChair() + pos) % getPlayerCount();
		else
			return pos;
	}

	public int getPlayerCount() {
	    /*int ret = 0;
	    Iterator iter = getSittedPlayers().iterator();
	    while (iter.hasNext()) {
            if(iter.next() != null)
                ret++;
        }
		return ret;*/
	    return getSittedPlayers().size();
	}

	/**
	 * Retorna verdadero si 'player' está sentado
	 */
	public boolean isSitted(TrucoPlayer player) {
		boolean ret = false;
//		players.contains(player);
		Iterator iter = sittedPlayers.iterator();
		while (iter.hasNext()) {
			TrucoPlayer element = (TrucoPlayer) iter.next();
			//Tengo que preguntar si no es nulo, xq el iterador viene con elementos null
			// del vector players
			if (element != null && element.getName().equals(player.getName())) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * @return Returns the players.
	 */
	public Vector getSittedPlayers() {
		return sittedPlayers;
	}

	/**
	 * @param players
	 *            The players to set.
	 */
	public void setSittedPlayers(Vector players) {
		this.sittedPlayers = players;
	}

	/**
	 * @return Returns the started.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @param started
	 *            The started to set.
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * @return Returns the playerLocal.
	 */
	public TrucoPlayer getPlayerLocal() {
		return playerLocal;
	}
}