package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveFCodeDetailDao;
import com.bvRadio.iLive.iLive.entity.ILiveFCodeDetail;

@Repository
public class ILiveFCodeDetailDaoImpl extends HibernateBaseDao<ILiveFCodeDetail,Long> implements ILiveFCodeDetailDao {

	@Override
	public void saveListFCode(List<ILiveFCodeDetail> list) {
		for(ILiveFCodeDetail l:list) {
			this.getSession().save(l);
		}
	}

	@Override
	protected Class<ILiveFCodeDetail> getEntityClass() {
		return ILiveFCodeDetail.class;
	}
	
}
