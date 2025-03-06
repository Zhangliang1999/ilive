package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 提现记录
 * @author YanXL
 *
 */
public class PresentRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 提现ID
	 */
	private Long present_id;
	/**
	 * 用户名称（用户ID）
	 */
	private String userName_Id;
	/**
	 * 用户ID
	 */
	private Integer user_id;
	/**
	 * 提现人民币额度（元）
	 */
	private Double present_rmb;
	/**
	 * 第三方订单号
	 */
	private String payment_order_number;
	/**
	 * 提现状态：0 ：处理中 1：拒绝提现 2：提现成功
	 */
	private Integer present_type;
	/**
	 * 提现提交时间
	 */
	private Timestamp present_startTime;
	/**
	 * 提现处理时间
	 */
	private Timestamp present_endTime;
	/**
	 * 提现账户
	 */
	private String present_account;
	/**
	 * 提现类型：0 支付宝，1 微信
	 */
	private Integer presentType;
	/**
	 * 提现方式:0 红包     1：主播收益
	 */
	private Integer present_income;
	/**
	 * 真实用户姓名
	 */
	private String real_name;
	
	public Long getPresent_id() {
		return present_id;
	}
	public void setPresent_id(Long present_id) {
		this.present_id = present_id;
	}
	public String getUserName_Id() {
		return userName_Id;
	}
	public void setUserName_Id(String userName_Id) {
		this.userName_Id = userName_Id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Double getPresent_rmb() {
		return present_rmb;
	}
	public void setPresent_rmb(Double present_rmb) {
		this.present_rmb = present_rmb;
	}
	public String getPayment_order_number() {
		return payment_order_number;
	}
	public void setPayment_order_number(String payment_order_number) {
		this.payment_order_number = payment_order_number;
	}
	public Integer getPresent_type() {
		return present_type;
	}
	public void setPresent_type(Integer present_type) {
		this.present_type = present_type;
	}
	public Timestamp getPresent_startTime() {
		return present_startTime;
	}
	public void setPresent_startTime(Timestamp present_startTime) {
		this.present_startTime = present_startTime;
	}
	public Timestamp getPresent_endTime() {
		return present_endTime;
	}
	public void setPresent_endTime(Timestamp present_endTime) {
		this.present_endTime = present_endTime;
	}
	public PresentRecordBean() {
		super();
	}
	public String getPresent_account() {
		return present_account;
	}
	public void setPresent_account(String present_account) {
		this.present_account = present_account;
	}
	public Integer getPresentType() {
		return presentType;
	}
	public void setPresentType(Integer presentType) {
		this.presentType = presentType;
	}
	public Integer getPresent_income() {
		return present_income;
	}
	public void setPresent_income(Integer present_income) {
		this.present_income = present_income;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	
}
