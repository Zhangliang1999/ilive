package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.AssistentLoginDao;
import com.bvRadio.iLive.iLive.entity.AssistentLogin;

public class AssistentLoginDaoImpl extends HibernateBaseDao<AssistentLogin, Long> implements
		AssistentLoginDao {

	@Override
	public void addAssistentLogin(AssistentLogin assistentLogin)
			throws Exception {
		getSession().save(assistentLogin);
	}

	@Override
	public Pagination selectAssistentLoginByPage(Integer roomId,Integer pageNo,
			Integer pageSize) throws Exception {
		Finder finder = Finder.create(" from AssistentLogin bean where bean.roomId=:roomId");
		finder.setParam("roomId", roomId);
		finder.append(" order by bean.loginTime desc");
		Pagination pagination = find(finder, pageNo, pageSize);
		return pagination;
	}

	@Override
	protected Class<AssistentLogin> getEntityClass() {
		return AssistentLogin.class;
	}

}
