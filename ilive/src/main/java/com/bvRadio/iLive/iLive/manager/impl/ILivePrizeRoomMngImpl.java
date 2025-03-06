package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILivePrizeRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILivePrizeRoom;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeRoomMng;

@Transactional
public class ILivePrizeRoomMngImpl implements ILivePrizeRoomMng {

	@Autowired
	private ILivePrizeRoomDao iLivePrizeRoomDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private ILivePrizeMng iLivePrizeMng;	//抽奖内容
	
	@Override
	public void save(ILivePrizeRoom iLivePrizeRoom) {
		Long nextId = fieldIdMng.getNextId("ilive_prize_room", "id", 1);
		iLivePrizeRoom.setId(nextId);
		iLivePrizeRoomDao.save(iLivePrizeRoom);
	}

	@Override
	public void update(ILivePrizeRoom iLivePrizeRoom) {
		iLivePrizeRoomDao.update(iLivePrizeRoom);
	}

	@Override
	public ILivePrizeRoom selectStartPrize(Integer roomId) {
		return iLivePrizeRoomDao.selectStartPrize(roomId);
	}

	@Override
	public ILivePrizeRoom selectPrize(Integer roomId, Long prizeId) {
		return iLivePrizeRoomDao.selectPrize(roomId, prizeId);
	}

	@Override
	public boolean isStartPrize(Integer roomId) {
		ILivePrizeRoom prizeRoom = selectStartPrize(roomId);
		return prizeRoom!=null;
	}

	@Override
	public void clearEnd(Integer roomId, Integer enterpriseId) {
		List<ILiveLottery> list = iLivePrizeMng.getListUserH5ByEnterpriseId(enterpriseId);
		List<Long> idList = new ArrayList<>();
		for(ILiveLottery prize:list) {
			idList.add(prize.getId());
		}
		iLivePrizeRoomDao.clearEnd(roomId,idList);
	}

}
