package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwzt.statistic.entity.base.BaseRequestLog;

@SuppressWarnings("serial")
public class RequestLog extends BaseRequestLog {
	/**
	 * 用户类型：未注册用户
	 */
	public static final Integer USER_TYPE_UNREGISTERED_USER = 1;
	/**
	 * 用户类型：已注册用户
	 */
	public static final Integer USER_TYPE_REGISTERED_USER = 2;
	/**
	 * 日志类型：用户进入直播间
	 */
	public static final Integer LOG_TYPE_USER_ENTER = 1;
	/**
	 * 日志类型：用户离开直播间
	 */
	public static final Integer LOG_TYPE_USER_LEAVE = 2;
	/**
	 * 日志类型：用户分享直播间
	 */
	public static final Integer LOG_TYPE_USER_SHARE = 3;
	/**
	 * 日志类型：用户点赞直播间
	 */
	public static final Integer LOG_TYPE_USER_LIKE = 4;
	/**
	 * 日志类型：用户在直播间发言
	 */
	public static final Integer LOG_TYPE_USER_COMMENT = 5;
	/**
	 * 日志类型：用户浏览
	 */
	public static final Integer LOG_TYPE_USER_VIEW = 6;
	/**
	 * 日志类型：用户关注
	 */
	public static final Integer LOG_TYPE_USER_CONCERN = 7;
	/**
	 * 日志类型：用户取消关注
	 */
	public static final Integer LOG_TYPE_USER_UNCONCERN = 8;
	/**
	 * 日志类型：用户签到
	 */
	public static final Integer LOG_TYPE_USER_SIGN = 9;
	/**
	 * 日志类型：直播开始
	 */
	public static final Integer LOG_TYPE_LIVE_BEGIN = 10;
	/**
	 * 日志类型：直播结束
	 */
	public static final Integer LOG_TYPE_LIVE_END = 11;
	/**
	 * 日志类型：用户送礼物
	 */
	public static final Integer LOG_TYPE_USER_GIFT = 12;
	/**
	 * 日志类型：用户打赏
	 */
	public static final Integer LOG_TYPE_USER_REWARD = 13;

	/**
	 * 请求类型：其他
	 */
	public static final Integer REQUEST_TYPE_OTHER = 0;
	/**
	 * 请求类型：ANDROID
	 */
	public static final Integer REQUEST_TYPE_ANDROID = 1;
	/**
	 * 请求类型：IOS
	 */
	public static final Integer REQUEST_TYPE_IOS = 2;
	/**
	 * 请求类型：PC
	 */
	public static final Integer REQUEST_TYPE_PC = 3;
	/**
	 * 请求类型：WAP
	 */
	public static final Integer REQUEST_TYPE_WAP = 4;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		Integer type = getRequestType();
		if (!RequestLog.REQUEST_TYPE_ANDROID.equals(type) && !RequestLog.REQUEST_TYPE_IOS.equals(type)
				&& !RequestLog.REQUEST_TYPE_PC.equals(type) && !RequestLog.REQUEST_TYPE_WAP.equals(type)) {
			setRequestType(RequestLog.REQUEST_TYPE_OTHER);
		}
	}

	public String getRequestTypeStr() {
		Integer requestType = getRequestType();
		if (REQUEST_TYPE_OTHER.equals(requestType)) {
			return "其他";
		} else if (REQUEST_TYPE_ANDROID.equals(requestType)) {
			return "安卓APP";
		} else if (REQUEST_TYPE_IOS.equals(requestType)) {
			return "苹果APP";
		} else if (REQUEST_TYPE_PC.equals(requestType)) {
			return "PC web";
		} else if (REQUEST_TYPE_WAP.equals(requestType)) {
			return "H5";
		}
		return null;
	}

	public String getLiveTitle() {
		if (null != liveInfo) {
			return liveInfo.getLiveTitle();
		}
		return null;
	}

	public String getNickName() {
		if (null != userInfo) {
			return userInfo.getNickName();
		}
		return null;
	}

	public String getIp() {
		if (null != ipAddress) {
			return ipAddress.getIp();
		}
		return null;
	}

	public String getRegion() {
		if (null != ipAddress) {
			return ipAddress.getRegion();
		}
		return null;
	}

	@JsonIgnore
	private LiveInfo liveInfo;
	@JsonIgnore
	private UserInfo userInfo;
	@JsonIgnore
	private IpAddress ipAddress;

	public LiveInfo getLiveInfo() {
		return liveInfo;
	}

	public void setLiveInfo(LiveInfo liveInfo) {
		this.liveInfo = liveInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public RequestLog() {
		super();
	}

	public RequestLog(Integer roomId, Long liveEventId, String userId, Integer userType, Integer requestType,
			Long ipCode) {
		super(roomId, liveEventId, userId, userType, requestType, null, ipCode);
	}

	public RequestLog(Integer roomId, Long liveEventId, String userId, Integer userType, Integer requestType,
			Integer logType, Long ipCode) {
		super(roomId, liveEventId, userId, userType, requestType, logType, ipCode);
	}

}
