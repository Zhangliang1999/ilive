package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.MeetingUserRecordDao;
import com.jwzt.statistic.entity.MeetingUserRecord;
import com.jwzt.statistic.entity.vo.MeetingEnterpriseResult;
import com.jwzt.statistic.entity.vo.MeetingStasticShowResult;

@Repository
public class MeetingUserRecordDaoImpl extends BaseHibernateDao<MeetingUserRecord, Long> implements MeetingUserRecordDao {
	@SuppressWarnings("unchecked")
	@Override
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		Finder finder = Finder.create("select count(bean) from MeetingUserRecord bean where 1=1");
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
	public List<MeetingUserRecord> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		Finder finder = Finder.create("select bean from MeetingUserRecord bean where 1=1");
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
		List<MeetingUserRecord> list = find(finder);
		return list;
	}

	@Override
	public MeetingUserRecord getBeanById(Long id) {
		if (null != id) {
			return super.get(id);
		}
		return null;
	}

	@Override
	public MeetingUserRecord save(MeetingUserRecord bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<MeetingUserRecord> getEntityClass() {
		return MeetingUserRecord.class;
	}

	@Override
	public Long getBean(Integer loginType) {
		Finder finder = Finder.create("select bean from MeetingUserRecord bean where 1=1");
		if(loginType!=null){
			finder.append(" and bean.loginType = :loginType");
			finder.setParam("loginType", loginType);
		}
		Query query = finder.createQuery(getSession());
		List<MeetingUserRecord> list = find(finder);
		return (long)list.size();
	}

}