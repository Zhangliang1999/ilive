package com.jwzt.DB.common;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	// 配置文件路径
	private static String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";

	// 单体session线程
	private static final ThreadLocal threadLocal = new ThreadLocal();

	private static final Configuration cfg = new Configuration();

	// 工厂类
	private static org.hibernate.SessionFactory sessionFactory;

	public static void iniSessionFactory() {
		if (sessionFactory == null) {
			try {
				cfg.configure(CONFIG_FILE_LOCATION);
				sessionFactory = cfg.buildSessionFactory();
			} catch (Exception e) {
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
	public synchronized static Session currentSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		if (session == null || !session.isConnected()) {
			if (sessionFactory == null) {
				try {
					cfg.configure(CONFIG_FILE_LOCATION);
					sessionFactory = cfg.buildSessionFactory();
				} catch (Exception e) {
					System.err.println("%%%% Error Creating SessionFactory %%%%");
					e.printStackTrace();
				}
			}
			session = sessionFactory.openSession();
			threadLocal.set(session);
		}

		return session;
	}

	/**
	 * 关闭单个ThreadLocal Session 实例
	 * 
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);

		if (session != null) {
			session.close();
			sessionFactory.close();
		}
	}
}
