package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.RechargeRecordDao;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.RechargeRecordMng;
@Service
@Transactional
public class RechargeRecordMngImpl implements RechargeRecordMng {
	
	@Autowired
	private RechargeRecordDao rechargeRecordDao;
	
	@Override
	@Transactional(readOnly = true)
	public Pagination getPage(Integer pageNo, Integer pageSize,Integer payment_status, Integer payment_type, Integer keyword) {
		Pagination pagination = new Pagination();
		try {
			pagination = rechargeRecordDao.getPage(pageNo, pageSize,payment_status, payment_type, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

	@Override
	public Pagination getPersonalPage(Integer pageNo, Integer pageSize,
			UserBean userBean,Integer payment_status) {
		Pagination pagination = new Pagination();
		try {
			pagination = rechargeRecordDao.getPersonalPage(pageNo, pageSize, userBean,payment_status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

}
