package com.jwzt.statistic.dao;

import java.util.List;

import com.jwzt.statistic.entity.FtpServer;

/**
 * ftp 服务器处理
 * @author YanXL
 *
 */
public interface FtpServerDao {
	/**
	 * 获取所有服务器
	 * @return
	 * @throws Exception
	 */
	public List<FtpServer> selectFtpServerAll() throws Exception;
}
