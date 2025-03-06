package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveViewWhiteBillDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;

public class ILiveViewWhiteBillDaoImpl extends HibernateBaseDao<ILiveViewWhiteBill, Long>
		implements ILiveViewWhiteBillDao {

	@Override
	public boolean checkUserInWhiteList(String userId, Long liveEventId) {
		String hql = "from ILiveViewWhiteBill where userId=?,liveEventId=?";
		List find = this.find(hql, Long.parseLong(userId),liveEventId);
		if(find!=null && !find.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	protected Class<ILiveViewWhiteBill> getEntityClass() {
		// TODO Auto-generated method stub
		return ILiveViewWhiteBill.class;
	}

	@Override
	public List<ILiveViewWhiteBill> getAllViewWhiteBill(Long liveEventId) {
		
		
		
		return null;
	}

}
