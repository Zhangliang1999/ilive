package com.jwzt.statistic.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.entity.VideoInfo;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.pool.EnterpriseStatisticPool;
import com.jwzt.statistic.pool.LiveEndStatisticPool;
import com.jwzt.statistic.pool.TotalStatisticPool;
import com.jwzt.statistic.pool.VideoStatisticPool;
import com.jwzt.statistic.task.EnterpriseSyncTask;
import com.jwzt.statistic.task.UserSyncTask;
import com.jwzt.statistic.task.VideoSyncTask;
import com.jwzt.statistic.utils.ConfigUtils;

@Controller
public class ToolAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/tool/{mode}")
	public String statistic(@PathVariable(value = "mode") String mode, Date statisticDate, Date startDate, Date endDate,
			HttpServletRequest request, ModelMap mp) {
		if (null == statisticDate && null == startDate && null == endDate) {
			RenderJsonUtils.addSuccess(mp);
			return "renderJson";
		}
		if (null != statisticDate) {
			statisticByDay(mode, statisticDate);
		} else if (null != startDate && null != endDate && startDate.getTime() < endDate.getTime()) {
			DateTime startTime = new DateTime(startDate);
			DateTime endTime = new DateTime(endDate);
			DateTime statisticTime = startTime;
			while (statisticTime.getMillis() < endTime.getMillis()) {
				statisticByDay(mode, statisticTime.toDate());
				statisticTime = statisticTime.plusDays(1);
			}

		}

		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	private void statisticByDay(String mode, Date statisticDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(statisticDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date startTime = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date endTime = calendar.getTime();
		log.debug("Tool runStatistic. mode={}, startTime={}, endTime={}", mode, startTime, endTime);
		if ("all".equals(mode) || "total".equals(mode)) {
			TotalStatisticPool.execute(startTime, endTime);
		}
		if ("all".equals(mode) || "live".equals(mode)) {
			List<LiveInfo> liveInfoList = liveInfoMng.listByParams(null, null, null, null, startTime, endTime);
			if (null != liveInfoList) {
				for (LiveInfo liveInfo : liveInfoList) {
					if (null != liveInfo) {
						Long liveEventId = liveInfo.getLiveEventId();
						LiveEndStatisticPool.execute(liveEventId);
					}
				}
			}
		}
		if ("all".equals(mode) || "enterprise".equals(mode)) {
			List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoMng.listByParams(null, null, null, null, false);
			if (null != enterpriseInfoList) {
				for (EnterpriseInfo enterpriseInfo : enterpriseInfoList) {
					if (null != enterpriseInfo) {
						Integer enterpriseId = enterpriseInfo.getId();
						if (null != enterpriseId) {
							EnterpriseStatisticPool.execute(enterpriseId, startTime, endTime);
						}
					}
				}
			}
		}
		if ("all".equals(mode) || "video".equals(mode)) {
			List<VideoInfo> videoInfoList = videoInfoMng.listByParams(null, null, null, null, false);
			for (VideoInfo videoInfo : videoInfoList) {
				if (null != videoInfo) {
					Long videoId = videoInfo.getId();
					if (null != videoId) {
						VideoStatisticPool.execute(videoId, startTime, endTime);
					}
				}
			}
		}
	}

	private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	@RequestMapping("/tool/sync/{mode}")
	public String sync(@PathVariable(value = "mode") String mode, HttpServletRequest request, ModelMap mp) {
		log.debug("Tool runStatistic. mode={}", mode);
		if ("all".equals(mode) || "enterprise".equals(mode)) {
			EnterpriseSyncTask enterpriseSyncTask = new EnterpriseSyncTask();
			THREAD_POOL_EXECUTOR.execute(enterpriseSyncTask);
		}
		if ("all".equals(mode) || "video".equals(mode)) {
			VideoSyncTask videoSyncTask = new VideoSyncTask();
			THREAD_POOL_EXECUTOR.execute(videoSyncTask);
		}
		if ("user".equals(mode)) {
			List<UserInfo> userList = userInfoMng.listByParams(null, null, null, null);
			if (null != userList) {
				List<Long> userIdList = new ArrayList<Long>();
				for (UserInfo userInfo : userList) {
					if (null != userInfo) {
						Long id = userInfo.getId();
						if (null != id) {
							userIdList.add(id);
						}
					}
				}
				UserSyncTask userSyncTask = new UserSyncTask(userIdList);
				THREAD_POOL_EXECUTOR.execute(userSyncTask);
			}

		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@RequestMapping("/tool/update/{mode}")
	public String update(@PathVariable(value = "mode") String mode, HttpServletRequest request, ModelMap mp) {
		log.debug("Tool runStatistic. mode={}", mode);
		if ("requestLogEnterpriseId".equals(mode)) {
			Integer[] logTypes = { RequestLog.LOG_TYPE_LIVE_BEGIN };
			List<RequestLog> list = requestLogMng.listByParams(null, null, null, logTypes, null, null, null, false,
					false, false);
			if (null != list) {
				for (RequestLog requestLog : list) {
					if (null != requestLog) {
						Integer enterpriseId = requestLog.getEnterpriseId();
						if (null == enterpriseId) {
							Long liveEventId = requestLog.getLiveEventId();
							if (null != liveEventId) {
								LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
								Integer enterpriseId2 = liveInfo.getEnterpriseId();
								requestLog.setEnterpriseId(enterpriseId2);
								requestLogMng.update(requestLog);
							}
						}
					}
				}
			}
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@RequestMapping("/tool/interface/{mode}")
	public String proxyInterface(@PathVariable(value = "mode") String mode, Integer startId, Integer size,
			HttpServletRequest request, ModelMap mp) throws IOException {
		log.debug("Tool runStatistic. mode={}", mode);
		if ("enterpriseList".equals(mode)) {
			String getEnterpriseListUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_LIST_URL);
			StringBuilder paramsBuilder = new StringBuilder();
			paramsBuilder.append("startId").append("=").append(startId).append("&");
			paramsBuilder.append("size").append("=").append(size);
			String params = paramsBuilder.toString();
			String getEnterpriseListResultJson = HttpUtils.sendGet(getEnterpriseListUrl + "?" + params, "utf-8");
			Map<?, ?> getEnterpriseListResultMap = JsonUtils.jsonToMap(getEnterpriseListResultJson);
			RenderJsonUtils.addSuccess(mp, getEnterpriseListResultMap);
			return "renderJson";
		} else if ("videoList".equals(mode)) {
			String getVideoListUrl = ConfigUtils.get(ConfigUtils.GET_VIDEO_LIST_URL);
			StringBuilder paramsBuilder = new StringBuilder();
			paramsBuilder.append("startId").append("=").append(startId).append("&");
			paramsBuilder.append("size").append("=").append(size);
			String params = paramsBuilder.toString();
			String getVideoListResultJson = HttpUtils.sendGet(getVideoListUrl + "?" + params, "utf-8");
			Map<?, ?> getVideoListResultMap = JsonUtils.jsonToMap(getVideoListResultJson);
			RenderJsonUtils.addSuccess(mp, getVideoListResultMap);
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@Autowired
	private EnterpriseInfoMng enterpriseInfoMng;
	@Autowired
	private VideoInfoMng videoInfoMng;
	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private RequestLogMng requestLogMng;
	@Autowired
	private UserInfoMng userInfoMng;
}
