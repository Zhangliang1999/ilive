package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;

public interface EquipmentOrderService {
	
	Long save(EquipmentOrder equipmentOrder);
	
	void delete(Long id);
	
	EquipmentOrder getById(Long id);
	
	void update(EquipmentOrder equipmentOrder);
	
	Pagination getPage(Long userId,Integer status,Integer type,Integer pageNo,Integer pageSize);
}
