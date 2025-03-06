package com.jwzt.DB.soms.vod.file;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.cdn.mount.MountInfo;
import com.jwzt.DB.cdn.mount.MountMgr;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.DB.soms.vod.catalog.CatalogInfo;
import com.jwzt.DB.soms.vod.catalog.CatalogMgr;
import com.jwzt.DB.soms.vod.catalog.ConvertTaskPublicMethod;
import com.jwzt.DB.soms.vod.convert.task.ConvertTaskInfo;
import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;
import com.jwzt.soms.web.DistributeMgr;
import com.jwzt.soms.web.PlayInfo;

public class FileMgr {
	/**
	 * 根据文件ID获取文件对象
	 * 
	 * @param file_Id
	 *            文件ID
	 * @return 获取的列表对象
	 */
	public FileInfo getFileInfo(Integer file_Id) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		FileInfo fileInfo = null;
		try {
			ts = session.beginTransaction();
			fileInfo = (FileInfo) session.get(FileInfo.class, file_Id);
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return fileInfo;
	}

	/**
	 * 根据Real_Id获取文件对象
	 * 
	 * @param Real_Id
	 * @return 获取的列表对象
	 */
	public FileInfo getFileInfoWithRealId(Integer Real_Id) {
		FileInfo fileInfo = null;
		try {
			List infoList = getFileList("from FileInfo where real_id=" + Real_Id);
			fileInfo = (infoList != null && infoList.size() > 0) ? (FileInfo) infoList.get(0) : fileInfo;
		} catch (Exception e) {
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		return fileInfo;
	}

	public FileInfo getFileInfoWithPriKey(String priKey) {
		FileInfo fileInfo = null;
		try {
			List infoList = getFileList("from FileInfo where guid_2='" + priKey + "'");
			fileInfo = (infoList != null && infoList.size() > 0) ? (FileInfo) infoList.get(0) : fileInfo;
		} catch (Exception e) {
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		return fileInfo;
	}

	

	/**
	 * 添加一个文件
	 * 
	 * @param fileInfo
	 *            要添加的文件对象
	 * @return 添加的文件ID
	 */
	public int addFileInfo(FileInfo fileInfo) {
		int nId = -1;
		Session session = HibernateUtil.currentSession();
		try {
			String fileType = fileInfo.getFile_path().substring(fileInfo.getFile_path().lastIndexOf(".") + 1);
			fileInfo.setVod_format(fileType);
		} catch (Exception ex) {

		}
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			session.save(fileInfo);
			ts.commit();
			nId = fileInfo.getFile_id().intValue();
		} catch (HibernateException e) {
			Logger.log("添加文件信息失败: " + e, 2);
			e.printStackTrace();
			ts.rollback();
		} finally {
			HibernateUtil.closeSession();
		}
		return nId;
	}

	/**
	 * 党校接口添加视频信息
	 * 
	 * @param fileInfo
	 * @return
	 */
	public int addFileInfo2(FileInfo fileInfo) {
		int nId = -1;
		int fileId = PKMgr.getNextId("soms4_vod_file");
		fileInfo.setFile_id(fileId);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			String fileType = fileInfo.getFile_path().substring(fileInfo.getFile_path().lastIndexOf(".") + 1);
			fileInfo.setVod_format(fileType);
		} catch (Exception ex) {

		}
		try {

			ts = session.beginTransaction();
			session.save(fileInfo);
			ts.commit();
			nId = fileId;
		} catch (HibernateException e) {
			Logger.log("添加文件信息失败: " + e, 2);
			e.printStackTrace();
			ts.rollback();
		} finally {
			HibernateUtil.closeSession();
		}
		return nId;
	}

	

	/**
	 * 编辑一个文件
	 * 
	 * @param fileInfo
	 *            要编辑的文件对象
	 * @return 编辑文件的文件ID
	 */
	public int editFileInfo(FileInfo fileInfo) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		int nId = -1;
		try {
			ts = session.beginTransaction();
			session.update(fileInfo);
			nId = fileInfo.getFile_id().intValue();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("修改文件信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return nId;
	}

	

	/**
	 * 根据栏目ID获取文件列表
	 * 
	 * @param catalog_id
	 *            栏目ID
	 * @return 获取的文件列表
	 */
	public List getFileInfoList(Integer catalog_id) {
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery("from FileInfo f where f.catalogInfo.catalog_id=:catalog_id");
			query.setInteger("catalog_id", catalog_id);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	

	/**
	 * 根据栏目ID判断是否存在文件
	 * 
	 * @param id
	 *            栏目ID
	 * @return t=1是存在文件,t=2是不存在文件
	 */
	public int getCatalogId(String id) {
		int t = 0;
		Session session = HibernateUtil.currentSession();

		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery("from FileInfo f where f.catalogInfo.catalog_id=:id");
			query.setString("id", id);
			List list = query.list();
			Iterator it = list.iterator();
			if (it.hasNext())
				t = 1;
			else
				t = 2;
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取栏目ID失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public int getRowCount(String catalogid)// 此方法用于查询分页的总行数,暂时不用
	{
		int num = 0;
		Session session = HibernateUtil.currentSession();
		try {
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("select count(*) from FileInfo f where f.catalogInfo.catalog_id=:catalogid");
			query.setString("catalogid", catalogid);
			Integer count = (Integer) query.uniqueResult();
			num = Integer.parseInt(String.valueOf(count));
			ts.commit();
		} catch (HibernateException e) {
			// System.out.println("查询总行数失败：" + e.getMessage());
			Logger.log("查询总行数失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		return num;
	}

	public List getFileInfo(int pageno, int rowsPer, String catalogid)// 此方法用hibernate实现分页,暂时不用
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		try {
			pageno = (pageno - 1) * 10;
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from FileInfo f where f.catalogInfo.catalog_id=:catalogid order by f.file_id desc");
			query.setString("catalogid", catalogid);
			query.setFirstResult(pageno);
			query.setMaxResults(rowsPer);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			// System.out.println("查询对象失败：" + e.getMessage());
			Logger.log("查询对象失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 删除文件
	 * 
	 * @param file_ids
	 *            文件的ID串
	 */
	public void deleteFileInfo(String file_ids) {
		String batchDelete = "from FileInfo f where f.parent_file_id in(" + file_ids + ")";
		Session session = HibernateUtil.currentSession();
		try {
			Query query = session.createQuery(batchDelete);
			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FileInfo fileInfo = (FileInfo) it.next();
				fileInfo.setFile_state(1);// 删除时将文件的状态更新为1
				editFileInfo(fileInfo);
			}
		} catch (HibernateException e) {
			Logger.log("删除文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * 删除文件及其附件 2008-07-09
	 * 
	 * @param file_ids
	 *            文件的ID串
	 */
	public boolean deleteFile(String file_ids) {
		boolean retValue = false;
		String hql = "delete from FileInfo where file_id in(" + file_ids + ")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = session.beginTransaction();
		try {
			Query query = session.createQuery(hql);
			query.executeUpdate();
			ts.commit();
			retValue = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			Logger.log("删除文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return retValue;
	}

	/**
	 * 删除文件
	 * 
	 * @param real_ids
	 *            realId的字符串
	 */
	public boolean deleteFileWithRealIds(String real_ids) {
		boolean bFlag = false;

		String hql = "delete from FileInfo where guid_2 in(" + real_ids + ")";
		Session session = HibernateUtil.currentSession();
		try {
			Query query = session.createQuery(hql);
			query.executeUpdate();

			bFlag = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			Logger.log("删除文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}

		return bFlag;
	}

	/**
	 * 删除分段文件
	 * 
	 * @param file_ids
	 *            文件的ID串
	 */
	public void deleteFenduanFileInfo(String file_ids) {
		String batchDelete = "from FileInfo f where f.file_id in(" + file_ids + ")";
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Query query = session.createQuery(batchDelete);

			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FileInfo fileInfo = (FileInfo) it.next();
				fileInfo.setFile_state(1);
				this.editFileInfo(fileInfo);

				// 修改主文件分段标识为: 无分段.
				session = HibernateUtil.currentSession();
				query = session.createQuery("from FileInfo f where f.parent_file_id <> file_id and f.parent_file_id=:parent_file_id and f.file_state=0");
				query.setInteger("parent_file_id", fileInfo.getParent_file_id());
				list = query.list();
				Iterator its = list.iterator();
				if (!its.hasNext()) {
					fileInfo = getFileInfo(fileInfo.getParent_file_id());
					fileInfo.setIf_segment(0);
					editFileInfo(fileInfo);
				}
				HibernateUtil.closeSession();
			}
		} catch (HibernateException e) {
			Logger.log("删除分段文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * 删除分段文件及其附件 2008-07-09
	 * 
	 * @param file_ids
	 *            文件的ID串
	 */
	public boolean deleteFenduanFile(String file_ids, Integer parent_file_id) {
		boolean retValue = false;
		String hql = "delete FileInfo where file_id in(" + file_ids + ")";
		// String hqls = "delete from TsFileInfo where file_id
		// in("+file_ids+")";
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.executeUpdate();
			// Query query1 = session.createQuery(hqls);
			// query1.executeUpdate();
			// 修改主文件分段标识为: 无分段.
			query = session.createQuery("from FileInfo where parent_file_id <> file_id and parent_file_id=:parent_file_id");
			query.setInteger("parent_file_id", parent_file_id);
			List list = query.list();
			Iterator it = list.iterator();
			if (!it.hasNext()) {
				FileInfo fileInfo = getFileInfo(parent_file_id);
				fileInfo.setIf_segment(0);// 当主文件下没有分段文件时将if_segment属性置为0
				editFileInfo(fileInfo);
			}
			ts.commit();
			retValue = true;
		} catch (HibernateException e) {
			Logger.log("删除分段文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return retValue;
	}

	/**
	 * 移动文件
	 * 
	 * @param catalog_ids
	 *            选择要移动的文件ID串
	 * @param catalog_id
	 *            要移动到哪个栏目下的栏目ID
	 * @param catalogid
	 *            文件原先所在的栏目ID
	 */
	public boolean moveFileInfo(String catalog_ids, Integer catalog_id, Integer catalogid) {
		boolean bFlag = false;
		Transaction ts = null;
		String batchMove = "";
		Query query = null;
		batchMove = "from FileInfo f where f.file_id in(" + catalog_ids + ") and f.catalogInfo.catalog_id=" + catalogid;
		try {
			CatalogMgr catalogMgr = new CatalogMgr();
			CatalogInfo catalogInfo = catalogMgr.getCatalogInfo(catalog_id);
			int oldRootId = catalogMgr.getCatalogInfo(catalogid).getRoot_id();
			int newRootId = catalogMgr.getCatalogInfo(catalog_id).getRoot_id();
			Session session = HibernateUtil.currentSession();
			ts = session.beginTransaction();
			query = session.createQuery(batchMove);
			// if(query.list().size()==0)
			// {
			// batchMove = "from FileInfo f where f.file_id in("+catalog_ids+")
			// and f.catalogInfo.catalog_id="+catalogid;
			// query = session.createQuery(batchMove);
			// }
			List list = query.list();
			Iterator it = list.iterator();
			FileInfo fileInfo = null;
			int rootId = -1;
			while (it.hasNext()) {
				fileInfo = (FileInfo) it.next();
				rootId = fileInfo.getCatalogInfo().getCatalog_id();
				fileInfo.setCatalogInfo(catalogInfo);
				if (oldRootId == 1 && newRootId != 1) {
					fileInfo.setFile_state(0);
				}
				session.update(fileInfo);
			}
			ts.commit();
			bFlag = true;

		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("移动文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return bFlag;
	}

	
	
	public List getFileList(String batchQuery) {
		Session session = HibernateUtil.currentSession();
		List list = null;
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery(batchQuery);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 根据广告ID获取广告列表
	 * 
	 * @param ad_ids
	 *            广告的ID串
	 * @return 获取的广告列表
	 */
	public List getAd(String[] ad_ids) {
		String batchQuery = "from FileInfo f where f.file_id in(" + StringUtil.stringArray2SqlIn(ad_ids) + ") order by f.file_id desc";
		List list = this.getFileList(batchQuery);
		return list;
	}

	/**
	 * 根据广告ID获取广告列表
	 * 
	 * @param ad_ids
	 *            广告的ID串
	 * @return 获取的广告列表
	 */
	public List getAd(String ad_ids) {
		String batchQuery = "from FileInfo f where f.file_id in(" + ad_ids + ") order by f.file_id desc";
		List list = this.getFileList(batchQuery);
		return list;
	}

	

	public Integer getCPUploadSize(int cp_id) {
		Integer upload_size = 0;
		Session session = HibernateUtil.currentSession();
		try {
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("select sum(file_size) from FileInfo f where f.cp_id=:cpid");
			query.setInteger("cpid", cp_id);
			upload_size = (Integer) query.uniqueResult();
			ts.commit();
		} catch (HibernateException e) {
			// System.out.println("查询总行数失败：" + e.getMessage());
			Logger.log("查询CP上传总量失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		if (upload_size == null) {
			upload_size = 0;
		}
		return upload_size;
	}

//给文件插入转码任务 通用方法
	public void insertConvertTaskMethod(FileInfo fileInfo,int task_code_id){
		try {
			int fileId = fileInfo.getFile_id();
					try {
						ConvertTaskInfo taskInfo = new ConvertTaskInfo();
						ConvertTaskPublicMethod taskMgr = new ConvertTaskPublicMethod();
						taskInfo.setFile_id(fileId);
						taskInfo.setFile_name(fileInfo.getFile_name());
						taskInfo.setTask_status(0);
						taskInfo.setTask_server_id(0);
						String catalog = fileInfo.getCatalogInfo().getCatalog_name() + "(" + fileInfo.getCatalogInfo().getCatalog_id() + ")";
						taskInfo.setTask_node_id(catalog);
						taskInfo.setTask_target_node_id(catalog);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
						String nowTime = sdf.format(new Date());
						taskInfo.setTask_time(nowTime);
						taskInfo.setTask_user("admin");
						taskInfo.setTask_code_id(task_code_id);
						boolean bool = taskMgr.addConvertTask(taskInfo);
						if (bool) {
							System.out.println("判断不符合切片条件后 添加视频转码任务信息成功!");
							Logger.log(Logger.getCurTime2()+" 判断不符合切片条件后 添加视频转码任务信息成功!", 3);
						} else {
							System.out.println("判断不符合切片条件后 添加视频转码任务信息失败!");
							Logger.log(Logger.getCurTime2()+" 判断不符合切片条件后 添加视频转码任务信息失败!", 3);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

		} catch (Exception ex) {

		}
	}
	/* 功能：获取相应发布点下的文件列表 */
	public List getFileListByMountId(int nMountId) {
		List list = null;
		String batchDelete = "from FileInfo i where i.upload_state=2 and i.if_segment=0 and i.file_state=0 and i.mountInfo.server_mount_id=" + nMountId;
		Session session = HibernateUtil.currentSession();
		//Transaction ts = null;
		try {
			Query query = session.createQuery(batchDelete);
			list = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			//ts.rollback();
			Logger.log("删除IP地址失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}

		return list;
	}

	/* 功能：获取相应发布点下的flv文件列表 */
	public List getFlvFileListByMountId(int nMountId) {
		List list = null;
		String batchDelete = "from FileInfo i where i.upload_state=2 and i.if_segment=0 and i.file_state=0 and is_flv='flv' and i.mountInfo.server_mount_id=" + nMountId;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			Query query = session.createQuery(batchDelete);
			list = query.list();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("删除IP地址失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}

		return list;
	}

	public List getFileListByMountId(int nMountId, int nMaxSize, int nStart) {
		List list = null;
		String batchDelete = "from FileInfo i where i.upload_state=2 and i.if_segment=0 and i.file_state=0 and i.mountInfo.server_mount_id=" + nMountId;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			Query query = session.createQuery(batchDelete);
			query.setMaxResults(nMaxSize);
			query.setFirstResult(nStart);
			list = query.list();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("删除IP地址失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}

		return list;
	}

	

	public String getFilePathWithFile(int nFileId) {
		String sFilePath = "";

		FileInfo fileInfo = getFileInfo(nFileId);

		MountInfo mountInfo = new MountMgr().getPublicNodeInfo(fileInfo.getMountInfo().getServer_mount_id());

		sFilePath = mountInfo.getBase_path() + "/" + fileInfo.getFile_path();
		sFilePath = StringUtil.replace(sFilePath, "//", "/");

		return sFilePath;
	}

	

	public List getFileByInterface(String fileIds) {
		List list = null;
		String hql = "from FileInfo where file_id in(" + fileIds + ") order by file_id desc";
		Session session = HibernateUtil.currentSession();
		try {
			Query query = session.createQuery(hql);
			list = query.list();
		} catch (HibernateException e) {
			Logger.log("查找文件失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}

		return list;
	}

	public FileInfo getFileByMountIDAndFilePath(int mount_id, String file_path) {
		FileInfo file = null;
		String hql = "from FileInfo where mountInfo.server_mount_id=" + mount_id + " and file_path='" + file_path + "'";
		try {
			List fileList = getFileList(hql);
			if (fileList != null && fileList.size() > 0) {
				file = (FileInfo) fileList.get(0);
			}
		} catch (Exception e) {
			Logger.log(Logger.getCurTime2()+"  根据发布点ID和路径获取文件失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}

		return file;
	}

	public String addBachFileInfo(ArrayList addfileInfoList) {
		int count = addfileInfoList.size();
		int fileids = PKMgr.getNextId("soms4_vod_file", 1, count);
		String result = "";

		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			for (int i = 0; i < addfileInfoList.size(); i++) {
				FileInfo file = (FileInfo) addfileInfoList.get(i);
				file.setFile_id(fileids);
				file.setParent_file_id(fileids);
				session.save(file);

				fileids++;
			}
			ts.commit();
			result = "true";
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
			Logger.log(Logger.getCurTime2()+" 从C++获取信息添加文件失败: " + e.getMessage(), 2);
			result = "false";
		} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}

	public String editBachFileInfo(List<FileInfo> editfileInfoList) {
		String result = "";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			for (int i = 0; i < editfileInfoList.size(); i++) {
				session.update(editfileInfoList.get(i));
			}
			ts.commit();
			result = "true";
		} catch (Exception e) {
			e.printStackTrace();
			ts.rollback();
			Logger.log(Logger.getCurTime2()+" 从C++获取信息更新文件失败: " + e.getMessage(), 2);
			result = "false";
		} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}

	public FileInfo getFileByFilePath(String file_path) {
		
		FileInfo file = null;
		
		String idsql = "select file_id from soms4_vod_file where file_path = '"+file_path+"' or file_path='/"+file_path+"'";
		System.out.println("idsql=="+idsql);
		int file_Id = 0;
		java.sql.Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		try {
			conn = com.jwzt.common.AppTools.getConnection();
			preStmt = conn.prepareStatement(idsql);
			rs = preStmt.executeQuery();
			while (rs.next()) {
				file_Id = rs.getInt("file_id");
				System.out.println("fileId=="+file_Id);
			}
			if(file_Id!=0){
				file = getFileInfo(file_Id);
			}
		}catch (Exception e) {
			System.out.println("路径获取文件失败: "+e.getMessage());
			Logger.log(Logger.getCurTime2()+" 路径获取文件失败: " + e.getMessage(), 2);
			e.printStackTrace();
		} finally {
			
			com.jwzt.common.AppTools.closeResultSet(rs);
			com.jwzt.common.AppTools.closeStatement(preStmt);
			com.jwzt.common.AppTools.closeConnection(conn);
			
			HibernateUtil.closeSession();
		}
		return file;
	}

	/**
	 * ZHW 2011-06-29
	 * 
	 * @param fileIds
	 *            文件ID的拼串 获取 相应file_id 的文件对象
	 */
	public List getFileInfoByIds(String fileIds) {
		List list = null;
		Session session = HibernateUtil.currentSession();
		try {
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from FileInfo f where f.file_id in('" + fileIds + "')");
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			Logger.log("通过 file_id 获取文件对象失败：" + e.getMessage(), 3);
			throw new RuntimeException(e);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	public int getMp4File(int findFileIdByProgramId) {
		return getMp4File(findFileIdByProgramId, "");
	}

	public int getMp4File(int findFileIdByProgramId, String string) {

		String lsql = "select * from soms4_vod_differ_format where original_vod_id = (select original_vod_id from soms4_vod_differ_format where codec_vod_id = " + findFileIdByProgramId
				+ ") and codec_vod_type = 'mp4'";
		int retStr = 0;
		java.sql.Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		try {
			conn = com.jwzt.common.AppTools.getConnection();
			preStmt = conn.prepareStatement(lsql);
			rs = preStmt.executeQuery();
			if (rs.next()) {
				retStr = rs.getInt("codec_vod_id");
			} else {
				lsql = "select * from soms4_vod_differ_format where original_vod_id = " + findFileIdByProgramId + " and codec_vod_type = 'mp4'";
				preStmt = conn.prepareStatement(lsql);
				rs = preStmt.executeQuery();
				if (rs.next()) {
					retStr = rs.getInt("codec_vod_id");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("查询异常：" + e.getMessage());

		} finally {
			AppTools.closeResultSet(rs);
			rs = null;
			AppTools.closeStatement(preStmt);
			preStmt = null;
			AppTools.closeConnection(conn);
			conn = null;
		}
		return retStr;
	}

	public boolean updateFileInfo(FileInfo fileInfo) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		boolean bet = false;
		try {
			ts = session.beginTransaction();
			session.update(fileInfo);
		
			ts.commit();
			bet = true;
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log(Logger.getCurTime2()+" 修改文件信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return bet;
	}
	public String getMp4Path(FileInfo fileInfo) {
		String lsql = "select http_address,server_mount_name,file_path,accessMethod.umsport from soms4_vod_file vod ,soms4_cdn_server_mount cnd ,soms4_cdn_accessMethods accessMethod , soms4_cdn_server_group scsg , soms4_cdn_server scs where  vod.mount_id =cnd.server_mount_id  and accessMethod.server_id = scs.server_id and scsg.server_group_id=cnd.server_group_id and scsg.server_group_id=scs.server_group_id and default_method = 1 and vod.file_id = " + fileInfo.getFile_id();
		String retStr = "";
		java.sql.Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		String http_add = "";
		String ftp_path = "";
		String file_path = "";
		Integer ums_port = 9600;
		System.out.println("lsql=="+lsql);
		Logger.log(Logger.getCurTime2()+"lsql=="+lsql, 3);
		try {
			conn = com.jwzt.common.AppTools.getConnection();
			preStmt = conn.prepareStatement(lsql);
			rs = preStmt.executeQuery();
			if (rs.next()) {
				http_add = rs.getString("http_address");
				ftp_path = rs.getString("server_mount_name");
				file_path = rs.getString("file_path");
				ums_port =rs.getInt("umsport");
				retStr = "http://" + http_add+":"+ums_port+"/" + ftp_path+"/"+ file_path;
			}
		} catch (Exception e) {
			System.out.println("查询异常：" + e.getMessage());
			Logger.log(Logger.getCurTime2()+"查询异常：" + e.getMessage(), 3);

		} finally {
			AppTools.closeResultSet(rs);
			rs = null;
			AppTools.closeStatement(preStmt);
			preStmt = null;
			AppTools.closeConnection(conn);
			conn = null;
		}
		return retStr;
	}
}
