package com.jwzt.DB.soms.live.liveChannel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;


import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;


public class ChannelMgr
{
	public int addChannelInfo(ChannelInfo channelInfo)
	{
		int nRet = -1;
		int nChannelId = PKMgr.getNextId("soms4_live_channel");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			channelInfo.setChannel_id(nChannelId);
			ts = session.beginTransaction();
			session.save(channelInfo);
			ts.commit();
			nRet = nChannelId;
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("添加直播频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}

		return nRet;
	}

	public int editChannelInfo(ChannelInfo channelInfo)
	{
		int nRet = -1;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			ts = session.beginTransaction();
			session.update(channelInfo);
			nRet = channelInfo.getChannel_id();
			ts.commit();
			
		}
		catch (Exception e)
		{  
			ts.rollback();
			Logger.log("修改直播频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nRet;
	}

	public boolean deleteChannelInfo(String[] channelIds)
	{
		boolean flag = false;
		String sDelIds = "";
		for (int i = 0; i < channelIds.length; i++)
		{
			sDelIds = sDelIds + channelIds[i] + ",";
		}
		sDelIds = sDelIds.substring(0, sDelIds.length() - 1);

		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			ts = session.beginTransaction();
			
			String sDelHql = "delete from ProgramInfo where channelInfo.channel_id in("
				+ sDelIds +")";
			Query query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			sDelHql = "delete from PlaybillInfo where channelInfo.channel_id in("
				+ sDelIds +")";
			query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			sDelHql = "delete from SavePlanInfo where channelInfo.channel_id in("
				+ sDelIds +")";
			query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			sDelHql = "delete from CDNLiveInfo where channel_id in("
				+ sDelIds +")";
			query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			sDelHql = "delete from CDNLiveStateInfo where channel_id in("
				+ sDelIds +")";
			query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			
			sDelHql = " delete from ChannelInfo c where c.channel_id in ("
				+ sDelIds + ")";
			query = session.createQuery(sDelHql);
			query.executeUpdate();
			
			ts.commit();
			flag = true;
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("删除直播频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return flag;
	}

	public ChannelInfo getChannelInfo(int nChannelId)
	{
		ChannelInfo channelInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			ts = session.beginTransaction();
		
			channelInfo = (ChannelInfo) session.get(ChannelInfo.class,
					nChannelId);
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("获取直播频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return channelInfo;
	}
	public List<ChannelInfo> getChannelInfos(String nChannelGroupIds)
	{
		List<ChannelInfo> list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{	
			ts = session.beginTransaction();
			Query query = session
					.createQuery(" from ChannelInfo c where c.channel_group_id in(" + nChannelGroupIds + ")");
			list = query.list();
			ts.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ts.rollback();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	public List<ChannelInfo> getChannelInfos(int nChannelGroupId)
	{
		List<ChannelInfo> list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{	
			ts = session.beginTransaction();
			Query query = session
					.createQuery(" from ChannelInfo c where c.channel_group_id = :channel_group_id order by chann_order ,channel_id desc ");
			query.setInteger("channel_group_id", nChannelGroupId);
			list = query.list();
			ts.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ts.rollback();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	/**
	 * 获取所有的频道列表
	 * @return
	 */
	public List getAllChannelList()
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{	
			ts = session.beginTransaction();
			Query query = session
					.createQuery(" from ChannelInfo");
			list = query.list();
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	/**
	 * 获取当前站点下的所有频道列表
	 * @return
	 */
	public List getChannelListBySite(String site_id)
	{
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{	
			String sql = "select channel_id,channel_name from soms4_live_channel c,soms4_vod_catalog v"+
				" where c.channel_group_id=v.catalog_id and v.site_id=? order by channel_id desc";
			
			conn = AppTools.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,site_id);
			
			rs = stmt.executeQuery();
			while(rs.next())
			{
				ChannelInfo info = new ChannelInfo();
				info.setChannel_id(rs.getInt(1));
				info.setChannel_name(rs.getString(2));
				list.add(info);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return list;
	}
	
	public void setChannOrder(int id,int order){
		
		Connection conn = null;
		PreparedStatement preStmt = null;

		try {
			conn = AppTools.getConnection();
			preStmt = conn.prepareStatement(" update soms4_live_channel set chann_order =? where channel_id=?");
			preStmt.setInt(1, order);
			preStmt.setInt(2, id);
			preStmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeStatement(preStmt);
			AppTools.closeConnection(conn);
		}
		
	}
	/**
	 * 获取所有的频道列表
	 * @return
	 */
	public List getAllChannelList(String sHqlConds)
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		if("".equals(sHqlConds))
		{
			list = getAllChannelList();
		}
		else
		{
			try
			{	
				Query query = session
						.createQuery(" from ChannelInfo where 1=1 and " + sHqlConds);
				list = query.list();
			}
			catch (Exception e)
			{
				Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
			}
			finally
			{
				HibernateUtil.closeSession();
			}
		}
		return list;
	}
	
	
	/**
	 * 获取频道信息列表
	 * @param nServerGroupId
	 * 服务器组ID
	 * @param channelType
	 * 频道类型: -1: 所有频道; 0：采集直播; 1：虚拟直播; 2：混和直播(单码流); 3：混和直播(多码流)
	 * @param deviceLiveType
	 * 频道类型: -1: 所有方式; 0：拉方式; 1：推方式;
	 * 		   此参数只有在频道类型为采集直播时有效.
	 * @return
	 */
	public List getChannelInfos(int nServerGroupId, int channelType, int deviceLiveType)
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			String sSubHql = "";
			if(channelType != -1)
			{
				sSubHql = "and c.channel_type=" + channelType;
				if((channelType == 0) && (deviceLiveType != -1))
				{
					sSubHql = sSubHql + " and c.isPost=" + deviceLiveType;
				}
			}
			
			ts = session.beginTransaction();
			Query query = session
					.createQuery(" from ChannelInfo c where 1=1 " + sSubHql + " and c.pub_server_group = :pub_server_group");
			query.setInteger("pub_server_group", nServerGroupId);
			list = query.list();
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("获取直播频道信息列表失败2: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	/**
	 * 
	 * @param nChannelId
	 * @param sNowDate
	 * @param state   1:生效（5A 6A 能取到新任务） 3：不生效(5A 6A 不能取到新任务)
	 * @return
	 */
	public boolean refreshChannelState(int nChannelId, String sNowDate,int state)
	{	
		Calendar calendar = Calendar.getInstance();
		String sCurrentDate = StringUtil.getCalendarAsString(calendar, 0);
		
		int nRet = -1;
		
//		String [] hourtime = sNowDate.trim().split("-");
//		String months =(hourtime[1].length())<2?"0"+hourtime[1]:hourtime[1];
//		String dates =(hourtime[2].length())<2?"0"+hourtime[2]:hourtime[2];
//		String dealhourtime = hourtime[0]+"-"+months+"-"+dates;
//		if(dealhourtime.equals(sCurrentDate))
//		{  
			ChannelInfo info = getChannelInfo(nChannelId);
			info.setIf_refresh(state);
			nRet = editChannelInfo(info);
//		}

		return nRet>0?true:false;
	}
	
	/**
	 * 获取所有的频道列表
	 * @return
	 */
	public List getAllChannelListBySubServer(int isTimeShiftChannel)
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{	
			
			StringBuffer hql = new StringBuffer();
			hql.append(" from ChannelInfo ");
			if(isTimeShiftChannel==1)
			{
				hql.append(" where sub_server_groups = '1'");
			}
			if(isTimeShiftChannel==2)
			{
				hql.append("where sub_server_groups = ''");
			}
			ts = session.beginTransaction();
			Query query = session
					.createQuery(hql.toString());
			list = query.list();
			ts.commit();
		}
		catch (Exception e)
		{
			ts.rollback();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
}
