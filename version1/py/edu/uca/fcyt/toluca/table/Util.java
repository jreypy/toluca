package py.edu.uca.fcyt.toluca.table;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;

public class Util
{
	public static float cardScale = .75f;
	private static final Hashtable pNames = getPlayNames();
	private static final Hashtable eNames = getEventNames();
	private static final byte[] plays = getPlays();

	// dibuja 'iIcon' en 'bImage'
	static public void copyImage(ImageIcon iIcon, BufferedImage bImage)
	{
		// verificaciones
		Util.verifParam(iIcon != null, "Parámetro 'iIcon' nulo");
		Util.verifParam(bImage != null, "Parámetro 'bImage' nulo");
		
		bImage.createGraphics().drawImage
		(
			iIcon.getImage(),
			new AffineTransform(),
			iIcon.getImageObserver()
		);
	}

	/**
	 * si 'exp' es falso arroja una excepción 
     * InvalidParameterException con mensaje 'msj'
     */	
	static public void verifParam(boolean exp, String msj)
	{
		if (!exp) throw new InvalidParameterException(msj);
	}

	/**
	 * si 'exp' es falso arroja una excepción 
     * RuntimeException con mensaje 'msj'
     */	
	static public void verif(boolean exp, String msj)
	{
		if (!exp) throw new RuntimeException(msj);
	}

	static public int sgn(int num)
	{
		if (num > 0) return 1;
		else if (num < 0) return -1;
		else return 0;
	}

	static public String lineInput(String msg, int nChars)
	{
		byte[] b = new byte[nChars];
		int r;

		System.out.print(msg);

		try
		{
			r = System.in.read(b);
			return new String(b, 0, r - 2);
		} catch (IOException e)
		{
			return null;
		}
	}
	
	static public void sleep(long msecs)
	{
		long tAct;
		
		tAct = System.currentTimeMillis();
		while (tAct + msecs > System.currentTimeMillis());
	}

	static public void sleep(py.edu.uca.fcyt.toluca.table.animation.Animator anim, long msecs)
	{
		long tAct;
		
		tAct = System.currentTimeMillis();
		while (tAct + msecs > System.currentTimeMillis());
	}
        
	/**
     * Duerme el Thread 't' por 'millis' milisegundos.
     * Si la dormida es interrumpida, lanza un 
     * RuntimeException basado en el InterruptedException
     * arrojado
     */
	public static void wait(Object t, long millis)
	{
		try
		{
			synchronized(t)
			{
				t.wait(millis);
			}
		}
		catch(InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
     * Retorna el nombre del palo 'palo'
     */
    public static String getKindName(int kind)
    {
    	switch (kind)
    	{
    		case Card.BASTO: return "basto";
    		case Card.COPA: return "copa";
    		case Card.ORO: return "oro";
    		case Card.ESPADA: return "espada";
    		default:
    			throw new InvalidParameterException
    			(
    				"Argumento 'kind' inválido"
    			);
    	}
    }

	/** 
	 * Convierte 'ang' en un ángulo en el rango [-PI/2, PI/2)
	 */
	public static double normAng(double ang)
	{
		while (ang >= Math.PI / 2) ang -= Math.PI;
		while (ang < -Math.PI / 2) ang += Math.PI;

		return ang;
	}	

	/**
     * Retorna un Hashtable con los pares 
     * (jugada: Byte, nombre: String)
     */
	private static Hashtable getPlayNames()
	{
		Hashtable ret;
		
		ret = new Hashtable();
		ret.put(new Byte(TrucoPlay.ENVIDO), "Envido");
		ret.put(new Byte(TrucoPlay.REAL_ENVIDO), "Real Envido");
		ret.put(new Byte(TrucoPlay.FALTA_ENVIDO), "Falta Envido");
		ret.put(new Byte(TrucoPlay.FLOR), "Flor");
		ret.put(new Byte(TrucoPlay.TRUCO), "Truco");
		ret.put(new Byte(TrucoPlay.QUIERO), "Quiero");
		ret.put(new Byte(TrucoPlay.RETRUCO), "Quiero Retruco");
		ret.put(new Byte(TrucoPlay.VALE_CUATRO), "Quiero Vale 4");
		ret.put(new Byte(TrucoPlay.NO_QUIERO), "No quiero");
		ret.put(new Byte(TrucoPlay.CANTO_ENVIDO), "Cantar");
		ret.put(new Byte(TrucoPlay.PASO_ENVIDO), "Paso envido");
		ret.put(new Byte(TrucoPlay.CERRARSE), "Cerrarse");
		ret.put(new Byte(TrucoPlay.ME_VOY_AL_MAZO), "Me voy al mazo");
		return ret;
    }

	/**
     * Retorna un Hashtable con los pares 
     * (evento: Byte, nombre: String)
     */
	private static Hashtable getEventNames()
	{
		Hashtable ret;
		
		ret = new Hashtable();
		ret.put(new Byte(TrucoEvent.ENVIDO), "Envido");
		ret.put(new Byte(TrucoEvent.REAL_ENVIDO), "Real Envido");
		ret.put(new Byte(TrucoEvent.FALTA_ENVIDO), "Falta Envido");
		ret.put(new Byte(TrucoEvent.FLOR), "Flor");
		ret.put(new Byte(TrucoEvent.TRUCO), "Truco");
		ret.put(new Byte(TrucoEvent.QUIERO), "Quiero");
		ret.put(new Byte(TrucoEvent.RETRUCO), "Quiero Retruco");
		ret.put(new Byte(TrucoEvent.VALE_CUATRO), "Quiero Vale 4");
		ret.put(new Byte(TrucoEvent.NO_QUIERO), "No quiero");
		ret.put(new Byte(TrucoEvent.CANTO_ENVIDO), "Cantar puntos");
		ret.put(new Byte(TrucoEvent.PASO_ENVIDO), "Paso envido");
		ret.put(new Byte(TrucoEvent.CERRARSE), "Cerrarse");
		ret.put(new Byte(TrucoEvent.ME_VOY_AL_MAZO), "Me voy al mazo");
		return ret;
    }

	/**
     * Retorna un array con las jugadas en 
     * el orden en que se deben mostrar
     */
	private static byte[] getPlays()
	{
		byte[] ret;
		
		ret = new byte[]
		{
			TrucoPlay.ENVIDO,
			TrucoPlay.REAL_ENVIDO,
			TrucoPlay.FALTA_ENVIDO,
			TrucoPlay.FLOR,
			TrucoPlay.TRUCO,
			TrucoPlay.QUIERO,
			TrucoPlay.RETRUCO,
			TrucoPlay.VALE_CUATRO,
			TrucoPlay.NO_QUIERO,
			TrucoPlay.CANTO_ENVIDO,
			TrucoPlay.PASO_ENVIDO,
			TrucoPlay.CERRARSE,
			TrucoPlay.ME_VOY_AL_MAZO
		};
		
		return ret;
    }

	/**
     * Retorna el nombre de una jugada
     */	
    public static String getPlayName(byte play)
    {
    	return (String) pNames.get(new Byte(play));
    }

	/**
     * Retorna el nombre de un evento
     */	
    public static String getEventName(byte play)
    {
    	return (String) eNames.get(new Byte(play));
    }

	/**
     * Retorna una jugada. 
     * @param index		Indice de la jugada a retornar.
     */    
    public static byte getPlay(int index)
    {
    	return plays[index];
    }
    
    /**
     * Retorna la cantidad de jugadas posibles.
     */
    public static int getPlayCount()
    {
    	return plays.length;
    }

    private static boolean message(String msg, boolean ret)
    {
    	System.out.println(msg);
    	return ret;
    }
    
    
    
    public static void main(String[] args)
    {
		new Object()
		{
			protected void finalize()
			{
				System.out.println("Hola");
			}
		};
		System.gc();
    }
    
    /**
     * Retorna el subdirectorio de imágenes
     */
    public static String getImagesDir()
    {
    	return "E:/Mis documentos/Programas/Java/Toluca/Table/py/edu/uca/fcyt/toluca/images/";
    }

	/**
	 * Retorna un vector de Strings que es un texto separado
	 * en varias líneas de manera que su ancho no sea mayor
	 * que <code>width</code>.
	 * @param text	Texto a analizar
	 * @param width	Tamaño máximo en puntos
	 * @param grOut	contexto de dibujo de la fuente
	 */
	public static String[] getLines
	(
		String text, int width, Graphics2D grOut
	)
	{
		LinkedList lines;
		StringTokenizer strTok;
		FontMetrics fMetrics;
		int lineSize = 0, tokWidth;
		String token, currLine;
		String[] strLines;
		int len;
		
		// crea la lista enlazada de líneas
		lines = new LinkedList();
		
		// obtiene los tókens (palabras) de 'text'
		strTok = new StringTokenizer(text, " ", true);
		
		// obtiene las medidas de la fuente de 'grOut'
		fMetrics = grOut.getFontMetrics();
		
		// inicializa el tamaño actual de la línea como para 
		// que al principio sobrepase el tamaño máximo
		lineSize = 0;
		
		// inicaliza la línea actual
		currLine = new String();
		
		// carga las líneas de texto
		while(strTok.hasMoreElements())
		{
			// obtiene el tóken y su tamaño
			token = (String) strTok.nextElement();
			tokWidth = fMetrics.stringWidth(token);
			
			// si el tamaño del tóken es mayor que 'width'
			while (tokWidth > width)
			{
				len = getNChars(token, 0, width, grOut);

				try {
                    lines.add(token.substring(0, len - 1));
                    token = token.substring(len, token.length() - 1);
                    tokWidth = fMetrics.stringWidth(token);
                } catch (Exception e) {
                    System.out.println("Hubo un error al querer parsear ->>   " + text);
                    token = "";
                    tokWidth = fMetrics.stringWidth(token);
                    break;
                }
			}

			// incrementa el tamaño actual de la línea
			lineSize += tokWidth;
			
			// si ha excedido el tamaño, habilitar una nueva línea
			if (lineSize > width)
			{
				lineSize = tokWidth;
				lines.add(currLine);
				currLine = new String(token);
			}
			// si no, agregar el tóken a la línea
			else currLine = currLine.concat(token);
		}
		
		lines.add(currLine);
		
		// crea el vector de Strings y lo carga
		strLines = new String[lines.size()];
		System.arraycopy(lines.toArray(), 0, strLines, 0, lines.size());

		return strLines;
	}

	/**
	 * Obtiene la cantidad de caracteres de un texto que entran 
	 * en una cierta cantidad de píxeles a lo largo del texto.
	 * @param text	Texto a analizar.
	 * @param start	Posición inicial del análisis.
	 * @param width	Tamaño máximo en píxeles.
	 * @param grOut	Contexto de dibujo del texto
	 */
	public static int getNChars
	(
		String text, int start, int width, Graphics2D grOut
	)
	{
		FontMetrics fMetrics;
		int max, shown, currWidth;
		char[] chars;

		// obtiene las medidas de la fuente actual
		fMetrics = grOut.getFontMetrics();

		// carga el tamaño actual y la cantidad 
		// de caracteres mostrados actualmente
		currWidth = 0;
		shown = 0;

		try
		{
			while(currWidth < width) 
			{
				currWidth += fMetrics.charWidth
				(
					text.charAt(start + shown++)
				);
			}
		}
		catch(IndexOutOfBoundsException ex) {}
		
		shown --;
		
		return shown;
	}

	public static String[] getLines(String text, int charWidth)
	{
		Vector vString;
		String[] strings;
		int lIndex, index;
		
		vString = new Vector();
		index = 0;
		while(true)
		{
			if (charWidth + index > text.length())
				if (index == text.length() + 1)	
					break;
				else 
					lIndex = text.length();
			else 
				lIndex = text.lastIndexOf(' ', charWidth + index - 1);
				
			vString.add(new String(text.substring(index, lIndex)));
			index = lIndex + 1;
			
		}
		strings = new String[vString.size()];
		for (int i = 0; i < strings.length; i++)
			strings[i] = (String) vString.get(i);
			
		return strings;
	}
}