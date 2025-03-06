/*package com.bvRadio.iLive.iLive.task;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bvRadio.iLive.aliyunlive.LiveConfigs;
import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.entity.ILiveLive;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.factory.MsgIdFactoryBean;
import com.bvRadio.iLive.iLive.manager.ILiveIpAddressMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveMng;
import com.bvRadio.iLive.iLive.manager.ILiveMessageMng;
import com.bvRadio.iLive.iLive.manager.ILiveSensitiveWordMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.DownloadURL;
import com.bvRadio.iLive.iLive.util.FfmpegUtils;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.web.ApplicationCache;

public class TranscodingTask1 extends TimerTask {
	//String content, String ip,Integer liveId,String Url,String userName,Integer duration, Integer msgType,
	//CommonsMultipartFile file, HttpServletRequest request
	private String content;
	private String ip;
	private Integer liveId;
	private String Url;
	private String userName;
	private Integer duration;
	private Integer msgType;
	private File file;
	private  HttpServletRequest request;
	public TranscodingTask1(String content,String ip,Integer liveId,String Url,String userName,Integer duration,Integer msgType,File file,HttpServletRequest request) {
		this.content=content;
		this.ip = ip;
		this.liveId=liveId;
		this.Url=Url;
		this.userName=userName;
		this.duration=duration;
		this.msgType=msgType;
		this.file=file;
		this.request=request;
	}
	private Logger log = LogManager.getLogger();
	ILiveUploadServerMng uploadServerMng = (ILiveUploadServerMng) ApplicationContextUtil
			.getBean("iLiveUploadServerMng");
	ILiveMessageMng messageMng = (ILiveMessageMng) ApplicationContextUtil
			.getBean("iLiveMessageMng");
	ILiveLiveMng liveMng = (ILiveLiveMng) ApplicationContextUtil
			.getBean("iLiveLiveMng");
	ILiveIpAddressMng ipAddressMng = (ILiveIpAddressMng) ApplicationContextUtil
			.getBean("iLiveIpAddressMng");
	ILiveSensitiveWordMng sensitiveWordMng = (ILiveSensitiveWordMng) ApplicationContextUtil
			.getBean("iLiveSensitiveWordMng");

	public void run() {

		try {
			// System.out.println("源文件路径"+file.getAbsolutePath());
			String httpUrlString=null;
			ILiveUploadServer uploadServer = uploadServerMng.findDefaultSever();
			String uncompressFileExt = FileUtils.getFileExt(file.getName());
			String uploadFilePath = "/Interact"
					+ FileUtils.getTimeFilePath(file.getName());
			String tempFileName = (System.currentTimeMillis() / 1000) + ".mp4";
			String pathString=file.getAbsolutePath();
			
			File tempUpFile = new File(file.getAbsolutePath().substring(
					0,file.getAbsolutePath().lastIndexOf("/"))
					+ "/" + tempFileName);
			
			
			if (!tempUpFile.exists()) {
				tempUpFile.createNewFile();
			}
			// System.out.println("源文件路径"+file.getAbsolutePath());
			// System.out.println("转码后的路径"+tempUpFile.getAbsolutePath());
			boolean transcodeResult = FfmpegUtils.transcodeForVideo(file.getAbsolutePath(), tempUpFile.getAbsolutePath());
			if(transcodeResult){
				file.delete();
				DownloadURL url=new DownloadURL();
				FileInputStream in;
				in = new FileInputStream(tempUpFile.getAbsolutePath());
				uploadServer.setFtpPath("/oss/");
				boolean uploadResult = uploadServer.upload(uploadFilePath.substring(uploadFilePath.lastIndexOf("/")+1), in);
				// System.out.println("准备上传Oss");
				String ossurl=LiveConfigs.get(LiveConfigs.OSSURL);
				int index=uploadFilePath.lastIndexOf("/");
				String osspath=uploadFilePath.substring(1,index);
				String ossname=uploadFilePath.substring(index+1);
				ossurl=ossurl.replace(":name", ossname).replace(":url", osspath);
				String httpJosn=url.downloadUrl(ossurl);
				JSONObject object=new JSONObject(httpJosn);
				httpUrlString=(String) object.get("fileUrl");
				tempUpFile.delete();
			}

			// System.out.println("创建消息");
			MsgIdFactoryBean msgIdFacory = new MsgIdFactoryBean();
			String msgId = msgIdFacory.getObject();
			Timestamp createTime = new Timestamp(new Date().getTime());
			//2018.1.19新增
			Map<String,String> tempMap = new HashMap<String,String>();
			
			tempMap.put("senderName", userName);
			tempMap.put("msgType",msgType+"");
			tempMap.put("content",content);
			tempMap.put("createTime", createTime+"");
			tempMap.put("address", httpUrlString);
			net.sf.json.JSONObject json1=net.sf.json.JSONObject.fromObject(tempMap);
			String extra=json1.toString();
			//
			ILiveLive live =liveMng.findById(liveId);
			Boolean enableSensitiveword = live.getEnableSensitiveword();
			String replaceStr = content;
			if (null != enableSensitiveword && enableSensitiveword) {
				replaceStr = sensitiveWordMng.replaceSensitiveWord(content);
			}
			// System.out.println("图片路径"+httpUrlString);
			ILiveIpAddress ipAddress = ipAddressMng.getIpAddressByIp(ip);
			ILiveMessage message = new ILiveMessage(Long.parseLong(msgId),
					live, ipAddress, userName, content+"JWZTEXT"+httpUrlString, replaceStr+"JWZTEXT"+httpUrlString, msgType,
					createTime, 1, null,1,extra);
			
			message.setSenderImage(Url);
			message.setChecked(true);
			message.setDeleted(false);
			message.setSenderLevel(0);
			messageMng.save(message);
			// System.out.println("保存消息");
			// 后台新消息放到直播消息缓存
			Hashtable<Integer, List<ILiveMessage>> liveMsgMap = ApplicationCache
					.getLiveMsgMap();
			List<ILiveMessage> liveMsgList = liveMsgMap.get(liveId);
			if (null == liveMsgList) {
				liveMsgList = new ArrayList<ILiveMessage>();
				liveMsgMap.put(liveId, liveMsgList);
			}
			try {
				liveMsgList.add(message);
			} catch (Exception e) {
				log.error("后台新消息放到直播消息缓存出错", e);
			}
			// 后台新消息放到用户缓存
			// System.out.println("消息放入用户缓存");
			Hashtable<Integer, Hashtable<String, UserBean>> userListMap = ApplicationCache
					.getUserListMap();
			Hashtable<String, UserBean> userMap = userListMap.get(liveId);
			if (null == userMap) {
				userMap = new Hashtable<String, UserBean>();
				userListMap.put(liveId, userMap);
			}
			Iterator<String> iterator = userMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				UserBean user = userMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				try {
					userMsgList.add(message);
				} catch (Exception e) {
					log.error("后台新消息放到用户缓存出错", e);
				}
			}
			// 后台新消息放到管理员缓存里
			Hashtable<Integer, Hashtable<Integer, UserBean>> adminListMap = ApplicationCache
					.getAdminListMap();
			Hashtable<Integer, UserBean> adminMap = adminListMap.get(liveId);
			if (null == adminMap) {
				adminMap = new Hashtable<Integer, UserBean>();
				adminListMap.put(liveId, adminMap);
			}
			Iterator<Integer> adminIterator = adminMap.keySet().iterator();
			while (adminIterator.hasNext()) {
				Integer key = adminIterator.next();
				UserBean user = adminMap.get(key);
				List<ILiveMessage> userMsgList = user.getMsgList();
				try {
					userMsgList.add(message);
				} catch (Exception e) {
					log.error("后台新消息放到管理员缓存出错", e);
				}
			}
			return;

			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
*/