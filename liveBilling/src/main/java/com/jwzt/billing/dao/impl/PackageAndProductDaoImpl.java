package com.jwzt.billing.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.PackageAndProductDao;
import com.jwzt.billing.entity.PackageAndProduct;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;

/**
 * @author ysf
 */
@Repository
public class PackageAndProductDaoImpl extends BaseHibernateDao<PackageAndProduct, String>
		implements PackageAndProductDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PackageAndProduct> listByParams(Integer packageId, Integer productId) {
		Finder finder = createFinder(packageId, productId);
		return find(finder);
	}

	private Finder createFinder(Integer packageId, Integer productId) {
		Finder finder = Finder.create("select bean from PackageAndProduct bean where 1=1");
		if (null != packageId) {
			finder.append(" and bean.packageId = :packageId");
			finder.setParam("packageId", packageId);
		}
		if (null != productId) {
			finder.append(" and bean.productId = :productId");
			finder.setParam("productId", productId);
		}
		finder.append(" order by bean.productId asc, bean.createTime asc");
		return finder;
	}

	@Override
	public PackageAndProduct save(PackageAndProduct bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<PackageAndProduct> getEntityClass() {
		return PackageAndProduct.class;
	}

}
