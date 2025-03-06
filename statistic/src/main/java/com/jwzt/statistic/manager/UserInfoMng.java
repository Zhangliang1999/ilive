package com.jwzt.statistic.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jwzt.statistic.entity.UserInfo;

public interface UserInfoMng {

	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType);

	public List<UserInfo> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime);

	public UserInfo getBeanById(Long id);

	public UserInfo save(UserInfo bean);

	public UserInfo update(UserInfo bean);

	public UserInfo saveOrUpdateFromDataMap(final Map<?, ?> dataMap);
}