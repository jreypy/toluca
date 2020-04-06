package py.edu.uca.fcyt.toluca.table;
import py.edu.uca.fcyt.game.ChatPanel;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * Muestra las indicaciones en el chat
 * @author Grupo Interfaz de Juego
 */
class IndThread extends Thread
{
	private ChatPanel chat;
	private boolean alive = true;

	// carga las indicaciones
	private static String[] indications = new String[]
	{
		"Haz click en una carita para sentarte.",
		"Sólo el que está en la silla de posición baja " + 
			"(la roja) puede iniciar un juego.",
		"Inicia tus mensajes con \\ si no quieres que " + 
			"salgan en la mesa."
	};

	/**
     * Construye una nueva instancia de IndThread.
     * @param table		Table asociado.
     */
	public IndThread(ChatPanel chat)
	{
		this.chat = chat;
	}
	
	public void run()
	{
	
		Util.wait(this, 1000);
		setPriority(Thread.MIN_PRIORITY);
	        // muestra las indicaciones
	    for (int i = 0; i < indications.length && alive; i++)
	    {
	        chat.showChatMessage
	        (
                    new TrucoPlayer("[ System ]"),
                    indications[i]
	        );
	    	Util.wait(this, indications[i].length() * 50);
		}
	}
	
//	protected void finalize()
//	{
//		System.out.println(this + " finalized");
//	}
	
	/** Detiene la ejecución de este Thread */
	synchronized public void stopThread()
	{
		alive = false;
		notify();
	}
}
