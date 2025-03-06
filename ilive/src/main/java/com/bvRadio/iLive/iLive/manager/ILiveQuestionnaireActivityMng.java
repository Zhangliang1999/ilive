package com.bvRadio.iLive.iLive.manager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.vo.ILiveWenJuanResult;

public interface ILiveQuestionnaireActivityMng {

	public Pagination getPage(Integer roomId,String name,Integer pageNo,Integer pageSize);
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name,String time, Integer pageNo,Integer pageSize);
	
	public ILiveQuestionnaireActivity getById(Long questionnaireId);

	public Long save(ILiveQuestionnaireActivity questionnaireActivity);
	
	public void update(ILiveQuestionnaireActivity questionnaireActivity);
	
	public void createQuestionnaire(ILiveQuestionnaireActivity iLiveQuestionnaireActivity,String strList);
	public void updateQuestionnaire(ILiveQuestionnaireActivity iLiveQuestionnaireActivity,String strList);
	
	public ILiveQuestionnaireActivity getActivityByRoomId(Integer roomId);
	public ILiveQuestionnaireActivity getActivityByenterpriseId(Integer enterpriseId);
	public ILiveQuestionnaireActivity getActivityByEnterpriseId(Integer enterpriseId);
	
	/**
	 * 获取直播间已开启的和没有开启的投票活动
	 * @param roomId
	 * @return
	 */
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList(Integer roomId);
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByEnterpriseId(Integer enterpriseId);
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByUserId(Long userId);
	Pagination getpageByUserId(Long userId,String questionnairename,Integer pageNo,Integer pageSize);
	/**
	 * 获取直播间已开启的投票活动
	 * @param roomId
	 * @return
	 */
	public ILiveQuestionnaireActivity getH5Questionnaire(Integer roomId);
	public ILiveQuestionnaireActivity getH5Questionnaire2(Integer enterpriseId);
	
	public void Questionnaire(Long questionnaireId,Long userId,String string, String questionAnswers);
	
	public ILiveQuestionnaireActivity getResult(Long questionnaireId);
	public ILiveQuestionnaireActivity getById1(Long questionnaireId,Long problemId,Integer pageNo);
	public List<ILiveWenJuanResult> getByuserId(Long questionnaireId, Long userId);
	public Map<Long, Object> getByquestionnaireId(Long questionnaireId);
}
