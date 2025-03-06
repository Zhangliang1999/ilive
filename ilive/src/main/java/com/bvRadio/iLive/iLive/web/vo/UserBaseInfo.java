package com.bvRadio.iLive.iLive.web.vo;

import org.json.JSONObject;

import com.bvRadio.iLive.iLive.util.SignUtils;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

public class UserBaseInfo {

	private Long time;

	private String ip;

	private String sign;

	private Integer type;

	private Integer roomId;

	private Long liveEventId;
	
	private Long enterpriseId;

	private String userId;
	
	private Long fileId;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		long time = System.currentTimeMillis();
		json.put("time", time);
		json.put("ip", StringPatternUtil.convertEmpty(this.getIp()));
		json.put("type", this.getType() == null ? 0 : this.getType());
		json.put("roomId", this.getRoomId() == null ? 0 : this.getRoomId());
		json.put("liveEventId", this.getLiveEventId() == null ? 0 : this.getLiveEventId());
		json.put("fileId", this.getFileId() == null ? 0 : this.getFileId());
		json.put("enterpriseId", this.getEnterpriseId() == null ? 0 : this.getEnterpriseId());
		json.put("userId", StringPatternUtil.convertEmpty(userId));
		String sign = SignUtils.getMD5(time + "__jwzt_chinaNet");
		json.put("sign", sign);
		return json;
	}

}
