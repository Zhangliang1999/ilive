package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveMediaFileShareConfigDao;
import com.bvRadio.iLive.iLive.dao.ILiveRoomShareConfigDao;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveRoomShareConfig;
import com.bvRadio.iLive.iLive.entity.ILiveSendMsg;

public class ILiveMediaFileConfigDaoImpl extends HibernateBaseDao<ILiveMediaFileShareConfig, Long>
		implements ILiveMediaFileShareConfigDao {

	@Override
	protected Class<ILiveMediaFileShareConfig> getEntityClass() {
		return ILiveMediaFileShareConfig.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ILiveMediaFileShareConfig> getShareConfig(Integer fileId) {
		return this.find("from ILiveMediaFileShareConfig where fileId=?", fileId);
	}

	@Override
	public void addIliveMediaFileShareConfig(ILiveMediaFileShareConfig shareConfig) {
		shareConfig.setOpenStatus(0);
		this.getSession().save(shareConfig);
	}

	/**
	 * 
	 */
	@Override
	public void updateConfigDao(ILiveMediaFileShareConfig configShare) {
		this.getSession().update(configShare);
	}

	/**
	 * 
	 */
	@Override
	public ILiveMediaFileShareConfig getConfigShare(Long circleId) {
		return this.get(circleId);
	}

	@Override
	public Long selectMaxId() {
		String hql = "from ILiveMediaFileShareConfig  order by share_id desc";
		Finder finder = Finder.create(hql);
		List<ILiveMediaFileShareConfig> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			Long id=find.get(0).getShareId();
			if(id==null){
				id=0L;
			}
			return id+1L;
		}
		return null;
	}

}
