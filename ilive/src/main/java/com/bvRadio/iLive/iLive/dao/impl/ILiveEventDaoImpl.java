package com.bvRadio.iLive.iLive.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEventDao;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;

@Repository
public class ILiveEventDaoImpl extends HibernateBaseDao<ILiveEvent, Long> implements ILiveEventDao {

	@Override
	public ILiveEvent selectILiveEventByID(Long liveEventId) {
		ILiveEvent iLiveEvent = get(liveEventId);
		return iLiveEvent;
	}

	@Override
	protected Class<ILiveEvent> getEntityClass() {
		return ILiveEvent.class;
	}

	@Override
	public Long saveIliveMng(ILiveEvent iLiveEvent) {
		this.getSession().save(iLiveEvent);
		return iLiveEvent.getLiveEventId();
	}

	@Override
	public void updateILiveEvent(ILiveEvent dbEvent) {
		this.getSession().update(dbEvent);
	}

	@Override
	public Pagination selectILiveEventPage(Integer roomId, Long liveEventId, Integer pageNo, Integer pageSize)
			throws Exception {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where bean.isDelete=:isDelete ");
		finder.setParam("isDelete", false);
		if (roomId != null) {
			finder.append(" and bean.roomId =:roomId ");
			finder.setParam("roomId", roomId);
		}
		// if(liveEventId!=null){
		// finder.append(" and bean.liveEventId !=:liveEventId ");
		// finder.setParam("liveEventId", liveEventId);
		// }
		Pagination find = find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public void updateILiveEventByIsDelete(Long liveEventId, boolean isDelete) throws Exception {
		ILiveEvent iLiveEvent = get(liveEventId);
		if (iLiveEvent != null) {
			iLiveEvent.setIsDelete(isDelete);
			getSession().update(iLiveEvent);
		}
	}

	@Override
	public void updateILiveEventByCommentsAllow(Long evenId, Integer commentsAllow) throws Exception {
		ILiveEvent iLiveEvent = get(evenId);
		if (iLiveEvent != null) {
			iLiveEvent.setCommentsAllow(commentsAllow);
			getSession().update(iLiveEvent);
		}
	}

	@Override
	public void updateILiveEventByCommentsAudit(Long evenId, Integer commentsAudit) throws Exception {
		ILiveEvent iLiveEvent = get(evenId);
		if (iLiveEvent != null) {
			iLiveEvent.setCommentsAudit(commentsAudit);
			getSession().update(iLiveEvent);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveEvent> getLiveEventByStartTime(Integer time) {
		String hql = "from ILiveEvent where (isNotify=0 or isNotify =null)";
		Query query = this.getSession().createQuery(hql);
		List<ILiveEvent> list = query.list();
		Iterator<ILiveEvent> iterator = list.iterator();
		List<ILiveEvent> listres = new LinkedList<>();
		long now = new Date().getTime();
		while (iterator.hasNext()) {
			ILiveEvent iLiveEvent = (ILiveEvent) iterator.next();
			if(iLiveEvent.getLiveStartTime().getTime()>now && iLiveEvent.getLiveStartTime().getTime()<(now+time*1000*60)) {
				listres.add(iLiveEvent);
			}
		}
		return listres;
//		String hql = "from ILiveEvent where liveStartTime between :nowTime and :designTime and (isNotify=0 or isNotify =null)";
//		Query query = this.getSession().createQuery(hql);
//		Long now = new Date().getTime();
//		Timestamp nowTime = new Timestamp(now);
//		Timestamp designTime = new Timestamp(now + time * 60 * 1000);
//		query.setParameter("nowTime", nowTime);
//		query.setParameter("designTime", designTime);
//		List<ILiveEvent> list = query.list();
//		return list;
	}

	@Override
	public Pagination getLiveEventsByRoomId(Integer roomId, Integer pageNo, Integer pageSize) {
		String hql = " from ILiveEvent where isDelete=0 and liveStatus=3 and roomId=:roomId order by liveStartTime desc ";
		Finder create = Finder.create(hql);
		create.setParam("roomId", roomId);
		return this.find(create, pageNo, pageSize);
	}

	@Override
	public List<ILiveEvent> getListEvent(Integer roomId) {
		String hql = "from ILiveEvent where roomId = :roomId order by liveEventId DESC";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveEvent> list = query.list();
		return list;
	}

}
