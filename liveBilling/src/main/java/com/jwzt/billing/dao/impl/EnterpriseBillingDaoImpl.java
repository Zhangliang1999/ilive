package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.EnterpriseBillingDao;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class EnterpriseBillingDaoImpl extends BaseHibernateDao<EnterpriseBilling, Integer>
		implements EnterpriseBillingDao {

	@Override
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			int pageNo, int pageSize) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseBilling> listByParams(Integer enterpriseId, String enterpriseName, Date startTime,
			Date endTime) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, startTime, endTime);
		List<EnterpriseBilling> list = find(finder);
		return list;
	}

	private Finder createFinderByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from EnterpriseBilling bean where 1=1");
		finder.append(" and (1=-1");
		if (null != enterpriseId) {
			finder.append(" or bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (StringUtils.isNotBlank(enterpriseName)) {
			finder.append(" or bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%" + enterpriseName + "%");
		}
		finder.append(" )");
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc, bean.updateTime desc, bean.enterpriseId desc");
		return finder;
	}

	@Override
	public EnterpriseBilling sumTotal() {
		Finder finder = Finder
				.create("select" + " sum(bean.settleAmount) as settleAmount," + " sum(bean.totalAmount) as totalAmount,"
						+ " sum(bean.platformAmount) as platformAmount" + " from EnterpriseBilling bean where 1=1");
		Query query = finder.createQuery(getSession());
		query.setResultTransformer(Transformers.aliasToBean(EnterpriseBilling.class));
		return (EnterpriseBilling) query.uniqueResult();
	}

	@Override
	public EnterpriseBilling getBeanById(Integer id) {
		EnterpriseBilling bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public EnterpriseBilling save(EnterpriseBilling bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<EnterpriseBilling> getEntityClass() {
		return EnterpriseBilling.class;
	}

}
