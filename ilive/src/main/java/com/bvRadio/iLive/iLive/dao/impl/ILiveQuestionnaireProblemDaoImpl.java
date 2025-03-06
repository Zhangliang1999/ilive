package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireProblemDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;

public class ILiveQuestionnaireProblemDaoImpl extends HibernateBaseDao<ILiveQuestionnaireProblem, Long> implements ILiveQuestionnaireProblemDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveQuestionnaireProblem> getListByQuestionnaireId(Long questionnaireId) {
		String hql = "from ILiveQuestionnaireProblem where questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		List<ILiveQuestionnaireProblem> list = query.list();
		return list;
	}

	@Override
	protected Class<ILiveQuestionnaireProblem> getEntityClass() {
		return ILiveQuestionnaireProblem.class;
	}

	@Override
	public void save(ILiveQuestionnaireProblem iLiveQuestionnaireProblem) {
		this.getSession().save(iLiveQuestionnaireProblem);
	}

	@Override
	public void update(ILiveQuestionnaireProblem iLiveQuestionnaireProblem) {
		this.getSession().update(iLiveQuestionnaireProblem);
	}

	@Override
	public void deleteById(Long id) {
		String hql ="delete from ILiveQuestionnaireProblem where id=:id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	@Override
	public ILiveQuestionnaireProblem getById(Long id) {
		return this.get(id);
	}

	@Override
	public void deleteAllByQuestionnaireId(Long questionnaireId) {
		String hql ="delete from ILiveQuestionnaireProblem where questionnaireId=:questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		query.executeUpdate();
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnaireProblem where questionnaireId = :questionnaireId");
		finder.setParam("questionnaireId", questionnaireId);
		finder.append(" order by id ASC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveQuestionnaireProblem> getResultById(Long questionnaireId, Long problemId, Integer pageNo,
			Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnaireProblem where questionnaireId = :questionnaireId and id=:id");
		finder.setParam("questionnaireId", questionnaireId);
		finder.setParam("id", problemId);
		finder.append(" order by id ASC");
		Pagination p=find(finder, pageNo, pageSize);
		return (List<ILiveQuestionnaireProblem>) p.getList();
	}

}
