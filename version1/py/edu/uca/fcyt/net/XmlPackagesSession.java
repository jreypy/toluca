/*
 * XmlPackagesSession.java
 *
 * Created on 3 de noviembre de 2002, 10:35
 */

package py.edu.uca.fcyt.net;

//import py.edu.uca.fcyt.toluca.XMLParser;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;









/**
 *
 * @author  psanta
 */
public abstract class XmlPackagesSession implements Runnable
{
	//se elimino de aca el log4j porque esta clase tambien se usa en el cliente
	//static Logger logger = Logger.getLogger(XmlPackagesSession.class);
	/** Constants */
	public static final int XML_PACKAGE_DELIMITER = '{';
	public static final int XML_PACKAGE_SESSION_INIT_OK = 1;
	
	
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean live;
	private boolean use_package_delimiter;
	
	/** Creates a new instance of XmlPackagesSession */
	public XmlPackagesSession()
	{
		socket = null;
		live = true;
		use_package_delimiter = true;
	}
	
	
	  
	
	public void run()
	{
		StringBuffer sBuff=new StringBuffer();
		
		//String rawPacket = "";
		// Loop until live = false
		
		//logger.debug("el descriptor de socket en el thread es:" + socket);
		try {
	
			while (live)
			{
				
				int c=0;
				
					
						
						
				
				c = in.read();
			//	logger.debug("llego un antes del if " +c );
				if(c==-1)
				{
					live=false;
					connectionFailed("Cayó bruscamente");
					
				}
						
					
				if (c != XML_PACKAGE_DELIMITER)
				{
					//rawPacket += (char)c;
					sBuff.append((char)c);
					
				} else
				{
				
					
						//String trimPacket=rawPacket.trim();
						//System.out.println("Se recibe el paquete \n"+trimPacket);
						//System.out.println("se recibe un \n"+sBuff.toString());
						
						try {
                            ByteArrayInputStream bais = new ByteArrayInputStream(
                                    sBuff.toString().getBytes());

                            //						StringReader sreader = new StringReader(sBuff.toString());
                            //						StringBufferInputStream inputStream=new StringBufferInputStream(sBuff.toString());
                            //System.out.println(sBuff.toString());
                            XMLDecoder d = new XMLDecoder(bais);
                            Object result = d.readObject();
                            //inputStream.close();
                            //logger.debug("Se resive un objeto");
                            /*System.out.println("\t\tEl objeto es: " + result);
                            System.out.println("\t\ttipo: "
                                    + result.getClass().getName());
                            TolucaConstants.dumpBeanToXML(result);*/
                            receiveObject(result); 
                        } catch (Exception e) {
                            System.err.println("Se fue a al carajo ........");
                            e.printStackTrace(System.err);
                        }		
						sBuff=new StringBuffer();
					//rawPacket = "";
				}

			

			//TODO no convendría un sleep chiquito? - 20050214		
			}
			
		} catch (IOException e) {
			live=false;
			connectionFailed(e.getMessage());
		}

			
		
	}
	
	public void sendXmlPackage(Object bean)
	{
		//logger.debug("enviando el paquete xml");
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		XMLEncoder e=new XMLEncoder(new BufferedOutputStream(outStream));
		e.writeObject(bean);
		e.close();
		
		sendXmlPackage(new String (outStream.toByteArray()));
		
	}
	
	public void sendXmlPackage(String xmlRaw)
	{
	//	System.out.println("I am going to send to the socket: \n" + xmlRaw);
		out.print(xmlRaw);
		if (use_package_delimiter)
			out.write(XML_PACKAGE_DELIMITER);
		
		out.flush();
	}
	
	
	
	
	
	
	
	
	/** Getter for property socket.
	 * @return Value of property socket.
	 */
	public java.net.Socket getSocket()
	{
		return socket;
	}
	
	/** Setter for property socket.
	 * @param socket New value of property socket.
	 */
	public void setSocket(java.net.Socket socket) throws IOException
	{
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		
	}
	
	public void close() throws IOException
	{
		//in.close();
		//out.close();
		socket.close();
		
	}
	public abstract void connectionFailed(String mensaje);
	public abstract void receiveObject(Object bean);
	public abstract int init() throws IOException;
	public abstract String getInitErrorMessage(int errcode);
   
}

