package com.bvRadio.iLive.iLive.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveMediaFileShareConfigDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomShareConfigDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.vo.ILiveShareInfoVo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileShareConfigMng;
import com.bvRadio.iLive.iLive.manager.ILiveRoomShareConfigMng;
import com.bvRadio.iLive.iLive.util.BeanUtilsExt;

public class ILiveMediaFileShareConfigMngImpl implements ILiveMediaFileShareConfigMng {

	@Autowired
	private ILiveMediaFileShareConfigDao shareConfigDao;

	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;

	@Override
	@Transactional(readOnly=true)
	public List<ILiveMediaFileShareConfig> getShareConfig(Integer fileId) {
		return shareConfigDao.getShareConfig(fileId);
	}

	@Override
	@Transactional
	public List<ILiveMediaFileShareConfig> addIliveMediaFileShareConfig(ILiveMediaFileShareConfig shareConfig) {
		Long pkId =shareConfigDao.selectMaxId();
		if(pkId==null){
			pkId=1L;
		}
		ILiveMediaFileShareConfig shareOtherConfig = new ILiveMediaFileShareConfig();
		List<ILiveMediaFileShareConfig> list = new ArrayList<>();
		try {
			BeanUtilsExt.copyProperties(shareOtherConfig, shareConfig);
			shareConfig.setShareId(pkId);
			shareConfig.setShareType(ILiveMediaFileShareConfig.FRIEND_SINGLE);
			shareConfigDao.addIliveMediaFileShareConfig(shareConfig);

			shareOtherConfig.setShareId(pkId + 1);
			shareOtherConfig.setShareType(ILiveMediaFileShareConfig.FRIEND_CIRCLE);
			shareConfigDao.addIliveMediaFileShareConfig(shareOtherConfig);
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
		ILiveMediaFileShareConfig configShare = shareConfigDao.getConfigShare(iLiveShareInfoVo.getSingleId());
		if (configShare != null) {
			configShare.setMediaFileName(iLiveShareInfoVo.getLiveSingleTitle());
			configShare.setShareImg(iLiveShareInfoVo.getSingleImg());
			configShare.setMediaFileDesc(iLiveShareInfoVo.getLiveSingleDesc());
			configShare.setIfEdit(1);
			shareConfigDao.updateConfigDao(configShare);
		}

		// 朋友圈
		ILiveMediaFileShareConfig circleShare = shareConfigDao.getConfigShare(iLiveShareInfoVo.getCircleId());
		if (circleShare != null) {
			circleShare.setMediaFileName(iLiveShareInfoVo.getLiveCircleTitle());
			circleShare.setShareImg(iLiveShareInfoVo.getCircleImg());
			circleShare.setMediaFileDesc(iLiveShareInfoVo.getLiveCircleDesc());
			configShare.setIfEdit(1);
			shareConfigDao.updateConfigDao(circleShare);
		}
	}

	@Override
	@Transactional
	public void updateShare(ILiveMediaFileShareConfig share) {
		shareConfigDao.updateConfigDao(share);
	}
	@Override
	public Long selectMaxId() {
		// TODO Auto-generated method stub
		return shareConfigDao.selectMaxId();
	}
}
