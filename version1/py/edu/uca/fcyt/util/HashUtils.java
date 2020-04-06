/*
 * HashUtils.java
 *
 * Created on 10 de junio de 2003, 03:36 PM
 */

package py.edu.uca.fcyt.util;

import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author  PABLO JAVIER
 */
public class HashUtils {
    
    public static void imprimirHash(Hashtable hash) {
        Iterator it = hash.keySet().iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }
}
