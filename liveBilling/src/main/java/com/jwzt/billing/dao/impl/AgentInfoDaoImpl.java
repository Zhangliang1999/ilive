package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.AgentInfoDao;
import com.jwzt.billing.entity.AgentInfo;
import com.jwzt.billing.utils.StringPatternUtil;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class AgentInfoDaoImpl extends BaseHibernateDao<AgentInfo, Integer> implements AgentInfoDao {

	@Override
	public Pagination pageByParams(String agentName, Integer status, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Finder finder = createFinderByParams(agentName, status, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentInfo> listByParams(String agentName, Integer status, Date startTime, Date endTime) {
		Finder finder = createFinderByParams(agentName, status, startTime, endTime);
		return find(finder);
	}

	@Override
	public AgentInfo getBeanById(Integer id) {
		AgentInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public AgentInfo save(AgentInfo bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<AgentInfo> getEntityClass() {
		return AgentInfo.class;
	}

	private Finder createFinderByParams(String agentName, Integer status, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from AgentInfo bean where 1=1");
		if (StringUtils.isNotBlank(agentName)) {
			boolean agentNameInt = StringPatternUtil.isInteger(agentName);
			if(agentNameInt){
				finder.append(" and (bean.id =:id or bean.agentName like:agentName)");
				finder.setParam("id", Long.parseLong(agentName));
				finder.setParam("agentName", "%"+agentName+"%");
			}else{
				finder.append(" and bean.agentName like :agentName");
				finder.setParam("agentName", "%"+agentName+"%");
			}
			}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc");
		return finder;
	}

}
