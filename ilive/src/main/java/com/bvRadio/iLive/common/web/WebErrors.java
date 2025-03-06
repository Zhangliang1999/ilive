package com.bvRadio.iLive.common.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

/**
 * WEB错误信息
 * 
 * 可以通过MessageSource实现国际化。
 */
public abstract class WebErrors {
	/**
	 * email正则表达式
	 */
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$");
	/**
	 * username正则表达式
	 */
	public static final Pattern USERNAME_PATTERN = Pattern.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$");

	public WebErrors() {
	}

	/**
	 * 添加错误字符串
	 * 
	 * @param error
	 */
	public void addErrorString(String error) {
		getErrors().add(error);
	}

	/**
	 * 添加错误。
	 * 
	 * @param error
	 */
	public void addError(String error) {
		getErrors().add(error);
	}

	/**
	 * 是否存在错误
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	/**
	 * 错误数量
	 * 
	 * @return
	 */
	public int getCount() {
		return errors == null ? 0 : errors.size();
	}

	/**
	 * 错误列表
	 * 
	 * @return
	 */
	public List<String> getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		return errors;
	}

	/**
	 * 将错误信息保存至ModelMap，并返回错误页面。
	 * 
	 * @param model
	 * @return 错误页面地址
	 * @see org.springframework.ui.ModelMap
	 */
	public String showErrorPage(ModelMap model) {
		toModel(model);
		return getErrorPage();
	}

	/**
	 * 将错误信息保存至ModelMap
	 * 
	 * @param model
	 */
	public void toModel(Map<String, Object> model) {
		Assert.notNull(model);
		if (!hasErrors()) {
			throw new IllegalStateException("no errors found!");
		}
		model.put(getErrorAttrName(), getErrors());
	}

	public boolean ifNull(Object o, String field) {
		if (o == null) {
			addErrorString("缺少参数：" + field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifEmpty(Object[] o, String field) {
		if (o == null || o.length <= 0) {
			addErrorString("缺少参数：" + field);
			return true;
		} else {
			return false;
		}
	}

	public boolean ifBlank(String s, String field, int maxLength) {
		if (StringUtils.isBlank(s)) {
			addErrorString("缺少" + field);
			return true;
		}
		if (ifOutOfLength(s, field, 0, maxLength)) {
			return true;
		}
		return false;
	}

	public boolean ifOutOfLength(String s, String field, int minLength, int maxLength) {
		if (s == null) {
			addErrorString("缺少参数：" + field);
			return true;
		}
		int len = s.length();
		if (len < minLength || len > maxLength) {
			addErrorString(field + "长度超出限制（" + minLength + "~" + maxLength + ")");
			return true;
		}
		return false;
	}

	public boolean ifNotEmail(String email, String field, int maxLength) {
		if (ifBlank(email, field, maxLength)) {
			return true;
		}
		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			addErrorString("不是正确的email格式：" + field);
			return true;
		}
		return false;
	}

	public boolean ifNotUsername(String username, String field, int minLength, int maxLength) {
		if (ifOutOfLength(username, field, minLength, maxLength)) {
			return true;
		}
		Matcher m = USERNAME_PATTERN.matcher(username);
		if (!m.matches()) {
			addErrorString("错误的用户名格式： " + field);
			return true;
		}
		return false;
	}

	public boolean ifNotExist(Object o, Class<?> clazz, Serializable id) {
		if (o == null) {
			addErrorString(clazz.getSimpleName() + "找不到，id=" + id);
			return true;
		} else {
			return false;
		}
	}

	public void noPermission(Class<?> clazz, Serializable id) {
		addErrorString(clazz.getSimpleName() + "没有权限，id=" + id);
	}

	private List<String> errors;

	/**
	 * 获得错误页面的地址
	 * 
	 * @return
	 */
	protected abstract String getErrorPage();

	/**
	 * 获得错误参数名称
	 * 
	 * @return
	 */
	protected abstract String getErrorAttrName();
}
