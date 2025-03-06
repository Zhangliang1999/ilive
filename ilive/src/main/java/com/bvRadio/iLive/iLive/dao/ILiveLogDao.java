package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;
import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveLog;

public interface ILiveLogDao {
	public ILiveLog findById(Integer id);

	public ILiveLog save(ILiveLog  bean);

	public ILiveLog deleteById(Integer id);

	public void update(ILiveLog bean);
	
	public ILiveLog updateByUpdater(Updater<ILiveLog> updater);

}