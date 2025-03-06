package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;

public interface ILiveQuestionnaireProblemDao {

	public List<ILiveQuestionnaireProblem> getListByQuestionnaireId(Long questionnaireId);
	
	public void save(ILiveQuestionnaireProblem iLiveQuestionnaireProblem);
	
	public void update(ILiveQuestionnaireProblem iLiveQuestionnaireProblem);
	
	public void deleteById(Long id);
	
	public ILiveQuestionnaireProblem getById(Long id);
	
	public void deleteAllByQuestionnaireId(Long questionnaireId);

	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize);

	public List<ILiveQuestionnaireProblem> getResultById(Long questionnaireId, Long problemId, Integer pageNo,
			Integer pageSize);
}
