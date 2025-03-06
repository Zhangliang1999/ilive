package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSVoteItem;

public class BBSVoteItem extends BaseBBSVoteItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BBSVoteActivity bbsVoteActivity;
	
	public BBSVoteActivity getBbsVoteActivity() {
		return bbsVoteActivity;
	}


	public void setBbsVoteActivity(BBSVoteActivity bbsVoteActivity) {
		this.bbsVoteActivity = bbsVoteActivity;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public BBSVoteItem() {
		super();
		
	}


	public BBSVoteItem(Integer itemId) {
		super(itemId);
		
	}

}