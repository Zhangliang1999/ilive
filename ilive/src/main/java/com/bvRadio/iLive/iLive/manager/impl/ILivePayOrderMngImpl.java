package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILivePayOrderDao;
import com.bvRadio.iLive.iLive.entity.ILivePayOrder;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePayOrderMng;
@Service
public class ILivePayOrderMngImpl implements ILivePayOrderMng {
	@Autowired
	private ILivePayOrderDao iLivePayOrderDao;
	@Autowired
	private ILiveFieldIdManagerMng fieldIdManagerMng;
	
	@Override
	@Transactional
	public Long addILivePayOrder(ILivePayOrder iLivePayOrder) throws Exception {
		Long nextId = fieldIdManagerMng.getNextId("", "", 1);
		iLivePayOrder.setId(nextId);
		iLivePayOrderDao.insertILivePayOrder(iLivePayOrder);
		return nextId;
	}

	@Override
	@Transactional(readOnly=true)
	public ILivePayOrder selectILivePayOrder(String merOrderNo, String orderNo)
			throws Exception {
		List<ILivePayOrder> list = iLivePayOrderDao.selectILivePayOrder(merOrderNo,orderNo);
		if(list!=null){
			if(list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}

	@Override
	@Transactional
	public boolean updateILivePayOrder(ILivePayOrder iLivePayOrder) {
		try {
			iLivePayOrderDao.updateILivePayOrder(iLivePayOrder);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public ILivePayOrder selectILivePayOrderById(Long orderId) throws Exception {
		return iLivePayOrderDao.selectILivePayOrderById(orderId);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean selectILivePayOrderByEvenIdAndUserId(Long userId,
			Long evenId, Integer orderType) throws Exception {
		List<ILivePayOrder> orders = iLivePayOrderDao.selectILivePayOrderByEvenIdAndUserId(userId, evenId, orderType);
		boolean ret = false;
		for (ILivePayOrder iLivePayOrder : orders) {
			if(iLivePayOrder.getPayStatus()==ILivePayOrder.WX_PAY_STATUS_YES){
				return true;
			}
		}
		return ret;
	}

}
