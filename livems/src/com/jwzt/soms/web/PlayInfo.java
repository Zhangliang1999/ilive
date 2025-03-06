package com.jwzt.soms.web;

import java.util.ArrayList;

public class PlayInfo 
{
	/**
	 * 文件的播放地址
	 */
	public String sAddress = "";
	
	/**
	 * 文件预览的播放地址
	 */
	public String sPreviewAddress = "";
	
	/**
	 * 是否需要加密  2：加密文件
	 */
	public int nIfDrm = 0;
	
	/**
	 * 是否分段 1：分段文件
	 */
	public int nIfSegment = 0;
	
	/**
	 * 分段文件的开始时间
	 */
	public int nStartTime = 0;
	
	/**
	 * 分段文件的结束时间
	 */
	public int nEndTime = 0;
	
    /**
     * 返回错误ID  0：正确   1：IP受限！  2：服务器忙！ 3：用户所在区域没有该文件！4: 频道信息错误!
     */
	public int nErrorId = 0;
    
	/**
	 * 错误的描述信息
	 */
	public String sErrorDesc = "";
    
    /**
     * 所用协议
     */
	public String sProtocol = "";
	
	/**
     * 预览所用协议
     */
	public String sPreviewProtocol = "";
    
	/**
	 * 播放器的类型 media ，real
	 */
	public String sPlayerType = "";
	
	/**
	 * 预览播放器的类型 media ，real
	 */
	public String sPreviewPlayerType = "";
    
	/**
	 * 该协议所支持的文件类型  如：wmv;asf;wma
	 */
	public String sFileType = "";
	
	/**
	 * 该协议所支持的预览文件类型  如：wmv;asf;wma
	 */
	public String sPreviewFileType = "";
    
	/**
	 * 所用端口
	 */
	public String sPort = "";
	
	/**
	 * 预览所用端口
	 */
	public String sPreviewPort = "";
    
    /**
     * <server_id></server_id><server_grp_id></server_grp_id><server_region_id></server_region_id>
     */
	public String sServerInfo = "";

    /**
     * 广告播放类表字符串
     */
	public String sAdTable = "";
    
	/**
	 * 是否有广告
	 */
	public boolean bUseAd = false;
	
	/**
	 * P2P_ID
	 */
	public int nP2PId = -1;
	
	/**
	 * Real_Id
	 */
	public int nRealId = -1;
	
	/**
	 * 文件名称
	 */
	public String sFileName = "";
	
	/**
	 * 文件所在区域ID
	 */
	public int nFileRegionId = -1;
	
	/**
	 * 访问用户所在区域ID
	 */
	public int nUserRegionId = -1;
	
	/**
	 * 服务器提供类型:	1：nUserRegionId 直接提供服务
					2：nUserRegionId 有内容，但是访问满，转到 nFileRegionId
					3：nUserRegionId 没有内容，转到 nFileRegionId
	 */
	public int nCdnServiceType = -1;
	
	/**
	 * 提供服务的发布点类型：0, 存储发布点; 1, 分发发布点
	 */
	public int nMountType = -1;
	public int nMountId =-1;
	
	/**
	 * p2p使用地址
	 */
	public String sP2PAddress = "";
	
	
	/**
	 * 保存Token的容器
	 */
	public static ArrayList sTokenContent = new ArrayList();
}
