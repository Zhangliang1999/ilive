原型图地址：
企业自服务：http://47.93.187.130/tv189/
企业播管平台:http://${ip}:${port}/ilive/manager/enterprise/overview.do
X:\Administrator\workspace5\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps
#尚未解决的问题
日期 			状态					提交人  				问题描述
3.26							卫旗					联合主键Operand should contain 1 column(s)

// 简单创建企业信息
/app/room/cert/simple.jspx
{
	userName 账号
	simpleEnterpriseName 企业名称
	password  密码
	phoneNum  手机号
	vpassword 验证码
}

/appuser/sendmsg.jspx

//创建直播间		
liveTitle  标题
liveDesc   简介
返回
最新用户信息
验证码类型
登录：login
注册：reg
简单认证：simplecert

创建直播
直播聊天
/ilive/livephone/room/message/intoRoom.jspx
request :
{
userId  用户Id
liveId  直播间Id
sessionType  连接session类型  登录类型 0 IoSession（APP） 1 WebSocketSession(WEB登录)
userType      用户类型 	0 个人（观看端） 		1企业（直播端）
}	

response：
{
	code
	message
	data
}

发消息

ilive/app/room/getrecordlist.jspx
roomId



观看端进入接口之三部曲

第一部：检测房间是否需要登录
appInterfaceUrl:ilive/app/room/checkRoomLogin.jspx
requestMethod: GET
requestParam
{
	roomId
	userId
	loginToken
}
responseData
{
	roomNeedLogin  1需要登录  2不需要登录
	用户信息
	userInfo
	{
	
	}
}

第二步：针对房间鉴权类型，引导进入不同鉴权方式对应的鉴定界面  【引导完毕，需要重走三部曲】
appInterfaceUrl:ilive/app/room/getRoomInfo.jspx
requestMethod: GET
requestParam
{
	roomId
	userId
	loginToken
}
responseData
{

}

房间密码的接口
ilive/app/room/checkRoomPassword.jspx
roomId
roomPassword



第三步：进入直播间
进入房间
ilive/app/room/roomenter.jspx
roomId  房间号
sessionType   0 标识为个人


回看鉴权接口 (3步)
/ilive/app/room/vod/checklogin.jspx
/ilive/app/room/vod/guide.jspx
/ilive/app/room/vod/fileinfo.jspx
param{
	fileId 回看文件Id
	userId 回看用户Id
	loginToken  登录loginToken

}

发表评论
/ilive/app/room/vod/pushcomments.jspx
POST
param{
	userId用户Id
	comments 评论内容
	fileId 回看文件ID
}



获取评论 
/ilive/app/room/vod/getcomments.jspx
fileId 回看文件ID
pageNo 页码
pageSize 页数


检测房间密码
/ilive/app/room/vod/checkFilePassword.jspx
param{
	fileId  回看文件ID
	passWord 输入密码
}


用户信息修改
/ilive/appuser/useinfo/update.jspx
method:post
param{
	userId 用户ID
	nailName 用户昵称   ***非空
	loginToken	登录token
	userImg		用户头像地址
}

response {
	code : 
	message
	data :{
		同登录时返回的用户信息
	}
}


直播间信息修改 {同创建房间思路一致}
/ilive/livephone/room/edit.jspx
method:post
requetParam{
	roomId 房间ID
	liveTitle
	liveDesc
	hostname
	liveStartTime
	converAddr
	viewAuthorized
	viewPassword
	pushMsgSwitch
	logoPosition
	logoUrl
}

response {
	code : 
	message
	data :{
		同登录时返回的用户信息
	}
}

关注
/ilive/app/room/enterprise/concern.jspx

关注企业的直播
我的问答
/ilive/app/personal/questionAnswer.jspx
method：GET
param{
	pageNo
	pageSize
	userId
}

我的观看历史
/ilive/app/personal/viewhistory.jspx
method：GET
param{
	pageNo
	pageSize
	userId
}

我的关注的企业的直播
/ilive/app/personal/concernlive.jspx
method：GET
param{
	pageNo
	pageSize
	userId
}

我的被邀请的直播
/ilive/app/personal/invite.jspx
method：GET
param{
	pageNo
	pageSize
	userId
}


直播端回看API

我的直播回看
/ilive/livephone/room/vod/files.jspx
userId 用户ID
pageNo 页码
pageSize 页数

上下线
/ilive/livephone/room/vod/online.jspx
userId 用户ID
fileId 文件ID
onlineState 1为上线  0为下线


获取评论接口
/ilive/livephone/room/vod/getcomments.jspx
fileId 文件ID
pageNo 页码
pageSize 页数





