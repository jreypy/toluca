package py.edu.uca.fcyt.toluca.table;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import py.edu.uca.fcyt.toluca.table.animation.Animable;

/* 
 * Representa a un mazo en la mesa
 */
 
class TableDeck implements Animable
{
	TableCard cards[];
	
	// construye un TableDeck con 'nCards' cartas representándolo
	public TableDeck(int nCards)
	{
		super();
		
		// crea los TableCards
		cards = new TableCard[nCards];
		for (int i = 0; i < cards.length; i++)
		{
			cards[i] = new TableCard();
			cards[i].setBlackOutline(i < cards.length - 1);
		}
	}

	// agrega una nueva posición del maso
	public void pushState
	(
		int x, int y, double angle, double scale, long duration
	) 
	{
		double myScale;
		
		for (int i = 0; i < cards.length; i++)
		{
			myScale = Math.exp(i / 130.0);

			cards[i].pushState
			(
				(int) (x * myScale), 
				(int) (y * myScale), 
				angle, myScale * scale, null, duration
			);
		}
	}
	
	/**
     * Agrega una pausa a la animación del maso
     */
    public void pushPause(long duration)
    {
    	for (int i = 0; i < cards.length; i++)
    		cards[i].pushPause(duration);
    }
    
    /**
     * Agrega una pausa igual al tiempo que queda 
     * por animar y <code>duration</code>
     */
    public void pushGeneralPause(long duration)
    {
    	for (int i = 0; i < cards.length; i++)
    		cards[i].pushPause
    		(
    			duration - cards[i].getRemainingTime()
    		);
    }

	// devuelve la 'index'-ésima carta que conforma el maso
	public TableCard getTableCard(int index)
	{
		return cards[index];
	}
	
	// devuelve la cantidad de cartas que conforman el maso
	public int getCardCount() {	return cards.length; }
	
	public void setCutCards(int per, boolean undo, long duration)
	{
		double s, c;
		int sgn;
		TCardState tcState;
		
		// separa las cartas
		for (int i = 0; i < cards.length; i++)
		{
			tcState = cards[i].getCurrState();
			c = Math.cos(tcState.angle);
			s = Math.sin(tcState.angle);
			if (undo)
				sgn = (i < cards.length - (cards.length * fixPer(per)) / 100) ? -1 : 1;
			else
				sgn = (i < (cards.length * fixPer(per)) / 100) ? -1 : 1;

			cards[i].pushState
			(
				(int) (tcState.x + sgn * 40 * c),
				(int) (tcState.y + sgn * 40 * s),
				tcState.angle, 
				tcState.scale, 
				null, duration
			);
		}
		
	}
	
	public void setInvertCards(int per, long duration)
	{
		double myScale;
		int cut;
		TableCard[] copy = new TableCard[cards.length];
		TCardState tcState;
		
		// nivela sus profundidades al la 
		// profundidad de la primera carta
		for (int i = 0; i < cards.length; i++)
		{
			tcState = cards[i].getCurrState();
			myScale = Math.exp(i / 130.0);

			cards[i].pushState
			(
				(int) (tcState.x / myScale), 
				(int) (tcState.y / myScale), 
				tcState.angle, 
				tcState.scale / myScale, 
				null, duration
			);
			copy[i] = cards[i];
		}

		cut = (cards.length * fixPer(per)) / 100;

		for (int i = 0; i < cards.length; i++)
			cards[i] = copy[(i + cut) % cards.length];

		// actualiza las profundidades de las cartas
		for (int i = 0; i < cards.length; i++)
		{
			tcState = cards[i].getCurrState();
			myScale = Math.exp(i / 130.0);

			cards[i].pushState
			(
				(int) (tcState.x * myScale), 
				(int) (tcState.y * myScale), 
				tcState.angle, 
				tcState.scale * myScale, 
				null, 0
			);
		}
	}
		
	private int fixPer(int per)
	{
		if ((cards.length * per) / 100 == 0) 
			return 100 / cards.length + 1;
		else
			return per;
	}

	public void paint(int buffIndex) 
	{
		for (int i = 0; i < cards.length; i++)
			cards[i].paint(buffIndex);
	}

	public void advance() 
	{
		for (int i = 0; i < cards.length; i++)
			cards[i].advance();
	}

	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		for (int i = 0; i < cards.length; i++)
			cards[i].setOut(biOut, afTrans);
	}

}
	
	