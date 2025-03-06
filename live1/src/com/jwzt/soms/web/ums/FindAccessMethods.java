package com.jwzt.soms.web.ums;

/**
 * 对点播服务器的jdbc
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jwzt.common.AppTools;

/**
 * 查询数据库中关于ums
 * 
 * @author jwzt
 * 
 */
public class FindAccessMethods {
	/**
	 * 根据分组id获取他下面mode_tag为1的服务器的umsport
	 * 
	 * @param group_id
	 * @return
	 * @throws SQLException
	 */
	public static int FindservlerRegion(int group_id) throws SQLException {
		int umsport = 0;
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		String sql = "select a.umsport from ilive_server_access_method a left join ilive_server b on a.server_id=b.server_id " +
				"where b.group_id=?";
		try {
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, group_id);
			ResultSet ex = ps.executeQuery();
			if (ex.next()) {
				umsport = ex.getInt("umsport");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
		return umsport;
	}

	/**
	 * 查询文件夹名字
	 * 
	 * @param group_id
	 *            服务器组id
	 * @return
	 * @throws SQLException
	 */
	public static List<ServerMountBean> FindserverMountName(int group_id)
			throws SQLException {
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		String sql = "select server_mount_name,base_path from soms4_cdn_server_mount where server_group_id=?";
		List<ServerMountBean> list = new ArrayList<ServerMountBean>();
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, group_id);
			ResultSet ex = ps.executeQuery();
			ServerMountBean b = null;
			while (ex.next()) {
				b = new ServerMountBean();
				b.setServer_mount_name(ex.getString("server_mount_name"));
				b.setBase_path(ex.getString("base_path"));
				list.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
		return list;
	}

	/**
	 * 获取点播服务器路径
	 * 
	 * @param group_id
	 * @return
	 * @throws SQLException
	 */
	public static String FindusedSpace(int group_id) throws SQLException {
		String used_space = "";
		java.sql.Connection conn = AppTools.getConnection();
		PreparedStatement ps = null;
		String sql = "select [base_path] from [soms4_cdn_server_mount] where [server_group_id]=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, group_id);
			ResultSet ex = ps.executeQuery();
			if (ex.next()) {
				used_space = ex.getString("base_path");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
		return used_space;
	}
}
