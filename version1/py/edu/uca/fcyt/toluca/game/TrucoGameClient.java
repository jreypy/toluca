/*
 * TrucoClient.java
 *
 * Created on 5 de junio de 2003, 02:47 PM
 */

package py.edu.uca.fcyt.toluca.game;
import java.util.logging.Logger;

import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;

/**
 *
 * @author  Julio Rey
 */
public class TrucoGameClient extends TrucoGame
/*implements TrucoListener */ {
    protected Logger logeador = Logger.getLogger(TrucoGameClient.class.getName());
    
	/** Creates a new instance of TrucoClient */
	protected TrucoHandClient trucoHandCli;
	
	/**
	 * @deprecated
	 * 
	 * @param t1
	 * @param t2
	 */
	public TrucoGameClient(TrucoTeam t1, TrucoTeam t2)
	{
		this(t1, t2, 30);
//		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Se crea un nuevo truco Game Client");
		
	}
	
	public TrucoGameClient(TrucoTeam t1, TrucoTeam t2, int points)
	{
		super(t1, t2, points);
//		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Se crea un nuevo truco Game Client");
		
	}
	
	public void startGameClient()
	{
		numberOfPlayers = teams[0].getNumberOfPlayers()*2;
		playersPreparados = new boolean[numberOfPlayers];
		newGame();
		fireGameStarted(); //Ahora ya no hace falta avisar? Si hace falta, xq sino el Table no habilita el boton Ok
		//porque Table inicia el juego y se crea en el serva el TrucoGame (no estoy seguro. Ale)
		for (int i=0; i<numberOfPlayers; i++)
			playersPreparados[i] = true;
		//TODO Aca comentamos el startHandConfirmatedClient para probar porque no se deben disparar los eventos autom�ticamente
		//startHandConfirmatedClient();//fletes
	}
	/** Enviar mensaje a todos los oyentes sobre el final del juego.
	 *
	 */
	public void recibirCartas(TrucoPlayer tp,TrucoCard cards[])
	{
		trucoHandCli.recibirCartas(tp,cards);
		dealtCards(tp,cards);
		if (trucoHandCli.isRunHand())
			trucoHandCli.startHand();
	}
	public boolean esPosibleJugar(TrucoPlay tp)
	{
		return trucoHandCli.esPosibleJugar(tp);
	}
	public void play(TrucoEvent event)
	{
		switch(event.getTypeEvent())
		{
			case TrucoEvent.ENVIAR_CARTAS:
				recibirCartas(event.getPlayer(),event.getCards());
				//TODO aca parece que se tiene que llamar a recibir cartas
				cardsDeal(event);
				break;
			case TrucoEvent.PLAYER_CONFIRMADO:
				startHand(event.getPlayer());
				break;
			case TrucoEvent.INICIO_DE_JUEGO:
				//startHandConfirmatedClient();
				break;
			case TrucoEvent.INICIO_DE_MANO:
				startHandConfirmatedClient();
				break;
			case TrucoEvent.JUGAR_CARTA:
				break;
			case TrucoEvent.FIN_DE_JUEGO:
				super.fireEndOfGameEvent();
				break;
				
			default:
			  /*  TrucoPlay tp = event.toTrucoPlay();
				logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Se recibio un TrucoEvent para ser TrucoPlay"+ tp.getType());
				if (tp.getType()>=0){
					play(tp);
					playResponse(tp);
				}*/
				break;
		}
	}
	
	public void turn(TrucoEvent event)
	{
		//new Exception("Nada implementado aun :-(   ").printStackTrace(System.out);
		//TODO ACA TOCO PARA ASIGNAR EL TURNO, VERIFICAR ESTO.
		//trucoHandCli.startHand();
		
		fireTurnEvent(event.getPlayer(), TrucoEvent.JUGAR_CARTA);
	}
	public void endOfHand(TrucoEvent event)
	{
		new Exception("Nada implementado aun :-(   ").printStackTrace(System.out);
	}
	public void cardsDeal(TrucoEvent event)
	{
		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "event.getTypeEvent(): " + event.getTypeEvent());
		new Exception("En cardsDeal. Ac� se tendr�an que recibir las cartas").printStackTrace(System.out);
	}
	public void handStarted(TrucoEvent event)
	{
		new Exception("Nada implementado aun :-(   ").printStackTrace(System.out);
	}
	public void gameStarted(TrucoEvent event)
	{
		new Exception("Nada implementado aun :-(   ").printStackTrace(System.out);
	}
	public void endOfGame(TrucoEvent event)
	{
		new Exception("Nada implementado aun :-(   ").printStackTrace(System.out);
	}
	public TrucoPlayer getAssociatedPlayer()
	{
		return null;
	}
	synchronized public void play(TrucoPlay tp) throws InvalidPlayExcepcion
	{
		try
		{
			trucoHandCli.play(tp);
		}
		catch(InvalidPlayExcepcion e)
		{
			throw e;
		}
	}
	public void startHand(TrucoPlayer tPlayer)
	{
//		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "startHAND !!!!!!! EN trucoGame Cliente");
		firePlayEvent(tPlayer,TrucoEvent.PLAYER_CONFIRMADO);
	}
	public void startHandClient(TrucoPlayer tPlayer)
	{
		int i; //
//		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "cantidad de players preparados="+cantidadDePlayersPreparados);
		int numOfPlayer = teams[0].getNumberOfPlayer(tPlayer)*2;
		if (numOfPlayer >= 0)
		{
			if (playersPreparados[numOfPlayer]==false)
			{
				playersPreparados[numOfPlayer] = true;
				firePlayEvent(tPlayer,TrucoEvent.PLAYER_CONFIRMADO);
				cantidadDePlayersPreparados++;
//				logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, tPlayer.getName()+"confirmado");
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
//				logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, tPlayer.getName()+"confirmado");
			}
			else
				return;
		}
		if (cantidadDePlayersPreparados == numberOfPlayers)
		{
			cantidadDePlayersPreparados=0;
			startHandConfirmatedClient();
		}
	}
	private void startHandConfirmatedClient()
	{
		
		if(points[0] >= getGamePoints() || points[1] >= getGamePoints())
		{
			fireEndOfGameEvent();
		}
		else
		{
			numberOfHand++;
			fireHandStarted();/*para que se preparen los jugadores*/
			trucoHandCli = new TrucoHandClient(this, numberOfHand-1); /*se crea un truco hand y guardo la referencia*/
			trucoHand = trucoHandCli;		// Truco Jul�stico para evitar el STRESS. Muy feo, pero cumple el objetivo: EVITAR STRESS
			//TODO comentamos el starthand porque tiene que venir por que tiene que venir en un evento
		}
	}
	public TrucoCard getCard(byte myKind, byte myValue)
	{
		TrucoCard myCarta =  trucoHandCli.getCard(myKind,  myValue);
		if (myCarta == null)
		{
			logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "en TrucoGame getCard devuelve null");
		}
		return myCarta;
	}
	/**
	 *
	 */
/*	public void playResponse(TrucoPlay tp) {
			TrucoEvent te = tp.toTrucoEvent();
			logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "se hizo truco event="+te.getTypeEvent());
			if (tp.getCard())
			if (te.getCard() == null)
				logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "carta null en playResponse");
			if (te.getCard() != getCard((byte)te.getCard().getKind(),(byte)te.getCard().getValue()))
				logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "carta no tg en mazo");;
 
			firePlayResponseEvent(te.getPlayer(), te.getCard(), te.getTypeEvent());
	}*/
	public void playResponse(TrucoPlayer tp, TrucoCard tc)
	{
		firePlayResponseEvent(tp, tc, TrucoEvent.JUGAR_CARTA);
	}
	public void playResponse(TrucoPlayer tp, byte type)
	{
		firePlayResponseEvent(tp, type);
	}
	public void playResponse(TrucoPlayer tp, byte type, int value)
	{
		firePlayResponseEvent(tp, type, value);
	}
	
	public void fireEndOfHandEvent()
	{
		points[0] = points[0] + trucoHand.getPointsOfTeam(0);
		points[1] = points[1] + trucoHand.getPointsOfTeam(1);
		teams[0].setPoints(points[0]);
		teams[1].setPoints(points[1]);
		TrucoPlayer tp = teams[(numberOfHand+1)%2].getTrucoPlayerNumber((numberOfHand-1)%numberOfPlayers/2);
		TrucoEvent event = new TrucoEvent(this,numberOfHand,tp,TrucoEvent.FIN_DE_MANO);
		detalleDelPuntaje = trucoHand.getPointsDetail();
		
		for(int i=0; i< getListaListeners().size();i++)
		{
			((TrucoListener)(getListaListeners().get(i))).endOfHand(event);
		}
/*		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "------------------------------------------------------------------------");
		logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "--------------------------------------Puntajes--------------------------");
		for (int i=0; i<2; i++)
			logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, teams[i].getName()+" :"+teams[i].getPoints()+"puntos.");
		
		for (int i=0; i<detalleDelPuntaje.size(); i++)
			logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, ((PointsDetail)detalleDelPuntaje.get(i)).aString());*/
		
	}
}
