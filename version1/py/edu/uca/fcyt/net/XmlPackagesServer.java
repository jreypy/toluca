/*
 * XmlPackagesServer.java
 *
 * Created on 3 de noviembre de 2002, 8:23
 */

package py.edu.uca.fcyt.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import py.edu.uca.fcyt.toluca.net.CommunicatorServer;



/**
 *
 * @author  psanta
 */
public class XmlPackagesServer extends ServerSocket implements Runnable {
    
	static Logger logger = Logger.getLogger(XmlPackagesServer.class);
    private int clientsServed = 0;
    
    java.lang.Class theClass;
    
    public XmlPackagesServer(String className) throws IOException, ClassNotFoundException {
        super();
        
        theClass = Class.forName(className);
        XmlPackagesServerListenerVector = new Vector();
        
    }
    
    public XmlPackagesServer(int port, String className) throws IOException, ClassNotFoundException {
        super(port);
        theClass = Class.forName(className);
        XmlPackagesServerListenerVector = new Vector();
    }
    
    public XmlPackagesServer(int port, int backlog, String className) throws IOException, ClassNotFoundException {
        super(port, backlog);
        theClass = Class.forName(className);
        XmlPackagesServerListenerVector = new Vector();
    }
    
    public void run() {
        Socket socket;
        logger.info("Server started listening on port: " + getLocalPort());
        try {
            // Loops forever accepting connections
            while(true) {
                socket = accept();
                logger.info("Connection received");
                logger.info("el descriptor de socket es: " + socket);
                XmlPackagesSession xps = (XmlPackagesSession)theClass.newInstance();
                //socket.setSoTimeout(RoomServer.TIME_OUT);
                //socket.setSoTimeout(CommunicatorServer.SOCKET_TIMEOUT);
                xps.setSocket(socket);
                
                int retinit = xps.init();
                if (retinit == XmlPackagesSession.XML_PACKAGE_SESSION_INIT_OK) {
                    Thread xt = new Thread(xps, "xmlPackageSession");
                    xt.setPriority(Thread.MAX_PRIORITY);
                    xt.start();
                    //System.out.println("antes del sesion started");
                    fireSessionStarted(xps);
                } else {
                    logger.error("Cannot execute init method of " + theClass.getName() +  " successfully: " + xps.getInitErrorMessage(retinit));
                }
            }
        } catch (IOException e) {
            logger.error("I/O Exception in server");
            e.printStackTrace(System.out);
        } catch (InstantiationException e) {
            logger.error("Could not instantiate class: " + theClass.getName());
            e.printStackTrace(System.out);
        } catch (IllegalAccessException e) {
            logger.error("Illegal access exception in server");
            e.printStackTrace(System.out);
        }
        
    }
    
    /** Getter for property clientsServer.
     * @return Value of property clientsServer.
     */
    public int getClientsServed() {
        return clientsServed;
    }
    
    /** Setter for property clientsServer.
     * @param clientsServer New value of property clientsServer.
     */
    public void setClientsServed(int clientsServed) {
        this.clientsServed = clientsServed;
    }
    
    Vector XmlPackagesServerListenerVector;
    
    public void addXmlPackagesServerListener(XmlPackagesServerListener l) {
        XmlPackagesServerListenerVector.add(l);
    }
    
    private void fireSessionStarted(XmlPackagesSession xps) {
        Iterator iter = XmlPackagesServerListenerVector.listIterator();
        while(iter.hasNext()) {
            XmlPackagesServerListener ltmp = (XmlPackagesServerListener)iter.next();
            ltmp.SessionStarted(xps);
        }
    }
}

