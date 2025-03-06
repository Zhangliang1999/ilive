/**
 * 这个类负责处理一些和整个应用程序相关的逻辑
 */
package com.jwzt.DB.common;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;

import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;

import java.util.*;
public class AppGlobal
{
    /** 应用程序URL */
    public static final String appUrl = "/soms";

    /**  系统使用的数据源对象 */
    private static DataSource sysDataSource = lookupSysDataSource();
    /** 存放敏感字集合 **/
    public static List sensitiveWord=new ArrayList();
    /**敏感字过期时间 **/
    public static Timestamp timeStamp=null;
    /**
     * 获取系统数据源,这个方法目前只对Tomcat有效
     * @return 系统数据源
     */
    private static DataSource lookupSysDataSource()
    {
        DataSource retVal = null;
        String dsName = SomsConfigInfo.get("CMS_sysDataSource");
        //System.out.println("########dsName#####"+dsName);
        if (dsName != null)
        {
            try
            {
                InitialContext initCtx = new InitialContext();
                retVal = (DataSource)initCtx.lookup("java:/comp/env/" + dsName);
            }
            catch (Exception ex)
            {
                Logger.log(ex, 3);
            }
        }
        return retVal;
    }
   

  

    /**
     * 从连接池中获取数据库连接
     * @return  数据库连接
     */
    public static Connection getConn()
    {
        Connection conn = null;
        if (sysDataSource == null)
        {
            sysDataSource = lookupSysDataSource();
        }
        if (sysDataSource != null)
        {
            try
            {
                conn = sysDataSource.getConnection();
                //这里可以进行在连接上进行一些初始化工作，如Oracle数据库可以修改时间格式
                //目前使用的是SqlServer,所以这里先不做特别工作
            }
            catch (Exception ex)
            {
                conn = null;
                Logger.log(ex, 3);
            }
        }
        return conn;
    }

/*    public static Connection getConn()
    {
      Connection conn = null;
      try
      {
        Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=cms", "sa", "sa");
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      return conn;
    }*/
}