package com.jwzt.soms.web.ums;
/**
 * 解析xml配置
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.jwzt.common.SomsConfigInfo;

public class GetUmsXml {
	/**
	 * 解析当前config.xml
	 * 
	 */
	public static String getUMSStreamPort() {
		SAXBuilder builder = new SAXBuilder();
		String homePath = SomsConfigInfo.getHomePath();
		File configFile = new File(homePath + "\\UMS\\conf\\config.xml");
		String value = "";
		Document document = null;
		try {
			document = builder.build(configFile);
			Element root = document.getRootElement();
			Element child = root.getChild("UMSStreamPort");
			value = child.getValue();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 修改配置文件的端口号
	 * 
	 * @param port
	 * @return
	 * @throws IOException
	 */
	public static boolean updatexmlport(int port) throws IOException {
		SAXBuilder builder = new SAXBuilder();
		String homePath = SomsConfigInfo.getHomePath();
		File configFile = new File(homePath + "\\UMS\\conf\\config.xml");
		Document document = null;
		try {
			document = builder.build(configFile);
			Element root = document.getRootElement();
			Element child = root.getChild("UMSStreamPort");
			System.out.println(child.getValue());
			child.setText(Integer.toString(port));
			System.out.println(child.getValue());
			Format format = Format.getRawFormat();
			format.setEncoding("GBK");
			XMLOutputter out = new XMLOutputter(format);
			out.setFormat(format);
			//fileWriter = new FileWriter(configFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(configFile),"GBK");
			out.output(document, outputStreamWriter);
			outputStreamWriter.close();
			return true;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改appxml属性
	 * @param filepath
	 * @param base_path
	 * @return
	 * @throws IOException
	 */
	public static boolean updateAppxml(String filepath, String base_path)
			throws IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		File file = new File(filepath);
		try {
			document = builder.build(file);
			Element root = document.getRootElement();
			Element child = root.getChild("IODefine").getChild(
					"VirtualDirectory");
			System.out.println(child.getValue());
			child.setText(base_path);
			System.out.println(child.getValue());
			Format format = Format.getRawFormat();
			format.setEncoding("GBK");
			XMLOutputter out = new XMLOutputter();
			out.setFormat(format);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filepath),"GBK");
			out.output(document, outputStreamWriter);
			outputStreamWriter.close();
			return true;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取xml属性
	 * @param filepath
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static String getVirtualDirectory(String filepath)
			throws UnsupportedEncodingException, FileNotFoundException {
		String value = "";
		SAXBuilder builder = new SAXBuilder();
		File file = new File(filepath);
		Document document = null;
		try {
			document = builder.build(file);
			Element root = document.getRootElement();
			Element child = root.getChild("IODefine").getChild(
					"VirtualDirectory");
			value = child.getValue();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

}
