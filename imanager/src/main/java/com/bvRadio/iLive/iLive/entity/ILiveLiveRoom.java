package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveLiveRoom;

/**
 * @author administrator 直播间
 */
public class ILiveLiveRoom extends BaseILiveLiveRoom {

	/**
	 * 抽奖活动
	 */
	private Set<BBSVoteActivity> bbsVoteActivities;

	/**
	 * 试用状态 0为试用 1为非试用
	 */
	private Integer useState;

	/**
	 * 创建人手机号
	 */
	private String phone;
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getPhone(){
		return phone;
	}
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
		if(this.getRtmpAddr()!=null) {
			liveJson.put("rtmpAddr", rtmpAddr);
		}
		Timestamp createTime = getCreateTime();
		ILiveEvent liveEvent = getLiveEvent();
		Double affordMoney = getAffordMoney();
		Long managerId = getManagerId();
		String createPerson = getCreatePerson();
		Long liveViewNum = getLiveViewNum();
		Integer serverGroupId = getServerGroupId();
		liveJson.put("liveId", roomId);
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
		return liveJson;
	}

	/**
	 * 构建观看端的bean
	 * 
	 * @param iliveRoom
	 * @param iLiveEnterPrise
	 * @return
	 */
	public JSONObject toViewBean(ILiveLiveRoom iliveRoom, ILiveEnterprise iLiveEnterPrise) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("roomId", iliveRoom.getRoomId());
		jsonObject.put("openStatus", iliveRoom.getOpenStatus());
		ILiveEvent liveEvent = iliveRoom.getLiveEvent();
		JSONObject newJSonObject = liveEvent.toJsonObject();
		jsonObject.put("liveEvent", newJSonObject);
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

}
