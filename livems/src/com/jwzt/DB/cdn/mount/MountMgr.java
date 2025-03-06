package com.jwzt.DB.cdn.mount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import com.jwzt.common.StringUtil;

public class MountMgr 
{
	public int addPublicNodeInfo(MountInfo mountInfo)
	{	
		int nMountId = PKMgr.getNextId("soms4_cdn_server_mount");
		mountInfo.setServer_mount_id(nMountId);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(mountInfo);
			ts.commit();
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("添加发布点信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nMountId;
	}
	
	public int editPublicNodeInfo(MountInfo mountInfo)
	{
		Session session = HibernateUtil.currentSession();
		int nRet = -1;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.update(mountInfo);
			nRet = mountInfo.getServer_mount_id();
			ts.commit();
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("修改发布点信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nRet;
	}
	
	public boolean delMountInfo(String[] mountIds)
	{
		boolean bRet = false;
		String sDelHql = " from MountInfo m where m.server_mount_id in ("
					   + StringUtil.stringArray2SqlIn(mountIds) + ")";
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
				MountInfo mountInfo = (MountInfo)I.next();
				session.delete(mountInfo);
			}
			ts.commit();
			bRet = true;
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("删除发布点信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bRet;
	}
	
	
	
	public MountInfo getPublicNodeInfo(int nMountId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		MountInfo mountInfo = null;
		try
		{
			ts = session.beginTransaction();
			mountInfo = (MountInfo) session.get(MountInfo.class, nMountId);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取发布点信息失败：" + e.getMessage(),3);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return mountInfo;
	}
	
	public List getMountList(int nServerGroupId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from MountInfo where server_group.server_group_id = "+nServerGroupId+" order by server_mount_id desc");
			list = query.list();
			ts.commit();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			Logger.log("获取发布点列表信息失败: " + e.getMessage(), 3);
			ts.rollback();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	public List getMountListByType(int nServerGroupId, int mount_type)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			String sql = nServerGroupId != -1 ? " and server_group.server_group_id="+nServerGroupId : "";
			Query query = session.createQuery("from MountInfo where mount_type = :mount_type"+sql+" order by server_mount_id desc");
			query.setInteger("mount_type",mount_type);
			list = query.list();
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取发布点列表信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	public List getMountList(String sHqlConds)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			sHqlConds = (!"".equals(sHqlConds))?("and "+sHqlConds):sHqlConds;
			ts = session.beginTransaction();
			Query query = session.createQuery("from MountInfo m where 1=1 "+sHqlConds);
			list = query.list();
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取发布点列表信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	public MountInfo getRandomMountInfo(int nServerGroupId)
	{
		MountInfo mountInfo = new MountInfo();
		List mountList = getMountList(nServerGroupId);
		if(mountList.size() > 0)
		{
			int nRandom = (int)(Math.random()*mountList.size());
			mountInfo = (MountInfo)mountList.get(nRandom);
		}
		
		return mountInfo;
	}
	
	public List getMountsByRegion(int regionId)
	{
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			String sql = "select server_mount_id,mount_desc from soms4_cdn_server_mount m,soms4_cdn_server_group g"+
				" where m.server_group_id=g.server_group_id and m.mount_type=1 and server_region_id=? order by server_mount_id desc";
			
			conn = AppTools.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,regionId);
			
			rs = stmt.executeQuery();
			while(rs.next())
			{
				MountInfo mountInfo = new MountInfo();
				mountInfo.setServer_mount_id(rs.getInt(1));
				mountInfo.setMount_desc(rs.getString(2));
				list.add(mountInfo);
			}
		}
		catch (Exception e)
		{
			Logger.log("获取发布点失败: " + e.getMessage(), 2);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return list;
	}

	

}
