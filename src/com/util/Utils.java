/**
 * 
 */
package com.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public final class Utils {
	private static Logger LOGGER = Logger.getLogger(Utils.class);
	public static void waitMoment(long millis) {
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void waitMomentBySeconds(int seconds) {
	    try {
	        TimeUnit.SECONDS.sleep(seconds);;
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	public static Timestamp longTurnTimestamp(String str,SimpleDateFormat sd) {
		java.sql.Timestamp kh_time_answer = null;
		if(str != null && !"".equals(str)) {
			java.util.Date date = new java.util.Date(Long.parseLong(str));
			kh_time_answer= new java.sql.Timestamp(date.getTime());
		}
		return kh_time_answer;
	}
	
	public static Timestamp StringTurnTimestamp(String str,SimpleDateFormat sd) {
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.sql.Timestamp kh_time_answer = null;
		if(str != null && !"".equals(str)) {
			try {
				java.util.Date date = sd.parse(str);
				kh_time_answer= new java.sql.Timestamp(date.getTime());
			} catch (ParseException e) {
				LOGGER.error("时间格式转化错误： " +str);
				e.printStackTrace();
			}
		}
		return kh_time_answer;
	}
}
