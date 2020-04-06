/*
 * Deck.java
 *
 * Created on 5 de marzo de 2003, 11:07 PM
 */

package py.edu.uca.fcyt.toluca.game;

import py.edu.uca.fcyt.game.Card;

/**
 *
 * @author  Julio Rey || Chistian Benitez
 */
public class Deck {
    
    /** Creates a new instance of Deck */
    private TrucoCard[] deck = new TrucoCard[40];
    int cursor;
    public Deck() {
        cursor =0;
        createDeck();
    }
    private void createDeck(){
        int j=0; //indice auxiliar
        for (int i=1; i<=12; i++){
            if (i<8 || i>9){
                deck[j++] = new TrucoCard(Card.ESPADA,i);
                deck[j++] = new TrucoCard(Card.COPA,i);
                deck[j++] = new TrucoCard(Card.BASTO,i);
                deck[j++] = new TrucoCard(Card.ORO,i);
            }
        }
    }
    public void shuffle(){
        int posori, posdes;
        TrucoCard trucocardauxiliar;
        for (int i=0; i<200; i++){
            posori = (int)(Math.random()*40); //dos posiciones aleatorias e intercambiar
            posdes = (int)(Math.random()*40);
            trucocardauxiliar = deck[posori];
            deck[posori] = deck[posdes];
            deck[posdes] = trucocardauxiliar;
        }
        cursor =0;
    }
    public TrucoCard getTrucoCard(){
        return (deck[cursor++]);
    }
    public TrucoCard getCard(byte myKind, byte myValue){
    	for (int i=0; i<40; i++){
    		if (deck[i].getKind() == myKind && deck[i].getValue() == myValue)
    			return deck[i];	
    	}
    	return null;
    
    }
}
