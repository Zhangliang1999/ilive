package com.jwzt.statistic.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.page.Pagination;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.ResponseUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LiveStatisticResult;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.MinuteStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.entity.UserInfo;
import com.jwzt.statistic.entity.vo.RankInfo;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LiveStatisticResultMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.MinuteStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.TotalStatisticResultMng;
import com.jwzt.statistic.manager.UserInfoMng;
import com.jwzt.statistic.utils.ConfigUtils;
import com.jwzt.statistic.utils.ExcelUtils;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class LiveAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/live/page")
	public String page(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId,String enterpriseName, Date startTime,
			Date endTime, String liveLongTime,HttpServletRequest request, ModelMap mp, Integer pageNo,Integer UserViewingData,Integer ViewData) {
		System.out.println(UserViewingData);
		System.out.println(ViewData);
		try {
			if (null != liveTitle) {
				liveTitle = new String(liveTitle.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != enterpriseName) {
				enterpriseName = new String(enterpriseName.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			Integer startNum = null,endNum = null;
			try{
				if(null != liveLongTime && !"".equals(liveLongTime)){
					String[] longTime = liveLongTime.split("-");
					for(int i = 0 ; i < longTime.length;i++){
						if(i == 0){
							startNum = Integer.valueOf(longTime[i]) ;
						}else{
							endNum = Integer.valueOf(longTime[i]) ;
						}
					}
				}
			}catch(Exception e){
				startNum = null;
				endNum = null;
				log.error("LiveAct page liveLongTime error.", e);
			}
			Pagination page = liveInfoMng.pageByParams(roomId, liveEventId, liveTitle, enterpriseId, enterpriseName,startTime, endTime,
					checkPageNo(pageNo), CookieUtils.getPageSize(request),startNum,endNum);
			RenderJsonUtils.addSuccess(mp, page);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct page error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/excel_export")
	public void excelExport(Integer roomId, Long liveEventId, String liveTitle, Integer enterpriseId, Date startTime,
			Date endTime, HttpServletRequest request, HttpServletResponse response, ModelMap mp) {
		try {
			if (null != liveTitle) {
				liveTitle = new String(liveTitle.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<LiveInfo> beanList = liveInfoMng.listByParams(roomId, liveEventId, liveTitle, enterpriseId, startTime,
					endTime);
			if (null != beanList) {
				for (LiveInfo liveInfo : beanList) {
					if (null != liveInfo) {
						DecimalFormat df = new DecimalFormat("#0.00");
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Long liveEventId2 = liveInfo.getLiveEventId();
						String liveTitle2 = liveInfo.getLiveTitle();
						Long liveDuration = liveInfo.getLiveDuration();
						Integer enterpriseId2 = liveInfo.getEnterpriseId();
						String enterpriseName = liveInfo.getEnterpriseName();
						String liveStartTime = "";
						String liveEndTime = "";
						
						try{
							java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if(liveInfo.getLiveBeginTime()!=null)
								liveStartTime = sf.format(liveInfo.getLiveBeginTime());
							if(liveInfo.getLiveEndTime()!=null)
								liveEndTime = sf.format(liveInfo.getLiveEndTime());
						}catch(Exception e){
							e.printStackTrace();
						}
						
						liveInfo.getLiveBeginTime();
						dataMap.put("liveEventId", liveEventId2);
						dataMap.put("liveTitle", liveTitle2);
						dataMap.put("enterpriseId", enterpriseId2);
						dataMap.put("enterpriseName", enterpriseName);
						dataMap.put("liveStartTime",liveStartTime);
						dataMap.put("liveEndTime",liveEndTime);
						dataMap.put("liveDuration", df.format(liveDuration / 3600.0));
						LiveStatisticResult statisticResult = liveInfo.getStatisticResult();
						if (null != statisticResult) {
							Long viewNum = statisticResult.getViewNum();
							Long userNum = statisticResult.getUserNum();
							Long averageDuration = 0L;
							if (null != userNum && userNum != 0) {
								averageDuration = liveDuration / userNum;
							}
							Long maxMinuteUserNum = statisticResult.getMaxMinuteUserNum();
							Long likeNum = statisticResult.getLikeNum();
							Long signNum = statisticResult.getSignNum();
							Long shareNum = statisticResult.getShareNum();
							Long commentNum = statisticResult.getCommentNum();
							Double redPacketMoney = statisticResult.getRedPacketMoney();
							Long giftNum = statisticResult.getGiftNum();
							dataMap.put("viewNum", NumberUtils.checkNumber(viewNum));
							dataMap.put("userNum", NumberUtils.checkNumber(userNum));
							dataMap.put("maxMinuteUserNum", NumberUtils.checkNumber(maxMinuteUserNum));
							dataMap.put("averageDuration", df.format(averageDuration / 3600.0));
							dataMap.put("likeNum", NumberUtils.checkNumber(likeNum));
							dataMap.put("signNum", NumberUtils.checkNumber(signNum));
							dataMap.put("shareNum", NumberUtils.checkNumber(shareNum));
							dataMap.put("commentNum", NumberUtils.checkNumber(commentNum));
							//dataMap.put("redPacketMoney", NumberUtils.checkNumber(redPacketMoney));
							//dataMap.put("giftNum", NumberUtils.checkNumber(giftNum));
						}
						dataList.add(dataMap);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "直播统计_" + sdf.format(new Date());
			String[] keys = { "liveEventId", "liveTitle", "enterpriseId", "enterpriseName", "liveStartTime" , "liveEndTime", "liveDuration", "viewNum",
					"userNum", "maxMinuteUserNum", "averageDuration", "likeNum", "signNum", "shareNum", "commentNum" };
			String[] columnNames = { "场次ID", "直播名称", "企业ID", "企业名称","開始時間","結束時間", "直播时长（小时）", "累计观看次数（次）", "累计观看人数（人）", "最高并发（人）",
					"人均观看时长（小时）", "被点赞累计数（次）", "签到次数", "转发分享数（次）", "被评论累计数（次）" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("LiveAct excelExport error.", e);
		}
	}

	@RequestMapping("/live/info")
	public String info(Long liveEventId, HttpServletRequest request, ModelMap mp, Integer pageNo) {
		try {
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			RenderJsonUtils.addSuccess(mp, liveInfo);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct info error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/totalDurationForLine")
	public String totalDurationForLine(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<TotalStatisticResult> totalStatisticResultList = totalStatisticResultMng.listByParams(startTime,
					endTime);
			if (null != totalStatisticResultList) {
				for (TotalStatisticResult totalStatisticResult : totalStatisticResultList) {
					if (null != totalStatisticResult) {
						Long liveTotalDuration = totalStatisticResult.getLiveTotalDuration();
						Timestamp statisticTime = totalStatisticResult.getStatisticTime();
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("liveTotalDuration", NumberUtils.checkNumber(liveTotalDuration));
						dataMap.put("statisticTime", statisticTime);
						dataList.add(dataMap);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct totalDurationForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/singleDurationForPie")
	public String singleDurationForPie(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			Long liveDuraionUserNum0To10 = totalStatisticResult.getDurationLiveNum0To10();
			Long liveDuraionUserNum10To100 = totalStatisticResult.getDurationLiveNum10To100();
			Long liveDuraionUserNum100To200 = totalStatisticResult.getDurationLiveNum100To200();
			Long liveDuraionUserNum200To300 = totalStatisticResult.getDurationLiveNum200To300();
			Long liveDuraionUserNum300To = totalStatisticResult.getDurationLiveNum300To();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("liveDuraionUserNum0To10", NumberUtils.checkNumber(liveDuraionUserNum0To10));
			dataMap.put("liveDuraionUserNum10To100", NumberUtils.checkNumber(liveDuraionUserNum10To100));
			dataMap.put("liveDuraionUserNum100To200", NumberUtils.checkNumber(liveDuraionUserNum100To200));
			dataMap.put("liveDuraionUserNum200To300", NumberUtils.checkNumber(liveDuraionUserNum200To300));
			dataMap.put("liveDuraionUserNum300To", NumberUtils.checkNumber(liveDuraionUserNum300To));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct singleDurationForPie error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/eventNumForLine")
	public String eventNumForLine(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<TotalStatisticResult> totalStatisticResultList = totalStatisticResultMng.listByParams(startTime,
					endTime);
			if (null != totalStatisticResultList) {
				for (TotalStatisticResult totalStatisticResult : totalStatisticResultList) {
					if (null != totalStatisticResult) {
						Long liveEventTotalNum = totalStatisticResult.getLiveEventTotalNum();
						Timestamp statisticTime = totalStatisticResult.getStatisticTime();
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("liveEventTotalNum", NumberUtils.checkNumber(liveEventTotalNum));
						dataMap.put("statisticTime", statisticTime);
						dataList.add(dataMap);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct eventNumForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/viewNumForLine")
	public String viewNumForLine(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request,
			ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Timestamp liveBeginTime = liveInfo.getLiveBeginTime();
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<MinuteStatisticResult> minuteStatisticResultList = minuteStatisticResultMng
					.listByLiveEventId(liveEventId);
			if (null != minuteStatisticResultList) {
				for (MinuteStatisticResult minuteStatisticResult : minuteStatisticResultList) {
					if (null != minuteStatisticResult) {
						Long userNum = minuteStatisticResult.getUserNum();
						Long viewNum = minuteStatisticResult.getViewNum();
						Long viewTotalNum = minuteStatisticResult.getViewTotalNum();
						Long userTotalNum = minuteStatisticResult.getUserTotalNum();
						Timestamp minuteStatisticStartTime = minuteStatisticResult.getStartTime();
						Long minute = (minuteStatisticStartTime.getTime() - liveBeginTime.getTime()) / 1000 / 60;
						if (minute < 0) {
							minute = 0L;
						}
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("userNum", NumberUtils.checkNumber(userNum));
						dataMap.put("viewNum", NumberUtils.checkNumber(viewNum));
						dataMap.put("userTotalNum", NumberUtils.checkNumber(userTotalNum));
						dataMap.put("viewTotalNum", NumberUtils.checkNumber(viewTotalNum));
						dataMap.put("minute", minute);
						dataList.add(dataMap);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct viewNumForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/viewSource")
	public String viewSource(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			Long androidUserNum = liveStatisticResult.getAndroidUserNum();
			Long iosUserNum = liveStatisticResult.getIosUserNum();
			Long pcUserNum = liveStatisticResult.getPcUserNum();
			Long wapUserNum = liveStatisticResult.getWapUserNum();
			Long otherUserNum = liveStatisticResult.getOtherUserNum();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("androidUserNum", NumberUtils.checkNumber(androidUserNum));
			dataMap.put("iosUserNum", NumberUtils.checkNumber(iosUserNum));
			dataMap.put("pcUserNum", NumberUtils.checkNumber(pcUserNum));
			dataMap.put("wapUserNum", NumberUtils.checkNumber(wapUserNum));
			dataMap.put("otherUserNum", NumberUtils.checkNumber(otherUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct viewSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/registerSource")
	public String registerSource(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request,
			ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			Long newRegisterUserNum = liveStatisticResult.getNewRegisterUserNum();
			Long oldRegisterUserNum = liveStatisticResult.getOldRegisterUserNum();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("newRegisterUserNum", NumberUtils.checkNumber(newRegisterUserNum));
			dataMap.put("oldRegisterUserNum", NumberUtils.checkNumber(oldRegisterUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct registerSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/viewTimeForPie")
	public String viewTimeForPie(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request,
			ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			Long liveId = liveInfo.getId();
			LiveStatisticResult liveStatisticResult = liveStatisticResultMng.getBeanById(liveId);
			Long viewDuraionUserNum0To5 = liveStatisticResult.getViewDuraionUserNum0To5();
			Long viewDuraionUserNum5To10 = liveStatisticResult.getViewDuraionUserNum5To10();
			Long viewDuraionUserNum10To30 = liveStatisticResult.getViewDuraionUserNum10To30();
			Long viewDuraionUserNum30To60 = liveStatisticResult.getViewDuraionUserNum30To60();
			Long viewDuraionUserNum60To = liveStatisticResult.getViewDuraionUserNum60To();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("viewDuraionUserNum0To5", NumberUtils.checkNumber(viewDuraionUserNum0To5));
			dataMap.put("viewDuraionUserNum5To10", NumberUtils.checkNumber(viewDuraionUserNum5To10));
			dataMap.put("viewDuraionUserNum10To30", NumberUtils.checkNumber(viewDuraionUserNum10To30));
			dataMap.put("viewDuraionUserNum30To60", NumberUtils.checkNumber(viewDuraionUserNum30To60));
			dataMap.put("viewDuraionUserNum60To", NumberUtils.checkNumber(viewDuraionUserNum60To));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct viewTimeForPie error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/viewLocation")
	public String viewLocation(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request,
			ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listByFlag(liveEventId, LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, startTime, endTime);
			if (null != locationStatisticResultList) {
				String defaultProvinceName = ConfigUtils.get(ConfigUtils.DEFAULT_PROVINCE_NAME);
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						Long totalNum = locationStatisticResult.getTotalNum();
						if (StringUtils.isBlank(provinceName) || "xx".equalsIgnoreCase(provinceName)) {
							provinceName = defaultProvinceName;
						}
						boolean isExisted = false;
						for (Map<String, Object> dataMap : dataList) {
							String provinceNameInMap = (String) dataMap.get("provinceName");
							if (StringUtils.isNotBlank(provinceName) && provinceName.equals(provinceNameInMap)) {
								Long totalNumInMap = (Long) dataMap.get("totalNum");
								totalNum = totalNum + totalNumInMap;
								dataMap.put("totalNum", NumberUtils.checkNumber(totalNum));
								isExisted = true;
								break;
							}
						}
						if (!isExisted) {
							Map<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put("provinceName", provinceName);
							dataMap.put("totalNum", NumberUtils.checkNumber(totalNum));
							dataList.add(dataMap);
						}
					}
				}
			}
			Collections.sort(dataList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> map1, Map<String, Object> map2) {
					Long totalNum1 = (Long) map1.get("totalNum");
					Long totalNum2 = (Long) map2.get("totalNum");
					if (totalNum1 > totalNum2) {
						return -1;
					} else if (totalNum1 < totalNum2) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct viewLocation error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/viewLocationExcelExport")
	public void viewLocationExcelExport(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request,
			HttpServletResponse response, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng
					.listByFlag(liveEventId, LocationStatisticResult.FLAG_TYPE_LIVE_EVENT_VIEW, startTime, endTime);
			if (null != locationStatisticResultList) {
				String defaultProvinceName = ConfigUtils.get(ConfigUtils.DEFAULT_PROVINCE_NAME);
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						Long totalNum = locationStatisticResult.getTotalNum();
						if (StringUtils.isBlank(provinceName) || "xx".equalsIgnoreCase(provinceName)) {
							provinceName = defaultProvinceName;
						}
						boolean isExisted = false;
						for (Map<String, Object> dataMap : dataList) {
							String provinceNameInMap = (String) dataMap.get("provinceName");
							if (StringUtils.isNotBlank(provinceName) && provinceName.equals(provinceNameInMap)) {
								Long totalNumInMap = (Long) dataMap.get("totalNum");
								totalNum = totalNum + totalNumInMap;
								dataMap.put("totalNum", NumberUtils.checkNumber(totalNum));
								isExisted = true;
								break;
							}
						}
						if (!isExisted) {
							Map<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put("provinceName", provinceName);
							dataMap.put("totalNum", NumberUtils.checkNumber(totalNum));
							dataList.add(dataMap);
						}
					}
				}
			}
			Collections.sort(dataList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> map1, Map<String, Object> map2) {
					Long totalNum1 = (Long) map1.get("totalNum");
					Long totalNum2 = (Long) map2.get("totalNum");
					if (totalNum1 > totalNum2) {
						return -1;
					} else if (totalNum1 < totalNum2) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "用户观看直播ip地域分布_" + sdf.format(new Date());
			String[] keys = { "provinceName", "totalNum" };
			String[] columnNames = { "省份", "观众人数" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("LiveAct viewLocation error.", e);
		}
	}

	@RequestMapping("/live/shareList")
	public String shareList(Long liveEventId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			Integer[] logTypes = { RequestLog.LOG_TYPE_USER_SHARE };
			List<RankInfo> rankInfoList = requestLogMng.listRankGroupByUser(liveEventId, null, null, logTypes,
					startTime, endTime, 20);
			if (null != rankInfoList) {
				for (RankInfo rankInfo : rankInfoList) {
					if (null != rankInfo) {
						String name = rankInfo.getName();
						Long num = rankInfo.getNum();
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("name", name);
						dataMap.put("num", NumberUtils.checkNumber(num));
						dataList.add(dataMap);
					}
				}
			}
			Collections.sort(dataList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> map1, Map<String, Object> map2) {
					Long num1 = (Long) map1.get("num");
					Long num2 = (Long) map2.get("num");
					if (num1 > num2) {
						return -1;
					} else if (num1 < num2) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct shareList error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/userViewLogPage")
	public String userViewLogPage(Long liveEventId, String userId, String username, Date startTime, Date endTime,
			HttpServletRequest request, ModelMap mp, Integer pageNo) {
		try {
			if (null != username) {
				username = new String(username.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			String[] userIds = null;
			if (StringUtils.isNotBlank(username) || null != userId) {
				List<String> userIdList = new ArrayList<String>();
				if (StringUtils.isNotBlank(username)) {
					List<UserInfo> userInfoList = userInfoMng.listByParams(null, username, null, null);
					if (null != userInfoList) {
						for (UserInfo userInfo : userInfoList) {
							if (null != userInfo) {
								Long id = userInfo.getId();
								if (null != id) {
									userIdList.add(String.valueOf(id));
								}
							}

						}
					}
				}
				try {
					userIdList.add(userId);
				} catch (Exception e) {
				}
				if (userIdList.size() > 0) {
					userIds = new String[userIdList.size()];
					userIds = userIdList.toArray(userIds);
				}
			}
			Integer[] logTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Pagination page = requestLogMng.pageByParams(liveEventId, null, null, logTypes, userIds, startTime, endTime,
					true, true, true, checkPageNo(pageNo), CookieUtils.getPageSize(request));
			RenderJsonUtils.addSuccess(mp, page);
			return "renderJson";
		} catch (Exception e) {
			log.error("LiveAct userViewLogPage error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/live/excel_export_view_log")
	public void excelExportViewLog(Long liveEventId, String userId, String username, Date startTime, Date endTime,
			HttpServletRequest request, ModelMap mp, HttpServletResponse response, Integer pageNo) {
		try {
			if (null != username) {
				username = new String(username.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			String[] userIds = null;
			if (StringUtils.isNotBlank(username) || null != userId) {
				List<String> userIdList = new ArrayList<String>();
				if (StringUtils.isNotBlank(username)) {
					List<UserInfo> userInfoList = userInfoMng.listByParams(null, username, null, null);
					if (null != userInfoList) {
						for (UserInfo userInfo : userInfoList) {
							if (null != userInfo) {
								Long id = userInfo.getId();
								if (null != id) {
									userIdList.add(String.valueOf(id));
								}
							}

						}
					}
				}
				try {
					userIdList.add(userId);
				} catch (Exception e) {
				}
				if (userIdList.size() > 0) {
					userIds = new String[userIdList.size()];
					userIds = userIdList.toArray(userIds);
				}
			}
			Integer[] logTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			List<RequestLog> beanList = requestLogMng.listByParams(liveEventId, null, null, logTypes, userIds,
					startTime, endTime, true, true, true);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			if (null != beanList) {
				for (RequestLog requestLog : beanList) {
					if (null != requestLog) {
						try {
							Map<String, Object> dataMap = new HashMap<String, Object>();
							// "场次ID", "直播名称", "用户ID", "用户名", "观看IP", "IP所属地域",
							// "来源终端", "进入直播时间"
							Long liveEventId2 = requestLog.getLiveEventId();
							String liveTitle2 = requestLog.getLiveTitle();
							String userId2 = requestLog.getUserId();
							String nickName = requestLog.getNickName();
							String ip = requestLog.getIp();
							String region = requestLog.getRegion();
							String requestTypeStr = requestLog.getRequestTypeStr();
							Timestamp createTime = requestLog.getCreateTime();
							dataMap.put("liveEventId", liveEventId2);
							dataMap.put("liveTitle", liveTitle2);
							dataMap.put("userId", userId2);
							dataMap.put("nickName", nickName);
							dataMap.put("ip", ip);
							dataMap.put("region", region);
							dataMap.put("requestTypeStr", requestTypeStr);
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								dataMap.put("createTime", sdf.format(createTime));
							} catch (Exception e) {
							}
							dataList.add(dataMap);
						} catch (Exception e) {
						}
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "用户观看记录统计_" + sdf.format(new Date());
			String[] keys = { "liveEventId", "liveTitle", "userId", "nickName", "ip", "region", "requestTypeStr",
					"createTime" };
			String[] columnNames = { "场次ID", "直播名称", "用户ID", "用户名", "观看IP", "IP所属地域", "来源终端", "进入直播时间" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("LiveAct excelExportViewLog error.", e);
		}
	}

	@Autowired
	private LiveStatisticResultMng liveStatisticResultMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;
	@Autowired
	private TotalStatisticResultMng totalStatisticResultMng;
	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private MinuteStatisticResultMng minuteStatisticResultMng;
	@Autowired
	private RequestLogMng requestLogMng;
	@Autowired
	private UserInfoMng userInfoMng;

}
