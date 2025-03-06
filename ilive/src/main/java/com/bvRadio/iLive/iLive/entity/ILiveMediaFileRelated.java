package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveMediaFileRelated;

/**
 * 
 * @author ysf
 */
public class ILiveMediaFileRelated extends BaseILiveMediaFileRelated {
	private ILiveMediaFile relatedMediaFile;

	public ILiveMediaFile getRelatedMediaFile() {
		return relatedMediaFile;
	}

	public void setRelatedMediaFile(ILiveMediaFile relatedMediaFile) {
		this.relatedMediaFile = relatedMediaFile;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getOrderNum()) {
			setOrderNum((double)System.currentTimeMillis());
		}
	}

}
