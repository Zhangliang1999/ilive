package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.ProductInfoDao;
import com.jwzt.billing.entity.ProductInfo;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class ProductInfoDaoImpl extends BaseHibernateDao<ProductInfo, Integer> implements ProductInfoDao {

	@Override
	public Pagination pageByParams(String productName, Integer[] productTypes, Date startTime, Date endTime, int pageNo,
			int pageSize) {
		Finder finder = createFinderByParams(productName, productTypes, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductInfo> listByParams(String productName, Integer[] productTypes, Date startTime, Date endTime) {
		Finder finder = createFinderByParams(productName, productTypes, startTime, endTime);
		return find(finder);
	}

	@Override
	public ProductInfo getBeanById(Integer id) {
		ProductInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public ProductInfo save(ProductInfo bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<ProductInfo> getEntityClass() {
		return ProductInfo.class;
	}

	private Finder createFinderByParams(String productName, Integer[] productTypes, Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from ProductInfo bean where 1=1");
		if (StringUtils.isNotBlank(productName)) {
			finder.append(" and bean.productName like :productName");
			finder.setParam("productName", "%" + productName + "%");
		}
		if (null != productTypes && productTypes.length > 0) {
			finder.append(" and bean.productType in (:productTypes)");
			finder.setParamList("productTypes", productTypes);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		finder.append(" order by bean.createTime desc");
		return finder;
	}

}
