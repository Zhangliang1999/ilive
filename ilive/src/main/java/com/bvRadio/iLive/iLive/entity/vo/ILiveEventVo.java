package com.bvRadio.iLive.iLive.entity.vo;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 直播间操作类型
 * 
 * @author YanXL
 *
 */
public class ILiveEventVo implements Serializable{
	/**
	 * 审核时长 s
	 */
	private Integer checkedTime;
	/**
	 * 全局禁言类型: 0 开启 1 关闭
	 */
	private Integer estoppleType;
	/**
	 * 直播状态： 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
	 */
	private Integer liveStatus;
	/**
	 * 直播控制播放类型 : 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播 6 导播台
	 */
	private Integer playType;
	/**
	 * 流地址
	 */
	private String hlsUrl;
	/**
	 * 图片
	 */
	private String playBgAddr;

	public Integer getCheckedTime() {
		return checkedTime;
	}

	public void setCheckedTime(Integer checkedTime) {
		this.checkedTime = checkedTime;
	}

	public Integer getEstoppleType() {
		return estoppleType;
	}

	public void setEstoppleType(Integer estoppleType) {
		this.estoppleType = estoppleType;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}

	public Integer getPlayType() {
		return playType;
	}

	public void setPlayType(Integer playType) {
		this.playType = playType;
	}

	public ILiveEventVo() {
		super();
	}

	public String getHlsUrl() {
		return hlsUrl;
	}

	public void setHlsUrl(String hlsUrl) {
		this.hlsUrl = hlsUrl;
	}

	public String getPlayBgAddr() {
		return playBgAddr;
	}

	public void setPlayBgAddr(String playBgAddr) {
		this.playBgAddr = playBgAddr;
	}

	public JSONObject toJSONObject() {
		JSONObject jobj = new JSONObject();
		Integer checkedTime2 = this.getCheckedTime();
		if(checkedTime2!=null){
			jobj.put("checkedTime", checkedTime2);	
		}else{
			jobj.put("checkedTime", -1);
		}
		Integer estoppleType2 = this.getEstoppleType();
		if(estoppleType2!=null){
			jobj.put("estoppleType", estoppleType2);
		}else{
			jobj.put("estoppleType", 0);
		}
		Integer liveStatus2 = this.getLiveStatus();
		if(liveStatus2!=null){
			jobj.put("liveStatus", liveStatus2);
		}else{
			jobj.put("liveStatus", 1);
		}
		Integer playType2 = this.getPlayType();
		if(playType2!=null){
			jobj.put("playType", playType2);
		}else{
			jobj.put("playType", 1);
		}
		String hlsUrl2 = this.getHlsUrl();
		if(hlsUrl2!=null){
			jobj.put("hlsUrl", hlsUrl2);
		}else{
			jobj.put("hlsUrl", "0");
		}
		String playBgAddr2 = this.getPlayBgAddr();
		if(playBgAddr2!=null){
			jobj.put("playBgAddr", playBgAddr2);
		}else{
			jobj.put("playBgAddr", "0");
		}
		return jobj;
	}

}
