package com.bvRadio.iLive.iLive.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.entity.base.BaseILivePoint;

@SuppressWarnings("serial")
public class ILivePoint extends BaseILivePoint implements java.io.Serializable {

	private static final Logger log = LoggerFactory.getLogger(ILivePoint.class);

	public ILivePoint() {
	}

	public ILivePoint(Integer id) {
		super(id);
	}

}
