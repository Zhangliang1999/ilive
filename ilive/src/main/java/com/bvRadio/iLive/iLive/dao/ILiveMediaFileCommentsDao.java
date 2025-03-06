package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileComments;

/**
 * @author administrator
 * 
 */
public interface ILiveMediaFileCommentsDao {
	
	
	/**
	 * 
	 * @param comments
	 */
	public void addFileComments(ILiveMediaFileComments comments);
	
	/**
	 * 
	 * @param fileId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getFileComments(Long fileId,Integer pageNo,Integer pageSize);

	
	/**
	 * 获得评论数
	 * @param commentId
	 * @return
	 */
	public ILiveMediaFileComments getFileCommentById(Long commentId);
	/**
	 * 获取数据
	 * @param fileId 文件ID
	 * @param pageNo 页码
	 * @param pageSize 每页数据
	 * @param search 
	 * @return
	 */
	public Pagination selectILiveMediaFileCommentsPage(Long fileId,
			Integer pageNo, Integer pageSize,Integer uncheckQueryFlag, String search) throws Exception;
	/**
	 * 修改审核
	 * @param commentsId 评论ID
	 * @param checkState 审核状态
	 */
	public void updateCheckState(Long commentsId, Integer checkState) throws Exception;
	/**
	 * 修改删除状态
	 * @param commentsId
	 */
	public void updateDeleteState(Long commentsId);

	public List<ILiveMediaFileComments> getFileCommentByIds(Long[] ids);

	public void updateComments(ILiveMediaFileComments comments);

	
	/**
	 * 获取评论数
	 * @param fileId
	 * @return
	 */
	public Long getCommentsMount(Long fileId);

}
