package com.bvRadio.iLive.iLive.entity;

public class StationBean {
	private Integer stationId;
	private String stationName;

	public StationBean() {
		super();
	}

	public StationBean(Integer stationId, String stationName) {
		super();
		this.stationId = stationId;
		this.stationName = stationName;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
