package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomStatics;

public class ILiveRoomStaticsDaoImpl extends HibernateBaseDao<ILiveRoomStatics, Long> implements ILiveRoomStaticsDao {

	@Override
	protected Class<ILiveRoomStatics> getEntityClass() {
		return ILiveRoomStatics.class;
	}

	@Override
	public ILiveRoomStatics getRoomStaticsByEventId(Long eventId) {
		return this.get(eventId);
	}

	@Override
	public void saveRoomStaticDao(Long liveEventId, Long num) {
		ILiveRoomStatics statics = new ILiveRoomStatics();
		statics.setLiveEventId(liveEventId);
		statics.setShowNum(num);
		this.getSession().save(statics);
	}

	@Override
	public void updateRoomStatic(Long liveEventId, Long showNum) {
		ILiveRoomStatics iLiveRoomStatics = this.get(liveEventId);
		if (iLiveRoomStatics != null) {
			iLiveRoomStatics.setShowNum(showNum);
			this.getSession().update(iLiveRoomStatics);
		}

	}

}
