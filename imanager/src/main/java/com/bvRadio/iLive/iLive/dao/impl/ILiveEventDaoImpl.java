package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

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
	public Pagination selectILiveEventPage(Integer roomId, Long liveEventId,
			Integer pageNo, Integer pageSize) throws Exception{
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where bean.isDelete=:isDelete ");
		finder.setParam("isDelete", false);
		if(roomId != null){
			finder.append(" and bean.roomId =:roomId ");
			finder.setParam("roomId", roomId);
		}
//		if(liveEventId!=null){
//			finder.append(" and bean.liveEventId !=:liveEventId ");
//			finder.setParam("liveEventId", liveEventId);
//		}
		Pagination find = find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public void updateILiveEventByIsDelete(Long liveEventId, boolean isDelete)
			throws Exception {
		ILiveEvent iLiveEvent = get(liveEventId);
		if(iLiveEvent!=null){
			iLiveEvent.setIsDelete(isDelete);
			getSession().update(iLiveEvent);
		}
	}

	@Override
	public Pagination getPageByRoomId(Integer roomId, Integer pageSize, Integer pageNo) {
		Finder finder = Finder.create("select bean from ILiveEvent bean");
		finder.append(" where bean.roomId=:roomId ");
		finder.setParam("roomId", roomId);
		Pagination find = find(finder, pageNo, pageSize);
		return find;
	}

	@Override
	public List<ILiveEvent> findAllEventByRoomId(Integer roomId) {
		Finder finder = Finder.create("from ILiveEvent where roomId = :roomId");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveEvent> find = find(finder);
		return find;
	}

}
