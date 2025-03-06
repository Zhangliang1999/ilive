package com.bvRadio.iLive.iLive.manager.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireStatisticDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;

import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireStatisticMng;

@Service
@Transactional
public class ILiveQuestionnaireStatisticMngImpl implements ILiveQuestionnaireStatisticMng {

	@Autowired ILiveQuestionnaireStatisticDao iLiveQuestionnaireStatisticDao;
	
	@Override
	public ILiveQuestionnaireActivityStatistic getListByUserId(Long userId,Long QuestionnaireId) {
		// TODO Auto-generated method stub
		return iLiveQuestionnaireStatisticDao.getListByUserId(userId,QuestionnaireId);
	}

	@Override
	public void update(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic) {
		// TODO Auto-generated method stub
		iLiveQuestionnaireStatisticDao.update(iLiveQuestionnaireActivityStatistic);
	}

	@Override
	public void save(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic) {
		// TODO Auto-generated method stub
		iLiveQuestionnaireStatisticDao.save(iLiveQuestionnaireActivityStatistic);
	}

	@Override
	public Long maxId() {
		// TODO Auto-generated method stub
		return iLiveQuestionnaireStatisticDao.maxId();
	}

	@Override
	public List<ILiveQuestionnaireActivityStatistic> getListByQuestionnaireId(Long QuestionnaireId) {
		// TODO Auto-generated method stub
		return iLiveQuestionnaireStatisticDao.getListByQuestionnaireId(QuestionnaireId);
	}

	
}
