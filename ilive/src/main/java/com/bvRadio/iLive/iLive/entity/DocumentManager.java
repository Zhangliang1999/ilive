package com.bvRadio.iLive.iLive.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 文档类型
 * @author Wei
 *
 */
public class DocumentManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5506148813032105986L;

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 文件位置
	 */
	private String url;
	
	/**
	 * 类型    
	 */
	private String type;
	
	/**
	 * 大小
	 */
	private Integer size;
	
	/**
	 * 上传人
	 */
	private String userName;
	
	/**
	 * 上传时间
	 */
	private Timestamp createTime;
	
	/**
	 * 上传人企业
	 */
	private Integer enterpriseId;
	
	/**
	 * 更新人
	 */
	private String updateUserName;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 是否允许用户下载    0允许  1不允许
	 */
	private Integer isAllow;
	
	/**
	 * 上传人userId
	 */
	private Long userId;
	
	private Integer state;
	
	private Date stampTime;
	
	
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getStampTime() {
		return stampTime;
	}

	public void setStampTime(Date stampTime) {
		this.stampTime = stampTime;
	}

	private List<DocumentPicture> list;
	

	public List<DocumentPicture> getList() {
		return list;
	}

	public void setList(List<DocumentPicture> list) {
		this.list = list;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(Integer isAllow) {
		this.isAllow = isAllow;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	
	
	
}
