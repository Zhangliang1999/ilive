//提交绑定手机账户
function savePhone(obj) {
    var parent = $(obj).parent()
    var phoneNumber = parent.find($("input[name=phoneNumber]")).val();
    var vpassword = parent.find($("input[name=passwordCode]")).val();
    var roomId = $("input[name=roomId]").val();
    var userId = $("input[name=userId]").val();
    if (!isPoneAvailable(phoneNumber.trim())) {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "请输入正确的手机号"
        // });
        layerAlert('请输入正确的手机号')
        return false
    }
    if (vpassword == null || vpassword == undefined) {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "提示",
        //     Content: "验证码不能为空"
        // });
        layerAlert('验证码不能为空')
        return false;
    }

    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/app/personal/bindMobile.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "pc",
            userId: userId,
            phoneNum: phoneNumber,
            vpassword: vpassword
        },
        success: function (data) {
            if (data.code == 1) {
                window.location.href = "http://" + h5Base + "/pc/play.html?roomId=" + roomId + "&userId=" + userId;
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            }
        }
    });
}

//获取验证码
function getAuthenticode(obj) {
    var phoneNumber = $(obj).parent().prev().find($("input")).val();
    var type = $(obj).data('type') == 'reg' ? 'reg' : 'bindMobile'
    if (!isPoneAvailable(phoneNumber.trim())) {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "请输入正确的手机号"
        // });
        layerAlert('请输入正确的手机号')
        return false
    }

    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/appuser/sendmsg.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            phoneNum: phoneNumber,
            type: type,
            terminalType: "pc"
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                selectTimers(obj);
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            }
        }
    });
}

var countdown = 60;

function selectTimers(obj) {
    if (countdown == 0) {
        $(obj).removeAttr("disabled");
        $(obj).text("获取验证码").attr("onclick", "getAuthenticode(this)");
        countdown = 60;
    } else {
        $(obj).attr("disabled", true);
        $(obj).text("重新发送(" + countdown + "s)").removeAttr("onclick");
        countdown--;
        setTimeout(function () {
            selectTimers(obj)
        }, 1000);
    }
}

function questionSpan() {
    var liveMsgType = $("input[name=liveMsgType]").val();
    if (liveMsgType == 2) {
        $("input[name=liveMsgType]").val(3);
        $("#questionSpan").addClass("questionActive");
        $("input[name=content]").attr("maxlength", "100");
    } else {
        $("input[name=liveMsgType]").val(2);
        $("#questionSpan").removeClass("questionActive");
        $("input[name=content]").attr("maxlength", "40");
    }
}

function selectLiveMsgType(liveMsgType) {
    $("#questionSpan").removeClass("questionActive");
    if (liveMsgType == 2) {
        $("input[name=liveMsgType]").val(2);
        $("#questionSpan").attr("onclick", "questionSpan()");
        $("input[name=content]").removeAttr("readonly");
        $(".sendBtn").attr("onclick", "sendMessageBtn()");
    } else if (liveMsgType == 1) {
        $("input[name=liveMsgType]").val(1);
        $("input[name=content]").attr("readonly", "readonly");
        $("#questionSpan").removeAttr("onclick");
        $(".sendBtn").removeAttr("onclick");
        selectMessageLIVE();
    }
}

//点赞
function saveDianZan() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/register/registerRoom.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "pc",
            roomId: roomId
        },
        success: function (data) {
            if (data.code == 401) {
                window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            } else if (data.code == 1) {
                //selectDianZan();
                $("#dianZanSpan").removeAttr("onclick").addClass('saveDianZan');
            } else {
                $("#dianZanSpan").removeAttr("onclick").addClass('saveDianZan');
                /* $.DialogBySHF.Alert({
                  Width: 350,
                  Height: 200,
                  Title: "提示",
                  Content: data.message
             }); */
            }
        }
    });
}
//问卷
function showWenjuan() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var id = $("input[name=userId]").val();
    var QuestionnaireId=42;
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/questionnaire/startQuestionnaire.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "pc",
            userId: id,
            roomId:roomId
        },
        success: function (data) {
        	if(data.code==401){
        		 window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        	}else if (data.code == 1) {
                //selectDianZan();
        		 window.location.href = "http://" + h5Base + "/pc/showQuestionnaire.html?roomId=" + roomId;
            } else {
//                $("#dianZanSpan").removeAttr("onclick").addClass('saveDianZan');
                 $.DialogBySHF.Alert({
                  Width: 350,
                  Height: 200,
                  Title: "提示",
                  Content: data.message
             }); 
            }
        }
    });
}
//查询点赞结果
function selectDianZan() {

    var roomId = $("input[name=roomId]").val();//直播间ID
    var id = $("input[name=userId]").val();//用户ID
    var liveEventId = $("input[name=liveEventId]").val();//用户ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/register/queryIsRegister.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            liveEventId: liveEventId,
            userId: id,
        },
        success: function (data) {
            var code = data.code;
            if (code == 1) {
                //var isQiandaoReg = data.data.isQiandaoReg;//签到
                var isDianzanReg = data.data.isDianzanReg;//点赞
                if (isDianzanReg) {
                    $("#dianZanSpan").removeAttr("onclick").addClass('saveDianZan');
                }
            }
        }
    });
}

//获取直播间信息
function selectRoomLogo() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/selectRoom.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            roomId: roomId
        },
        success: function (data) {
            if (data.code == 1) {

                var iLiveLiveRoom = JSON.parse(data.iLiveLiveRoom);
                var liveEvent = iLiveLiveRoom.liveEvent;
                var liveTitle = liveEvent.liveTitle;
                var live_bg = iLiveLiveRoom.bgdImg
                var live_logo = iLiveLiveRoom.logoImg
                if (live_logo) {
                    $('.z-logo img').attr('src', live_logo)
                }
                if (live_bg) {
                    $('.header').css('background-image', 'url(' + live_bg + ')')
                }
                // console.log(live_bg)
                if (liveTitle == undefined) {
                    liveTitle = "天翼直播平台";
                }
                $("title").text(liveTitle);
                $("#roomName").text(liveTitle);
                // var openLogoSwitch = liveEvent.openLogoSwitch;//logo显示开关 0为关闭 1为开启
                // if (openLogoSwitch == 1) {
                //     var logoUrl = liveEvent.logoUrl;// 直播场次LOGO
                //     var logoPosition = liveEvent.logoPosition;//LOGO位置 1:左上角 2:右上角 3:左下角 4:右下角
                //     if (logoPosition == 1) {
                //         $(".pcLogo").css("display", "block");
                //         $(".pcLogo").addClass("logoleft");
                //         $(".pcLogo").removeClass("logoright");
                //         $(".pcLogo").removeClass("logobottomLeft");
                //         $(".pcLogo").removeClass("logobottomRight");
                //         $(".pcLogo").html("<img src=\"" + logoUrl + "\">");
                //     } else if (logoPosition == 2) {
                //         $(".pcLogo").css("display", "block");
                //         $(".pcLogo").removeClass("logoleft");
                //         $(".pcLogo").addClass("logoright");
                //         $(".pcLogo").removeClass("logobottomLeft");
                //         $(".pcLogo").removeClass("logobottomRight");
                //         $(".pcLogo").html("<img src=\"" + logoUrl + "\">");
                //     } else if (logoPosition == 3) {
                //         $(".pcLogo").css("display", "block");
                //         $(".pcLogo").removeClass("logoleft");
                //         $(".pcLogo").removeClass("logoright");
                //         $(".pcLogo").addClass("logobottomLeft");
                //         $(".pcLogo").removeClass("logobottomRight");
                //         $(".pcLogo").html("<img src=\"" + logoUrl + "\">");
                //     } else if (logoPosition == 4) {
                //         $(".pcLogo").css("display", "block");
                //         $(".pcLogo").removeClass("logoleft");
                //         $(".pcLogo").removeClass("logoright");
                //         $(".pcLogo").removeClass("logobottomLeft");
                //         $(".pcLogo").addClass("logobottomRight");
                //         $(".pcLogo").html("<img src=\"" + logoUrl + "\">");
                //     } else {
                //         $(".pcLogo").removeClass("logoleft");
                //         $(".pcLogo").removeClass("logoright");
                //         $(".pcLogo").removeClass("logobottomLeft");
                //         $(".pcLogo").removeClass("logobottomRight");
                //         $(".pcLogo").css("display", "none");
                //         $(".pcLogo").html("");
                //     }
                // } else {
                //     //关闭
                //     $(".pcLogo").removeClass("logoleft");
                //     $(".pcLogo").removeClass("logoright");
                //     $(".pcLogo").removeClass("logobottomLeft");
                //     $(".pcLogo").removeClass("logobottomRight");
                //     $(".pcLogo").css("display", "none");
                //     $(".pcLogo").html("");
                // }
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: "获取直播间信息失败"
                // });
                layerAlert('获取直播间信息失败')
            }
        }
    });
}

//预约和取消预约
function saveAppointment() {
    var userId = $("input[name=userId]").val();//用户ID
    var roomId = $("input[name=roomId]").val();//直播间ID
    if (userId == 0) {
        window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
    } else {
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/homepage/saveAppointment.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                userId: userId,
                roomId: roomId
            },
            success: function (data) {
                //401 手机登录
                //402     绑定手机号
                //403    微信打开
                if (data.code == 401) {
                    window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
                } else if (data.code == 402) {
                    saveMessagerPhoneNumber();
                } else if (data.code == 403) {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                } else if (data.code == 1) {
                    if (data.data.userid != undefined) {
                        var status = data.status
                        if (status == 1) {
                            //已预约
                            $('#uploadModal').modal();
                            selectAppointment();
                            /* $.DialogBySHF.Confirm({
                              Width: 350,
                              Height: 200,
                              Title: "预约提示",
                              Content: data.message,
                              ConfirmFun: openConfirmApp,
                              CancelFun: temporaryCancelApp
                         });
                        $("#btnDialogBySHFConfirm").val("现在打开");
                        $("#btnDialogBySHFCancel").val("暂时不用"); */
                        } else if (status == 2) {
                            selectAppointment();
                            // $.DialogBySHF.Alert({
                            //     Width: 350,
                            //     Height: 200,
                            //     Title: "预约提示",
                            //     Content: data.message,
                            // });
                            layerAlert(data.message)
                        }
                    } else {
                        //未预约
                        $(".appointment").text("我要预约");
                        // $.DialogBySHF.Alert({
                        //     Width: 350,
                        //     Height: 200,
                        //     Title: "预约提示",
                        //     Content: data.message
                        // });
                        layerAlert(data.message)
                    }
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: "预约失败"
                    // });
                    layerAlert('预约失败')
                }
            }
        });
    }
}

//现在打开
function openConfirmApp() {
    console.log("你确认了查看直播方更多直播内容");
    window.location.href = "http://apizb.tv189.com/version/wap/download/ios"
}

//暂时不用
function temporaryCancelApp() {
    console.log("你放弃查看直播方更多直播内容");
}

//绑定手机号
function saveMessagerPhoneNumber() {
    console.log("弹框进行手机号绑定");
    $('#myModal').modal();
}

//查看是否预约
function selectAppointment() {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId != 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/homepage/isAppointment.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                userId: userId,
                roomId: roomId
            },
            success: function (data) {
                if (data.code == 1) {
                    if (data.data.isAppointment == 1) {
                        //已预约
                        $("#saveAppointment").addClass("ordered");
                        $("#saveAppointment").text("已预约本场直播");
                    } else {
                        //未预约
                        $("#saveAppointment").removeClass("ordered");
                        $("#saveAppointment").text("预约本场直播");
                    }
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: "预约信息查询失败"
                    // });
                    layerAlert('预约信息查询失败')
                }
            }
        });
    }

}

var ws;

//数据接收
function selectHTML(roomId) {
    var interval = window.setInterval(function () { //每隔5秒钟发送一次心跳，避免websocket连接因超时而自动断开
        var ping = {"p": "1"};
        if (ws) {
            try {
                ws.send(JSON.stringify(ping));
                console.log("websocket 连接已关闭")
            } catch (error) {
                console.log("websocket 连接已关闭")
            }
        }
    }, 10000);

    //用户登录
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/app/room/roomenter.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId,
            sessionType: 1,//WebSocket使用用户
            webType: 2
        },
        success: function (data) {
            if (data.code == 404) {
                window.location.href = "http://" + h5Base + "/pc/end.html";
            } else if (data.code == 402) {
                var userId = $("input[name=userId]").val();
                if (userId != 0) {
                    window.location.href = "http://" + h5Base + "/pc/phoneNumber.html?roomId=" + roomId + "&userId=" + userId;
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: "绑定手机号，获取用户失败",
                    //     ConfirmFun: errorfunction
                    // });
                    layerAlert('绑定手机号，获取用户失败')
                }
            } else if (data.code == 1) {
                var userId = $("input[name=userId]").val();//用户ID
                if (userId != 0) {
                    selectDianZan();
                }
                var hlsAddr = data.hlsAddr;
                var rtmpAddr = data.rtmpAddr;
                videoDiv(rtmpAddr);
                var opType = 10;
                var estoppelType = data.estoppelType;
                if (estoppelType == 0) {
                    //是否全局禁言 0 禁言开启 1 关闭禁言
                    $("input[name=content]").attr("disabled", "disabled");
                    $(".sendBtn").removeAttr("onclick");
                } else {
                    var forbidTalk = data.forbidTalk;
                    console.log("禁言标示：" + forbidTalk);
                    if (forbidTalk == 1) {
                        opType = 11;
                        $("input[name=content]").attr("disabled", "disabled");
                        $(".sendBtn").removeAttr("onclick");
                    } else {
                        opType = 10;
                        $("input[name=content]").removeAttr("disabled");
                        $(".sendBtn").attr("onclick", "sendMessageBtn()");
                    }
                }
                if (typeof (WebSocket) == "undefined") {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: "你的浏览器不支持信息交流",
                    //     ConfirmFun: errorfunction
                    // });
                    layerAlert('你的浏览器不支持信息交流')
                    return;
                }
                var token = data.token;
                var url = "ws://" + tomcatBase + "/ilive/webSocketServer.jspx?username=" + token;
                ws = new WebSocket(url);
                ws.onopen = function (e) {
                    console.log("连接成功");
                }
                ws.onclose = function (e) {
                    console.log("关闭连接 ");
                }
                ws.onerror = function (e) {
                    console.log("建立连接异常" + e);
                }
                ws.onmessage = function (e) {
                    var iLiveMessage = JSON.parse(e.data);

                    switch (iLiveMessage.roomType) {
                        case 0:
                            var iLiveEventVo = iLiveMessage.iLiveEventVo;
                            estoppelType = iLiveEventVo.estoppleType;
                            if (estoppelType == 0) {
                                //是否全局禁言 0 禁言开启 1 关闭禁言
                                $("input[name=content]").attr("disabled", "disabled");
                                $(".sendBtn").removeAttr("onclick");
                            } else if (estoppelType == 1) {
                                if (opType == 11) {
                                    $("input[name=content]").attr("disabled", "disabled");
                                    $(".sendBtn").removeAttr("onclick");
                                } else {
                                    $("input[name=content]").removeAttr("disabled");
                                    $(".sendBtn").attr("onclick", "sendMessageBtn()");
                                }
                            }
                            /**
                             * 直播状态：
                             * 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
                             */
                            var liveStatus = iLiveEventVo.liveStatus;
                            var playBgAddr = iLiveEventVo.playBgAddr; //播放器封面
                            var converAddr = iLiveEventVo.converAddr //直播封面

                            switch (liveStatus) {
                                case 0:
                                    liveStart = 0;
                                    //开始直播
                                    $("#videoImage").css({
                                        'display': 'block',
                                        'background-image': 'url(' + converAddr + ')'
                                    })
                                    $("#flash").css("display", "none");
                                    $("#startLive").css("display", "block"); //未开始
                                    $("#stopLive").css("display", "none"); //暂停
                                    $("#endLive").css("display", "none"); // //结束
                                    $("#videoStartDiv").css("display", "block");
                                    break
                                case 1:
                                    //直播
                                    $("#flash").css("display", "block");
                                    $("#videoImage").css("display", "none");
                                    $("#videoStartDiv").css("display", "none");
                                    $("#startLive").css("display", "none");
                                    $("#stopLive").css("display", "none");
                                    $("#endLive").css("display", "none");
                                    $('.marqueeBox').css('bottom', '30px')
                                    break
                                case 2:
                                    liveStart = 0;
                                    //暂停
                                    $("#flash").css("display", "none");
                                    $("#videoImage").css({
                                        'display': 'block',
                                        'background-image': 'url(' + playBgAddr + ')'
                                    })
                                    $("#startLive").css("display", "none");
                                    $("#stopLive").css("display", "block");
                                    $("#endLive").css("display", "none");
                                    $("#videoStartDiv").css("display", "block");
                                    break
                                case 3:
                                    liveStart = 0;
                                    //直播结束
                                    $("#flash").css("display", "none");
                                    $("#videoImage").css({
                                        'display': 'block',
                                        'background-image': 'url(' + converAddr + ')'
                                    })
                                    $("#startLive").css("display", "none");
                                    $("#stopLive").css("display", "none");
                                    $("#endLive").css("display", "block");
                                    $("#videoStartDiv").css("display", "block");
                                    break
                                default:
                                    $("#videoImage").css({
                                        'display': 'block',
                                        'background-image': 'url(' + converAddr + ')'
                                    })
                            }
                            break

                        case 1:
                            if (iLiveMessage.deleted == 1) {
                                $("#msgId_" + iLiveMessage.msgId).remove();
                            } else {
                                //消息管理
                                var deleteAll = iLiveMessage.deleteAll;
                                if (deleteAll == 1) {
                                    $("#chatMessageDiv").children().remove();
                                    $("#topChatMessageDiv").children().remove();
                                } else {
                                    var userId = $("input[name=userId]").val();
                                    var liveMsgType = iLiveMessage.liveMsgType;
                                    var update = iLiveMessage.update;
                                    if (iLiveMessage.senderId == userId) {
                                        if (estoppelType == 1) {
                                            opType = iLiveMessage.opType;
                                            if (opType == 11) {
                                                $("input[name=content]").attr("disabled", "disabled");
                                                $(".sendBtn").removeAttr("onclick");
                                            } else {
                                                $("input[name=content]").removeAttr("disabled");
                                                $(".sendBtn").attr("onclick", "sendMessageBtn()");
                                            }
                                        } else {
                                            opType = iLiveMessage.opType;
                                        }
                                    }
                                    if (liveMsgType == 2) {
                                        if (iLiveMessage.checked == 1) {
                                            $("#msgId_" + iLiveMessage.msgId).remove();
                                            if (iLiveMessage.top == 0) {
                                                var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                HTMLTXT += "		<p class=\"sofa\">";
                                                HTMLTXT += "			<i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                                                HTMLTXT += "		</p>";
                                                HTMLTXT += "</div>";
                                                //新增聊天互动
                                                $("#topChatMessageDiv #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                $("#chatMessageDiv").prepend(HTMLTXT);
                                            } else {
                                                var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                HTMLTXT += "		<p class=\"sofa\">";
                                                HTMLTXT += "			<span class=\"spanTop\">顶</span><i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                                                HTMLTXT += "		</p>";
                                                HTMLTXT += "</div>";
                                                //新增聊天互动
                                                $("#chatMessageDiv #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                $("#topChatMessageDiv").prepend(HTMLTXT);
                                            }
                                        } else {
                                            if (iLiveMessage.senderId == userId) {
                                                $("#msgId_" + iLiveMessage.msgId).remove();
                                                if (iLiveMessage.top == 0) {
                                                    var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                    HTMLTXT += "		<p class=\"sofa\">";
                                                    HTMLTXT += "			<i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                                                    HTMLTXT += "		</p>";
                                                    HTMLTXT += "</div>";
                                                    //新增聊天互动
                                                    $("#topChatMessageDiv #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                    $("#chatMessageDiv").prepend(HTMLTXT);
                                                } else {
                                                    var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                    HTMLTXT += "		<p class=\"sofa\">";
                                                    HTMLTXT += "			<span class=\"spanTop\">顶</span><i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                                                    HTMLTXT += "		</p>";
                                                    HTMLTXT += "</div>";
                                                    //新增聊天互动
                                                    $("#chatMessageDiv #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                    $("#topChatMessageDiv").prepend(HTMLTXT);
                                                }
                                            }
                                        }
                                    } else if (liveMsgType == 3) {
                                        if (iLiveMessage.replyType == 1) {
                                            var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                            HTMLTXT += "	<p class=\"quiz\"><span class=\"spanTop\">提问</span><i>" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent + "</p>";
                                            HTMLTXT += "	<p class=\"answer btext\"><span class=\"spanTop bluegb\">回答</span><i>" + iLiveMessage.replyName + "：</i>" + iLiveMessage.replyContent + "</p>";
                                            HTMLTXT += "</div>";
                                            $("#msgId_" + iLiveMessage.msgId).remove();
                                            $("#chatMessageDiv").prepend(HTMLTXT);
                                        } else {
                                            var senderId = iLiveMessage.senderId;
                                            var userId = $("input[name=userId]").val();
                                            if (senderId == 0 || senderId == null || senderId == undefined) {
                                            } else {
                                                if (senderId == userId) {
                                                    var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                    HTMLTXT += "	<p class=\"quiz\"><span class=\"spanTop\">提问</span><i>" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent + "</p>";
                                                    HTMLTXT += "</div>";
                                                    $("#msgId_" + iLiveMessage.msgId).remove();
                                                    $("#chatMessageDiv").prepend(HTMLTXT);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break

                        case 2:
                            console.log("发现新话题");
                            break

                        case 3:
                            window.location.href = "http://" + h5Base + "/pc/end.html";
                            break

                        case 4:
                            console.log("广告滚动");
                            selectAdvertising();
                            break

                        case 5:
                            console.log("抽奖");
                            selectActions()
                            break

                        case 6:
                            selectActions()
                            console.log("投票");
                            break
                        case 7:
                            console.log("赠送礼物");
                            break
                        case 8:
                            break
                        case 9:
                            selectActions()
                            console.log("打赏");
                            break
                        case 10:
                            console.log("欢迎语");
                            break
                        case 11:
                            var documentId = iLiveMessage.documentId;
                            var documentPageNO = iLiveMessage.documentPageNO;
                            var documentManual = iLiveMessage.documentManual;
                            var documentDownload = iLiveMessage.documentDownload;
                            selectDocument(documentId, documentDownload, documentPageNO, documentManual);
                            break
                        case 12:
                            break
                        case 13:
                            break
                        case 14:
                            break
                        case 15:
                            console.log("签到活动");
                            selectActions()
                            break
                        case 16:
                            console.log("红包");
                            selectActions()
                            break
                        case 18:
                            var iLiveEventVo = iLiveMessage.iLiveEventVo;
                            estoppelType = iLiveEventVo.estoppleType;
                            if (estoppelType == 0) {
                                //是否全局禁言 0 禁言开启 1 关闭禁言
                                $("input[name=content]").attr("disabled", "disabled");
                                $(".sendBtn").removeAttr("onclick");
                            } else if (estoppelType == 1) {
                                if (opType == 11) {
                                    $("input[name=content]").attr("disabled", "disabled");
                                    $(".sendBtn").removeAttr("onclick");
                                } else {
                                    $("input[name=content]").removeAttr("disabled");
                                    $(".sendBtn").attr("onclick", "sendMessageBtn()");
                                }
                            }
                            break
                        default:
                            console.log("操作类型" + iLiveMessage.roomType);
                    }


                    // if (iLiveMessage.roomType == 1) {
                    //
                    // } else if (iLiveMessage.roomType == 0) {
                    //
                    // } else if (iLiveMessage.roomType == 2) {
                    //
                    // } else if (iLiveMessage.roomType == 3) {
                    //
                    // } else if (iLiveMessage.roomType == 4) {
                    //
                    // } else if (iLiveMessage.roomType == 11) {
                    //
                    // } else {
                    //     console.log("操作类型:" + iLiveMessage.roomType);
                    // }
                }
            } else if (data.code == 2) {
                //鉴权失败
                window.location.href = "http://" + h5Base + "/pc/permissions.html";
            } else if (data.code == 3) {
                //直播间人数已满
                window.location.href = "http://" + h5Base + "/pc/standby.html?roomId=" + roomId;
            } else {
                window.location.href = "http://" + h5Base + "/pc/error.html";
            }
        }
    });
}

//鉴权失败
function errorfunctionAuthentication() {
    window.location.href = "http://" + h5Base + "/pc/permissions.html";
}

//直播间人数已满
function errorfunctionFull() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    window.location.href = "http://" + h5Base + "/pc/standby.html?roomId=" + roomId;
}

//获取话题评论
function selectcommentsList(roomId) {
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/comments/selectList.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                var iLiveCommentsMap = JSON.parse(data.iLiveCommentsMap);
                for (var i = 0; i < iLiveCommentsMap.length; i++) {
                    var vo = iLiveCommentsMap[i];
                    var list = vo.list;
                    var HTMLTXT = "";
                    for (var j = 0; j < list.length; j++) {
                        var comments = list[j];
                        HTMLTXT += "<p>";
                        HTMLTXT += "		<i class=\"btext\">" + comments.commentsName + ":</i>" + comments.comments;
                        HTMLTXT += "</p>";
                    }
                    $("#commentsNumber_" + vo.msgId).text(list.length);
                    $("#commentsList_" + vo.msgId).html(HTMLTXT);
                    $('.reply div p').eq(2).nextAll().css("display", "none");
                    if (list.length > 3) {
                        $("#commentsList_" + vo.msgId).append('<span class=\"openMore\">点击查看更多</span>')
                    }
                    $('.topoicBox').on('click', '.openMore', function () {
                        $(this).hide()
                        $(this).siblings().show()
                    })
                }
                var praiseMap = JSON.parse(data.praiseMap);
                var userId = $("input[name=userId]").val();
                for (var key in praiseMap) {
                    var praiseNumber = praiseMap[key].length;
                    if (praiseNumber == undefined) {
                        praiseNumber = 0;
                    }
                    var ret = 0;
                    for (var i = 0; i < praiseNumber; i++) {
                        var praise = praiseMap[key][i];
                        var id = praise.userId;
                        if (parseInt(userId) == parseInt(id)) {
                            ret = 1;
                        }
                    }
                    if (ret == 0) {
                        var HTML = "<i onclick=\"saveILiveMessagePraise(" + key + ")\"></i>";
                        HTML += "<span id=\"praiseNumber_" + key + "\">" + praiseNumber + "</span>";
                        $("#messagePraise_" + key).html(HTML);
                    } else {
                        var HTML = "<i style=\"background-position: -29px -13px;\"></i>";
                        HTML += "<span id=\"praiseNumber_" + key + "\">" + praiseNumber + "</span>";
                        $("#messagePraise_" + key).html(HTML);
                    }
                }
                /* var number = data.number;
                if(number==undefined){
                    number=0;
                }
                $("#roomNumber").text(number); */
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: "获取话题评论失败"
                // });
                layerAlert('获取话题评论失败')
            }
        }
    });
}

function checknumberlength(obj, number) {
    if (number < 9999) {
        $(obj).text(number);
    } else {
        number = (number / 10000).toFixed(1) / 1
        $(obj).text(number + 'w');
    }
}

//获取直播间人数
function selectRoomNumber() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/online/number.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                var number = JSON.parse(data.data).number;
                if (number == undefined || number == null) {
                    number = 0;
                }
                checknumberlength($("#roomNumber"), number)
            }
        }
    });
    setTimeout(function () {
        selectRoomNumber()
    }, 6000)
}

//消息发送
function sendMessageBtn() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        loginHtml()
        return;
    }
    var liveMsgType = $("input[name=liveMsgType]").val();//发送消息类型
    var liveEventId = $("input[name=liveEventId]").val();//场次ID
    var msgContent = $("input[name=content]").val();//发送消息内容
    var topicComments = $('.reply_textarea input').val();
    // if (msgContent.replace("\s+", "") == "" || msgContent == undefined ) {
    //     layerAlert('内容不能为空')
    //     return;
    // }

    if (liveMsgType == 2 || liveMsgType == 3) {
        if (msgContent.replace("\s+", "") == "" || msgContent == undefined) {
            layerAlert('内容不能为空')
            return;
        }
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/sendMessage.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "pc",
                userId: userId,
                roomId: roomId,
                liveMsgType: liveMsgType,
                content: encodeURI(msgContent),
                liveEventId: liveEventId,
                msgType: 1
            },
            success: function (data) {
                var status = data.code;
                if (data.code == 401) {
                    window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
                } else if (data.code == 402) {
                    saveMessagerPhoneNumber();
                } else if (data.code == 403) {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                } else if (status == 1) {

                    var iLiveMessage = data.iLiveMessage;

                    if (liveMsgType == 2) {
                        var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                        HTMLTXT += "		<p class=\"sofa\">";
                        HTMLTXT += "			<i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                        HTMLTXT += "		</p>";
                        HTMLTXT += "</div>";
                        //新增聊天互动
                        $("#chatMessageDiv").prepend(HTMLTXT);
                    } else if (liveMsgType == 3) {
                        //问答
                        var HTMLTXT = "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                        HTMLTXT += "	<p class=\"quiz\"><label>提问</label><i>" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent + "</p>";
                        //HTMLTXT += "	<p class=\"answer btext\"><label class=\"bluegb\">回答</label><i>"+iLiveMessage.replyName+"：</i>"+iLiveMessage.replyContent+"</p>";
                        HTMLTXT += "</div>";
                        $("#chatMessageDiv").prepend(HTMLTXT);
                    }
                    $("input[name=content]").val("");
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                }
            }
        });
    } else if (liveMsgType == 1) {
        if (topicComments.replace("\s+", "") == "" || topicComments == undefined) {
            layerAlert('内容不能为空')
            return;
        }
        msgId = $('.reply_textarea').parent().attr('id').split('_')[1]
        if (msgId == 0) {
            // $.DialogBySHF.Alert({
            //     Width: 350,
            //     Height: 200,
            //     Title: "提示",
            //     Content: "请选择要评论的话题"
            // });
            // layerAlert('请选择要评论的话题')
            console.log('未获取到相应话题ID')
            return;
        }
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/saveComments.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "pc",
                userId: userId,
                msgId: msgId,
                comments: encodeURI(topicComments)
            },
            success: function (data) {
                var status = data.code;
                if (data.code == 401) {
                    window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
                } else if (data.code == 402) {
                    saveMessagerPhoneNumber();
                } else if (data.code == 403) {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                } else if (status == 1) {
                    var commentsInfo = $.parseJSON(data.comments);
                    $("input[name=liveMsgType]").val(1);
                    // $("input[name=content]").attr("readonly", "readonly");
                    // $(".sendBtn").removeAttr("onclick");
                    $(".reply_textarea input").val("");
                    var HTMLTXT = "	<p>";
                    HTMLTXT += "	<i class=\"btext\">" + commentsInfo.commentsName + "：</i>" + commentsInfo.comments;
                    HTMLTXT += "		</p>";
                    $('#commentsList_' + msgId).prepend(HTMLTXT)
                    msgId = 0;
                    // selectMessageLIVE();
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                }
            }
        });
    } else {
        alert(tablevalue);
    }
}

var msgId = 0;

//评论
function saveComments(id) {
    msgId = id;
    $("input[name=content]").removeAttr("readonly");
    $(".sendBtn").attr("onclick", "sendMessageBtn()");
    $('.reply_textarea button').attr("onclick", "sendMessageBtn()")
    $("input[name=liveMsgType]").val(1);//发送消息类型
}

//点赞
function saveILiveMessagePraise(id) {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        loginHtml()
        return;
    }
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/praise/savePraise.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "pc",
            userId: userId,
            msgId: id
        },
        success: function (data) {
            var status = data.code;
            if (data.code == 401) {
                window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            } else if (status == 1) {
                var roomId = $("input[name=roomId]").val();//直播间ID
                selectcommentsList(roomId);
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            }
        }
    });
}

//获取话题
function selectMessageLIVE() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/message/selectMessageLIVE.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                var commentsAllow = data.commentsAllow
                var messageList = data.data;
                var topTopicDiv = "";
                var topicDiv = "";
                for (var i = 0; i < messageList.length; i++) {
                    var iLiveMessage = messageList[i];
                    if (iLiveMessage.top == 0) {
                        //未置顶
                        topicDiv += "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                        topicDiv += "		<p class=\"sofa\">";
                        topicDiv += "			<i class=\"btext\">" + iLiveMessage.senderName + "：</i>";
                        if (iLiveMessage.msgType == 2) {
                            topicDiv += "		<br/>";
                        }
                        topicDiv += iLiveMessage.webContent;
                        topicDiv += "		</p>";
                        if (iLiveMessage.msgType == 2 || iLiveMessage.msgType == 4) {
                            var images = iLiveMessage.images;
                            topicDiv += "<p>";
                            for (var j = 0; j < images.length; j++) {
                                topicDiv += "<img class=\"toBig\" data-src=\"" + images[j] + "\" src=\"" + images[j] + "\" />";
                            }
                            topicDiv += "</p>";
                        } else if (iLiveMessage.msgType == 5 || iLiveMessage.msgType == 3) {
                            topicDiv += "<p>";
                            topicDiv += "<video class=\"toVideo\" controls=\"controls\"  src=\"" + iLiveMessage.video + "\">您的浏览器不支持Video标签</video>";
                            topicDiv += "<p>";
                        }
                        topicDiv += "		<p class=\"handle\">";
                        topicDiv += "			<span class=\"message\"><i onclick=\"saveComments(" + iLiveMessage.msgId + ")\"></i><span id=\"commentsNumber_" + iLiveMessage.msgId + "\">0</span></span>";
                        topicDiv += "		 	<span class=\"thumbup\" id=\"messagePraise_" + iLiveMessage.msgId + "\"><i></i><span id=\"praiseNumber_" + iLiveMessage.msgId + "\">0</span></span>";
                        topicDiv += "			<a class=\"replyBtn\" id=\"replyBtn_" + iLiveMessage.msgId + "\">回复</a>";
                        topicDiv += "		</p>";
                        topicDiv += "		<div class=\"reply\">";
                        topicDiv += "			<div id=\"commentsList_" + iLiveMessage.msgId + "\"></div>";
                        topicDiv += "		</div>";
                        topicDiv += "</div>";
                    } else {
                        //置顶
                        topTopicDiv += "<div id=\"msgId_" + iLiveMessage.msgId + "\">";
                        topTopicDiv += "		<p class=\"sofa\">";
                        topTopicDiv += "			<span class=\"spanTop\">顶</span><i class=\"btext\">" + iLiveMessage.senderName + "：</i>" + iLiveMessage.webContent;
                        topTopicDiv += "		</p>";
                        if (iLiveMessage.msgType == 2 || iLiveMessage.msgType == 4) {
                            topTopicDiv += "<p>";
                            var images = iLiveMessage.images;
                            for (var j = 0; j < images.length; j++) {
                                topTopicDiv += "<img class=\"toBig\" data-src=\"" + images[j] + "\" src=\"" + images[j] + "\" />";
                            }
                            topTopicDiv += "</p>";
                        } else if (iLiveMessage.msgType == 5 || iLiveMessage.msgType == 3) {
                            topTopicDiv += "<p>";
                            topTopicDiv += "<video class=\"toVideo\" controls=\"controls\"  src=\"" + iLiveMessage.video + "\">您的浏览器不支持Video标签</video>";
                            topTopicDiv += "</p>";
                        }
                        topTopicDiv += "		<p class=\"handle\">";
                        topTopicDiv += "			<span class=\"message\"><i></i><span id=\"commentsNumber_" + iLiveMessage.msgId + "\">0</span></span>";
                        topTopicDiv += "		 	<span class=\"thumbup\" id=\"messagePraise_" + iLiveMessage.msgId + "\"><i></i><span id=\"praiseNumber_" + iLiveMessage.msgId + "\">0</span></span>";
                        topTopicDiv += "			<a class=\"replyBtn\" id=\"replyBtn_" + iLiveMessage.msgId + "\">回复</a>";
                        topTopicDiv += "		</p>";
                        topTopicDiv += "		<div class=\"reply\">";
                        topTopicDiv += "			<div id=\"commentsList_" + iLiveMessage.msgId + "\"></div>";
                        topTopicDiv += "		</div>";
                        topTopicDiv += "</div>";
                    }
                }
                $("#topicDiv").html(topicDiv);
                $("#topTopicDiv").html(topTopicDiv);
                selectcommentsList(roomId);//页面加载5秒执行一次


                $('.topoicBox').on("click", '.replyBtn', function () {
                    if (commentsAllow == 1) {
                        $('.reply_textarea').remove()
                        $(this).parent().parent().append("<div class='reply_textarea'><input type='text' name='content' maxlength='30' value='' /><button type='submit' onclick=\"sendMessageBtn()\">发送</button></div>")
                    } else {
                        layerAlert('回复功能暂未开启')
                    }
                })

                $('.topoicBox').on('focus', '.reply_textarea input', function () {
                    var userId = $("input[name=userId]").val()
                    if (userId == 0) {
                        loginHtml()
                        return;
                    }
                })

                // 页面中只允许一个video播放
                var videos = document.getElementsByTagName('video');
                for (var i = videos.length - 1; i >= 0; i--) {
                    (function(){
                        var p = i;
                        videos[p].addEventListener('play',function(){
                            pauseAll(p);
                        })
                    })()
                }
                function pauseAll(index){
                    for (var j = videos.length - 1; j >= 0; j--) {
                        if (j!=index) videos[j].pause();
                    }
                }

            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            }
        }
    });
}

//企业关注  取消
function updateConcernStatus(enterpriseId, concernStatus) {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        loginHtml()
        return;
    }
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/app/room/enterprise/concern.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "pc",
            enterpriseId: enterpriseId,
            userId: userId,
            type: concernStatus
        },
        success: function (data) {
            console.log(data)
            var status = data.code;
            if (data.code == 401) {
                window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            } else if (status == 1) {
                if (concernStatus == 0) {
                    $("#yiguanzhu").css("display", "none");
                    $("#guanzhu").css("display", "block");
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "关注提示",
                    //     Content: data.message
                    // });
                    layerAlert(data.message)
                } else {
                    $("#yiguanzhu").css("display", "block");
                    $("#guanzhu").css("display", "none");
                    // $.DialogBySHF.Confirm({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "关注提示",
                    //     Content: data.message,
                    //     ConfirmFun: openConfirm,
                    //     CancelFun: temporaryCancel
                    // });
                    // $("#btnDialogBySHFConfirm").val("现在打开");
                    // $("#btnDialogBySHFCancel").val("暂时不用");
                    layerAlert(data.message)
                }
            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "提示",
                //     Content: data.message
                // });
                layerAlert(data.message)
            }
        }
    });
}

//现在打开
function openConfirm() {
    console.log("你确认了查看直播方更多直播内容");
}

//暂时不用
function temporaryCancel() {
    console.log("你放弃查看直播方更多直播内容");
}

//倒计时
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
    var dateTime = date.getTime() - newDate.getTime();   //时间差的毫秒数
    if (dateTime > 0) {
        var days = Math.floor(dateTime / (24 * 3600 * 1000));   //计算出相差天数
        var leave1 = dateTime % (24 * 3600 * 1000);   //计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000));
        //计算相差分钟数
        var leave2 = leave1 % (3600 * 1000);      //计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000));
        //计算相差秒数
        var leave3 = leave2 % (60 * 1000);      //计算分钟数后剩余的毫秒数
        var seconds = Math.round(leave3 / 1000);
        var dataTime = days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒";

        $("#startLiveTime").html("距离直播开始：<i>" + days + "</i>天<i>" + hours + "</i>小时<i>" + minutes + "</i>分<i>" + seconds + "</i>秒");
        setTimeout(function () {
            selectTimer(liveStartTime);
        }, 1000);
    } else {
        $("#startLiveTime").html("距离直播开始：<i>0</i>天<i>0</i>小时<i>0</i>分<i>0</i>秒");
    }
}

//回看
function selectMediaFile(fileId) {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    window.location.href = "http://" + h5Base + "/pc/review.html?fileId=" + fileId + "&userId=" + userId + "&roomId=" + roomId;
}

function videoDiv(pushStreamAddr) {

    interfaceHttp = pushStreamAddr,
        shareHtmlUrl = "",
        shareStatu = '',
        AddressSeparator = '#',
        authUrl = 'true',
        ifShowImageBtn = 'false',
        ifAuthCDN_WS = 'false',
        PlayVideoAspect = 'mc169',//mc43;mc169;diaplasis;bespread
        ifShowLogo = 'false',
        AudioPlayerType = '0',
        AVPlayerType = '0',
        ifShiftMove = 'true',


        _playAuto = 'true';

    var flashvars = "interfaceHttp=" + interfaceHttp +
        "&AudioPlayerType=" + AudioPlayerType +
        "&AVPlayerType=" + AVPlayerType +
        "&authUrl=" + authUrl +
        "&AddressSeparator=" + AddressSeparator +
        "&ifShowLogo=" + ifShowLogo +
        "&ifShiftMove=" + ifShiftMove +
        "&ifAuthCDN_WS=" + ifAuthCDN_WS +
        "&PlayVideoAspect=" + PlayVideoAspect +
        "&ifShowTime=" + ifShowTime +
        "&ifShowPoopBtn=" + ifShowPoopBtn +
        "&ifShowImageBtn=" + ifShowImageBtn +
        "&ifShowSetBtn=" + ifShowSetBtn +
        "&playAuto=" + _playAuto +
        "&ifShowStreamBtn=" + ifShowStream +
        "&bufferLongTime=" + bufferLongTime +
        "&streamType=" + streamType +
        "&function_lamp_method=" + function_lamp_method +
        "&small_video_window=" + small_video_window +
        "&interfaceType=" + interfaceType +
        "&function_stop_method=" + function_stop_method +
        "&ifForbitSeek=" + ifForbitSeek +
        "&shareStatu=" + shareStatu +
        "&shareSwfUrl=" + shareSwfUrl +
        "&shareHtmlUrl=" + shareHtmlUrl +
        "&ifShowMenuItem=" + ifShowMenuItem;

    var swfHtml = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="100%" height="100%" id="' + playerName + '" align="middle">\
				<param name="movie" value="' + swfUrl + '" />\
				<param name="quality" value="high" />\
				<param name="bgcolor" value="#000000" />\
				<param name="devicefont" value="false" />\
				<param name="wmode" value="opaque" />\
				<param name="allowScriptAccess" value="always" />\
				<param name="allowFullscreen" value="true" />\
				<param name="allownetworking" value="all" />\
				<param name="salign" value="lt" />\
				<param name="flashvars" value="' + flashvars + '"  />\
					<a href="http://www.adobe.com/go/getflash">\
						<img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="获得 Adobe Flash Player" />\
					</a>\
			</object>';
    if (navigator.plugins && navigator.mimeTypes && navigator.mimeTypes.length) {
        //alert(navigator.userAgent);
        swfHtml = '<embed   src="' + swfUrl + '" id="' + playerName + '" name="' + playerName + '" type="application/x-shockwave-flash" data="' + swfUrl + '"  quality="high" allowScriptAccess="always" redirectUrl="http://www.adobe.com/shockwave/download/download.cgi?" swLiveConnect="true" menu="false" allowFullScreen="true" salign="lt"  width="100%" height="100%" flashvars="' + flashvars + '">\
				</embed>';
    }
    var _id = 'flash';
    document.getElementById(_id).innerHTML = swfHtml;
}


//弹框
function errorfunction() {
    window.location.href = "http://" + h5Base + "/pc/error.html";
}

function loginHtml() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    window.location.href = "http://" + h5Base + "/pc/login.html?roomId=" + roomId;
}

function loginHtmlError() {
    // $.DialogBySHF.Alert({
    //     Width: 350,
    //     Height: 200,
    //     Title: "提示",
    //     Content: "你放弃了登录、无法进行操作"
    // });
    layerAlert('你放弃了登录、无法进行操作')
}

//滚屏消息
function selectAdvertising() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/selectAdvertising.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {

            if (data.code == 1) {
                var iLiveRollingAdvertising = data.data;
                var startType = iLiveRollingAdvertising.startType;
                if (startType == 1) {
                    console.log("开启");
                    var content = iLiveRollingAdvertising.content;
                    // $(".marqueeBox").removeClass("hide");
                    // $(".marqueeBox").text(content);

                    $(".marqueeBox").removeClass("hide").text(content);
                    $('.marqueeBox').marquee({
                        duration: 10000
                    });


                } else {
                    console.log("关闭");
                    $(".marqueeBox").addClass("hide");
                }
            } else {
                console.log("获取滚动消息失败：" + data.message);
            }
        }
    });
}

//获取当前直播中文档直播信息
var gallerySwiper, def_Manual;

function selectDocument(documentId, documentDownload, documentPageNO, documentManual) {
    def_Manual = documentManual
    if (documentId == -1) {
        checkslide()
        if (documentDownload == 0) {
            $(".download span").css("display", "none");
        } else {
            $(".download span").css("display", "inline-block");
        }
        gallerySwiper.slideTo(documentPageNO - 1, 1000, false);
        // if (documentManual == 0) {
        //     $(".pageDIV").css("display", "none");
        //     openPage(documentPageNO);
        // } else {
        //     $(".pageDIV").css("display", "block");
        // }
    } else {
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/document/getByd.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                docId: documentId
            },
            success: function (data) {
                if (data.code == 1) {
                    //文档发送改变
                    var documentManager = data.data;
                    documentPictures = documentManager.list;

                    //changeswiping(documentPictures, 'document')
                    var HTML = "";

                    HTML += '<div class="swiper-container" id="gallery">';
                    HTML += '  <div class="swiper-wrapper">';
                    // for (var i = 0; i < documentPictures.length; i++) {
                    //     var url = documentPictures[i].url;
                    //     HTML += '<div class="swiper-slide"><img data-src="' + url + '"  class="swiper-lazy"><div class="swiper-lazy-preloader"></div></div>'
                    // }
                    HTML += '  </div></div>';
                    HTML += '<div class="pagintatioin">' +
                        '<div class="prevAll"><i class="iconfont icon-prevall"></i></div>' +
                        '<div class="prev"><i class="iconfont icon-prev"></i></div>' +
                        '<div class="swiper-pagination document-pagination"></div>' +
                        '<div class="next"><i class="iconfont icon-next"></i></div>' +
                        '<div class="nextAll"><i class="iconfont icon-nextall"></i></div>' +
                        '<div  class="inputText"><input type="text" value="1"></div>' +
                        '<div class="documentBtn"><button>确定</button></div>' +
                        '<div class="download"><span onclick="selectDocumentList(' + documentId + ')" ></span></div>' +
                        '</div>';

                    HTML += ''
                    // for (var i = 0; i < documentPictures.length; i++) {
                    //     var url = documentPictures[i].url;
                    //     HTML += "<li><img src=\"" + url + "\"></li>";
                    // }
                    $(".document").html(HTML);


                    //$("#ul").html(HTML);
                    wendangJS(documentPictures, documentPageNO - 1);

                    if (documentDownload == 0) {
                        $(".download span").css("display", "none");
                    } else {
                        $(".download span").css("display", "inline-block");
                    }
                    // if (documentManual == 0) {
                    //     $(".pageDIV").css("display", "none");
                    //     //openPage(documentPageNO);
                    // } else {
                    //     $(".pageDIV").css("display", "block");
                    //     //openPage(documentPageNO);
                    // }
                } else {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "警告",
                    //     Content: "data.message"
                    // });
                    layerAlert(data.message)
                }
            }
        });
    }
}

//所有文档
function selectDocumentList(enterpriseId) {
    alert('开发中')
    // $.ajax({
    //     type: "POST",//请求方式 get/post
    //     url: "http://" + tomcatBase + "/ilive/document/getByEnterpriseId.jspx",
    //     dataType: "jsonp",
    //     jsonp: "callback",
    //     cache: false,
    //     data: {
    //         enterpriseId: enterpriseId
    //     },
    //     success: function (data) {
    //         if (data.code == 1) {
    //             var documentList = data.data;
    //             var HTML = "";
    //             for (var i = 0; i < documentList.length; i++) {
    //                 var document = documentList[i];
    //                 var url = document.url;
    //                 var name = document.name;
    //                 var size = document.size;
    //                 var type = document.type;
    //                 HTML += "<tr>";
    //                 HTML += "	<td><input type=\"checkbox\" style=\"vertical-align: middle;\" url=\"" + url + "\"/></td>";
    //                 HTML += "	<td>";
    //                 if (type == "doc" || type == "docx") {
    //                     HTML += "		<img class=\"pptPic\" src=\"images/word.png\" alt=\"\" />" + name;
    //                 } else {
    //                     HTML += "		<img class=\"pptPic\" src=\"images/ppt.png\" alt=\"\" />" + name;
    //                 }
    //                 HTML += "	</td>";
    //                 HTML += "	<td>" + size + "b</td>";
    //                 HTML += "	<td onclick=\"downloadFile('" + url + "')\"><i class=\"fa fa-download\" style=\"font-size:14px;cursor: pointer;\"></i></td>";
    //                 HTML += "</tr>";
    //             }
    //             $("#fileTbody").html(HTML);
    //         } else {
    //             $.DialogBySHF.Alert({
    //                 Width: 350,
    //                 Height: 200,
    //                 Title: "警告",
    //                 Content: "data.message"
    //             });
    //         }
    //     }
    // });
}

var firstImg;
var num;
var imgW;
var timer;

function wendangJS(documentPictures, realIndex) {

    gallerySwiper = new Swiper('#gallery', {
        initialSlide: realIndex || 0,
        noSwiping: true,
        observer: true,
        observeParents: true,
        width: $('#gallery').width(),
        pagination: {
            el: '.document-pagination',
            type: 'fraction',
        },
        virtual: {
            slides: (function () {
                var slides = [];
                for (var i = 0; i < documentPictures.length; i++) {
                    var url = documentPictures[i].url;
                    slides.push('<div class="swiper-slide"><img data-src="' + url + '"  class="swiper-lazy"><div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div></div>');
                }
                return slides;
            }()),
        },
        lazy: {
            loadPrevNext: true,
            loadPrevNextAmount: 2,
        },
    })
    gallerySwiper.slideTo(realIndex, 1000, false);
    checkslide()

    // //获取第几张图片
    // $(".totle").html($('#ol li').length);
    // // 添加到最后的位置 并设置 ul 的宽度
    // num = 1;
    // $(".num").html(num);
}

function checkslide(documentPageNO) {
    if (!def_Manual) {
        gallerySwiper.$wrapperEl.addClass('swiper-no-swiping')
        $('.pagintatioin').hide()
    } else {
        $('.pagintatioin .prevAll').click(function () {
            gallerySwiper.slideTo(0, 1000, false);
        })

        $('.pagintatioin .prev').click(function () {
            gallerySwiper.slidePrev();
        })

        $('.pagintatioin .next').click(function () {
            gallerySwiper.slideNext();
        })
        $('.pagintatioin .nextAll').click(function () {
            var num = $('.swiper-pagination-total').text()
            gallerySwiper.slideTo(num - 1, 1000, false);
        })
        $('.pagintatioin .documentBtn').click(function () {
            var num = $('.inputText').find('input').val()
            gallerySwiper.slideTo(num - 1, 1000, false);
        })
        gallerySwiper.$wrapperEl.removeClass('swiper-no-swiping')
        $('.pagintatioin').show()

    }


}

// function rightPageUL() {
//     firstImg = $('#ul li').first().clone();
//     imgW = $('#ul img').width();
//     // 下一张
//     num = num + 1;
//     if (num >= $('#ul li').length + 1) {
//         num = 1
//     }
//     moveImgUL(num);
//     $(".num").html(num);
// }
//
// function leftPageUL() {
//     firstImg = $('#ul li').first().clone();
//     imgW = $('#ul img').width();
//     // 上一张
//     num = num - 1;
//     if (num <= 0) {
//         num = $('#ul li').length;
//     }
//     moveImgUL(num);
//     $(".num").html(num);
// }
//
// function rightPageOL() {
//     firstImg = $('#ol li').first().clone();
//     imgW = $('#ol img').width();
//     // 下一张
//     num = num + 1;
//     if (num >= $('#ol li').length + 1) {
//         num = 1
//     }
//     moveImgOL();
//     $(".num").html(num);
// }
//
// function leftPageOL() {
//     firstImg = $('#ol li').first().clone();
//     imgW = $('#ol img').width();
//     // 上一张
//     num = num - 1;
//     if (num <= 0) {
//         num = $('#ol li').length;
//     }
//     moveImgOL();
//     $(".num").html(num);
// }
//
// function home() {
//     openPage(1);
// }
//
// function last() {
//     openPage($('#ol li').length);
// }
//
// function openPage(documentPageNO) {
//     firstImg = $('#ol li').first().clone();
//     imgW = $('#ol img').width();
//     if (documentPageNO >= $('#ol li').length + 1) {
//         documentPageNO = 1
//     } else if (documentPageNO <= 0) {
//         documentPageNO = $('#ol li').length;
//     }
//     num = documentPageNO
//     moveImgOL();
//     $(".num").html(documentPageNO);
// }
//
// //移动到指定的图片
// function moveImgOL() {
//     console.log(num)
//     // 移动图片动画
//     $('#ol').stop().animate({left: (num - 1) * imgW * -1}, 400);
// }
//
// //移动到指定的图片
// function moveImgUL() {
//     console.log(num)
//     // 移动图片动画
//     $('#ul').stop().animate({left: (num - 1) * imgW * -1}, 400);
// }
//切换文档和视频
function openDocument() {
    var $flash = $('.play-video')
    var $document = $('.document')
    var flashTmp = $flash.clone()
    var documentTmp = $document.clone()

    $document.html(flashTmp.children())
    $flash.html(documentTmp.children())

    wendangJS(documentPictures, gallerySwiper.realIndex)

}

// function changeswiping(documentPictures,) {
//     gallerySwiper = new Swiper('#gallery', {
//         noSwiping: true,
//         width: $('#gallery').width(),
//         pagination: {
//             el: '.swiper-pagination',
//             type: 'fraction',
//         },
//         virtual: {
//             slides: (function () {
//                 var slides = [];
//                 for (var i = 0; i < documentPictures.length; i++) {
//                     var url = documentPictures[i].url;
//                     slides.push('<div class="swiper-slide"><img data-src="' + url + '"  class="swiper-lazy"><div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div></div>');
//                 }
//                 return slides;
//             }()),
//         }
//
//     })
//
// }
//下载
function downloadFile(url) {
    window.location.href = url;
}

//批量下载
function downloadAll() {
    $("#fileTbody tr").each(function (index, item) {
        if ($(item).find("input").is(':checked')) {
            var url = $(item).find("input").attr("url");
            window.location.href = url;
        }
    });
}

function selectAll() {
    if ($("#checkboxAll").is(':checked')) {
        $("#fileTbody tr").each(function (index, item) {
            $(item).find("input").prop("checked", true);
        });
    } else {
        $("#fileTbody").each(function (index, item) {
            $(item).find("input").prop("checked", false);
        });
    }
}

function loadJsCss(filename, filetype) {
    if (filetype == "js") {
        var fileref = document.createElement('script')//创建标签
        fileref.setAttribute("type", "text/javascript")//定义属性type的值为text/javascript
        fileref.setAttribute("src", filename)//文件的地址
    } else if (filetype == "css") {
        var fileref = document.createElement("link")
        fileref.setAttribute("rel", "stylesheet")
        fileref.setAttribute("type", "text/css")
        fileref.setAttribute("href", filename)
    }
    if (typeof fileref != "undefined") {
        document.getElementsByTagName("head")[0].appendChild(fileref)
    }
}

//红包
var hbflag = false

function hongbao() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/prize/isRedPacket.jspx",

        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            var code = data.code;
            if (code == 1) {
                openhongbao(data.packet.command)
                hbflag = true

            } else {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "警告",
                //     Content: "data.message"
                // });
                layerAlert(data.message)
            }
        }
    })

}

function openhongbao(text) {
    customModal()
    if (hbflag) return false
    var arrText = text.split('')
    var canvas = $('#popuphongbao')[0]
    var cxt = canvas.getContext('2d')
    var imageBox = $('#imghongbao')[0]
    var img = new Image()

    var arrText1 = []
    var arrText2 = []
    $.each(arrText, function (i, v) {
        if (i <= 9) {
            arrText1.push(v)
        } else {
            arrText2.push(v)
        }
    })
    var stringText1 = arrText1.join('  ')
    var stringText2 = arrText2.join('  ')

    img.src = 'images/hongbao.png'
    img.onload = function () {
        cxt.drawImage(img, 0, 0)
        cxt.textBaseline = 'middle'
        cxt.textAlign = 'center'

        var left = canvas.width / 2
        var top = canvas.height / 2 + 92


        cxt.fillStyle = "#000"

        if (stringText2.length > 0) {
            cxt.font = "16px Microsoft YaHei"
            cxt.fillText(stringText1, left, top - 10)
            cxt.fillText(stringText2, left, top + 12)
        } else {
            cxt.font = "24px Microsoft YaHei"
            cxt.fillText(stringText1, left, top)
        }

        imageBox.src = canvas.toDataURL("image/jpg")


    }

}

//签到
function qiandao() {
    GetSignId(function (signId) {
        $("#actionContent").html(qiandaoHTML(signId));
    })
}


function GetSignId(callback) {
    var roomId = $("input[name=roomId]").val()
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/prize/isSign.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            callback(data.signId)
            if (data.code == 1) {
                callback(data.signId)
            }
        }
    })
}

//验证手机号是否签到
function validate(obj, signId) {

    var phoneNumber = obj.value;
    if (phoneNumber != null && phoneNumber != "") {
        if (!isPoneAvailable(phoneNumber.trim())) {
            $.DialogBySHF.Alert({
                Width: 350,
                Height: 200,
                Title: "警告",
                Content: "请输入正确的手机号"
            });
            return false
        }
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/prize/siginPhone.jspx?userPhone=" + phoneNumber + "&signId=" + signId,
            dataType: "jsonp",
            jsonp: "callback",
            async: false,
            cache: false,
            success: function (data) {
                var status = data.status;
                if (status == 1) {
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "警告",
                    //     Content: "您已签到"
                    // });
                    layerAlert('您已签到')
                    $('.QiandaoBtn').attr("disabled", "true").addClass('disableBtn');
                    return false
                } else {
                    $('.QiandaoBtn').removeAttr("disabled").removeClass('disableBtn');
                }
            }
        });
    }

}

function saveQiandao(obj, signId) {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var $parent = $(obj).parent()

    var phoneNumber = $parent.find($("input[name=phoneNumber]")).val();
    var smscode = $parent.find($("input[name=vPassword]")).val();
    var username = $parent.find($("input[name=username]")).val();

    if (phoneNumber == undefined || phoneNumber.replace("\s+", "") == "") {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "请输入手机号"
        // });
        layerAlert('请输入手机号')
        return false
    }
    if (smscode == undefined || smscode.replace("\s+", "") == "") {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "验证码不能为空"
        // });
        layerAlert('验证码不能为空')
        return false
    }
    if (username == undefined || username.replace("\s+", "") == "") {
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "昵称不能为空"
        // });
        layerAlert('昵称不能为空')
        return false
    }

    var url = "http://" + tomcatBase + "/ilive/prize/userSign.jspx"

    $.ajax({
        type: "POST", //请求方式 get/post
        url: url,
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            userphone: phoneNumber,
            username: encodeURIComponent(username),
            vPassword: smscode,
            roomId: roomId,
            signId: signId
        },
        success: function (data) {

            if (data.status == "0") {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "警告",
                //     Content: data.msg
                // });
                layerAlert(data.msg)
                return false
            }
            if (data.status == 1000) {
                $('#actionBar').modal('hide')
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "签到成功",
                //     Content: data.msg
                // });
                layerAlert(data.msg)
                return false
            }
            if (data.status == 1001) {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "警告",
                //     Content: data.msg
                // });
                layerAlert(data.msg)
                return false

            }
            if (data.status == 1002) {
                // $.DialogBySHF.Alert({
                //     Width: 350,
                //     Height: 200,
                //     Title: "警告",
                //     Content: data.msg
                // });
                layerAlert(data.msg)
                return false
            }
        }
    });

}


function qiandaoHTML(signId) {
    if (signId) {
        html = '<div class="main-qiandao">' +
            '<div class="form-group">' +
            '<input type="text" class="form-control" placeholder="手机号"   maxlength="11" name="phoneNumber" id="phoneNumber" onblur="validate(this,' + signId + ')"/><i class="iconfont icon-shouji"></i>' +
            '</div>' +
            '<div class="form-group">' +
            '<input type="text" class="form-control vPassword" placeholder="动态密码"  maxlength="6" name="vPassword"/><i class="iconfont icon-yanzhengma"></i><span class="yzmBtn1" data-type="reg" onclick="getAuthenticode(this)" id="spenTimer">获取动态密码</span>' +
            '</div>' +
            '<div class="form-group">' +
            '<input type="text" class="form-control" style="padding: 10px" placeholder="昵称 建议使用您的真实姓名签到，2-10个字" minlength="2" maxlength="10" name="username"/>' +
            '</div>' +
            '<button type="button" class="btn  QiandaoBtn tyBtn btn-lg btn-block"  disabled onclick="saveQiandao(this,' + signId + ')">点击立即签到</button>' +
            '</div>'
    } else {
        html = '活动已结束'
    }

    return html

}

var $body = $(document.body)
var $backdrop = null

function customModal() {
    $backdrop = $(document.createElement('div'))
        .addClass('modal-backdrop fade in')
        .appendTo($body)
    $body.addClass('modal-open')
    $('.Popup').show()
}

function removeModal(element) {
    $body.removeClass('modal-open')
    $backdrop.remove()
    $backdrop = null
    $(element).parent().hide()
}

//互动
$('#actionBar').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var Title = button.data('title')
    var type = button.data('type')
    var modal = $(this)
    modal.find('.modal-title').text(Title)
    switch (type) {
        case 'qiandao':
            qiandao()
            break
        case 'choujiang':
            inPrize()
            break
        case 'hongbao':
            hongbao()
        case 'toupiao':
            selectTouPiao()
        case 'wenjuan':
            selectWenjuan()
    }
});
$('#actionBar').on('hide.bs.modal', function (event) {
    $("#actionContent").children().remove()

});

function selectActions() {
    console.log("回去抽奖、投票是否开启");
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/prize/isStartPrizr.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            roomId: roomId
        },
        success: function (data) {
            var code = data.code;
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (code == 1) {
                //抽奖
                // appLotteryAddr: "http://zbt.tv189.net/phone/appLottery.html?roomId=2001"
                // appQuestionAnswerAddr: "http://zbt.tv189.net/phone/appQuestionAnswer.html?roomId=2001"
                // appVoteAddr: "http://zbt.tv189.net/phone/appVote.html?roomId=2001"
                // isPlayRewards:打赏
                // isStartPrizr: 抽奖
                // isStartQuestionAnswer: 礼物
                // isStartRedPacket: 红包
                // isStartSign: 签到
                // isStartVote: 投票

                var isPlayRewards = data.data.isPlayRewards //打赏
                var isStartPrizr = data.data.isStartPrizr  //抽奖
                // var isStartQuestionAnswer= data.data.isStartQuestionAnswer //礼物
                var isStartRedPacket = data.data.isStartRedPacket //红包
                var isStartSign = data.data.isStartSign //签到
                var isStartVote = data.data.isStartVote //投票
                var isStartQuestionnaire=data.data.isStartQuestionnaire //问卷
                if (isStartSign) { //签到
                    setTimeout(function () {
                        $('#qiandao').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#qiandao').addClass('hide')
                    }, 100)
                }

                if (isPlayRewards) {
                    setTimeout(function () { //打赏
                        $('#dashang').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#dashang').addClass('hide')
                    }, 100)
                }


                if (isStartPrizr) {
                    setTimeout(function () {//抽奖
                        $('#choujiang').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#choujiang').addClass('hide')
                    }, 100)
                }

                if (isStartRedPacket) {//红包
                    setTimeout(function () {
                        $('#hongbao').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#hongbao').addClass('hide')
                    }, 100)
                }

                if (isStartVote) { //投票
                    setTimeout(function () {
                        $('#toupiao').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#toupiao').addClass('hide')
                    }, 100)
                }
                if (isStartQuestionnaire) { //问卷
                    setTimeout(function () {
                        $('#wenjuan').removeClass('hide')

                    }, 100)
                } else {
                    setTimeout(function () {
                        $('#wenjuan').addClass('hide')
                    }, 100)
                }
            } else {
                console.log(data.message)
            }
        }
    });
}

function isPoneAvailable(str) {
    var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
    if (!myreg.test(str)) {
        return false;
    } else {
        return true;
    }
}

// function selectRoom(roomId, callback) {
//     $.ajax({
//         type: "GET",//请求方式 get/post
//         url: "http://" + tomcatBase + "/ilive/selectRoom.jspx",
//         dataType: "jsonp",
//         jsonp: "callback",
//         cache: false,
//         data: {
//             roomId: roomId
//         },
//         success: function (data) {
//             callback(data)
//         }
//     });
// }

function selectPageDecorate(roomId, callback) {
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/selectPageDecorate.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId
        },
        success: function (data) {
            var code = data.code;
            if (code == 1) {
                callback(data)
            }

        }
    });
}
