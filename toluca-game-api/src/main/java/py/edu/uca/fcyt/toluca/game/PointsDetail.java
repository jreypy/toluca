/*
 * PointsDetail.java
 *
 * Created on 2 de junio de 2003, 01:08 PM
 */

package py.edu.uca.fcyt.toluca.game;

/**
 *
 * @author  julioc
 */
public class PointsDetail {
    
    /** Creates a new instance of PointsDetail */
    public static int ENVIDO = 1;
    public static int ENVIDO_ENVIDO = 2;
    public static int ENVIDO_ENVIDO_ENVIDO = 3;
    public static int REAL_ENVIDO = 4;
    public static int ENVIDO_ENVIDO_REALENVIDO = 5;
    public static int ENVIDO_REALENVIDO_REALENVIDO = 6;
    public static int REALENVIDO_REALENVIDO_REALENVIDO = 7;
    public static int FALTA_ENVIDO=8;
    public static int REALENVIDO_REALENVIDO = 9;
    public static int ENVIDO_REALENVIDO = 10;
    
    
    public static int ENVIDO_NOQUERIDO = 11;
    public static int ENVIDO_ENVIDO_NOQUERIDO = 12;
    public static int ENVIDO_ENVIDO_ENVIDO_NOQUERIDO = 13;
    public static int REAL_ENVIDO_NOQUERIDO = 14;
    public static int ENVIDO_ENVIDO_REALENVIDO_NOQUERIDO = 15;
    public static int ENVIDO_REALENVIDO_REALENVIDO_NOQUERIDO = 16;
    public static int REALENVIDO_REALENVIDO_REALENVIDO_NOQUERIDO = 17;
    public static int FALTA_ENVIDO_NOQUERIDO=18;
    public static int REALENVIDO_REALENVIDO_NOQUERIDO = 19;
    public static int ENVIDO_REALENVIDO_NOQUERIDO = 20;
    
    public static int ENVIDO_NOMOSTRADO = 21;
    public static int ENVIDO_ENVIDO_NOMOSTRADO = 22;
    public static int ENVIDO_ENVIDO_ENVIDO_NOMOSTRADO = 23;
    public static int REAL_ENVIDO_NOMOSTRADO = 24;
    public static int ENVIDO_ENVIDO_REALENVIDO_NOMOSTRADO = 25;
    public static int ENVIDO_REALENVIDO_REALENVIDO_NOMOSTRADO = 26;
    public static int REALENVIDO_REALENVIDO_REALENVIDO_NOMOSTRADO = 27;
    public static int FALTA_ENVIDO_NOMOSTRADA=28;
    public static int REALENVIDO_REALENVIDO_NOMOSTRADA = 29;
    public static int ENVIDO_REALENVIDO_NOMOSTRADA = 30;
    
    public static int FLOR_CANTADA_MOSTRADA = 31;
    public static int FLOR_CANTADA_NOMOSTRADA = 32;
    public static int FLOR_NOCANTADA_MOSTRADA = 33;
    
    public static int RETIRARSE_SIN_DEJAR_QUE_OTROS_CANTEN = 50;
    public static int NO_SE_CANTO_TRUCO = 51;
    public static int TRUCO_NO_QUERIDO = 52;
    public static int TRUCO = 53;
    public static int RETRUCO_NO_QUERIDO = 54;
    public static int RETRUCO = 55;
    public static int VALECUATRO_NO_QUERIDO = 56;
    public static int VALECUATRO = 57;
    
    
 
    protected TrucoTeam equipoGanador;
    protected TrucoPlayer responsable;
    protected int puntosGanados;
    protected int ganadosPor;
    
    public PointsDetail(TrucoTeam tm, int gp, int pg) {
        equipoGanador = tm;
        puntosGanados = pg;
        ganadosPor = gp;
    }
    public PointsDetail (TrucoTeam tm, int gp, int pg, TrucoPlayer responsable){
        this(tm,gp,pg);
        this.responsable = responsable;
    }
    public String aString(){
        String detalle = new String();
        switch (ganadosPor){
            case 1:
                detalle =  puntosGanados + " puntos para "+ equipoGanador.getName()+" por envido de " + responsable.getName();
                break;
            case 2:
                detalle =  puntosGanados + " puntos para "+ equipoGanador.getName()+" por envido, envido de " + responsable.getName();
                
                break;
            case 3:
                detalle =  puntosGanados + " puntos para "+ equipoGanador.getName()+" por envido, envido, envido de " + responsable.getName();
                break;
            case 4:    
                detalle =  puntosGanados + " puntos para "+ equipoGanador.getName()+" por real envido de " + responsable.getName();
                break;
            case 5:
                detalle =  puntosGanados + " puntos para "+ equipoGanador.getName()+" por envido, envido, real envido de " + responsable.getName();
                break;
            case 6:
                detalle =  puntosGanados + " puntos para " + equipoGanador.getName()+" por envido, real envido, real envido de "+responsable.getName();
                break;
            case 7:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, real envido, real envido de "+responsable.getName();
                break;
            case 8:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por falta envido de "+responsable.getName();
                break;
            case 9:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, real envido de "+responsable.getName();
                break;
            case 10:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, real envido de "+responsable.getName();
                break;
                
            case 11:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido no querido de "+responsable.getName();
                break;
            case 12:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido no querido de "+responsable.getName();
                break;
            case 13:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido, envido no querido de "+responsable.getName();
                break;
            case 14:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido no querido de "+responsable.getName();
                break;
            case 15:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido, real envido no querido de "+responsable.getName();
                break;
            case 16:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, real envido, real envido no querido de "+responsable.getName();
                break;
            case 17:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, envido, real envido no querido de "+responsable.getName();
                break;
            case 18:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por Falta envido no querido de "+responsable.getName();
                break;
            case 19:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, real envido no querido de "+responsable.getName();
                break;
            case 20:   
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, real envido no querido de "+responsable.getName();
                break;
            case 21:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido no mostrado de "+responsable.getName();
                break;
            case 22:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido no mostrado "+responsable.getName();
                break;
            case 23:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido, envido, no mostrado de "+responsable.getName();
                break;
            case 24:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido no mostrado de "+responsable.getName();
                break;
                
            case 25:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, envido, real envido no mostrado de "+responsable.getName();
                break;
            case 26:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, real envido, real envido no mostrado de "+responsable.getName();
                break;
            case 27:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, real envido, real envido no mostrado de "+responsable.getName();
                break;
            case 28:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por Falta envido, no mostrado de "+responsable.getName();
                break;
            case 29:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por envido, real envido, no mostrado de "+responsable.getName();
                break;
            case 30:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por real envido, real envido, no mostrado de "+responsable.getName();
                break;
            case 31:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por flor de "+responsable.getName();
                break;
            case 32:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por flor no mostrada de "+responsable.getName();
                break;
            case 33:    
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por flor no cantada de "+responsable.getName();
                break;
            case 50:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por que se retiraron sin que dejar que canten algo";
                break;
            case 51:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por que no se canto nada";
                break;
            case 52:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por truco no querido";
                break;
            case 53:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por truco";
                break;
            case 54:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por retruco no querido";
                break;
            case 55:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por retruco";
                break;
            case 56:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por vale cuatro no querido";
                break;
            case 57:
                detalle =  puntosGanados+" puntos para "+ equipoGanador.getName()+" por vale cuatro. Pie";
                break;
            default:
                detalle = "desconocido por"+ganadosPor;
        }
        return detalle;
    }
    
}
