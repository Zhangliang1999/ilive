package com.bvRadio.iLive.iLive.entity.vo;
/**
 * 直播间操作类型
 * @author YanXL
 *
 */
public class ILiveEventVo {
	/**
	 * 审核时长 s
	 */
	private Integer checkedTime;
	/**
	 * 全局禁言类型:  0 开启  1 关闭  
	 */
	private  Integer estoppleType;
	/**
	 * 直播状态：
	 * 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
	 */
	private Integer liveStatus;
	/**
	 *直播控制播放类型 : 
	 * 1 推流直播 2 拉流直播 3 列表直播 4 PC手机桌面直播 5 手机直播   6 导播台
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
	
}
