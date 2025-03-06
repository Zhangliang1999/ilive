package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 账户余额
 * @author Administrator
 */
public class UserBalancesBean implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	private Integer user_id;
	/**
	 * 用户名称
	 */
	private String user_name;
	/**
	 * 用户余额
	 */
	private Long user_balances;
	/**
	 * 累计消费额度
	 */
	private Long user_consumption;
	/**
	 * 创建时间
	 */
	private Timestamp create_time;
	/**
	 * 本月收益（收到的礼物经过折算后存入用户收益）
	 */
	private Long user_income;
	/**
	 * 用户红包收益（元）
	 */
	private double red_income;
	/**
	 * 总收益
	 */
	private Long all_income;
	/**
	 * 佣金
	 */
	private double commission_income;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Long getUser_balances() {
		return user_balances;
	}
	public void setUser_balances(Long user_balances) {
		this.user_balances = user_balances;
	}
	public Long getUser_consumption() {
		return user_consumption;
	}
	public void setUser_consumption(Long user_consumption) {
		this.user_consumption = user_consumption;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public UserBalancesBean() {
		super();
	}
	public Long getUser_income() {
		return user_income;
	}
	public void setUser_income(Long user_income) {
		this.user_income = user_income;
	}
	public double getRed_income() {
		return red_income;
	}
	public void setRed_income(double red_income) {
		this.red_income = red_income;
	}
	public Long getAll_income() {
		return all_income;
	}
	public void setAll_income(Long all_income) {
		this.all_income = all_income;
	}
	public double getCommission_income() {
		return commission_income;
	}
	public void setCommission_income(double commission_income) {
		this.commission_income = commission_income;
	}
	
}
