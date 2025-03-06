package com.bvRadio.iLive.iLive.manager;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;

public interface ILiveMediaFileCommentsMng {

	/**
	 * 
	 * @param comments
	 */
	public Long addFileComments(ILiveMediaFileComments comments);

	/**
	 * 
	 * @param fileId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getFileComments(Long fileId, Integer pageNo, Integer pageSize);

	/**
	 * 获得评论数
	 * 
	 * @param commentId
	 * @return
	 */
	public ILiveMediaFileComments getFileCommentById(Long commentId);
	/**
	 * 获取评论数据
	 * @param fileId 文件ID
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param uncheckQueryFlag 
	 * @param search 
	 * @return
	 */
	public Pagination selectILiveMediaFileCommentsPage(Long fileId, Integer pageNo,
			Integer pageSize, Integer uncheckQueryFlag, String search);
	/**
	 * 修改审核
	 * @param commentsId 评论ID
	 * @param checkState 审核状态
	 * @throws Exception 
	 */
	public void updateCheckState(Long commentsId, Integer checkState) throws Exception;
	/**
	 * 删除数据
	 * @param commentsId
	 */
	public void updateDeleteState(Long commentsId);

	
	/**
	 * 批量审核评论
	 * @param ids
	 */
	public void batchCheck(Long[] ids);
	
	
	/**
	 * 批量删除评论
	 */
	public void batchDelete(Long[] ids);
	/**
	 * 更新
	 */
	public void update(ILiveMediaFileComments comments);

}
