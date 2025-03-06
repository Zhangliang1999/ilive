package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveCertTopicDao;
import com.bvRadio.iLive.iLive.dao.ILiveManagerStateDao;
import com.bvRadio.iLive.iLive.entity.ILiveCertTopic;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveManagerState;
import com.bvRadio.iLive.iLive.manager.ILiveCertTopicMng;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveManagerMng;


public class ILiveCertTopicMngImpl implements ILiveCertTopicMng {

	@Autowired
	private ILiveCertTopicDao iLiveCertTopicDao;

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;

	@Autowired
	private ILiveManagerStateDao iLiveManagerStateDao;

	@Autowired
	private ILiveManagerMng iLiveManagerMng;

	@Override
	public List<ILiveCertTopic> getCertTopicList(Integer enterpriseId) {
		List<ILiveCertTopic> certTopicList = iLiveCertTopicDao.getCertTopicList(enterpriseId);
		return certTopicList;
	}

	@Override
	@Transactional
	public boolean addCertTopic(ILiveCertTopic certTopic) {
		Long topicId = fieldIdMng.getNextId("imanager_cert_topic", "topic_id", 1);
		certTopic.setTopicId(topicId);
		
		List<ILiveManager> iLiveManagerByEnterpriseId = iLiveManagerMng
				.getILiveManagerByEnterpriseId(certTopic.getEnterpriseId());
		if (iLiveManagerByEnterpriseId != null && !iLiveManagerByEnterpriseId.isEmpty()) {
			for (ILiveManager iManager : iLiveManagerByEnterpriseId) {
				ILiveManagerState managerState = new ILiveManagerState();
				ILiveManagerState iliveManagerState = iLiveManagerStateDao.getIliveManagerState(iManager.getUserId());
				if (iliveManagerState != null) {
					iliveManagerState.setCertStatus(certTopic.getCertStatus());
					iLiveManagerStateDao.updateIliveManagerState(iliveManagerState);
				} else {
					managerState.setCertStatus(certTopic.getCertStatus());
					managerState.setManagerId(iManager.getUserId());
					iLiveManagerStateDao.saveIliveManagerState(managerState);
				}
			}
		}
		return iLiveCertTopicDao.addCertTopic(certTopic);
	}

}
