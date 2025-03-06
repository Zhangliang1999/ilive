package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;

public interface ILiveQuestionnaireOptionDao {

	public List<ILiveQuestionnaireOption> getListByProblemId(Long problemId);
	
	public void save(ILiveQuestionnaireOption iLiveQuestionnaireOption);
	
	public void update(ILiveQuestionnaireOption iLiveQuestionnaireOption);
	
	public void delete(Long id);
	
	public ILiveQuestionnaireOption getById(Long id);
	
	public void deleteAllByProblemId(Long problemId);
	
}
