package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveAppointmentDao;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;

@Repository
public class ILiveAppointmentDaoImpl extends HibernateBaseDao<ILiveAppointment, Integer> implements ILiveAppointmentDao {

	@Override
	public void saveILiveAppointment(ILiveAppointment iLiveAppointment) {
		this.getSession().save(iLiveAppointment);
	}

	@Override
	protected Class<ILiveAppointment> getEntityClass() {
		return ILiveAppointment.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveAppointment> getAppointmentList(String userid) {
		String hql = "from ILiveAppointment where userid = :userid";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userid", userid);
		return query.list();
	}

	@Override
	public ILiveAppointment getAppointmentByUseridAndEventid(String userid, Long eventId) {
		String hql = "from ILiveAppointment where userid = :userid and liveEventId = :eventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userid", userid);
		query.setParameter("eventId", eventId);
		@SuppressWarnings("unchecked")
		List<ILiveAppointment> list = query.list();
		if (list.size()==0) {
			return null;
		}else {
			return (ILiveAppointment) list.get(0);
		}
	}

	@Override
	public void cancelAppointment(String userid, Integer roomId) {
		String hql = "delete from ILiveAppointment where userid = :userid and roomId = :roomId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userid", userid);
		query.setParameter("roomId", roomId);
		query.executeUpdate();
		
	}

	@Override
	public boolean isAppointment(String userid, Long eventId) {
		String hql = "from ILiveAppointment where userid = :userid and liveEventId = :eventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userid", userid);
		query.setParameter("eventId", eventId);
		@SuppressWarnings("unchecked")
		List<ILiveAppointment> list = query.list();
		if (list.size()==0) {
			return false;
		}else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveAppointment> getAppointmentList(String userid, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveAppointment where userid = :userid");
		finder.setParam("userid", userid);
		Pagination find = find(finder, pageNo, pageSize);
		return (List<ILiveAppointment>) find.getList();
	}

	@Override
	public List<ILiveAppointment> getListByEventId(Long eventId) {
		String hql = "from ILiveAppointment where liveEventId = :liveEventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", eventId);
		@SuppressWarnings("unchecked")
		List<ILiveAppointment> list = query.list();
		return list;
	}

	@Override
	public List<String> getUserListByEventId(Long eventId) {
		String hql = "select userid from ILiveAppointment where liveEventId = :liveEventId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("liveEventId", eventId);
		@SuppressWarnings("unchecked")
		List<String> list = query.list();
		return list;
	}

	@Override
	public Pagination getPage(String name, Long eventId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveAppointment where liveEventId = :liveEventId");
		finder.setParam("liveEventId", eventId);
		finder.append(" order by id DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	public Pagination getAppointmentPage(String userid,Integer roomId, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from ILiveAppointment where userid = :userid");
		finder.setParam("userid", userid);
		if (roomId!=null) {
			finder.append(" and roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		finder.append(" order by createTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveAppointment> getAppointmentListByUserIdAndRoomId(String userid, Integer roomId) {
		Finder finder = Finder.create("from ILiveAppointment where userid = :userid");
		finder.setParam("userid", userid);
		if (roomId!=null) {
			finder.append(" and roomId = :roomId");
			finder.setParam("roomId", roomId);
		}
		finder.append(" order by createTime DESC");
		return find(finder);
	}

}
