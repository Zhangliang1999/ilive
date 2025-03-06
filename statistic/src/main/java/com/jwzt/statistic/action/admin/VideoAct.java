package com.jwzt.statistic.action.admin;

import static com.jwzt.common.page.SimplePage.checkPageNo;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwzt.common.page.Pagination;
import com.jwzt.common.utils.TimeUtils;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.ResponseUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.entity.VideoInfo;
import com.jwzt.statistic.entity.VideoStatisticResult;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.manager.VideoInfoMng;
import com.jwzt.statistic.manager.VideoStatisticResultMng;
import com.jwzt.statistic.utils.ConfigUtils;
import com.jwzt.statistic.utils.ExcelUtils;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class VideoAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/video/page")
	public String page(Long videoId, String videoTitle, Date startTime, Date endTime, HttpServletRequest request,
			ModelMap mp, Integer pageNo) {
		
		try {
			if (null != videoTitle) {
				videoTitle = new String(videoTitle.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			Pagination page = videoInfoMng.pageByParams(videoId, videoTitle, startTime, endTime, checkPageNo(pageNo),
					CookieUtils.getPageSize(request));
			RenderJsonUtils.addSuccess(mp, page);
			return "renderJson";
		} catch (Exception e) {
			log.error("VideoAct page error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/excel_export")
	public void excelExport(Long videoId, String videoTitle, Date startTime, Date endTime, HttpServletRequest request,
			HttpServletResponse response, ModelMap mp) {
		try {
			if (null != videoTitle) {
				videoTitle = new String(videoTitle.getBytes("iso-8859-1"), "utf-8");
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
			List<VideoInfo> beanList = videoInfoMng.listByParams(videoId, videoTitle, startTime, endTime, true);
			if (null != beanList) {
				for (VideoInfo videoInfo : beanList) {
					if (null != videoInfo) {
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Long id = videoInfo.getId();
						String title = videoInfo.getTitle();
						Long duration = videoInfo.getDuration();
						duration = null != duration ? duration : 0L;
						dataMap.put("videoId", id);
						dataMap.put("videoTitle", title);
						dataMap.put("duration", TimeUtils.formatDuring(duration * 1000));
						VideoStatisticResult statisticResult = videoInfo.getStatisticResult();
						if (null != statisticResult) {
							Long viewTotalNum = statisticResult.getViewTotalNum();
							Long userTotalNum = statisticResult.getUserTotalNum();
							Long likeTotalNum = statisticResult.getLikeTotalNum();
							Long commentTotalNum = statisticResult.getCommentTotalNum();
							dataMap.put("viewTotalNum", NumberUtils.checkNumber(viewTotalNum));
							dataMap.put("userTotalNum", NumberUtils.checkNumber(userTotalNum));
							dataMap.put("likeTotalNum", NumberUtils.checkNumber(likeTotalNum));
							dataMap.put("commentTotalNum", NumberUtils.checkNumber(commentTotalNum));
						}
						dataList.add(dataMap);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "回看统计_" + sdf.format(new Date());
			String[] keys = { "videoId", "videoTitle", "duration", "viewTotalNum", "userTotalNum", "averageDuration",
					"likeTotalNum", "commentTotalNum" };
			String[] columnNames = { "回看ID", "回看名称", "回看时长", "累计观看次数（次）", "累计观看人数（人）", "被点赞累计数（次）", "被评论累计数（次）" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("VideoAct total error.", e);
		}
	}

	@RequestMapping("/video/info")
	public String info(Long videoId, HttpServletRequest request, ModelMap mp, Integer pageNo) {
		try {
			VideoInfo videoInfo = videoInfoMng.getBeanById(videoId);
			RenderJsonUtils.addSuccess(mp, videoInfo);
			return "renderJson";
		} catch (Exception e) {
			log.error("VideoAct info error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/viewNumForLine")
	public String viewNumForLine(Long videoId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			List<VideoStatisticResult> videoStatisticResultList = videoStatisticResultMng.listByParams(videoId,
					startTime, endTime);
			if (null != videoStatisticResultList) {
				for (VideoStatisticResult videoStatisticResult : videoStatisticResultList) {
					if (null != videoStatisticResult) {
						Long viewTotalNum = videoStatisticResult.getCurDayView();
						Long userTotalNum = videoStatisticResult.getCurDayNum();
						Timestamp statisticTime = videoStatisticResult.getStatisticTime();
						Map<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("viewTotalNum", NumberUtils.checkNumber(viewTotalNum==null?0:viewTotalNum));
						dataMap.put("userTotalNum", NumberUtils.checkNumber(userTotalNum==null?0:userTotalNum));
						dataMap.put("statisticTime", statisticTime);
						dataList.add(dataMap);
					}
				}
			}
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("VideoAct viewNumForLine error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/viewSource")
	public String viewSource(Long videoId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
//			VideoStatisticResult videoStatisticResult = videoStatisticResultMng.sumByParams(videoId, startTime,
//					endTime);
			VideoStatisticResult videoStatisticResult = videoStatisticResultMng.sumByParams2(videoId, startTime,
					endTime);
			Long androidUserNum = videoStatisticResult.getAndroidUserNum();
			Long iosUserNum = videoStatisticResult.getIosUserNum();
			Long wapUserNum = videoStatisticResult.getWapUserNum();
			Long pcUserNum = videoStatisticResult.getPcUserNum();
			Long otherUserNum = videoStatisticResult.getOtherUserNum();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("androidUserNum", NumberUtils.checkNumber(androidUserNum));
			dataMap.put("iosUserNum", NumberUtils.checkNumber(iosUserNum));
			dataMap.put("wapUserNum", NumberUtils.checkNumber(wapUserNum));
			dataMap.put("pcUserNum", NumberUtils.checkNumber(pcUserNum));
			dataMap.put("otherUserNum", NumberUtils.checkNumber(otherUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("VideoAct viewSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/registerSource")
	public String registerSource(Long videoId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			VideoStatisticResult videoStatisticResult = videoStatisticResultMng.sumByParams2(videoId, startTime,
					endTime);
			Long newRegisterUserNum = videoStatisticResult.getNewRegisterUserNum();
			Long oldRegisterUserNum = videoStatisticResult.getOldRegisterUserNum();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("newRegisterUserNum", NumberUtils.checkNumber(newRegisterUserNum));
			dataMap.put("oldRegisterUserNum", NumberUtils.checkNumber(oldRegisterUserNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("VideoAct registerSource error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/viewLocation")
	public String viewLocation(Long videoId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(videoId,
					LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW, startTime, endTime);

			
			if (null != locationStatisticResultList) {
				String defaultProvinceName = ConfigUtils.get(ConfigUtils.DEFAULT_PROVINCE_NAME);
				for (LocationStatisticResult locationStatisticResult : locationStatisticResultList) {
					if (null != locationStatisticResult) {
						String provinceName = locationStatisticResult.getProvinceName();
						provinceName = provinceName==null?"其他":provinceName.trim();

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
			log.error("VideoAct viewLocation error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/video/viewLocationExcelExport")
	public void viewLocationExcelExport(Long videoId, Date startTime, Date endTime, HttpServletRequest request,
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(videoId,
					LocationStatisticResult.FLAG_TYPE_VIDEO_VIEW, startTime, endTime);
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
			log.error("VideoAct viewLocationExcelExport error.", e);
		}
	}

	@Autowired
	private VideoInfoMng videoInfoMng;
	@Autowired
	private VideoStatisticResultMng videoStatisticResultMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;

}
