package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 充值记录
 * @author YanXL
 *
 */
public class RechargeRecordBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 记录id
	 */
	private Long recharge_id;
	/**
	 * 用户名称（用户ID）
	 */
	private String userName_Id;
	/**
	 * 用户ID
	 */
	private Integer user_id;
	
	/**
	 * 人民币金额
	 */
	private Double rmb_amount; 
	/**
	 * 兑换虚拟币
	 */
	private Long virtual_currency;
	/**
	 * 赠送虚拟币数
	 */
	private Long send_currency;
	/**
	 * 订单号
	 */
	private String order_number;
	/**
	 * 支付类型：0：支付宝    1：微信
	 */
	private Integer payment_type;
	/**
	 * 支付订单号
	 */
	private String payment_order_number;
	/**
	 * 支付状态：0 ：已支付    1：未支付
	 */
	private Integer payment_status;
	/**
	 * 订单时间
	 */
	private Timestamp payment_time;
	
	public Long getRecharge_id() {
		return recharge_id;
	}
	public void setRecharge_id(Long recharge_id) {
		this.recharge_id = recharge_id;
	}
	public String getUserName_Id() {
		return userName_Id;
	}
	public void setUserName_Id(String userName_Id) {
		this.userName_Id = userName_Id;
	}
	public Double getRmb_amount() {
		return rmb_amount;
	}
	public void setRmb_amount(Double rmb_amount) {
		this.rmb_amount = rmb_amount;
	}
	public Long getVirtual_currency() {
		return virtual_currency;
	}
	public void setVirtual_currency(Long virtual_currency) {
		this.virtual_currency = virtual_currency;
	}
	public Long getSend_currency() {
		return send_currency;
	}
	public void setSend_currency(Long send_currency) {
		this.send_currency = send_currency;
	}
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public Integer getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(Integer payment_type) {
		this.payment_type = payment_type;
	}
	public String getPayment_order_number() {
		return payment_order_number;
	}
	public void setPayment_order_number(String payment_order_number) {
		this.payment_order_number = payment_order_number;
	}
	public Integer getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(Integer payment_status) {
		this.payment_status = payment_status;
	}
	public Timestamp getPayment_time() {
		return payment_time;
	}
	public void setPayment_time(Timestamp payment_time) {
		this.payment_time = payment_time;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public RechargeRecordBean() {
		super();
	}
	
}
