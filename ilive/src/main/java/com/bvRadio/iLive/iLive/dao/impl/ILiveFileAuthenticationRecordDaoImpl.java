package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.iLive.dao.ILiveFileAuthenticationRecordDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileAuthenticationRecord;

public class ILiveFileAuthenticationRecordDaoImpl extends HibernateBaseDao<ILiveFileAuthenticationRecord, Long>
		implements ILiveFileAuthenticationRecordDao {

	@Override
	protected Class<ILiveFileAuthenticationRecord> getEntityClass() {
		return ILiveFileAuthenticationRecord.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILiveFileAuthenticationRecord getILiveFileViewAuthBill(long useId, Long fileId) {
		Finder finder = Finder.create(
				"from ILiveFileAuthenticationRecord where userId=:userId and fileId=:fileId and deleteStatus=0");
		finder.setParam("userId", useId);
		finder.setParam("fileId", fileId);
		List<ILiveFileAuthenticationRecord> find = this.find(finder);
		if (find != null && !find.isEmpty()) {
			return find.get(0);
		}
		return null;
	}

	@Override
	public void addILiveViewAuthBill(ILiveFileAuthenticationRecord fileAuthRecord) {
		fileAuthRecord.setDeleteStatus(0);
		this.getSession().save(fileAuthRecord);
	}

	@Override
	public boolean deleteFileAuthenticationRecordByFileId(Long fileId) {
		String hql = " update ILiveFileAuthenticationRecord set deleteStatus=1 where fileId=:fileId";
		this.getSession().createQuery(hql).setParameter("fileId", fileId).executeUpdate();
		return true;
	}

}
