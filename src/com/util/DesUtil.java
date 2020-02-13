package com.util;



import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;



//import com.hx.common2.util.lang.Base64;

//import com.hx.jwt.Base64;

//import org.bouncycastle.util.encoders.Base64;



/**
 * @类型名称：DesUtil
 * @说明：des 加解密工具类
 * @创建�?: 万志�?  
 * @创建时间: 2019�?12�?13�? 下午5:19:09
 * @修改�?: 万志�?  
 * @修改时间: 2019�?12�?13�? 下午5:19:09 
        */  
    
public class DesUtil {

	/* DES解密 */
	public static String decrypt(String message, String key) throws Exception {

	    byte[] bytesrc = Base64.getDecoder().decode(message);
	    //convertHexString(message);
	    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	    IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
	    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
	    byte[] retByte = cipher.doFinal(bytesrc);
	    return new String(retByte);
	}


	/* DES加密 */
	public static byte[] encrypt(String message, String key) throws Exception {
	    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	    IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	    return cipher.doFinal(message.getBytes("UTF-8"));
	}
	
}
