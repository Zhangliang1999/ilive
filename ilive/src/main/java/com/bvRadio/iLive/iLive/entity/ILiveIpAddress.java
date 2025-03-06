package com.bvRadio.iLive.iLive.entity;

import org.apache.commons.lang.StringUtils;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveIpAddress;

@SuppressWarnings("serial")
public class ILiveIpAddress extends BaseILiveIpAddress implements java.io.Serializable {

	public String getAddress() {
		String address = null;
		if (!StringUtils.isBlank(getCountry())) {
			address = getCountry() + " ";
		}
		if (!StringUtils.isBlank(getRegion())) {
			if (null == address) {
				address = getRegion() + " ";
			} else {
				address += getRegion() + " ";
			}
		}
		if (!StringUtils.isBlank(getCity())) {
			if (null == address) {
				address = getCity();
			} else {
				address += getCity();
			}
		}
		return address;
	}

	public ILiveIpAddress() {
		super();
	}

	public ILiveIpAddress(String ip, Long ipCode) {
		super(ip, ipCode);
	}

	public ILiveIpAddress(String ip, Long ipCode, String country, String countryId, String area,
			String areaId, String region, String regionId, String city, String cityId, String county,
			String countyId, String isp, String ispId) {
		super(ip, ipCode, country, countryId, area, areaId, region, regionId, city, cityId, county,
				countyId, isp, ispId);
	}

}
