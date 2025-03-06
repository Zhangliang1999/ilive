package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;

public interface ILiveIpAddressDao {

	public ILiveIpAddress findByIpCode(Long ipCode);

	public ILiveIpAddress save(ILiveIpAddress bean);

	public void update(ILiveIpAddress bean);
}