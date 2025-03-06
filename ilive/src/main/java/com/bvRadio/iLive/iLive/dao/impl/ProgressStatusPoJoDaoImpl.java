package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ProgressStatusPoJoDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;
import com.bvRadio.iLive.iLive.entity.PictureInfo;
import com.bvRadio.iLive.iLive.entity.ProgressStatusPoJo;
import com.jwzt.DB.common.HibernateUtil;

@Repository
public class ProgressStatusPoJoDaoImpl extends HibernateBaseDao<ProgressStatusPoJo, Integer> implements ProgressStatusPoJoDao {

	@Override
	public ProgressStatusPoJo getUpdateTask(String taskUUID) {
		Session session = this.getSession();
		ProgressStatusPoJo statusPoJo = null;
		Query query = session.createQuery("from ProgressStatusPoJo where taskUUID='"+taskUUID+"'");
		statusPoJo = (ProgressStatusPoJo)query.uniqueResult();
		return statusPoJo;
	}

	@Override
	protected Class<ProgressStatusPoJo> getEntityClass() {
		// TODO Auto-generated method stub
		return ProgressStatusPoJo.class;
	}
	
	@Override
	public void save(ProgressStatusPoJo progressStatusPoJo) {
		this.getSession().save(progressStatusPoJo);
	}

}
