package com.bvRadio.iLive.iLive.dao;



import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;

public interface ILiveRedPacketDao {

	Pagination getPage(String name,Integer roomId,Integer pageNo,Integer pageSize);
	
	void save(ILiveRedPacket ILiveRedPacket);
	
	void update(ILiveRedPacket ILiveRedPacketo);
	
	void delete(Long id);
	
	ILiveRedPacket getById(Long id);
	List<ILiveRedPacket> getListByRoomId(Integer roomId);
	ILiveRedPacket getIsStart(Integer roomId);
}
