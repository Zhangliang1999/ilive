package com.jwzt.livems.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import net.sf.json.JSONArray;

import com.jwzt.DB.soms.vod.convert.codec.ConvertCodecInfo;
import com.jwzt.DB.soms.vod.convert.codec.ConvertCodecMgr;
import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.DB.soms.vod.file.FileMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.filemonitor.FileDirMonitor;
import com.jwzt.livems.vod.FileAVInfo;
import com.jwzt.livems.vod.RecordVodMgr;

/**
 * 扫描新上传文件队列
 * @author 郭云飞
 *
 */
public class FileQueueTask extends TimerTask {
	public static void main(String[] args) {
		FileQueueTask task = new FileQueueTask();
		System.out.println(task.canReadFile("D:\\SQLFULL_CHS.iso"));
	}
	public static  synchronized FilePathPo getTask(){
		return FileDirMonitor.queue.poll();
	}
	@Override
	public void run() {
		Thread current = Thread.currentThread();  
	Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】开始", 3);
	System.out.println(Logger.getCurTime2()+"  扫描文件队列  长度为"+FileDirMonitor.queue.size());
		if(FileDirMonitor.queue.size()>0){
			FilePathPo po = new FilePathPo();
			po = FileQueueTask.getTask();
			System.out.println("取完任务扫描文件队列  长度为"+FileDirMonitor.queue.size());
			
			String totalPathAndPath =po.getPath();
			String totalPath = totalPathAndPath.split("JWZTBR")[0];
			String path = totalPathAndPath.split("JWZTBR")[1];
			if(canReadFile(totalPath)){
				//处理此文件
				System.out.println("文件可读 处理文件 "+path);
				Logger.log(Logger.getCurTime2()+"文件可读 处理文件 "+path, 3);
				procVodFile(totalPath,path,po);
			}else{
				FileDirMonitor.queue.offer(po);
			}
		}
		Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】结束", 3);
	}
	/**
	 * 处理新上传的文件
	 * @param path
	 * @param po 
	 * @param path2 
	 */
	private void procVodFile(String totalPath, String path, FilePathPo po) {
		try {
			Thread current = Thread.currentThread();  
			Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】开始处理文件", 3);
			FileMgr fileMgr = new FileMgr();
			ConvertCodecMgr codecMgr = new ConvertCodecMgr();
			path = path.replaceAll("\\\\", "\\/");
			System.out.println("vodPath=="+path);
			if(path.startsWith("/")){
				path = path.substring(1);
			}
			FileInfo fileInfo = null;
			try{
			 fileInfo = fileMgr.getFileByFilePath(path);
			}catch(Exception e){
				FileDirMonitor.queue.offer(po);
			}
			System.out.println(Logger.getCurTime2()+"更新数fileInfo=="+fileInfo);
			
			//是否要强制切片
			String forceCut = SomsConfigInfo.get("forceCut");
			System.out.println(Logger.getCurTime2()+"forceCut=="+forceCut);
			if(fileInfo==null && !"1".equals(forceCut)){
				Logger.log(Logger.getCurTime2()+"更新数fileInfo=="+fileInfo, 3);
				if(po.getRepeatNum()<1800){
				po.setRepeatNum(po.getRepeatNum()+1);
				FileDirMonitor.queue.offer(po);
				}else{
					System.out.println(Logger.getCurTime2()+ "["+po.getPath()+"] 此路径重试超过1800次 被踢出队列");
				}
			}else{
				
				if(fileInfo==null ) fileInfo = new FileInfo();
				
				String fileName = totalPath.substring(totalPath.lastIndexOf("/")+1, totalPath.length());
				String fileExt = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
				//调用C++ 获取文件信息
				RecordVodMgr recordVodMgr = new RecordVodMgr();
				Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】拿文件信息开始", 3);
				FileAVInfo  fileAVInfo= recordVodMgr.getAVFileInfo(totalPath);
				Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】拿文件信息结束", 3);
				//更新数据库 文件信息
				System.out.println(Logger.getCurTime2()+" 更新数fileAVInfo=="+fileAVInfo);
				if(fileAVInfo==null){
					po.setRepeatNum(po.getRepeatNum()+1);
					FileDirMonitor.queue.offer(po);
					return;
				}
				//能查到路径对应的文件信息后  更新文件信息 删除任务信息 
				try{
						File file  = new File(SomsConfigInfo.getHomePath()+"/filequeueTask/"+po.getUuid()+".txt");
						boolean deleteUUfile = file.delete();
						System.out.println(Logger.getCurTime2()+"线程【"+current.getId()+"】删除队列文件:"+deleteUUfile+"##"+po.getUuid());
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】删除队列文件:"+deleteUUfile+"##"+po.getUuid(), 3);
					}catch(Exception e){
						e.printStackTrace();
					}
				boolean bet = updateFileAVInfo(fileAVInfo,fileInfo);
				System.out.println(Logger.getCurTime2()+" 更新数据库 文件信息=="+bet);
				Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】 更新数据库 文件信息=="+bet,3);
				System.out.println(Logger.getCurTime2()+"线程【"+current.getId()+"】  fileAVInfo.getFileFormat()="+ fileAVInfo.getFileFormat());
				Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】 fileAVInfo.getFileFormat()="+ fileAVInfo.getFileFormat(), 3);
				
				String M3U8VideoCodec ="h264";
				String VideoCodec = fileAVInfo.getVideoCodec();	
				String FileFormat = fileAVInfo.getFileFormat();
				String fileType = fileAVInfo.getFileType();
				String  AudioCodec= fileAVInfo.getAudioCodec();
				VideoCodec = VideoCodec.toLowerCase();
				AudioCodec = AudioCodec.toLowerCase();
				boolean canM3U8 = false;
				boolean canCutPic = false;
				System.out.println("FileFormat:"+FileFormat+":");
				System.out.println("fileType:"+fileType+":");
				System.out.println("VideoCodec:"+VideoCodec+":");
				System.out.println("AudioCodec.toLowerCase():"+AudioCodec.toLowerCase()+":");
					
				if(FileFormat.toLowerCase().equals("mp4")){
					if(fileType.equals("only_audio")&&(AudioCodec.toLowerCase().equals("mp3")||AudioCodec.toLowerCase().equals("mp3"))){
						canM3U8=true;
					}else if(fileType.indexOf("video")!=-1 && M3U8VideoCodec.indexOf(VideoCodec)!=-1&&(AudioCodec.toLowerCase().equals("mp3")||AudioCodec.toLowerCase().equals("aac"))){
						
						canM3U8=true;
						canCutPic = true;
					}
				}else if(path.toLowerCase().endsWith(".mp3")){
					if(AudioCodec.toLowerCase().equals("aac")||AudioCodec.toLowerCase().equals("mp3")){
						fileAVInfo.setVideoWidth("0");
						fileAVInfo.setVideoHeight("0");
						canM3U8=true;
					}
				}
				if(("1".equals(forceCut) || fileInfo.getMountInfo().getIscut()==null || fileInfo.getMountInfo().getIscut()==1)){
					if(canM3U8){
						//不需要转码  可以做切片
						//做切片
						Logger.log(Logger.getCurTime2()+" 开始对文件ID["+fileInfo.getFile_id()+"] 路径{"+totalPath+"} 进行切片", 3);
						
						if(path.toLowerCase().endsWith(".mp3")){
							recordVodMgr.Mp3SegM3U8(totalPath);
						}else{
							recordVodMgr.SegM3U8(totalPath);
						}

						Logger.log(Logger.getCurTime2()+" 结束对文件ID["+fileInfo.getFile_id()+"] 路径{"+totalPath+"} 进行切片", 3);
						
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】开始制作M3U8文件", 3);
						//制作M3U8 文件
						String fileDir = totalPath.substring(0, totalPath.lastIndexOf("\\"));
						String fileDirName = totalPath.substring(totalPath.lastIndexOf("\\")+1, totalPath.length());
						String fileM3 = fileDirName.replaceAll("\\.", "_");
						String bandDir = fileM3;
						String mainM3U8FileName ="tzwj_main_"+fileM3+".m3u8";
						String totalM3U8Name = fileM3.split("_")[0]+".m3u8";
						
						
						File totalM3U8 = new File(fileDir+"/"+totalM3U8Name);
						File mainM3U8File = new File(fileDir+"/"+mainM3U8FileName);
						String M3UHead = "#EXTM3U";
						String streamM3 = "#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH="+fileInfo.getBand_width()*1000;
						String bandM3 = bandDir+"/tzwj_video.m3u8";
						String addM3U8="";
						if(!mainM3U8File.exists()){
							try {
								mainM3U8File.createNewFile();
								addM3U8+=M3UHead+"\r\n";
								addM3U8+=streamM3+"\r\n";
								addM3U8+=bandM3+"\r\n";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							addM3U8+=streamM3+"\r\n";
							addM3U8+=bandM3+"\r\n";
						}
						String tM3U8 ="";
						if(!totalM3U8.exists()){
							try {
								totalM3U8.createNewFile();
								tM3U8+=M3UHead+"\r\n";
								tM3U8+=streamM3+"\r\n";
								tM3U8+=bandM3+"\r\n";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							tM3U8+=streamM3+"\r\n";
							tM3U8+=bandM3+"\r\n";
						}
						try {
							FileOutputStream out = new FileOutputStream(mainM3U8File, true);
							out.write(addM3U8.getBytes("utf-8"));
							out.flush();
							out.close();
							FileOutputStream out2 = new FileOutputStream(totalM3U8, true);
							out2.write(tM3U8.getBytes("utf-8"));
							out2.flush();
							out2.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】结束制作M3U8文件", 3);
						//写完之后做排序   //读取m3u8 文件
						
						//取出所有的码流 信息   并  排序
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】取出所有的码流 信息   并  排序", 3);
						List<Integer> bandWidthList = new ArrayList<Integer>();
						try {
							FileInputStream fins = new FileInputStream(totalM3U8);
							BufferedReader reader2 = new BufferedReader(new InputStreamReader(fins ));
							String dd = "";
							while((dd = reader2.readLine()) != null)
							{
								if(dd.indexOf("BANDWIDTH=")!=-1){
									Integer bandw =Integer.parseInt( dd.substring(dd.lastIndexOf("=")+1));
									bandWidthList.add(bandw);
								}
							}
							reader2.close();
							fins.close();
							Collections.sort(bandWidthList); //排序
							//清空M3U8文件
							FileWriter fw = new FileWriter(totalM3U8);
							fw.write("");
							fw.close();
							//按降序 重新写入M3U8文件
							FileOutputStream outF = new FileOutputStream(totalM3U8, true);
							String M3U8Sorted = "#EXTM3U\r\n";
							for(int i=bandWidthList.size()-1;i>=0;i--){
								int bandwidth = bandWidthList.get(i);
								M3U8Sorted+="#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH="+bandwidth+"\r\n";
								String bandDir2 = fileM3;
								M3U8Sorted+= bandDir2+"/tzwj_video.m3u8\r\n";
							}
							outF.write(M3U8Sorted.getBytes("utf-8"));
							outF.flush();
							outF.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】取出所有的码流 信息   并  排序结束", 3);
					//写下载文件
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】写下载文件开始", 3);
						
						File downloadJson = new File(fileDir+"/"+"downloadJson.txt");
						List<FileJson> downList = new ArrayList<FileJson>();
						if(downloadJson.exists()){
							FileInputStream fins = new FileInputStream(downloadJson);
							BufferedReader reader3 = new BufferedReader(new InputStreamReader(fins ));
							String oldJsonStr = "";
							String dd="";
							while((dd = reader3.readLine()) != null)
							{
								oldJsonStr+=dd;
							}
							reader3.close();
							fins.close();
							if(!oldJsonStr.equals("")){
								JSONArray json = JSONArray.fromObject(oldJsonStr);
								downList= (List<FileJson>)JSONArray.toCollection(json, FileJson.class);
							}
						}else{
							downloadJson.createNewFile();
						}
						FileJson fileJson = new FileJson();
						fileJson.setFileName(fileDirName);
						fileJson.setBandWidth(fileInfo.getBand_width());
						downList.add(fileJson);
						FileCompartor comparator=new FileCompartor();  
						Collections.sort(downList, comparator);
						System.out.println("downList.size()=="+downList.size());
						JSONArray array = JSONArray.fromObject(downList);
					    String jsonstr = array.toString();
					    System.out.println("jsonstr=="+jsonstr);
					    FileWriter fwDown = new FileWriter(downloadJson);
					    fwDown.write("");
					    fwDown.close();
					    
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】写下载文件结束", 3);
						//按降序 重新写入M3U8文件
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】按降序 重新写入M3U8文件开始", 3);
						FileOutputStream outDown = new FileOutputStream(downloadJson, true);
						outDown.write(jsonstr.getBytes("utf-8"));
						outDown.flush();
						outDown.close();
						Logger.log(Logger.getCurTime2()+"线程【"+current.getId()+"】按降序 重新写入M3U8文件开始", 3);
					}else{
						//需要转码
						//获取默认转码参数 进行转码
						ConvertCodecInfo codeInfo = 	codecMgr.getConvertCodecInfoDefault();
						fileMgr.insertConvertTaskMethod(fileInfo,codeInfo.getCodecId());
					}
					
					if(!bet){
						Thread.sleep(60000);
						fileInfo = fileMgr.getFileByFilePath(path);
						updateFileAVInfo(fileAVInfo,fileInfo);
					}
					if(canCutPic){
						System.out.println(Logger.getCurTime2()+"线程【"+current.getId()+"】  开始截图");
						recordVodMgr.CutPic(totalPath,fileInfo,1,"20%",3,20,true,true);
					}
					
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private boolean updateFileAVInfo(FileAVInfo fileAVInfo, FileInfo fileInfo) {
		boolean bet= false;
		try {
			double d_duration =Double.parseDouble(fileAVInfo.getFileDuration()) ;
			int duration = (int) ((d_duration*100))/100;
			
			String vod_width_height = fileAVInfo.getVideoWidth()+"*"+fileAVInfo.getVideoHeight();
			fileInfo.setDuration(duration);
			fileInfo.setVod_width_height(vod_width_height);
			String file_size = fileAVInfo.getFileSize();
			int fileSize = (int) (Long.parseLong(file_size)/1024);
			fileInfo.setFile_size(fileSize);
			int band_width = (fileSize/duration)*8;
			fileInfo.setBand_width(band_width);
			FileMgr fileMgr = new FileMgr();
			if(fileInfo.getFile_id()!= null && fileInfo.getFile_id()>0)
				bet = fileMgr.updateFileInfo(fileInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bet;
	}
	/**
	 * 判断文件是否可读 可操作
	 * @param fileName
	 * @return
	 */
	private boolean canReadFile(String fileName) {
		boolean bet = false;
		try {
			File uploadFile = new File(fileName); //得到上传文件
			//InputStream  ins = new FileInputStream(uploadFile);
			String rename = fileName+"1";
			File reFile = new File(rename); 
			bet=uploadFile.renameTo(reFile);
			if(bet){
				reFile.renameTo(uploadFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bet=false;
		}
		System.out.println("文件   "+fileName+"    是否能读取了=="+bet);
		Logger.log(Logger.getCurTime2()+"  文件   "+fileName+"    是否能读取了=="+bet,3);
		return bet;
	}
	
}
