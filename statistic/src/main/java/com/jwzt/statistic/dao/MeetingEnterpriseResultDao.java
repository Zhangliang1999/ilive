package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;

public interface MeetingEnterpriseResultDao {
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<MeetingEnterpriseResult> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);

	public MeetingEnterpriseResult getBeanById(Long id);

	public MeetingEnterpriseResult save(MeetingEnterpriseResult bean);

	public MeetingEnterpriseResult updateByUpdater(Updater<MeetingEnterpriseResult> updater);

	public Long getBean(Integer loginType);

	public MeetingEnterpriseResult getBeanByEnterpriseId(Long enterpriseId);

	public MeetingEnterpriseResult update(MeetingEnterpriseResult bean);

	public List<MeetingEnterpriseResult> getList();

}