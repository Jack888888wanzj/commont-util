package com.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class CommonUtils {
    private static Logger LOGGER = Logger.getLogger(CommonUtils.class);
            
	public static HashMap convertParamsToMap(Map<String,List<String>> params){
		HashMap rMap = new HashMap();
		Iterator<String> it = params.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<String> val = (List<String>)params.get(key);
			rMap.put(key, val.get(0));
		}
		return rMap;
	}
	
	public static String getDateMilis(String dateTime, String dateFormat){
		if(dateFormat==null) dateFormat="yyyy-MM-dd HH:mm:ss";
		try{
			Calendar c = Calendar.getInstance();		  
			c.setTime(new SimpleDateFormat(dateFormat).parse(dateTime));
			DecimalFormat df = new DecimalFormat("0");
			String r = df.format(c.getTimeInMillis());
			return r;
		}catch(Exception e){
		}
		return "";
	}
	
	public static String getDateMilis(Date dateTime){
		try{
			Calendar c = Calendar.getInstance();		  
			c.setTime(dateTime);
			DecimalFormat df = new DecimalFormat("0");
			String r = df.format(c.getTimeInMillis());
			return r;
		}catch(Exception e){
		}
		return "";
	}
	
	public static String getNowMilis(){
		try{
			Calendar c = Calendar.getInstance();		  
			c.setTime(new Date());
			DecimalFormat df = new DecimalFormat("0");
			String r = df.format(c.getTimeInMillis());
			return r;
		}catch(Exception e){
		}
		return "";
	}
	
	public static String getNowStr(String dateFormat){
		if(dateFormat==null) dateFormat="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpledateformat = new SimpleDateFormat(dateFormat);
        String s = simpledateformat.format(new Date());
        return s;
	}
	
	public static String getProperty(String propertyFileName, String propertyName){
		try{
			InputStream in = CommonUtils.class.getResourceAsStream(propertyFileName);
			Properties p = new Properties();
			p.load(in);
			String propertyValue = p.getProperty(propertyName);
			return propertyValue;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String formatDoubleString(String dbs, String format){
		if(dbs==null || "null".equals(dbs) || "".equals(dbs)) dbs="0.0000";
		Double db = Double.parseDouble(dbs);
		DecimalFormat df = new DecimalFormat(format);
		String r = df.format(db);
		return r;
	}

    public static Map<String, String> convertStringMap(Map params)
    {
        Map<String, String> rMap = new HashMap<String, String>();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext())
        {
            Object key = it.next();
            Object val = params.get(key);
            rMap.put(String.valueOf(key), String.valueOf(val));
        }
        return rMap;
    }
    
    public static String mapRString(Map<String,Object> map,String key){
    	String rString = "";
		try{
			if(map==null || key == null || "".equals(key)){
				rString = "";
			}else{
				if(map.get(key)!=null){
					rString = (String.valueOf(map.get(key))).trim();
				}else if(map.get(key.toUpperCase())!=null){
					rString = (String.valueOf(map.get(key.toUpperCase()))).trim();
				}else{
					rString = ""; 
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("mapRString failed map = " + map + "; key = " + key);
			rString = "";
		}
		return rString;
	}
    
    public static int mapRInt(Map<String,Object> map,String key){
		int rInt = 0;
		try{
			if(map==null || key == null || "".equals(key)){
				rInt = 0;
			}else{
				if(map.get(key)!=null){
					rInt = Integer.parseInt(String.valueOf(map.get(key)).trim());
				}else if(map.get(key.toUpperCase()) != null){
					rInt = Integer.parseInt(String.valueOf(map.get(key.toUpperCase())).trim());
				}else{
					rInt = 0;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("mapRInt failed map = " + map + "; key = " + key);
			rInt = 0;
		}
		return rInt;
	}
    
	public static boolean isNum(String str){
		try{
			if("".equals(str) || str ==null){
				return false;
			}
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			return pattern.matcher(str).matches(); 
		}catch(Exception e){
			return false;
		}
	}
	  public static int mapRInt(Map<String,Object> map,String key,int defaultValue){
			int rInt = 0;
			try{
				if(map==null || key == null || "".equals(key)){
					rInt = defaultValue;
				}else{
					if(map.get(key)!=null){
						rInt = Integer.parseInt(String.valueOf(map.get(key)).trim());
					}else if(map.get(key.toUpperCase()) != null){
						rInt = Integer.parseInt(String.valueOf(map.get(key.toUpperCase())).trim());
					}else{
						rInt = defaultValue;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error("mapRInt failed map = " + map + "; key = " + key);
				rInt = defaultValue;
			}
			return rInt;
		}
	  public static String mapRString(Map<String,Object> map,String key,String defaultValue){
	    	String rString = "";
			try{
				if(defaultValue == null)
					defaultValue = "";
				if(map==null || key == null || "".equals(key)){
					rString = defaultValue;
				}else{
					if(map.get(key)!=null){
						rString = (String.valueOf(map.get(key))).trim();
					}else if(map.get(key.toUpperCase())!=null){
						rString = (String.valueOf(map.get(key.toUpperCase()))).trim();
					}else{
						rString = defaultValue; 
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error("mapRString failed map = " + map + "; key = " + key);
				rString = defaultValue;
			}
			return rString;
		}
}
