/*
 * TrucoHand.java
 *
 * Created on 5 de marzo de 2003, 02:49 PM
 */

package py.edu.uca.fcyt.toluca.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.statusGame.TrucoStatusTable;

/**
 * constructor
 * 
 * @author Julio Rey || Cristian Benitez
 *  
 */
public class TrucoHand {

    protected TrucoGame game;

    /* estados del juego */
    protected static final byte PRIMERA_RONDA = 0;

    protected static final byte SEGUNDA_RONDA = 1;

    protected static final byte TERCERA_RONDA = 2;

    protected final byte FLOR = 3;

    protected static final byte CONTRAFLOR = 4;

    protected static final byte CONTRAFLOR_AL_RESTO = 5;

    protected static final byte ENVIDO = 6;

    protected static final byte REAL_ENVIDO = 7;

    protected static final byte FALTA_ENVIDO = 8;

    protected static final byte ESPERANDO_RESPUESTA_DEL_TRUCO = 9;

    protected static final byte ESPERANDO_RESPUESTA_DEL_RETRUCO = 10;

    protected static final byte ESPERANDO_RESPUESTA_DEL_VALECUATRO = 11;

    protected static final byte RONDA_DE_FLOR = 13;

    protected static final byte RONDA_DE_ENVIDO = 14;

    protected boolean seTerminoLaMano = false;

    protected LinkedList puntajes = new LinkedList(); //lista delos puntajes de
                                                      // esta mano;

    protected int numerodecarta = 0; //cuantas cartas se jugaron

    protected TrucoPlayer pie1; //pie del team 1;

    protected TrucoPlayer pie2; //pie del team 2;

    protected int[] winRound = new int[3]; //ganadores de la ronda

    protected boolean equipo1_canto_flor = false; /*
                                                   * los equipos ya cantaron
                                                   * flor?
                                                   */

    protected boolean equipo2_canto_flor = false;
    
    /** Contiene los nombres de los que ya cantaron flor*/
    protected ArrayList jugadoresFloridos = new ArrayList();

    protected boolean florFlor = false; //si se canto flor flor

    protected boolean contraFlor = false; //si se canto contra flor

    protected boolean contraFlorAlResto = false; //si se canto contra flor al resto

    protected boolean flor = false; /* ya se canto flor */

    protected boolean envido = false; //se canto envido?

    protected boolean realEnvido = false; //se canto real envido?

    protected boolean faltaEnvido = false; //se canto falta envido?

    protected boolean sePuedeCantarEnvido = true; //se puede cantar Envido?

    protected boolean sePuedeCantarFlor = true; //se puede cantar Flor?

    protected boolean elEnvidoEstaPrimero = false;

    protected int numberOfEnvidos = 0; //cantidad de envido que se dijeron

    protected int numberOfRealEnvidos = 0; //cantidad de real envido que se
                                           // dijeron

    protected int pointsOfEnvido = 0;

    protected boolean truco = false;//se canto truco

    protected boolean retruco = false;//" " retrtuco

    protected boolean valecuatro = false;//" " vale Cuatro

    protected int pointsOfHand = 1;

    protected int cantidadDePlayers = 0;

    protected TrucoStatusTable statusTable;

    protected TrucoTeam[] teams = new TrucoTeam[2];

    protected int[] points = new int[2];

    protected TrucoPlayer playTurn = null; //turno para jugar

    protected int primerTurnoNumber; //numero de jugador que juega primero

    protected boolean huboRondaDeEnvido = false;

    protected int playTurnNumber; //numero de jugador que le toca tirar carta

    protected int envidoTurnNumber; //numero de jugador que le toca cantar su
                                    // envido

    protected int florTurnNumber; /*
                                   * numero de jugador que le toca cantar su
                                   * flor
                                   */

    protected Team laPalabra = null; //quien puede cantar retruco, etc

    protected int pieTeam1Number; //numero de player que es pie del equipo1

    protected int pieTeam2Number; //numero de player que es pie del equipo2

    protected int cartasJugadas = 0; //cantidad de cartas jugadas

    protected int numeroDeRonda = 1; //numero de ronda

    protected TrucoPlayer quieroYCanto;//quiero y canto tanto;

    protected byte estadoActual = PRIMERA_RONDA; //estado Actual, para
                                                 // controlar el juego

    protected Vector detalleDePuntaje;

    protected int puntosParaEnvido;

    protected int pointsOfHandDetail = PointsDetail.NO_SE_CANTO_TRUCO;

    protected boolean jugadadequieroycanto = false; /*
                                                     * esto es una
                                                     * verguenza!!!!!!! pero no
                                                     * se ocurrio otra cosa.
                                                     * julio rey
                                                     */

    public TrucoHand() {

    }

    /**
     * Contruye un TrucoHand.
     * 
     * @param tg
     *            juego al que pertenece(un truco hand es una parte de un
     *            TrucoGame)
     * @param reparteCartas
     *            numero de jugador que repartira(para saber quien tiene el
     *            primer turno)
     */
    public TrucoHand(TrucoGame tg, int reparteCartas) { //Se crea la Instancia
                                                        // de la mano
        game = tg; //a que game se refiere
        cantidadDePlayers = game.getNumberOfPlayers();
        primerTurnoNumber = (reparteCartas + 1) % cantidadDePlayers;
        playTurnNumber = primerTurnoNumber; //quien juega la primera carta
        envidoTurnNumber = primerTurnoNumber; //quien canta el primer envido en
                                              // caso de cantar
        florTurnNumber = primerTurnoNumber; //quien canta su flor en caso de
                                            // haber
        statusTable = new TrucoStatusTable(cantidadDePlayers); //se crea un
                                                               // estado de la
                                                               // mesa
        detalleDePuntaje = new Vector();
        for (int i = 0; i < 3; i++)
            winRound[i] = -1;
        points[0] = 0; /* Cerar los puntajes. */
        points[0] = 0;
        teams[0] = game.getTeam(0);
        teams[1] = game.getTeam(1);
        getPies(); //obtener los pies de la mano
        dealtCards(); //repartir cartas
    }

    /**
     * Inicia el TrucoHand.
     */
    public void startHand() {
    	
    	System.out.println("---------------------------------------");
    	System.out.println("---- EMPIEZA LA MANO  ----");    	
        playTurn(); //asignar turno
    }

    protected void getPies() { /* quienes \seran los pies de esta mano */
        pieTeam1Number = (primerTurnoNumber + cantidadDePlayers - 1)
                % (cantidadDePlayers); //numero de player que sera pie del
        pie1 = teams[0].getTrucoPlayerNumber(pieTeam1Number / 2); //obtener
                                                                  // player
        pieTeam2Number = (primerTurnoNumber + cantidadDePlayers - 2)
                % (cantidadDePlayers); //numero de player que sera pie del
                                       // team2
        pie2 = teams[1].getTrucoPlayerNumber(pieTeam2Number / 2); //obtener
                                                                  // playe
        if (pieTeam1Number % 2 == 1) {
            int pieAuxiliar = pieTeam1Number;
            pieTeam1Number = pieTeam2Number;
            pieTeam2Number = pieAuxiliar;
        }

    }

    /* devolver el numero de player que es pie */
    /**
     * Retorna el jugador que es el pie del equipo
     * 
     * @param i
     *            Numero de Equipo del Player a ser retornado.
     * @return Numero de TrucoPlayer que es pie del TrucoTeam.
     */
    public int getPieTeam(int i) { //obtener el numero de player pie
        if (i == 1)
            return (pieTeam1Number);
        if (i == 2)
            return (pieTeam2Number);
        return -1;
    }

    /* repartir cartas a todos los players del game */
    protected void dealtCards() throws InvalidPlayExcepcion {
        int i;
        TrucoCard[] cardsToPlayer;
        TrucoPlayer pl;
        try {
            for (i = 0; i < cantidadDePlayers; i++) {
                pl = teams[i % 2].getTrucoPlayerNumber(i / 2);
                cardsToPlayer = statusTable.getPlayerCards(i); //2pregunto al
                                                               // estado de la
                                                               // mesa cuales
                                                               // son sus cartas
                if (cardsToPlayer == null)
                    System.out.println("error null en dealtcards de TrucoHand");
                game.dealtCards(pl, cardsToPlayer); //3les envio al player las
                                                    // cartas
                /*
                 * pl = teams[0].getPlayerNumber(i); cardsToPlayer =
                 * statusTable.getPlayerCards((2*i)); //2pregunto al estado de
                 * la mesa cuales son sus cartas
                 * game.dealCards(pl,cardsToPlayer); //3les envio al player las
                 * cartas pl = teams[1].getPlayerNumber(i); cardsToPlayer =
                 * statusTable.getPlayerCards((2*i+1)); // " '" """"2
                 * game.dealCards(pl,cardsToPlayer); //" """ 3
                 */
            }
            // TODO COMMENTED-out porque daba una excepciï¿½n:
            // Teorï¿½a de PS: No hace falta. Llamada al pedo que no va a volver
            // Teorï¿½a de AA: Estï¿½ mal el tipo del evento nomï¿½s.
            //game.fireCardsDealt();
        } catch (InvalidPlayExcepcion e) {
            throw e;
        }
    }

    /* asignar al player el turno */
    protected void playTurn() throws InvalidPlayExcepcion {
        if (statusTable.estaCerrado(playTurnNumber)) {
            nextPlayTurn();
            return;
        }
        playTurn = teams[playTurnNumber % 2]
                .getTrucoPlayerNumber(playTurnNumber / 2);

        game.fireTurnEvent(playTurn, TrucoEvent.TURNO_JUGAR_CARTA);
    }

    protected void nextPlayTurn() throws InvalidPlayExcepcion {
        cartasJugadas++;
        if (cartasJugadas % cantidadDePlayers == 0) {
            winRound[numeroDeRonda - 1] = statusTable
                    .resultadoRonda(numeroDeRonda); /* fin de ronda */

            if (winRound[numeroDeRonda - 1] >= 0)
                playTurnNumber = winRound[numeroDeRonda - 1];
            else
                playTurnNumber = (playTurnNumber + 1) % cantidadDePlayers;/*
                                                                           * siguiente
                                                                           * player
                                                                           */

            statusTable.terminoRonda();

            if (finDeRonda())
                return;
            numeroDeRonda++;
            volverAEstadoDeJuego();
        } else {
            playTurnNumber = (playTurnNumber + 1) % cantidadDePlayers;/*
                                                                       * siguiente
                                                                       * player
                                                                       */
        }
        playTurn();/* asignar turno */
    }

    protected boolean finDeRonda() throws InvalidPlayExcepcion {
        sePuedeCantarFlor = false;
        sePuedeCantarEnvido = false;

        if (numeroDeRonda == 1)/* en la primera ronda no se decide nada */
            return false;
        int t1 = 0; //rondas al team 1;
        int t2 = 0; //ronda al team 2;
        int e = 0;
        for (int i = 0; i < numeroDeRonda; i++) {
            if (winRound[i] < 0) { /* empate en la ronda */
                e++;
            }
            if (winRound[i] >= 0 && winRound[i] % 2 == 0) {/*
                                                            * gano el team 1 la
                                                            * ronda i
                                                            */
                t1++;
            }
            if (winRound[i] >= 0 && winRound[i] % 2 == 1) {/*
                                                            * gano el team 2 la
                                                            * ronda i
                                                            */
                t2++;
            }
        }
        if (numeroDeRonda == 2) { //en dos rondas
            if (e == 2) /* empate las dos rondas se decide en la tercera */
                return false;
            if (t1 == t2) //1 punto para cada , se decide en tercera
                return false;
            if (t1 == 0) { //1 0 2 para equipo 1, gano la mano
                finDeMano(1);
                return true;
            }
            if (t2 == 0) {
                finDeMano(0); //gano equipo 2
                return true;
            }
            return false;//no termino la ronda
        } else { /* si es la tercera */
            int tercera = winRound[2];
            if (tercera >= 0)
                finDeMano(tercera % 2);//ganador de tercera
            else {//empate
                int winner = winRound[0];/* gana la pri */
                if (winner < 0) /* si fue empate la pri */
                    winner = primerTurnoNumber;/**/
                finDeMano(winner); //gana equipo mano^
            }
        }
        return true;
    }

    /**
     * avisaa al trucoHand sobre el final de la mano
     */
    protected void finDeMano_controlarFlor() {
        /* verificar player por player sus cartas para verificar flor */
        for (int i = 0; i < cantidadDePlayers; i++) {
            TrucoPlayer responsable = teams[i % 2].getTrucoPlayerNumber(i / 2);
            if (statusTable.mostroFlor(i)) { /* si el player canto flor */
                if (statusTable.jugoTresCartas(i)) { //y coloco en mesa
                    points[i % 2] = points[i % 2] + 3;//tres puntos porque
                                                      // mostro
                    detalleDePuntaje
                            .add(new PointsDetail(teams[i % 2],
                                    PointsDetail.FLOR_CANTADA_MOSTRADA, 3,
                                    responsable));
                } else {
                    if (statusTable.estaCerrado(i)) {/* y se cerro */
                        points[(i + 1) % 2] = points[(i + 1) % 2] + 3; //se le
                                                                       // da 3
                                                                       // al
                                                                       // equipo
                                                                       // contrario
                                                                       // penalizacion
                                                                       // por no
                                                                       // mostrar
                        detalleDePuntaje.add(new PointsDetail(
                                teams[(i + 1) % 2],
                                PointsDetail.FLOR_NOCANTADA_MOSTRADA, 3,
                                responsable));
                    } else/* si no se cerro */
                    {
                        points[(i) % 2] = points[(i) % 2] + 3; //se le da 3
                                                               // puntos por
                                                               // cada flor
                        detalleDePuntaje.add(new PointsDetail(teams[i % 2],
                                PointsDetail.FLOR_CANTADA_MOSTRADA, 3,
                                responsable));
                    }
                }
            } else {//si no canto
                if (statusTable.tieneFlor(i)) { //si podia cantar flor
                    if (statusTable.jugoTresCartas(i)
                            || (huboRondaDeEnvido && statusTable.mostraraFlor(
                                    i, primerTurnoNumber))) { //y mostro que
                                                              // tenia
                        points[(i + 1) % 2] = points[(i + 1) % 2] + 3; //tres
                                                                       // puntos
                                                                       // para
                                                                       // equipo
                                                                       // contrario
                                                                       // por
                                                                       // penalizacion
                        detalleDePuntaje.add(new PointsDetail(
                                teams[(i + 1) % 2],
                                PointsDetail.FLOR_NOCANTADA_MOSTRADA, 3,
                                responsable));
                    }/* si no, no pasa nada */
                }
            }
        }
//        System.out.println("controlo la flor con exito");
    }

    /**
     * 
     * 
     * @return
     */
    protected int[] finDeMano_controlarEnvido() {
        int[] result = { 0, 0 };

        int resultadoEnvido = statusTable.resultadoEnvido(primerTurnoNumber);
        TrucoPlayer responsable = teams[resultadoEnvido % 2]
                .getTrucoPlayerNumber(resultadoEnvido / 2);
        if (!statusTable.mostroEnvido(resultadoEnvido)) {
            resultadoEnvido = (resultadoEnvido + 1) % 2;
            puntosParaEnvido = puntosParaEnvido + 20;
        }
        if (resultadoEnvido % 2 == 0) {
            result[0] = pointsOfEnvido;
        } else {
            result[1] = pointsOfEnvido;
        }
        return result;
    }

    /**
     * Chanchada máxima:
     * 
     * @see finDeMano_controlarEnvido
     * 
     * es un copy paste de ese método.
     */
    protected void finDeManoSetearEnvido() {
        int[] result = finDeMano_controlarEnvido();

        int resultadoEnvido = statusTable.resultadoEnvido(primerTurnoNumber);
        TrucoPlayer responsable = teams[resultadoEnvido % 2]
                .getTrucoPlayerNumber(resultadoEnvido / 2);
        if (!statusTable.mostroEnvido(resultadoEnvido)) {
            resultadoEnvido = (resultadoEnvido + 1) % 2;
            puntosParaEnvido = puntosParaEnvido + 20;
        }
        if (resultadoEnvido % 2 == 0) {
            points[0] = points[0] + result[0];
            detalleDePuntaje.add(new PointsDetail(teams[0], puntosParaEnvido,
                    pointsOfEnvido, responsable));
        } else {
            points[1] = points[1] + result[1];
            detalleDePuntaje.add(new PointsDetail(teams[1], puntosParaEnvido,
                    pointsOfEnvido, responsable));
        }
    }

    /**
     * 
     * @param win
     *            el nro de equipo que ganó (0, 1)
     * @throws InvalidPlayExcepcion
     */
    protected void finDeMano(int win) throws InvalidPlayExcepcion {

        if (seTerminoLaMano)
            return;
        seTerminoLaMano = true;

        if (huboRondaDeEnvido)/* si hubo ronda de envido */
            //			finDeMano_controlarEnvido();/*controlar*/
            finDeManoSetearEnvido();
        /* controlas los puntos por flor */
        finDeMano_controlarFlor();
        if (win % 2 == 0) {
            detalleDePuntaje.add(new PointsDetail(teams[0], pointsOfHandDetail,
                    pointsOfHand, pie1));
            points[0] = points[0] + pointsOfHand;
        } else {
            detalleDePuntaje.add(new PointsDetail(teams[1], pointsOfHandDetail,
                    pointsOfHand, pie2));
            points[1] = points[1] + pointsOfHand;
        }
        game.fireEndOfHandEvent();
        displayFinDeMano();
        game.EndOfHandEvent();
    }

    protected void displayFinDeMano() {
        if (huboRondaDeEnvido)
            displayFinDeEnvido();
//        System.out.println("error en el tema de fin de flor puede ser");
        displayFinDeFlor();

    }

    protected void displayFinDeEnvido() {
        int resultadoEnvido = statusTable.resultadoEnvido(primerTurnoNumber);
        TrucoPlayer playerAMostrarCartas = teams[resultadoEnvido % 2]
                .getTrucoPlayerNumber(resultadoEnvido / 2);
        if (statusTable.jugoSusCartasDeEnvido(resultadoEnvido))
            return;
//        System.out.println("porque entro por aca");
        for (int i = 0; i < 2; i++) {
            if (statusTable.jugoSusCartasDeEnvido(resultadoEnvido))
                return;
            TrucoCard cartaAMostrar = statusTable
                    .getCardNoPlayingForEnvido(resultadoEnvido);
            if (cartaAMostrar != null) {
                if (statusTable.jugarCartaOffLine(resultadoEnvido,
                        cartaAMostrar))
                    game.firePlayResponseEvent(playerAMostrarCartas,
                            cartaAMostrar, TrucoEvent.JUGAR_CARTA);
                else
                    System.out
                            .println("--------------------------------------------\nNo puede mostrar las cartas-vericar porque posiblemente es error!! en TrucoHand FinDeMano_ControlarEnvido"
                                    + resultadoEnvido);
            }
        }
//        System.out.println("display Fin de envido con exito");
    }

    protected void displayFinDeFlor() {
        for (int i = 0; i < cantidadDePlayers; i++) {
            if (statusTable.mostroFlor(i)) {
                while (!statusTable.jugoTresCartas(i)) {
                    TrucoCard cartaAMostrar = statusTable.getCardNoPlaying(i);
                    if (cartaAMostrar != null) {
                        statusTable.jugarCartaOffLine(i, cartaAMostrar);
                        /*System.out.println("mostrar "
                                + cartaAMostrar.getValue() + "de:"
                                + cartaAMostrar.getKind() + "del Player"
                                + teams[i % 2].getTrucoPlayerNumber(i / 2));*/
                        game.firePlayResponseEvent(teams[i % 2]
                                .getTrucoPlayerNumber(i / 2), cartaAMostrar,
                                TrucoEvent.JUGAR_CARTA);
                    } else {
                        System.out
                                .println("Error en display fin de Flor, avisar a gente de Truco Game");
                    }
                }
            }
        }
    }

    protected void envidoTurn() throws InvalidPlayExcepcion {
        if (statusTable.estaCerrado(envidoTurnNumber)) {
            nextEnvidoTurn();
            return;
        }
        playTurn = teams[envidoTurnNumber % 2]
                .getTrucoPlayerNumber(envidoTurnNumber / 2);
        /*System.out.println(playTurnNumber + "-" + primerTurnoNumber
                + "quieroycanto=" + quieroYCanto.getName() + ";"
                + playTurn.getName());*/
        if (quieroYCanto == playTurn && envidoTurnNumber == primerTurnoNumber) {
            try {
                jugadadequieroycanto = true;
                play(new TrucoPlay(playTurn, TrucoPlay.CANTO_ENVIDO,
                        statusTable.getValueOfEnvido(primerTurnoNumber)));
            } catch (InvalidPlayExcepcion e) {
                throw e;
            }
        } else {
            game.fireTurnEvent(playTurn, TrucoEvent.TURNO_CANTAR_ENVIDO,
                    statusTable.getValueOfEnvido(envidoTurnNumber));
        }

    }

    protected void nextEnvidoTurn() throws InvalidPlayExcepcion {

        do {
            envidoTurnNumber = (++envidoTurnNumber) % cantidadDePlayers; //siguiente
                                                                         // turno
            if (envidoTurnNumber == primerTurnoNumber) { /*
                                                          * si termino la ronda
                                                          * de envidos
                                                          */
                finDeEnvido();
                return;
            }

        } while (statusTable.estaCerrado(envidoTurnNumber));
        envidoTurn(); //asignar player
    }

    /* no esta habilitado */
    /*
     * void florTurn() throws InvalidPlayExcepcion {
     * if(!statusTable.mostroFlor(florTurnNumber)) throw (new
     * InvalidPlayExcepcion("\n**Excepcion de turno en florTurn **")); playTurn =
     * teams[florTurnNumber%2].getPlayerNumber(florTurnNumber/2); } void
     * nextFlorTurn() throws InvalidPlayExcepcion{
     * while(!statusTable.mostroFlor(florTurnNumber)){
     * florTurnNumber=(florTurnNumber+1)%cantidadDePlayers; if(florTurnNumber ==
     * primerTurnoNumber){ finDeFlor(); return; } } try{ florTurn(); }
     * catch(InvalidPlayExcepcion e){ throw e; } }
     */
    /**
     * Verifica si es posible realizar una jugada.
     * 
     * @param tp
     *            TrucoPlay que quiere ser verificada.
     * @return retorna verdadero en caso de ser validï¿½ la jugada, falso en
     *         caso contrario.
     */
    /* esta funcion tengo que cambiar con choco */
    public boolean esPosibleJugar(TrucoPlay tp) {
        int numPlayer = getNumberOfPlayer(tp.getPlayer());
        if (numPlayer == -1)
            return false;

        if (seTerminoLaMano)/* ya termino la mano */
            return false;

        switch (tp.getType()) {

        case TrucoPlay.ENVIDO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (sePuedeCantarEnvido(tp) > 0)
                return false;
            break;

        case TrucoPlay.REAL_ENVIDO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (sePuedeCantarEnvido(tp) > 0)
                return false;
            break;

        case TrucoPlay.FALTA_ENVIDO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (sePuedeCantarEnvido(tp) > 0)
                return false;
            break;

        case TrucoPlay.TRUCO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju

            if (playTurn != tp.getPlayer()
                    || (estadoActual == PRIMERA_RONDA && playTurn != pie1 && playTurn != pie2))
                return false; /* el player no puede cantar TRUCO ahora */
            if (truco)
                return false;/*
                              * ("Ya se canto truco, no se puede cantar mas
                              * nada"));
                              */
            if (laPalabra != null)
                return false; /* el el equipo no tiene la palabra */
            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA)
                return false; /* no hay estado para cantar */

            break;

        case TrucoPlay.RETRUCO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (playTurn != tp.getPlayer()
                    || (estadoActual == PRIMERA_RONDA && playTurn != pie1 && playTurn != pie2))
                return false; /* el player no puede cantar ahora */
            if (!truco)
                return false; /* tdvia no se canto truco */
            if (retruco)
                return false;/*
                              * ("Ya se canto retruco, no se puede cantar mas
                              * nada"));
                              */
            if (!laPalabra.isPlayerTeam(playTurn))
                return false; /* el el equipo no tiene la palabra */

            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO)
                return false; /* no es posible cantar */

            break;

        case TrucoPlay.VALE_CUATRO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju

            if (playTurn != tp.getPlayer()
                    || (estadoActual == PRIMERA_RONDA && playTurn != pie1 && playTurn != pie2))
                return false; /* el player no puede cantar, */
            if (!retruco)
                return false; /* tdvia no se canto truco */
            if (valecuatro)
                return false; /* ya no se puede nada, se canto vale cuatro */
            if (!laPalabra.isPlayerTeam(playTurn))
                return false; /* el equipo no tiene la palabra */
            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_RETRUCO)
                return false; /* en este estado no se puede cantar */

            break;

        case TrucoPlay.QUIERO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju

            if (playTurn != pie1 && playTurn != pie2)
                return false; /* solamente los pies pueden responder eso */
            if (estadoActual != ENVIDO && estadoActual != REAL_ENVIDO
                    && estadoActual != FALTA_ENVIDO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_RETRUCO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_VALECUATRO)
                return false; //no se reponde nada
            break;

        case TrucoPlay.NO_QUIERO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (playTurn != pie1 && playTurn != pie2)
                return false; /* solamente los pies pueden responder eso */
            if (estadoActual != ENVIDO && estadoActual != REAL_ENVIDO
                    && estadoActual != FALTA_ENVIDO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_RETRUCO
                    && estadoActual != ESPERANDO_RESPUESTA_DEL_VALECUATRO)
                return false; //no se reponde nada

            break;

        case TrucoPlay.ME_VOY_AL_MAZO:
            if (tp.getPlayer() != playTurn)
                return false;
            if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)
                return false; /*
                               * solamente los pies puede llevar al mazo al
                               * equipo
                               */
            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA)
                return false; /*
                               * solo se pueden import al mazo en estado de
                               * juego
                               */
            break;

        case TrucoPlay.JUGAR_CARTA:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA)
                return false; //solamente en juegos de ronda se puede jugar
                              // carta
            /* aqui>>>>> */
            if (!statusTable.puedeJugarCarta(playTurnNumber, tp.getCard()))
                return false;/* no tiene esa carta para jugar o ya jugo */
            break;

        case TrucoPlay.PASO_ENVIDO:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (estadoActual != RONDA_DE_ENVIDO)
                return false; /* no puede pasar envido porque no es ronda */
            break;

        case TrucoPlay.PASO_FLOR:
        //if(estadoActual != RONDA_DE_ENVIDO)
        //    return false;

        case TrucoPlay.CERRARSE:
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                    && estadoActual != TERCERA_RONDA)
                return false; //no es posible cerrase si no es en ronda de
                              // juego.
            if (statusTable.estaCerrado(playTurnNumber))
                return false;

            if (cantidadDePlayers == 2)
                return false; /* es lo mismo que irse al mazo */

            break;

        case TrucoPlay.CANTO_ENVIDO:

            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju

            if (estadoActual != RONDA_DE_ENVIDO)
                return false;

            if (!statusTable.tieneEnvido(envidoTurnNumber, tp.getValue()))
                return false;

            break;

        case TrucoPlay.FLOR:
            /* VERIFICANDO LA FLOR */
            if (tp.getPlayer() != playTurn)
                return false; //no es el turno, no puede ju
            if (!statusTable.puedeCantarFlor(getNumberOfPlayer(playTurn)))
                return false;

            if (!sePuedeCantarFlor)/*
                                    * ya no se puede cantar flor, por que se
                                    * canto envido o no es primera ronda etc.
                                    */
                return false;

            if (estadoActual != PRIMERA_RONDA && estadoActual != ENVIDO
                    && estadoActual != REAL_ENVIDO
                    && estadoActual != FALTA_ENVIDO)
                return false; /* fuera de estos estados no se puede cantar */
            if (statusTable.jugoPrimeraCarta(getNumberOfPlayer(tp.getPlayer())))
                return false;//el player ya jugo su primera carta

            /*//Parche porcino, le caga si el mismo equipo tiene más de una Flor
            //Pero evita que se cante N veces la flor
            if (playTurnNumber % 2 == 0) {
                if (equipo1_canto_flor)
                    return false;
            } else {
                if (equipo2_canto_flor)
                    return false;
            }*/
            if(jugadoresFloridos.contains(tp.getPlayer().getName()))
                return false;

            break;

        default:
            System.out
                    .println("no esta definido el TrucoPlay de tipo "
                            + tp.getType()
                            + "es un error avisar a la Gente de TrucoGame, Metodo esPosibleJugar(TrucoPlay)");
            return false;

        }
        return true;
    }

    /**
     * Realizar una jugada.
     * 
     * @param tp
     *            Jugada(TrucoPlay a ser realizadï¿½).
     * @throws InvalidPlayExcepcion
     *             Tita en caso de detectarse una jugada invï¿½lida.
     */
    public void play(TrucoPlay tp) throws InvalidPlayExcepcion {
        //    	System.out.println("Voy a jugar: " + tp.getType() + " soy: " +
        // tp.getPlayer().getName());
        try {
            switch (tp.getType()) {
            case 62: /* Jugar carta */
                if (tp.getCard() == null) {
                    System.out.println("carta en trucohand es null!");
                } else {
                    /*System.out.println("carta en trucohand"
                            + tp.getCard().getKind() + ","
                            + tp.getCard().getValue());*/
                }
                jugarCarta(tp);
                break;
            case TrucoPlay.ENVIDO:
                cantarEnvido(tp);/* cantar envido */
                break;
            case TrucoPlay.REAL_ENVIDO:
                cantarEnvido(tp);/* cantar real envido */
                break;
            case TrucoPlay.FALTA_ENVIDO:
                cantarEnvido(tp);/* cantar falta envido */
                break;
            case TrucoPlay.QUIERO:
                quiero(tp);/* responder - quiero */
                break;

            case TrucoPlay.NO_QUIERO:
                noQuiero(tp);/* responde - no quiero */
                break;

            case TrucoPlay.CANTO_ENVIDO:
                rondaEnvido(tp); /* canto mi tanto */
                break;

            case TrucoPlay.PASO_ENVIDO:
                rondaEnvido(tp);/* paso o no canto "golpear la mesa" */
                break;
            case TrucoPlay.FLOR:
                cantarFlor(tp);
                break;
            /**
             * case TrucoPlay.CONTRA_FLOR: cantarFlor(tp); break; No esta
             * habilitado
             */
            /*
             * case TrucoPlay.CANTO_FLOR: rondaFlor(tp); break;
             *//* No esta Habilitado */
            case TrucoPlay.TRUCO:
                cantarTruco(tp);
                break;
            case TrucoPlay.RETRUCO:
                cantarTruco(tp);
                break;
            case TrucoPlay.VALE_CUATRO:
                cantarTruco(tp);
                break;
            case TrucoPlay.CERRARSE:
                cerrarse(tp);
                break;

            case TrucoPlay.ME_VOY_AL_MAZO:
                meVoyAlMazo(tp);
                break;
            case TrucoPlay.PLAYER_CONFIRMADO: //Esta es la jugada q viene
                                              // cuando se presiona "Ok' en el
                                              // Table
                game.startHand(tp.getPlayer()); //No se si este es el metodo
                                                // correcto
                break;
            case TrucoPlay.FIN_DE_MANO:
                //Creo q ahora ya no hace falta. Ale 2003.08.25 11:26 PM
                //finDeRonda(); //o tiene q ser nexPlayTurn () ??? o playTurn()
                // ??
                //nextPlayTurn();
                playTurn();
                break;
            default:
                throw (new InvalidPlayExcepcion(
                        "error grave - TrucoPlay no definido: " + tp.getType()));
            }
        } catch (InvalidPlayExcepcion e) {
            throw e;
        }

    }

    protected void meVoyAlMazo() throws InvalidPlayExcepcion {
        if (estadoActual == PRIMERA_RONDA && sePuedeCantarEnvido
                && cartasJugadas < cantidadDePlayers - 1) { //puntos de
                                                            // penalizacion por
                                                            // retirarse en
                                                            // primera ronda,
                                                            // habiendo
                                                            // posibilidad que
                                                            // el equipo
                                                            // contrario cante
                                                            // envido
            pointsOfHandDetail = PointsDetail.RETIRARSE_SIN_DEJAR_QUE_OTROS_CANTEN;
            pointsOfHand = 2; /* puntos de penalizacion por Retirarse */
        }
        finDeMano((playTurnNumber + 1) % 2);
    }

    protected void meVoyAlMazo(TrucoPlay tp) throws InvalidPlayExcepcion {
        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion("No es el turno de ese player"));

        if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)
            throw (new InvalidPlayExcepcion(
                    "solamente los pies pueden retirarse"));

        if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                && estadoActual != TERCERA_RONDA)
            throw (new InvalidPlayExcepcion(
                    "No es posible cerrarse en este momento"));

        game.firePlayEvent(tp.getPlayer(), TrucoEvent.ME_VOY_AL_MAZO); /* eventos */

        if (tp.getPlayer() == pie1)
            playTurnNumber = pieTeam1Number;
        else
            playTurnNumber = pieTeam2Number;

        meVoyAlMazo();

    }

    protected void cerrarse(TrucoPlay tp) throws InvalidPlayExcepcion {

        int cerradosDelTeam1 = 0;
        int cerradosDelTeam2 = 0;

        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion("No es el turno de ese player"));
        if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                && estadoActual != TERCERA_RONDA)
            throw (new InvalidPlayExcepcion(
                    "No es posible cerrarse en este momento"));
        if (!statusTable.seCerro(playTurnNumber))
            throw (new InvalidPlayExcepcion(
                    "No pudo cerrarse - Avisar a la gente de TrucoGame"));

        for (int i = 0; i < cantidadDePlayers; i++) {
            if (statusTable.estaCerrado(i)) {
                if (i % 2 == 0)
                    cerradosDelTeam1++;
                else
                    cerradosDelTeam2++;
            }
        }
        if (cerradosDelTeam1 == cantidadDePlayers / 2) {
            playTurnNumber = pieTeam1Number;
            game.firePlayEvent(tp.getPlayer(), tp.getType());
            meVoyAlMazo();
            return;
        }
        if (cerradosDelTeam2 == cantidadDePlayers / 2) {
            playTurnNumber = pieTeam2Number;
            game.firePlayEvent(tp.getPlayer(), tp.getType());
            meVoyAlMazo();
            return;
        }
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        nextPlayTurn();
    }

    protected void rondaEnvido(TrucoPlay tp) throws InvalidPlayExcepcion {

        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion(tp.getPlayer().getName()
                    + "TrucoHand - rondaEnvido(TrucoPlay): no es tu turno"
                    + playTurn.getName()));
        if (estadoActual != RONDA_DE_ENVIDO)
            throw (new InvalidPlayExcepcion(
                    "TrucoHand - rondaEnvido(TrucoPlay): no es ronda de envido"
                            + estadoActual));

        if (tp.getType() == TrucoPlay.CANTO_ENVIDO) {
            if (!statusTable.jugarEnvido(envidoTurnNumber, tp.getValue()))
                throw (new InvalidPlayExcepcion(
                        tp.getPlayer().getName()
                                + "TrucoHand - rondaEnvido(TrucoPlay): no puedes cantar ese envido: ("
                                + tp.getValue() + ")"));
            if (jugadadequieroycanto == true) {
                game.firePlayResponseEvent(tp.getPlayer(),
                        TrucoEvent.CANTO_ENVIDO, tp.getValue());
            } else {
                game.firePlayEvent(tp.getPlayer(), TrucoEvent.CANTO_ENVIDO, tp
                        .getValue());
            }
            jugadadequieroycanto = false;
        } else {
//            System.out.println("paso el Envido");
            game.firePlayEvent(tp.getPlayer(), TrucoEvent.PASO_ENVIDO);
        }
        nextEnvidoTurn();
    }/*
      * No esta Habilitado protected void rondaFlor(TrucoPlay tp) throws
      * InvalidPlayExcepcion{ if(tp.getPlayer() != playTurn) throw (new
      * InvalidPlayExcepcion("No es el turno de ese jugador")); if(estadoActual !=
      * RONDA_DE_FLOR) throw (new InvalidPlayExcepcion("No es ronda de flor"));
      * 
      *  }
      */

    protected void quiero(TrucoPlay tp) throws InvalidPlayExcepcion {

        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion(tp.getPlayer().getName()
                    + "no es el turno del player"));
        if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)
            throw (new InvalidPlayExcepcion(
                    "solamente el pie puede responder: el pie es"
                            + pie1.getName() + pie2.getName()));
        try {
            switch (estadoActual) {
            case ENVIDO:
                quieroEnvido(tp);
                break;
            case REAL_ENVIDO:
                quieroEnvido(tp);
                break;
            case FALTA_ENVIDO:
                quieroEnvido(tp);
                break;
            /*
             * case CONTRAFLOR: quieroContraflor(tp); break; case
             * CONTRAFLOR_AL_RESTO: quieroContraflor(tp); break;
             */
            case ESPERANDO_RESPUESTA_DEL_TRUCO:
                quieroTruco(tp);
                break;
            case ESPERANDO_RESPUESTA_DEL_RETRUCO:
                quieroTruco(tp);
                break;
            case ESPERANDO_RESPUESTA_DEL_VALECUATRO:
                quieroTruco(tp);
                break;
            default:
                throw (new InvalidPlayExcepcion(
                        "TrucoHand quiero(TrucoPlay): No se responde a nada"));

            }
        } catch (InvalidPlayExcepcion e) {
            throw e;
        }
    }

    protected void quieroTruco(TrucoPlay tp) throws InvalidPlayExcepcion {
        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion("no es el turno de ese jugador"));
        if (playTurn != pie1 && playTurn != pie2)
            throw (new InvalidPlayExcepcion(
                    "solamente el pie puede responder el quiero"));
        sePuedeCantarEnvido = false;
        if (estadoActual == ESPERANDO_RESPUESTA_DEL_TRUCO) {
            pointsOfHand = 2;
            pointsOfHandDetail = PointsDetail.TRUCO;
        }
        if (estadoActual == ESPERANDO_RESPUESTA_DEL_RETRUCO) {
            pointsOfHand = 3;
            pointsOfHandDetail = PointsDetail.RETRUCO;
        }
        if (estadoActual == ESPERANDO_RESPUESTA_DEL_VALECUATRO) {
            pointsOfHand = 4;
            pointsOfHandDetail = PointsDetail.VALECUATRO;

        }
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        volverAEstadoDeJuego();
        playTurn();
    }

    /*
     * NoHabilitadoprotected void quieroContraflor(TrucoPlay tp){
     * if(estadoActual != CONTRAFLOR)
     * 
     * game.firePlayEvent(tp.getPlayer(),tp.getType()); estadoActual =
     * RONDA_DE_FLOR; }
     */
    protected void quieroEnvido(TrucoPlay tp) throws InvalidPlayExcepcion {

        sePuedeCantarEnvido = false; /* ya no se puede volver cantar envido */
        sePuedeCantarFlor = false; /* ya no se puede cantar flor */
        huboRondaDeEnvido = true;
        if (estadoActual == FALTA_ENVIDO) {
            pointsOfEnvido = game.getFaltear();
            puntosParaEnvido = PointsDetail.FALTA_ENVIDO;
        } else {
            pointsOfEnvido = numberOfEnvidos * 2 + numberOfRealEnvidos * 3; //puntajes
                                                                            // que
                                                                            // se
                                                                            // jugaran
            if (numberOfEnvidos == 1 && numberOfRealEnvidos == 0)
                puntosParaEnvido = PointsDetail.ENVIDO;
            if (numberOfEnvidos == 2 && numberOfRealEnvidos == 0)
                puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO;
            if (numberOfEnvidos == 3 && numberOfRealEnvidos == 0)
                puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO_ENVIDO;
            if (numberOfEnvidos == 0 && numberOfRealEnvidos == 1)
                puntosParaEnvido = PointsDetail.REAL_ENVIDO;
            if (numberOfEnvidos == 2 && numberOfRealEnvidos == 1)
                puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO_REALENVIDO;
            if (numberOfEnvidos == 1 && numberOfRealEnvidos == 2)
                puntosParaEnvido = PointsDetail.ENVIDO_REALENVIDO_REALENVIDO;
            if (numberOfEnvidos == 0 && numberOfRealEnvidos == 3)
                puntosParaEnvido = PointsDetail.REALENVIDO_REALENVIDO_REALENVIDO;
            if (numberOfEnvidos == 0 && numberOfRealEnvidos == 2)
                puntosParaEnvido = PointsDetail.REALENVIDO_REALENVIDO;
            if (numberOfEnvidos == 1 && numberOfRealEnvidos == 1)
                puntosParaEnvido = PointsDetail.ENVIDO_REALENVIDO;
        }

        estadoActual = RONDA_DE_ENVIDO;
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        quieroYCanto = tp.getPlayer();
        envidoTurn();
    }

    protected void noQuiero(TrucoPlay tp) throws InvalidPlayExcepcion {
        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion("no es el turno de ese player"));
        if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)
            throw (new InvalidPlayExcepcion("solamente el pie puede responder"
                    + pie1.getName() + pie2.getName()));

        try {
            switch (estadoActual) {
            case ENVIDO:
                noQuieroEnvido(tp);
                break;
            case REAL_ENVIDO:
                noQuieroEnvido(tp);
                break;
            case FALTA_ENVIDO:
                noQuieroEnvido(tp);
                break;
            case ESPERANDO_RESPUESTA_DEL_TRUCO:
                noQuieroTruco(tp);
                break;
            case ESPERANDO_RESPUESTA_DEL_RETRUCO:
                noQuieroTruco(tp);
                break;
            case ESPERANDO_RESPUESTA_DEL_VALECUATRO:
                noQuieroTruco(tp);
                break;
            }
        } catch (InvalidPlayExcepcion e) {
            throw e;
        }
    }

    protected void noQuieroEnvido(TrucoPlay tp) throws InvalidPlayExcepcion {

        if (estadoActual != ENVIDO && estadoActual != REAL_ENVIDO
                && estadoActual != FALTA_ENVIDO)
            throw (new InvalidPlayExcepcion("No se esta respondiendo a nada"));
        sePuedeCantarEnvido = false;
        sePuedeCantarFlor = false;

        if ((numberOfEnvidos + numberOfRealEnvidos) == 1) {
            pointsOfEnvido = 1;
            puntosParaEnvido = PointsDetail.REAL_ENVIDO_NOQUERIDO;
            if (numberOfEnvidos == 1)
                puntosParaEnvido = PointsDetail.ENVIDO_NOQUERIDO;
            if (estadoActual == FALTA_ENVIDO) {
                pointsOfEnvido = numberOfEnvidos * 2 + numberOfRealEnvidos * 3;
                puntosParaEnvido = PointsDetail.FALTA_ENVIDO_NOQUERIDO;

            }
        } else {
            if (estadoActual == FALTA_ENVIDO) {
                if ((numberOfEnvidos + numberOfRealEnvidos) == 0)
                    pointsOfEnvido = 1;
                else
                    pointsOfEnvido = numberOfEnvidos * 2 + numberOfRealEnvidos
                            * 3; //puntajes que se jugaran
                puntosParaEnvido = PointsDetail.FALTA_ENVIDO_NOQUERIDO;
            } else {
                if (estadoActual == ENVIDO)
                    pointsOfEnvido = (numberOfEnvidos - 1) * 2
                            + numberOfRealEnvidos * 3;
                if (estadoActual == REAL_ENVIDO)
                    pointsOfEnvido = (numberOfRealEnvidos - 1) * 3
                            + numberOfEnvidos * 2;

                if (numberOfEnvidos == 1 && numberOfRealEnvidos == 0)
                    puntosParaEnvido = PointsDetail.ENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 2 && numberOfRealEnvidos == 0)
                    puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 3 && numberOfRealEnvidos == 0)
                    puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO_ENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 0 && numberOfRealEnvidos == 1)
                    puntosParaEnvido = PointsDetail.REAL_ENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 2 && numberOfRealEnvidos == 1)
                    puntosParaEnvido = PointsDetail.ENVIDO_ENVIDO_REALENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 1 && numberOfRealEnvidos == 2)
                    puntosParaEnvido = PointsDetail.ENVIDO_REALENVIDO_REALENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 0 && numberOfRealEnvidos == 3)
                    puntosParaEnvido = PointsDetail.REALENVIDO_REALENVIDO_REALENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 0 && numberOfRealEnvidos == 2)
                    puntosParaEnvido = PointsDetail.REALENVIDO_REALENVIDO_NOQUERIDO;
                if (numberOfEnvidos == 1 && numberOfRealEnvidos == 1)
                    puntosParaEnvido = PointsDetail.ENVIDO_REALENVIDO_NOQUERIDO;
            }
        }
        if (tp.getPlayer() == pie1) { //al no querer ya se asigna los puntos
            //puntajes.add(new String(pointsOfEnvido + "al equipo: " +
            // teams[1).getName + " por Envido no querido");
            detalleDePuntaje.add(new PointsDetail(teams[1], puntosParaEnvido,
                    pointsOfEnvido, pie1));
            points[1] = pointsOfEnvido + points[1];
        }
        if (tp.getPlayer() == pie2) {
            //puntajes.add(new String(pointsOfEnvido + "al equipo: " +
            // teams[0).getName + " por Envido no querido");
            detalleDePuntaje.add(new PointsDetail(teams[0], puntosParaEnvido,
                    pointsOfEnvido, pie2));
            points[0] = pointsOfEnvido + points[0];
        }
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        if (elEnvidoEstaPrimero) {
            elEnvidoEstaPrimero();
            return;
        } else {
            volverAEstadoDeJuego();
            playTurn();
        }
    }

    protected void noQuieroTruco(TrucoPlay tp) throws InvalidPlayExcepcion {
        if (tp.getPlayer() != playTurn)
            throw (new InvalidPlayExcepcion("No es el turno de ese player"));
        if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)
            throw (new InvalidPlayExcepcion(
                    "Solamente los pies pueden aceptar o no"));
        if (estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO
                && estadoActual != ESPERANDO_RESPUESTA_DEL_RETRUCO
                && estadoActual != ESPERANDO_RESPUESTA_DEL_VALECUATRO)
            throw (new InvalidPlayExcepcion(
                    "noQuieroTruco - No se esta respondiendo a nada"));

        game.firePlayEvent(tp.getPlayer(), tp.getType());
        if (pointsOfHand == 1)
            pointsOfHandDetail = PointsDetail.TRUCO_NO_QUERIDO;
        if (pointsOfHand == 2)
            pointsOfHandDetail = PointsDetail.RETRUCO_NO_QUERIDO;
        if (pointsOfHand == 3)
            pointsOfHandDetail = PointsDetail.VALECUATRO_NO_QUERIDO;

        if (teams[0].isPlayerTeam(playTurn)) {

            finDeMano(1);
        } else {

            finDeMano(0);
        }

    }

    /* el player juega una carta */
    protected void jugarCarta(TrucoPlay tp) throws InvalidPlayExcepcion { //alguien
                                                                          // juega
                                                                          // una
                                                                          // carta

        if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                && estadoActual != TERCERA_RONDA)
            throw (new InvalidPlayExcepcion(
                    "TrucoHand - jugarCarta(TrucoPlay ) > No se puede jugar carta"));
        if (playTurn != tp.getPlayer()) {
            System.out.println("Es el turno de: " + playTurn.getName());
            System.out.println("Quiere jugar: " + tp.getPlayer().getName());

            throw (new InvalidPlayExcepcion(
                    "TrucoHand - jugarCarta(TrucoPlay ) > No es el turno de ese player"));
        }
        //if(!playTurn.getName().trim().equals(tp.getPlayer().getName().trim()))

//        System.out.println("probando en trucohand la carta");
        if (statusTable == null) {
            System.out.println("el statusTable es null ;(");
        }
        if (!statusTable.jugarCarta(playTurnNumber, tp.getCard())) //*****************************funcion
                                                                   // que
                                                                   // necesito
                                                                   // de choco
            throw (new InvalidPlayExcepcion(
                    "TrucoHand - jugarCarta(TrucoPlay ) > el player no puede jugar esa carta"));

        //if (this instanceof TrucoHandClient) {
        //if ()
        game.firePlayEvent(playTurn, tp.getCard(), TrucoEvent.JUGAR_CARTA);
        /*
         * } else {
         * game.firePlayResponseEvent(playTurn,tp.getCard(),TrucoEvent.JUGAR_CARTA); }
         */
        nextPlayTurn();
    }

    protected int sePuedeCantarEnvido(TrucoPlay tp) {

        if (!sePuedeCantarEnvido) /* ya no se puede cantar */
            return 1;

        if (playTurn != tp.getPlayer())/* no es el turno de ese player */
            return 2;

        if (tp.getPlayer() != pie1 && tp.getPlayer() != pie2)/*
                                                              * no es pie el que
                                                              * canta
                                                              */
            return 3;

        if ((numberOfEnvidos + numberOfRealEnvidos) >= 3
                && tp.getType() != TrucoPlay.FALTA_ENVIDO)/*
                                                           * ya se cantaron mas
                                                           * de
                                                           */
            return 4;

        if (estadoActual != PRIMERA_RONDA && estadoActual != ENVIDO
                && estadoActual != REAL_ENVIDO
                && estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO)
            return 5;

        if (statusTable.jugoPrimeraCarta(getNumberOfPlayer(tp.getPlayer()))
                && estadoActual != ENVIDO && estadoActual != REAL_ENVIDO
                && estadoActual != FALTA_ENVIDO)
            return 6;
        
        return 0;// si se puede.
    }

    /* El Player canta envido, real envido o falta envido>>>>>>> */

    protected void cantarEnvido(TrucoPlay tp) throws InvalidPlayExcepcion { //cantar
                                                                            // envido

        if (sePuedeCantarEnvido(tp) > 0)
            throw (new InvalidPlayExcepcion("error al cantar el envido"
                    + sePuedeCantarEnvido(tp)));

        byte respuesta = ENVIDO; //que respuesta enviar

        switch (estadoActual) {
        case PRIMERA_RONDA:
            if (tp.getType() == TrucoPlay.ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_ENVIDO;
                numberOfEnvidos = 1; //primer envido
                estadoActual = ENVIDO;
            }
            if (tp.getType() == TrucoPlay.REAL_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_REALENVIDO;
                estadoActual = REAL_ENVIDO;
                numberOfRealEnvidos = 1; //primer real
            }
            if (tp.getType() == TrucoPlay.FALTA_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_FALTAENVIDO;
                estadoActual = FALTA_ENVIDO;
            }
            break;
        case ENVIDO: //ya se habria cantado envido

            sePuedeCantarFlor = false; //ya no se puede cantar flor

            if (tp.getType() == TrucoPlay.ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_ENVIDO;
                numberOfEnvidos = numberOfEnvidos + 1; //otro envido
                estadoActual = ENVIDO;
            }
            if (tp.getType() == TrucoPlay.REAL_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_REALENVIDO;
                numberOfRealEnvidos = numberOfRealEnvidos + 1; //otro real
                estadoActual = REAL_ENVIDO;
            }
            if (tp.getType() == TrucoPlay.FALTA_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_FALTAENVIDO;
                estadoActual = FALTA_ENVIDO;
            }
            break;

        case REAL_ENVIDO:
            sePuedeCantarFlor = false; //ya no se puede cantar flor

            if (tp.getType() == TrucoPlay.ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_ENVIDO;
                numberOfEnvidos = numberOfEnvidos + 1; //otro envido
                estadoActual = ENVIDO;
            }
            if (tp.getType() == TrucoPlay.REAL_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_REALENVIDO;
                numberOfRealEnvidos = numberOfRealEnvidos + 1; //otro real
                estadoActual = REAL_ENVIDO;
            }
            if (tp.getType() == TrucoPlay.FALTA_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_FALTAENVIDO;
                estadoActual = FALTA_ENVIDO;
            }
            break;
        case FALTA_ENVIDO:
            throw (new InvalidPlayExcepcion(
                    "TrucoHand - cantarEnvido(TrucoPlay) : el player no puede cantar falta envido porque ya se le canto"));

        case ESPERANDO_RESPUESTA_DEL_TRUCO:

            sePuedeCantarFlor = false; //ya no se puede cantar flor
            elEnvidoEstaPrimero = true; //avisar que tiene que responder a
                                        // truco despues de la ronda

            if (tp.getType() == TrucoPlay.ENVIDO) {

                respuesta = TrucoEvent.TURNO_RESPONDER_ENVIDO;
                numberOfEnvidos = 1; //primer envido
                estadoActual = ENVIDO;
            }
            if (tp.getType() == TrucoPlay.REAL_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_REALENVIDO;
                estadoActual = REAL_ENVIDO;
                numberOfRealEnvidos = 1; //primer real
            }
            if (tp.getType() == TrucoPlay.FALTA_ENVIDO) {
                respuesta = TrucoEvent.TURNO_RESPONDER_FALTAENVIDO;
                estadoActual = FALTA_ENVIDO;
            }
            break;
        default:
            throw (new InvalidPlayExcepcion(
                    "TrucoHand - cantarEnvido(TrucoPlay) : en este estado no se puede cantar envido"));

        }
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        esperarRespuesta();
        int numeroDePlayer = getNumberOfPlayer(playTurn);
        if (primerTurnoNumber == numeroDePlayer) {
            game.fireTurnEvent(playTurn, respuesta, statusTable
                    .getValueOfEnvido(numeroDePlayer));
        } else {
            game.fireTurnEvent(playTurn, respuesta, -1);
        }

    }

    /* cambiar turno y esperar respuesta */
    protected void esperarRespuesta() { //asignar turnos al pie

        if (teams[0].isPlayerTeam(playTurn)) {
            playTurn = pie2;
        } else {
            playTurn = pie1;
        }
    }

    protected int getNumberOfPlayer(TrucoPlayer tp) {
        // RSHK - cambiï¿½ (decia getNumberOfPLayer(tp)) porque no habï¿½a
        // definiciï¿½n correspondiente
        int numero = -1;
        if (teams[0].isPlayerTeam(tp)) {
            numero = teams[0].getNumberOfPlayer(tp);
            return numero * 2;
        }
        if (teams[1].isPlayerTeam(tp)) {
            numero = teams[1].getNumberOfPlayer(tp);
            return numero * 2 + 1;
        }
        //System.out.println("opa, error grave - getNumberOfPlayer - Avisar a
        // la gente de TrucoGame+no se encontro"+tp.getName());
        return -1;

    }

    protected void cantarFlor(TrucoPlay tp) throws InvalidPlayExcepcion {

        if (tp.getPlayer() != playTurn) /* no es el turno del jugador */
            throw (new InvalidPlayExcepcion("No es el turno del jugador"));
        if (!sePuedeCantarFlor) /* ya acepto envido o algun error porahi */
            throw (new InvalidPlayExcepcion("ya no se puede cantar flor"));
        if (!statusTable.cantaFlor(getNumberOfPlayer(tp.getPlayer())))
            throw (new InvalidPlayExcepcion("el player no puede cantar flor"));
        if (estadoActual != PRIMERA_RONDA && estadoActual != ENVIDO
                && estadoActual != REAL_ENVIDO && estadoActual != FALTA_ENVIDO)
            throw (new InvalidPlayExcepcion("en este estado no se puede cantar"
                    + estadoActual));
        if (statusTable.jugoPrimeraCarta(getNumberOfPlayer(tp.getPlayer())))
            throw (new InvalidPlayExcepcion(
                    "Este player ya no puede cantar mas, ya jugo cartas"
                            + estadoActual));

        sePuedeCantarEnvido = false; /* ya no se puede cantar envido */

        jugadoresFloridos.add(tp.getPlayer().getName());
        if (playTurnNumber % 2 == 0)
            equipo1_canto_flor = true;
        else
            equipo2_canto_flor = true;

        game.firePlayEvent(tp.getPlayer(), tp.getType());
        volverAEstadoDeJuego();
        playTurn();

    }

    /* No esta habilitado>> */
    /*
     * public void cantarContraFlor(TrucoPlay tp) throws InvalidPlayExcepcion{
     * 
     * if(tp.getPlayer() != playTurn) /*no es el turno del jugador throw(new
     * InvalidPlayExcepcion("No es el turno del jugador"));
     * if(!sePuedeCantarFlor) /*ya acepto envido o algun error porahi throw(new
     * InvalidPlayExcepcion("ya no se puede cantar flor"));
     * if(!statusTable.CantaFlor(playTurnNumber)) throw(new
     * InvalidPlayExcepcion("el player no puede cantar flor"));
     * if(playTurnNumber%2 == 0 && equipo2_canto_flor==false) /*canta equipo1 y
     * equipo 2 tdvia tiene flor throw(new InvalidPlayExcepcion("el equipo
     * contrario tdvia canto flor"));
     * 
     * if(playTurnNumber%2 != 0 && equipo1_canto_flor==false)/*canta equipo1 y
     * equipo 2 tdvia tiene throw(new InvalidPlayExcepcion("el equipo contrario
     * tdvia canto flor"));
     * 
     * contraFlor=true; sePuedeCantarEnvido=false; /*ya no se puede cantar
     * envido
     * 
     * game.firePlayEvent(tp.getPlayer(),tp.getType()); esperarRespuesta();
     * game.fireTurnEvent(playTurn,TrucoEvent.TURNO_RESPONDER_CONTRAFLOR); }
     */
    /*
     * public void contraFlor(TrucoPlay tp) throws InvalidPlayExcepcion{
     *  }
     */
    /**
     * @param tp
     * @throws InvalidPlayExcepcion
     */
    protected void cantarTruco(TrucoPlay tp) throws InvalidPlayExcepcion {
        if (playTurn != tp.getPlayer()
                || (estadoActual == PRIMERA_RONDA && playTurn != pie1 && playTurn != pie2))
            throw (new InvalidPlayExcepcion("el jugador no puede cantar truco"));
        if (valecuatro)
            throw (new InvalidPlayExcepcion(
                    "Ya se canto todo, no se puede cantar mas nada"));
        if (laPalabra != null && !laPalabra.isPlayerTeam(playTurn))
            throw (new InvalidPlayExcepcion(
                    "ese equipo no puede cantar truco - no tiene la palabra"));

        if (estadoActual != PRIMERA_RONDA && estadoActual != SEGUNDA_RONDA
                && estadoActual != TERCERA_RONDA
                && estadoActual != ESPERANDO_RESPUESTA_DEL_TRUCO
                && estadoActual != ESPERANDO_RESPUESTA_DEL_RETRUCO
                && estadoActual != ESPERANDO_RESPUESTA_DEL_VALECUATRO)
            throw (new InvalidPlayExcepcion("no es posible cantar truco"
                    + estadoActual));

        byte typeTurn = TrucoEvent.TRUCO;
        switch (tp.getType()) {
        case TrucoPlay.TRUCO:
            if (truco)
                throw (new InvalidPlayExcepcion("Ya se canto truco"));//ya se
                                                                      // canto
                                                                      // truco
            truco = true; //se canto truco
            laPalabra = cambiarEquipo(playTurn);//cambio la palabra
            estadoActual = ESPERANDO_RESPUESTA_DEL_TRUCO;
            typeTurn = TrucoEvent.TURNO_RESPONDER_TRUCO;
            break;

        case TrucoPlay.RETRUCO:
            if (retruco)
                throw (new InvalidPlayExcepcion("Ya se canto retruco"));//ya se
                                                                        // canto
                                                                        // retruco
            if (!truco)
                throw (new InvalidPlayExcepcion("Todavia no se canto truco"));//ya
                                                                              // se
                                                                              // canto
                                                                              // retruco

            pointsOfHand = 2;
            retruco = true;//se canto retruco
            laPalabra = cambiarEquipo(playTurn);//cambio la palabra
            estadoActual = ESPERANDO_RESPUESTA_DEL_RETRUCO;
            typeTurn = TrucoEvent.TURNO_RESPONDER_RETRUCO;
            sePuedeCantarEnvido = false;
            break;

        case TrucoPlay.VALE_CUATRO:
            if (valecuatro)
                throw (new InvalidPlayExcepcion("Ya se canto vale Cuatro"));//ya
                                                                            // se
                                                                            // canto
                                                                            // vale
                                                                            // cuatro
            if (!retruco)
                throw (new InvalidPlayExcepcion("Todavia no se canto retruco"));//ya
                                                                                // se
                                                                                // canto
                                                                                // retruco
            valecuatro = true; //se canto valecuatro
            pointsOfHand = 3;
            estadoActual = ESPERANDO_RESPUESTA_DEL_VALECUATRO;
            typeTurn = TrucoEvent.TURNO_RESPONDER_RETRUCO;
            sePuedeCantarEnvido = false;
            break;
        }
        game.firePlayEvent(tp.getPlayer(), tp.getType());
        esperarRespuesta();
        game.fireTurnEvent(playTurn, typeTurn);

    }

    protected void finDeEnvido() throws InvalidPlayExcepcion {
        if (elEnvidoEstaPrimero) {
            elEnvidoEstaPrimero();
            return;
        }
        volverAEstadoDeJuego();
        // puntos actuales
        //puntos obtenidos por envido.
        int[] resultEnvido = finDeMano_controlarEnvido();

        if (game.getGameTotalPoints(teams[0]) + resultEnvido[0] >= game.getGamePoints()
                || game.getGameTotalPoints(teams[1]) + resultEnvido[1] >= game.getGamePoints()) {
            seTerminoLaMano = true;
            displayFinDeMano();
            finDeManoSetearEnvido();
            game.fireEndOfHandEvent();
            game.EndOfHandEvent();
            //TODO: Ver con Julio que tiene esto que ver con el findemano
        } else
            playTurn();
    }

    protected Team cambiarEquipo(TrucoPlayer pl) {
        if (teams[0].isPlayerTeam(pl))
            return teams[1];
        return teams[0];
    }

    /*
     * void finDeFlor(){ int puntos; if(contraFlorAlResto){
     * if(statusTable.resultadoFlor()%2== 0){ puntos = game.alResto(0);
     * /*cuantos puntos le flata para ganar points[0] = puntos +
     * points[0];/*sumar puntos //puntajes.add(new String("equipo: " +
     * teams.getName(0) + " gano" + puntos + " puntos de Contra Flor Al
     * Resto")); /*lista de puntajes
     *  } else{ puntos = game.alResto(1);/*cuantos puntos le falta para ganar
     * points[1] = puntos + points[1]; /*sumar puntos //puntajes.add(new
     * String("equipo: " + teams.getName(0) + " gano" + puntos + " puntos de
     * Contra Flor Al Resto")); } } else{ if(statusTable.resultadoFlor()%2 ==
     * 0){ puntos = statusTable.cuantasFloresGano()*3;/*3 puntos por cada flor
     * ganada points[0] = puntos + points[0];/*sumar puntos
     */
    //puntajes.add(new String("equipo: " + teams.getName(0) + " gano" + puntos
    // + " puntos de Contra Flor));
    /*
     * listade puntajes } else{ puntos = statusTable.cuantasFloresGano()*3;/*3
     * puntos por cada flor ganada points[1] = puntos + points[1]; /*sumar
     * puntos //puntajes.add(new String("equipo: " + teams.getName(0) + " gano" +
     * puntos + " puntos de Contra Flor)); } }
     * 
     * volverAEstadoDeJuego(); playTurn(); }
     */
    protected void elEnvidoEstaPrimero() {
        if (!elEnvidoEstaPrimero)
            return;
        elEnvidoEstaPrimero = false;
        if (laPalabra.isPlayerTeam(pie1))
            playTurn = pie2;
        else
            playTurn = pie1;

        estadoActual = ESPERANDO_RESPUESTA_DEL_TRUCO;
        game.firePlayResponseEvent(playTurn, TrucoEvent.TRUCO);
        esperarRespuesta();
        game.fireTurnEvent(playTurn, TrucoEvent.TURNO_RESPONDER_TRUCO);
    }

    protected void volverAEstadoDeJuego() {
        switch (numeroDeRonda) {
        case 1:
            estadoActual = PRIMERA_RONDA;
            break;
        case 2:
            estadoActual = SEGUNDA_RONDA;
            break;
        case 3:
            estadoActual = TERCERA_RONDA;
            break;
        }
    }

    /**
     * retorna el puntaje de un equipo en mano
     * 
     * @param team
     *            el equipo de quien se quiere saber
     * @return Valor del puntaje en la mano del team.
     */
    public int getPointsOfTeam(int team) {
        if (team % 2 == 0)
            return (points[0]);
        return points[1];
    }

    /**
     * Retorta el valor del Envido que puede cantar el TrucoPlayer
     * 
     * @param tp
     *            TrucoPlayer del quien se devolverï¿½ el valor de su Envido.
     * @return Valor del Envido del TrucoPlayer.
     */
    public int getValueOfEnvido(TrucoPlayer tp) {
        int numOfPlayer = getNumberOfPlayer(tp);
        return statusTable.getValueOfEnvido(numOfPlayer);

    }

    public Vector getPointsDetail() {
        return detalleDePuntaje;
    }

    public TrucoCard getCard(byte mykind, byte myvalue) {
        return statusTable.getCard(mykind, myvalue);
    }
}