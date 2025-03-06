package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRecord;

public interface ILiveLiveRecordDao {
	public Pagination getPage(Integer id, Integer userId, Integer roomId,
			Timestamp beginTime, Integer income,
			Integer totalNumber,int pageNo, int pageSize);
	
	public ILiveLiveRecord findById(Integer id);

	public ILiveLiveRecord save(ILiveLiveRecord bean);

	public ILiveLiveRecord deleteById(Integer id);

	public void update(ILiveLiveRecord bean);
	
	public ILiveLiveRecord updateByUpdater(Updater<ILiveLiveRecord> updater);

}