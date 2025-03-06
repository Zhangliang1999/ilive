package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveGuestsDao;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;

public class ILiveGuestsDaoImpl  extends HibernateBaseDao<ILiveGuests, Long> implements ILiveGuestsDao {

	@Override
	public ILiveGuests insertILiveGuests(ILiveGuests iLiveGuests)
			throws Exception {
		ILiveGuests save = (ILiveGuests) getSession().save(iLiveGuests);
		return save;
	}

	@Override
	protected Class<ILiveGuests> getEntityClass() {
		return ILiveGuests.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveGuests> selectILiveGuestsList(Integer roomId,
			boolean isDelete)  throws Exception {
		Finder finder = Finder.create("select bean from ILiveGuests bean ");
		finder.append(" where bean.iLiveLiveRoom.roomId = :roomId");
		finder.setParam("roomId", roomId);
		finder.append(" and bean.isDelete = :isDelete");
		finder.setParam("isDelete", isDelete);
		return find(finder);
	}

	@Override
	public ILiveGuests selectILiveGuestsByID(Long guestsId) throws Exception {
		ILiveGuests iLiveGuests = get(guestsId);
		return iLiveGuests;
	}

	@Override
	public void deleteILiveGuests(Long guestsId) throws Exception {
		ILiveGuests iLiveGuests = get(guestsId);
		iLiveGuests.setIsDelete(true);
		getSession().update(iLiveGuests);
	}

}
