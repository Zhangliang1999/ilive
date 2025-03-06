package com.bvRadio.iLive.iLive.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveMessagePraiseDao;
import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
import com.bvRadio.iLive.iLive.manager.ILiveMessagePraiseMng;
@Service
public class ILiveMessagePraiseMngImpl implements ILiveMessagePraiseMng {
	
	@Autowired
	private ILiveMessagePraiseDao iLiveMessagePraiseDao;//
	
	@Override
	public List<ILiveMessagePraise> selectILiveMessagePraisByMsgId(Long msgId) {
		List<ILiveMessagePraise> praises = new ArrayList<ILiveMessagePraise>();
		try {
			praises = iLiveMessagePraiseDao.selectILiveMessagePraisByMsgId(msgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return praises;
	}

	@Override
	@Transactional
	public void addILiveMessagePraise(ILiveMessagePraise iLiveMessagePraise) {
		try {
			iLiveMessagePraiseDao.insertILiveMessagePraise(iLiveMessagePraise);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILiveMessagePraise selectILiveMessagePraise(Long msgId, Long userId) {
		ILiveMessagePraise iLiveMessagePraise = new ILiveMessagePraise();
		try {
			iLiveMessagePraise = iLiveMessagePraiseDao.selectILiveMessagePraise(msgId, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iLiveMessagePraise;
	}
}
