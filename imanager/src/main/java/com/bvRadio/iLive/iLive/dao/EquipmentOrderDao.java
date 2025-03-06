package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;

public interface EquipmentOrderDao {

	void save(EquipmentOrder equipmentOrder);
	
	void delete(Long id);
	
	EquipmentOrder getById(Long id);
	
	void update(EquipmentOrder equipmentOrder);
	
	Pagination getPage(Long userId,Integer status,Integer type,Integer pageNo,Integer pageSize);
	
	Pagination getAllPageByType(Integer type, Integer pageNo, Integer pageSize);
}
