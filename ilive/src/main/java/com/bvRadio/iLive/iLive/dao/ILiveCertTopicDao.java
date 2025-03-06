package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveCertTopic;



public interface ILiveCertTopicDao {
	
	/**
	 * 获取评论记录
	 * @param enterpriseId
	 * @return
	 */
	public List<ILiveCertTopic> getCertTopicList(Integer enterpriseId);
	
	/**
	 * 新增评论信息
	 * @param certTopic
	 * @return
	 */
	public boolean addCertTopic(ILiveCertTopic certTopic);

}
