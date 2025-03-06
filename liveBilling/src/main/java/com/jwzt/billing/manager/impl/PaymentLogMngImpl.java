package com.jwzt.billing.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwzt.billing.action.admin.WelcomeAct;
import com.jwzt.billing.dao.PaymentLogDao;
import com.jwzt.billing.entity.AgentInfo;
import com.jwzt.billing.entity.EnterpriseBilling;
import com.jwzt.billing.entity.PackageInfo;
import com.jwzt.billing.entity.PaymentLog;
import com.jwzt.billing.manager.AgentInfoMng;
import com.jwzt.billing.manager.EnterpriseAndProductMng;
import com.jwzt.billing.manager.EnterpriseBillingMng;
import com.jwzt.billing.manager.PackageInfoMng;
import com.jwzt.billing.manager.PaymentLogMng;
import com.jwzt.billing.task.IliveNotifyTask;
import com.jwzt.common.manager.FieldIdManagerMng;
import com.jwzt.common.page.Pagination;

/**
 * @author ysf
 */
@Service
public class PaymentLogMngImpl implements PaymentLogMng {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination pageByParams(String queryContent,Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, int pageNo, int pageSize, boolean initBean) {
		Pagination page = dao.pageByParams(queryContent,enterpriseId, paymentType, status, channelType, startTime, endTime, pageNo,
				pageSize);
		if (null != page && initBean) {
			List<PaymentLog> list = (List<PaymentLog>) page.getList();
			if (null != list) {
				for (PaymentLog bean : list) {
					initBean(bean);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PaymentLog> listByParams(Integer enterpriseId, Integer paymentType, Integer status, Integer channelType,
			Date startTime, Date endTime, boolean initBean) {
		List<PaymentLog> list = dao.listByParams(enterpriseId, paymentType, status, channelType, startTime, endTime);
		if (null != list && initBean) {
			for (PaymentLog bean : list) {
				initBean(bean);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public PaymentLog getBeanById(Long id, boolean initBean) {
		PaymentLog bean = dao.getBeanById(id);
		if (initBean) {
			initBean(bean);
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog save(final PaymentLog bean) {
		if (null != bean) {
			Long nextId = fieldIdManagerMng.getNextId("billing_payment_log", "id", 1L);
			if (nextId > 0) {
				bean.setId(nextId);
				bean.initFieldValue();
				dao.save(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog update(Long id, Integer agentId, Integer sellUserId, Boolean paid, Integer paymentWay,
			String paymentFlowId, Timestamp paymentTime, Double paymentPrice, Double agentDeductionRate,String paymentDesc,Integer paymentAuto) {
		PaymentLog bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setAgentId(agentId);
				if (null != sellUserId) {
					bean.setSellUserId(Long.valueOf(sellUserId));
				} else {
					bean.setSellUserId(null);
				}
				bean.setPaid(paid);
				bean.setPaymentWay(paymentWay);
				bean.setPaymentFlowId(paymentFlowId);
				bean.setPaymentTime(paymentTime);
				bean.setPaymentPrice(paymentPrice);
				bean.setAgentDeductionRate(agentDeductionRate);
				bean.setPaymentDesc(paymentDesc);
				bean.setPaymentAuto(paymentAuto);
				bean = dao.update(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog processById(Long id) {
		PaymentLog bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(PaymentLog.STATUS_PROCESSING);
				bean = dao.update(bean);
			}
		}
		return bean;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog completeById(Long id,Integer certStatus,Timestamp startTime) {
		PaymentLog bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(PaymentLog.STATUS_COMPLETED);
				bean = dao.update(bean);
				Integer enterpriseId = bean.getEnterpriseId();
				if (null != enterpriseId) {
					Integer packageId = bean.getPackageId();
					if (null != packageId) {
						Integer paymentType = bean.getPaymentType();
						if (PaymentLog.PAYMENT_TYPE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, true,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_BUY_INCREMENT_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_UPGRADE_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_PACKAGE_CONTINUE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, true, false,startTime);
						}
						//重新计算账户套餐使用情况
						WelcomeAct.reCheckUsedPruduct(enterpriseId);
						//重新更新缓存
						WelcomeAct.recheckUsedCache(enterpriseId, certStatus);
					}
					try {
						ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>());
						Integer[] ids = { enterpriseId };
						IliveNotifyTask iliveNotifyTask = new IliveNotifyTask(ids,
								IliveNotifyTask.NOTIFY_MODE_UPDATE_PRODUCT);
						threadPoolExecutor.execute(iliveNotifyTask);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bean;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog completeByAuto(PaymentLog bean,Integer certStatus,Timestamp startTime) {
			if (null != bean) {
				bean.setStatus(PaymentLog.STATUS_COMPLETED);
				bean = dao.update(bean);
				Integer enterpriseId = bean.getEnterpriseId();
				if (null != enterpriseId) {
					Integer packageId = bean.getPackageId();
					if (null != packageId) {
						Integer paymentType = bean.getPaymentType();
						if (PaymentLog.PAYMENT_TYPE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_BUY_INCREMENT_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_UPGRADE_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,startTime);
						} else if (PaymentLog.PAYMENT_TYPE_PACKAGE_CONTINUE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, true, false,startTime);
						}
						//重新计算账户套餐使用情况
						WelcomeAct.reCheckUsedPruduct(enterpriseId);
						//重新更新缓存
						WelcomeAct.recheckUsedCache(enterpriseId, certStatus);
					}
					try {
						ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>());
						Integer[] ids = { enterpriseId };
						IliveNotifyTask iliveNotifyTask = new IliveNotifyTask(ids,
								IliveNotifyTask.NOTIFY_MODE_UPDATE_PRODUCT);
						threadPoolExecutor.execute(iliveNotifyTask);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		return bean;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog yytcompleteById(PaymentLog bean,Integer certStatus) {
		
			if (null != bean) {
				bean.setStatus(PaymentLog.STATUS_COMPLETED);
				bean = dao.update(bean);
				Integer enterpriseId = bean.getEnterpriseId();
				if (null != enterpriseId) {
					Integer packageId = bean.getPackageId();
					if (null != packageId) {
						Integer paymentType = bean.getPaymentType();
						if (PaymentLog.PAYMENT_TYPE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,null);
						} else if (PaymentLog.PAYMENT_TYPE_BUY_INCREMENT_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,null);
						} else if (PaymentLog.PAYMENT_TYPE_UPGRADE_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, false, false,null);
						} else if (PaymentLog.PAYMENT_TYPE_PACKAGE_CONTINUE_BUY_BASIC_PACKAGE.equals(paymentType)) {
							enterpriseAndProductMng.insertOrUpgradePackage(enterpriseId, packageId, true, false,null);
						}
						//重新计算账户套餐使用情况
						WelcomeAct.reCheckUsedPruduct(enterpriseId);
						//重新更新缓存
						WelcomeAct.recheckUsedCache(enterpriseId, certStatus);
					}
					try {
						ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 100, 0L, TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>());
						Integer[] ids = { enterpriseId };
						IliveNotifyTask iliveNotifyTask = new IliveNotifyTask(ids,
								IliveNotifyTask.NOTIFY_MODE_UPDATE_PRODUCT);
						threadPoolExecutor.execute(iliveNotifyTask);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		return bean;
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PaymentLog cancelById(Long id, String cancelReason) {
		PaymentLog bean = null;
		if (null != id) {
			bean = dao.getBeanById(id);
			if (null != bean) {
				bean.setStatus(PaymentLog.STATUS_CANCELED);
				bean.setCancelReason(cancelReason);
				bean = dao.update(bean);
			}
		}
		return bean;
	}

	private void initBean(final PaymentLog bean) {
		if (null != bean) {
			Integer packageId = bean.getPackageId();
			if (null != packageId) {
				PackageInfo packageInfo = packageInfoMng.getBeanById(packageId, false);
				bean.setPackageInfo(packageInfo);
			}
			Integer enterpriseId = bean.getEnterpriseId();
			if (null != enterpriseId) {
				EnterpriseBilling enterpriseBilling = enterpriseBillingMng.getBeanById(enterpriseId, false);
				bean.setEnterpriseBilling(enterpriseBilling);
				bean.setContactNumber(enterpriseBilling.getUserPhone());
			}
			Integer agentId = bean.getAgentId();
			if (null != agentId) {
				AgentInfo agentInfo = agentInfoMng.getBeanById(agentId);
				bean.setAgentInfo(agentInfo);
			}
		}
	}

	@Autowired
	private PaymentLogDao dao;
	@Autowired
	private FieldIdManagerMng fieldIdManagerMng;
	@Autowired
	private PackageInfoMng packageInfoMng;
	@Autowired
	private EnterpriseBillingMng enterpriseBillingMng;
	@Autowired
	private AgentInfoMng agentInfoMng;
	@Autowired
	private EnterpriseAndProductMng enterpriseAndProductMng;
	@Override
	public List<PaymentLog> listByParamsByAuto(Integer paymentAuto) {
		// TODO Auto-generated method stub
		return dao.listByParamsByAuto(paymentAuto);
	}

	@Override
	public PaymentLog update(PaymentLog bean) {
		// TODO Auto-generated method stub
		return dao.update(bean);
	}
}
