package py.edu.uca.fcyt.toluca.table;

import java.awt.Color;
import java.awt.Cursor;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Vector;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.PointsDetail;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.sound.PlaySound;
import py.edu.uca.fcyt.toluca.table.animation.Animator;

/**
 * Escucha los eventos del juego
 */
class TrListener implements TrucoListener {
    protected LinkedList trucoListeners; // listeners de eventos

    private Table table;

    private boolean endOfHand = false;

    private int cartasJugadas = 0;

    /** Construye un TrucoListenerManger con Table 'table' */
    public TrListener(Table table) {
        trucoListeners = new LinkedList();
        setTable(table);
    }

    /** Registra un listener de eventos TrucoListener */
    public void addListener(TrucoListener t) {
        Util.verifParam(t != null, "Par�metro 't' nulo");
        trucoListeners.add(t);
    }

    /** Invocado cuando se inicia el juego de truco */
    public void gameStarted(TrucoEvent event) {
        //System.out.println("Game started for player " +
        // getAssociatedPlayer().getName());
        //		new Throwable("").printStackTrace(System.out);
        getTable().getJTrucoTable().buttons[TrucoTable.BUTTON_INICIAR_OK]
                .setText("Ok");
        getTable().getJTrucoTable().getJButton("Ayuda").setEnabled(false);
        getTable().getJTrucoTable().getJBinvitar().setEnabled(false);
        cartasJugadas = 0;
    }

    /**
     * Devuelve la cantidad de jugadores
     */
    private int playerCount() {
        return getPManager().getPlayerCount();
    }

    public void play(TrucoEvent event) {
        //System.out.println("Void play method in " +
        // this.getClass().getName());
        //Aca llega cuando se "Confirma" un jugador desde el serva
        //new Throwable("Nada implementado :-(").printStackTrace(System.out);
        //playResponse(event);
    }

    public void playResponse(TrucoEvent event) {
        int pos;
        TrucoPlayer player;
        byte type;
        String name;
        CardManager cManager;
        Card card;
        int team;
        //		JLabel jlSaying;

        getTable().setCursor(Cursor.DEFAULT_CURSOR);

        //		jlSaying = getTable().getJTrucoTable().jlSaying;

        cManager = getCManager();

        type = event.getTypeEvent();
        player = event.getPlayer();
        pos = getPManager().getPos(player);
        name = Util.getEventName(type);
        team = getTable().getPManager().getChair(player) % 2;
        //si name da null quiere decir q no fue ninguna jugada que se cante.
        // ale

        //		System.out.println
        //		(
        //			"\nJugada para jugador " +
        //			getAssociatedPlayer().getName() + ": " + name
        //		);

        switch (type) {
        case TrucoEvent.JUGAR_CARTA:
            PlaySound.play(PlaySound.CARD_PLAYED_SOUND);
            cartasJugadas++;
            if (cartasJugadas == 3) {
                getTable()
                        .getJTrucoTable()
                        .addSystemLog(
                                "Para ver todas las cartas jugadas a la mesa, haz click sobre las mismas\n");
            }
            card = event.getCard();
            cManager.playCard(pos, card);
            // Somos la estirpe guaran�
            if (getTable().getPlayer() == player)
                //if (getAssociatedPlayer() == player)
                cManager.showCards(null);
            else if (endOfHand)
                cManager.pushPause(pos, 250);
            //System.out.println("Carta jugada: " + card.getDescription());
            break;

        case TrucoEvent.CERRARSE:
            //System.out.println("Cerrarse! pos: " + pos);
            cManager.playClosed(pos);
            break;

        case TrucoEvent.PLAYER_CONFIRMADO:
            //System.out.println("TrucoPlayer confirmado");
            break;

        case TrucoEvent.CANTO_ENVIDO:
            if (event.getValue() == -1)
                System.out.println("Canto envido inv�ldo!");
            else {
                String val = Integer.toString(event.getValue());

                drawBalloon(event.getPlayer(), val, true);

                /*
                 * jlSaying.setText ( jlSaying.getText() +
                 * (jlSaying.getText().equals("Canto: ") ? "" : " - ") + " " +
                 * val + (team == 0 ? "(R)" : "(A)") );
                 */
                getTable().addJugada(team, val);
                getTable().getJTrucoTable()
                        .addLog(event.getPlayer(), team, val);
            }

            break;

        case TrucoEvent.CARTAS_REPARTIDAS:
            break;

        default:
            try {
                drawBalloon(event.getPlayer(), name, true);

                if (type == TrucoEvent.TRUCO)
                    getTable().handStarted();

                /*
                 * jlSaying.setText ( jlSaying.getText() +
                 * (jlSaying.getText().equals("Canto: ") ? "" : " - ") + " " +
                 * name + (team == 0 ? "(R)" : "(A)") );
                 */
                getTable().addJugada(team, name);
                getTable().getJTrucoTable().addLog(event.getPlayer(), team,
                        name);
            } catch (InvalidParameterException ex) {
                System.out.println("---------------------- ");
                System.out.println("- Unknown play event - ");
                System.out.println("---------------------- ");
                System.out.println("Play number: " + type);
                System.out.println("Play name: " + name);
                System.out.println("TrucoPlayer: " + event.getPlayer());
                System.out.println("---------------------- ");
                //					throw new IllegalStateException();
            }

            break;
        }
    }

    public void turn(TrucoEvent event) {
        Vector aPlays = new Vector();
        TrucoGame tGame;
        TrucoPlay tPlay;
        TrucoPlayer aPlayer;
        byte play;
        int valEnvido;

        /*
         * System.out.println("Turn for player " + event.getPlayer().getName());
         * System.out.println("Turn type: " + event.getTypeEvent());
         */

        // obtiene el TrucoGame de la mesa y el jugador actual
        tGame = getTable().getTGame();
        aPlayer = (TrucoPlayer) getTable().getPlayer();

        // establece el estado de la mesa
        if (event.getPlayer() == getAssociatedPlayer()) {
            PlaySound.play(PlaySound.TURN_SOUND);
            getTable().flash(true);

            if (event.getTypeEvent() == TrucoEvent.TURNO_JUGAR_CARTA)
                getTable().setStatus(Table.PLAY);
            else
                getTable().setStatus(Table.PLAYCANTO);

            getTable().envidoPoints = event.getValue();

            // caga el vector de jugadas habilitadas
            for (int i = 0; i < Util.getPlayCount(); i++) {
                play = Util.getPlay(i);

                if (play == TrucoPlay.CANTO_ENVIDO) {
                    tPlay = new TrucoPlay((TrucoPlayer) aPlayer, play,
                            getTable().envidoPoints);
                } else
                    tPlay = new TrucoPlay(aPlayer, play);

                if (tGame.esPosibleJugar(tPlay))
                    aPlays.add(new Byte(play));
            }

            getTable().setAllowedPlays(aPlays);
        } else {
            getTable().flash(false);
            if (getTable().getPManager().isSitted(getTable().getPlayer()))
                getTable().setStatus(Table.WAIT);
            else
                getTable().setStatus(Table.WATCH);
        }

        // resalta la carita con turno
        getFManager().setHighlight(
                getTable().getPManager().getPos(event.getPlayer()));

    }

    public void endOfHand(TrucoEvent event) {
        int pun1, pun2;
        TrucoGame tGame;

        getTable().flash(false);

        //System.out.println("End of hand for player " +
        // getAssociatedPlayer().getName());

        tGame = getTable().getTGame();

        pun1 = getTable().getPoints(0);
        pun2 = getTable().getPoints(1);

        getTable().updateScore(pun1, pun2);

        String sStrings[], string, token;
        Vector vStrings;
        Vector vPoints;

        vPoints = tGame.getDetallesDeLaMano();

        sStrings = new String[vPoints.size()
                + (getTable().getStatus() == Table.WATCH ? 0 : 1)];
        for (int i = 0; i < vPoints.size(); i++)
            sStrings[i] = ((PointsDetail) vPoints.get(i)).aString();

        if (getTable().getStatus() != Table.WATCH) {
            sStrings[sStrings.length - 1] = "Presiona Ok para continuar";
            getTable().setStatus(Table.WAIT);
            getTable().getJTrucoTable().buttons[TrucoTable.BUTTON_INICIAR_OK]
                    .setEnabled(true);
        }

        getTable().getTTextAnimator().showStrings(sStrings,
                new Color[] { Color.BLACK, Color.YELLOW, Color.ORANGE }, -50,
                500, 15000);

        endOfHand = true;
    }

    public void cardsDeal(TrucoEvent event) {
        Card[] cards;
        int sign;

        //Fnew Exception("dealing cards...").printStackTrace(System.out);
        //System.out.println("Cards deal for player " +
        // getAssociatedPlayer().getName());

        cards = event.getCards();

        //		for (int i = 0; i < 3; i++)
        //			System.out.println(cards[i].getDescription());

        if (event.getPlayer() == getAssociatedPlayer()) {
            // carga las se�as
            for (int i = 0; i < 3; i++) {
                sign = Sign.getSign(cards[i]);
                if (sign != Sign.NONE)
                    getTable().addSign(sign);
                //System.out.println(table.getPlayer().getName() + " con se�a
                // " + Sign.getName(sign));
            }
            getCManager().showCards(cards);
            getTable().getJTrucoTable().addSystemLog(
                    "Haz click sobre la carta que quieras jugar\n");
            getTable().getJTrucoTable().addSystemLog(
                    "El borde parpadeando te indicara el turno\n");
            getTable().getJTrucoTable().addSystemLog(
                    "En tu turno, se habilitan botones para los cantos\n");
        }
    }

    public void handStarted(TrucoEvent event) {
        TrucoPlayer tPlayer;
        int dealPos, turnPos;
        CardManager cManager;

        getTable().getTTextAnimator().clearAll();

        getTable().getJTrucoTable().jlSaying.setText("Canto: ");

        //new Exception("player: " +
        // getAssociatedPlayer().getName()).printStackTrace(System.out);
        //System.out.println("New hand started for player " +
        // getAssociatedPlayer().getName());

        endOfHand = false;
        getTable().getJTrucoTable().buttons[TrucoTable.BUTTON_INICIAR_OK]
                .setEnabled(false);
        cManager = getCManager();

        getTable().clearSigns();
        getTable().setPrimerTurno(true);

        tPlayer = (TrucoPlayer) event.getTrucoPlayer();

        dealPos = getPManager().getPos(tPlayer);
        //System.out.println(tPlayer.getName() + " is dealing");
        turnPos = (dealPos + 1) % playerCount();

        cManager.gatherCards(dealPos);

        PlaySound.play(PlaySound.DEAL_SOUND);
        cManager.deal(dealPos, false);
        cManager.take();
        cManager.putDeckInTable(playerCount() * 3 * 250 + 100, turnPos);
    }

    public void endOfGame(TrucoEvent event) {
        getTable().endOfGame(event);
    }

    public TrucoPlayer getAssociatedPlayer() {
        return getTable().getPlayer();
    }

    public void drawBalloon(TrucoPlayer player, String text, boolean highlighted) {
        Face face;

        // verificaciones
        Util.verifParam(player != null, "Par�metro 'player' nulo");
        Util.verifParam(text != null, "Par�metro 'text' nulo");

        if (player != getAssociatedPlayer()) {
            // obtiene la carita
            face = getFManager().getFace(getPManager().getPos(player));

            face.pushText(text, highlighted, 1750);
            face.pushText(null, false, 100);
        }

        //		// muestra el mensaje tambi�n en el chat si est� jugando
        //		if (!(table.getStatus() == Table.WATCH))
        //			table.getJTrucoTable().jpChat.showChatMessage
        //			(
        //				player, text
        //			);
    }

    /** Retorna el animador */
    private Animator getAnimator() {
        return getTable().getAnimator();
    }

    /** Retorna el manejador de cartas */
    private CardManager getCManager() {
        return getTable().getCManager();
    }

    /** Retorna el manejador de jugadores */
    private PlayerManager getPManager() {
        return getTable().getPManager();
    }

    /** Retorna el manejador de caritas */
    private FaceManager getFManager() {
        return getTable().getFManager();
    }

    /** Retorna el TrucoGame */
    private TrucoGame getTGame() {
        return getTable().getTGame();
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}