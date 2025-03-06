package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveLotteryShareDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryShare;

@Repository
public class ILiveLotteryShareDaoImpl extends HibernateBaseDao<ILiveLotteryShare, Long> implements ILiveLotteryShareDao {

	@Override
	public void save(ILiveLotteryShare iLiveLotteryShare) {
		this.getSession().save(iLiveLotteryShare);
	}

	@Override
	public ILiveLotteryShare getRecordByUserId(Long userId,Long lotteryId) {
		String hql = "from ILiveLotteryShare where userId = :userId and lotteryId = :lotteryId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("lotteryId", lotteryId);
		List<ILiveLotteryShare> list = query.list();
		if (list.size()!=0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	protected Class<ILiveLotteryShare> getEntityClass() {
		return ILiveLotteryShare.class;
	}

}
