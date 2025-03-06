package com.jwzt.DB.soms.live.liveChannel.shift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.DB.soms.live.liveChannel.ChannelInfo;

import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;


public class ChanneShiftlMgr
{
	public int addChannelInfo(ChannelInfoShift channelInfo)
	{
		int nRet = -1;
		int nChannelId = PKMgr.getNextId("soms4_live_channel_shift");
		Session session = HibernateUtil.currentSession();
		
		try
		{
			channelInfo.setChannel_id(nChannelId);
			session.save(channelInfo);
			nRet = nChannelId;
		}
		catch (Exception e)
		{
			Logger.log("添加直播频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}

		return nRet;
	}
	public int addChanne(ChannelInfoShift channelInfo)
	{
		int ret =0;
		Connection conn = null;
		//int nChannelId = PKMgr.getNextId("soms4_live_channel_shift");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{	
			String sql = "insert into soms4_live_channel_shift values(?,?,?,?,?,?,?,?,?,?,?)";
			
			conn = AppTools.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,channelInfo.getChannel_id());
			stmt.setInt(2,channelInfo.getLiveTypeHigh());
			stmt.setInt(3,channelInfo.getLiveTypeLow());
			stmt.setInt(4, channelInfo.getChannel_mount_id());
			stmt.setString(5, channelInfo.getChannel_mountPath());
			stmt.setString(6, channelInfo.getChannel_programPathHigh());
			stmt.setString(7, channelInfo.getChannel_programPathLow());
			stmt.setString(8, channelInfo.getChannel_durationHigh());
			stmt.setString(9, channelInfo.getChannel_durationLow());
			stmt.setString(10, channelInfo.getChannel_create_time());
			stmt.setString(11, channelInfo.getRecord_time());
			ret = stmt.executeUpdate();
			
		}
		catch (Exception e)
		{
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return ret;
	}
	public int editChannelInfo(ChannelInfoShift channelInfo)
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

	public ChannelInfoShift getChannelInfo(int nChannelId)
	{
		ChannelInfoShift channelInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{
			ts = session.beginTransaction();
			channelInfo = (ChannelInfoShift) session.get(ChannelInfoShift.class,
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

	public List getChannelInfos(int nChannelGroupId)
	{
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;

		try
		{	
			ts = session.beginTransaction();
			Query query = session
					.createQuery(" from ChannelInfoShift c where c.channel_group_id = :channel_group_id");
			query.setInteger("channel_group_id", nChannelGroupId);
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
					.createQuery(" from ChannelInfoShift");
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
