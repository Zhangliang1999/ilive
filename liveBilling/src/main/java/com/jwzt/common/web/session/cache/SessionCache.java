package com.jwzt.common.web.session.cache;

import java.io.Serializable;
import java.util.Map;

public interface SessionCache {
	/**
	 * 获取属性
	 * 
	 * @param root
	 * @param name
	 * @return
	 */
	public Serializable getAttribute(String root, String name);

	/**
	 * 设置属性
	 * 
	 * @param root
	 * @param name
	 * @param value
	 * @param exp
	 */
	public void setAttribute(String root, String name, Serializable value, int exp);

	/**
	 * 清空缓存
	 * 
	 * @param root
	 */
	public void clear(String root);

	/**
	 * 判断是否存在
	 * 
	 * @param root
	 * @return
	 */
	public boolean exist(String root);

	/**
	 * 获取session
	 * 
	 * @param root
	 * @return
	 */
	public Map<String, Serializable> getSession(String root);

	/**
	 * 设置session
	 * 
	 * @param root
	 * @param session
	 * @param exp
	 */
	public void setSession(String root, Map<String, Serializable> session, int exp);
}
