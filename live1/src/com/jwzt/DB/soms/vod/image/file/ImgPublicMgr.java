package com.jwzt.DB.soms.vod.image.file;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;

/**
 * 对视频图片库进行相关操作
 * 
 * @author 许业生 2009-11-04
 */
public class ImgPublicMgr
{
	/**
	 * 将图片信息保存到soms4_img_file表中
	 * 
	 * @author 许业生 2009-11-04
	 * @param img参数
	 * @return 操作结果，true表示成功，false表示失败
	 */
	public boolean addImageFile(ImageFileInfo imageFileInfo)
	{
		boolean bFlag = false;
		Integer img_id = PKMgr.getNextId("soms4_image_file");
		System.out.println(img_id);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			imageFileInfo.setImg_id(img_id);
			// 打开事物
			ts = session.beginTransaction();
			session.save(imageFileInfo);
			ts.commit();
			bFlag = true;
		}
		catch (Exception e)
		{
			bFlag = false;
			ts.rollback();
			Logger.log("添加图片信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bFlag;
	}

	/**
	 * 检查文件类型是否是图片
	 * 
	 * @param fileType
	 * @return 操作结果，true表示成功，false表示失败
	 */
	public static boolean checkIsPictureFile(String fileType)
	{
		
		boolean flag = false;
		String[] imgArray = { "jpg", "bmp", "gif","png" };// 判断文件类型的数组
		for (int i = 0; i < imgArray.length; i++)
		{
			if (imgArray[i].equals(fileType.trim()))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}

	public List getFileList(String batchQuery)
	{
		Session session = HibernateUtil.currentSession();
		List list = null;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(batchQuery);
			list = query.list();
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 根据文件ID获取文件对象
	 * 
	 * @param file_Id 文件ID
	 * @return 获取的列表对象
	 */
	public ImageFileInfo getFileInfoWithFileId(Integer file_id)
	{
		ImageFileInfo fileInfo = null;
		try
		{
			List infoList = getFileList("from ImageFileInfo where file_id=" + file_id);
			fileInfo = (infoList != null && infoList.size() > 0) ? (ImageFileInfo) infoList.get(0) : fileInfo;
		}
		catch (Exception e)
		{
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		return fileInfo;
	}
	
	/**
	 * 根据文件ID获取文件对象
	 * 
	 * @param file_Id 文件ID
	 * @return 获取的列表对象
	 */
	public ImageFileInfo getFileInfoWithImgId(String imgId)
	{
		ImageFileInfo fileInfo = null;
		try
		{
			List infoList = getFileList("from ImageFileInfo where img_id=" + imgId);
			fileInfo = (infoList != null && infoList.size() > 0) ? (ImageFileInfo) infoList.get(0) : fileInfo;
		}
		catch (Exception e)
		{
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		return fileInfo;
	}
	
	/**
	 * 
	 * @param 根据传入的addimgInfoList的数组，批量入库。
	 * @return 返回布尔值
	 * @author Zxl
	 * @serialData 2009.11.11
	 */
	 public String addBachImagesInfo(ArrayList addimgInfoList)
	    {
	    	int count = addimgInfoList.size();
	    	int img_id = PKMgr.getNextId("soms4_image_file",1,count);
	    	String result = "";

	    	Session session = HibernateUtil.currentSession();
			Transaction ts = null;
			try
			{
				ts = session.beginTransaction();
				for(int i=0; i<addimgInfoList.size(); i++)
				{
					ImageFileInfo img = (ImageFileInfo)addimgInfoList.get(i);
					img.setImg_id(img_id);
					session.save(img);

					img_id++;
				}
				ts.commit();
				result = "true";
			}
			catch(Exception e)
			{
				e.printStackTrace();
				ts.rollback();
				Logger.log("从C++获取信息添加文件失败: " + e.getMessage(), 2);
				result = "false";
			}
			finally
			{
				HibernateUtil.closeSession();
			}
	    	return result;
	    }

	/**
     * 通过图片ID图片信息
     * @param property_id 配置信息的账号ID
     * @return 操作结果，true表示成功，false表示失败
     * @author 许业生 2009-11-2
     */
    public boolean delImageFile(String img_id)
    {
    	 Connection conn = null;
    	 boolean retVal = true;
    	 PreparedStatement stmt = null;
         try {
    		 conn = AppTools.getConnection();
        	 if(conn!=null)
        	 {
        		 stmt = conn.prepareStatement(" delete from soms4_image_file where img_id = ?");
        		 stmt.setString(1, img_id);
        		 stmt.executeUpdate();
        	 }
		} catch (Exception e) {
			Logger.log(e, 3);
			retVal=false;
		}finally
		{
			//关闭数据库所有连接
			AppTools.closeStatement(stmt);
			stmt = null;
			HibernateUtil.closeSession();
			AppTools.closeConnection(conn);
			conn = null;
		}
		return retVal;
    }
    
    /**
     * 通过图片ID图片信息
     * @param property_id 配置信息的账号ID
     * @return 操作结果，true表示成功，false表示失败
     * @author 许业生 2009-11-2
     */
    public boolean editImageFile(String img_id,String title)
    {
    	 Connection conn = null;
    	 boolean retVal = false;
    	 PreparedStatement stmt = null;
         try {
    		 conn = AppTools.getConnection();
        	 if(conn!=null)
        	 {
        		 stmt = conn.prepareStatement(" update soms4_image_file set img_name=? where img_id = ?");
        		 stmt.setString(1, title);
        		 stmt.setString(2, img_id);
        		 stmt.executeUpdate();
        		 retVal = true;
        	 }
        	 
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(e, 3);
			retVal=false;
		}finally
		{
			//关闭数据库所有连接
			AppTools.closeStatement(stmt);
			stmt = null;
			HibernateUtil.closeSession();
			AppTools.closeConnection(conn);
			conn = null;
		}
		return retVal;
    }
}
