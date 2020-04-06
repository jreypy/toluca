package py.edu.uca.fcyt.toluca.net;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import py.edu.uca.fcyt.net.XmlPackagesServer;
import py.edu.uca.fcyt.net.XmlPackagesServerListener;
import py.edu.uca.fcyt.net.XmlPackagesSession;
import py.edu.uca.fcyt.toluca.RoomServer;

/**
 * @author CYT UCA
 *
 * 
 */
public class ConnectionManager implements XmlPackagesServerListener {
	static Logger logger = Logger.getLogger(ConnectionManager.class);
    XmlPackagesServer server;
    /** Puerto donde escucha el conn mannager */
    public static int SERVER_PORT = 6767;
    /** Fully qualified name de la clase*/
    public static final String SERVER_FQN = "py.edu.uca.fcyt.toluca.net.CommunicatorServer";
    
    /** Nombre de la propiedad para el Puerto donde escucha el conn mannager */
    public static String PROP_SERVER_PORT = "integer.server_port";
    /** Nombre de la propiedad para el Fully qualified name de la clase*/
    public static final String PROP_SERVER_FQN = "string.server_fqn";
    
    /** Holds value of property roomserver. */
    private RoomServer roomServer;
    
    /**Las sesesiones de XmlPackageSession*/
    private ArrayList vecSesiones;
    
    
    public ConnectionManager(RoomServer rs) {
        try {
            logger.debug("Soy un Connection mannager y ya fui instanciado");
            roomServer=rs;
            vecSesiones = new ArrayList();
            int port = Integer.parseInt(rs.getProperties().getProperty(PROP_SERVER_PORT));
            server = new XmlPackagesServer( port, rs.getProperties().getProperty(PROP_SERVER_FQN));
            server.addXmlPackagesServerListener(this);
            new Thread(server, "connection-manager").start();
            
        } catch (java.io.IOException e) {
            logger.error("I/O exception in server");
            e.printStackTrace(System.out);
        } catch (java.lang.ClassNotFoundException e) {
            logger.error("Class not found in server: " + e.getMessage());
            e.printStackTrace(System.out);
        }
        
    }
    
    public static void main(String[] args) {

    }
    
    public void SessionStarted(XmlPackagesSession xps) {
        //logger.info("Se creo una sesi�on");
        vecSesiones.add(xps);
        CommunicatorServer cts = (CommunicatorServer)xps;
        cts.setRoomServer(roomServer);
    }
    
    
   
    public ArrayList getVecSesiones() {
        return this.vecSesiones;
    }
    
     public void setVecSesiones(ArrayList vecSesiones) {
        this.vecSesiones = vecSesiones;
    }
     public boolean removeCommunicator(CommunicatorServer comm)
     {
     	
     	logger.info("Se elimina un communicator "+comm);
     	return vecSesiones.remove(comm);
     	
     	
     }

}

