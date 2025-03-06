package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveSubAccountDao;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseFansMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubAccountMng;
@Service
@Transactional
public class ILiveSubAccountMngImpl implements ILiveSubAccountMng {
	@Autowired
	private ILiveSubAccountDao iLiveSubAccountDao;
	@Autowired
	private ILiveFieldIdManagerMng fieldManagerMng;
	@Autowired
	private ILiveEnterpriseFansMng enterpriseFansMng;
	/**
	 * 日志
	 */
	Logger logger = LoggerFactory.getLogger(ILiveSubAccountMngImpl.class);
	@Override
	@Transactional(readOnly=true)
	public Pagination selectILiveManagerPage(Integer pageNo, Integer pageSize,
			Integer enterpriseId) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = iLiveSubAccountDao.selectILiveManagerPage(pageNo,pageSize,enterpriseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public boolean addILiveSubAccountMng(ILiveManager manager) {
		boolean ret = false;
		try {
			long userId = fieldManagerMng.getNextId("ilive_manager", "user_id", 1);
			manager.setUserId(userId);
			iLiveSubAccountDao.addILiveSubAccountMng(manager);
			/**
			 * 加关注一个企业
			 */
			try {
				Long enterpriseId = manager.getEnterpriseId(); 
				enterpriseFansMng.addEnterpriseConcern(enterpriseId.intValue(), userId);
			} catch (Exception e) {
				logger.error("注册用户关注的时候发生异常" + e.toString());
			}
			ret = true;
		} catch (Exception e) {
			logger.error("注册用户" + e.toString());
		}
		return ret;
	}

	@Override
	public List<ILiveManager> getILiveManagerPage(Integer enterpriseId,String roomId,String userId) {
		// TODO Auto-generated method stub
		return iLiveSubAccountDao.getILiveManagerPage(enterpriseId,roomId,userId);
	}

	@Override
	public List<ILiveManager> selectILiveManagerPage(Integer enterpriseId) {
		// TODO Auto-generated method stub
		return iLiveSubAccountDao.getILiveManagerPage(enterpriseId);
	}

	
}
