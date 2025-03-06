package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnairePeopleDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnairePeople;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnairePeopleMng;

@Service
@Transactional
public class ILiveQuestionnairePeopleMngImpl implements ILiveQuestionnairePeopleMng {

	@Autowired
	private ILiveQuestionnairePeopleDao iLiveQuestionnairePeopleDao;	//用户投票记录

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveQuestionnairePeople> getListByUserId(Long userId,String mobile,Long QuestionnaireId) {
		return iLiveQuestionnairePeopleDao.getListByUserId(userId,mobile,QuestionnaireId);
	}

	@Override
	public Long save(ILiveQuestionnairePeople iLiveQuestionnairePeople) {
		Long nextId = fieldIdMng.getNextId("ilive_Questionnaire_people", "id", 1);
		iLiveQuestionnairePeople.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveQuestionnairePeople.setCreateTime(now);
		iLiveQuestionnairePeopleDao.save(iLiveQuestionnairePeople);
		return nextId;
	}

	@Override
	public Integer getAllPeopleByQuestionnaireId(Long QuestionnaireId) {
		return iLiveQuestionnairePeopleDao.getAllPeopleByQuestionnaireId(QuestionnaireId);
	}

	@Override
	public Integer getPeopleByOptionId(Long optionId) {
		return iLiveQuestionnairePeopleDao.getPeopleByOptionId(optionId);
	}

	@Override
	public Integer getPeopleByProblemId(Long problemId) {
		return iLiveQuestionnairePeopleDao.getPeopleByProblemId(problemId);
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return iLiveQuestionnairePeopleDao.getByQuestionnaireId(questionnaireId,pageNo,pageSize);
	}

	@Override
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId) {
		// TODO Auto-generated method stub
		return iLiveQuestionnairePeopleDao.getListByQuestionnaireId(questionnaireId);
	}

	@Override
	public List<ILiveQuestionnairePeople> getListByQuestionnaireId(Long questionnaireId, Long problemId) {
		// TODO Auto-generated method stub
		return iLiveQuestionnairePeopleDao.getListByQuestionnaireId(questionnaireId,problemId);
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Long problemId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return iLiveQuestionnairePeopleDao.getByQuestionnaireId(questionnaireId,problemId,pageNo,pageSize);
	}
}
