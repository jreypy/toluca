package py.edu.uca.fcyt.toluca.statusGame.statusGameCliente;

import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.statusGame.StatusEnvido;
import py.edu.uca.fcyt.toluca.statusGame.StatusFlor;
import py.edu.uca.fcyt.toluca.statusGame.StatusMano;
import py.edu.uca.fcyt.toluca.statusGame.StatusPlayer;
import py.edu.uca.fcyt.toluca.statusGame.TrucoStatusTable;
//import py.edu.uca.fcyt.toluca.statusGame;


public class TrucoStatusTableCliente extends TrucoStatusTable{

        /** Recibe las cartas del jugador
         */             
        public void recibirCartas(int player,TrucoCard cartas[]) //reparte  3 cartas a todos los jugadores
        {
            //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Agregando Cartas!!!!!!!");
            for(int i=0;i<3;i++){
                    //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "carta para"+player+","+cartas[i].getValue()+" de "+cartas[i].getKind());
                    estado[player].agregarCarta(cartas[i]);
            }
        }
        /** Imprime en la consola las cartas del jugador <B>(Quitar en la version final)</B>
         * @param cual El nro del jugador
         */        
        public TrucoStatusTableCliente(int cantidadDeJugadores)
        {
            cJugadores=cantidadDeJugadores;
            estado=new StatusPlayer[cJugadores];
            flores=new StatusFlor(cJugadores);
            envidos=new StatusEnvido(cJugadores);
            mano=new StatusMano(cJugadores);     
            //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "Ya se creo los objetos status...");
            for (int i=0;i<cJugadores;i++)
                    estado[i]=new StatusPlayer();
         }
        
        /** Recupera las cartas del jugador
         * @param jugador El numero del jugador
         * @return Un vector con los jugadores
         */        
       public TrucoCard[] getPlayerCards(int jugador)
       {
           if(jugador>=0 && jugador< cJugadores)
               return estado[jugador].mostrarCartas();
           else
               return null;
       }
        
}
	