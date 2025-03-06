package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnairePeopleDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;

@Repository
public class ILiveQuestionnairePeopleDaoImpl extends HibernateBaseDao<ILiveQuestionnairePeople, Long> implements ILiveQuestionnairePeopleDao {

	@Override
	protected Class<ILiveQuestionnairePeople> getEntityClass() {
		return ILiveQuestionnairePeople.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveQuestionnairePeople> getListByUserId(Long userId,String mobile,Long questionnaireId) {
		String hql = "from ILiveQuestionnairePeople where (userId = :userId or mobile =:mobile) and questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("mobile", mobile);
		query.setParameter("questionnaireId", questionnaireId);
		List<ILiveQuestionnairePeople> list = query.list();
		return list;
	}

	@Override
	public Integer getAllPeopleByQuestionnaireId(Long questionnaireId) {
		String hql = "select count(*) from ILiveQuestionnairePeople where questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public Integer getPeopleByOptionId(Long optionId) {
		String hql = "select count(*) from ILiveQuestionnairePeople where questionnaireOptionId = :questionnaireOptionId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireOptionId", optionId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public void save(ILiveQuestionnairePeople iLiveQuestionnairePeople) {
		this.getSession().save(iLiveQuestionnairePeople);
	}

	@Override
	public Integer getPeopleByProblemId(Long problemId) {
		String hql = "select count(id) from ILiveQuestionnairePeople where questionnaireProblemId = :problemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("problemId", problemId);
		Long num = (Long)query.uniqueResult();
		return num.intValue();
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnairePeople where questionnaireId = :questionnaireId and id in(select max(id) from ILiveQuestionnairePeople group by userId)");
		finder.setParam("questionnaireId", questionnaireId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId) {
		String hql = "from ILiveQuestionnairePeople where  questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		List<ILiveQuestionnairePeople> list = query.list();
		return list;
	}

	@Override
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId, Long problemId) {
		String hql = "from ILiveQuestionnairePeople where  questionnaireId = :questionnaireId and questionnaireProblemId= :questionnaireProblemId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		query.setParameter("questionnaireProblemId", problemId);
		List<ILiveQuestionnairePeople> list = query.list();
		return list;
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Long problemId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnairePeople where questionnaireId = :questionnaireId and questionnaireProblemId =:questionnaireProblemId");
		finder.setParam("questionnaireId", questionnaireId);
		finder.setParam("questionnaireProblemId", problemId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

}
