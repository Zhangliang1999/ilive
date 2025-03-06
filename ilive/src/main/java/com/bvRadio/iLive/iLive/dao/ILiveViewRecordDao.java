package com.bvRadio.iLive.iLive.dao;

import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;

public interface ILiveViewRecordDao {
	
	/**
	 * 新增一条观看记录
	 */
	public boolean addILiveViewRecord(ILiveViewRecord viewRecord);
	
	/**
	 * 分页获取观看记录
	 * @param sqlMap
	 * @return
	 */
	public Pagination getILiveViewRecords(Map<String,Object> sqlMap);

	public boolean updateILiveViewRecord(ILiveViewRecord view);

	/**
	 * 获取对象
	 * @param viewRecord
	 * @return
	 */
	public ILiveViewRecord getIliveViewByViewVo(ILiveViewRecord viewRecord);

	public ILiveViewRecord getLiveViewRecordById(Long recordId);

}
