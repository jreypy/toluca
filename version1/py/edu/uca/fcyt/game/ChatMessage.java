package py.edu.uca.fcyt.game;

/** Java class "ChatMessage.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * <p>
 * 
 * </p>
 */
public class ChatMessage {
   public ChatMessage(TrucoPlayer player, String htmlMessage) {
	   setPlayer(player);
	   setHtmlMessage(htmlMessage);
       
   }
   public ChatMessage()
   {
   }
  ///////////////////////////////////////
  // attributes

   private String origin;

/**
 * @return Returns the origin.
 */
public String getOrigin() {
	return origin;
}
/**
 * @param origin The origin to set.
 */
public void setOrigin(String origin) {
	this.origin = origin;
}
/**
 * @return Returns the owner.
 */
public TrucoPlayer getOwner() {
	return owner;
}
/**
 * @param owner The owner to set.
 */
public void setOwner(TrucoPlayer owner) {
	this.owner = owner;
}
/**
 * <p>
 * Represents ...
 * </p>
 */
	private TrucoPlayer owner; 

/**
 * <p>
 * Represents ...
 * </p>
 */
	private String htmlMessage; 

  ///////////////////////////////////////
  // operations


/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * @return a String with ...
 * </p>
 */
	public String getHtmlMessage() {        /** lock-end */
		return htmlMessage;
	} // end getHtmlMessage        /** lock-begin */

/**
 * <p>
 * Does ...
 * </p><p>
 * 
 * </p><p>
 * 
 * @param _htmlMessage ...
 * </p>
 */
	public void setHtmlMessage(String _htmlMessage) {        /** lock-end */
		htmlMessage = _htmlMessage;
	}
    
	/** Getter for property owner.
	 * @return Value of property owner.
	 *
	 */
	public TrucoPlayer getPlayer() {
		return owner;
	}
    
	/** Setter for property owner.
	 * @param owner New value of property owner.
	 *
	 */
	public void setPlayer(TrucoPlayer owner) {
		this.owner = owner;
	}
    
 // end setHtmlMessage        /** lock-begin */

} // end ChatMessage





