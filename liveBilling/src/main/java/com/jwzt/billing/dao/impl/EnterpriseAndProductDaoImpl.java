package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.EnterpriseAndProductDao;
import com.jwzt.billing.entity.EnterpriseAndProduct;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;

/**
 * @author ysf
 */
@Repository
public class EnterpriseAndProductDaoImpl extends BaseHibernateDao<EnterpriseAndProduct, String>
		implements EnterpriseAndProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseAndProduct> listByParams(Integer enterpriseId, Integer productId, Date vaildCheckTime) {
		Finder finder = createFinder(enterpriseId, productId, vaildCheckTime);
		return find(finder);
	}

	private Finder createFinder(Integer enterpriseId, Integer productId, Date vaildCheckTime) {
		Finder finder = Finder.create("select bean from EnterpriseAndProduct bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != productId) {
			finder.append(" and bean.productId = :productId");
			finder.setParam("productId", productId);
		}
		if (null != vaildCheckTime) {
			finder.append(" and bean.vaildStartTime <= :vaildCheckTime");
			finder.append(" and bean.vaildEndTime >= :vaildCheckTime");
			finder.setParam("vaildCheckTime", vaildCheckTime);
		}
		finder.append(" order by bean.productId asc, bean.createTime asc");
		return finder;
	}

	@Override
	public EnterpriseAndProduct save(EnterpriseAndProduct bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	public EnterpriseAndProduct update(EnterpriseAndProduct bean) {
		if (null != bean) {
			getSession().update(bean);
		}
		return bean;
	}

	@Override
	protected Class<EnterpriseAndProduct> getEntityClass() {
		return EnterpriseAndProduct.class;
	}

}
