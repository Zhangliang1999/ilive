package com.jwzt.statistic.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jwzt.statistic.entity.FtpServer;
import com.jwzt.statistic.manager.FtpServerManager;
import com.jwzt.statistic.utils.ApplicationContextUtil;
import com.jwzt.statistic.utils.ConfigUtils;

/**
 * 定时处理文件
 * 
 * @author YanXL
 * 
 */
public class OnloadFileTask extends TimerTask {
	private static List<String> list;
	@Override
	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.info("====日志文件处理开始==============" + format.format(new Date()));
		ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
		FtpServerManager ftpServerManager = (FtpServerManager) applicationContext.getBean("ftpServerManager");
		List<FtpServer> ftpServers = ftpServerManager.selectFtpServerAll();
		if (ftpServers == null) {
			ftpServers = new ArrayList<FtpServer>();
		}
		// 本地文件路径
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy_MM/dd");
		String fileUrl = ConfigUtils.get("onload_ftp_log");
		if(fileUrl==null){
			log.info("==日志文件本地存储位置设置失败==="+fileUrl+"====使用默认地址工程部署根目录下logServers文件夹内部=======");
			fileUrl = loginFileUrl()+"logServers/";
		}
		for (FtpServer ftpServer : ftpServers) {
			fileUrl = fileUrl + "/" +ftpServer.getServerId()+"/"+ format2.format(new Date()) + "/";
			list = new ArrayList<String>();
			System.out.println("FTPIP: "+ftpServer.getFtpIp()+"	FTPPOST:"+ftpServer.getFtpPost());
			checkUserMsg(ftpServer,fileUrl);
		}
	}

	ExecutorService newFixedThreadPool = null;

	public OnloadFileTask() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}

	private static final Logger log = LoggerFactory.getLogger(OnloadFileTask.class);
	
	public String loginFileUrl(){
		//获取项目在tomcat部署的路径
	    String nodepath = this.getClass().getClassLoader().getResource("/").getPath();
	    String filePath = nodepath.substring(1, nodepath.length() - 17); 
	    filePath =  filePath.substring(0, filePath.indexOf("/")+1);
	    System.out.println("filePath=========================:"+filePath);
		return filePath;
	}
	private void checkUserMsg(final FtpServer server,final String fileUrl) {
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FTPClient ftpClient = new FTPClient();
					ftpClient.setDefaultPort(server.getFtpPost());
					ftpClient.connect(server.getFtpIp());
					ftpClient.setControlEncoding("GBK");//注意编码格式 
					boolean isLogin = ftpClient.login(server.getLoginName(),server.getLoginPassword());
					if (isLogin) {
						ftpClient.enterLocalPassiveMode();
						String ftpPath = server.getFtpPath();
						if(ftpPath==null){
							ftpPath="";
						}
						selectlist(ftpClient, ftpPath);
						if(list.isEmpty()){
							log.info("FTP: "+server.getFtpIp()+"=====文件为空====");
						}else{
							for (String url : list) {
								String fileName = url.substring(url.lastIndexOf("/")+1);
								log.info("==========开始下载文件========fileName："+fileName);
								OutputStream outputStream = null;
								try {
									File entryDir = new File(fileUrl);
									// 如果文件夹路径不存在，则创建文件夹
									if (!entryDir.exists()|| !entryDir.isDirectory()) {
										entryDir.mkdirs();
									}
									File locaFile = new File(fileUrl+ fileName);
									// 判断文件是否存在，存在则返回
									if (locaFile.exists()) {
										//删除ftp文件
										ftpClient.deleteFile(url);
										return;
									} else {
										outputStream = new FileOutputStream(locaFile);
										ftpClient.changeWorkingDirectory(url.replace(fileName, ""));
										boolean b = ftpClient.retrieveFile(new String(fileName.getBytes("GBK"), "iso-8859-1"),outputStream);
										if(b){
											log.info("==========下载成功========fileName："+fileName);
											//删除ftp文件
											ftpClient.deleteFile(url);
										}else{
											log.info("==========下载失败========fileName："+fileName);
										}
										outputStream.flush();
										outputStream.close();
									}
								} catch (Exception e) {
									e.printStackTrace();
									log.error("=====文件下载失败", e.toString());
								} finally {
									try {
										if (outputStream != null) {
											outputStream.close();
										}
									} catch (IOException e) {
										log.error("=====流关闭失败", e.toString());
									}
								}
							}
						}
					} else {
						log.info("==========ftp连接失败========");
						log.info(" IP: " + server.getFtpIp());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("ftp连接异常 ", e.toString());
				}
			}
		});
	}
	
	/** 
     * 递归遍历出目录下面所有文件 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束 
     * @throws IOException 
     */  
    public void selectlist(FTPClient ftp,String pathName) throws IOException{
    	if(pathName==""){
    		pathName = "/"+pathName;
    	}else{
    		if(!pathName.endsWith("/")){
        		pathName = pathName+"/";
        	}
            if(!pathName.startsWith("/")){  
            	pathName = "/"+pathName;
            }
    	}
    	
        Integer onload_day_hours = 3;
		try {
			onload_day_hours = Integer.valueOf(ConfigUtils.get("onload_day_hours"));
		} catch (Exception e) {
			System.out.println("=====================自定义定时错误采用默认时间见凌晨三点进行文件迁移=======================");
		}
        if(pathName.startsWith("/")&&pathName.endsWith("/")){  
	        String directory = pathName;  
	        //更换目录到当前目录  
	        FTPFile[] files = ftp.listFiles(directory);  
	        for(FTPFile file:files){  
	            if(file.isFile()){  
	            	Date time = file.getTimestamp().getTime();
	            	if((new Date().getTime() - time.getTime()) > (onload_day_hours*60*60*1000)){
	            		list.add(directory+file.getName());
	            	}else{
	            		System.out.println("文件时间不是昨天文件不予迁移：     "+file.getName());
	            	}
	            }else if(file.isDirectory()){  
	            	selectlist(ftp,directory+file.getName()+"/");  
	            }  
	        }  
        }else{
        	log.error("ftpPost 错误");
        }
    }
}
