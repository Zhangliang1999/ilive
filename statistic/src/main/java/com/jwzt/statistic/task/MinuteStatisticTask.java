package com.jwzt.statistic.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.IiveEventCurrentMax;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.entity.UserViewLog;
import com.jwzt.statistic.manager.IiveEventCurrentMaxMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.UserViewLogMng;
import com.jwzt.statistic.pool.LiveEndStatisticPool;
import com.jwzt.statistic.pool.UserSyncPool;

/**
 * 每分钟统计观看用户数
 * @author gstars
 *
 */
public class MinuteStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();
	private Long liveEventId;
	private Integer enterpriseId;

	public MinuteStatisticTask(Long liveEventId) {
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
			log.debug("MinuteStatisticPool run. liveEventId={}", liveEventId);
			LiveInfoMng liveInfoMng = (LiveInfoMng) context.getBean("LiveInfoMng");
			UserViewLogMng userViewLogMng = (UserViewLogMng) context.getBean("UserViewLogMng");
			RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
			UserInfoMng userInfoMng = (UserInfoMng) context.getBean("UserInfoMng");
			MinuteStatisticResultMng minuteStatisticResultMng = (MinuteStatisticResultMng) context
					.getBean("MinuteStatisticResultMng");
			IiveEventCurrentMaxMng liveEventCurrentMaxMng = context.getBean(IiveEventCurrentMaxMng.class);
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				log.warn("liveInfo is null, liveEventId={}", liveEventId);
				return;
			}
			
			enterpriseId = liveInfo.getEnterpriseId();
			boolean isFirstStatistic = false;
			Timestamp liveLastStatisticTime = liveInfo.getLastStatisticTime();
			if (null == liveLastStatisticTime) {
				Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
				liveLastStatisticTime = liveBeginTime;
				isFirstStatistic = true;
			}
			boolean isLastStatistic = false;
			DateTime statisticStartTime = new DateTime(liveLastStatisticTime);
			DateTime statisticEndTime;
			Timestamp liveEndTime = liveInfo.getLiveEndTime();
			if (null != liveEndTime) {
				statisticEndTime = new DateTime(liveEndTime);
				isLastStatistic = true;
			} else {
				statisticEndTime = new DateTime().plusMinutes(-1).secondOfMinute().withMinimumValue();
			}
			try {
				log.debug("MinuteStatisticPool time:{}~{}", statisticStartTime.toString("yyyy-MM-dd HH:mm:ss"),
						statisticEndTime.toString("yyyy-MM-dd HH:mm:ss"));
				log.debug("开始初始化上次统计的用户累计观看时长");

				Map<String, UserViewLog> userViewLogMap = null;
				if (isFirstStatistic) {
					userViewLogMap = new HashMap<String, UserViewLog>();
				} else {
					userViewLogMap = initUserViewTimeResut(userViewLogMng);
				}
				log.debug("开始进行统计，遍历每分钟的日志");
				DateTime minuteStatisticStartTime = statisticStartTime;
				DateTime minuteStatisticEndTime = null;
				while (null == minuteStatisticEndTime
						|| minuteStatisticEndTime.getMillis() < statisticEndTime.getMillis()) {

					if (minuteStatisticStartTime.plusMinutes(1).getMillis() < statisticEndTime.getMillis()) {
						minuteStatisticEndTime = minuteStatisticStartTime.plusMinutes(1).secondOfMinute()
								.withMinimumValue();
					} else {
						minuteStatisticEndTime = statisticEndTime;
					}
					log.debug("开始统计:{}~{}", minuteStatisticStartTime.toString("yyyy-MM-dd HH:mm:ss"),
							minuteStatisticEndTime.toString("yyyy-MM-dd HH:mm:ss"));
					
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
					
					
					Long viewNum = 0L;
					
					Integer[] logTypes = { RequestLog.LOG_TYPE_USER_ENTER, RequestLog.LOG_TYPE_USER_LEAVE };
					List<RequestLog> minuteLogList = requestLogMng.listByParams(liveEventId, null, null, logTypes, null,
							minuteStatisticStartTime.toDate(), minuteStatisticEndTime.toDate(), false, false, false);
					if (null != minuteLogList) {
						for (RequestLog requestLog : minuteLogList) {
							if (null != requestLog) {
								statisticUserViewTime(minuteStatisticStartTime, minuteStatisticEndTime, userViewLogMap,
										requestLog);
								//Integer logType = requestLog.getLogType();
								//if (RequestLog.LOG_TYPE_USER_ENTER.equals(logType)) {
								//	viewNum++;
								//}else {
								//	viewNum--;
								//}

							}
						}
					}
					
					//Integer enterviewNum = requestLogMng.getNumByEvent(liveEventId, 1);
					//Integer leaveviewNum = requestLogMng.getNumByEvent(liveEventId, 2);
					
					
					Integer[] countLogTypes = { RequestLog.LOG_TYPE_USER_ENTER };
					Long viewTotalNum = requestLogMng.countRequestNum(liveEventId, null, null, countLogTypes, null,
							minuteStatisticEndTime.toDate());
					Long userTotalNum = requestLogMng.countUserNum(liveEventId, null, null, null, countLogTypes, null,
							minuteStatisticEndTime.toDate());
					
					Integer[] leaveCountLogTypes = { RequestLog.LOG_TYPE_USER_LEAVE };
					//因为log统计有一个问题，微信连接websocket时会出现连续连接和断开，整个持续连接时间不到1分钟导致，如果按照正常统计离开人数会导致此人在这一分钟内连接断开但是没有统计进入并发人数导致并发不正确，这里处理统计离开人员只统计2分钟前的离开人员，这样可以记录本分钟内进入的人员为在线用户。
					Long leaveViewTotalNum = requestLogMng.countRequestNum(liveEventId, null, null, leaveCountLogTypes, null,
							minuteStatisticEndTime.plus(-120000).toDate());
					//Long leaveUserTotalNum = requestLogMng.countUserNum(liveEventId, null, null, null, leaveCountLogTypes, null,
					//		minuteStatisticEndTime.toDate());
					
					viewNum = viewTotalNum -leaveViewTotalNum;
					if(viewNum<0)viewNum=0L;
					
					// 统计每分钟人数
					List<Long> userIdList = new ArrayList<Long>();
					Long userNum = 0L;

					//Integer enter = requestLogMng.getNumByEvent(liveEventId, 1,true);
					//Integer leave = requestLogMng.getNumByEvent(liveEventId, 2,true);
					
					userNum=viewNum;
					// 同步用户
					if (userIdList.size() > 0) {
						UserSyncPool.execute(userIdList);
					}
					// 判断更新，减1000方便查询
					MinuteStatisticResult minuteStatisticResult = minuteStatisticResultMng.getBeanByParams(liveEventId,
							minuteStatisticStartTime.plus(-1000).toDate(), minuteStatisticEndTime.toDate());
					if (null == minuteStatisticResult) {
						minuteStatisticResult = new MinuteStatisticResult();
						minuteStatisticResult.setLiveEventId(liveEventId);
						minuteStatisticResult.setUserNum(userNum);
						minuteStatisticResult.setViewNum(viewNum);
						minuteStatisticResult.setUserTotalNum(userTotalNum);
						minuteStatisticResult.setViewTotalNum(viewTotalNum);
						minuteStatisticResult.setStartTime(new Timestamp(minuteStatisticStartTime.getMillis()));
						minuteStatisticResult.setEndTime(new Timestamp(minuteStatisticEndTime.getMillis()));
						minuteStatisticResultMng.save(minuteStatisticResult);
					} else {
						minuteStatisticResult.setLiveEventId(liveEventId);
						minuteStatisticResult.setUserNum(userNum);
						minuteStatisticResult.setViewNum(viewNum);
						minuteStatisticResult.setUserTotalNum(userTotalNum);
						minuteStatisticResult.setViewTotalNum(viewTotalNum);
						minuteStatisticResult.setStartTime(new Timestamp(minuteStatisticStartTime.getMillis()));
						minuteStatisticResult.setEndTime(new Timestamp(minuteStatisticEndTime.getMillis()));
						minuteStatisticResultMng.update(minuteStatisticResult);
					}

					minuteStatisticStartTime = minuteStatisticEndTime;
				}
				// 时长结果入库
				saveOrUpdateUserViewTime(userViewLogMap, userViewLogMng, statisticEndTime);
				if (isLastStatistic) {
					log.debug("最后一次统计，统计完成后更新直播人数峰值信息。liveEventId={}", liveEventId);
					LiveEndStatisticPool.execute(liveEventId);
				}
			} catch (Exception e) {
				log.error("MinuteStatisticTask error.", e);
			}
			// 更新直播的信息
			liveInfo.setStatisticing(false);
			//liveInfo.setIsFinshed(1);
			liveInfo.setLastStatisticTime(new Timestamp(statisticEndTime.getMillis()));
			try {
				if (liveInfo.getLastStatisticTime().getTime()>=liveInfo.getLiveEndTime().getTime()) {
					liveInfo.setIsFinshed(1);
				}
			} catch (Exception e) {
				//log.error("MinuteStatisticTask 时间判断.", e);
			}
			liveInfoMng.update(liveInfo);
		} catch (Exception e) {
			log.error("MinuteStatisticTask error.", e);
		}finally {
			SegmentStatisticTask.eventSet.remove(liveEventId);
		}
	}

	private Map<String, UserViewLog> initUserViewTimeResut(final UserViewLogMng userViewLogMng) {
		Map<String, UserViewLog> userViewLogMap = new HashMap<String, UserViewLog>();
		List<UserViewLog> userViewLogList = userViewLogMng.listByLiveEventId(liveEventId);
		if (null != userViewLogList) {
			for (UserViewLog userViewLog : userViewLogList) {
				if (null != userViewLog) {
					String userId = userViewLog.getUserId();
					Integer userType = userViewLog.getUserType();
					Integer requestType = userViewLog.getRequestType();
					userViewLogMap.put(requestType + "_" + userType + "_" + userId, userViewLog);
				}
			}
		}
		log.debug("初始化完成，userViewLogMap.size()={}", userViewLogMap.size());
		return userViewLogMap;
	}

	private void statisticUserViewTime(DateTime minuteStatisticStartTime, DateTime minuteStatisticEndTime,
			Map<String, UserViewLog> userViewLogMap, RequestLog requestLog) {
		String userId = requestLog.getUserId();
		Integer userType = requestLog.getUserType();
		Integer requestType = requestLog.getRequestType();
		Long ipCode = requestLog.getIpCode();
		UserViewLog userViewLog = userViewLogMap.get(requestType + "_" + userType + "_" + userId);
		Timestamp logCreateTime = requestLog.getCreateTime();
		Integer logType = requestLog.getLogType();
		if (RequestLog.LOG_TYPE_USER_ENTER.equals(logType)) {
			// 用户进入直播间的日志
			if (null != userViewLog) {
				// 用户重复进入的情况
				Timestamp lastStatisticTime = userViewLog.getLastStatisticTime();
				if (logCreateTime.getTime() > lastStatisticTime.getTime()) {
					//如果是进入状态说明之前有退出的可能没有记录到，这时候就处理为一直在线记录在线时间
					if(userViewLog.getStatus().equals(userViewLog.STATUS_ENTER)){
						Timestamp resultLastStatisticTime = userViewLog.getLastStatisticTime();
						Long viewTotalTime = userViewLog.getViewTotalTime();
						Long addViewTotalTime = logCreateTime.getTime() - resultLastStatisticTime.getTime();
						Long newViewTotalTime = viewTotalTime + (addViewTotalTime > 0 ? addViewTotalTime : 0) / 1000;
						userViewLog.setViewTotalTime(newViewTotalTime);
					}
					userViewLog.setLastStatisticTime(logCreateTime);
					userViewLog.setStatus(UserViewLog.STATUS_ENTER);
				}
			} else {
				// 用户第一次进入的情况
				userViewLog = new UserViewLog(liveEventId, userId, userType, logCreateTime, 0L);
				userViewLog.setEnterpriseId(enterpriseId);
				userViewLog.setRequestType(requestType);
				userViewLog.setStatus(UserViewLog.STATUS_ENTER);
				userViewLog.setIpCode(ipCode);
				userViewLogMap.put(requestType + "_" + userType + "_" + userId, userViewLog);
			}
		} else if (RequestLog.LOG_TYPE_USER_LEAVE.equals(logType)) {
			// 用户离开直播间的日志，计算观看时长
			if (null != userViewLog && UserViewLog.STATUS_ENTER.equals(userViewLog.getStatus())) {
				Timestamp lastStatisticTime = userViewLog.getLastStatisticTime();
				if (logCreateTime.getTime() > lastStatisticTime.getTime()) {
					Timestamp resultLastStatisticTime = userViewLog.getLastStatisticTime();
					Long viewTotalTime = userViewLog.getViewTotalTime();
					Long addViewTotalTime = logCreateTime.getTime() - resultLastStatisticTime.getTime();
					Long newViewTotalTime = viewTotalTime + (addViewTotalTime > 0 ? addViewTotalTime : 0) / 1000;
					userViewLog.setViewTotalTime(newViewTotalTime);
					userViewLog.setLastStatisticTime(logCreateTime);
					userViewLog.setStatus(UserViewLog.STATUS_LEAVE);
				}
			}
		}

	}

	private void saveOrUpdateUserViewTime(final Map<String, UserViewLog> userViewLogMap,
			final UserViewLogMng userViewLogMng, final DateTime statisticEndTime) {
		log.debug("时长结果入库，userViewLogMap.size()={}", userViewLogMap.size());

		Iterator<Entry<String, UserViewLog>> iterator = userViewLogMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, UserViewLog> entry = (Map.Entry<String, UserViewLog>) iterator.next();
			UserViewLog userViewLog = entry.getValue();

			// 计算未离开直播间的用户的观看时长
			if (UserViewLog.STATUS_ENTER.equals(userViewLog.getStatus())) {
				Timestamp resultLastStatisticTime = userViewLog.getLastStatisticTime();
				Long viewTotalTime = userViewLog.getViewTotalTime();
				Long addViewTotalTime = statisticEndTime.getMillis() - resultLastStatisticTime.getTime();
				Long newViewTotalTime = viewTotalTime + (addViewTotalTime > 0 ? addViewTotalTime : 0) / 1000;
				userViewLog.setViewTotalTime(newViewTotalTime);
				userViewLog.setLastStatisticTime(new Timestamp(statisticEndTime.getMillis()));
			}

			if (null != userViewLog) {
				Integer requestType = userViewLog.getRequestType();
				Integer userType = userViewLog.getUserType();
				String userId = userViewLog.getUserId();
				log.debug("liveEventId={}, requestType={}, userType={},userId ={}",liveEventId, requestType, userType, userId);
				UserViewLog oldUserViewLog = userViewLogMng.getBeanByParams(liveEventId, requestType, userType, userId);
				if (null == oldUserViewLog) {
					userViewLogMng.save(userViewLog);
				} else {
					String id = oldUserViewLog.getId();
					userViewLog.setId(id);
					userViewLogMng.update(userViewLog);
				}
			}
		}
	}
}