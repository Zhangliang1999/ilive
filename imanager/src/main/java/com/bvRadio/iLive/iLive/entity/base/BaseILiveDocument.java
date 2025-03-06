package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator
 * 文档直播关联表
 */
public class BaseILiveDocument {
	
	/**
	 * ID
	 */
	private Integer docLiveId;
	
	/**
	 * 直播场次
	 */
	private Long liveEventId;
	
	
	/**
	 * 直播文件ID
	 */
	private Long docFileId;


	public Integer getDocLiveId() {
		return docLiveId;
	}


	public void setDocLiveId(Integer docLiveId) {
		this.docLiveId = docLiveId;
	}


	public Long getLiveEventId() {
		return liveEventId;
	}


	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}


	public Long getDocFileId() {
		return docFileId;
	}


	public void setDocFileId(Long docFileId) {
		this.docFileId = docFileId;
	}
	
	
	
	
	
	
	

}
