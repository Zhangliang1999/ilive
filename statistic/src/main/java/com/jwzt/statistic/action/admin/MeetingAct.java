package com.jwzt.statistic.action.admin;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.MeetingStatisticResult;
import com.jwzt.statistic.entity.MeetingUserRecord;
import com.jwzt.statistic.entity.TotalStatisticResult;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.MeetingEnterpriseResultMng;
import com.jwzt.statistic.manager.MeetingStatisticResultMng;
import com.jwzt.statistic.manager.MeetingUserRecordMng;
import com.jwzt.statistic.utils.NumberUtils;

@Controller
public class MeetingAct {
	private static final Logger log = LogManager.getLogger();
	/**
	 * 会议直播统计
	 * 
	 * @param startTime
	 * @param endTime
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/meeting/total")
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
			//获取会议的总直播时长、平均直播时长、总数、总录制数、总转播数
			MeetingStasticShowResult meetingStatisticResult = meetingStatisticResultMng.sumByParams();
			Long meetingSumTime=meetingStatisticResult.getMeetingSumTime()/3600L;
			Double meetingAvgTime=(meetingStatisticResult.getMeetingAvgTime()/3600.0);
			Long totalNum=meetingStatisticResult.getTotalNum();
			DecimalFormat df = new DecimalFormat("#.00");
			//获取会议时间分别在30分钟以内、30-1小时、1-2小时、2小时以上的数量
			Long num30=meetingStatisticResultMng.getBeanByMeetingTime(null, 1800L);
			Long num301=meetingStatisticResultMng.getBeanByMeetingTime(1800L, 3600L);
			Long num12=meetingStatisticResultMng.getBeanByMeetingTime(3600L, 7200L);
			Long num2=meetingStatisticResultMng.getBeanByMeetingTime(7200L, null);
			//获取会议在各个时段的召开次数
			List<MeetingStatisticResult> meeting0to1=meetingStatisticResultMng.listByParams(startTime, endTime);
			//查询所有企业数量
			List<EnterpriseInfo> enterpriseInfos=enterpriseInfoMng.listByParams(null, null, null, null, false);
			Long enterpriseTotalNum=(long)enterpriseInfos.size();
			//查询前10企业
			//List testlist=meetingStatisticResultMng.top10();
			
			List<MeetingEnterpriseResult> EnterpriseResult=meetingEnterpriseResultMng.getList();
			//使用会议的企业数
			//暂时
			List usedEnterpriseResult=meetingStatisticResultMng.top10();
			Long usedEnterpriseNum=(long)usedEnterpriseResult.size();
			//占比
			long enterpriseRate=(usedEnterpriseNum/enterpriseTotalNum)*100;
			//会议总召开数
			Long meetingTotalNum=0L;
			List<MeetingStatisticResult> list=meetingStatisticResultMng.listByParams(null, null);
			meetingTotalNum=(long)list.size();
			//会议频次
			Long meetingRate=meetingTotalNum/usedEnterpriseNum;
			//pc参会数量
			Long PcSumNum=meetingUserRecordMng.getBean(MeetingUserRecord.LOGIN_TYPE_PC);
			//app参会数量
			Long APPSumNum=meetingUserRecordMng.getBean(MeetingUserRecord.LOGIN_TYPE_APP);
			//录制数量
			Long recordNum=meetingStatisticResultMng.getBeanByRecordOrRedirect(1, null);
			//转播数量
			Long redirectNum=meetingStatisticResultMng.getBeanByRecordOrRedirect(null, 1);
			//召开时段分布
		
			Long startTime0to1 =this.getTimeAreaNum(0);
			Long startTime1to2 =this.getTimeAreaNum(1);
			Long startTime2to3 =this.getTimeAreaNum(2);
			Long startTime3to4 =this.getTimeAreaNum(3);
			Long startTime4to5 =this.getTimeAreaNum(4);
			Long startTime5to6 =this.getTimeAreaNum(5);
			Long startTime6to7 =this.getTimeAreaNum(6);
			Long startTime7to8 =this.getTimeAreaNum(7);
			Long startTime8to9 =this.getTimeAreaNum(8);
			Long startTime9to10 =this.getTimeAreaNum(9);
			Long startTime10to11 =this.getTimeAreaNum(10);
			Long startTime11to12 =this.getTimeAreaNum(11);
			Long startTime12to13 =this.getTimeAreaNum(12);
			Long startTime13to14 =this.getTimeAreaNum(13);
			Long startTime14to15 =this.getTimeAreaNum(14);
			Long startTime15to16 =this.getTimeAreaNum(15);
			Long startTime16to17 =this.getTimeAreaNum(16);
			Long startTime17to18 =this.getTimeAreaNum(17);
			Long startTime18to19 =this.getTimeAreaNum(18);
			Long startTime19to20 =this.getTimeAreaNum(19);
			Long startTime20to21 =this.getTimeAreaNum(20);
			Long startTime21to22 =this.getTimeAreaNum(21);
			Long startTime22to23 =this.getTimeAreaNum(22);
			Long startTime23to24 =this.getTimeAreaNum(23);
			
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("meetingSumTime", meetingSumTime==null?0L:meetingSumTime);
			dataMap.put("meetingAvgTime", meetingAvgTime==null?0.0:df.format(meetingAvgTime));
			dataMap.put("num30", NumberUtils.checkNumber(num30));
			dataMap.put("num301", NumberUtils.checkNumber(num301));
			dataMap.put("num12", NumberUtils.checkNumber(num12));
			dataMap.put("num2", NumberUtils.checkNumber(num2));
//			if(EnterpriseResult!=null){
//				for(int i=0;i<10;i++){
//					dataMap.put("enterpriseName"+(i+1), EnterpriseResult.get(i).getEnterpriseName());
//					dataMap.put("enterpriseNum"+(i+1),NumberUtils.checkNumber( EnterpriseResult.get(i).getUseTime()));
//				}
//			}
			if(usedEnterpriseResult!=null&&usedEnterpriseResult.size()>0){
				for(int i=0;i<usedEnterpriseResult.size();i++){
					Object []enterpriseResult=(Object[]) usedEnterpriseResult.get(i);
					System.out.println(i+"======"+enterpriseResult[1]+":"+enterpriseResult[2]);
					dataMap.put("enterpriseName"+(i+1), enterpriseResult[1]);
					dataMap.put("enterpriseNum"+(i+1),NumberUtils.checkNumber(Long.parseLong(enterpriseResult[2].toString())));
				}
			}
			//参会方数
			for(int i=1;i<10;i++){
				Long figureNum=meetingStatisticResultMng.getBeanByFigure(i);
				dataMap.put("figureNum"+i,NumberUtils.checkNumber(figureNum));
			}
			dataMap.put("usedEnterpriseNum", NumberUtils.checkNumber(usedEnterpriseNum));
			dataMap.put("enterpriseRate",NumberUtils.checkNumber(enterpriseRate));
			dataMap.put("meetingTotalNum", NumberUtils.checkNumber(meetingTotalNum));
			dataMap.put("meetingRate", NumberUtils.checkNumber(meetingRate));
			dataMap.put("PcSumNum", NumberUtils.checkNumber(PcSumNum));
			dataMap.put("APPSumNum", NumberUtils.checkNumber(APPSumNum));
			dataMap.put("recordNum", NumberUtils.checkNumber(recordNum));
			dataMap.put("unRecordNum", NumberUtils.checkNumber(meetingTotalNum-recordNum));
			dataMap.put("redirectNum", NumberUtils.checkNumber(redirectNum));
			dataMap.put("unRedirectNum", NumberUtils.checkNumber(meetingTotalNum-redirectNum));
			dataMap.put("startTime0to1", NumberUtils.checkNumber(startTime0to1));
			dataMap.put("startTime1to2", NumberUtils.checkNumber(startTime1to2));
			dataMap.put("startTime2to3", NumberUtils.checkNumber(startTime2to3));
			dataMap.put("startTime3to4", NumberUtils.checkNumber(startTime3to4));
			dataMap.put("startTime4to5", NumberUtils.checkNumber(startTime4to5));
			dataMap.put("startTime5to6", NumberUtils.checkNumber(startTime5to6));
			dataMap.put("startTime6to7", NumberUtils.checkNumber(startTime6to7));
			dataMap.put("startTime7to8", NumberUtils.checkNumber(startTime7to8));
			dataMap.put("startTime8to9", NumberUtils.checkNumber(startTime8to9));
			dataMap.put("startTime9to10", NumberUtils.checkNumber(startTime9to10));
			dataMap.put("startTime10to11", NumberUtils.checkNumber(startTime10to11));
			dataMap.put("startTime11to12", NumberUtils.checkNumber(startTime11to12));
			dataMap.put("startTime12to13", NumberUtils.checkNumber(startTime12to13));
			dataMap.put("startTime13to14", NumberUtils.checkNumber(startTime13to14));
			dataMap.put("startTime14to15", NumberUtils.checkNumber(startTime14to15));
			dataMap.put("startTime15to16", NumberUtils.checkNumber(startTime15to16));
			dataMap.put("startTime16to17", NumberUtils.checkNumber(startTime16to17));
			dataMap.put("startTime17to18", NumberUtils.checkNumber(startTime17to18));
			dataMap.put("startTime18to19", NumberUtils.checkNumber(startTime18to19));
			dataMap.put("startTime19to20", NumberUtils.checkNumber(startTime19to20));
			dataMap.put("startTime20to21", NumberUtils.checkNumber(startTime20to21));
			dataMap.put("startTime21to22", NumberUtils.checkNumber(startTime21to22));
			dataMap.put("startTime22to23", NumberUtils.checkNumber(startTime22to23));
			dataMap.put("startTime23to24", NumberUtils.checkNumber(startTime23to24));
			RenderJsonUtils.addSuccess(mp, dataMap);
			return "renderJson";
		} catch (Exception e) {
			log.error("MeetingAct total error.", e);
			RenderJsonUtils.addError(mp, "error");
			return "renderJson";
		}

	}
   private Long getTimeAreaNum1(Integer start){
	   final Date startTime = new Date();
	   Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.set(Calendar.HOUR_OF_DAY, start);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date viewTimeHourStartTime = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, start);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date viewTimeHourEndTime = calendar.getTime();
		List<MeetingStatisticResult> list=meetingStatisticResultMng.listByParams(viewTimeHourStartTime, viewTimeHourEndTime);
		if(list!=null&&list.size()>0){
			return (long) list.size();
		}
	   return 0L;
	   
   }
	private Long getTimeAreaNum(Integer start){
		 Calendar calendar=Calendar.getInstance();
		 calendar.set(2019, 5, 12);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52); 
	     Date date=calendar.getTime();//date就是你需要的时间
	    // System.out.println(date);
	     Date date1=new Date();
	     Integer num=differentDays(date,date1);
	    // System.out.println(num);
	     Long subnum=0L;
	     for(int i=0;i<=num;i++){
	    	  Calendar calendar1 = Calendar.getInstance();
	    	  calendar1.setTime(date);
	    	  calendar1.add(calendar1.DAY_OF_YEAR,i);
	    	  Date date2=calendar1.getTime();
	    	  Calendar calendar2 = Calendar.getInstance();
	  		calendar2.setTime(date2);
	    	  calendar2.set(Calendar.HOUR_OF_DAY, start);
	  		calendar2.set(Calendar.MINUTE, 0);
	  		calendar2.set(Calendar.SECOND, 0);
	  		calendar2.set(Calendar.MILLISECOND, 0);
	  		Date viewTimeHourStartTime = calendar2.getTime();
	  		calendar2.set(Calendar.HOUR_OF_DAY, start);
	  		calendar2.set(Calendar.MINUTE, 59);
	  		calendar2.set(Calendar.SECOND, 59);
	  		calendar2.set(Calendar.MILLISECOND, 999);
	  		Date viewTimeHourEndTime = calendar2.getTime();
	  		List<MeetingStatisticResult> list=meetingStatisticResultMng.listByParams(viewTimeHourStartTime, viewTimeHourEndTime);
			if(list!=null&&list.size()>0){
				subnum=subnum+ (long) list.size();
			}
	     }
         return subnum;
	}
	/**
	 * date2比date1多的天数
	 * @param date1    
	 * @param date2
	 * @return    
	 */
	public static int differentDays(Date date1,Date date2)
	{
	    Calendar cal1 = Calendar.getInstance();
	    cal1.setTime(date1);
	    
	    Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(date2);
	   int day1= cal1.get(Calendar.DAY_OF_YEAR);
	    int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	    
	    int year1 = cal1.get(Calendar.YEAR);
	    int year2 = cal2.get(Calendar.YEAR);
	    if(year1 != year2)   //同一年
	    {
	        int timeDistance = 0 ;
	        for(int i = year1 ; i < year2 ; i ++)
	        {
	            if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	            {
	                timeDistance += 366;
	            }
	            else    //不是闰年
	            {
	                timeDistance += 365;
	            }
	        }
	        
	        return timeDistance + (day2-day1) ;
	    }
	    else    //不同年
	    {
	        System.out.println("判断day2 - day1 : " + (day2-day1));
	        return day2-day1;
	    }
	}
	@Autowired
	private MeetingStatisticResultMng meetingStatisticResultMng;
	@Autowired
	private EnterpriseInfoMng enterpriseInfoMng;
	@Autowired
	private MeetingUserRecordMng meetingUserRecordMng;
	@Autowired
	private MeetingEnterpriseResultMng meetingEnterpriseResultMng;
}
