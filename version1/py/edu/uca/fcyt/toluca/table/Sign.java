package py.edu.uca.fcyt.toluca.table;

import java.util.Hashtable;

import py.edu.uca.fcyt.game.Card;

/**
 * Clase que contiene las constantes de las señas
 */
public class Sign
{
	public static final int NONE = 0;
	public static final int MACHO = 1;
	public static final int HEMBRA = 2;
	public static final int ESPADA_7 = 3;
	public static final int ORO_7 = 4; 
	public static final int VAL_3 = 5;
	public static final int VAL_2 = 6;
	public static final int VAL_1 = 7;
	public static final int ENVIDO = 8;
	public static final int SECA = 9;
	
	private static Hashtable sNames = getNames();
	
	/**
     * Retorna verdadero si 'sign' es una seña válida
     */
	public static boolean isSign(int sign)
	{
		return sign >= NONE || sign <= SECA;
	}
	
	/**
     * Retorna la seña correspondiente a la carta 'card'
     */
    public static int getSign(Card card)
    {
    	int val, kind;
    	
    	// obtiene el valor y el palo de la carta
    	val = card.getValue();
    	kind = card.getKind();
    	
    	// retorna la seña
    	switch (val)
    	{
    		case 1:
    			if (kind == Card.ESPADA) return MACHO;
    			else if (kind == Card.BASTO) return HEMBRA;
    			else return VAL_1;
    			
    		case 7:
    			if (kind == Card.ESPADA) return ESPADA_7;
    			else if (kind == Card.ORO) return ORO_7;
    			else return NONE;
    		
    		case 3: return VAL_3;
    		case 2: return VAL_2;

			default:
				return NONE;
		}
    }
    
    /**
     * Retorna un Hastable con el par (seña: int, nombre: String).
     */
    private static Hashtable getNames()
    {
    	Hashtable ret;
    	
    	ret = new Hashtable();
    	
 		ret.put(new Integer(NONE), "Nada");
		ret.put(new Integer(MACHO), "Macho");
		ret.put(new Integer(HEMBRA), "Hembra");
		ret.put(new Integer(ESPADA_7), "7 de espada");
		ret.put(new Integer(ORO_7), "7 de oro");
		ret.put(new Integer(VAL_3), "Tricota");
		ret.put(new Integer(VAL_2), "Duquesa");
		ret.put(new Integer(VAL_1), "Guempe");
		ret.put(new Integer(ENVIDO), "Tanto");
		ret.put(new Integer(SECA), "Seca");
		
		return ret;
   }
   
   /**
    * Retorna el nombre de una seña
    * @param sign	seña
    */
   public static String getName(int sign)
   {
		return (String) sNames.get(new Integer(sign));
   }
}