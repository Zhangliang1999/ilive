package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.billing.dao.PackageInfoDao;
import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class PackageInfoDaoImpl extends BaseHibernateDao<PackageInfo, Integer> implements PackageInfoDao {

	@Override
	public Pagination pageByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean orderByNum) {
		Finder finder = createFinderByParams(packageName, packageTypes, status, channelTypes, startTime, endTime,
				orderByNum);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PackageInfo> listByParams(String packageName, Integer[] packageTypes, Integer status,
			String channelTypes, Date startTime, Date endTime, boolean orderByNum) {
		Finder finder = createFinderByParams(packageName, packageTypes, status, channelTypes, startTime, endTime,
				orderByNum);
		return find(finder);
	}

	@Override
	public PackageInfo getBeanById(Integer id) {
		PackageInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public PackageInfo save(PackageInfo bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	protected Class<PackageInfo> getEntityClass() {
		return PackageInfo.class;
	}

	private Finder createFinderByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, boolean orderByNum) {
		Finder finder = Finder.create("select bean from PackageInfo bean where 1=1");
		if (StringUtils.isNotBlank(packageName)) {
			finder.append(" and bean.packageName = :packageName");
			finder.setParam("packageName", packageName);
		}
		if (null != packageTypes && packageTypes.length > 0) {
			finder.append(" and bean.packageType in (:packageType)");
			finder.setParamList("packageType", packageTypes);
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (StringUtils.isNotBlank(channelTypes)) {
			finder.append(" and bean.channelTypes like :channelTypes");
			finder.setParam("channelTypes", "%" + channelTypes + "%");
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if (orderByNum) {
			finder.append(" order by bean.orderNum asc, bean.createTime desc");
		} else {
			finder.append(" order by bean.createTime desc");
		}
		return finder;
	}

}
