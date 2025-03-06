/**
 * 摘要：这个类负责提供一些应用程序级的全局变量和公用方法，和业务逻辑无关
 */
package com.jwzt.common;

import java.io.*;
import java.sql.*;
import java.net.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.common.Logger;

public class AppTools
{
	/**
	 * 在给定的servlet的上下文中包含给定的资源
	 * 
	 * @param servlet
	 *            给定的servlet
	 * @param request
	 * @param response
	 * @appURL 应用程序URL
	 * @resURL 资源URL
	 */
	public static void includeRes(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, String appURL,
			String resURL) throws IOException, ServletException
	{
		ServletContext ctx = servlet.getServletContext().getContext(appURL);
		if (ctx != null)
		{
			RequestDispatcher dispatcher = ctx.getRequestDispatcher(resURL);
			if (dispatcher != null)
			{
				dispatcher.include(request, response);
			}
		}
	}

	/**
	 * 关闭给定的reader
	 * 
	 * @param reader
	 *            要关闭的reader
	 */
	public static void closeReader(Reader reader)
	{
		try
		{
			if (reader != null)
			{
				reader.close();
				reader = null;
			}
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * 关闭给定的statment
	 * 
	 * @param stmt
	 *            要关闭的statment
	 */
	public static void closeStatement(java.sql.Statement stmt)
	{
		try
		{
			if (stmt != null)
			{
				stmt.close();
				stmt = null;
			}
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * 关闭给定的结果集
	 * 
	 * @param result
	 *            要关闭的结果集
	 */
	public static void closeResultSet(java.sql.ResultSet result)
	{
		try
		{
			if (result != null)
			{
				result.close();
				result = null;
			}
		}
		catch (Exception ex)
		{
		}
	}
	
	/**
	 * 通过hibernate获取数据库连接
	 * @return	获取的数据库连接
	 */
	public static Connection getConnection()
	{
		Connection conn = null;

		try
		{
			conn = HibernateUtil.currentSession().connection();
		}
		catch (Exception e)
		{
			Logger.log("获取数据库链接出错 --> " + e, 3);
		}

		return conn;
	}
	
	/**
	 * 关闭链接
	 * 
	 * @param conn
	 *            数据库连接
	 */
	public static void closeConnection(Connection conn)
	{
		boolean bRet = false;
		try
		{
			if (conn != null)
			{
				conn.close();
				conn = null;
				HibernateUtil.closeSession();
			}
		}
		catch (Exception ex)
		{
			// todo:Logger.log(ex, 2);
		}
	}

	/**
	 * 设置Connection的AutoCommit
	 * 
	 * @param conn
	 *            数据库连接
	 * @param bValue
	 *            要设置的值
	 */
	public static boolean setAutoCommit(Connection conn, boolean bValue)
	{
		boolean bRet = false;
		try
		{
			conn.setAutoCommit(bValue);
			bRet = true;
		}
		catch (Exception ex)
		{
			// todo:Logger.log(ex, 2);
		}
		return bRet;
	}

	/**
	 * 回滚事务
	 * 
	 * @param conn
	 *            数据库连接
	 */
	public static boolean rollback(Connection conn)
	{
		boolean bRet = false;
		try
		{
			conn.rollback();
			bRet = true;
		}
		catch (Exception ex)
		{
			// todo:Logger.log(ex, 2);
		}
		return bRet;
	}

	/**
	 * 递交事务
	 * 
	 * @param conn
	 *            数据库连接
	 */
	public static boolean commit(Connection conn)
	{
		boolean bRet = false;
		try
		{
			conn.commit();
			bRet = true;
		}
		catch (Exception ex)
		{
			// todo:Logger.log(ex, 2);
		}
		return bRet;
	}

	/**
	 * 关闭给定的HttpURLConnection
	 * 
	 * @param conn
	 *            要关闭的Http连接
	 */
	public static void closeHttpConnection(HttpURLConnection conn)
	{
		if (conn != null)
		{
			conn.disconnect();
			conn = null;
		}
	}

	/**
	 * 下载指定URL的内容
	 * 
	 * @param url
	 *            要下载的URL
	 * @return 指定URL的内容，如果失败则返回null
	 */
	public static String downloadUrl(String url)
	{
		String strRet = null;
		if (url != null)
		{
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try
			{
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setDoInput(true);
				reader = new BufferedReader(new InputStreamReader(httpTarget.getInputStream(), "UTF-8"));

				StringBuffer strBuffer = new StringBuffer(1024);
				String line = reader.readLine();
				while (line != null)
				{
					strBuffer.append(line + "\n");
					line = reader.readLine();
				}
				strRet = strBuffer.toString();
			}
			catch (Exception ex)
			{
				System.out.println("url:"+url);
				ex.printStackTrace();
				// todo:Logger.log(ex, 1);
				strRet = null;
			}
			finally
			{
				closeReader(reader);
				closeHttpConnection(httpTarget);
			}
		}
		return strRet;
	}

	/**
	 * 设置servlet中输出器的属性
	 * 
	 * @param response
	 */
	public static void setResponseAttr(HttpServletResponse response)
	{
		response.setContentType("text/html; charset=UTF-8");
	}

	/**
	 * 以yyyy-mm-dd hh24:mi:ss的格式返回当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime()
	{
		StringBuffer retVal = new StringBuffer(64);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		retVal.append(cal.get(Calendar.YEAR)).append("-").append(cal.get(Calendar.MONTH) + 1).append("-");
		retVal.append(cal.get(Calendar.DAY_OF_MONTH)).append(" ").append(cal.get(Calendar.HOUR_OF_DAY));
		retVal.append(":").append(cal.get(Calendar.MINUTE)).append(":").append(cal.get(Calendar.SECOND));
		return retVal.toString();
	}

	/**
	 * 工具方法将给定的字符缓冲区清空
	 * 
	 * @param buffer
	 *            缓冲区
	 * @param size
	 *            缓冲区的大小
	 */
	public static void clearCharBuffer(char[] buffer, int size)
	{
		for (int i = 0; i < size; i++)
		{
			if (buffer[i] == 0)
			{
				break;
			}
			buffer[i] = 0;
		}
	}

	/**
	 * 工具方法将给生成6位随机的密码(包括数字和字符)
	 */
	public static String createPassword()
	{
		String sRet = "";
		String str = "1,2,3,4,5,6,7,8,9";
		String stra[] = StringUtil.splitString2Array(str, ",");
		int nBit = 7;// 位数
		for (int i = 0; i < nBit; i++)
		{
			sRet = sRet + stra[(int) ((int) (Math.random() * 9))];
		}
		return sRet;
	}

	/**
	 * 工具方法将给生成4位随机的密码(包括数字和字符)
	 */
	public static String getRandomNumber()
	{
		String sRet = "";
		String str = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
		String stra[] = StringUtil.splitString2Array(str, ",");
		int nBit = 4;// 位数
		for (int i = 0; i < nBit; i++)
		{
			sRet = sRet + stra[(int) ((int) (Math.random() * 10))];
		}
		return sRet;
	}

	/**
	 * 工具方法将给生成6位随机的密码(仅包括数字)
	 */
	public static String createNumPassword()
	{
		String sRet = "";
		String str = "1,2,3,4,5,6,7,8,9";
		String stra[] = StringUtil.splitString2Array(str, ",");
		int nBit = 6;// 位数
		for (int i = 0; i < nBit; i++)
		{
			sRet = sRet + stra[(int) ((int) (Math.random() * 9))];
		}
		return sRet;
	}

	/**
	 * 获得任意区间内的随机数
	 * 
	 * @param nLow
	 *            区间最小值
	 * @param nHigh
	 *            区间最大值
	 * @return 随即整数
	 */
	public static int getRangeNum(int nLow, int nHigh)
	{
		double dRet = Math.random() * (1 + nHigh - nLow) + nLow;
		return (int) Math.floor(dRet);
	}

	/**
	 * 生成随机数
	 * 
	 * @param nBit
	 *            随机数的位数
	 * @param mode
	 *            随机数的模式：1：纯数字，其他：数字和字母组合
	 * @return 随机数
	 */
	public static String createRandomNum(int nBit, int mode)
	{
		if (nBit < 4 || nBit > 50) nBit = 8;

		//String sStr = "1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
		String sStr = "1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String[] str = new String[30];

		if (mode == 1)
			str = StringUtil.splitString2Array(sStr.substring(0, 17), ",");
		else
			str = StringUtil.splitString2Array(sStr, ",");

		String sRet = "";
		for (int i = 0; i < nBit; i++)
		{
			sRet = sRet + str[getRangeNum(0, str.length - 1)];
		}

		return sRet;
	}

	/***************************************************************************
	 * <p>
	 * Title:手机号码的类型,移动或者联通
	 * </p>
	 * <p>
	 * Description:手机号码的类型,移动或者联通
	 * </p>
	 * 
	 * @param strMobileNo
	 *            需要判断的手机号码
	 **************************************************************************/
	public static int getMobileType(String strMobileNo)
	{
		String strPrefix = strMobileNo.substring(0, 3);
		int iReturn = -1;
		if (strPrefix.trim().compareToIgnoreCase("135") == 0 || strPrefix.trim().compareToIgnoreCase("134") == 0
				|| strPrefix.trim().compareToIgnoreCase("136") == 0 || strPrefix.trim().compareToIgnoreCase("137") == 0
				|| strPrefix.trim().compareToIgnoreCase("138") == 0 || strPrefix.trim().compareToIgnoreCase("139") == 0)
		{
			iReturn = 1;
		}
		else if (strPrefix.trim().compareToIgnoreCase("130") == 0 || strPrefix.trim().compareToIgnoreCase("131") == 0
				|| strPrefix.trim().compareToIgnoreCase("132") == 0 || strPrefix.trim().compareToIgnoreCase("133") == 0)
		{
			iReturn = 2;
		}
		return iReturn;
	}

	/**
	 * 工具方法将给生成一组随机数列
	 */
	public static int[] getRandomArray(int value)
	{
		int[] arrays = new int[value];
		for (int i = 0; i < arrays.length; i++)
		{
			arrays[i] = 10000;
		}

		int count;
		for (int i = 0; i < arrays.length; i++)
		{
			do
			{
				count = (int) (Math.random() * value);
			}
			while (isInArray(arrays, count));
			arrays[i] = count;
		}

		return arrays;
	}

	private static boolean isInArray(int[] arrays, int value)
	{
		boolean result = false;
		for (int i = 0; i < arrays.length; i++)
		{
			if (arrays[i] == value) return true;
		}
		return result;
	}
	
	public static ArrayList getMacAddress()
	{
		ArrayList addrs = new ArrayList();
		Process process = null;
		BufferedReader reader = null;

		try
		{
			String command = "ipconfig -all";

			process = Runtime.getRuntime().exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				if(line.indexOf("Physical Address") >= 0)
				{
					int index = line.indexOf(":");
					String result = line.substring(index + 1);
					addrs.add(result.trim());
				}
			}
		}
		catch(IOException e)
		{
			Logger.log("1->AppTools.getMacAddress() : \r\n" + e.getMessage(), 2);
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();

				process.waitFor();
			}
			catch(Exception e)
			{
				Logger.log("2->AppTools.getMacAddress() : \r\n" + e.getMessage(), 2);
			}
		}

		return addrs;
	}
	
	public static boolean checkPhysicalAddress(String physicalAddress)
	{
		boolean value = false;

		ArrayList addrs = AppTools.getMacAddress();
		for(int i = 0; i < addrs.size(); i++)
		{
			String addr = (String)addrs.get(i);
			if(addr.equals(physicalAddress))
			{
				value = true;
				break;
			}
		}

		return value;
	}
	
	/**
	 * 比较两个日期的大小
	 *
	 * @param calendar1	第一个日期
	 * @param calendar2	第二个日期
	 * @return	1:第一个日期大于第二个日期	0:相等	-1:第一个小于第二个
	 */
	public static int compareDate(Date date1, Date date2)
	{
		int retValue = 0;

		if(date1.before(date2))
		{
			retValue = -1;
		}

		if(date1.after(date2))
		{
			retValue = 1;
		}

		return retValue;
	}
	
	
	/**
     * 拷贝文件
     *
     * @param     src 源文件
     * @param     dest 目标文件
     * 是否重试
     * @return    成功与否
     */
	public static boolean copyFile(String src, String dest , boolean rep)
	{
		boolean retValue = false;

		File srcFile = new File(src);
		if(srcFile != null && srcFile.exists() && srcFile.isFile())
		{
			File destFile = new File(dest);
			if(destFile != null)
			{
				destFile.getParentFile().mkdirs();
				FileInputStream input = null;
			    FileOutputStream output = null;
			    FileLock lock=null;
				try
				{
					if(destFile.exists())
					{
						destFile.delete();
					}
					destFile.createNewFile();
					input = new FileInputStream(srcFile);
				    output = new FileOutputStream(destFile);
				    try{
				    	lock = output.getChannel().tryLock();
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
				    
				    byte[] buffer = new byte[1024];
				    int i=0;
				    while((i = input.read(buffer)) != -1)
				    {
				        output.write(buffer, 0, i);
				    }

				    retValue = true;
				}
				catch(IOException e)
				{
					e.printStackTrace();
					
					try
					{
						if(lock!=null)
						{
							lock.release();//锁释放
							lock = null;
						}
						if(input != null)
						{
							input.close();
							input = null;
						}
						if(output != null)	
						{
							output.close();
							output = null;
						}
						if(rep)
						{
							
							Thread.sleep(5000);
							copyFile(src, dest ,false);
						}
						else
						{
						}
					}
					catch(Exception z)
					{z.printStackTrace();}

				}
				finally
				{
					try
					{
						if(lock!=null)
						{
							lock.release();//锁释放
							lock = null;
						}
						if(input != null)
						{
							input.close();
							input = null;
						}
						if(output != null)	
						{
							output.close();
							output = null;
						}
					}
					catch(Exception z)
					{z.printStackTrace();}
				}
			}
		}

		return retValue;
	}

}
