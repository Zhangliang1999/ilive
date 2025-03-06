package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.AgentInfo;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface AgentInfoDao {
	Pagination pageByParams(String agentName, Integer status, Date startTime, Date endTime, int pageNo, int pageSize);

	List<AgentInfo> listByParams(String agentName, Integer status, Date startTime, Date endTime);

	AgentInfo getBeanById(Integer id);

	AgentInfo save(AgentInfo bean);

	AgentInfo updateByUpdater(Updater<AgentInfo> updater);
}
