package com.bvRadio.iLive.iLive.dao;


import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;

public interface ILiveQuestionnaireStatisticDao {

	public ILiveQuestionnaireActivityStatistic getListByUserId(Long userId,Long QuestionnaireId);
	
	public void update(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic);
	
	public void save(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic);
	
	public Long maxId();

	public List<ILiveQuestionnaireActivityStatistic> getListByQuestionnaireId(Long questionnaireId);
}
