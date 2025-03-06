package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ILiveFileViewStatics;

public interface ILiveFileViewStaticsDao {
	
	
	/**
	 * 获取观看人数
	 * @param fileId
	 * @return
	 */
	public ILiveFileViewStatics getViewNum(Long fileId);
	
	
	/**
	 * 增加观看数量
	 * @param viewStatics
	 */
	public void addViewNum(ILiveFileViewStatics viewStatics);
	
	
	/**
	 * 初始化观看人数
	 * @param viewStatics
	 */
	public void initViewNum(ILiveFileViewStatics viewStatics);

}
