package com.jwzt.soms.sys.property;
/**
 * 对配置文件进行相关增、删、改操作
 * @author 许业生 2009-11-2
 */
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.StringUtil;

public class SysPropertyManager{
	public SysPropertyManager(){}
    /** 用来写日志的writer对象 */
    private PrintWriter writer = null;
    
	 /** 增加配置信息的sql语句 */
    private static final String AddProperty =
            " insert into soms4_sys_property"
            + "(property_id, property_name, property_value,property_type,property_desc) "
            + "values(?, ?, ?, ?,?)";
    /** 用来删除配置信息 的sql 语句 */
    private static final String DelPropertyInfo=
            " delete from soms4_sys_property where property_id = ? ";
    
    /** 删除多个配置信息的信息 */
    private static final String DelProperty =
            " delete from soms4_sys_property where property_id in (?) ";
    
    /** 更新配置信息的sql语句,因为是static final所以多次String + 不影响效率 */
    private static final String UpdatePropertyInfo =
              "update soms4_sys_property set"
            + " property_name = ? "
            + ", property_value = ? "
            + ",property_type = ?"
            + ", property_desc = ? "
            + "where property_id = ?";
    /** 通过配账号ID查找账号信息*/
    private static final String selectPropertyById="select * from soms4_sys_property where property_id=?";
    
    /**
     * 将给定的系统配置信息增加到系统配置信息(cms_sys_property)表中 
     * @author 许业生 2009-11-2
     * @param SysPropety  给定的配置信息
     * @return   操作结果，true表示成功，false表示失败
     */
    public boolean addProperty(SysPropertyBean sysProperty)
    {
        boolean retVal = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
        	//获取数据库链接
            conn = AppTools.getConnection();
            //如果获取的数据库连接不为空
            if (conn != null)
            {
            	//传入sql语句
                stmt = conn.prepareStatement(AddProperty);
                stmt.setString(1, sysProperty.getPropertyId());
                stmt.setString(2, sysProperty.getPropertyName());
                stmt.setString(3, sysProperty.getPropertyValue());
                stmt.setString(4, sysProperty.getPropertyType());
                stmt.setString(5, sysProperty.getPropertyDesc());
                //执行sql语句
                stmt.executeUpdate();
            }
        }
        catch (Exception ex)
        {
        	Logger.log(ex, 3);
            retVal = false;
            ex.printStackTrace();
        }
        finally
        {
        	//关闭所有连接
            AppTools.closeStatement(stmt);
            stmt = null;
            HibernateUtil.closeSession();
            AppTools.closeConnection(conn);
            conn = null;
        }
      //将结果返回
        return retVal;
    }
    /**
     * 通过配置的账号ID删除配置信息
     * @param property_id 配置信息的账号ID
     * @return 操作结果，true表示成功，false表示失败
     * @author 许业生 2009-11-2
     */
    public boolean delPropetyInfo(String property_id)
    {
    	 Connection conn = null;
    	 boolean retVal = true;
    	 PreparedStatement stmt = null;
         try {
        	 //获取数据库链接
    		 conn = AppTools.getConnection();
        	 if(conn!=null)
        	 {
        		 //传入sql语句
        		 stmt = conn.prepareStatement(DelPropertyInfo);
        		 stmt.setString(1, property_id);
        		 stmt.executeUpdate();
        	 }
		} catch (Exception e) {
			Logger.log(e, 3);
			retVal=false;
		}finally
		{
			//关闭数据库所有连接
			AppTools.closeStatement(stmt);
			stmt = null;
			HibernateUtil.closeSession();
			AppTools.closeConnection(conn);
			conn = null;
		}
		//将结果返回
		return retVal;
    }
    /**
     * 通过配置账号ID查找配置文件所有信息
     * @author 许业生 2009-11-2
     * @param propertyId 配置账号ID
     * @return 配置信息对象
     */
    public SysPropertyBean selectPropertyById(String propertyId)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        SysPropertyBean sys =null;
        try
        {
        	//获取数据库链接
            conn = AppTools.getConnection();
            //如果获取的数据库连接不为空
            if (conn != null)
            {
            	//传入sql语句
                stmt = conn.prepareStatement(selectPropertyById);
                stmt.setString(1,propertyId);
                //执行sql语句
                rs = stmt.executeQuery();
                if(rs.next())
                {
                	sys=new SysPropertyBean();
                	sys.setPropertyId(rs.getString("property_id"));
                	sys.setPropertyName(rs.getString("property_name"));
                	sys.setPropertyValue(rs.getString("property_value"));
                	sys.setPropertyType(rs.getString("property_Type"));
                	sys.setPropertyDesc(rs.getString("property_desc"));
                }
            }
        }
        catch (Exception ex)
        {
        	Logger.log(ex, 3);
        	ex.printStackTrace();
        }
        finally
        {
        	//关闭数据库所有连接
            AppTools.closeStatement(stmt);
            stmt = null;
            HibernateUtil.closeSession();
            AppTools.closeConnection(conn);
            conn = null;
        }
        //将结果返回
        return sys;
    }
    /**
     * 批量删除配置信息
     * @param ids 配置账号ID数组
     * @throws Exception 异常
     * @author 许业生 2009-11-2
     */
    public void delPropertys(String[] ids)
    {
    	//获取数据库链接
    	Connection conn =AppTools.getConnection();
    	Statement smt = null; 
    	try
    	{
    		if (conn != null)
    		{
    			conn.setAutoCommit(false);
    			smt = conn.createStatement();
    			StringBuffer sql = new StringBuffer(1024);
                sql.append("delete from soms4_sys_property where property_id in(");   
                sql.append(StringUtil.stringArray2SqlInStr(ids));
                sql.append(") ");
                smt.executeUpdate(sql.toString());
                smt.close();
                //提交
    			conn.commit();
    		}
    	}
    	catch (Exception ex)
    	{
    		Logger.log(ex, 3);
    		//回滚
    		AppTools.rollback(conn);
    	}
    	finally
    	{
    		//关闭所有数据库链接
    		AppTools.closeStatement(smt);
    		smt = null;
    		HibernateUtil.closeSession();
    		AppTools.closeConnection(conn);
    		conn = null;
    	}
    }

    /**
     * 更新存储在数据库中的系统配置信息
     * @param property 配置信息对象
     * @return  操作结果,true表示成功，false表示失败
     * @author 许业生 2009-11-2
     */
    public boolean updatePropertyInfo(SysPropertyBean property)
    {
        boolean retVal = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
        	//获取数据库链接
            conn = AppTools.getConnection();
            if (conn != null)
            {
            	//传入sql语句
                stmt = conn.prepareStatement(UpdatePropertyInfo);
                stmt.setString(1, property.getPropertyName());
                stmt.setString(2, property.getPropertyValue());
                stmt.setString(3, property.getPropertyType());
                stmt.setString(4, property.getPropertyDesc());
                stmt.setString(5, property.getPropertyId());
                //执行sql语句
                stmt.executeUpdate();
            }
        }
        catch (Exception ex)
        {
        	Logger.log(ex, 3);
        	ex.printStackTrace();
            retVal = false;
        }
        finally
        {
        	//关闭所有数据库连接
            AppTools.closeStatement(stmt);
            stmt = null;
            HibernateUtil.closeSession();
            AppTools.closeConnection(conn);
            conn = null;
        }
        //将结果返回
        return retVal;
    }
    /**
     * 查找所有配置信息
     * @return list集合
     * @throws Exception 异常
     */
     public static  List selectAll()
    {
    	List list = new ArrayList();
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	 try {
			 String sql  = " select property_id,property_name,property_value,property_type,property_desc from soms4_sys_property ";
			 //获取数据库链接
			 conn = AppTools.getConnection();
			 stmt = conn.prepareStatement(sql);
			 rs = stmt.executeQuery();
			 while(rs.next())
			 {
				 SysPropertyBean sys = new SysPropertyBean();
				 sys.setPropertyId(rs.getString("property_id"));
				 sys.setPropertyName(rs.getString("property_name"));
				 sys.setPropertyValue(rs.getString("property_value"));
				 sys.setPropertyType(rs.getString("property_Type"));
				 sys.setPropertyDesc(rs.getString("property_desc"));
				 list.add(sys);
			 }
		} catch (Exception e) 
		{
			Logger.log(e, 3);
			e.printStackTrace();
		}finally
        {
        	//关闭所有数据库连接
			if(rs!=null)
			{
				AppTools.closeResultSet(rs);
				rs=null;
			}
            AppTools.closeStatement(stmt);
            stmt = null;
            HibernateUtil.closeSession();
            AppTools.closeConnection(conn);
            conn = null;
        }
		//返回list集合
         return list;
    }
     public static  List distinctSelectAll()
     {
     	List list = new ArrayList();
     	Connection conn = null;
     	PreparedStatement stmt = null;
     	ResultSet rs = null;
     	 try {
 			 String sql  = " select  distinct property_type from soms4_sys_property ";
 			 //获取数据库链接
 			conn = AppTools.getConnection();
 			 stmt = conn.prepareStatement(sql);
 			 rs = stmt.executeQuery();
 			 while(rs.next())
 			 {
 				 SysPropertyBean sys = new  SysPropertyBean();
 				 sys.setPropertyType(rs.getString("property_type"));
 				 list.add(sys);
 			 }
 		} catch (Exception e) {
 			Logger.log(e, 3);
 			e.printStackTrace();
 		}finally
         {
         	//关闭所有数据库连接
 			if(rs!=null)
 			{
 				AppTools.closeResultSet(rs);
 				rs=null;
 			}
             AppTools.closeStatement(stmt);
             stmt = null;
             AppTools.closeConnection(conn);
             conn = null;
         }
          return list;
 	}
}
