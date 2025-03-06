package com.jwzt.DB.cdn.serverGroup;

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

public class ServerGroupMgr
{

	
	/**
	 * @return	添加CDN中的服务器组定义
	 * 
	 * */
	public int addCDNServerGroupInfo(ServerGroupInfo serverGroupInfo)
	{
		int  nServerGrpId = PKMgr.getNextId("soms4_cdn_server_group");
		serverGroupInfo.setServer_group_id(nServerGrpId);
		serverGroupInfo.setVirtual_ip("");
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(serverGroupInfo);
			ts.commit();
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("添加服务器组信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nServerGrpId;
	}
	
	public int editServerGroupInfo(ServerGroupInfo serverGroupInfo)
	{
		Session session = HibernateUtil.currentSession();
		int nRet = -1;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.update(serverGroupInfo);
			nRet = serverGroupInfo.getServer_group_id();
			ts.commit();
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("修改服务器组信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nRet;
	}
	
	public boolean editServerGroupByLogicId2Zero(int nLogicalId)
	{
		boolean bRet = false;
		String sLog="";
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer sSql = new StringBuffer(1024);
		sSql.append("update soms4_cdn_server_group set ");
		sSql.append("logical_server_group_id=0 where logical_server_group_id=?");
		try
		{
			Session session = HibernateUtil.currentSession();
            conn = session.connection();
            
            conn.setAutoCommit(false);
            preStmt = conn.prepareStatement(sSql.toString());
            preStmt.setInt(1,nLogicalId);
            preStmt.executeUpdate();
            bRet = true;
            conn.commit();
		}
		catch(Exception e)
		{
            sLog = "CDNServerGroupMgr->editServerGroupByLogicId2Zero 添加信息时，出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
			HibernateUtil.closeSession();
            preStmt = null;
            conn = null;
		}
		return bRet;
	}
	
	public boolean delServerGroupInfo(String[] nServerGroupId)
	{
		boolean bRet = false;
		String sDelHql = " from ServerGroupInfo s where s.server_group_id in ("
					   + StringUtil.stringArray2SqlIn(nServerGroupId) + ")";
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
				ServerGroupInfo serverGroupInfo = (ServerGroupInfo)I.next();
				session.delete(serverGroupInfo);
			}
			ts.commit();
			bRet = true;
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("删除服务器信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bRet;
	}
	
	/**
	 * @return	CDNServerGroupInfo 获取详细信息
	 * 
	 * */
	public ServerGroupInfo getServerGroupInfo(int nServerGroupId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		ServerGroupInfo serverGroupInfo = null;
		try
		{
			ts = session.beginTransaction();
			serverGroupInfo = (ServerGroupInfo) session.get(ServerGroupInfo.class, nServerGroupId);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取服务器信息组信息失败：" + e.getMessage(),3);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return serverGroupInfo;
	}
	
	public List getCDNServerGroupList(int nRegionId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try 
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from ServerGroupInfo s where s.server_region.server_region_id=" + nRegionId);
			list = query.list();
			ts.commit();
        }
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("获取服务器组信息列表失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	
	/**
	 * 得到某个区域的服务器组，或者服务器集群的个数
	 * @param nRegionId
	 * @return
	 */
	/*	
		public Vector getCDNServerGroupListByLogicalId(int nRegionId)
		{
			Vector CDNServerGroupListTemp = new Vector();
			Vector CDNServerGroupList = new Vector();
			Vector logicList = new Vector();
			LogicServerGroupMgr logicServerGroupMgr = new LogicServerGroupMgr();
			CDNServerGroupInfo serverGroupInfo = null;
			LogicServerGroupInfo logicServerGroupInfo = null;
			CDNServerGroupList = getCDNServerGroupListByLogicalId(nRegionId,0);
			
			logicList = logicServerGroupMgr.getLogicServerGroupList(nRegionId);
			for (int i=0;i<logicList.size();i++)
			{
				serverGroupInfo = new CDNServerGroupInfo();
				logicServerGroupInfo = (LogicServerGroupInfo)logicList.get(i);
				int temp = logicServerGroupInfo.nLogicalServerGroupId;
				CDNServerGroupListTemp = getCDNServerGroupListByLogicalId(nRegionId,temp);
				serverGroupInfo = (CDNServerGroupInfo)CDNServerGroupListTemp.get(0);
				CDNServerGroupList.add(serverGroupInfo);
			}
			return CDNServerGroupList;
		}*/
		/**
		 * 根据服务器组集群ID，得到区域的服务器组，或者服务器集群的个数
		 * @param nRegionId
		 * @return
		 */
			
	/*		public Vector getCDNServerGroupListByLogicalId(int nRegionId,int nLogicalServerGroupId)
			{
				Vector CDNServerGroupList = new Vector();
				String sLog="";
				Connection conn = null;
				PreparedStatement preStmt = null;
				ResultSet rs = null;

				StringBuffer sSql = new StringBuffer(1024);
				sSql.append("select server_group_id,server_region_id,server_group_name,distribute_status,");
				sSql.append("server_group_type,virtual_ip,disk_space,public_disk_space,warning_space,");
				sSql.append("left_space,loadbalance_mode,if_monitor,monitor_frequency,logical_server_group_id");
				sSql.append(" from soms_cdn_server_group where server_region_id=? and logical_server_group_id = ?");
				try
				{
					conn = AppGlobal.getConn();

		            preStmt = conn.prepareStatement(sSql.toString());
		            preStmt.setInt(1,nRegionId);
		            preStmt.setInt(2,nLogicalServerGroupId);
		            rs = preStmt.executeQuery();
		            while(rs.next())
		            {
		            	CDNServerGroupInfo serverGroupInfo = new CDNServerGroupInfo();
		            	
		            	serverGroupInfo.nServerGroupId = rs.getInt("server_group_id");
		            	serverGroupInfo.nServerRegionId = rs.getInt("server_region_id");
		            	serverGroupInfo.sServerGroupName = rs.getString("server_group_name");
		            	serverGroupInfo.nDistributeStatus = rs.getInt("distribute_status");
		            	serverGroupInfo.nServerGroupType = rs.getInt("server_group_type");
		            	serverGroupInfo.sVirtualIP = rs.getString("virtual_ip");
		            	serverGroupInfo.nDiskSpace = rs.getInt("disk_space");
						serverGroupInfo.nPublicDiskSpace = rs.getInt("public_disk_space");
						serverGroupInfo.nWarningSpace = rs.getInt("warning_space");
						serverGroupInfo.nLeftSpace = rs.getInt("left_space");
						serverGroupInfo.nLoadBalanceMode = rs.getInt("loadbalance_mode");
						serverGroupInfo.nIfMonitor = rs.getInt("if_monitor");
						serverGroupInfo.nMonitorFrequency = rs.getInt("monitor_frequency");
						serverGroupInfo.nLogicalServerGroupID = rs.getInt("logical_server_group_id");
						CDNServerGroupList.add(serverGroupInfo);
		            }

				}
				catch(Exception e)
				{
		            sLog = "CDNServerGroupMgr->getCDNServerGroupListByLogicalId 获取服务器组列表时，出错" + e.toString();
		            Logger.log(sLog, 3);
				}
				finally
				{
					AppTools.closeResultSet(rs);
					rs = null;
		            AppTools.closeStatement(preStmt);
		            preStmt = null;
		            AppTools.closeConnection(conn);
		            conn = null;
				}
				return CDNServerGroupList;
			}	*/
	
	
	/*	
	public Vector getServerGroupListByLId(int nLogicId)
	{
		Vector groupList = new Vector();
				
		String sLog="";
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		String sSql = "select * from soms_cdn_server_group where logical_server_group_id=?";
		try 
		{
			conn = AppGlobal.getConn();
            preStmt = conn.prepareStatement(sSql);
            preStmt.setInt(1,nLogicId);
            rs = preStmt.executeQuery();
            while (rs.next())
            {
            	CDNServerGroupInfo serverGroupInfo = new CDNServerGroupInfo();
            	serverGroupInfo.nServerGroupId = rs.getInt("server_group_id");
            	serverGroupInfo.nServerRegionId = rs.getInt("server_region_id");
            	serverGroupInfo.sServerGroupName = rs.getString("server_group_name");
            	serverGroupInfo.nDistributeStatus = rs.getInt("distribute_status");
            	serverGroupInfo.nServerGroupType = rs.getInt("server_group_type");
            	serverGroupInfo.sVirtualIP = rs.getString("virtual_ip");
            	serverGroupInfo.nDiskSpace = rs.getInt("disk_space");
				serverGroupInfo.nPublicDiskSpace = rs.getInt("public_disk_space");
				serverGroupInfo.nWarningSpace = rs.getInt("warning_space");
				serverGroupInfo.nLeftSpace = rs.getInt("left_space");
				serverGroupInfo.nLoadBalanceMode = rs.getInt("loadbalance_mode");
				serverGroupInfo.nIfMonitor = rs.getInt("if_monitor");
				serverGroupInfo.nMonitorFrequency = rs.getInt("monitor_frequency");
				
				groupList.add(serverGroupInfo);
            }
        }
		catch(Exception e)
		{
            sLog = "CDNServerGroupMgr->getServerGroupInfo 获取信息时，出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			rs = null;
            AppTools.closeStatement(preStmt);
            preStmt = null;
            AppTools.closeConnection(conn);
            conn = null;
		}
		return groupList;
	}
*/
	
	/*
	public boolean editServerGroupByLogicId(CDNServerGroupInfo serverGroupInfo)
	{
		boolean bRet = false;
		String sLog="";
		Connection conn = null;
		PreparedStatement preStmt = null;
		StringBuffer sSql = new StringBuffer(1024);
		sSql.append("update soms_cdn_server_group set ");
		sSql.append("logical_server_group_id=? where server_group_id=?");
		try
		{
			conn = AppGlobal.getConn();
            preStmt = conn.prepareStatement(sSql.toString());
            preStmt.setInt(1,serverGroupInfo.nLogicalServerGroupID);
            preStmt.setInt(2,serverGroupInfo.nServerGroupId);
            preStmt.executeUpdate();
            bRet = true;
		}
		catch(Exception e)
		{
            sLog = "CDNServerGroupMgr->editServerGroupByLogicId 添加信息时，出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
            AppTools.closeStatement(preStmt);
            preStmt = null;
            AppTools.closeConnection(conn);
            conn = null;
		}
		return bRet;
	}
	*/
	
	/**
	 * @return	getServerRegionList 获取详细信息
	 * 
	 * */
/*	public Vector getServerRegionList(String sServerGroupIds)
	{
		CDNServerGroupInfo serverGroupInfo = null;
		Vector serverInfoList = new Vector();
		String sLog="";
		Connection conn = null;
		Statement Stat = null;
		ResultSet rs = null;
		String sSql = "select DISTINCT(server_region_id) from soms_cdn_server_group " +
				"where server_group_id in ("+sServerGroupIds+")";
		try 
		{
			
			conn = AppGlobal.getConn();
			Stat = conn.createStatement();
            rs = Stat.executeQuery(sSql);
            while (rs.next())
            {
            	serverGroupInfo = new CDNServerGroupInfo();
            	serverGroupInfo.nServerRegionId = rs.getInt("server_region_id");
            	serverInfoList.addElement(serverGroupInfo);
            }
        }
		catch(Exception e)
		{
            sLog = "CDNServerMgr->getServerRegionList 获取信息时，出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			rs = null;
            AppTools.closeStatement(Stat);
            Stat = null;
            AppTools.closeConnection(conn);
            conn = null;
		}
		return serverInfoList;
	}
	public Vector getPrivateSpace(String nServerGroupIds)
	{
		Vector privateSpaceList = new Vector();
		CDNServerGroupInfo serverGroupInfo = new CDNServerGroupInfo();
		String sLog="";
		int nPrivateSpace = -1;
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		String sSql = "select * from soms_cdn_server_group where server_group_id in ("+nServerGroupIds+")";
		try 
		{
			conn = AppGlobal.getConn();
            preStmt = conn.prepareStatement(sSql);
            rs = preStmt.executeQuery();
            while (rs.next())
            {
            	serverGroupInfo.nServerGroupId = rs.getInt("server_group_id");
            	serverGroupInfo.nServerRegionId = rs.getInt("server_region_id");
            	serverGroupInfo.nPrivateDiskSpace = rs.getInt("disk_space") - rs.getInt("public_disk_space");
            	privateSpaceList.add(serverGroupInfo);
            }
        }
		catch(Exception e)
		{
            sLog = "CDNServerMgr->getPrivateSpace 获取信息时，出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			rs = null;
            AppTools.closeStatement(preStmt);
            preStmt = null;
            AppTools.closeConnection(conn);
            conn = null;
		}
		return privateSpaceList;
	}*/
	
	/**
	 * 根据服务器组内的负载均衡策略查找最佳的服务器承担链接
	 * 
	 * @param nServerGroupId
	 * @return  String 服务器组内承担链接的服务器地址:ID  如：192.168.1.58:serverId
	 */
/*	public String getAddressByStrategy(int nServerGroupId)
	{
	    String sAddress = "busy";
	    
	    //获取服务器组信息
	    CDNServerGroupInfo cdnServerGroupInfo = getServerGroupInfo(nServerGroupId);
	    
	    //获取服务器信息
	    CDNServerInfo cdnServerInfo = new CDNServerInfo();
	    CDNServerMgr cdnServerMgr = new CDNServerMgr();
	    Vector vServerList = new Vector();
	    
	    //当服务器组的负载均衡策略为网络负载均衡时
	    if(cdnServerGroupInfo.nLoadBalanceMode == 3)
	    {
	        //返回的地址为服务器组的虚拟IP
	        vServerList = cdnServerMgr.getServerInfoList(nServerGroupId);
	        
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            int nNowConn = 0;
	            int nMaxConn = 0;
	            
	            cdnServerInfo = (CDNServerInfo)vServerList.get(0);
	            int nMainServerId = cdnServerInfo.nServerId;
	            for(int i=0; i<vServerList.size(); i++)
	            {
	                cdnServerInfo = (CDNServerInfo)vServerList.get(i);
	                if(cdnServerInfo.nMode == 1)
	                {
	                    nMainServerId = cdnServerInfo.nServerId;
	                }
	                nNowConn = nNowConn + cdnServerInfo.nMonitorConnections;
	                nMaxConn = nMaxConn + cdnServerInfo.nLoadbalance;
	            }
	            if(nNowConn < nMaxConn)
	            {
	                sAddress = cdnServerGroupInfo.sVirtualIP+";"+String.valueOf(nMainServerId);
	            }
	        }
	        else
	        {
	            sAddress = "error";    
	        }
	    }
	    
	    //当服务器组的负载均衡策略为软件随机时
	    if(cdnServerGroupInfo.nLoadBalanceMode == 2)
	    {
	        //找出该服务器组内所有的服务器
	        vServerList = cdnServerMgr.getServerInfoList(nServerGroupId);
	        
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            int nRandom = 0;
	            float fTempRate = 2; 
	            //循环15次，如果找到就返回地址，否则返回“busy”
	            for(int i=0; i<15; i++)
	            {
	                //找出一个随机的不忙的服务器承担连接
	                nRandom = (int)(Math.random()*vServerList.size());
	                cdnServerInfo = (CDNServerInfo)vServerList.get(nRandom);
	                
	                if(cdnServerInfo.nLoadbalance > 0)
	                {
	                    fTempRate = cdnServerInfo.nMonitorConnections/cdnServerInfo.nLoadbalance;
	                }
	                
	                if(fTempRate < 1)
	                {
	                    sAddress = cdnServerInfo.sAddress+";"+String.valueOf(cdnServerInfo.nServerId);
	                    break;
	                }
	            }
	        } 
	        else
	        {
	            sAddress = "error";    
	        }
	    }
	    
	    //当服务器组的负载均衡策略为软件智能时
	    if(cdnServerGroupInfo.nLoadBalanceMode == 1)
	    {
	        //找出该服务器组内所有的服务器
	        vServerList = cdnServerMgr.getServerInfoList(nServerGroupId);	   
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            float fRate = 2;
	            float fTempRate = 2;
	            int nTempId = 0;
	            for(int i=0; i<vServerList.size(); i++)
	            {
	                cdnServerInfo = (CDNServerInfo)vServerList.get(i);
	                if(cdnServerInfo.nLoadbalance > 0)
	                {
	                    fTempRate = cdnServerInfo.nMonitorConnections/cdnServerInfo.nLoadbalance;
	                }
	                
	                if(fTempRate < fRate)
	                {
	                    fRate = fTempRate;
	                    nTempId = i;
	                }
	            }
	            
	            if(fRate < 1)
	            {
	                cdnServerInfo = (CDNServerInfo)vServerList.get(nTempId);
	                sAddress = cdnServerInfo.sAddress+";"+String.valueOf(cdnServerInfo.nServerId);
	            }
	        }
	        else
	        {
	            sAddress = "error";    
	        }
	    }
	    
	    return sAddress;
	}*/
}
