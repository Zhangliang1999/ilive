package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveUser;

public class ILiveUserEntity extends BaseILiveUser {

	public ILiveUserEntity() {
		super();
	}

	public void init() {
		if (super.getDisabled() == null) {
			setDisabled(false);
		}
	}

}
