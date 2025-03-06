package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;
import com.bvRadio.iLive.iLive.entity.IliveSubRoom;

/**
 * 礼物管理
 * @author YanXL
 *
 */
public interface ILiveSubRoomDao {
	
	public List<IliveSubRoom> selectILiveSubById(Long userId);

	public void save(IliveSubRoom iLiveLiveRoom);
	
	public Integer selectMaxId();

	public void delete(Long userId,String roomId);

	public void update(IliveSubRoom iLiveLiveRoom);

	public IliveSubRoom getSubRoom(Long userId);

	public void delete(String roomId);

	public List<IliveSubRoom> getSubRoomByIds(String userIds);

	
}
