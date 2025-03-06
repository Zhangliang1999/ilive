package com.jwzt.DB.soms.live.proFile;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.DB.soms.live.codeFlow.CodeFlowInfo;
import com.jwzt.common.Logger;

public class ProFileMgr
{
	public int addProFileInfo(ProFileInfo proFileInfo)
	{
		int nProFileId = PKMgr.getNextId("soms4_live_profile");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			proFileInfo.setProfile_id(nProFileId);
			ts = session.beginTransaction();
			session.save(proFileInfo);
			ts.commit();
			nProFileId = proFileInfo.getProfile_id();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("添加配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return nProFileId ;
	}
	
	public void addProFileInfoCustomId(ProFileInfo proFileInfo)
	{
		//int nProFileId = PKMgr.getNextId("soms4_live_profile");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			//proFileInfo.setProfile_id(nProFileId);
			ts = session.beginTransaction();
			session.save(proFileInfo);
			ts.commit();
			//nProFileId = proFileInfo.getProfile_id();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("添加配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		//return nProFileId ;
	}
	
	public int editProFileInfo(ProFileInfo proFileInfo)
	{
		int nRet = -1;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			session.update(proFileInfo);
			nRet = proFileInfo.getProfile_id();
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("修改配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return nRet;
	}
	
	public boolean deleteProFileInfo(String[] proFileIds)
	{
		boolean flag = false;
		
		String sDelIds = "";
		for(int i=0; i<proFileIds.length; i++)
		{
			sDelIds = sDelIds + proFileIds[i] + ",";
		}
		sDelIds = sDelIds.substring(0, sDelIds.length()-1);
		
		String sDelHql = " from ProFileInfo p where p.profile_id in (" + sDelIds + ")";
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
				ProFileInfo proFileInfo = (ProFileInfo)I.next();
				session.delete(proFileInfo);
			}
			ts.commit();
			flag = true;
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("删除配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return flag;
	}
	
	public ProFileInfo getProFileInfo(int nProFileId)
	{
		ProFileInfo proFileInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			proFileInfo = (ProFileInfo)session.get(ProFileInfo.class, nProFileId);
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("获取配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}
		
		return  proFileInfo;
	}
	
	/**
	 * 根据音频码流ID获取音频码流信息
	 * 
	 * @param nAudioCodeId
	 * @return
	 */
	public LiveAudioCodeInfo getAudioCodeInfo(int nAudioCodeId)
	{
		LiveAudioCodeInfo liveAudioCodeInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			liveAudioCodeInfo = (LiveAudioCodeInfo)session.get(LiveAudioCodeInfo.class, nAudioCodeId);
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("获取音频码流信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}
		
		return  liveAudioCodeInfo;
	}
	
	/**
	 * 根据视频窗口ID获取视频窗口信息
	 * 
	 * @param nAudioCodeId
	 * @return
	 */
	public LiveVideoWinInfo getVideoWinInfo(int nVideoWinId)
	{
		LiveVideoWinInfo liveVideoWinInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		
		try
		{
			ts = session.beginTransaction();
			liveVideoWinInfo = (LiveVideoWinInfo)session.get(LiveVideoWinInfo.class, nVideoWinId);
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("获取音频码流信息失败: " + e.getMessage(), 2);
		}
		finally
		{			
			HibernateUtil.closeSession();
		}
		
		return  liveVideoWinInfo;
	}
	
	public void addBatchProFile(String site_id)
	{
		int count = 16;//要添加多少条
		int num = 0;//计数器
		
		int proFileId = PKMgr.getNextId("soms4_live_profile",1,count);
		//int proFileId = 211;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from ProFileInfo where site_id=1");
			
			List proFileList = query.list();
			
			for(int i=proFileId; i<proFileId+count; i++)
			{
				ProFileInfo proFileInfo = (ProFileInfo)proFileList.get(num);
				
				ProFileInfo info = new ProFileInfo();
				info.setProfile_id(i);
				info.setProfile_name(proFileInfo.getProfile_name());
				info.setIfvideo(proFileInfo.getIfvideo());
				info.setAudio_conf_id(proFileInfo.getAudio_conf_id());
				info.setVideo_code_name(proFileInfo.getVideo_code_name());
				info.setWin_id(proFileInfo.getWin_id());
				info.setVideo_conf_ids(proFileInfo.getVideo_conf_ids());
				info.setProfile_desc(proFileInfo.getProfile_desc());
				info.setSite_id(Integer.valueOf(site_id));
				
				session.save(info);
				
				num++;
			}
			ts.commit();
		}
		catch(Exception e)
		{
			ts.rollback();
			Logger.log("添加配置文件信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
}
