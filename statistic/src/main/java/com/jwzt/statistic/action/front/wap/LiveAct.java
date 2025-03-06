package com.jwzt.statistic.action.front.wap;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.constants.RenderJsonConstants;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;

@Controller
public class LiveAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/live/info")
	public String info(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}

			Map<String, Object> data = new HashMap<String, Object>();
			String liveTitle = liveInfo.getLiveTitle();
			Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
			String coverAddr = liveInfo.getCoverAddr();
			String enterpriseImg = liveInfo.getEnterpriseImg();
			String enterpriseName = liveInfo.getEnterpriseName();
			data.put("liveTitle", liveTitle);
			data.put("liveBeginTime", liveBeginTime);
			data.put("liveEndTime", liveEndTime);
			data.put("coverAddr", coverAddr);
			data.put("enterpriseImg", enterpriseImg);
			data.put("enterpriseName", enterpriseName);
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", data);
				return "renderJson";
			}
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/live/statistic/simple")
	public String simpleStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			Long userNum = 0L;
			Long shareNum = 0L;
			Long commentNum = 0L;
			Long likeNum = 0L;
			Long viewNum = 0L;
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			if (null != liveStatisticResult) {
				userNum = liveStatisticResult.getUserNum();
				//shareNum = liveStatisticResult.getShareNum();
				shareNum = requestLogMng.getNumByRoom(liveInfo.getRoomId(), 3).longValue();
				commentNum = liveStatisticResult.getCommentNum();
				//likeNum = liveStatisticResult.getLikeNum();
				likeNum = requestLogMng.getNumByRoom(liveInfo.getRoomId(), 4).longValue();
				viewNum = liveStatisticResult.getViewNum();
			}
			data.put("userNum", userNum);
			data.put("shareNum", shareNum);
			data.put("commentNum", commentNum);
			data.put("likeNum", likeNum);
			data.put("viewNum", viewNum);
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/live/statistic/minute")
	public String minuteStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			Long maxMinute = 0L;
			Long maxMinuteUserNum = 0L;
			Long liveId = liveInfo.getId();
			
			//* 废弃卫旗写的代码IiveEventCurrentMax应该是不需要的
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			if (null != liveStatisticResult) {
				maxMinute = liveStatisticResult.getMaxMinute();
				maxMinuteUserNum = liveStatisticResult.getMaxMinuteUserNum();
			}
			
			data.put("maxMinute", maxMinute);
			data.put("maxMinuteUserNum", maxMinuteUserNum);
			
			
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@RequestMapping("/live/statistic/totalAndAverage")
	public String totalAndAverageStatistic(Long id, HttpServletRequest request, ModelMap mp) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(id);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null == liveEndTime) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_LIVE_NOT_END, "直播未结束", null);
				return "renderJson";
			}
			Timestamp lastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == lastStatisticTime || lastStatisticTime.getTime() < liveEndTime.getTime()) {
				RenderJsonUtils.addError(mp, RenderJsonConstants.JSON_STATUS_STATISTIC_NOT_END, "统计未结束", null);
				return "renderJson";
			}
			Map<String, Object> data = new HashMap<String, Object>();
			Long userNum = 0L;
			Long viewTotalDuration = 0L;
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			if (null != liveStatisticResult) {
				userNum = liveStatisticResult.getUserNum();
				viewTotalDuration = liveStatisticResult.getViewTotalDuration();
			}
			Long totalTime = liveInfo.getLiveDuration();
			data.put("userNum", userNum);
			data.put("totalTime", totalTime);
			data.put("viewTotalDuration", viewTotalDuration);
			RenderJsonUtils.addSuccess(mp, data);
			return "renderJson";
		} catch (Exception e) {
			log.debug("LiveAct.info error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
	}

	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private LiveStatisticResultMng liveStatisticResultMng;
	
	@Autowired
	private RequestLogMng requestLogMng;
	
}
