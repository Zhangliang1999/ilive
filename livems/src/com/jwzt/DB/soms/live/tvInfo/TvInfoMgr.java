package com.jwzt.DB.soms.live.tvInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.Logger;

public class TvInfoMgr
{
	public int addCodeFlowInfo(TvInfo tvInfo)
	{
		int pkid = PKMgr.getNextId("soms4_live_TvProgram");
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			tvInfo.setPkid(pkid);
			ts = session.beginTransaction();
			session.save(tvInfo);
			ts.commit();
			pkid = tvInfo.getPkid();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("添加电台切换信息失败：" + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return pkid;
	}
	
	public TvInfo getTvInfo(int nPkid)
	{
		Session session  = HibernateUtil.currentSession();
		TvInfo tvInfo = null;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			tvInfo = (TvInfo)session.get(TvInfo.class, nPkid);
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("获取电台切换信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return tvInfo;
	}
	
	public boolean deleteTvInfo(String[] sPkids)
	{
		boolean flag = false;
		
		String sDelIds = "";
		for(int i=0; i<sPkids.length; i++)
		{
			sDelIds = sDelIds + sPkids[i] + ",";
		}
		sDelIds = sDelIds.substring(0, sDelIds.length()-1);
		
		String sDelHql = " from TvInfo c where c.pkid in (" + sDelIds + ")";
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
				TvInfo tvInfo = (TvInfo)I.next();
				session.delete(tvInfo);
			}
			ts.commit();
			
			flag = true;
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("删除电台切换信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return flag;
	}
	
	public int editCodeFlowInfo(TvInfo tvInfo)
	{
		Session session  = HibernateUtil.currentSession();
		Transaction ts = null;
		int nId = -1;
		try
		{
			ts = session.beginTransaction();
			session.update(tvInfo);
			nId = tvInfo.getPkid();
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("修改电台切换信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nId;
	}
	
	public void addBatchTvInfo(String site_id)
	{
		int count = 11;//要添加多少条
		int num = 0;//计数器
		
		int codeFlowId = PKMgr.getNextId("soms4_live_TvProgram",1,count);

		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from TvInfo where site_id=1");
			List codeFlowList = query.list();
			
			for(int i=codeFlowId; i<codeFlowId+count; i++)
			{
				TvInfo codeInfo = (TvInfo)codeFlowList.get(num);
				
				TvInfo info = new TvInfo();
				info.setPkid(i);
				info.setTvid(codeInfo.getTvid());
				info.setTvName(codeInfo.getTvName());
				info.setSite_id(Integer.valueOf(site_id));
				session.save(info);
				num++;
			}
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("添加电台切换信息失败：" + e.getMessage(), 2);
		}
		finally
		{
	 	   HibernateUtil.closeSession();
		}
	}
	public ArrayList getTvInfoList(String site_id)
	{
		int count = 11;//要添加多少条
		int num = 0;//计数器
		int codeFlowId = PKMgr.getNextId("soms4_live_TvProgram",1,count);
		ArrayList list = new ArrayList();
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from TvInfo where site_id="+site_id);
			List tvInfoList = query.list();
			if(tvInfoList!=null && tvInfoList.size()>0){
				for(int i=0;i<tvInfoList.size();i++){
					TvInfo codeInfo = (TvInfo)tvInfoList.get(i);
					TvInfo info = new TvInfo();
					info.setPkid(i);
					info.setTvid(codeInfo.getTvid());
					info.setTvName(codeInfo.getTvName());
					info.setSite_id(Integer.valueOf(site_id));
					list.add(info);
				}
			}
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("添加电台切换信息失败：" + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
}
