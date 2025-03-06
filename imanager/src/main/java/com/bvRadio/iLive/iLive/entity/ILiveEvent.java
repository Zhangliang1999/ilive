package com.bvRadio.iLive.iLive.entity;

import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveEvent;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

@SuppressWarnings("serial")
public class ILiveEvent extends BaseILiveEvent implements java.io.Serializable {

	private String converAddrHttp;

	private String playBgAddrHttp;

	private String diyfromAddr;
	
	/**
	 * 是否推送消息向粉丝 1是 0否
	 */
	private Integer pushMsgSwitch;

	/**
	 * 自定义菜单
	 */
	List<PageDecorate> pageRecordList;

	/**
	 * 关联企业
	 */
	ILiveEnterprise enterprise;

	/**
	 * 视频个数
	 */
	private Integer fileSize;

	public String getConverAddrHttp() {
		return converAddrHttp;
	}

	public void setConverAddrHttp(String converAddrHttp) {
		this.converAddrHttp = converAddrHttp;
	}

	public String getPlayBgAddrHttp() {
		return playBgAddrHttp;
	}

	public void setPlayBgAddrHttp(String playBgAddrHttp) {
		this.playBgAddrHttp = playBgAddrHttp;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public List<PageDecorate> getPageRecordList() {
		return pageRecordList;
	}

	public void setPageRecordList(List<PageDecorate> pageRecordList) {
		this.pageRecordList = pageRecordList;
	}

	public ILiveEnterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(ILiveEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	// 直播简介
	private static final Integer LIVE_DESC_TYPE = 4;

	public JSONObject toJsonObject() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("liveEventId", this.getLiveEventId());
		jsonObj.put("liveTitle", StringPatternUtil.convertEmpty(this.getLiveTitle()));
		jsonObj.put("liveDesc", StringPatternUtil.convertEmpty(this.getLiveDesc()));
		jsonObj.put("liveStartTime", sdf.format(this.getLiveStartTime()));
		jsonObj.put("liveStatus", this.getLiveStatus());
		jsonObj.put("converAddr", StringPatternUtil.convertEmpty(this.getConverAddr()));
		jsonObj.put("hostname", StringPatternUtil.convertEmpty(this.getHostName()));
		jsonObj.put("openLogoSwitch", this.getOpenLogoSwitch() == null ? 0 : this.getOpenLogoSwitch());
		jsonObj.put("openSignupSwitch", this.getOpenSignupSwitch());
		jsonObj.put("openGuideSwitch", this.getOpenGuideSwitch());
		jsonObj.put("openCountdownSwitch", this.getOpenCountdownSwitch());
		jsonObj.put("openViewNumSwitch", this.getOpenViewNumSwitch());
		jsonObj.put("countdownTitle", this.getCountdownTitle());
		jsonObj.put("guideAddr", this.getGuideAddr());
		jsonObj.put("playBgAddr", StringPatternUtil.convertEmpty(this.getPlayBgAddr()));
		jsonObj.put("viewAuthorized", this.getViewAuthorized());
		jsonObj.put("welcomeMsg", this.getWelcomeMsg());
		jsonObj.put("diyformAddr", this.getDiyfromAddr());
		jsonObj.put("logoUrl", StringPatternUtil.convertEmpty(this.getLogoUrl()));
		jsonObj.put("logoPosition", this.getLogoPosition() == null ? 0 : this.getLogoPosition());
		List<PageDecorate> pageRecordList = this.getPageRecordList();
		if (pageRecordList != null) {
			JSONArray Jarr = new JSONArray();
			for (PageDecorate decorate : pageRecordList) {
				if (decorate.getMenuType().equals(LIVE_DESC_TYPE)) {
					if (decorate.getRichContent() != null && !"".equals(decorate.getRichContent().trim())) {
						jsonObj.put("liveDesc", decorate.getRichContent());
					}
				}
				JSONObject jsonOj = new JSONObject();
				jsonOj.put("menuName", decorate.getMenuName());
				jsonOj.put("menuType", decorate.getMenuType());
				jsonOj.put("menuOrder", decorate.getMenuOrder());
				Jarr.add(jsonOj);
			}
			jsonObj.put("pageDecorate", Jarr);
		}
		return jsonObj;
	}

	public JSONObject toFullJsonObject() {
		JSONObject jsonObj = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonObj.put("liveEventId", this.getLiveEventId());
		jsonObj.put("liveTitle", this.getLiveTitle());
		jsonObj.put("liveDesc", this.getLiveDesc());
		jsonObj.put("liveStartTime", sdf.format(this.getLiveStartTime()));
		jsonObj.put("liveStatus", this.getLiveStatus());
		jsonObj.put("converAddr", this.getConverAddr());
		jsonObj.put("hostname", this.getHostName());
		return jsonObj;
	}

	public String getDiyfromAddr() {
		return diyfromAddr;
	}

	public void setDiyfromAddr(String diyfromAddr) {
		this.diyfromAddr = diyfromAddr;
	}

	public Integer getPushMsgSwitch() {
		return pushMsgSwitch;
	}

	public void setPushMsgSwitch(Integer pushMsgSwitch) {
		this.pushMsgSwitch = pushMsgSwitch;
	}
	
	
	

}
