/*
 * TrucoPlayer.java
 *
 * Created on 5 de marzo de 2003, 02:55 PM
 */

package py.edu.uca.fcyt.toluca.game;


 

/** Clase que representa a un jugador de Truco.
 * @author Julio Rey || Christian Benitez
 */
public class TrucoPlayer {

    protected String name = ""; 
    private String fullName = ""; 
    private String pass = ""; 
    private String email = ""; 
    protected int rating = -1; 
    protected int oldRating;
	
	/**
	 * @return Returns the oldRating.
	 */
	public int getOldRating() {
		return oldRating;
	}
	/**
	 * @param oldRating The oldRating to set.
	 */
	public void setOldRating(int oldRating) {
		this.oldRating = oldRating;
	}
  ///////////////////////////////////////
  // operations

    public String getName() { return name; } // end getName        
    public void setName(String _name) {        
        name = _name;
    } // end setName        

    public String getFullName() {        
        return fullName;
    } // end getFullName        

    public void setFullName(String _fullName) {        
        fullName = _fullName;
    } // end setFullName        

    public String getPass() {        
        return pass;
    } // end getPass        

    public void setPass(String _pass) {        
        pass = _pass;
    } // end setPass        

    public String getEmail() {        
        return email;
    } // end getEmail        

    public void setEmail(String _email) {        
        email = _email;
    } // end setEmail        
    
    public int getRating() { return rating; }
    
    public void setRating(int rating) 
    { 
    	this.rating = rating;
    }
    /** Constructor de un TrucoPlayer con su nombre identificador.
     * @param name String que se asignar� como nombre identificador del TrucoPlayer.
     */
    /*public TrucoPlayer(){
    }*/
    public TrucoPlayer (String name) {
        this();
        this.name = name;
    }
    /** Constructor de un TrucoPlayer.
     */    

    public TrucoPlayer (String name, int rating) {
        this(name);
        setRating(rating);
    }
    public TrucoPlayer()
    {
        }
    public String toString()
    {
    	return new String(getName()+ "("+getRating()+")");
    }
    
    /* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equalsO(Object obj) {
		boolean ret = false;		
		if(obj instanceof TrucoPlayer)
		{
			String este = getName() + " - " + getRating();
			TrucoPlayer tp = (TrucoPlayer) obj;
			String elOtro = tp.getName() + " - " + tp.getRating();
			ret = este.equals(elOtro);
		} ret = super.equals(obj);
		
		return ret;
	}
}
