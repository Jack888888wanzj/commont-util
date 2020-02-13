package com.util;

import java.security.MessageDigest;

 
        /**
 * @类型名称：Md5Util
 * @说明：md5数字加签方法
 * @创建�?: 万志�?  
 * @创建时间: 2019�?12�?13�? 下午5:21:42
 * @修改�?: 万志�?  
 * @修改时间: 2019�?12�?13�? 下午5:21:42 
        */  
    
public class Md5Util {

	public static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	
}
