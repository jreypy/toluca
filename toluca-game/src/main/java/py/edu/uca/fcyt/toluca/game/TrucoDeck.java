/**
 *
 * @author  Cristhian Benitez - Julio Rey
 */
/**
 * <p>
 * Representa el mazo de cartas del truco.
 * </p>
 */
package py.edu.uca.fcyt.toluca.game;

public class TrucoDeck {
    private byte indiceArriba;
    private byte indiceAbajo;
    private TrucoCard[] elMazo;
    /** Llena el mazo de cartas */
    private void llenarMaso()
    {
        int con=0;
        for(int palo=1;palo<=4;palo++){
            for(int valor=1;valor<=12;valor++) {
                
                if((valor>=1 && valor<8) || (valor>=10)){
                   elMazo[con]=new TrucoCard(palo,valor);
                   con++;
                }
            }
        }
    }
    /**
     *<p>
     *Crea una nueva instancia del TrucoDeck, la llena de cartas y la mezcla
     *<p>
     */
    public TrucoDeck() {
        elMazo=new TrucoCard[40];
        llenarMaso();
        shuffle();
        indiceArriba=39;
        indiceAbajo=0;
    }
    /**
     *<p>
     *Devuelve la carta que esta ensima del mazo
     *@return un TrucoCard que es la carta en el tope del mazo
     *<p>
     */
    public TrucoCard getTopCard() {
        TrucoCard aux;
        aux=elMazo[indiceArriba];
        indiceArriba--;
        return aux;
    }
    /**
     *<p>
     *Devuelve la carta que esta abajo del mazo
     *@return un TrucoCard que es la carta al pie del mazo
     *<p>
     */
    TrucoCard getBottomCard() {
        TrucoCard aux;
        aux=elMazo[indiceAbajo];
        indiceAbajo++;
        return aux;
    }
	private void intercambiar(Object o1, Object o2)	{
		Object tmp = o1;
		o1 = o2;
		o2 = tmp;	
	}
	
    /**
    *<p>
    * Mezcla las cartas del maso
    *<p>
    */
    void shuffle()	{
	int aux,aux2;
        TrucoCard temp;
        for(int j=0;j<6;j++){
            for(int i=0;i<40;i++) {
                aux=(int) (Math.random()*39);
                aux2=(int) (Math.random()*39);
                temp=elMazo[aux2];
                elMazo[aux2]=elMazo[aux];
                elMazo[aux]=temp;
            }
            for(int i=0;i<=39;i++)
            {
                aux=(int) (Math.random()*39);
                temp=elMazo[i];
                elMazo[i]=elMazo[aux];
                elMazo[aux]=temp;
            }
        }
    }
    
    
    void Cut(int index) {
    }
	public TrucoCard getCard(byte myKind, byte myValue){
		   for (int i=0; i<40; i++){
			   if (elMazo[i].getKind() == myKind && elMazo[i].getValue() == myValue){
                                System.out.println("la carta buscada fue encontrada con exito");
				   return elMazo[i];	
                           }
		   }
                   System.out.println("la carta buscada fue no encontrada con exito :(");
		   return null;
    
	   }
    
}
