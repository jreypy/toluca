package py.edu.uca.fcyt.toluca.statusGame;

import py.edu.uca.fcyt.toluca.game.TrucoCard;


/** Guarda y controla el estado de un jugador en la mano
 */
//package py.edu.uca.fcyt.toluca;
public class StatusPlayer{
    private TrucoCard[] cartas;
    private boolean primeraCarta;//Guarda verdadero si el jugador jugo la primera carta
    private int cCartas; //Indica la cantidad de cartas que tiene el jugador
    private int cantidadDeCartasJugadas;/*julio coloco este atributo porque no entendio el cCartas como funcionaba*/
    private boolean seCerro;
    /**
     */    
    public boolean envido;
    public boolean flor;
    /** Indica si el jugador canto o no flor
     */
    public boolean cantoFlor;
    /** indica si el jugador canto o no envido
     */    
    public boolean cantoEnvido;

    /** Constructor por omision, crea una nueva instancia de StatusPlayer
     */    
    public StatusPlayer(){
        seCerro=primeraCarta=envido=flor=cantoEnvido=cantoFlor=false;
        cartas=new TrucoCard[3];
        cCartas=0;
        cantidadDeCartasJugadas = 0;
    }
    /** Guarda un el estado de cierre del jugador. El jugador que se cierra no puede jugar mas
     */    
    public void cerrar()
    {
        seCerro=true;
    }
    public boolean estaCerrado()
    {
        return seCerro;
    }
    /** Recive la carta cuando se le reparte y la guarda
     * @return Verdadero si Jugo ya alguna carta, falso si no
     */
    public boolean jugoPrimeraCarta (){
        return primeraCarta;
    }
    
    public boolean jugoTresCartas (){
        if(cantidadDeCartasJugadas==3)
            return true;
        else
            return false;
    }
    
    /** Recibe las cartas cuando se reparten
     * @param Cual Es la carta que se recibe
     */    
    public void agregarCarta(TrucoCard Cual){
        //new Exception("repartija de cartas").printStackTrace(System.out);
     //   System.out.println("Agregando carta al player!!");
        cartas[cCartas]=Cual;
        cCartas++;
    }
    /** Retorna un vector de tres con las cartas del jugador
     * @return Vector de con 3 TrucoCards que son las cartas del jugador
     */    
    public TrucoCard[] mostrarCartas()
    {
        return cartas;
    }
    //Imprime un estado borrar para publicar
    /** Imprime un estado de las cartas de jugador en la consola "QUITAR"
     */
    public void statusPrint(){
        System.out.println("Cartas");
        for(int i=0;i<3;i++){
            System.out.print("Palo: ");
            System.out.print(cartas[i].getKind());
            System.out.print(" - ");
            System.out.print("Valor: ");
            System.out.println(cartas[i].getValue());
            System.out.print(" Jugada: ");
            System.out.println(cartas[i].isFlipped());
        }
            System.out.println(puedeCantarFlor());
     }
    
    private int valor_envido(TrucoCard cual)
    {// Devuelve el valor de la carta en el envido
        if(cual.getValue()>=10)
            return 0;
        else
            return cual.getValue();
    }

    private boolean son_mismo_palo(TrucoCard uno,TrucoCard dos)
    { //Verdadero si son del mismo palo
        if(uno.getKind()==dos.getKind())
            return true;
        else
            return false;
    }
    /** verifica si puede cantar ese tanto con alguna combinacion de dos carta del mismo palo
     * @param cuales
     * @param cuanto 
     * @return  */
    private boolean contar_tanto(TrucoCard[] cuales, int cuanto)
    {
        if (son_mismo_palo(cuales[0],cuales[1]) && valor_envido(cuales[0]) + valor_envido(cuales[1])+20 == cuanto){
            return true;
        }
        if (son_mismo_palo(cuales[0],cuales[2]) && valor_envido(cuales[0]) + valor_envido(cuales[2])+20 == cuanto){
            return true;
        }
        if (son_mismo_palo(cuales[1],cuales[2]) && valor_envido(cuales[1]) + valor_envido(cuales[2])+20 == cuanto){
            return true;
        }
        return false;                                
    }
    /** devuelve el envido mayor del jugador
     * @return  el valor del envido - deprecated for Julio Rey*/
    /*public int contarTanto()
    {
        return contar_tanto(cartas);
    }*/
    /** Devuelve Verdadero si puede cantar envido del valor de cuanto
     * @param cuanto indica cuento es el valor del envido
     * @return  verdadero si es cierto que puede cantar eso*/
    public boolean puedeCantarEnvido(int cuanto)
	//Devuelve verdadero si el jugador player puede cantar cuanto de envido
    {
         //System.out.println ("conte " + contar_tanto(cartas) +"\n y me preguntaron " + cuanto );
        if(cuanto>=20 && contar_tanto(cartas,cuanto)){	
            return true;}
        else{
            if(cuanto<20)
                for(int i=0;i<3;i++)
                    if(valor_envido(cartas[i])==cuanto)
                        return true;
            return false;
        }
    }
    public boolean mostroEnvido (){
        if (!seCerro)
            return true;
        if (jugoSusCartasDeEnvido())
            return true;
        return false;
    }
    public boolean jugoSusCartasDeEnvido(){
        int valueOfEnvido= getValueOfEnvido();
        if (valueOfEnvido >= 20){
            for (int i=0; i<3; i++){
                if (son_mismo_palo(cartas[i],cartas[(i+1)%3])){
                    int valorAct = valor_envido(cartas[i])+valor_envido(cartas[(i+1)%3])+20;
                    if (valorAct==valueOfEnvido){
                        if (cartas[i].isFlipped() && cartas[(i+1)%3].isFlipped())
                            return true;
                    }
                }
            }
            return false;
        }
        for (int i=0; i<3; i++){
            int valorAct = valor_envido(cartas[i]);
            if (valorAct ==valueOfEnvido && cartas[i].isFlipped())
                return true;
        }
        return false;
    }
    public int getValueOfEnvido (){
        int valueOfEnvido=-1;
        for (int i=0; i<3; i++){
            if (son_mismo_palo(cartas[i],cartas[(i+1)%3])){
                int valorAct = valor_envido(cartas[i])+valor_envido(cartas[(i+1)%3])+20;
                if (valorAct>valueOfEnvido)
                    valueOfEnvido = valorAct;
            }
        }
        if(valueOfEnvido >= 0)
            return valueOfEnvido;
        for (int i=0; i<3; i++){
            int valorAct = valor_envido(cartas[i]);
            if (valorAct > valueOfEnvido)
                valueOfEnvido = valorAct;
        }
        return valueOfEnvido;
    }
    /** Devuelve Verdadero si el jugador tiene flor
     * @return  verdadero si el jugador puede cantar o si tiene una flor
     */
    public boolean puedeCantarFlor()
    {
        if(cartas[0].getKind()==cartas[1].getKind() && cartas[0].getKind()==cartas[2].getKind())
            return true;
        return false;
    }
    /** Cuenta el valor de la flor del jugador
     * @return Retorna un int con el valor de la flor o 0 en caso que el jugador no tenga flor
     */    
    public int contarFlor()
    {
        if(puedeCantarFlor()){
            return (valor_envido(cartas[0])+valor_envido(cartas[1])+valor_envido(cartas[2]));
           }
        else 
            return 0;
    }
    /*metodo insertado por julio*/
    public TrucoCard getCardNoPlaying(){
        for (int i=2; i>=0; i--){
            if(!cartas[i].isFlipped())
                return cartas[i];
        }
        return null;
    }
    public TrucoCard getCardNoPlayingForEnvido(){
        int valueOfEnvido=getValueOfEnvido();
        TrucoCard cartaAEnviar = null;
        if (valueOfEnvido >= 20){
            for (int i=0; i<3; i++){
                if (son_mismo_palo(cartas[i],cartas[(i+1)%3])){
                    int valorAct = valor_envido(cartas[i])+valor_envido(cartas[(i+1)%3])+20;
                    if (valorAct==valueOfEnvido){
                        if (!cartas[i].isFlipped() && (i+1)%3>i)
                            return cartas[i];
                        if (!cartas[(i+1)%3].isFlipped())
                            return cartas[(i+1)%3];
                        return cartas[i];
                    }
                }
            }
        }
        for (int i=2; i>=0; i--){
            int valorAct = valor_envido(cartas[i]);
            if (valorAct == valueOfEnvido)
                return cartas[i];
    }
        return null;
    }
    /** Marca una carta como jugada
     * @param cual indica que carta se va a jugar
     * @return  devuelve verdadero si se pudo jugar o falso y ocurrio un error (la carta no existe o ya se jugo)*/
    

    public boolean jugarCarta (TrucoCard cual)
    {
//         if (cual == null){
//             System.out.println("La cual Carta es null!");
//         }
//         else{
//             System.out.println("La cual no es null"+cual.getKind()+"-"+cual.getValue());
//         }
             
        for(int i=0;i<3;i++){
            if (cartas[i] == null){
                System.out.println("cartas["+i+"]"+"is null ;(");
            }
            if(cartas[i].equals(cual) && cartas[i].isFlipped()==false){
                cartas[i].setFlipped(true);
                cantidadDeCartasJugadas++;
                primeraCarta=true;
                return true;
            }
        }
        return false;
    }
    
    /** Verifica si el jugador puede jugar una carta
     * @param cual Es la carta que el jugador desea jugar
     * @return Devuelve 1 si puede jugar, 0 si ya se jugo y -1 si el jugador no tiene esa carta
     */    
    public int puedeJugarCarta(TrucoCard cual)
    {//Busca la carta y verifica si la carta no se jugo ya
        if (cual == null){
            System.out.println("statusplayer trucocard cual es null");
        }
        if(!seCerro){           
            for(int i=0;i<3;i++){
                //if(cartas[i]==cual && cartas[i].isFlipped()==false)
                if(cartas[i].equals(cual) && cartas[i].isFlipped()==false){
                    System.out.println("status player 1 - ok");
                    return 1;
                }
                else if(cartas[i].equals(cual) && cartas[i].isFlipped()==true){
                    System.out.println("status player 2 - ok");
                    return 0;}
            }       
        }
        System.out.println("status player 3 - ok");
        return -1;
    }
    public boolean mostraraFlor (){
        if (cantidadDeCartasJugadas == 3)/*ya mostro*/
            return true;
        if (cantidadDeCartasJugadas == 0)/*no puede mostrar*/
            return false;
        if (jugoSusCartasDeEnvido() && cantidadDeCartasJugadas == 2)
            return false;

        int valueOfEnvido= getValueOfEnvido();
        if (valueOfEnvido >= 20){
            for (int i=0; i<3; i++){
                if (son_mismo_palo(cartas[i],cartas[(i+1)%3])){
                    int valorAct = valor_envido(cartas[i])+valor_envido(cartas[(i+1)%3])+20;
                    if (valorAct==valueOfEnvido){
                        if (!cartas[i].isFlipped() && cartas[(i+1)%3].isFlipped()){
                            if (cantidadDeCartasJugadas == 2)
                                return true;
                            if (cantidadDeCartasJugadas == 1)
                                return false;
                        }
                        if (cartas[i].isFlipped() && !cartas[(i+1)%3].isFlipped()){
                            if (cantidadDeCartasJugadas == 2)
                                return true;
                            if (cantidadDeCartasJugadas == 1) 
                                return false;
                        }
                        if (!cartas[i].isFlipped() && !cartas[(i+1)%3].isFlipped()){
                            if (valueOfEnvido != 20)
                                return true;
                            return false;
                        }
                    }
                }
            }
            return false;
        }
        for (int i=0; i<3; i++){
            int valorAct = valor_envido(cartas[i]);
            if (cartas[i].isFlipped())
                return false;
        }
        if (cantidadDeCartasJugadas == 2)
            return true;
        return false;
    }
}
