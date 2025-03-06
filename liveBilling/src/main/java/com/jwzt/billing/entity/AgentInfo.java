package com.jwzt.billing.entity;
/**
* @author ysf
*/

import java.sql.Timestamp;

import com.jwzt.billing.entity.base.BaseAgentInfo;

@SuppressWarnings("serial")
public class AgentInfo extends BaseAgentInfo {
	/**
	 * 状态：新增
	 */
	public final static Integer STATUS_NEW = 1;
	/**
	 * 状态：上线
	 */
	public final static Integer STATUS_ONLINE = 2;
	/**
	 * 状态：下线
	 */
	public final static Integer STATUS_OFFLINE = 3;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatus()) {
			setStatus(STATUS_ONLINE);
		}
	}

	public AgentInfo() {
		super();
	}

}
