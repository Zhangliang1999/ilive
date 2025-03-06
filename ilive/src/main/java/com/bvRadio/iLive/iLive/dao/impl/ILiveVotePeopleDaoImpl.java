package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveVotePeopleDao;
import com.bvRadio.iLive.iLive.entity.ILiveVotePeople;

@Repository
public class ILiveVotePeopleDaoImpl extends HibernateBaseDao<ILiveVotePeople, Long> implements ILiveVotePeopleDao {

	@Override
	protected Class<ILiveVotePeople> getEntityClass() {
		return ILiveVotePeople.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveVotePeople> getListByUserId(Long userId,Long voteId) {
		String hql = "from ILiveVotePeople where userId = :userId and voteId = :voteId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("voteId", voteId);
		List<ILiveVotePeople> list = query.list();
		return list;
	}

	@Override
	public Integer getAllPeopleByVoteId(Long voteId) {
		String hql = "select count(*) from ILiveVotePeople where voteId = :voteId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteId", voteId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public Integer getPeopleByOptionId(Long optionId) {
		String hql = "select count(*) from ILiveVotePeople where voteOptionId = :voteOptionId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteOptionId", optionId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public void save(ILiveVotePeople iLiveVotePeople) {
		this.getSession().save(iLiveVotePeople);
	}

	@Override
	public Integer getPeopleByProblemId(Long problemId) {
		String hql = "select count(id) from ILiveVotePeople where voteProblemId = :problemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("problemId", problemId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

}
