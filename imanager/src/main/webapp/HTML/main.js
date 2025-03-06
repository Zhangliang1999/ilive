	var h5Base="zb.tv189.com"; 
	var tomcatBase = "mp.zb.tv189.com";
	//var h5Base="192.168.222.156:80";
	//var tomcatBase = "192.168.222.156:8080";
	//页面操作============================================================
	//企业关注  取消
	function updateConcernStatus(enterpriseId,concernStatus){
		$.ajax({
			type : "GET",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/app/room/enterprise/concern.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				enterpriseId : enterpriseId,
				userId : userId,
				type : concernStatus
			},
			success : function(data) {
				console.log(data);
				var status = data.code;
				if(status==1){
					if(concernStatus==0){
						$("#concernStatus").text("关注企业");
						$("#concernStatus").attr("onclick","updateConcernStatus(1)");
					}else{
						$("#concernStatus").text("取消关注");
						$("#concernStatus").attr("onclick","updateConcernStatus(0)");
					}
				}else{
					alert(data.message);
				}
			}
		});
	}
	//点赞
	function saveILiveMessagePraise(msgId){
		var userId = $("input[name=userId]").val();//用户ID
		if(userId==0){
			if(confirm("是否进入登录？")){
				var roomId = $("input[name=roomId]").val();//直播间ID
				window.location.href="http://"+h5Base+"/phone/login.html?roomId="+roomId;
			}else{
				alert("不登录无法发送消息");
			}
			return;
		}
		$.ajax({
			type : "POST",//请求方式 get/post
			url : "http://"+tomcatBase+"/ilive/praise/savePraise.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				userId : userId,
				msgId : msgId
			},
			success : function(data) {
				var status = data.code;
				if(status==1){
					var praiseNumber = data.praiseNumber;
					if(praiseNumber==0){
						var number = $("#praiseNumber_").text();
						var HTML = "<i class=\"fa fa-thumbs-up\"></i>";
						HTML += "<span id=\"praiseNumber_"+msgId+"\">"+number+"</span>";
						$("#messagePraise_"+msgId).html(HTML);
					}else{
						var HTML = "<i class=\"fa fa-thumbs-up\"></i>";
							HTML += "<span id=\"praiseNumber_"+msgId+"\">"+praiseNumber+"</span>";
						$("#messagePraise_"+msgId).html(HTML);
					}
					return;
				}else{
					alert("操作失败");
				}
			}
    	});
		
	}
	
	function selectMediaFile(fileId){
		var roomId = $("input[name=roomId]").val();//直播间ID
		var userId = $("input[name=userId]").val();//用户ID
		window.location.href="http://"+h5Base+"/phone/review.html?fileId="+fileId+"&userId="+userId+"&roomId="+roomId;
	}
	function selectTimer(liveStartTime) {
		var tempStrs = liveStartTime.split(" ");  //截取时间
		// 解析日期部分
		var dateStrs = tempStrs[0].split("-");
		var start_year = parseInt(dateStrs[0], 10);
		var start_month = parseInt(dateStrs[1], 10) - 1;
		var start_day = parseInt(dateStrs[2], 10);
		// 解析时间部分
		var timeStrs = tempStrs[1].split(":");
		var start_hour = parseInt(timeStrs [0], 10);
		var start_minute = parseInt(timeStrs[1], 10);
		var start_second = parseInt(timeStrs[2], 10);
		var date = new Date(start_year, start_month, start_day, start_hour, start_minute, start_second);
		var newDate = new Date();   // 获取当前时间
		var dateTime =  date.getTime() - newDate.getTime();   //时间差的毫秒数      
		if(dateTime>0){
			var days=Math.floor(dateTime/(24*3600*1000));   //计算出相差天数  
			var leave1=dateTime%(24*3600*1000);   //计算天数后剩余的毫秒数 
			var hours=Math.floor(leave1/(3600*1000)) ; 
		    //计算相差分钟数  
		    var leave2=leave1%(3600*1000);      //计算小时数后剩余的毫秒数  
		    var minutes=Math.floor(leave2/(60*1000));  
		    //计算相差秒数  
		    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数  
		    var seconds=Math.round(leave3/1000); 
		    var dataTime = days +" 天 "+hours+" 小时 "+minutes+" 分钟 "+seconds+" 秒";
		    console.log(dataTime);
			$("#openCountdownSwitch").text(dataTime);
			setTimeout(function() {
				selectTimer(liveStartTime);
			}, 1000);
		}else{
			$("#openCountdownSwitch").text("0 天 0 小时 0 分钟 0  秒");
		}
	    
	}
	function selectHTML(roomId){
		var ws;
		window.setInterval(function(){ //每隔5秒钟发送一次心跳，避免websocket连接因超时而自动断开
			var ping = {"p":"1"};
			ws.send(JSON.stringify(ping));
		},10000);
		//用户登录
		$.ajax({
			type : "GET",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/app/room/roomenter.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				roomId : roomId,
				sessionType : 1//WebSocket使用用户
			},
			success : function(data) {
				if (data.code == 1) {
					var hlsAddr = data.hlsAddr;
					$("video").attr("src",hlsAddr);
					var token = data.token;
					console.log(token);
					if (typeof (WebSocket) == "undefined") {
						alert("您的浏览器不支持WebSocket");
						return;
					}
					var url = "ws://" + tomcatBase
							+ "/ilive/webSocketServer.jspx?username="
							+ token;
					ws = new WebSocket(url);
					ws.onopen = function(e) {
						console.log("连接成功");
					}
					ws.onclose = function(e) {
						console.log("关闭连接 " + e.reason);
					}
					ws.onerror = function(e) {
						console.log(e);
						console.log("建立连接异常" + e);
					}
					ws.onmessage = function(e) {
						var iLiveMessage = JSON.parse(e.data);
						var liveMsgType = iLiveMessage.liveMsgType;
						if (iLiveMessage.roomType == 1) {
							if (iLiveMessage.deleted == 1) {
								$("#msgId_" + iLiveMessage.msgId).remove();
							} else {
								//消息管理
								var deleteAll = iLiveMessage.deleteAll;
								if (deleteAll == 1) {
									$("#talkBoxList2").remove();
								} else {
									var userId = $("input[name=userId]").val();
									if(iLiveMessage.senderId==userId){
										console.log(iLiveMessage);
										if(iLiveMessage.opType==11){
											$("input[name=msgContent]").attr("disabled","disabled");
										}else{
											$("input[name=msgContent]").removeAttr("disabled");
										}
									}
									if (liveMsgType == 2) {
										if (iLiveMessage.checked == 1) {
											$("#msgId_" + iLiveMessage.msgId).remove();
											if (iLiveMessage.top == 0) {
												var HTMLTXT = "<div class=\"talkBoxList\" id=\"msgId_"+iLiveMessage.msgId+"\"> <p> <span class=\"username\">"
														+ iLiveMessage.senderName
														+ ":</span>"
														+ iLiveMessage.content
														+ "</p></div>";
												//新增聊天互动
												$("#topTalkBoxList2 #msgId_"+ iLiveMessage.msgId).prepend(HTMLTXT);
												$("#talkBoxList2").prepend(HTMLTXT);
											} else {
												var HTMLTXT = "<div class=\"talkBoxList\" id=\"msgId_"+iLiveMessage.msgId+"\"> <p> <span class=\"username\">"
														+ iLiveMessage.senderName
														+ ":</span><span class=\"zhiding\">顶</span>"
														+ iLiveMessage.content
														+ "</p></div>";
												//新增聊天互动
												$("#talkBoxList2 #msgId_"+ iLiveMessage.msgId).prepend(HTMLTXT);
												$("#topTalkBoxList2").prepend(HTMLTXT);
											}
										}
									} else if (liveMsgType == 3) {
										//问答
										if (iLiveMessage.replyType == 0) {
												//未回答
										} else {
											var HTMLTXT = "<div class=\"answerBox\" id=\"msgId_"+iLiveMessage.msgId+"\" >";
											HTMLTXT += "<p> <span class=\"question\">问</span><span class=\"username\">"
													+ iLiveMessage.senderName
													+ ":</span>"
													+ iLiveMessage.content
													+ "</p>";
											//回答
											HTMLTXT += "<p class=\"answerCon short\"><span class=\"answer\">答</span><span class=\"username\">"
													+ iLiveMessage.replyName
													+ ":</span>"
													+ iLiveMessage.replyContent
													+ "</p>";
											$("#msgId_"+ iLiveMessage.msgId).remove();
											HTMLTXT += "</div>";
											$("#talkBoxList2").prepend(HTMLTXT);
										}
									}else if(liveMsgType == 1){
											var HTMLTXT = "<div class=\"themeBox\" id=\"msgId_"+iLiveMessage.msgId+"\">";
												HTMLTXT += "	<div class=\"themeHeader\" >";
												HTMLTXT +="		<div class=\"themeImg\">";
												HTMLTXT +="			<img src=\""+iLiveMessage.senderImage+"\"  />";
												HTMLTXT +="		</div>";
												HTMLTXT +="		<div class=\"themeName\">"+iLiveMessage.senderName+"</div>";
												HTMLTXT +="		<div class=\"themeTime\">"+iLiveMessage.createTime+"</div>";
												HTMLTXT +="</div>";
												HTMLTXT +="<div class=\"themeBody\">";
												HTMLTXT +="		<div class=\"themeText\">"+iLiveMessage.content+"</div>";
												HTMLTXT +="		<div class=\"themeBottom\">";
												HTMLTXT +="			<div class=\"comment\">";
												HTMLTXT +="				<i class=\"fa fa-commenting\" onclick=\"commentsList("+iLiveMessage.msgId+")\"></i><span id=\"commentsNumber_"+iLiveMessage.msgId+"\">0</span>";
												HTMLTXT +="			</div>";	
												HTMLTXT +="			<div class=\"upvote\" id=\"messagePraise_"+iLiveMessage.msgId+"\">";
												HTMLTXT +="				<i class=\"fa fa-thumbs-o-up\" onclick=\"saveILiveMessagePraise("+iLiveMessage.msgId+")\" ></i><span id=\"praiseNumber_"+iLiveMessage.msgId+"\">"+iLiveMessage.praiseNumber+"</span>";		
												HTMLTXT +="			</div>";
												HTMLTXT +="			<div class=\"arrowBox\">";
												HTMLTXT +="				<i class=\"fa fa-angle-down\"></i>";
												HTMLTXT +="			</div>";
												HTMLTXT +="		</div>";
												HTMLTXT +="</div>";	
												HTMLTXT +="<div class=\"themeFooter\" id=\"commentsList_"+iLiveMessage.msgId+"\">";
												HTMLTXT +="</div></div>";
											$("#themeDiv").prepend(HTMLTXT);
										}
									}
							}
						
						}else if(iLiveMessage.roomType == 0){
							var iLiveEventVo = JSON.parse(iLiveMessage.iLiveEventVo);
							console.log(iLiveEventVo);
							var estoppleType = iLiveEventVo.estoppleType;
							if(estoppleType==0){
								//是否全局禁言 0 禁言开启 1 关闭禁言
								$("input[name=msgContent]").attr("disabled","disabled");
							}else{
								$("input[name=msgContent]").removeAttr("disabled");
							}
							/**
							 * 直播状态：
							 * 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
							 */
							var liveStatus = iLiveEventVo.liveStatus;
							var playBgAddr = iLiveEventVo.playBgAddr;
							console.log(playBgAddr);
							if(liveStatus==0){
								//开始直播直播未开始
								$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播人员在赶来的路上...</div>");
							}else if(liveStatus==1){
								//直播中
								$("#videoImg").html("<video src=\""+iLiveEventVo.hlsUrl+"\" controls playsinline  webkit-playsinline playsinline x5-playsinline x-webkit-airplay=\"allow\"></video>");
							}else if(liveStatus==2){
								//暂停
								$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播人员休息中...</div>");
							}else if(liveStatus==3){
								//直播结束
								$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播结束...</div>");
							}else{
								$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播处理中...</div>");
							}
						} else {
							console.log("操作类型"
									+ iLiveMessage.roomType);
						}
					}
				}
			}
		});
	}	
	//选择发送消息类型
	function selectMsgType() {
		var liveMsgType = $(".liactive").attr("liveMsgType");;
		if(liveMsgType==2){
			var msgValue = $(".wen").attr("msgValue");
			if (msgValue != 3) {
				$(".wen").css({
					background : "url(\"/phone/img/YesWen.png\") no-repeat",
					backgroundSize : "100%"
				});
				$(".wen").attr("msgValue", 3);
			} else {
				$(".wen").css({
					background : "url(\"/phone/img/wen.png\") no-repeat",
					backgroundSize : "100%"
				});
				$(".wen").attr("msgValue", 2);
			}
		}
	}
	
	//发送消息
	function sendMessageSubmitted() {
		var roomId = $("input[name=roomId]").val();//直播间ID
		var userId = $("input[name=userId]").val();//用户ID
		if(userId==0){
			if(confirm("是否进入登录？")){
				window.location.href="http://"+h5Base+"/phone/login.html?roomId="+roomId;
			}else{
				alert("不登录无法发送消息");
			}
			return;
		}
		var liveMsgType = $(".wen").attr("msgValue");//发送消息类型
		console.log("消息类型：" + liveMsgType);
		var liveEventId = $("input[name=liveEventId]").val();//场次ID
		var msgContent = $("input[name=msgContent]").val();//发送消息内容
		if (msgContent.replace("\s+", "") == "" || msgContent == undefined) {
			alert("请输入发送内容");
			return;
		}
		if (liveMsgType == 2||liveMsgType==3) {
			$.ajax({
						type : "POST",//请求方式 get/post
						url : "http://" + tomcatBase + "/ilive/sendMessage.jspx",
						dataType : "jsonp",
						jsonp : "callback",
						cache : false,
						data : {
							userId : userId,
							roomId : roomId,
							liveMsgType : liveMsgType,
							content : encodeURI(msgContent),
							liveEventId : liveEventId
						},
						success : function(data) {
							var status = data.code;
							if (status == 1) {
								console.log("发送消息成功");
								var iLiveMessage = data.iLiveMessage;
								console.log(iLiveMessage);
								if (liveMsgType == 3) {
									$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
									$(".wen").attr("msgValue", 2);
								}
								if (liveMsgType == 2) {
									var HTMLTXT = "<div class=\"talkBoxList\" id=\"msgId_"+iLiveMessage.msgId+"\"> <p> <span class=\"username\">"
										+ iLiveMessage.senderName
										+ ":</span>"
										+ iLiveMessage.content
										+ "</p></div>";
									//新增聊天互动
									$("#talkBoxList2").prepend(HTMLTXT);		
								} else if (liveMsgType == 3) {
									//问答
									var HTMLTXT = "<div class=\"answerBox\" id=\"msgId_"+iLiveMessage.msgId+"\" >";
									HTMLTXT += "<p> <span class=\"question\">问</span><span class=\"username\">"
											+ iLiveMessage.senderName
											+ ":</span>"
											+ iLiveMessage.content
											+ "</p>";
									HTMLTXT += "</div>";
									$("#talkBoxList2").prepend(HTMLTXT);
								}
								$("input[name=msgContent]").val("");
								/*$(".qiandao").removeClass("hide");
								$(".liwu").removeClass("hide");
								$(".sendMessage").addClass("hide");*/
							} else {
								alert(data.message);
							}
						}
					});
		} else if (liveMsgType == 1) {
			var msgId = $("input[name=commentsMsgId]").val();
			if(msgId==0){
				alert("请选择评论话题");
				return;
			}
			$.ajax({
				type : "POST",//请求方式 get/post
				url : "http://" + tomcatBase + "/ilive/saveComments.jspx",
				dataType : "jsonp",
				jsonp : "callback",
				cache : false,
				data : {
					userId : userId,
					msgId : msgId,
					comments : encodeURI(msgContent)
				},
				success : function(data) {
					var status = data.code;
					if (status == 1) {
						$("#sendMessageDIV").css("display","none");
						$("input[name=commentsMsgId]").val(0);
						$("input[name=msgContent]").val("");
						var comments = JSON.parse(data.comments);
						console.log(comments);
						var HTMLTXT ="<div class=\"pinlun\">";
							HTMLTXT +="		<span class=\"plname\">"+comments.commentsName+":</span>"+comments.comments;
							HTMLTXT +="	</div>";
						$("#commentsNumber_"+msgId).text(parseInt($("#commentsNumber_"+msgId).text())+1);
						$("#commentsList_"+msgId).append(HTMLTXT);
					} else {
						alert(data.message);
					}
				}
			});
		} else {
			alert(tablevalue);
		}
	}
	//选择评论话题
	function commentsList(msgId){
		$("#sendMessageDIV").css("display","block");
		$("input[name=commentsMsgId]").val(msgId);
	}
	//获取话题评论
	function selectcommentsList(roomId){
		$.ajax({
			type : "POST",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/comments/selectList.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				roomId : roomId
			},
			success : function(data) {
				var status = data.code;
				if (status == 1) {
					var iLiveCommentsMap = JSON.parse(data.iLiveCommentsMap);
					for (var i = 0; i < iLiveCommentsMap.length; i++) {
						var vo = iLiveCommentsMap[i];
						var list = vo.list;
						var HTMLTXT ="";
						for (var j = 0; j < list.length; j++) {
							var comments = list[j];
							HTMLTXT +="<div class=\"pinlun\">";
							HTMLTXT +="		<span class=\"plname\">"+comments.commentsName+":</span>"+comments.comments;
							HTMLTXT +="	</div>";
						}
						$("#commentsNumber_"+vo.msgId).text(list.length);
						$("#commentsList_"+vo.msgId).html(HTMLTXT);
					}
					var praiseMap = JSON.parse(data.praiseMap);
					console.log(praiseMap);
					var userId = $("input[name=userId]").val();
					for(var key in praiseMap)  {
						var  praiseNumber = praiseMap[key].length;
						if(praiseNumber==undefined){
							praiseNumber=0;
						}
						var ret = 0;
						for (var i = 0; i < praiseNumber; i++) {
							var praise =  praiseMap[key][i];
							var id = praise.userId;
							if(parseInt(userId)==parseInt(id)){
								ret=1;
							}
						}
						if(ret==0){
							var HTML = "<i class=\"fa fa-thumbs-o-up\" onclick=\"saveILiveMessagePraise("+key+")\"></i>";
							HTML += "<span id=\"praiseNumber_"+key+"\">"+praiseNumber+"</span>";
							$("#messagePraise_"+key).html(HTML);
						} else{
							var HTML = "<i class=\"fa fa-thumbs-up\"></i>";
							HTML += "<span id=\"praiseNumber_"+key+"\">"+praiseNumber+"</span>";
							$("#messagePraise_"+key).html(HTML);
						}
					} 
				} else {
					alert(data.message);
				}
			}
		});
	}	
$(function(){
	var roomId=0;
	var param = window.location.search;
	var newsidinfo = param.substr(param.indexOf("?") + 1, param.length)//取出参数字符串 这里会获得类似“id=1”这样的字符串
	var split = newsidinfo.split("&")
	for (var i = 0; i < split.length; i++) {
		var newsids = split[i].split("=");//对获得的参数字符串按照“=”进行分割
		var newsid = newsids[1];//得到参数值
		var newsname = newsids[0];
		if (newsname == "roomId") {
			$("input[name=roomId]").val(newsid);
			roomId = newsid;
		}else if(newsname == "userId"){
			$("input[name=userId]").val(newsid);
		}
	}
	if(roomId==0){
		alert("直播间错误");
	}else{
		$.ajax({
			type : "GET",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/selectPageDecorate.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				roomId  : roomId
			},
			success : function(data) {
				var code = data.code;
				if(code==1){
					var menuBoxDIV = "";
					var contentBoxDIV = "";
					var pageDecorates = JSON.parse(data.pageDecorates);
					for (var i = 0; i < pageDecorates.length; i++) {
						var pageDecorate = pageDecorates[i];
						var liveMsgType = pageDecorate.menuType;
						if(liveMsgType==4){
							menuBoxDIV +=" <li ";
							if(i==0){
								menuBoxDIV +="class=\"liactive\"";
								$(".wen").attr("msgValue",liveMsgType);
								if(liveMsgType==2){
									$("#sendMessageDIV").css("display","block");
									$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
								}else{
									$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
									$("#sendMessageDIV").css("display","none");
								}
							}
							menuBoxDIV +=" liveMsgType=\""+liveMsgType+"\">直播简介</li>";
							contentBoxDIV+="<li class=\"zbjj\" id=\"enterpriseList\"></li>";
						}else if(liveMsgType==5){
							menuBoxDIV+=" <li ";
							if(i==0){
								menuBoxDIV +="class=\"liactive\"";
								$(".wen").attr("msgValue",liveMsgType);
								$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
								if(liveMsgType==2){
									$("#sendMessageDIV").css("display","block");
								}else{
									$("#sendMessageDIV").css("display","none");
								}
							}
							menuBoxDIV +=" liveMsgType=\""+liveMsgType+"\">回看</li>";
							contentBoxDIV +="<li class=\"review\" id=\"recallingDiv\"></li>";
						}else if(liveMsgType==6){
							menuBoxDIV+=" <li ";
							if(i==0){
								menuBoxDIV +="class=\"liactive\"";
								$(".wen").attr("msgValue",liveMsgType);
								$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
								if(liveMsgType==2){
									$("#sendMessageDIV").css("display","block");
								}else{
									$("#sendMessageDIV").css("display","none");
								}
							}
							menuBoxDIV +=" liveMsgType=\""+liveMsgType+"\">文档</li>";
							contentBoxDIV +="<li class=\"wendang\">";
							contentBoxDIV +="	<div class=\"wendangBox\">";
							contentBoxDIV +="		<img src=\"img/111.jpg\" alt=\"\" />";
							contentBoxDIV +="	</div>";
							contentBoxDIV +="	<div class=\"wendangBottom\">";
							contentBoxDIV +="		<div class=\"pptImg\">";
							contentBoxDIV +="			<img src=\"img/ppt.png\" />";
							contentBoxDIV +="		</div>";
							contentBoxDIV +="		<div class=\"pptCon\">";
							contentBoxDIV +="			<p class=\"pptName\">工业互联网峰会PPT</p>";
							contentBoxDIV +="			<p class=\"pptSize\">100M</p>";
							contentBoxDIV +="		</div>";
							contentBoxDIV +="		<div class=\"pptBtn\">";
							contentBoxDIV +="			<button><i class=\"fa fa-eye\"></i>预览</button>";
							contentBoxDIV +="		</div>";
							contentBoxDIV +="	</div>";
							contentBoxDIV +="</li>";
						}else if(liveMsgType==1){
							menuBoxDIV+=" <li ";
							if(i==0){
								menuBoxDIV +="class=\"liactive\"";
								$(".wen").attr("msgValue",liveMsgType);
								$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
								if(liveMsgType==2){
									$("#sendMessageDIV").css("display","block");
								}else{
									$("#sendMessageDIV").css("display","none");
								}
							}
							menuBoxDIV +=" liveMsgType=\""+liveMsgType+"\">话题</li>";
							contentBoxDIV +="<li class=\"theme\">";
							contentBoxDIV +="	<div class=\"themeCon\" id=\"themeDiv\"></div>";
							contentBoxDIV +="</li>";
						}else if(liveMsgType==2){
							menuBoxDIV+=" <li ";
							if(i==0){
								menuBoxDIV +="class=\"liactive\"";
								$(".wen").attr("msgValue",liveMsgType);
								$(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
								if(liveMsgType==2){
									$("#sendMessageDIV").css("display","block");
								}else{
									$("#sendMessageDIV").css("display","none");
								}
							}
							menuBoxDIV +=" liveMsgType=\""+liveMsgType+"\">聊天</li>";
							contentBoxDIV +="<li class=\"talkLi\">";
							contentBoxDIV +="	<div id=\"mescroll\" class=\"mescroll\">";
							contentBoxDIV +="		<div class=\"talkBox\">";
							contentBoxDIV +="			<div id=\"topTalkBoxList2\"></div>";
							contentBoxDIV +="			<div id=\"talkBoxList2\"></div>";
							contentBoxDIV +="		</div>";
							contentBoxDIV +="	</div>";
							contentBoxDIV +="</li>";
						}
					}
					$("#menuBoxDIV").append(menuBoxDIV);
					$("#contentBoxDIV").prepend(contentBoxDIV);
					var tableNumber = pageDecorates.length;
					var getBox = function(className){
					    return document.querySelector("." + className)
					}
					var tabNumber = tableNumber*100;
					var minwidth = tabNumber-200;
					$(".contentBox ul").css("width",tabNumber+"%");
					var fistBoxController = (function(){
					    var firstBox = getBox("firstBox");
					    var liList = $(".menuBox li");
					    var ul = document.querySelector(".contentBox ul");
					    var controller = {
					        muneController : function(){
					            for(var i = 0; i < liList.length; i++){				
					                liList[i].addEventListener("touchstart",function(event,n){
					                    return function(){
					                        for(var j = 0; j < liList.length; j++){
					                            liList[j].classList.remove("liactive");
					                        }
					                        this.classList.add("liactive");
					                        var liveMsgType = $(".liactive").attr("liveMsgType");
					                        if(liveMsgType==2){
												$("#sendMessageDIV").css("display","block");
											}else{
												$("#sendMessageDIV").css("display","none");
											}
							                $(".wen").attr("msgValue",liveMsgType);
							                $(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
					                        ul.style.left = "-" + n * 100 + "%";
					                    }
					                }(event,i))
					            }
					        }(),
					        contentCtroller : function(){
					            var contentBox = document.querySelector(".contentBox");
					            ul.style.left = "0%";
					            var startX,disX;
					            contentBox.addEventListener("touchstart",function(event){         
									var touche = event.touches[0];
					                startX = touche.clientX;
					            })
					            contentBox.addEventListener("touchend",function(event){
					                var touche = event.changedTouches[0];
					                disX = touche.clientX - startX;
					                var leftNum = parseInt(ul.style.left);				
					                if(disX > 0 && disX > 100){					
					                        if(leftNum <= -100){
					                            ul.style.left = leftNum + 100 + "%";
					                            var left = (~ parseInt(ul.style.left) + 1) / 100;
					                            menuController(left);
					                        }
					                }else if(disX < 0 && disX < -100){					
					                        if(leftNum >= -minwidth){
					                            ul.style.left = leftNum - 100 + "%";
					                            var left = (~ parseInt(ul.style.left) + 1) / 100;
					                            menuController(left);		
					                        }
					                }
					            })
					            contentBox.addEventListener("touchmove",function(event){
					                event.stopPropagation()
					            });

					            function menuController(order){
					                for(var i = 0; i < liList.length; i++){
					                    liList[i].classList.remove("liactive");
					                }
					                liList[order].classList.add("liactive");
					                var liveMsgType = $(".liactive").attr("liveMsgType");
					                $(".wen").attr("msgValue",liveMsgType);
					                $(".wen").css({background : "url(\"/phone/img/wen.png\") no-repeat",backgroundSize : "100%"});
					                if(liveMsgType==2){
										$("#sendMessageDIV").css("display","block");
									}else{
										$("#sendMessageDIV").css("display","none");
									}
					            }
					        }()
					    }


					}())
					var winHeight = $(window).height();
					var VideoBox = $(".videoBox").height();
					var menuBox = $(".menuBox").height();
					var talkCon = $(".talkCon").height();
					$(".contentBox").css("height", winHeight - VideoBox - menuBox - 2);
					$(".contentBox ul li").css("height", winHeight - VideoBox - menuBox);
					$(".talkLi").css("height", winHeight - VideoBox - menuBox - talkCon);
					//图片点击放大
					$(".imgBox").lightGallery({
						loop : false,
						auto : false,
						closable : true
					});
					//评论框获取焦点  展示发送
					/*$(".talkCon #saytext").focus(function() {
						$(".qiandao").addClass("hide");
						$(".liwu").addClass("hide");
						$(".sendMessage").removeClass("hide");
					});
					$(".talkCon #saytext").blur(function() {
						$(".qiandao").removeClass("hide");
						$(".liwu").removeClass("hide");
						$(".sendMessage").addClass("hide");
						if ($(this).val().length > 0) {
							$(".qiandao").addClass("hide");
							$(".liwu").addClass("hide");
							$(".sendMessage").removeClass("hide");
						}
					});*/
					var roomId = $("input[name=roomId]").val();
					if (roomId == 0) {
						alert("直播间不存在");
					}else{
						$.ajax({
							type : "GET",//请求方式 get/post
							url : "http://" + tomcatBase + "/ilive/app/room/checkRoomLogin.jspx",
							dataType : "jsonp",
							jsonp : "callback",
							cache : false,
							data : {
								roomId  : roomId
							},
							success : function(data) {
								var status = data.code;
								if (status == 1) {
									var roomNeedLogin = JSON.parse(data.data).roomNeedLogin;
									if(roomNeedLogin==1){
										window.location.href="http://"+h5Base+"/phone/login.html?roomId="+roomId;
										return;
									}else if(roomNeedLogin==2){
										var userInfo = JSON.parse(data.data).userInfo;
										if(userInfo!=undefined||userInfo!=null){
											var userId = userInfo.userId;
											if(userId!=undefined||userId!=null){
												$("input[name=userId]").val(userId);
											}
										}
										$.ajax({
											type : "GET",//请求方式 get/post
											url : "http://" + tomcatBase + "/ilive/app/room/getRoomInfo.jspx",
											dataType : "jsonp",
											jsonp : "callback",
											cache : false,
											data : {
												roomId : roomId
											},
											success : function(data) {
												var status = data.code;
												if(status==1){
													var liveEvent = data.data.liveEvent;
													console.log(liveEvent);
													if(liveEvent==null||liveEvent==undefined){
														alert("直播间信息错误！");
														return;
													}
													
													var liveStatus = liveEvent.liveStatus;
													var playBgAddr = liveEvent.playBgAddr;
													console.log(playBgAddr);
													if(liveStatus==0){
														//开始直播
														var openCountdownSwitch = liveEvent.openCountdownSwitch;
														if(openCountdownSwitch==1){
															var liveStartTime = liveEvent.liveStartTime;
															var tempStrs = liveStartTime.split(" ");  //截取时间
															// 解析日期部分
															var dateStrs = tempStrs[0].split("-");
															var start_year = parseInt(dateStrs[0], 10);
															var start_month = parseInt(dateStrs[1], 10) - 1;
															var start_day = parseInt(dateStrs[2], 10);
															// 解析时间部分
															var timeStrs = tempStrs[1].split(":");
															var start_hour = parseInt(timeStrs [0], 10);
															var start_minute = parseInt(timeStrs[1], 10);
															var start_second = parseInt(timeStrs[2], 10);
															var date = new Date(start_year, start_month, start_day, start_hour, start_minute, start_second);
															var newDate = new Date();   // 获取当前时间
															var dateTime =  date.getTime() - newDate.getTime();   //时间差的毫秒数      
															//计算出相差天数  
														    var days=Math.floor(dateTime/(24*3600*1000))  
														    //计算出小时数  
															var leave1=dateTime%(24*3600*1000);   //计算天数后剩余的毫秒数  
														    var hours=Math.floor(leave1/(3600*1000)) ; 
														    //计算相差分钟数  
														    var leave2=leave1%(3600*1000);      //计算小时数后剩余的毫秒数  
														    var minutes=Math.floor(leave2/(60*1000));  
														    //计算相差秒数  
														    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数  
														    var seconds=Math.round(leave3/1000); 
														    var dataTime = days +" 天 "+hours+" 小时 "+minutes+" 分钟 "+seconds+" 秒";
														    console.log(dataTime);
															$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播人员在赶来的路上...<br><span id=\"openCountdownSwitch\"  style=\"color: red;\">"+dataTime+"</span></div>");
															//倒计时事件
														    selectTimer(liveStartTime);
														}else{
															$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播人员在赶来的路上...</div>");
														}
													}else if(liveStatus==1){
														//直播中
														$("#videoImg").html("<video src=\"oceans.mp4\" controls playsinline  webkit-playsinline playsinline x5-playsinline x-webkit-airplay=\"allow\"></video>");
													}else if(liveStatus==2){
														//暂停
														$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播人员休息中...</div>");
													}else if(liveStatus==3){
														//直播结束
														$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播结束...</div>");
													}else{
														$("#videoImg").html("<img src=\""+playBgAddr+"\" alt=\"\"/><div class=\"textMask\">直播处理中...</div>");
													}
													
													var pageDecorates = liveEvent.pageDecorate;
													if(pageDecorates==null||pageDecorates==undefined){
														alert("直播间信息错误！");
														return;
													}
													var zbjj = 0;
													var zbhk = 0;
													var htzb = 0;
													for (var i = 0; i < pageDecorates.length; i++) {
														var pageDecorate = pageDecorates[i];
														if(pageDecorate.menuType==4){
															zbjj=1;
														}else if(pageDecorate.menuType==5){
															zbhk=1;
														}else if(pageDecorate.menuType==1){
															htzb=1;
														}
													}
													if(zbjj!=0){
														var enterprise = data.data.enterprise;
														if(enterprise!=null||enterprise!=undefined){
															var enterpriseId = enterprise.enterpriseId;
															var enterpriseImg = enterprise.enterpriseImg;
															var enterpriseName = enterprise.enterpriseName;
															var enterpriseDesc = enterprise.enterpriseDesc;
															var concernStatus = enterprise.concernStatus;
															var enterpriseList = "<div class=\"zbjjHeader\" id=\"enterpriseId_"+enterpriseId+"\">";
																enterpriseList += "		<div class=\"userImg\"><img src=\""+enterpriseImg+"\" /></div>";
																enterpriseList +="		<div class=\"qiyeName\">";
																enterpriseList +="			<p class=\"allName\">"+enterpriseName+"</p>";
																enterpriseList +="			<p class=\"presonName\">"+enterpriseDesc+"</p>";
																enterpriseList +="		</div>";
																if(concernStatus==0){
																	enterpriseList +="		<div class=\"followBox\" id=\"concernStatus\" onclick=\"updateConcernStatus("+enterpriseId+",0)\">关注企业</div>";
																}else{
																	enterpriseList +="		<div class=\"followBox\" id=\"concernStatus\" onclick=\"updateConcernStatus("+enterpriseId+",1)\">取消关注</div>";
																}
																
																enterpriseList +="</div>";
																enterpriseList +="<div id=\"liveDesc_enterpriseId\"></div>";
															$("#enterpriseList").html(enterpriseList);
														}else{
															var enterpriseList ="<div id=\"liveDesc_enterpriseId\"></div>";
															$("#enterpriseList").html(enterpriseList);
														}
														var liveDesc = liveEvent.liveDesc;
														$("#liveDesc_enterpriseId").html(liveDesc);
													}
													var liveEventId = liveEvent.liveEventId;
													$("input[name=liveEventId]").val(liveEventId);
													var openSignupSwitch = liveEvent.openSignupSwitch;
													var estoppelType = liveEvent.estoppelType;
													if(estoppelType==0){
														//是否全局禁言 0 禁言开启 1 关闭禁言
														$("input[name=msgContent]").attr("disabled","disabled");
													}else{
														$("input[name=msgContent]").removeAttr("disabled");
													}
													if(openSignupSwitch==1){
														var diyformAddr = liveEvent.diyformAddr;
														window.location.href="http://"+h5Base+"/phone/form.html?roomId="+roomId;
														return;
													}
													var viewAuthorized = liveEvent.viewAuthorized;
													if(viewAuthorized==2){
														window.location.href="http://"+h5Base+"/phone/password.html?roomId="+roomId;
														return;
													}else{
														selectHTML(roomId);
														if(htzb!=0){
															window.setTimeout("selectcommentsList("+roomId+")",5000);//页面加载5秒执行一次
															window.setInterval("selectcommentsList("+roomId+")",30000);//循环执行每一分钟执行一次
														}
													}
													if(zbhk!=0){
														$.ajax({
															type : "GET",//请求方式 get/post
															url : "http://" + tomcatBase + "/ilive/app/room/getrecordlist.jspx",
															dataType : "jsonp",
															jsonp : "callback",
															cache : false,
															data : {
																roomId  : roomId
															},
															success : function(data) {
																if(data.code==1){
																	var list = data.data;
																	var recallingDiv = "";
																	for (var i = 0; i < list.length; i++) {
																		var mediaFile = list[i];
																		recallingDiv += "<div class=\"reviewList\" id=\"fileId_"+mediaFile.fileId+"\" onclick=\"selectMediaFile("+mediaFile.fileId+")\" >";
																		recallingDiv +="	<div class=\"reviewLeft\">";
																		recallingDiv +="		<img src=\""+mediaFile.fileImg+"\" >";
																		recallingDiv +="		<div class=\"reviewMask\">";
																		recallingDiv +="			<span><i class=\"fa fa-play\"></i>"+mediaFile.playNum+"</span>";
																		recallingDiv +="			<span><i class=\"fa fa-commenting\"></i>"+mediaFile.commentsNum+"</span> <span class=\"maskTime\">"+mediaFile.fileDuration+"</span>";
																		recallingDiv +="		</div></div>";
																		recallingDiv +="	<div class=\"reviewRight\">";
																		recallingDiv +="		<div class=\"reviewRightTop\">"+mediaFile.fileName+"</div>";
																		recallingDiv +="		<div class=\"reviewRightBottom\">"+mediaFile.createTime+"</div>";
																		recallingDiv +="	</div>";
																		recallingDiv +="</div>";
																	}
																	$("#recallingDiv").prepend(recallingDiv);	
																}else{
																	alert(data.message);
																}
															}
														});
													}
												} else {
													alert(data.message);
												}
											}
										});
									}
								} else {
									alert(data.message);
								}
							}
						});	
					}
				}
			}
		});
	}
});	
