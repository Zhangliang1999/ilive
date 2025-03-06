package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveViewRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveViewRecord;

public class ILiveViewRecordDaoImpl extends HibernateBaseDao<ILiveViewRecord, Long> implements ILiveViewRecordDao {

	@Override
	protected Class<ILiveViewRecord> getEntityClass() {
		return ILiveViewRecord.class;
	}

	@Override
	public boolean addILiveViewRecord(ILiveViewRecord viewRecord) {
		this.getSession().save(viewRecord);
		return true;
	}

	@Override
	public Pagination getILiveViewRecords(Map<String, Object> sqlMap) {
		String hql =  "from ILiveViewRecord where managerId=:managerId and deleteStatus=0 order by recordTime desc ";
		Finder finder = Finder.create(hql);
		finder.setParam("managerId", sqlMap.get("userId"));
		Pagination find = this.find(finder, (Integer)sqlMap.get("pageNo"), (Integer)sqlMap.get("pageSize"));
		return find;
	}

	@Override
	public boolean updateILiveViewRecord(ILiveViewRecord view) {
		this.getSession().update(view);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveViewRecord getIliveViewByViewVo(ILiveViewRecord viewRecord) {
		List<ILiveViewRecord> find = this.find("from ILiveViewRecord where managerId=? and viewType=? and outerId=?", viewRecord.getManagerId(),viewRecord.getViewType(),viewRecord.getOuterId());
		if(find!=null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
		// return (ILiveViewRecord) this.findUnique();
	}

	@Override
	public ILiveViewRecord getLiveViewRecordById(Long recordId) {
		return this.get(recordId);
	}
	
	
	

}
