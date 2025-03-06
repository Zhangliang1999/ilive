package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 问卷活动
 * @author Wei
 *
 */
public class ILiveQuestionnaireActivity implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -175138892081295702L;
	/**
	 * 投票活动id
	 */
	private Long id;
	
	/**
	 * 直播间id
	 */
	private Integer roomId;
	/**
	 * 问卷活动名称
	 */
	private String questionnaireName;
	
	/**
	 * 问卷开始时间
	 */
	private Timestamp startTime;
	
	/**
	 * 问卷结束时间
	 */
	private Timestamp endTime;
	
	/**
	 * 是否对用户公开   0不公开   1公开
	 */
	private Integer isOpen;
	
	/**
	 * 参与人数
	 */
	private Integer number;
	/**
	 * 浏览人数
	 */
	private Integer lookNumber;
	/**
	 * 问卷活动是否开启  0未开启   1开启
	 */
	private Integer isSwitch = 0;
	
	/**
	 * 问卷是否结束  0未结束   1结束
	 */
	private Integer isEnd;
	
	/**
	 * 以前是否开启过   0没有   1有
	 */
	private Integer isBeforeSwitch;
	
	private List<ILiveQuestionnaireProblem> list;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 修改时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;
	
	/**
	 * 用户id
	 */
	private Long userId;
    /**
     * 问卷简介
     * @return
     */
	private String questionnaireDesc;
	/**
	 * 备注
	 * @return
	 */
	private String msg;
	/**
	 * 是否开启手机验证
	 * @return
	 */
	 private Integer identity;
	 /**
	  * 是否开始身份验证
	  * @return
	  */
	 private Integer authentication;
	public Integer getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	public Integer getIsBeforeSwitch() {
		return isBeforeSwitch;
	}

	public void setIsBeforeSwitch(Integer isBeforeSwitch) {
		this.isBeforeSwitch = isBeforeSwitch;
	}


	public Integer getIsSwitch() {
		return isSwitch;
	}

	public void setIsSwitch(Integer isSwitch) {
		this.isSwitch = isSwitch;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getLookNumber() {
		return lookNumber;
	}

	public void setLookNumber(Integer lookNumber) {
		this.lookNumber = lookNumber;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public List<ILiveQuestionnaireProblem> getList() {
		return list;
	}

	public void setList(List<ILiveQuestionnaireProblem> list) {
		this.list = list;
	}

	public String getQuestionnaireName() {
		return questionnaireName;
	}

	public void setQuestionnaireName(String questionnaireName) {
		this.questionnaireName = questionnaireName;
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

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getQuestionnaireDesc() {
		return questionnaireDesc;
	}

	public void setQuestionnaireDesc(String questionnaireDesc) {
		this.questionnaireDesc = questionnaireDesc;
	}

	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public Integer getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Integer authentication) {
		this.authentication = authentication;
	}
	
}
