package com.bvRadio.iLive.iLive.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.bvRadio.iLive.iLive.entity.UserBean;

public class SystemXMLTomcatUrl {
	/**
	 * 读取文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable getHomePath() throws Exception {
		Hashtable allProperties = new Hashtable();
		String path = System.getProperty("AppBasePath_iLive");
		File configFile = new File(path + "conf" + File.separator + "system.xml");
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(configFile);
		Element root = document.getRootElement();
		// 读取系统的的配置信息
		List proList = root.getChild("rootProperties").getChildren("parameters");
		Element tempPara = null;
		for (int i = 0; i < proList.size(); i++) {
			tempPara = (Element) proList.get(i);
			allProperties.put(tempPara.getChildText("para-name"), tempPara.getChildText("para-value"));
		}
		return allProperties;
	}

	/**
	 * 获取地址
	 * 
	 * @param paraName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getUrl(String paraName) {
		String URL = "";
		try {
			Hashtable homePath = getHomePath();
			URL = (String) homePath.get(paraName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return URL;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<UserBean> outUserBeanXml() throws Exception{
		List<UserBean> list = new ArrayList<UserBean>();
		String path = System.getProperty("AppBasePath_iLive");
		File file = new File(path + "conf" + File.separator + "userBean.xml");
		SAXBuilder builder = new SAXBuilder();   
		Document doc = builder.build(file);   
		Element element = doc.getRootElement();   
		Element USERS = element.getChild("USERS"); 
		List<Element> children = USERS.getChildren("USER");
		for (Element element2 : children) {
			UserBean userBean = new UserBean();
			String userId = element2.getChildText("userId");
			try {
				userBean.setUserId(userId);
			} catch (Exception e) {
				userBean.setUserId("99");
			}
			String userName = element2.getChildText("userName");
			userBean.setUsername(userName);
			String userImage = element2.getChildText("userImage");
			userBean.setUserThumbImg(userImage);
			String userLevel = element2.getChildText("userLevel");
			try {
				userBean.setLevel(Integer.parseInt(userLevel));
			} catch (Exception e) {
				userBean.setLevel(0);
			}
			userBean.setUserType(1);
			list.add(userBean);
		}
		return list;
	}
}
