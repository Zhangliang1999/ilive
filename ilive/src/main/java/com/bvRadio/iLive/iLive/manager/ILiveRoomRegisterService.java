package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveRoomRegister;

public interface ILiveRoomRegisterService {

	//保存签到、点赞数据
	ILiveRoomRegister saveILiveRoomRegiste(ILiveRoomRegister iLiveRoomRegister);
	
	/**
	 * 不要使用这个接口
	 * @param userId
	 * @param roomId
	 * @return
	 */
	ILiveRoomRegister queryILiveRoomRegisterByUserIdAndRoomId(String userId,Integer roomId);
	
	//查询直播间是否签到
	boolean queryILiveIsRegister(String userId,Long liveEventId);
	
	//查询直播间是否点赞
	boolean queryIliveRoomRegister(String userId,Integer roomId);
	
	//查询回看是否点赞
	boolean queryMediaRegister(String userId,Long fileId);
	
	Pagination querySignByRoomId(Integer roomId,Long liveEventId,String name,Integer state,Integer pageNo,Integer pageSize);
	
	Long MediaRegisterCount(Long fileId);
	Long ILiveIsRegisterCount(Long liveEventId);
}
