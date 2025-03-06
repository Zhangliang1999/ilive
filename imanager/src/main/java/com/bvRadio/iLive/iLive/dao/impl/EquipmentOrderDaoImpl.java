package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.EquipmentOrderDao;
import com.bvRadio.iLive.iLive.entity.EquipmentOrder;

@Repository
public class EquipmentOrderDaoImpl extends HibernateBaseDao<EquipmentOrder, Long> implements EquipmentOrderDao{

	@Override
	public void save(EquipmentOrder equipmentOrder) {
		this.getSession().save(equipmentOrder);
	}

	@Override
	public void delete(Long id) {
		EquipmentOrder equipmentOrder = this.get(id);
		equipmentOrder.setIsDel(1);
		this.getSession().update(equipmentOrder);
	}

	@Override
	public EquipmentOrder getById(Long id) {
		return this.get(id);
	}

	@Override
	public void update(EquipmentOrder equipmentOrder) {
		this.getSession().update(equipmentOrder);
	}

	@Override
	public Pagination getPage(Long userId, Integer status, Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from EquipmentOrder where userId = :userId and isDel = 0");
		finder.setParam("userId", userId);
		if (status!=null&&status!=0) {
			finder.append(" and status = :status");
			finder.setParam("status", status);
		}
		if(type!=null&&type!=0) {
			finder.append(" and type = :type");
			finder.setParam("type", type);
		}
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<EquipmentOrder> getEntityClass() {
		return EquipmentOrder.class;
	}

	@Override
	public Pagination getAllPageByType(Integer type, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from EquipmentOrder where type = :type and isDel = 0 order by updateTime");
		finder.setParam("type",type);
		return find(finder, pageNo, pageSize);
	}

}
