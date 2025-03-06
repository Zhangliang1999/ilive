package com.bvRadio.iLive.iLive.entity.base;

/**
 * @author administrator
 * 白名单
 */
public abstract class BaseILiveViewWhiteBill {
	
	// 名单ID
	private Long billId;
	
	// 直播房间Id
	private Long liveEventId;
	
	// 用户手机号
	private String phoneNum;
	
	//用户姓名
	private String userName;
	
	//用户ID
	private Long userId;
	

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}


	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

}
