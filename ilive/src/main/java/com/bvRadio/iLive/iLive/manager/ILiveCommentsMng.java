package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveComments;

/**
 * 评论
 * @author YanXL
 *
 */
public interface ILiveCommentsMng {
	/**
	 * 添加评论
	 * @param liveComments
	 */
	public void saveILiveComments(ILiveComments liveComments);
	/**
	 * 根据消息ID获取信息
	 * @param msgId 话题ID
	 * @param isChecked 审核状态
	 * @param isDelete 删除状态
	 * @return
	 */
	public List<ILiveComments> selectILiveCommentsByMsgId(Long msgId,
			boolean isChecked, boolean isDelete);
	/**
	 * 根据消息ID获取所有评论
	 * @param msgId 消息ID
	 * @param isDelete 是否删除 
	 * @return
	 */
	public List<ILiveComments> selectILiveCommentsListByMsgId(Long msgId, boolean isDelete );
	/**
	 * 删除评论
	 * @param commentsId
	 */
	public void deleteILiveComments(Long commentsId);
	/**
	 * 根据主键获取数据
	 * @param commentsId
	 * @return
	 */
	public ILiveComments getILiveCommentsById(Long commentsId)  throws Exception ;
	/**
	 * 修改评论信息
	 * @param iLiveComments
	 */
	public void updateIliveCommentsById(ILiveComments iLiveComments)  throws Exception ;
	

}
