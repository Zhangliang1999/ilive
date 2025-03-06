package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;

public interface ILiveQuestionnaireProblemMng {

	List<ILiveQuestionnaireProblem> getListByQuestionnaireId(Long questionnaireId);
	
	public Long save(ILiveQuestionnaireProblem iLiveQuestionnaireProblem);
	
	public void update(ILiveQuestionnaireProblem iLiveQuestionnaireProblem);
	
	public void deleteById(Long id);
	
	ILiveQuestionnaireProblem getById(Long id);
	Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize);
	public void deleteAllByQuestionnaireId(Long questionnaireId);
	
	public List<ILiveQuestionnaireProblem> getResultById(Long QuestionnaireId,Long problemId,Integer pageNo, Integer pageSize);
}
