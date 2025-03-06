package com.jwzt.common.hibernate3;

import static org.hibernate.EntityMode.POJO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jwzt.common.page.Pagination;
import com.jwzt.common.utils.MyBeanUtils;

/**
 * hibernate DAO基类
 * 
 * 提供hql分页查询，拷贝更新等一些常用功能。
 * 
 * @param <T>
 *            entity class
 * @param <ID>
 *            entity id
 */

public abstract class BaseHibernateDao<T, ID extends Serializable> {
	/**
	 * 日志，可用于子类
	 */
	protected Logger log = LogManager.getLogger(getClass());

	/**
	 * HIBERNATE 的 order 属性
	 */
	protected static final String ORDER_ENTRIES = "orderEntries";

	/**
	 * 通过HQL查询对象列表
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */

	protected List find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 通过HQL查询唯一对象
	 */
	protected Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	protected Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 通过Finder获得分页数据
	 * 
	 * @param finder
	 *            Finder对象
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @return
	 */

	protected Pagination find(Finder finder, int pageNo, int pageSize) {
		int totalCount = countQueryResult(finder);
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());
			return p;
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		List list = query.list();
		p.setList(list);
		return p;
	}

	/**
	 * 通过Finder获得列表数据
	 * 
	 * @param finder
	 * @return
	 */
	protected List find(Finder finder) {
		Query query = finder.createQuery(getSession());
		List list = query.list();
		return list;
	}

	/**
	 * 通过Criteria获得分页数据
	 * 
	 * @param crit
	 * @param pageNo
	 * @param pageSize
	 * @param projection
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Pagination findByCriteria(Criteria crit, int pageNo, int pageSize) {
		CriteriaImpl impl = (CriteriaImpl) crit;
		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) MyBeanUtils.getFieldValue(impl, ORDER_ENTRIES);
			MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, new ArrayList());
		} catch (Exception e) {
			throw new RuntimeException("cannot read/write 'orderEntries' from CriteriaImpl", e);
		}

		int totalCount = ((Number) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());
			return p;
		}

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		crit.setProjection(projection);
		if (projection == null) {
			crit.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			crit.setResultTransformer(transformer);
		}
		try {
			MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, orderEntries);
		} catch (Exception e) {
			throw new RuntimeException("set 'orderEntries' to CriteriaImpl faild", e);
		}
		crit.setFirstResult(p.getFirstResult());
		crit.setMaxResults(p.getPageSize());
		p.setList(crit.list());
		return p;
	}

	/**
	 * 获得Finder的记录总数
	 * 
	 * @param finder
	 * @return
	 */
	protected int countQueryResult(Finder finder) {
		Query query = getSession().createQuery(finder.getRowCountHql());
		finder.setParamsToQuery(query);
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		return ((Number) query.iterate().next()).intValue();
	}

	@Autowired
	protected SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	protected Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 * @see Session.get(Class,Serializable)
	 * @param id
	 * @return 持久化对象
	 */
	protected T get(ID id) {
		return get(id, false);
	}

	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id
	 *            对象ID
	 * @param lock
	 *            是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	@SuppressWarnings("unchecked")
	protected T get(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().get(getEntityClass(), id, LockMode.UPGRADE);
		} else {
			entity = (T) getSession().get(getEntityClass(), id);
		}
		return entity;
	}

	/**
	 * 按属性查找对象列表
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByProperty(String property, Object value) {
		Assert.hasText(property);
		return createCriteria(Restrictions.eq(property, value)).list();
	}

	/**
	 * 按属性查找唯一对象
	 */
	@SuppressWarnings("unchecked")
	protected T findUniqueByProperty(String property, Object value) {
		Assert.hasText(property);
		Assert.notNull(value);
		return (T) createCriteria(Restrictions.eq(property, value)).uniqueResult();
	}

	/**
	 * 按属性统计记录数
	 * 
	 * @param property
	 * @param value
	 * @return
	 */
	protected int countByProperty(String property, Object value) {
		Assert.hasText(property);
		Assert.notNull(value);
		return ((Number) (createCriteria(Restrictions.eq(property, value)).setProjection(Projections.rowCount())
				.uniqueResult())).intValue();
	}

	/**
	 * 按Criterion查询列表数据.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 */
	@SuppressWarnings("rawtypes")
	protected List findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	/**
	 * 通过Updater更新对象
	 * 
	 * @param updater
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T updateByUpdater(Updater<T> updater) {
		ClassMetadata cm = sessionFactory.getClassMetadata(getEntityClass());
		T bean = updater.getBean();
		T po = (T) getSession().get(getEntityClass(), cm.getIdentifier(bean, POJO));
		updaterCopyToPersistentObject(updater, po, cm);
		return po;
	}

	/**
	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新。
	 * 
	 * @param updater
	 * @param po
	 */
	private void updaterCopyToPersistentObject(Updater<T> updater, T po, ClassMetadata cm) {
		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		T bean = updater.getBean();
		Object value;
		for (String propName : propNames) {
			if (propName.equals(identifierName)) {
				continue;
			}
			try {
				value = MyBeanUtils.getSimpleProperty(bean, propName);
				if (!updater.isUpdate(propName, value)) {
					continue;
				}
				cm.setPropertyValue(po, propName, value, POJO);
			} catch (Exception e) {
				throw new RuntimeException("copy property to persistent object failed: '" + propName + "'", e);
			}
		}
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	protected Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(getEntityClass());
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	protected Long getOrder() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmss");
		String format = dateFormat.format(date) + "000";
		return Long.parseLong(format);
	}

	/**
	 * 获得Dao对于的实体类
	 * 
	 * @return
	 */
	abstract protected Class<T> getEntityClass();

}
