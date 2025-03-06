package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveSignRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveSignRoom;

public class ILiveSignRoomDaoImpl extends HibernateBaseDao<ILiveSignRoom, Long> implements ILiveSignRoomDao {

	@Override
	public void save(ILiveSignRoom iLiveSignRoom) {
		this.getSession().save(iLiveSignRoom);
	}

	@Override
	public void update(ILiveSignRoom iLiveSignRoom) {
		this.getSession().update(iLiveSignRoom);
	}

	@Override
	public ILiveSignRoom selectStartSign(Integer roomId) {
		Finder finder = Finder.create("from ILiveSignRoom where roomId = :roomId and isOpen = 1");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveSignRoom> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find.get(0);
		}
		return null;
	}
	@Override
	public List<ILiveSignRoom> selectAllSign(Integer roomId) {
		Finder finder = Finder.create("from ILiveSignRoom where roomId = :roomId ");
		finder.setParam("roomId", roomId);
		@SuppressWarnings("unchecked")
		List<ILiveSignRoom> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find;
		}
		return null;
	}
	@Override
	public ILiveSignRoom selectSign(Integer roomId, Long signId) {
		Finder finder = Finder.create("from ILiveSignRoom where roomId = :roomId and signId = :signId");
		finder.setParam("roomId", roomId);
		finder.setParam("signId", signId);
		@SuppressWarnings("unchecked")
		List<ILiveSignRoom> find = find(finder);
		if (find!=null&&find.size()>0) {
			return find.get(0);
		}
		return null;
	}

	@Override
	protected Class<ILiveSignRoom> getEntityClass() {
		return ILiveSignRoom.class;
	}

}
