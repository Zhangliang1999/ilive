package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveLiangNumberDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiangNumber;

@Repository
public class ILiveLiangNumberDaoImpl extends HibernateBaseDao<ILiveLiangNumber, Integer> implements ILiveLiangNumberDao {
	public Pagination getPage(Integer id, Integer status,int pageNo, int pageSize) {
		Finder finder = Finder.create("select bean from ILiveLiangNumbe bean");
		finder.append(" where 1=1");
		if (null != id) {
			finder.append(" and bean.id = :id");
			finder.setParam("id", id);
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		return find(finder, pageNo, pageSize);
	}
	@Override
	public ILiveLiangNumber findByNumber(Integer number) {
		Finder finder = Finder.create("select bean from ILiveLiangNumbe bean");
		if(null!=number){
			finder.append(" and bean.liangNumber = :number");
			finder.setParam("number", number);
		}
		return (ILiveLiangNumber) find(finder).get(0);
	}

	public ILiveLiangNumber findById(Integer id) {
		ILiveLiangNumber entity = get(id);
		return entity;
	}

	public ILiveLiangNumber save(ILiveLiangNumber bean) {
		getSession().save(bean);
		return bean;
	}

	public void update(ILiveLiangNumber bean) {
		getSession().update(bean);
	}

	public ILiveLiangNumber deleteById(Integer id) {
		ILiveLiangNumber entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<ILiveLiangNumber> getEntityClass() {
		return ILiveLiangNumber.class;
	}


}