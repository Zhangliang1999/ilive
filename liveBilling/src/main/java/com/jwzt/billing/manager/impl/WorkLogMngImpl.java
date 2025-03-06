package com.jwzt.billing.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jwzt.billing.entity.bo.WorkLog;
import com.jwzt.billing.manager.WorkLogMng;
import com.jwzt.billing.utils.ConfigUtils;
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;

/**
 * @author ysf
 */
@Service
public class WorkLogMngImpl implements WorkLogMng {

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime) throws IOException {
		String getLogUrl = ConfigUtils.get("get_log_url");
		Map<String, String> params = new HashMap<String, String>();
		if (null != systemId) {
			params.put("systemId", String.valueOf(systemId));
		}
		if (null != modelId) {
			params.put("modelId", String.valueOf(modelId));
		}
		if (null != contentId) {
			params.put("contentId", contentId);
		}
		if (null != userId) {
			params.put("userId", String.valueOf(userId));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != startTime) {
			params.put("startTime", sdf.format(startTime));
		}
		if (null != endTime) {
			params.put("endTime", sdf.format(endTime));
		}
		String httpReturnString = HttpUtils.sendPost(getLogUrl, params, "utf-8");
		Map<?, ?> jsonToMap = JsonUtils.jsonToMap(httpReturnString);
		List<WorkLog> list = null;
		Integer code = (Integer) jsonToMap.get("code");
		if (null != code && code.equals(1)) {
			Map<?, ?> pageMap = (Map<?, ?>) jsonToMap.get("data");
			list = (List<WorkLog>) pageMap.get("list");
		}
		return list;
	}

	@Override
	public WorkLog save(final WorkLog bean) throws IOException {
		if (null != bean) {
			String saveLogUrl = ConfigUtils.get("save_log_url");
			Map<String, String> params = bean.toParamsMap();
			HttpUtils.sendPost(saveLogUrl, params, "utf-8");
		}
		return bean;
	}

}
