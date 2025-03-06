package com.bvRadio.iLive.iLive.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveIpAddressDao;
import com.bvRadio.iLive.iLive.entity.ILiveIpAddress;
import com.bvRadio.iLive.iLive.manager.ILiveIpAddressMng;
import com.bvRadio.iLive.iLive.task.IpAddressTask;
import com.bvRadio.iLive.iLive.util.IPUtils;

@Service
@Transactional
public class ILiveIpAddressMngImpl implements ILiveIpAddressMng {

	@Transactional(readOnly = true)
	public ILiveIpAddress findByIpCode(Long ipCode) {
		ILiveIpAddress entity = dao.findByIpCode(ipCode);
		return entity;
	}

	@Transactional(readOnly = true)
	public ILiveIpAddress getIpAddressByIp(String ip) {
		long ipCode = IPUtils.ipToLong(ip);
		ILiveIpAddress ipAddress = findByIpCode(ipCode);
		if (null == ipAddress) {
			ipAddress = new ILiveIpAddress(ip, ipCode);
			save(ipAddress);
			// 启动线程解析ip，获取地区
			IpAddressTask ipAddressTask = new IpAddressTask(ip);
			new Thread(ipAddressTask).start();
		} else if (StringUtils.isBlank(ipAddress.getCountry())) {
			// 判断到数据库中的IP对应地址为空 启动线程解析ip，重新获取地区
			IpAddressTask ipAddressTask = new IpAddressTask(ip);
			new Thread(ipAddressTask).start();
		}
		return ipAddress;
	}

	public ILiveIpAddress save(ILiveIpAddress bean) {
		return dao.save(bean);
	}

	public void update(ILiveIpAddress bean) {
		if (null != bean && null != bean.getIpCode()) {
			dao.update(bean);
		}
	}

	@Autowired
	private ILiveIpAddressDao dao;

}
