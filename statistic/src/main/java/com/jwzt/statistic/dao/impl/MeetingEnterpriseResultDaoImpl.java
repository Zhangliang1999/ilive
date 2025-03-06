package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.MeetingEnterpriseResultDao;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;

@Repository
public class MeetingEnterpriseResultDaoImpl extends BaseHibernateDao<MeetingEnterpriseResult, Long> implements MeetingEnterpriseResultDao {
	@SuppressWarnings("unchecked")
	@Override
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		Finder finder = Finder.create("select count(bean) from MeetingEnterpriseResult bean where 1=1");
		if (null != ids && ids.length > 0) {
			finder.append(" and bean.id in (:ids)");
			finder.setParamList("ids", ids);
		}
		if (null != startTimeOfRegisterTime) {
			finder.append(" and bean.registerTime >= :startTimeOfRegisterTime");
			finder.setParam("startTimeOfRegisterTime", startTimeOfRegisterTime);
		}
		if (null != endTimeOfRegisterTime) {
			finder.append(" and bean.registerTime <= :endTimeOfRegisterTime");
			finder.setParam("endTimeOfRegisterTime", endTimeOfRegisterTime);
		}
		if (null != terminalType) {
			finder.append(" and bean.terminalType = :terminalType");
			finder.setParam("terminalType", terminalType);
		}
		List<Long> list = find(finder);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeetingEnterpriseResult> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		Finder finder = Finder.create("select bean from MeetingEnterpriseResult bean where 1=1");
		if (null != ids && ids.length > 0) {
			finder.append(" and bean.id in (:ids)");
			finder.setParamList("ids", ids);
		}
		if (StringUtils.isNotBlank(username)) {
			finder.append(" and (bean.nickName like :username or bean.phoneNum like :username)");
			finder.setParam("username", "%" + username + "%");
		}
		if (null != startTimeOfRegisterTime) {
			finder.append(" and bean.registerTime >= :startTimeOfRegisterTime");
			finder.setParam("startTimeOfRegisterTime", startTimeOfRegisterTime);
		}
		if (null != endTimeOfRegisterTime) {
			finder.append(" and bean.registerTime <= :endTimeOfRegisterTime");
			finder.setParam("endTimeOfRegisterTime", endTimeOfRegisterTime);
		}
		List<MeetingEnterpriseResult> list = find(finder);
		return list;
	}

	@Override
	public MeetingEnterpriseResult getBeanById(Long id) {
		if (null != id) {
			return super.get(id);
		}
		return null;
	}

	@Override
	public MeetingEnterpriseResult save(MeetingEnterpriseResult bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<MeetingEnterpriseResult> getEntityClass() {
		return MeetingEnterpriseResult.class;
	}

	@Override
	public Long getBean(Integer loginType) {
		Finder finder = Finder.create("select bean from MeetingEnterpriseResult bean where 1=1");
		if(loginType!=null){
			finder.append(" and bean.loginType = :loginType");
			finder.setParam("loginType", loginType);
		}
		Query query = finder.createQuery(getSession());
		List<MeetingEnterpriseResult> list = find(finder);
		return (long)list.size();
	}

	@Override
	public MeetingEnterpriseResult getBeanByEnterpriseId(Long enterpriseId) {
		Finder finder = Finder.create("select bean from MeetingEnterpriseResult bean where 1=1");
		if(enterpriseId!=null){
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		Query query = finder.createQuery(getSession());
		List<MeetingEnterpriseResult> list = find(finder);
		if(list!=null&&list.size()>1){
			return list.get(0);
		}
		return null;
	}

	@Override
	public MeetingEnterpriseResult update(MeetingEnterpriseResult bean) {
		getSession().update(bean);
		return bean;
	}

	@Override
	public List<MeetingEnterpriseResult> getList() {
		Finder finder = Finder.create("select bean from MeetingEnterpriseResult bean where 1=1 order by bean.useTime desc");
		
		Query query = finder.createQuery(getSession());
		List<MeetingEnterpriseResult> list = find(finder);
		if(list!=null&&list.size()>1){
			return list;
		}
		return null;
	}

}