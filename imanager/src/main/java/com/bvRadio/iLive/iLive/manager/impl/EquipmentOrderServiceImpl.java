package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.EquipmentOrderDao;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;
import com.bvRadio.iLive.iLive.manager.EquipmentOrderService;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class EquipmentOrderServiceImpl implements EquipmentOrderService{

	@Autowired
	private EquipmentOrderDao equipmentOrderDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Long save(EquipmentOrder equipmentOrder) {
		Long nextId = filedIdMng.getNextId("equipment_order", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		equipmentOrder.setId(nextId);
		equipmentOrder.setCreateTime(now);
		equipmentOrder.setUpdateTime(now);
		equipmentOrder.setIsDel(0);
		equipmentOrderDao.save(equipmentOrder);
		return nextId;
	}

	@Override
	public void delete(Long id) {
		equipmentOrderDao.delete(id);
	}

	@Override
	public EquipmentOrder getById(Long id) {
		return equipmentOrderDao.getById(id);
	}

	@Override
	public void update(EquipmentOrder equipmentOrder) {
		Timestamp now = new Timestamp(new Date().getTime());
		equipmentOrder.setUpdateTime(now);
		equipmentOrderDao.update(equipmentOrder);
	}

	@Override
	public Pagination getPage(Long userId, Integer status, Integer type, Integer pageNo, Integer pageSize) {
		return equipmentOrderDao.getPage(userId, status, type, pageNo, pageSize);
	}

	@Override
	public Pagination getAllPageByType(Integer type, Integer pageNo, Integer pageSize) {
		return equipmentOrderDao.getAllPageByType(type,pageNo,pageSize);
	}

}
