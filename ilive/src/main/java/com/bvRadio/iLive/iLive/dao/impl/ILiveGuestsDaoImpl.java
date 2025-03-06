package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveGuestsDao;
import com.bvRadio.iLive.iLive.entity.ILiveGuests;

public class ILiveGuestsDaoImpl  extends HibernateBaseDao<ILiveGuests, Integer> implements ILiveGuestsDao {

	@Override
	public void insertILiveGuests(ILiveGuests iLiveGuests)
			throws Exception {
		getSession().save(iLiveGuests);
	}

	@Override
	protected Class<ILiveGuests> getEntityClass() {
		return ILiveGuests.class;
	}

	@Override
	public ILiveGuests selectILiveGuestsByID(Integer roomId)  throws Exception {
		ILiveGuests iLiveGuests = get(roomId);
		return iLiveGuests;
	}

	@Override
	public void updateILiveGuests(ILiveGuests iLiveGuests) throws Exception {
		getSession().update(iLiveGuests);
	}

}
