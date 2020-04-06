package py.edu.uca.fcyt.toluca.table;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Vector;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.table.animation.Animable;
import py.edu.uca.fcyt.toluca.table.animation.Animator;

/**
 * Maneja a las caritas en el juego
 */
class FaceManager implements Animable
{
	private Animator animator;
	private Vector faces;			// caritas
	private final FaceState[] pts;	// posiciones de cada carita
	private Vector toRemove;		// vector de caritas a borrar
	private BufferedImage[] biOut;
	private AffineTransform afTrans;
	
	/**
     * Construye un FaceManager con un 
     * PlayTable 'pTable' asociado
     */
	public FaceManager()
	{
		int w, h, offX, offY;
		
		// crea el vector de caritas
		faces = new Vector();

		// crea el vector de caritas a remover
		toRemove = new Vector();
		
		w = (PlayTable.TABLE_WIDTH - Face.WIDTH - 16) / 2 + 17;
		h = (PlayTable.TABLE_HEIGHT - Face.HEIGHT - 16) / 2 + 17;

		// carga las posiciones de cada carita
		pts = new FaceState[]
		{
			new FaceState(0, h, 0, -12, 0, 1),
			new FaceState(w, h, 0, -12, 0, 1),
			new FaceState(w, 0, 0, 0, 0, 1),
			new FaceState(w, -h, 0, 0, 0, 1),
			new FaceState(0, -h, 0, 0, 0, 1),
			new FaceState(-w, -h, 0, 0, 0, 1),
			new FaceState(-w, 0, 0, 0, 0, 1),
			new FaceState(-w, h, 0, -12, 0, 1)
		};
	}
	
	/** 
	 * Agrega caritas si nombre a la mesa 
	 * @param n		cantidad de caritas a agregar
	 */
	synchronized public void addUnnamedFaces(int n)
	{
		Face face;
        
        // agrega las caritas a 'pTable'
        for (int i = 0; i < n; i++)
        {
        	face = new Face
        	(
        		"", 
        		getChairColor(i),
        		Math.PI * (2.0 / n * i + 1)
        	);
            faces.add(face);
        }
	}

	/** 
	 * Agrega las caritas con los nombres de los jugadores
	 * contenidos en pManager en el orden correspondiente
	 */
	synchronized public void addFaces
	(
		PlayerManager pManager, boolean putChairZero
	)
	{
        TrucoPlayer player;
        int chair;
        Face face;
        int ini;
        
        ini = putChairZero ? 0 : 1;

		// agrega las caritas
        for (int i = ini; i < pManager.getPlayerCount(); i++) 
        {
        	// obtiene la i-ésima silla y el TrucoPlayer sentado en ella
        	chair = pManager.getChair(i);
            player = pManager.getPlayer(chair);
            
            // crea una nueva carita con el nombre del jugador 
            // y el color de equipo correspondiente
            face = new Face
            (
            	player.getName(), 
            	getChairColor(chair),
            	Math.PI * (2.0 / pManager.getPlayerCount() * i + 1)
            );
            
            //face.setFacesDir(Util.getImagesDir() + "/faces/standard/");
            face.setOut(biOut, afTrans);
            
	    	face.loadFacesFromURL
	    	(
				"/py/edu/uca/fcyt/toluca/images/faces/standard/"
			);

        	// agrega la carita 
            faces.add(face);
        }
    }
    
	/** 
	 * Retorna el color de una silla 
	 */
    private Color getChairColor(int index)
    {
    	if (index % 2 == 0) return Color.RED;
    	else return Color.BLUE;
    }
    
    /**
     * Establece atributos de resaltado de todas las 
     * caritas a falso exepto la que está en <code>index</code>
     * @param index		Índice de la carita a resaltar, si es
     *					-1 no resalta ninguna carita
     */
    public void setHighlight(int index)
    {
    	// verificaciones
    	Util.verifParam
    	(
    		index > -2 || index < faces.size(),
    		"Parámetro 'index' inválido: " + index
    	);
    	
    	for (int i = 0, j = 0; i < faces.size() + 1; i++)
    	{
    		try
    		{
    			getFace(i).highlighted = false;
    		}
    		catch(ArrayIndexOutOfBoundsException ex)
    		{
    		}
//    		System.out.println("Face " + i + " not highlighted");
    	}
    	
		try
		{
			getFace(index).highlighted = true;
//    			System.out.println("Face " + index + " highlighted");
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
		}
    }
    
    /**
     * Retorna una posición si fue 
     * clickeada, si no retorna -1
     */
    synchronized public int getPosIfClick(int x, int y) 
    {
        Face face;
        
        // busca la carita clickeada
        for (int i = 0; i < faces.size(); i++) 
        {
        	face = (Face) faces.get(i);
            if (face.inside(x, y)) return i;
		}

        return -1;
    }
    
    /**
     * Muestra la seña
     */
    synchronized public void showSign(int pos, int sign)
    {
    	Face face;
    	
    	// obtiene la carita en la posición 'pos'
    	face = (Face) faces.get(pos);
    	
    	// establece la seña de la carita 
    	face.pushSign(sign, 250);

    	// restaura la carita
    	face.pushSign(Sign.NONE, 100);
    }
    
    /**
     * Retorna una carita
     * @param index 	índice de la carita
     */
    synchronized public Face getFace(int index)
    {
    	return (Face) faces.get(index - faces.size() % 2);
    }
    
    /**
     * Remueve todas las caritas
     */
    synchronized public void removeFaces()
    {
    	Iterator fIter;
    	
		fIter = faces.iterator();
		while (fIter.hasNext())
		{
			toRemove.add(fIter.next());
			fIter.remove();
		}
    }

	synchronized public void paint(int buffIndex) 
	{
		for (int i = 0; i < faces.size(); i++)
			((Face) faces.get(i)).paint(buffIndex);

		for (int i = 0; i < toRemove.size(); i++)
			((Face) toRemove.get(i)).paint(buffIndex);

		removePending();
	}

	/**
     * Remueve las caritas marcadas para eliminación
     */
	synchronized private void removePending()
	{
		Iterator fIter;
		Face face;
		
		fIter = toRemove.iterator();
		while (fIter.hasNext())
		{
			face = (Face) fIter.next();
			if (face.getRemainingTime() == 0) 
				fIter.remove();
		}
	}

	synchronized public void advance() 
	{
		for (int i = 0; i < faces.size(); i++)
			((Face) faces.get(i)).advance();
		
		for (int i = 0; i < toRemove.size(); i++)
			((Face) toRemove.get(i)).advance();
	}
	
	/**
     * Retorna las estados estándar de cada carita
     * @param cant	cantidad de caritas
     */
	synchronized private FaceState[] getLocations(int cant)
	{
		FaceState[] bStates;
		
		bStates = new FaceState[cant];
	
		switch(cant)
		{
			case 2:
				bStates[0] = pts[0];
				bStates[1] = pts[4];
				break;		
			case 4:
				bStates[0] = pts[0];
				bStates[1] = pts[2];
				bStates[2] = pts[4];
				bStates[3] = pts[6];
				break;
			case 6:
				bStates[0] = pts[0];
				bStates[1] = pts[1];
				bStates[2] = pts[3];
				bStates[3] = pts[4];
				bStates[4] = pts[5];
				bStates[5] = pts[7];
				break;
			default:
				throw new InvalidParameterException
				(
					"Parámetro 'cant' = " + cant + " inválido"
				);
		}	
		
		return bStates;
	}
	
    /**
     * Agrega una pausa a todas las caritas de un vector de tal 
     * manera que todas la culminen dentro de una cierta cantidad 
     * específica de tiempo. 
     * Si existen caritas del vector que tienen un tiempo restante
     * mayor que el tiempo especificado, éstas son omitidas,
     * terminando sus transiciones después que las demás caritas. 
     * Las caritas que tengan un tiempo menor adquieren una pausa 
     * igual a <b>time</b> - tiempo restante.
     * @param time		cantidad de tiempo dentro de la cual
     *					los objetos deben estar transicionando
     *					o en pausa
     */
	synchronized public void pushGeneralPause(long time)
	{
		Face face;
		
    	// agrega a cada carita la diferencia
    	for (int i = 0; i < faces.size(); i++)
    	{
			face = (Face) faces.get(i);
    		if (!toRemove.contains(face))
				face.pushPause(time - face.getRemainingTime());
    	}
    }

	/**
     * Esconde las caritas
     */
	synchronized public void hideFaces()
	{
    	Face face;
    	FaceState fState;
    	FaceState[] fStates;
    	int delta, toHide;
    	
    	toHide = faces.size();
		delta = (toHide / 2f == toHide / 2) ? 0 : 1;
    	
		// obtiene las posiciones para las caritas
        fStates = getLocations(toHide + delta);
//        System.out.println("aasdas: " + (toHide + delta));
    	
    	for (int i = 0; i < faces.size(); i++)
    	{
    		face = (Face) faces.get(i);
			fState = new FaceState(fStates[delta++]);
			fState.x *= 1.5;
			fState.y *= 1.5;
			face.pushState(fState, 500);
    	}
	}
	
	/**
     * Muestra las caritas
     */
	synchronized public void showFaces()
	{
		FaceState[] fStates;
		Face face;
		int delta, toShow;
		
		toShow = faces.size();
		delta = (toShow / 2f == toShow / 2) ? 0 : 1;
		
		// obtiene las posiciones para las caritas
        fStates = getLocations(toShow + delta);

        // agrega las caritas a 'pTable'
        for (int i = 0; i < faces.size(); i++) try 
        {
        	face = (Face) faces.get(i);
        	face.pushState
        	(
        		new FaceState(fStates[delta++]), 500
        	);
        }
        catch (NullPointerException ex)
        {
        	System.out.println((Face) faces.get(i));
        	System.exit(1);
        }
	}
	
    /**
     * Obtiene el tiempo que falta para que todas las
     * caritas terminen de transicionar.
     */
    synchronized public long getRemaninigTime()
    {
    	long actTime, maxTime = 0;
    	
    	// obtiene el tiempo máximo
    	for (int i = 0; i < faces.size(); i++)
    	{
    		actTime = ((Face) faces.get(i)).getRemainingTime();
    		if (actTime > maxTime) maxTime = actTime;
    	}
    	
    	return maxTime;
    }		
    
    /**
     * Hace que una cierta carita diga algo
     * @param pos	posición de la carita
     * @param text	lo que va a decir
     */
    synchronized public void pushText
    (
    	int pos, String text, boolean highlighted
    )
    {
    	Face face;

		// limita el texto
		try { text = text.substring(0, 70); } 
		catch(IndexOutOfBoundsException ex) {}
    	
    	pos -= faces.size() % 2;
    	if (pos < 0) return;
    	
    	face = (Face) faces.get(pos);
    	face.pushText
    	(
    		text, highlighted, 700 + (long) text.length() * 50
    	);
    	face.pushText(null, false, 100);
    }
	/* (non-Javadoc)
	 * @see py.edu.uca.fcyt.toluca.table.animation.Animable#setOut(java.awt.image.BufferedImage, java.awt.geom.AffineTransform)
	 */
	public void setOut(BufferedImage[] biOut, AffineTransform afTrans)
	{
		this.biOut = biOut;
		this.afTrans = afTrans;
		for (int i = 0; i < faces.size(); i++)
			((Face) faces.get(i)).setOut(biOut, afTrans);
	}

}
