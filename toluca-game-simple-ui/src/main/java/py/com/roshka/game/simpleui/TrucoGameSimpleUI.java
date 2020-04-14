package py.com.roshka.game.simpleui;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoGameImpl;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;
import py.edu.uca.fcyt.toluca.game.ui.TPlayer;
import py.edu.uca.fcyt.toluca.game.ui.TrucoGameClientTest;

import javax.swing.*;
import java.util.List;

public class TrucoGameSimpleUI {
    Logger logger = Logger.getLogger(TrucoGameSimpleUI.class);


    public TrucoGameSimpleUI() throws Exception {
        logger.debug("Starting TrucoGameSimpleUI with 4 players");


        TrucoTeam jreyTeam = new TrucoTeam();
        TrucoPlayer jrey = new TrucoPlayer("Julio");
        TrucoPlayer zid1 = new TrucoPlayer("Paulo");

        TrucoTeam cBenitezTeam = new TrucoTeam();
        TrucoPlayer cBenitez = new TrucoPlayer("Choco");
        TrucoPlayer zid2 = new TrucoPlayer("Dani");

        cBenitezTeam.addPlayer(cBenitez);
//            cBenitezTeam.addPlayer(zid2);

        jreyTeam.addPlayer(jrey);
//            jreyTeam.addPlayer(zid1);

        TrucoGame tg = new TrucoGameImpl(cBenitezTeam, jreyTeam);

        TPlayer tp1 = new TPlayer(jrey, tg, jreyTeam.getPlayers().size() + cBenitezTeam.getPlayers().size());
        TPlayer tp2 = new TPlayer(cBenitez, tg, jreyTeam.getPlayers().size() + cBenitezTeam.getPlayers().size());

//            TPlayer tp3 = new TPlayer(zid1, tg, cBenitezTeam, jreyTeam);
//            TPlayer tp4 = new TPlayer(zid2, tg, cBenitezTeam, jreyTeam);
        tg.addTrucoListener(tp1);
        tg.addTrucoListener(tp2);
//            tg.addTrucoListener(tp3);
//            tg.addTrucoListener(tp4);

//            tp3.show();
//            tp4.show();

        tg.startGame();

    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        Logger root = Logger.getLogger("");
        root.setLevel(Level.DEBUG);

        System.out.println("Hooola+++");
//        TrucoGameSimpleUI ti = new TrucoGameSimpleUI();
        {
            TrucoGameClientTest[] players = new TrucoGameClientTest[]{
                    new TrucoGameClientTest("mano_amiga")
            };
            new TrucoGameClientTest("julio", players).connect();
        }
    }
}
