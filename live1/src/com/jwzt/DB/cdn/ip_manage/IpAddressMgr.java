package com.jwzt.DB.cdn.ip_manage;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.Logger;

public class IpAddressMgr 
{
	public void addIp_manage(IpAddressInfo ipAddressInfo)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(ipAddressInfo);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("添加IP地址失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	public void deleteIpAddress(String catalog_ids)
	{
		String batchDelete = "from IpAddressInfo i where i.address_id in("+catalog_ids+")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			Query query = session.createQuery(batchDelete);
			List list = query.list();
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				IpAddressInfo ipAddressInfo = (IpAddressInfo)it.next();
				new IpSegmentMgr().deleteSegment(ipAddressInfo.getAddress_id());
				session = HibernateUtil.currentSession();
				ts = session.beginTransaction();
				session.delete(ipAddressInfo);
				ts.commit();
				HibernateUtil.closeSession();
			}
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("删除IP地址失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	public void editIpAddress(IpAddressInfo ipAddressInfo)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.update(ipAddressInfo);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("修改IP地址失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	public IpAddressInfo getIpAddressInfo(int address_id)
	{
		Session session = HibernateUtil.currentSession();
		IpAddressInfo ipAddressInfo = null;
		try
		{			
			ipAddressInfo = (IpAddressInfo)session.get(IpAddressInfo.class,address_id);
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			Logger.log("获取IP地址失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return ipAddressInfo;
	}
	
	public IpAddressInfo getIpAddressInfo(String address_name,int address_id )
	{
		String sql = "";
		if(address_id==-1){
			sql = "from IpAddressInfo i where i.address_name='"+address_name+"'";
		}else{
			sql = "from IpAddressInfo i where (i.address_name='"+address_name+"'and i.address_id <>"+address_id+")";
		}
		
		Session session = HibernateUtil.currentSession();
		IpAddressInfo ipAddressInfo = null;
		try
		{			
			Query query = session.createQuery(sql);
			List list = query.list();
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				ipAddressInfo = (IpAddressInfo)it.next();
			}
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			Logger.log("获取IP地址失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return ipAddressInfo;
	}
	
	public void setIp_manage(String catalog_ids,Integer if_use)
	{
		String batchSet = "from IpAddressInfo i where i.address_id in("+catalog_ids+")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery(batchSet);
			List list = query.list();
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				IpAddressInfo ipAddressInfo = (IpAddressInfo)it.next();
				ipAddressInfo.setIf_use(if_use);
				session.update(ipAddressInfo);
			}
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("设置IP是否生效失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	
	/**
	 * 根据条件获取IP段列表
	 * @param sCon
	 * @return
	 */
	public List getIpAddressList(String sCon)
	{
		List ipAddressList = null;
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			String sSql = "";
			if(!sCon.equals(""))
			{
				sSql = " and " + sCon;
			}
			Query query = session.createQuery("from IpAddressInfo where 1=1" + sSql);
			ipAddressList = query.list();
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取IP段列表信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		
		return ipAddressList;
	}
	
	/**
	 * 获取正在使用的所有的IP段
	 * @return
	 */
	public List getUsedIpAddressList()
	{
		return getIpAddressList(" if_use=1");
	}
	/**
	 * 获取不能使用的所有的IP段
	 * @return
	 */
	public List getNoUsedIpAddressList()
	{
		return getIpAddressList(" if_use=0");
	}
	
	/**
	 * 获取可用并不需要登录的IP段
	 * @return
	 */
	public List getUsedAndNoLoginIpAddressList()
	{
		return getIpAddressList(" if_need_login=0 and if_use=1");
	}
	
	/**
	 * 获取可用并允许类型的IP段
	 * @return
	 */
	public List getUsedAndPermitIpAddressList()
	{
		return getIpAddressList(" type=1 and if_use=1");
	}
	
	/**
	 * 获取可用并禁止类型的IP段
	 * @return
	 */
	public List getUsedAndForbidIpAddressList()
	{
		return getIpAddressList(" type=0");
	}
	
	/**
	 * 检查用户的IP地址是否合法
	 * 
	 * @param nUserIpValue  -1：所有的允许的IP  其他：相应的IP段ID
	 * @param nIpAddressId
	 * @return nRet -1：不合法IP(illegalIP)  1：不需要登录的IP(noNeedLoginIP) 
	 * 			 2：在定义的IP段内(illegalIP)  3：在某个禁止的IP段内(illegalIP)
	 */
	public String checkAllIpIsLegal(long nUserIpValue, int nIpAddressId)
	{
		String sRet = "illegalIP";
		int nRet = -1;
		
		boolean bFlag = false;
		List ipAddressList = null;
		
		if(nIpAddressId == -1)
		{
			//所有禁止的IP段
			ipAddressList = getUsedAndForbidIpAddressList();
			if(ipAddressList != null && ipAddressList.size()>0)
			{
				for(int i=0; i<ipAddressList.size(); i++)
				{
					IpAddressInfo ipAddressInfo = (IpAddressInfo)ipAddressList.get(i);
					nRet = checkIpIsLegal(nUserIpValue, ipAddressInfo.getAddress_id());
					if(nRet > -1)
					{
						bFlag = true;
						break;
					}
				}
				if(bFlag)
				{
					sRet = "forbidIP";
					return sRet;
				}
			}
			//所有IP段是否是生效的
			ipAddressList = getNoUsedIpAddressList();
			if(ipAddressList != null && ipAddressList.size()>0)
			{
				for(int i=0; i<ipAddressList.size(); i++)
				{
					IpAddressInfo ipAddressInfo = (IpAddressInfo)ipAddressList.get(i);
					nRet = checkIpIsLegal(nUserIpValue, ipAddressInfo.getAddress_id());
					if(nRet > -1)
					{
						bFlag = true;
						break;
					}
				}
				if(bFlag)
				{
					sRet = "noLegalIP";
					return sRet;
				}
			}
			//所有不需要登录的IP段列表
			ipAddressList = getUsedAndNoLoginIpAddressList();
			if(ipAddressList != null && ipAddressList.size()>0)
			{
				for(int i=0; i<ipAddressList.size(); i++)
				{
					IpAddressInfo ipAddressInfo = (IpAddressInfo)ipAddressList.get(i);
					nRet = checkIpIsLegal(nUserIpValue, ipAddressInfo.getAddress_id());
					if(nRet > -1)
					{
						bFlag = true;
						break;
					}
				}
				if(bFlag)
				{
					sRet = "noNeedLoginIP";
					return sRet;
				}
			}
			//所有允许的IP段
			ipAddressList = getUsedAndPermitIpAddressList();
			if(ipAddressList != null && ipAddressList.size()>0)
			{
				for(int i=0; i<ipAddressList.size(); i++)
				{
					IpAddressInfo ipAddressInfo = (IpAddressInfo)ipAddressList.get(i);
					nRet = checkIpIsLegal(nUserIpValue, ipAddressInfo.getAddress_id());
					if(nRet > -1)
					{
						bFlag = true;
						break;
					}
				}
				if(bFlag)
				{
					sRet = "legalIP";
					return sRet;
				}
			}
		}
		else
		{
			IpAddressInfo ipAddressInfo = getIpAddressInfo(nIpAddressId);
			
			if(ipAddressInfo.getIf_use() == 1)
			{
				nRet = checkIpIsLegal(nUserIpValue, nIpAddressId);
				if(nRet > -1)
				{
					//不需要登录的IP段
					if(ipAddressInfo.getIf_need_login() == 0)
					{
						sRet = "noNeedLoginIP";
						return sRet;
					}
					//禁止的IP段
					if(ipAddressInfo.getType() == 0)
					{
						sRet = "forbidIP";
						return sRet;
					}
					//允许的IP段
					if(ipAddressInfo.getType() == 1)
					{
						sRet = "legalIP";
						return sRet;
					}
				}
			}
		}
		
		return sRet;
	}
	
	/**
	 * 检查用户的IP地址是否在某个IP段内
	 * @param nUserIpValue
	 * @param nIpAddressId
	 * @return
	 */
	public int checkIpIsLegal(long nUserIpValue, int nIpAddressId)
	{
		int nRet = -1;
		
		List ipSegmentList = null;
		IpSegmentMgr ipSegmentMgr = new IpSegmentMgr();
		IpSegmentInfo ipSegmentInfo = new IpSegmentInfo();
		
		IpAddressInfo ipAddressInfo = getIpAddressInfo(nIpAddressId);
		if(ipAddressInfo!=null)
		{
			ipSegmentList = ipSegmentMgr.getIpSegmentInfoList(ipAddressInfo.getAddress_id());
			if(ipSegmentList != null && ipSegmentList.size()>0)
			{
				for(int m=0; m<ipSegmentList.size(); m++)
				{
					ipSegmentInfo = (IpSegmentInfo)ipSegmentList.get(m);
					
					if(ipSegmentInfo.getIp_start_value() <= nUserIpValue && nUserIpValue <= ipSegmentInfo.getIp_end_value())
					{
						nRet = nIpAddressId;
						break;
					}
				}
			}
		}
		return nRet;
	}
	
}