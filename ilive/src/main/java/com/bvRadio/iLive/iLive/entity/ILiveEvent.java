package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveEvent;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

@SuppressWarnings("serial")
public class ILiveEvent extends BaseILiveEvent implements java.io.Serializable {
	/**
	 * 观看授权方式：公开观看
	 */
	public final static Integer VIEW_AUTHORIZED_FREE = 1;
	/**
	 * 观看授权方式：密码观看
	 */
	public final static Integer VIEW_AUTHORIZED_PASSWORD = 2;
	/**
	 * 观看授权方式：付费观看
	 */
	public final static Integer VIEW_AUTHORIZED_PAY = 3;
	/**
	 * 观看授权方式：白名单观看
	 */
	public final static Integer VIEW_AUTHORIZED_WHITE_LIST = 4;
	/**
	 * 观看授权方式：登录观看
	 */
	public final static Integer VIEW_AUTHORIZED_LOGIN = 5;
	/**
	 * 观看授权方式：F码观看
	 */
	public final static Integer VIEW_AUTHORIZED_FCODE = 6;
	/**
	 * 观看授权方式：第三方授權观看
	 */
	public final static Integer VIEW_AUTHORIZED_THIRD = 7;
	
	private String converAddrHttp;

	private String playBgAddrHttp;

	private String diyfromAddr;

	/**
	 * app端的报名表单
	 */
	private String appDiyformAddr;

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

	/**
	 * 视频随机收录开始时间
	 * 
	 * @return
	 */
	private Timestamp randomRecordStartTime;

	/**
	 * 视频随机收录结束时间
	 * 
	 * @return
	 */
	private Timestamp randomRecordEndTime;

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
		jsonObj.put("openFCodeSwitch", this.getOpenFCodeSwitch());
		jsonObj.put("countdownTitle", this.getCountdownTitle());
		jsonObj.put("guideAddr", this.getGuideAddr());
		jsonObj.put("playBgAddr", StringPatternUtil.convertEmpty(this.getPlayBgAddr()));
		jsonObj.put("viewAuthorized", this.getViewAuthorized());
		jsonObj.put("authPassword", this.getViewPassword());
		jsonObj.put("viewMoney", this.getViewMoney());
		jsonObj.put("welcomeMsg", this.getWelcomeMsg());
		jsonObj.put("diyformAddr", this.getDiyfromAddr());
		jsonObj.put("appDiyformAddr", this.getAppDiyformAddr());
		jsonObj.put("logoUrl", StringPatternUtil.convertEmpty(this.getLogoUrl()));
		jsonObj.put("logoPosition", this.getLogoPosition() == null ? 0 : this.getLogoPosition());
		jsonObj.put("estoppelType", this.getEstoppelType());
		jsonObj.put("isSign", this.getIsSign() == null ? 0 : this.getIsSign());
		jsonObj.put("documentId", this.getDocumentId() == null ? 0 : this.getDocumentId());
		jsonObj.put("documentDownload", this.getDocumentDownload() == null ? 0 : this.getDocumentDownload());
		jsonObj.put("documentPageNO", this.getDocumentPageNO() == null ? 1 : this.getDocumentPageNO());
		jsonObj.put("documentManual", this.getDocumentManual() == null ? 0 : this.getDocumentManual());
		jsonObj.put("needLogin", this.getNeedLogin() == null ? 0 : this.getNeedLogin());
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
				jsonOj.put("richContent", decorate.getRichContent() == null ? "" : decorate.getRichContent());
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
		jsonObj.put("isSign", this.getIsSign() == null ? 0 : this.getIsSign());
		jsonObj.put("documentId", this.getDocumentId() == null ? 0 : this.getDocumentId());
		jsonObj.put("documentDownload", this.getDocumentDownload() == null ? 0 : this.getDocumentDownload());
		jsonObj.put("documentPageNO", this.getDocumentPageNO() == null ? 1 : this.getDocumentPageNO());
		jsonObj.put("documentManual", this.getDocumentManual() == null ? 0 : this.getDocumentManual());
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

	public String getAppDiyformAddr() {
		return appDiyformAddr;
	}

	public void setAppDiyformAddr(String appDiyformAddr) {
		this.appDiyformAddr = appDiyformAddr;
	}

	public Timestamp getRandomRecordStartTime() {
		return randomRecordStartTime;
	}

	public void setRandomRecordStartTime(Timestamp randomRecordStartTime) {
		this.randomRecordStartTime = randomRecordStartTime;
	}

	public Timestamp getRandomRecordEndTime() {
		return randomRecordEndTime;
	}

	public void setRandomRecordEndTime(Timestamp randomRecordEndTime) {
		this.randomRecordEndTime = randomRecordEndTime;
	}

}
