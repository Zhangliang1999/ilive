package com.jwzt.soms.web.ums;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import com.jwzt.common.SomsConfigInfo;

/**
 * 
 * 关于ums的操作
 * 
 * @author jwzt
 * 
 */
public class OperateUms {
	private static final String umspath = "\\UMS\\UMSApps";
	private static final String xmlpath = "\\Application.xml";

	/**
	 * 对查询出来的数据和配置文件进行匹配，如果不相同就替换为数据库的数据
	 * 
	 * @return
	 */
	public static void PanUMSStreamPort() {
		// 获取ums配置文件中port
		String umsStreamPort = GetUmsXml.getUMSStreamPort();
		// 获取配置文件里分组id
		String group_idstr = SomsConfigInfo.get("vodServerGroupId");
		Integer findservlerRegion = null;
		try {
			// 获取数据库的umsport
			findservlerRegion = FindAccessMethods.FindservlerRegion(Integer
					.parseInt(group_idstr));
		} catch (NumberFormatException e) {
			System.out.println("整型转换异常");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql执行异常");
			e.printStackTrace();
		}
		String serverport = findservlerRegion.toString();
		// 判断数据库和配置文件是否匹配
		if (!umsStreamPort.equals(serverport)) {
			try {
				boolean updatexmlport = GetUmsXml
						.updatexmlport(findservlerRegion);
				if (updatexmlport) {
					System.out.println("-----umsport修改成功-----");
				}
			} catch (IOException e) {
				System.out.println("修改端口号异常");
			}
		} else {
			System.out.println("config.xml端口匹配");
		}
	}

	/**
	 * 匹配Application.xml
	 * 
	 * @throws Exception
	 */
	public static void FindFiletoUms() throws Exception {
		String group_idstr = SomsConfigInfo.get("vodServerGroupId");
		try {
			// 获取Application.xml相对路径
			List<ServerMountBean> findserverMountName = FindAccessMethods
					.FindserverMountName(Integer.parseInt(group_idstr));
			// 匹配Application.xml中路径
			UpdateFiletoUms(findserverMountName);
		} catch (NumberFormatException e) {
			System.out.println("整型转换异常");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql执行异常");
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 判断路径是否存在文件，有文件判断是否匹配，没有文件复制文件
	 * 
	 * @param serverMountName
	 *            相对路径名字
	 * @param groupId
	 *            服务器组id
	 * @throws IOException
	 */
	public static void UpdateFiletoUms(List<ServerMountBean> findserverMountName)
			throws IOException {
		String filepath = "";
		String finalname = "";
		String homePath = SomsConfigInfo.getHomePath();
		
		for (ServerMountBean s : findserverMountName) {
			StringBuffer sbf = new StringBuffer();
			String serverMountName = s.getServer_mount_name();
			// 判断数据库中查询是否带有/
			if (serverMountName.indexOf("/") != -1) {
				finalname = serverMountName.replace("/", "\\");
				filepath = sbf.append(homePath).append(umspath).append(
						finalname).append(xmlpath).toString();
			} else {
				finalname = "\\" + serverMountName;
				filepath = sbf.append(homePath).append(umspath).append(
						finalname).append(xmlpath).toString();
			}
			File file = new File(filepath);
			// 判断文件是否存在,不存在时新建，存在是判断文件中的内容
			if (file.exists()) {
				// 获取到xml路径
				String virtualDirectory = GetUmsXml
						.getVirtualDirectory(filepath);
				String findusedSpace = s.getBase_path();
				if (findusedSpace != null && virtualDirectory != null) {
					// 判断数据库和xml是否匹配，不匹配修改
					if (!virtualDirectory.equals(findusedSpace)) {
						boolean updateAppxml = GetUmsXml.updateAppxml(filepath,
								findusedSpace);
						if (updateAppxml) {
							System.out.println("app修改成功");
						}
					} else {
						System.out.println("Application.xml配置和数据库匹配");
					}
				}

			} else {
				InputStream inStream = null;
				FileOutputStream fs = null;
				// 获取本地磁盘上的Application.xml
				String old = SomsConfigInfo.getHomePath();
				String appold = old + "\\conf\\Application.xml";
				String substring = filepath.substring(0, filepath
						.lastIndexOf("\\"));
				File dis = new File(substring);
				// 创建新文件夹
				dis.mkdir();
				try {
					int bytesum = 0;
					int byteread = 0;
					File oldfile = new File(appold);
					if (oldfile.exists()) { // 文件存在时
						inStream = new FileInputStream(appold); // 读入原文件
						System.out.println("filepath:"+filepath);
						File newfile = new File(filepath);
						// 创建新的文件
						newfile.createNewFile();
						fs = new FileOutputStream(filepath);
						byte[] buffer = new byte[1024];
						while ((byteread = inStream.read(buffer)) != -1) {
							bytesum += byteread; // 字节数 文件大小
							fs.write(buffer, 0, byteread);
						}
						fs.flush();
						System.out.println("复制成功");
						// 调用本身
						UpdateFiletoUms(findserverMountName);
					}
				} catch (Exception e) {
					System.out.println("复制单个文件操作出错");
					e.printStackTrace();
				} finally {
					inStream.close();
					fs.close();
				}
			}
		}
	}
}
