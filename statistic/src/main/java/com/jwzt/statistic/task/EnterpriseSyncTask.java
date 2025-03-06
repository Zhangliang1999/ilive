package com.jwzt.statistic.task;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.utils.ConfigUtils;

/**
 * 企业信息同步
 * @author gstars
 *
 */
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
				return;
			}
			log.debug("EnterpriseSyncTask run. syncMode={}", syncMode);
			EnterpriseInfoMng enterpriseInfoMng = (EnterpriseInfoMng) context.getBean("EnterpriseInfoMng");
			if (SYNC_MODE_LIST.equals(syncMode)) {
				String getEnterpriseListUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_LIST_URL);
				Integer startId = 0;
				Integer resultSize = 0;
				do {
					StringBuilder paramsBuilder = new StringBuilder();
					paramsBuilder.append("startId").append("=").append(startId).append("&");
					paramsBuilder.append("size").append("=").append(LIST_SIZE);
					String params = paramsBuilder.toString();
					String getEnterpriseListResultJson = HttpUtils.sendGet(getEnterpriseListUrl + "?" + params,
							"utf-8");
					Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
					// 回看同步
					Integer code = (Integer) getEnterpriseListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getEnterpriseListResultMap.get("data");
						if (null != dataList) {
							resultSize = dataList.size();
							for (Map<?, ?> dataMap : dataList) {
								try {
									Integer enterpriseId = (Integer) dataMap.get("enterpriseId");
									enterpriseInfoMng.saveOrUpdateFromDataMap(dataMap);
									startId = enterpriseId;
								} catch (Exception e) {
									log.warn("saveEnterpriseInfo error.", e);
								}
							}
						}
					}
				} while (resultSize > 0);
			} else if (SYNC_MODE_SINGLE.equals(syncMode)) {
				// 回看根据ids获取信息
				String getEnterpriseInfoUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_INFO_URL);
				if (null != ids && ids.length > 0) {
					StringBuilder paramsBuilder = new StringBuilder();
					for (Integer id : ids) {
						paramsBuilder.append("ids[]").append("=").append(id).append("&");
					}
					String params = paramsBuilder.toString();
					String getEnterpriseListResultJson = HttpUtils.sendPost(getEnterpriseInfoUrl, params, "utf-8");
					Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
					// 回看同步
					Integer code = (Integer) getEnterpriseListResultMap.get("code");
					if (null != code && code == 1) {
						List<Map<?, ?>> dataList = (List<Map<?, ?>>) getEnterpriseListResultMap.get("data");
						if (null != dataList) {
							for (Map<?, ?> dataMap : dataList) {
								try {
									enterpriseInfoMng.saveOrUpdateFromDataMap(dataMap);
								} catch (Exception e) {
									log.warn("saveEnterpriseInfo error.", e);
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