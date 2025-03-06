package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveEstoppel;

/**
 * 禁言实体
 * 
 * @author YanXL
 * 
 */
public class ILiveEstoppel extends BaseILiveEstoppel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ILiveEstoppel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ILiveEstoppel(Integer roomId, Long userId) {
		super(roomId, userId);
	}

	public ILiveEstoppel(Long userId, String userIp, Timestamp createTime) {
		super(userId, userIp, createTime);
	}

}