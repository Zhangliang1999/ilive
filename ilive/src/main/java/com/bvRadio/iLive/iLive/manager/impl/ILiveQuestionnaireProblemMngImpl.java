package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireProblemDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireProblem;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireProblemMng;

@Service
@Transactional
public class ILiveQuestionnaireProblemMngImpl implements ILiveQuestionnaireProblemMng{

	@Autowired
	private ILiveQuestionnaireProblemDao iLiveQuestionnaireProblemDao;	//投票问题
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveQuestionnaireProblem> getListByQuestionnaireId(Long QuestionnaireId) {
		return iLiveQuestionnaireProblemDao.getListByQuestionnaireId(QuestionnaireId);
	}

	@Override
	public Long save(ILiveQuestionnaireProblem iLiveQuestionnaireProblem) {
		Long nextId = fieldIdMng.getNextId("ilive_Questionnaire_problem", "id", 1);
		iLiveQuestionnaireProblem.setId(nextId);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveQuestionnaireProblem.setCreateTime(now);
		iLiveQuestionnaireProblem.setUpdateTime(now);
		iLiveQuestionnaireProblemDao.save(iLiveQuestionnaireProblem);
		return nextId;
	}

	@Override
	public void update(ILiveQuestionnaireProblem iLiveQuestionnaireProblem) {
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveQuestionnaireProblem.setUpdateTime(now);
		iLiveQuestionnaireProblemDao.update(iLiveQuestionnaireProblem);
	}

	@Override
	public void deleteById(Long id) {
		iLiveQuestionnaireProblemDao.deleteById(id);
	}

	@Override
	public ILiveQuestionnaireProblem getById(Long id) {
		return iLiveQuestionnaireProblemDao.getById(id);
	}

	@Override
	public void deleteAllByQuestionnaireId(Long QuestionnaireId) {
		iLiveQuestionnaireProblemDao.deleteAllByQuestionnaireId(QuestionnaireId);
	}

	@Override
	public Pagination getByQuestionnaireId(Long questionnaireId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		return iLiveQuestionnaireProblemDao.getByQuestionnaireId(questionnaireId,pageNo,pageSize);
	}

	@Override
	public List<ILiveQuestionnaireProblem> getResultById(Long QuestionnaireId, Long problemId, Integer pageNo,
			Integer pageSize) {
		// TODO Auto-generated method stub
		return iLiveQuestionnaireProblemDao.getResultById(QuestionnaireId,problemId,pageNo,pageSize);
	}

}
