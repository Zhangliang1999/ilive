package com.jwzt.statistic.manager;

import java.util.List;

import com.jwzt.statistic.entity.FtpServer;

/**
 * Ftp 服务
 * @author YanXL
 *
 */
public interface FtpServerManager {

	/**
	 * 获取所有服务器
	 * @return
	 * @throws Exception
	 */
	public List<FtpServer> selectFtpServerAll();
}
