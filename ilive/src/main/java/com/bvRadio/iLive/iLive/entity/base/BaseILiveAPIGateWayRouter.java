package com.bvRadio.iLive.iLive.entity.base;

/**
 * API接口路由
 * @author administrator
 */
public class BaseILiveAPIGateWayRouter {
	
	/**
	 * API地址ID
	 */
	private Long routerId;
	
	
	/**
	 * API地址url
	 */
	private String routerUrl;


	public String getRouterUrl() {
		return routerUrl;
	}


	public void setRouterUrl(String routerUrl) {
		this.routerUrl = routerUrl;
	}


	public Long getRouterId() {
		return routerId;
	}


	public void setRouterId(Long routerId) {
		this.routerId = routerId;
	}
	
	
	
	
}
