package com.jwzt.statistic.manager;

import java.util.Date;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.WorkingLog;

/**
 * 工作日志
 * @author YanXL
 *
 */
public interface WorkingLogManager {
	/**
	 * 新增数据
	 * @param workingLog
	 * @throws Exception
	 */
	public void addWorkingLog(WorkingLog workingLog) throws Exception;
	/**
	 * 
	 * 获取日志数据
	 * @param systemId 系统ID
	 * @param modelId 模块ID
	 * @param contentId 内容ID
	 * @param content 内容
	 * @param contentName 内容名称
	 * @param userId 用户ID
	 * @param userName 用户名称
	 * @param terminal 终端
	 * @param startTime 开始时间（时间段查询,如果结束时间为空时 以当前时间为结束时间）
	 * @param endTime   结束时间（时间段查询，如果开始时间为空时，获取结束时间之前所有数据）
	 * @param pageSize  每页数据条数
	 * @param pageNo 页码 
	 * @return
	 * @throws Exception
	 */
	public Pagination selectWorkingLogAll(Integer systemId, Integer modelId,String contentId,
			String content, String contentName, Integer userId,
			String userName, String terminal, Date startTime, Date endTime, Integer pageNo, Integer pageSize) throws Exception;
}
