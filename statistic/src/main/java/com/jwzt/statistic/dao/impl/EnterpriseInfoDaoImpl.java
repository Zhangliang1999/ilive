package com.jwzt.statistic.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.dao.EnterpriseInfoDao;
import com.jwzt.statistic.entity.EnterpriseInfo;

@Repository
public class EnterpriseInfoDaoImpl extends BaseHibernateDao<EnterpriseInfo, Integer> implements EnterpriseInfoDao {
	@Override
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			int pageNo, int pageSize,Integer certStatus, Integer entype, String bindPhone,Integer stamp) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, startTime, endTime,certStatus,entype,bindPhone,stamp);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnterpriseInfo> listByParams(Integer enterpriseId, String enterpriseName, Date startTime,
			Date endTime) {
		Finder finder = createFinderByParams(enterpriseId, enterpriseName, startTime, endTime,null,null,null,null);
		return find(finder);
	}

	private Finder createFinderByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			Integer certStatus, Integer entype, String bindPhone,Integer stamp) {
		Finder finder = Finder.create("select bean from EnterpriseInfo bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.id = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (StringUtils.isNotBlank(enterpriseName)) {
			finder.append(" and bean.enterpriseName like :enterpriseName");
			finder.setParam("enterpriseName", "%" + enterpriseName + "%");
		}
		if (null != certStatus && -1 != certStatus ) {
			if(certStatus == 4){
				finder.append(" and bean.certStatus = :certStatus");
			}else{
				finder.append(" and bean.certStatus != :certStatus");
			}
			finder.setParam("certStatus", certStatus);
		}
		if (null != entype && -1 != entype ) {
			finder.append(" and bean.entype = :entype");
			finder.setParam("entype", entype);
		}
		if (StringUtils.isNotBlank(bindPhone)) {
			finder.append(" and bean.bindPhone like :bindPhone");
			finder.setParam("bindPhone", "%" + bindPhone + "%");
		}
		if (null != stamp && -1 != stamp ) {
			finder.append(" and bean.stamp = :stamp");
			finder.setParam("stamp", stamp);
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

	@Override
	public EnterpriseInfo getBeanById(Integer id) {
		EnterpriseInfo bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public EnterpriseInfo save(EnterpriseInfo bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<EnterpriseInfo> getEntityClass() {
		return EnterpriseInfo.class;
	}

}