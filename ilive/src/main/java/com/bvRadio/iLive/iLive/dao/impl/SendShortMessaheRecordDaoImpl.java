package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.SendShortMessaheRecordDao;
import com.bvRadio.iLive.iLive.entity.SendShortMessaheRecord;

@Repository
public class SendShortMessaheRecordDaoImpl extends HibernateBaseDao<SendShortMessaheRecord, Long> implements SendShortMessaheRecordDao{

	@Override
	public List<SendShortMessaheRecord> getListByRecordId(Long recordId,String mobile) {
		StringBuilder hql = new StringBuilder("from SendShortMessaheRecord where recordId = :recordId");
		if(mobile!=null&&!"".equals(mobile)) {
			hql.append(" and receiveUserPhone like :mobile");
		}
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("recordId", recordId);
		if(mobile!=null&&!"".equals(mobile)) {
			query.setParameter("mobile", "%"+mobile+"%");
		}
		@SuppressWarnings("unchecked")
		List<SendShortMessaheRecord> list = query.list();
		return list;
	}

	@Override
	public void save(SendShortMessaheRecord sendShortMessaheRecord) {
		this.getSession().save(sendShortMessaheRecord);
	}

	@Override
	protected Class<SendShortMessaheRecord> getEntityClass() {
		return SendShortMessaheRecord.class;
	}

	@Override
	public Integer getNumberByEnterpriseId(Integer enterpriseId) {
		String hql = "from SendShortMessaheRecord where enterpriseId = :enterpriseId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("enterpriseId", enterpriseId);
		List<SendShortMessaheRecord> list = query.list();
		Integer number =  list.size();
		return number;
	}

	@Override
	public List<SendShortMessaheRecord> getListByRecord(Long id, String mobile, String start, String stop) {
		StringBuilder hql = new StringBuilder("from SendShortMessaheRecord where 1=1 ");
		Query query = this.getSession().createQuery(hql.toString());
		if(id!=null&&!"".equals(id)) {
			hql.append(" and recordId = :recordId");
			query.setParameter("recordId", id);
		}
		if(mobile!=null&&!"".equals(mobile)) {
			hql.append(" and receiveUserPhone like :mobile");
			query.setParameter("mobile", "%"+mobile+"%");
		}
		if(start!=null&&!"".equals(start)) {
			hql.append(" and createTime > :start");
			query.setParameter("start", start);
		}
		if(stop!=null&&!"".equals(stop)) {
			hql.append(" and createTime < :stop");
			query.setParameter("stop", stop);
		}
		List<SendShortMessaheRecord> list = query.list();
		return list;
	}

}
