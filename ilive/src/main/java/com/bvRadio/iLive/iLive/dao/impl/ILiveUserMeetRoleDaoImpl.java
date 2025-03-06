package com.bvRadio.iLive.iLive.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveUserMeetRoleDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveUserMeetRole;

@Repository
public class ILiveUserMeetRoleDaoImpl extends HibernateBaseDao<ILiveUserMeetRole, Long> implements ILiveUserMeetRoleDao {

	

	@Override
	public void save(ILiveUserMeetRole ILiveUserMeetRole) {
		this.getSession().save(ILiveUserMeetRole);
	}

	@Override
	public void update(ILiveUserMeetRole ILiveUserMeetRoleo) {
		this.getSession().update(ILiveUserMeetRoleo);
	}

	@Override
	public ILiveUserMeetRole getById(Long id) {
		String hql = "from ILiveUserMeetRole where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		List<ILiveUserMeetRole> list = query.list();
		return list.get(0);
	}

	@Override
	protected Class<ILiveUserMeetRole> getEntityClass() {
		return ILiveUserMeetRole.class;
	}

	
	

	@Override
	public ILiveUserMeetRole getByUserMeetId(Integer roomId, String userId) {
		String hql = "from ILiveUserMeetRole where roomId =:roomId and userId=:userId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("userId", userId);
		List<ILiveUserMeetRole> list = query.list();
		if (list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}

	@Override
	public List<ILiveUserMeetRole> getListByType(Integer roomId, Integer type) {
		String hql = "from ILiveUserMeetRole where roomId =:roomId and roleType=:type";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		query.setParameter("type", type);
		List<ILiveUserMeetRole> list = query.list();
		return list;
	}

	@Override
	public void delete(Long id) {
		String hql = "delete from ILiveUserMeetRole where id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}


}
