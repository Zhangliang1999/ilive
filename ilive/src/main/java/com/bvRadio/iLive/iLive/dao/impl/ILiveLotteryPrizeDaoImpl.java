package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveLotteryPrizeDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryPrize;

@Repository
public class ILiveLotteryPrizeDaoImpl extends HibernateBaseDao<ILiveLotteryPrize, Long> implements ILiveLotteryPrizeDao {

	@Override
	public void save(ILiveLotteryPrize prize) {
		this.getSession().save(prize);
	}

	@Override
	protected Class<ILiveLotteryPrize> getEntityClass() {
		return ILiveLotteryPrize.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLotteryPrize> getListByLotteryId(Long lotteryId) {
		String hql = "from ILiveLotteryPrize where lotteryId=:lotteryId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryId", lotteryId);
		List<ILiveLotteryPrize> list = query.list();
		return list;
	}

	@Override
	public ILiveLotteryPrize getById(Long id) {
		return this.get(id);
	}

	@Override
	public void update(ILiveLotteryPrize iLiveLotteryPrize) {
		this.getSession().update(iLiveLotteryPrize);
	}

	@Override
	public void deleteAllByLotteryId(Long lotteryId) {
		String hql = "delete from ILiveLotteryPrize where lotteryId=:lotteryId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryId",lotteryId);
		query.executeUpdate();
	}

}
