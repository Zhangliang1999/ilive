package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 活动参与人
 * @author Wei
 *
 */
public class ILiveLotteryParktake implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1373260164676119510L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 抽奖活动id
	 */
	private Long lotteryId;
	
	/**
	 * 参与人id
	 */
	private Long userId;
	
	/**
	 * 参与人姓名
	 */
	private String userName;
	
	/**
	 * 参与人联系方式
	 */
	private String phone;
	
	/**
	 * 是否中奖   0未中奖    1中奖
	 */
	private Integer isPrize;
	
	/**
	 * 奖品id
	 */
	private Long prizeId;
	
	/**
	 * 奖品名称
	 */
	private String prize;
	
	/**
	 * 奖项名称
	 */
	private String prizepro;
	
	/**
	 * 参与终端
	 */
	private String terminal;
	
	/**
	 * IP地址
	 */
	private String IP;
	
	/**
	 * 所在地区
	 */
	private String area;
	
	/**
	 * 参与时间
	 */
	private Timestamp createTime;
	
	public String getPrizepro() {
		return prizepro;
	}

	public void setPrizepro(String prizepro) {
		this.prizepro = prizepro;
	}

	public Long getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Long prizeId) {
		this.prizeId = prizeId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getIsPrize() {
		return isPrize;
	}

	public void setIsPrize(Integer isPrize) {
		this.isPrize = isPrize;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
