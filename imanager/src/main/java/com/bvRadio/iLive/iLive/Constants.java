package com.bvRadio.iLive.iLive;

/**
 * 常量
 * 
 */
public class Constants {
	/**
	 * 管理员角色
	 */
	public static final int ADMIN_ROLE = 1;
	/**
	 * 普通用户角色
	 */
	public static final int COMMON_USER_ROLE = 2;
	/**
	 * 成功状态码
	 */
	public static final int SUCCESS_STATUS = 1;
	/**
	 * 错误状态码
	 */
	public static final int ERROR_STATUS = 0;
	/**
	 * 文字信息类型
	 */
	public static final int MSG_TYPE_TXT = 1;
	/**
	 * 图片信息类型
	 */
	public static final int MSG_TYPE_IMG = 2;
	/**
	 * 音频信息类型
	 */
	public static final int MSG_TYPE_AUDIO = 3;
	/**
	 * 视频信息类型
	 */
	public static final int MSG_TYPE_VIDEO = 4;
	/**
	 * 上传图片文件类型
	 */
	public static final String UPLOAD_FILE_TYPE_IMAGE = "image";
	/**
	 * 上传视频文件类型
	 */
	public static final String UPLOAD_FILE_TYPE_VIDEO = "video";
	/**
	 * 新加上传视频类型--垫片
	 */
	public static final String UPLOAD_FILE_TYPE_BUTTON = "button";
	/**
	 * 上传互动图片文件类型（需要旋转）
	 */
	public static final String UPLOAD_FILE_TYPE_INTERACT_IMAGE = "interactImage";
	/**
	 * 直播信息之图文直播消息类型   话题
	 */
	public static final int LIVE_MSG_TYPE_LIVE = 1;
	/**
	 * 直播信息之聊天互动消息类型
	 */
	public static final int LIVE_MSG_TYPE_INTERACT = 2;
	/**
	 * 直播信息之问答消息类型
	 */
	public static final int LIVE_MSG_TYPE_QUIZ = 3;
	/**
	 * 直播信息之直播简介类型
	 */
	public static final int LIVE_MSG_TYPE_ZBJJ = 4;
	/**
	 * 直播信息之视频列表类型
	 */
	public static final int LIVE_MSG_TYPE_SPLB = 5;
	/**
	 * 直播信息之文档直播类型
	 */
	public static final int LIVE_MSG_TYPE_WDZB = 6;
	/**
	 * 未读状态
	 */
	public static final int MSG_STATUS_UNREAD = 0;
	/**
	 * 已读状态
	 */
	public static final int MSG_STATUS_READ = 1;
	/**
	 * 直播类型之音频直播
	 */
	public static final int LIVE_TYPE_AUDIO = 1;
	/**
	 * 直播类型之视频直播
	 */
	public static final int LIVE_TYPE_VIDEO = 2;
	/**
	 * 直播状态之下线
	 */
	public static final int LIVE_STATUS_OFFLINE = 0;
	/**
	 * 直播状态之上线
	 */
	public static final int LIVE_STATUS_ONLINE = 1;
	/**
	 * socket session中的用户对象key
	 */
	public static final String SOCKET_SESSION_USER_KEY = "user";
	/**
	 * socket session中的直播ID key
	 */
	public static final String SOCKET_SESSION_LIVE_ID_KEY = "liveId";

	
//=========================================================================================================================================
	
	/**
	 * 提现状态：0 ：处理中 
	 */
	public static final Integer PRESENT_ING = 0;
	/**
	 * 提现状态： 1：拒绝提现
	 */
	public static final Integer PRESENT_NO = 1;
	/**
	 * 提现状态： 2：提现成功
	 */
	public static final Integer PRESENT_YES = 2;
	
	/**
	 * 支付类型：  1：微信
	 */
	public static final Integer PAYMENT_TYPE_WEIXIN=1;
	/**
	 * 支付类型：0：支付宝    
	 */
	public static final Integer PAYMENT_TYPE_ALIPAY=0;
	/**
	 * 苹果购买支付
	 */
	public static final Integer PAYMENT_TYPE_APPLE=2;
	/**
	 *苹果端充值 
	 */
	public static final Integer PAYMENT_TYPE_PORT_IPD=0;
	/**
	 *安卓端充值 
	 */
	public static final Integer PAYMENT_TYPE_PORT_AD=1;
	/**
	 * 支付状态：0 ：已支付    
	 */
	public static final Integer PAYMENT_STATUS_YES=0;
	/**
	 * 支付状态： 1：未支付
	 */
	public static final Integer PAYMENT_STATUS_NO=1;
	/**
	 * 支付状态：2 ：退款    
	 */
	public static final Integer PAYMENT_STATUS_REFUND=2;
	
	/**
	 * 消费类型：0:收入 
	 */
	public static final Integer CONSUMPTION_TYPE_IN=0;
	/**
	 * 消费类型：1:支出 
	 */
	public static final Integer CONSUMPTION_TYPE_OUT=1;
	/**
	 * 提现账户类型 ：0 支付宝 
	 */
	public static final Integer PRESENT_TYPE_alipay=0;
	/**
	 * 提现账户类型：1 微信
	 */
	public static final Integer PRESENT_TYPE_weixin=1;
	/**
	 * 提现方式：0 ：红包收益提现
	 */
	public static final Integer PRESENT_Income_red=0;
	
	/**
	 * 提现方式：1 ：用户收益提现
	 */
	public static final Integer PRESENT_Income_user=1;
	/**
	 * 红包类型： 送
	 */
	public static final Integer RED_OUT=0;
	/**
	 * 红包类型：收
	 */
	public static final Integer RED_IN=1;
	/**
	 * 红包类型：退回
	 */
	public static final Integer RED_BACK=2;
	
}
