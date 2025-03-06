package com.jwzt.statistic.action.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.manager.TotalStatisticResultMng;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class TotalAct {
	private static final Logger log = LogManager.getLogger();

	/**
	 * 直播总场次、发起直播商户数、平均直播总时长、累计总观看人数、累计总观看次数 总分享数、总点赞数、总评论数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/total/total")
	public String total(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			TotalStatisticResult totalStatisticResult = totalStatisticResultMng.sumByParams(startTime, endTime);
			// 直播总场次
			Long liveEventTotalNum = totalStatisticResult.getLiveEventTotalNum();
			// 发起直播商户数
			Long liveEnterpriseTotalNum = totalStatisticResult.getLiveEnterpriseTotalNum();
			// 累计总观看人数
			Long liveViewUserTotalNum = totalStatisticResult.getLiveViewUserTotalNum();
			// 累计总观看次数
			Long liveViewTotalNum = totalStatisticResult.getLiveViewTotalNum();
			Long liveTotalDuration = totalStatisticResult.getLiveTotalDuration();
			// 平均直播总时长
			Long liveTotalAverageDuraion;
			if (null != liveEventTotalNum && liveEventTotalNum > 0) {
				liveTotalAverageDuraion = liveTotalDuration / liveEventTotalNum;
			} else {
				liveTotalAverageDuraion = 0L;
			}
			// 总分享数
			Long liveLikeTotalNum = totalStatisticResult.getLiveLikeTotalNum();
			// 总点赞数
			Long liveShareTotalNum = totalStatisticResult.getLiveShareTotalNum();
			// 总评论数
			Long liveCommentTotalNum = totalStatisticResult.getLiveCommentTotalNum();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("liveEventTotalNum", NumberUtils.checkNumber(liveEventTotalNum));
			dataMap.put("liveEnterpriseTotalNum", NumberUtils.checkNumber(liveEnterpriseTotalNum));
			dataMap.put("liveViewUserTotalNum", NumberUtils.checkNumber(liveViewUserTotalNum));
			dataMap.put("liveViewTotalNum", NumberUtils.checkNumber(liveViewTotalNum));
			dataMap.put("liveTotalAverageDuraion", NumberUtils.checkNumber(liveTotalAverageDuraion));
			dataMap.put("liveLikeTotalNum", NumberUtils.checkNumber(liveLikeTotalNum));
			dataMap.put("liveShareTotalNum", NumberUtils.checkNumber(liveShareTotalNum));
			dataMap.put("liveCommentTotalNum", NumberUtils.checkNumber(liveCommentTotalNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("TotalAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}

	}

	/**
	 * 直播并发高峰?直播并发高峰场次? 最多收看人数出现于哪场直播、观看人数、 企业名称、观看时间
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/total/max")
	public String max(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			TotalStatisticResult totalStatisticResult = totalStatisticResultMng.getLastBeanByEndTime(endTime);
			Long numOfMaxViewUserNum = totalStatisticResult.getNumOfMaxViewUserNumAboutLive();
			String enterpriseNameOfMaxViewUserNum = totalStatisticResult.getEnterpriseNameOfMaxViewUserNumAboutLive();
			Timestamp timeOfMaxViewUserNum = totalStatisticResult.getTimeOfMaxViewUserNumAboutLive();
			String liveTitleOfMaxViewUserNum = totalStatisticResult.getLiveTitleOfMaxViewUserNumAboutLive();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("userNum", NumberUtils.checkNumber(numOfMaxViewUserNum));
			dataMap.put("viewTime", timeOfMaxViewUserNum);
			dataMap.put("liveTitle", liveTitleOfMaxViewUserNum);
			dataMap.put("enterpriseName", enterpriseNameOfMaxViewUserNum);
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("TotalAct max error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	/**
	 * 直播新、老企业数， 观看新、老用户数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/total/registerSource")
	public String registerSource(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			TotalStatisticResult totalStatisticResult = totalStatisticResultMng.sumByParams(startTime, endTime);
			Long newRegisterEnterpriseNum = totalStatisticResult.getNewRegisterEnterpriseNum();
			Long oldRegisterEnterpriseNum = totalStatisticResult.getOldRegisterEnterpriseNum();
			Long newRegisterUserNum = totalStatisticResult.getNewRegisterUserNumAboutLive();
			Long oldRegisterUserNum = totalStatisticResult.getOldRegisterUserNumAboutLive();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("newRegisterEnterpriseNum", NumberUtils.checkNumber(newRegisterEnterpriseNum));
			dataMap.put("oldRegisterEnterpriseNum", NumberUtils.checkNumber(oldRegisterEnterpriseNum));
			dataMap.put("newRegisterUserNum", NumberUtils.checkNumber(newRegisterUserNum));
			dataMap.put("oldRegisterUserNum", NumberUtils.checkNumber(oldRegisterUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("TotalAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@Autowired
	private TotalStatisticResultMng totalStatisticResultMng;
}
