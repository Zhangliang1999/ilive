package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;

public interface ILiveQuestionnaireRoomMng {

	void save(ILiveQuestionnaireRoom iLiveQuestionnaireRoom);
	
	void update(ILiveQuestionnaireRoom iLiveQuestionnaireRoom);
	
	ILiveQuestionnaireRoom getStartByRoomId(Integer roomId);
	
	boolean isStartQuestionnaire(Integer roomId);
	
	ILiveQuestionnaireRoom selectByRoomIdAndQuestionnaireId(Integer roomId,Long questionnaireId);
	
	void clearEnd(Integer roomId,Integer enterpriseId);
}
