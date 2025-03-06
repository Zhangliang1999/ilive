package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.hibernate3.Updater;
import com.jwzt.statistic.entity.UserInfo;

public interface UserInfoDao {
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<UserInfo> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);

	public UserInfo getBeanById(Long id);

	public UserInfo save(UserInfo bean);

	public UserInfo updateByUpdater(Updater<UserInfo> updater);

}