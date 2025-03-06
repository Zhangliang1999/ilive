package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jwzt.statistic.entity.MeetingUserRecord;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;

public interface MeetingUserRecordMng {

	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<MeetingUserRecord> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);
	
    public Long getBean(Integer loginType);
    
	public MeetingUserRecord getBeanById(Long id);

	public MeetingUserRecord save(MeetingUserRecord bean);



	
}