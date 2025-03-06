package com.bvRadio.iLive.iLive.action.front.vo;

/**
 * 观看记录vo类
 * @author administrator
 */
public class AppViewRecordVo {
	
	// 观看类型 	1直播 	2回看
	private Integer viewType;
	
	/**
	 * 观看直播对象
	 */
	private AppILiveRoom appILiveRoom;
	
	
	/**
	 * 观看回看文件对象
	 */
	private AppMediaFile appMediaFile;
	
	/**
	 * 观看时间
	 * @return
	 */
	private String viewTime;
	
	private Long recordId;


	public Integer getViewType() {
		return viewType;
	}


	public void setViewType(Integer viewType) {
		this.viewType = viewType;
	}


	public AppILiveRoom getAppILiveRoom() {
		return appILiveRoom;
	}


	public void setAppILiveRoom(AppILiveRoom appILiveRoom) {
		this.appILiveRoom = appILiveRoom;
	}


	public AppMediaFile getAppMediaFile() {
		return appMediaFile;
	}


	public void setAppMediaFile(AppMediaFile appMediaFile) {
		this.appMediaFile = appMediaFile;
	}


	public String getViewTime() {
		return viewTime;
	}


	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}


	public Long getRecordId() {
		return recordId;
	}


	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	
	
	

}
