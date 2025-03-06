package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

/**
 * 设备订单
 * @author Wei
 */
public class EquipmentOrder {
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 设备id
	 */
	private Long equipmentId;
	
	/**
	 * 工单类型    1设备租赁    2设备购买  3直播服务
	 */
	private Integer type;
	
	/**
	 * 工单状态    1处理中   2已关闭   3已处理
	 */
	private Integer status;
	
	/**
	 * 租用地区
	 */
	private String area;
	
	/**
	 * 租用开始时间
	 */
	private Timestamp startTime;
	
	/**
	 * 租用结束时间
	 */
	private Timestamp endTime;
	
	/**
	 * 联系人姓名
	 */
	private String contactName;
	
	/**
	 * 联系人联系方式
	 */
	private String contactPhone;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 申请的账号id
	 */
	private Long userId;
	
	/**
	 * 申请的账号
	 */
	private String applyAccount;
	
	/**
	 * 1租用   2卖
	 */
	private Integer rentOrSell;
	
	private Equipment equipment;
	
	
	/**
	 * 直播场景   1发布会 2、企业年会 3、峰会论坛  4、才艺表演  5、企业内训  6、教育培训  7、其他
	 */
	private Integer liveScene;
	
	/**
	 * 直播日期
	 */
	private Timestamp liveTime;
	
	/**
	 * 直播城市
	 */
	private String city;
	
	/**
	 * 预计人数   1、0-100   2、100-500  3、500-2000  4、2000以上
	 */
	private Integer predictNum;
	
	/**
	 * 预计时长   1、0-4小时   2、4-8小时    3、8小时以上
	 */
	private Integer predictDuration;
	
	/**
	 * 直播预算
	 */
	private Integer predictBudget;
	
	/**
	 * 其他需求
	 */
	private String otherDemand;
	
	/**
	 * 删除标记   0未删除   1删除
	 */
	private Integer isDel;
	
	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getLiveScene() {
		return liveScene;
	}

	public void setLiveScene(Integer liveScene) {
		this.liveScene = liveScene;
	}

	public Timestamp getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(Timestamp liveTime) {
		this.liveTime = liveTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPredictNum() {
		return predictNum;
	}

	public void setPredictNum(Integer predictNum) {
		this.predictNum = predictNum;
	}

	public Integer getPredictDuration() {
		return predictDuration;
	}

	public void setPredictDuration(Integer predictDuration) {
		this.predictDuration = predictDuration;
	}

	public Integer getPredictBudget() {
		return predictBudget;
	}

	public void setPredictBudget(Integer predictBudget) {
		this.predictBudget = predictBudget;
	}

	public String getOtherDemand() {
		return otherDemand;
	}

	public void setOtherDemand(String otherDemand) {
		this.otherDemand = otherDemand;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Integer getRentOrSell() {
		return rentOrSell;
	}

	public void setRentOrSell(Integer rentOrSell) {
		this.rentOrSell = rentOrSell;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getApplyAccount() {
		return applyAccount;
	}

	public void setApplyAccount(String applyAccount) {
		this.applyAccount = applyAccount;
	}
	
}
