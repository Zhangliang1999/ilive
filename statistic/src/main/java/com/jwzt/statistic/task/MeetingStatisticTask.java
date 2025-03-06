package com.jwzt.statistic.task;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.jwzt.statistic.entity.EnterpriseInfo;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.manager.EnterpriseInfoMng;
import com.jwzt.statistic.manager.MeetingEnterpriseResultMng;
import com.jwzt.statistic.manager.MeetingStatisticResultMng;
/**
 * 会议总体信息统计
 * @author zl
 *
 */
public class MeetingStatisticTask extends TimerTask {

	private static final Logger log = LogManager.getLogger();

	private static final Long DEFAULT_Meeting_FLAG_ID = -1L;
	private final Date startTime;
	private final Date endTime;

	public MeetingStatisticTask(final Date startTime, final Date endTime) {
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
			log.debug("MeetingStatisticTask run.");
			MeetingStatisticResultMng meetingStatisticResultMng = (MeetingStatisticResultMng) context
					.getBean("MeetingStatisticResultMng");
			EnterpriseInfoMng enterpriseInfoMng = (EnterpriseInfoMng) context.getBean("EnterpriseInfoMng");
			MeetingEnterpriseResultMng meetingEnterpriseResultMng = (MeetingEnterpriseResultMng) context.getBean("MeetingEnterpriseResultMng");
			List<EnterpriseInfo> enterpriseInfos=enterpriseInfoMng.listByParams(null, null, null, null, false);
            for(EnterpriseInfo info:enterpriseInfos){
            	Integer enterPriseId=info.getId();
            	//根据企业id查询使用次数
            Long num=meetingStatisticResultMng.getBeanByEnterpriseId(enterPriseId.longValue());
            MeetingEnterpriseResult result=meetingEnterpriseResultMng.getBeanByEnterpriseId(enterPriseId.longValue());
            if(result==null){
            	MeetingEnterpriseResult resultNew=new MeetingEnterpriseResult();
            	String id = UUID.randomUUID().toString().replace("-", "");
            	resultNew.setId(id);
            	resultNew.setEnterpriseId(enterPriseId.longValue());
            	resultNew.setEnterpriseName(info.getEnterpriseName());
            	resultNew.setUseTime(num);
            	meetingEnterpriseResultMng.save(resultNew);
            }else{
            	result.setUseTime(num);
            	meetingEnterpriseResultMng.update(result);
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
//			Long viewTimeHour0To3 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour3To6 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour6To9 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour9To12 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour12To15 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour15To18 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour18To21 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);
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
//			Long viewTimeHour21To24 = requestLogMng.countUserNum(null, null, null, null, countViewTimeHourLogTypes,
//					viewTimeHourStartTime, viewTimeHourEndTime);

		

			
			
			
		} catch (Exception e) {
			log.error("MeetingStatisticTask error.", e);
		}
	}

	
}