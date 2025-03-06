package com.jwzt.billing.entity;
/**
* @author ysf
*/

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwzt.billing.entity.base.BasePaymentLog;
import com.jwzt.billing.entity.bo.WorkLog;

@SuppressWarnings("serial")
public class PaymentLog extends BasePaymentLog {
	/**
	 * 状态；新增
	 */
	public static final Integer STATUS_NEW = 1;
	/**
	 * 状态；处理中
	 */
	public static final Integer STATUS_PROCESSING = 2;
	/**
	 * 状态；完成
	 */
	public static final Integer STATUS_COMPLETED = 3;
	/**
	 * 状态；取消
	 */
	public static final Integer STATUS_CANCELED = 4;
	/**
	 * 订单类型：套餐订购
	 */
	public static final Integer PAYMENT_TYPE_BUY_BASIC_PACKAGE = 1;
	/**
	 * 订单类型：资源包叠加
	 */
	public static final Integer PAYMENT_TYPE_BUY_INCREMENT_PACKAGE = 2;
	/**
	 * 订单类型：套餐升级
	 */
	public static final Integer PAYMENT_TYPE_UPGRADE_BASIC_PACKAGE = 3;
	/**
	 * 订单类型：套餐续购
	 */
	public static final Integer PAYMENT_TYPE_PACKAGE_CONTINUE_BUY_BASIC_PACKAGE = 4;

	/**
	 * 支付方式：线下转账
	 */
	public static final Integer PAYMENT_WAY_OFFLINE_TRANSFER = 1;
	/**
	 * 支付方式：银行转账
	 */
	public static final Integer PAYMENT_WAY_BANK_TRANSFER = 2;
	/**
	 * 支付方式：支付宝
	 */
	public static final Integer PAYMENT_WAY_ALIPAY = 3;
	/**
	 * 支付方式：微信
	 */
	public static final Integer PAYMENT_WAY_WECHAT = 4;
	/**
	 * 支付方式：营业厅
	 */
	public static final Integer PAYMENT_WAY_YYT = 5;
	/**
	 * 渠道：官方平台
	 */
	public final static Integer CHANNEL_TYPE_OFFICIAL_PLATFORM = 1;
	/**
	 * 渠道：渠道代理
	 */
	public final static Integer CHANNEL_TYPE_CHANNEL_AGENT = 2;
	/**
	 * 渠道：集团产品库
	 */
	public final static Integer CHANNEL_TYPE_GROUP_PRODUCT_LIBRARY = 3;
	
	/**
	 *自动续订
	 */
    public final static Integer PAYMENTAUTO_YES=1;
    /**
     * 不自动续订
     */
    public final static Integer PAYMENTAUTO_NO=1;
	private PackageInfo packageInfo;
	@JsonIgnore
	private EnterpriseBilling enterpriseBilling;
	@JsonIgnore
	private AgentInfo agentInfo;

	private List<WorkLog> workLogList;

	public String getPackageName() {
		if (null != packageInfo) {
			return packageInfo.getPackageName();
		}
		return null;
	}

	public String getEnterpriseName() {
		if (null != enterpriseBilling) {
			return enterpriseBilling.getEnterpriseName();
		}
		return null;
	}

	public String getAgentName() {
		if (null != agentInfo) {
			return agentInfo.getAgentName();
		}
		return null;
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatus()) {
			setStatus(STATUS_NEW);
		}
		if (null == getPaid()) {
			setPaid(false);
		}
		if (null == getRefunded()) {
			setRefunded(false);
		}
	}

	public PaymentLog() {
		super();
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	public EnterpriseBilling getEnterpriseBilling() {
		return enterpriseBilling;
	}

	public void setEnterpriseBilling(EnterpriseBilling enterpriseBilling) {
		this.enterpriseBilling = enterpriseBilling;
	}

	public AgentInfo getAgentInfo() {
		return agentInfo;
	}

	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}

	public List<WorkLog> getWorkLogList() {
		return workLogList;
	}

	public void setWorkLogList(List<WorkLog> workLogList) {
		this.workLogList = workLogList;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
