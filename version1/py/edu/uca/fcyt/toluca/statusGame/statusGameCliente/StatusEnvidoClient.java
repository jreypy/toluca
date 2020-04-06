package py.edu.uca.fcyt.toluca.statusGame.statusGameCliente;

/*
 * statusEnvido.java
 *
 * Created on 24 de marzo de 2003, 09:05 AM
 */

/** Hereda todo statusFlor y crea un statusEnvido
 * @author Cristhian Benitez - Julio Rey
 */
//package py.edu.uca.fcyt.toluca;
public class StatusEnvidoClient extends StatusFlorClient {
    
    /** Busca quien tiene el envido mayor
     * @return 0 si hay un empate, 1 si la flor mayor esta en el equipo 1 o 2 si esta en el equipo 2
     */    
     public int envidoMayor(int equipoMano)
    {
        return super.florMayor(equipoMano);
     }
    
     /** Guarda el valor d eun envido
      * @param quien El nro del jugador valor de 0 a cantidad de jugadores -1
      * @param valor El valor del envido a guardar
      * @return Verdadero si no hubo problemas
      */     
    public int agregarEnvido(int quien,int valor){
        return super.agregarFlor(quien,valor);
    }
    /** Creates a new instance of statusEnvido
     * @param cantidad La cantidad de jugadores
     */
    public StatusEnvidoClient(int cantidad) {
        super.valores=new int[cantidad];
        super.cjugadores=cantidad;
        for(int i=0;i<cantidad;i++)
           super.valores[i]=-1;
    }
    
}
