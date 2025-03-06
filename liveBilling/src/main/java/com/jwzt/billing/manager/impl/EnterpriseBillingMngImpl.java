package com.jwzt.billing.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.dao.EnterpriseBillingDao;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.manager.RevenueFlowManager;
import com.jwzt.common.hibernate3.Updater;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class EnterpriseBillingMngImpl implements EnterpriseBillingMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(Integer enterpriseId, String enterpriseName, Date startTime, Date endTime,
			int pageNo, int pageSize, boolean initBean) {
		Pagination page = dao.pageByParams(enterpriseId, enterpriseName, startTime, endTime, pageNo, pageSize);
		if (null != page && initBean) {
			List<EnterpriseBilling> list = (List<EnterpriseBilling>) page.getList();
			if (null != list) {
				for (EnterpriseBilling bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EnterpriseBilling> listByParams(Integer enterpriseId, String enterpriseName, Date startTime,
			Date endTime, boolean initBean) {
		List<EnterpriseBilling> list = dao.listByParams(enterpriseId, enterpriseName, startTime, endTime);
		if (null != list && initBean) {
			for (EnterpriseBilling bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseBilling sumTotal() {
		EnterpriseBilling bean = dao.sumTotal();
		initBean(bean);
		return bean;
	}

	@Override
	@Transactional(readOnly = true)
	public EnterpriseBilling getBeanById(Integer id, boolean initBean) {
		EnterpriseBilling bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseBilling save(EnterpriseBilling bean) {
		if (null != bean) {
			bean.initFieldValue();
			bean = dao.save(bean);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseBilling update(Integer enterpriseId,String enterpriseName, Double settleAmount, Double totalAmount,
			Double platformAmount,Timestamp applyTime,Timestamp certTime, String userPhone) {
		EnterpriseBilling bean = null;
		if (null != enterpriseId) {
			bean = new EnterpriseBilling();
			bean.setEnterpriseId(enterpriseId);
			bean.setEnterpriseName(enterpriseName);
			bean.setSettleAmount(settleAmount);
			bean.setTotalAmount(totalAmount);
			bean.setPlatformAmount(platformAmount);
			bean.setCertTime(certTime);
			bean.setApplyTime(applyTime);
			bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			bean.setUserPhone(userPhone);
			Updater<EnterpriseBilling> updater = new Updater<EnterpriseBilling>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseBilling updateRevenueAccount(Integer enterpriseId, Boolean openRevenueAccount,
			Long revenueAccountWorkOrderId) {
		EnterpriseBilling bean = null;
		if (null != enterpriseId) {
			bean = new EnterpriseBilling();
			bean.setEnterpriseId(enterpriseId);
			bean.setOpenRevenueAccount(openRevenueAccount);
			bean.setRevenueAccountWorkOrderId(revenueAccountWorkOrderId);
			Updater<EnterpriseBilling> updater = new Updater<EnterpriseBilling>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseBilling updateRedPackageAccount(Integer enterpriseId, Boolean openRedPackageAccount,
			Long redPackageAccountWorkOrderId) {
		EnterpriseBilling bean = null;
		if (null != enterpriseId) {
			bean = new EnterpriseBilling();
			bean.setEnterpriseId(enterpriseId);
			bean.setOpenRedPackageAccount(openRedPackageAccount);
			bean.setRedPackageAccountWorkOrderId(redPackageAccountWorkOrderId);
			Updater<EnterpriseBilling> updater = new Updater<EnterpriseBilling>(bean);
			bean = dao.updateByUpdater(updater);
		}
		return bean;
	}

	@Override
	@Transactional
	public EnterpriseBilling saveOrUpdateFromDataMap(final Map<?, ?> dataMap) {
		String enterpriseName = (String) dataMap.get("enterpriseName");
		String enterpriseImg = (String) dataMap.get("enterpriseImg");
		Integer enterpriseId = (Integer) dataMap.get("enterpriseId");
		Integer certStatus = (Integer) dataMap.get("certStatus");
		String userPhone=(String)dataMap.get("bindPhone");
		if(userPhone==null){
			userPhone=(String)dataMap.get("contactPhone");
		}
		System.out.println("企业id："+enterpriseId+"绑定手机号："+userPhone);
		Timestamp applyTime;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			applyTime = Timestamp.valueOf(dataMap.get("applyTime").toString());
		} catch (Exception e) {
			applyTime = null;
		}
		Timestamp certTime;
		try {
			certTime = Timestamp.valueOf(dataMap.get("certTime").toString());
		} catch (Exception e) {
			certTime = null;
		}
		EnterpriseBilling enterpriseBilling = getBeanById(enterpriseId, false);
		if (null == enterpriseBilling) {
			enterpriseBilling = new EnterpriseBilling();
			enterpriseBilling.setEnterpriseId(enterpriseId);
			enterpriseBilling.setEnterpriseName(enterpriseName);
			enterpriseBilling.setEnterpriseImg(enterpriseImg);
			enterpriseBilling.setCertStatus(certStatus);
			enterpriseBilling.setApplyTime(applyTime);
			enterpriseBilling.setCertTime(certTime);
			enterpriseBilling.setUserPhone(userPhone);
			enterpriseBilling = save(enterpriseBilling);
		} else {
			enterpriseBilling = update(enterpriseId,enterpriseName, null, null, null,applyTime,certTime,userPhone);
		}
		return enterpriseBilling;
	}

	private void initBean(final EnterpriseBilling bean) {
		if (null != bean) {
			Integer enterpriseId = bean.getEnterpriseId();
			Double unsettlementAccountByEnterprise = revenueFlowManager
					.sumUnsettlementAccountByEnterprise(enterpriseId);
			bean.setUnsettleAmount(unsettlementAccountByEnterprise);
		}
	}

	@Autowired
	private EnterpriseBillingDao dao;
	@Autowired
	private RevenueFlowManager revenueFlowManager;

}
