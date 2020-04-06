/*
 * Created on 27-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package py.edu.uca.fcyt.util.mail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import py.com.roshka.util.mail.Mailer;
import py.edu.uca.fcyt.util.ConnectionInfo;
import py.edu.uca.fcyt.util.ConnectionPool;
import py.edu.uca.fcyt.util.exception.InitializationExeption;
import py.edu.uca.fcyt.util.exception.InvalidPoolException;

/**
 * @author Alejandro
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TolucaBulkMail {

	private static ConnectionPool connPool;
	private static String dburl = "jdbc:firebirdsql:192.168.16.5/3050:/opt/interbase/data/TOLUCA.GDB";
	private static String username = "sysdba";
	private static String password = "asdf";
	private static String sql = "select jugador, email from jugadores";

	private static String texto = "Sres TRUQUEROS:\n\n" +
		"    Cumplimos en informarles que en el mes de Junio se realizará el primer torneo de truco en línea.  La modalidad va a ser de equipos de dos personas, habra importantes premios.\n\n"+
		"    Esperamos que vayan practicando en  http://www.truco.com.py, vamos a mantenerlos al tanto sobre las inscripciones.  El ránking de jugadores se encuentra en: http://www.truco.com.py:8180/toluca/ranking.do\n\n" +
		"    Saludos!\n\n soporte-truco";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidPoolException, IOException {
		ResultSet rs;
		PreparedStatement ps ;
		ConnectionInfo cinfo = new ConnectionInfo("org.firebirdsql.jdbc.FBDriver", dburl, username, password);
		try {
			connPool = new ConnectionPool(cinfo, 1);
			
			ps= connPool.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			
			String email;
		
			while (rs.next()) {
				email = rs.getString("email");
				try {
					Mailer.sendMail("mail.roshka.com.py", "elmacho@truco.com.py", email, "Torneo de truco - www.truco.com.py", texto);
					System.out.print(".");
				} catch (AddressException e1) {
					System.err.println(".");
				} catch (IOException e1) {
				} catch (MessagingException e1) {
				}
				
			}
    		rs.close();
    		ps.close();
    	} catch (InitializationExeption e) {
    		throw new SQLException("Invalid Pool" + e.getMessage());
    	}
	}
	

	
}
