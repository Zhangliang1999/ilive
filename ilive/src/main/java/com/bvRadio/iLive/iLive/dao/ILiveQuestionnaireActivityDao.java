package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;
import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;

public interface ILiveQuestionnaireActivityDao {

	public Pagination getPage(Integer roomId,String name, Integer pageNo, Integer pageSize);
	public Pagination getPageByEnterpriseId(Integer enterpriseId,String name, String time, Integer pageNo, Integer pageSize);
	
	public ILiveQuestionnaireActivity getById(Long questionnaireId);
	
	public void save(ILiveQuestionnaireActivity questionnaireActivity);
	
	public void update(ILiveQuestionnaireActivity questionnaireActivity);
	
	public ILiveQuestionnaireActivity getActivityByRoomId(Integer roomId);
	ILiveQuestionnaireActivity getActivityByenterpriseId(Integer enterpriseId);
	public ILiveQuestionnaireActivity getActivityByEnterpriseId(Integer enterpriseId);
	
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList(Integer roomId);
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireList2(Integer enterpriseId);
	public List<ILiveQuestionnaireActivity> getH5QuestionnaireListByEnterpriseId(Integer enterpriseId);
	List<ILiveQuestionnaireActivity> getH5QuestionnaireListByUserId(Long userId);
	Pagination getpageByUserId(Long userId,String questionnairename,Integer pageNo,Integer pageSize);
	public ILiveQuestionnaireActivity getH5Questionnaire2(Integer enterpriseId);
	public ILiveQuestionnaireActivity getH5Questionnaire(Integer roomId);
	
	void checkEndIsClose();
}
