package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.IliveRoomThird;




public interface ILiveRoomThirdMng {
	public List<IliveRoomThird> selectILiveRoomThirdById(Integer roomId);
	public List<IliveRoomThird> getILiveRoomThirdById(Integer id);
	public void save(IliveRoomThird iliveRoomThird);
	
	public Integer selectMaxId();

	public void delete(Integer id);

	public void update(IliveRoomThird iliveRoomThird);
	public void updateStatues(Integer roomId,Integer Status);
	public IliveRoomThird getRoomThird(Integer id);

	public Pagination selectILiveRoomThirdPage(Integer i, Integer j, Integer roomId);
}
