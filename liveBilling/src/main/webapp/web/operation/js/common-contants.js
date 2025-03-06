var baseHomeUrl = "/liveBilling";
var loginUrl = "http://mgr.zbt.tv189.net/imanager/operator/login.do";
var logoutUrl = "http://mgr.zbt.tv189.net/imanager/operator/logout.do";

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
var packageStatusBeans = [ {
	label : "新建",
	value : 1
}, {
	label : "发售中",
	value : 2
}, {
	label : "停售",
	value : 3
} ];
var channelTypesBeans = [ {
	label : "官方平台",
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