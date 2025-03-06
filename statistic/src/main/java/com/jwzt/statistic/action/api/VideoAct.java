package com.jwzt.statistic.action.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.manager.VideoInfoMng;

@Controller
public class VideoAct {
	private static final Logger log = LogManager.getLogger();

	/**
	 * 新增回看记录
	 * @param videoInfoJson
	 * @param time
	 * @param ip
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/video/save")
	public String save(@RequestBody String videoInfoJson, Long time, String ip, HttpServletRequest request,
			ModelMap mp) {
		try {
			log.debug("VideoAct.save, videoInfoJson:{}", videoInfoJson);
			if (StringUtils.isBlank(ip)) {
				throw new Exception("Ip is blank.");
			}
			if (null == time) {
				throw new Exception("time is null.");
			}
			Map<?, ?> jsonMap = JsonUtils.jsonToMap(videoInfoJson);
			videoInfoMng.saveOrUpdateFromDataMap(jsonMap);
		} catch (Exception e) {
			log.debug("VideoAct.save error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	/**
	 * 更新回看信息
	 * @param videoInfoJson
	 * @param time
	 * @param ip
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/video/update")
	public String update(@RequestBody String videoInfoJson, Long time, String ip, HttpServletRequest request,
			ModelMap mp) {
		try {
			log.debug("VideoAct.save, videoInfoJson:{}", videoInfoJson);
			if (StringUtils.isBlank(ip)) {
				throw new Exception("Ip is blank.");
			}
			if (null == time) {
				throw new Exception("time is null.");
			}
			Map<?, ?> jsonMap = JsonUtils.jsonToMap(videoInfoJson);
			videoInfoMng.saveOrUpdateFromDataMap(jsonMap);
		} catch (Exception e) {
			log.debug("VideoAct.save error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@Autowired
	private VideoInfoMng videoInfoMng;
}
