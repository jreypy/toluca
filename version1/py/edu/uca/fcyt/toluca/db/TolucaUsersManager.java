/*
 * Created on 14-sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.db;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author jrey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TolucaUsersManager implements Runnable {

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    private static final long wait = 1000*60*10; 
    HashMap values;
    
    boolean live;
    public boolean isLive() {
        return live;
    }
    public void setLive(boolean live) {
        this.live = live;
    }
    public HashMap getValues() {
        return values;
    }
    public void setValues(HashMap values) {
        this.values = values;
    }
    public TolucaUsersManager(HashMap values){
        this.values = values;
        Thread tr = new Thread(this);
        live = true;
        tr.start();
    }
    public void run() {
        while (live){
            try {
                Thread.sleep((long) (1000*60*1.8));
            } catch (InterruptedException e) {
            }
            if (values != null){
                Iterator it = values.values().iterator();
                
                while (it.hasNext()){
                    TolucaUsers tu = (TolucaUsers) it.next();
                    Calendar c ;
                    if (tu.getLoginTime().getTimeInMillis()-Calendar.getInstance().getTimeInMillis()>=wait){
                        values.remove(tu.getKey());
                    }
                }
            }
        }
        
    }

}
