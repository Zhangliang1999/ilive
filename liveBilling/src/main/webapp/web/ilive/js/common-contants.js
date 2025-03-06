var baseHomeUrl = "/liveBilling";
var loginUrl = "http://mp.zbt.tv189.net/ilive/admin/login.do";
var logoutUrl = "http://mp.zbt.tv189.net/ilive/admin/logout.do";

// 结算单状态
var settlementLogStatusBeans = [ {
	label : "已结算",
	value : 1
}, {
	label : "作废",
	value : 2
} ];
// 结算单发票状态
var invoiceStatusBeans = [ {
	label : "未开",
	value : 0
}, {
	label : "已开",
	value : 1
} ];
// 流水结算状态
var settlementStatusBeans = [ {
	label : "未结算",
	value : 1
}, {
	label : "已结算",
	value : 2
} ];
// 流水收益类型
var flowTypeBeans = [ {
	label : "礼物",
	value : 1
}, {
	label : "打赏",
	value : 2
}, {
	label : "付费观看",
	value : 3
} ];
var packageTypeBeans = [ {
	label : "基础套餐",
	value : 1
}, {
	label : "存储叠加",
	value : 2
}, {
	label : "短信叠加",
	value : 3
}, {
	label : "计时叠加",
	value : 4
}, {
	label : "子账户人数叠加",
	value : 5
}, {
	label : "并发叠加",
	value : 6
} ];
var vaildDurationBeans = [ {
	label : "年",
	value : 1
}, {
	label : "月",
	value : 2
}, {
	label : "日",
	value : 3
} ];
var paymentStatusBeans = [ {
	label : "申请中",
	value : 1
}, {
	label : "处理中",
	value : 2
}, {
	label : "完成",
	value : 3
}, {
	label : "取消",
	value : 4
} ];
var paymentTypeBeans = [ {
	label : "套餐订购",
	value : 1
}, {
	label : "资源包叠加",
	value : 2
}, {
	label : "套餐升级",
	value : 3
}, {
	label : "套餐续购",
	value : 4
} ];
var paymentAutoBeans = [ {
	label : "续订",
	value : 1
}, {
	label : "不续订",
	value : 2
} ];
var paymentWayBeans = [ {
	label : "线下转账",
	value : 1
}, {
	label : "银行转账",
	value : 2
}, {
	label : "支付宝",
	value : 3
}, {
	label : "微信",
	value : 4
} ];
var channelTypeBeans = [ {
	label : "官网平台",
	value : 1
}, {
	label : "渠道代理",
	value : 2
}, {
	label : "集团产品库",
	value : 3
} ];
var productTypeBeans = [ {
	label : "功能权限",
	value : 1
}, {
	label : "存储空间",
	value : 2
}, {
	label : "短信",
	value : 3
}, {
	label : "时长",
	value : 4
}, {
	label : "并发",
	value : 5
}, {
	label : "子账户人数",
	value : 6
} ];