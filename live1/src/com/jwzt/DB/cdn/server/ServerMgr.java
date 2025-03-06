package com.jwzt.DB.cdn.server;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.jwzt.DB.cdn.accessMethods.AccessMethodsInfo;
import com.jwzt.DB.cdn.accessMethods.AccessMethodsMgr;
import com.jwzt.DB.cdn.ip_manage.IpAddressMgr;
import com.jwzt.DB.cdn.serverGroup.*;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;
import com.jwzt.soms.web.DistributeMgr;

public class ServerMgr
{
	/**
	 * @return	添加CDN中的服务器定义
	 * 
	 * */
	public int addCDNServerInfo(ServerInfo serverInfo)
	{
		int  nServerId = PKMgr.getNextId("soms4_cdn_server");
		
		serverInfo.setServer_id(nServerId);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(serverInfo);
			ts.commit();
		}
		catch(HibernateException e)
		{
			nServerId = -1;
			ts.rollback();
			Logger.log("添加服务器信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nServerId;
	}
	
	public int editCDNServerInfo(ServerInfo serverInfo)
	{
		Session session = HibernateUtil.currentSession();
		int nRet = -1;
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.update(serverInfo);
			nRet = serverInfo.getServer_id();
			ts.commit();
		}
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("修改服务器信息失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nRet;
	}
	
	public boolean delServerInfo(String[] serverIds)
	{
		boolean bRet = false;
		String sDelHql = " from ServerInfo s where s.server_id in ("
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
				ServerInfo serverInfo = (ServerInfo)I.next();
				session.delete(serverInfo);
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
	
	public ServerInfo getServerInfo(int nServerId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		ServerInfo serverInfo = null;
		try
		{
			ts = session.beginTransaction();
			serverInfo = (ServerInfo) session.get(ServerInfo.class, nServerId);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取服务器信息失败：" + e.getMessage(),3);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return serverInfo;
	}
	
	public ServerInfo getMainServerInfo(int nServerGroupId)
	{
		ServerInfo serverInfo = new ServerInfo();
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from ServerInfo s where s.mode=1 and s.server_group.server_group_id="+nServerGroupId);
			List list = query.list();
			Iterator I = list.iterator();
			if (I.hasNext())
			{
				serverInfo = (ServerInfo)I.next();
			}
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			e.printStackTrace();
			Logger.log("获取主服务器信息失败：" + e.getMessage(),3);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		
		return serverInfo;
	}
	
	public List getServerInfoList(int nServerGroupId)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try 
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from ServerInfo s where s.server_group.server_group_id=" + nServerGroupId);
			list = query.list();
			ts.commit();
        }
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("获取服务器信息列表失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	public List getServerInfoList(String sServerGroupIds)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try 
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from ServerInfo s where s.server_group.server_group_id in (" + sServerGroupIds + ")");
			list = query.list();
			ts.commit();
        }
		catch(HibernateException e)
		{
			ts.rollback();
			Logger.log("获取服务器信息列表失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 根据服务器组内的负载均衡策略查找最佳的服务器承担链接
	 * 
	 * @param nServerGroupId
	 * @param url 格式为：主机名+端口号，如124.42.1.100:8080
	 * @param sIp 客户端ip
	 * @return  服务器组内承担链接的服务器(修改返回值为访问方式 xhf 2010-01-08)
	 */
	public AccessMethodsInfo getAddressByStrategy(int nServerGroupId,String url,String sIp)
	{
		AccessMethodsInfo info = null;
	    
	    //获取服务器组信息
	    ServerGroupMgr serverGroupMgr = new ServerGroupMgr();
	    ServerGroupInfo cdnServerGroupInfo = serverGroupMgr.getServerGroupInfo(nServerGroupId);
	    
	    //获取服务器信息
	    ServerInfo serverInfo = new ServerInfo();
	    ServerMgr serverMgr = new ServerMgr();
	    
        //找出该服务器组内所有的服务器
	    List vServerList = new ArrayList();
        vServerList = serverMgr.getServerInfoList(nServerGroupId);
	    
	    //当服务器组的负载均衡策略为网络负载均衡时
	    if(cdnServerGroupInfo.getLoadbalance_mode() == 3)
	    {
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            int nNowConn = 0;
	            int nMaxConn = 0;
	            
	            serverInfo = (ServerInfo)vServerList.get(0);
	            int nMainServerId = serverInfo.getServer_id();
	            for(int i=0; i<vServerList.size(); i++)
	            {
	            	serverInfo = (ServerInfo)vServerList.get(i);
	                if(serverInfo.getServer_id() == 1)
	                {
	                    nMainServerId = serverInfo.getServer_id();
	                }
	                nNowConn = nNowConn + serverInfo.getMonitor_connections();
	                nMaxConn = nMaxConn + serverInfo.getLoadbalance();
	            }
	            if(nNowConn < nMaxConn)
	            {
	                //sAddress = cdnServerGroupInfo.getVirtual_ip()+";"+String.valueOf(nMainServerId);
	                info = getAccessMethod(url, nMainServerId, sIp);
	            }
	        }
	        else
	        {
	            //sAddress = "error";    
	        }
	    }

	    
	    //当服务器组的负载均衡策略为软件随机时
	    if(cdnServerGroupInfo.getLoadbalance_mode() == 2)
	    {
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            int nRandom = 0;
	           	            
                //找出一个随机的不忙的服务器承担连接
                nRandom = (int)(Math.random()*vServerList.size());
                serverInfo = (ServerInfo)vServerList.get(nRandom);                

                info = getAccessMethod(url, serverInfo.getServer_id(), sIp);
	        } 
	    }
	    
	    //当服务器组的负载均衡策略为软件智能时
	    if(cdnServerGroupInfo.getLoadbalance_mode() == 1)
	    {   
	        if(vServerList != null && vServerList.size() > 0)
	        {
	            
	        	//先remove 连接数满的
	            for(int i=0; i<vServerList.size(); i++)
	            {
	            	serverInfo = (ServerInfo)vServerList.get(i);
	            	//连接数超出
	                if(serverInfo.getMonitor_connections()>serverInfo.getLoadbalance())
	                {
	                	if(vServerList.size() == 1)
	                	{
	                		break;
	                	}
	                	vServerList.remove(i);
	                }
	                
	            }
	            
	            //找出连接最小的
	            ServerInfo tempServerInfo = (ServerInfo)vServerList.get(0);
	            for(int i=0; i<vServerList.size(); i++)
	            {
	            	serverInfo = (ServerInfo)vServerList.get(i);
	            	//连接数超出
	                if(tempServerInfo.getMonitor_connections()>serverInfo.getMonitor_connections())
	                {
	                	tempServerInfo = serverInfo;
	                }                
	            }
	            //获取正确的访问方式
                info = getAccessMethod(url, tempServerInfo.getServer_id(), sIp);
            
	        }
	    }
	    
	    return info;
	}
	
    /**
     * 根据tomcat URL ，服务器id以及客户端ip，获取正确的访问方式
     * @author xhf 2009-12-18
     * @param url  访问路径，格式为：主机名+端口号，如124.42.1.100:8080
     * @param serverId  服务器id
     * @param sIp  客户端ip地址
     * @return  访问方式
     */
    public static AccessMethodsInfo getAccessMethod(String url, int nServerId , String sIp)
    {
    	AccessMethodsInfo info = null;
    	
    	AccessMethodsInfo defultMethod = null;
    	IpAddressMgr ipAddressMgr = new IpAddressMgr();
    	
        //根据服务器id得到服务器信息
        ServerInfo serverInfo = new ServerMgr().getServerInfo(nServerId);
     
        //得到该服务器的所有访问方式
        AccessMethodsMgr accessMethodsMgr = new AccessMethodsMgr();
        List<AccessMethodsInfo> methodList = accessMethodsMgr.getAccessMethodsList(" server_id = "+serverInfo.getServer_id());
        
        //如果该服务器只有一种访问方式
        if(methodList!=null && methodList.size()==1)
        {
            //返回该访问方式 
        	info = (AccessMethodsInfo)methodList.get(0);
        }
        else if(methodList!=null && methodList.size()>1)
        {
        	//-------------绑定给定url的访问方式集合---------------
        	List<AccessMethodsInfo> methodList1 = new ArrayList<AccessMethodsInfo>();
        	int count = 0;
        	for(int i=0;i<methodList.size();i++)
        	{
        		AccessMethodsInfo methodInfo = (AccessMethodsInfo)methodList.get(i);
        		if(methodInfo.getDefault_method()==1)
        		{
        			//默认的访问方式
        			defultMethod = methodInfo;
        		}
        		String urlAddress = methodInfo.getUrl_address();
        		if(url!=null &&!"".equals(url)&& methodInfo.getDefault_url()==1)
        		{
        			if(urlAddress.indexOf(":")==-1)
        			{
        				urlAddress += ":8080";
        			} 
        			if(urlAddress.equals(url))
        			{
            			methodList1.add(methodInfo);
            			count++;
        			}
        		}        		
        	}
        	
        	//如果存在唯一的URL绑定，与用户的访问路径（即参数url）相同
        	if(count==1)
        	{
        		//返回该URL对应的访问方式
        		info = (AccessMethodsInfo)methodList1.get(0);
        	}
        	//如果存在多种访问方式的URL绑定，与用户的访问路径（即参数url）相同
            else if(count>1 && sIp!=null && !"".equals(sIp))
        	{     	
            	String userIp = StringUtil.ip2String(sIp);            	
        		//--------------在集合methodList1中，找绑定给定ip段的访问方式集合---------------
            	List<AccessMethodsInfo> methodList2 = new ArrayList<AccessMethodsInfo>();
            	int count2 = 0;
        		for(int i=0;i<methodList1.size();i++)
        		{
        			AccessMethodsInfo methodInfo = (AccessMethodsInfo)methodList1.get(i);
            		String ipSegments = methodInfo.getIp_ids(); 
            		String ipAddressId = null;
            		
            		//注：数据库访问方式表中记录的ip段形式为：bb(107)->aa(106)
            		if(ipSegments!=null && !"".equals(ipSegments))
            		{
	        			String ipSegment[] = StringUtil.splitString2Array(ipSegments, "->");
	            		for(int j=0;j<ipSegment.length;j++)
	            		{
	            			ipAddressId = ipSegment[j].substring(ipSegment[j].lastIndexOf("(")+1, ipSegment[j].lastIndexOf(")"));
	            			//检验客户端ip是否在该访问记录绑定的Ip段中
	            			int nRet = ipAddressMgr.checkIpIsLegal(Long.parseLong(userIp),Integer.parseInt(ipAddressId));
	                		if(nRet>0)
	                		{                    			
	                			methodList2.add(methodInfo);
	                			count2++;
	                		}
	            		}
            		}
        		}
            	
        		//如果只有一个IP段，包含客户端ip（参数sIp）
        		if(count2==1)
        		{
        			//返回这个IP段对应的访问方式
            		info = (AccessMethodsInfo)methodList2.get(0);
        		}
        		//如果有多个IP段，包含客户端ip（参数sIp）
        		else if(count2>1)
        		{
        			//任意返回一个访问方式
        			info = (AccessMethodsInfo)methodList2.get(1);
        		}
        		//------如果methodList2中，所有访问方式都没有绑定给定的ip-------
        		else
        		{
        			return defultMethod;
        		}
        	}
            //没有一个访问方式绑定给定的url,或给定的url为空
            else if(sIp!=null && !"".equals(sIp))
        	{     	
            	String userIp = StringUtil.ip2String(sIp);            	
        		//--------------在集合methodList中，找绑定给定ip段的访问方式集合---------------
            	List<AccessMethodsInfo> methodList3 = new ArrayList<AccessMethodsInfo>();
            	int count3 = 0;
        		for(int i=0;i<methodList.size();i++)
        		{
        			AccessMethodsInfo methodInfo = (AccessMethodsInfo)methodList.get(i);
            		String ipSegments = methodInfo.getIp_ids(); 
            		String ipAddressId = null;
            		
            		//注：数据库访问方式表中记录的ip段形式为：bb(107)->aa(106)
            		if(ipSegments!=null && !"".equals(ipSegments))
            		{
	        			String ipSegment[] = StringUtil.splitString2Array(ipSegments, "->");
	            		for(int j=0;j<ipSegment.length;j++)
	            		{
	            			ipAddressId = ipSegment[j].substring(ipSegment[j].lastIndexOf("(")+1, ipSegment[j].lastIndexOf(")"));
	            			//检验客户端ip是否在该访问记录绑定的Ip段中
	            			int nRet = ipAddressMgr.checkIpIsLegal(Long.parseLong(userIp),Integer.parseInt(ipAddressId));
	                		if(nRet>0)
	                		{                    			
	                			methodList3.add(methodInfo);
	                			count3++;
	                		}
	            		}
            		}
        		}
            	
        		//如果只有一个IP段，包含客户端ip（参数sIp）
        		if(count3==1)
        		{
        			//返回这个IP段对应的访问方式
            		info = (AccessMethodsInfo)methodList3.get(0);
        		}
        		//如果有多个IP段，包含客户端ip（参数sIp）
        		else if(count3>1)
        		{
        			//任意返回一个访问方式
        			info = (AccessMethodsInfo)methodList3.get(1);
        		}
        		//------如果所有访问方式都没有绑定给定ip-------
        		else
        		{
        			return defultMethod;
        		}
        	}
        }
        
        return info;         	
    }	
	
	/**
	 * @return	getServerInfoList 获取详细信息
	 * @param nType 1:server 2:serverGroup 3:region 4:all
	 * @param nId: special ID
	 * */
/*	public Vector getServerInfoList(int nType ,int nId)
	{
	    Vector serverInfoList = new Vector();
		CDNServerInfo serverInfo = null;
				
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		
		String sLog = "";
		String sCon = "";
		StringBuffer sSql = new StringBuffer();
		switch (nType)
		{
			case 1: sCon = " where server_id=" + String.valueOf(nId);
					break;
			case 2: sCon = " where server_group_id=" + String.valueOf(nId);
					break;
			case 3: sCon = " where server_region_id=" + String.valueOf(nId);
					break;
			case 4: sCon = "";
					break;
		}
		sSql.append("select server_id,server_group_id,server_region_id,server_name,address,platform,type,mode");
		sSql.append(",logPath,number_of_cpu,memory_size,band_width,ftp_user");
		sSql.append(",ftp_pwd,loadbalance,monitor_cpu,monitor_mem,monitor_band_width");
		sSql.append(",monitor_connections,last_updated from soms_cdn_server ").append(sCon);
		
		try 
		{
			conn = AppGlobal.getConn();
            preStmt = conn.prepareStatement(sSql.toString());
            rs = preStmt.executeQuery();
            
            while(rs.next())
            {
            	serverInfo = new CDNServerInfo();
            	serverInfo.nServerId = rs.getInt("server_id");
            	serverInfo.nServerGroupId = rs.getInt("server_group_id");
            	serverInfo.nServerGroupId = rs.getInt("server_region_id");
            	serverInfo.sServerName = rs.getString("server_name");
            	serverInfo.sAddress = rs.getString("address");
            	serverInfo.nPlatForm = rs.getInt("platform");
				serverInfo.nType = rs.getInt("type");
				serverInfo.nMode = rs.getInt("mode");
				serverInfo.sLogPath = rs.getString("logPath");
				serverInfo.nNumberOfCPU = rs.getInt("number_of_cpu");
				serverInfo.nMemorySize = rs.getInt("memory_size");
				serverInfo.nBandWidth = rs.getInt("band_width");
				serverInfo.sFtpUser = rs.getString("ftp_user");
				serverInfo.sFtpPwd = rs.getString("ftp_pwd");
				serverInfo.nLoadbalance = rs.getInt("loadbalance");
				serverInfo.fMonitorCPU = rs.getDouble("monitor_cpu");
				serverInfo.fMonitorBandMem = rs.getDouble("monitor_mem");
				serverInfo.fMonitorBandWidth = rs.getInt("monitor_band_width");   
				serverInfo.nMonitorConnections = rs.getInt("monitor_connections");
				serverInfo.dLastUpdated = rs.getTimestamp("last_updated");
				
				serverInfoList.add(serverInfo);
			}
        }
		catch(Exception e)
		{
            sLog = "CDNServerMgr->getServerInfo 获取信息时，出错" + e.toString();
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
		
		return serverInfoList;
	}
*/
/*
	public int getServerGroupSize(int nServerId)
	{
		int nRet = 0;
		CDNServerInfo serverInfo = new CDNServerInfo();
		CDNServerGroupMgr cdnServerGroupMgr = new CDNServerGroupMgr();
		CDNServerGroupInfo cdnServerGroupInfo = new CDNServerGroupInfo();
		serverInfo = getServerInfo(nServerId);
		cdnServerGroupInfo = cdnServerGroupMgr.getServerGroupInfo(serverInfo.nServerGroupId);
		nRet = cdnServerGroupInfo.nDiskSpace * 1024;
		return nRet;
	}
*/
/*
	public boolean ifNoBusy(int nServerId)
	{
		boolean flag = false;
		String sLog="";
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rest = null;
		String sSql = "select * from soms_cdn_server where server_id=? and monitor_connections<loadbalance";
		try 
		{
			conn = AppGlobal.getConn();
			//conn = AppGlobal.getConnection();
            preStmt = conn.prepareStatement(sSql);
            preStmt.setInt(1,nServerId);
            rest = preStmt.executeQuery();
            if(rest.next())
            {
            	flag = true;
            }
        }
		catch(Exception e)
		{
            sLog = "CDNServerMgr->ifBusy 判断时出错" + e.toString();
            Logger.log(sLog, 3);
		}
		finally
		{
            AppTools.closeResultSet(rest);
            rest = null;
			AppTools.closeStatement(preStmt);
            preStmt = null;
            AppTools.closeConnection(conn);
            conn = null;
		}
		return flag;
	}
*/
}
