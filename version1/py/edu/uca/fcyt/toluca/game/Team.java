/*
 * Team.java
 *
 * Created on 3 de marzo de 2003, 10:45 PM
 */
package py.edu.uca.fcyt.toluca.game;

import java.util.ArrayList;
import java.util.LinkedList;

/** Clase que representa a un Equipo de cualquier juego.
 * @authors Julio Rey || Christian Benitez
 */
public class Team {
    
    /** Creates a new instance of Team */
    protected int points=0;
    /** Numero de jugadores que tiene el Equipo.
     */    
    protected int numberOfPlayers=0;
    /** Nombre del Equipo.
     */    
    protected String name;
    /** Lista de los jugadores que tiene el Equipo.
     */    
    protected ArrayList playersList = new ArrayList();
    /** Contructor del Equipo.
     */    
    public Team() { //constructor
        numberOfPlayers = 0;
    }
    /*definir un nombre al equipo*/
    /** Contructor del Equipo, con un nombre identificador.
     * @param name Nombre del Equipo.
     */    
    public Team(String name){
        this.name = name; 
    }
    /*insertar players*/
    /** Adherirse un nuevo jugador al Equipo.
     * @param pl Jugador que se adhiere.
     */    
    public void addPlayer(TrucoPlayer pl){
        numberOfPlayers++;
        playersList.add(pl);
    }
    /*obtener el player numero x*/
    /** Retorna el Player  numero int, del Equipo.
     * @param numberOfPlayer Numero del Player que será Retornado.
     * @return El Player del Equipo.
     */    
    public TrucoPlayer getPlayerNumber (int numberOfPlayer){
        return (TrucoPlayer)playersList.get(numberOfPlayer);
    }
    /*retorna la cantidad de jugadores que tiene el equipo*/
    /** Retorna la cantidad de jugadores que tiene el Equipo.
     * @return el valor de su numero de integrantes.
     */    
    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }
    /*puntos del equipo*/
    /** Retorna el puntaje del Equipo.
     * @return El valor de su puntaje.
     */    
    public int getPoints(){
        return points;
    }
    /** Verifica si un Player es integrante del Equipo.
     * @param pl Player a verificar si es integrante.
     * @return Retorna <B>true</B> en caso de ser integrante, <B>false</B> en caso contrario.
     */    
    public boolean isPlayerTeam(TrucoPlayer pl){
    	for (int i=0; i<playersList.size(); i++){
    		if ((TrucoPlayer)(playersList.get(i))==pl)
    			return true;
    	}
    	return false;
    }
    /** Retorna el Nombre del Equipo.
     * @return String que identifica al Equipo.
     */    
    public String getName(){
    	return name;
    }
    /** Configura el nombre del equipo.
     * @param newname String a ser asignado como nombre identificador del equipo.
     */    
    public void setName(String newname){
        name = newname;
    }
    /** Configura el Puntaje del Equipo.
     * @param pts puntaje a ser asignado al Equipo.
     */    
    public void setPoints(int pts){
        points = pts;
    }
    
    public ArrayList getPlayers() {
    	return playersList;
    }
}
