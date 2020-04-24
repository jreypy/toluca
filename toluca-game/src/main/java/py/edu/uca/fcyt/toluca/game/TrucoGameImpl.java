package py.edu.uca.fcyt.toluca.game;

/*
 * trucoGame.java
 *
 * Created on 3 de marzo de 2003, 10:25 PM
 */

import py.edu.uca.fcyt.game.Game;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Clase que representa y administra un juego de Truco.
 *
 * @version 1.0
 * @authors Julio Rey || Christian Benitez
 */
public class TrucoGameImpl extends TrucoGame {

    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TrucoGameImpl.class);

    //TODO: Poner estos 2 en un constructor.
    private int gamePoints = 30;
    private int buenaPoints = gamePoints / 2;


    /**
     * Creates a new instance of trucoGame
     */


//    static Logger logger = Logger.getLogger(RoomServer.class);


    /**
     * @param tableNumber The tableNumber to set.
     */
    public void setTableNumber(int tableNumber) {
        logger.debug("Setting TableNumeber [" + tableNumber + "]");
        this.tableNumber = tableNumber;
    }

    protected int[] points = new int[2]; //puntajes de los teams

    protected TrucoHand trucoHand; //mano actual


    protected int numberOfTeams = 0; //numero de equipos
    protected int reparteCartas = 0; //quien empieza la mano
    protected boolean playersPreparados[]; //lista de players que estan preparados
    protected int cantidadDePlayersPreparados;
    protected Vector detalleDelPuntaje;


    public TrucoGameImpl() {
    }

    /**
     * Constructor con dos equipos, asi crea un TrucoGame
     *
     * @param tm1 Equipo 1 que jugará el TrucoGame.
     * @param tm2 Equipo 1 que jugará el TrucoGame.
     * @deprecated
     */
    public TrucoGameImpl(TrucoTeam tm1, TrucoTeam tm2) { //contructor con los teams
        this(tm1, tm2, 30);
    }

    /**
     * Constructor con dos equipos, asi crea un TrucoGame
     *
     * @param tm1 Equipo 1 que jugará el TrucoGame.
     * @param tm2 Equipo 1 que jugará el TrucoGame.
     */
    public TrucoGameImpl(TrucoTeam tm1, TrucoTeam tm2, int points) { //contructor con los teams
        setGamePoints(points);
        teams[0] = tm1;
        teams[1] = tm2;
        numberOfHand = 0;
        newGame();
    }

    /**
     * Dispara el inicio del juego
     */
    public void startGame() {
        if (teams[0].getNumberOfPlayers() != teams[1].getNumberOfPlayers())
            throw new IllegalArgumentException("Teams must have the same players quantity");

        numberOfPlayers = teams[0].getNumberOfPlayers() + teams[1].getNumberOfPlayers();
        logger.info("Starting TrucoGame [" + numberOfPlayers + "]");

        playersPreparados = new boolean[numberOfPlayers];
        newGame();
        fireGameStarted();


        for (int i = 0; i < numberOfPlayers; i++)
            playersPreparados[i] = true;

        logger.debug("PlayersReady [" + playersPreparados + "]");
        startHandConfirmated();
        logger.debug("StartHandConfirmated");
    }

    /**
     * nuevo Juego
     */
    public void newGame() { //juevo juego
        logger.debug("New Game is starting");
        points[0] = 0; //cerando el puntaje
        points[1] = 0;
    }

    /**
     * adherir un nuevo TrucoListener al juego.
     *
     * @param tl Oyente que se adhiere a la lista.
     */


    /**
     * configurar los equipos que jugarán el TrucoGame.
     *
     * @param team_1 Equipo 1 que jugará el TrucoGame.
     * @param team_2 Equipo dos que jugará el TrucoGame
     */
    public void setTeam(TrucoTeam team_1, TrucoTeam team_2) {//insertar los teams que partciparan del juego de truco
        teams[0] = team_1;
        teams[1] = team_2;
    }


    /**
     * Retorna el Equipo que es Numero i.
     *
     * @param i numero de Team (o 0 o 1)
     * @return Retorna un Equipo(TrucoTeam).
     */

    public TrucoTeam getTeam(int i) { //retorna el team numero i
        if (i == 0 || i == 1)
            return teams[i];
        return null;
    }

    /**
     * Retorna el puntaje Total de uno de los Teams.
     *
     * @param tm TrucoTeam de quien se retorna el Puntaje.
     * @return El valor del puntaje de un TrucoTeam.
     * @throws InvalidPlayExcepcion tira en caso de que el TrucoTeam no participe en el TrucoGame.
     */
    public int getGameTotalPoints(TrucoTeam tm) throws InvalidPlayExcepcion { //el puntaje del team tm
        if (teams[0] == tm)
            return points[0];
        if (teams[1] == tm)
            return points[1];
        throw (new InvalidPlayExcepcion("getGameTotalPoints() - fuera del dominio de la funcion"));
        //   return 0;
    }

    /**
     * configurar el puntaje del equipo
     *
     * @param tm  Team a configurar su puntaje.
     * @param pts Puntaje a ser configurado.
     */
    public void setGameTotalPoints(TrucoTeam tm, int pts) { //inserta puntaje
        if (teams[0] == tm) {
            points[0] = pts;
            return;
        }
        if (teams[1] == tm) {
            points[1] = pts;
            return;
        }
    }

    /**
     * Metodo para realizar una jugada TrucoPlay.
     *
     * @param tp Jugada a ser realizada (TrucoPlay).
     * @throws InvalidPlayExcepcion Excepcion en caso de dectarse que no es posible hacer esa jugada.
     */

    synchronized public void play(TrucoPlay tp)
            throws InvalidPlayExcepcion { //play trucoGame

        if (tp == null) {
            throw new IllegalArgumentException("TrucoPlay is required");
        }
        if (teams[0] == null || teams[1] == null)
            throw (new InvalidPlayExcepcion("Teams not found in Trucogame"));
		/*try
		{*/
        if (trucoHand == null)
            throw new IllegalArgumentException("TrucoHand is null");

        trucoHand.play(tp);
        //firePlayToOtherClients(tp);
			
/*		} //Comente esta parte xq si el metodo hace tiene "throws" no hace falta el try,catch
		catch (InvalidPlayExcepcion e)
		{
			e.printStackTrace(System.out);
			throw e;
		}*/
    }

    /**
     * Retorna Verdadero si posible realizar una jugada.
     *
     * @param tp Jugada a verificar si es posible ser realizada.
     * @return Retorna si es v�lido la jugada.
     */
    public boolean esPosibleJugar(TrucoPlay tp) {
        return trucoHand.esPosibleJugar(tp);
    }

	
	
	/*public void fireEventType(byte type){
		TrucoEvent event = new TrucoEvent(this,numberOfHand,type);
	}*/

    /**
     * Enviar mensaje a todos los oyentes sobre el final de la mano
     */
    public void fireEndOfHandEvent() {
        logger.debug("Fire End Of Hand");
        points[0] = points[0] + trucoHand.getPointsOfTeam(0);
        points[1] = points[1] + trucoHand.getPointsOfTeam(1);

        teams[0].setPoints(points[0]);
        teams[1].setPoints(points[1]);

        detalleDelPuntaje = trucoHand.getPointsDetail();


        logger.info("------------------------------------------------------------------------");
        logger.info("--------------------------------------Puntajes--------------------------");
        logger.info("------------------------------------------------------------------------");

        for (int i = 0; i < 2; i++)
            logger.info(teams[i].getName() + " :" + teams[i].getPoints() + "puntos.");

        logger.info("------------------------------------------------------------------------");

        for (int i = 0; i < detalleDelPuntaje.size(); i++)
            logger.info(((PointsDetail) detalleDelPuntaje.get(i)).aString());

        logger.info("------------------------------------------------------------------------");
        logger.info("------------------------------------------------------------------------");

        setPlayersReady();
        super.fireEndOfHandEvent();
    }

    public void EndOfHandEvent() {
        logger.warn("End of hand shaque termina la mano");
        /*
         * Esto fue agregado por Cricco. Se pretende controlar si termina el juego que salgan nomas
         * */
        if (!(this instanceof TrucoGameClient) && (points[0] >= this.gamePoints || points[1] >= this.gamePoints)) {
            System.out.println("se teeeermin el jueeeego");
            logger.info("Se teeermina el jueeeeego");
            fireEndOfGameEvent();
        } else {
            newHand();
        }
        //newHand();
    }

    private void setPlayersReady() {
        logger.debug("***  Set Players Ready [" + playersPreparados + "]");
        cantidadDePlayersPreparados = 0;
        for (int i = 0; i < numberOfPlayers; i++)/*desbloquear que reinicien la mano*/
            playersPreparados[i] = false;
    }


    /**
     * Para dejarse en espera a que inicie la siguiente mano.
     *
     * @param tPlayer Player que esta preparado.
     * @ Una vez que todos los TrucoPlayers hallan preparado, empieza la siguiente mano.
     */
    public void startHand(TrucoPlayer tPlayer) {
        int i; //
        logger.debug("Players ready [" + cantidadDePlayersPreparados + "]");
        int numOfPlayer = teams[0].getNumberOfPlayer(tPlayer) * 2;

        if (numOfPlayer >= 0) {
            if (playersPreparados[numOfPlayer] == false) {
                playersPreparados[numOfPlayer] = true;
                firePlayEvent(tPlayer, TrucoEvent.PLAYER_CONFIRMADO);
                cantidadDePlayersPreparados++;
                logger.info(tPlayer.getName() + "[confirmado][" + cantidadDePlayersPreparados + "]");
            } else
                return;
        }
        numOfPlayer = teams[1].getNumberOfPlayer(tPlayer) * 2 + 1;

        if (numOfPlayer >= 0) {
            if (playersPreparados[numOfPlayer] == false) {
                playersPreparados[numOfPlayer] = true;
                firePlayEvent(tPlayer, TrucoEvent.PLAYER_CONFIRMADO);
                cantidadDePlayersPreparados++;
                System.out.println(tPlayer.getName() + "confirmado");
            } else
                return;
        }

        if (cantidadDePlayersPreparados == numberOfPlayers) {
            cantidadDePlayersPreparados = 0;
            startHandConfirmated();
        }
    }

    protected void startHandConfirmated() {
        logger.info("Starting Hand");
        if (points[0] >= this.gamePoints || points[1] >= this.gamePoints) {
            fireEndOfGameEvent();
        } else {
            numberOfHand++;
            fireHandStarted();/*para que se preparen los jugadores*/
            trucoHand = new TrucoHand(this, numberOfHand - 1); /*se crea un truco hand y guardo la referencia*/
            trucoHand.startHand();
        }
    }

    protected void newHand() { //nueva mano
        logger.info("NewHand");
        if (teams[0].getNumberOfPlayers() != teams[1].getNumberOfPlayers())
            throw new TrucoGameRuntimeException("TrucoGame.newHand - la cantidad de players de los Teams son distintos");


        //reparteCartas=(++reparteCartas)%getNumberOfPlayers();/*incrementa el que reparte las cartas*/
        ///System.out.println("despues va repartir las cartas" + reparteCartas + "numero de players" + getNumberOfPlayers());

    }


    /**
     * Retorna el numero de jugadores del TrucoGame.
     *
     * @return Cantidad de Jugadores.
     */
    public int getNumberOfPlayers() {
        return (numberOfPlayers);
    }


    /**
     * Retorna cuantos puntos se juegan en caso de faltear.
     *
     * @return retorna el valor del puntaje
     */

    public int getFaltear() {
        int i = 0;
        if (points[1] > points[0])
            i = 1;
        if (points[i] >= this.buenaPoints)
            return (this.gamePoints - points[i]);
        return (this.buenaPoints - points[i]);
    }

    /**
     * Retorna la cantidad de puntos que se juegan, en caso de jugar Al Resto.
     *
     * @return Retorna la cantidad de puntos.
     */
    public int alResto() {
        if (points[0] > points[1])
            return (this.gamePoints - points[0]);
        return (this.gamePoints - points[1]);
    }

    public Vector getDetallesDeLaMano() {
        return detalleDelPuntaje;
    }
    /*obtener el mejor envido que puede cantar el player*/

    /**
     * Retorna el Valor del Envido que puede cantar un Player.
     *
     * @param tp TrucoPlayer a ser retornado su valor de Envido.
     * @return el valor del envido del TrucoPlayer.
     * <p>
     * public int getValueOfEnvido(TrucoPlayer tp) {
     * <p>
     * return trucoHand.getValueOfEnvido(tp);
     * <p>
     * <p>
     * <p>
     * }
     */
    public TrucoCard getCard(byte myKind, byte myValue) {
        TrucoCard myCarta = trucoHand.getCard(myKind, myValue);
        if (myCarta == null) {
            System.out.println("en TrucoGame getCard devuelve null");
        }
        return myCarta;
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
     */
    public void setTeamGanador(int team) {
        if (team == 0)
            points[0] = points[1] + 1;
        else
            points[1] = points[0] + 1;
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
        setBuenaPoints(gamePoints / 2);
    }


    @Override
    public int getValorEnvido(TrucoPlayer trucoPlayer) {
        return trucoHand.getValueOfEnvido(trucoPlayer);
    }

    @Override
    public TrucoCard getCardNoPlayed(TrucoPlayer trucoPlayer) {
        int playerIndex = trucoHand.getNumberOfPlayer(trucoPlayer);
        return trucoHand.getCardNoPlayed(playerIndex);
    }
}
