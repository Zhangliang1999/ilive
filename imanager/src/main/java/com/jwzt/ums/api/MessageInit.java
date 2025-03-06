package com.jwzt.ums.api;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

public class MessageInit {

	/**
	 * 获得列发布点消息DOM
	 * @return
	 */
	public static Document getPublishingListDOM(){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("GetPublishingList");
		root.addContent(CMD);
		Document document = new Document(root);
		return document;
	}
	
	
	/**
	 * 获得设置发布点延迟和垫片消息DOM
	 * @param SAppName 引用名
	 * @param SMountName 发布点名
	 * @param SBackupMove 垫片文件路径
	 * @param SDelayTime 延迟播出秒数
	 * @param SAction 1:垫片生效 ， 0:垫片失效
	 * @return
	 */
	public static Document getPublishAutoBackupDOM(String SAppName , String SMountName , String SBackupMove , String SDelayTime , String SAction ){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("PublishAutoBackup");
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element BackupMove = new Element("BackupMove");
		BackupMove.setText(SBackupMove);
		Element DelayTime = new Element("DelayTime");
		DelayTime.setText(SDelayTime);
		Element Action = new Element("Action");
		Action.setText(SAction);
		
		root.addContent(CMD);
		root.addContent(AppName);
		root.addContent(MountName);
		root.addContent(BackupMove);
		root.addContent(DelayTime);
		root.addContent(Action);
		
		Document document = new Document(root);
		return document;
	}
	
	/**
	 * 获得设置发布点拉流消息DOM
	 * @param SAppName 应用名
	 * @param SMountName 发布点
	 * @param SSrcAddr RTMP远程流
	 * @return
	 */
	public static Document getRelayAddrDOM(String SAppName , String SMountName , String SSrcAddr ){
		
		Element root = new Element("root");
		Element RelayMap = new Element("RelayMap");
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element SrcAddr = new Element("SrcAddr");
		SrcAddr.setText(SSrcAddr);
		
		RelayMap.addContent(AppName);
		RelayMap.addContent(MountName);
		RelayMap.addContent(SrcAddr);
		root.addContent(RelayMap);
		
		Document document = new Document(root);
		return document;
		
	}
	/**
	 * 获得设置发布点拉流消息DOM--test
	 * @param SAppName 应用名
	 * @param SMountName 发布点
	 * @param SSrcAddr RTMP远程流
	 * @return
	 */
	public static Document getRelayAddrDOM1(String SChannelID, String SAppName , String SMountName , String SRtmpAddr){
		
		Element root = new Element("root");
		Element OutputUdp = new Element("OutputUdp");
		Element ChannelID = new Element("ChannelID");
		ChannelID.setText(SChannelID);
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element RtmpAddr = new Element("RtmpAddr");
		CDATA cdata = new CDATA(SRtmpAddr);
		RtmpAddr.addContent(cdata);
		Element UdpAddr = new Element("UdpAddr");
		UdpAddr.setText("");
		Element TSSendCBR = new Element("TSSendCBR");
		TSSendCBR.setText("1");
		Element TSSendNULLKb = new Element("TSSendNULLKb");
		TSSendNULLKb.setText("500");
		Element TSSendTTL = new Element("TSSendTTL");
		TSSendTTL.setText("10");
		Element TSSendBindIP = new Element("TSSendBindIP");
		TSSendBindIP.setText("");
		Element TSFixBitrate = new Element("TSFixBitrate");
		TSFixBitrate.setText("1000");
		Element PMTID = new Element("PMTID");
		PMTID.setText("258");
		Element VideoPID = new Element("VideoPID");
		VideoPID.setText("515");
		Element AudioPID = new Element("AudioPID");
		AudioPID.setText("662");
		Element PCRID = new Element("PCRID");
		PCRID.setText("8190");
		Element PCRPeriodMS = new Element("PCRPeriodMS");
		PCRPeriodMS.setText("30");
		OutputUdp.addContent(ChannelID);
		OutputUdp.addContent(AppName);
		OutputUdp.addContent(MountName);
		OutputUdp.addContent(RtmpAddr);
		OutputUdp.addContent(UdpAddr);
		OutputUdp.addContent(TSSendCBR);
		OutputUdp.addContent(TSSendNULLKb);
		OutputUdp.addContent(TSSendTTL);
		OutputUdp.addContent(TSSendBindIP);
		OutputUdp.addContent(TSFixBitrate);
		OutputUdp.addContent(PMTID);
		OutputUdp.addContent(VideoPID);
		OutputUdp.addContent(AudioPID);
		OutputUdp.addContent(PCRID);
		OutputUdp.addContent(PCRPeriodMS);
		root.addContent(OutputUdp);
		
		Document document = new Document(root);
		return document;
		
	}
	/**
	 * 获得设置发布点开关消息DOM
	 * @param SAppName
	 * @param SMountName
	 * @param SAction
	 * @param SDuration
	 * @return
	 */
	public static Document getAllowPublish(String SAppName , String SMountName , String SAction , String SDuration ){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("AllowPublish");
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element Action = new Element("Action");
		Action.setText(SAction);
		Element Duration = new Element("Duration");
		Duration.setText(SDuration);
		
		root.addContent(CMD);
		root.addContent(AppName);
		root.addContent(MountName);
		root.addContent(Action);
		root.addContent(Duration);
		
		Document document = new Document(root);
		return document;
		
	}
	
	/**
	 * 获得设置发布点播放开关消息DOM
	 * @param SAppName
	 * @param SMountName
	 * @param SAction
	 * @return
	 */
	public static Document getAllowPlay(String SAppName , String SMountName , String SAction ){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("AllowPlay");
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element Action = new Element("Action");
		Action.setText(SAction);
		
		root.addContent(CMD);
		root.addContent(AppName);
		root.addContent(MountName);
		root.addContent(Action);
		
		Document document = new Document(root);
		return document;
	}
	
	/**
	 * 获得设置发布点静音开关消息DOM
	 * @param SAppName
	 * @param SMountName
	 * @param SAction
	 * @return
	 */
	public static Document getMuteAudio(String SAppName , String SMountName , String SAction ){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("MuteAudio");
		Element AppName = new Element("AppName");
		AppName.setText(SAppName);
		Element MountName = new Element("MountName");
		MountName.setText(SMountName);
		Element Action = new Element("Action");
		Action.setText(SAction);
		
		root.addContent(CMD);
		root.addContent(AppName);
		root.addContent(MountName);
		root.addContent(Action);
		
		Document document = new Document(root);
		return document;
	}
	
	/**
	 * 获得重启消息DOM
	 * @param SAppName
	 * @param SMountName
	 * @param SAction
	 * @return
	 */
	public static Document getReset( ){
		
		Element root = new Element("root");
		Element CMD = new Element("CMD");
		CMD.setText("Reset");
		root.addContent(CMD);
		Document document = new Document(root);
		return document;
	}

}
