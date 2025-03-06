package com.bvRadio.iLive.iLive.entity;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSIpAddress;

@SuppressWarnings("serial")
public class BBSIpAddress extends BaseBBSIpAddress implements java.io.Serializable {

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

	public BBSIpAddress() {
	}

	public BBSIpAddress(String ip, Long ipCode) {
		super(ip, ipCode);
	}


}
