package com.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

 
        /**
 * @类型名称：JavaScriptUtil
 * @说明：	js 操作工具类
 * @创建者: 万志军  
 * @创建时间: 2020年1月7日 下午3:32:46
 * @修改者: 万志军  
 * @修改时间: 2020年1月7日 下午3:32:46 
        */  
    
public class JavaScriptUtil {

	
	private static ScriptEngine se;
	private static Logger log = Logger.getLogger(JavaScriptUtil.class.getName());
	
	//初始化javascript引擎
	static {
		se = new ScriptEngineManager().getEngineByName("javascript");
		// des的文件位置
		InputStream is =  JavaScriptUtil.class.getResourceAsStream("/des.js");
		try {
			se.eval(new InputStreamReader(is));
		} catch (ScriptException e) {
			log.error("---javascript引擎   初始化异常-----", e.getCause());
		}
		
	}
	        /**
	 * @方法名称: encrypt
	 * @说明:  默认des.js（class路径）文件，传送待处理数据Map
	 * @参数 @param param
	 * @参数 @return
	 * @参数 @throws FileNotFoundException
	 * @参数 @throws ScriptException
	 * @参数 @throws IOException
	 * @参数 @throws NoSuchMethodException
	 * @返回类型 String    
	 * @创建者: 万志军  
	 * @创建时间: 2020年1月7日 下午3:33:33
	 * @修改者: 万志军  
	 * @修改时间: 2020年1月7日 下午3:33:33        
	        */   
	    
	public static String encrypt(Map param) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
		String encryResult = "";
		if (se instanceof Invocable) {
			Invocable invocable = (Invocable) se;
			String jsonParams = JSON.toJSONString(param);
			encryResult = (String) invocable.invokeFunction("encrypt", jsonParams, "888888");
		}
		return encryResult;
	}
	
	 
	        /**
	 * @方法名称: 数据处理
	 * @说明:  传送待处理数据Map和JS文件地址（class路径，如class的des.js文件，入参传送des.js）
	 * @参数 @param param
	 * @参数 @param jsAddr
	 * @参数 @return
	 * @参数 @throws FileNotFoundException
	 * @参数 @throws ScriptException
	 * @参数 @throws IOException
	 * @参数 @throws NoSuchMethodException
	 * @返回类型 String    
	 * @创建者: 万志军  
	 * @创建时间: 2020年1月7日 下午3:36:33
	 * @修改者: 万志军  
	 * @修改时间: 2020年1月7日 下午3:36:33        
	        */   
	    
	public static String encrypt(Map param,String jsAddr) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
		String encryResult = "";
		InputStream is =  JavaScriptUtil.class.getResourceAsStream(new StringBuilder("/").append(jsAddr).toString());
		se.eval(new InputStreamReader(is));
		if (se instanceof Invocable) {
			Invocable invocable = (Invocable) se;
			String jsonParams = JSON.toJSONString(param);
			encryResult = (String) invocable.invokeFunction("encrypt", jsonParams, "888888");
			System.out.println(encryResult);
		}
		return encryResult;
	}
	
	
	
	public static String execFunc(String funcionName,String jsAddr,Object... param) throws FileNotFoundException, ScriptException, IOException, NoSuchMethodException {
		String execResult = "";
		/*org.springframework.core.io.Resource desJs2 = new ClassPathResource(jsAddr);
		se.eval(new FileReader(desJs2.getFile()));*/
		InputStream is =  JavaScriptUtil.class.getResourceAsStream(new StringBuilder("/").append(jsAddr).toString());
		se.eval(new InputStreamReader(is));
		if (se instanceof Invocable) {
			Invocable invocable = (Invocable) se;
			execResult = (String) invocable.invokeFunction(funcionName, param);
			System.out.println(execResult);
		}
		return execResult;
	}
	
	public static void main(String args[])
	{
		Map param = new HashMap();
		param.put("aa", "hello World");
		try {
			String result =encrypt(param);
			System.out.print(result);
		} catch (NoSuchMethodException | ScriptException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
