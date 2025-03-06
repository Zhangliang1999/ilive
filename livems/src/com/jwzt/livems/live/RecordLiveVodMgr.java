package com.jwzt.livems.live;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.livems.vod.RecordVodMgr;

public class RecordLiveVodMgr {
	
	public static Map<String,Integer> deleteLiveMountMap = new HashMap<String,Integer>();
	public static void main(String[] args) {
		System.out.println(getPrevDay(6));
	}
	/**
	 * 录制文件
	 * @param mountName 发布点名
	 * @param startTime 开始时间
	 * @param length 录制时长
	 * @param recordPath 录制目标文件地址 d:/vod/live211/20140101/100/
	 * @return
	 */
	public String  recordLive(String mountName , Timestamp startTime , long length){
		Logger.log(Logger.getCurTime2()+ " " +mountName+" 收到回看任务",3);
		String ret = "";
		Integer fileLiveDay =6;
		String VodFileLiveDays = SomsConfigInfo.get("VodFileLiveDays");
		if(VodFileLiveDays!=null&&!VodFileLiveDays.equals("")){
			try {
				fileLiveDay = Integer.parseInt(VodFileLiveDays);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Integer prevDayInteger = getPrevDay(fileLiveDay);
		String liveDir = SomsConfigInfo.get("liveDir")+"/"+mountName;
		File liveMountFilePath = new File(liveDir);
		if(liveMountFilePath.exists()){
			File[] list = liveMountFilePath.listFiles();
			for(File dir : list){
				if(dir.isDirectory()){
					String format = "yyyyMMdd";
					
					String sIndexFile =dir.getPath()+"/";
					//节目开始日期的index文件
					String startTimeIndexFile = sIndexFile+com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, startTime);
					startTimeIndexFile += "_index.txt";
					
					//节目结束日期的index文件
					String endTimeIndexFile = sIndexFile+com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, new Timestamp(startTime.getTime()+length*1000));
					endTimeIndexFile += "_index.txt";
					
					File startIndexFile = new File(startTimeIndexFile);
					File endIndexFile = new File(endTimeIndexFile);
					if(startIndexFile.exists()){
						/*
						long lastModifyTime = startIndexFile.lastModified();
						//如果该文件有30分钟没有更新 那么就跳过
						if(   (System.currentTimeMillis()-lastModifyTime)/1000>60*30){
							continue;
						}
						*/
						
						Timestamp endTime = getEndTime(startTime , length);
						String timeformat = "HHmmss";
						String datetimeformat = "yyyyMMddHHmmss";
						String sStartTime = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(timeformat, startTime);
						String startDateTime = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(datetimeformat, startTime);
						String sEndTime = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(timeformat, endTime);
						//String path = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, startTime) +"/" + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime) + length +".mp4"+ "/" + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime)+ "_" + length+dir.getName() + "_" +".mp4";
						try{
							if(endIndexFile.exists() && startIndexFile.exists())
								record(startIndexFile ,endIndexFile, dir.getPath() , sStartTime , sEndTime,startDateTime,length , startTime);
							ret += dir.getName()+",";
						}catch(Exception e){
							e.printStackTrace();
							Logger.log(e.getMessage(), 3);
						}
						
					}
					
				}
			}
			TheardOut theardOut = new TheardOut(mountName, prevDayInteger);
			theardOut.start();
		}

		return ret;
	}
	/**
	 * 为了提高效率采用执行线程
	 * @author gstars
	 *
	 */
	private class TheardOut extends Thread {

		private String mountName ;
		private Integer prevDayInteger ;
		public TheardOut(String mountName, Integer prevDayInteger){
			this.mountName = mountName;
			this.prevDayInteger = prevDayInteger;
		}
		
		public void run()
	    {
			try{
				//删除几天前的数据
				if(null==deleteLiveMountMap.get(mountName)||deleteLiveMountMap.get(mountName)<prevDayInteger){
					Logger.log(Logger.getCurTime2()+"   deleteLiveMountMap.get("+mountName+")="+deleteLiveMountMap.get(mountName),3);
					deleteLiveMountMap.put(mountName,prevDayInteger);
					deleteLiveVodFile(mountName,prevDayInteger);
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	}
	/**
	 * 录制文件
	 * @param fIndexFile D:\live211\500K\20141126_index.txt
	 * @param sLiveMounthPath  D:\live211\500K
	 * @param startDateTime 
	 * @param lStartTime 开始时间 小时分钟秒
	 * @param lEndTime 结束时间 小时分钟秒
	 * @param sDesFile 目标录制文件地址 d:/vod/live211/20140101/100/live211_500K.mp4
	 * @return
	 * @throws IOException 
	 */
	private boolean record(File startIndexFile ,File endIndexFile,String sLiveMounthPath , String sStartTime , String sEndTime, String startDateTime,long length , Timestamp startTime) throws IOException{
		boolean ret = false;
		int starttime = Integer.parseInt(sStartTime);
		int endtime = Integer.parseInt(sEndTime);
		//需要输出的文件列表
		List<String> recordFileList = new ArrayList();
		FileInputStream fileInputStream = new FileInputStream(startIndexFile);
		InputStreamReader read = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        FileInputStream endfileInputStream = new FileInputStream(endIndexFile);
        InputStreamReader endread = new InputStreamReader(endfileInputStream);
        BufferedReader endbufferedReader = new BufferedReader(endread);
        String endlineTxt = null;
        if(starttime<endtime){
        	 try{
     	        while((lineTxt = bufferedReader.readLine()) != null){
     	        	 
     	             String[] message = lineTxt.split("[|]");
     	             String[] datetime = message[0].split("_");
     	             int time = Integer.parseInt(datetime[1]);
     	             if(time >= starttime && time <endtime){
     	            	 recordFileList.add(lineTxt);
     	             }
     	        }
             }catch(Exception e){
     			e.printStackTrace();
     			Logger.log(e.getMessage(), 3);
     		}
        }else{
        	try{
        		//跨天的节目 找到开始日期的index文件  把所有时间大于开始时间的ts加入到列表中
     	        while((lineTxt = bufferedReader.readLine()) != null){
    	             String[] message = lineTxt.split("[|]");
    	             String[] datetime = message[0].split("_");
    	             int time = Integer.parseInt(datetime[1]);
    	             if(time >= starttime){ 
    	            	 recordFileList.add(lineTxt);
    	             }
    	        }
     	     //跨天的节目 找到结束日期的index文件  把所有时间小于结束时间的ts加入到列表中
     	        while((endlineTxt = endbufferedReader.readLine()) != null){
     	        	 
     	             String[] message = endlineTxt.split("[|]");
     	             String[] datetime = message[0].split("_");
     	             int time = Integer.parseInt(datetime[1]);
     	             if(time <endtime){
     	            	 recordFileList.add(endlineTxt);
     	             }
     	        }
             }catch(Exception e){
     			e.printStackTrace();
     			Logger.log(e.getMessage(), 3);
     		}
        }
       
        read.close();
        endbufferedReader.close();
        creatVodLiveM3U8File(recordFileList,sLiveMounthPath,startDateTime,length);
        
        //为文件截图
        if(recordFileList != null && recordFileList.size()>0){
        	String filePath = recordFileList.get(0);
        	File piceFile = new File(sLiveMounthPath + filePath);
        	System.out.println("piceFile:"+piceFile);
    		String datetimeformat = "yyyyMMddHHmmss";
    		String picFileName = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(datetimeformat, startTime);
    		picFileName = picFileName + ".jpg";
    		UUID uuid = UUID.randomUUID();
    		File uuidFile = new File(SomsConfigInfo.getHomePath()+ "/tempTask/"+uuid.toString()+".txt");
    		uuidFile.getParentFile().mkdirs();
			uuidFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile));
			String buffer ="<SRCFILE>"+sLiveMounthPath + filePath+"</SRCFILE>\r\n";
			buffer+="<GrabPos>1</GrabPos>\r\n";
			buffer+="<GrabPosType>0</GrabPosType>\r\n";
			buffer+="<GrabNUM>1</GrabNUM>\r\n";
			buffer+="<IntervalSecond>1</IntervalSecond>\r\n";
			buffer="<DESTFILE>"+sLiveMounthPath+"/pic/"+picFileName+"</DESTFILE>\r\n";
			writer.write(buffer); 
			RecordVodMgr recordVodMgr = new RecordVodMgr();
			boolean bet =  recordVodMgr.BVCutPic(uuidFile.getPath());

        }
        
		return ret;
	}
	
	/**
	 * 创建录制文件
	 * @param recordFileList 切片文件集合
	 * @param sStartTime 
	 * @param startDateTime 
	 * @param length 
	 * @param startOffSet 	 第一片开始时间
	 * @param endOffSet      最后一片结束时间
	 * @param desFile		 生成的目标mp4文件
	 * @return
	 */
	private boolean creatVodLiveM3U8File(List<String> recordFileList ,String  sLiveMounthPath, String startDateTime, long length){
		//System.out.println("startDateTime==="+startDateTime);
		//System.out.println("sLiveMounthPath==="+sLiveMounthPath);
		String channelDir = sLiveMounthPath.substring(0,sLiveMounthPath.lastIndexOf("\\"));
		String bandDir = sLiveMounthPath.substring(sLiveMounthPath.lastIndexOf("\\")+1);
		String bandWidth = bandDir.replaceAll("k", "").replaceAll("K", "");
		String channelM3U8FileName = channelDir+"\\"+startDateTime+length+".m3u8";
		String programM3U8FileName = sLiveMounthPath+"\\"+startDateTime+length+".m3u8";
		String bandM3U8FileName = bandDir+"/"+startDateTime+length+".m3u8";
		File m3u8File = new File(programM3U8FileName);
		File channelm3u8File = new File(channelM3U8FileName);
		boolean ret = false ;
		try {
			m3u8File.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(m3u8File));
			StringBuffer sb = new StringBuffer();
			sb.append("#EXTM3U");
			sb.append("\r\n");
			sb.append("#EXT-X-VERSION:3");
			sb.append("\r\n");
			sb.append("#EXT-X-ALLOW-CACHE:YES");
			sb.append("\r\n");
			sb.append("#EXT-X-TARGETDURATION:13");
			sb.append("\r\n");
			sb.append("#EXT-X-PLAYLIST-TYPE:VOD");
			sb.append("\r\n");
			sb.append("#EXT-X-MEDIA-SEQUENCE:1");
			sb.append("\r\n");
			for(int i=0 ; i<recordFileList.size() ; i++)
			{
					String lineTxt = recordFileList.get(i);
					String[] message = lineTxt.split("[|]");
					 String timelong = message[1];
					 String tsPath = message[2];
					 tsPath = tsPath.substring(0, tsPath.lastIndexOf(".")+1)+"ts";
					 System.out.println("tsPath="+tsPath);
					 sb.append("#EXTINF:"+timelong+",");
					 sb.append("\r\n");
					 sb.append(tsPath);
					 sb.append("\r\n");
			}
			sb.append("#EXT-X-ENDLIST");
			writer.write(sb.toString());
			writer.close();
			String M3UHead = "#EXTM3U";
			String streamM3 = "#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH="+Integer.parseInt(bandWidth)*1000;
			String addM3U8="";
			if(!channelm3u8File.exists()){
				try {
					channelm3u8File.createNewFile();
					addM3U8+=M3UHead+"\r\n";
					addM3U8+=streamM3+"\r\n";
					addM3U8+=bandM3U8FileName+"\r\n";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				addM3U8+=streamM3+"\r\n";
				addM3U8+=bandM3U8FileName+"\r\n";
			}
			try {
				FileOutputStream out = new FileOutputStream(channelm3u8File, true);
				out.write(addM3U8.getBytes("utf-8"));
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<Integer> bandWidthList = new ArrayList<Integer>();
			try {
				FileInputStream fins = new FileInputStream(channelm3u8File);
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(fins ));
				String dd = "";
				while((dd = reader2.readLine()) != null)
				{
					if(dd.indexOf("BANDWIDTH=")!=-1){
						Integer bandw =Integer.parseInt( dd.substring(dd.lastIndexOf("=")+1));
						if(!bandWidthList.contains(bandw)){
							bandWidthList.add(bandw);
						}
					}
				}
				reader2.close();
				fins.close();
				Collections.sort(bandWidthList); //排序
				
				//清空M3U8文件
				FileWriter fw = new FileWriter(channelm3u8File);
				fw.write("");
				fw.close();
				
				//按降序 重新写入M3U8文件
				FileOutputStream outF = new FileOutputStream(channelm3u8File, true);
				String M3U8Sorted = "#EXTM3U\r\n";
				for(int i=bandWidthList.size()-1;i>=0;i--){
					int bandwidth = bandWidthList.get(i);
					M3U8Sorted+="#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH="+bandwidth+"\r\n";
					String bandDir2 = bandwidth/1000+"k";
					M3U8Sorted+= bandDir2+"/"+startDateTime+length+".m3u8\r\n";
				}
				outF.write(M3U8Sorted.getBytes("utf-8"));
				outF.flush();
				outF.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret ;
	}
	
	public boolean  RunBVAppend(String commandTxt){
		boolean ret = false;
		System.out.println(commandTxt);		
		Process process = null;
		BufferedReader reader = null;
		BufferedReader errorreader = null;
		try
		{
			Properties props=System.getProperties(); //获得系统属性集   
			String osName = props.getProperty("os.name"); //操作系统名称 
			if(osName.toLowerCase().indexOf("windows")>=0)
			{
				String command = SomsConfigInfo.getHomePath()+"/BVAppend/BVAppend2014.exe "+commandTxt;
				//String command = SomsConfigInfo.getHomePath()+"/BVAppend/start.bat "+commandTxt;
				//String command = "ping 192.168.1.31";
				process = Runtime.getRuntime().exec(command);
				errorreader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while((line = errorreader.readLine()) != null)
				{
					System.out.println(line);
				}
				//process.waitFor();
				while((line = reader.readLine()) != null)
				{
					System.out.println(line);
				}
			}
			else
			{
					

				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();
				if(errorreader != null)
					errorreader.close();
				process.waitFor();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		System.out.println("END:"+ret);
		return ret;
	}
	
	
	/**
	 * 获得某个开始时间的结束时间
	 * @param startTime 开始时间
	 * @param length 时长 秒
	 * @return
	 */
	private Timestamp getEndTime(Timestamp startTime , long length){
		Timestamp ret = startTime;
		
		ret = new Timestamp(startTime.getTime()+(length*1000));
		
		return ret;
	}
	//获取当前日期前几天的日期 年月日的整形数值
	public static Integer getPrevDay(int prev){
				if(prev<=0){
					prev=1;
				}
			 Calendar cal=Calendar.getInstance();
				cal.add(Calendar.DATE,-prev);
			    Date curDate=cal.getTime();
				SimpleDateFormat ymdFormatter = new SimpleDateFormat("yyyyMMdd");
				String curDateStr = ymdFormatter.format(curDate);
				Integer curDateInt = Integer.parseInt(curDateStr);
			  return curDateInt;
	}
	/**
	 * 仅保留发布点下当前日期及之前固定天数的回看文件
	 * @param prevDayInteger 
	 * @return 
	 */
	public static void deleteLiveVodFile(String mountName, Integer prevDayInteger){
		String liveDir = SomsConfigInfo.get("liveDir")+"/"+mountName;
		Logger.log("删除发布点【"+liveDir+"】下 前"+prevDayInteger+"天之前的直播回看文件及直播切片！",3);
		File liveMountFilePath = new File(liveDir);
		if(liveMountFilePath.exists()){
			File[] list = liveMountFilePath.listFiles();
			for(File file : list){
				if(file.isDirectory()){
					for(File subdir : file.listFiles()){
						if(subdir.isDirectory()){
							deleteSingleVodFile(prevDayInteger,subdir);
						}
					}
				}else{
					deleteSingleVodFile(prevDayInteger,file);
				}
			}
		}
	}
	/**
	 * 判断 是否在保留日期 之前  做删除文件操作
	 * @param filename
	 * @param prevDayInteger
	 * @param file
	 */
	private static void deleteSingleVodFile(Integer prevDayInteger,File file) {
	    String filename = file.getName();
		if(filename.length()>=8){
			String fileHead = filename.substring(0, 8);
			if(fileHead.startsWith("2")){
				try {
					Integer fileint = Integer.parseInt(fileHead);
					if(fileint<prevDayInteger){
						boolean filedeleted = deleteFile(file);
						Logger.log("删除回看文件"+filename+" 结果："+filedeleted,3);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
				
		}
	}
	private static boolean deleteFile(File file){ 
	     try {
	    	 String liveDir = SomsConfigInfo.get("liveDir");
	    	 File livePath =new File(liveDir);
	    	 int mountNum = livePath.listFiles().length;
			Thread.sleep(1000/mountNum);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
		if(file.exists()){                    //判断文件是否存在
		    if(file.isFile()){                    //判断是否是文件
		     file.delete();                       //delete()方法 你应该知道 是删除的意思;

		    }else if(file.isDirectory()){              //否则如果它是一个目录
		     File files[] = file.listFiles();               //声明目录下所有的文件 files[];
		     for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
		      deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
		     }
		     file.delete();
		    } 
		   }else{ 
		    System.out.println("所删除的文件不存在！"+'\n'); 
		   }
		   return true; 
		} 
}
