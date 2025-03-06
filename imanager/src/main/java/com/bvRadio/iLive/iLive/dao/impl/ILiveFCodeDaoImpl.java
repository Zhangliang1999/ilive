package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDao;
import com.bvRadio.iLive.iLive.entity.ILiveFCode;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;

@Repository
public class ILiveFCodeDaoImpl extends HibernateBaseDao<ILiveFCode, Long> implements ILiveFCodeDao {

	@Override
	public boolean saveFCode(ILiveFCode liveCode) {
		this.getSession().save(liveCode);
		return true;
	}

	@Override
	protected Class<ILiveFCode> getEntityClass() {
		return ILiveFCode.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ILiveFCode> getAllFCode() {
		String hql = "from ILiveFCode where isDelete = 0";
		Query query = this.getSession().createQuery(hql);
		List<ILiveFCode> list = query.list();
		return list;
	}

	@Override
	public void deleteByCodeId(ILiveFCode liveFCode) {
		this.getSession().update(liveFCode);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ILiveFCode getById(Long codeId) {
		String hql = "from ILiveFCode where codeId = " + codeId + "";
		Query query = this.getSession().createQuery(hql);
		List<ILiveFCode> list = query.list();
		return list.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ILiveFCodeDetail> getDetailByCodeId(Long codeId) {
		String hql = "from ILiveFCodeDetail where codeId = " + codeId + "";
		Query query = this.getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public Pagination getMainPager(String userName, int cpn, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveFCode bean");
		return find(finder, cpn, pageSize);
	}

}
