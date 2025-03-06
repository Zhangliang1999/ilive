package com.jwzt.billing.entity;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.jwzt.billing.entity.base.BaseEnterpriseBilling;

/**
 * @author ysf
 */
public class EnterpriseBilling extends BaseEnterpriseBilling {

	private static final long serialVersionUID = -5169501774562533669L;
	/**
	 * 企业认证状态：未提交认证
	 */
	public static final Integer CERT_STATUS_NOT_SUBMITTED = 0;
	/**
	 * 企业认证状态：认证中
	 */
	public static final Integer CERT_STATUS_OPERATING = 1;
	/**
	 * 企业认证状态：认证通过
	 */
	public static final Integer CERT_STATUS_SUCCESS = 4;
	/**
	 * 企业认证状态：认证失败
	 */
	public static final Integer CERT_STATUS_ERROR = 5;
	/**
	 * 收益余额
	 */
	private Double unsettleAmount;

	public Integer getBetaRemainingDays() {
		if (CERT_STATUS_SUCCESS.equals(getCertStatus())) {
			return 0;
		} else {
			DateTime begin = new DateTime(getApplyTime());
			DateTime end = new DateTime();
			Period period = new Period(begin, end, PeriodType.days());
			int days = period.getDays();
			if ((30 - days) < 0) {
				return 0;
			} else {
				return (30 - days);
			}
		}
	}

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getSettleAmount()) {
			setSettleAmount(0.0);
		}
		if (null == getTotalAmount()) {
			setTotalAmount(0.0);
		}
		if (null == getPlatformAmount()) {
			setPlatformAmount(0.0);
		}
		if (null == getOpenRevenueAccount()) {
			setOpenRevenueAccount(false);
		}
		if (null == getOpenRedPackageAccount()) {
			setOpenRedPackageAccount(false);
		}
	}

	public EnterpriseBilling() {
		super();
	}

	public Double getUnsettleAmount() {
		return unsettleAmount;
	}

	public void setUnsettleAmount(Double unsettleAmount) {
		this.unsettleAmount = unsettleAmount;
	}

}
