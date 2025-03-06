package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveVoteProblemDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteProblem;

public class ILiveVoteProblemDaoImpl extends HibernateBaseDao<ILiveVoteProblem, Long> implements ILiveVoteProblemDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveVoteProblem> getListByVoteId(Long voteId) {
		String hql = "from ILiveVoteProblem where voteId = :voteId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteId", voteId);
		List<ILiveVoteProblem> list = query.list();
		return list;
	}

	@Override
	protected Class<ILiveVoteProblem> getEntityClass() {
		return ILiveVoteProblem.class;
	}

	@Override
	public void save(ILiveVoteProblem iLiveVoteProblem) {
		this.getSession().save(iLiveVoteProblem);
	}

	@Override
	public void update(ILiveVoteProblem iLiveVoteProblem) {
		this.getSession().update(iLiveVoteProblem);
	}

	@Override
	public void deleteById(Long id) {
		String hql ="delete from ILiveVoteProblem where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveVoteProblem getById(Long id) {
		return this.get(id);
	}

	@Override
	public void deleteAllByVoteId(Long voteId) {
		String hql ="delete from ILiveVoteProblem where voteId=:voteId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteId", voteId);
		query.executeUpdate();
	}

}
