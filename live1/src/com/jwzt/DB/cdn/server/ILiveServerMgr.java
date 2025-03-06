package com.jwzt.DB.cdn.server;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.Logger;

public class ILiveServerMgr {

	/**
	 * 获取主服务器，根据组ID
	 * 
	 * @param nServerGroupId
	 * @return
	 */
	public ILiveServer getMainServerInfo(int nServerGroupId) {
		ILiveServer serverInfo = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery(
					"from ILiveServer s where s.mode=1 and s.server_group.server_group_id=" + nServerGroupId);
			List list = query.list();
			Iterator I = list.iterator();
			if (I.hasNext()) {
				serverInfo = (ILiveServer) I.next();
			}
			ts.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (null != ts) {
				ts.rollback();
			}
			Logger.log("获取主服务器信息失败：" + e.getMessage(), 3);
		} finally {
			HibernateUtil.closeSession();
		}

		return serverInfo;
	}

}
