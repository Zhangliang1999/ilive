package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveConvertTaskDao;
import com.bvRadio.iLive.iLive.entity.ILiveConvertTask;

@Repository
public class ILiveConvertTaskDaoImpl extends HibernateBaseDao<ILiveConvertTask,Integer> implements ILiveConvertTaskDao {

	@Override
	public void saveConvertTask(ILiveConvertTask iLiveConvertTask) {
		this.getSession().save(iLiveConvertTask);
	}

	@Override
	public void updateConvertTask(ILiveConvertTask iLiveConvertTask) {
		this.getSession().update(iLiveConvertTask);
	}
	
	
	@Override
	protected Class<ILiveConvertTask> getEntityClass() {
		return ILiveConvertTask.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveConvertTask getConvertTask(Integer taskId) {
		String hql = "from ILiveConvertTask where taskId = :taskId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("taskId", taskId);
		List<ILiveConvertTask> list = query.list();
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveConvertTask> getConvertTaskByState(Integer state) {
		String hql = "from ILiveConvertTask where state = :state";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("state", state);
		List<ILiveConvertTask> list = query.list();
		return list;
	}

}
