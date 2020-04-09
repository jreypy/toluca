/*
 * Created on 14-sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.db;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Calendar;

import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

/**
 * @author jrey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TolucaUsers {
    TrucoPlayer trucoPlayer;
    Calendar loginTime;
    String key;
    public TolucaUsers(){
        loginTime = Calendar.getInstance();
    }
    public void generateRamdom(){
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte bytes[] = new byte[20];
            bytes = random.generateSeed(20);
            this.key = toHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
            	
    }
    public Calendar getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(Calendar loginTime) {
        this.loginTime = loginTime;
    }
    public TrucoPlayer getTrucoPlayer() {
        return trucoPlayer;
    }
    public void setTrucoPlayer(TrucoPlayer trucoPlayer) {
        this.trucoPlayer = trucoPlayer;
    }
	private static String toHexString(byte[] digest) {
		char[] values =
			{
				'0',
				'1',
				'2',
				'3',
				'4',
				'5',
				'6',
				'7',
				'8',
				'9',
				'a',
				'b',
				'c',
				'd',
				'e',
				'f' };
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			byte b = digest[i];
			hex.append(values[(b >> 4) & 0xf]);
			hex.append(values[b & 0x0f]);
		}
		return hex.toString();
	}
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
