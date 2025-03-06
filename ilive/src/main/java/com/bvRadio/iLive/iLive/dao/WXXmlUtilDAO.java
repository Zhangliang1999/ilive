package com.bvRadio.iLive.iLive.dao;

import java.util.Map;

import com.thoughtworks.xstream.XStream;

public interface WXXmlUtilDAO {
	/**
	 * 解析XML
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> parseXml(String msg) throws Exception;
	/**
	 * 扩展xstream，使其支持CDATA块
	 */
	public XStream xstream() throws Exception;
}
