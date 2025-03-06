package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;

public interface ILiveIpAddressMng {
	public ILiveIpAddress findByIpCode(Long ipCode);

	public ILiveIpAddress getIpAddressByIp(String ip);

	public ILiveIpAddress save(ILiveIpAddress bean);

	public void update(ILiveIpAddress bean);

}