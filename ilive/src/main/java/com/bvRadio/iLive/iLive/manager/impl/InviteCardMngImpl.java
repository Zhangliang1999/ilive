package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.InviteCardDao;
import com.bvRadio.iLive.iLive.entity.InviteCard;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.InviteCardMng;

@Service
@Transactional
public class InviteCardMngImpl implements InviteCardMng {

	@Autowired
	private InviteCardDao inviteCardDao;
	
	
	
	@Override
	public void save(InviteCard inviteCard) {
		inviteCardDao.save(inviteCard);
	}

	@Override
	public void update(InviteCard inviteCard) {
		inviteCardDao.update(inviteCard);
	}

	@Override
	public InviteCard getByroomId(Integer roomId) {
		return inviteCardDao.getByroomId(roomId);
	}

}
