package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 消费记录
 * @author YanXL
 *
 */
public class ConsumptionRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 消费ID
	 */
	private Long consumption_id;
	/**
	 * 消费类型：0/收入   1/支出
	 */
	private Integer consumption_type;
	/**
	 * 消费行为
	 */
	private String consumption_status;
	/**
	 * 用户（ID）
	 */
	private String userName_Id;
	/**
	 * 用户ID
	 */
	private Integer user_id;
	/**
	 * 主播名称（ ID）
	 */
	private String adminName_Id;
	/**
	 * 主播ID
	 */
	private Integer admin_id;
	/**
	 * 礼物名称（礼物ID）
	 */
	private String giftName_Id;
	/**
	 * 礼物ID
	 */
	private Integer gift_id;
	/**
	 * 礼物数量
	 */
	private Integer gift_number;
	/**
	 * 礼物总价
	 */
	private Long gift_total;
	/**
	 * 直播ID
	 */
	private Integer live_Id;
	/**
	 * 赠送时间
	 */
	private Timestamp gift_time;
	public Long getConsumption_id() {
		return consumption_id;
	}
	public void setConsumption_id(Long consumption_id) {
		this.consumption_id = consumption_id;
	}
	public Integer getConsumption_type() {
		return consumption_type;
	}
	public void setConsumption_type(Integer consumption_type) {
		this.consumption_type = consumption_type;
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
	public String getAdminName_Id() {
		return adminName_Id;
	}
	public void setAdminName_Id(String adminName_Id) {
		this.adminName_Id = adminName_Id;
	}
	public Integer getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}
	public String getGiftName_Id() {
		return giftName_Id;
	}
	public void setGiftName_Id(String giftName_Id) {
		this.giftName_Id = giftName_Id;
	}
	public Integer getGift_id() {
		return gift_id;
	}
	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}
	public Integer getGift_number() {
		return gift_number;
	}
	public void setGift_number(Integer gift_number) {
		this.gift_number = gift_number;
	}
	public Long getGift_total() {
		return gift_total;
	}
	public void setGift_total(Long gift_total) {
		this.gift_total = gift_total;
	}
	public Integer getLive_Id() {
		return live_Id;
	}
	public void setLive_Id(Integer live_Id) {
		this.live_Id = live_Id;
	}
	public Timestamp getGift_time() {
		return gift_time;
	}
	public void setGift_time(Timestamp gift_time) {
		this.gift_time = gift_time;
	}
	public ConsumptionRecordBean() {
		super();
	}
	public String getConsumption_status() {
		return consumption_status;
	}
	public void setConsumption_status(String consumption_status) {
		this.consumption_status = consumption_status;
	}
	
}
