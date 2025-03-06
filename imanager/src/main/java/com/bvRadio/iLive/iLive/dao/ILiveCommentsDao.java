package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveComments;

/**
 * 评论 
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
	 * 根据用户id查询评论
	 * @param iLiveComments
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getByUserId(ILiveComments iLiveComments, Integer pageNo, Integer pageSize);

	public ILiveComments queryById(Long commentsId);
	
	public void update(ILiveComments iLiveComments);
	
}
