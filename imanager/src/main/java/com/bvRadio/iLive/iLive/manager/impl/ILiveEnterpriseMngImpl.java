package com.bvRadio.iLive.iLive.manager.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveEnterpriseDao;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

@Transactional
@Service
public class ILiveEnterpriseMngImpl implements ILiveEnterpriseMng {

	@Autowired
	private ILiveEnterpriseDao iLiveEnterpriseDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Autowired
	private HttpServletRequest request;

	@Override
	@Transactional
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise) {
		long enterpriseId = fieldIdMng.getNextId("ilive_enterprise", "enterprise_id", 1);
		iLiveEnterprise.setEnterpriseId((int) enterpriseId);
		UserBean user = ILiveUtils.getUser(request);
		Long userId = Long.valueOf(user.getUserId());
		ILiveManager iLiveManager = iLiveManagerMng.getILiveManager(userId);
		iLiveManager.setEnterPrise(iLiveEnterprise);
		user.setEnterpriseId((int) enterpriseId);
		ILiveUtils.setUser(request, user);
		return iLiveEnterpriseDao.saveILiveEnterprise(iLiveEnterprise);
	}

	@Override
	public ILiveEnterprise getILiveEnterPrise(Integer enterpriseId) {
		return iLiveEnterpriseDao.getILiveEnterpriseById(enterpriseId);
	}

	@Override
	public ILiveEnterprise getdefaultEnterprise() {
		return iLiveEnterpriseDao.getILiveEnterpriseById(Integer.parseInt(ConfigUtils.get("defaultEnterpriseId")));
	}

	@Override
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType) {
		return iLiveEnterpriseDao.getPagerForView(keyWord,pageNo,pageSize,searchType);
	}

}
