package py.edu.uca.fcyt.toluca.event;

/** Java class "TrucoEvent.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */



import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;

import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;


public class TrucoEvent
{
	
	///////////////////////////////////////
	/*tipoy de eventos*/
	public static final byte ENVIAR_CARTAS=0;
	public static final byte ENVIDO=1; //mensajes de juego , para el pie contestar
	public static final byte REAL_ENVIDO=2;
	public static final byte FALTA_ENVIDO=3;
	
	public static final byte FLOR=11;//mensajes de juego
	public static final byte CONTRA_FLOR=12; //elpiedebe contestar//
	public static final byte CONTRA_FLOR_AL_RESTO=13;//el pie debe contestar//
	public static final byte CON_FLOR_ME_ACHICO=14;
	
	public static final byte TRUCO=21; //mensajes de juego el `pie debe contestar//
	public static final byte RETRUCO=22;
	public static final byte VALE_CUATRO=23;
	
	public static final byte QUIERO=31;//mensajes de juego
	public static final byte NO_QUIERO=32;
	public static final byte ME_VOY_AL_MAZO=33;
	
	public static final byte TURNO_JUGAR_CARTA= 50;
	public static final byte TURNO_CANTAR_ENVIDO= 51;
	public static final byte TURNO_CANTAR_FLOR = 52;
	public static final byte TURNO_RESPONDER_TRUCO = 53;
	public static final byte TURNO_RESPONDER_RETRUCO = 54;
	public static final byte TURNO_RESPONDER_VALECUATRO = 55;
	public static final byte TURNO_RESPONDER_CONTRAFLOR = 56;
	public static final byte TURNO_RESPONDER_CONTRAFLORALRESTO = 57;
	public static final byte TURNO_RESPONDER_ENVIDO = 58;
	public static final byte TURNO_RESPONDER_REALENVIDO = 59;
	public static final byte TURNO_RESPONDER_FALTAENVIDO = 60;
	
	public static final byte JUGAR_CARTA=61;
	public static final byte CANTO_ENVIDO=62;//avisar los cantos
	public static final byte CANTO_FLOR = 63;
	public static final byte PASO_ENVIDO = 64;
	public static final byte PASO_FLOR = 65;
	public static final byte CERRARSE = 66;
	/*no utilizadas...*/
	public static final byte SIN_MENSAJES=100;
	public static final byte INICIO_DE_JUEGO = 101;
	public static final byte INICIO_DE_MANO = 102;
	public static final byte FIN_DE_MANO = 103;
	public static final byte FIN_DE_JUEGO = 104;
	public static final byte CARTAS_REPARTIDAS = 105;
	//Segun la impresion, el tipo de play q viene es 103
	//no me queda claro si FIN_DE_RONDA y FIN_DE_MANO son distintos realmente
	//porque al terminar la ronda (alguien gana una repartida de 3 cartas por jugador)
	//se dispara el nro. 103
	public static final byte FIN_DE_RONDA = 111;
	/*para mensajes de confirmacion de espera del player*/
	public static final byte PLAYER_CONFIRMADO = 110;
	
	
	
	
	
	// attributes
	
	
	/**
	 * <p>
	 * Represents ...
	 * </p>
	 */
	private TrucoGame game;  //EL JUEGO
	private int hand;  //NUMERO DE MANO
	private TrucoPlayer player;  //PLAYER QUE DISPARA EL EVENTO
	private byte type;  //TIPO DE JUGADA 0 CANTO
	private TrucoCard[] cards;
	private TrucoCard card;
	private int tableNumber;//para que el Communicator sepa a quien ejecutarle los metodos
	private int value=-1;
	/*repartir cartas*/
	public TrucoEvent()
	{
	}
	public TrucoEvent(TrucoGame game, int hand, TrucoPlayer player)
	{
		this(game,hand);
		this.player = player;
	}
	public TrucoEvent(int tableId,int hand,TrucoPlayer player,byte type, TrucoCard[] cards)
	{
		this.tableNumber = tableId;
		this.hand = hand;
		this.player = player;
		this.type = type;
		this.cards = cards;
	}
	public TrucoEvent(int tableId,int hand,TrucoPlayer player,byte type,int value)
	{
		this.tableNumber = tableId;
		this.hand = hand;
		this.player = player;
		this.type = type;
		this.value = value;
	}
	public TrucoEvent(TrucoGame game, int hand)
	{
		this.game = game;
		this.hand = hand;
		
	}
	public TrucoEvent(TrucoGame game,int hand, TrucoPlayer player, byte type, TrucoCard[] cards)
	{ //contructor de instancias
		this(game,hand,player,type);
		this.cards = cards;
	}
	/*cantos envido truco, flor etc*/
	public TrucoEvent(TrucoGame game,int hand, TrucoPlayer player, byte type)
	{
		this(game,hand,type);
		this.player = player;
		
	}
	public TrucoEvent(int tableId,int hand,TrucoPlayer player,byte type)
	{
		this.tableNumber=tableId;
		this.hand=hand;
		this.player=player;
		this.type=type;
	}
	public TrucoEvent(TrucoGame game,int hand, byte type)
	{
		this.game = game;
		this.hand= hand;
		this.type = type;
	}
	/*jugadas */
	public TrucoEvent(TrucoGame game, int hand, TrucoPlayer player, byte type, TrucoCard card)
	{
		this(game, hand, player, type);
		this.card = card;
	}
	public TrucoEvent(int tableid,int hand,TrucoPlayer player,byte type,TrucoCard card)
	{
		this(tableid,hand,player,type);
		this.card=card;
	}
	public TrucoEvent(TrucoGame game, int hand, TrucoPlayer player, byte type, int value)
	{
		this(game, hand, player,type);
		this.value = value;
	}
	
	public TrucoGame getTrucoGame()
	{ //obtener el TrucoGame que disparo el evento
		return game;
	}
	public int getNumberOfHand()
	{ //obtener el numero de mano del truco game
		return hand;
	}
	public TrucoPlayer getTrucoPlayer()
	{ //obtener el player
		return player;
	}
	public byte getTypeEvent()
	{ //tipo de evento
		return type;
	}
	public TrucoCard[] getCards()
	{
		return cards;
	}
	public void setCard(TrucoCard tc)
	{
		this.card = tc;
	}
	
	public TrucoCard getCard()
	{
		return card;
	}
	public void setPlayer(TrucoPlayer tp)
	{
		player = tp;
		
	}
	public void setTypeEvent(byte type)
	{
		this.type = type;
	}
	
	public TrucoPlayer getPlayer()
	{
		return player;
	}
	public void  setValue(int value)
	{
		this.value = value;
	}
	public int getValue()
	{
		return value;
	}
	public TrucoPlay toTrucoPlay()
	{
		TrucoPlay tp = new TrucoPlay();
		tp.setPlayer(player);
		tp.setTableNumber(tableNumber);
		byte typeevent = 0;
		TrucoCard carta = null;
		

		
		switch (type)
		{
			case 1:
				typeevent = 1;
				break;
			case 2:
				typeevent = 2;
				break;
			case 3:
				typeevent = 3;
				break;
			case 11:
				typeevent = 11;
				break;
			case 12:
				typeevent = 12;
				break;
			case 13:
				typeevent = 13;
				break;
			case 14:
				typeevent = 14;
				break;
			case 21:
				typeevent = 21;
				break;
			case 22:
				typeevent = 22;
				break;
			case 23:
				typeevent = 23;
				break;
			case 31:
				typeevent = 31;
				break;
			case 32:
				typeevent = 32;
				break;
			case 33:
				typeevent = 33;
				break;
			case 61:
				typeevent = 62;
				carta = card;
				break;
			case 62:
				typeevent = 61;
				break;
			case 63:
				typeevent = 65;
				break;
			case 64:
				typeevent = 63;
				break;
			case 65:
				typeevent = 64;
				break;
			case 66:
				typeevent = 66;
				break;
			case FIN_DE_MANO:
				typeevent = FIN_DE_MANO;
				break;
			case 110:
				//System.out.println("Vino el Player Confirmado: " + tp.getPlayer());
				typeevent = 110;
				break;
			default :
				typeevent = -1;
				new Throwable("No se conoce el tipo del Truco Play: " + type).printStackTrace(System.out);
				break;
		}
		//System.out.println("evento "+type +" a "+typeevent+"!");
		tp.setType(typeevent);
		tp.setCard(carta);
		tp.setValue(value);
		return tp;
	}
	public void setTableNumber(int tableNumber)
	{
		this.tableNumber=tableNumber;
	}
	public int getTableNumber()
	{
		return tableNumber;
	}
	
	
	
	
	
	
	
	
	
	
	
	//SE VA CON EL CANTO
   /*
   public Document xmlCreateTurno()
	{//Del lado del cliente dispara el evento turno
	   Element ROOT = new Element ("Turno");
	
		Element TIPO =new Element ("Type");
		TIPO.setAttribute("id",String.valueOf(type));
		ROOT.addContent(TIPO);
	
		Element GAME =new Element ("Game");
		GAME.setAttribute("id",String.valueOf(game.getId()));
		ROOT.addContent(GAME);
	
		Element HAND = new Element("Hand");
		HAND.setAttribute("number",String.valueOf(hand));
		ROOT.addContent(HAND);
	
		Element PLAYER =new Element("Player");
		PLAYER.setAttribute("name",player.getName());
		ROOT.addContent(PLAYER);
		Document doc=new Document(ROOT);
		return doc;
   }*/
	
	
	
	//SE VA CON EL CANTO NOMAS
/*   public Document xmlCreateTerminalMessage()
  {
		Element ROOT = new Element ("TerminalMessage");
 
		Element TIPO =new Element ("Type");
		TIPO.setAttribute("id",String.valueOf(type));
		ROOT.addContent(TIPO);
 
		Element TABLE =new Element ("Table");
		TABLE.setAttribute("id",String.valueOf(getTableNumber()));
		ROOT.addContent(TABLE);
 
		Element HAND = new Element("Hand");
		HAND.setAttribute("number",String.valueOf(hand));
		ROOT.addContent(HAND);
 
		Element PLAYER =new Element("Player");
		PLAYER.setAttribute("name",player.getName());
		ROOT.addContent(PLAYER);
 
 
		Document doc=new Document(ROOT);
		return doc;
   }*/
	
	public TrucoPlay getTrucoPlay()
	{
		// TODO Hacer el SWITCH gigantesco, probar y ya est�
		TrucoPlay tp = null;
		switch(type)
		{
/*		 case ENVIAR_CARTAS:
			 doc = xmlCreateSendCards();
			 break;
		 case ENVIDO:
			 doc = xmlCreateCanto();
			 break;
		 case REAL_ENVIDO:
			 doc = xmlCreateCanto();
			 break;
		 case FALTA_ENVIDO:
			 doc = xmlCreateCanto();
			 break;
		 case FLOR:
			 doc = xmlCreateCanto();
			 break;
		 case CANTO_ENVIDO:
			 doc = xmlCreateCanto();
			 break;
 
		 case CANTO_FLOR:
			 doc = xmlCreateCanto();
			 break;
 
		 case CONTRA_FLOR:
			 doc = xmlCreateCanto();
			 break;
 
		 case CONTRA_FLOR_AL_RESTO:
			 doc = xmlCreateCanto();
			 break;
		 case CON_FLOR_ME_ACHICO:
			 doc = xmlCreateCanto();
			 break;
		 case TRUCO:
			 doc = xmlCreateCanto();
			 break;
		 case RETRUCO:
			 doc = xmlCreateCanto();
			 break;
		 case VALE_CUATRO:
			 doc = xmlCreateCanto();
			 break;
		 case QUIERO:
			 doc = xmlCreateCanto();
			 break;
		 case NO_QUIERO:
			 doc = xmlCreateCanto();
			 break;
		 case ME_VOY_AL_MAZO:
			 doc = xmlCreateCanto();
			 break;
		 case CERRARSE:
			 doc = xmlCreateCanto();
			 break;
		 case PASO_ENVIDO:
			 doc = xmlCreateCanto();
			 break;
		 case PASO_FLOR:
			 doc = xmlCreateCanto();
			 break;
		 case TURNO_JUGAR_CARTA:
			 doc = xmlCreateCanto();
			 break;
 
		 case TURNO_CANTAR_FLOR:
			 doc = xmlCreateCanto();
			 break;
		 case TURNO_RESPONDER_TRUCO:
			 doc = xmlCreateCanto();
			 break;
		 case TURNO_RESPONDER_RETRUCO:
			 doc = xmlCreateCanto();
			 break;
		 case TURNO_RESPONDER_VALECUATRO:
			 doc = xmlCreateCanto();
			 break;
		 case TURNO_RESPONDER_ENVIDO:
			 doc = xmlCreateCantarTanto();
			 break;
		 case TURNO_RESPONDER_REALENVIDO:
			 doc = xmlCreateCantarTanto();
			 break;
		 case TURNO_RESPONDER_FALTAENVIDO:
			 doc = xmlCreateCantarTanto();
			 break;
		 case TURNO_RESPONDER_CONTRAFLOR:
			 //doc =xmlCreateCantarTanto();
			 break;
		 case TURNO_RESPONDER_CONTRAFLORALRESTO:
			 //doc = xmlCreateCantarTanto();
			 break;*/
			case JUGAR_CARTA:
				tp = new TrucoPlay(getPlayer(), TrucoPlay.JUGAR_CARTA, getCard());
				break;
			case FIN_DE_MANO:
				//doc = xmlCreateCanto();
				break;
			case FIN_DE_JUEGO:
				//doc = xmlCreateCanto();
				break;
			case INICIO_DE_JUEGO:
				//doc = xmlCreateCanto();
				break;
			case INICIO_DE_MANO:
				//doc = xmlCreateCanto();
				break;
			default:
				System.out.println("tipo de event no encontrado:" + type);
		}
		return tp;
	}
	
	
	public static void main(String[] args) {
		
		
		TrucoEvent event=new TrucoEvent();
		event.setGame(new TrucoGame(new TrucoTeam("Blancos"),new TrucoTeam("Negros")));
		event.setTableNumber(2);
		event.setCard(new TrucoCard(2,2));
		event.setType(TrucoEvent.CANTO_FLOR);
		event.setTypeEvent((byte)33);
		event.setValue(3);
		 
		
		XMLEncoder e=	new XMLEncoder(		new BufferedOutputStream(System.out));
		e.writeObject(event);
		e.close();
	}
	

	/**
	 * @return
	 */
	public TrucoGame getGame() {
		return game;
	}

	/**
	 * @return
	 */
	public int getHand() {
		return hand;
	}

	/**
	 * @return
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @param cards
	 */
	public void setCards(TrucoCard[] cards) {
		this.cards = cards;
	}

	/**
	 * @param game
	 */
	public void setGame(TrucoGame game) {
		this.game = game;
	}

	/**
	 * @param i
	 */
	public void setHand(int i) {
		hand = i;
	}

	/**
	 * @param b
	 */
	public void setType(byte b) {
		type = b;
	}


} // end TrucoEvent



