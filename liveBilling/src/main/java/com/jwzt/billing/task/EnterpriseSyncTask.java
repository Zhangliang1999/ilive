package com.jwzt.billing.task;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.common.entity.Config;
import com.jwzt.common.manager.ConfigMng;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;

public class EnterpriseSyncTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	private static final Integer SYNC_MODE_LIST = 1;

	private static final Integer SYNC_MODE_SINGLE = 2;

	private static final Integer LIST_SIZE = 100;

	private Integer[] ids;

	private Integer syncMode;

	public EnterpriseSyncTask() {
		super();
		syncMode = SYNC_MODE_LIST;
	}

	public EnterpriseSyncTask(Integer[] ids) {
		super();
		this.ids = ids;
		syncMode = SYNC_MODE_SINGLE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				context = ContextLoader.getCurrentWebApplicationContext();
			}
			log.debug("EnterpriseSyncTask run. syncMode={}", syncMode);
			EnterpriseBillingMng enterpriseBillingMng = (EnterpriseBillingMng) context.getBean("EnterpriseBillingMng");
			if (SYNC_MODE_LIST.equals(syncMode)) {
				String getEnterpriseListUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_LIST_URL);
				ConfigMng configMng = (ConfigMng) context.getBean("ConfigMng");
				String lastSyncEnterpriseIdValue = configMng.getValue(Config.LAST_SYNC_ENTERPRISE_ID);
				Integer startId;
				try {
					startId = Integer.parseInt(lastSyncEnterpriseIdValue);
				} catch (Exception e) {
					startId = 0;
				}
				Integer newLastSyncEnterpriseId = null;
				Integer resultSize = 0;
				do {
					resultSize = 0;
					StringBuilder paramsBuilder = new StringBuilder();
					paramsBuilder.append("startId").append("=").append(startId).append("&");
					paramsBuilder.append("size").append("=").append(LIST_SIZE).append("&");
					String params = paramsBuilder.toString();
					String getEnterpriseListResultJson = HttpUtils.sendPost(getEnterpriseListUrl, params, "utf-8");
					Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
					// 回看同步
					Integer code = (Integer) getEnterpriseListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getEnterpriseListResultMap.get("data");
						if (null != dataList) {
							for (Map<?, ?> dataMap : dataList) {
								try {
									Integer enterpriseId = (Integer) dataMap.get("enterpriseId");
									enterpriseBillingMng.saveOrUpdateFromDataMap(dataMap);
									resultSize++;
									newLastSyncEnterpriseId = enterpriseId;
									startId = newLastSyncEnterpriseId;
								} catch (Exception e) {
									log.warn("saveEnterpriseBilling error.", e);
								}
							}
						}
					}
					if (null != newLastSyncEnterpriseId) {
						configMng.updateOrSave(Config.LAST_SYNC_ENTERPRISE_ID, newLastSyncEnterpriseId + "");
					}
				} while (resultSize > 0);
				configMng.updateOrSave(Config.LAST_SYNC_ENTERPRISE_ID, "0");
			} else if (SYNC_MODE_SINGLE.equals(syncMode)) {
				// 回看根据ids获取信息
				String getEnterpriseBillingUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_INFO_URL);
				if (null != ids && ids.length > 0) {
					StringBuilder paramsBuilder = new StringBuilder();
					for (Integer id : ids) {
						paramsBuilder.append("ids[]").append("=").append(id).append("&");
					}
					String params = paramsBuilder.toString();
					String getEnterpriseListResultJson = HttpUtils.sendPost(getEnterpriseBillingUrl, params, "utf-8");
					Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
					// 回看同步
					Integer code = (Integer) getEnterpriseListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getEnterpriseListResultMap.get("data");
						if (null != dataList) {
							for (Map<?, ?> dataMap : dataList) {
								try {
									enterpriseBillingMng.saveOrUpdateFromDataMap(dataMap);
								} catch (Exception e) {
									log.warn("saveEnterpriseBilling error.", e);
								}
							}
						}
					}
				}
			}
			log.debug("EnterpriseSyncTask end.");
		} catch (Exception e) {
			log.error("EnterpriseSyncTask error.", e);
		}
	}

}