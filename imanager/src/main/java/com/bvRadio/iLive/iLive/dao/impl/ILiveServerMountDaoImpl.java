package com.bvRadio.iLive.iLive.dao.impl;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerMountDao;
import com.bvRadio.iLive.iLive.entity.MountInfo;

/**
 * 发布点对应的管理类
 * 
 * @author administrator
 *
 */
public class ILiveServerMountDaoImpl extends HibernateBaseDao<MountInfo, Integer> implements ILiveServerMountDao {

	@Override
	protected Class<MountInfo> getEntityClass() {
		return MountInfo.class;
	}

	@Override
	public MountInfo getMountInfoById(Integer mountId) {
		return this.get(mountId);
	}

}
