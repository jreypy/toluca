/*
 * TrucoHandClient.java
 *
 * Created on 5 de junio de 2003, 20:34
 */

package py.edu.uca.fcyt.toluca.game;

import java.util.Vector;
import java.util.logging.Logger;

import py.edu.uca.fcyt.toluca.TolucaConstants;
import py.edu.uca.fcyt.toluca.statusGame.statusGameCliente.TrucoStatusTableCliente;

/**
 * 
 * @author Julio Rey
 */

public class TrucoHandClient extends TrucoHand {

    protected Logger logeador = Logger.getLogger(TrucoHandClient.class
            .getName());

    /** Creates a new instance of TrucoHandClient */
    protected py.edu.uca.fcyt.toluca.statusGame.statusGameCliente.TrucoStatusTableCliente statusTableClie;

    private TrucoGameClient gameclie;

    private boolean cartasRecibidasDePlayers[];

    private boolean runTrucoHandClient = false;

    private int cantidadDeCartasDePlayersRecibidas;

    private Vector miMazo;

    public TrucoHandClient(TrucoGameClient game, int reparteCartas) {
        //super(game, reparteCartas); No se tiene q repartir las cartas, tiene
        // q venir de un evento del serva
        this.gameclie = game;
        this.game = game;

        cantidadDePlayers = game.getNumberOfPlayers();
        miMazo = new Vector();
        cartasRecibidasDePlayers = new boolean[cantidadDePlayers];
        for (int i = 0; i < cantidadDePlayers; i++)
            cartasRecibidasDePlayers[i] = false;
        primerTurnoNumber = (reparteCartas + 1) % cantidadDePlayers;
        playTurnNumber = primerTurnoNumber; //quien juega la primera carta
        envidoTurnNumber = primerTurnoNumber; //quien canta el primer envido en
                                              // caso de cantar
        florTurnNumber = primerTurnoNumber; //quien canta su flor en caso de
                                            // haber
        cantidadDeCartasDePlayersRecibidas = 0;
        statusTableClie = new TrucoStatusTableCliente(cantidadDePlayers); //se
                                                                          // crea
                                                                          // un
                                                                          // estado
                                                                          // de
                                                                          // la
                                                                          // mesa
        statusTable = statusTableClie;
        detalleDePuntaje = new Vector();
        for (int i = 0; i < 3; i++)
            winRound[i] = -1;
        points[0] = 0; /* Cerar los puntajes. */
        points[0] = 0;
        teams[0] = game.getTeam(0);
        teams[1] = game.getTeam(1);
        getPies(); //obtener los pies de la mano
    }

    public void recibirCartas(TrucoPlayer tp, TrucoCard cartas[]) {

        int numeroDelPlayer = getNumberOfPlayer(tp);
        if (cartasRecibidasDePlayers[numeroDelPlayer])
            return;
        for (int i = 0; i < 3; i++) {
            miMazo.add(cartas[i]);
        }
        cartasRecibidasDePlayers[numeroDelPlayer] = true;
        statusTableClie.recibirCartas(numeroDelPlayer, cartas);
        cantidadDeCartasDePlayersRecibidas++;
        if (cantidadDeCartasDePlayersRecibidas == cantidadDePlayers)
            runTrucoHandClient = true;
        /*logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,
                "TrucoHandCLIENTE recibe cartas :)");
        logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "para: "
                + tp.getName());*/
    }

    public boolean isRunHand() {
        return runTrucoHandClient;
    }

    public TrucoCard getCard(byte myKind, byte myValue) {
        TrucoCard carta;
        for (int i = 0; i < miMazo.size(); i++) {
            carta = (TrucoCard) (miMazo.get(i));
            if (carta.getKind() == myKind && carta.getValue() == myValue)
                return carta;
        }
        return null;
    }

}