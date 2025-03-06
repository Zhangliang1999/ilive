package com.bvRadio.iLive.iLive.dao;

import java.sql.Timestamp;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveReport;

public interface ILiveLiveReportDao {
	public Pagination getPage(Integer id, String reporter, String reported,
			String content, Integer statu, Timestamp submitTime,
			Timestamp dealTime,int pageNo, int pageSize);
	
	public ILiveLiveReport findById(Integer id);

	public ILiveLiveReport save(ILiveLiveReport bean);

	public ILiveLiveReport deleteById(Integer id);

	public void update(ILiveLiveReport bean);
	
	public ILiveLiveReport updateByUpdater(Updater<ILiveLiveReport> updater);

}