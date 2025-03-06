package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveViewSetRecord;

public interface ILiveViewSetRecordMng {

	Integer saveRecord(ILiveViewSetRecord iLiveViewSetRecord);
	
	List<ILiveViewSetRecord> getByRoomId(Integer roomId);
	
	Pagination getPage(Integer roomId,Integer pageNo,Integer pageSize);
}
