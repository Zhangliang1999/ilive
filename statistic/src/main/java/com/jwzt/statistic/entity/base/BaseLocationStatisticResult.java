package com.jwzt.statistic.entity.base;

import java.sql.Timestamp;

/**
 * @author ysf
 */

@SuppressWarnings("serial")
public class BaseLocationStatisticResult implements java.io.Serializable {

	private String id;
	private Long flagId;
	private Integer flagType;
	private String provinceName;
	private Long totalNum;
	private Timestamp createTime;
	private Timestamp statisticTime;
	private String city;

	public BaseLocationStatisticResult() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getFlagId() {
		return flagId;
	}

	public void setFlagId(Long flagId) {
		this.flagId = flagId;
	}

	public Integer getFlagType() {
		return flagType;
	}

	public void setFlagType(Integer flagType) {
		this.flagType = flagType;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getStatisticTime() {
		return statisticTime;
	}

	public void setStatisticTime(Timestamp statisticTime) {
		this.statisticTime = statisticTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
