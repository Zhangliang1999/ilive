package com.jwzt.common.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.jwzt.common.dao.FieldIdManagerDao;
import com.jwzt.common.entity.FieldIdManager;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;

@Repository
public class FieldIdManagerDaoImpl extends BaseHibernateDao<FieldIdManager, Integer> implements FieldIdManagerDao {
	@Override
	@SuppressWarnings("unchecked")
	public synchronized Long getNextId(String tableName, String fieldName, Long step) {
		Long ret = -1L;
		Session openSession = null;
		Transaction transaction = null;
		try {
			openSession = openSession();
			transaction = openSession.beginTransaction();
			Finder f = Finder.create("select bean from FieldIdManager bean");
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
			List<FieldIdManager> fieldIdManagerList = query.list();
			FieldIdManager fieldIdManager = null;
			if (fieldIdManagerList.size() > 0) {
				fieldIdManager = fieldIdManagerList.get(0);
			}
			Long nextId = 0L;
			if (null != fieldIdManager) {
				nextId = fieldIdManager.getNextId();
				fieldIdManager.setNextId(nextId + step);
				openSession.update(fieldIdManager);
				ret = nextId + step - 1;
			} else {
				fieldIdManager = new FieldIdManager(getNextManagerId(openSession), tableName, fieldName);
				nextId = 100L;
				fieldIdManager.setNextId(nextId + step);
				openSession.save(fieldIdManager);
				ret = nextId + step - 1;
			}
			transaction.commit();
		} catch (Exception e) {
			if (null != transaction) {
				transaction.rollback();
			}
		} finally {
			if (null != openSession) {
				openSession.close();
			}
		}
		return ret;

	}

	@SuppressWarnings("unchecked")
	private Integer getNextManagerId(Session openSession) {
		Finder f = Finder.create("select bean from FieldIdManager bean");
		f.append(" order by bean.id desc");
		Query query = f.createQuery(openSession);
		List<FieldIdManager> fieldIdManagerList = query.list();
		FieldIdManager fieldIdManager = null;
		if (fieldIdManagerList.size() > 0) {
			fieldIdManager = fieldIdManagerList.get(0);
		}
		Integer nextManagerId = 0;
		if (null != fieldIdManager) {
			nextManagerId = fieldIdManager.getId();
		}
		return nextManagerId + 1;
	}

	@Override
	protected Class<FieldIdManager> getEntityClass() {
		return FieldIdManager.class;
	}

}