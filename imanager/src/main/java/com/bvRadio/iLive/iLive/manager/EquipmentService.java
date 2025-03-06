package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.Equipment;

public interface EquipmentService {
	
	Long save(Equipment equipment);
	
	Equipment getById(Long id);

	void update(Equipment equipment);
	
	void delete(Long id);
	
	Pagination getPage(Long userId,String name,Integer pageNo,Integer pageSize);
	
	Pagination getAllPageByRentOrSell(Integer rentOrSell,Integer isShangjia,String name,Integer pageNo,Integer pageSize);
}
