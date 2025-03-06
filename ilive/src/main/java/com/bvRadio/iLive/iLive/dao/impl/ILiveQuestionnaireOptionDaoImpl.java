package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireOptionDao;
import com.bvRadio.iLive.iLive.dao.ILiveVoteOptionDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;
import com.bvRadio.iLive.iLive.entity.ILiveVoteOption;

public class ILiveQuestionnaireOptionDaoImpl extends HibernateBaseDao<ILiveQuestionnaireOption, Long> implements ILiveQuestionnaireOptionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveQuestionnaireOption> getListByProblemId(Long problemId) {
		String hql = "from ILiveQuestionnaireOption where questionnaireProblemId = :questionnaireProblemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireProblemId", problemId);
		List<ILiveQuestionnaireOption> list = query.list();
		return list;
	}

	@Override
	protected Class<ILiveQuestionnaireOption> getEntityClass() {
		return ILiveQuestionnaireOption.class;
	}

	@Override
	public void save(ILiveQuestionnaireOption iLiveQuestionnaireOption) {
		this.getSession().save(iLiveQuestionnaireOption);
	}

	@Override
	public void update(ILiveQuestionnaireOption iLiveQuestionnaireOption) {
		this.getSession().update(iLiveQuestionnaireOption);
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveQuestionnaireOption where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveQuestionnaireOption getById(Long id) {
		return this.get(id);
	}

	@Override
	public void deleteAllByProblemId(Long problemId) {
		String hql = "delete from ILiveQuestionnaireOption where questionnaireProblemId=:questionnaireProblemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireProblemId", problemId);
		query.executeUpdate();
	}


}
