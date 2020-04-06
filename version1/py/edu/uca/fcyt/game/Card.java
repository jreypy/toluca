package py.edu.uca.fcyt.game;

import java.util.Hashtable;

import javax.swing.ImageIcon;


public class Card
{
	public static final int ORO = 1;
	public static final int ESPADA = 2;
	public static final int COPA = 3;
	public static final int BASTO = 4;
	public static final Hashtable kNames = getKindNames();

	private byte value;			// valor de la carta
	private byte kind;			// palod de la carta
	protected boolean flipped;	// carta cerrada (flipped) o abierta

	// construye una carta
	public Card(int kind, int value)
	{
		setKind(kind);
		setValue(value);
	}
	public Card()
	{
	}

	// devuelven el valor y el palo de la carta, y si est� abierta o no
	public byte getValue() { return value; }
	public byte getKind() { return kind; }
	public boolean isFlipped() { return flipped; }

	// establece el valor de la carta
	public Card setValue(int value)
	{
		if (value < 1 || value > 12) throw new RuntimeException("Valor inv�lido");
		this.value = (byte) value;
		return this;
	}

	// establece el palo de la carta
	public Card setKind(int kind)
	{
		if (kind < 1 || kind > 4) throw new RuntimeException("Palo inv�lido");
		this.kind = (byte) kind;
		return this;
	}

	// establece si la carta est� abierta o no
	public Card setFlipped(boolean flipped)
	{
		this.flipped = flipped;
		return this;
	}


	// obtiene un 'ImageIcon' contiene al dibujo de la carta
	public ImageIcon getImageIcon()
	{
		String[] kinds = new String[] {"Oro", "Espada", "Copa", "Basto"};

		//return new ImageIcon(Util.getImagesDir() + kinds[kind - 1] + "/" + value + ".gif");
		return new ImageIcon(
		getClass().getResource(
		"/py/edu/uca/fcyt/toluca/images/" + kinds[kind - 1] + "/" + value + ".gif")
		);
	}
	
	/**
	 * Retorna el c�digo hash.
	 * c1.hashCode() == c2.hashCode(), si y s�lo si 
	 * c1.getValue() == c2.Value() y c1.getKind() == c2.getKind()
	 */
	public int hashCode()
	{
		return (kind - 1) * 12 + value - 1;
	}
    
	/**
	 * Retorna verdadero si una carta es igual a �sta. Si 
	 * <code> o </code> no es un Card, retorna falso.
	 * @param o		la carta en cuestion
	 */
	public boolean equals(Object o)
	{
		Card card;
		try
		{
			card = (Card) o;
			return card.value == value && card.kind == kind;
		}
		catch (ClassCastException ex)
		{
			return false;
		}
	}
    
	/**
	 * Retorna un Hashtable con los pares 
	 * (palo: int, nombre: String)
	 */
	private static Hashtable getKindNames()
	{
		Hashtable ret;
    	
		ret = new Hashtable();
		ret.put(new Integer(COPA), "Copa");
		ret.put(new Integer(ORO), "Oro");
		ret.put(new Integer(BASTO), "Basto");
		ret.put(new Integer(ESPADA), "Espada");
		return ret;
	}
    
	/**
	 * Retorna el nombre de un palo
	 * @param kind	Palo
	 */
	public static String getKindName(int kind)
	{
		return (String) kNames.get(new Integer(kind));
	}
    
	/**
	 * Retorna la descripci�n de la carta
	 */
	public String getDescription()
	{
		return new String(getValue() + " de " + getKindName(kind));
	}
	/**
	 * @param b
	 */
	public void setKind(byte b) {
		kind = b;
	}

	/**
	 * @param b
	 */
	public void setValue(byte b) {
		value = b;
	}

}
