package com.bvRadio.iLive.iLive.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.InviteCardDao;
import com.bvRadio.iLive.iLive.entity.InviteCard;

@Repository
public class InviteCardDaoImpl extends HibernateBaseDao<InviteCard, Integer> implements InviteCardDao{

	@Override
	public void save(InviteCard inviteCard) {
		this.getSession().save(inviteCard);
	}

	@Override
	public void update(InviteCard inviteCard) {
		this.getSession().update(inviteCard);
	}

	@Override
	protected Class<InviteCard> getEntityClass() {
		return InviteCard.class;
	}

	@Override
	public InviteCard getByroomId(Integer roomId) {
		return this.get(roomId);
	}

}
