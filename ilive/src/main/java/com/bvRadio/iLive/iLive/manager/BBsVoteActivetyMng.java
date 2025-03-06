package com.bvRadio.iLive.iLive.manager;

import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.BBSVoteActivity;

/**
 * 互动管理投票活动接口
 * @author administrator
 */
public interface BBsVoteActivetyMng {
	
	
	/**
	 * 保存文件
	 * @param bBBSVoteActivity
	 * @return
	 */
	public boolean saveBBSVoteActivity(BBSVoteActivity bBSVoteActivity);
	
	
	/**
	 * 删除单个投票活动
	 * @param id 主键
	 */
	public void deleteBBsVoteActivity(Long id);
	
	/**
	 * 删除多个投票活动
	 * @param ids 主键的数组
	 */
	public void deleteBBsVoteActivityByIds(Long[] ids);
	
	
	/**
	 * 根据主键ID获取数据
	 * @param voteId 主键
	 * @return
	 */
	public BBSVoteActivity selectBBsVoteActivityById(
			Long voteId) throws Exception;

	
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
