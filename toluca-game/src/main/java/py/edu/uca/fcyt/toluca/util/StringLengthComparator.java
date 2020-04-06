/* StringLengthComparator.java
 * Created on Jan 14, 2005
 *
 * Last modified: $Date: 2005/01/14 13:45:58 $
 * @version $Revision: 1.1 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca.util;

import java.util.Comparator;

/**
 * 
 * @author afeltes
 *
 */
public class StringLengthComparator implements Comparator {

    public int compare(Object o1, Object o2) {        
        String s1 = (String)o1;
        String s2 = (String)o2;
        return s1.length() - s2.length();
    }

}
