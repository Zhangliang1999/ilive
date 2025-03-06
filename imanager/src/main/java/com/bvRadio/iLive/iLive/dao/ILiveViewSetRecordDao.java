package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;

public interface ILiveViewSetRecordDao {

	void saveRecord(ILiveViewSetRecord iLiveViewSetRecord);
	
	public List<ILiveViewSetRecord> getByRoomId(Integer roomId);
	
	Pagination getPage(Integer roomId,Integer pageNo,Integer pageSize);
}
