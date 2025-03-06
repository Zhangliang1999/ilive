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

	@Override
	public boolean deleteLiveViewAuthBillByLiveEventId(Long iLiveEventId) {
		String hql = " update ILiveViewAuthBill set deleteStatus=1 where liveEventId=:liveEventId";
		this.getSession().createQuery(hql).setParameter("liveEventId", iLiveEventId).executeUpdate();
		return true;
	}

	@Override
	public boolean deleteLiveViewAuthBillByLiveRoomId(Integer roomId,String userId) {
		String hql = " update ILiveViewAuthBill set deleteStatus=1 where liveRoomId=:liveRoomId and authType=6 and userId="+userId;
		this.getSession().createQuery(hql).setParameter("liveRoomId", roomId).executeUpdate();
		return true;
	}

	@Override
	public ILiveViewAuthBill checkILiveViewAuthBill(String userId, Integer roomId) {
		return (ILiveViewAuthBill) this.findUnique(
				"from ILiveViewAuthBill where userId=? and liveRoomId=? and deleteStatus=0 and authType=6", userId, roomId);
	}

	@Override
	public ILiveViewAuthBill checkILiveViewAuthBillByFileId(String userId, Long fileId) {
		return (ILiveViewAuthBill) this.findUnique(
				"from ILiveViewAuthBill where userId=? and fileId=? and deleteStatus=0 and authType=6", userId, fileId);
	}

	@Override
	public boolean deleteLiveViewAuthBillByfileId(String userId, Long fileId) {
		String hql = " update ILiveViewAuthBill set deleteStatus=1 where fileId=:fileId and authType=6 and userId="+userId;
		this.getSession().createQuery(hql).setParameter("fileId", fileId).executeUpdate();
		return true;
	}

}
