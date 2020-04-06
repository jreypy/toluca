package py.edu.uca.fcyt.toluca.statusGame;

import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoDeck;


public class TrucoStatusTable{
	protected int cJugadores; //Guarda la cantidad de jugadores
        protected py.edu.uca.fcyt.toluca.statusGame.StatusPlayer[] estado;
        protected py.edu.uca.fcyt.toluca.statusGame.StatusEnvido envidos;
        protected TrucoDeck elMazo;
        protected py.edu.uca.fcyt.toluca.statusGame.StatusFlor flores;
        protected py.edu.uca.fcyt.toluca.statusGame.StatusMano mano;
        private void repartir() //reparte  3 cartas a todos los jugadores
        {
            for(int i=0;i<3;i++)
                for(int j=0;j<cJugadores;j++){
                    estado[j].agregarCarta(elMazo.getTopCard());
                }
                
        }
        /** Imprime en la consola las cartas del jugador <B>(Quitar en la version final)</B>
         * @param cual El nro del jugador
         */        
        public void imprimirEstado(int cual)
        {
            estado[cual].statusPrint();
        }
        public TrucoStatusTable(){
            estado=new StatusPlayer[cJugadores];
            flores=new StatusFlor(cJugadores);
            envidos=new StatusEnvido(cJugadores);
            mano=new StatusMano(cJugadores);            
            for (int i=0;i<cJugadores;i++)
                    estado[i]=new StatusPlayer();
        }
        public TrucoStatusTable(int cantidadDeJugadores)
        {
            cJugadores=cantidadDeJugadores;
            estado=new py.edu.uca.fcyt.toluca.statusGame.StatusPlayer[cJugadores];
            flores=new py.edu.uca.fcyt.toluca.statusGame.StatusFlor(cJugadores);
            elMazo=new TrucoDeck();
            flores=new py.edu.uca.fcyt.toluca.statusGame.StatusFlor(cJugadores);
            envidos=new py.edu.uca.fcyt.toluca.statusGame.StatusEnvido(cJugadores);
            mano=new py.edu.uca.fcyt.toluca.statusGame.StatusMano(cJugadores);
            
            for (int i=0;i<cJugadores;i++)
                    estado[i]=new py.edu.uca.fcyt.toluca.statusGame.StatusPlayer();
            repartir();
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
       public void terminoRonda(){
            mano.terminoRonda();
       }
       //--------------------------------------------------------------------------------------------------------
//        Seccion Juego - Varios
//--------------------------------------------------------------------------------------------------------
        /*posibilidad de jugar carta dicho jugador, editado por Julio Rey*/
       public boolean puedeJugarCarta(int jugador, TrucoCard carta)
        {
            
            if(jugador >= 0 && jugador < cJugadores && estado[jugador].puedeJugarCarta(carta) == 1){
                return true;
            }
            return false;
                
        }
       public TrucoCard getCardNoPlaying(int Jugador){
           if (Jugador >= 0 && Jugador < cJugadores)
               return estado[Jugador].getCardNoPlaying();
          // System.out.println("ocurre un grave error!!!");
           return null;
       }
       public TrucoCard getCardNoPlayingForEnvido(int Jugador){
           if (Jugador >= 0 && Jugador < cJugadores)
               return estado[Jugador].getCardNoPlayingForEnvido();
          // System.out.println("ocurre un grave error!!!");
           return null;
       }
       /** Juega una carta del jugador
        * @param jugador El nro de jugador
        * @param carta La carta que se va a jugar
        * @return Verdadero si se puede jugar esa carta
        */       
	public boolean jugarCarta(int jugador, TrucoCard carta)
        {
            if(jugador>=0 && jugador< cJugadores && estado[jugador].puedeJugarCarta(carta)==1){
               if(mano.jugarCarta(jugador,carta)!=5){
                   System.out.println("mano.jugarCarta por este motivo no pude jugar....");
                   return false;
               }
               return estado[jugador].jugarCarta(carta);
           } else
               return false;
        } 
        public boolean jugarCartaOffLine(int jugador, TrucoCard carta){
            if(jugador>=0 && jugador< cJugadores && estado[jugador].puedeJugarCarta(carta)==1)
                return estado[jugador].jugarCarta(carta);
            return false;
        }
        
        /** Cierra a un jugador. Un jugador cerrado ya no puede jugar cartas
         * @param jugador
         * @return  */        
        public boolean seCerro(int jugador)
        {
            //System.out.println("cerrar a "+ jugador);
            if(jugador>=0 && jugador<cJugadores)
                estado[jugador].cerrar();
            else 
                return false;
            return true;
        }

        /** Recupera el estado de cierre del jugador.
         * @param jugador El nro del jugador
         * @return Verdadero si el jugador esta cerrado
         */        
        public boolean estaCerrado(int jugador)
        {
            //System.out.println("pregunta si esta cerrado");
            if(jugador>=0 && jugador<cJugadores)
                return estado[jugador].estaCerrado();
            return false;
        }
        
        /** Devuelve Verdadero si el jugador ya jugo alguna carta
         * @param jugador El nro del jugador
         * @return Verdadero si ya jugo alguna carta. Falso caso contrario
         */        
        public boolean jugoPrimeraCarta(int jugador)
        {
            if(jugador>=0 && jugador<cJugadores)
                return estado[jugador].jugoPrimeraCarta();
            return false;
        }
        
        public int resultadoRonda(int ronda)
        {
            return mano.resultadoRonda(ronda);
        }
//--------------------------------------------------------------------------------------------------------
//        Seccion Flor
//--------------------------------------------------------------------------------------------------------

        /** Guarda a modo de bandera si el jugador canto o no flor durante la mano
         * y verifica si puede cantar y canta
         * @param jugador El nro de jugador
         */        
	public boolean cantaFlor(int jugador)//banderas
        {
            if(!puedeCantarFlor(jugador))
                return false;
            estado[jugador].cantoFlor=true;
            return true;
        }

        /** Pregunta si el jugador puede o no cantar flor
         * @param jugador El nro de jugador
         * @return Verdadero si no hubo problemas falso si los hubo
         */        
        public boolean puedeCantarFlor(int jugador)
       {           
                return estado[jugador].puedeCantarFlor();
       }

        /** Pregunta si el jugador canto flor
         * @param jugador El nro de jugador
         * @return Verdadero si el jugador <B>ya canto </B>flor, falso si no
         */        
        public boolean mostroFlor(int jugador)
        {
            return estado[jugador].cantoFlor;
        }
        
        /** Verifica si tiene flor el jugador
         * @param jugador El nro del jugador
         * @return Verdadero si el jugador tiene flor
         */        
	public boolean tieneFlor(int jugador)
        {
             return estado[jugador].puedeCantarFlor();
        }
        /** Realiza el juego de una flor, verifica que se pueda jugar, guarda las baderas y los valores
         * @param jugador El nro del jugadorb que canto
         * @param valor El valor de la flor
         * @return Verdadero si se pudo jugar la flor, falso si no
         */        
	public boolean jugarFlor(int jugador, int valor)
        {
            if(estado[jugador].puedeCantarFlor() && estado[jugador].contarFlor()==(int)valor){
                estado[jugador].cantoFlor=true;
                flores.agregarFlor(jugador,valor);
                return true;
            } else 
                return false;
           
        }     
        /** Devuelve el valor de la flor del jugador, se debe estar seguro que el jugador tiene flor antes porque la <B>funcion no controla </B>si tiene o no
         * @param jugador el numero del jugador
         * @return El valor de la flor
         */        
        public int valor_flor(int jugador)
        {
                return estado[jugador].contarFlor();
        }

        /** Devuelve quien gano la ronda de flores
         * @return 1 para el equipo par 2 para el impar 0 en caso de empate
         */        
        public int resultadoFlor(int equipoMano)
        {
            return flores.florMayor(equipoMano);
        }
        public boolean mostraraFlor (int jugador,int equiMano){
            if (estado[jugador].jugoTresCartas())
                return true;
            if (resultadoEnvido(equiMano) != jugador)
                return false;
            if (jugador >= 0 && jugador < cJugadores){
                return estado[jugador].mostraraFlor();
            }
            System.out.println("Error en TST-mostraraFlor: Avisar a la gente de TrucoGame");
            return false;
        }
//--------------------------------------------------------------------------------------------------------
//        Seccion Envido
//--------------------------------------------------------------------------------------------------------
    
        /** Verifica que el jugador tenga el envido que canto
         * @param jugador El nro del jugador
         * @param valor El valor del envido
         * @return Verdadero si puede cantar
         */        
        public boolean tieneEnvido(int jugador,int valor)
        {
            if(jugador>=0 && jugador<cJugadores)
                return estado[jugador].puedeCantarEnvido(valor);
            else
                return false;
        }
        /*creado por Julio Rey, devolver el mejor envido*/
        public int getValueOfEnvido(int jugador){
            if(jugador >= 0 && jugador<cJugadores){
                return estado[jugador].getValueOfEnvido();
            }
            return -1;
            
        }
        /**
         * @param jugador
         * @param valor
         * @return
         */        
        
        
        public boolean jugarEnvido(int jugador,int valor)
        {
           // System.out.println("jugar envido verifica:"+jugador+"que quiere cantar"+valor);
            if(tieneEnvido(jugador,valor))    {
                estado[jugador].cantoEnvido=true;
                if(envidos.agregarEnvido(jugador,valor)==5)
                    return true;
            }
           // System.out.println("TT - jugar envido , false;");
            return false;
        }
        
        

        public int resultadoEnvido(int equipoMano)
        {
            return envidos.envidoMayor(equipoMano);
        }
        public boolean mostroEnvido (int jugador){
            if (jugador >= 0 && jugador < cJugadores){
                return estado[jugador].mostroEnvido();
            }
            System.out.println("Error en TST-mostroEnvido: Avisar a la gente de TrucoGame");
            return false;
        }
        public boolean jugoTresCartas(int jugador){
            return (estado[jugador].jugoTresCartas());
        }
        public boolean jugoSusCartasDeEnvido(int jugador){
            if (jugador >= 0 && jugador < cJugadores){
                return (estado[jugador].jugoTresCartas());
            }
            System.out.println("Error en TST-jugo Sus Cartas de Envido: Avisar a la gente de TrucoGame");
            return false;
        }
        public TrucoCard getCard(byte myKind, byte myValue){
        	return elMazo.getCard((byte)myKind, (byte)myValue); 
        
        }
}
	