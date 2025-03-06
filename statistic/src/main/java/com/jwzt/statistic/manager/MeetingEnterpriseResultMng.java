package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;


import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;

public interface MeetingEnterpriseResultMng {

	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<MeetingEnterpriseResult> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);
	
    public Long getBean(Integer loginType);
    
	public MeetingEnterpriseResult getBeanById(Long id);
	public MeetingEnterpriseResult getBeanByEnterpriseId(Long enterpriseId);
	public MeetingEnterpriseResult save(MeetingEnterpriseResult bean);
	public MeetingEnterpriseResult update(MeetingEnterpriseResult bean);

	public List<MeetingEnterpriseResult> getList();


	
}