package com.jwzt.statistic.task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.entity.VideoStatisticResult;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.manager.VideoStatisticResultMng;

/**
 * 每天执行回看统计
 * @author gstars
 *
 */
public class VideoStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();
	private final Long videoId;
	private final Date startTime;
	private final Date endTime;

	public VideoStatisticTask(final Long videoId, final Date startTime, final Date endTime) {
		super();
		this.videoId = videoId;
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
			log.debug("VideoStatisticTask run.");
			VideoStatisticResultMng videoStatisticResultMng = (VideoStatisticResultMng) context
					.getBean("VideoStatisticResultMng");
			LocationStatisticResultMng locationStatisticResultMng = (LocationStatisticResultMng) context
					.getBean("LocationStatisticResultMng");
			UserInfoMng userInfoMng = (UserInfoMng) context.getBean("UserInfoMng");
			RequestLogMng requestLogMng = (RequestLogMng) context.getBean("RequestLogMng");
			Integer[] countUserLogTypes = { RequestLog.LOG_TYPE_USER_VIEW };
			// 观看来源
			Long otherUserNum = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_OTHER,
					countUserLogTypes, null, null);
			Long androidUserNum = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_ANDROID,
					countUserLogTypes, null, null);
			Long iosUserNum = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_IOS,
					countUserLogTypes, null, null);
			Long pcUserNum = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_PC,
					countUserLogTypes, null, null);
			Long wapUserNum = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_WAP,
					countUserLogTypes, null, null);
			Long userNum = otherUserNum + androidUserNum + iosUserNum + pcUserNum + wapUserNum;
		
			Long otherUserNum1 = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_OTHER,
					countUserLogTypes, startTime, endTime);
			Long androidUserNum1 = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_ANDROID,
					countUserLogTypes,startTime, endTime);
			Long iosUserNum1 = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_IOS,
					countUserLogTypes, startTime, endTime);
			Long pcUserNum1 = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_PC,
					countUserLogTypes, startTime, endTime);
			Long wapUserNum1 = requestLogMng.countUserNum(null, null, videoId, RequestLog.REQUEST_TYPE_WAP,
					countUserLogTypes, startTime, endTime);
			Long userNum1 = otherUserNum1 + androidUserNum1 + iosUserNum1 + pcUserNum1 + wapUserNum1;

			Integer[] countLogTypes = { RequestLog.LOG_TYPE_USER_VIEW };
			
			// 累计观看次数
			Long viewTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, null, null);
			Long viewTotalNum1 = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_SHARE;
			// 累计分享数
			Long shareTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_LIKE;
			// 累计点赞数
			Long likeTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, null, null);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_COMMENT;
			// 累计互动数
			Long commentTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, null,
					null);
/*			// 累计观看次数
			Long viewTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_SHARE;
			// 累计分享数
			Long shareTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_LIKE;
			// 累计点赞数
			Long likeTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, startTime, endTime);
			countLogTypes[0] = RequestLog.LOG_TYPE_USER_COMMENT;
			// 累计互动数
			Long commentTotalNum = requestLogMng.countRequestNum(null, null, videoId, countLogTypes, startTime,
					endTime);
*/			Long newRegisterUserNum = 0L;
			Long oldRegisterUserNum = 0L;
			Integer[] listLogTypes = { RequestLog.LOG_TYPE_USER_VIEW };
			List<RequestLog> requestLogList = requestLogMng.listByParams(null, null, videoId, listLogTypes, null,
					null, null, false, false, false);
/*			List<RequestLog> requestLogList = requestLogMng.listByParams(null, null, videoId, listLogTypes, null,
					startTime, endTime, false, false, false);
*/			if (null != requestLogList && requestLogList.size() > 0) {
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
							if (null != registerTime && null != startTime && null != endTime
									&& registerTime.getTime() >= startTime.getTime()
									&& registerTime.getTime() <= endTime.getTime()) {
								newRegisterUserNum++;
							}
						}
					}
				}
				if (userNum > newRegisterUserNum) {
					oldRegisterUserNum = userNum - newRegisterUserNum;
				}
			}

			// 加1000方便查询
			Timestamp statisticTime = new Timestamp(startTime.getTime() + 1000);
			List<VideoStatisticResult> videoStatisticResultList = videoStatisticResultMng.listByParams(videoId,
					startTime, endTime);
			if (null != videoStatisticResultList && videoStatisticResultList.size() > 0) {
				VideoStatisticResult videoStatisticResult = videoStatisticResultList.get(0);
				videoStatisticResult.setVideoId(videoId);
				videoStatisticResult.setUserTotalNum(userNum);
				videoStatisticResult.setViewTotalNum(viewTotalNum);
				videoStatisticResult.setAndroidUserNum(androidUserNum);
				videoStatisticResult.setIosUserNum(iosUserNum);
				videoStatisticResult.setWapUserNum(wapUserNum);
				videoStatisticResult.setPcUserNum(pcUserNum);
				videoStatisticResult.setOtherUserNum(otherUserNum);
				videoStatisticResult.setViewTotalNum(viewTotalNum);
				videoStatisticResult.setShareTotalNum(shareTotalNum);
				videoStatisticResult.setLikeTotalNum(likeTotalNum);
				videoStatisticResult.setCommentTotalNum(commentTotalNum);
				videoStatisticResult.setNewRegisterUserNum(newRegisterUserNum);
				videoStatisticResult.setOldRegisterUserNum(oldRegisterUserNum);
				videoStatisticResult.setStatisticTime(statisticTime);
				videoStatisticResult.setCurDayNum(userNum1);
				videoStatisticResult.setCurDayView(viewTotalNum1);
				videoStatisticResultMng.update(videoStatisticResult);
			} else {
				VideoStatisticResult videoStatisticResult = new VideoStatisticResult();
				videoStatisticResult.setVideoId(videoId);
				videoStatisticResult.setUserTotalNum(userNum);
				videoStatisticResult.setViewTotalNum(viewTotalNum);
				videoStatisticResult.setAndroidUserNum(androidUserNum);
				videoStatisticResult.setIosUserNum(iosUserNum);
				videoStatisticResult.setWapUserNum(wapUserNum);
				videoStatisticResult.setPcUserNum(pcUserNum);
				videoStatisticResult.setOtherUserNum(otherUserNum);
				videoStatisticResult.setViewTotalNum(viewTotalNum);
				videoStatisticResult.setShareTotalNum(shareTotalNum);
				videoStatisticResult.setLikeTotalNum(likeTotalNum);
				videoStatisticResult.setCommentTotalNum(commentTotalNum);
				videoStatisticResult.setNewRegisterUserNum(newRegisterUserNum);
				videoStatisticResult.setOldRegisterUserNum(oldRegisterUserNum);
				videoStatisticResult.setStatisticTime(statisticTime);
				videoStatisticResult.setCurDayNum(userNum1);
				videoStatisticResult.setCurDayView(viewTotalNum1);
				videoStatisticResultMng.save(videoStatisticResult);
			}

			// 统计推流地域

			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listStatisticResutlByFlag(videoId, LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW, startTime,
							endTime);

			if (null != locationStatisticResultList) {
				// 入库
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						locationStatisticResult.setStatisticTime(new Timestamp(startTime.getTime()));
						String provinceName = locationStatisticResult.getProvinceName();
						LocationStatisticResult oldLocationStatisticResult = locationStatisticResultMng
								.getBeanByProvinceName(provinceName, videoId,
										LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW, startTime, endTime);
						if (null == oldLocationStatisticResult) {
							locationStatisticResult.setFlagId(videoId);
							locationStatisticResult.setFlagType(LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW);
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
			log.error("VideoStatisticTask error.", e);
		}
	}
}