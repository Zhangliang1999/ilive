package com.jwzt.billing.constants;

/**
 * @author ysf
 */
public class RenderJsonConstants {
	/**
	 * 接口返回状态码：用户未登录
	 */
	public static final Integer CODE_NOT_LOGIN = 501;
	/**
	 * 接口返回状态码：流水不存在
	 */
	public static final Integer CODE_FLOW_NOT_FOUND = 502;
	/**
	 * 接口返回状态码：流水已结算
	 */
	public static final Integer CODE_FLOW_SETTLED = 503;
	/**
	 * 接口返回状态码：流水不是一个企业的
	 */
	public static final Integer CODE_FLOW_NOT_SAME_ENTERPRISE = 504;
}
