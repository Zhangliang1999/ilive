package com.jwzt.statistic.dao;

import java.util.List;

import com.jwzt.statistic.entity.IpAddress;

public interface IpAddressDao {
	public List<IpAddress> listNull();

	public IpAddress getBeanByIpCode(Long ipCode);

	public IpAddress save(IpAddress bean);

	public void update(IpAddress bean);
	
	List<IpAddress> isNotSuccess();
}