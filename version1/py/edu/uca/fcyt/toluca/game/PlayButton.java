/*
 * TPlayerMouseListener.java
 *
 * Created on 8 de abril de 2002, 10:12 AM
 */
package py.edu.uca.fcyt.toluca.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *
 * @author  Julio Rey
 */
public class PlayButton implements MouseListener{
    boolean jugo = false;
    TrucoGame tg;
    TrucoCard TC;
    TrucoPlayer TP;
    public PlayButton (TrucoPlayer pl, TrucoGame tg){
        this.tg = tg;
    this.TP = pl;
    }
    public void setCard(TrucoCard TC){
        this.TC = TC;
    }
    public void mouseClicked(MouseEvent e) {
        TrucoPlay tplay = new TrucoPlay((TrucoPlayer)TP,TrucoPlay.JUGAR_CARTA,TC);
        try{
            if(tg.esPosibleJugar(tplay))
                tg.play(tplay);
        }
        catch(InvalidPlayExcepcion ex){
            System.out.println("la reputa carajo, excepcion - " +ex.toString());
        }
       
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
    public void mousePressed(MouseEvent e){
    }
    public void mouseReleased(MouseEvent e){
   
    }
}