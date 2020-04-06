/*
 * InvalidPlayExcepcion.java
 *
 * Created on 6 de marzo de 2003, 11:35 PM
 */

package py.edu.uca.fcyt.toluca.game;

/**
 *
 * @author  Julio Rey || Christian Benitez
 */
public class InvalidPlayExcepcion extends java.lang.RuntimeException {
    
    /**
     * Creates a new instance of <code>InvalidPlayExcepcion</code> without detail message.
     */
    public InvalidPlayExcepcion() {
    }
    
 
    /**
     * Constructs an instance of <code>InvalidPlayExcepcion</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidPlayExcepcion(String msg) {
        super(msg);
    }
}
