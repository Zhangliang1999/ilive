package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;

/**
 * 点赞
 * @author YanXL
 *
 */
public interface ILiveMessagePraiseMng {
	/**
	 * 根据话题ID获取点赞数
	 * @param msgId
	 * @return
	 */
	public List<ILiveMessagePraise> selectILiveMessagePraisByMsgId(Long msgId);
	
	/**
	 * 添加点赞
	 * @throws Exception
	 */
	public void addILiveMessagePraise(ILiveMessagePraise iLiveMessagePraise);
	/**
	 *  获取点赞数据
	 * @param msgId 话题ID
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public ILiveMessagePraise selectILiveMessagePraise(Long msgId,Long userId);
}
