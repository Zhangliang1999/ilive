package com.jwzt.DB.soms.vod.convert.codec;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.DB.soms.vod.convert.task.ConvertTaskInfo;
import com.jwzt.common.Logger;

/**
 * 视频格式管理类，负责数据库操作
 * @author xhf 2009-11-16
 *
 */
public class ConvertCodecMgr
{
	/**
	 * 添加一条视频格式信息
	 * @param codecInfo 要插入的视频格式信息类
	 * @return 视频id
	 */
	public int addConvertCodecInfo(ConvertCodecInfo codecInfo)
	{	
		int codecId = PKMgr.getNextId("soms4_convert_codec");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			codecInfo.setCodecId(codecId);
			ts = session.beginTransaction();
			session.save(codecInfo);
			ts.commit();
			codecId = codecInfo.getCodecId();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("添加视频格式失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return codecId ;
	}
	/**
	 * 修改视频格式信息
	 * @param codecInfo
	 * @return
	 */
	public int editConvertCodecInfo(ConvertCodecInfo codecInfo)
	{
		int nRet = -1;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			session.update(codecInfo);
			nRet = codecInfo.getCodecId();
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("修改视频格式信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return nRet;
	}

	/**
	 * 批量删除视频格式
	 * @param codecIds 所有要删除的视频格式id
	 * @return 操作成功与否 true：成功， false：失败
	 */
	@SuppressWarnings("unchecked")
	public boolean deleteCodecInfo(String[] codecIds)
	{
		boolean flag = false;
		
		String sDelIds = "";
		for(int i=0; i<codecIds.length; i++)
		{
			sDelIds = sDelIds + codecIds[i] + ",";
		}
		sDelIds = sDelIds.substring(0, sDelIds.length()-1);
		
		String sDelHql = " from ConvertCodecInfo c where c.codecId in (" + sDelIds + ")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(sDelHql);
			List<ConvertCodecInfo> list = query.list();
			Iterator<ConvertCodecInfo> I = list.iterator();
			while(I.hasNext())
			{
				ConvertCodecInfo codecInfo = (ConvertCodecInfo)I.next();
				session.delete(codecInfo);
			}
			ts.commit();
			flag = true;
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("删除视频格式失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return flag;
	}

	/**
	 * 根据视频格式id返回视频格式信息
	 * @param nCodecId 视频格式id
	 * @return 视频格式信息类
	 */
	public ConvertCodecInfo getConvertCodecInfo(int nCodecId)
	{
		ConvertCodecInfo codecInfo = null;
		Session session = HibernateUtil.currentSession();		
		try
		{
			codecInfo = (ConvertCodecInfo)session.get(ConvertCodecInfo.class, nCodecId);
		}
		catch(Exception e)
		{
			Logger.log("获取视频格式信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}		
		return  codecInfo;
	}
	
	/**
	 * 获取所有的视频格式
	 * @return 所有的视频格式
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllFileType()
	{
		List<String> list = null;
		Session session = HibernateUtil.currentSession();
			
		try
		{
			Query query = session.createQuery("select distinct codecFiletype from ConvertCodecInfo group by codecFiletype");
			list = query.list();
		}
		catch(Exception e)
		{
			Logger.log("获取视频格式信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}
		
		return  list;
	}
	/**
	 * 根据视频格式id返回视频格式信息
	 * @param nCodecId 视频格式id
	 * @return 视频格式信息类
	 */
	public ConvertCodecInfo getConvertCodecInfoDefault()
	{
		ConvertCodecInfo codecInfo = null;
		Session session = HibernateUtil.currentSession();		
		try
		{
			List infoList = getCodeList("from ConvertCodecInfo where isdefault=1");
			codecInfo = (infoList != null && infoList.size()>0)?(ConvertCodecInfo)infoList.get(0):codecInfo;
		}
		catch(Exception e)
		{
			Logger.log("获取视频格式信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}		
		return  codecInfo;
	}
	private List getCodeList(String hql) {
		Session session = HibernateUtil.currentSession();
		List list = null;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(hql);
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
	
}
