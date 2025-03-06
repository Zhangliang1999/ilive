package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.BBSDiyformNameDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformName;
import com.bvRadio.iLive.iLive.manager.BBSDiyformNameService;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Transactional
public class BBSDiyformNameServiceImpl implements BBSDiyformNameService {

	@Autowired
	private BBSDiyformNameDao bbsDiyformNameDao;
	@Autowired
	private ILiveFieldIdManagerMng iLiveFieldIdManagerMng;
	
	@Override
	public void save(BBSDiyformName bbsDiyformName) {
		Long nextId = iLiveFieldIdManagerMng.getNextId("bbs_diyform_name", "id", 1);
		bbsDiyformName.setId(nextId.intValue());
		bbsDiyformName.setCreateTime(new Timestamp(new Date().getTime()));
		bbsDiyformNameDao.save(bbsDiyformName);
	}

	@Override
	public BBSDiyformName get(Integer bbsDiyformId, Integer updateMark) {
		return bbsDiyformNameDao.get(bbsDiyformId, updateMark);
	}

	@Override
	public BBSDiyformName getLast(Integer bbsDiyformId) {
		return bbsDiyformNameDao.getLast(bbsDiyformId);
	}

	@Override
	public void update(BBSDiyformName bbsDiyformName) {
		bbsDiyformNameDao.update(bbsDiyformName);
	}

	@Override
	public List<BBSDiyformName> getListByFormId(Integer formId) {
		return bbsDiyformNameDao.getListByFormId(formId);
	}

}
