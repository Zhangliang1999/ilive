package com.bvRadio.iLive.iLive.manager;


import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivityStatistic;

public interface ILiveQuestionnaireStatisticMng {

	public ILiveQuestionnaireActivityStatistic getListByUserId(Long userId,Long QuestionnaireId);
	public List<ILiveQuestionnaireActivityStatistic> getListByQuestionnaireId(Long QuestionnaireId);
	public void update(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic);
	
	public void save(ILiveQuestionnaireActivityStatistic iLiveQuestionnaireActivityStatistic);
	
	public Long maxId();
}
