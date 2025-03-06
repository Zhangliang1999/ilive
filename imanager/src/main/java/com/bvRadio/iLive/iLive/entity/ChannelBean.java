package com.bvRadio.iLive.iLive.entity;

public class ChannelBean {
	private Integer channelId;
	private String channelName;
	private String palyUrl;
	private String shortName;
	private Integer stationId;

	public ChannelBean() {
		super();
	}

	public ChannelBean(Integer channelId, String channelName) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPalyUrl() {
		return palyUrl;
	}

	public void setPalyUrl(String palyUrl) {
		this.palyUrl = palyUrl;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

}
