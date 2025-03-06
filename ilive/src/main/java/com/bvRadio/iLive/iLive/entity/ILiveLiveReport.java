package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveLiveReport;

public class ILiveLiveReport extends BaseILiveLiveReport {
	
	/**
	 */
	private static final long serialVersionUID = 2805440436102392085L;
	
	public void initFieldValue() {
		if(null==getSubmitTime()){
			setSubmitTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public ILiveLiveReport() {
		super();
	}
	public ILiveLiveReport(Integer id, String reporter, String reported,
			String content, Integer statu, Timestamp submitTime,
			Timestamp dealTime){
		super(id,reporter,reported,content,statu,submitTime,dealTime);
	};
	
}
