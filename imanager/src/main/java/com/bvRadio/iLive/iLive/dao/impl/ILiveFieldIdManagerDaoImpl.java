package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.bvRadio.iLive.iLive.dao.ILiveFieldIdManagerDao;
import com.bvRadio.iLive.iLive.entity.ILiveFieldIdManager;
import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;

@Repository
public class ILiveFieldIdManagerDaoImpl extends HibernateBaseDao<ILiveFieldIdManager, Integer> implements ILiveFieldIdManagerDao {

	@SuppressWarnings("unchecked")
	public Long getNextId(String tableName, String fieldName, Integer step) {
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
		List<ILiveFieldIdManager> iLiveFieldIdManagerList = find(f);
		ILiveFieldIdManager iLiveFieldIdManager = null;
		if (iLiveFieldIdManagerList.size() > 0) {
			iLiveFieldIdManager = iLiveFieldIdManagerList.get(0);
		}
		Long nextId = 0L;
		if (null != iLiveFieldIdManager) {
			nextId = iLiveFieldIdManager.getNextId();
			iLiveFieldIdManager.setNextId(nextId + step);
			getSession().update(iLiveFieldIdManager);
		} else {
			log.error("获取键值失败，tableName = {} ，fieldName = {} ", tableName, fieldName);
			return -1L;
		}
		return nextId + step - 1;
	}

	@Override
	protected Class<ILiveFieldIdManager> getEntityClass() {
		return ILiveFieldIdManager.class;
	}

}