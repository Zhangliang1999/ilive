package com.jwzt.DB.soms.vod.convert.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;

public class ConvertTaskMgr
{
	/**
	 * 将转码任务信息保存到soms4_convert_task表中
	 * 
	 * @author 许业生 2009-11-11
	 * @param taskInfo参数
	 * @return 操作结果，true表示成功，false表示失败
	 */
	public boolean addConvertTask(ConvertTaskInfo taskInfo)
	{
		boolean bFlag = false;
		Integer task_id = PKMgr.getNextId("soms4_convert_task");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			taskInfo.setTask_id(task_id);
			// 打开事物
			ts = session.beginTransaction();
			session.save(taskInfo);
			ts.commit();
			bFlag = true;
		}
		catch (HibernateException e)
		{
			bFlag = false;
			ts.rollback();
			Logger.log("添加转码任务信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bFlag;
	}
	/**
	 * 批量删除分段任务信息
	 * @param Ids
	 * @return
	 */
	public boolean delConvertTaskInfo(String[] Ids)
	{
		boolean bRet = false;
		String sDelHql = " from ConvertTaskInfo  where task_id in ("+ ConvertTaskMgr.stringArray2SqlIn(Ids) + ")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(sDelHql);
			List list = query.list();
			Iterator I = list.iterator();
			while (I.hasNext())
			{
				ConvertTaskInfo convertTaskInfo = (ConvertTaskInfo)I.next();
				session.delete(convertTaskInfo);
			}
			ts.commit();
			bRet = true;
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("删除分段任务信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bRet;
	}
	 /**
     * id数组转换成一个字符串
     * @param sArray   id数组
     * @return    拼凑好的字符串
     */
    public static String stringArray2SqlIn(String[] sArray)
    {
        StringBuffer sBuffer = new StringBuffer(256);
        if (sArray == null)
        {
            return sBuffer.toString();
        }

        int nLen = sArray.length;
        for(int i=0; i<nLen; i++)
        {
            if (i == 0)
            {
                sBuffer.append(sArray[i]);
            }
            else
            {
                sBuffer.append(",").append(sArray[i]);
            }
        }
        return sBuffer.toString();
    }
    /**
     * 获取转码任务信息
     * @param batchQuery
     * @return
     */
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
	 * 根据转码任务ID获取转码任务对象
	 * @param   task_Id 转码任务ID
	 * @return  获取的列表对象
	 */
	public ConvertTaskInfo getConvertTaskInfoByTaskId(Integer task_id)
	{
		ConvertTaskInfo convertTaskInfo = null;
		try
		{
			List infoList = getFileList("from ConvertTaskInfo where task_id=" + task_id);
			convertTaskInfo = (infoList != null && infoList.size()>0)?(ConvertTaskInfo)infoList.get(0):convertTaskInfo;
		}
		catch (Exception e)
		{
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		}
		return convertTaskInfo;
	}
	/**
	 * 重试转码任务
	 * @param convertTaskInfo
	 * @return
	 */
	public boolean editConvertTask(ConvertTaskInfo convertTaskInfo)
	{
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		boolean bool=false;
		try
		{
			ts = session.beginTransaction();
			session.update(convertTaskInfo);
			ts.commit();
			bool=true;
		}
		catch (HibernateException e)
		{
			bool=false;
			ts.rollback();
			Logger.log("重试转码任务信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bool;
	}
	/**
	 * 获取转码任务信息
	 * 
	 * @return
	 */
	public List getConvertTaskList(String hql)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			hql = ("".equals(hql))?hql:(" and " + hql);
			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(" from ConvertTaskInfo where 1=1 "+hql);
			ts.commit();
			list = query.list();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取转码任务信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return list;
	}
	
	

}
