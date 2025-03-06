package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveHomepageLinkDao;
import com.bvRadio.iLive.iLive.entity.ILiveHomepageLink;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveHomepageLinkMng;

@Service
@Transactional
public class ILiveHomepageLinkMngImpl implements ILiveHomepageLinkMng {

	@Autowired
	private ILiveHomepageLinkDao iLiveHomepageLinkDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public ILiveHomepageLink save(ILiveHomepageLink link) {
		Long nextId = fieldIdMng.getNextId("ilive_homepage_link", "id", 1);
		link.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		link.setCreateTime(now);
		iLiveHomepageLinkDao.save(link);
		return link;
	}

	@Override
	public ILiveHomepageLink getById(Long id) {
		return iLiveHomepageLinkDao.getById(id);
	}

	@Override
	public void update(ILiveHomepageLink link) {
		iLiveHomepageLinkDao.update(link);
	}

	@Override
	public Pagination getPage(Integer roomId, Integer pageNo, Integer pageSize) {
		return iLiveHomepageLinkDao.getPage(roomId, pageNo, pageSize);
	}

	@Override
	public List<ILiveHomepageLink> getListByEnterpriseId(Integer enterpriseId) {
		return iLiveHomepageLinkDao.getListByEnterpriseId(enterpriseId);
	}
	
	
	
	
}
