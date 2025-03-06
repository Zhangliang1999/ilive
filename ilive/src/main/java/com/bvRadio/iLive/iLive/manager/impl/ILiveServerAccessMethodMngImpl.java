package com.bvRadio.iLive.iLive.manager.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.cache.Cache;
import com.bvRadio.iLive.common.cache.CacheManager;
import com.bvRadio.iLive.iLive.dao.ILiveServerAccessMethodDao;
import com.bvRadio.iLive.iLive.dao.ILiveServerGroupDao;
import com.bvRadio.iLive.iLive.entity.ILiveServer;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.entity.ILiveServerGroup;
import com.bvRadio.iLive.iLive.entity.MountInfo;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerMountMng;

@Service
public class ILiveServerAccessMethodMngImpl implements ILiveServerAccessMethodMng {

	private static final Integer true_flag = 1;

	@Autowired
	private ILiveServerGroupDao groupDao;

	@Autowired
	private ILiveServerAccessMethodDao methodDao;

	@Autowired
	private ILiveServerMountMng serverMountMng;

	@Transactional(readOnly = true)
	@Override
	public ILiveServerAccessMethod getAccessMethodBySeverGroupId(Integer serverGroupId) {
		Cache cacheInfo = CacheManager.getCacheInfo("accessMethod_" + serverGroupId);
		if (cacheInfo == null) {
			ILiveServerGroup iLiveServerGroup = groupDao.getILiveServerGroupById(serverGroupId);
			Set<ILiveServer> serverList = iLiveServerGroup.getServerList();
			Set<MountInfo> mountList = iLiveServerGroup.getMountList();
			if (!serverList.isEmpty()) {
				int serverId = 0;
				MountInfo mountinfo = null;
				for (ILiveServer server : serverList) {
					Integer mode = server.getMode();
					if (true_flag.equals(mode)) {
						serverId = server.getServer_id();
						break;
					}
				}
				if (mountList != null) {
					for (MountInfo info : mountList) {
						Integer isDefault = info.getIsDefault();
						if (true_flag.equals(isDefault)) {
							mountinfo = info;
							break;
						}
					}
				}
				ILiveServerAccessMethod accessMethod = methodDao.getMethodByServerId(serverId);
				accessMethod.setMountInfo(mountinfo);
				Cache cache = new Cache();
				cache.setValue(accessMethod);
				CacheManager.putCache("accessMethod_" + serverGroupId, cache);
				return accessMethod;
			}
			return null;
		} else {
			ILiveServerAccessMethod accessMethod = (ILiveServerAccessMethod) cacheInfo.getValue();
			return accessMethod;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ILiveServerAccessMethod getAccessMethodByMountId(Integer serverMountId) {
		MountInfo mountInfoById = serverMountMng.getMountInfoById(serverMountId);
		Integer server_group_id = mountInfoById.getServer_group().getServer_group_id();
		return this.getAccessMethodBySeverGroupId(server_group_id);
	}

}
