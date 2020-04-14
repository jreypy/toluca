/*
 * InvalidPlayExcepcion.java
 *
 * Created on 6 de marzo de 2003, 11:35 PM
 */

package py.edu.uca.fcyt.toluca.game;

/**
 * @author Julio Rey || Christian Benitez
 */
public class InvalidPlayExcepcion extends Exception {

    TrucoPlay trucoPlay;

    /**
     * Creates a new instance of <code>InvalidPlayExcepcion</code> without detail message.
     */
    public InvalidPlayExcepcion() {
    }


    /**
     * Constructs an instance of <code>InvalidPlayExcepcion</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidPlayExcepcion(String msg) {
        super(msg);
    }

    public InvalidPlayExcepcion(String message, TrucoPlay trucoPlay) {
        super(message);
        this.trucoPlay = trucoPlay;
    }

    public TrucoPlay getTrucoPlay() {
        return trucoPlay;
    }
}
