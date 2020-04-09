package py.edu.uca.fcyt.toluca.game;

/*
 * statusFlor.java
 *
 * Created on 23 de marzo de 2003, 02:07 PM
 */

/** Clase auxiliar para guardar los cantos de las flores, verificar quien gano
 * o si hubo empte
 * @author Cristhian Benitez Julio Rey
 */
//package py.edu.uca.fcyt.toluca;
public class statusFlor {
    protected  int[] valores;
    protected  int cjugadores;
    protected boolean hayAlguna;
    /** Agrega el canto de una flor
     * @param quien Indica el nro del jugador que canto
     * @param valor El valor del canto del jugador. <B>No verifica si puede cantar o no</B>
     * @return Verdadero si no hubo problemas. Falso si el nro del jugador no es valido
     * o si ya tiene un valor de una flor previa
     */    
    public int agregarFlor(int quien,int valor)
    {
        hayAlguna=true;
        if(quien>=0 && quien<cjugadores && valores[quien]>-1)
            valores[quien]=valor;
        else
            return 0;
        return 5;
    }
    /** Devuelve cuantas personas cantaron flor en el equipo que figura e team
     * @param team Falso para el equipo 1 Verdadero para el 2
     * @return La cantidad de personas que cantaron Flor
     */    
    public int floresTeam(boolean team)
    {
        int total=0;
        int equipo=0;
        if (team)
            equipo=1;
        for(int i=equipo;i<cjugadores;i+=2)
            if(valores[i]!=-1)
                total++;
        return total;
    }
    
    /** Buscar flor mayor en los dos equipos
     * @return 0 si hay un empate, 1 si la flor mayor esta en el equipo 1 o 2 si esta en el equipo 2
     */    
    public int florMayor()
    {
        int mayor,lugar;
        boolean empate=false;
        mayor=-1;
        lugar=0;
        for(int i=0;i<cjugadores;i++)        {
            if(valores[i]>mayor){
                lugar=i;
                mayor=valores[i];
                empate=false;
            }else if(valores[i]==mayor)
                empate=true;
        }
        if(empate)
            return -1;
        else {
            if((lugar%2)==0)
                return 0;
            else
                return 1;
        }
    }
    /** Creates a new instance of statusFlor
     * @param cantidad Indica el nro de jugadores
     */
    public statusFlor(int cantidad) {
        cjugadores=cantidad;
        valores=new int[cantidad];
        hayAlguna=false;
        for(int i=0;i<cantidad;i++)
            valores[i]=-1;
    }
    
    public statusFlor(){
        hayAlguna=false;
        cjugadores=-1;
    }
    
}



