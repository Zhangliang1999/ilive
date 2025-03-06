package com.bvRadio.iLive.iLive.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireOptionDao;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireOption;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireOptionMng;

@Transactional
public class ILiveQuestionnaireOptionMngImpl implements ILiveQuestionnaireOptionMng {

	@Autowired
	private ILiveQuestionnaireOptionDao iLiveQuestionnaireOptionDao;		//投票问题选项
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public List<ILiveQuestionnaireOption> getListByProblemId(Long problemId) {
		return iLiveQuestionnaireOptionDao.getListByProblemId(problemId);
	}

	@Override
	public Long save(ILiveQuestionnaireOption iLiveQuestionnaireOption) {
		Long nextId = fieldIdMng.getNextId("ilive_Questionnaire_option", "id", 1);
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveQuestionnaireOption.setId(nextId);
		iLiveQuestionnaireOption.setCreateTime(now);
		iLiveQuestionnaireOption.setUpdateTime(now);
		iLiveQuestionnaireOptionDao.save(iLiveQuestionnaireOption);
		return nextId;
	}

	@Override
	public void update(ILiveQuestionnaireOption iLiveQuestionnaireOption) {
		Timestamp now = new Timestamp(new Date().getTime());
		iLiveQuestionnaireOption.setUpdateTime(now);
		iLiveQuestionnaireOptionDao.update(iLiveQuestionnaireOption);
	}

	@Override
	public void delete(Long id) {
		iLiveQuestionnaireOptionDao.delete(id);
	}

	@Override
	public ILiveQuestionnaireOption getById(Long id) {
		return iLiveQuestionnaireOptionDao.getById(id);
	}

	@Override
	public void deleteAllByProblemId(Long problemId) {
		iLiveQuestionnaireOptionDao.deleteAllByProblemId(problemId);
	}


}
