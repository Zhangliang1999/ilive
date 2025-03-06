package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveQuestionnaireRoom;

public interface ILiveQuestionnaireRoomDao {

	void save(ILiveQuestionnaireRoom iLiveQuestionnaireRoom);
	
	void update(ILiveQuestionnaireRoom iLiveQuestionnaireRoom);
	
	ILiveQuestionnaireRoom getStartByRoomId(Integer roomId);
	
	ILiveQuestionnaireRoom selectByRoomIdAndQuestionnaireId(Integer roomId, Long questionnaireId);
	
	void clearEnd(Integer roomId,List<Long> list);
}
