package com.bvRadio.iLive.iLive.manager;

import java.sql.Timestamp;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveLiveReport;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;

public interface ILiveLiveReportMng {
	public Pagination getPage(Integer id, String reporter, String reported,
			String content, Integer statu, Timestamp submitTime,
			Timestamp dealTime,int pageNo, int pageSize);

	public ILiveLiveReport findById(Integer id);

	public ILiveLiveReport save(ILiveLiveReport bean);

	public ILiveLiveReport deleteById(Integer id);

	public ILiveLiveReport update(ILiveLiveReport bean);
	
}
