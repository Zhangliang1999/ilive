package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnairePeopleDao;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireStatisticDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

@Repository
public class ILiveQuestionnaireStatisticDaoImpl extends HibernateBaseDao<ILiveQuestionnaireActivityStatistic, Long> implements ILiveQuestionnaireStatisticDao {

	@Override
	protected Class<ILiveQuestionnaireActivityStatistic> getEntityClass() {
		return ILiveQuestionnaireActivityStatistic.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveQuestionnaireActivityStatistic getListByUserId(Long userId,Long QuestionnaireId) {
		String hql = "from ILiveQuestionnaireActivityStatistic where userId = :userId and questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("questionnaireId", QuestionnaireId);
		List<ILiveQuestionnaireActivityStatistic> find = query.list();
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}

	@Override
	public void update(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic) {
		this.getSession().update(iLiveQuestionnaireActivityStatistic);
		
	}

	@Override
	public void save(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic) {
		// TODO Auto-generated method stub
		this.getSession().save(iLiveQuestionnaireActivityStatistic);
	}

	@Override
	public Long maxId() {
		String hql = "from ILiveQuestionnaireActivityStatistic  order by id desc";
		Finder finder = Finder.create(hql);
		List<ILiveQuestionnaireActivityStatistic> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Long id=find.get(0).getId();
			return id+1;
		}else{
			return 1L;
		}
	}

	@Override
	public List<ILiveQuestionnaireActivityStatistic> getListByQuestionnaireId(Long questionnaireId) {
		String hql = "from ILiveQuestionnaireActivityStatistic where questionnaireId = :questionnaireId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("questionnaireId", questionnaireId);
		List<ILiveQuestionnaireActivityStatistic> find = query.list();
		if (find != null && !find.isEmpty()) {
			return find;
		}
		return null;
	}

	

}
