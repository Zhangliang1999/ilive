package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveComments;

/**
 * 话题 评论 
 * @author YanXL
 *
 */
public interface ILiveCommentsDao {
	/**
	 * 新增评论
	 * @param liveComments 评论实体
	 * @throws Exception
	 */
	public void saveILiveComments(ILiveComments liveComments) throws Exception;
	/**
	 * 获取数据
	 * @param msgId 话题ID
	 * @param isChecked 审核标示
	 * @param isDelete 删除标示
	 * @return
	 * @throws Exception
	 */
	public List<ILiveComments> selectILiveCommentsByMsgId(Long msgId,
			boolean isChecked, boolean isDelete) throws Exception;
	/**
	 * 根据消息ID 获取数据
	 * @param msgId 消息ID
	 * @param isDelete 
	 * @return
	 * @throws Exception
	 */
	public List<ILiveComments> selectILiveCommentsListByMsgId(Long msgId, boolean isDelete) throws Exception;
	/**
	 * 删除数据
	 * @param commentsId 评论ID
 	 * @throws Exception
	 */
	public void deleteILiveComments(Long commentsId) throws Exception;
	/**
	 * 根据话题ID 和  用户ID获取评论
	 * @param msgId 话题ID
	 * @param userId 用户ID
	 * @return
	 */
	public List<ILiveComments> selectILiveCommentsByUserIdAndMsgId(Long msgId,Long userId) throws Exception;
	/**
	 * 获取数据
	 * @param commentsId 主键ID
	 * @return
	 * @throws Exception
	 */
	public ILiveComments getILiveCommentsById(Long commentsId) throws Exception;
	/**
	 * 修改数据
	 * @param iLiveComments
	 * @throws Exception
	 */
	public void updateIliveCommentsById(ILiveComments iLiveComments) throws Exception;
	

}
