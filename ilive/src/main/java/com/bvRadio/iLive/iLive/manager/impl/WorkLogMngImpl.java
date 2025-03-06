package com.bvRadio.iLive.iLive.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bvRadio.iLive.iLive.entity.WorkLog;
import com.bvRadio.iLive.iLive.manager.WorkLogMng;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.JsonUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

import net.sf.json.JSONObject;

@Service
public class WorkLogMngImpl implements WorkLogMng {

	@Override
	public List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime) {
		String getLogUrl = ConfigUtils.get("log_url");
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
		String httpReturnString = null;
		try {
			httpReturnString = HttpUtils.sendPost("http://"+getLogUrl+"/logServer/service/log/list", params, "utf-8");
			System.out.println(httpReturnString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<?, ?> jsonToMap = JsonUtils.jsonToMap(JSONObject.fromObject(httpReturnString));
		List<WorkLog> list = null;
		Integer code = (Integer) jsonToMap.get("code");
		if (null != code && code.equals(1)) {
			Map<?, ?> pageMap = (Map<?, ?>) jsonToMap.get("data");
			list = (List<WorkLog>) pageMap.get("list");
		}
		return list;
	}

	@Override
	public String save(WorkLog bean) {
		try {
			if (null != bean) {
				String getLogUrl = ConfigUtils.get("log_url");
				String saveLogUrl = "http://" + getLogUrl + "/logServer/service/log/save";
				Map<String, String> params = bean.toParamsMap();
				try {
					String sendPost = HttpUtils.sendPost(saveLogUrl, params, "utf-8");
					System.out.println(sendPost);
					return sendPost;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
