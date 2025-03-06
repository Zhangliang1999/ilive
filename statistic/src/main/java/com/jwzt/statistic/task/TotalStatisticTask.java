package com.jwzt.statistic.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.TotalStatisticResultMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.UserViewLogMng;
/**
 * 平台总体信息统计
 * @author gstars
 *
 */
public class TotalStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	private static final Long DEFAULT_TOTAL_FLAG_ID = -1L;
	private final Date startTime;
	private final Date endTime;

	public TotalStatisticTask(final Date startTime, final Date endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public void run() {
		try {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			log.debug("TotalStatisticTask run.");
			TotalStatisticResultMng totalStatisticResultMng = (TotalStatisticResultMng) context
					.getBean("TotalStatisticResultMng");
			UserInfoMng userInfoMng = (UserInfoMng) context.getBean("UserInfoMng");
			RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
			UserViewLogMng userViewLogMng = (UserViewLogMng) context.getBean("UserViewLogMng");
			LiveStatisticResultMng liveStatisticResultMng = (LiveStatisticResultMng) context
					.getBean("LiveStatisticResultMng");
			EnterpriseInfoMng enterpriseInfoMng = (EnterpriseInfoMng) context.getBean("EnterpriseInfoMng");
			LiveInfoMng liveInfoMng = (LiveInfoMng) context.getBean("LiveInfoMng");
			LocationStatisticResultMng locationStatisticResultMng = (LocationStatisticResultMng) context
					.getBean("LocationStatisticResultMng");
			MinuteStatisticResultMng minuteStatisticResultMng = (MinuteStatisticResultMng) context
					.getBean("MinuteStatisticResultMng");

			// 直播场次、累计直播时长
			List<LiveInfo> liveInfoList = liveInfoMng.listByParams(null, null, null, null, startTime, endTime);
			Long liveEventTotalNum = 0L;
			Long liveTotalDuration = 0L;
			final List<Integer> enterpriseIds = new ArrayList<Integer>();
			Long liveEnterpriseTotalNum = 0L;
			Long newRegisterUserNumAboutLive = 0L;
			Long oldRegisterUserNumAboutLive = 0L;
			Long newRegisterEnterpriseNum = 0L;
			Long oldRegisterEnterpriseNum = 0L;
			Long durationLiveNum0To10 = 0L;
			Long durationLiveNum10To100 = 0L;
			Long durationLiveNum100To200 = 0L;
			Long durationLiveNum200To300 = 0L;
			Long durationLiveNum300To = 0L;
			if (null != liveInfoList) {
				liveEventTotalNum += Long.valueOf(liveInfoList.size());
				for (LiveInfo liveInfo : liveInfoList) {
					if (null != liveInfo) {
						Long liveId = liveInfo.getId();
						Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
						Timestamp liveEndTime = liveInfo.getLiveEndTime();
						if (null == liveEndTime || null == liveBeginTime) {
							continue;
						}
						Long singleDuration = (liveEndTime.getTime() - liveBeginTime.getTime()) / 1000;
						if (singleDuration > 0) {
							liveTotalDuration += singleDuration;
							// 直播时长分布统计
							if (singleDuration >= 0 && singleDuration < 10 * 60) {
								durationLiveNum0To10++;
							} else if (singleDuration >= 10 * 60 && singleDuration < 100 * 60) {
								durationLiveNum10To100++;
							} else if (singleDuration >= 100 * 60 && singleDuration < 200 * 60) {
								durationLiveNum100To200++;
							} else if (singleDuration >= 200 * 60 && singleDuration < 300 * 60) {
								durationLiveNum200To300++;
							} else if (singleDuration >= 300 * 60) {
								durationLiveNum300To++;
							}
						}
						Integer enterpriseId = liveInfo.getEnterpriseId();
						if (!enterpriseIds.contains(enterpriseId)) {
							liveEnterpriseTotalNum++;
							// 新老企业
							EnterpriseInfo enterpriseInfo = enterpriseInfoMng.getBeanById(enterpriseId);
							if (null != enterpriseInfo) {
								Timestamp certTime = enterpriseInfo.getCertTime();
								if (null != certTime && certTime.getTime() >= startTime.getTime()
										&& certTime.getTime() < endTime.getTime()) {
									newRegisterEnterpriseNum++;
								} else {
									oldRegisterEnterpriseNum++;
								}
							}

							enterpriseIds.add(enterpriseId);
						}
						// 新老用户
						LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
						if (null != liveStatisticResult) {
							Long newRegisterUserNum2 = liveStatisticResult.getNewRegisterUserNum();
							Long oldRegisterUserNum2 = liveStatisticResult.getOldRegisterUserNum();
							if (null != newRegisterUserNum2) {
								newRegisterUserNumAboutLive += newRegisterUserNum2;
							}
							if (null != oldRegisterUserNum2) {
								oldRegisterUserNumAboutLive += oldRegisterUserNum2;
							}
						}

					}
				}
			}
			Long userTotalNum = userInfoMng.countUserNum(null, null, null, null);
			Long newRegisterUserNumAboutDay = userInfoMng.countUserNum(null, startTime, endTime, null);
			Long oldRegisterUserNumAboutDay = userTotalNum - newRegisterUserNumAboutDay > 0
					? userTotalNum - newRegisterUserNumAboutDay
					: 0;
			// 用户观看总时长、用户观看人次
			Long viewTotalNum = userViewLogMng.countUserNumByParams(null, null, null, null, startTime, endTime);
			Long viewTotalDuration = userViewLogMng.sumViewDuarationByParams(null, null, startTime, endTime);
			Integer[] countLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			// 累计观看人数
			Long liveViewUserTotalNum = requestLogMng.countUserNum(null, null, null, null, countLogTypes, startTime,
					endTime);
			// 累计观看次数
			Long liveViewTotalNum = requestLogMng.countRequestNum(null, null, null, countLogTypes, startTime, endTime);
			// 用户注册来源统计
			Long totalRegisterUserNum = userInfoMng.countUserNum(null, startTime, endTime, null);
			Long androidRegisterUserNum = userInfoMng.countUserNum(null, startTime, endTime,
					UserInfo.TERMINAL_TYPE_ANDROID);
			Long iosRegisterUserNum = userInfoMng.countUserNum(null, startTime, endTime, UserInfo.TERMINAL_TYPE_IOS);
			Long pcRegisterUserNum = userInfoMng.countUserNum(null, startTime, endTime, UserInfo.TERMINAL_TYPE_PC);
			Long wapRegisterUserNum = userInfoMng.countUserNum(null, startTime, endTime, UserInfo.TERMINAL_TYPE_H5);
			wapRegisterUserNum += userInfoMng.countUserNum(null, startTime, endTime, UserInfo.TERMINAL_TYPE_WEIXIN);
			Long otherRegisterUserNum = totalRegisterUserNum - androidRegisterUserNum - iosRegisterUserNum
					- pcRegisterUserNum - wapRegisterUserNum;
			// 用户观看来源统计
			Long androidViewUserNum = requestLogMng.countUserNum(null, null, null, RequestLog.REQUEST_TYPE_ANDROID,
					countLogTypes, startTime, endTime);
			Long iosViewUserNum = requestLogMng.countUserNum(null, null, null, RequestLog.REQUEST_TYPE_IOS,
					countLogTypes, startTime, endTime);
			Long pcViewUserNum = requestLogMng.countUserNum(null, null, null, RequestLog.REQUEST_TYPE_PC, countLogTypes,
					startTime, endTime);
			Long wapViewUserNum = requestLogMng.countUserNum(null, null, null, RequestLog.REQUEST_TYPE_WAP,
					countLogTypes, startTime, endTime);
			Long otherViewUserNum = 0L;
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_SHARE;
			// 累计分享数
			Long liveShareTotalNum = requestLogMng.countRequestNum(null, null, null, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_LIKE;
			// 累计点赞数
			Long liveLikeTotalNum = requestLogMng.countRequestNum(null, null, null, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_COMMENT;
			// 累计互动数
			Long liveCommentTotalNum = requestLogMng.countRequestNum(null, null, null, countLogTypes, startTime,
					endTime);

			// 用户观看时长分布统计
			Long durationViewUserNum0To5 = userViewLogMng.countUserNumByParams(null, null, 0L * 60, 5L * 60, startTime,
					endTime);
			Long durationViewUserNum5To10 = userViewLogMng.countUserNumByParams(null, null, 5L * 60, 10L * 60,
					startTime, endTime);
			Long durationViewUserNum10To30 = userViewLogMng.countUserNumByParams(null, null, 10L * 60, 30L * 60,
					startTime, endTime);
			Long durationViewUserNum30To60 = userViewLogMng.countUserNumByParams(null, null, 30L * 60, 60L * 60,
					startTime, endTime);
			Long durationViewUserNum60To = userViewLogMng.countUserNumByParams(null, null, 60L * 60, null, startTime,
					endTime);
			TotalStatisticResult lastTotalStatisticResult = totalStatisticResultMng.getLastBeanByEndTime(startTime);
			// 最大观看峰值统计
			Long numOfMaxViewUserNumAboutLive = 0L;
			String liveTitleOfMaxViewUserNumAboutLive = "";
			String enterpriseNameOfMaxViewUserNumAboutLive = "";
			Timestamp timeOfMaxViewUserNumAboutLive = new Timestamp(startTime.getTime());
			if (null != lastTotalStatisticResult) {
				numOfMaxViewUserNumAboutLive = lastTotalStatisticResult.getNumOfMaxViewUserNumAboutLive();
				liveTitleOfMaxViewUserNumAboutLive = lastTotalStatisticResult.getLiveTitleOfMaxViewUserNumAboutLive();
				enterpriseNameOfMaxViewUserNumAboutLive = lastTotalStatisticResult
						.getEnterpriseNameOfMaxViewUserNumAboutLive();
				timeOfMaxViewUserNumAboutLive = lastTotalStatisticResult.getTimeOfMaxViewUserNumAboutLive();
			}
			MinuteStatisticResult minuteStatisticResultWithMaxMinuteUserNum = minuteStatisticResultMng
					.getBeanWithMaxMinuteUserNum(null, startTime, endTime);
			if (null != minuteStatisticResultWithMaxMinuteUserNum) {
				Long userNum = minuteStatisticResultWithMaxMinuteUserNum.getUserNum();
				if (null != userNum && userNum > numOfMaxViewUserNumAboutLive) {
					numOfMaxViewUserNumAboutLive = userNum;
					Long liveEventId = minuteStatisticResultWithMaxMinuteUserNum.getLiveEventId();
					timeOfMaxViewUserNumAboutLive = minuteStatisticResultWithMaxMinuteUserNum.getStartTime();
					LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
					if (null != liveInfo) {
						liveTitleOfMaxViewUserNumAboutLive = liveInfo.getLiveTitle();
						Integer enterpriseId = liveInfo.getEnterpriseId();
						EnterpriseInfo enterpriseInfo = enterpriseInfoMng.getBeanById(enterpriseId);
						if (null != enterpriseInfo) {
							enterpriseNameOfMaxViewUserNumAboutLive = enterpriseInfo.getEnterpriseName();
						}
					}
				}
			}

			// 最大注册峰值统计
			Long numOfMaxNewRegisterUserNumAboutLive = 0L;
			Timestamp timeOfMaxNewRegisterUserNumAboutLive = new Timestamp(startTime.getTime());
			String liveTitleOfMaxNewRegisterUserNumAboutLive = "";
			String enterpriseNameOfMaxNewRegisterUserNumAboutLive = "";
			if (null != lastTotalStatisticResult) {
				numOfMaxNewRegisterUserNumAboutLive = lastTotalStatisticResult.getNumOfMaxNewRegisterUserNumAboutLive();
				timeOfMaxNewRegisterUserNumAboutLive = lastTotalStatisticResult
						.getTimeOfMaxNewRegisterUserNumAboutLive();
				liveTitleOfMaxNewRegisterUserNumAboutLive = lastTotalStatisticResult
						.getLiveTitleOfMaxNewRegisterUserNumAboutLive();
				enterpriseNameOfMaxNewRegisterUserNumAboutLive = lastTotalStatisticResult
						.getEnterpriseNameOfMaxNewRegisterUserNumAboutLive();
			}
			LiveStatisticResult liveStatisticResultWithMaxNewRegisterUserNum = liveStatisticResultMng
					.getBeanWithMaxNewRegisterUserNum(startTime, endTime);
			if (null != liveStatisticResultWithMaxNewRegisterUserNum) {
				Long newRegisterUserNumBySingleLive = liveStatisticResultWithMaxNewRegisterUserNum
						.getNewRegisterUserNum();
				if (null != newRegisterUserNumBySingleLive
						&& newRegisterUserNumBySingleLive > numOfMaxNewRegisterUserNumAboutLive) {
					numOfMaxNewRegisterUserNumAboutLive = newRegisterUserNumBySingleLive;
					Long liveId = liveStatisticResultWithMaxNewRegisterUserNum.getId();
					LiveInfo liveInfo = liveInfoMng.getBeanById(liveId);
					if (null != liveInfo) {
						liveTitleOfMaxNewRegisterUserNumAboutLive = liveInfo.getLiveTitle();
						Integer enterpriseId = liveInfo.getEnterpriseId();
						timeOfMaxNewRegisterUserNumAboutLive = liveInfo.getLiveBeginTime();
						EnterpriseInfo enterpriseInfo = enterpriseInfoMng.getBeanById(enterpriseId);
						if (null != enterpriseInfo) {
							enterpriseNameOfMaxNewRegisterUserNumAboutLive = enterpriseInfo.getEnterpriseName();
						}
					}
				}
			}

			// 用户观看时间点分布统计
			Integer[] countViewTimeHourLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 2);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour0To3 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 3);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 5);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour3To6 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour6To9 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 11);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour9To12 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 14);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour12To15 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 15);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 17);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour15To18 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 18);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 21);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour18To21 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 21);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			viewTimeHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			viewTimeHourEndTime = calendar.getTime();
			Long viewTimeHour21To24 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
					viewTimeHourStartTime, viewTimeHourEndTime);

			// 入库
			List<TotalStatisticResult> totalStatisticResultList = totalStatisticResultMng.listByParams(startTime,
					endTime);
			// 加1000方便查询
			Timestamp statisticTime = new Timestamp(startTime.getTime() + 1000);
			if (null != totalStatisticResultList && totalStatisticResultList.size() > 0) {
				TotalStatisticResult totalStatisticResult = totalStatisticResultList.get(0);
				fillData(totalStatisticResult, liveEventTotalNum, liveEnterpriseTotalNum, liveTotalDuration,
						liveViewTotalNum, liveViewUserTotalNum, userTotalNum, viewTotalDuration, viewTotalNum,
						liveLikeTotalNum, liveCommentTotalNum, liveShareTotalNum, newRegisterUserNumAboutLive,
						oldRegisterUserNumAboutLive, newRegisterUserNumAboutDay, oldRegisterUserNumAboutDay,
						newRegisterEnterpriseNum, oldRegisterEnterpriseNum, durationLiveNum0To10,
						durationLiveNum10To100, durationLiveNum100To200, durationLiveNum200To300, durationLiveNum300To,
						durationViewUserNum0To5, durationViewUserNum5To10, durationViewUserNum10To30,
						durationViewUserNum30To60, durationViewUserNum60To, numOfMaxViewUserNumAboutLive,
						timeOfMaxViewUserNumAboutLive, liveTitleOfMaxViewUserNumAboutLive,
						enterpriseNameOfMaxViewUserNumAboutLive, numOfMaxNewRegisterUserNumAboutLive,
						timeOfMaxNewRegisterUserNumAboutLive, liveTitleOfMaxNewRegisterUserNumAboutLive,
						enterpriseNameOfMaxNewRegisterUserNumAboutLive, androidRegisterUserNum, iosRegisterUserNum,
						pcRegisterUserNum, wapRegisterUserNum, otherRegisterUserNum, androidViewUserNum, iosViewUserNum,
						pcViewUserNum, wapViewUserNum, otherViewUserNum, viewTimeHour0To3, viewTimeHour3To6,
						viewTimeHour6To9, viewTimeHour9To12, viewTimeHour12To15, viewTimeHour15To18, viewTimeHour18To21,
						viewTimeHour21To24, statisticTime);
				totalStatisticResultMng.update(totalStatisticResult);
			} else {
				TotalStatisticResult totalStatisticResult = new TotalStatisticResult();
				fillData(totalStatisticResult, liveEventTotalNum, liveEnterpriseTotalNum, liveTotalDuration,
						liveViewTotalNum, liveViewUserTotalNum, userTotalNum, viewTotalDuration, viewTotalNum,
						liveLikeTotalNum, liveCommentTotalNum, liveShareTotalNum, newRegisterUserNumAboutLive,
						oldRegisterUserNumAboutLive, newRegisterUserNumAboutDay, oldRegisterUserNumAboutDay,
						newRegisterEnterpriseNum, oldRegisterEnterpriseNum, durationLiveNum0To10,
						durationLiveNum10To100, durationLiveNum100To200, durationLiveNum200To300, durationLiveNum300To,
						durationViewUserNum0To5, durationViewUserNum5To10, durationViewUserNum10To30,
						durationViewUserNum30To60, durationViewUserNum60To, numOfMaxViewUserNumAboutLive,
						timeOfMaxViewUserNumAboutLive, liveTitleOfMaxViewUserNumAboutLive,
						enterpriseNameOfMaxViewUserNumAboutLive, numOfMaxNewRegisterUserNumAboutLive,
						timeOfMaxNewRegisterUserNumAboutLive, liveTitleOfMaxNewRegisterUserNumAboutLive,
						enterpriseNameOfMaxNewRegisterUserNumAboutLive, androidRegisterUserNum, iosRegisterUserNum,
						pcRegisterUserNum, wapRegisterUserNum, otherRegisterUserNum, androidViewUserNum, iosViewUserNum,
						pcViewUserNum, wapViewUserNum, otherViewUserNum, viewTimeHour0To3, viewTimeHour3To6,
						viewTimeHour6To9, viewTimeHour9To12, viewTimeHour12To15, viewTimeHour15To18, viewTimeHour18To21,
						viewTimeHour21To24, statisticTime);
				totalStatisticResultMng.save(totalStatisticResult);
			}

			// 统计推流地域
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(null, LocationStatisticResult.FLAG_TYPE_TOTAL_BEGIN_LIVE, startTime,
							endTime);
			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						locationStatisticResult.setStatisticTime(new Timestamp(startTime.getTime()));
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, DEFAULT_TOTAL_FLAG_ID,
										LocationStatisticResult.FLAG_TYPE_TOTAL_BEGIN_LIVE, startTime, endTime);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(DEFAULT_TOTAL_FLAG_ID);
							locationStatisticResult.setFlagType(LocationStatisticResult.FLAG_TYPE_TOTAL_BEGIN_LIVE);
							locationStatisticResultMng.save(locationStatisticResult);
						} else {
							Long totalNum = locationStatisticResult.getTotalNum();
							oldLocationStatisticResult.setTotalNum(totalNum);
							locationStatisticResultMng.update(oldLocationStatisticResult);
						}
					}
				}
			}
			// 用户观看统计
			locationStatisticResultList = locationStatisticResultMng.listStatisticResutlByFlag(null,
					LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW, startTime, endTime);
			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						locationStatisticResult.setStatisticTime(new Timestamp(startTime.getTime()));
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, DEFAULT_TOTAL_FLAG_ID,
										LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW, startTime, endTime);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(DEFAULT_TOTAL_FLAG_ID);
							locationStatisticResult.setFlagType(LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW);
							locationStatisticResultMng.save(locationStatisticResult);
						} else {
							Long totalNum = locationStatisticResult.getTotalNum();
							oldLocationStatisticResult.setTotalNum(totalNum);
							locationStatisticResultMng.update(oldLocationStatisticResult);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("TotalStatisticTask error.", e);
		}
	}

	private void fillData(final TotalStatisticResult totalStatisticResult, Long liveEventTotalNum,
			Long liveEnterpriseTotalNum, Long liveTotalDuration, Long liveViewTotalNum, Long liveViewUserTotalNum,
			Long userTotalNum, Long viewTotalDuration, Long viewTotalNum, Long liveLikeTotalNum,
			Long liveCommentTotalNum, Long liveShareTotalNum, Long newRegisterUserNumAboutLive,
			Long oldRegisterUserNumAboutLive, Long newRegisterUserNumAboutDay, Long oldRegisterUserNumAboutDay,
			Long newRegisterEnterpriseNum, Long oldRegisterEnterpriseNum, Long durationLiveNum0To10,
			Long durationLiveNum10To100, Long durationLiveNum100To200, Long durationLiveNum200To300,
			Long durationLiveNum300To, Long durationViewUserNum0To5, Long durationViewUserNum5To10,
			Long durationViewUserNum10To30, Long durationViewUserNum30To60, Long durationViewUserNum60To,
			Long numOfMaxViewUserNumAboutLive, Timestamp timeOfMaxViewUserNumAboutLive,
			String liveTitleOfMaxViewUserNumAboutLive, String enterpriseNameOfMaxViewUserNumAboutLive,
			Long numOfMaxNewRegisterUserNumAboutLive, Timestamp timeOfMaxNewRegisterUserNumAboutLive,
			String liveTitleOfMaxNewRegisterUserNumAboutLive, String enterpriseNameOfMaxNewRegisterUserNumAboutLive,
			Long androidRegisterUserNum, Long iosRegisterUserNum, Long pcRegisterUserNum, Long wapRegisterUserNum,
			Long otherRegisterUserNum, Long androidViewUserNum, Long iosViewUserNum, Long pcViewUserNum,
			Long wapViewUserNum, Long otherViewUserNum, Long viewTimeHour0To3, Long viewTimeHour3To6,
			Long viewTimeHour6To9, Long viewTimeHour9To12, Long viewTimeHour12To15, Long viewTimeHour15To18,
			Long viewTimeHour18To21, Long viewTimeHour21To24, Timestamp statisticTime) {
		if (null != totalStatisticResult) {
			totalStatisticResult.setLiveEventTotalNum(liveEventTotalNum);
			totalStatisticResult.setLiveEnterpriseTotalNum(liveEnterpriseTotalNum);
			totalStatisticResult.setLiveTotalDuration(liveTotalDuration);
			totalStatisticResult.setLiveViewTotalNum(liveViewTotalNum);
			totalStatisticResult.setLiveViewUserTotalNum(liveViewUserTotalNum);
			totalStatisticResult.setUserTotalNum(userTotalNum);
			totalStatisticResult.setViewTotalDuration(viewTotalDuration);
			totalStatisticResult.setViewTotalNum(viewTotalNum);
			totalStatisticResult.setLiveLikeTotalNum(liveLikeTotalNum);
			totalStatisticResult.setLiveCommentTotalNum(liveCommentTotalNum);
			totalStatisticResult.setLiveShareTotalNum(liveShareTotalNum);
			totalStatisticResult.setNewRegisterUserNumAboutLive(newRegisterUserNumAboutLive);
			totalStatisticResult.setOldRegisterUserNumAboutLive(oldRegisterUserNumAboutLive);
			totalStatisticResult.setNewRegisterUserNumAboutDay(newRegisterUserNumAboutDay);
			totalStatisticResult.setOldRegisterUserNumAboutDay(oldRegisterUserNumAboutDay);
			totalStatisticResult.setNewRegisterEnterpriseNum(newRegisterEnterpriseNum);
			totalStatisticResult.setOldRegisterEnterpriseNum(oldRegisterEnterpriseNum);
			totalStatisticResult.setDurationLiveNum0To10(durationLiveNum0To10);
			totalStatisticResult.setDurationLiveNum10To100(durationLiveNum10To100);
			totalStatisticResult.setDurationLiveNum100To200(durationLiveNum100To200);
			totalStatisticResult.setDurationLiveNum200To300(durationLiveNum200To300);
			totalStatisticResult.setDurationLiveNum300To(durationLiveNum300To);
			totalStatisticResult.setDurationViewUserNum0To5(durationViewUserNum0To5);
			totalStatisticResult.setDurationViewUserNum5To10(durationViewUserNum5To10);
			totalStatisticResult.setDurationViewUserNum10To30(durationViewUserNum10To30);
			totalStatisticResult.setDurationViewUserNum30To60(durationViewUserNum30To60);
			totalStatisticResult.setDurationViewUserNum60To(durationViewUserNum60To);
			totalStatisticResult.setNumOfMaxViewUserNumAboutLive(numOfMaxViewUserNumAboutLive);
			totalStatisticResult.setTimeOfMaxViewUserNumAboutLive(timeOfMaxViewUserNumAboutLive);
			totalStatisticResult.setLiveTitleOfMaxViewUserNumAboutLive(liveTitleOfMaxViewUserNumAboutLive);
			totalStatisticResult.setEnterpriseNameOfMaxViewUserNumAboutLive(enterpriseNameOfMaxViewUserNumAboutLive);
			totalStatisticResult.setNumOfMaxNewRegisterUserNumAboutLive(numOfMaxNewRegisterUserNumAboutLive);
			totalStatisticResult.setTimeOfMaxNewRegisterUserNumAboutLive(timeOfMaxNewRegisterUserNumAboutLive);
			totalStatisticResult
					.setLiveTitleOfMaxNewRegisterUserNumAboutLive(liveTitleOfMaxNewRegisterUserNumAboutLive);
			totalStatisticResult
					.setEnterpriseNameOfMaxNewRegisterUserNumAboutLive(enterpriseNameOfMaxNewRegisterUserNumAboutLive);
			totalStatisticResult.setAndroidRegisterUserNum(androidRegisterUserNum);
			totalStatisticResult.setIosRegisterUserNum(iosRegisterUserNum);
			totalStatisticResult.setPcRegisterUserNum(pcRegisterUserNum);
			totalStatisticResult.setWapRegisterUserNum(wapRegisterUserNum);
			totalStatisticResult.setOtherRegisterUserNum(otherRegisterUserNum);
			totalStatisticResult.setAndroidViewUserNum(androidViewUserNum);
			totalStatisticResult.setIosViewUserNum(iosViewUserNum);
			totalStatisticResult.setPcViewUserNum(pcViewUserNum);
			totalStatisticResult.setWapViewUserNum(wapViewUserNum);
			totalStatisticResult.setOtherViewUserNum(otherViewUserNum);
			totalStatisticResult.setViewTimeHour0To3(viewTimeHour0To3);
			totalStatisticResult.setViewTimeHour3To6(viewTimeHour3To6);
			totalStatisticResult.setViewTimeHour6To9(viewTimeHour6To9);
			totalStatisticResult.setViewTimeHour9To12(viewTimeHour9To12);
			totalStatisticResult.setViewTimeHour12To15(viewTimeHour12To15);
			totalStatisticResult.setViewTimeHour15To18(viewTimeHour15To18);
			totalStatisticResult.setViewTimeHour18To21(viewTimeHour18To21);
			totalStatisticResult.setViewTimeHour21To24(viewTimeHour21To24);
			totalStatisticResult.setStatisticTime(statisticTime);
		}
	}
}