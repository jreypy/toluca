package py.edu.uca.fcyt.toluca.game;

import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;

import java.awt.dnd.DragGestureEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public abstract class TrucoGame {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TrucoGame.class);
    private LinkedList listenerlist = new LinkedList(); //instancia de la lista de Trucolistener; //lista de todos los listener
    protected int tableNumber;
    protected int numberOfHand; //numero de mano actual
    protected TrucoTeam[] teams = new TrucoTeam[2]; //equipos que juegan
    protected int numberOfPlayers; //cantidad de jugadores


    abstract public void startGame();

    abstract public void startHand(TrucoPlayer tPlayer);

    abstract public boolean esPosibleJugar(TrucoPlay tp);

    abstract public void play(TrucoPlay tp) throws InvalidPlayExcepcion;

    abstract public Vector getDetallesDeLaMano();

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    /**
     * Retorna el numero de mano actual del TrucoGame.
     *
     * @return El numero de mano actual del TrucoGame.
     */
    public int getNumberOfHand() {
        return numberOfHand;
    }

    public TrucoTeam[] getTeams() {
        return teams;
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
        throw new IllegalArgumentException("Index [" + i + "] Invalid to get Team");
    }

    public void addTrucoListener(TrucoListener tl) {
        logger.debug("Truco Listener added [" + tl + "]");
        listenerlist.add(tl);
    }

    public List getListenerlist() {
        return new ArrayList<>(listenerlist);
    }

    /**
     * @param tptmp
     */
    public void removeTrucoListener(TrucoPlayer tptmp) {
        try {
            System.out.println("Se elimina un truco listener del trucogame, vamos a eliminar a:" + tptmp.getName());
            for (int i = 0; i < listenerlist.size(); i++) {
                TrucoListener comm = ((TrucoListener) (listenerlist.get(i)));
                if (comm.getAssociatedPlayer().getName() == tptmp.getName()) {
                    listenerlist.remove(i);
                    System.out.println("Eliminamos el listener del player " + comm.getAssociatedPlayer().getName());
                }

            }
        } catch (NullPointerException npe) {
            System.out.println("No se pudo eliminar el listener " + tptmp.getName());
        }
    }


    /**
     * Envia las cartas a los jugadores.
     *
     * @param tp   TrucoPlayer a quien irá las cartas.
     * @param card carta a ser enviadas.
     */
    public void dealtCards(TrucoPlayer tp, TrucoCard[] card) {//reparte las cartas a los jugadores
        TrucoEvent event = new TrucoEvent(this, numberOfHand, tp, (byte) 0, card);
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
//			System.out.println(i+"ejecutando cardsDEal de:" + getClass().getName());
            ((TrucoListener) listenerlist.get(i)).cardsDeal(event);
        }
    }

    /**
     * Enviar mensajes a todos los oyentes del cambio de turno.
     *
     * @param pl   TrucoPlayer a quien se le asigna el turno.
     * @param type Tipo de Turno a ser asignado.
     */
    public void fireTurnEvent(TrucoPlayer pl, byte type) { //avisar quien juega con type el tipo de turno, ronda de cartas, ronda de envidos o flores etc
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type); //crear el evento
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) { //enviarle a todos el evento
            ((TrucoListener) (listenerlist.get(i))).turn(event);
        }
    }

    public void fireTurnEvent(TrucoPlayer pl, byte type, int value) { //avisar el Turno con envio del value of envido
        TrucoEvent event1 = new TrucoEvent(this, numberOfHand, pl, type, value); //crear el evento1
        TrucoEvent event2 = new TrucoEvent(this, numberOfHand, pl, type); //crear el evento2
        event1.setTableNumber(getTableNumber());
        event2.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            if (((TrucoListener) listenerlist.get(i)).getAssociatedPlayer() == pl)
                ((TrucoListener) (listenerlist.get(i))).turn(event1);
            else
                ((TrucoListener) (listenerlist.get(i))).turn(event2);
        }
    }


    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl   TrucoPlayer que realizo la jugada.
     * @param type Tipo de Jugada que realizó.
     */
    public void firePlayEvent(TrucoPlayer pl, byte type) { //eventos de juego sin carta o canto
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type);
        event.setTableNumber(getTableNumber());
        firePlayEvent(event);
    }

    protected void firePlayEvent(TrucoEvent event) {
        for (int i = 0; i < listenerlist.size(); i++) {
            //			((TrucoListener)(listenerlist.get(i))).play(event);
            // 			Se cambi� la llamada en intento desesperado por hacer funcionar esto
            //			((TrucoListener)(listenerlist.get(i))).playResponse(event);
            ((TrucoListener) (listenerlist.get(i))).play(event);
        }
    }

    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl   TrucoPlayer que realizó la jugada.
     * @param card Carta que jugó el Player.
     * @param type Tipo de jugada que realizó.
     */
    public void firePlayEvent(TrucoPlayer pl, TrucoCard card, byte type) { //eventos de juego con carta
        logger.debug("FirePlayEvent [" + pl + "][" + card + "][" + type + "]");

        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type, card);
        event.setTableNumber(getTableNumber());

        firePlayEvent(event);
    }

    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl    Player que realiz� la jugada.
     * @param type  Tipo de jugada que realiz�.
     * @param value Valor del canto (para jugadas de canto de valor Envido o Flor).
     */
    public void firePlayEvent(TrucoPlayer pl, byte type, int value) {//eventos de canto de tanto
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type, value);
        event.setTableNumber(getTableNumber());
        firePlayEvent(event);
    }

    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl   TrucoPlayer que realizó la jugada.
     * @param card Carta que jugó el Player.
     * @param type Tipo de jugada que realizó.
     */
    public void firePlayResponseEvent(TrucoPlayer pl, TrucoCard card, byte type) { //eventos de juego con carta
        //System.out.println("se envia el mensaje de PlayEvent");
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type, card);
        event.setTableNumber(getTableNumber());
        System.out.println("Se va a disparar un evento de play response.  El tamaño de la lista de listeners es: " + listenerlist.size());
        firePlayResponseEvent(event);
    }

    protected void firePlayResponseEvent(TrucoEvent event) {
        for (int i = 0; i < listenerlist.size(); i++) {
            System.out.println("FirePlayEvent para: " + listenerlist.get(i).getClass().getName());
            //		((TrucoListener)(listenerlist.get(i))).play(event);
            ((TrucoListener) (listenerlist.get(i))).playResponse(event);
        }
    }


    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl   TrucoPlayer que realizo la jugada.
     * @param type Tipo de Jugada que realiz�.
     */
    public void firePlayResponseEvent(TrucoPlayer pl, byte type) { //eventos de juego sin carta o canto
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type);
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            //			((TrucoListener)(listenerlist.get(i))).play(event);
            //			Se cambió la llamada en intento desesperado por hacer funcionar esto
            ((TrucoListener) (listenerlist.get(i))).playResponse(event);
            //			((TrucoListener)(listenerlist.get(i))).play(event);
        }
    }

    /**
     * Enviar mensaje de jugada a todos los oyentes del juego.
     *
     * @param pl    Player que realizó la jugada.
     * @param type  Tipo de jugada que realizó.
     * @param value Valor del canto (para jugadas de canto de valor Envido o Flor).
     */
    public void firePlayResponseEvent(TrucoPlayer pl, byte type, int value) {//eventos de canto de tanto
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type, value);
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            //			((TrucoListener)(listenerlist.get(i))).play(event);
            ((TrucoListener) (listenerlist.get(i))).playResponse(event);
        }
    }


    public void fireEndOfHandEvent() {
        TrucoEvent event = new TrucoEvent(this, numberOfHand, TrucoEvent.FIN_DE_MANO);
        fireEndOfHandEvent(event);
    }

    protected void fireEndOfHandEvent(TrucoEvent event) {
        logger.debug("Fire End Of Hand");
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            ((TrucoListener) (listenerlist.get(i))).endOfHand(event);
        }
    }

    /**
     * Enviar mensaje a todos los oyentes sobre el final del juego.
     */
    public void fireEndOfGameEvent() {
        logger.info("End Of Game [" + tableNumber + "]");
        TrucoEvent event = new TrucoEvent(this, numberOfHand, TrucoEvent.FIN_DE_JUEGO);
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            ((TrucoListener) (listenerlist.get(i))).endOfGame(event);
        }
    }

    /**
     * Enviar las cartas a cada jugador.
     */
    public void fireCardsDealt() {
        TrucoEvent event = new TrucoEvent(this, TrucoEvent.ENVIAR_CARTAS);
        event.setTableNumber(getTableNumber());
        for (int i = 0; i < listenerlist.size(); i++) {
            ((TrucoListener) (listenerlist.get(i))).play(event);
        }
    }

    /**
     * Enviar mensaje a todos los oyentes sobre el el comienzo del juego.
     */
    public void fireGameStarted() {
        logger.debug("Fire Game Started Event");

        TrucoEvent event = new TrucoEvent(this, numberOfHand, TrucoEvent.INICIO_DE_JUEGO);
        event.setTableNumber(getTableNumber());

        for (int i = 0; i < listenerlist.size(); i++) {
            ((TrucoListener) (listenerlist.get(i))).gameStarted(event);
        }
    }

    /**
     * Enviar mensaje a todos los oyentes sobre el el comienzo de la mano.
     */
    public void fireHandStarted() {
        logger.info(" fireHandStarted " + getNumberOfHand());
        logger.info(" el equipo[0] es: " + teams[0]);
        logger.info(" el equipo[1] es: " + teams[1]);
        logger.info(teams[1] == null);
        logger.info(" numero de players de los equipos = " + teams[0].getNumberOfPlayers() + "y" + teams[0].getNumberOfPlayers());

        TrucoPlayer tp = teams[(numberOfHand + 1) % 2].getTrucoPlayerNumber((numberOfHand - 1) % numberOfPlayers / 2);
        TrucoEvent event = new TrucoEvent(this, numberOfHand, tp, TrucoEvent.INICIO_DE_MANO);
        event.setTableNumber(getTableNumber());

        for (int i = 0; i < listenerlist.size(); i++) {
            ((TrucoListener) (listenerlist.get(i))).handStarted(event);
        }
    }


    public int getValorEnvido(TrucoPlayer trucoPlayer) {
        throw new RuntimeException("Method not implemented yet");
    }


    public TrucoCard getCardNoPlayed(TrucoPlayer trucoPlayer) {
        throw new RuntimeException("Method not implemented yet");
    }

}


