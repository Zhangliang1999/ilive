package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveGift;

@Repository
public class ILiveGiftDaoImpl extends HibernateBaseDao<ILiveGift, Long> implements ILiveGiftDao  {

	@Override
	protected Class<ILiveGift> getEntityClass() {
		return ILiveGift.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveGift> selectIliveGiftByUserId(Long userId,Integer roomId)  throws Exception{
		Finder finder = Finder.create("select bean from ILiveGift bean");
		finder.append(" where bean.userId=:userId ");
		finder.setParam("userId", userId);
		finder.append(" and bean.roomId=:roomId ");
		finder.setParam("roomId", roomId);
		List<ILiveGift> find = find(finder);
		return find;
	}

	@Override
	public void saveILiveGift(ILiveGift iLiveGift) throws Exception {
		getSession().save(iLiveGift);
	}

	@Override
	public void deleteILiveGiftById(Long giftId) throws Exception {
		getSession().delete(get(giftId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILiveGift> selectIliveGiftByGiftType(Integer roomId,
			Integer giftType) throws Exception {
		Finder finder = Finder.create("select bean from ILiveGift bean");
		finder.append(" where bean.giftType=:giftType ");
		finder.setParam("giftType", giftType);
		finder.append(" and bean.roomId=:roomId ");
		finder.setParam("roomId", roomId);
		List<ILiveGift> find = find(finder);
		return find;
	}

	@Override
	public ILiveGift selectIliveGiftByGiftId(Long giftId) throws Exception {
		ILiveGift iLiveGift = get(giftId);
		return iLiveGift;
	}

}
