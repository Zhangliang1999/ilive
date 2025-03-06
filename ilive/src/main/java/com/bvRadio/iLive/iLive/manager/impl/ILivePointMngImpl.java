package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.dao.ILivePointDao;
import com.bvRadio.iLive.iLive.entity.ILivePoint;
import com.bvRadio.iLive.iLive.manager.ILivePointMng;

@Service
@Transactional
public  class ILivePointMngImpl implements ILivePointMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<ILivePoint> getList() {
		List<ILivePoint> list = dao.getList();
		return list;
	}

	@Transactional(readOnly = true)
	public ILivePoint findById(Integer id) {
		ILivePoint entity = dao.findById(id);
		return entity;
	}

	

	public ILivePoint save(ILivePoint bean) {
		long nextId = iLiveFieldIdManagerDao.getNextId("iLive_point", "point_id", 1);
		if (nextId != -1) {
			bean.setPointId((int)nextId);
			dao.save(bean);
		}
		return bean;
	}

	public ILivePoint update(ILivePoint bean) {
		if (null != bean && null != bean.getPointId()) {
			
				dao.update(bean);
			
		}
		return bean;
	}

	public ILivePoint deleteById(Integer id) {
		ILivePoint bean = dao.deleteById(id);
		return bean;
	}

	public ILivePoint[] deleteByIds(Integer[] ids) {
		ILivePoint[] beans = new ILivePoint[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ILivePointDao dao;
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Autowired
	public void setDao(ILivePointDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}

	@Override
	public ILivePoint findDefaultPoint() {
		ILivePoint entity=dao.findDefaultPoint();
		return entity;
	}
}
