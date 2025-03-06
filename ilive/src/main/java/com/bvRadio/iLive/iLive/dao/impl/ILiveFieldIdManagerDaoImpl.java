package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveFieldIdManager;
import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;

@Repository
public class ILiveFieldIdManagerDaoImpl extends HibernateBaseDao<ILiveFieldIdManager, Integer> implements ILiveFieldIdManagerDao {

	@SuppressWarnings("unchecked")
	public synchronized Long getNextId(String tableName, String fieldName, Integer step) {
		Long ret  = -1L;
		Session openSession = null;
		Transaction transaction = null;
		try {
			openSession = openSession();
			transaction = openSession.beginTransaction();
			 Finder f = Finder.create("select bean from ILiveFieldIdManager bean");
				f.append(" where 1=1");
				if (!StringUtils.isBlank(tableName)) {
					f.append(" and bean.tableName = :tableName");
					f.setParam("tableName", tableName);
					if (!StringUtils.isBlank(fieldName)) {
						f.append(" and bean.fieldName = :fieldName");
						f.setParam("fieldName", fieldName);
					}
				}
				f.append(" order by bean.id asc");
				Query query = f.createQuery(openSession);
				List<ILiveFieldIdManager> iLiveFieldIdManagerList = query.list();;
				ILiveFieldIdManager iLiveFieldIdManager = null;
				if (iLiveFieldIdManagerList.size() > 0) {
					iLiveFieldIdManager = iLiveFieldIdManagerList.get(0);
				}
				Long nextId = 0L;
				if (null != iLiveFieldIdManager) {
					nextId = iLiveFieldIdManager.getNextId();
					iLiveFieldIdManager.setNextId(nextId + step);
					openSession.update(iLiveFieldIdManager);
					transaction.commit();
					ret =  nextId + step - 1;
				} else {
					transaction.rollback();
					log.error("获取键值失败，tableName = {} ，fieldName = {} ", tableName, fieldName);
					ret = -1L;
				}
		} catch (Exception e) {
			if(null!=transaction){
				transaction.rollback();
			}
		}finally {
			if(null!=openSession){
				openSession.close();
			}
		}
		return ret;
		
	}

	@Override
	protected Class<ILiveFieldIdManager> getEntityClass() {
		return ILiveFieldIdManager.class;
	}

}