package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;

public interface ILiveAPIGateWayRouterMng {
	
	
	/**
	 * @return
	 */
	public List<ILiveAPIGateWayRouter> getRouterList();

	/**
	 * @param routerId
	 * @return
	 */
	public ILiveAPIGateWayRouter getRouterById(Long routerId);
	
	ILiveAPIGateWayRouter get();
}
