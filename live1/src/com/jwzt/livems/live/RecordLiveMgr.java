package com.jwzt.livems.live;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.jwzt.DB.cdn.accessMethods.ILiveServerAccessMethod;
import com.jwzt.DB.cdn.accessMethods.ILiveServerAccessMgr;
import com.jwzt.DB.cdn.mount.MountInfo;
import com.jwzt.DB.cdn.mount.MountMgr;
import com.jwzt.DB.cdn.server.ILiveServer;
import com.jwzt.DB.cdn.server.ILiveServerMgr;
import com.jwzt.DB.cdn.serverGroup.ILiveServerGroup;
import com.jwzt.DB.soms.live.liveChannel.ChannelInfo;
import com.jwzt.DB.soms.live.liveChannel.ChannelMgr;
import com.jwzt.DB.soms.live.liveProgram.ProgramInfo;
import com.jwzt.DB.soms.live.liveProgram.ProgramMgr;
import com.jwzt.DB.soms.vod.catalog.CatalogInfo;
import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.common.AppTools;
import com.jwzt.common.FtpClientUtil;
import com.jwzt.common.Logger;
import com.jwzt.common.RunExec;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.common.StringUtil;

public class RecordLiveMgr {

	public static void main(String[] args) {
		String flvStr = "20150311/15/live612_1426057241_video_seq_99.ts";
		flvStr = flvStr.substring(flvStr.lastIndexOf("_") + 1, flvStr.lastIndexOf("."));
		System.out.println(flvStr);
	}

	/**
	 * 录制文件
	 * 
	 * @param mountName
	 *            发布点名
	 * @param startTime
	 *            开始时间
	 * @param length
	 *            录制时长
	 * @param recordPath
	 *            录制目标文件地址 d:/vod/
	 * @param destGroupId
	 * @param serverGroupId
	 * @return
	 */
	public String recordLive(String mountName, Timestamp startTime, long length, String recordPath,
			String serverGroupId, String destGroupId, String ftpPath) {
		String ret = "";
		Logger.log("mountName:" + mountName, 3);
		Logger.log("startTime:" + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("yyyy-MM-dd HH:mm:ss", startTime), 3);
		Logger.log("length:" + length, 3);
		Logger.log("recordPath:" + recordPath, 3);

		// 对应的收录缓存位置+发布点名字+live
		String liveDir = com.jwzt.common.SomsConfigInfo.get("liveDir") + "/" + mountName;
		liveDir = liveDir.replaceAll("//", "/");
		Logger.log("liveDir:" + liveDir, 3);
		File liveMountFilePath = new File(liveDir);
		if (liveMountFilePath.exists()) {
			File[] list = liveMountFilePath.listFiles();
			if (list != null) {
				for (File dir : list) {
					if (dir.isDirectory()) {
						String format = "yyyyMMdd";
						String sIndexFile = dir.getPath() + "/";
						// 节目开始日期的index文件
						String startTimeIndexFile = sIndexFile
								+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, startTime);
						startTimeIndexFile += "_index.txt";
						// 节目结束日期的index文件
						String endTimeIndexFile = sIndexFile + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format,
								new Timestamp(startTime.getTime() + length * 1000));
						endTimeIndexFile += "_index.txt";
						File startIndexFile = new File(startTimeIndexFile);
						File endIndexFile = new File(endTimeIndexFile);
						Logger.log("startTimeIndexFile:" + startTimeIndexFile, 3);
						Logger.log("endTimeIndexFile:" + endTimeIndexFile, 3);
						if (startIndexFile.exists()) {
							// long lastModifyTime =
							// startIndexFile.lastModified();
							// 如果录制的节目是今天的并且该文件有30分钟没有更新 那么就跳过
							// if ((System.currentTimeMillis() - lastModifyTime)
							// / 1000 > 60 * 30
							// &&
							// com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format,
							// startTime)
							// .equals(com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format,
							// today))) {
							// continue;
							// }
							Timestamp endTime = getEndTime(startTime, length);
							String timeformat = "HHmmss";
							String sStartTime = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(timeformat, startTime);
							String sEndTime = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(timeformat, endTime);
							String sDesFile = recordPath + "/" + mountName + "/"
									+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, startTime) + "/"
									+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime) + length
									+ "_mp4" + "/" + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime)
									+ "_" + length + "_" + dir.getName() + ".mp4";
							String ftpDir = ftpPath + "/" + mountName + "/"
									+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format, startTime) + "/"
									+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime) + length
									+ "_mp4" + "/";
							String ftpFileName = com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime)
									+ "_" + length + "_" + dir.getName() + ".mp4";
							String cacheDir = SomsConfigInfo.getHomePath() + "/cache/" + "/" + mountName + "/"
									+ com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime) + length
									+ "_mp4" + "/" + com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss", startTime)
									+ "_" + length + "_" + dir.getName() + ".mp4";
							// String path =
							// com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING(format,
							// startTime) +"/" +
							// com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss",
							// startTime) + length +".mp4"+ "/" +
							// com.jwzt.common.StringUtil.SQLTIMESTAMP2STRING("HHmmss",
							// startTime)+ "_" + length+dir.getName() + "_"
							// +".mp4";
							try {
								TheardOut th = new TheardOut(startIndexFile, endIndexFile, dir.getPath(), sStartTime,
										sEndTime, sDesFile, cacheDir, serverGroupId, destGroupId, ftpDir, ftpFileName,
										"-1", "-1");
								th.start();
								ret += ftpDir + ftpFileName + ",";
							} catch (Exception e) {
								e.printStackTrace();
								Logger.log(e.getMessage(), 3);
							}
						} else {
							Logger.log("startIndexFile:" + startIndexFile.getAbsolutePath() + "存在的结果："
									+ startIndexFile.exists() + ",不存在", 3);
						}
					} else {
						Logger.log("dir:" + dir.getAbsolutePath() + "不存在", 3);
					}
				}
			} else {
				System.out.println("list === null ");
			}
		} else {
			Logger.log("liveDir:" + liveDir + "不存在", 3);
		}
		return ret;
	}

	/**
	 * 录制文件
	 * 
	 * @param fIndexFile
	 *            D:\live211\500K\20141126_index.txt
	 * @param sLiveMounthPath
	 *            D:\live211\500K
	 * @param lStartTime
	 *            开始时间 小时分钟秒
	 * @param lEndTime
	 *            结束时间 小时分钟秒
	 * @param sDesFile
	 *            目标录制文件地址 d:/vod/
	 * @param sEndOffSet
	 * @param sStartOffSet
	 * @return
	 * @throws IOException
	 */
	private boolean record(File startIndexFile, File endIndexFile, String sLiveMounthPath, String sStartTime,
			String sEndTime, String sDesFile, String sStartOffSet, String sEndOffSet) throws IOException {
		boolean ret = false;
		int starttime = Integer.parseInt(sStartTime);
		int endtime = Integer.parseInt(sEndTime);
		// 需要输出的文件列表
		List<String> recordFileList = new ArrayList<String>();
		// 第一个文件开始录制时间
		int startOffSet = -1;
		// 最后一个文件结束录制时间
		int endOffSet = -1;
		FileInputStream fileInputStream = new FileInputStream(startIndexFile);
		InputStreamReader read = new InputStreamReader(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		String preLineTxt = null;
		FileInputStream endfileInputStream = new FileInputStream(endIndexFile);
		InputStreamReader endread = new InputStreamReader(endfileInputStream);
		BufferedReader endbufferedReader = new BufferedReader(endread);
		String endlineTxt = null;
		String endpreLineTxt = null;
		Logger.log(Logger.getCurTime2() + "startIndexFile:" + startIndexFile, 3);
		Logger.log(Logger.getCurTime2() + "endIndexFile:" + endIndexFile, 3);
		Logger.log(Logger.getCurTime2() + "sStartTime:" + sStartTime, 3);
		Logger.log(Logger.getCurTime2() + "sEndTime:" + sEndTime, 3);
		if (starttime < endtime) {
			try {
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] message = lineTxt.split("[|]");
					String[] datetime = message[0].split("_");
					int time = Integer.parseInt(datetime[1]);
					if (time >= starttime && time < endtime) {
						// 如果是第一次进来判断一下之前一个文件也需要加入到列表
						if (preLineTxt != null && startOffSet == -1) {
							startOffSet = time - starttime;
							if (startOffSet > 0) {
								boolean endsWith = lineTxt.endsWith("_0.flv");
								if (!endsWith) {
									String[] preMessage = preLineTxt.split("[|]");
									int length = Integer.parseInt(preMessage[1]);
									startOffSet = length - startOffSet;
									recordFileList.add(sLiveMounthPath + "/" + preMessage[2]);
								}
							}
						}
						// 如果录制时间不够开始时间的话
						else if (preLineTxt == null && startOffSet == -1) {
							startOffSet = 0;
						}
						recordFileList.add(sLiveMounthPath + "/" + message[2]);
					}
					// 如果开始录制以后到了录制结束时
					if (time >= endtime) {
						if (startOffSet != -1) {
							endOffSet = time - endtime;
							if (endOffSet != 0) {
								String[] preMessage = preLineTxt.split("[|]");
								int length = Integer.parseInt(preMessage[1]);
								endOffSet = length - endOffSet;
							} else {
								endOffSet = -1;
							}
						}
						break;
					}
					preLineTxt = lineTxt;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		} else {
			try {
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] message = lineTxt.split("[|]");
					String[] datetime = message[0].split("_");
					int time = Integer.parseInt(datetime[1]);
					if (time >= starttime) {
						// 如果是第一次进来判断一下之前一个文件也需要加入到列表
						if (preLineTxt != null && startOffSet == -1) {
							startOffSet = time - starttime;
							if (startOffSet > 0) {
								boolean endsWith = lineTxt.endsWith("_0.flv");
								if (endsWith) {
									String[] preMessage = preLineTxt.split("[|]");
									int length = Integer.parseInt(preMessage[1]);
									startOffSet = length - startOffSet;
									recordFileList.add(sLiveMounthPath + "/" + preMessage[2]);
								}
							}

						}
						// 如果录制时间不够开始时间的话
						else if (preLineTxt == null && startOffSet == -1) {
							startOffSet = 0;
						}
						recordFileList.add(sLiveMounthPath + "/" + message[2]);
					}
					preLineTxt = lineTxt;
				}

				while ((endlineTxt = endbufferedReader.readLine()) != null) {
					String[] message = endlineTxt.split("[|]");
					String[] datetime = message[0].split("_");
					int time = Integer.parseInt(datetime[1]);
					if (time < endtime) {
						// 如果是第一次进来判断一下之前一个文件也需要加入到列表
						if (endpreLineTxt != null && startOffSet == -1) {
							startOffSet = time - starttime;
							if (startOffSet > 0) {
								String[] preMessage = endpreLineTxt.split("[|]");
								int length = Integer.parseInt(preMessage[1]);
								startOffSet = length - startOffSet;
								recordFileList.add(sLiveMounthPath + "/" + preMessage[2]);
							}

						}
						// 如果录制时间不够开始时间的话
						else if (endpreLineTxt == null && startOffSet == -1) {
							startOffSet = 0;
						}
						recordFileList.add(sLiveMounthPath + "/" + message[2]);
					}
					// 如果开始录制以后到了录制结束时
					if (time >= endtime) {
						if (startOffSet != -1) {
							endOffSet = time - endtime;
							if (endOffSet != 0) {
								String[] preMessage = preLineTxt.split("[|]");
								int length = Integer.parseInt(preMessage[1]);
								endOffSet = length - endOffSet;
							} else {
								endOffSet = -1;
							}

						}
						break;
					}
					endpreLineTxt = endlineTxt;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		read.close();
		endbufferedReader.close();
		try {
			Logger.log(Logger.getCurTime2() + "recordFileList:" + recordFileList.size(), 3);
		} catch (Exception e) {
		}
		// 生成输出文件
		if (startOffSet != -1) {
			if (startOffSet == 0)
				startOffSet = -1;
			creatVodFile(recordFileList, startOffSet, endOffSet, sDesFile, sStartOffSet, sEndOffSet);
		}
		return ret;
	}

	/**
	 * 创建录制文件
	 * 
	 * @param recordFileList
	 *            切片文件集合
	 * @param startOffSet
	 *            第一片开始时间
	 * @param endOffSet
	 *            最后一片结束时间
	 * @param desFile
	 *            生成的目标mp4文件
	 * @param sEndOffSet
	 * @param sStartOffSet
	 * @return
	 */
	private static boolean creatVodFile(List<String> recordFileList, int startOffSet, int endOffSet, String desFile,
			String sStartOffSet, String sEndOffSet) {
		boolean ret = false;
		UUID uuid = UUID.randomUUID();
		File uuidFile = new File(SomsConfigInfo.getHomePath() + "/tempTask/" + uuid.toString() + ".txt");
		try {    
			uuidFile.getParentFile().mkdirs();
			uuidFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile));
			for(int i=0 ; i<recordFileList.size() ; i++)
			{
				String buffer = "<SRCFILE>"+recordFileList.get(i)+"|";
				if(i==0){
					buffer += sStartOffSet;
				}else{
					buffer += "-1";
				}
				buffer += "|";
				
				if(i==recordFileList.size()-1){
					buffer += sEndOffSet;
				}else{
					buffer += "-1";
				}
				buffer += "</SRCFILE>";
				if(i==recordFileList.size()-1 && endOffSet ==0 ){
					//如果最后一个切片的长度是0则最后一个切片丢掉，不要加到任务队列里了。协助处理UMS时间不整齐的BUG
					
				}
				else{
					writer.write(buffer+"\r\n"); 
				}
				
			}
			writer.write("<DESTFILE>"+desFile+"</DESTFILE>"); 
			         
			writer.close();
			} catch (Exception e) {
			e.printStackTrace();
			Logger.log(e.getMessage(), 3);
		}

		if ("0".equals(SomsConfigInfo.get("linuxConfig"))) {
			// 调用C++
			// RunBVAppend(uuidFile.getPath());
			RunExec exec = new RunExec();
			Properties props = System.getProperties(); // 获得系统属性集
			String osName = props.getProperty("os.name"); // 操作系统名称
			if (osName.toLowerCase().indexOf("windows") >= 0) {
				boolean s = exec
						.RunBVAppend(SomsConfigInfo.getHomePath() + "/BVSTools/BVAppend2014.exe " + uuidFile.getPath());
				Logger.log(SomsConfigInfo.getHomePath() + "/BVSTools/BVAppend2014.exe " + uuidFile.getPath(), 3);
				if (s) {
					desFile = desFile.replaceAll("\\\\", "/");
					s = RunExec.SYRunBVAppend(
							SomsConfigInfo.getHomePath() + "/BVSTools/mp4box1/mp4box.exe -hint " + desFile);
					Logger.log(SomsConfigInfo.getHomePath() + "/BVSTools/mp4box1/mp4box.exeo -hint " + desFile, 3);
				}
			}
		} else {
			Logger.log(SomsConfigInfo.getHomePath() + "调用desFile收录 ", 3);
			// 调用ffpeg命令
			RunExec exec = new RunExec();
			File destFile = new File(desFile);
			if (!destFile.getParentFile().exists() && !destFile.getParentFile().isDirectory()) {
				destFile.getParentFile().mkdirs();
			}
			Logger.log("调用bvcrd_append收录 uuidFile " + uuidFile.getAbsolutePath(), 3);
			boolean runBVAppend = exec.RunBVAppend("bvcrd_append " + uuidFile.getPath(), true);
			Logger.log("调用bvcrd_append收录命令的结果为 " + runBVAppend, 3);
			Logger.log("调用bvcrd_append收录 desFile " + desFile, 3);
		}
		return ret;
	}

	public boolean RunBVAppend(String commandTxt) {
		boolean ret = false;
		System.out.println(commandTxt);
		Process process = null;
		BufferedReader reader = null;
		BufferedReader errorreader = null;
		try {
			Properties props = System.getProperties(); // 获得系统属性集
			String osName = props.getProperty("os.name"); // 操作系统名称
			if (osName.toLowerCase().indexOf("windows") >= 0) {
				String command = SomsConfigInfo.getHomePath() + "/BVAppend/BVAppend2014.exe " + commandTxt;
				// String command =
				// SomsConfigInfo.getHomePath()+"/BVAppend/start.bat
				// "+commandTxt;
				// String command = "ping 192.168.1.31";
				process = Runtime.getRuntime().exec(command);
				errorreader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = errorreader.readLine()) != null) {
					System.out.println(line);
				}
				// process.waitFor();
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (errorreader != null)
					errorreader.close();
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		System.out.println("END:" + ret);
		return ret;
	}

	/**
	 * 获得某个开始时间的结束时间
	 * 
	 * @param startTime
	 *            开始时间
	 * @param length
	 *            时长 秒
	 * @return
	 */
	private Timestamp getEndTime(Timestamp startTime, long length) {
		Timestamp ret = startTime;

		ret = new Timestamp(startTime.getTime() + (length * 1000));

		return ret;
	}

	/**
	 * 为了提高效率采用执行线程
	 * 
	 * @author gstars
	 *
	 */
	private class TheardOut extends Thread {

		private File startIndexFile;
		private File endIndexFile;
		private String sLiveMounthPath;
		private String sStartTime;
		private String sEndTime;
		private String sDesFile;
		private String cacheDir;
		private String serverGroupId;
		private String destGroupId;
		private String ftpDir;
		private String ftpFileName;
		private String sStartOffSet;
		private String sEndOffSet;

		public TheardOut(File startIndexFile, File endIndexFile, String sLiveMounthPath, String sStartTime,
				String sEndTime, String sDesFile, String cacheDir, String serverGroupId, String destGroupId,
				String ftpDir, String ftpFileName, String sStartOffSet, String sEndOffSet) {
			this.startIndexFile = startIndexFile;
			this.endIndexFile = endIndexFile;
			this.sLiveMounthPath = sLiveMounthPath;
			this.sStartTime = sStartTime;
			this.sEndTime = sEndTime;
			this.sDesFile = sDesFile;
			this.cacheDir = cacheDir;
			this.serverGroupId = serverGroupId;
			this.destGroupId = destGroupId;
			this.ftpDir = ftpDir;
			this.ftpFileName = ftpFileName;
			this.sEndOffSet = sEndOffSet;
			this.sStartOffSet = sStartOffSet;
		}

		private Integer true_flag = 1;

		@SuppressWarnings("unchecked")
		public void run() {
			try {
				record(startIndexFile, endIndexFile, sLiveMounthPath, sStartTime, sEndTime, cacheDir, sStartOffSet,
						sEndOffSet);
				File srcFile = new File(cacheDir);
				String NeedFTP = SomsConfigInfo.get("NeedFTP");
				if (NeedFTP.equals("1")) {
					ILiveServerMgr serverMgr = new ILiveServerMgr();
					ILiveServer server = null;
					try {
						server = serverMgr.getMainServerInfo(Integer.parseInt(destGroupId));
					} catch (Exception e) {
					}
					if (server == null) {
						Thread.sleep(1000);
						// mysql不利用连接池 8小时不操作连接断开，重试确保连接连上
						server = serverMgr.getMainServerInfo(Integer.parseInt(destGroupId));
					}
					MountMgr moutMgr = new MountMgr();
					List<MountInfo> mountList = moutMgr.getMountList(Integer.parseInt(destGroupId));
					MountInfo mountInfo = null;
					for (MountInfo minfo : mountList) {
						Integer isDefault = minfo.getIsDefault();
						if (true_flag.equals(isDefault)) {
							mountInfo = minfo;
							break;
						}
					}
					ILiveServerAccessMgr accessMethodsMgr = new ILiveServerAccessMgr();
					List<ILiveServerAccessMethod> list = accessMethodsMgr
							.getAccessMethodsList("iliveServerId=" + server.getServer_id() + " and default_method=1");
					ILiveServerAccessMethod amInfo = list.get(0);
					String serverAddr = amInfo.getFtp_address();
					int index = serverAddr.indexOf(":");
					int port = 21;
					if (index != -1) {
						port = Integer.parseInt(serverAddr.substring(index + 1));
						serverAddr = serverAddr.substring(0, index);
					}
					System.out.println("serverAddr=" + serverAddr);
					Logger.log("serverAddr:" + serverAddr, 3);
					System.out.println("port=" + port);
					System.out.println("amInfo.getFtp_user()=" + amInfo.getFtp_user());
					System.out.println("amInfo.getFtp_pwd()=" + amInfo.getFtp_pwd());
					String ftpPathMount = mountInfo.getFtp_path();
					FtpClientUtil ftpUtil = new FtpClientUtil(serverAddr, port, amInfo.getFtp_user(),
							amInfo.getFtp_pwd());
					System.out.println("cacheDir=" + cacheDir);
					System.out.println("ftpFileName=" + ftpFileName);
					System.out.println("ftpDir=" + ftpDir);
					String remoteDir = ftpPathMount + ftpDir;
					remoteDir = remoteDir.replace("//", "/");
					Logger.log("remoteDir:" + ftpPathMount + ftpDir, 3);
					boolean upload = ftpUtil.upload(cacheDir, ftpFileName, remoteDir);
					Logger.log("cacheDir:" + cacheDir, 3);
					Logger.log("remotePath:" + ftpPathMount + ftpDir + ftpFileName, 3);
					Logger.log("upload Result:" + upload, 3);
				} else {
					System.out.println("直播服务器和点播服务器在一台机器上，可以直接拷贝，是另外一种方式");
					System.out.println("从缓存目录移动文件到正式目录");
					System.out.println(cacheDir + "to" + sDesFile);
					AppTools.copyFile(cacheDir, sDesFile, false);
					System.out.println("目录移动文件完成");
				}
				srcFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 为了提高效率采用执行线程
	 * 
	 * @author gstars
	 *
	 */
	private class TheardDownloadOut extends Thread {

		private List<ProgramInfo> programList;
		private String liveDir;
		private String ymd;

		public TheardDownloadOut(List<ProgramInfo> programList, String liveDir, String ymd) {
			this.programList = programList;
			this.liveDir = liveDir;
			this.ymd = ymd;
		}

		public void run() {
			try {
				Timestamp firstProgramStartTime = null;
				SimpleDateFormat formatHMS = new SimpleDateFormat("HHmmss");
				StringBuffer commandBuffer = new StringBuffer();
				int channelId = -1;
				int fileIndex = 0;
				for (ProgramInfo programInfo : programList) {
					channelId = programInfo.getChannelInfo().getChannel_id();
					if (fileIndex == 0) {
						firstProgramStartTime = programInfo.getStart_time();
					}
					fileIndex++;
					ILiveServerMgr serverMgr = new ILiveServerMgr();
					FileInfo fileInfo = programInfo.getFileInfo();
					CatalogInfo catalogInfo = fileInfo.getCatalogInfo();
					MountInfo mountInfo = catalogInfo.getMountInfo();
					ILiveServerGroup serverGroupInfo = mountInfo.getServer_group();
					ILiveServer serverInfo = serverMgr.getMainServerInfo(serverGroupInfo.getServer_group_id());
					ILiveServerAccessMgr accessMethodsMgr = new ILiveServerAccessMgr();
					List<ILiveServerAccessMethod> list = accessMethodsMgr
							.getAccessMethodsList("server_id=" + serverInfo.getServer_id() + " and default_method=1");
					ILiveServerAccessMethod amInfo = list.get(0);
					String server = amInfo.getFtp_address();
					int index = server.indexOf(":");
					int port = 21;
					if (index != -1) {
						port = Integer.parseInt(server.substring(index));
						server = server.substring(0, index);
					}
					System.out.println("server=" + server);
					System.out.println("port=" + port);
					System.out.println("amInfo.getFtp_user()=" + amInfo.getFtp_user());
					System.out.println("amInfo.getFtp_pwd()=" + amInfo.getFtp_pwd());
					FtpClientUtil ftpUtil = new FtpClientUtil(server, port, amInfo.getFtp_user(), amInfo.getFtp_pwd());
					String ftpDirectoryAndFileName = mountInfo.getFtp_path() + fileInfo.getFile_path();
					String localDirectoryAndFileName = liveDir + fileIndex + ".mp4";
					commandBuffer.append("<SRCFILE>" + localDirectoryAndFileName + "</SRCFILE>");
					long filesize = ftpUtil.download(ftpDirectoryAndFileName, localDirectoryAndFileName);
				}
				commandBuffer.append("<SegTSVideoFile>1</SegTSVideoFile>");
				commandBuffer.append("<SegTSAudioFile>1</SegTSAudioFile>");

				if ("0".equals(SomsConfigInfo.get("ifCUTFLV"))) {
					commandBuffer.append("<SegFLVVideoFile>0</SegFLVVideoFile>");
				} else {
					commandBuffer.append("<SegFLVVideoFile>1</SegFLVVideoFile>");
				}

				String firstProHMS = formatHMS.format(firstProgramStartTime);
				String BeginTime = ymd + "_" + firstProHMS;
				commandBuffer.append("<BeginTime>" + BeginTime + "</BeginTime>");
				commandBuffer.append("<DESTFILE>" + liveDir + "</DESTFILE>");
				UUID uuid1 = UUID.randomUUID();
				File uuidFile1 = new File(SomsConfigInfo.getHomePath() + "/tempTask/" + uuid1.toString() + ".txt");
				try {
					uuidFile1.getParentFile().mkdirs();
					uuidFile1.createNewFile();
					BufferedWriter writer = new BufferedWriter(new FileWriter(uuidFile1));
					writer.write(commandBuffer.toString());
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 做切片
				boolean bet = BVFileProgram(uuidFile1.getPath());
				// 切片完成后 转移index文件位置
				if (bet) {
					File IndexFile = new File(liveDir + ymd + "_index.txt");
					moveIndexFile(IndexFile, liveDir, ymd);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private List<String> moveIndexFile(File indexFile, String liveDir, String ymd) {
			List<String> recordList = new ArrayList<String>();
			File liveFile = new File(liveDir);
			String livePath = liveFile.getParent();
			File indexFileDest = new File(livePath + "/" + ymd + "_index.txt");
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(indexFileDest));

				FileInputStream fin = new FileInputStream(indexFile);
				BufferedReader dr = new BufferedReader(new InputStreamReader(fin));
				String linetxt = "";
				while ((linetxt = dr.readLine()) != null) {
					String linetxtHead = linetxt.substring(0, linetxt.lastIndexOf("|") + 1);
					String linetxtEnd = linetxt.substring(linetxt.lastIndexOf("|") + 1);
					linetxtEnd = ymd + "/" + linetxtEnd;
					linetxt = linetxtHead + linetxtEnd;
					writer.write(linetxt);
					writer.write("\r\n");
				}
				writer.close();
				dr.close();
				fin.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return recordList;

		}

		// 做虚拟直播文件切片处理
		private boolean BVFileProgram(String commandTxt) {
			RunExec runExec = new RunExec();
			boolean bet = false;
			try {
				Properties props = System.getProperties(); // 获得系统属性集
				String osName = props.getProperty("os.name"); // 操作系统名称
				if (osName.toLowerCase().indexOf("windows") >= 0) {
					String command = SomsConfigInfo.getHomePath() + "\\BVSTools\\BV_CutterMore.exe  " + commandTxt;
					System.out.println(command);
					bet = runExec.RunBVAppend(command);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bet;
		}
	}

	public String segmentLive(String mountName, String m3u8_beginmessage, String m3u8_endmessage, String recordPath,
			String serverGroupId, String destGroupId, String ftppath) {
		String ret = "";
		String startFlv = m3u8_beginmessage.split(":")[0];
		String endFlv = m3u8_endmessage.split(":")[0];
		String startflvName = startFlv.substring(startFlv.lastIndexOf("/"), startFlv.length());
		String endflvName = endFlv.substring(endFlv.lastIndexOf("/"), endFlv.length());
		String startLong = startflvName.split("_")[1];
		String endLong = endflvName.split("_")[1];
		String sStartOffSet = m3u8_beginmessage.split(":")[1];
		String sEndOffSet = m3u8_endmessage.split(":")[1];

		int startOffSet = Math.round(Float.parseFloat(m3u8_beginmessage.split(":")[1]));
		int endOffSet = Math.round(Float.parseFloat(m3u8_endmessage.split(":")[1]));
		long startSeq = new Long(getSeqFlv(startFlv, mountName) + startOffSet);
		long endSeq = new Long(getSeqFlv(endFlv, mountName) + endOffSet);
		String sStartTime = StringUtil.getCount2Time(startSeq, 2).replaceAll(":", "");
		String sEndTime = StringUtil.getCount2Time(endSeq, 2).replaceAll(":", "");
		String liveDir = com.jwzt.common.SomsConfigInfo.get("liveDir") + "/" + mountName;
		Date date = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("HHmmssS");
		SimpleDateFormat format3 = new SimpleDateFormat("HHmmssS");
		String ymd = format1.format(date);
		String hms = format2.format(date);

		File liveMountFilePath = new File(liveDir);
		if (liveMountFilePath.exists()) {
			File[] list = liveMountFilePath.listFiles();
			for (File dir : list) {
				if (dir.isDirectory()) {
					String sIndexFile = dir.getPath() + "/";
					// 节目开始日期的index文件
					String startTimeIndexFile = sIndexFile + startFlv.substring(0, startFlv.indexOf("/"));
					startTimeIndexFile += "_index.txt";

					// 节目结束日期的index文件
					String endTimeIndexFile = sIndexFile + endFlv.substring(0, endFlv.indexOf("/"));
					endTimeIndexFile += "_index.txt";

					File startIndexFile = new File(startTimeIndexFile);
					File endIndexFile = new File(endTimeIndexFile);
					if (startIndexFile.exists()) {
						String sDesFile = recordPath + "/" + startLong + "_" + endLong + "_" + dir.getName() + ".mp4";
						long fileNameHead = System.currentTimeMillis();
						String cacheDir = SomsConfigInfo.getHomePath() + "/cache/" + mountName + "/" + fileNameHead
								+ "_" + dir.getName() + ".mp4";
						String ftpDir = ftppath + "/" + mountName + "/seq/" + ymd;
						String ftpFileName = startLong + "_" + endLong + "_" + dir.getName() + ".mp4";
						try {
							TheardOut th = new TheardOut(startIndexFile, endIndexFile, dir.getPath(), sStartTime,
									sEndTime, sDesFile, cacheDir, serverGroupId, destGroupId, ftpDir, ftpFileName,
									sStartOffSet, sEndOffSet);
							th.start();
							ret += dir.getName() + ",";
						} catch (Exception e) {
							e.printStackTrace();
							Logger.log(e.getMessage(), 3);
						}
					}

				}
			}

		}
		return ret;
	}

	public int getSeqFlv(String flvPath, String mountName) {
		int time = 0;
		String liveDir = com.jwzt.common.SomsConfigInfo.get("liveDir") + "/" + mountName;
		File liveMountFilePath = new File(liveDir);
		if (liveMountFilePath.exists()) {
			File[] list = liveMountFilePath.listFiles();
			for (File dir : list) {
				if (dir.isDirectory()) {
					String format = "yyyyMMdd";
					String sIndexFile = dir.getPath() + "/";
					sIndexFile += flvPath.substring(0, flvPath.indexOf("/"));
					sIndexFile += "_index.txt";
					File fIndexFile = new File(sIndexFile);
					if (fIndexFile.exists()) {
						try {
							// 需要输出的文件列表

							FileInputStream fileInputStream = new FileInputStream(fIndexFile);
							InputStreamReader read = new InputStreamReader(fileInputStream);

							BufferedReader bufferedReader = new BufferedReader(read);
							String lineTxt = null;
							while ((lineTxt = bufferedReader.readLine()) != null) {
								String datetime = lineTxt.split("[|]")[0];
								String flvFile = lineTxt.split("[|]")[2];
								String hms = datetime.split("_")[1];
								if (flvFile.indexOf(flvPath) != -1) {
									int h = Integer.parseInt(hms.substring(0, 2));
									int m = Integer.parseInt(hms.substring(2, 4));
									int s = Integer.parseInt(hms.substring(4, 6));
									time = h * 3600 + m * 60 + s;
								}
							}
							read.close();
						} catch (Exception e) {
							e.printStackTrace();
							Logger.log(e.getMessage(), 3);
						}
					}
				}
			}
		}
		return time;
	}

	public boolean recordSeq(File fIndexFile, String sLiveMounthPath, String startFlv, String endFlv, String cacheDir,
			int startOffSet, int endOffSet) {
		boolean ret = false;
		List<String> recordFileList = new ArrayList<String>();
		try {
			// 需要输出的文件列表

			FileInputStream fileInputStream = new FileInputStream(fIndexFile);
			InputStreamReader read = new InputStreamReader(fileInputStream);

			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String flvFile = lineTxt.split("[|]")[2];
				recordFileList.add(sLiveMounthPath + "/" + flvFile);
			}
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(e.getMessage(), 3);
		}
		int startFlvIndex = 0;
		int endFlvIndex = 0;
		for (int i = 0; i < recordFileList.size(); i++) {
			if (recordFileList.get(i).indexOf(startFlv) != -1) {
				startFlvIndex = i;
			}
			if (recordFileList.get(i).indexOf(endFlv) != -1) {
				endFlvIndex = i;
			}
		}
		recordFileList = recordFileList.subList(startFlvIndex, endFlvIndex + 1);
		System.out.println("输出处理后分段文件列表::");
		for (int i = 0; i < recordFileList.size(); i++) {
			System.out.println(recordFileList.get(i));
		}
		System.out.println("输出处理后分段文件列表结束");
		// 生成输出文件
		if (startOffSet != -1) {
			if (startOffSet == 0)
				startOffSet = -1;
			creatVodFile(recordFileList, startOffSet, endOffSet, cacheDir, "", "");
		}

		return ret;

	}

	public String FileProgram(Integer channelId, String ymd) {
		ProgramMgr programMgr = new ProgramMgr();
		ChannelMgr channelMgr = new ChannelMgr();
		ChannelInfo channelInfo = channelMgr.getChannelInfo(channelId);
		String mountName = channelInfo.getChannel_mount_name();
		List<ProgramInfo> programList = programMgr.getProListInDay("", channelId, -1, true);
		// 目前虚拟直播 定死码流为800K目录
		// 目录结构为 live/live201/800k/20150408/
		String liveDir = com.jwzt.common.SomsConfigInfo.get("liveDir") + mountName + "\\800k\\" + ymd + "\\";
		File mountDir = new File(com.jwzt.common.SomsConfigInfo.get("liveDir") + mountName);
		File bandDir = new File(com.jwzt.common.SomsConfigInfo.get("liveDir") + mountName + "\\800k");
		File liveDirFile = new File(liveDir);
		if (!mountDir.exists()) {
			mountDir.mkdir();
		}
		if (!bandDir.exists()) {
			bandDir.mkdir();
		}
		boolean deleteRet = deleteFile(liveDirFile);
		if (!liveDirFile.exists()) {
			liveDirFile.mkdir();
		}
		if (deleteRet) {
			try {
				TheardDownloadOut th = new TheardDownloadOut(programList, liveDir, ymd);
				th.start();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log(e.getMessage(), 3);
			}
		}
		return "800k";
	}

	public boolean deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;

			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
		return true;
	}

	public void updateFileProgramM3U8(ChannelInfo channelInfo) {
		Date date = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		String ymd = format1.format(date);
		String mountName = channelInfo.getChannel_mount_name();
		File m3u8File = new File(com.jwzt.common.SomsConfigInfo.get("liveDir") + mountName + "\\800k\\tzwj_video.m3u8");
		File indexFile = new File(
				com.jwzt.common.SomsConfigInfo.get("liveDir") + mountName + "\\800k\\" + ymd + "_index.txt");
		if (!indexFile.exists()) {
			return;
		}
		if (!m3u8File.exists()) {
			try {
				m3u8File.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fileWriter = new FileWriter(m3u8File);
			BufferedWriter writer = new BufferedWriter(new FileWriter(m3u8File));
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			String nowHms = StringUtil.SQLTIMESTAMP2STRING("HHmmss", nowTime);
			List<String> recordList = readIndexFile(indexFile);
			String preline = "";
			String midline = "";
			String nextline = "";
			List<String> m3u8List = new ArrayList<String>();
			if (recordList == null || recordList.size() == 0) {
				writer.close();
				return;
			}
			for (int index = 0; index < recordList.size(); index++) {
				// 20150407_095634|11|20150407/09/live253_1428371794_video_seq_0.flv
				midline = recordList.get(index);
				String startHMS = midline.split("[|]")[0].split("_")[1];
				// 如果 其开始时间大于当前时间 找到其前一个 和后一个 并跳出查找 结束循环
				if (Integer.parseInt(startHMS) > Integer.parseInt(nowHms)) {
					if (index != 0) {
						preline = recordList.get(index - 1);
					}
					if (index + 1 < recordList.size()) {
						nextline = recordList.get(index + 1);
					}
					break;
				}
			}
			if (preline.length() != 0) {
				m3u8List.add(preline);
			}
			if (midline.length() != 0) {
				m3u8List.add(midline);
			}
			if (nextline.length() != 0) {
				m3u8List.add(nextline);
			}
			if (m3u8List.size() > 0) {
				int targetduration = 0;
				String firstId = "";
				for (String str : m3u8List) {
					targetduration += Integer.parseInt(str.split("[|]")[1]);
				}
				targetduration = targetduration / m3u8List.size();
				String flvStr = m3u8List.get(0).split("[|]")[2];
				firstId = flvStr.substring(flvStr.lastIndexOf("_") + 1, flvStr.lastIndexOf("."));
				fileWriter.write("");
				fileWriter.flush();
				fileWriter.close();
				writer.write("#EXTM3U");
				writer.write("\r\n");
				writer.write("#EXT-X-ALLOW-CACHE:NO");
				writer.write("\r\n");
				writer.write("#EXT-X-TARGETDURATION:" + targetduration);
				writer.write("\r\n");
				writer.write("#EXT-X-MEDIA-SEQUENCE:" + firstId);
				writer.write("\r\n");
				for (String str : m3u8List) {
					String duration = str.split("[|]")[1];
					String filepath = str.split("[|]")[2];
					filepath = filepath.replaceAll(".flv", ".ts");
					writer.write("#EXTINF:" + duration + ",");
					writer.write("\r\n");
					writer.write(filepath);
					writer.write("\r\n");
				}
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<String> readIndexFile(File indexFile) {
		List<String> recordList = new ArrayList<String>();
		try {
			FileInputStream fin = new FileInputStream(indexFile);
			BufferedReader dr = new BufferedReader(new InputStreamReader(fin));
			String linetxt = "";
			while ((linetxt = dr.readLine()) != null) {
				recordList.add(linetxt);
			}
			fin.close();
			dr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordList;
	}

}
