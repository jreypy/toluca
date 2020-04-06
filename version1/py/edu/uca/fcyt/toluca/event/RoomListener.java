package py.edu.uca.fcyt.toluca.event;

/** Java interface "RoomListener.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */

/**
 * <p>
 * 
 * @author Zuni
 * </p>
 */
public interface RoomListener extends SpaceListener {

  ///////////////////////////////////////
  // operations

/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * </p><p>
 * 
 * @param ev ...
 * </p>
 */
    public void tableCreated(RoomEvent ev);
/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * </p><p>
 * 
 * @param re ...
 * </p>
 */
    public void tableRemoved(RoomEvent re);
/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * </p><p>
 * 
 * @param ev ...
 * </p>
 */
    public void tableModified(RoomEvent ev);
/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * @param ev ...
 * </p><p>
 * 
 * </p>
 */
    public void tableJoined(RoomEvent ev);
/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * @param ev ...
 * </p><p>
 * 
 * </p>
 */
    public void createTableRequested(RoomEvent ev);
    
    public void tableJoinRequested(RoomEvent ev);

    public void loginCompleted(RoomEvent ev);
    
    public void loginRequested(RoomEvent ev);
    /** * <p> * Does ... * </p><p> *  * </p><p> *  * @param ev ... * </p> */    
    /** * <p> * Does ... * </p><p> *  * </p><p> *  * @param ev ... * </p> */    
    public void loginFailed(RoomEvent ev);
    
    /*
     * Nuevo, ver si esta correcto
     */
    public void playerLeft(RoomEvent ev);
    public void rankingChanged(RoomEvent ev);    
} // end RoomListener







