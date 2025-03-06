package com.jwzt.common;

public class Debug
{
	public static boolean bFlag = true;
	public static String sObjectName = "";
	
	public static void debugPrint(String sMessage)
	{
		if(bFlag)
		{
			System.err.println("Debug: " + sMessage);
		}
	}
	
	public static void debugPrint(String sMessage, Object oObject)
	{
		if(bFlag)
		{
			System.err.println("Debug: " + sMessage);
			System.err.println("Class: " + oObject.getClass().getName());
		}
	}
	
	public static void debufPrint2(String sMessage, Object oObject)
	{
		if(bFlag)
		{
			if(oObject.getClass().getName().contains(sObjectName))
			{
				System.err.println("Debug: " + sMessage);
			}
		}
	}
}
