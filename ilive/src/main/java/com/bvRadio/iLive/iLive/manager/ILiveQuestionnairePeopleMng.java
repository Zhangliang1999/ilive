package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;

public interface ILiveQuestionnairePeopleMng {

	public List<ILiveQuestionnairePeople> getListByUserId(Long userId,String mobile,Long questionnaireId);
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId);
	public Long save(ILiveQuestionnairePeople iLiveQuestionnairePeople);
	public Pagination getByQuestionnaireId(Long questionnaireId,Integer pageNo,Integer pageSize);
	public Integer getAllPeopleByQuestionnaireId(Long questionnaireId);
	public Integer getPeopleByOptionId(Long optionId);
	public Integer getPeopleByProblemId(Long problemId);
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId, Long problemId);
	public Pagination getByQuestionnaireId(Long questionnaireId, Long problemId,Integer pageNo,Integer pageSize);
	
}
