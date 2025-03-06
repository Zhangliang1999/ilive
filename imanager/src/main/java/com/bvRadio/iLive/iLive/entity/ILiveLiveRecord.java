package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveLiveRecord;

public class ILiveLiveRecord extends BaseILiveLiveRecord{

	public ILiveLiveRecord() {
		super();
	}
	public ILiveLiveRecord(Integer id, Integer userId, Integer roomId,
			Timestamp beginTime, Timestamp endTime, Integer income,
			Integer totalNumber){
		super(id,userId,roomId,beginTime,endTime,income,totalNumber);
	};
	
}
