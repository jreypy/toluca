package py.edu.uca.fcyt.toluca.game;


public class TrucoInterface {
    public TrucoInterface(){
        try{
            TrucoTeam jreyTeam = new TrucoTeam();
            TrucoPlayer jrey = new TrucoPlayer("Julio");
            TrucoPlayer zid1 = new TrucoPlayer("Paulo");
            TrucoTeam cBenitezTeam = new TrucoTeam();
            TrucoPlayer cBenitez = new TrucoPlayer("Choco");
            TrucoPlayer zid2 = new TrucoPlayer("Dani");
            cBenitezTeam.addPlayer(cBenitez);
            cBenitezTeam.addPlayer(zid2);
            jreyTeam.addPlayer(jrey);
            jreyTeam.addPlayer(zid1);
            TrucoGame tg = new TrucoGame(cBenitezTeam,jreyTeam);
            TPlayer tp1 = new TPlayer(jrey, tg,cBenitezTeam,jreyTeam);
            TPlayer tp2 = new TPlayer(cBenitez, tg, cBenitezTeam, jreyTeam);
            TPlayer tp3 = new TPlayer(zid1, tg, cBenitezTeam,jreyTeam);
            TPlayer tp4 = new TPlayer(zid2, tg, cBenitezTeam, jreyTeam);
            tg.addTrucoListener(tp1);
            tg.addTrucoListener(tp2);
            tg.addTrucoListener(tp3);
            tg.addTrucoListener(tp4);
            tp2.show();
            tp1.show();
            tp3.show();
            tp4.show();

            tg.startGame();
        }
        catch (InvalidPlayExcepcion e){
        }
    }
      public static void main(String[] args) throws InvalidPlayExcepcion{
        System.out.println("Hooola+++");
        TrucoInterface ti = new TrucoInterface();
        
    }
}

