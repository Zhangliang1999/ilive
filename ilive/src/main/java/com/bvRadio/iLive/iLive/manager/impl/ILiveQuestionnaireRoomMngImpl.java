package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveQuestionnaireRoomDao;
import com.bvRadio.iLive.iLive.entity.ILiveLottery;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireActivity;
import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireActivityMng;
import com.bvRadio.iLive.iLive.manager.ILiveQuestionnaireRoomMng;

@Transactional
public class ILiveQuestionnaireRoomMngImpl implements ILiveQuestionnaireRoomMng {

	@Autowired
	private ILiveQuestionnaireRoomDao iLiveQuestionnaireRoomDao;
	
	@Autowired
	private ILiveQuestionnaireActivityMng iLiveQuestionnaireActivityMng;
	
	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键
	
	@Override
	public void save(ILiveQuestionnaireRoom iLiveQuestionnaireRoom) {
		Long nextId = fieldIdMng.getNextId("ilive_Questionnaire_room","id", 1);
		iLiveQuestionnaireRoom.setId(nextId);
		iLiveQuestionnaireRoomDao.save(iLiveQuestionnaireRoom);
	}

	@Override
	public void update(ILiveQuestionnaireRoom iLiveQuestionnaireRoom) {
		iLiveQuestionnaireRoomDao.update(iLiveQuestionnaireRoom);
	}

	@Override
	public ILiveQuestionnaireRoom getStartByRoomId(Integer roomId) {
		return iLiveQuestionnaireRoomDao.getStartByRoomId(roomId);
	}

	@Override
	public boolean isStartQuestionnaire(Integer roomId) {
		ILiveQuestionnaireRoom startByRoomId = getStartByRoomId(roomId);
		return startByRoomId!=null;
	}

	@Override
	public ILiveQuestionnaireRoom selectByRoomIdAndQuestionnaireId(Integer roomId, Long QuestionnaireId) {
		return iLiveQuestionnaireRoomDao.selectByRoomIdAndQuestionnaireId(roomId,QuestionnaireId);
	}

	@Override
	public void clearEnd(Integer roomId, Integer enterpriseId) {
		System.out.println("***********************************************进入mng");
		List<ILiveQuestionnaireActivity> QuestionnaireList = iLiveQuestionnaireActivityMng.getH5QuestionnaireListByEnterpriseId(enterpriseId);
		List<Long> idList = new ArrayList<>();
		for(ILiveQuestionnaireActivity Questionnaire:QuestionnaireList) {
			idList.add(Questionnaire.getId());
		}
		if(idList!=null&&idList.size()!=0) {
			iLiveQuestionnaireRoomDao.clearEnd(roomId,idList);
		}
	}

}
