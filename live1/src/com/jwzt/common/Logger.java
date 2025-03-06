/**
 * 日志记录器，负责记录系统日志
 */
package com.jwzt.common;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Logger
{
	// /** 整个系统唯一的日志记录器 */
	// private static Logger logger = new Logger();
	//
	// /** 用来写日志的writer对象 */
	// private PrintWriter writer = null;
	//
	// /**
	// * 私有的构造函数
	// */
	// private Logger()
	// {
	// StringBuffer logPath = new StringBuffer(255);
	// String seprator = System.getProperty("file.separator");
	// logPath.append(SomsConfigInfo.getHomePath());
	// logPath.append(seprator).append("log").append(seprator);
	// logPath.append(getCurTime()).append(".log");
	// //System.out.println(logPath.toString());
	// File logFile = new File(logPath.toString());
	// if (logFile != null)
	// {
	// try
	// {
	// logFile.createNewFile();
	// writer = new PrintWriter(new FileWriter(logFile), true);
	// }
	// catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// }
	// }
	//
	// /**
	// * 按照给定的级别记录给定的信息到日志中去
	// * @param msg 要记录的信息
	// * @param level 级别
	// */
	// public static void log(String msg, int level)
	// {
	// if (SomsConfigInfo.getDebugLevel() + level > 3)
	// {
	// logger.writeLog(msg);
	// }
	// if (level == 1)
	// {
	// System.out.println(msg);
	// }
	// }
	//
	// /**
	// * 按照给定的级别记录给定的异常到日志中去
	// * @param msg 要记录的异常
	// * @param level 级别
	// */
	// public static void log(Exception ex, int level)
	// {
	// if (SomsConfigInfo.getDebugLevel() + level > 3)
	// {
	// logger.writeLog(ex);
	// }
	// if (level == 1)
	// {
	// ex.printStackTrace();
	// }
	// }
	//
	// /**
	// * 写给定的信息到日志中去
	// * @param msg 给定的信息
	// */
	// synchronized private void writeLog(String msg)
	// {
	// writer.println(msg);
	// }
	//
	// /**
	// * 写给定的异常到日志中去
	// * @param msg 给定的异常
	// */
	// synchronized private void writeLog(Exception ex)
	// {
	// ex.printStackTrace(writer);
	// }
	//
	// /**
	// * 获取当前时间,格式为年-月-日---时-分-秒
	// * @return 当前时间
	// */
	// private String getCurTime()
	// {
	// StringBuffer buffer = new StringBuffer(32);
	// java.util.Calendar cal = java.util.Calendar.getInstance();
	// int year = cal.get(Calendar.YEAR);
	// int month = cal.get(Calendar.MONTH) + 1;
	// int day = cal.get(Calendar.DAY_OF_MONTH);
	// //int hour = cal.get(Calendar.HOUR);
	// //int minute = cal.get(Calendar.MINUTE);
	// //int second = cal.get(Calendar.SECOND);
	// buffer.append(year).append("-").append(month).append("-").append(day);
	// //buffer.append(year).append("-").append(month).append("-").append(day).append("---");
	// //
	// buffer.append(hour).append("-").append(minute).append("-").append(second);
	// return buffer.toString();
	// }

	/** 整个系统唯一的日志记录器 */
	private static Logger logger = new Logger();

	/** 用来写日志的writer对象 */
	private PrintWriter writer = null;

	/**
	 * 私有的构造函数
	 */
	private Logger()
	{
		StringBuffer logPath = new StringBuffer(255);
		String seprator = System.getProperty("file.separator");
		logPath.append(SomsConfigInfo.getHomePath());
		logPath.append(seprator).append("log").append(seprator);
		logPath.append(getCurTime()).append(".log");
		File logFile = new File(logPath.toString());
		if (logFile != null)
		{
			try
			{
				logFile.createNewFile();
				writer = new PrintWriter(new FileWriter(logFile), true);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 按照给定的级别记录给定的信息到日志中去
	 * 
	 * @param msg 要记录的信息
	 * @param level 级别
	 */
	public static void log(String msg, int level)
	{
		if (SomsConfigInfo.getDebugLevel() + level > 3)
		{
			System.out.println(getCurTime2()+ ":" +msg);
			logger.writeLog(msg);
		}
		if (level == 1)
		{
			System.out.println(getCurTime2()+ ":" +msg);
		}
	}

	/**
	 * 按照给定的级别记录给定的异常到日志中去
	 * 
	 * @param msg 要记录的异常
	 * @param level 级别
	 */
	public static void log(Exception ex, int level)
	{
		if (SomsConfigInfo.getDebugLevel() + level > 3)
		{
			logger.writeLog(ex);
		}
		if (level == 1)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 写给定的信息到日志中去
	 * 
	 * @param msg 给定的信息
	 */
	synchronized private void writeLog(String msg)
	{
		StringBuffer logPath = new StringBuffer(255);
		String seprator = System.getProperty("file.separator");
		logPath.append(SomsConfigInfo.getHomePath());
		logPath.append(seprator).append("log").append(seprator);
		logPath.append(getCurTime()).append(".log");
		File logFile = new File(logPath.toString());
		if (logFile.exists())
		{
			try
			{
				writer = new PrintWriter(new FileWriter(logFile, true), true);
				writer.println(msg);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			try
			{
				logFile.createNewFile();
				writer = new PrintWriter(new FileWriter(logFile, true), true);
				writer.println(msg);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 写给定的异常到日志中去
	 * 
	 * @param msg 给定的异常
	 */
	synchronized private void writeLog(Exception ex)
	{
		StringBuffer logPath = new StringBuffer(255);
		String seprator = System.getProperty("file.separator");
		logPath.append(SomsConfigInfo.getHomePath());
		logPath.append(seprator).append("log").append(seprator);
		logPath.append(getCurTime()).append(".log");
		File logFile = new File(logPath.toString());
		if (logFile.exists())
		{
			try
			{
				writer = new PrintWriter(new FileWriter(logFile, true), true);
				ex.printStackTrace(writer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				logFile.createNewFile();
				writer = new PrintWriter(new FileWriter(logFile, true), true);
				ex.printStackTrace(writer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取当前时间,格式为年-月-日
	 * 
	 * @return 当前时间
	 */
	private String getCurTime()
	{
		StringBuffer buffer = new StringBuffer(32);
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH) + 1;
		int day = cal.get(cal.DAY_OF_MONTH);
		// int hour = cal.get(cal.HOUR);
		// int minute = cal.get(cal.MINUTE);
		// int second = cal.get(cal.SECOND);
		buffer.append(year).append("-").append(month).append("-").append(day);
		// buffer.append(hour).append("-").append(minute).append("-").append(second);
		return buffer.toString();
	}
	/**
	 * 获取当前时间,格式为年/月/日 时：分：秒
	 * 
	 * @return 当前时间
	 */
	public static String getCurTime2()
	{
		return StringUtil.getCurentDateTimeAsString();
	}
}