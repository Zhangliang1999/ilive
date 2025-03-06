package com.jwzt.statistic.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseLiveInfo implements java.io.Serializable {

	private Long id;
	private Integer roomId;
	private Long liveEventId;
	private String liveTitle;
	private String coverAddr;
	private Timestamp liveBeginTime;
	private Timestamp liveEndTime;
	private Timestamp createTime;
	private Boolean openDecorate;
	private Integer baseNum;
	private Integer multipleNum;
	private Integer enterpriseId;
	private String enterpriseName;
	private String enterpriseDesc;
	private String enterpriseImg;
	private Long ipCode;

	private Timestamp lastStatisticTime;
	private Boolean statisticing;

	private Integer isFinshed;
	
	public Integer getIsFinshed() {
		return isFinshed;
	}

	public void setIsFinshed(Integer isFinshed) {
		this.isFinshed = isFinshed;
	}

	public BaseLiveInfo() {
		super();
	}

	public BaseLiveInfo(Integer roomId, Long liveEventId, String liveTitle, String coverAddr, Boolean openDecorate,
			Integer baseNum, Integer multipleNum, Integer enterpriseId, String enterpriseName, String enterpriseDesc,
			String enterpriseImg) {
		super();
		this.roomId = roomId;
		this.liveEventId = liveEventId;
		this.liveTitle = liveTitle;
		this.coverAddr = coverAddr;
		this.openDecorate = openDecorate;
		this.baseNum = baseNum;
		this.multipleNum = multipleNum;
		this.enterpriseId = enterpriseId;
		this.enterpriseName = enterpriseName;
		this.enterpriseDesc = enterpriseDesc;
		this.enterpriseImg = enterpriseImg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public String getCoverAddr() {
		return coverAddr;
	}

	public void setCoverAddr(String coverAddr) {
		this.coverAddr = coverAddr;
	}

	public Timestamp getLiveBeginTime() {
		return liveBeginTime;
	}

	public void setLiveBeginTime(Timestamp liveBeginTime) {
		this.liveBeginTime = liveBeginTime;
	}

	public Timestamp getLiveEndTime() {
		return liveEndTime;
	}

	public void setLiveEndTime(Timestamp liveEndTime) {
		this.liveEndTime = liveEndTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getOpenDecorate() {
		return openDecorate;
	}

	public void setOpenDecorate(Boolean openDecorate) {
		this.openDecorate = openDecorate;
	}

	public Integer getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(Integer baseNum) {
		this.baseNum = baseNum;
	}

	public Integer getMultipleNum() {
		return multipleNum;
	}

	public void setMultipleNum(Integer multipleNum) {
		this.multipleNum = multipleNum;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseDesc() {
		return enterpriseDesc;
	}

	public void setEnterpriseDesc(String enterpriseDesc) {
		this.enterpriseDesc = enterpriseDesc;
	}

	public String getEnterpriseImg() {
		return enterpriseImg;
	}

	public void setEnterpriseImg(String enterpriseImg) {
		this.enterpriseImg = enterpriseImg;
	}

	public Timestamp getLastStatisticTime() {
		return lastStatisticTime;
	}

	public void setLastStatisticTime(Timestamp lastStatisticTime) {
		this.lastStatisticTime = lastStatisticTime;
	}

	public Long getIpCode() {
		return ipCode;
	}

	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}

	public Boolean getStatisticing() {
		return statisticing;
	}

	public void setStatisticing(Boolean statisticing) {
		this.statisticing = statisticing;
	}

}
