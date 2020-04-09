
package py.edu.uca.fcyt.toluca.guinicio;


public class RowRanking {

	private String user;
	private Integer ranking;
	public static final Integer RANKING_GRIS=new Integer(0);
	public static final Integer RANKING_VERDE=new Integer(1);
	public static final Integer RANKING_AZUL=new Integer(2);
	public static final Integer RANKING_LILA=new Integer(3);
	public static final Integer RANKING_NARANJA=new Integer(4);
	public static final Integer RANKING_ROJO=new Integer(5);
	public RowRanking(String user,Integer ranking)
	{
		this.user=user;
		this.ranking=ranking;
		
	}
	/**
	 * @return Returns the ranking.
	 */
	public Integer getRanking() {
		return ranking;
	}
	/**
	 * @param ranking The ranking to set.
	 */
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	public Integer getRankingStatus()
	{
	    return getRankingStatus(ranking.intValue());
	}
	
	public static Integer getRankingStatus(int rank)
	{
		
		if(rank>1999)
			return RANKING_ROJO;
		else if(rank >1849)
		    return RANKING_LILA;
		else if(rank >1749)
		    return RANKING_NARANJA;			
		else if(rank> 1649)
		    return RANKING_VERDE;			
		else if(rank>1549 )
		    return RANKING_AZUL;
		else 
			return RANKING_GRIS;
		
	}
}
