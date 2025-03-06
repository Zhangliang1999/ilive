package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.InviteCard;

public interface InviteCardDao {

	void save(InviteCard inviteCard);
	
	void update(InviteCard inviteCard);
	
	InviteCard getByroomId(Integer roomId);
}
