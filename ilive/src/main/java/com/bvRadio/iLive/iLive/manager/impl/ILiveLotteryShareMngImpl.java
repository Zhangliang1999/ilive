package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.poi.ss.formula.functions.Roman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveLotteryShareDao;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveLotteryShare;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveLotteryShareMng;
import com.bvRadio.iLive.iLive.manager.ILivePrizeMng;

@Service
@Transactional
public class ILiveLotteryShareMngImpl implements ILiveLotteryShareMng {

	@Autowired
	private ILiveLotteryShareDao iLiveLotteryShareDao;	//保存记录
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Autowired
	private ILivePrizeMng iLivePrizeMng;	//获取主键
	
	@Autowired
	private ILiveLiveRoomMng iLiveRoomMng;
	
	@Override
	public void save(ILiveLotteryShare iLiveLotteryShare) {
		Long nextId = fieldIdMng.getNextId("ilive_lottery_share", "id", 1);
		iLiveLotteryShare.setId(nextId);
		iLiveLotteryShare.setCreateTime(new Timestamp(new Date().getTime()));
		iLiveLotteryShareDao.save(iLiveLotteryShare);
	}

	@Override
	public ILiveLotteryShare getRecordByUserIdAndLotteryId(Long userId, Long lotteryId) {
		return iLiveLotteryShareDao.getRecordByUserId(userId, lotteryId);
	}

	@Override
	public int share(Integer roomId, Long userId) {
		ILiveLiveRoom room = iLiveRoomMng.findById(roomId);
		ILiveLottery startPrize = null;
		if (room!=null&&room.getEnterpriseId()!=null) {
			startPrize = iLivePrizeMng.isStartPrize(roomId);
		}
		if (startPrize == null) {
			//当前没有开启的活动
			return 0;
		}else {
			//保存一条记录
			ILiveLotteryShare share = new ILiveLotteryShare();
			share.setLotteryId(startPrize.getId());
			share.setUserId(userId);
			this.save(share);
			return 1;
		}
	}

}
