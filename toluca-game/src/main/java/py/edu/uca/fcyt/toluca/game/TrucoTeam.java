/*
 * TrucoTeam.java
 *
 * Created on 5 de marzo de 2003, 02:58 PM
 */

package py.edu.uca.fcyt.toluca.game;


/** Clase que representa a un equipo de Truco.
 * @author Julio Rey
 */
public class TrucoTeam extends Team{
    
    public static final int ROJO = 0;
    public static final int AZUL = 1;
    
    /** Constructor de instancia de TrucoTeam.
     * @param name String que representa el nombre identificador del TrucoTeam.
     */
    public TrucoTeam(String name) {
        super(name);
    }
    /** Constructor de Instancia del TrucoTeam.
     */    
    public TrucoTeam(){
    }
    /** Retorna el numero del TrucoPlayer.
     * @return int que representa el numero de ese equipo.
     * @param pl TrucoPlayer de quien se devolver? su numero.
     */    
    public int getNumberOfPlayer(TrucoPlayer pl){
        for (int i=0; i<playersList.size(); i++){
    		if ((TrucoPlayer)(playersList.get(i))==pl)
    			return i;
    	}
    	return -1;
    }
    /** Retorna el Player de numero i.
     * @param numberOfPlayer numero de TrucoPlayer a ser retornado.
     * @return TrucoPlayer que es el integrante numero i del TrucoTeam.
     */    
    public TrucoPlayer getTrucoPlayerNumber (int numberOfPlayer){
        return (TrucoPlayer)super.getPlayerNumber(numberOfPlayer);
    }
    /** Verifica si es integrante del TrucoTeam.
     * @return <B>true</B> si es integrante, <B>false</B> si no es integrante.
     * @param tPl TrucoPlayer a ser verificado si es integrante.
     */    
    public boolean isPlayerTrucoTeam(TrucoPlayer tPl){
        return super.isPlayerTeam(tPl);
    }
    /** Adherir nuevo TrucoPlayer al Equipo.
     * @param tPl TrucoPlayer a ser adherido a la lista de TrucoPlayers del equipo.
     */    
    
    public void addPlayer (TrucoPlayer tPl){
        super.addPlayer(tPl);   
    }
    public TrucoPlayer getPlayer (String aname){
		for (int i=0; i<playersList.size(); i++){
			if (((TrucoPlayer)(playersList.get(i))).getName()==aname)
				return (TrucoPlayer)(playersList.get(i));
				
		}
		return null;
    }
    
}
