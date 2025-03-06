package com.bvRadio.iLive.iLive.dao;


import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSVoteActivity;

public interface BBsVoteActivityDao {
	
	/**
	 * 保存投票活动
	 * @param bbsVoteActivity
	 * @return
	 */
	public boolean saveBBsVoteActivity(BBSVoteActivity bbsVoteActivity);
	
	/**
	 * 根据主键ID获取数据
	 * @param voteId 主键
	 * @return
	 */
	public BBSVoteActivity selectBBsVoteActivityById(
			Long voteId) throws Exception;
		
	/**
	 * 删除单个投票活动（把delflag更新为1，并不是实际删除）
	 * @param id 主键
	 */
	public void deleteBBsVoteActivity(Long id);
	/**
	 * 删除多个投票活动
	 * @param ids 主键的数组
	 */
	public void deleteBBsVoteActivityByIds(Long[] ids);
	
	
	/**
	 * 查询文件分页
	 * @param sqlParam 查询参数 （delFlag 删除标记参数，  roomId 直播间id，  
	 * voteTitle 文件名称参数）
	 * @param pageNo 页码
	 * @param pageSize 条数
	 * @return
	 */
	public Pagination getBBsVoteAvtivityPage(Map<String, Object> sqlParam, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 修改活动
	 * @param sqlParam
	 */
	public void updateBBsVoteActivityById(BBSVoteActivity sqlParam);

	
}
