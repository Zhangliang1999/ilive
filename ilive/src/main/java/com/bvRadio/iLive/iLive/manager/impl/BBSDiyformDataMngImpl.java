package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.BBSDiyformDataDao;
import com.bvRadio.iLive.iLive.dao.ILiveManagerDao;
import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.manager.BBSDiyformDataMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;

@Service
@Transactional
public class BBSDiyformDataMngImpl implements BBSDiyformDataMng {

	@Autowired
	private BBSDiyformDataDao bbsDiyformDataDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;

	@Autowired
	private ILiveManagerDao iLiveManagerDao;

	@Override
	public void saveBBSDiyfromData(BBSDiyformData bbsDiyformData) {
		bbsDiyformDataDao.saveDiyformData(bbsDiyformData);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkUserSignUp(String userId, Integer formId) {
		return bbsDiyformDataDao.checkUserSignUp(userId, formId);
	}

	@Override
	public void saveBBSDiyfromData(List<BBSDiyformData> diyformDatas) {
		long nextId = fieldIdMng.getNextId("bbs_diyform_data", "id", diyformDatas.size());
		long submitId = nextId;
		int first = (int) nextId - diyformDatas.size();
		for (int i = 0; i < diyformDatas.size(); i++) {
			diyformDatas.get(i).setId(first);
			diyformDatas.get(i).setSubmitId(submitId);
			first++;
		}
		bbsDiyformDataDao.saveDiyFormList(diyformDatas);
	}
	
	
		
	@Override
	@Transactional(readOnly = true)
	public Pagination distinctDiyformSubmitId(Integer formId, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
		Pagination distinctDiyformUser = bbsDiyformDataDao.distinctDiyformSunmitId(formId, startTime, endTime, pageNo, pageSize);
		return distinctDiyformUser;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Long> distinctDiyformSubmitId(Integer formId, Date startTime, Date endTime) {
		return bbsDiyformDataDao.distinctDiyformSunmitId(formId, startTime, endTime);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ILiveManager> distinctDiyformUser(Integer formId, Integer pageNo, Integer pageSize) {
		try {
			Pagination distinctDiyformUser = bbsDiyformDataDao.distinctDiyformUser(formId, pageNo, pageSize);
			if(distinctDiyformUser!=null) {
				List<Long> list = (List<Long>) distinctDiyformUser.getList();
				if (list != null && !list.isEmpty()) {
					Long[] managerList = new Long[list.size()];
					for (int i = 0; i < managerList.length; i++) {
						managerList[i] = list.get(i);
					}
					List<ILiveManager> liveMaangers = iLiveManagerDao.getIiveManagerList(managerList);
					if (liveMaangers != null && !liveMaangers.isEmpty()) {
						Map<Long, ILiveManager> map = new HashMap<>();
						for (ILiveManager manager : liveMaangers) {
							map.put(manager.getUserId(), manager);
						}
						List<ILiveManager> newManager = new ArrayList<>();
						for (Long userId : managerList) {
							newManager.add(map.get(userId));
						}
						return newManager;
					}
				}
			}else {
				return null;
			}
		}catch (Exception e) {
			return null;
		}
		return null;
	}

	
	
	/**
	 * 根据提交ID查问题
	 * @param userId
	 * @param questionIds
	 * @return
	 */
	@Override
	public String[] getFormStatic(Long submitId, Integer[] questionIds) {
		 String[] formStatic = bbsDiyformDataDao.getFormStaticBySubmitId(submitId,questionIds);
		 return formStatic;
	}
	
	/**
	 * 根据用户查问题
	 * @param userId
	 * @param questionIds
	 * @return
	 */
	@Override
	public String[] getFormStatic(ILiveManager manager, Integer[] questionIds) {
		 String[] formStatic = bbsDiyformDataDao.getFormStatic(manager,questionIds);
		 return formStatic;
	}

	@Override
	public List<ILiveManager> distinctDiyformUserAndUsername(String username, Integer formId, Integer pageNo,
			Integer pageSize) {
		Pagination distinctDiyformUser = bbsDiyformDataDao.distinctDiyformUser(formId, pageNo, pageSize);
		List<Long> list = (List<Long>) distinctDiyformUser.getList();
		if (list != null && !list.isEmpty()) {
			Long[] managerList = new Long[list.size()];
			for (int i = 0; i < managerList.length; i++) {
				managerList[i] = list.get(i);
			}
			List<ILiveManager> liveMaangers = iLiveManagerDao.getIiveManagerListAndName(username,managerList);
			if (liveMaangers != null && !liveMaangers.isEmpty()) {
				Map<Long, ILiveManager> map = new HashMap<>();
				for (ILiveManager manager : liveMaangers) {
					map.put(manager.getUserId(), manager);
				}
				List<ILiveManager> newManager = new ArrayList<>();
				for (Long userId : managerList) {
					newManager.add(map.get(userId));
				}
				return newManager;
			}
		}
		return null;
	}

}
