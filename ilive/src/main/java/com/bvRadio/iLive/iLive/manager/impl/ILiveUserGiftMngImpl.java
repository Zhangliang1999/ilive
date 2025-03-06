package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveUserGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveUserGift;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveUserGiftMng;
@Service
public class ILiveUserGiftMngImpl implements ILiveUserGiftMng {
	
	@Autowired
	private ILiveUserGiftDao iLiveUserGiftDao;
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	@Override
	@Transactional(readOnly = true)
	public Pagination selectILiveUserGiftPage(String userContent,
			String roomContent, Integer roomId, Integer pageNo,
			Integer pageSize) throws Exception {
		Pagination pagination = iLiveUserGiftDao.selectILiveUserGiftPage(userContent,roomContent,roomId,pageNo,pageSize);
		return pagination;
	}

	@Override
	@Transactional
	public void addILiveUserGift(ILiveUserGift iLiveUserGift) throws Exception {
		Long nextId = iLiveFieldIdManagerMng.getNextId("ilive_gift_user", "id", 1);
		iLiveUserGift.setId(nextId);
		iLiveUserGiftDao.insertILiveUserGift(iLiveUserGift);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveUserGift> selectILiveUserGiftExcel(String userContent,
			String roomContent, Integer roomId) throws Exception {
		List<ILiveUserGift> list = iLiveUserGiftDao.selectILiveUserGiftExcel(userContent,roomContent,roomId);
		return list;
	}

}
