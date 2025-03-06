package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BeseILiveComments;
/**
 * 评论
 * @author YanXL
 *
 */
public class ILiveComments extends BeseILiveComments implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 是否被禁言   0 否   1是
	 */
	private Integer estoppelType;
	
	public Integer getEstoppelType() {
		return estoppelType;
	}
	public void setEstoppelType(Integer estoppelType) {
		this.estoppelType = estoppelType;
	}
}
