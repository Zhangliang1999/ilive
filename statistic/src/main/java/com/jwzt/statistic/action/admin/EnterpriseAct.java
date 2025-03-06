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
import com.jwzt.common.utils.HttpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.CookieUtils;
import com.jwzt.common.web.ResponseUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.EnterpriseStatisticResult;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.LocationStatisticResult;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.EnterpriseStatisticResultMng;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.LocationStatisticResultMng;
import com.jwzt.statistic.utils.ConfigUtils;
import com.jwzt.statistic.utils.ExcelUtils;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class EnterpriseAct {
	private static final Logger log = LogManager.getLogger();

	@RequestMapping("/enterprise/page")
	public String page(Integer enterpriseId, String enterpriseName, Integer certStatus, Integer entype, String bindPhone,Integer stamp,
			Date startTime, Date endTime,HttpServletRequest request, ModelMap mp, Integer pageNo) {
		try {
			if (null != enterpriseName) {
				enterpriseName = new String(enterpriseName.getBytes("iso-8859-1"), "utf-8");
			}
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if(null == certStatus){
				certStatus = -1;
			}
			if(null == entype){
				entype = -1;
			}
			if(null == stamp){
				stamp = -1;
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			Pagination page = enterpriseInfoMng.pageByParams(enterpriseId, enterpriseName, null, null, startTime,
					endTime, true, checkPageNo(pageNo), CookieUtils.getPageSize(request),certStatus,entype,bindPhone,stamp);
			RenderJsonUtils.addSuccess(mp, page);
			return "renderJson";
		} catch (Exception e) {
			log.error("EnterpriseAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/enterprise/excel_export")
	public void excelExport(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			HttpServletRequest request, HttpServletResponse response, ModelMap mp) {
		try {
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
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<EnterpriseInfo> beanList = enterpriseInfoMng.listByParams(enterpriseId, enterpriseName, startTime,
					endTime, true);
			if (null != beanList) {
				for (EnterpriseInfo enterpriseInfo : beanList) {
					if (null != enterpriseInfo) {
						Map<String, Object> dataMap = new HashMap<String, Object>();
						Integer id = enterpriseInfo.getId();
						String enterpriseName2 = enterpriseInfo.getEnterpriseName();
						//String contactPhone = enterpriseInfo.getContactPhone();
						String bindPhone = enterpriseInfo.getBindPhone();
						Integer certStatus = enterpriseInfo.getCertStatus();
						Integer stamp = enterpriseInfo.getStamp();
						Integer entype = enterpriseInfo.getEntype();
						dataMap.put("id", id);
						dataMap.put("enterpriseName", enterpriseName2);
						//dataMap.put("contactPhone", contactPhone);
						//绑定手机号
						dataMap.put("bindPhone", bindPhone);
						switch (certStatus) {
						case 0:
							dataMap.put("certStatus", "未提交认证");
							break;
						case 1:
							dataMap.put("certStatus", "认证中");
							break;
						case 4:
							dataMap.put("certStatus", "认证通过");
							break;
						case 5:
							dataMap.put("certStatus", "认证失败");
							break;
						default:
							dataMap.put("certStatus", "认证中");
							break;
						}
						if (stamp == 0) {
							dataMap.put("stamp", "个人");
						}else {
							dataMap.put("stamp", "企业");
						}
						if (entype==1) {
							dataMap.put("entype", "外部测试");
						}else if(entype == 2){
							dataMap.put("entype", "内部测试");
						}else if (entype == 3) {
							dataMap.put("entype", "签约用户");
						}else {
							dataMap.put("entype", "");
						}
						
						EnterpriseStatisticResult statisticResult = enterpriseInfo.getStatisticResult();
						if (null != statisticResult) {
							Long fansNum = statisticResult.getFansNum();
							Long liveEventTotalNum = statisticResult.getLiveEventTotalNum();
							Long liveTotalDuration = statisticResult.getLiveTotalDuration();
							Long liveViewTotalNum = statisticResult.getLiveViewTotalNum();
							Long liveViewUserTotalNum = statisticResult.getLiveViewUserTotalNum();
							Long liveLikeTotalNum = statisticResult.getLiveLikeTotalNum();
							Long liveCommentTotalNum = statisticResult.getLiveCommentTotalNum();
							Double redPacketMoney = statisticResult.getRedPacketMoney();
							Long giftNum = statisticResult.getGiftNum();
							DecimalFormat df = new DecimalFormat("#0.00");
							dataMap.put("fansNum", fansNum);
							dataMap.put("liveEventTotalNum", liveEventTotalNum);
							dataMap.put("liveTotalDuration", df.format(liveTotalDuration / 3600.0));
							dataMap.put("liveViewTotalNum", liveViewTotalNum);
							dataMap.put("liveViewUserTotalNum", liveViewUserTotalNum);
							dataMap.put("liveLikeTotalNum", liveLikeTotalNum);
							dataMap.put("liveCommentTotalNum", liveCommentTotalNum);
							dataMap.put("redPacketMoney", redPacketMoney);
							dataMap.put("giftNum", giftNum);
						}
						dataList.add(dataMap);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileName = "企业_" + sdf.format(new Date());
			String[] keys = { "id", "enterpriseName", "bindPhone","certStatus","stamp","entype", "fansNum", "liveEventTotalNum",
					"liveTotalDuration", "liveViewTotalNum", "liveViewUserTotalNum", "liveLikeTotalNum",
					"liveCommentTotalNum", "redPacketMoney", "giftNum" };
			String[] columnNames = { "企业ID", "企业名称", "绑定手机号","认证状态","认证类型","企业类型", "粉丝数", "企业直播场次（次）", "累计直播时长（小时）", "观看累计次数（次）", "观看累计人数（人）",
					"被点赞累计数（次）", "被评论累计数（次）", "发出红包总金额（元）", "收到礼物次数（次）" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("EnterpriseAct total error.", e);
		}
	}

	@RequestMapping("/enterprise/beginLiveForColumn")
	public String beginLiveForColumn(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(enterpriseId,
					startTime, endTime);
			Long beginLiveHour0To3 = enterpriseStatisticResult.getBeginLiveHour0To3();
			Long beginLiveHour3To6 = enterpriseStatisticResult.getBeginLiveHour3To6();
			Long beginLiveHour6To9 = enterpriseStatisticResult.getBeginLiveHour6To9();
			Long beginLiveHour9To12 = enterpriseStatisticResult.getBeginLiveHour9To12();
			Long beginLiveHour12To15 = enterpriseStatisticResult.getBeginLiveHour12To15();
			Long beginLiveHour15To18 = enterpriseStatisticResult.getBeginLiveHour15To18();
			Long beginLiveHour18To21 = enterpriseStatisticResult.getBeginLiveHour18To21();
			Long beginLiveHour21To24 = enterpriseStatisticResult.getBeginLiveHour21To24();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("beginLiveHour0To3", NumberUtils.checkNumber(beginLiveHour0To3));
			dataMap.put("beginLiveHour3To6", NumberUtils.checkNumber(beginLiveHour3To6));
			dataMap.put("beginLiveHour6To9", NumberUtils.checkNumber(beginLiveHour6To9));
			dataMap.put("beginLiveHour9To12", NumberUtils.checkNumber(beginLiveHour9To12));
			dataMap.put("beginLiveHour12To15", NumberUtils.checkNumber(beginLiveHour12To15));
			dataMap.put("beginLiveHour15To18", NumberUtils.checkNumber(beginLiveHour15To18));
			dataMap.put("beginLiveHour18To21", NumberUtils.checkNumber(beginLiveHour18To21));
			dataMap.put("beginLiveHour21To24", NumberUtils.checkNumber(beginLiveHour21To24));
			
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("EnterpriseAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/enterprise/beginLocation")
	public String beginLocation(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(
					null != enterpriseId ? Long.valueOf(enterpriseId) : null,
					LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE, startTime, endTime);
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
			log.error("EnterpriseAct viewLocation error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/enterprise/beginLocationExcelExport")
	public void beginLocationExcelExport(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(
					null != enterpriseId ? Long.valueOf(enterpriseId) : null,
					LocationStatisticResult.FLAG_TYPE_ENTERPRISE_BEGIN_LIVE, startTime, endTime);
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
			String fileName = "企业直播推流ip地域分布_" + sdf.format(new Date());
			String[] keys = { "provinceName", "totalNum" };
			String[] columnNames = { "省份", "观众人数" };
			// excel填充数据
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ExcelUtils.createWorkBook(ExcelUtils.createExcelRecordFromMap(dataList, keys), keys, columnNames).write(os);
			ResponseUtils.downloadHandler(fileName, os, response);
		} catch (Exception e) {
			log.error("EnterpriseAct beginLocationExcelExport error.", e);
		}
	}

	@RequestMapping("/enterprise/liveTotal")
	public String liveTotal(Integer enterpriseId, HttpServletRequest request, ModelMap mp){
		try{
			LiveInfo liveInfo = liveInfoMng.sumByLiveMostEnterprise();
			//LiveInfo liveInfo = liveInfoMng.sumByLiveMostEnterprise(enterpriseId);
			int liveEvent30DaysTotalNum = liveInfoMng.enterpriseByMost();//平台近30天活跃商户
			Long liveEventMostTotalNum = liveInfo.getLiveEventTotalNum();//发起直播最多的商户的发起直播场数
			Long liveViewUserTotalNum = liveInfo.getLiveViewUserTotalNum();//发起直播最多的商户的总观看人数
			String liveMostEnterpriseName = liveInfo.getEnterpriseName();//发起直播最多的商户的商户名称

			int enterpriseTotalNum = 0 , enterpriseFormalContractTotalNum = 0 ;
			try{
				String getVideoListUrl = ConfigUtils.get(ConfigUtils.GET_ENTERPRISE_NUM_URL);
				String getVideoListResultJson = HttpUtils.sendPost(getVideoListUrl, "", "utf-8");
				Map<?, ?> getVideoListResultMap = JsonUtils.jsonToMap(getVideoListResultJson);
				// 回看同步
				int status = (int) getVideoListResultMap.get("status");
				if(status == 1){
					enterpriseTotalNum = (int) getVideoListResultMap.get("userNum");//平台总商户数
					enterpriseFormalContractTotalNum =  (int)getVideoListResultMap.get("contractUserNum");//正式签约商户
				}
			}catch(Exception e){
				
			}

			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("enterpriseTotalNum", enterpriseTotalNum );
			dataMap.put("enterpriseFormalContractTotalNum", enterpriseFormalContractTotalNum);
			dataMap.put("liveEvent30DaysTotalNum", liveEvent30DaysTotalNum);
			dataMap.put("liveMostEnterpriseName", liveMostEnterpriseName);
			dataMap.put("liveEventMostTotalNum", liveEventMostTotalNum );
			dataMap.put("liveViewTotalUser", liveViewUserTotalNum);
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("TotalAct liveTotal error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}
	
	@RequestMapping("/enterprise/total")
	public String total(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(enterpriseId,
					startTime, endTime);
			// 直播总场次
			Long liveEventTotalNum = enterpriseStatisticResult.getLiveEventTotalNum();
			// 企业粉丝数
			Long fansNum = enterpriseStatisticResult.getFansNum();
			// 累计总观看人数
			Long liveViewUserTotalNum = enterpriseStatisticResult.getLiveViewUserTotalNum();
			// 累计总观看次数
			Long liveViewTotalNum = enterpriseStatisticResult.getLiveViewTotalNum();
			// 直播总时长
			Long liveTotalDuration = enterpriseStatisticResult.getLiveTotalDuration();
			// 平均直播总时长
			Long liveTotalAverageDuraion;
			if (null != liveEventTotalNum && liveEventTotalNum > 0) {
				liveTotalAverageDuraion = liveTotalDuration / liveEventTotalNum;
			} else {
				liveTotalAverageDuraion = 0L;
			}
			// 总分享数
			Long liveLikeTotalNum = enterpriseStatisticResult.getLiveLikeTotalNum();
			// 总点赞数
			Long liveShareTotalNum = enterpriseStatisticResult.getLiveShareTotalNum();
			// 总评论数
			Long liveCommentTotalNum = enterpriseStatisticResult.getLiveCommentTotalNum();
			Double redPacketMoney = enterpriseStatisticResult.getRedPacketMoney();
			Long giftNum = enterpriseStatisticResult.getGiftNum();
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("liveEventTotalNum", NumberUtils.checkNumber(liveEventTotalNum));
			dataMap.put("liveEnterpriseTotalNum", NumberUtils.checkNumber(fansNum));
			dataMap.put("liveViewUserTotalNum", NumberUtils.checkNumber(liveViewUserTotalNum));
			dataMap.put("liveViewTotalNum", NumberUtils.checkNumber(liveViewTotalNum));
			dataMap.put("liveTotalDuration", NumberUtils.checkNumber(liveTotalDuration));
			dataMap.put("liveTotalAverageDuraion", NumberUtils.checkNumber(liveTotalAverageDuraion));
			dataMap.put("liveLikeTotalNum", NumberUtils.checkNumber(liveLikeTotalNum));
			dataMap.put("liveShareTotalNum", NumberUtils.checkNumber(liveShareTotalNum));
			dataMap.put("liveCommentTotalNum", NumberUtils.checkNumber(liveCommentTotalNum));
			dataMap.put("redPacketMoney", NumberUtils.checkNumber(redPacketMoney));
			dataMap.put("giftNum", NumberUtils.checkNumber(giftNum));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("TotalAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}
	}

	@RequestMapping("/enterprise/max")
	public String max(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request, ModelMap mp) {
		try {
			if (null != startTime) {
				startTime = new DateTime(startTime).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
						.withMillisOfSecond(0).toDate();
			}
			if (null != endTime) {
				endTime = new DateTime(endTime).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59)
						.withMillisOfSecond(999).toDate();
			}
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng
					.getLastBeanByEndTime(enterpriseId, endTime);
			Long numOfMaxViewUserNum = enterpriseStatisticResult.getNumOfMaxViewUserNumAboutLive();
			String enterpriseNameOfMaxViewUserNum = enterpriseStatisticResult
					.getEnterpriseNameOfMaxViewUserNumAboutLive();
			Timestamp timeOfMaxViewUserNum = enterpriseStatisticResult.getTimeOfMaxViewUserNumAboutLive();
			String liveTitleOfMaxViewUserNum = enterpriseStatisticResult.getLiveTitleOfMaxViewUserNumAboutLive();
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
	 * 用户观看来源：安卓、IOS、PC、WAP
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/enterprise/viewSource")
	public String viewSource(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(enterpriseId,
					startTime, endTime);
			Long androidUserViewNum = enterpriseStatisticResult.getAndroidViewUserNum();
			Long iosUserViewNum = enterpriseStatisticResult.getIosViewUserNum();
			Long pcUserViewNum = enterpriseStatisticResult.getPcViewUserNum();
			Long wapUserViewNum = enterpriseStatisticResult.getWapViewUserNum();
			Long otherUserViewNum = enterpriseStatisticResult.getOtherViewUserNum();
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
	@RequestMapping("/enterprise/viewTimeForLine")
	public String viewTimeForLine(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			List<EnterpriseStatisticResult> enterpriseStatisticResultList = enterpriseStatisticResultMng
					.listByParams(enterpriseId, startTime, endTime);
			for (EnterpriseStatisticResult enterpriseStatisticResult : enterpriseStatisticResultList) {
				if (null != enterpriseStatisticResult) {
					Long viewTotalDuration = enterpriseStatisticResult.getViewTotalDuration();
					Long viewTotalNum = enterpriseStatisticResult.getViewTotalNum();
					Timestamp statisticTime = enterpriseStatisticResult.getStatisticTime();
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

	@RequestMapping("/enterprise/viewTimeForColumn")
	public String viewTimeForColumn(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(enterpriseId,
					startTime, endTime);
			Long viewTimeHour0To3 = enterpriseStatisticResult.getViewTimeHour0To3();
			Long viewTimeHour3To6 = enterpriseStatisticResult.getViewTimeHour3To6();
			Long viewTimeHour6To9 = enterpriseStatisticResult.getViewTimeHour6To9();
			Long viewTimeHour9To12 = enterpriseStatisticResult.getViewTimeHour9To12();
			Long viewTimeHour12To15 = enterpriseStatisticResult.getViewTimeHour12To15();
			Long viewTimeHour15To18 = enterpriseStatisticResult.getViewTimeHour15To18();
			Long viewTimeHour18To21 = enterpriseStatisticResult.getViewTimeHour18To21();
			Long viewTimeHour21To24 = enterpriseStatisticResult.getViewTimeHour21To24();
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

	@RequestMapping("/enterprise/viewLocation")
	public String viewLocation(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			List<LocationStatisticResult> locationStatisticResultList = locationStatisticResultMng.listByFlag(
					null != enterpriseId ? Long.valueOf(enterpriseId) : null,
					LocationStatisticResult.FLAG_TYPE_ENTERPRISE_LIVE_VIEW, startTime, endTime);
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
			RenderJsonUtils.addSuccess(mp, dataList);
			return "renderJson";
		} catch (Exception e) {
			log.error("EnterpriseAct viewLocation error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
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
	@RequestMapping("/enterprise/viewTimeForPie")
	public String viewTimeForPie(Integer enterpriseId, Date startTime, Date endTime, HttpServletRequest request,
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
			EnterpriseStatisticResult enterpriseStatisticResult = enterpriseStatisticResultMng.sumByParams(enterpriseId,
					startTime, endTime);
			Long viewDuraionUserNum0To5 = enterpriseStatisticResult.getDurationViewUserNum0To5();
			Long viewDuraionUserNum5To10 = enterpriseStatisticResult.getDurationViewUserNum5To10();
			Long viewDuraionUserNum10To30 = enterpriseStatisticResult.getDurationViewUserNum10To30();
			Long viewDuraionUserNum30To60 = enterpriseStatisticResult.getDurationViewUserNum30To60();
			Long viewDuraionUserNum60To = enterpriseStatisticResult.getDurationViewUserNum60To();
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
	private EnterpriseStatisticResultMng enterpriseStatisticResultMng;
	@Autowired
	private LocationStatisticResultMng locationStatisticResultMng;
	@Autowired
	private EnterpriseInfoMng enterpriseInfoMng;
	@Autowired
	private LiveInfoMng liveInfoMng;
}
