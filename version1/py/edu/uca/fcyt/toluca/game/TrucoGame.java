package py.edu.uca.fcyt.toluca.game;

/*
 * trucoGame.java
 *
 * Created on 3 de marzo de 2003, 10:25 PM
 */
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import py.edu.uca.fcyt.game.Game;
import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;

/** Clase que representa y administra un juego de Truco.
 * @authors Julio Rey || Christian Benitez
 * @version 1.0
 */
public class TrucoGame extends Game
{

	//TODO: Poner estos 2 en un constructor. 
	private int gamePoints = 30;
	private int buenaPoints = gamePoints / 2;
	
	
	private static Logger logger=Logger.getLogger(TrucoGame.class.getName());
	/** Creates a new instance of trucoGame */
	LinkedList listenerlist; //lista de todos los listener
	private int tableNumber;
//    static Logger logger = Logger.getLogger(RoomServer.class);
	/**
	 * @return Returns the tableNumber.
	 */
	public int getTableNumber() {
		return tableNumber;
	}
	/**
	 * @param tableNumber The tableNumber to set.
	 */
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}
	protected int[] points = new int [2]; //puntajes de los teams
	protected TrucoTeam[] teams = new TrucoTeam[2]; //equipos que juegan
	protected TrucoHand trucoHand; //mano actual
	protected int numberOfHand; //numero de mano actual
	protected int numberOfPlayers; //cantidad de jugadores
	protected int numberOfTeams=0; //numero de equipos
	protected int reparteCartas = 0; //quien empieza la mano
	protected boolean playersPreparados[]; //lista de players que estan preparados
	protected int cantidadDePlayersPreparados;
	protected Vector detalleDelPuntaje;
	
	
	
	public TrucoGame()
	{
	}
	/** Constructor con dos equipos, asi crea un TrucoGame
	 * @param tm1 Equipo 1 que jugarï¿½ el TrucoGame.
	 * @param tm2 Equipo 1 que jugarï¿½ el TrucoGame.
	 * 
	 * @deprecated
	 */
	public TrucoGame(TrucoTeam tm1, TrucoTeam tm2)
	{ //contructor con los teams
		this(tm1, tm2, 30);
	}
	
	/** Constructor con dos equipos, asi crea un TrucoGame
	 * @param tm1 Equipo 1 que jugarï¿½ el TrucoGame.
	 * @param tm2 Equipo 1 que jugarï¿½ el TrucoGame.
	 */
	public TrucoGame(TrucoTeam tm1, TrucoTeam tm2, int points)
	{ //contructor con los teams
		
		setGamePoints(points);
		listenerlist = new LinkedList(); //instancia de la lista de Trucolistener
		teams[0] = tm1;
		teams[1] = tm2;
		numberOfHand = 0;
		newGame();
	}
	
	/** Dispara el inicio del juego
	 */
	public void startGame()
	{
		numberOfPlayers = teams[0].getNumberOfPlayers()*2;
		playersPreparados = new boolean[numberOfPlayers];
		newGame();
		fireGameStarted();
		for (int i=0; i<numberOfPlayers; i++)
			playersPreparados[i] = true;
		startHandConfirmated();
	}
	/** nuevo Juego
	 */
	public void newGame()
	{ //juevo juego
		points[0] = 0; //cerando el puntaje
		points[1] = 0;
	}
	/** adherir un nuevo TrucoListener al juego.
	 * @param tl Oyente que se adhiere a la lista.
	 */
	public void addTrucoListener(TrucoListener tl)
	{
		listenerlist.add(tl);
		System.out.println("Se agrega un truco listener al trucogame ");
	//	new Throwable().printStackTrace();
		
	}
	/** configurar los equipos que jugarï¿½n el TrucoGame.
	 * @param team_1 Equipo 1 que jugarï¿½ el TrucoGame.
	 * @param team_2 Equipo dos que jugarï¿½ el TrucoGame
	 */
	public void setTeam(TrucoTeam team_1, TrucoTeam team_2)
	{//insertar los teams que partciparan del juego de truco
		teams[0] = team_1;
		teams[1] = team_2;
	}
	/** Envia las cartas a los jugadores.
	 * @param tp TrucoPlayer a quien irï¿½ las cartas.
	 * @param card carta a ser enviadas.
	 */
	public void dealtCards(TrucoPlayer tp, TrucoCard[] card)
	{//reparte las cartas a los jugadores
		TrucoEvent event = new TrucoEvent(this,numberOfHand,tp,(byte)0,card);
		event.setTableNumber(getTableNumber());
		for (int i=0; i<listenerlist.size(); i++)
		{
//			System.out.println(i+"ejecutando cardsDEal de:" + getClass().getName());
			((TrucoListener)listenerlist.get(i)).cardsDeal(event);
		}
	}
	/** Retorna el Equipo que es Numero i.
	 * @param i numero de Team (o 0 o 1)
	 * @return Retorna un Equipo(TrucoTeam).
	 */
	
	public TrucoTeam getTeam(int i)
	{ //retorna el team numero i
		if (i == 0 || i == 1)
			return teams[i];
		return null;
	}
	/** Retorna el puntaje Total de uno de los Teams.
	 * @param tm TrucoTeam de quien se retorna el Puntaje.
	 * @return El valor del puntaje de un TrucoTeam.
	 * @throws InvalidPlayExcepcion tira en caso de que el TrucoTeam no participe en el TrucoGame.
	 */
	public int getGameTotalPoints(TrucoTeam tm) throws InvalidPlayExcepcion
	{ //el puntaje del team tm
		if (teams[0] == tm)
			return points[0];
		if (teams[1] == tm)
			return points[1];
		throw (new InvalidPlayExcepcion("getGameTotalPoints() - fuera del dominio de la funcion"));
		//   return 0;
	}
	/** configurar el puntaje del equipo
	 * @param tm Team a configurar su puntaje.
	 * @param pts Puntaje a ser configurado.
	 */
	public void setGameTotalPoints(TrucoTeam tm, int pts)
	{ //inserta puntaje
		if (teams[0] == tm)
		{
			points[0] = pts;
			return;
		}
		if (teams[1] == tm)
		{
			points[1] = pts;
			return;
		}
	}
	/** Metodo para realizar una jugada TrucoPlay.
	 * @param tp Jugada a ser realizada (TrucoPlay).
	 * @throws InvalidPlayExcepcion Excepcion en caso de dectarse que no es posible hacer esa jugada.
	 */
	
	synchronized public void play(TrucoPlay tp)
	throws InvalidPlayExcepcion
	{ //play trucoGame
		
		if (teams[0] == null || teams[1] == null)
			throw (new InvalidPlayExcepcion("Teams not found in Trucogame"));
		/*try
		{*/
			trucoHand.play(tp);
			//firePlayToOtherClients(tp);
			
/*		} //Comente esta parte xq si el metodo hace tiene "throws" no hace falta el try,catch
		catch (InvalidPlayExcepcion e)
		{
			e.printStackTrace(System.out);
			throw e;
		}*/
	}
	/** Retorna Verdadero si posible realizar una jugada.
	 * @param tp Jugada a verificar si es posible ser realizada.
	 * @return Retorna si es vï¿½lido la jugada.
	 */
	public boolean esPosibleJugar(TrucoPlay tp)
	{
		return trucoHand.esPosibleJugar(tp);
	}
	/** Enviar mensajes a todos los oyentes del cambio de turno.
	 * @param pl TrucoPlayer a quien se le asigna el turno.
	 * @param type Tipo de Turno a ser asignado.
	 */
	public void fireTurnEvent(TrucoPlayer pl,byte type)
	{ //avisar quien juega con type el tipo de turno, ronda de cartas, ronda de envidos o flores etc
		
		TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type); //crear el evento
		event.setTableNumber(getTableNumber());
		for (int i=0; i<listenerlist.size(); i++)
		{ //enviarle a todos el evento
			((TrucoListener)(listenerlist.get(i))).turn(event);
		}
	}
	public void fireTurnEvent(TrucoPlayer pl,byte type, int value)
	{ //avisar el Turno con envio del value of envido
		TrucoEvent event1 = new TrucoEvent(this, numberOfHand, pl, type, value ); //crear el evento1
		TrucoEvent event2 = new TrucoEvent(this, numberOfHand, pl, type); //crear el evento2
		event1.setTableNumber(getTableNumber());
		event2.setTableNumber(getTableNumber());
		for (int i=0; i<listenerlist.size(); i++)
		{
			if (((TrucoListener)listenerlist.get(i)).getAssociatedPlayer()==pl)
				((TrucoListener)(listenerlist.get(i))).turn(event1);
			else
				((TrucoListener)(listenerlist.get(i))).turn(event2);
		}
	}
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl TrucoPlayer que realizo la jugada.
	 * @param type Tipo de Jugada que realizï¿½.
	 */
	public void firePlayEvent(TrucoPlayer pl, byte type)
	{ //eventos de juego sin carta o canto
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl,type);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			//			((TrucoListener)(listenerlist.get(i))).play(event);
			// 			Se cambiï¿½ la llamada en intento desesperado por hacer funcionar esto
			//			((TrucoListener)(listenerlist.get(i))).playResponse(event);
			((TrucoListener)(listenerlist.get(i))).play(event);
		}
	}
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl TrucoPlayer que realizï¿½ la jugada.
	 * @param card Carta que jugï¿½ el Player.
	 * @param type Tipo de jugada que realizï¿½.
	 */
	public  void firePlayEvent(TrucoPlayer pl, TrucoCard card,byte type)
	{ //eventos de juego con carta
		System.out.println("se envia el mensaje de PlayEvent");
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl,type,card);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			System.out.println("firePlayEvent para: " + listenerlist.get(i).getClass().getName());
			((TrucoListener)(listenerlist.get(i))).play(event);
			//		((TrucoListener)(listenerlist.get(i))).playResponse(event);
		}
	}
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl Player que realizï¿½ la jugada.
	 * @param type Tipo de jugada que realizï¿½.
	 * @param value Valor del canto (para jugadas de canto de valor Envido o Flor).
	 */
	public void firePlayEvent(TrucoPlayer pl, byte type, int value)
	{//eventos de canto de tanto
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl, type, value);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).play(event);
			//			((TrucoListener)(listenerlist.get(i))).playResponse(event);
		}
	}
	
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl TrucoPlayer que realizo la jugada.
	 * @param type Tipo de Jugada que realizï¿½.
	 */
	public void firePlayResponseEvent(TrucoPlayer pl, byte type)
	{ //eventos de juego sin carta o canto
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl,type);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			//			((TrucoListener)(listenerlist.get(i))).play(event);
			//			Se cambiï¿½ la llamada en intento desesperado por hacer funcionar esto
			((TrucoListener)(listenerlist.get(i))).playResponse(event);
			//			((TrucoListener)(listenerlist.get(i))).play(event);
		}
	}
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl TrucoPlayer que realizï¿½ la jugada.
	 * @param card Carta que jugï¿½ el Player.
	 * @param type Tipo de jugada que realizï¿½.
	 */
	public  void firePlayResponseEvent(TrucoPlayer pl, TrucoCard card,byte type)
	{ //eventos de juego con carta
		//System.out.println("se envia el mensaje de PlayEvent");
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl,type,card);
		event.setTableNumber(getTableNumber());
		System.out.println("Se va a disparar un evento de play response.  El tamaño de la lista de listeners es: " + listenerlist.size());
		for(int i=0; i<listenerlist.size();i++)
		{
			System.out.println("FirePlayEvent para: " + listenerlist.get(i).getClass().getName());
			//		((TrucoListener)(listenerlist.get(i))).play(event);
			((TrucoListener)(listenerlist.get(i))).playResponse(event);
		}
	}
	/** Enviar mensaje de jugada a todos los oyentes del juego.
	 * @param pl Player que realizï¿½ la jugada.
	 * @param type Tipo de jugada que realizï¿½.
	 * @param value Valor del canto (para jugadas de canto de valor Envido o Flor).
	 */
	public void firePlayResponseEvent(TrucoPlayer pl, byte type, int value)
	{//eventos de canto de tanto
		TrucoEvent event = new TrucoEvent(this,numberOfHand,pl, type, value);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			//			((TrucoListener)(listenerlist.get(i))).play(event);
			((TrucoListener)(listenerlist.get(i))).playResponse(event);
		}
	}
	
	
	/*public void fireEventType(byte type){
		TrucoEvent event = new TrucoEvent(this,numberOfHand,type);
	}*/
	/** 
	 * Enviar mensaje a todos los oyentes sobre el final de la mano
	 */
	public void fireEndOfHandEvent()
	{
		points[0] = points[0] + trucoHand.getPointsOfTeam(0);
		points[1] = points[1] + trucoHand.getPointsOfTeam(1);
		teams[0].setPoints(points[0]);
		teams[1].setPoints(points[1]);
		TrucoEvent event = new TrucoEvent(this,numberOfHand,TrucoEvent.FIN_DE_MANO);
		event.setTableNumber(getTableNumber());
		detalleDelPuntaje = trucoHand.getPointsDetail();
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).endOfHand(event);
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("--------------------------------------Puntajes--------------------------");
		for (int i=0; i<2; i++)
			System.out.println(teams[i].getName()+" :"+teams[i].getPoints()+"puntos.");
		
		for (int i=0; i<detalleDelPuntaje.size(); i++)
			System.out.println(((PointsDetail)detalleDelPuntaje.get(i)).aString());
		
	}
	public void EndOfHandEvent()
	{
	    logger.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"End of hand shaque termina la mano");
	    
	    /*
	     * Esto fue agregado por Cricco. Se pretende controlar si termina el juego que salgan nomas
	     * */
	    if(!(this instanceof TrucoGameClient) && (points[0] >= this.gamePoints || points[1] >= this.gamePoints) )
		{
	        System.out.println("se teeeermin el jueeeego");
	        logger.log(TolucaConstants.CLIENT_INFO_LOG_LEVEL,"Se teeermina el jueeeeego");
			fireEndOfGameEvent();
		}
	    else
	    {
		newHand();
	    }
	    //newHand();
	}
	/** Enviar mensaje a todos los oyentes sobre el final del juego.
	 */
	public void fireEndOfGameEvent()
	{
		TrucoEvent event = new TrucoEvent(this,numberOfHand,TrucoEvent.FIN_DE_JUEGO);
		event.setTableNumber(getTableNumber());
//		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).endOfGame(event);
		}
	}
	
	/** Enviar las cartas a cada jugador.
	 *
	 */
	public void fireCardsDealt()
	{
		TrucoEvent event = new TrucoEvent(this,TrucoEvent.ENVIAR_CARTAS);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).play(event);
		}
	}
	/** Enviar mensaje a todos los oyentes sobre el el comienzo de la mano.
	 */
	public void fireHandStarted()
	{
		System.out.println(" fireHandStarted "+ getNumberOfHand());
		System.out.println(" el equipo es: " + teams[0]);
		System.out.println(teams[1]==null);
		System.out.println(" numero de players de los equipos = "+teams[0].getNumberOfPlayers()+"y"+teams[0].getNumberOfPlayers());
		TrucoPlayer tp = teams[(numberOfHand+1)%2].getTrucoPlayerNumber((numberOfHand-1)%numberOfPlayers/2);
		TrucoEvent event = new TrucoEvent(this,numberOfHand,tp,TrucoEvent.INICIO_DE_MANO);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).handStarted(event);
		}
	}
	/** Enviar mensaje a todos los oyentes sobre el el comienzo del juego.
	 */
	public void fireGameStarted()
	{
		TrucoEvent event = new TrucoEvent(this,numberOfHand,TrucoEvent.INICIO_DE_JUEGO);
		event.setTableNumber(getTableNumber());
		for(int i=0; i<listenerlist.size();i++)
		{
			((TrucoListener)(listenerlist.get(i))).gameStarted(event);
		}
	}
	/** Para dejarse en espera a que inicie la siguiente mano.
	 * @param tPlayer Player que esta preparado.
	 * @ Una vez que todos los TrucoPlayers hallan preparado, empieza la siguiente mano.
	 */
	public void startHand(TrucoPlayer tPlayer)
	{
		int i; //
		System.out.println("cantidad de players preparados="+cantidadDePlayersPreparados);
		int numOfPlayer = teams[0].getNumberOfPlayer(tPlayer)*2;
		if (numOfPlayer >= 0)
		{
			if (playersPreparados[numOfPlayer]==false)
			{
				playersPreparados[numOfPlayer] = true;
				firePlayEvent(tPlayer,TrucoEvent.PLAYER_CONFIRMADO);
				cantidadDePlayersPreparados++;
				System.out.println(tPlayer.getName()+"confirmado");
			}
			else
				return;
		}
		numOfPlayer = teams[1].getNumberOfPlayer(tPlayer)*2+1;
		if (numOfPlayer>=0)
		{
			if (playersPreparados[numOfPlayer]==false)
			{
				playersPreparados[numOfPlayer] = true;
				firePlayEvent(tPlayer,TrucoEvent.PLAYER_CONFIRMADO);
				cantidadDePlayersPreparados++;
				System.out.println(tPlayer.getName()+"confirmado");
			}
			else
				return;
		}
		if (cantidadDePlayersPreparados == numberOfPlayers)
		{
			cantidadDePlayersPreparados=0;
			startHandConfirmated();
		}
	}
	protected void startHandConfirmated()
	{
		System.out.println("enviando starthand a todos!!");
		if(points[0] >= this.gamePoints || points[1] >= this.gamePoints)
		{
			fireEndOfGameEvent();
		}
		else
		{
			numberOfHand++;
			fireHandStarted();/*para que se preparen los jugadores*/
			trucoHand = new TrucoHand(this, numberOfHand-1); /*se crea un truco hand y guardo la referencia*/
			trucoHand.startHand();
		}
	}
	protected void newHand()
	{ //nueva mano
		
		if (teams[0].getNumberOfPlayers() != teams[1].getNumberOfPlayers())
			throw (new InvalidPlayExcepcion("TrucoGame.newHand - la cantidad de players de los Teams son distintos"));
		for (int i=0; i<numberOfPlayers; i++)/*desbloquear que reinicien la mano*/
			playersPreparados[i]= false;
		
		//reparteCartas=(++reparteCartas)%getNumberOfPlayers();/*incrementa el que reparte las cartas*/
		///System.out.println("despues va repartir las cartas" + reparteCartas + "numero de players" + getNumberOfPlayers());
		
	}
	/** Retorna el numero de jugadores del TrucoGame.
	 * @return Cantidad de Jugadores.
	 */
	public int getNumberOfPlayers()
	{
		return (numberOfPlayers);
	}
	/** Retorna el numero de mano actual del TrucoGame.
	 * @return El numero de mano actual del TrucoGame.
	 */
	public int getNumberOfHand()
	{
		return numberOfHand;
	}
	/** Retorna cuantos puntos se juegan en caso de faltear.
	 * @return retorna el valor del puntaje
	 */
	
	public int getFaltear()
	{
		int i=0;
		if(points[1] > points[0])
			i=1;
		if(points[i]>= this.buenaPoints)
			return (this.gamePoints - points[i]);
		return (this.buenaPoints - points[i]);
	}
	/** Retorna la cantidad de puntos que se juegan, en caso de jugar Al Resto.
	 * @return Retorna la cantidad de puntos.
	 */
	public int alResto()
	{
		if (points[0]>points[1])
			return (this.gamePoints - points[0]);
		return (this.gamePoints - points[1]);
	}
	public Vector getDetallesDeLaMano()
	{
		return detalleDelPuntaje;
	}
	/*obtener el mejor envido que puede cantar el player*/
	/** Retorna el Valor del Envido que puede cantar un Player.
	 * @param tp TrucoPlayer a ser retornado su valor de Envido.
	 * @return el valor del envido del TrucoPlayer.
	 *
	 * public int getValueOfEnvido(TrucoPlayer tp) {
	 *
	 * return trucoHand.getValueOfEnvido(tp);
	 *
	 *
	 *
	 * }*/
	public TrucoCard getCard(byte myKind, byte myValue)
	{
		TrucoCard myCarta =  trucoHand.getCard(myKind,  myValue);
		if (myCarta == null)
		{
			System.out.println("en TrucoGame getCard devuelve null");
		}
		return myCarta;
	}
	/**
	 * @param tptmp
	 */
	public void removeTrucoListener(TrucoPlayer tptmp) {
		try {
			System.out.println("Se elimina un truco listener del trucogame, vamos a eliminar a:" + tptmp.getName());
			for(int i=0; i<listenerlist.size();i++)
			{
				TrucoListener comm = ((TrucoListener)(listenerlist.get(i)));
				if (comm.getAssociatedPlayer().getName() == tptmp.getName()) {
					listenerlist.remove(i);
					System.out.println("Eliminamos el listener del player " + comm.getAssociatedPlayer().getName());
				}
	
			}
		} catch (NullPointerException npe) {
			System.out.println("No se pudo eliminar el listener " + tptmp.getName() );
		}
		
	}
	public LinkedList getListaListeners() {
		return listenerlist;
	}
	
	public TrucoTeam[] getTeams() {
		return teams;
	}
	/**
	 * 
	 */
	public int getTeamGanador() {
		//TODO: Ver que el partido termine cuando alguno de los dos llega a this.gameTotalPoints
		return points[0] > points[1] ? 0 : 1;
		
	}
	/**
	 * Permite forzar para que un team tenga puntos de ganador
	 * */
	public void setTeamGanador(int team)
	{
	    if(team==0)
	        points[0]=points[1]+1;
	    else
	        points[1]=points[0]+1;
	}
	
    public int[] getPoints() {
        return points;
    }
    public void setPoints(int[] points) {
        this.points = points;
    }
	public int getBuenaPoints() {
		return buenaPoints;
	}
	public void setBuenaPoints(int buenaPoints) {
		this.buenaPoints = buenaPoints;
	}
	public int getGamePoints() {
		return gamePoints;
	}
	public void setGamePoints(int gamePoints) {
		this.gamePoints = gamePoints;
		setBuenaPoints( gamePoints / 2);
	}
}
