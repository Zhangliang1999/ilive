package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveViewAuthBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewAuthBill;

public class ILiveViewAuthBillDaoImpl extends HibernateBaseDao<ILiveViewAuthBill, Long>
		implements ILiveViewAuthBillDao {

	@Override
	protected Class<ILiveViewAuthBill> getEntityClass() {
		return ILiveViewAuthBill.class;
	}

	@Override
	public void addILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill) {
		this.getSession().save(iLiveViewAuthBill);
	}

	@Override
	public void updateILiveViewAuthBill(ILiveViewAuthBill iLiveViewAuthBill) {
		this.getSession().update(iLiveViewAuthBill);
	}

	@Override
	public ILiveViewAuthBill checkILiveViewAuthBill(String userId, Long liveEventId) {
		return (ILiveViewAuthBill) this.findUnique(
				"from ILiveViewAuthBill where userId=? and liveEventId=? and deleteStatus=0", userId, liveEventId);
	}

}
