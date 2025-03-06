package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireActivityDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;

public class ILiveQuestionnaireActivityDaoImpl extends HibernateBaseDao<ILiveQuestionnaireActivity, Long> implements ILiveQuestionnaireActivityDao {

	@Override
	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnaireActivity where roomId = :roomId");
		finder.setParam("roomId", roomId);
		if(name!=null && !"".equals(name)) {
			finder.append(" and questionnaireName like :questionnaireName");
			finder.setParam("questionnaireName", "%"+name+"%");
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}
	
	@Override
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, String time,Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create(" from ILiveQuestionnaireActivity where enterpriseId = :enterpriseId");
		finder.setParam("enterpriseId", enterpriseId);
		if(name!=null && !"".equals(name)) {
			finder.append(" and questionnaireName like :questionnaireName");
			finder.setParam("questionnaireName", "%"+name+"%");
		}
		if(time!=null){
			finder.append(" and startTime <= :time and endTime >= :time");
			finder.setParam("time", Timestamp.valueOf(time));
		}
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<ILiveQuestionnaireActivity> getEntityClass() {
		return ILiveQuestionnaireActivity.class;
	}

	@Override
	public ILiveQuestionnaireActivity getById(Long questionnaireId) {
		return this.get(questionnaireId);
	}

	@Override
	public void save(ILiveQuestionnaireActivity questionnaireActivity) {
		this.getSession().save(questionnaireActivity);
	}

	@Override
	public void update(ILiveQuestionnaireActivity questionnaireActivity) {
		this.getSession().update(questionnaireActivity);
	}

	@Override
	public ILiveQuestionnaireActivity getActivityByRoomId(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isSwitch = 1";
		String hql = "from ILiveQuestionnaireActivity where roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		ILiveQuestionnaireActivity activity = (ILiveQuestionnaireActivity) query.uniqueResult();
		return activity;
	}
	@Override
	public ILiveQuestionnaireActivity getActivityByEnterpriseId(Integer enterpriseId) {
		//String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		ILiveQuestionnaireActivity activity = (ILiveQuestionnaireActivity) query.uniqueResult();
		return activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 order by isSwitch Desc,id Desc";
		String hql = "from ILiveQuestionnaireActivity where roomId = :roomId and isEnd = 0 order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveQuestionnaireActivity> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveQuestionnaireActivity getH5Questionnaire(Integer roomId) {
		//String hql = "from ILiveVoteActivity where roomId = :roomId and isEnd = 0 and isSwitch = 1 ";
		String hql = "from ILiveQuestionnaireActivity where roomId = :roomId and isEnd = 0 ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveQuestionnaireActivity> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public void checkEndIsClose() {
		//String hql = "update ILiveVoteActivity set isSwitch = 0 where isEnd = 1";
//		String hql = "update ILiveVoteActivity set isSwitch = 0 where isEnd = 1";
//		Query query = this.getSession().createQuery(hql);
//		query.executeUpdate();
	}

	@Override
	public ILiveQuestionnaireActivity getActivityByenterpriseId(Integer enterpriseId) {
		//String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		String hql = "from ILiveQuestionnaireActivity where enterpriseId = :enterpriseId and isSwitch = 1";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveQuestionnaireActivity> list =  query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByEnterpriseId(Integer enterpriseId) {
		String hql = "from ILiveQuestionnaireActivity where enterpriseId = :enterpriseId and isEnd = 0 order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveQuestionnaireActivity> list = query.list();
		return list;
	}

	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList2(Integer enterpriseId) {
//		String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isEnd = 0 order by isSwitch Desc,id Desc";
//		Query query = this.getSession().createQuery(hql);
//		query.setParameter("enterpriseId", enterpriseId);
//		List<ILiveVoteActivity> list = query.list();
		return null;
	}

	@Override
	public ILiveQuestionnaireActivity getH5Questionnaire2(Integer enterpriseId) {
		/*String hql = "from ILiveVoteActivity where enterpriseId = :enterpriseId and isEnd = 0 and isSwitch = 1 ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<ILiveVoteActivity> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}*/
		return null;
	}

	@Override
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByUserId(Long userId) {
		String hql = "from ILiveQuestionnaireActivity where userId = :userId and isEnd = 0 order by id Desc";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List<ILiveQuestionnaireActivity> list = query.list();
		return list;
	}

	@Override
	public Pagination getpageByUserId(Long userId,String questionnairename,Integer pageNo,Integer pageSize) {
		Finder finder = Finder.create("from ILiveVoteActivity where userId = :userId and isEnd = 0 ");
		finder.setParam("userId", userId);
		if (questionnairename!=null&&!questionnairename.equals("")) {
			finder.append("and questionnairename like :questionnairename");
			finder.setParam("questionnairename", "%"+questionnairename+"%");
		}
		finder.append(" order by id Desc");
		return find(finder, pageNo, pageSize);
	}

}
