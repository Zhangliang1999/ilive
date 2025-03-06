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
	 * 根据userID获取评论
	 * @param commons
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getFileCommentsById(ILiveMediaFileComments commons, Integer pageNo, Integer pageSize);

	public void updateComments(ILiveMediaFileComments commons);
	
	public Pagination getPageByUserId(Long userId, Long fileId, String keyword, Integer pageNo, Integer pageSize);
	
	public List<ILiveMediaFileComments> getListByUserId(Long userId, Long fileId, String keyword);
}
