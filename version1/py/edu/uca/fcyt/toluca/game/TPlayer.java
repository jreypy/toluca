package py.edu.uca.fcyt.toluca.game;

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import py.edu.uca.fcyt.game.Card;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
//import java.*;

public class TPlayer extends JFrame 
implements TrucoListener{
    TrucoPlayer asociado;
    TrucoGame TG;
    TrucoCard[] cards = new TrucoCard[3];
    
    JTextArea textfield=  new JTextArea();
    JTextField turno = new JTextField();
    JButton miscartas[] = new JButton[3];
    PlayButton botones[] = new PlayButton[3];
    Icon icon = new ImageIcon("c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/dorso.gif");
    JPanel elPanel = new JPanel();
    JButton cartasJugadas[];
    TrucoTeam team;
    TrucoTeam team2;
    int nCartasJugadas=0;
    int cartasAJugar=0;
    JSpinner elSpinner = new JSpinner();
    
    public TPlayer(TrucoPlayer pl,TrucoGame TG,TrucoTeam team,TrucoTeam team2){
        this.team = team;
        this.team2 = team2;
        int i=0;
        cartasAJugar = 2*team.getNumberOfPlayers()*3;
        cartasJugadas = new JButton [cartasAJugar];
        //elPanel.setMinimumSize(new Dimension(1000,1000));    
        elPanel.setPreferredSize(new Dimension(1460,800));
        textfield.setPreferredSize(new Dimension(230,200));
        turno.setPreferredSize(new Dimension(50,100));
        this.TG = TG;
        asociado = pl;
	PlayButton pb;
        for (i=0; i<3;i++){
            miscartas[i] = new JButton(icon);
            elPanel.add(miscartas[i]);
            pb = new PlayButton(asociado,TG);
            botones[i] = pb;
            miscartas[i].addMouseListener(pb);
        }
        elPanel.add(turno);
        elPanel.add(textfield);
        
        for(i=0; i<cartasAJugar; i++){
            cartasJugadas[i] = new JButton(new ImageIcon("c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/dorso.gif"));
            elPanel.add(cartasJugadas[i]);
        }
        losbotones();
        
	setTitle("Truco - " + pl.getName());//el titulo del panel
	
        
	getContentPane().add(elPanel);
	pack();
    }
    void losbotones(){
        
        JButton boton=new JButton("Envido");
        boton.addMouseListener(new TTruco(this,TrucoPlay.ENVIDO));
        elPanel.add(boton);
        boton = new JButton("Real Envido");
        boton.addMouseListener(new TTruco(this,TrucoPlay.REAL_ENVIDO));
        elPanel.add(boton);
        boton = new JButton("flor");
        boton.addMouseListener(new TTruco(this,TrucoPlay.FLOR));
        elPanel.add(boton);
        boton = new JButton("FALTA ENVIDO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.FALTA_ENVIDO));
        elPanel.add(boton);

        boton = new JButton("Truco");
        boton.addMouseListener(new TTruco(this,TrucoPlay.TRUCO));
        elPanel.add(boton);
        
        boton = new JButton("ReTruco");
        boton.addMouseListener(new TTruco(this,TrucoPlay.RETRUCO));
        elPanel.add(boton);
        
        boton = new JButton("Vale Cuatro");
        boton.addMouseListener(new TTruco(this,TrucoPlay.VALE_CUATRO));
        elPanel.add(boton);
        
                
        boton = new JButton("QUIERO");
        
        boton.addMouseListener(new TTruco(this,TrucoPlay.QUIERO));
        elPanel.add(boton);
        boton = new JButton("NO QUIERO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.NO_QUIERO));
        elPanel.add(boton);
        
        boton = new JButton("CANTAR : ");
        boton.addMouseListener(new TTruco(this,TrucoPlay.CANTO_ENVIDO));
        elPanel.add(boton);
        
        elSpinner.setPreferredSize(new Dimension(100,50));
        elPanel.add(elSpinner);
        
        boton = new JButton("PASO EL ENVIDO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.PASO_ENVIDO));
        elPanel.add(boton);
        
        boton = new JButton("ME VOY AL MAZO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.ME_VOY_AL_MAZO));
        elPanel.add(boton);
        
        boton = new JButton("ME CIERRO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.CERRARSE));
        elPanel.add(boton);
        
        boton = new JButton("ME VOY AL MAZO");
        boton.addMouseListener(new TTruco(this,TrucoPlay.ME_VOY_AL_MAZO));
        elPanel.add(boton);
       
    }
    
	public void play(TrucoEvent event) {
		System.out.println("Void play method in " + this.getClass().getName());		
	}
    
    public void playResponse(TrucoEvent event){
//        System.out.println(elSpinner.getValue());
        if(event.getTypeEvent() == TrucoEvent.JUGAR_CARTA){
            System.out.println("queria jugar carta"+event.getTypeEvent());
            icon = aIcono(event.getCard());
            cartasJugadas[nCartasJugadas++].setIcon(icon);
        }
        else
        {
            textfield.setText(textfield.getText() + "\ntype"+event.getTypeEvent());
            System.out.println(event.getTypeEvent());
        }
    }
    public void turn (TrucoEvent event){
//        System.out.println(asociado.getName());
        turno.setText((event.getPlayer()).getName());
        
        if(event.getPlayer() == asociado)
            textfield.setText(textfield.getText() + "\n***** "+asociado.getName()+" *****");
        else
            textfield.setText(textfield.getText() + "\n"+event.getPlayer().getName());
    textfield.setText(textfield.getText() + " ~ "  + event.getTypeEvent() );
       
    }
    public void endOfHand(TrucoEvent event){
        textfield.setText(textfield.getText() + "\nfin de mano");
    }
    public void endOfGame(TrucoEvent event){
        textfield.setText(textfield.getText() + "\nfin de juego");
//        System.out.println("end of game");
    }
    public void cardsDeal(TrucoEvent event){
        TrucoCard[] Tcards = event.getCards();
        textfield.setText(textfield.getText() + "\nrecibiendo cartas...");
        if (Tcards == null) System.out.println("TCARD ES NULO!!!!!");
        for(int i=0; i<3; i++){
            cards[i] = Tcards[i];
            miscartas[i].setIcon(aIcono(cards[i]));
            botones[i].setCard(cards[i]);
        }
    }
    public void handStarted(TrucoEvent event){
        setTitle("Truco - "+ TG.getNumberOfHand()+" - " + asociado.getName() + " // " + team.getPoints() +" - " + team2.getPoints());//el titulo del panel
       for(int i=0; i<cartasAJugar; i++){
            cartasJugadas[i].setIcon(new ImageIcon("c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/dorso.gif"));
          
        }

        nCartasJugadas=0;
        textfield.setText("\nempieza mano..");
//        System.out.println("hand Started");
    }
    public void gameStarted(TrucoEvent event){
                textfield.setText(textfield.getText() + "\nempieza juego");
//        System.out.println("Game Started");
    }
    public TrucoPlayer getAssociatedPlayer(){
        return asociado;
    }

    Icon aIcono(TrucoCard carta){
        String lacarta = null;
        switch(carta.getKind()){
            case Card.ORO:
                lacarta = "c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/" + "oro/"+carta.getValue()+".gif";
                break;
            case Card.BASTO:
                lacarta = "c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/" + "basto/"+carta.getValue()+".gif";
                break;
            case Card.COPA:
                lacarta = "c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/" + "copa/"+carta.getValue()+".gif";
                break;
            case Card.ESPADA:
                lacarta = "c:/pablo/toluca/py/edu/uca/fcyt/toluca/images/" + "espada/"+carta.getValue()+".gif";
                break;
        }
        icon = new ImageIcon(lacarta);
        return icon;
    }
    public void cantar(byte type){
        try{
            if(type!=TrucoPlay.CANTO_ENVIDO){
                TrucoPlay tp = new TrucoPlay(asociado,type);
                if(!TG.esPosibleJugar(tp))
                    System.out.println("epa un error posiblemente" + tp.getType());
                else
                    TG.play(tp);
            }
            else{
                TrucoPlay tp = new TrucoPlay(asociado,type,tanto());
                if(!TG.esPosibleJugar(tp))
                    System.out.println("epa un error posiblemente" + tp.getType());
                else
                    TG.play(tp);
            }
        }
        catch(InvalidPlayExcepcion e){
            System.out.println(e.toString());
        }
    }
    int tanto(){
        int r;
        r = Integer.parseInt((elSpinner.getValue()).toString());
        return r;
    }
    String printtipo (byte tipo){
        String n;
        switch(tipo){
            case TrucoPlay.JUGAR_CARTA :
                n = "jugar carta";
                break;
            case TrucoPlay.CERRARSE :
                n = "me cierro";
                break;
            case TrucoPlay.ME_VOY_AL_MAZO:
                n="me voy al mazo";
                break;
            case TrucoPlay.TRUCO :
                n="truco";
                break;
            case TrucoPlay.RETRUCO : 
                n="retruco";
                break;
            case TrucoPlay.VALE_CUATRO :
                n=" vale cuatro" ;
                break;
            case TrucoPlay.CANTO_ENVIDO :
                n="canto envido";
                break;
            case TrucoPlay.FLOR:
                n="flor!";
                break;
            case TrucoPlay.PASO_ENVIDO:
                n="paso envido";
                break;
            default:
                n="error";
                break;
        }
        return n;
    }
}



