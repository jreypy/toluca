/*
 * game.java
 *
 * Created on 3 de marzo de 2003, 10:05 PM
 */

package py.edu.uca.fcyt.game;

/**
 *
 * @authors  Julio Rey || Christian Benitez
 */
public class Game {
    
    /** Creates a new instance of game */
    protected int numberOfPlayers;
    protected int gameNumber;
    
    public Game() {
    }
    
    public int getGameNumber() { //obtener el numero de juego
       return (gameNumber);
    } 
    
    public void setGameNumber(int gameNumber) { //asigna el numero de Juego
        this.gameNumber = gameNumber;
    }
    
    public int getNumberOfPlayers() {   //obtener el numero de players
        return numberOfPlayers;
    }
    
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
