package com.bvRadio.iLive.iLive.action.util;

import java.util.Hashtable;
import java.util.List;

/**
 * 企业功能缓存
 * @author YanXL
 *
 */
public class EnterpriseCache {
	
	/**
	 * 企业开通功能
	 * @key enterpriseId 企业ID
	 * @value  list 功能编码集
	 */
	private static Hashtable<Integer, List<Integer>> enterpriseMap = new Hashtable<Integer, List<Integer>>();
	/**
	 * 直播间对应企业
	 * @key roomId 直播间ID
	 * @value  enterpriseId 企业ID
	 */
	private static Hashtable<Integer, Integer> roomEnterpriseMap = new Hashtable<Integer, Integer>();
	/**
	 * @key 企业ID
	 * @value   @key String    store      sms        duration  concurrent
	 * 			@value  String 存储时 单位：kb  短信     单位：条  时长     单位：s  并发数 单位：个         
	 */
	private static Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = new Hashtable<Integer, Hashtable<String, String>>();
	/**
	 * 企业开通功能
	 * @key enterpriseId 企业ID
	 * @value  list 功能编码集
	 */
	public static Hashtable<Integer, List<Integer>> getEnterpriseMap() {
		return enterpriseMap;
	}
	/**
	 * 直播间对应企业
	 * @key roomId 直播间ID
	 * @value  enterpriseId 企业ID
	 */
	public static Hashtable<Integer, Integer> getRoomEnterpriseMap() {
		return roomEnterpriseMap;
	}
	/**
	 * @key 企业ID
	 * @value   @key String    store      sms        duration  concurrent
	 * 			@value  String 存储时 单位：kb  短信     单位：条  时长     单位：s  并发数 单位：个
	 */
	public static Hashtable<Integer, Hashtable<String, String>> getEnterpriseStrMap() {
		return enterpriseStrMap;
	}
	/**
	 * 已使用  企业存储容量标示
	 */
	public static final String ENTERPRISE_USE_Store="useStore";
	/**
	 * 总量  企业存储容量标示
	 */
	public static final String ENTERPRISE_Store="store";
	/**
	 * 已使用 企业邀请短信条数
	 */
	public static final String ENTERPRISE_USE_Sms="useSms";
	/**
	 * 总量  企业邀请短信条数
	 */
	public static final String ENTERPRISE_Sms="sms";
	/**
	 * 已使用 企业直播时长
	 */
	public static final String ENTERPRISE_USE_Duration="useDuration";
	/**
	 * 总量  企业直播时长
	 */
	public static final String ENTERPRISE_Duration="duration";
	/**
	 * 已使用 企业并发
	 */
	public static final String ENTERPRISE_USE_Concurrent="useConcurrent";
	/**
	 * 总量 企业并发
	 */
	public static final String ENTERPRISE_Concurrent="concurrent";
	
	/**
	 * 直播创建（个数不限)	10001
	 */
	public static final Integer ENTERPRISE_FUNCTION_createLive=10001;
	/**
	 *直播创建  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_createLive_Content="您无权创建更多的直播间，请完成认证或者购买相应套餐";
	/**
	 * 手机	10002
	 */
	public static final Integer ENTERPRISE_FUNCTION_cellPhone=10002;
	/**
	 *手机  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_cellPhone_Content="请购买 手机  套餐";
	/**
	 * 设备直播（摄像机）	10003
	 */
	public static final Integer ENTERPRISE_FUNCTION_equipmentLive=10003;
	/**
	 *设备直播（摄像机）  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_equipmentLive_Content="请购买 设备直播（摄像机）  套餐";
	/**
	 * 拉流直播	10004
	 */
	public static final Integer ENTERPRISE_FUNCTION_lahueLive=10004;
	/**
	 *拉流直播  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_lahueLive_Content="请购买  拉流直播  套餐";
	/**
	 * 直播播放嵌入		10005
	 */
	public static final Integer ENTERPRISE_FUNCTION_livePlay=10005;
	/**
	 *直播播放嵌入  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_livePlay_Content="请购买  直播播放嵌入  套餐";
	/**
	 * 直播实时录制		10006
	 */
	public static final Integer ENTERPRISE_FUNCTION_liveRecording=10006;
	/**
	 *直播实时录制  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_liveRecording_Content="请购买  直播实时录制  套餐";
	/**
	 * 自动生成回看		10007
	 */
	public static final Integer ENTERPRISE_FUNCTION_automaticallyGenerate =10007;
	/**
	 *自动生成回看	 提示语
	 */
	public static final String ENTERPRISE_FUNCTION_automaticallyGenerate_Content="请购买  自动生成回看  套餐";
	/**
	 * 实时拆条		10008
	 */
	public static final Integer ENTERPRISE_FUNCTION_stripRemoval =10008;
	/**
	 *实时拆条	 提示语
	 */
	public static final String ENTERPRISE_FUNCTION_stripRemoval_Content="请购买  实时拆条  套餐";
	/**
	 * 精彩截图		10009
	 */
	public static final Integer ENTERPRISE_FUNCTION_wonderfulScreenshot =10009;
	/**
	 *精彩截图	 提示语
	 */
	public static final String ENTERPRISE_FUNCTION_wonderfulScreenshot_Content="请购买  精彩截图  套餐";
	/**
	 * 垫片/垫图（宣传片播放）		10010
	 */
	public static final Integer ENTERPRISE_FUNCTION_Gasket =10010;
	/**
	 * 垫片/垫图（宣传片播放）	 提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Gasket_Content="请购买  垫片/垫图（宣传片播放） 套餐";
	/**
	 * 云导播			10011
	 */
	public static final Integer ENTERPRISE_FUNCTION_CloudGuidedSeeding =10011;
	/**
	 * 云导播	 提示语
	 */
	public static final String ENTERPRISE_FUNCTION_CloudGuidedSeeding_Content="请购买  云导播  套餐";
	/**
	 * 直播矩阵监控			10012
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveMatrixMonitoring =10012;
	/**
	 * 直播矩阵监控    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveMatrixMonitoring_Content="请购买  直播矩阵监控  套餐";
	/**
	 * 延迟直播			10013
	 */
	public static final Integer ENTERPRISE_FUNCTION_DelayedLive =10013;
	/**
	 * 延迟直播    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_DelayedLive_Content="请购买   延迟直播  套餐";
	/**
	 * 聊天/弹幕		10014
	 */
	public static final Integer ENTERPRISE_FUNCTION_ChatBombScreen =10014;
	/**
	 * 聊天/弹幕    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_ChatBombScreen_Content="请购买   聊天/弹幕  套餐";
	/**
	 * 问答		10015
	 */
	public static final Integer ENTERPRISE_FUNCTION_Question =10015;
	/**
	 * 问答    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Question_Content="请购买   问答  套餐";
	/**
	 * 签到		10016
	 */
	public static final Integer ENTERPRISE_FUNCTION_Sign =10016;
	/**
	 * 签到    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Sign_Content="请购买   签到    套餐";
	/**
	 * 投票		10017
	 */
	public static final Integer ENTERPRISE_FUNCTION_Vote =10017;
	/**
	 * 投票    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Vote_Content="请购买   投票   套餐";
	/**
	 * 抽奖		10018
	 */
	public static final Integer ENTERPRISE_FUNCTION_Lottery =10018;
	/**
	 * 抽奖    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Lottery_Content="请购买   抽奖  套餐";
	/**
	 * 跑马灯（公告）		10019
	 */
	public static final Integer ENTERPRISE_FUNCTION_Advertising =10019;
	/**
	 * 跑马灯    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Advertising_Content="请购买   跑马灯  套餐";
	/**
	 * 微信红包		10020
	 */
	public static final Integer ENTERPRISE_FUNCTION_WechatRed =10020;
	/**
	 * 微信红包    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_WechatRed_Content="请购买  微信红包  套餐";
	/**
	 * 打赏/礼物		10021
	 */
	public static final Integer ENTERPRISE_FUNCTION_PlayGift =10021;
	/**
	 * 打赏/礼物  提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PlayGift_Content="请购买 打赏/礼物  套餐";
	/**
	 * 点赞		10022
	 */
	public static final Integer ENTERPRISE_FUNCTION_DotPraise =10022;
	/**
	 * 点赞    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_DotPraise_Content="请购买  点赞  套餐";
	/**
	 * 图文直播（话题直播）		10023
	 */
	public static final Integer ENTERPRISE_FUNCTION_TopicBroadcast =10023;
	/**
	 * 图文直播（话题直播）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_TopicBroadcast_Content="请购买  图文直播（话题直播）  套餐";
	/**
	 * 连麦			10024
	 */
	public static final Integer ENTERPRISE_FUNCTION_LianMai =10024;
	/**
	 * 连麦    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LianMai_Content="请购买  连麦  套餐";
	/**
	 * 文档直播		10025
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveDocument =10025;
	/**
	 * 文档直播    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveDocument_Content="请购买  文档直播  套餐";
	/**
	 * 直播内容简介自定义（详情）		10026
	 */
	public static final Integer ENTERPRISE_FUNCTION_BroadcastContentDetails =10026;
	/**
	 * 直播内容简介自定义（详情）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_BroadcastContentDetails_Content="请购买  直播内容简介自定义（详情）  套餐";
	/**
	 * 直播封面自定义		10027
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveCover =10027;
	/**
	 * 播封面自定义    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveCover_Content="请购买  播封面自定义  套餐";
	/**
	 * 播放器背景图自定义		10028
	 */
	public static final Integer ENTERPRISE_FUNCTION_PlayBackground =10028;
	/**
	 * 播放器背景图自定义    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PlayBackground_Content="请购买  播放器背景图自定义  套餐";
	/**
	 * 播放画面台标自定义		10029
	 */
	public static final Integer ENTERPRISE_FUNCTION_PlayTableMark =10029;
	/**
	 * 播放画面台标自定义    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PlayTableMark_Content="请购买  播放画面台标自定义  套餐";
	/**
	 * 直播引导页（内容介绍页）		10030
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveGuideMap =10030;
	/**
	 * 直播引导页（内容介绍页）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveGuideMap_Content="请购买  直播引导页（内容介绍页）  套餐";
	/**
	 * 直播邀请函自定义		10031
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveInvitationLetter =10031;
	/**
	 * 直播邀请函自定义    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveInvitationLetter_Content="请购买  直播邀请函自定义   套餐";
	/**
	 * 观看页栏目自定义（移动端）		10032
	 */
	public static final Integer ENTERPRISE_FUNCTION_WatchColumns =10032;
	/**
	 * 观看页栏目自定义（移动端）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_WatchColumns_Content="请购买  观看页栏目自定义（移动端）   套餐";
	/**
	 * 观看页风格自定义（PC）		10033
	 */
	public static final Integer ENTERPRISE_FUNCTION_ViewStyle =10033;
	/**
	 * 观看页风格自定义（PC）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_ViewStyle_Content="请购买  观看页风格自定义（PC）   套餐";
	/**
	 * 观看页去天翼直播LOGO（PC）		10034
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveViewLogo =10034;
	/**
	 * 观看页去天翼直播LOGO（PC）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveViewLogo_Content="请购买  观看页去天翼直播LOGO（PC）   套餐";
	/**
	 * 微信分享自定义（去天翼直播LOGO）		10035
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveShareLogo =10035;
	/**
	 * 微信分享自定义（去天翼直播LOGO）    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveShareLogo_Content="请购买  微信分享自定义（去天翼直播LOGO）   套餐";
	/**
	 * 直播自服务平台LOGO自定义		10036
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveSelfServiceLogo =10036;
	/**
	 * 直播自服务平台LOGO自定义    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveSelfServiceLogo_Content="请购买  直播自服务平台LOGO自定义   套餐";
	/**
	 * 微信分享、QQ分享		10037
	 */
	public static final Integer ENTERPRISE_FUNCTION_Share =10037;
	/**
	 * 微信分享、QQ分享	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Share_Content="请购买  微信分享、QQ分享   套餐";
	/**
	 * 第三方平台推流		10038
	 */
	public static final Integer ENTERPRISE_FUNCTION_PushStream =10038;
	/**
	 * 第三方平台推流	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PushStream_Content="请购买  第三方平台推流   套餐";
	/**
	 * 观看直播报名		10039
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveRegistration =10039;
	/**
	 * 观看直播报名	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveRegistration_Content="请购买  观看直播报名   套餐";
	/**
	 * 直播预约		10040
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveBooking =10040;
	/**
	 * 直播预约	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveBooking_Content="请购买  直播预约   套餐";
	/**
	 * 直播倒计时		10041
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveCountdown =10041;
	/**
	 * 直播倒计时	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveCountdown_Content="请购买  直播倒计时   套餐";
	/**
	 * 公开观看		10042
	 */
	public static final Integer ENTERPRISE_FUNCTION_PublicViewing =10042;
	/**
	 * 公开观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PublicViewing_Content="请购买  公开观看   套餐";
	/**
	 * 白名单观看		10043
	 */
	public static final Integer ENTERPRISE_FUNCTION_WhiteList =10043;
	/**
	 * 白名单观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_WhiteList_Content="请购买  白名单观看   套餐";
	/**
	 * 密码观看		10044
	 */
	public static final Integer ENTERPRISE_FUNCTION_PasswordWatch =10044;
	/**
	 * 密码观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PasswordWatch_Content="请购买 密码观看   套餐";
	/**
	 * 付费观看		10045
	 */
	public static final Integer ENTERPRISE_FUNCTION_PayTowatch =10045;
	/**
	 * 付费观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PayTowatch_Content="请购买 付费观看   套餐";
	/**
	 * 手机验证观看		10046
	 */
	public static final Integer ENTERPRISE_FUNCTION_PMobileVerificationWatch =10046;
	/**
	 * 手机验证观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_PMobileVerificationWatch_Content="请购买 手机验证观看   套餐";
	/**
	 * 企业主页自定义		10047
	 */
	public static final Integer ENTERPRISE_FUNCTION_CorporateHome =10047;
	/**
	 * 企业主页自定义	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_CorporateHome_Content="请购买  企业主页自定义   套餐";
	/**
	 * 企业主页嵌入微信公众号		10048
	 */
	public static final Integer ENTERPRISE_FUNCTION_CorporateHomePublicnumber =10048;
	/**
	 * 企业主页嵌入微信公众号	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_CorporateHomePublicnumber_Content="请购买  企业主页嵌入微信公众号   套餐";
	/**
	 * 专题页自定义		10049
	 */
	public static final Integer ENTERPRISE_FUNCTION_TopicPage =10049;
	/**
	 * 专题页自定义	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_TopicPage_Content="请购买  专题页自定义   套餐";
	/**
	 * 专题页嵌入微信公众号		10050
	 */
	public static final Integer ENTERPRISE_FUNCTION_TopicPagePublicnumber =10050;
	/**
	 * 专题页嵌入微信公众号	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_TopicPagePublicnumber_Content="请购买  专题页嵌入微信公众号   套餐";
	/**
	 * 关注企业		10051
	 */
	public static final Integer ENTERPRISE_FUNCTION_FocusOnTheEnterprise =10051;
	/**
	 * 关注企业	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_FocusOnTheEnterprise_Content="请购买  关注企业  套餐";
	/**
	 * 企业粉丝信息导出			10052
	 */
	public static final Integer ENTERPRISE_FUNCTION_EnterpriseFanImport =10052;
	/**
	 * 企业粉丝信息导出	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_EnterpriseFanImport_Content="请购买 企业粉丝信息导出  套餐";
	/**
	 * 观众黑名单管理		10053
	 */
	public static final Integer ENTERPRISE_FUNCTION_AudienceBlacklist =10053;
	/**
	 * 观众黑名单管理	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_AudienceBlacklist_Content="请购买 观众黑名单管理  套餐";
	/**
	 * 直播助手			10054
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveAssistant =10054;
	/**
	 * 直播助手	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveAssistant_Content="请购买 直播助手  套餐";
	/**
	 * 直播间授权管理			10055
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveRoomAuthorization =10055;
	/**
	 * 直播间授权管理	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveRoomAuthorization_Content="请购买 直播间授权管理  套餐";
	/**
	 * 子账号管理				10056
	 */
	public static final Integer ENTERPRISE_FUNCTION_SubAccountManagement =10056;
	/**
	 * 子账号管理	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_SubAccountManagement_Content="请购买 子账号管理  套餐";
	/**
	 * 向粉丝推荐直播-APP内推送直播推荐			10057
	 */
	public static final Integer ENTERPRISE_FUNCTION_APPContentRecommendation =10057;
	/**
	 * 向粉丝推荐直播-APP内推送直播推荐	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_APPContentRecommendation_Content="请购买 向粉丝推荐直播-APP内推送直播推荐  套餐";
	/**
	 * 邀请观看-向指定的用户发送邀请观看短信      1万条	10058
	 */
	public static final Integer ENTERPRISE_FUNCTION_InvitedToWatch =10058;
	/**
	 * 邀请观看-向指定的用户发送邀请观看短信	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_InvitedToWatch_Content="请购买 邀请观看-向指定的用户发送邀请观看短信  套餐";
	/**
	 * 视频/图片存储	100G	500G	1000G	10059
	 */
	public static final Integer ENTERPRISE_FUNCTION_Store =10059;
	/**
	 * 视频/图片存储	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Store_Content="请购买 视频/图片存储  套餐";
	/**
	 * 内容下载		10060
	 */
	public static final Integer ENTERPRISE_FUNCTION_ContentDownload =10060;
	/**
	 * 内容下载	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_ContentDownload_Content="请购买 内容下载  套餐";
	/**
	 * 内容上传		10061
	 */
	public static final Integer ENTERPRISE_FUNCTION_ContentUpload =10061;
	/**
	 * 内容上传	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_ContentUpload_Content="请购买 内容上传  套餐";
	/**
	 * 视频在线剪辑		10062
	 */
	public static final Integer ENTERPRISE_FUNCTION_VideoClips =10062;
	/**
	 * 视频在线剪辑		    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_VideoClips_Content="请购买 视频在线剪辑	  套餐";
	/**
	 * 点播视频观看		10063
	 */
	public static final Integer ENTERPRISE_FUNCTION_VodWatch =10063;
	/**
	 * 点播视频观看	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_VodWatch_Content="请购买 点播视频观看  套餐";
	/**
	 * 直播数据报表		10064
	 */
	public static final Integer ENTERPRISE_FUNCTION_LiveDataReport =10064;
	/**
	 * 直播数据报表	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LiveDataReport_Content="请购买 直播数据报表  套餐";
	/**
	 * 回看数据报表		10065
	 */
	public static final Integer ENTERPRISE_FUNCTION_LookBackATheDataReport =10065;
	/**
	 * 回看数据报表	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_LookBackATheDataReport_Content="请购买 回看数据报表  套餐";
	/**
	 * 用户观看数据明细查询			10066
	 */
	public static final Integer ENTERPRISE_FUNCTION_UserViewingData =10066;
	/**
	 * 用户观看数据明细查询	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_UserViewingData_Content="请购买 用户观看数据明细查询  套餐";
	/**
	 * 观看数据导出			10067
	 */
	public static final Integer ENTERPRISE_FUNCTION_ViewData =10067;
	/**
	 * 观看数据导出	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_ViewData_Content="请购买 观看数据导出  套餐";
	/**
	 * API深度植入			10068
	 */
	public static final Integer ENTERPRISE_FUNCTION_API =10068;
	/**
	 * API深度植入	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_API_Content="请购买 API深度植入  套餐";
	/**
	 * SDK深入植入			10069
	 */
	public static final Integer ENTERPRISE_FUNCTION_SDK =10069;
	/**
	 * SDK深入植入	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_SDK_Content="请购买 SDK深入植入  套餐";
	/**
	 * 官网/微信公众号嵌入			10070
	 */
	public static final Integer ENTERPRISE_FUNCTION_Website =10070;
	/**
	 * 官网/微信公众号嵌入	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_Website_Content="请购买 官网/微信公众号嵌入  套餐";
	/**
	 * 防盗链		10071
	 */
	public static final Integer ENTERPRISE_FUNCTION_AntiTheftChain =10071;
	/**
	 * 防盗链		    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_AntiTheftChain_Content="请购买 防盗链  套餐";
	/**
	 * 桌面直播工具		10073
	 */
	public static final Integer ENTERPRISE_FUNCTION_DeskTop =10073;
	/**
	 * 桌面直播工具	    提示语
	 */
	public static final String ENTERPRISE_FUNCTION_DeskTop_Content="请购买 桌面直播工具  套餐";
}
