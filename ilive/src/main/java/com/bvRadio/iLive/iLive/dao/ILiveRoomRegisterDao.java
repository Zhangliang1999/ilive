package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;

public interface ILiveRoomRegisterDao {

	//保存签到、点赞数据
	void saveILiveRoomRegiste(ILiveRoomRegister iLiveRoomRegister);
	
	//查询签到、点赞数据
	ILiveRoomRegister queryILiveRoomRegisterByUserIdAndRoomId(String userId,Integer roomId);
	
	//查询直播场次是否签到
	boolean queryILiveIsRegister(String userId,Long liveEventId);
	
	//查询直播间是否点赞
	boolean queryIliveRoomRegister(String userId,Integer roomId);
	
	public boolean queryMediaRegister(String userId, Long fileId);
	
	public Pagination querySignByRoomId(Integer roomId,Long liveEventId, String name,Integer state,Integer pageNo,Integer pageSize);

	Long MediaRegisterCount(Long fileId);

	Long ILiveIsRegisterCount(Long liveEventId);
	
}
