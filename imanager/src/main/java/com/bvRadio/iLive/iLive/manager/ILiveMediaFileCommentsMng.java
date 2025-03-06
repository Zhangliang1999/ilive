package com.bvRadio.iLive.iLive.manager;

import java.util.List;

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
	
	public Pagination getFileCommentsById(ILiveMediaFileComments commons,Integer pageNo, Integer pageSize);

	public void update(ILiveMediaFileComments commons);
	
	Pagination getPageByUserId(Long userId,Long fileId,String keyword,Integer pageNo,Integer pageSize);
	List<ILiveMediaFileComments> getListByUserId(Long userId,Long fileId,String keyword);
}
