package py.edu.uca.fcyt.toluca;

import py.edu.uca.fcyt.toluca.event.RoomEvent;

/** Java class "LoginFailedException.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */

/**
 * <p>
 * 
 * </p>
 */
public class LoginFailedException extends Exception {

    private RoomEvent revent = null;

    ///////////////////////////////////////
    // attributes

    public LoginFailedException() {
    }

    public LoginFailedException(String msg) {
        super(msg);
    }

    /**
     * @param event
     */
    public LoginFailedException(RoomEvent event) {
        super(event.getErrorMsg());
        setRevent(event);
    }

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int type;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    public static final int USER_DOES_NOT_EXISTS = 1;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    public static final int PASSWORD_DOES_NOT_MATCH = 2;

    ///////////////////////////////////////
    // operations

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * @return a int with ...
     *         </p>
     */
    public int getType() {
        return type;
    } // end getType

    /**
     * <p>
     * Does ...
     * </p>
     * <p>
     * 
     * </p>
     * <p>
     * 
     * @param _type
     *            ...
     *            </p>
     */
    public void setType(int _type) {
        type = _type;
    } // end setType

    /**
     * @return Returns the revent.
     */
    public RoomEvent getRevent() {
        return revent;
    }

    /**
     * @param revent
     *            The revent to set.
     */
    public void setRevent(RoomEvent revent) {
        this.revent = revent;
    }
} // end LoginFailedException

