package com.jwzt.DB.soms.live.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jwzt.common.AppTools;
import com.jwzt.common.SomsConfigInfo;

public enum ILiveMakeScreenRetryTaskMgr {

	INSTANCE;

	public List<Long> getRetryTask() {
		String maxTryTime = SomsConfigInfo.get("maxTryTime");
		if (StringUtils.isEmpty(maxTryTime)) {
			maxTryTime = "30";
		}
		int parseInt = Integer.parseInt(maxTryTime);
		List<Long> listArr = new ArrayList<Long>();
		String sql = "select file_id from ilive_file_retrytask where retry_time<" + parseInt
				+ " and deal_result=0 order by create_time asc ";
		// System.out.println(sql);
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		ResultSet ex = null;
		try {
			ps = conn.prepareStatement(sql);
			ex = ps.executeQuery();
			while (ex.next()) {
				listArr.add(ex.getLong("file_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			AppTools.closeResultSet(ex);
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return listArr;
	}

	/**
	 * 保存任务
	 */
	public void saveTask(ILiveMakeScreenRetryTask retryTask) {
		String sql = "insert into ilive_file_retrytask (file_id,task_node,create_time,deal_result,retry_time) values(?,?,?,?,?)";
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		ResultSet ex = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, retryTask.getFileId());
			ps.setString(2, retryTask.getTaskNode());
			ps.setTimestamp(3, retryTask.getTaskCreateTime());
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AppTools.closeResultSet(ex);
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
	}

	public ILiveMakeScreenRetryTask getTaskById(Long fileId) {
		String sql = "select * from ilive_file_retrytask where file_id=?";
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		ResultSet ex = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, fileId);
			ResultSet executeQuery = ps.executeQuery();
			if (executeQuery.next()) {
				ILiveMakeScreenRetryTask task = new ILiveMakeScreenRetryTask();
				task.setFileId(fileId);
				task.setDealResult(executeQuery.getInt("deal_result"));
				task.setRetryTime(executeQuery.getInt("retry_time"));
				task.setTaskCreateTime(executeQuery.getTimestamp("create_time"));
				task.setTaskNode(executeQuery.getString("task_node"));
				return task;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AppTools.closeResultSet(ex);
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return null;
	}

	/**
	 * 修改任务
	 * 
	 * @param retryTask
	 */
	public void updateTask(ILiveMakeScreenRetryTask retryTask) {
		ILiveMakeScreenRetryTask retryTaskPo = this.getTaskById(retryTask.getFileId());
		if (retryTaskPo != null) {
			String sql = "update ilive_file_retrytask set deal_result=?,retry_time=retry_time+1,create_time=?,task_node=? where file_id=?";
			java.sql.Connection conn = AppTools.getConnection();
			PreparedStatement ps = null;
			ResultSet ex = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, retryTask.getDealResult());
				ps.setTimestamp(2, retryTask.getTaskCreateTime());
				ps.setString(3, retryTask.getTaskNode());
				ps.setLong(4, retryTask.getFileId());
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				AppTools.closeResultSet(ex);
				AppTools.closeStatement(ps);
				AppTools.closeConnection(conn);
			}
		} else {
			this.saveTask(retryTask);
		}
	}

}
