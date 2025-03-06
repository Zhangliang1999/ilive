package com.jwzt.statistic.task;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.IPSeeker;
import com.jwzt.common.utils.IpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.utils.Utils;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.manager.IpAddressMng;

/**
 * 获取IP物理地址
 * @author gstars
 *
 */
public class IpAddressTask extends TimerTask {

	private static final String IP_ADDRESS_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=:ip";

	private static final Logger log = LogManager.getLogger();
	private String ip;

	public IpAddressTask() {
		super();
	}

	public IpAddressTask(String ip) {
		super();
		this.ip = ip;
	}

	@Override
	public void run() {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (null == context) {
			return;
		}
		IpAddressMng ipAddressMng = (IpAddressMng) context.getBean("IpAddressMng");
		long ipCode = IpUtils.ipToLong(ip);
		IpAddress ipAddress = ipAddressMng.getBeanByIpCode(ipCode);
		try {
			if (null == ipAddress) {
				try{
					ipAddress = getIpAddressFromQQ(ip);
					ipAddressMng.save(ipAddress);
				}catch(Exception e){
					e.printStackTrace();
					//本地没有的IP地址从互联网搜索
					ipAddress = getIpAddressFromInternet(ip);
					ipAddressMng.save(ipAddress);
				}
				
			}
		} catch (Exception e) {
			log.warn("根据IP获取城市出错，ip = {}", ip, e);
			if (null == ipAddress) {
				ipAddress = new IpAddress();
				ipAddress.setIp(ip);
				ipAddress.setIpCode(ipCode);
				ipAddress.setIsSuccess(1);
				ipAddressMng.save(ipAddress);
			}else {
				ipAddress.setIp(ip);
				ipAddress.setIsSuccess(1);
				ipAddressMng.update(ipAddress);
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static IpAddress getIpAddressFromQQ(String ip) throws Exception {
		//log.debug("从QQ本地IP库上获取城市出错，ip = {}", ip);
		IpAddress bean = null;
		IPSeeker ipseeker = IPSeeker.getInstance();
		String seekret = ipseeker.getCountry(ip);
		String[] areaz = Utils.getArea(seekret);

				String country = "";
				String countryId = "";
				String area = "";
				String areaId = "";
				String region = areaz[0];
				String regionId = "";
				String city = areaz[1];
				String cityId = "";
				String county = "";
				String countyId = "";
				String isp = "";
				String ispId = "";
				long ipCode = IpUtils.ipToLong(ip);
				bean = new IpAddress(ip, ipCode, country, countryId, area, areaId, region, regionId, city, cityId,
						county, countyId, isp, ispId);

		return bean;
	}

	@SuppressWarnings("unchecked")
	public static IpAddress getIpAddressFromInternet(String ip) throws IOException {
		log.debug("从互联网上获取城市出错，ip = {}", ip);
		IpAddress bean = null;
		String ipAddressUrl = IP_ADDRESS_URL;
		ipAddressUrl = ipAddressUrl.replace(":ip", ip);
		String result = "";
		result = HttpUtils.sendGet(ipAddressUrl, "UTF-8");
		Map<?, ?> resultJson = JsonUtils.jsonToMap(result);
		Integer code = (Integer) resultJson.get("code");
		if (null != code && code == 0) {
			Map<String, String> dataJson = (Map<String, String>) resultJson.get("data");
			if (null != dataJson) {
				String country = dataJson.get("country");
				String countryId = dataJson.get("country_id");
				String area = dataJson.get("area");
				String areaId = dataJson.get("area_id");
				String region = dataJson.get("region");
				String regionId = dataJson.get("region_id");
				String city = dataJson.get("city");
				String cityId = dataJson.get("city_id");
				String county = dataJson.get("county");
				String countyId = dataJson.get("county_id");
				String isp = dataJson.get("isp");
				String ispId = dataJson.get("isp_id");
				long ipCode = IpUtils.ipToLong(ip);
				bean = new IpAddress(ip, ipCode, country, countryId, area, areaId, region, regionId, city, cityId,
						county, countyId, isp, ispId);
			}
		}
		return bean;
	}
}