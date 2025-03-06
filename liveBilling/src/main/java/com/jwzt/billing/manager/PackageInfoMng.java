package com.jwzt.billing.manager;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface PackageInfoMng {
	Pagination pageByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean initBean, boolean orderByNum);

	List<PackageInfo> listByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, boolean initBean, boolean orderByNum);

	PackageInfo getBeanById(Integer id, boolean initBean);

	PackageInfo save(PackageInfo bean, Integer[] productIds);

	PackageInfo update(PackageInfo bean);
}
