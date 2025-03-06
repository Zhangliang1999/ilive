package com.jwzt.livems.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jwzt.common.SomsConfigInfo;

import net.sf.json.JSONObject;

/**
 * @author ysf
 */
public class LiveBillingUtils {

	/**
	 * 存储
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Store = 2;
	/**
	 * 短信
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Sms = 3;
	/**
	 * 时长
	 */
	public static final Integer ENTERPRISE_USE_TYPE_Duration = 4;

	/**
	 * 获取产品使用信息
	 * 
	 * @param enterpriseId
	 *            企业ID
	 * @return
	 */
	public static Long getEnterpriseRemainingStorage(Integer enterpriseId, Integer certStatus) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("enterpriseId", enterpriseId + "");
			certStatus = certStatus == null ? 0 : certStatus;
			map.put("certStatus", certStatus + "");
			String url = SomsConfigInfo.get("live_billing_get_product_info_url");
			String postJson = HttpUtils.sendPost(url, map, "UTF-8");
			JSONObject jsonObject = JSONObject.fromObject(postJson);
			Integer code = (Integer) jsonObject.get("code");
			if (code == 1) {
				// 解析功能码
				JSONObject data = (JSONObject) jsonObject.get("data");
				// 存储 已使用 usedStorageProduct kb 总共 storageProduct kb
				String usedStorageProduct = data.get("usedStorageProduct").toString();
				String storageProduct = data.get("storageProduct").toString();
				return Long.parseLong(storageProduct) - Long.parseLong(usedStorageProduct);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	/**
	 * 产品使用 通知
	 * 
	 * @param enterpriseId
	 *            企业ID
	 * @param useType
	 *            使用类型 2：存储/kb 3：短信/条 4：时长/s
	 * @param useValue
	 *            使用数量 存储时 单位： KB 短信 单位：条 时长 单位：秒 并发数 单位：个
	 * @return false 数据发送成功 true 数据发送失败
	 */
	public static void updateEnterpriseProductInfo(Integer enterpriseId, Integer useType, Integer useValue,
			Integer certStatus) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("enterpriseId", enterpriseId + "");
		map.put("useType", useType + "");
		map.put("useValue", useValue + "");
		certStatus = certStatus == null ? 0 : certStatus;
		map.put("certStatus", certStatus + "");
		String url = SomsConfigInfo.get("live_billing_update_product_info_url");
		HttpUtils.sendPost(url, map, "UTF-8");
	}

}
