package py.edu.uca.fcyt.toluca.statusGame;

import py.edu.uca.fcyt.toluca.game.TrucoCard;

import java.util.logging.Logger;

/*
 * statusMano.java
 *
 * Created on 20 de marzo de 2003, 03:11 PM
 */

/**
 * Clase auxiliar que se encarga de manejar lo que pasa en una mano del juego
 *
 * @author Cristhian Benitez - Julio Rey
 */
public class StatusMano {
    protected Logger logeador = Logger.getLogger(StatusMano.class.getName());

    /**
     * Creates a new instance of statusMano
     */
    private TrucoCard[][] cartasJugadas;
    private int cjugadores;
    private int ronda;
    private int[] resultados;
    private int nCartasJugadas;

    /**
     * Crea una instancia del statusMano y crea el vector con la contidad de jugadores
     *
     * @param cantidad Indica la cantidad de jugadores que participan de la mano
     */
    public StatusMano(int cantidad) {

        nCartasJugadas = ronda = 0;
        cartasJugadas = new TrucoCard[3][cantidad];
        resultados = new int[3];
        cjugadores = cantidad;
    }

    /**
     * Indica que la ronda se termino y permite jugar la siguiente ronda
     *
     * @return nada
     */
    public int terminoRonda() {
        resultados[ronda] = resultadoRonda(ronda + 1);
        nCartasJugadas = 0;
        ronda++;
        return 5;
    }

    /*busca la mayor carta del equipo en una ronda
     *equipo 0 el primer equipo 1 el otro-*/
    private int hallarMayorCarta(int equipo, int Ronda) {
        //System.out.println("hallarMayorCarta - sm - equipo " + equipo + "ronda " + Ronda);
        int valor = 0, i, quien = 0;
        for (i = equipo; i < cjugadores; i += 2) {
            if (cartasJugadas[Ronda][i] != null && valor <= cartasJugadas[Ronda][i].getValueInGame()) {
                valor = cartasJugadas[Ronda][i].getValueInGame();
                quien = i;
            }
        }
        return quien;//retorna el lugar donde esta la carta mayor
    }

    private int hallarQuienMayorCarta(int Ronda) {//Busca las cartas mayores en los dos equipos y luego las compara
        int valor = 0, i = 0, eq1 = 0, eq2 = 0, v1, v2;
        eq1 = hallarMayorCarta(0, Ronda);//el jugador con la carta mas alta
        eq2 = hallarMayorCarta(1, Ronda);
//		System.out.println("*************Valores de las cartas de la ronda****************");
//		System.out.println(" equipo uno jugo "+cartasJugadas[Ronda][eq1]);
//		System.out.println(" equipo dos jugo "+cartasJugadas[Ronda][eq2]);

        v1 = cartasJugadas[Ronda][eq1].getValueInGame();//el valor de su carta mas alta
        v2 = cartasJugadas[Ronda][eq2].getValueInGame();
//		System.out.println(" v1 = "+v1+" v2 = "+v2);
//		System.out.println("***************************************************************");
        if (v1 > v2)
            return eq1;
        else if (v2 > v1)
            return eq2;
        else//si empate
            return -1;
    }

    public int resultadoRonda(int Ronda) {
        return hallarQuienMayorCarta(Ronda - 1);
    }

    /**
     * Representa el juego de una carta, cuando el jugador tira a la mesa su carta
     * Guarda la carta en un vector
     *
     * @param jugador Un int que representa el numero del jugador
     * @param cual    Un TrucoCard que es la carta que el jugador juega
     * @return retorna 5 si se pudo jugar la carta correctamente o 0 si hubo un error
     */
    public int jugarCarta(int jugador, TrucoCard cual) {
        if (jugador >= 0 && jugador < cjugadores && cartasJugadas[ronda][jugador] == null) { //nro de jugador valido y que no halla carta alli
            cartasJugadas[ronda][jugador] = cual;
            nCartasJugadas++;
            return 5;
        } else if (cartasJugadas[ronda][jugador] != null) {
            logeador.warning("Jugador [" + jugador + "] ya jugó carta en la ronda [" + ronda + "]");
        }
        logeador.warning("Indice de Jugador Inválido");
        return 0;

    }

}
