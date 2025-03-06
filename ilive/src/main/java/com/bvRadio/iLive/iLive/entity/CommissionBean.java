package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;

/**
 * 佣金级别
 * @author Administrator
 *
 */
public class CommissionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 佣金id 
	 */
	private Integer commission_id;
	/**
	 * 名称
	 */
	private String commission_name;
	/**
	 * 佣金等级
	 */
	private Integer commission_level;
	/**
	 * 佣金比例   %
	 */
	private Integer commission_proportion;
	/**
	 * 最小赚取虚拟币
	 */
	private Integer min_currency;
	/**
	 * 最高赚取  当为0时为无上限
	 */
	private Integer max_currency;
	
	public Integer getCommission_id() {
		return commission_id;
	}
	public void setCommission_id(Integer commission_id) {
		this.commission_id = commission_id;
	}
	public String getCommission_name() {
		return commission_name;
	}
	public void setCommission_name(String commission_name) {
		this.commission_name = commission_name;
	}
	public Integer getCommission_level() {
		return commission_level;
	}
	public void setCommission_level(Integer commission_level) {
		this.commission_level = commission_level;
	}
	public Integer getCommission_proportion() {
		return commission_proportion;
	}
	public void setCommission_proportion(Integer commission_proportion) {
		this.commission_proportion = commission_proportion;
	}
	public CommissionBean() {
		super();
	}
	public Integer getMin_currency() {
		return min_currency;
	}
	public void setMin_currency(Integer min_currency) {
		this.min_currency = min_currency;
	}
	public Integer getMax_currency() {
		return max_currency;
	}
	public void setMax_currency(Integer max_currency) {
		this.max_currency = max_currency;
	}
	
}
