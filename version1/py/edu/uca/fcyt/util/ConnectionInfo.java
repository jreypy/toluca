/*
 * Created on Mar 31, 2004
 *
 */
package py.edu.uca.fcyt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author PABLO JAVIER
 *
 */
public class ConnectionInfo {
	private String jdbc_driver;
	private String jdbc_url;
	private String jdbc_username;
	private String jdbc_password;
	
	/**
	 * 
	 */
	public ConnectionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConnectionInfo(String jdbc_driver, String jdbc_url, String jdbc_username, String jdbc_password) {
		setJdbc_driver(jdbc_driver);
		setJdbc_url(jdbc_url);
		setJdbc_username(jdbc_username);
		setJdbc_password(jdbc_password);
		
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(jdbc_driver);
		Connection conn = DriverManager.getConnection(jdbc_url, jdbc_username, jdbc_password);
		return conn;		
	}

	/**
	 * @return
	 */
	public String getJdbc_driver() {
		return jdbc_driver;
	}

	/**
	 * @return
	 */
	public String getJdbc_password() {
		return jdbc_password;
	}

	/**
	 * @return
	 */
	public String getJdbc_url() {
		return jdbc_url;
	}

	/**
	 * @return
	 */
	public String getJdbc_username() {
		return jdbc_username;
	}

	/**
	 * @param string
	 */
	public void setJdbc_driver(String string) {
		jdbc_driver = string;
	}

	/**
	 * @param string
	 */
	public void setJdbc_password(String string) {
		jdbc_password = string;
	}

	/**
	 * @param string
	 */
	public void setJdbc_url(String string) {
		jdbc_url = string;
	}

	/**
	 * @param string
	 */
	public void setJdbc_username(String string) {
		jdbc_username = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("<ConnectionInfo jdbcDriver=\"").append(jdbc_driver).append("\" ").
		append(" jdbcUrl=\"").append(jdbc_url).append("\" ").
		append(" jdbcUserName=\"").append(jdbc_username).append("\" ").
		append(" jdbcPassword=\"").append(jdbc_password).append("\"/>");
				
		return sbuf.toString();
	}


}
