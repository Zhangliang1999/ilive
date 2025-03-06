package com.jwzt.DB.cdn.accessMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;

public class AccessMethodsMgr
{
	/**
	 * 添加访问方式
	 * 
	 * @author 许业生 2009-12-16
	 * @param taskInfo参数
	 * @return 操作结果，true表示成功，false表示失败
	 */
	public boolean addAccessMehtods(AccessMethodsInfo methodInfo)
	{
		boolean bFlag = false;
		Integer method_id = PKMgr.getNextId("soms4_cdn_accessMethods");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			methodInfo.setMethod_id(method_id);
			// 打开事物
			ts = session.beginTransaction();
			session.save(methodInfo);
			ts.commit();
			bFlag = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			bFlag = false;
			ts.rollback();
			Logger.log("添加访问方式信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bFlag;
	}
	/**
	 * 批量删除访问方式信息
	 * @param Ids
	 * @return
	 */
	public boolean delAccessMethodsInfo(int method_id)
	{
		boolean bRet = false;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Connection conn = session.connection();
			PreparedStatement ps = conn.prepareStatement("delete from soms4_cdn_accessMethods where method_id = ?");
			ps.setInt(1,method_id);
			ps.executeUpdate();
			ts.commit();
			bRet=true;
		}
		catch (Exception e)
		{
			bRet=false;
			ts.rollback();
			Logger.log("删除访问方式失败: " + e.getMessage(), 2);
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
     * 获取访问方式信息
     * @param batchQuery
     * @return
     */
    public List getAccessMethodList(String batchQuery)
	{
		Session session = HibernateUtil.currentSession();
		List list = null;
		try
		{
			Query query = session.createQuery(batchQuery);
			list = query.list();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			Logger.log("获取访问方式信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	/**
	 * 根据访问方式ID获取访问方式信息
	 * @param   method_Id 访问方式ID
	 * @return  获取的列表对象
	 */
	public AccessMethodsInfo getAccessMethodsInfoByMethodId(Integer method_id)
	{
		AccessMethodsInfo methodInfo = null;
		try
		{
			List infoList = getAccessMethodList("from AccessMethodsInfo where method_id=" + method_id);
			methodInfo = (infoList != null && infoList.size()>0)?(AccessMethodsInfo)infoList.get(0):methodInfo;
		}
		catch (Exception e)
		{
			Logger.log("获取访问方式信息失败: " + e.getMessage(), 2);
		}
		return methodInfo;
	}
	/**
	 * 获取访问方式信息
	 * 
	 * @return
	 */
	public List<AccessMethodsInfo> getAccessMethodsList(String hql)
	{
		Session session = HibernateUtil.currentSession();
		List<AccessMethodsInfo> list = null;
		try
		{
			hql = ("".equals(hql))?hql:(" and " + hql);
			Query query = session.createQuery(" from AccessMethodsInfo where 1=1 "+hql);
			list = query.list();
		}
		catch (HibernateException e)
		{
			Logger.log("获取访问方式信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return list;
	}
	/**
	 * 修改访问方式信息
	 * @param mehtodInfo
	 * @return
	 */
	public boolean updateAccessMethod(AccessMethodsInfo mehtodInfo)
	{
		boolean bFlag = false;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.update(mehtodInfo);
			ts.commit();
			bFlag=true;
		}
		catch (Exception e)
		{
			bFlag=false;
			ts.rollback();
			Logger.log("修改访问方式信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bFlag;
	}
	/**
	 * 批量删除访问方式ID
	 * @param serverIds
	 * @return
	 */
	public boolean delMethodsInfo(String[] serverIds)
	{
		boolean bRet = false;
		String sDelHql = " from AccessMethodsInfo A where A.server_id in ("
					   + StringUtil.stringArray2SqlIn(serverIds) + ")";
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
				AccessMethodsInfo methodInfo = (AccessMethodsInfo)I.next();
				session.delete(methodInfo);
			}
			ts.commit();
			bRet = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			ts.rollback();
			Logger.log("删除访问方式信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bRet;
	}

}
