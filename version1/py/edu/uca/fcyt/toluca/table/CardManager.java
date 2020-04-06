package py.edu.uca.fcyt.toluca.table;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.table.animation.Animable;

/**
 * Maneja la animación de las cartas
 */
class CardManager implements Animable
{
	
	private TablePlayer players[];	// jugadores de mesa
	private Vector toDraw;			// qué player[i] dibujar
	private int playerCount = -1;	// cantidad de jugadores
	private TableDeck tDeck; 		// maso

	/** construye un CardManager */
	public CardManager(int playerCount)
	{
		int i;

		// crea el maso el cual lo representan 6 cartas
		tDeck = new TableDeck(6);

		// inicializa las variables
		this.playerCount = playerCount;

		createTablePlayers();
	}

	// crea los jugadores de mesa
	synchronized private void createTablePlayers()
	{
		TableCard tCards[];
		
		// verificaciones
		Util.verif
		(
			playerCount != -1, "Cantidad de jugadores inválida"
		);

		// crea el vector de jugadores de mesa
		players =  new TablePlayer[playerCount];
		
		// crea el vector de orden de dibujo
		toDraw = new Vector();

		// carga las cartas a cada jugador de mesa
		for (int i = 0; i < playerCount; i++)
		{
			tCards = new TableCard[3];
			for (int j = 0; j < 3; j++)
			{
				tCards[j] = new TableCard();
			}

			// crea el 'i'-ésimo jugador de mesa
			players[i] = new TablePlayer(i, playerCount, tCards);
			
			// el orden de dibujo es el orden de creación
			toDraw.add(new Integer(i));
		}
	}
	
	/** Pone todas las cartas en manos del jugador 'dealer' */
	synchronized public void putCardsInDealer(int dealer)
	{
		// pone el maso en el jugador 'dealer'
		tDeck.pushState
		(
			(int) (players[dealer].getX() * 4),
			(int) (players[dealer].getY() * 3.4),
			0, Util.cardScale, 0
		);


		for (int i = 0; i < playerCount; i++)
			players[i].pushState(players[dealer], 1, 0);
	}

	/** Reparte las cartas */
	synchronized public void deal(int dealer, boolean touch)
	{
		int p, c;
		TableCard tCard;
		long startPause, endPause;

		// verificaciones
		Util.verifParam(dealer < playerCount, "Parámetro 'dealer' inválido");

		Integer dealInt;
		
		dealInt = new Integer(dealer);
		toDraw.remove(dealInt);
		toDraw.add(0, dealInt);
		
		for (int i = 0; i < playerCount * 3; i++)
		{
			p = (i + dealer + 1) % playerCount;
			c = i / playerCount;
			
			tCard = players[p].getUnplayed(c);

			tCard.pushPause(i * 250);
			players[p].setDraw(tCard, 350);
			tCard.pushPause((2 - c) * 250 * playerCount + 100);
		}

			
	}

	/** Hace que cada jugador tome sus cartas */
	synchronized public void take()
	{
		for (int i = 0; i < players.length; i++)
			players[i].setTake(1500);
	}

	/**
	 * Hace que el 'player'-ésimo jugador no actual
	 * juege su carta 'card'
	 */
	synchronized public TableCard playCard(int player, Card card)
	{
		TableCard tCard;
		
		// verificaciones
		Util.verifParam
		(
			player > 0 || player < playerCount, 
			"Parámetro 'player' inválido: " +  player
		);
		
		return players[player].setPlayCard(card, 250);
	}
	
	/**
     * Hace que un jugador juege todas sus cartas cerradas.
     * @param player	posición del jugador
     */
     public void playClosed(int player)
     {
     	players[player].setCloseCards(250);
     	players[player].pushPause(1000);
     }

	/** Pone el maso en el jugador de índice 'dealer' */
	synchronized public void putDeckInTable(long delay, int dealer)
	{
		int x, y;
		double ang;
		int d;

		// verificaciones
		Util.verifParam
		(
			dealer >= 0 && dealer < playerCount,
			"Parámetro 'dealer' inválido"
		);

		d = (dealer == 0 ? playerCount - 1 : dealer - 1);

		x = players[dealer].getX();
		x += players[d].getX();

		y = players[dealer].getY();
		y += players[d].getY();

		ang = players[dealer].getAngle();
		ang += players[d].getAngle();
		ang /= 2;

		if (dealer == 0) ang -= Math.PI;

		switch (playerCount)
		{
			case 2:

				x = (int) ((dealer - .5f) * 350);

			case 4:
				x *= .73 * 1.5;
				y *= .93 * 1.5;
				break;

			case 6:
				x *= .83;
				y *= 1;
				break;
		}

		tDeck.pushPause(delay);
		tDeck.pushState
		(
			x, y, ang, Util.cardScale, 1000
		);
	}

	/**
	 * Hace que el jugador 'dealer'
	 * recoja las cartas y el maso
	 */
	synchronized public void gatherCards(int dealer)
	{
		double escX, escY;

		initCards();

		// verificaciones
		Util.verifParam
		(
			dealer >= 0 && dealer < playerCount,
			"Parámetro 'dealer' inválido"
		);

		switch (playerCount)
		{
			case 2:
				escX = 0; escY = 3f;
				break;

			case 4:
				escX = 3f; escY = 3f;
				break;

			default:
				escX = 2.5f; escY = 3.5f;
				break;
		}

		tDeck.pushState
		(
			(int) (players[dealer].getX() * escX),
			(int) (players[dealer].getY() * escY),
			0, Util.cardScale, 1000
		);

		for (int i = 0; i < playerCount; i++)
			players[i].pushState(players[dealer], 1, 1000);
	}

//	/** Asigna las cartas del jugador actual */
//	synchronized public void setCards(Card cards[])
//	{
//		for (int i = 0; i < 3; i++)
//			players[0].getTCard(i).setCard(cards[i]);
//	}
	
	/** 
	 * Establece siguiente TableCard no jugada del  
	 * 'player'-ésimo player a 'card'. Devuelve su índice'
	 */
//	synchronized public TableCard setCard(int player, Card card)
//	{
//		TableCard tCard;
//		
//		return players[player].setCard(card);
//	}

//	/** Hace que el jugador actual esconda sus cartas */
//	synchronized public void hideCards()
//	{
//		players[0].setTake(false, 500);
//	}
	
	/**
	 * Hace que las cartas del 'player'-ésimo
	 * jugador se muestren
	 */
	synchronized public void showPlayed(int player)
	{
		players[player].setShowPlayed(false, 500);
		players[player].pushPause(1500);
		players[player].setShowPlayed(true, 500);
	}

	/** Corta el mazo en un porcentaje 'per' */
	synchronized public void cutDeck(int per)
	{
		tDeck.setCutCards(per, false, 500);
		tDeck.setInvertCards(per, 200);

		for (int i = 0; i < tDeck.getCardCount(); i++)
		{
//			animator.toTop(tDeck.getTableCard(i));
		}

		tDeck.setCutCards(per, true, 500);
	}

	/**
	 * Juega una carta del jugador actual si 
	 * se clickeó en ella, y la devuelve
	 */
	synchronized public Card playCardIfClick(int x, int y)
	{
		TableCard tc;
		TCardState tcState;
		long r2;
		r2 = (long) (Math.pow(TableCard.CARD_WIDTH / 2, 2));

		for (int i = players[0].getUnplayedCount() - 1; i >= 0; i--)
		{
			tc = players[0].getUnplayed(i);
			tcState = tc.getCurrState();
			if
			(
				Math.pow(x - tcState.x, 2) +
				Math.pow(y - tcState.y, 2)
				<= r2
			)
			{
				if (tc.getRemainingTime() == 0)
					return tc.getCard();
				else
					return null;
			}
		}
		return null;
	}

	/**
	 * Muestra las cartas de un jugador
	 * si se cliqueó en ellas
	 */
	synchronized public int getClickCards(int x, int y)
	{
		long r2;


		r2 = (long) Math.pow(Util.cardScale * TableCard.CARD_RADIUS, 2);
		for (int i = 0; i < playerCount; i++) if
		(
			Math.pow(x - players[i].getX(), 2) +
			Math.pow(y - players[i].getY(), 2) <= r2
		) 
		{
			if (players[i].getRemainigTime() == 0)
				return i;
			else
				return -1;
		}

		return -1;
	}
	
	/** Hace que el jugador actual muestre sus cartas */
	synchronized public void showCards(Card[] cards)
	{
		players[0].setShowHand(cards, 500);
	}
	
	/**
     * Agrega una pausa a la animación de las 
     * cartas del jugador en la posición 'pos'
     */
    synchronized public void pushPause(int pos, long duration)
    {
    	players[pos].pushPause(duration);
    }

	synchronized public void paint(int buffIndex) 
	{
		tDeck.paint(buffIndex);
		
		for (int i = 0; i < players.length; i++)
			players[((Integer) toDraw.get(i)).intValue()].paint
			(
				buffIndex
			);
	}

//	synchronized public void clear(Graphics2D grOut) 
//	{
//		for (int i = 0; i < players.length; i++)
//			players[i].clear(grOut);
//
//		tDeck.clear(grOut);
//	}

	synchronized public void advance() 
	{
		for (int i = 0; i < players.length; i++)
			players[i].advance();

		tDeck.advance();
	}
 
    /**
     * Obtiene el tiempo que falta para que todas las
     * cartas terminen de transicionar.
     */
    synchronized public long getRemainigTime()
    {
    	long actTime, maxTime = 0;
    	
    	// obtiene el tiempo máximo
    	for (int i = 0; i < players.length; i++)
    	{
    		actTime = players[i].getRemainigTime();
    		if (actTime > maxTime) maxTime = actTime;
    	}
    	
    	return maxTime;
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
		for (int i = 0; i < players.length; i++)
			pushGeneralPause(i, time);
		
		tDeck.pushGeneralPause(time);
	}   	

    /**
     * Agrega una pausa a todas las cartas igual a <b>time</b> - 
     * tiempo restante.
     * Si existen caritas del vector que tienen un tiempo restante
     * mayor que el tiempo especificado, éstas son omitidas,
     * @param time		cantidad de tiempo especificado
     */
	synchronized public void pushGeneralPause(int pos, long time)
	{
		players[pos].pushGeneralPause(time);
	}
	
	/**
     * (Re)inicializa a todas las cartas
     */
    synchronized private void initCards()
    {
		for (int i = 0; i < playerCount; i++)
			players[i].initUnplayed();
	}
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.animation.Animable#setOut(java.awt.image.BufferedImage, java.awt.geom.AffineTransform)
	 */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		for (int i = 0; i < players.length; i++)
			players[i].setOut(biOut, afTrans);
			
		tDeck.setOut(biOut, afTrans);
	}

}