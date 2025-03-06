package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;

public interface ILiveServerAccessMethodMng {
	
	
	/**
	 * 根据组Id获取服务器地址信息
	 * @param serverGroupId
	 * @return
	 */
	public ILiveServerAccessMethod getAccessMethodBySeverGroupId(Integer serverGroupId);

	public ILiveServerAccessMethod getAccessMethodByMountId(Integer serverMountId);

}
