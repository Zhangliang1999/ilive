package com.jwzt.DB.cdn.accessMethods;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.cdn.serverGroup.ILiveServerGroup;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.Logger;

public class ILiveServerAccessMgr {

	/**
	 * @return CDNServerGroupInfo 获取详细信息
	 * 
	 */
	public ILiveServerGroup getServerGroupInfo(int nServerGroupId) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		ILiveServerGroup serverGroupInfo = null;
		try {
			ts = session.beginTransaction();
			serverGroupInfo = (ILiveServerGroup) session.get(ILiveServerGroup.class, nServerGroupId);
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取服务器信息组信息失败：" + e.getMessage(), 3);
		} finally {
			HibernateUtil.closeSession();
		}
		return serverGroupInfo;
	}

	public List<ILiveServerAccessMethod> getAccessMethodsList(String hql) {
		Session session = HibernateUtil.currentSession();
		List<ILiveServerAccessMethod> list = null;
		try {
			hql = ("".equals(hql)) ? hql : (" and " + hql);
			Query query = session.createQuery(" from ILiveServerAccessMethod where 1=1 " + hql);
			list = query.list();
		} catch (HibernateException e) {
			Logger.log("获取访问方式信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

}
