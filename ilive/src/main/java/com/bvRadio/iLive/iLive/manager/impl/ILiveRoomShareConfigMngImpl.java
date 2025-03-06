package com.bvRadio.iLive.iLive.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveRoomShareConfigDao;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomShareConfigMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;

public class ILiveRoomShareConfigMngImpl implements ILiveRoomShareConfigMng {

	@Autowired
	private ILiveRoomShareConfigDao shareConfigDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	@Transactional(readOnly=true)
	public List<ILiveRoomShareConfig> getShareConfig(Integer roomId) {
		return shareConfigDao.getShareConfig(roomId);
	}

	@Override
	@Transactional
	public List<ILiveRoomShareConfig> addIliveRoomShareConfig(ILiveRoomShareConfig shareConfig) {
		Long pkId = iLiveFieldIdManagerMng.getNextId("ilive_roomshare_config", "share_id", 2);
		ILiveRoomShareConfig shareOtherConfig = new ILiveRoomShareConfig();
		List<ILiveRoomShareConfig> list = new ArrayList<>();
		try {
			BeanUtilsExt.copyProperties(shareOtherConfig, shareConfig);
			shareConfig.setShareId(pkId);
			shareConfig.setShareType(ILiveRoomShareConfig.FRIEND_SINGLE);
			shareConfigDao.addIliveRoomShareConfig(shareConfig);

			shareOtherConfig.setShareId(pkId - 1);
			shareOtherConfig.setShareType(ILiveRoomShareConfig.FRIEND_CIRCLE);
			shareConfigDao.addIliveRoomShareConfig(shareOtherConfig);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		list.add(shareOtherConfig);
		list.add(shareConfig);
		return list;
	}

	@Override
	@Transactional
	public void updateConfigShare(ILiveShareInfoVo iLiveShareInfoVo) {
		// 朋友
		ILiveRoomShareConfig configShare = shareConfigDao.getConfigShare(iLiveShareInfoVo.getSingleId());
		if (configShare != null) {
			configShare.setLiveTitle(iLiveShareInfoVo.getLiveSingleTitle());
			configShare.setLiveImg(iLiveShareInfoVo.getSingleImg());
			configShare.setLiveDesc(iLiveShareInfoVo.getLiveSingleDesc());
			shareConfigDao.updateConfigDao(configShare);
		}

		// 朋友圈
		ILiveRoomShareConfig circleShare = shareConfigDao.getConfigShare(iLiveShareInfoVo.getCircleId());
		if (circleShare != null) {
			circleShare.setLiveTitle(iLiveShareInfoVo.getLiveCircleTitle());
			circleShare.setLiveImg(iLiveShareInfoVo.getCircleImg());
			circleShare.setLiveDesc(iLiveShareInfoVo.getLiveCircleDesc());
			shareConfigDao.updateConfigDao(circleShare);
		}
	}

	@Override
	@Transactional
	public void updateShare(ILiveRoomShareConfig share) {
		shareConfigDao.updateConfigDao(share);
	}

}
