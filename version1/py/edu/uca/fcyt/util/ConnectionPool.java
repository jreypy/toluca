/*
 * Created on Jul 1, 2004
 *
 */
package py.edu.uca.fcyt.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import py.edu.uca.fcyt.util.exception.InitializationExeption;
import py.edu.uca.fcyt.util.exception.InvalidPoolException;

/**
 * @author PABLO JAVIER
 *
 */
public class ConnectionPool {
	
	private static Logger logger = Logger.getLogger(ConnectionPool.class);
	
	private ConnectionInfo connInfo;
	private int numberOfConnections;
	private int nextConnection;
	private ArrayList connections;		

	public ConnectionPool(ConnectionInfo connInfo, int numberOfConnections)
	throws InitializationExeption, ClassNotFoundException, SQLException 
	{
		
		super();
		
		if (numberOfConnections <= 0) {
			throw new InitializationExeption("Number of connections " + numberOfConnections + "in ConnectionPool must be greater than 0");			
		}
		
		this.connInfo = connInfo;
		
		this.numberOfConnections = numberOfConnections;
		this.nextConnection = 0;
		
		connections = new ArrayList();
		
		int i;
		
		for (i = 0; i < numberOfConnections; i++) {
			connections.add(connInfo.getConnection());		
		}
		
		logger.info("Successfully set up of " + numberOfConnections + " database connections");
		
	}
	
	public synchronized Connection getConnection()
	throws ClassNotFoundException, SQLException, InvalidPoolException {
		
		if (connections == null)
			throw new InvalidPoolException("Pool has been stopped and is no longer valid. Create a new one");
		
		Connection ret = (Connection) connections.get(nextConnection);
		
		if (ret.isClosed()) {
			logger.warn("Connection is closed! Trying to replace it with a new one");
			ret = connInfo.getConnection();
			connections.add(nextConnection, ret);
			connections.remove(nextConnection+1);	// Remove old, disconnected one
		}
		
		nextConnection++;
		
		if (nextConnection == numberOfConnections) {
			nextConnection = 0;
		}
		
		return ret;
		
	}
	
	public synchronized void stop() throws SQLException {
		for (Iterator iter = connections.iterator(); iter.hasNext();) {
			Connection element = (Connection) iter.next();			
			element.close();
			iter.remove();
		}
		connections = null;
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuffer sbuf = new StringBuffer();
		
		sbuf.append("<ConnectionPool numberOfConnection=\"").append(getNumberOfConnections()).append("\" nextConnection=\"").append(getNextConnection()).append("\">").append(getConnInfo().toString()).append("</ConnectionPool>");
		
		return sbuf.toString();
		
	}

	/**
	 * @return
	 */
	public ConnectionInfo getConnInfo() {
		return connInfo;
	}

	/**
	 * @return
	 */
	public int getNextConnection() {
		return nextConnection;
	}

	/**
	 * @return
	 */
	public int getNumberOfConnections() {
		return numberOfConnections;
	}

	/**
	 * @param i
	 */
	public void setNumberOfConnections(int i) {
		numberOfConnections = i;
	}

}
