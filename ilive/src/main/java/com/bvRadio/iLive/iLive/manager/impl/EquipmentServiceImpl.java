package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.EquipmentDao;
import com.bvRadio.iLive.iLive.entity.Equipment;
import com.bvRadio.iLive.iLive.manager.EquipmentService;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	private EquipmentDao equipmentDao;
	
	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;
	
	@Override
	public Long save(Equipment equipment) {
		Long nextId = filedIdMng.getNextId("equipment", "id", 1);
		equipment.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		equipment.setCreateTime(now);
		equipment.setUpdateTime(now);
		equipmentDao.save(equipment);
		return nextId;
	}

	@Override
	public Equipment getById(Long id) {
		return equipmentDao.getById(id);
	}

	@Override
	public void update(Equipment equipment) {
		equipmentDao.update(equipment);
	}

	@Override
	public void delete(Long id) {
		equipmentDao.delete(id);
	}

	@Override
	public Pagination getPage(Long userId,String name, Integer pageNo, Integer pageSize,Integer rentOrSell) {
		return equipmentDao.getPage(userId,name, pageNo, pageSize,rentOrSell);
	}

}
