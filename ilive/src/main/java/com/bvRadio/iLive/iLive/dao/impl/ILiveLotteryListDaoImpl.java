package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveLotteryListDao;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryList;

@Repository
public class ILiveLotteryListDaoImpl extends HibernateBaseDao<ILiveLotteryList, Long> implements ILiveLotteryListDao {

	@Override
	public void save(ILiveLotteryList iliveLotteryList) {
		this.getSession().save(iliveLotteryList);
	}

	@Override
	protected Class<ILiveLotteryList> getEntityClass() {
		return ILiveLotteryList.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLotteryList> getWhiteListByPrizeId(Long prizeId) {
		String hql = "from ILiveLotteryList where lotteryPrizeId = :lotteryPrizeId and listType = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryPrizeId", prizeId);
		List<ILiveLotteryList> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveLotteryList> getBlackListByLotteryId(Long lotteryId) {
		String hql = "from ILiveLotteryList where lotteryId = :lotteryId and listType = 2";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryId", lotteryId);
		List<ILiveLotteryList> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isWhiteList(Long prizeId, String phone) {
		String hql = "from ILiveLotteryList where lotteryPrizeId = :lotteryPrizeId and phone = :phone and listType = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryPrizeId", prizeId);
		query.setParameter("phone", phone);
		List<ILiveLotteryList> list = query.list();
		if(list.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isBlackList(Long lotteryId, String phone) {
		String hql = "from ILiveLotteryList where lotteryId = :lotteryId and phone = :phone and listType = 2";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("lotteryId", lotteryId);
		query.setParameter("phone", phone);
		List<ILiveLotteryList> list = query.list();
		if (list.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}

}
