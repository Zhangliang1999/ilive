package com.bvRadio.iLive.iLive.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class FaceUtils {
	/**
	 * 获取表情对象集
	 * @return
	 */
	public static List<FaceVo> selectFace(){
		List<FaceVo> list = new ArrayList<FaceVo>();
		try {
			String path = System.getProperty("AppBasePath_iLive");
			File file = new File(path + "conf" + File.separator + "face.xml");
			SAXBuilder builder = new SAXBuilder();   
			Document doc = builder.build(file);   
			Element element = doc.getRootElement();   
			Element FACES = element.getChild("FACES"); 
			List<Element> children = FACES.getChildren("FACE");
			if(children==null){
				children = new ArrayList<Element>();
			}
			for (Element element2 : children) {
				FaceVo face = new FaceVo();
				String key = element2.getChildText("key");
				String value = element2.getChildText("value");
				face.setKey(key);
				face.setValue(value);
				list.add(face);
			}
			System.out.println("获取成功");
		}catch (Exception e) {
			System.out.println("获取失败！"+e.toString());
		}
		return list;
	}
	/**
	 * 获取图片IMG标签
	 * @return
	 */
	public static String selectFaceImg(String content){
		try {
			String path = System.getProperty("AppBasePath_iLive");
			File file = new File(path + "conf" + File.separator + "userBean.xml");
			SAXBuilder builder = new SAXBuilder();   
			Document doc = builder.build(file);   
			Element element = doc.getRootElement();   
			Element FACES = element.getChild("FACES"); 
			List<Element> children = FACES.getChildren("FACE");
			if(children==null){
				children = new ArrayList<Element>();
			}
			for (Element element2 : children) {
				String key = element2.getChildText("key");
				String value = element2.getChildText("value");
				content = content.replace(key, "<img src=\""+value+"\" >");
			}
			System.out.println("获取成功");
		}catch (Exception e) {
			System.out.println("获取失败！"+e.toString());
		}
		return content;
	}
}
