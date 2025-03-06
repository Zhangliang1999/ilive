package com.jwzt.billing.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.FunctionInfoDao;
import com.jwzt.billing.entity.FunctionInfo;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;

/**
 * @author ysf
 */
@Repository
public class FunctionInfoDaoImpl extends BaseHibernateDao<FunctionInfo, Integer> implements FunctionInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FunctionInfo> listByParams(Integer parentId) {
		Finder finder = createFinderByParams(parentId);
		return find(finder);
	}

	@Override
	public FunctionInfo getBeanById(Integer id) {
		FunctionInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	protected Class<FunctionInfo> getEntityClass() {
		return FunctionInfo.class;
	}

	private Finder createFinderByParams(Integer parentId) {
		Finder finder = Finder.create("select bean from FunctionInfo bean where 1=1");
		if (null != parentId) {
			finder.append(" and bean.parentId = :parentId");
			finder.setParam("parentId", parentId);
		}
		finder.append(" order by bean.id asc,createTime asc");
		return finder;
	}

}
