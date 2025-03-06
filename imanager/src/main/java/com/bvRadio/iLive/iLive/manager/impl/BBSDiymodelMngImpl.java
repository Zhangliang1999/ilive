package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiymodelDao;
import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.manager.BBSDiymodelMng;

@Service
@Transactional
public class BBSDiymodelMngImpl implements BBSDiymodelMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<BBSDiymodel> getList() {
		List<BBSDiymodel> list = dao.getList();
		return list;
	}

	@Transactional(readOnly = true)
	public List<BBSDiymodel> getListByDiyformId(Integer diyformId) {
		List<BBSDiymodel> list = dao.getListByDiyformId(diyformId);
		return list;
	}

	@Transactional(readOnly = true)
	public BBSDiymodel findById(Integer id) {
		BBSDiymodel entity = dao.findById(id);
		return entity;
	}

	public BBSDiymodel save(BBSDiymodel bbsDiymodel) {
		long nextId = iLiveFieldIdManagerDao.getNextId("bbs_diymodel", "diymodel_id", 1);
		if (nextId != -1) {
			bbsDiymodel.setDiymodelId((int)nextId);
			dao.save(bbsDiymodel);
		}
		return bbsDiymodel;
	}

	public BBSDiymodel update(BBSDiymodel bean) {
		Updater<BBSDiymodel> updater = new Updater<BBSDiymodel>(bean);
		BBSDiymodel entity = dao.updateByUpdater(updater);
		return entity;
	}

	public void updateUser(BBSDiymodel user) {
		Updater<BBSDiymodel> updater = new Updater<BBSDiymodel>(user);
		dao.updateByUpdater(updater);
	}

	public BBSDiymodel deleteById(Integer id) {
		BBSDiymodel bean = dao.deleteById(id);
		return bean;
	}

	public List<BBSDiymodel> deleteByDiyformId(Integer diyformId) {
		return dao.deleteByDiyformId(diyformId);
	}

	public BBSDiymodel[] deleteByIds(Integer[] ids) {
		BBSDiymodel[] beans = new BBSDiymodel[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BBSDiymodelDao dao;
	private ILiveFieldIdManagerDao iLiveFieldIdManagerDao;

	@Autowired
	public void setDao(BBSDiymodelDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsFieldIdManagerDao(ILiveFieldIdManagerDao iLiveFieldIdManagerDao) {
		this.iLiveFieldIdManagerDao = iLiveFieldIdManagerDao;
	}
}
