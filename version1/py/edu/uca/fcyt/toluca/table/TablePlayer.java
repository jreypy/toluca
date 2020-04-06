package py.edu.uca.fcyt.toluca.table;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Vector;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.table.animation.Animable;

/**
 * Maneja a las cartas de un jugador
 */
class TablePlayer implements Animable, TableCardListener
{
	// contiene el par (TableCard, Posición)
//	private class TCardPos
//	{
//		public TableCard tCard;
//		public int pos;
//		public TCardPos(TableCard tCard, int pos)
//		{
//			this.tCard = tCard;
//			this.pos = pos;
//		}
//	}
	
	private int posX, posY;	// posición x e y de la repartida
	private double angle;	// ángulo de repartida
	private int playerPos;			// número de silla
	private final LinkedList toTable;	// cartas a jugar
	private final LinkedList toHand;	// cartas a restaurar
								
	private final Vector played;		// cartas ya jugadas
	private final Vector unplayed;		// cartas no jugadas
	private final Vector inHand;		// cartas en mano
	private final Vector onTable;		// cartas en mano

	// escala de la posición x e y
	private final static double POS_SCALE_X = .4f;
	private final static double POS_SCALE_Y = .2f;

	/**
     * Construye un TablePlayer en la posición 'playerPos' con
     * 'playerCount' cantidad de jugadores y TableCards 'cards'
     */
	public TablePlayer(int playerPos, int playerCount, TableCard tCards[])
	{
		int i, j, k;

		// verificaciones
		Util.verifParam
		(
			playerPos >= 0 && playerPos < 6, 
			"Parámetro 'playerPos' inválido"
		);

		// establece la posición (x, y) y el ángulo de juego
		switch (playerCount)
		{
			case 2:
				posX = (int) (POS_SCALE_X * PlayTable.TABLE_WIDTH * -Math.cos((playerPos + .5) * Math.PI));
				posY = (int) (POS_SCALE_Y * PlayTable.TABLE_HEIGHT * Math.sin((playerPos + .5) * Math.PI));
				angle = -(playerPos * Math.PI);
				break;
			case 4:
				posX = (int) (.5f * POS_SCALE_X * PlayTable.TABLE_WIDTH * -Math.cos((playerPos + 1) * Math.PI / 2));
				posY = (int) (POS_SCALE_Y * PlayTable.TABLE_HEIGHT * Math.sin((playerPos + 1) * Math.PI / 2));
				angle = -((playerPos) * Math.PI / 2);
				break;
			case 6:
				k = playerPos < 2 ? playerPos :
					playerPos < 5 ? playerPos + 1 :
					7;

				posX = (int) (.8 * POS_SCALE_X * PlayTable.TABLE_WIDTH * -Math.cos((k + 2) * Math.PI / 4));
				posY = (int) (POS_SCALE_Y * PlayTable.TABLE_HEIGHT * Math.sin((k + 2) * Math.PI / 4));
				angle = -(k * Math.PI / 4);

				break;
			default:
				throw new RuntimeException("Cantidad de jugadores inválido");
		}

		// crea el vector de cartas jugadas y no jugadas
		played = new Vector();
		unplayed = new Vector();
		inHand = new Vector();
		onTable = new Vector();
		
		// carga el vector de TableCards no jugados y en mano
		for (i = 0; i < tCards.length; i++)
		{
			unplayed.add(tCards[i]);
			inHand.add(tCards[i]);
			tCards[i].addListener(this);
		}

		// carga la posición del jugador
		this.playerPos = playerPos;
		
		// crea la lista enlazada de las cartas que se jugarán
		// y el de cartas que se restaurarán
		toTable = new LinkedList();
		toHand = new LinkedList();
	}
	
	/**
     * Marca a todas las cartas como no jugadas
     */
    synchronized public void initUnplayed()
    {
    	int cCount;
    	TableCard tCard;
    	
    	cCount = played.size() + unplayed.size();
    	
    	// verificaciones
    	verifIntegrity();
    	
    	// recarga las cartas no jugadas
    	for (int i = 0; i < played.size(); i++)
    	{
    		tCard = getPlayed(i);
    		unplayed.add(tCard);

    		if (!toHand.contains(tCard)) toHand.add(tCard);
    	}
    	
    	played.clear();
	}
    
	/**
	 * Pone la posicion finales de la 'index'-ésima carta boca
	 * abajo y de manera desordenada (la reparte), con
	 * una duración de animación 'duration'
	 */
	synchronized public void setDraw
	(
		TableCard tCard, long duration
	)
	{
		// manda al tope la carta y la reparte
		//toTop(tCard);
		tCard.pushState
		(
			(int) (posX + Math.random() * 5 - 2.5),
			(int) (posY + Math.random() * 5 - 2.5),
			Math.random() * Math.PI * 2 - 1,
			Util.cardScale, null, duration
		);
	}

	/**
	 * Pone las posiciones finales de las
	 * cartas en la mano de cada jugador
	 */
	synchronized public TablePlayer setTake(long duration)
	{
		double ang;
		TableCard tCard;

		// pone el estado de las cartas en las manos de cada jugador
		for(int i = 0; i < unplayed.size(); i++)
		{
			tCard = getUnplayed(i);
			ang = tCard.getLastState().angle;
			tCard.pushState
			(
				(int) (posX * 2),
				(int) (posY * 3.4),
				ang + Util.normAng(angle - ang), 
				Util.cardScale, null, duration
			);

			tCard.pushState
			(
				(int) (posX * 2),
				(int) (posY * 3.4),
				angle, 
				Util.cardScale, null, 0
			);
		}

		return this;
	}
	
	/**
	 * Establece los datos de animación
	 * para jugar una carta.
	 * @param tCard		TableCard a jugar
	 * @param duration	duración de la animación
	 */
	synchronized public TableCard setPlayCard
	(
		Card card, long duration
	)
	{
		TableCard tCard;
		
		tCard = getUnplayed(card);
		if (tCard == null) tCard = getUnplayed(0);
		
		playCard(tCard);
		
		// establece el estado del TableCard como para jugarla
		tCard.pushState
		(
			(int) (posX + 5 * Math.cos(angle) * (played.size() - 2)),
			(int) (posY + 5 * Math.sin(angle) * (played.size() - 2)),
			angle, Util.cardScale, card, duration
		);
		
		return tCard;
	}
	
	/**
     * Retorna la 'index'-ésima carta jugada
     */
    synchronized public TableCard getPlayed(int index)
    {
    	return (TableCard) played.get(index);
    }

	/**
     * Retorna la 'index'-ésima carta no jugada
     */
    synchronized public TableCard getUnplayed(int index)
    {
    	return (TableCard) unplayed.get(index);
    }

	/**
     * Retorna la 'index'-ésima carta en la mano
     */
    synchronized public TableCard getInHand(int index)
    {
    	return (TableCard) inHand.get(index);
    }

	/**
     * Retorna la 'index'-ésima carta en la mesa
     */
    synchronized public TableCard getOnTable(int index)
    {
    	return (TableCard) onTable.get(index);
    }

	/**
	 * Establece los datos de animación
	 * para cerrar todas las cartas
	 * @param duration	duración de la animación
	 */
	synchronized public void setCloseCards
	(
		long duration
	)
	{
		TableCard tCard;
		
		for (int i = 0; i < unplayed.size(); i++)
		{
			tCard = getUnplayed(i);
			// establece el estado del TableCard como para jugarla
			tCard.pushState
			(
				(int) (posX + 5 * Math.cos(angle) * (i - 1)),
				(int) (posY + 5 * Math.sin(angle) * (i - 1)),
				angle, Util.cardScale, null, duration
			);
		}
	}
	
	/**
     * Retorna la cantidad de cartas jugadas
     */
    synchronized public int getUnplayedCount()
    {
    	return unplayed.size();
    }
    
    /**
     * Verifica que la suma de cartas jugadas y las no
     * jugadas sea igual a 3
     */
    private void verifIntegrity()
    {
    	int puCount, htCount;
    	
    	puCount = played.size() + unplayed.size();
    	htCount = inHand.size() + onTable.size();
    	
    	Util.verif
    	(
    		puCount == 3,
    		"La suma de cartas jugadas y no jugadas es " + puCount
    	);

    	Util.verif
    	(
    		htCount == 3,
    		"La suma de cartas en mano y en la mesa es " + htCount
    	);
    }

	/**
	 * Establece los datos de animación para mostrar las cartas
	 * del jugador (que debe estar en la posición 0)
	 * @param duration	duración de la animación
	 */
	synchronized public void setShowHand
	(
		Card[] cards, long duration
	)
	{
		double c, m;
		Card card;
		TableCard tCard;

		// verificaciones
		Util.verif(playerPos == 0, "El jugador debe ser el 0");
		verifIntegrity();
		
		switch (played.size())
		{
			case 0: c = -1; break;
			case 1: c = -.5f; break;
			default: c = 0;
		}

		for (int i = 0; i < unplayed.size(); i++)
		{
			tCard = getUnplayed(i);
			m = (c == 0) ? 15 : 0;
			try { card = cards[i]; }
			catch (NullPointerException ex)
			{
				card = tCard.getCard();
			}
			
			tCard.pushState
			(
				(int) (40 * c),
				(int) (PlayTable.TABLE_HEIGHT/2 - 40 - m),
				.6f * c, 1, card, duration
			);
			c++;
		}
	}

	/**
	 * Establece los datos de animación para "abrir" o "cerrar" las
	 * cartas jugadas dependiendo de restore (falso o verdadero resp.)
	 */
	synchronized public void setShowPlayed
	(
		boolean restore, long duration
	)
	{
		double m, n, x, y;
		TCardState tcState;
		TableCard tCard;
		

		m = restore ? 0 : 50;
		n = (played.size() - 1) / 2.0;

		// establece los estados de cada TableCard
		for (int i = 0; i < played.size(); i++)
		{
			tCard = getPlayed(i);
			tCard.pushState
			(
				posX + (int) ((5 * (i - 1) + (i - n) * m) * Math.cos(angle)),
				posY + (int) ((5 * (i - 1) + (i - n) * m) * Math.sin(angle)),
				angle, Util.cardScale, tCard.getCard(), duration
			);
		}
	}


	/**
	 * Pone la posición y ángulo de la carta a la posición
	 * de colección de un cierto TablePlayer
	 */
	synchronized public void pushState
	(
		TablePlayer tPlayer, float scale, long duration
	)
	{
		TableCard tCard;
		Card card;
		TCardState lastState = null;
		
		for (int i = 0; i < unplayed.size(); i++)
		{
			tCard = getUnplayed(i);
			
			try 
			{ 
				lastState = tCard.getLastState();
				card = lastState.card; 
			}
			catch(NullPointerException ex1) 
			{ 
				try
				{
					card = tCard.getCurrState().card;
				}
				catch(NullPointerException ex2)
				{
					card = null; 
				}
			}

			tCard.pushState
			(
				(int) (tPlayer.posX * 2),
				(int) (tPlayer.posY * 3.4f),
				tPlayer.angle,
				Util.cardScale * scale, 
				card, duration
			);
		}
	}

	// retornan la posición (x, y) de repartición
	public int getX() { return posX; }
	public int getY() { return posY; }
	public double getAngle() { return angle; }
	
	synchronized public void paint(int buffIndex) 
	{
		for (int i = 0; i < onTable.size(); i++)
			getOnTable(i).paint(buffIndex);
		for (int i = 0; i < inHand.size(); i++)
			getInHand(i).paint(buffIndex);
	}

	synchronized public void advance() 
	{
		for (int i = 0; i < played.size(); i++)
			getPlayed(i).advance();

		for (int i = 0; i < unplayed.size(); i++)
			getUnplayed(i).advance();
	}

    /**
     * Agrega una pausa a todas las cartas igual a <b>time</b> - 
     * tiempo restante.
     * Si existen caritas del vector que tienen un tiempo restante
     * mayor que el tiempo especificado, éstas son omitidas,
     * @param time		cantidad de tiempo especificado
     */
	synchronized public void pushGeneralPause(long time)
	{
		TableCard tCard;
		
    	// agrega a cada carta la pausa
    	for (int i = 0; i < played.size(); i++)
    	{
    		tCard = getPlayed(i);
			tCard.pushPause(time - tCard.getRemainingTime());
		}

    	for (int i = 0; i < unplayed.size(); i++)
    	{
    		tCard = getUnplayed(i);
			tCard.pushPause(time - tCard.getRemainingTime());
		}
	}  	
	
    /**
     * Obtiene el tiempo que falta para que todas las
     * cartas terminen de transicionar.
     */
    synchronized public long getRemainigTime()
    {
    	long actTime, maxTime = 0;
    	
    	// obtiene el tiempo máximo
    	for (int i = 0; i < played.size(); i++)
    	{
    		actTime = getPlayed(i).getRemainingTime();
    		if (actTime > maxTime) maxTime = actTime;
    	}
    	for (int i = 0; i < unplayed.size(); i++)
    	{
    		actTime = getUnplayed(i).getRemainingTime();
    		if (actTime > maxTime) maxTime = actTime;
    	}
    	
    	return maxTime;
    }		
    
	/**
     * Retorna el {@link TableCard} que contiene a 
     * <code>card</code>. Si no existe dicho TableCard, 
     * retorna <code>null</code>.
     */
    synchronized public TableCard getUnplayed(Card card)
    {
    	TableCard tCard;
    	for (int i = 0; i < unplayed.size(); i++)
    	{
    		tCard = getUnplayed(i);
    		if (card == tCard.getCard())
    			return tCard;
    	}
    	return null;
    }
     
    /**
     * Agrega una pausa a todas las cartas
     * @param duration	duración de la pausa
     */
	 synchronized public void pushPause(long duration)
	 {
	 	for (int i = 0; i < played.size(); i++)
	 		getPlayed(i).pushPause(duration);
	
	 	for (int i = 0; i < unplayed.size(); i++)
	 		getUnplayed(i).pushPause(duration);
	 }

	synchronized public void transitionCompleted(TableCard tCard) 
	{
		// verificaciones
		verifIntegrity();
		
		if (toTable.remove(tCard))
			if (inHand.remove(tCard)) onTable.add(tCard);

		if (toHand.remove(tCard))
			if (onTable.remove(tCard)) inHand.add(tCard);
	}
	
    /**
     * Manda una carta del vector de cartas no jugadas al de 
     * jugadas.
     * @param tCard	TableCard a mandar.
     */
    synchronized private void playCard(TableCard tCard)
    {
    	// verificaciones
    	verifIntegrity();
    	
    	if (unplayed.remove(tCard)) played.add(tCard);
	
		if (tCard.getRemainingTime() == 0)
		{
	    	if (inHand.remove(tCard)) onTable.add(tCard);
	    }
	    else if (!toTable.contains(tCard)) toTable.add(tCard);
    }

    /**
     * Manda una carta del vector de cartas jugadas al de 
     * no jugadas.
     * @param tCard	TableCard a mandar.
     */
    synchronized private void unplayCard(TableCard tCard)
    {
    	int cCount;
    	
    	cCount = played.size() + unplayed.size();
    	
    	// verificaciones
    	verifIntegrity();
    	
    	if (played.remove(tCard)) unplayed.add(tCard);

		if (tCard.getRemainingTime() == 0)
		{
	    	if (onTable.remove(tCard)) inHand.add(tCard);
	    }
	    else if (!toHand.contains(tCard)) toHand.add(tCard);
    }
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.animation.Animable#setOut(java.awt.image.BufferedImage, java.awt.geom.AffineTransform)
	 */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		for (int i = 0; i < onTable.size(); i++)
			getOnTable(i).setOut(biOut, afTrans);
		for (int i = 0; i < inHand.size(); i++)
			getInHand(i).setOut(biOut, afTrans);
	}
}