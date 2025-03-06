package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.statistic.dao.UserInfoDao;
import com.jwzt.statistic.entity.UserInfo;

@Repository
public class UserInfoDaoImpl extends BaseHibernateDao<UserInfo, Long> implements UserInfoDao {
	@SuppressWarnings("unchecked")
	@Override
	public Long countUserNum(Long[] ids, Date startTimeOfRegisterTime, Date endTimeOfRegisterTime,
			Integer terminalType) {
		Finder finder = Finder.create("select count(bean) from UserInfo bean where 1=1");
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
	public List<UserInfo> listByParams(Long[] ids, String username, Date startTimeOfRegisterTime,
			Date endTimeOfRegisterTime) {
		Finder finder = Finder.create("select bean from UserInfo bean where 1=1");
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
		List<UserInfo> list = find(finder);
		return list;
	}

	@Override
	public UserInfo getBeanById(Long id) {
		if (null != id) {
			return super.get(id);
		}
		return null;
	}

	@Override
	public UserInfo save(UserInfo bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<UserInfo> getEntityClass() {
		return UserInfo.class;
	}

}