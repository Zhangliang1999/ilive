package com.bvRadio.iLive.manager.manager.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.JsonUtils;
import com.bvRadio.iLive.manager.entity.WorkLog;
import com.bvRadio.iLive.manager.manager.WorkLogMng;

import net.sf.json.JSONObject;

@Service
public class WorkLogMngImpl implements WorkLogMng {

	@Override
	public List<WorkLog> listByParams(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime,Integer pageNo,Integer pageSize) {
		String getLogUrl = "http://mgr.zbt.tv189.net/logServer/service/log/list";
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
		params.put("pageNo", String.valueOf(pageNo==null?1:pageNo));
		params.put("pageSize", String.valueOf(pageSize==null?20:pageSize));
		String httpReturnString = null;
		try {
			httpReturnString = HttpUtils.sendPost(getLogUrl, params, "utf-8");
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
	public Pagination getPage(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime) {
		String getLogUrl = "http://mgr.zbt.tv189.net/logServer/service/log/list";
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
			httpReturnString = HttpUtils.sendPost(getLogUrl, params, "utf-8");
			System.out.println(httpReturnString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<?, ?> jsonToMap = JsonUtils.jsonToMap(JSONObject.fromObject(httpReturnString));
		Pagination page = null;
		Integer code = (Integer) jsonToMap.get("code");
		if (null != code && code.equals(1)) {
			page = (Pagination) jsonToMap.get("data");
		}
		return page;
	}

	@Override
	public Map<?, ?> getList(Integer systemId, Integer modelId, String contentId, Long userId, Date startTime,
			Date endTime, Integer pageNo, Integer pageSize) {
		String getLogUrl = "http://mgr.zbt.tv189.net/logServer/service/log/list";
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
		params.put("pageNo", String.valueOf(pageNo==null?1:pageNo));
		params.put("pageSize", String.valueOf(pageSize==null?20:pageSize));
		String httpReturnString = null;
		try {
			httpReturnString = HttpUtils.sendPost(getLogUrl, params, "utf-8");
			System.out.println(httpReturnString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<?, ?> jsonToMap = JsonUtils.jsonToMap(JSONObject.fromObject(httpReturnString));
		List<WorkLog> list = null;
		Integer code = (Integer) jsonToMap.get("code");
		Map<String, Object> map = new HashMap<>();
		if (null != code && code.equals(1)) {
			Map<?, ?> pageMap = (Map<?, ?>) jsonToMap.get("data");
			return pageMap;
		}
		return null;
	}

	@Override
	public String save(WorkLog bean) {
		if (null != bean) {
			String saveLogUrl = "http://mgr.zbt.tv189.net/logServer/service/log/save";
			Map<String, String> params = bean.toParamsMap();
			try {
				String sendPost = HttpUtils.sendPost(saveLogUrl, params, "utf-8");
				System.out.println(sendPost);
				return sendPost;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}
