/**
 * HTML界面中的一些显示元素
 */
package com.jwzt.common;

import java.sql.*;

import com.jwzt.DB.common.HibernateUtil;
import org.hibernate.Session;

public class UIMgr
{
	/**
	 * 获取表中的字段，生成HTML中的option对象
	 * @param sSql
	 *        需要处理的表，第一个字段是id，第二个字段是name
	 * @return HTML中iption中的值
	 */
	public static String getOptionList(String sSql)
	{
		StringBuffer sRet = new StringBuffer(1024);
		String sLog = "";

		Connection conn = AppTools.getConnection();

		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sSql.toString());
			if (rs != null)
			{
				while (rs.next())
				{
					sRet.append("<option value=\"").append(rs.getString(1))
							.append("\"").append(">");
					sRet.append(rs.getString(2)).append("</optioin>");
				}
			}
			else
			{
				sLog = "UIMgr->getOptionList->获取列表（rs=null）";
				Logger.log(sLog, 3);
			}
		}
		catch (Exception e)
		{
			sLog = "UIMgr->getOptionList->获取列表错误！";
			Logger.log(sLog, 3);
			Logger.log(e, 3);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return sRet.toString();
	}

	public static String getCheckBoxList(String sSql, String sCheckBoxName)
	{
		StringBuffer sRet = new StringBuffer(1024);
		String sLog = "";

		Connection conn = AppTools.getConnection();

		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sSql.toString());
			if (rs != null)
			{
				int i = 1;
				sCheckBoxName = ("".equals(sCheckBoxName))?"checkbox1":sCheckBoxName;
				
				while (rs.next())
				{
					String sBR = (i % 5 == 0) ? "<br>" : "";

					sRet.append("<span style=\"width=50px;\"><input type='checkbox' name='")
						.append(sCheckBoxName).append("' value='")
						.append(rs.getString(1)).append("'>")
						.append(rs.getString(2)).append("</span>")
						.append(sBR);
					
					i++;
				}
			}
			else
			{
				sLog = "UIMgr->getOptionList->获取列表（rs=null）";
				Logger.log(sLog, 3);
			}
		}
		catch (Exception e)
		{
			sLog = "UIMgr->getOptionList->获取列表错误！";
			Logger.log(sLog, 3);
			Logger.log(e, 3);
		}
		finally
		{
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return sRet.toString();

	}
}
