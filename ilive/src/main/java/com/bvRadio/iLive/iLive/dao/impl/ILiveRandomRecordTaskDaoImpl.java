package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Map;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveRandomRecordTaskDao;
import com.bvRadio.iLive.iLive.entity.ILiveRandomRecordTask;

public class ILiveRandomRecordTaskDaoImpl extends HibernateBaseDao<ILiveRandomRecordTask, Long>
		implements ILiveRandomRecordTaskDao {

	@Override
	protected Class<ILiveRandomRecordTask> getEntityClass() {
		return ILiveRandomRecordTask.class;
	}

	@Override
	public void addTask(ILiveRandomRecordTask task) {
		this.getSession().save(task);
	}

	@Override
	public void updateTask(ILiveRandomRecordTask task) {
		this.getSession().update(task);
	}

	@Override
	public ILiveRandomRecordTask getTask(Map<String, Object> sqlParam) {
		String hql = "from ILiveRandomRecordTask where userId=? and liveEventId=? and liveStatus=?";
		ILiveRandomRecordTask task = (ILiveRandomRecordTask) this.findUnique(hql, sqlParam.get("userId"),
				sqlParam.get("liveEventId"), sqlParam.get("liveStatus"));
		return task;
	}

	@Override
	public void saveTask(ILiveRandomRecordTask task) {
		this.getSession().save(task);
	}

	@Override
	public void updateRecordTask(ILiveRandomRecordTask task) {
		this.getSession().update(task);
	}

}
