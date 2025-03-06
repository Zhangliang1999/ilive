package com.jwzt.livems.vod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.DB.soms.vod.file.FileMgr;
import com.jwzt.DB.soms.vod.image.file.ImageFileInfo;
import com.jwzt.DB.soms.vod.image.file.ImgPublicMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.RunExec;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.common.StringUtil;

public class RecordVodMgr {

	
	
	
	public FileAVInfo   BVGetAVFileInfo(String commandTxt,String desFilePath){
		RunExec runExec = new RunExec();
		FileAVInfo fileAVInfo =new FileAVInfo();
		BufferedReader reader = null;
		try
		{
			Properties props=System.getProperties(); //获得系统属性集   
			String osName = props.getProperty("os.name"); //操作系统名称 
			if(osName.toLowerCase().indexOf("windows")>=0)
			{
				String command =SomsConfigInfo. getHomePath()+"\\BVSTools\\BVGetAVFileInfo.exe  "+commandTxt;
				//String command = SomsConfigInfo.getHomePath()+"/BVAppend/start.bat "+commandTxt;
				//String command = "ping 192.168.1.31";
				boolean bet = runExec.RunBVAppend(command);
				if(bet){
						reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(desFilePath)) ));
						String dd = "";
						String desline = "";
						while((dd = reader.readLine()) != null)
						{
							desline+=dd;
						}
						reader.close();
						System.out.println("获取文件属性返回值 desline=="+desline);
						Logger.log(Logger.getCurTime2()+"  获取文件属性返回值 desline=="+desline,3);
						if(desline.equals("")){
							return null;
						}
						fileAVInfo.setAudioBitrate(StringUtil.getTagValue(desline, "AudioBitrate"));
						fileAVInfo.setAudioChannel(StringUtil.getTagValue(desline, "AudioChannel"));
						fileAVInfo.setAudioCodec(StringUtil.getTagValue(desline, "AudioCodec"));
						fileAVInfo.setAudioSamprate(StringUtil.getTagValue(desline, "AudioSamprate"));
						fileAVInfo.setFileDuration(StringUtil.getTagValue(desline, "FileDuration"));
						fileAVInfo.setFileSize(StringUtil.getTagValue(desline, "FileSize"));
						fileAVInfo.setFileType(StringUtil.getTagValue(desline, "FileType"));
						fileAVInfo.setVideoBitrate(StringUtil.getTagValue(desline, "VideoBitrate"));
						fileAVInfo.setVideoCodec(StringUtil.getTagValue(desline, "VideoCodec"));
						fileAVInfo.setVideoHeight(StringUtil.getTagValue(desline, "VideoHeight"));
						fileAVInfo.setVideoWidth(StringUtil.getTagValue(desline, "VideoWidth"));
						fileAVInfo.setFileFormat(StringUtil.getTagValue(desline, "FileFormat"));
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
	
		
		return fileAVInfo;
	}

	public FileAVInfo getAVFileInfo(String totalPath) {
		UUID uuid1 = UUID.randomUUID();
		File uuidFile1 = new File(SomsConfigInfo.getHomePath()+ "/tempTask/"+uuid1.toString()+".txt");
		UUID uuid2 = UUID.randomUUID();
		File uuidFile2 = new File(SomsConfigInfo.getHomePath()+"/tempTask/"+uuid2.toString()+".txt");
		
		try{    
			uuidFile1.getParentFile().mkdirs();
			uuidFile1.createNewFile();
			uuidFile2.getParentFile().mkdirs();
			uuidFile2.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile1));
		
			
			String buffer ="<SRCFILE>"+totalPath+"</SRCFILE>";
			writer.write(buffer+"\r\n"); 
			buffer="<DESTFILE>"+uuidFile2.getPath()+"</DESTFILE>";
			writer.write(buffer); 
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	return 	BVGetAVFileInfo(uuidFile1.getPath(),uuidFile2.getPath());
	
	}

	public boolean SegM3U8(String totalPath) {
		UUID uuid1 = UUID.randomUUID();
		File uuidFile1 = new File(SomsConfigInfo.getHomePath()+ "/tempTask/"+uuid1.toString()+".txt");
		try{    
			uuidFile1.getParentFile().mkdirs();
			uuidFile1.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile1));
			String buffer ="<SRCFILE>"+totalPath+"</SRCFILE>\r\n";
			buffer+="<SegTSVideoFile>1</SegTSVideoFile>\r\n";
			buffer+="<SegTSAudioFile>1</SegTSAudioFile>\r\n";
			
			if("0".equals(SomsConfigInfo.get("ifCUTFLV"))){
				buffer+="<SegFLVVideoFile>0</SegFLVVideoFile>\r\n";
			}else{
				buffer+="<SegFLVVideoFile>1</SegFLVVideoFile>\r\n";
			}

			writer.write(buffer); 
			String fileDir = totalPath.substring(0, totalPath.lastIndexOf("\\"));
			String fileName = totalPath.substring(totalPath.lastIndexOf("\\")+1, totalPath.length());
			String fileM3 = fileName.replaceAll("\\.", "_");
			String bandDir = fileDir+"\\"+fileM3+"\\";
			buffer="<DESTFILE>"+bandDir+"</DESTFILE>";
			writer.write(buffer); 
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return BVSegM3U8(uuidFile1.getPath());
	}

	private boolean BVSegM3U8(String commandTxt) {
		RunExec runExec = new RunExec();
		boolean bet=false;
		try
		{
			Properties props=System.getProperties(); //获得系统属性集   
			String osName = props.getProperty("os.name"); //操作系统名称 
			if(osName.toLowerCase().indexOf("windows")>=0)
			{
				String command =SomsConfigInfo. getHomePath()+"\\BVSTools\\BV_Cutter.exe  "+commandTxt;
				System.out.println(command);
				 bet = runExec.RunBVAppend(command,false);
			}
			else
			{
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bet;
	}
	
	
	/**
	 * mp3文件切片
	 * @param totalPath
	 * @return
	 */
	public boolean Mp3SegM3U8(String totalPath) {
		
		boolean bet=false;
		
		String outF = totalPath.substring(0,totalPath.lastIndexOf("."))+"_"+totalPath.substring(totalPath.lastIndexOf(".")+1);
		File fileF = new File(outF);
		if(!fileF.exists())
			fileF.mkdirs();
		String command = SomsConfigInfo. getHomePath()+"\\BVSTools\\ffmpeg\\ffmpeg -i " + totalPath + " -c copy -map 0 -f segment -segment_list " + outF + "\\tzwj_video.m3u8 -segment_time 10 " + outF + "\\tzwj%03d.ts";
		System.out.println("mp3 comm:"+command);
		RunExec runExec = new RunExec();
		bet = runExec.RunBVAppend(command,true);
		
		
		return bet;
	}
	
	
/**
 * 
 * @param totalPath   源文件全路径
 * @param fileInfo      源文件信息
 * @param GrabPosType //0 表示时间，1表示百分比
 * @param GrabPos 时间(秒，如1.345)或者百分比
 * @param GrabNUM 截图数量
 * @param isInsertDB 是否入库
 * 			isAutoPro 是否是自动执行的服务
 * @return
 */
	public List<String> CutPic(String totalPath, FileInfo fileInfo,int GrabPosType,String GrabPos,int GrabNUM,int IntervalSecond,boolean isInsertDB ,boolean isAutoPro) {
		FileMgr fileMgr = new FileMgr();
		UUID uuid = UUID.randomUUID();
		File uuidFile = new File(SomsConfigInfo.getHomePath()+ "/tempTask/"+uuid.toString()+".txt");
		UUID uUuidPic = UUID.randomUUID();
		String uuidPic = uUuidPic.toString();
		
		//if(totalPath.indexOf("\\")!=-1){
			totalPath = totalPath.replace("\\\\", "/");
			totalPath = totalPath.replace("\\", "/");
		//}else{
			totalPath = totalPath.replaceAll("//", "/");
		//}
		
		//如果是自动执行的服务则文件名和路径要规范以便系统可以提前入库
		if(isAutoPro && fileInfo != null ){
			uuidPic = String.valueOf(fileInfo.getFile_id());
		}else if(fileInfo == null){
			//如果没有文件信息则需要按照文件名生成截图
			uuidPic = totalPath.substring(totalPath.lastIndexOf("/")+1,totalPath.lastIndexOf("."));
		}
		
		List<String> picPathList = new ArrayList<String>();
		try{    
			uuidFile.getParentFile().mkdirs();
			uuidFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile));
			System.out.println("totalPath1=="+totalPath);
			
			System.out.println("totalPath2=="+totalPath);
			String buffer ="<SRCFILE>"+totalPath+"</SRCFILE>\r\n";
			buffer+="<GrabPos>"+GrabPos+"</GrabPos>\r\n";
			buffer+="<GrabPosType>"+GrabPosType+"</GrabPosType>\r\n";
			buffer+="<GrabNUM>"+GrabNUM+"</GrabNUM>\r\n";
			buffer+="<IntervalSecond>"+IntervalSecond+"</IntervalSecond>\r\n";
			writer.write(buffer); 
			String fileDir = "";
			if(totalPath.lastIndexOf("\\")!=-1){
				 fileDir = totalPath.substring(0,totalPath.lastIndexOf("\\")+1);
			}else{
				fileDir = totalPath.substring(0,totalPath.lastIndexOf("/")+1);
			}
			System.out.println("fileDir=="+fileDir);
			for(int i=1;i<=GrabNUM;i++){
				String bandDir= fileDir+uuidPic+"_00"+i+".jpg";
				buffer="<DESTFILE>"+bandDir+"</DESTFILE>\r\n";
				writer.write(buffer); 
			}
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		boolean bet =  BVCutPic(uuidFile.getPath());
		if(bet){
			ImgPublicMgr  imgPublicMgr = new ImgPublicMgr();
			String httpFilePath = fileMgr.getMp4Path(fileInfo);
			System.out.println("httpFilePath=="+httpFilePath);
			String httpFilePathDir = httpFilePath.substring(0, httpFilePath.lastIndexOf("/"));
			for(int i=1;i<=GrabNUM;i++){
				String httpPic= httpFilePathDir+"/"+uuidPic+"_00"+i+".jpg";
				ImageFileInfo imgFile = new ImageFileInfo();
				imgFile.setCatalog_id(fileInfo.getCatalogInfo().getCatalog_id());
				imgFile.setFile_id(fileInfo.getFile_id());
				// imgFile.setImg_path("/cms" + pic_Path);
				imgFile.setImg_path(httpPic);
				imgFile.setImg_type("jpg");
				imgFile.setImg_name(uuidPic+"_00"+i+".jpg");
				picPathList.add(httpPic);
				if(isInsertDB){
					boolean bl = imgPublicMgr.addImageFile(imgFile);
					System.out.println("截图入库成功!");
				}
			}
			
		}
		return picPathList;
	}

	public boolean BVCutPic(String commandTxt) {
	RunExec runExec = new RunExec();
	boolean bet=false;
	try
	{
		Properties props=System.getProperties(); //获得系统属性集   
		String osName = props.getProperty("os.name"); //操作系统名称 
		if(osName.toLowerCase().indexOf("windows")>=0)
		{
			String command =SomsConfigInfo. getHomePath()+"\\grabpic\\BV_CutPIC.exe  "+commandTxt;
			 bet = runExec.RunBVAppend(command);
		}
		else
		{
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return bet;
}

	public String cutFileSinglePIC(FileInfo fileInfo, String cutTime) {
		System.out.println("cutPicTime=="+cutTime);
		String fileLocalPath = fileInfo.getCatalogInfo().getMountInfo().getBase_path()+fileInfo.getFile_path();
		List<String> picPathList = new ArrayList<String>();
		picPathList = CutPic(fileLocalPath, fileInfo, 0, cutTime, 1, 50,false , false );
		return picPathList.get(0);
	}
}
