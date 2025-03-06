package com.bvRadio.iLive.manager.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.manager.dao.ILiveEnterpriseDao;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterprise> getList() {
		String hql = "from ILiveEnterprise where isDel ==0 ";
		Query query = this.getSession().createQuery(hql);
		List<ILiveEnterprise> list = query.list();
		return list;
	}

	@Override
	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize) {
		Finder finder = null;
		if (pass == 1) {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 2 and bean.isDel = 0");
		} else {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 0 and bean.isDel = 0");
		}
		if (content != null) {
			finder.append(" and bean.enterpriseRegName like :enterpriseRegName");
			finder.setParam("enterpriseRegName", "%" + content + "%");
		}
		if ((enterprisetype != null) && (!("0").equals(enterprisetype))) {
			finder.append(" and bean.enterpriseType = :enterpriseType");
			finder.setParam("enterpriseType", enterprisetype);
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize) {
		this.getSession().clear();
		Finder finder = null;
		if (pass == 1) {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 2 and bean.isDel = 0");
		} else {
			finder = Finder
					.create("select bean from ILiveEnterprise bean where bean.certStatus = 0 and bean.isDel = 0");
		}
		if (content != null) {
			finder.append(" and bean.enterpriseRegName like :enterpriseRegName");
			finder.setParam("enterpriseRegName", "%" + content + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public void update(ILiveEnterprise iLiveEnterprise) {
		ILiveEnterprise iLiveEnterpriseNew = this.get(iLiveEnterprise.getEnterpriseId());
		iLiveEnterprise.setCertStatus(iLiveEnterprise.getCertStatus());
		this.getSession().update(iLiveEnterpriseNew);
	}

	@Override
	public void del(Integer enterpriseId) {
		String hql = "update ILiveEnterprise set isDel = 1 where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.executeUpdate();
	}

	@Override
	public void updateEntity(ILiveEnterprise enterPrise) {
		this.getSession().update(enterPrise);
	}

}
