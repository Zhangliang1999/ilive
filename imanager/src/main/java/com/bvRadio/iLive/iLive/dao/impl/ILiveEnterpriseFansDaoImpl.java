package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseFansDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;

@Repository
public class ILiveEnterpriseFansDaoImpl extends HibernateBaseDao<ILiveEnterpriseFans, Integer>
		implements ILiveEnterpriseFansDao {

	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("select bean from ILiveEnterpriseFans bean where isDel = null and isBlacklist = null");
		if (queryNum != null) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName", "%" + queryNum + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("select bean from ILiveEnterpriseFans bean where isDel = null and isBlacklist = 1");
		if (queryNum != null) {
			finder.append(" and bean.userName like :userName");
			finder.setParam("userName", "%" + queryNum + "%");
		}
		return find(finder, pageNo == null ? 1 : pageNo, pageSize);
	}

	@Override
	protected Class<ILiveEnterpriseFans> getEntityClass() {
		return ILiveEnterpriseFans.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId = :enterpriseId and userId = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("userId", userId);
		List<ILiveEnterpriseFans> list = query.list();
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public void addEnterpriseConcern(ILiveEnterpriseFans fans) {
		this.getSession().save(fans);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isExist(Integer enterpriseId, Long userId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId = :enterpriseId and userId = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		query.setParameter("userId", userId);
		List<ILiveEnterpriseFans> list = query.list();
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void delFans(Long id) {
		String hql = "update ILiveEnterpriseFans set isDel = '1' where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public void letblack(Long id) {
		String hql = "update ILiveEnterpriseFans set isBlacklist = '1' where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public int getFansNum(Integer enterpriseId) {
		String hql = "select count(*) from ILiveEnterpriseFans where isDel = null";
		Query query = this.getSession().createQuery(hql);
		Long num = (Long) query.uniqueResult();
		int n = num.intValue();
		return n;
	}

	@Override
	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans) {
		this.getSession().delete(fans);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterpriseFans> getEnterpriseFansById(Integer enterpriseId) {
		String hql = "from ILiveEnterpriseFans where enterpriseId=:enterpriseId";
		Finder create = Finder.create(hql);
		create.setParam("enterpriseId", enterpriseId);
		return this.find(create);
	}

	@Override
	public void batchUpdateFans(List<ILiveEnterpriseFans> enterpriseList) {
		for (ILiveEnterpriseFans fans : enterpriseList) {
			fans.setForbidState(1);
			getSession().update(fans);
			getSession().flush();
		}
	}

	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId = :userId and (isDel = null or isDel = 0)");
		finder.setParam("userId", userId);
		return find(finder, pageNo==null?1:pageNo, pageSize==null?10:pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEnterpriseFans> getListByUserId(Long userId) {
		Finder finder = Finder
				.create("from ILiveEnterpriseFans where userId = :userId and (isDel = null or isDel = 0)");
		finder.setParam("userId", userId);
		return find(finder);
	}

}
