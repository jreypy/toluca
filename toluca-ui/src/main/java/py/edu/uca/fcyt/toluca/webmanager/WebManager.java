/*
 * Created on 14-sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.webmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author jrey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WebManager implements Runnable{

    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    
        
    
    ServerSocket s;
    boolean live = false;
    public WebManager() throws NumberFormatException, IOException{
  //      s = new ServerSocket(Integer.parseInt(TolucaProperties.getProperty(TolucaProperties.WEBMANAGERPORT)));
        Thread t = new Thread(this);
        t.start();
    }
    public void setAlive(boolean live){
        this.live = live;
    }
    public boolean isAlive(){
        return this.live;
    }
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        live = true;
        while (live){
            try {
                socket = s.accept();
                
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String command;

            /*try {
                
                command = reader.readLine();
                
               try{
                    
                    String values[] = command.split(" ");
                    if (values[0].equals("LOGIN") && values.length==3){
                        try {
                            DbOperations db = RoomServer.getDBOperations();
                            String logueado = db.login(values[1],values[2]);
                            if (logueado != null)
                                writer.println(logueado);
                            else
                                writer.println("ERROR");
                            writer.flush();
                            
                        } catch (SQLException e2) {
                            e2.printStackTrace();
                            writer.println("ERROR");
                            writer.flush();
                        } catch (ClassNotFoundException e2) {
                            e2.printStackTrace();
                            writer.println("ERROR");
                            writer.flush();
                        }
                    }
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/
        }
        
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        /*String s = "Hola mundo cruel ";
        String[] m=  s.split(" ");
        for (int i=0; i<m.length; i++)
           System.out.println(m[i]);*/
        
        
        
    }
}
