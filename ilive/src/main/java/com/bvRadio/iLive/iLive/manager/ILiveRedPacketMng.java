package com.bvRadio.iLive.iLive.manager;


import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveRedPacket;

public interface ILiveRedPacketMng {

	Pagination getPage(String name,Integer roomId,Integer pageNo,Integer pageSize);
	
	Long save(ILiveRedPacket ILiveRedPacket);
	
	void update(ILiveRedPacket ILiveRedPacket);
	
	void delete(Long id);
	
	ILiveRedPacket getById(Long id);

	List<ILiveRedPacket> getListByRoomId(Integer roomId);

	ILiveRedPacket getIsStart(Integer roomId);
}
