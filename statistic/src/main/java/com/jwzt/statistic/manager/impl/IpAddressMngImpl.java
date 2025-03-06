package com.jwzt.statistic.manager.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.common.utils.IpUtils;
import com.jwzt.statistic.dao.IpAddressDao;
import com.jwzt.statistic.entity.IpAddress;
import com.jwzt.statistic.manager.IpAddressMng;
import com.jwzt.statistic.pool.IpAddressPool;

/**
 * 
 * @author ysf
 *
 */
@Service
public class IpAddressMngImpl implements IpAddressMng {

	@Override
	@Transactional(readOnly = true)
	public List<IpAddress> listNull() {
		return dao.listNull();
	}

	@Override
	@Transactional(readOnly = true)
	public IpAddress getBeanByIpCode(Long ipCode) {
		IpAddress entity = dao.getBeanByIpCode(ipCode);
		return entity;
	}

	@Override
	@Transactional(readOnly = true)
	public IpAddress getBeanByIp(String ip) {
		long ipCode = IpUtils.ipToLong(ip);
		IpAddress ipAddress = getBeanByIpCode(ipCode);
		if (null == ipAddress) {
			// 启动线程解析ip，获取地区
			ipAddress = new IpAddress(ip, ipCode);
			IpAddressPool.execute(ip);
		} else if (StringUtils.isBlank(ipAddress.getCountry())) {
			// 判断到数据库中的IP对应地址为空 启动线程解析ip，重新获取地区
			IpAddressPool.execute(ip);
		}
		return ipAddress;
	}

	@Override
	@Transactional
	public IpAddress save(IpAddress bean) {
		return dao.save(bean);
	}

	@Override
	@Transactional
	public void update(IpAddress bean) {
		if (null != bean && null != bean.getIpCode()) {
			dao.update(bean);
		}
	}

	@Autowired
	private IpAddressDao dao;

	@Override
	public List<IpAddress> isNotSuccess() {
		return dao.isNotSuccess();
	}

}
