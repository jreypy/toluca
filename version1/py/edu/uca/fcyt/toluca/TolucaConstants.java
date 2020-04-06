/* TolucaConstants.java
 * Created on Sep 22, 2004
 *
 * Last modified: $Date: 2005/05/06 05:34:19 $
 * @version $Revision: 1.7 $ 
 * @author afeltes
 */
package py.edu.uca.fcyt.toluca;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.util.Date;
import java.util.logging.Level;

/**
 * 
 * @author afeltes
 *  
 */
public class TolucaConstants {

    public static final Level CLIENT_DEBUG_LOG_LEVEL = Level.INFO;

    public static final Level CLIENT_INFO_LOG_LEVEL = Level.WARNING;

    public static final Level CLIENT_ERROR_LOG_LEVEL = Level.SEVERE;
    
    public static final Level DEFAULT_LOG_LEVEL = Level.WARNING;

    /**
     * @param result
     */
    public static void dumpBeanToXML(Object result) {
        System.out
                .println("/****************************************************");
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(System.out));
        e.writeObject(result);
        System.out
                .println("****************************************************/");
    }

    /**
     * @param value
     * @param string
     * @param string2
     * @return
     */
    public static String replaceString(String value, String string,
            String string2) {
        StringBuffer ret = new StringBuffer();
        String tmp = value;
        int i = -1;
        int nOfReplaces = 0;
        int charNotReplaced = 0;

        if ((i = tmp.indexOf(string)) != -1) {
            while (tmp.trim().length() > 0 && i >= 0) {
                for (int j = 0; j < i; j++) {
                    ret.append(value.toCharArray()[charNotReplaced
                            + nOfReplaces * string.length()]);
                    charNotReplaced++;
                }
                nOfReplaces++;
                ret.append(string2);
                tmp = value.substring(charNotReplaced + nOfReplaces
                        * string.length());
                i = tmp.indexOf(string);
            }
            ret.append(tmp);
        } else
            ret.append(string);
        return ret.toString();
    }

    public static void main(String[] args) {
        String ori = "aloooo \\n \\n pepe";
        String replaced = TolucaConstants.replaceString(ori, "p", "POP");
        System.out.println(replaced);
    }

    /**
     * @return
     */
    public static boolean isWindowFamily() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    }

    /**
     * @param client_debug_log_level2
     * @param string
     */
    public static void log(Level client_debug_log_level2, String string) {
        if (!client_debug_log_level2.equals(Level.OFF))
            System.out.println(new Date().toString() + " ["
                    + getCallingClassName() + "] "
                    + client_debug_log_level2.toString() + " "+ getCallingMethodName() + " - " + string);
    }

    public static synchronized String getCallingClassName() {
        java.lang.Throwable throwable = new java.lang.Throwable();
        java.lang.StackTraceElement[] stes = throwable.getStackTrace();
        //		for(int i=0; i<stes.length; i++)
        //			System.out.println("stes[" +i+ "],getClassName()" +
        // stes[i].getClassName());
        return stes[2].getClassName();
    }

    public static synchronized String getCallingMethodName() {
        java.lang.Throwable throwable = new java.lang.Throwable();
        java.lang.StackTraceElement[] stes = throwable.getStackTrace();
        //		for(int i=0; i<stes.length; i++)
        //			System.out.println("stes[" +i+ "],getClassName()" +
        // stes[i].getClassName());
        return stes[2].getMethodName();
    }

    /**
     * @param string
     */
    public static void log(String string) {
        log(DEFAULT_LOG_LEVEL, string);
    }

}