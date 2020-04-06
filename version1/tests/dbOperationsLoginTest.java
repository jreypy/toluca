/*
 * Created on 06-ene-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tests;


import java.sql.SQLException;
import java.util.Properties;

import py.edu.uca.fcyt.toluca.LoginFailedException;
import py.edu.uca.fcyt.toluca.RoomServer;
import py.edu.uca.fcyt.toluca.db.DbOperations;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;

import junit.framework.TestCase;
/**
 * @author Alejandro Alliana
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class dbOperationsLoginTest extends TestCase {

	DbOperations dbo;
	
	public DbOperations getDBOperations() throws SQLException, ClassNotFoundException {
		if (dbo == null) {
			Properties p = new Properties();
			p.put(DbOperations.DBURL,
							"jdbc:firebirdsql:192.168.16.5/3050:/opt/interbase/data/TOLUCA.GDB");
			p.put(DbOperations.USER_NAME, "sysdba");
			p.put(DbOperations.PASSWORD, "asdf");

			dbo = new DbOperations(p, new RoomServer());
		}
		return dbo;
		
	}
	
	public void LoginOK() {
//		Properties p = new Properties();
//		p.put(DbOperations.DBURL, "jdbc:firebirdsql:192.168.16.5/3050:/opt/interbase/data/TOLUCA.GDB");
//		p.put(DbOperations.USER_NAME, "sysdba" );
//		p.put(DbOperations.PASSWORD, "asdf");
		
		try {
			DbOperations db = getDBOperations();
			
			TrucoPlayer tp = db.authenticatePlayer("cid", "cid");
			
			System.out.println(tp);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		public void LoginNOK() {
			Properties p = new Properties();
			p.put(DbOperations.DBURL, "jdbc:firebirdsql:192.168.16.5/3050:/opt/interbase/data/TOLUCA.GDB");
			p.put(DbOperations.USER_NAME, "sysdba" );
			p.put(DbOperations.PASSWORD, "asdf");
			
			try {
				DbOperations db = new DbOperations(p, new RoomServer());
				
				TrucoPlayer tp = db.authenticatePlayer("cid", "a");
				
				System.out.println(tp);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
    public void testNuevaPartida() throws SQLException, ClassNotFoundException {
		getDBOperations();
		TrucoTeam tt1 = new TrucoTeam();
		tt1.addPlayer(new TrucoPlayer("cid", 108));
		TrucoTeam tt2 = new TrucoTeam();
		tt2.addPlayer(new TrucoPlayer("jrey", 801));
		dbo.updateGameData(tt1	, tt2, 1 );
		
		
	}
	
}
