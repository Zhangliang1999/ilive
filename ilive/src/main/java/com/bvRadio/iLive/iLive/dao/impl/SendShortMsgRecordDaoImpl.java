package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.SendShortMessaheRecordDao;
import com.bvRadio.iLive.iLive.dao.SendShortMsgRecordDao;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;
import com.bvRadio.iLive.iLive.entity.SendShortMsgRecode;

@Repository
public class SendShortMsgRecordDaoImpl extends HibernateBaseDao<SendShortMsgRecode, Long> implements SendShortMsgRecordDao{

	@Override
	public List<SendShortMsgRecode> getListByMobile(String mobile) {
		StringBuilder hql = new StringBuilder("from SendShortMsgRecode where 1=1");
		if(mobile!=null&&!"".equals(mobile)) {
			hql.append(" and mobile = :mobile");
		}
		Query query = this.getSession().createQuery(hql.toString());
		@SuppressWarnings("unchecked")
		List<SendShortMsgRecode> list = query.list();
		return list;
	}

	@Override
	public void save(SendShortMsgRecode sendShortMsgRecode) {
		this.getSession().save(sendShortMsgRecode);
	}

	@Override
	protected Class<SendShortMsgRecode> getEntityClass() {
		return SendShortMsgRecode.class;
	}

	@Override
	public void update(SendShortMsgRecode sendShortMsgRecode) {
		// TODO Auto-generated method stub
		this.getSession().update(sendShortMsgRecode);
	}

	

}
