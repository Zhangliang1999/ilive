package com.jwzt.statistic.task;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.EnterpriseStatisticResultMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserViewLogMng;
/***
 * 针对企业的统计
 * @author gstars
 *
 */
public class EnterpriseStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	private Integer enterpriseId;
	private Date startTime;
	private Date endTime;

	public EnterpriseStatisticTask(final Integer enterpriseId, final Date startTime, final Date endTime) {
		super();
		this.enterpriseId = enterpriseId;
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
			log.debug("EnterpriseStatisticTask run.");
			EnterpriseStatisticResultMng enterpriseStatisticResultMng = (EnterpriseStatisticResultMng) context
					.getBean("EnterpriseStatisticResultMng");
			LocationStatisticResultMng locationStatisticResultMng = (LocationStatisticResultMng) context
					.getBean("LocationStatisticResultMng");
			MinuteStatisticResultMng minuteStatisticResultMng = (MinuteStatisticResultMng) context
					.getBean("MinuteStatisticResultMng");
			RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
			LiveInfoMng liveInfoMng = (LiveInfoMng) context.getBean("LiveInfoMng");
			UserViewLogMng userViewLogMng = (UserViewLogMng) context.getBean("UserViewLogMng");
			EnterpriseInfoMng enterpriseInfoMng = (EnterpriseInfoMng) context.getBean("EnterpriseInfoMng");
			Integer[] countUserLogTypes = { RequestLog.LOG_TYPE_LIVE_BEGIN };
			// 用户观看来源统计
			countUserLogTypes[0] = RequestLog.LOG_TYPE_USER_ENTER;
			Long androidViewUserNum = requestLogMng.countUserNum(null, enterpriseId, null,
					RequestLog.REQUEST_TYPE_ANDROID, countUserLogTypes, startTime, endTime);
			Long iosViewUserNum = requestLogMng.countUserNum(null, enterpriseId, null, RequestLog.REQUEST_TYPE_IOS,
					countUserLogTypes, startTime, endTime);
			Long pcViewUserNum = requestLogMng.countUserNum(null, enterpriseId, null, RequestLog.REQUEST_TYPE_PC,
					countUserLogTypes, startTime, endTime);
			Long wapViewUserNum = requestLogMng.countUserNum(null, enterpriseId, null, RequestLog.REQUEST_TYPE_WAP,
					countUserLogTypes, startTime, endTime);
			Long otherViewUserNum = 0L;
			// 用户观看总时长、用户观看人次
			Long viewTotalNum = userViewLogMng.countUserNumByParams(null, enterpriseId, null, null, startTime, endTime);
			Long viewTotalDuration = userViewLogMng.sumViewDuarationByParams(null, enterpriseId, startTime, endTime);
			// 直播场次、累计直播时长
			List<LiveInfo> liveInfoList = liveInfoMng.listByParams(null, null, null, enterpriseId, startTime, endTime);
			Long liveEventTotalNum = 0L;
			Long liveTotalDuration = 0L;
			if (null != liveInfoList) {
				liveEventTotalNum += liveInfoList.size();
				for (LiveInfo liveInfo : liveInfoList) {
					if (null != liveInfo) {
						Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
						Timestamp liveEndTime = liveInfo.getLiveEndTime();
						if (null == liveEndTime || null == liveBeginTime) {
							continue;
						}
						Long singleDuration = (liveEndTime.getTime() - liveBeginTime.getTime()) / 1000;
						if (singleDuration > 0) {
							liveTotalDuration += singleDuration;
						}
					}
				}
			}
			Integer[] countLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			// 累计观看人数
			Long liveViewUserTotalNum = requestLogMng.countUserNum(null, enterpriseId, null, null, countLogTypes,
					startTime, endTime);
			// 累计观看次数
			Long liveViewTotalNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime,
					endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_SHARE;
			// 累计分享数
			Long liveShareTotalNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime,
					endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_LIKE;
			// 累计点赞数
			Long liveLikeTotalNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime,
					endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_COMMENT;
			// 累计互动数
			Long liveCommentTotalNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime,
					endTime);
			// 累计粉丝数(关注减去取消关注)
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_CONCERN;
			Long fansNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_UNCONCERN;
			fansNum -= requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_GIFT;
			// 累计礼物数
			Long giftNum = requestLogMng.countRequestNum(null, enterpriseId, null, countLogTypes, startTime, endTime);
			// 累计红包
			Double redPacketMoney = 0.0;
			// 用户观看时长分布统计
			Long durationViewUserNum0To5 = userViewLogMng.countUserNumByParams(null, enterpriseId, 0L * 60, 5L * 60,
					startTime, endTime);
			Long durationViewUserNum5To10 = userViewLogMng.countUserNumByParams(null, enterpriseId, 5L * 60, 10L * 60,
					startTime, endTime);
			Long durationViewUserNum10To30 = userViewLogMng.countUserNumByParams(null, enterpriseId, 10L * 60, 30L * 60,
					startTime, endTime);
			Long durationViewUserNum30To60 = userViewLogMng.countUserNumByParams(null, enterpriseId, 30L * 60, 60L * 60,
					startTime, endTime);
			Long durationViewUserNum60To = userViewLogMng.countUserNumByParams(null, enterpriseId, 60L * 60, null,
					startTime, endTime);
			// 企业推流时间点分布统计
			Integer[] countBeginLiveHourLogTypes = { RequestLog.LOG_TYPE_LIVE_BEGIN };
			Integer[] countViewTimeHourLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 2);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour0To3 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour0To3 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 3);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 5);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour3To6 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour3To6 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 6);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour6To9 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour6To9 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 9);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 11);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour9To12 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour9To12 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 14);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour12To15 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour12To15 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 15);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 17);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour15To18 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour15To18 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 18);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 21);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour18To21 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour18To21 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			calendar.set(Calendar.HOUR_OF_DAY, 21);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			beginLiveHourStartTime = calendar.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			beginLiveHourEndTime = calendar.getTime();
			Long viewTimeHour21To24 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countViewTimeHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);
			Long beginLiveHour21To24 = requestLogMng.countUserNum(null, enterpriseId, null, null,
					countBeginLiveHourLogTypes, beginLiveHourStartTime, beginLiveHourEndTime);

			// 最大观看峰值统计
			Long numOfMaxViewUserNumAboutLive = 0L;
			String liveTitleOfMaxViewUserNumAboutLive = "";
			String enterpriseNameOfMaxViewUserNumAboutLive = "";
			Timestamp timeOfMaxViewUserNumAboutLive = new Timestamp(startTime.getTime());
			EnterpriseStatisticResult lastEnterpriseStatisticResult = enterpriseStatisticResultMng
					.getLastBeanByEndTime(enterpriseId, startTime);
			if (null != lastEnterpriseStatisticResult) {
				numOfMaxViewUserNumAboutLive = lastEnterpriseStatisticResult.getNumOfMaxViewUserNumAboutLive();
				liveTitleOfMaxViewUserNumAboutLive = lastEnterpriseStatisticResult
						.getLiveTitleOfMaxViewUserNumAboutLive();
				enterpriseNameOfMaxViewUserNumAboutLive = lastEnterpriseStatisticResult
						.getEnterpriseNameOfMaxViewUserNumAboutLive();
				timeOfMaxViewUserNumAboutLive = lastEnterpriseStatisticResult.getTimeOfMaxViewUserNumAboutLive();
			}
			
			
			System.out.println("#################:"+enterpriseNameOfMaxViewUserNumAboutLive+":"+liveTitleOfMaxViewUserNumAboutLive);
			
			MinuteStatisticResult minuteStatisticResultWithMaxMinuteUserNum = minuteStatisticResultMng
					.getBeanWithMaxMinuteUserNumByEnterpriseId(enterpriseId.longValue(), startTime, endTime);
			System.out.println("minuteStatisticResultWithMaxMinuteUserNum:"+minuteStatisticResultWithMaxMinuteUserNum);
			
			if (null != minuteStatisticResultWithMaxMinuteUserNum) {
				Long userNum = minuteStatisticResultWithMaxMinuteUserNum.getUserNum();
				System.out.println("userNum:"+userNum+":");
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
			System.out.println("#################enterpriseId.longValue():"+enterpriseId.longValue());
			System.out.println("$$$$$$$$$$$$$4:"+enterpriseNameOfMaxViewUserNumAboutLive+":"+liveTitleOfMaxViewUserNumAboutLive);
			// 加1000方便查询
			Timestamp statisticTime = new Timestamp(startTime.getTime() + 1000);
			List<EnterpriseStatisticResult> enterpriseStatisticResultList = enterpriseStatisticResultMng
					.listByParams(enterpriseId, startTime, endTime);
			if (null != enterpriseStatisticResultList && enterpriseStatisticResultList.size() > 0) {
				EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultList.get(0);
				fillData(enterpriseStatisticResult, enterpriseId, liveEventTotalNum, liveTotalDuration,
						liveViewTotalNum, liveViewUserTotalNum, liveLikeTotalNum, liveCommentTotalNum,
						liveShareTotalNum, viewTotalDuration, viewTotalNum, androidViewUserNum, iosViewUserNum,
						pcViewUserNum, wapViewUserNum, otherViewUserNum, numOfMaxViewUserNumAboutLive,
						timeOfMaxViewUserNumAboutLive, liveTitleOfMaxViewUserNumAboutLive,
						enterpriseNameOfMaxViewUserNumAboutLive, durationViewUserNum0To5, durationViewUserNum5To10,
						durationViewUserNum10To30, durationViewUserNum30To60, durationViewUserNum60To, viewTimeHour0To3,
						viewTimeHour3To6, viewTimeHour6To9, viewTimeHour9To12, viewTimeHour12To15, viewTimeHour15To18,
						viewTimeHour18To21, viewTimeHour21To24, beginLiveHour0To3, beginLiveHour3To6, beginLiveHour6To9,
						beginLiveHour9To12, beginLiveHour12To15, beginLiveHour15To18, beginLiveHour18To21,
						beginLiveHour21To24, fansNum, redPacketMoney, giftNum, statisticTime);
				enterpriseStatisticResultMng.update(enterpriseStatisticResult);
			} else {
				EnterpriseStatisticResult enterpriseStatisticResult = new EnterpriseStatisticResult();
				fillData(enterpriseStatisticResult, enterpriseId, liveEventTotalNum, liveTotalDuration,
						liveViewTotalNum, liveViewUserTotalNum, liveLikeTotalNum, liveCommentTotalNum,
						liveShareTotalNum, viewTotalDuration, viewTotalNum, androidViewUserNum, iosViewUserNum,
						pcViewUserNum, wapViewUserNum, otherViewUserNum, numOfMaxViewUserNumAboutLive,
						timeOfMaxViewUserNumAboutLive, liveTitleOfMaxViewUserNumAboutLive,
						enterpriseNameOfMaxViewUserNumAboutLive, durationViewUserNum0To5, durationViewUserNum5To10,
						durationViewUserNum10To30, durationViewUserNum30To60, durationViewUserNum60To, viewTimeHour0To3,
						viewTimeHour3To6, viewTimeHour6To9, viewTimeHour9To12, viewTimeHour12To15, viewTimeHour15To18,
						viewTimeHour18To21, viewTimeHour21To24, beginLiveHour0To3, beginLiveHour3To6, beginLiveHour6To9,
						beginLiveHour9To12, beginLiveHour12To15, beginLiveHour15To18, beginLiveHour18To21,
						beginLiveHour21To24, fansNum, redPacketMoney, giftNum, statisticTime);
				enterpriseStatisticResultMng.save(enterpriseStatisticResult);
			}
			// 统计推流地域
			List<LocationStatisticResult> beginLiveLocationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(Long.valueOf(enterpriseId),
							LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE, startTime, endTime);
			if (null != beginLiveLocationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : beginLiveLocationStatisticResultList) {
					if (null != locationStatisticResult) {
						locationStatisticResult.setStatisticTime(new Timestamp(startTime.getTime()));
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, Long.valueOf(enterpriseId),
										LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE, startTime, endTime);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(Long.valueOf(enterpriseId));
							locationStatisticResult
									.setFlagType(LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE);
							locationStatisticResultMng.save(locationStatisticResult);
						} else {
							Long totalNum = locationStatisticResult.getTotalNum();
							oldLocationStatisticResult.setTotalNum(totalNum);
							locationStatisticResultMng.update(oldLocationStatisticResult);
						}
					}
				}
			}
			// 统计观看地域
			List<LocationStatisticResult> viewLocationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(Long.valueOf(enterpriseId),
							LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW, startTime, endTime);
			if (null != viewLocationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : viewLocationStatisticResultList) {
					if (null != locationStatisticResult) {
						locationStatisticResult.setStatisticTime(new Timestamp(startTime.getTime()));
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, Long.valueOf(enterpriseId),
										LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW, startTime, endTime);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(Long.valueOf(enterpriseId));
							locationStatisticResult.setFlagType(LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW);
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
			log.error("EnterpriseStatisticTask error.", e);
		}
	}

	private void fillData(EnterpriseStatisticResult enterpriseStatisticResult, Integer enterpriseId,
			Long liveEventTotalNum, Long liveTotalDuration, Long liveViewTotalNum, Long liveViewUserTotalNum,
			Long liveLikeTotalNum, Long liveCommentTotalNum, Long liveShareTotalNum, Long viewTotalDuration,
			Long viewTotalNum, Long androidViewUserNum, Long iosViewUserNum, Long pcViewUserNum, Long wapViewUserNum,
			Long otherViewUserNum, Long numOfMaxViewUserNumAboutLive, Timestamp timeOfMaxViewUserNumAboutLive,
			String liveTitleOfMaxViewUserNumAboutLive, String enterpriseNameOfMaxViewUserNumAboutLive,
			Long durationViewUserNum0To5, Long durationViewUserNum5To10, Long durationViewUserNum10To30,
			Long durationViewUserNum30To60, Long durationViewUserNum60To, Long viewTimeHour0To3, Long viewTimeHour3To6,
			Long viewTimeHour6To9, Long viewTimeHour9To12, Long viewTimeHour12To15, Long viewTimeHour15To18,
			Long viewTimeHour18To21, Long viewTimeHour21To24, Long beginLiveHour0To3, Long beginLiveHour3To6,
			Long beginLiveHour6To9, Long beginLiveHour9To12, Long beginLiveHour12To15, Long beginLiveHour15To18,
			Long beginLiveHour18To21, Long beginLiveHour21To24, Long fansNum, Double redPacketMoney, Long giftNum,
			Timestamp statisticTime) {
		if (null != enterpriseStatisticResult) {
			enterpriseStatisticResult.setEnterpriseId(enterpriseId);
			enterpriseStatisticResult.setLiveEventTotalNum(liveEventTotalNum);
			enterpriseStatisticResult.setLiveTotalDuration(liveTotalDuration);
			enterpriseStatisticResult.setLiveViewTotalNum(liveViewTotalNum);
			enterpriseStatisticResult.setLiveViewUserTotalNum(liveViewUserTotalNum);
			enterpriseStatisticResult.setViewTotalDuration(viewTotalDuration);
			enterpriseStatisticResult.setViewTotalNum(viewTotalNum);
			enterpriseStatisticResult.setLiveLikeTotalNum(liveLikeTotalNum);
			enterpriseStatisticResult.setLiveCommentTotalNum(liveCommentTotalNum);
			enterpriseStatisticResult.setLiveShareTotalNum(liveShareTotalNum);
			enterpriseStatisticResult.setAndroidViewUserNum(androidViewUserNum);
			enterpriseStatisticResult.setIosViewUserNum(iosViewUserNum);
			enterpriseStatisticResult.setPcViewUserNum(pcViewUserNum);
			enterpriseStatisticResult.setWapViewUserNum(wapViewUserNum);
			enterpriseStatisticResult.setOtherViewUserNum(otherViewUserNum);
			enterpriseStatisticResult.setNumOfMaxViewUserNumAboutLive(numOfMaxViewUserNumAboutLive);
			enterpriseStatisticResult.setTimeOfMaxViewUserNumAboutLive(timeOfMaxViewUserNumAboutLive);
			enterpriseStatisticResult.setLiveTitleOfMaxViewUserNumAboutLive(liveTitleOfMaxViewUserNumAboutLive);
			enterpriseStatisticResult
					.setEnterpriseNameOfMaxViewUserNumAboutLive(enterpriseNameOfMaxViewUserNumAboutLive);
			enterpriseStatisticResult.setDurationViewUserNum0To5(durationViewUserNum0To5);
			enterpriseStatisticResult.setDurationViewUserNum5To10(durationViewUserNum5To10);
			enterpriseStatisticResult.setDurationViewUserNum10To30(durationViewUserNum10To30);
			enterpriseStatisticResult.setDurationViewUserNum30To60(durationViewUserNum30To60);
			enterpriseStatisticResult.setDurationViewUserNum60To(durationViewUserNum60To);
			enterpriseStatisticResult.setViewTimeHour0To3(viewTimeHour0To3);
			enterpriseStatisticResult.setViewTimeHour3To6(viewTimeHour3To6);
			enterpriseStatisticResult.setViewTimeHour6To9(viewTimeHour6To9);
			enterpriseStatisticResult.setViewTimeHour9To12(viewTimeHour9To12);
			enterpriseStatisticResult.setViewTimeHour12To15(viewTimeHour12To15);
			enterpriseStatisticResult.setViewTimeHour15To18(viewTimeHour15To18);
			enterpriseStatisticResult.setViewTimeHour18To21(viewTimeHour18To21);
			enterpriseStatisticResult.setViewTimeHour21To24(viewTimeHour21To24);
			enterpriseStatisticResult.setBeginLiveHour0To3(beginLiveHour0To3);
			enterpriseStatisticResult.setBeginLiveHour3To6(beginLiveHour3To6);
			enterpriseStatisticResult.setBeginLiveHour6To9(beginLiveHour6To9);
			enterpriseStatisticResult.setBeginLiveHour9To12(beginLiveHour9To12);
			enterpriseStatisticResult.setBeginLiveHour12To15(beginLiveHour12To15);
			enterpriseStatisticResult.setBeginLiveHour15To18(beginLiveHour15To18);
			enterpriseStatisticResult.setBeginLiveHour18To21(beginLiveHour18To21);
			enterpriseStatisticResult.setBeginLiveHour21To24(beginLiveHour21To24);
			enterpriseStatisticResult.setFansNum(fansNum);
			enterpriseStatisticResult.setRedPacketMoney(redPacketMoney);
			enterpriseStatisticResult.setGiftNum(giftNum);
			enterpriseStatisticResult.setStatisticTime(statisticTime);
		}

	}
}