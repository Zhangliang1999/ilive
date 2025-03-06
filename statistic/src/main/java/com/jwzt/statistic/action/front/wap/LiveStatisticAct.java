package com.jwzt.statistic.action.front.wap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;

@Controller
public class LiveStatisticAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/statistic/live/viewSource")
	public String viewSource(Long liveEventId, HttpServletRequest request, ModelMap mp) {
		try {
			Integer[] countUserLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Long otherUserNum = requestLogMng.countUserNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_OTHER,
					countUserLogTypes, null, null);
			Long androidUserNum = requestLogMng.countUserNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_ANDROID,
					countUserLogTypes, null, null);
			Long iosUserNum = requestLogMng.countUserNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_IOS,
					countUserLogTypes, null, null);
			Long pcUserNum = requestLogMng.countUserNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_PC,
					countUserLogTypes, null, null);
			Long wapUserNum = requestLogMng.countUserNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_WAP,
					countUserLogTypes, null, null);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("androidUserNum", androidUserNum);
			data.put("iosUserNum", iosUserNum);
			data.put("pcUserNum", pcUserNum);
			data.put("wapUserNum", wapUserNum);
			data.put("otherUserNum", otherUserNum);
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/statistic/live/location")
	public String location(Long liveEventId, HttpServletRequest request, ModelMap mp) {
		try {
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(liveEventId, LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, null,
							null);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						Long totalNum = locationStatisticResult.getTotalNum();
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("provinceName", provinceName);
						data.put("totalNum", totalNum);
						list.add(data);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, list);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/statistic/live/locationGroupByCity")
	public String locationGroupByCity(Long liveEventId, HttpServletRequest request, ModelMap mp) {
		try {
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlagGroupByCity(liveEventId, LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, null,
							null);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						String city = locationStatisticResult.getCity();
						Long totalNum = locationStatisticResult.getTotalNum();
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("provinceName", provinceName);
						data.put("city", city);
						data.put("totalNum", totalNum);
						list.add(data);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, list);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@Autowired
	private RequestLogMng requestLogMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;
}
