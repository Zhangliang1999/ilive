package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.RedRecordingBeanDao;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.RedRecordingBeanService;
@Service
@Transactional
public class RedRecordingBeanServiceImpl implements RedRecordingBeanService {
	
	@Autowired
	private RedRecordingBeanDao redRecordingBeanDao;
	@Override
	public Pagination selectRedRecordingByPage(Integer pageNo, Integer pageSize){
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			pagination = redRecordingBeanDao.selectRedRecordingByPage(pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}
	@Override
	public Pagination selectRedRecordingByPageUserBean(int pageNo, int pageSize,
			UserBean userBean) {
		Pagination pagination = new Pagination(pageNo, pageSize, 0);
		try {
			if(userBean!=null){
				long userId = Long.valueOf(userBean.getUserId());
				pagination = redRecordingBeanDao.selectRedRecordingByPageUserId(pageNo, pageSize,(int)userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagination;
	}

}
