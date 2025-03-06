package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;

public interface ILiveQuestionnaireOptionMng {

	public List<ILiveQuestionnaireOption> getListByProblemId(Long problemId);
	
	public Long save(ILiveQuestionnaireOption iLiveQuestionnaireOption);
	
	public void update(ILiveQuestionnaireOption iLiveQuestionnaireOption);
	
	public void delete(Long id);
	
	ILiveQuestionnaireOption getById(Long id);
	
	void deleteAllByProblemId(Long problemId);
	
}
