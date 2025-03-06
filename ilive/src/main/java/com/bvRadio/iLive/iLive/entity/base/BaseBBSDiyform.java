package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSDiyformData;
import com.bvRadio.iLive.iLive.entity.BBSDiymodel;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

/**
 * 
 * @author administrator
 * 直播自定义表单
 */
@SuppressWarnings("serial")
public abstract class BaseBBSDiyform implements java.io.Serializable {
	
	
	/**
	 * 主键
	 */
	private Integer diyformId;
	
	/**
	 * 表单名称
	 */
	private String diyformName;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 创建时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 模板集合
	 */
	private Set<BBSDiymodel> bbsDiymodels;
	
	
	/**
	 * 数据集合
	 */
	private Set<BBSDiyformData> bbsDiyformDatas;
	
	
	/**
	 * 运行最大选项
	 */
	private Integer maxOption;
	
	/**
	 * 是否运行观看结果 (1公开 0不公开)
	 */
	private Integer allowSeeResult;
	
	/**
	 * 投票次数(1一次  2多次)
	 */
	private Integer voteCount;
	
	/**
	 * 投票开始时间
	 */
	private Timestamp voteStartTime;

	/**
	 * 投票结束时间
	 */
	private Timestamp voteEndTime;
	
	private ILiveManager iLiveManager;
	
	/**
	 * 报名表单对应的直播间
	 */
	private Integer roomId;
	
	/**
	 * 删除标记 1 为删除 0为未删除
	 */
	private Integer delFlag;
	
	/**
	 * 第几次修改标识
	 */
	private Integer mark;

	// Constructors

	/** default constructor */
	public BaseBBSDiyform() {
	}

	/** minimal constructor */
	public BaseBBSDiyform(Integer diyformId) {
		this.diyformId = diyformId;
	}

	/** full constructor */
	public BaseBBSDiyform(Integer diyformId,  String diyformName, Timestamp createTime, Set<BBSDiymodel> bbsDiymodels,
			Set<BBSDiyformData> bbsDiyformDatas) {
		this.diyformId = diyformId;
		this.diyformName = diyformName;
		this.createTime = createTime;
		this.bbsDiymodels = bbsDiymodels;
		this.bbsDiyformDatas = bbsDiyformDatas;
	}

	// Property accessors

	public Integer getDiyformId() {
		return this.diyformId;
	}

	public void setDiyformId(Integer diyformId) {
		this.diyformId = diyformId;
	}


	public String getDiyformName() {
		return this.diyformName;
	}

	public void setDiyformName(String diyformName) {
		this.diyformName = diyformName;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Set<BBSDiymodel> getBbsDiymodels() {
		return this.bbsDiymodels;
	}

	public void setBbsDiymodels(Set<BBSDiymodel> bbsDiymodels) {
		this.bbsDiymodels = bbsDiymodels;
	}

	public Set<BBSDiyformData> getBbsDiyformDatas() {
		return this.bbsDiyformDatas;
	}

	public void setBbsDiyformDatas(Set<BBSDiyformData> bbsDiyformDatas) {
		this.bbsDiyformDatas = bbsDiyformDatas;
	}

	public ILiveManager getiLiveManager() {
		return iLiveManager;
	}

	public void setiLiveManager(ILiveManager iLiveManager) {
		this.iLiveManager = iLiveManager;
	}

	public Integer getMaxOption() {
		return maxOption;
	}

	public void setMaxOption(Integer maxOption) {
		this.maxOption = maxOption;
	}

	public Integer getAllowSeeResult() {
		return allowSeeResult;
	}

	public void setAllowSeeResult(Integer allowSeeResult) {
		this.allowSeeResult = allowSeeResult;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

	public Timestamp getVoteStartTime() {
		return voteStartTime;
	}

	public void setVoteStartTime(Timestamp voteStartTime) {
		this.voteStartTime = voteStartTime;
	}

	public Timestamp getVoteEndTime() {
		return voteEndTime;
	}

	public void setVoteEndTime(Timestamp voteEndTime) {
		this.voteEndTime = voteEndTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
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

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}
	
}