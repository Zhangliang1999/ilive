package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseILiveIpAddress implements java.io.Serializable {

	private String ip;
	private Long ipCode;
	private String country;
	private String countryId;
	private String area;
	private String areaId;
	private String region;
	private String regionId;
	private String city;
	private String cityId;
	private String county;
	private String countyId;
	private String isp;
	private String ispId;

	public BaseILiveIpAddress() {
	}

	public BaseILiveIpAddress(String ip, Long ipCode) {
		this.ip = ip;
		this.ipCode = ipCode;
	}

	public BaseILiveIpAddress(String ip, Long ipCode, String country, String countryId, String area,
			String areaId, String region, String regionId, String city, String cityId, String county,
			String countyId, String isp, String ispId) {
		super();
		this.ip = ip;
		this.ipCode = ipCode;
		this.country = country;
		this.countryId = countryId;
		this.area = area;
		this.areaId = areaId;
		this.region = region;
		this.regionId = regionId;
		this.city = city;
		this.cityId = cityId;
		this.county = county;
		this.countyId = countyId;
		this.isp = isp;
		this.ispId = ispId;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getIpCode() {
		return this.ipCode;
	}

	public void setIpCode(Long ipCode) {
		this.ipCode = ipCode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryId() {
		return this.countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountyId() {
		return this.countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getIsp() {
		return this.isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIspId() {
		return this.ispId;
	}

	public void setIspId(String ispId) {
		this.ispId = ispId;
	}

}