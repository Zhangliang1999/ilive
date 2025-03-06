package com.jwzt.common.web.springmvc;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

/**
 * @author ysf
 */
public class RenderJsonUtils {

	/**
	 * json返回成功常量
	 */
	public static final int JSON_STATUS_SUCCESS = 1;

	/**
	 * json返回失败常量
	 */
	public static final int JSON_STATUS_ERROR = 0;

	/**
	 * 添加NULL参数的错误信息
	 * 
	 * @param mp
	 */
	public final static void addNullParamsError(final ModelMap mp) {
		addNullParamsError(mp, null);
	}

	/**
	 * 添加NULL参数的错误信息
	 * 
	 * @param mp
	 * @param paramName
	 *            NULL参数的名称
	 */
	public final static void addNullParamsError(final ModelMap mp, String paramName) {
		String message;
		if (StringUtils.isBlank(paramName)) {
			message = "参数为空。";
		} else {
			message = "参数 " + paramName + " 为空。";
		}
		addError(mp, JSON_STATUS_ERROR, message, "");
	}

	/**
	 * 添加数据库中不存在实体的错误信息
	 * 
	 * @param mp
	 */
	public final static void addNotExistBeanError(final ModelMap mp) {
		addNotExistBeanError(mp, null);
	}

	/**
	 * 添加数据库中不存在实体的错误信息
	 * 
	 * @param mp
	 * @param beanName
	 *            不存在实体的名称
	 */
	public final static void addNotExistBeanError(final ModelMap mp, String beanName) {
		String message;
		if (StringUtils.isBlank(beanName)) {
			message = "内容不存在。";
		} else {
			message = beanName + " 不存在。";
		}
		addError(mp, JSON_STATUS_ERROR, message, "");
	}

	/**
	 * 添加成功的信息 status为默认，message为默认，data为默认
	 * 
	 * @param mp
	 */
	public final static void addSuccess(final ModelMap mp) {
		addSuccess(mp, null, null, null);
	}

	/**
	 * 添加成功的信息 status为默认，message为默认
	 * 
	 * @param mp
	 * @param data
	 *            数据
	 */
	public final static void addSuccess(final ModelMap mp, Object data) {
		addSuccess(mp, null, null, data);
	}

	/**
	 * 添加成功的信息 status为默认，message为默认
	 * 
	 * @param mp
	 * @param status
	 *            状态
	 * @param data
	 *            数据
	 */
	public final static void addSuccess(final ModelMap mp, Integer status, Object data) {
		addSuccess(mp, status, null, data);
	}

	/**
	 * 添加成功的信息 status为默认，message为默认
	 * 
	 * @param mp
	 * @param message
	 *            信息
	 * @param data
	 *            数据
	 */
	public final static void addSuccess(final ModelMap mp, String message, Object data) {
		addSuccess(mp, null, message, data);
	}

	/**
	 * 添加成功的信息
	 * 
	 * @param mp
	 * @param status
	 *            状态，默认是 JSON_STATUS_SUCCESS = 100
	 * @param message
	 *            信息，默认是 "成功。"
	 * @param data
	 *            数据，默认是""
	 */
	public final static void addSuccess(final ModelMap mp, Integer status, String message, Object data) {
		if (null == status) {
			status = JSON_STATUS_SUCCESS;
		}
		if (StringUtils.isBlank(message)) {
			message = "成功。";
		}
		if (null == data) {
			data = "";
		}
		addCommon(mp, status, message, data);
	}

	/**
	 * 添加错误的信息
	 * 
	 * @param mp
	 * @param status
	 *            状态，默认是 JSON_STATUS_SUCCESS = 100
	 * @param message
	 *            信息，默认是 "成功。"
	 */
	public final static void addError(final ModelMap mp, String message) {
		addError(mp, null, message, null);
	}

	/**
	 * 添加错误的信息
	 * 
	 * @param mp
	 * @param message
	 *            信息，默认是 "失败。"
	 * @param data
	 *            数据，默认是""
	 */
	public final static void addError(final ModelMap mp, String message, Object data) {
		if (StringUtils.isBlank(message)) {
			message = "失败。";
		}
		addError(mp, null, message, data);
	}

	/**
	 * 添加错误的信息
	 * 
	 * @param mp
	 * @param status
	 *            状态，默认是 JSON_STATUS_ERROR = 500
	 * @param message
	 *            信息，默认是 "失败。"
	 * @param data
	 *            数据，默认是""
	 */
	public final static void addError(final ModelMap mp, Integer status, String message, Object data) {
		if (null == status) {
			status = JSON_STATUS_ERROR;
		}
		if (StringUtils.isBlank(message)) {
			message = "失败。";
		}
		if (null == data) {
			data = "";
		}
		addCommon(mp, status, message, data);
	}

	/**
	 * 添加信息
	 * 
	 * @param mp
	 *            状态
	 * @param status
	 *            信息
	 * @param message
	 *            数据
	 * @param data
	 */
	public final static void addCommon(final ModelMap mp, final int status, String message, Object data) {
		Assert.notNull(mp, "ModelMap mp is null.");
		mp.put("code", status);
		if (StringUtils.isBlank(message)) {
			mp.put("message", "");
		} else {
			mp.put("message", message);
		}
		if (null == data) {
			mp.put("data", "");
		} else {
			mp.put("data", data);
		}
	}
}
