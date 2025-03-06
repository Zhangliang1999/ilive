package com.jwzt.livems.ilive;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.jwzt.common.AppTools;
import com.jwzt.livems.ilive.exception.DbUpdaterException;

public enum ILiveMediaFileMgr {

	INSTANCE;

	public void updateILiveMediaFile(MediaFile mediaFile) throws DbUpdaterException {
		String sql = " update ilive_media_file set duration=?,file_rate=?,width_height=?,file_size=?,file_extension=?,file_cover=?,fetch_info_result=1,media_info_dealState=1 where file_id=? ";
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, mediaFile.getDuration());
			ps.setDouble(2, mediaFile.getBiterate());
			ps.setString(3, mediaFile.getFileWh());
			ps.setInt(4, mediaFile.getFileSize());
			ps.setString(5, mediaFile.getFileExt());
			ps.setString(6, mediaFile.getFileConver());
			ps.setLong(7, mediaFile.getFileId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbUpdaterException();
		} finally {
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}

	}

	public MediaFile getILiveMediaFile() throws SQLException {
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		String sql = " select file_id,file_path from ilive_media_file where file_type=1 and del_flag=0 and fetch_info_result=0 limit 1";
		ResultSet ex = null;
		try {
			ps = conn.prepareStatement(sql);
			ex = ps.executeQuery();
			if (ex.next()) {
				String filePath = ex.getString("file_path");
				long fileId = ex.getLong("file_id");
				MediaFile mfile = new MediaFile();
				mfile.setFileId(fileId);
				mfile.setFilePath(filePath);
				return mfile;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			AppTools.closeResultSet(ex);
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return null;
	}

	public void abandonILiveMediaFile(MediaFile iLiveMediaFile) throws Exception {
		if (iLiveMediaFile != null && iLiveMediaFile.getFileId() != null) {
			String sql = " update ilive_media_file set fetch_info_result=1,media_info_dealState=0 where file_id=? ";
			java.sql.Connection conn = AppTools.getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setLong(1, iLiveMediaFile.getFileId());
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			} finally {
				AppTools.closeStatement(ps);
				AppTools.closeConnection(conn);
			}
		}

	}

	public MediaFile getILiveMediaFileByFileId(Long fileId) {
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		String sql = " select file_id,file_path from ilive_media_file where file_type=1 and del_flag=0 and file_id="
				+ fileId;
		ResultSet ex = null;
		try {
			ps = conn.prepareStatement(sql);
			ex = ps.executeQuery();
			if (ex.next()) {
				String filePath = ex.getString("file_path");
				MediaFile mfile = new MediaFile();
				mfile.setFileId(fileId);
				mfile.setFilePath(filePath);
				return mfile;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			AppTools.closeResultSet(ex);
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return null;
	}

	/**
	 * 更新暂存状态
	 * 
	 * @param fileId
	 * @param isMediaType
	 * @throws Exception
	 */
	public Integer getEnterpriseId(Long fileId) throws Exception {
		if (fileId != null ) {
			String sql = " select enterprise_id from ilive_media_file where file_id=?";
			java.sql.Connection conn = AppTools.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setLong(1, fileId);
				rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt("enterprise_id");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			} finally {
				AppTools.closeResultSet(rs);
				AppTools.closeStatement(ps);
				AppTools.closeConnection(conn);
			}
		}
		return null;
	}
	/**
	 * 根据企业ID查询企业认证状态
	 * 
	 * @param fileId
	 * @param isMediaType
	 * @throws Exception
	 */
	public Integer getEnterpriseCer(Integer enterpriseId) throws Exception {
		if (enterpriseId != null ) {
			String sql = " select cert_status from ilive_enterprise where enterprise_id=?";
			java.sql.Connection conn = AppTools.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setLong(1, enterpriseId);
				rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt("cert_status");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			} finally {
				AppTools.closeResultSet(rs);
				AppTools.closeStatement(ps);
				AppTools.closeConnection(conn);
			}
		}
		return null;
	}
	/**
	 * 更新暂存状态
	 * @param fileId
	 * @param isMediaType
	 * @throws Exception
	 */
	public void updateIsMediaType(Long fileId, Integer isMediaType) throws Exception {
		if (fileId != null && isMediaType != null) {
			String sql = " update ilive_media_file set is_media_type=?,temporary_time=? where file_id=?";
			java.sql.Connection conn = AppTools.getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1, isMediaType);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_YEAR, 3);
				ps.setDate(2, new java.sql.Date(calendar.getTime().getTime()));
				ps.setLong(3, fileId);
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			} finally {
				AppTools.closeStatement(ps);
				AppTools.closeConnection(conn);
			}
		}
	}
}
