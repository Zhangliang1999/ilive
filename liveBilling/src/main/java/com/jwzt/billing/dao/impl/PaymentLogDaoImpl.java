package com.jwzt.billing.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;


import com.jwzt.billing.dao.PaymentLogDao;
import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.billing.utils.StringPatternUtil;
import com.jwzt.common.hibernate3.BaseHibernateDao;
import com.jwzt.common.hibernate3.Finder;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Repository
public class PaymentLogDaoImpl extends BaseHibernateDao<PaymentLog, Long> implements PaymentLogDao {

	@Override
	public Pagination pageByParams(String queryContent,Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, int pageNo, int pageSize) {
		Finder finder = createFinderByParams(queryContent,enterpriseId, paymentType, status, channelType, startTime, endTime);
		return find(finder, pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentLog> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime) {
		Finder finder = createFinderByParams(null,enterpriseId, paymentType, status, channelType, startTime, endTime);
		return find(finder);
	}

	@Override
	public PaymentLog getBeanById(Long id) {
		PaymentLog bean = null;
		if (null != id) {
			bean = super.get(id);
		}
		return bean;
	}

	@Override
	public PaymentLog save(PaymentLog bean) {
		if (null != bean) {
			getSession().save(bean);
		}
		return bean;
	}

	@Override
	public PaymentLog update(PaymentLog bean) {
		if (null != bean) {
			getSession().update(bean);
		}
		return bean;
	}

	@Override
	protected Class<PaymentLog> getEntityClass() {
		return PaymentLog.class;
	}

	private Finder createFinderByParams(String queryContent,Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime) {
		Finder finder = Finder.create("select bean from PaymentLog bean where 1=1");
		if (null != enterpriseId) {
			finder.append(" and bean.enterpriseId = :enterpriseId");
			finder.setParam("enterpriseId", enterpriseId);
		}
		if (null != paymentType) {
			finder.append(" and bean.paymentType = :paymentType");
			finder.setParam("paymentType", paymentType);
		}
		if (null != status) {
			finder.append(" and bean.status = :status");
			finder.setParam("status", status);
		}
		if (null != channelType) {
			finder.append(" and bean.channelType = :channelType");
			finder.setParam("channelType", channelType);
		}
		if (null != startTime) {
			finder.append(" and bean.createTime >= :startTime");
			finder.setParam("startTime", startTime);
		}
		if (null != endTime) {
			finder.append(" and bean.createTime <= :endTime");
			finder.setParam("endTime", endTime);
		}
		if(null !=queryContent){
			boolean queryContentInt = StringPatternUtil.isInteger(queryContent);
			if(queryContentInt){
				finder.append(" and (bean.id = :queryContentid or bean.enterpriseId in(select enterpriseId from EnterpriseBilling where enterpriseName like :queryContent))");
				finder.setParam("queryContentid", Long.parseLong(queryContent));
				finder.setParam("queryContent", "%"+queryContent+"%");
			}else{
				finder.append(" and  bean.enterpriseId in(select enterpriseId from EnterpriseBilling where enterpriseName like :queryContent)");
				finder.setParam("queryContent", "%"+queryContent+"%");
			}
		}
		finder.append(" order by bean.createTime desc");
		return finder;
	}

	@Override
	public List<PaymentLog> listByParamsByAuto(Integer paymentAuto) {
		Finder finder = Finder.create("select bean from PaymentLog bean where 1=1");
		finder.append(" and bean.paymentAuto = :paymentAuto");
		finder.setParam("paymentAuto", paymentAuto);
		finder.append(" order by bean.createTime desc");
		return find(finder);
	}

}
