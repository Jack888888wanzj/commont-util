package com.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

 
	/**
	* @类型名称：DownLoadFile
	* @说明：文件下载工具类
	* @创建者: 万志军  
	* @创建时间: 2019年7月26日 上午10:21:31
	* @修改者: 万志军  
	* @修改时间: 2019年7月26日 上午10:21:31 
    */  
    
public class DownLoadFile{

	private static Logger logger = Logger.getLogger(DownLoadFile.class);
	
            /**
     * @方法名称: downLoadFromUrl
     * @说明: 	从网络Url中下载文件，需传新文件名，保存地址 
     * @参数 @param urlStr
     * @参数 @param fileName
     * @参数 @param savePath
     * @参数 @throws IOException
     * @返回类型 void    
     * @创建者: 万志军  
     * @创建时间: 2019年7月26日 上午10:21:51
     * @修改者: 万志军  
     * @修改时间: 2019年7月26日 上午10:21:51        
            */   
        
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();  
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        
        logger.info(getData.length+"");
        
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists() || !saveDir.isDirectory()){
            saveDir.mkdirs();
        }
        File file = new File(saveDir+File.separator+fileName);  
        logger.info(file.getPath());
        
        FileOutputStream fos = new FileOutputStream(file);     
        fos.write(getData); 
        
        if(fos!=null){
            fos.close();  
        }
        if(inputStream!=null){
            inputStream.close();
        }
        logger.info("文件下载info:"+url+" download success ！");

    }
    

    
     
            /**
     * @方法名称: downLoadFromUrl
     * @说明: 从网络Url中下载文件，
     * @参数 @param urlStr
     * @参数 @param savePath
     * @参数 @throws IOException
     * @返回类型 void    
     * @创建者: 万志军  
     * @创建时间: 2019年7月26日 下午2:45:07
     * @修改者: 万志军  
     * @修改时间: 2019年7月26日 下午2:45:07        
            */   
        
    public static void  downLoadFromUrl(String urlStr,String savePath) throws IOException{
        URL url = new URL(urlStr);  
       
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();  
        //获取自己数组
        byte[] getData = readInputStream(inputStream);    
        logger.info(getData.length+"");
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists() || !saveDir.isDirectory() ){
            saveDir.mkdirs();
        }
        //获取URL文件名
        String fileName = null;
        fileName =  FilenameUtils.getName(urlStr);
        
        if(fileName==null || fileName.isEmpty() ||(fileName !=null && fileName.indexOf("?")>-1))
        {//	根据时间戳和指定的文件类型，生成新文件名
//        	fileName = getFileName(GlobalsControl.getGlobals().getProperty("volice.downloadFileType"));
        	fileName = getFileName("mp3");
        }
        
        File file = new File(saveDir+File.separator+fileName);  
        
        logger.info(file.getPath());
        FileOutputStream fos = new FileOutputStream(file);     
        fos.write(getData); 
       
        if(fos!=null){
            fos.close();  
        }
        if(inputStream!=null){
            inputStream.close();
        }
        logger.info("文件下载info:"+url+" download success ！"); 

    }

            /**
     * @方法名称: readInputStream
     * @说明: 从输入流中获取字节数组
     * @参数 @param inputStream
     * @参数 @return
     * @参数 @throws IOException
     * @返回类型 byte[]    
     * @创建者: 万志军  
     * @创建时间: 2019年7月26日 上午10:26:33
     * @修改者: 万志军  
     * @修改时间: 2019年7月26日 上午10:26:33        
            */   
        
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    }  
    
     
            /**
     * @方法名称: getFileName
     * @说明: 	按时间戳生成指定格式的文件名
     * @参数 @param fileType
     * @参数 @return
     * @返回类型 String    
     * @创建者: 万志军  
     * @创建时间: 2019年7月26日 上午10:26:55
     * @修改者: 万志军  
     * @修改时间: 2019年7月26日 上午10:26:55        
            */   
        
    public static  String getFileName(String fileType) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	String timeStamp = simpleDateFormat.format(new Date());
    	timeStamp = timeStamp+"."+fileType;
    	return timeStamp;
    }
     
            /**
     * @方法名称: downLoadFromUrl2
     * @说明: 从网络Url中下载文件，
     * @参数 @param urlStr
     * @参数 @param savePath
     * @参数 @throws IOException
     * @返回类型 void    
     * @创建者: 万志军  
     * @创建时间: 2019年11月21日 下午1:56:27
     * @修改者: 万志军  
     * @修改时间: 2019年11月21日 下午1:56:27        
            */   
        
    public static void  downLoadFromUrl2(String urlStr,String savePath) throws IOException{
        URL url = new URL(urlStr);  
       
        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists() || !saveDir.isDirectory() ){
            saveDir.mkdirs();
        }
        //获取URL文件名
        String fileName = null;
        fileName =  FilenameUtils.getName(urlStr);
        
        if(fileName==null || fileName.isEmpty() ||(fileName !=null && fileName.indexOf("?")>-1))
        {//	根据时间戳和指定的文件类型，生成新文件名
//        	fileName = getFileName(GlobalsControl.getGlobals().getProperty("volice.downloadFileType"));
        	fileName = getFileName("mp3");
        	
        }
        
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        
        FileOutputStream fileOutputStream = new FileOutputStream(new StringBuilder(savePath).append("/").append(fileName).toString());
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        
       
        if(fileOutputStream!=null){
        	fileOutputStream.close();  
        }
        if(readableByteChannel!=null){
        	readableByteChannel.close();
        }
        logger.info("文件下载info:"+url+" download success ！"); 
    }
    
     
            /**
     * @方法名称: batchDownloadFromFile
     * @说明: 根据filePath中的文件地址，将文件解析，并下载所有文件
     * @参数 @param filePath
     * @参数 @param savePath
     * @返回类型 void    
     * @创建者: 万志军  
     * @创建时间: 2019年11月21日 下午1:50:01
     * @修改者: 万志军  
     * @修改时间: 2019年11月21日 下午1:50:01        
            */   
        
    public static void batchDownloadFromFile(String filePath,String savePath)
    {
    	if(StringUtils.isEmpty(filePath))
    	{
    		logger.error("请输入文件地址！");
    		return;
    	}
    	if(StringUtils.isEmpty(savePath))
    	{
//    		savePath=GlobalsControl.getGlobals().getProperty("volice.downloadPath");
    		logger.error("请配置文件存储地址！");
    		return;
    	}
    	try {
    		FileReader fr = new FileReader(filePath);
			List<String> fileAddrs = IOUtils.readLines(fr);
			int threadCount = 20;
			int perNum = fileAddrs.size()/threadCount;
			List<threadDownloadFile> threadPool = new ArrayList();
			long now = System.currentTimeMillis();
			for(int i=0;i<threadCount;i++)
			{
				int startIndex  = perNum*i;
				int endIndex = startIndex+perNum;
				if(i==threadCount-1)
					endIndex = fileAddrs.size();
				List<String> fileAddr = fileAddrs.subList(startIndex, endIndex);
				threadDownloadFile tdlf = new threadDownloadFile(fileAddr,savePath);
				threadPool.add(tdlf);
				tdlf.start();
			}
			while(true)
			{
				for(int i=0;i<threadPool.size();i++)
				{
					if(!threadPool.get(i).isAlive())
					{
						threadPool.remove(threadPool.get(i));
					}
				}
				if(threadPool.isEmpty() ||threadPool ==null)
					break;
			}
			long end = System.currentTimeMillis();
			System.out.println("耗时（秒）："+(end-now)/1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
     
            /**
     * @类型名称：threadDownloadFile
     * @说明：多线程执行下载任务
     * @创建者: 万志军  
     * @创建时间: 2019年11月21日 下午1:55:26
     * @修改者: 万志军  
     * @修改时间: 2019年11月21日 下午1:55:26 
            */  
        
    static class threadDownloadFile extends Thread  {
   	 
    	private List<String> fileAddrs;
    	private String savePath;
    	
    	threadDownloadFile(List<String> fileAddrs,String savePath) {
    		this.fileAddrs =fileAddrs;
    		this.savePath = savePath;
    	}
    	@Override
    	public void run() {
    		 for(String fileAddr:fileAddrs)
    		 {
    			 try {
					downLoadFromUrl2(fileAddr, savePath);
				} catch (IOException e) {
					logger.error("录音文件"+fileAddr+"下载异常："+e.getMessage());
				}
    		 }
    	}
    }
    
    
}

