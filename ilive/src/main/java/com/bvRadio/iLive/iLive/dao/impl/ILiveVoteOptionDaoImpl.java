package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveVoteOptionDao;
import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;

public class ILiveVoteOptionDaoImpl extends HibernateBaseDao<ILiveVoteOption, Long> implements ILiveVoteOptionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveVoteOption> getListByProblemId(Long problemId) {
		String hql = "from ILiveVoteOption where voteProblemId = :voteProblemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteProblemId", problemId);
		List<ILiveVoteOption> list = query.list();
		return list;
	}

	@Override
	protected Class<ILiveVoteOption> getEntityClass() {
		return ILiveVoteOption.class;
	}

	@Override
	public void save(ILiveVoteOption iLiveVoteOption) {
		this.getSession().save(iLiveVoteOption);
	}

	@Override
	public void update(ILiveVoteOption iLiveVoteOption) {
		this.getSession().update(iLiveVoteOption);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveVoteOption where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveVoteOption getById(Long id) {
		return this.get(id);
	}

	@Override
	public void deleteAllByProblemId(Long problemId) {
		String hql = "delete from ILiveVoteOption where voteProblemId=:voteProblemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("voteProblemId", problemId);
		query.executeUpdate();
	}


}
