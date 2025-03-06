package com.jwzt.statistic.manager;

import java.util.List;

import com.jwzt.statistic.entity.IpAddress;

public interface IpAddressMng {
	public List<IpAddress> listNull();
	
	public IpAddress getBeanByIpCode(Long ipCode);

	public IpAddress getBeanByIp(String ip);

	public IpAddress save(IpAddress bean);

	public void update(IpAddress bean);
	
	public List<IpAddress> isNotSuccess();

}