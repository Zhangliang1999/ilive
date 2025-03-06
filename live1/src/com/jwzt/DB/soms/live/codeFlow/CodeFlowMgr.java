package com.jwzt.DB.soms.live.codeFlow;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.Logger;

public class CodeFlowMgr
{
	public int addCodeFlowInfo(CodeFlowInfo codeFlowInfo)
	{
		int codeFlowId = PKMgr.getNextId("soms4_live_video_codeflow");
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			codeFlowInfo.setCodeflow_id(codeFlowId);
			ts = session.beginTransaction();
			session.save(codeFlowInfo);
			ts.commit();
			codeFlowId = codeFlowInfo.getCodeflow_id();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("添加码流信息失败：" + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return codeFlowId;
	}
	
	public CodeFlowInfo getCodeFlowInfo(int nCodeFlowId)
	{
		Session session  = HibernateUtil.currentSession();
		CodeFlowInfo codeFlowInfo = null;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			codeFlowInfo = (CodeFlowInfo)session.get(CodeFlowInfo.class, nCodeFlowId);
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("获取码流信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return codeFlowInfo;
	}
	
	public boolean deleteCodeFlowInfo(String[] sCodeFlowIds)
	{
		boolean flag = false;
		
		String sDelIds = "";
		for(int i=0; i<sCodeFlowIds.length; i++)
		{
			sDelIds = sDelIds + sCodeFlowIds[i] + ",";
		}
		sDelIds = sDelIds.substring(0, sDelIds.length()-1);
		
		String sDelHql = " from CodeFlowInfo c where c.codeflow_id in (" + sDelIds + ")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(sDelHql);
			List list = query.list();
			Iterator I = list.iterator();
			while(I.hasNext())
			{
				CodeFlowInfo codeFlowInfo = (CodeFlowInfo)I.next();
				session.delete(codeFlowInfo);
			}
			ts.commit();
			
			flag = true;
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("删除码流信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return flag;
	}
	
	public int editCodeFlowInfo(CodeFlowInfo codeFlowInfo)
	{
		Session session  = HibernateUtil.currentSession();
		Transaction ts = null;
		int nId = -1;
		try
		{
			ts = session.beginTransaction();
			session.update(codeFlowInfo);
			nId = codeFlowInfo.getCodeflow_id();
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("修改码流信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nId;
	}
	
	public void addBatchCodeFlowInfo(String site_id)
	{
		int count = 11;//要添加多少条
		int num = 0;//计数器
		
		int codeFlowId = PKMgr.getNextId("soms4_live_video_codeflow",1,count);

		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from CodeFlowInfo where site_id=1");
			List codeFlowList = query.list();
			
			for(int i=codeFlowId; i<codeFlowId+count; i++)
			{
				CodeFlowInfo codeInfo = (CodeFlowInfo)codeFlowList.get(num);
				
				CodeFlowInfo info = new CodeFlowInfo();
				info.setCodeflow_id(i);
				info.setCodeflow_name(codeInfo.getCodeflow_name());
				info.setBit_rate(codeInfo.getBit_rate());
				info.setFps(codeInfo.getFps());
				info.setVideo_quality(codeInfo.getVideo_quality());
				info.setSecond_per_key(codeInfo.getSecond_per_key());
				info.setSite_id(Integer.valueOf(site_id));
				
				session.save(info);
				
				num++;
			}
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("添加码流信息失败：" + e.getMessage(), 2);
		}
		finally
		{
	 	   HibernateUtil.closeSession();
		}
	}
}
