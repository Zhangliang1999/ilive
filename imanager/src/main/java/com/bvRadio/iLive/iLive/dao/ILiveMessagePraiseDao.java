package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveMessagePraise;
/**
 * 点赞
 * @author YanXL
 *
 */
public interface ILiveMessagePraiseDao {
	/**
	 * 根据话题ID获取点赞集
	 * @param msgId 话题Id
	 * @return
	 * @throws Exception
	 */
	public List<ILiveMessagePraise> selectILiveMessagePraisByMsgId(Long msgId) throws Exception;
	/**
	 * 添加点赞
	 * @throws Exception
	 */
	public void insertILiveMessagePraise(ILiveMessagePraise iLiveMessagePraise) throws Exception;
	/**
	 *  获取点赞数据
	 * @param msgId 话题ID
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public ILiveMessagePraise selectILiveMessagePraise(Long msgId,Long userId) throws Exception;

}
