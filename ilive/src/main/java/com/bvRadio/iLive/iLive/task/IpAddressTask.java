package com.bvRadio.iLive.iLive.task;

import java.io.IOException;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.manager.ILiveIpAddressMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.IPUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class IpAddressTask extends TimerTask {
	private static final Logger log = LoggerFactory.getLogger(IpAddressTask.class);
	private String ip;

	public IpAddressTask() {
		super();
	}

	public IpAddressTask(String ip) {
		super();
		this.ip = ip;
	}

	public void run() {
		try {
			ILiveIpAddressMng iLiveIpAddressMng = (ILiveIpAddressMng) ApplicationContextUtil
					.getBean("iLiveIpAddressMng");
			ILiveIpAddress ipAddress = getIpAddressFromInternet(ip);
			iLiveIpAddressMng.update(ipAddress);
		} catch (IOException e) {
			log.error("根据IP获取城市出错，ip = {}", ip, e);
		} catch (JSONException e) {
			log.error("根据IP获取城市出错，ip = {}", ip, e);
		}
	}

	private ILiveIpAddress getIpAddressFromInternet(String ip) throws IOException, JSONException {
		ILiveIpAddress bean = null;
		String ipAddressUrl = ConfigUtils.get(ConfigUtils.IP_ADDRESS_URL);
		ipAddressUrl = ipAddressUrl.replace(":ip", ip);
		String result = "";
		result = HttpUtils.sendGet(ipAddressUrl, "UTF-8");
		JSONObject resultJson = new JSONObject(result);
		Integer code = resultJson.getInt("code");
		if (null != code && code == 0) {
			JSONObject dataJson = resultJson.getJSONObject("data");
			if (null != dataJson) {
				String country = dataJson.getString("country");
				String countryId = dataJson.getString("country_id");
				String area = dataJson.getString("area");
				String areaId = dataJson.getString("area_id");
				String region = dataJson.getString("region");
				String regionId = dataJson.getString("region_id");
				String city = dataJson.getString("city");
				String cityId = dataJson.getString("city_id");
				String county = dataJson.getString("county");
				String countyId = dataJson.getString("county_id");
				String isp = dataJson.getString("isp");
				String ispId = dataJson.getString("isp_id");
				long ipCode = IPUtils.ipToLong(ip);
				bean = new ILiveIpAddress(ip, ipCode, country, countryId, area, areaId, region, regionId,
						city, cityId, county, countyId, isp, ispId);
			}
		}
		return bean;
	}
}