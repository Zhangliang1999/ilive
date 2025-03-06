package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveLiveRoom;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * @author administrator 直播间
 */
public class ILiveLiveRoom extends BaseILiveLiveRoom implements java.io.Serializable {
	/**
	 * 直播类型：普通
	 */
	public static final Integer LIVE_TYPE_COMMON = 1;
	/**
	 * 直播类型：连麦
	 */
	public static final Integer LIVE_TYPE_MICROPHONE = 2;
	
	/**
	 * 抽奖活动
	 */
	private Set<BBSVoteActivity> bbsVoteActivities;

	/**
	 * 试用状态 0为试用 1为非试用
	 */
	private Integer useState;

	public Set<BBSVoteActivity> getBbsVoteActivities() {
		return bbsVoteActivities;
	}

	public void setBbsVoteActivities(Set<BBSVoteActivity> bbsVoteActivities) {
		this.bbsVoteActivities = bbsVoteActivities;
	}

	/**
	 */
	private static final long serialVersionUID = -8498362535664476769L;

	/**
	 * 播放器播放的流地址
	 */
	private String hlsAddr;

	/**
	 * 直播推流的地址
	 */
	private String rtmpAddr;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public JSONObject putLiveInJson(JSONObject liveJson) throws JSONException {
		if (null == liveJson) {
			liveJson = new JSONObject();
		}
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		liveJson.put("liveId", roomId);
		liveJson.put("roomId", roomId);
		if (this.getRtmpAddr() != null) {
			liveJson.put("rtmpAddr", this.getRtmpAddr());
		}
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {
			liveJson.put("liveEvent", liveEvent.toJsonObject());
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		return liveJson;
	}

	public JSONObject putLiveInJson(JSONObject liveJson, ILiveEvent liveEvent) throws JSONException {
		if (null == liveJson) {
			liveJson = new JSONObject();
		}
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Timestamp createTime = getCreateTime();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		Integer liveType = getLiveType();
		liveJson.put("liveId", roomId);
		liveJson.put("roomId", roomId);
		if (this.getRtmpAddr() != null) {
			liveJson.put("rtmpAddr", this.getRtmpAddr());
		}
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {

			liveJson.put("liveEvent", liveEvent.toJsonObject());
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		if (liveType != null) {
			liveJson.put("liveType", liveType);
		} else {
			liveJson.put("liveType", LIVE_TYPE_COMMON);
		}
		return liveJson;
	}

	public JSONObject putFullLiveInfJSon(ILiveLiveRoom iliveRoom) {
		JSONObject liveJson = new JSONObject();
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		liveJson.put("liveId", roomId);
		liveJson.put("roomId", roomId);
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {
			JSONObject eventObj = liveEvent.toFullJsonObject();
			liveJson.put("liveEvent", eventObj);
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		return liveJson;
	}

	public JSONObject putNewLiveInJson(ILiveLiveRoom iliveRoom) {
		JSONObject liveJson = new JSONObject();
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Integer useState = getUseState() == null ? 0 : getUseState();
		liveJson.put("useState", useState);
		if (this.getRtmpAddr() != null) {
			liveJson.put("rtmpAddr", rtmpAddr);
		}
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		Integer liveType = getLiveType();
		Integer roomType =getRoomType();
		String meetId =getMeetId();
		Integer meetPushId = getMeetPushRoomId();
		liveJson.put("liveId", roomId);
		liveJson.put("roomId", roomId);
		String hlsPlayUrl=getHlsPlayUrl();
		String rtmpPlayUrl=getRtmpPlayUrl();
		liveJson.put("hlsPlayUrl", hlsPlayUrl);
		liveJson.put("rtmpPlayUrl", rtmpPlayUrl);
		liveJson.put("ViewAddr", "http://zb.tv189.com/pc/play.html?roomId="+roomId);
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {
			JSONObject eventObj = liveEvent.toJsonObject();
			liveJson.put("liveEvent", eventObj);
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		if (liveType != null) {
			liveJson.put("liveType", liveType);
		} else {
			liveJson.put("liveType", LIVE_TYPE_COMMON);
		}
		if (getLiveDelay() != null){
			liveJson.put("liveDelay", getLiveDelay());
		} else {
			liveJson.put("liveDelay", 0);
		}
		if (roomType != null){
			liveJson.put("roomType", roomType);
		} else {
			liveJson.put("roomType", 0);
		}
		if (meetPushId != null){
			liveJson.put("meetPushId", meetPushId);
		}
		
		return liveJson;
	}
	public JSONObject putNewcommenLiveInJson(ILiveLiveRoom iliveRoom) {
		JSONObject liveJson = new JSONObject();
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Integer useState = getUseState() == null ? 0 : getUseState();
		liveJson.put("useState", useState);
		if (this.getRtmpAddr() != null) {
			liveJson.put("rtmpAddr", rtmpAddr);
		}
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		Integer liveType = getLiveType();
		Integer roomType =getRoomType();
		String meetId =getMeetId();
		Integer meetPushId = getMeetPushRoomId();
		liveJson.put("liveId", roomId);
		liveJson.put("roomId", roomId);
		String hlsPlayUrl=getHlsPlayUrl();
		String rtmpPlayUrl=getRtmpPlayUrl();
		liveJson.put("hlsPlayUrl", hlsPlayUrl);
		liveJson.put("rtmpPlayUrl", rtmpPlayUrl);
		liveJson.put("viewAddr", "http://zb.tv189.com/pc/play.html?roomId="+roomId);
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {
			JSONObject eventObj = liveEvent.toJsonObject();
			liveJson.put("liveEvent", eventObj);
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		if (liveType != null) {
			liveJson.put("liveType", liveType);
		} else {
			liveJson.put("liveType", LIVE_TYPE_COMMON);
		}
		if (getLiveDelay() != null){
			liveJson.put("liveDelay", getLiveDelay());
		} else {
			liveJson.put("liveDelay", 0);
		}
		if (roomType != null){
			liveJson.put("roomType", roomType);
		} else {
			liveJson.put("roomType", 0);
		}
		if (meetPushId != null){
			liveJson.put("meetPushId", meetPushId);
		}
		
		return liveJson;
	}
	/**
	 * 构建观看端的bean
	 * 
	 * @param iliveRoom
	 * @param iLiveEnterPrise
	 * @return
	 */
	public JSONObject toViewBean(ILiveLiveRoom iliveRoom, ILiveEvent liveEvent, ILiveEnterprise iLiveEnterPrise) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("roomId", iliveRoom.getRoomId());
		jsonObject.put("liveId", iliveRoom.getRoomId());
		jsonObject.put("openStatus", iliveRoom.getOpenStatus());
		JSONObject newJSonObject = liveEvent.toJsonObject();
		jsonObject.put("liveEvent", newJSonObject);
		jsonObject.put("liveType", iliveRoom.getLiveType());
		if (iLiveEnterPrise != null) {
			JSONObject enterrpriseObj = iLiveEnterPrise.toSimpleJsonObject();
			jsonObject.put("enterprise", enterrpriseObj);
		}
		return jsonObject;
	}

	public String getHlsAddr() {
		return this.hlsAddr;
	}

	public void setHlsAddr(String hlsAddr) {
		this.hlsAddr = hlsAddr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String rtmpToHLS(String pushStreamAddr) {
		if (pushStreamAddr != null) {
			pushStreamAddr = pushStreamAddr.replace("rtmp", "http");
			// rtmp://172.18.2.32:1935/live/live308_tzwj_5000k
			String[] split = pushStreamAddr.split("/");
			if (split.length > 0) {
				String suffix = split[2];
				String[] split2 = suffix.split("_");
				String append = split2[0] + "/" + split2[1] + "/" + split2[2] + "/tzwj_video.m3u8";
				return split[0] + split[1] + append;
			}
		}
		return null;
	}

	public Integer getUseState() {
		return useState;
	}

	public void setUseState(Integer useState) {
		this.useState = useState;
	}

	public String getRtmpAddr() {
		return rtmpAddr;
	}

	public void setRtmpAddr(String rtmpAddr) {
		this.rtmpAddr = rtmpAddr;
	}
	 /**
     * rtmp播放地址
     */
	private String rtmpPlayUrl;
	/**
	 * hls播放地址
	 */
	private String hlsPlayUrl;
	
	public String getHlsPlayUrl() {
		return hlsPlayUrl;
	}

	public void setHlsPlayUrl(String hlsPlayUrl) {
		this.hlsPlayUrl = hlsPlayUrl;
	}

	public String getRtmpPlayUrl() {
		return rtmpPlayUrl;
	}

	public void setRtmpPlayUrl(String rtmpPlayUrl) {
		this.rtmpPlayUrl = rtmpPlayUrl;
	}
	public JSONObject putPlayNewLiveInJson(ILiveLiveRoom room) {
		JSONObject liveJson = new JSONObject();
		Integer roomId = getRoomId();
		Integer openStatus = getOpenStatus();
		Integer useState = getUseState() == null ? 0 : getUseState();
		liveJson.put("useState", useState);
		if (this.getRtmpAddr() != null) {
			liveJson.put("rtmpAddr", rtmpAddr);
		}
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		Integer roomType =getRoomType();
		String meetId =getMeetId();
		Integer meetPushId = getMeetPushRoomId();
		liveJson.put("liveId", roomId);
		liveJson.put("ViewAddr", ConfigUtils.get("free_login_pc_play")+"?roomId="+roomId);
		liveJson.put("roomId", roomId);
		liveJson.put("liveType", room.getLiveType());
		if (openStatus != null) {
			liveJson.put("openStatus", openStatus);
		} else {
			liveJson.put("openStatus", 0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			liveJson.put("createTime", sdf.format(createTime));
		} catch (Exception e) {
			liveJson.put("createTime", "");
		}
		if (liveEvent != null) {
			JSONObject eventObj = liveEvent.toJsonObject();
			eventObj.put("viewPassword", liveEvent.getViewPassword() == null ? "" : liveEvent.getViewAddr());
			eventObj.put("pushMsgSwitch", liveEvent.getPushMsgSwitch() == null ? 0 : liveEvent.getPushMsgSwitch());
			liveJson.put("liveEvent", eventObj);
		} else {
			liveJson.put("liveEvent", new ILiveEvent());
		}
		if (affordMoney != null) {
			liveJson.put("affordMoney", affordMoney);
		} else {
			liveJson.put("affordMoney", 0);
		}
		if (managerId != null) {
			liveJson.put("managerId", managerId);
		} else {
			liveJson.put("managerId", 0);
		}
		if (!StringUtils.isBlank(createPerson)) {
			liveJson.put("createPerson", createPerson);
		} else {
			liveJson.put("createPerson", "");
		}
		if (liveViewNum != null) {
			liveJson.put("liveViewNum", liveViewNum);
		} else {
			liveJson.put("liveViewNum", 0);
		}
		if (serverGroupId != null) {
			liveJson.put("serverGroupId", serverGroupId);
		} else {
			liveJson.put("serverGroupId", 0);
		}
		if (getLiveDelay() != null) {
			liveJson.put("liveDelay", getLiveDelay());
		} else {
			liveJson.put("liveDelay", 0);
		}
		if (roomType != null){
			liveJson.put("roomType", roomType);
		} else {
			liveJson.put("roomType", 0);
		}
		if (meetPushId != null){
			liveJson.put("meetPushId", meetPushId);
		}
		return liveJson;
	}

}
