package com.bvRadio.iLive.iLive.dao.impl;

import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.EquipmentDao;
import com.bvRadio.iLive.iLive.entity.Equipment;

@Repository
public class EquipmentDaoImpl extends HibernateBaseDao<Equipment, Long> implements EquipmentDao{

	@Override
	public void save(Equipment equipment) {
		this.getSession().save(equipment);
	}

	@Override
	public Equipment getById(Long id) {
		return this.get(id);
	}

	@Override
	public void update(Equipment equipment) {
		this.getSession().update(equipment);
	}

	@Override
	public void delete(Long id) {
		Equipment equipment = this.get(id);
		this.getSession().delete(equipment);
	}

	@Override
	public Pagination getPage(Long userId,String name, Integer pageNo, Integer pageSize) {
		Finder finder = Finder.create("from Equipment where userId = :userId");
		finder.setParam("userId", userId);
		if(name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		finder.append(" order by updateTime DESC");
		return find(finder, pageNo, pageSize);
	}

	@Override
	protected Class<Equipment> getEntityClass() {
		return Equipment.class;
	}

	@Override
	public Pagination getAllPageByRentOrSell(Integer rentOrSell, Integer isShangjia, String name, Integer pageNo,
			Integer pageSize) {
		Finder finder = Finder.create("from Equipment where rentOrSell = :rentOrSell");
		finder.setParam("rentOrSell", rentOrSell);
		if(name!=null&&!name.equals("")) {
			finder.append(" and name like :name");
			finder.setParam("name", "%"+name+"%");
		}
		if(isShangjia!=null&&isShangjia!=-1) {
			finder.append(" and isShangjia = :isShangjia");
			finder.setParam("isShangjia", isShangjia);
		}
		finder.append(" order by updateTime DESC");
		return find(finder, pageNo, pageSize);
	}

}
