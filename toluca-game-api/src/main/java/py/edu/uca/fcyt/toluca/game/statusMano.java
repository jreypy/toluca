package py.edu.uca.fcyt.toluca.game;


/*
 * statusMano.java
 *
 * Created on 20 de marzo de 2003, 03:11 PM
 */

/** Clase auxiliar que se encarga de manejar lo que pasa en una mano del juego
 * @author Cristhian Benitez - Julio Rey
 */
public class statusMano {
    
    /** Creates a new instance of statusMano */
    private TrucoCard[][] cartasJugadas;
    private int cjugadores;
    private int ronda;
    private int[] resultados;
    private int nCartasJugadas;

    /** Crea una instancia del statusMano y crea el vector con la contidad de jugadores
     * @param cantidad Indica la cantidad de jugadores que participan de la mano
     */    
    public statusMano(int cantidad) {       
            
            nCartasJugadas=ronda=0;
            cartasJugadas=new TrucoCard[3][cantidad];
            resultados=new int[3];
            cjugadores=cantidad;
    }
    /** Indica que la ronda se termino y permite jugar la siguiente ronda
     * @return nada
     */    
    public int terminoRonda()
    {
        resultados[ronda]=resultadoRonda(ronda);
        nCartasJugadas=0;
        ronda++;
        return 5;
    }
    private int hallarMayorCarta(int equipo,int Ronda)
    {
        int valor=0,i,quien=0;
            for(i=equipo;i<cjugadores;i+=2){
                if(cartasJugadas[Ronda][i]!=null && valor<=cartasJugadas[Ronda][i].getValueInGame()){
                    valor=cartasJugadas[Ronda][i].getValueInGame();
                    quien=i;
                    }               
                }
        return quien;//retorna el lugar donde esta la carta mayor
    }
    
    private int hallarQuienMayorCarta(int Ronda)
    {//Busca las cartas mayores en los dos equipos y luego las compara
        int valor=0,i=0,eq1=0,eq2=0,v1,v2;
        eq1=hallarMayorCarta(0,Ronda);
        eq2=hallarMayorCarta(1,Ronda);
        v1=cartasJugadas[Ronda][eq1].getValueInGame();
        v2=cartasJugadas[Ronda][eq2].getValueInGame();
        if(v1>v2)
            return eq1;
        else if (v2>v1)
            return eq2;
        else
            return -1;
    }
    
    public int resultadoRonda(int Ronda){
        return hallarQuienMayorCarta(Ronda);
    }

    /** Representa el juego de una carta, cuando el jugador tira a la mesa su carta
     * Guarda la carta en un vector
     * @param jugador Un int que representa el numero del jugador
     * @param cual Un TrucoCard que es la carta que el jugador juega
     * @return retorna 5 si se pudo jugar la carta correctamente o 0 si hubo un error
     */    
    public int jugarCarta(int jugador,TrucoCard cual)
    {
        if(jugador>=0 && jugador<cjugadores && cartasJugadas[jugador]==null){
            cartasJugadas[ronda][jugador]=cual;
            nCartasJugadas++;
        }else 
            return 0;          
        return 5;
    }
    
}
