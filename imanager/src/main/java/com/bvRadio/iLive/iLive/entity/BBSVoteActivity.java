package com.bvRadio.iLive.iLive.entity;

import java.util.Set;

import com.bvRadio.iLive.iLive.entity.base.BaseBBSVoteActivity;

@SuppressWarnings("serial")
public class BBSVoteActivity extends BaseBBSVoteActivity implements java.io.Serializable {
	
	private ILiveLiveRoom iLiveLiveRoom;
	
	private Set<BBSVoteItem> bbsVoteItems;
	

	public Set<BBSVoteItem> getBbsVoteItems() {
		return bbsVoteItems;
	}

	public void setBbsVoteItems(Set<BBSVoteItem> bbsVoteItems) {
		this.bbsVoteItems = bbsVoteItems;
	}

	public ILiveLiveRoom getiLiveLiveRoom() {
		return iLiveLiveRoom;
	}

	public void setiLiveLiveRoom(ILiveLiveRoom iLiveLiveRoom) {
		this.iLiveLiveRoom = iLiveLiveRoom;
	}
 
}
