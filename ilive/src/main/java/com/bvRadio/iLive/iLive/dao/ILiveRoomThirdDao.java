package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.IliveRoomThird;



/**
 * 第三方推流管理
 * @author zhangliang
 *
 */
public interface ILiveRoomThirdDao {
	
	public List<IliveRoomThird> selectILiveRoomThirdById(Integer roomId);

	public void save(IliveRoomThird iliveRoomThird);
	
	public Integer selectMaxId();

	public void delete(Integer roomId);

	public void update(IliveRoomThird iliveRoomThird);

	public IliveRoomThird getRoomThird(Integer roomId);

	public Pagination selectILiveRoomThirdPage(Integer pageNo, Integer pageSize, Integer roomId) throws Exception;

	public List<IliveRoomThird> getILiveRoomThirdById(Integer id);

	public void updateStatues(Integer roomId,Integer Status);
}
