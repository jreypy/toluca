package py.edu.uca.fcyt.toluca.game.ui;

import org.apache.log4j.Logger;
import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
//import java.*;

public class TPlayer extends JFrame
        implements TrucoListener {

    Logger logger = Logger.getLogger(TPlayer.class);

    static String DORSO = "dorso.gif";

    TrucoPlayer asociado;
    TrucoGame TG;
    TrucoCard[] cards = new TrucoCard[3];

    JTextArea textfield = new JTextArea();
    JTextField turno = new JTextField();
    JButton miscartas[] = new JButton[3];
    PlayButton botones[] = new PlayButton[3];


    Icon icon = null;

    {
        try {
            icon = getImage(DORSO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    JPanel elPanel = new JPanel();
    JButton cartasJugadas[];
    TrucoTeam team;
    TrucoTeam team2;
    int nCartasJugadas = 0;
    int cartasAJugar = 0;
    JSpinner elSpinner = new JSpinner();

    public TPlayer(TrucoPlayer pl, TrucoGame TG, TrucoTeam team, TrucoTeam team2) throws Exception {
        this.team = team;
        this.team2 = team2;
        int i = 0;
        cartasAJugar = 2 * team.getNumberOfPlayers() * 3;
        cartasJugadas = new JButton[cartasAJugar];
        //elPanel.setMinimumSize(new Dimension(1000,1000));    
        elPanel.setPreferredSize(new Dimension(1460, 800));
        textfield.setPreferredSize(new Dimension(230, 200));
        turno.setPreferredSize(new Dimension(50, 100));
        this.TG = TG;
        asociado = pl;
        PlayButton pb;
        for (i = 0; i < 3; i++) {
            miscartas[i] = new JButton(icon);
            elPanel.add(miscartas[i]);
            pb = new PlayButton(asociado, TG);
            botones[i] = pb;
            miscartas[i].addMouseListener(pb);
        }
        elPanel.add(turno);
        elPanel.add(textfield);

        for (i = 0; i < cartasAJugar; i++) {

            Icon icon = getImage(DORSO);
            cartasJugadas[i] = new JButton(icon);
            elPanel.add(cartasJugadas[i]);


        }
        losbotones();

        setTitle("Truco - " + pl.getName());//el titulo del panel


        getContentPane().add(elPanel);
        pack();
    }

    void losbotones() {

        JButton boton = new JButton("Envido");
        boton.addMouseListener(new TTruco(this, TrucoPlay.ENVIDO));
        elPanel.add(boton);
        boton = new JButton("Real Envido");
        boton.addMouseListener(new TTruco(this, TrucoPlay.REAL_ENVIDO));
        elPanel.add(boton);
        boton = new JButton("flor");
        boton.addMouseListener(new TTruco(this, TrucoPlay.FLOR));
        elPanel.add(boton);
        boton = new JButton("FALTA ENVIDO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.FALTA_ENVIDO));
        elPanel.add(boton);

        boton = new JButton("Truco");
        boton.addMouseListener(new TTruco(this, TrucoPlay.TRUCO));
        elPanel.add(boton);

        boton = new JButton("ReTruco");
        boton.addMouseListener(new TTruco(this, TrucoPlay.RETRUCO));
        elPanel.add(boton);

        boton = new JButton("Vale Cuatro");
        boton.addMouseListener(new TTruco(this, TrucoPlay.VALE_CUATRO));
        elPanel.add(boton);


        boton = new JButton("QUIERO");

        boton.addMouseListener(new TTruco(this, TrucoPlay.QUIERO));
        elPanel.add(boton);
        boton = new JButton("NO QUIERO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.NO_QUIERO));
        elPanel.add(boton);

        boton = new JButton("CANTAR : ");
        boton.addMouseListener(new TTruco(this, TrucoPlay.CANTO_ENVIDO));
        elPanel.add(boton);

        elSpinner.setPreferredSize(new Dimension(100, 50));
        elPanel.add(elSpinner);

        boton = new JButton("PASO EL ENVIDO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.PASO_ENVIDO));
        elPanel.add(boton);

        boton = new JButton("ME VOY AL MAZO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.ME_VOY_AL_MAZO));
        elPanel.add(boton);

        boton = new JButton("ME CIERRO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.CERRARSE));
        elPanel.add(boton);

        boton = new JButton("ME VOY AL MAZO");
        boton.addMouseListener(new TTruco(this, TrucoPlay.ME_VOY_AL_MAZO));
        elPanel.add(boton);

    }

    public void play(TrucoEvent event) {
        logger.debug("[" + asociado.getName() + "]TrucoEvent received [" + event + "]");

        if (event.getTypeEvent() == TrucoEvent.JUGAR_CARTA) {
            Icon icon = aIcono(event.getCard());
            logger.debug("Set Icon [" + nCartasJugadas + "][" + icon + "]");
            cartasJugadas[nCartasJugadas++].setIcon(icon);
        } else {
            textfield.setText(textfield.getText() + "\ntype" + event.getTypeEvent());
            System.out.println(event.getTypeEvent());
        }


    }

    public void playResponse(TrucoEvent event) {
        logger.debug("Receiving PlayResponse [" + event + "]");
//        System.out.println(elSpinner.getValue());
        if (event.getTypeEvent() == TrucoEvent.JUGAR_CARTA) {
            Icon icon = aIcono(event.getCard());
            logger.debug("Set Icon [" + nCartasJugadas + "][" + icon + "]");
            cartasJugadas[nCartasJugadas++].setIcon(icon);
        } else {
            textfield.setText(textfield.getText() + "\ntype" + event.getTypeEvent());
            System.out.println(event.getTypeEvent());
            //
            if (asociado.equals(event.getPlayer())) {
                JOptionPane.showMessageDialog(elPanel, "Jugada", "[" + event.getPlayer().getName() + "]: [" + event.description() + "]", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }

    public void turn(final TrucoEvent event) {
        logger.debug("turnevent received [" + event.getPlayer() + "]");
//        System.out.println(asociado.getName());
        turno.setText((event.getPlayer()).getName());

        if (event.getPlayer() == asociado)
            textfield.setText(textfield.getText() + "\n***** " + asociado.getName() + " *****");
        else
            textfield.setText(textfield.getText() + "\n" + event.getPlayer().getName());

        textfield.setText(textfield.getText() + " ~ " + event.getTypeEvent());

        //
        if (asociado.equals(event.getPlayer())) {
            final JFrame frame = this;
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.pack();
                    frame.setVisible(true);
//                    frame.setAlwaysOnTop(true);
                    frame.toFront();
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    frame.repaint();
                    frame.requestFocus();
                    logger.info("Request Focus");
                    JOptionPane.showMessageDialog(elPanel, "Es tu turno", "Turno de [" + event.getPlayer().getName() + "]", JOptionPane.PLAIN_MESSAGE);

                }
            });
        }

    }

    public void endOfHand(TrucoEvent event) {
        textfield.setText(textfield.getText() + "\nfin de mano");
        JOptionPane.showMessageDialog(null, "Mano Finalizada", "Mano Finalizada", JOptionPane.PLAIN_MESSAGE);
        logger.debug("Fire new Hand");
        TG.startHand(asociado);
    }

    public void endOfGame(TrucoEvent event) {
        textfield.setText(textfield.getText() + "\nfin de juego");
//        System.out.println("end of game");
    }

    /**
     * Receiving Card to play
     *
     * @param event
     */
    public void cardsDeal(TrucoEvent event) {
        logger.debug("[" + asociado.getName() + "] -> receiving [" + event.getPlayer().getName() + "]Cards [" + event.getCards() + "]");
        TrucoCard[] Tcards = event.getCards();
        textfield.setText(textfield.getText() + "\nrecibiendo cartas...");

        if (Tcards == null) throw new IllegalArgumentException("Cards could not be null");
        if (asociado.equals(event.getPlayer())) {
            logger.debug("Rendering Card if is mine [" + asociado.getName() + "]");
            for (int i = 0; i < 3; i++) {
                cards[i] = Tcards[i];
                miscartas[i].setIcon(aIcono(cards[i]));
                botones[i].setCard(cards[i]);
            }
        }

    }

    public void handStarted(TrucoEvent event) {
        setTitle("Truco - " + TG.getNumberOfHand() + " - " + asociado.getName() + " // " + team.getPoints() + " - " + team2.getPoints());//el titulo del panel
        for (int i = 0; i < cartasAJugar; i++) {
            try {
                cartasJugadas[i].setIcon(getImage(DORSO));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        nCartasJugadas = 0;
        textfield.setText("\nempieza mano..");
//        System.out.println("hand Started");
    }

    public void gameStarted(TrucoEvent event) {
        textfield.setText(textfield.getText() + "\nempieza juego");
//        System.out.println("Game Started");
    }

    public TrucoPlayer getAssociatedPlayer() {
        return asociado;
    }

    Icon aIcono(TrucoCard carta) {
        logger.info("Request Icon for [" + carta + "]");
        String lacarta = null;
        switch (carta.getKind()) {
            case Card.ORO:
                lacarta = "Oro/" + carta.getValue() + ".gif";
                break;
            case Card.BASTO:
                lacarta = "Basto/" + carta.getValue() + ".gif";
                break;
            case Card.COPA:
                lacarta = "Copa/" + carta.getValue() + ".gif";
                break;
            case Card.ESPADA:
                lacarta = "Espada/" + carta.getValue() + ".gif";
                break;
        }
        try {
            icon = getImage(lacarta);
        } catch (Exception e) {
            logger.error("Error getting image for [" + lacarta + "]", e);
        }
        return icon;
    }

    private ImageIcon getImage(String image) throws Exception {
        String spec = "images/" + image;
        try {
            URL url = TPlayer.class.getClassLoader().getResource(spec);
            return new ImageIcon(url);
        } catch (Exception e) {
            logger.error("Error getting image [" + spec + "]");
            throw e;
        }

    }

    public void cantar(byte type) {
        logger.debug("Cantar [ " + type + "]");
        try {
            if (type != TrucoPlay.CANTO_ENVIDO) {

                TrucoPlay tp = new TrucoPlay(asociado, type);
                logger.debug("[" + this.getAssociatedPlayer().getName() + " ] plays [" + tp.description() + "]");
                if (!TG.esPosibleJugar(tp))
                    logger.warn("epa un error posiblemente" + tp.getType());
                else
                    TG.play(tp);

            } else {
                int envido = tanto();
                TrucoPlay tp = new TrucoPlay(asociado, type, envido);
                logger.info("Cantar Envido [" + asociado.getName() + "][" + tp.description() + "][" + envido + "]");
                if (!TG.esPosibleJugar(tp))
                    logger.warn("epa un error posiblemente" + tp.getType());
                else
                    TG.play(tp);
            }
        } catch (InvalidPlayExcepcion e) {
            logger.error(e.getMessage(), e);
        }
    }

    int tanto() {
        int r;
        r = Integer.parseInt((elSpinner.getValue()).toString());
        return r;
    }

    String printtipo(byte tipo) {
        String n;
        switch (tipo) {
            case TrucoPlay.JUGAR_CARTA:
                n = "jugar carta";
                break;
            case TrucoPlay.CERRARSE:
                n = "me cierro";
                break;
            case TrucoPlay.ME_VOY_AL_MAZO:
                n = "me voy al mazo";
                break;
            case TrucoPlay.TRUCO:
                n = "truco";
                break;
            case TrucoPlay.RETRUCO:
                n = "retruco";
                break;
            case TrucoPlay.VALE_CUATRO:
                n = " vale cuatro";
                break;
            case TrucoPlay.CANTO_ENVIDO:
                n = "canto envido";
                break;
            case TrucoPlay.FLOR:
                n = "flor!";
                break;
            case TrucoPlay.PASO_ENVIDO:
                n = "paso envido";
                break;
            default:
                n = "error";
                break;
        }
        return n;
    }
}



