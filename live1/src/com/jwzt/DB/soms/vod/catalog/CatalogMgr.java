package com.jwzt.DB.soms.vod.catalog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;

/**
 * 
 * @author 刘勇涛
 *
 * 该类封装了对发布点这张表的所有数据库操作
 */

public class CatalogMgr
{
	public List getCatalogList()
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session
					.createQuery("from com.jwzt.DB.soms.vod.catalog.CatalogInfo where catalog_state=0 order by parent_id");
			ts.commit();
			list = query.list();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	/**
	 * 曲明志
	 * @param nType
	 * @return
	 * 根据不同的类型返回相应的树 1:点播栏目树  2:广告类型树  3:直播频道组树
	 */
	public List getCatalogListByType(int nType,HttpSession httpSession)
	{
		List list = null;
		int site_id = -1;
		
		try
		{
			if(SomsConfigInfo.get("isDisdictBySite").trim().equals("1"))
			{
				String temp_site_id=(String)httpSession.getAttribute("cms_site_id");
				site_id = Integer.parseInt(temp_site_id);
			}
			
			list = getCatalogListByType(nType, site_id);
		}
		catch (HibernateException e)
		{
			Logger.log("获取树失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	
	/**
	 * 曲明志
	 * @param
	 * @return
	 */
	public List getCatalogList(String sSubHql)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(" from ChannelInfo where 1=1 and " + sSubHql);
			ts.commit();
			list = query.list();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取树失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	
	/**
	 * 许海峰
	 * @param hql子句
	 * @return 
	 */
	public List getCatalogInfo(String sSubHql)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try
		{
			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(" from CatalogInfo where 1=1 and " + sSubHql);
			ts.commit();
			list = query.list();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();			ts.rollback();
			Logger.log("获取树失败: " + e.getMessage(), 2);
		}
		finally
		{
			try
			{
				HibernateUtil.closeSession();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		}
		return list;
	}
	
	/**
	 * 曲明志
	 * @param nType
	 * @return
	 * 根据不同的类型返回相应的树 1:点播栏目树  2:广告类型树  3:直播频道组树
	 */
	public List getCatalogListByType(int nType, int nSiteId)
	{
		Session session = HibernateUtil.currentSession();
		//Transaction ts = null;
		List list = null;
		try
		{
			String sSql = null;
			if(nSiteId == -1)
			{
				sSql = "from com.jwzt.DB.soms.vod.catalog.CatalogInfo where catalog_type="+nType+" order by parent_id, node_archive_day desc";
			}
			else
			{
				sSql = "from com.jwzt.DB.soms.vod.catalog.CatalogInfo where catalog_type="+nType+" and (site_id=0 or site_id="+nSiteId+") order by parent_id, node_archive_day desc";
			}
			
			//ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(sSql);
			//ts.commit();
			list = query.list();
		}
		catch (HibernateException e)
		{
			//ts.rollback();
			Logger.log("获取树失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return list;
	}
	

	public int addCatalog(CatalogInfo catalogInfo)//该方法用于添加栏目时被调用
	{
		int nCatalog_id=0;
		int catalog_id=PKMgr.getNextId("soms4_vod_catalog");
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			catalogInfo.setCatalog_id(catalog_id);

			if(catalogInfo.getRoot_id() == 0)
			{
				catalogInfo.setRoot_id(catalog_id);
			}
			ts = session.beginTransaction();
			session.save(catalogInfo);
			ts.commit();
			nCatalog_id = catalogInfo.getCatalog_id().intValue();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			ts.rollback();
			Logger.log("添加栏目失败: " + e, 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nCatalog_id;
	}
	
	public int addCatalog2(CatalogInfo catalogInfo)
	{
		int nRet = -1;
		
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try
		{
			ts = session.beginTransaction();
			session.save(catalogInfo);
			ts.commit();
			nRet = catalogInfo.getCatalog_id().intValue();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("添加栏目2失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nRet;
	}
	
	

	public CatalogInfo getCatalogInfo(Integer nCatalog_Id)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		CatalogInfo catalogInfo = null;
		try
		{
			ts = session.beginTransaction();
			catalogInfo = (CatalogInfo) session.get(CatalogInfo.class,
					nCatalog_Id);
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("获取栏目失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return catalogInfo;
	}

	public int editCatalogInfo(CatalogInfo catalogInfo)
	{
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		int nId = -1;
		try
		{
			ts = session.beginTransaction();
			session.update(catalogInfo);
			nId = catalogInfo.getCatalog_id().intValue();
			ts.commit();
		}
		catch (HibernateException e)
		{
			ts.rollback();
			Logger.log("修改栏目失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return nId;
	}

	public int getCatalogParentid(String id)//此方法暂时不用
	{
		int t = 0;
		Session session = HibernateUtil.currentSession();
		try
		{
			Transaction ts = session.beginTransaction();
			Query query = session
					.createQuery("from CatalogInfo c where c.parent_id=:id");
			query.setString("id", id);
			List list = query.list();
			Iterator it = list.iterator();
			if (it.hasNext())
				t = 1;
			else
				t = 2;
			ts.commit();
		}
		catch (HibernateException e)
		{
			//System.out.println("添加栏目失败：" + e.getMessage());
			Logger.log("添加栏目失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return t;
	}
	
	/**
	 * 党校根据节点名找应的bean
	 * @param name
	 * @return
	 */
	public CatalogInfo getCatalogInfoName(String name)//此方法暂时不用
	{
		CatalogInfo cataInfo = null;
		Session session = HibernateUtil.currentSession();
		try
		{
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from CatalogInfo c where c.catalog_name='"+name+"'");
			List list = query.list();
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				cataInfo = (CatalogInfo)it.next();
			}
				
			ts.commit();
		}
		catch (HibernateException e)
		{
			//System.out.println("添加栏目失败：" + e.getMessage());
			Logger.log("党校根据节点名找应的bean：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return cataInfo;
	}
	
	public List getCatalogSiteList(int nType)
	{
		Session session = HibernateUtil.currentSession();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rest = null;
		List siteList = new ArrayList();
		
		try
		{
			conn = session.connection();
			String sSql = "select distinct site_id from soms4_vod_catalog where catalog_type=" + nType + " group by site_id";
			stat = conn.prepareStatement(sSql);
			rest = stat.executeQuery();
			
			int nSiteId = -1;
			while(rest.next())
			{
				nSiteId = rest.getInt("site_id");
				siteList.add(nSiteId);
			}
		}
		catch (Exception e)
		{
			//System.out.println("获取站点列表失败：" + e.getMessage());
			Logger.log("获取站点列表失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		}
		finally
		{
			com.jwzt.common.AppTools.closeResultSet(rest);
			com.jwzt.common.AppTools.closeStatement(stat);
			com.jwzt.common.AppTools.closeConnection(conn);
		}
		return siteList;
	}

	public int getCatalogId(int parent_id)
	{
		int t = 0;
		Session session = HibernateUtil.currentSession();
		try
		{
			Transaction ts = session.beginTransaction();
			Query query = session
					.createQuery("from CatalogInfo c where c.catalog_id=:parent_id");
			query.setInteger("parent_id", parent_id);
			List list = query.list();
			Iterator it = list.iterator();
			if (it.hasNext())
				t = 1;
			else
				t = 2;
			ts.commit();
		}
		catch (HibernateException e)
		{
			//System.out.println("添加栏目失败：" + e.getMessage());
			Logger.log("添加栏目失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return t;
	}
	
	/**
	 * 删除视频栏目
	 * @author xhf 2009.10.23
	 * @param catalogIds  要删除的栏目id
	 * @return 删除成功还是失败
	 */
	public boolean deleteCatalog(String catalogIds)
	{
		boolean bFlag = false;
		String hql = "delete from CatalogInfo where catalog_id in ("+catalogIds+")";
		Session session = HibernateUtil.currentSession();
		try
		{
			Query query = session.createQuery(hql);
			query.executeUpdate();
			bFlag = true;
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			Logger.log("删除频道信息失败: " + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return bFlag;
	}
	
    /**
     * 设置节点的顺序 
     * @author xhf
     * @param sNodeId   要移动的节点的 nodeId
     * @param sNodeIndex  移动到该节点之后的 nodeiId
     * @return  true:设置成功，false：设置失败
     */
    public boolean setNodeOrder(String sNodeId, String sNodeIndex)
    {
    	boolean bRet = false;
    	Session session = HibernateUtil.currentSession();

        boolean isFirst = false;
        int lCurrentOrder = -1;

        try
        {
		  //查找父节点
		  String hql = "SELECT parent_id FROM CatalogInfo WHERE catalog_id=" + sNodeId;
		  Query query = session.createQuery(hql);
		  List list = query.list();
		  int nParentId = -1;
		  if(list.size()>0)
		  {
		    nParentId = (Integer)list.get(0);
		  }
		  else
		  {
		    return bRet;
		  }
		
		  int nIndex = Integer.parseInt(sNodeIndex);
		  if (nIndex == 0)
		  {
			  String hql2 = "";
			  hql2 =  "SELECT  node_archive_day FROM CatalogInfo WHERE"
				  	+ " parent_id=" + nParentId+" ORDER BY node_archive_day DESC";
			  
			  query = session.createQuery(hql2);
			  query.setMaxResults(1);//只查询一次
			  list = query.list();
			  if (list.size()>0)
			  {
			      lCurrentOrder = (Integer)list.get(0);
			      lCurrentOrder++;
			      bRet = true;
			      isFirst = true;
			  }
		  }
		  else
		  {
		    String hql2 = "SELECT catalog_id,node_archive_day FROM CatalogInfo where "
		    			+ "parent_id=" + nParentId + " ORDER BY node_archive_day DESC";
		    query = session.createQuery(hql2);
		    list = query.list();
		    for(int i=0;i<list.size();i++)
		    {
		      Object obj[] = (Object[])list.get(i);
		      if ((Integer)obj[0] == nIndex)
		      {
		    	  lCurrentOrder = (Integer)obj[1];
		    	  bRet = true;
		      }
		    }
		  }
		
		  if(bRet)
		  {
			//开始事务
			Transaction ts = session.beginTransaction();
			if (isFirst)
			{
			  bRet = false;
			  isFirst = false;
			  String sTmp = "UPDATE CatalogInfo SET node_archive_day="
				  			+ lCurrentOrder + " WHERE catalog_id=" + sNodeId;
			  query = session.createQuery(sTmp);
			  query.executeUpdate();
			  bRet = true;
			  isFirst = true;
			}
			else
			{
			  bRet = false;
			  String sSelectStmt = "SELECT catalog_id,node_archive_day FROM CatalogInfo"
				  				+ " WHERE node_archive_day>=" + lCurrentOrder
				  				+ " ORDER BY node_archive_day";              
			  query = session.createQuery(sSelectStmt);
			  list = query.list();
			  
			  String sUpdateStmt = "UPDATE CatalogInfo set node_archive_day=:nodeIndex" 
				  				+ " WHERE catalog_id=:catalogId";
			  query = session.createQuery(sUpdateStmt);
			  for(int i=0;i<list.size();i++)
			  {
			    Object obj[] = (Object[])list.get(i);
			    int id = (Integer)obj[0];
			    int archive = (Integer)obj[1];
			    query.setInteger("nodeIndex", archive+1);//在该节点前的node_archive_day属性值都加1
			    query.setInteger("catalogId",id );
			    query.executeUpdate();
			  }
			  query.setInteger("nodeIndex", lCurrentOrder);
			  query.setInteger("catalogId", Integer.parseInt(sNodeId));
			  query.executeUpdate();
			  bRet = true;
			}
		    ts.commit();//提交事务
		  }
		}
        catch (Exception e) {
			//e.printStackTrace();
			Logger.log("设置栏目顺序失败: " + e.getMessage(), 2);
        }
        finally {
        	HibernateUtil.closeSession();
        }
      return bRet;
    }
    
    /**
     * 根据栏目id和站点id，返回所有一级子栏目中node_archive_day的最大值，新建栏目时用
     * @author xhf 2009-11-13
     * @param  parent_id
     * @param  site_id
     * @return
     */
	public int getMaxOrder(int parent_id,int site_id)
	{
		int t = 0;
		Session session = HibernateUtil.currentSession();
		try
		{			
			Query query = session.createQuery("select c.node_archive_day from CatalogInfo c where c.parent_id=:parent_id and c.site_id=:site_id order by node_archive_day desc");
			query.setInteger("parent_id", parent_id);
			query.setInteger("site_id", site_id);
			List list = query.list();
			if(list!=null && list.size()>0)
			{
				t = (Integer)list.get(0);
			}			
		}
		catch (Exception e)
		{
			Logger.log("添加栏目失败：" + e.getMessage(), 2);
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return t;
	}
	
	/**
	 * 获取栏目的目录结构
	 * @param catalogId
	 * @return
	 */
	public String getCatalogPath(int catalogId)
	{
		String path = "";
		CatalogInfo catalogInfo = getCatalogInfo(catalogId);
		path = catalogInfo.getCatalog_name();
		int parentId = catalogInfo.getParent_id();
		while(parentId!=0 )
		{
			catalogInfo = getCatalogInfo(parentId);
			path = catalogInfo.getCatalog_name()+"/"+path;
			parentId = catalogInfo.getParent_id();
		}
		return path;
	}	
	

	public List getRootCatalogBySiteId(int nSiteId) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try {
			String sSql = null;
			sSql = "from com.jwzt.DB.soms.vod.catalog.CatalogInfo where catalog_type=1 and (site_id=0 or site_id=" + nSiteId + ") and parent_id = 0 and catalog_id <> 1 order by parent_id, node_archive_day desc";
			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(sSql);
			ts.commit();
			list = query.list();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取树失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}
	public List getCatalogListBySite(int nSiteId) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try {
			String sSql = null;
			sSql = "from com.jwzt.DB.soms.vod.catalog.CatalogInfo where catalog_type=1 and (site_id=0 or site_id=" + nSiteId + ") order by parent_id, node_archive_day desc";

			ts = session.beginTransaction();
			session.beginTransaction();
			Query query = session.createQuery(sSql);
			ts.commit();
			list = query.list();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取树失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	public Integer getCatalogIdMin() {
		Session session = HibernateUtil.currentSession();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rest = null;
		
		Integer catalog_id = -1;
		try
		{
			conn = session.connection();
			String sSql = "SELECT min(catalog_id) catalog_id FROM soms4_vod_catalog svc where svc.catalog_type=1 and svc.parent_id=0 and svc.catalog_id<>1";
			stat = conn.prepareStatement(sSql);
			rest = stat.executeQuery();
			
			while(rest.next())
			{
				catalog_id = rest.getInt("catalog_id");
			}
		}
		catch (Exception e)
		{
			//System.out.println("获取站点列表失败：" + e.getMessage());
			Logger.log("获取站点列表失败：" + e.getMessage(), 2);
			throw new RuntimeException(e);
		}
		finally
		{
			com.jwzt.common.AppTools.closeResultSet(rest);
			com.jwzt.common.AppTools.closeStatement(stat);
			com.jwzt.common.AppTools.closeConnection(conn);
		}
		return catalog_id;
	}
}
