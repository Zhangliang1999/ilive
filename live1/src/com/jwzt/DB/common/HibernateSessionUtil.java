package com.jwzt.DB.common;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jwzt.common.Logger;

public class HibernateSessionUtil implements Serializable
{
	private static final long serialVersionUID = -177878931403052747L;

	//绑定session的ThreadLocal
	private static final ThreadLocal tLocalsess = new ThreadLocal();

	//绑定transaction的ThreadLocal
	private static final ThreadLocal tLocaltx = new ThreadLocal();
	
	//配置文件路径
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	
	//工厂类
	private static org.hibernate.SessionFactory sessionFactory;
	
	private static final Configuration cfg = new Configuration();
	
	static
	{
		if (sessionFactory == null)
		{
			try
			{
				sessionFactory = cfg.configure(CONFIG_FILE_LOCATION).buildSessionFactory();
			}
			catch (Exception e)
			{
				System.err.println("%%%% Error Creating SessionFactory %%%%");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回 ThreadLocal Session 实例
	 * 
	 * @return Session
	 * @throws HibernateException
	 */
	public synchronized static Session currentSession()
	{
		Session session = (Session) tLocalsess.get();

		// open a new one, if none can be found.
		try
		{
			if (session == null)
			{
				session = sessionFactory.openSession();
				tLocalsess.set(session);
			}
		}
		catch (HibernateException e)
		{
			Logger.log(e, 2);
		}
		return session;
	}

	/**
	 * 关闭单个ThreadLocal Session 实例
	 * 
	 * @throws HibernateException
	 */
	public static void closeSession()
	{
		try
		{
			Session session = (Session) tLocalsess.get();
			tLocalsess.set(null);
			if (session != null && session.isOpen())
			{
				session.close();
			}

		}
		catch (HibernateException e)
		{
			Logger.log(e, 2);
		}
	}

	/**
	 * 开始事务
	 */
	public static void beginTransaction()
	{
		Transaction tx = (Transaction) tLocaltx.get();
		try
		{
			if (tx == null)
			{
				tx = currentSession().beginTransaction();
				tLocaltx.set(tx);
			}
		}
		catch (HibernateException e)
		{
			Logger.log(e, 2);
		}
	}

	/**
	 * 提交事务
	 */
	public static void commitTransaction()
	{
		Transaction tx = (Transaction) tLocaltx.get();
		try
		{
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
			{
				tx.commit();
			}
			tLocaltx.set(null);
		}
		catch (HibernateException e)
		{
			Logger.log(e, 2);
		}
	}

	/**
	 * 事务回滚
	 */
	public static void rollbackTransaction()
	{
		Transaction tx = (Transaction) tLocaltx.get();
		try
		{
			tLocaltx.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
			{
				tx.rollback();
			}
		}
		catch (HibernateException e)
		{
			Logger.log(e, 2);
		}
		finally
		{
			closeSession();
		}
	}

	//HibernateSessionUtil.beginTransaction();
	//HibernateSessionUtil.currentSession();
}
