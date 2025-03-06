package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomSuggestDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomSuggest;

public class ILiveRoomSuggestDaoImpl extends HibernateBaseDao<ILiveRoomSuggest, Long> implements ILiveRoomSuggestDao {

	@Override
	public void addRoomSuggest(ILiveRoomSuggest roomSuggest) {
		this.getSession().save(roomSuggest);
	}

	@Override
	protected Class<ILiveRoomSuggest> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveRoomSuggest.class;
	}

}
