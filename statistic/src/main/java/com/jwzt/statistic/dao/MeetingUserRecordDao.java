package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.MeetingUserRecord;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;

public interface MeetingUserRecordDao {
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<MeetingUserRecord> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);

	public MeetingUserRecord getBeanById(Long id);

	public MeetingUserRecord save(MeetingUserRecord bean);

	public MeetingUserRecord updateByUpdater(Updater<MeetingUserRecord> updater);

	public Long getBean(Integer loginType);

}