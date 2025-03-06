package com.bvRadio.iLive.iLive.manager;

import java.sql.Timestamp;


import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRecord;

public interface ILiveLiveRecordMng {
	public Pagination getPage(Integer id, Integer userId, Integer roomId,
			Timestamp beginTime, Integer income,Integer totalNumber,int pageNo, int pageSize);
	
	public ILiveLiveRecord findById(Integer id);

	public ILiveLiveRecord save(ILiveLiveRecord bean);

	public ILiveLiveRecord deleteById(Integer id);

	public ILiveLiveRecord update(ILiveLiveRecord bean);
	
}
