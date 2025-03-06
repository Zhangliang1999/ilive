package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseFansDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class ILiveEnterpriseFansMngImpl implements ILiveEnterpriseFansMng {

	@Autowired
	private ILiveEnterpriseFansDao fansDao;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Transactional
	@Override
	public Pagination getPage(String queryNum, Integer pageNo, Integer pageSize) {
		return fansDao.getPage(queryNum, pageNo, pageSize);
	}

	@Transactional
	@Override
	public Pagination getPageBlack(String queryNum, Integer pageNo, Integer pageSize) {
		return fansDao.getPageBlack(queryNum, pageNo, pageSize);
	}

	@Transactional
	@Override
	public boolean addEnterpriseConcern(Integer enterpriseId, Long userId) {
		ILiveEnterpriseFans fans = this.findEnterpriseFans(enterpriseId, userId);
		if (fans == null) {
			fans = new ILiveEnterpriseFans();
			Long nextId = filedIdMng.getNextId("enterprise_fans", "id", 1);
			fans.setId(nextId);
			fans.setEnterpriseId(enterpriseId);
			fans.setUserId(userId);
			fansDao.addEnterpriseConcern(fans);
			return true;
		} else {
			return false;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ILiveEnterpriseFans findEnterpriseFans(Integer enterpriseId, Long userId) {
		return fansDao.findEnterpriseFans(enterpriseId, userId);
	}

	@Transactional
	@Override
	public boolean isExist(Integer enterpriseId, Long userId) {
		return fansDao.isExist(enterpriseId, userId);
	}

	@Transactional
	@Override
	public void delFans(Long id) {
		fansDao.delFans(id);
	}

	@Transactional
	@Override
	public void updateBatchEnterpriseState(Integer enterpriseId) {
		List<ILiveEnterpriseFans> enterpriseList = fansDao.getEnterpriseFansById(enterpriseId);
		if (enterpriseList != null && !enterpriseList.isEmpty()) {
			fansDao.batchUpdateFans(enterpriseList);
		}
	}

	@Transactional
	@Override
	public void letblack(Long id) {
		fansDao.letblack(id);
	}

	@Override
	public int getFansNum(Integer enterpriseId) {
		return fansDao.getFansNum(enterpriseId);
	}

	@Override
	@Transactional
	public void deleteEnterpriseConcern(Integer enterpriseId, Long userId) {
		ILiveEnterpriseFans fans = this.findEnterpriseFans(enterpriseId, userId);
		if (fans != null) {
			this.deleteEnterpriseConcern(fans);
		}
	}

	@Override
	@Transactional
	public void deleteEnterpriseConcern(ILiveEnterpriseFans fans) {
		fansDao.deleteEnterpriseConcern(fans);
	}

	@Override
	public Pagination getPageByUserId(Long userId, Integer pageNo, Integer pageSize) {
		return fansDao.getPageByUserId(userId,pageNo,pageSize);
	}

	@Override
	public List<ILiveEnterpriseFans> getListByUserId(Long userId) {
		return fansDao.getListByUserId(userId);
	}

}
