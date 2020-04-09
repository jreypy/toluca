
package py.edu.uca.fcyt.toluca.net;

import py.edu.uca.fcyt.toluca.event.RoomEvent;

/**
 * @author Dani Cricco
 *
 *14-mar-2005
 */
public class ConexionTester implements Runnable{

    private Communicator comm;
    private boolean running=true;
    private long intervalo=1000;
    public ConexionTester(Communicator comm,long intervalo)
    {
        this.intervalo=intervalo;
        this.comm=comm;
    }
    public void run() {

        
        RoomEvent roomEvent=new RoomEvent();
        roomEvent.setType(RoomEvent.TYPE_TEST_CONEXION);
        while(isRunning())
        {
            
            roomEvent.setMsSend(System.currentTimeMillis());
            comm.sendXmlPackage(roomEvent);
            try {
                Thread.sleep(intervalo);
            } catch (InterruptedException e) {
                
            }
            
        }
        
    }
    
    
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
}
