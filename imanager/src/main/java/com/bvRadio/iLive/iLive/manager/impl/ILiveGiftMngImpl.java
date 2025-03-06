package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveGiftDao;
import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.manager.ILiveGiftMng;

public class ILiveGiftMngImpl implements ILiveGiftMng{
	   @Autowired
	   private ILiveGiftDao iLiveGiftDao;

	@Override
	public void save(ILiveGift iLiveGift) {
		// TODO Auto-generated method stub
		iLiveGiftDao.save(iLiveGift);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		iLiveGiftDao.delete(id);
	}

	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return iLiveGiftDao.getPage(pageNo,pageSize);
	}

	@Override
	public void update(ILiveGift bean) {
		// TODO Auto-generated method stub
		iLiveGiftDao.update(bean);
	}

	@Override
	public ILiveGift findById(Integer id) {
		return iLiveGiftDao.findById(id);
	}

	@Override
	public void deleteByIds(Integer[] ids) {
		iLiveGiftDao.deleteByIds(ids);
	}

	@Override
	public List<ILiveGift> findGiftList() {
		List<ILiveGift> list = iLiveGiftDao.selectILiveGiftList();
		return list;
	}
	   
	   
	   
}
