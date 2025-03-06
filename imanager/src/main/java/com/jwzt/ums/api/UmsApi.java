package com.jwzt.ums.api;

import com.jwzt.common.AppTools;
import com.jwzt.common.Md5;
import com.jwzt.live4g.mgr.UmsMountInfo;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.jwzt.common.DownloadURL;

public class UmsApi {

	private String umsIP = "";
	private String umsPort = "";
	/* 认证串的数列号码 */
	private static int nSeq = 1;
	
	/**
	 * 
	 * @param umsIP UMSIP地址
	 * @param umsPort UMS端口
	 */
	public UmsApi(String umsIP , String umsPort){
		this.umsIP = umsIP;
		this.umsPort = umsPort;
	}
	
	public String getUmsInterfaceUrl(){
		return getUmsInterfaceUrl("ControlPublish");
	}
	
	public String getUmsInterfaceUrl(String command){
		String ret = "";
		ret = "http://"+umsIP+":"+umsPort+"/BV_UMSCMD?function="+command+"&"+getAuthString();		
		return ret;
	}
	

	
	private String getAuthString() {
		Calendar calendar = Calendar.getInstance();
		int nMonth = calendar.get(Calendar.MONTH) + 1;
		int nDay = calendar.get(Calendar.DAY_OF_MONTH);
		int nHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nMin = calendar.get(Calendar.MINUTE);
		int nSecond = calendar.get(Calendar.SECOND);

		long lTime = ((nMonth * 30) + nDay) * 24 * 3600 + nHour * 3600 + nMin * 60 + nSecond;
		// System.out.println("nMonth="+nMonth+" nDay="+nDay+" nHour="+nHour+" nMin="+nMin+" nSecond="+nSecond);
		// System.out.println("lTime="+lTime);
		String sTime = "" + lTime;

		nSeq = (nSeq + 1) % 10000;

		Md5 md5 = new Md5();
		StringBuffer sAuth = new StringBuffer();
		sAuth.append("auth=").append(AppTools.createRandomNum(4, 1)).append(sTime)
				.append(AppTools.createRandomNum(4, 1)).append("@").append(nSeq).append("@")
				.append(md5.getMD5ofStr(sTime + "jwzt" + nSeq)).append("");

		return sAuth.toString();
	}
	
	
	
	/**
	 * 获得所有正在直播的发布点
	 * @return null标识异常
	 */
	public List<UmsMountInfo> getUmsMountName(){
		List<UmsMountInfo> ret = null;
		try{
			Document document = null;
			
			 SAXBuilder builder = new SAXBuilder();
			 try
	         {
				 document = MessageInit.getPublishingListDOM();
	         }catch(Exception e){
	        	 e.printStackTrace();
	         }
			
			String umsInterface = getUmsInterfaceUrl();
			
			DownloadURL downloadURL = new DownloadURL();
			 String test=OperationXMLByJdom.doc2String(document);
			System.out.println("document:"+test);
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "UTF-8", document);
			
			Element retroot = retDocument.getRootElement();
			String status = retroot.getChild("status").getText();
			
			List<Element> publishList = retroot.getChildren("Publish");
			ret = new ArrayList();
			for(Element publish : publishList){
				
				UmsMountInfo umsMountInfo = new UmsMountInfo();
				umsMountInfo.setAppName(publish.getChildText("AppName"));
				umsMountInfo.setMountName(publish.getChildText("MountName"));
				umsMountInfo.setMuteAudio(publish.getChildText("MuteAudio"));
				ret.add(umsMountInfo);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 设置发布点垫片
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @param SBackupMove 垫片所在UMS系统本地文件地址/home/ums/ums.mp4
	 * @param SDelayTime 发布点延迟直播
	 * @param active 垫片是否生效
	 * @return
	 */
	public boolean setPublishAutoBackup(String SAppName , String SMountName ,String SBackupMove , String SDelayTime ,boolean active){
		boolean ret = false;
		
		try{
			Document document = null;
			
			 try
	         {
				 String Sactive = "0";
				 if(active){
					 Sactive = "1";
				 }
				 document = MessageInit.getPublishAutoBackupDOM(SAppName ,  SMountName , SBackupMove ,  SDelayTime , Sactive);
	         }catch(Exception e){
	        	 e.printStackTrace();
	         }
			
			String umsInterface = getUmsInterfaceUrl("LiveMgr");
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 删除发布点垫片设置
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean delPublishAutoBackup(String SAppName , String SMountName){
		boolean ret = false;
		
		try{
			Document document = null;
			
			try
			{
				String Sactive = "3";
				document = MessageInit.getPublishAutoBackupDOM(SAppName ,  SMountName , "" ,  "0" , Sactive);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			String umsInterface = getUmsInterfaceUrl("LiveMgr");
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 设置发布点拉远程流
	 * @param SAppName 应用名
	 * @param SMountName  发布点
	 * @param SSrcAddr 远程流地址
	 * @return
	 */
	public boolean SetRelayAddr(String SAppName , String SMountName ,String SSrcAddr){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getRelayAddrDOM(SAppName, SMountName, SSrcAddr);
			
			String umsInterface = getUmsInterfaceUrl("SetRelayAddr");
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	/**
	 * 设置发布点拉远程流--第三方推流
	 * @param SAppName 应用名
	 * @param SMountName  发布点
	 * @param SSrcAddr 远程流地址
	 * @return
	 */
	public boolean SetRelayAddrThird(String SChannelID,String SAppName , String SMountName ,String SSrcAddr){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getRelayAddrDOM1(SChannelID,SAppName, SMountName, SSrcAddr);
            String test=OperationXMLByJdom.doc2String(document);
			System.out.println("document:"+test);
			String umsInterface = getUmsInterfaceUrl("StreamDistribute");
			DownloadURL downloadURL = new DownloadURL();
			System.out.println("通知ums");
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			if(retDocument==null) {
				System.out.println("返回信息为空");
				return ret;
			}else {
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			System.out.println("msg:"+retroot.getChild("msg").getText());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	/**
	 * 设置发布点开关
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @param SAction 0关闭发布点 1打开发布点
	 * @param SDuration 发布点自动开启时间秒数，-1为不自动开启
	 * @return
	 */
	public boolean setAllowPublish(String SAppName , String SMountName ,String SAction , String SDuration){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getAllowPublish(SAppName, SMountName, SAction, SDuration);
			
			String umsInterface = getUmsInterfaceUrl();
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * 关闭发布点
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean closeMount(String SAppName , String SMountName){
		return setAllowPublish(SAppName,SMountName,"0","-1");
	}
	
	/**
	 * 打开发布点
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean openMount(String SAppName , String SMountName){
		return setAllowPublish(SAppName,SMountName,"1","-1");
	}
	
	/**
	 * 设置发布点播放开关
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @param SAction 0关闭发布点 1打开发布点
	 * @return
	 */
	public boolean setAllowPlay(String SAppName , String SMountName ,String SAction ){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getAllowPlay(SAppName, SMountName, SAction);
			
			String umsInterface = getUmsInterfaceUrl();
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			DownloadURL downloadURL = new DownloadURL();
			String umsInterface = getUmsInterfaceUrl();
			System.out.println("%%"+downloadURL.downloadUrl(umsInterface, "POST", "GBK")+"%%");
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	/**
	 * 关闭发布点播放
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean closeMountPlay(String SAppName , String SMountName){
		return setAllowPlay(SAppName,SMountName,"0");
	}
	
	/**
	 * 打开发布点播放
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean openMountPlay(String SAppName , String SMountName){
		return setAllowPlay(SAppName,SMountName,"1");
	}
	
	/**
	 * 设置发布点播放开关
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @param SAction 0关闭发布点 1打开发布点
	 * @return
	 */
	public boolean setMuteAudio(String SAppName , String SMountName ,String SAction ){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getMuteAudio(SAppName, SMountName, SAction);
			
			String umsInterface = getUmsInterfaceUrl();
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	/**
	 * 关闭发布点播放
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean openAudio(String SAppName , String SMountName){
		return setMuteAudio(SAppName,SMountName,"0");
	}
	
	/**
	 * 打开发布点播放
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @return
	 */
	public boolean muteAudio(String SAppName , String SMountName){
		return setMuteAudio(SAppName,SMountName,"1");
	}
	
	/**
	 * 设置发布点播放开关
	 * @param SAppName 应用名
	 * @param SMountName 发布点名
	 * @param SAction 0关闭发布点 1打开发布点
	 * @return
	 */
	public boolean resetUMS(){
		boolean ret = false;
		
		try{
			Document document = null;
			
			document = MessageInit.getReset();
			
			String umsInterface = getUmsInterfaceUrl("RestartUMS");
			
			DownloadURL downloadURL = new DownloadURL();
			
			Document retDocument = downloadURL.downloadUrlWithXML(umsInterface, "POST", "GBK", document);
			
			Element retroot = retDocument.getRootElement();
			if("1".equals(retroot.getChild("status").getText())){
				ret = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	
	
	
}
