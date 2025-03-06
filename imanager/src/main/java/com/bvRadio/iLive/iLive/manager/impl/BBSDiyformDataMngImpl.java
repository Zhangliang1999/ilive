package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.BBSDiyformDataDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;


@Service
@Transactional
public class BBSDiyformDataMngImpl implements BBSDiyformDataMng  {

	@Autowired
	private BBSDiyformDataDao bbsDiyformDataDao;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;
	
	
	@Override
	public void saveBBSDiyfromData(BBSDiyformData bbsDiyformData) {
		bbsDiyformDataDao.saveDiyformData(bbsDiyformData);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean checkUserSignUp(String userId, Integer formId) {
		return bbsDiyformDataDao.checkUserSignUp(userId,formId);
	}

	@Override
	public void saveBBSDiyfromData(List<BBSDiyformData> diyformDatas) {
		long nextId = fieldIdMng.getNextId("bbs_diyform_data", "id", diyformDatas.size());
		int first = (int)nextId-diyformDatas.size();
		for (int i = 0; i < diyformDatas.size(); i++) {
			diyformDatas.get(i).setId(first);
			first++;
		}
		bbsDiyformDataDao.saveDiyFormList(diyformDatas);
	}

	
	
	
	
}
