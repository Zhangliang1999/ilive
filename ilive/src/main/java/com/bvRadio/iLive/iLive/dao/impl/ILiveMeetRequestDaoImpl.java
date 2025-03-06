package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveMeetRequestDao;
import com.bvRadio.iLive.iLive.entity.ILiveMeetRequest;

@Repository
public class ILiveMeetRequestDaoImpl extends HibernateBaseDao<ILiveMeetRequest, Long> implements ILiveMeetRequestDao {

	

	@Override
	public void save(ILiveMeetRequest ILiveMeetRequest) {
		this.getSession().save(ILiveMeetRequest);
	}

	@Override
	public void update(ILiveMeetRequest ILiveMeetRequesto) {
		this.getSession().update(ILiveMeetRequesto);
	}

	@Override
	public ILiveMeetRequest getById(String id) {
		String hql = "from ILiveMeetRequest where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveMeetRequest> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveMeetRequest> getEntityClass() {
		return ILiveMeetRequest.class;
	}

	
	

	@Override
	public ILiveMeetRequest getByMeetId(Integer roomId) {
		String hql = "from ILiveMeetRequest where roomId =:roomId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveMeetRequest> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public ILiveMeetRequest getHostByMeetId(Integer roomId) {
		String hql = "from ILiveMeetRequest where type=1 and roomId =:roomId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveMeetRequest> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public ILiveMeetRequest getStudentByMeetId(Integer roomId) {
		String hql = "from ILiveMeetRequest where type=3 and  roomId =:roomId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		List<ILiveMeetRequest> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	
}
