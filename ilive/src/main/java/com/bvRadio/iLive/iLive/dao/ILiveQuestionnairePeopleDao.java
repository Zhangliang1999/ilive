package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;

public interface ILiveQuestionnairePeopleDao {

	public List<ILiveQuestionnairePeople> getListByUserId(Long userId,String mobile, Long questionnaireId);
	
	public Integer getAllPeopleByQuestionnaireId(Long questionnaireId);
	
	public Integer getPeopleByOptionId(Long optionId);
	
	public void save(ILiveQuestionnairePeople iLiveQuestionnairePeople);
	
	public Integer getPeopleByProblemId(Long problemId);

	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize);

	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId);

	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId, Long problemId);

	public Pagination getByQuestionnaireId(Long questionnaireId, Long problemId, Integer pageNo, Integer pageSize);
}
