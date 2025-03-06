package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 充值规则
 * @author YanXL
 *
 */
public class RechargeRulesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 规则ID
	 */
	private Integer rules_id;
	/**
	 * 规则名称
	 */
	private String rules_name;
	/**
	 * 人民币价格最低
	 */
	private Integer rmb_price_min;
	/**
	 * 人民币价格最高
	 */
	private Integer rmb_price_max;
	/**
	 * 赠送虚拟币数量
	 */
	private Integer gift_virtual_currency;
	/**
	 * 发布时间
	 */
	private Timestamp rules_time;
	
	public Integer getRules_id() {
		return rules_id;
	}
	public void setRules_id(Integer rules_id) {
		this.rules_id = rules_id;
	}
	public String getRules_name() {
		return rules_name;
	}
	public void setRules_name(String rules_name) {
		this.rules_name = rules_name;
	}
	public Integer getRmb_price_min() {
		return rmb_price_min;
	}
	public void setRmb_price_min(Integer rmb_price_min) {
		this.rmb_price_min = rmb_price_min;
	}
	public Integer getRmb_price_max() {
		return rmb_price_max;
	}
	public void setRmb_price_max(Integer rmb_price_max) {
		this.rmb_price_max = rmb_price_max;
	}
	public Integer getGift_virtual_currency() {
		return gift_virtual_currency;
	}
	public void setGift_virtual_currency(Integer gift_virtual_currency) {
		this.gift_virtual_currency = gift_virtual_currency;
	}
	public Timestamp getRules_time() {
		return rules_time;
	}
	public void setRules_time(Timestamp rules_time) {
		this.rules_time = rules_time;
	}
	public RechargeRulesBean() {
		super();
	}
	
}
