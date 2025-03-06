package com.bvRadio.iLive.core.web;

public class WebErrors extends com.bvRadio.iLive.common.web.springmvc.WebErrors {
	/**
	 * 默认错误页面
	 */
	public static final String ERROR_PAGE = "/common/error_message";
	/**
	 * 默认错误信息属性名称
	 */
	public static final String ERROR_ATTR_NAME = "errors";

	/**
	 * 通过HttpServletRequest创建WebErrors
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 * @return 如果LocaleResolver存在则返回国际化WebErrors
	 */
	public static WebErrors create() {
		return new WebErrors();
	}

	public WebErrors() {
	}

	@Override
	protected String getErrorAttrName() {
		return ERROR_ATTR_NAME;
	}

	@Override
	protected String getErrorPage() {
		return ERROR_PAGE;
	}
}
