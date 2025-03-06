package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.iLive.action.front.vo.AppViewRecordVo;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;

public interface ILiveViewRecordMng {
	
	/**
	 * 新增一条观看记录
	 */
	public boolean addILiveViewRecord(ILiveViewRecord viewRecord);
	
	/**
	 * 分页获取观看记录
	 * @param sqlMap
	 * @return
	 */
	public List<AppViewRecordVo> getILiveViewRecords(Map<String,Object> sqlMap);

	public ILiveViewRecord getLiveViewRecordById(Long recordId);

	public void updateLiveRecord(ILiveViewRecord liveViewRecord);

}
