package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.iLive.entity.InviteCard;

public interface InviteCardMng {

	void save(InviteCard inviteCard);
	
	void update(InviteCard inviteCard);
	
	InviteCard getByroomId(Integer roomId);
}
