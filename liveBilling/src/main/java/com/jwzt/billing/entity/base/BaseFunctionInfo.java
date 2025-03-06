package com.jwzt.billing.entity.base;
/**
* @author ysf
*/

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BaseFunctionInfo implements java.io.Serializable {

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 父ID
	 */
	private Integer parentId;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 功能名称
	 */
	private String functionName;
	/**
	 * 权限吗
	 */
	private Integer functionCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Integer getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Integer functionCode) {
		this.functionCode = functionCode;
	}

}
