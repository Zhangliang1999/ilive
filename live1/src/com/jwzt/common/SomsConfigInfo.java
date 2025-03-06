package com.jwzt.common;

import java.io.File;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.jwzt.common.Logger;
import com.jwzt.soms.sys.property.SysPropertyBean;
import com.jwzt.soms.sys.property.SysPropertyManager;


public class SomsConfigInfo
{
    public static Hashtable allProperties= new Hashtable();

    /*SOMS的路�?*/
    private static String sSomsHomePath = null;
    
    /*CMS路径 */
    private static String cmsHomePath = null;
    static
    {
    	
    	loadConfig();
    	/*
    	//静�?�代码块 从数据库中取出所有的配置信息 放到HashTable�?
    	try {
			List list = SysPropertyManager.selectAll();
			for(int j=0;j<list.size();j++)
            {
            	SysPropertyBean sys = (SysPropertyBean)list.get(j);
            	allProperties.put(sys.getPropertyId(),sys.getPropertyValue());
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	*/
    }
    private static void loadConfig()
    {
    	File configFile = new File(getHomePath() + "/conf/system.xml");
        if (configFile != null)
        {
            SAXBuilder builder = new SAXBuilder();
            try
            {
                Document document = builder.build(configFile);
                Element root = document.getRootElement();

                //读取系统的的配置信息
                List proList = root.getChild("rootProperties").getChildren("parameters");
                Element tempPara = null;
                for(int i=0;i<proList.size();i++)
                {
                    tempPara = (Element) proList.get(i);
                    allProperties.put(tempPara.getChildText("para-name"),tempPara.getChildText("para-value"));
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Logger.log("读取配置文件错误�?" + ex.toString(), 2);
            }
            builder = null;
        }
    }
    @Deprecated
    public static String getHomePathDep()
    {
		if(sSomsHomePath == null)
		{
			SomsConfigInfo somsResource = new SomsConfigInfo();
	    	String path = somsResource.getClass().getResource("").getPath();
	    	path = path.substring(1, path.indexOf("WEB-INF")-1);
	    	
	    	try
			{
	    		sSomsHomePath = URLDecoder.decode(path, "UTF-8");
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		
		}
		return sSomsHomePath;
    }
    
    /**
     * 
     * 此方法为获取配置文件的目录ps加，以后不用再web.xml中配置了
     * 为兼用linux改用如下方式 获取工程根目�?
     * @return
     */
    
    public static String getHomePath()
    {
    	  String classPath = SomsConfigInfo.class.getClassLoader().getResource("/").getPath();
    	  //String classPath = "D:/work7/livems/livems/WEB-INF/classes/aaaa";
    	  //windows�?
    	  if("\\".equals(File.separator)){   
    		  sSomsHomePath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes"));
    		  sSomsHomePath = sSomsHomePath.replace("/", "\\");
    	  }
    	  //linux�?
    	  if("/".equals(File.separator)){   
    		  sSomsHomePath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes"));
    		  sSomsHomePath = sSomsHomePath.replace("\\", "/");
    	  }
    	  //sSomsHomePath = "D:/work7/livems/livems";
    	  return sSomsHomePath;

    }
    public static String getCMSHomePath()
    {
		if(cmsHomePath == null)
		{
			SomsConfigInfo somsResource = new SomsConfigInfo();
	    	String path = somsResource.getClass().getResource("").getPath();
	    	path = path.substring(1, path.indexOf("WEB-INF")-6)+"cms";
	    	
	    	try
			{
	    		
	    		cmsHomePath = URLDecoder.decode(path, "UTF-8");
	    		//判断操作系统版本
	    		Properties props=System.getProperties();
	    		String systemInfo = props.getProperty("os.name");
	    		systemInfo = systemInfo.toLowerCase();
	    		if(systemInfo.indexOf("linux") != -1)
	    		{
	    			cmsHomePath = "/"+cmsHomePath;
	    		}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		
		}
		return cmsHomePath;
    }
    public static int getDebugLevel()
    {
        return 2;
    }

    public static void reloadConfig()
    {
        loadConfig();
    }

    public static String get(String paraName)
    {
    	String getValue = (String)allProperties.get((String)paraName);
    	if(getValue==null||getValue.equals(null))
    	{
    		return "";
    	}
    	//避免这个配置项使用了反斜杠的时候后面的流程报错
    	if("liveDir".equals(paraName)){
    		getValue = getValue.replaceAll("\\\\", "/");
    	}
        return getValue;
    }
    public static String remove(String paraName)
    {
        return (String)allProperties.remove(paraName);
    }
    public static void put(String key, String value) {
    	allProperties.put(key, value);
    }
    /** 系统唯一获得配置信息接口方法,request获取请求地址,判断应给用户�?么地�? */
    public static String get(String paraName,HttpServletRequest request)
    {
        String retVal=null;
    	if("CMS_Address".equals(paraName)||"CMS_smsUrl".equals(paraName)||"SOMS4_Address".equals(paraName))
    	{
    		//修改后的流程,如果request为null，返回默认配置文件地�?
    		if(request==null)
        	{
        		retVal=(String)allProperties.get((String)paraName);
        	}
        	else
        	{
        		String requestURL=request.getRequestURL().toString();
        		requestURL=requestURL.replace("\\","/");
        		requestURL=requestURL.substring(requestURL.indexOf("//")+2,requestURL.length());
        		requestURL=requestURL.substring(0, requestURL.indexOf("/"));
        		//System.out.println("SomsConfigInfo里的请求地址="+requestURL);
        		retVal=requestURL;
        	}
    	}
    	else
    	{
    		//原来流程的返回�??
    		retVal=(String)allProperties.get((String)paraName);
    	}
    	
    	return retVal;
    }

}