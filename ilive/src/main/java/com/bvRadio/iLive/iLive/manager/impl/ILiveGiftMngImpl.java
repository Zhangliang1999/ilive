package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;
@Service
public class ILiveGiftMngImpl implements ILiveGiftMng{
	@Autowired
	private ILiveGiftDao iLiveGiftDao;
	@Autowired
	private ILiveFieldIdManagerMng fieldIdManagerMng;
	@Override
	@Transactional(readOnly = true)
	public List<ILiveGift> selectIliveGiftByUserId(Long userId,Integer roomId){
		List<ILiveGift> iLiveGifts = new ArrayList<ILiveGift>();
		try {
			iLiveGifts = iLiveGiftDao.selectIliveGiftByUserId(userId,roomId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveGifts;
	}

	@Override
	@Transactional
	public void addILiveGift(ILiveGift iLiveGift) throws Exception {
		Long giftId = fieldIdManagerMng.getNextId("ilive_gift", "gift_id", 1);
		iLiveGift.setGiftId(giftId);
		iLiveGiftDao.saveILiveGift(iLiveGift);
	}

	@Override
	@Transactional
	public void deleteILiveGiftById(Long giftId) throws Exception {
		iLiveGiftDao.deleteILiveGiftById(giftId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveGift> selectIliveGiftByGiftType(Integer roomId,
			Integer giftType) {
		List<ILiveGift> list = new ArrayList<ILiveGift>();
		try {
			list = iLiveGiftDao.selectIliveGiftByGiftType(roomId,giftType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public ILiveGift selectIliveGiftByGiftId(Long giftId) {
		ILiveGift iLiveGift = new ILiveGift();
		try {
			iLiveGift = iLiveGiftDao.selectIliveGiftByGiftId(giftId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveGift;
	}
	   
}
