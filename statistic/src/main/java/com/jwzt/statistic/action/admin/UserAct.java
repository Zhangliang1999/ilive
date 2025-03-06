package com.jwzt.statistic.action.admin;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
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

import com.jwzt.common.web.ResponseUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.RequestLogMng;
import com.jwzt.statistic.manager.TotalStatisticResultMng;
import com.jwzt.statistic.utils.ConfigUtils;
import com.jwzt.statistic.utils.ExcelUtils;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class UserAct {
	private static final Logger log = LogManager.getLogger();

	/**
	 * 平台用户总数、平台近30天新增用户、平台近30天活跃用户;
	 * 
	 * 单场直播最多新增用户 直播名，人数，企业名，直播开始时间
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/total")
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
			if (null == startTime && null == endTime) {
				endTime = new DateTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
				startTime = new DateTime(endTime).plusDays(-30).withHourOfDay(0).withMinuteOfHour(0)
						.withSecondOfMinute(0).withMillisOfSecond(0).toDate();
			}
			TotalStatisticResult totalStatisticResult = totalStatisticResultMng.sumByParams(startTime, endTime);
			Long newRegisterUserNum = totalStatisticResult.getNewRegisterUserNumAboutDay();

			TotalStatisticResult lastTotalStatisticResult = totalStatisticResultMng.getLastBeanByEndTime(endTime);
			Long userTotalNum = lastTotalStatisticResult.getUserTotalNum();
			Long numOfMaxNewRegisterUserNum = lastTotalStatisticResult.getNumOfMaxNewRegisterUserNumAboutLive();
			Timestamp timeOfMaxNewRegisterUserNum = lastTotalStatisticResult.getTimeOfMaxNewRegisterUserNumAboutLive();
			String liveTitleOfMaxNewRegisterUserNum = lastTotalStatisticResult
					.getLiveTitleOfMaxNewRegisterUserNumAboutLive();
			String enterpriseNameOfMaxNewRegisterUserNum = lastTotalStatisticResult
					.getEnterpriseNameOfMaxNewRegisterUserNumAboutLive();
			Integer[] logTypes = { RequestLog.LOG_TYPE_USER_ENTER };
			Long newActiveUserNum = requestLogMng.countUserNum(null, null, null, null, logTypes, startTime, endTime);
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("userTotalNum", NumberUtils.checkNumber(userTotalNum));
			dataMap.put("newRegisterUserNum", NumberUtils.checkNumber(newRegisterUserNum));
			dataMap.put("newActiveUserNum", NumberUtils.checkNumber(newActiveUserNum));
			dataMap.put("maxNewRegisterUserNum", NumberUtils.checkNumber(numOfMaxNewRegisterUserNum));
			dataMap.put("liveBeginTime", timeOfMaxNewRegisterUserNum);
			dataMap.put("liveTitle", liveTitleOfMaxNewRegisterUserNum);
			dataMap.put("enterpriseName", enterpriseNameOfMaxNewRegisterUserNum);
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	/**
	 * 用户注册来源：安卓、IOS、PC、WAP
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/registerSource")
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
			Long androidUserNum = totalStatisticResult.getAndroidRegisterUserNum();
			Long iosUserNum = totalStatisticResult.getIosRegisterUserNum();
			Long pcUserNum = totalStatisticResult.getPcRegisterUserNum();
			Long wapUserNum = totalStatisticResult.getWapRegisterUserNum();
			Long otherUserNum = totalStatisticResult.getOtherRegisterUserNum();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("androidUserNum", NumberUtils.checkNumber(androidUserNum));
			dataMap.put("iosUserNum", NumberUtils.checkNumber(iosUserNum));
			dataMap.put("pcUserNum", NumberUtils.checkNumber(pcUserNum));
			dataMap.put("wapUserNum", NumberUtils.checkNumber(wapUserNum));
			dataMap.put("otherUserNum", NumberUtils.checkNumber(otherUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct registerSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	/**
	 * 用户观看来源：安卓、IOS、PC、WAP
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/viewSource")
	public String viewSource(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			Long androidUserViewNum = totalStatisticResult.getAndroidViewUserNum();
			Long iosUserViewNum = totalStatisticResult.getIosViewUserNum();
			Long pcUserViewNum = totalStatisticResult.getPcViewUserNum();
			Long wapUserViewNum = totalStatisticResult.getWapViewUserNum();
			Long otherUserViewNum = totalStatisticResult.getOtherViewUserNum();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("androidUserViewNum", NumberUtils.checkNumber(androidUserViewNum));
			dataMap.put("iosUserViewNum", NumberUtils.checkNumber(iosUserViewNum));
			dataMap.put("pcUserViewNum", NumberUtils.checkNumber(pcUserViewNum));
			dataMap.put("wapUserViewNum", NumberUtils.checkNumber(wapUserViewNum));
			dataMap.put("otherUserViewNum", NumberUtils.checkNumber(otherUserViewNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct viewSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	/**
	 * 用户平均观看直播时长变化
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/viewTimeForLine")
	public String viewTimeForLine(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			for (TotalStatisticResult totalStatisticResult : totalStatisticResultList) {
				if (null != totalStatisticResult) {
					Long viewTotalDuration = totalStatisticResult.getViewTotalDuration();
					Long viewTotalNum = totalStatisticResult.getViewTotalNum();
					Timestamp statisticTime = totalStatisticResult.getStatisticTime();
					Map<String, Object> dataMap = new HashMap<>();
					dataMap.put("statisticTime", statisticTime);
					dataMap.put("viewTotalDuration", NumberUtils.checkNumber(viewTotalDuration));
					dataMap.put("viewTotalNum", NumberUtils.checkNumber(viewTotalNum));
					dataList.add(dataMap);
				}
			}
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct viewTimeForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/user/viewTimeForColumn")
	public String viewTimeForColumn(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			Long viewTimeHour0To3 = totalStatisticResult.getViewTimeHour0To3();
			Long viewTimeHour3To6 = totalStatisticResult.getViewTimeHour3To6();
			Long viewTimeHour6To9 = totalStatisticResult.getViewTimeHour6To9();
			Long viewTimeHour9To12 = totalStatisticResult.getViewTimeHour9To12();
			Long viewTimeHour12To15 = totalStatisticResult.getViewTimeHour12To15();
			Long viewTimeHour15To18 = totalStatisticResult.getViewTimeHour15To18();
			Long viewTimeHour18To21 = totalStatisticResult.getViewTimeHour18To21();
			Long viewTimeHour21To24 = totalStatisticResult.getViewTimeHour21To24();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("viewTimeHour0To3", NumberUtils.checkNumber(viewTimeHour0To3));
			dataMap.put("viewTimeHour3To6", NumberUtils.checkNumber(viewTimeHour3To6));
			dataMap.put("viewTimeHour6To9", NumberUtils.checkNumber(viewTimeHour6To9));
			dataMap.put("viewTimeHour9To12", NumberUtils.checkNumber(viewTimeHour9To12));
			dataMap.put("viewTimeHour12To15", NumberUtils.checkNumber(viewTimeHour12To15));
			dataMap.put("viewTimeHour15To18", NumberUtils.checkNumber(viewTimeHour15To18));
			dataMap.put("viewTimeHour18To21", NumberUtils.checkNumber(viewTimeHour18To21));
			dataMap.put("viewTimeHour21To24", NumberUtils.checkNumber(viewTimeHour21To24));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct viewTimeForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	/**
	 * 用户观看直播ip地域分布
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/viewLocation")
	public String viewLocation(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(null,
					LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW, startTime, endTime);
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
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("provinceName", provinceName);
							map.put("totalNum", NumberUtils.checkNumber(totalNum));
							dataList.add(map);
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
			log.error("UserAct viewLocation error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/user/viewLocationExcelExport")
	public void viewLocationExcelExport(Date startTime, Date endTime, HttpServletRequest request,
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(null,
					LocationStatisticResult.FLAG_TYPE_TOTAL_VIEW, startTime, endTime);
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
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("provinceName", provinceName);
							map.put("totalNum", NumberUtils.checkNumber(totalNum));
							dataList.add(map);
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
			log.error("UserAct viewLocationExcelExport error.", e);
		}
	}

	/**
	 * 用户平均每场观看直播时长
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/user/viewTimeForPie")
	public String viewTimeForPie(Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			Long viewDuraionUserNum0To5 = totalStatisticResult.getDurationViewUserNum0To5();
			Long viewDuraionUserNum5To10 = totalStatisticResult.getDurationViewUserNum5To10();
			Long viewDuraionUserNum10To30 = totalStatisticResult.getDurationViewUserNum10To30();
			Long viewDuraionUserNum30To60 = totalStatisticResult.getDurationViewUserNum30To60();
			Long viewDuraionUserNum60To = totalStatisticResult.getDurationViewUserNum60To();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("viewDuraionUserNum0To5", NumberUtils.checkNumber(viewDuraionUserNum0To5));
			dataMap.put("viewDuraionUserNum5To10", NumberUtils.checkNumber(viewDuraionUserNum5To10));
			dataMap.put("viewDuraionUserNum10To30", NumberUtils.checkNumber(viewDuraionUserNum10To30));
			dataMap.put("viewDuraionUserNum30To60", NumberUtils.checkNumber(viewDuraionUserNum30To60));
			dataMap.put("viewDuraionUserNum60To", NumberUtils.checkNumber(viewDuraionUserNum60To));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("UserAct viewTimeForPie error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@Autowired
	private TotalStatisticResultMng totalStatisticResultMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;
	@Autowired
	private RequestLogMng requestLogMng;
}
