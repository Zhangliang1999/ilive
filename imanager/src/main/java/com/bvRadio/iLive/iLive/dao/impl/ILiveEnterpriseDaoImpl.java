package com.bvRadio.iLive.iLive.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;

@Repository
public class ILiveEnterpriseDaoImpl extends HibernateBaseDao<ILiveEnterprise, Integer> implements ILiveEnterpriseDao {

	@Override
	protected Class<ILiveEnterprise> getEntityClass() {
		return ILiveEnterprise.class;
	}

	@Override
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId) {
		return this.get(enterpriseId);
	}

	@Override
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise) {
		this.getSession().save(iLiveEnterprise);
		return true;
	}

	@Override
	public boolean deleteILiveEnterprise(Integer enterpriseId) {
		Session session = this.getSession();
		ILiveEnterprise enterprise = (ILiveEnterprise) session.get(ILiveEnterprise.class, enterpriseId);
		session.delete(enterprise);
		return true;
	}

	@Override
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		Finder finder = Finder.create("select enterprise from ILiveEnterprise enterprise where 1=1 ");
		if (keyWord != null) {
			boolean roomNameInt = StringPatternUtil.isInteger(keyWord);
			if (roomNameInt) {
				finder.append(" and cast(enterprise.enterpriseId as string) like :enterpriseId");
				finder.setParam("enterpriseId", "%" + keyWord + "%");
			} else {
				finder.append(" and enterprise.enterpriseName like :enterpriseName");
				finder.setParam("enterpriseName", "%" + keyWord + "%");
			}
			finder.append(" and enterprise.isDel =:isDel");
			finder.setParam("isDel", 0);
		} else {
			finder.append(" and enterprise.isDel =:isDel");
			finder.setParam("isDel", 0);
		}
		// 认证通过的
		finder.append(" and enterprise.certStatus=2");
		return find(finder, pageNo, pageSize);
	}

}
