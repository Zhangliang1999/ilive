package com.jwzt.statistic.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.entity.base.BaseLiveInfo;
import com.jwzt.statistic.utils.ConfigUtils;

@SuppressWarnings("serial")
public class LiveInfo extends BaseLiveInfo {
	@JsonIgnore
	private IpAddress ipAddress;
	private Long liveEventTotalNum;
	private Long liveViewUserTotalNum;

	private LiveStatisticResult statisticResult;

	public Long getLiveEventTotalNum() {
		return liveEventTotalNum;
	}

	public void setLiveEventTotalNum(Long liveEventTotalNum) {
		this.liveEventTotalNum = liveEventTotalNum;
	}

	public Long getLiveViewUserTotalNum() {
		return liveViewUserTotalNum;
	}

	public void setLiveViewUserTotalNum(Long liveViewUserTotalNum) {
		this.liveViewUserTotalNum = liveViewUserTotalNum;
	}

	public LiveStatisticResult getStatisticResult() {
		return statisticResult;
	}

	public void setStatisticResult(LiveStatisticResult statisticResult) {
		this.statisticResult = statisticResult;
	}

	public IpAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(IpAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProvinceName() {
		if (null != getIpAddress()) {
			String city = getIpAddress().getCity();
			if (StringUtils.isBlank(city) || "xx".equalsIgnoreCase(city)) {
				String defaultProvinceName = ConfigUtils.get(ConfigUtils.DEFAULT_PROVINCE_NAME);
				city = defaultProvinceName;
			}
			return city;
		}
		return null;
	}

	public Long getLiveDuration() {
		try {
			Long duration = (getLiveEndTime().getTime() - getLiveBeginTime().getTime()) / 1000;
			if (duration > 0) {
				return duration;
			}
			return 0L;
		} catch (Exception e) {
		}
		return 0L;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatisticing()) {
			setStatisticing(false);
		}
	}

	public LiveInfo() {
		super();
	}

	public LiveInfo(Integer roomId, Long liveEventId, String liveTitle, String coverAddr, Boolean openDecorate,
			Integer baseNum, Integer multipleNum, Integer enterpriseId, String enterpriseName, String enterpriseDesc,
			String enterpriseImg) {
		super(roomId, liveEventId, liveTitle, coverAddr, openDecorate, baseNum, multipleNum, enterpriseId,
				enterpriseName, enterpriseDesc, enterpriseImg);
	}
}
