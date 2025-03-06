package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;


public interface ILiveSubRoomMng {
	public List<IliveSubRoom> selectILiveSubById(Long userId);

	public void save(IliveSubRoom iLiveSubLevel);
	
	public Integer selectMaxId();

	public void delete(Long userId,String roomId);

	public void update(IliveSubRoom iLiveSubLevel);

	public IliveSubRoom getSubRoom(Long userId);
	//清除该直播间下所有已分配的子账户
	public void delete(String roomId);

	public List<IliveSubRoom> getSubRoomByIds(String userIds);
}
