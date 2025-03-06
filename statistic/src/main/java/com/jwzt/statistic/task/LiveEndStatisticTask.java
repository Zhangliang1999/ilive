package com.jwzt.statistic.task;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.IiveEventCurrentMax;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.manager.IiveEventCurrentMaxMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.UserViewLogMng;

/**
 * 直播结束统计
 * 
 * @author ysf
 *
 */

public class LiveEndStatisticTask extends TimerTask {
	private static final Logger log = LogManager.getLogger();
	private Long liveEventId;

	public LiveEndStatisticTask(Long liveEventId) {
		super();
		this.liveEventId = liveEventId;
	}

	@Override
	public void run() {
		try {
			
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			log.debug("LiveEndStatisticTask run. liveEventId={} ", liveEventId);

			LiveInfoMng liveInfoMng = (LiveInfoMng) context.getBean("LiveInfoMng");
			RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
			LiveStatisticResultMng liveStatisticResultMng = (LiveStatisticResultMng) context
					.getBean("LiveStatisticResultMng");
			LocationStatisticResultMng locationStatisticResultMng = (LocationStatisticResultMng) context
					.getBean("LocationStatisticResultMng");
			UserViewLogMng userViewLogMng = (UserViewLogMng) context.getBean("UserViewLogMng");
			UserInfoMng userInfoMng = (UserInfoMng) context.getBean("UserInfoMng");
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			IiveEventCurrentMaxMng liveEventCurrentMaxMng = context.getBean(IiveEventCurrentMaxMng.class);
			if (null == liveInfo) {
				log.warn("liveInfo is null, liveEventId={}", liveEventId);
				return;
			}
			Long liveId = liveInfo.getId();
			Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			log.debug("最后一次统计，统计完成后更新直播人数峰值信息。liveEventId={}", liveEventId);
			MinuteStatisticResultMng minuteStatisticResultMng = (MinuteStatisticResultMng) context
					.getBean("MinuteStatisticResultMng");
			MinuteStatisticResult minuteStatisticResultOfMaxMinuteUserNum = minuteStatisticResultMng
					.getBeanWithMaxMinuteUserNum(liveEventId, null, null);
			Long maxMinute = 0L;
			Long maxMinuteUserNum = 0L;
			if (null != minuteStatisticResultOfMaxMinuteUserNum) {
				Timestamp startTime = minuteStatisticResultOfMaxMinuteUserNum.getStartTime();
				maxMinute = (startTime.getTime() - liveBeginTime.getTime()) / 1000;
				if (maxMinute < 0) {
					maxMinute = 0L;
				}
				maxMinuteUserNum = minuteStatisticResultOfMaxMinuteUserNum.getUserNum();
			}
			Integer[] countUserLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Long otherUserNum = requestLogMng.countRequestNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_OTHER,
					countUserLogTypes, null, null);
			Long androidUserNum = requestLogMng.countRequestNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_ANDROID,
					countUserLogTypes, null, null);
			Long iosUserNum = requestLogMng.countRequestNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_IOS,
					countUserLogTypes, null, null);
			Long pcUserNum = requestLogMng.countRequestNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_PC,
					countUserLogTypes, null, null);
			Long wapUserNum = requestLogMng.countRequestNum(liveEventId, null, null, RequestLog.REQUEST_TYPE_WAP,
					countUserLogTypes, null, null);
			Long userNum = requestLogMng.countUserNum(liveEventId, null, null, null,
					countUserLogTypes, null, null);

			Long viewTotalDuration = userViewLogMng.sumViewDuarationByParams(liveEventId, null, null, null);

			Integer[] countLogTypes = { RequestLog.LOG_TYPE_USER_SHARE };
			Long shareNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_LIKE;
			Long likeNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_COMMENT;
			Long commentNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_ENTER;
			Long viewNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_SIGN;
			Long signNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null, null);

			try {
				if (null == commentNum || commentNum == 0) {
					commentNum = liveStatisticResultMng.countCommentNumForLiveDatabase(liveEventId);
				}
				if (null == likeNum || likeNum == 0) {
					likeNum = liveStatisticResultMng.countLikeNumForLiveDatabase(liveEventId);
				}
			} catch (Exception e) {
			}
			

			Long newRegisterUserNum = 0L;
			Long oldRegisterUserNum = 0L;
			Integer[] listLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			List<RequestLog> requestLogList = requestLogMng.listByParams(liveEventId, null, null, listLogTypes, null,
					liveBeginTime, liveEndTime, false, false, false);
			if (null != requestLogList && requestLogList.size() > 0) {
				Long[] userIds = new Long[requestLogList.size()];
				for (int i = 0; i < requestLogList.size(); i++) {
					try {
						RequestLog requestLog = requestLogList.get(i);
						Integer userType = requestLog.getUserType();
						if (null != requestLog && RequestLog.USER_TYPE_REGISTERED_USER.equals(userType)) {
							userIds[i] = Long.parseLong(requestLog.getUserId());
						}
					} catch (Exception e) {
					}
				}
				List<UserInfo> userInfoList = userInfoMng.listByParams(userIds, null, null, null);
				if (null != userInfoList) {
					for (UserInfo userInfo : userInfoList) {
						if (null != userInfo) {
							Timestamp registerTime = userInfo.getRegisterTime();
							if (null != registerTime && null != liveBeginTime && null != liveEndTime
									&& registerTime.getTime() >= liveBeginTime.getTime()
									&& registerTime.getTime() <= liveEndTime.getTime()) {
								newRegisterUserNum++;
							}
						}
					}
				}
				if (userNum > newRegisterUserNum) {
					oldRegisterUserNum = userNum - newRegisterUserNum;
				}
			}

			try {
				Integer enter = requestLogMng.getNumByEvent(liveEventId, 1);
				Integer leave = requestLogMng.getNumByEvent(liveEventId, 2);
				IiveEventCurrentMax max = liveEventCurrentMaxMng.getByEventId(liveEventId);
				if (max == null) {
					max = new IiveEventCurrentMax();
					max.setiLiveEvent(liveEventId);
					max.setMaxPeople(Math.abs(enter-leave));
					max.setThisTime(System.currentTimeMillis());
					liveEventCurrentMaxMng.save(max);
				}else {
					if ((enter - leave)>max.getMaxPeople()) {
						max.setMaxPeople(Math.abs(enter-leave));
						max.setThisTime(System.currentTimeMillis());
						liveEventCurrentMaxMng.update(max);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 用户观看时长分布统计
			Long viewDuraionUserNum0To5 = userViewLogMng.countUserNumByParams(liveEventId, null, 0L * 60, 5L * 60, null,
					null);
			Long viewDuraionUserNum5To10 = userViewLogMng.countUserNumByParams(liveEventId, null, 5L * 60, 10L * 60,
					null, null);
			Long viewDuraionUserNum10To30 = userViewLogMng.countUserNumByParams(liveEventId, null, 10L * 60, 30L * 60,
					null, null);
			Long viewDuraionUserNum30To60 = userViewLogMng.countUserNumByParams(liveEventId, null, 30L * 60, 60L * 60,
					null, null);
			Long viewDuraionUserNum60To = userViewLogMng.countUserNumByParams(liveEventId, null, 60L * 60, null, null,
					null);

			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			Double redPacketMoney = 0.0;
			Long giftNum = 0L;
			if (null == liveStatisticResult) {
				liveStatisticResult = new LiveStatisticResult(liveId);
				fillData(liveStatisticResult, maxMinute, maxMinuteUserNum, userNum, viewTotalDuration, androidUserNum,
						iosUserNum, pcUserNum, wapUserNum, otherUserNum, shareNum, likeNum, commentNum,
						newRegisterUserNum, oldRegisterUserNum, viewDuraionUserNum0To5, viewDuraionUserNum5To10,
						viewDuraionUserNum10To30, viewDuraionUserNum30To60, viewDuraionUserNum60To, viewNum, signNum,
						redPacketMoney, giftNum);
				liveStatisticResultMng.save(liveStatisticResult);
			} else {
				fillData(liveStatisticResult, maxMinute, maxMinuteUserNum, userNum, viewTotalDuration, androidUserNum,
						iosUserNum, pcUserNum, wapUserNum, otherUserNum, shareNum, likeNum, commentNum,
						newRegisterUserNum, oldRegisterUserNum, viewDuraionUserNum0To5, viewDuraionUserNum5To10,
						viewDuraionUserNum10To30, viewDuraionUserNum30To60, viewDuraionUserNum60To, viewNum, signNum,
						redPacketMoney, giftNum);
				liveStatisticResultMng.update(liveStatisticResult);
			}

			// 开始区域统计
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(liveEventId, LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, null,
							null);

			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, liveEventId,
										LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, null, null);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(liveEventId);
							locationStatisticResult.setFlagType(LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW);
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
			log.error("MinuteStatisticTask error.", e);
		}
	}

	private void fillData(LiveStatisticResult liveStatisticResult, Long maxMinute, Long maxMinuteUserNum, Long userNum,
			Long viewTotalDuration, Long androidUserNum, Long iosUserNum, Long pcUserNum, Long wapUserNum,
			Long otherUserNum, Long shareNum, Long likeNum, Long commentNum, Long newRegisterUserNum,
			Long oldRegisterUserNum, Long viewDuraionUserNum0To5, Long viewDuraionUserNum5To10,
			Long viewDuraionUserNum10To30, Long viewDuraionUserNum30To60, Long viewDuraionUserNum60To, Long viewNum,
			Long signNum, Double redPacketMoney, Long giftNum) {
		if (null != liveStatisticResult) {
			liveStatisticResult.setMaxMinute(maxMinute);
			liveStatisticResult.setMaxMinuteUserNum(maxMinuteUserNum);
			liveStatisticResult.setUserNum(userNum);
			liveStatisticResult.setAndroidUserNum(androidUserNum);
			liveStatisticResult.setIosUserNum(iosUserNum);
			liveStatisticResult.setPcUserNum(pcUserNum);
			liveStatisticResult.setWapUserNum(wapUserNum);
			liveStatisticResult.setShareNum(shareNum);
			liveStatisticResult.setLikeNum(likeNum);
			liveStatisticResult.setCommentNum(commentNum);
			liveStatisticResult.setNewRegisterUserNum(newRegisterUserNum);
			liveStatisticResult.setOldRegisterUserNum(oldRegisterUserNum);
			liveStatisticResult.setViewDuraionUserNum0To5(viewDuraionUserNum0To5);
			liveStatisticResult.setViewDuraionUserNum5To10(viewDuraionUserNum5To10);
			liveStatisticResult.setViewDuraionUserNum10To30(viewDuraionUserNum10To30);
			liveStatisticResult.setViewDuraionUserNum30To60(viewDuraionUserNum30To60);
			liveStatisticResult.setViewDuraionUserNum60To(viewDuraionUserNum60To);
			liveStatisticResult.setViewNum(viewNum);
			liveStatisticResult.setSignNum(signNum);
			liveStatisticResult.setRedPacketMoney(redPacketMoney);
			liveStatisticResult.setGiftNum(giftNum);
			liveStatisticResult.setOtherUserNum(otherUserNum);
			liveStatisticResult.setViewTotalDuration(viewTotalDuration);
		}
	}

}