package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSVoteLog;

@SuppressWarnings("serial")
public class BBSVoteLog extends BaseBBSVoteLog implements java.io.Serializable {

	private BBSVoteItem bbsVoteItem;
	
	public BBSVoteItem getBbsVoteItem() {
		return bbsVoteItem;
	}

	public void setBbsVoteItem(BBSVoteItem bbsVoteItem) {
		this.bbsVoteItem = bbsVoteItem;
	}

	public BBSVoteLog() {
		super();

	}

	public BBSVoteLog(Integer id) {
		super(id);

	}

}
