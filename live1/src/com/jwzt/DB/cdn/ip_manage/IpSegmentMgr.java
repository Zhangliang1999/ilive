package com.jwzt.DB.cdn.ip_manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.Logger;

public class IpSegmentMgr 
{
	public void addIp_segment(IpSegmentInfo ipSegmentInfo)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(ipSegmentInfo);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("添加IP分段失败: " + e.getMessage(), 2);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	public void deleteSegment(int address_id)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			Connection conn = session.connection();
			PreparedStatement ps = conn.prepareStatement("delete from soms4_ip_segment where address_id = ?");
			ps.setInt(1,address_id);
			ps.executeUpdate();
			ts.commit();
		}
		catch (SQLException e)
		{
			ts.rollback();
			Logger.log("删除IP段失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
	}
	public List getIpSegmentInfoList(int address_id)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from IpSegmentInfo i where i.ipAddressInfo.address_id = :address_id");
			query.setInteger("address_id",address_id);
			list = query.list();
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
		return list;
	}
	
	/**
	 * 获取ip段信息集合
	 * @param clause
	 * @return
	 */
	public static List<IpSegmentInfo> getIpSegmentInfoList()
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List<IpSegmentInfo> list = null;
		try
		{
			ts = session.beginTransaction();
			Query query = session.createQuery("from IpSegmentInfo ");
			list = query.list();
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
		return list;
	}
}