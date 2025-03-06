package com.jwzt.billing.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
public interface PackageInfoDao {
	Pagination pageByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean orderByNum);

	List<PackageInfo> listByParams(String packageName, Integer[] packageTypes, Integer status, String channelTypes,
			Date startTime, Date endTime, boolean orderByNum);

	PackageInfo getBeanById(Integer id);

	PackageInfo save(PackageInfo bean);

	PackageInfo updateByUpdater(Updater<PackageInfo> updater);
}
