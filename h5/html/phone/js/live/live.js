var ws, liveEventId = 0,
    nailName, dianzanNum = 0,
    swiperStatus,
    pageSwiper,
    lastTouchEnd = 0,
    u = navigator.userAgent,
    gable_estoppelType, //禁言
    person_estoppelType = 1; //个人禁言  0打开

if (!/Android|webOS|iPhone|iPod|iPad|BlackBerry/i.test(u)) {
    window.location.href = "http://" + h5Base + "/pc/play.html?roomId=" + Params.roomId; //PC
}

if (Params.roomId == 0) {
    errorfunction()
} else {

    if (judgeDeviceType.isWeiXin) {
        // 执行操作
        createAPI("http://" + tomcatBase + "/ilive/app/wx/app.jspx", {
                roomId: Params.roomId
            })
            .then(function (res) {
                try {
                    var status = res.loginWx;
                    if (status == 1) {
                        window.stop();
                        window.location.href = res.loginUrl;
                        return false;
                    } else {
                        init()
                    }
                } catch (e) {
                    layerOpen(e.name + ": " + e.message)
                }

            })
    } else {
        init()
    }


    function init() {
        checklogin()
    }

    //第一次鉴权
    function checklogin() {
        createAPI("http://" + tomcatBase + "/ilive/app/room/checkRoomLogin.jspx", {
            roomId: Params.roomId
        }).then(function (data) {
            try {
                var res = JSON.parse(data.data);
                var userInfo = res.userInfo;

                if (userInfo != undefined || userInfo != null) {
                    userId = userInfo.userId;
                    nailName = userInfo.nailName
                    if (userId != undefined || userId != null) {
                        $("input[name=userId]").val(userId);
                    }
                    var plinfo = {
                        'userphone': userInfo.mobile
                    };
                    window.sessionStorage.setItem('plinfo', JSON.stringify(plinfo))
                }
                switch (data.code) {
                    case 0:
                        layer.open({
                            content: data.message,
                            btn: '确定',
                            yes: function () {
                                errorfunction()
                            }
                        });
                        break
                    case 1:
                        //session 房间名字
                        window.sessionStorage.setItem('roomName', res.roomTitle || '天翼直播平台')
                        //var repeateGuide = res.repeateGuide;
                        var roomNeedLogin = res.roomNeedLogin;

                        // if (repeateGuide === 0) {
                        //     var openGuideSwitch = res.openGuideSwitch;
                        //     if (openGuideSwitch === 1) {
                        //         window.location.replace('http://' + h5Base + '/phone/guide.html?roomId=' + Params.roomId)
                        //         return false;
                        //     }
                        // }

                        //进入引导页
                        var openGuideSwitch = res.openGuideSwitch;
                        if (openGuideSwitch === 1) {
                            setguides()
                        }
                        /**  roomNeedLogin
                         *   1.需要登录
                         *   2不需要登录.
                         */

                        if (roomNeedLogin === 1) {
                            //登陆
                            setlogin()
                            //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId;
                        } else if (roomNeedLogin === 2) {
                            // if (userInfo != undefined || userInfo != null) {
                            //     userId = userInfo.userId;
                            //     nailName = userInfo.nailName
                            //     if (userId != undefined || userId != null) {
                            //         $("input[name=userId]").val(userId);
                            //     }
                            //     var plinfo = {'userphone': userInfo.mobile};
                            //     window.sessionStorage.setItem('plinfo', JSON.stringify(plinfo))
                            // }
                            secondPermission() //第二次鉴权
                        }

                        break
                    case 404:
                        window.location.href = "http://" + h5Base + "/phone/end.html";
                        break
                    case 402:
                        //绑定手机号
                        setlogin('bindPhone')
                        // if (userInfo != undefined || userInfo != null) {
                        //     var userId = userInfo.userId || 0;
                        //     // if (userId != undefined || userId != null) {
                        //     //     $("input[name=userId]").val(userId);
                        //     // }
                        //     window.location.href = "http://" + h5Base + "/phone/phoneNumber.html?roomId=" + Params.roomId + "&userId=" + userId;
                        // } else {
                        //     layer.open({
                        //         content: '绑定手机号，获取用户失败'
                        //         , btn: '确定'
                        //         , yes: function () {
                        //             errorfunction()
                        //         }
                        //     });
                        // }
                        break
                }
            } catch (e) {
                layerOpen(e.name + ": " + e.message)
            }


        })

    }

    //第二次鉴权
    function secondPermission() {
        createAPI("http://" + tomcatBase + "/ilive/app/room/getRoomInfo.jspx", {
                roomId: Params.roomId
            })
            .then(function (res) {
                try {
                    switch (res.code) {
                        case 0:
                            window.location.href = "http://" + h5Base + "/phone/nopermission.html?roomId=" + Params.roomId;
                            break
                        case 3: //直播间人数已满
                            window.location.href = "http://" + h5Base + "/phone/standby.html?roomId=" + Params.roomId;
                            break
                        case 404:
                            window.location.href = "http://" + h5Base + "/phone/end.html";
                            break
                        case 402:
                            if (userId !== 0) {
                                setlogin('bindPhone')
                            } else {
                                layer.open({
                                    content: '绑定手机号，获取用户失败',
                                    btn: '确定',
                                    yes: function () {
                                        errorfunction(getRoomInfo)
                                    }
                                });
                            }
                            break
                        case 1:

                            var liveEvent = res.data.liveEvent;
                            var viewAuthorized = liveEvent.viewAuthorized; //观看授权
                            var openSignupSwitch = liveEvent.openSignupSwitch; //表单

                            if (openSignupSwitch == 1) { //表单登陆
                                window.location.href = "http://" + h5Base + "/phone/form.html?roomId=" + Params.roomId;
                                return false
                            }
                            //观看授权
                            switch (viewAuthorized) {
                                case 2:
                                    //密码登陆
                                    setlogin('password')
                                    //window.location.href = "http://" + h5Base + "/phone/password.html?roomId=" + Params.roomId;
                                    break
                                case 3:
                                    //付费观看
                                case 6:
                                    setlogin('FCode')
                                    //window.location.href = "http://" + h5Base + "/phone/FCode.html?roomId=" + Params.roomId;
                                    break
                                default:
                                    //观看直播
                                    thirdPermission(res) //第三次鉴权
                                    break
                            }

                            break
                        default:
                            window.location.href = "http://" + h5Base + "/phone/nopermission.html?roomId=" + Params.roomId;
                            break
                    }
                } catch (e) {
                    layerOpen(e.name + ": " + e.message)
                }

            })
    }

    //第三次鉴权
    function thirdPermission(getRoomInfo) {
        createAPI("http://" + tomcatBase + "/ilive/app/room/roomenter.jspx", {
                roomId: Params.roomId,
                sessionType: 1,
                webType: 1
            })
            .then(function (res) {
                try {
                    var status = res.code;
                    switch (status) {
                        case 1:
                            $('#loding').hide()
                            var liveEvent = res.data.liveEvent;
                            $("input[name=liveEventId]").val(liveEvent.liveEventId);
                            liveEventId = liveEvent.liveEventId
                            //设置标题
                            var roomName = liveEvent.liveTitle || "";
                            document.title = roomName;
                            _template.page()
                            _height = $(window).height() - $('.menuBox').offset().top;
                            //设置菜单
                            setDecorate(getRoomInfo)
                            //观看直播
                            WatchVideo(res) //roomenter
                            //单个禁言
                            var forbidTalk = res.forbidTalk; // 1 打开 0 关闭
                            if (forbidTalk) {
                                person_estoppelType = 0
                                $('#chatipt').find('span').text('目前还不能讨论哦~')
                            }
                            //检查互动活动是否开启
                            isStartPrizr()
                            //查询签到点赞
                            checkRegister(liveEvent.liveEventId)
                            //签到数量
                            roomCount(liveEvent.liveEventId)
                            //初始化礼物列表
                            //gift()
                            // $('#loding').fadeOut()
                            //分享
                            wxshare.init()
                            break
                        case 2:
                            //白名单
                            var permissionsjump = window.sessionStorage.getItem('permissionsjump');
                            if (permissionsjump == null || permissionsjump == 1) {
                                window.sessionStorage.setItem('permissionsjump', 1)

                                setlogin()

                                // window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                                return false
                            }

                            window.location.href = "http://" + h5Base + "/phone/permissions.html?roomId=" + roomId;
                            break
                        case 3:
                            //直播间人数已满
                            window.location.href = "http://" + h5Base + "/phone/standby.html?roomId=" + roomId;
                            break
                        default:
                            layer.open({
                                content: '第三次失败，请联系管理员',
                                btn: '确定',
                                yes: function () {
                                    errorfunction()
                                }
                            });
                            break


                    }
                } catch (e) {
                    layerOpen(e.name + ": " + e.message)
                }


                //观看授权
                // switch (viewAuthorized) {
                //     case 1:
                //
                //         break
                //     case 2:
                //         //密码登陆
                //         window.location.href = "http://" + h5Base + "/phone/password.html?roomId=" + Params.roomId;
                //         break
                //     case 3:
                //     //付费观看
                //     case 4:
                //         //白名单观看
                //         var permissionsjump = window.sessionStorage.getItem('permissionsjump');
                //         if (permissionsjump == null || permissionsjump == 'true') {
                //             window.sessionStorage.setItem('permissionsjump', true)
                //             window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                //             return false
                //         }
                //
                //         window.location.href = "http://" + h5Base + "/phone/permissions.html?roomId=" + roomId;
                //         break
                //     case 6:
                //         window.location.href = "http://" + h5Base + "/phone/FCode.html?roomId=" + Params.roomId;
                //         break
                //     default:
                //         //观看直播
                //         alert('viewAuthorized错误')
                //         break
                // }
                //普通状态
                //检查互动活动是否开启


            })


    }
}


/**************视频类********************/
function WatchVideo(getroomenter) {

    setVideoInfo(getroomenter) //设置video
    setAdvertising() //滚屏消息
    //ws 连接
    setWebSocket(getroomenter.token)
}

//设置video
function setVideoInfo(data) {
    myPlayer = videojs('video', {
        textTrackDisplay: false,
        errorDisplay: false
    }) || null;
    var newbtn = $('<button id="x5-fullscreen" class="vjs-fullscreen-control vjs-control vjs-button" type="button" style="display:none"><span aria-hidden="true" class="vjs-icon-placeholder"></span><span class="vjs-control-text" aria-live="polite">Fullscreen</span></button>');
    var controlBar = document.getElementsByClassName('vjs-control-bar')[0];
    insertBeforeNode = document.getElementsByClassName('vjs-fullscreen-control')[0];
    controlBar.insertBefore(newbtn[0], insertBeforeNode);

    var liveEvent = data.data.liveEvent; //直播状态
    
    var hlsAddr = Params.roomId==10256 ? 'http://txcdn.pull.zb.tv189.com/live/zb.m3u8' : data.hlsAddr; //caibin

    //视频统一设置
    $("#playBg").css("background-image", "url(" + liveEvent.converAddr + ")")
    $("#iLiveRollingAdvertising").css('bottom', '0'); //滚屏消息
    $('.vjs-big-play-button').addClass("hide");
    switch (liveEvent.liveStatus) {
        case 0: //预约
            videoAppointment(liveEvent)
            break
        case 1: //直播
            videoOnLive(liveEvent, hlsAddr)
            console.log('直播')
            break
        case 2: //暂停
            videoPause(liveEvent)
            console.log('暂停')
            break
        case 3: //
            videoClose(liveEvent)
            console.log('关闭')
            break
    }

}

//滚屏消息
function setAdvertising() {
    createAPI("http://" + tomcatBase + "/ilive/selectAdvertising.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            if (res.code == 1) {
                var content = res.data.content;
                var startType = res.data.startType;
                $("#iLiveRollingAdvertising").css("display", 'none');
                if (startType === 1) {
                    console.log("滚屏开启");
                    $("#iLiveRollingAdvertising")
                        .css('display', 'block')
                        .text(content)
                        .marquee({
                            duration: 5000
                        });
                    return
                }
            } else {
                console.log("获取滚动消息失败：" + data.message);
            }
        })
}

//预约
function videoAppointment(liveEvent) {
    var openCountdownSwitch = liveEvent.openCountdownSwitch; //开启倒计时
    var countdownTitle = liveEvent.countdownTitle; //倒计时标题
    var Obj_data = liveEvent.liveStartTime;
    //ISYUYUE = true;
    //$('.yuyueBox').css('display', 'block')
    $('#enterpriseList').css('padding-bottom', '50px');
    selectAppointment(); //是否预约
    if (openCountdownSwitch === 1) {
        $("#customContent").text(countdownTitle);
        $(".videoPlay").css('display', 'block');
        $("#timeStart").css('display', 'block');
        $('#countdown').countdown(Obj_data).on('update.countdown', function (event) {
            $(this).html(
                event.strftime('' +
                    '<span class="countspan digit">%d<span class="countDate count0"></span></span><span class="countDiv"></span>' +
                    '<span class="countspan digit">%H<span class="countDate count1"></span></span><span class="countDiv"></span>' +
                    '<span class="countspan digit">%M<span class="countDate count2"></span></span>' +
                    ''
                ));
            var days = event.offset.days;
            var daysToWeek = event.offset.daysToWeek;
            var hours = event.offset.hours;
            var minutes = event.offset.minutes;

            if (days == 0 && daysToWeek == 0 && hours == 0 && minutes <= 10) {
                $(".appointment").addClass('soon').text("即将开始");
            }
        }).on('finish.countdown', function () {
            $("#timeStart").css('display', 'none');
            $("#endLive").css('display', 'block').find(".textMask").text('本场直播尚未开始');
        });
        return false
    }

    $("#endLive").css('display', 'block').find(".textMask").text('本场直播尚未开始');
}

//直播
var onceTime = false;

function videoOnLive(liveEvent, hlsAddr) {
    $("#playBg").css('display', 'none')
    $("#endLive").css('display', 'none')
    $('#iLiveRollingAdvertising').css('bottom', '16%')
    $('.yuyueBox').css('display', 'none') //预约提醒隐藏
    $('#videoPlay').css('display', 'none')
    $(".vjs-has-started .vjs-poster").css("display", "none")
    myPlayer.ready(function () {
        $('.vjs-big-play-button').removeClass("hide");
        this.src({
            src: hlsAddr,
            type: 'application/x-mpegURL'
        })
        this.poster(liveEvent.playBgAddr)
        this.pause()
        $('.vjs-live-control').text('实时直播')

        this.on('play', function () {
            if (onceTime) { //如果有图文视频就暂停直播 
                ThemePlay.forEach(function (value) {
                    value.pause()
                })
                onceTime = false //图文 多次出现
            }
            //Android
            if (judgeDeviceType.isAndroid) {
                dcVideo()
            }

        });
        this.on('error', function () {
            var mediaError = this.error();
            console.log(mediaError);
            if (mediaError.code == 1) { //MEDIA_ERR_ABORTED 视频播放被终止
                console.log("视频播放被终止");
            } else if (mediaError.code == 2) { //MEDIA_ERR_NETWORK 网络错误导致视频下载中途失败
                console.log("网络错误导致视频下载中途失败");
            } else if (mediaError.code == 3) { //MEDIA_ERR_DECODE 由于视频文件损坏或是该视频使用了你的浏览器不支持的功能，播放终止。
                console.log("由于视频文件损坏或是该视频使用了你的浏览器不支持的功能，播放终止。");
            } else if (mediaError.code == 4) { //MEDIA_ERR_SRC_NOT_SUPPORTED 视频因格式不支持或者服务器或网络的问题无法加载。
                console.log("视频因格式不支持或者服务器或网络的问题无法加载。");
            } else if (mediaError.code == 5) { //MEDIA_ERR_ENCRYPTED 视频已加密，无法解密。
                console.log("视频已加密，无法解密。");
            }
        })
    })
}
//x5 video
function dcVideo() {
    var video = $("video")[0];
    var itemHeight = window.screen.height; //获取手机高度
    var itemWidth = window.screen.width; //手机宽度

    video.addEventListener('x5videoenterfullscreen', function () {
        $('.vjs-fullscreen-control').hide()
        $('.vjs-loading-spinner').hide()
        setdcVideo()
        $('#x5-fullscreen').on('click', function () {
            $('#x5-fullscreen').hide()
            $("video").css({
                "width": "",
                "height": ""
            })
        })

    });
    video.addEventListener('x5videoexitfullscreen', function () {
        setdcVideo()
    })
    video.addEventListener('ended', function () {

    })
}

function setdcVideo() {
    $('#x5-fullscreen').show()
    var width = window.screen.width;
    var height = window.screen.height;
    setTimeout(function () {
        _height = $(window).height() - $('.menuBox').offset().top;
    }, 350)

    $("video").css({
        "width": width,
        "height": height
    })
}
//暂停
function videoPause() {
    $("#playBg").css('display', 'none');
    $('#iLiveRollingAdvertising').css('bottom', '0');
    $("#endLive").css('display', 'block').find(".textMask").text("当前暂无直播画面");
    $('.yuyueBox').css('display', 'none') //预约提醒隐藏
    $(".vjs-has-started .vjs-poster").css("display", "block")
    // myPlayer.ready(function () {
    //     $('.vjs-big-play-button').addClass("hide");
    //     this.pause()
    //     this.src({src: 'none', type: 'application/x-mpegURL'})
    // })
}

//关闭
function videoClose() {
    $('.yuyueBox').css('display', 'none')
    $("#endLive").css('display', 'block').find(".textMask").text("本场直播已结束");
    $('#iLiveRollingAdvertising').css('bottom', '0');
}

/**************WebSocket连接 ws********************/


function setWebSocket(token) {

    if (typeof (WebSocket) == "undefined") {
        layer.open({
            content: '你的浏览器不支持信息交流',
            btn: '确定',
            yes: function () {
                errorfunction()
            }
        });
        return;
    }
    //  connentWS()
    var url = "ws://" + tomcatBase + "/ilive/webSocketServer.jspx?username=" + token;
    ws = new WebSocket(url);
    ws.onopen = function () {
        heartCheck.start();
        console.log("连接成功" + new Date().toUTCString());

    }
    ws.onclose = function () {
        console.log("关闭连接 " + new Date().toUTCString());

    }
    ws.onerror = function () {

        console.log("建立连接异常" + new Date().toUTCString());

        //window.location.href = window.location.href;
    }
    ws.onmessage = function (e) {

        heartCheck.reset();
        var iLiveMessage = JSON.parse(e.data);
        var roomType = iLiveMessage.roomType; // 0直播管理  1消息管理
        switch (roomType) {
            case 0:

                //禁言
                var estoppelType = iLiveMessage.iLiveEventVo.estoppleType; //全局禁言

                //全局禁言
                if (estoppelType == 0) {
                    gable_estoppelType = 0 //禁言了
                    person_estoppelType = 0
                    $('#chatipt').find('span').text('目前还不能讨论哦~')
                } else {
                    gable_estoppelType = 1
                    person_estoppelType = 1
                    $('#chatipt').find('span').text('聊几句吧~~')
                }


                /**
                 * 直播状态：
                 * 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
                 */
                var liveStatus = iLiveMessage.iLiveEventVo.liveStatus;
                switch (liveStatus) {
                    case 1:
                        videoOnLive(iLiveMessage.iLiveEventVo, iLiveMessage.iLiveEventVo.hlsUrl)
                        break
                    case 2:
                        setTimeout(function () {
                            videoPause()
                            myPlayer.ready(function () {
                                this.pause()
                                $('.vjs-big-play-button').addClass("hide");
                                this.src({
                                    src: null,
                                    type: 'application/x-mpegURL'
                                })

                            })
                        }, 5000);

                        break
                    case 3:
                        setTimeout(function () {
                            videoClose();
                            $("#playBg").css('display', 'block');
                            myPlayer.ready(function () {
                                $('.vjs-big-play-button').addClass("hide");
                                this.pause();
                                this.src({
                                    src: 'none',
                                    type: 'application/x-mpegURL'
                                })

                            })

                        }, 23000);

                        break
                    case 4:
                        break
                    case 5:
                        break
                    case 6:
                        break


                }
                console.log('直播管理')
                break
            case 1:
                var disableValue = false;
                //全部删除
                if (iLiveMessage.deleteAll == 1) {
                    $('#commentBox').children().remove();
                    $('#dixian').css('display', 'none')
                    return;
                }

                //删除
                if (iLiveMessage.deleted == 1) {
                    $("#msgId_" + iLiveMessage.msgId).remove();
                    if ($('.talkline').length > 5) {
                        $('#dixian').css('display', 'block')
                    } else {
                        $('#dixian').css('display', 'none')
                    }
                    return;
                }
                //禁言
                var senderId = iLiveMessage.senderId; //发送者id
                var opType = iLiveMessage.opType; //个人禁言


                //如果全局禁言关闭 则判断个人禁言
                if (gable_estoppelType == 1) { // 0 开启  1 关闭
                    if (userId === senderId) {
                        if (opType == 11) {
                            person_estoppelType = 0 //禁言了

                            $('#chatipt').find('span').text('目前还不能讨论哦~')
                        } else {
                            person_estoppelType = 1 //没有禁言了

                            $('#chatipt').find('span').text('聊几句吧~~')
                        }
                    }
                }
                // if (userId === senderId) {
                //     if (opType == 11) {
                //         gable_estoppelType = 0 //禁言了
                //         $('#chatipt').find('span').text('目前还不能讨论哦~')
                //     } else {
                //         gable_estoppelType = 1 //没有禁言了
                //         $('#chatipt').find('span').text('聊几句吧~~')
                //     }
                // }


                // if (userId === senderId && opType == 10) {
                //     gable_estoppelType = 0 //禁言了
                //     $('#chatipt').find('span').text('目前还不能讨论哦~')
                // } else {
                //     gable_estoppelType = 1 //没有禁言了
                //     $('#chatipt').find('span').text('聊几句吧~~')
                // }
                //相同的评论
                $('.talkline').each(function () {
                    var userTapMsg = this.id;
                    var sysMsg = "msgId_" + iLiveMessage.msgId;
                    if (userTapMsg == sysMsg) {
                        $("#msgId_" + iLiveMessage.msgId).remove(); //相同评论踢了
                    }
                })

                // $("#msgId_" + iLiveMessage.msgId).remove();
                //重新组织 和回看一致字段 评论数据
                var value = {
                    'commentsId': iLiveMessage.msgId,
                    'liveMsgType': iLiveMessage.liveMsgType, // 2评论 3 问答
                    'topFlagNum': iLiveMessage.top, //置顶
                    'userImg': iLiveMessage.senderImage,
                    'commentsUser': iLiveMessage.senderName,
                    'createTime': iLiveMessage.createTime,
                    'comments': iLiveMessage.webContent,
                    'replyName': iLiveMessage.replyName,
                    'replyContent': iLiveMessage.replyContent,
                    'replyType': iLiveMessage.replyType
                };
                var dom = _template.commentDom(value);

                if (iLiveMessage.checked == 1) {
                    if (iLiveMessage.top == 1) { //置顶
                        $('#commentTop').prepend(dom)
                        return
                    }
                    $('#comment').prepend(dom)
                } else {
                    if (userId === senderId) {
                        $('#comment').prepend(dom)
                    }
                  
                }

                //添加底线
                if ($('.talkline').length > 5) {
                    $('#dixian').css('display', 'block')
                } else {
                    $('#dixian').css('display', 'none')
                }
                console.log('消息管理')
                break
            case 2:
                console.log("发现新话题");
                $("#redDot").css('display', 'block');
                $("#newTopic").css('display', 'block');
                break
            case 3:
                window.location.href = "http://" + h5Base + "/phone/end.html";
                break
            case 4:
                console.log("广告滚动");
                setAdvertising()
                break
            case 5:
                console.log("抽奖");
                isStartPrizr()
                break
            case 6:
                console.log("投票");
                isStartPrizr()
                break
            case 7:
                console.log("赠送礼物");
                break
            case 8:
                console.log("8");
                break
            case 9:
                console.log("打赏");
                break
            case 10:
                var welcomeLanguage = iLiveMessage.welcomeLanguage;
                arr.push({
                    "code": 2,
                    "welcomeLanguage": welcomeLanguage
                })
                giveGift();

                console.log("欢迎语");
                break
            case 11:
                var dcouments = {
                    'documentId': iLiveMessage.documentId,
                    'documentDownload': iLiveMessage.documentDownload,
                    'documentPageNO': iLiveMessage.documentPageNO,
                    'documentManual': iLiveMessage.documentManual
                };
                selectDocument(dcouments);

                console.log("文档");
                break
            case 15:
                console.log("签到活动");
                isStartPrizr()
                break
            case 16:
                console.log("红包");
                isStartPrizr()
                break
            default:
                console.log("操作类型" + iLiveMessage.roomType);

        }

    }


}

var heartCheck = {
    timeout: 20000,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        this.start();
    },
    start: function () {
        var self = this;
        this.timeoutObj = setTimeout(function () {
            var ping = {
                "p": "1"
            };
            ws.send(JSON.stringify(ping));
            console.log("websocket 发送")
            self.serverTimeoutObj = setTimeout(function () {
                ws.close(); //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    },
};

/**************菜单类********************/
function setDecorate(getRoomInfo) {
    var liveEvent = getRoomInfo.data.liveEvent;
    var pageDecorate = liveEvent.pageDecorate;
    gable_estoppelType = liveEvent.estoppelType; //禁言
    var dcouments = {
        'documentId': liveEvent.documentId,
        'documentDownload': liveEvent.documentDownload,
        'documentPageNO': liveEvent.documentPageNO,
        'documentManual': liveEvent.documentManual
    };
    /**
     * menuType
     * 1话题
     * 2聊天互动
     * 3在线问答
     * 4详情
     * 5往期回看
     * 6文档
     */
    var ind = 0;
    pageDecorate.forEach(function (value, index) {
        if (value.menuType == 5) return
        var redDot = value.menuType == 1 ? '<i class="redDot" id="redDot" style=" display:none;"></i>' : ''; //话题小圆点
        var Oli = '<li class="' + (index == 0 ? ' liactive' : '') + ' swiper-slide" data-index="' + ind + '" data-menuType=' + value.menuType + ' ><span>' + value.menuName + '</span>' + redDot + '</li>';
        var $swiperContent = $('#swiperContent');
        $('#swiperMenu .swiper-wrapper').append(Oli) //标题
        ind++
        switch (value.menuType) {
            case 1: //话题
                $swiperContent.find('.swiper-wrapper')
                    .append('<div class="swiper-slide" id="theme">\n' +
                        '<div id="themes" class="mescroll">' +
                        '<div class="hintBox" id="newTopic" style="display: none">有一条新的内容，下拉刷新观看</div>' +
                        '<div id="dataList" class="data-list"><div id="themesAllTop"></div><div id="themesAll"></div>' +
                        '</div>' +
                        '</div>')
                //获取话题评论
                if (index == 0) setThemes()

                break
            case 2: // 2聊天互动
                $swiperContent.find('.swiper-wrapper').append('<div class="swiper-slide" id="talkLi">\n' +
                    '<div class="commentBox mescroll"><div id="commentTop"></div><div id="comment"></div><div class="dixian" id="dixian" style="display: none"><span>-- 我是有底线的 --</span></div></div>' +
                    '<div class="newtalkCon" id="sendMessageDIV" tablevalue="1">\n' +
                    '            <div class="talkConLeft ">\n' +
                    '                <img src="img/graywen.png" alt="">\n' +
                    '            </div>\n' +
                    '            <div class="talkConRight" id="chatipt">\n' +
                    '                   <span>聊几句吧~</span>\n' +
                    '            </div>\n' +
                    '            <!-- <div class="giftIcon hide" onclick="openGiftIcon()"></div> -->\n' +
                    '        </div>' +
                    '</div>\n')

                //评论弹层
                chatipt()
                break
            case 3:
                break
            case 4: //4详情
                $swiperContent.find('.swiper-wrapper')
                    .append('<div class="swiper-slide ">\n' +
                        '<div class="commentBox mescroll" id="enterpriseList">' +
                        '<div class="cell showDesc" id="showDesc"></div>\n' +
                        '<div class="cell zbjcompany" id="zbjcompany"></div>\n' +
                        '<div class="cell reviewfsAll" id="reviewsAll" style="display: none"></div>\n' +
                        '<div class="cell ListConTit" id="ListConTit">\n' +
                        '  <div class="title ellipsis liveTitle">内容介绍</div>\n' +
                        '  <div id="livedescdiv"></div>\n' +
                        '</div>\n' +
                        '</div>\n' +
                        '<div class="yuyueBox" id="liveStart">\n' +
                        '     <button class="yuyue appointment" id="saveAppointment">预约开播通知</button>\n' +
                        '</div>\n' +
                        '</div>\n')

                enterpriseList(getRoomInfo) //设置企业
                // if (ISYUYUE || ISYUYUE == null) { //如果是预约 切换 出现预约按钮
                //     if (index == 0) $('.yuyueBox').css('display', 'block')
                // }
                saveAppointment()
                break
            case 5:
                //5往期回看
                $swiperContent.find('.swiper-wrapper').append('<div class="swiper-slide" id="review">5往期回看</div>')
                break
            case 6: //6文档
                $swiperContent.find('.swiper-wrapper').append('<div class="swiper-slide"><div class="wendang"></div></div>')
                if (liveEvent.documentId != 0) {
                    selectDocument(dcouments);
                }
                break


        }

    })

    //菜单切换

    var $swiperMenu = $('#swiperMenu').find('li');
    // new Swiper('#swiperMenu', {
    //     slidesPerView: ind,
    //     on: {shareinfo
    //         tap: function (e) {
    //             pageSwiper.slideTo(this.clickedIndex, 500, false);//跳转
    //             this.$el.find('li').removeClass('liactive');
    //             this.$el.find('li').eq(this.clickedIndex).addClass('liactive');
    //             if (this.$el.find('li').eq(this.clickedIndex).data('menutype') == 1) {
    //                 Theme_mescroll.resetUpScroll();//重置列表数据
    //                 //setThemes() //滑动是图文触发点击
    //             }
    //         },
    //     },
    // });
    $swiperMenu.on('click', function () {
        var i = Number($(this).data('index'));
        pageSwiper.slideTo(i); //以轮播的方式切换列表

    })
    pageSwiper = new Swiper('#swiperContent', {
        on: {

            slideChangeTransitionStart: function () {
                this.allowSlidePrev = true;
                this.allowSlideNext = true;
                // var $swiperMenu = $('#swiperMenu').find('li')
                $swiperMenu.removeClass('liactive')
                    .eq(this.activeIndex)
                    .addClass('liactive');

                var menuType = $swiperMenu.eq(this.activeIndex).data('menutype');
                if (menuType == 1) {
                    if (Theme_mescroll) {
                        Theme_mescroll.resetUpScroll(); //重置列表数据
                    } else {
                        setThemes() //滑动是图文触发点击
                    }
                }


                if (this.isBeginning) {
                    this.allowSlidePrev = false
                } else if (this.isEnd) {
                    this.allowSlideNext = false;
                }
            },
        },
    });
}

/****************1图文************************/
function setThemes() {
    Theme_mescroll = new MeScroll("themes", {
        down: {
            use: true,
            isBounce: false,
            noMoreSize: 4,
            clearEmptyId: "dataList",
            callback: selectMessageLIVE,

        },
        up: {
            auto: false,
            use: false,
            clearEmptyId: "dataList"

        }
    });
    var swiperStatus = false;
    var swiper = new Swiper('#origin-img', {
        zoom: true,
        zoomMax: 5,
        zoomMin: 2,
        width: window.innerWidth,
        virtual: true,
        preloadImages: false,
        pagination: {
            el: '.swiper-pagination',
            type: 'fraction',
        },
        on: {
            tap: function () {
                $('#origin-img').fadeOut();
                this.virtual.slides.length = 0;
                this.virtual.cache = [];
                swiperStatus = false;

            },
        },

    });

    $(document).delegate(".thumb .toBig", "click", function () {
        clickIndex = $(this).index();
        var imglist = $(this).parent().find("img");
        $(imglist).each(function (i, v) {
            var imgs = $(v).attr("src");
            swiper.virtual.appendSlide("<div class=\"swiper-zoom-container\"><img src=\"" + imgs + "\" /></div>");
        })
        swiper.slideTo(clickIndex);
        $('#origin-img').fadeIn();
        swiperStatus = true;
    })

}

//获取话题
var Theme_mescroll, commentsAllow, senderImg;

function selectMessageLIVE() {
    getListDataFromNet(function (curPageData) {
        Theme_mescroll.endSuccess(curPageData.length);
        setListData(curPageData);
    }, function () {
        Theme_mescroll.endErr();
    });
}

function getListDataFromNet(successCallback, errorCallback) {
    try {
        Promise.all([
            //获取话题
            createAPI("http://" + tomcatBase + "/ilive/message/selectMessageLIVE.jspx", {
                roomId: Params.roomId
            }),
            //获取话题评论
            createAPI("http://" + tomcatBase + "/ilive/comments/selectList.jspx", {
                roomId: Params.roomId,
                userId: userId
            })
        ]).then(function (arr) {
            var [selectMessage, selectList] = arr;
            //允许评论
            commentsAllow = selectMessage.commentsAllow
            senderImg = selectMessage.senderImg

            if (selectMessage.code == 1) {
                var Message = selectMessage.data;
                var iLiveCommentsMap = JSON.parse(selectList.iLiveCommentsMap);
                var praiseMap = JSON.parse(selectList.praiseMap);
                //组合
                var commentlist = restructureMSG(Message, iLiveCommentsMap, praiseMap, senderImg);
                successCallback && successCallback(commentlist);
            }
        })
    } catch (e) {
        errorCallback && errorCallback();
    }
}

/* Message 话题
 *  iLiveCommentsMap 话题评论
 *  praiseMap 题评点赞
 *  */
function restructureMSG(Message, iLiveCommentsMap, praiseMap, senderImg) {
    var TmpCommentList = [];
    var TmpComments = [];

    //评论重构
    iLiveCommentsMap.forEach(function (comments) {
        TmpComments[comments.msgId] = comments
    })
    Message.forEach(function (msg) {
        msg.comments = TmpComments[msg.msgId] || []
        msg.praiseMap = praiseMap[msg.msgId] || []
        msg.senderImg = senderImg || ''
        TmpCommentList.push(msg)
    })
    return TmpCommentList
}

//输出图文dom

var ThemePlay = [];

function setListData(commentlist) {
    // var dom = [];
    // var domTop = [];
    var str = '';
    //重新加载
    $('#themesAllTop').children().remove()
    $('#themesAll').children().remove()
    //先这样了
    commentlist.forEach(function (value) {
        str = _template.theme(value)
        if (value.top == 1) {
            // domTop.push(str)
            $('#themesAllTop').append(str) //置顶

        } else {
            //dom.push(str)
            $('#themesAll').append(str) //普通
        }
        //发布时间
        $('#msgId_' + value.msgId).find('.themeTime').attr('datetime', value.createTime);
        //设置video
        if (value.video) {
            var videoW = $('.thumb').width();
            var ThemeVideo = videojs('video_' + value.msgId, {
                height: videoW * 9 / 16,
                width: videoW,
            }, function () {
                var _this = this;
                setTimeout(function () {
                    _this.src({
                        src: value.video
                    })
                    _this.on('play', function () {
                        ThemePlay.forEach(function (value) {
                            if (_this.id_ == value.id_) return //当前视频不暂停
                            value.pause()
                        })
                        myPlayer.pause() //直播暂停
                        onceTime = true
                    })
                }, 0)

            });
            ThemePlay.push(ThemeVideo)

        }
    })
    //时间 1天前
    timeago.render(document.querySelectorAll('.themeTime'), 'zh_CN');


}

//查看更多话题评论
function selectCommentAll(obj) {
    $(obj).css('display', 'none')
        .prev().find('.pinlun').removeClass('hide')
}

//点赞留言拓展
function replyMore(obj) {
    var $replysIcon = $(obj).parents('.replyIcon').find('.replys-icon');
    //本身是否展开
    if ($replysIcon.hasClass('show')) {
        $replysIcon.toggleClass('show')
        return
    }
    $('.replys-icon').removeClass('show')
    $replysIcon.toggleClass('show')
    // $(obj).parents('.themeBox').siblings().find('.replys-icon').removeClass('show')

}

//话题点赞
/**
 *
 * @param obj
 * @param msgId
 * @param flat 已赞
 */

function saveILiveMessagePraise(obj, msgId, flat) {
    replyMore(obj)
    if (flat) return
    if (userId === 0) {
        setlogin()
        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        return false;
    }
    createAPI("http://" + tomcatBase + "/ilive/praise/savePraise.jspx", {
            userId: userId,
            msgId: msgId
        })
        .then(function (res) {
            var status = res.code;

            if (status == 401) {
                setlogin()
                //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            } else if (status == 402) {
                setlogin('bindPhone')
            } else if (status == 403) {

            }
            if (status == 1) {
                var dom = '<span class="dzName">' + nailName + '</span>';
                $('#msgId_' + msgId + '')
                    .find('.themeFooter').removeClass('hide')
                    .find('.themeFooter-top').removeClass('hide').append(dom)
                // var praiseNumber = res.praiseNumber;

                $(obj).attr('onclick', 'saveILiveMessagePraise(this,' + msgId + ', true)').find('span').text('已赞')

            } else {
                layerOpen('点赞操作失败')
            }
        })
}


/****************4详情************************/
function enterpriseList(data) {
    var isSign = data.data.isSign; //签到
    var enterprise = { //企业信息
        enterpriseId: data.data.enterprise.enterpriseId,
        enterpriseImg: data.data.enterprise.enterpriseImg,
        enterpriseName: data.data.enterprise.enterpriseName,
        enterpriseDesc: data.data.enterprise.enterpriseDesc,
        concernTotal: data.data.enterprise.concernTotal,
        certStatus: data.data.enterprise.certStatus,
        concernStatus: data.data.enterprise.concernStatus,
        isReg: data.isReg

    };
    // //标题文字
    // $('#liveTitle').text(data.data.liveEvent.liveTitle)
    // //开播时间
    // $('.viewNum').text(data.data.liveEvent.liveStartTime)
    var json = {
        liveTitle: data.data.liveEvent.liveTitle,
        liveStartTime: data.data.liveEvent.liveStartTime
    };
    _template.showDesc(json)
    //观看人数
    selectRoomNumber();
    //企业设置
    _template.zbjcompany(enterprise)
    //相关视频
    getReviewList()
    //简介
    $('#livedescdiv').html(data.data.liveEvent.liveDesc)
    //console.log(enterprise)

}

//相关视频
function getReviewList() {
    createAPI("http://" + tomcatBase + "/ilive/app/room/getrecordlist.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            if (res.code === 1 && res.data.length > 0) {
                var relatedVideo = res.relatedVideo;
                if (relatedVideo == 0) return

                var dom = _template.reviewsAll(res);
                var allreview = _template.show_all_review(res.data);
                $('#reviewsAll').css('display', 'block').html(dom);
                $('.review-List').html(allreview);
                new Swiper('#reviews', {
                    slidesPerView: 2.5,
                    spaceBetween: 5
                })

                window.showAllReview.onclick = function () {

                    popup({
                        id: 'review',
                        title: res.hkmc,
                        content: $('#review').html()
                    })

                    mescroll_review = new MeScroll("mescroll_review", {
                        down: {
                            use: false,
                        },
                        up: {
                            page: {
                                size: 15 //每页数据条数,默认10
                            },
                            auto: true,
                            isBounce: false,
                            noMoreSize: 8,
                            htmlNodata: '<p class="upwarp-nodata">-- 我是有底线的 --</p>',
                            callback: reviewCallback,
                        }
                    });

                }
            }

        })
}

var mescroll_review;
var Num = 2;

function reviewCallback(page) {
    getListReview(Num, page.size, function (curPageData) {
        mescroll_review.endSuccess(curPageData.length);
        var dom = _template.show_all_review(curPageData);
        $('.review-List').append(dom);
        $('.mescroll-hardware').prev().css('padding-bottom', 0);
    }, function () {
        review_mescroll.endErr();
    });
}


function getListReview(pageNum, pageSize, successCallback, errorCallback) {

    try {
        createAPI("http://" + tomcatBase + "/ilive/app/room/getrecordlist.jspx", {
            roomId: Params.roomId,
            pageNo: pageNum,
            pageSize: pageSize
        }).then(function (res) {
            if (res.data.length > 0) Num++;
            successCallback && successCallback(res.data);
        })
    } catch (e) {
        errorCallback && errorCallback();
    }
}


//获取直播间人数
function selectRoomNumber() {
    createAPI("http://" + tomcatBase + "/ilive/online/number.jspx", {
        roomId: Params.roomId
    }).then(function (res) {
        var status = res.code;
        if (status == 1) {
            var number = JSON.parse(res.data).number;
            if (number == undefined || number == null) {
                number = 0;
            }
            $(".onlineNumberSpan").html(setNum(number))
            setTimeout(function () {
                selectRoomNumber()
            }, 10 * 1000)
        }
    })
}

//查看是否预约
function selectAppointment() {
    if (userId != 0) {
        createAPI("http://" + tomcatBase + "/ilive/homepage/isAppointment.jspx", {
                roomId: Params.roomId,
                userId: userId
            })
            .then(function (res) {
                if (res.code == 1) {
                    if (res.data.isAppointment == 1) {
                        //已预约
                        $(".appointment").addClass('select').text("取消预约开播");
                    } else {
                        //未预约
                        $(".appointment").removeClass('select').text("预约开播通知");
                    }
                } else {
                    layerOpen('预约信息查询失败!')
                }
            })
    }

}

//检查互动活动是否开启
function isStartPrizr() {
    createAPI("http://" + tomcatBase + "/ilive/prize/isStartPrizr.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            var isStartRedPacket = res.data.isStartRedPacket, //红包
                isStartVote = res.data.isStartVote, //投票
                isStartPrizr = res.data.isStartPrizr, //抽奖
                isStartSign = res.data.isStartSign; //签到

            if (isStartSign) { //签到
                $('#qiandao').css('display', 'block')
            } else {
                $('#qiandao').css('display', 'none')
            }

            if (isStartPrizr) { //抽奖
                $("#draw").css('display', 'block')
            } else {
                $("#draw").css('display', 'none')
            }

            if (isStartVote) { //投票
                $("#toupiao").css('display', 'block')
            } else {
                $("#toupiao").css('display', 'none')
            }

            if (isStartRedPacket) { //红包
                $("#hongbao").css('display', 'block')
            } else {
                $("#hongbao").css('display', 'none')
            }

        })

}

function checkRegister(EventId) {
    createAPI("http://" + tomcatBase + "/ilive/register/queryIsRegister.jspx", {
            liveEventId: EventId,
            userId: userId,
        })
        .then(function (res) {
            if (res.code == 1) {
                var code = res.code;
                if (code == 1) {
                    var isQiandaoReg = res.data.isQiandaoReg; //签到
                    var isDianzanReg = res.data.isDianzanReg; //点赞
                    if (isQiandaoReg == 1) {
                        $('#qiandao').addClass('active');
                    }
                    if (isDianzanReg == 1) {
                        $('#dianzan').addClass('active')
                            .find('span').eq(0).text('已赞');
                    }
                } else {
                    layerOpen('获取签到、点赞失败')
                    return false
                }
            }
        })
}

//点赞数量
function roomCount(EventId) {
    createAPI("http://" + tomcatBase + "/ilive/register/RoomRegisterCount.jspx", {
            roomId: Params.roomId,
            eventId: EventId
        })
        .then(function (res) {
            if (res.code == 1) {
                dianzanNum = res.data.count
                $('#dianzan_num').text(dianzanNum)
            }
        })
    setTimeout(function () {
        roomCount(EventId)
    }, 60000)
}

//签到
var signId = 0;
window.qiandao.onclick = function () {
    $('#btnIcons').click()
    GetSignId(function () {
        popup({
            title: '签到',
            content: qiandaoHTML()
        })
    })


}

function GetSignId(callback) {
    createAPI("http://" + tomcatBase + "/ilive/prize/isSign.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            if (res.code == 1) {
                signId = res.signId
                callback()
            } else {
                layerOpen(res.message)
                return false
            }
        })
}

function qiandaoHTML() {
    html = '<div class="main-popup ">' +
        '<div class="Input">' +
        '<input type="tel" placeholder="手机号"   maxlength="11" name="phoneNum" onblur="validate(this)" id="phoneNum"/>' +
        '</div>' +
        '<div class="Input">' +
        '<input type="tel" placeholder="动态密码"  maxlength="6"  name="vPassword_Q" onblur="wchatHackInput()"/><span class="yzmBtn1" onclick="getAuthenticode(this)" id="spenTimer">获取动态密码</span>' +
        '</div>' +
        '<div class="Input">' +
        '<input type="text" placeholder="昵称 建议使用您的真实姓名签到，2-10个字"  minlength="2" maxlength="10" onblur="wchatHackInput()" name="username_Q"/>' +
        '</div>' +
        '<div class="text-center loginBox">' +
        '<button type="button" class="confirmbutton QiandaoBtn disableBtn" disabled onclick="saveQiandao()">点击立即签到</button>' +
        '</div>' +
        '</div>';
    return html

}

function validate(obj) {
    wchatHackInput()
    var phoneNumber = obj.value;
    if (phoneNumber != null && phoneNumber != "") {
        if (!isPoneAvailable(phoneNumber.trim())) {
            layerOpen('请输入正确的手机号')
            return false
        }
        createAPI("http://" + tomcatBase + "/ilive/prize/siginPhone.jspx?", {
            userPhone: phoneNumber,
            signId: signId
        }).then(function (res) {
            var status = res.status;
            if (status == 1) {
                layerOpen('您已签到')
                $('.QiandaoBtn').attr("disabled", "true").addClass('disableBtn');
                return false
            } else {
                $('.QiandaoBtn').removeAttr("disabled").removeClass('disableBtn');
            }
        })
    }

}

function saveQiandao() {
    wchatHackInput()
    var phoneNumber = $("input[name=phoneNum]").val();
    var smscode = $("input[name=vPassword_Q]").val();
    var username = $("input[name=username_Q]").val();

    if (phoneNumber == undefined || phoneNumber.replace("\s+", "") == "") {
        layerOpen('请输入手机号!')
        return false
    }
    if (smscode == undefined || smscode.replace("\s+", "") == "") {
        layerOpen('验证码不能为空!')
        return false
    }
    if (username == undefined || username.replace("\s+", "") == "") {
        layerOpen('昵称不能为空!')
        return false
    }

    createAPI("http://" + tomcatBase + "/ilive/prize/userSign.jspx", {
        userphone: phoneNumber,
        username: encodeURIComponent(username),
        vPassword: smscode,
        roomId: roomId,
        signId: signId
    }).then(function (res) {
        if (res.status == "0") {
            layerOpen(res.msg)
            return false
        }
        if (res.status == 1000) {
            layerOpen(res.msg)
            setTimeout(function () {
                layer.closeAll()
            }, 2000)

            return false
        }
        if (res.status == 1001) {
            layerOpen(res.msg)
            return false

        }
        if (res.status == 1002) {
            layerOpen(res.msg)
            return false
        }
    })
}


//点赞
function saveMediaFile() {
    createAPI("http://" + tomcatBase + "/ilive/register/registerRoom.jspx", {
            userId: userId,
            roomId: Params.roomId
        })
        .then(function (res) {
            if (res.code == 401) {
                window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                // if (confirm("是否进入登录？")) {
                //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                // } else {
                //     $.DialogBySHF.Alert({
                //         Width: 350,
                //         Height: 200,
                //         Title: "警告",
                //         Content: data.message
                //     });
                // }
            } else if (res.code == 402) {
                saveMessagerPhoneNumber();
            } else if (res.code == 403) {

            } else if (res.code == 1) {
                dianzanNum++
                $('#dianzan_num').text(dianzanNum)
                $('#dianzan').addClass('active').find('span').eq(0).text('已赞');
                // $("#dianZan").css({"background": "url(img/dianzan.png) no-repeat", "background-size": "100% 100%"});
            } else {
                layerOpen(res.message)
            }


        })
}

var hbflag = false;
//红包
$('#hongbao').on('click', function () {
    $('#btnIcons').click()
    createAPI("http://" + tomcatBase + "/ilive/prize/isRedPacket.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            if (res.code == 1) {
                var popConcen = layer.open({
                    type: 1,
                    content: hbdom(),
                    className: 'popuo-concernMsg',

                });
                $('#popclose').on('click', function () {
                    layer.close(popConcen)
                })
                openhongbao(res.packet.command)
                hbflag = true
            } else {
                layerOpen(res.message)
            }
        })

})

function hbdom() {
    return '<div class="hongbao ">\n' +
        '<canvas width="510" height="661" id="popuphongbao" style="display: none"></canvas>\n' +
        '<img src="" id="imghongbao" class="copyBtn" data-clipboard-action="copy" data-clipboard-target="#copyhongbao" onclick="">\n' +
        '<input id="copyhongbao" type="text" value="">\n' +
        '</div>\n' +
        '<div class="close" id="popclose"></div>'
}

function openhongbao(text) {

    if (hbflag) return
    var arrText = text.split(''),
        canvas = $('#popuphongbao')[0],
        cxt = canvas.getContext('2d'),
        imageBox = $('#imghongbao')[0],
        img = new Image(),
        arrText1 = [],
        arrText2 = [];
    $.each(arrText, function (i, v) {
        if (i <= 9) {
            arrText1.push(v)
        } else {
            arrText2.push(v)
        }
    })
    var stringText1 = arrText1.join('  ');
    var stringText2 = arrText2.join('  ');

    img.src = 'img/hongbaokl.png'
    img.onload = function () {
        cxt.drawImage(img, 0, 0)
        cxt.textBaseline = 'middle'
        cxt.textAlign = 'center'

        var left = canvas.width / 2;
        var top = canvas.height / 2;


        cxt.fillStyle = "#000"

        if (stringText2.length > 0) {
            cxt.font = "24px Microsoft YaHei"
            cxt.fillText(stringText1, left, top + 45)
            cxt.fillText(stringText2, left, top + 80)
        } else {
            cxt.font = "28px Microsoft YaHei"
            cxt.fillText(stringText1, left, top + 65)
        }

        imageBox.src = canvas.toDataURL("image/jpg")
        $('#copyhongbao').val(text)


    }
    var copyBtn = new ClipboardJS('.copyBtn');
    copyBtn.on('success', function (e) {
        console.info('Action:', e.action);
        console.info('Text:', e.text);
        console.info('Trigger:', e.trigger);
        layerOpen('复制成功')
        e.clearSelection();
    });
    copyBtn.on('error', function (e) {
        layerOpen('复制失败，请长按复制')
    });

}

//投票
$('#toupiao').on('click', function () {
    $('#btnIcons').click()
    if (userId == 0) {
        setlogin()
        return false
    } else {
        popup({
            title: '投票',
            content: _template.tpdom()
        })
        selectTouPiao()
    }
})
//抽奖
$('#draw').on('click', function () {
    $('#btnIcons').click()
    if (userId == 0) {
        setlogin()
        return false
        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId;
    } else {
        popup({
            title: '抽奖',
            content: _template.drawdom()
        })
        inPrize()
    }
})
//活动icon
$('#btnIcons').on('click', function () {
    $(this).toggleClass('active')
    $(this).prev().toggleClass('active ')
})

//预约和取消预约
function saveAppointment() {
    $('#saveAppointment').on('click', function () {
        if (userId == 0) {
            setlogin()
            return false
            // window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId;
        } else {
            if ($(this).hasClass('soon')) return
            createAPI("http://" + tomcatBase + "/ilive/homepage/saveAppointment.jspx", {
                    roomId: Params.roomId,
                    userId: userId
                })
                .then(function (res) {
                    if (res.code == 401) {
                        setlogin()
                        return false
                        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                    } else if (res.code == 402) {
                        setlogin('bindPhone')
                        return false
                    } else if (res.code == 403) {

                    } else if (res.code == 1) {
                        if (res.data.userid != undefined) {
                            var status = res.status;
                            if (status == 1) {
                                //已预约
                                layerOpen('<div class="title">预约成功</div><div class="content">将在直播开始前10分钟，通过短信和天翼直播APP的消息通知您观看！</div>', 3)
                                $(".appointment").addClass('select').text("取消预约开播");
                            } else if (status == 2) {
                                $(".appointment").removeClass('select').text("预约开播通知");
                                layerOpen(res.message)
                            }
                        } else {
                            //未预约
                            $(".appointment").removeClass('select').text("预约开播通知");
                            layerOpen(res.message)
                        }
                    } else {
                        layerOpen('预约失败')
                    }
                })
        }
    })
}

//评论弹层
function chatipt() {
    if (!person_estoppelType) {
        $('#chatipt').find('span').text('目前还不能讨论哦~')

    }
    $('#chatipt').on('click', function () {
        // 禁言
        if (!person_estoppelType) return
        //打开评论框
        chatiptOpen()
        $('.wen').show()
    })
    $('.talkConLeft').on('click', function () {
        // 禁言
        if (!person_estoppelType) return
        //打开评论框
        chatiptOpen()
        $('.wen').show()
    })


}

//话题评论弹层
function commentsList(obj, msgId) {
    replyMore(obj)
    $("input[name=commentsMsgId]").val(msgId);
    if (commentsAllow == 0) {
        layerOpen('评论功能已关闭')
        return
    }
    chatiptOpen('theme')

}


$(document.body).delegate('.talkConLeft', 'click', function () {
    $(".wen").attr("msgValue", "3")
        .css({
            "background": "url(img/question.png) no-repeat",
            "background-size": "100% 100%"
        });
    setTimeout(function () {
        $('.textarea').focus()

    }, 0)
})


//文档
var gallerySwiper, thumbsSwiper, def_Manual;

function selectDocument(obj) {
    var documentId = obj.documentId;
    var documentDownload = obj.documentDownload;
    var documentPageNO = obj.documentPageNO;
    var documentManual = obj.documentManual;

    def_Manual = obj.documentManual
    if (documentId == -1) {

        checkslide()
        // if (documentDownload == 0) {
        //     $(".pptBtn").addClass("hide");
        // }
        gallerySwiper.slideTo(documentPageNO - 1, 1000, false);
    } else {
        createAPI("http://" + tomcatBase + "/ilive/document/getByd.jspx", {
                docId: documentId
            })
            .then(function (res) {
                if (res.code == 1) {
                    //文档发送改变
                    if (res.data != null) {
                        var documentManager = res.data;
                        var documentPictures = documentManager.list;
                        var HTML = '<div class="Wtitle">' + documentManager.name + '</div>\n';
                        HTML += '<div class="swiper-container " id="gallery">\n';
                        HTML += '  <div class="swiper-wrapper">\n';
                        HTML += '  </div>\n';
                        HTML += '</div>\n'
                        HTML += '<div class="swiper-container " id="thumbs">\n';
                        HTML += '  <div class="swiper-wrapper">\n';
                        for (var i = 0; i < documentPictures.length; i++) {
                            var url = documentPictures[i].url;
                            HTML += '<div class="swiper-slide"><img src="' + url + '"></div>\n'
                        }
                        HTML += '  </div>';
                        HTML += '<div class="swiper-scrollbar"></div>\n'
                        HTML += '</div>\n'

                        HTML += "</div>\n";
                        // if (documentDownload == 0) {
                        //     HTML += "	<div class=\"pptBtn hide\"></div>\n";
                        // } else {
                        //     HTML += "	<div class=\"pptBtn\" style=\"top:40%\"  onclick=\"downloadMask()\"></div>\n";
                        // }
                        $(".wendang").html(HTML);
                        wendangJS(documentPictures, documentPageNO - 1);

                        if (documentDownload == 0) {
                            $(".pptBtn").addClass("hide");
                        } else {
                            $(".pptBtn").removeClass("hide");
                        }
                    }

                } else {
                    layerOpen(res.message)
                }
            })
    }
}

function wendangJS(documentPictures, realIndex) {
    thumbsSwiper = new Swiper('#thumbs', {
        spaceBetween: 10,
        slidesPerView: 3.5,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
        noSwiping: true,
        freeMode: true,
        freeModeMomentumRatio: 5
    })
    gallerySwiper = new Swiper('#gallery', {
        spaceBetween: 10,
        width: window.innerWidth,
        noSwiping: true,
        observer: true, //修改swiper自己或子元素时，自动初始化swiper
        observeParents: true, //修改swiper的父元素时，自动初始化swiper
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
        thumbs: {
            swiper: thumbsSwiper,
        },
        scrollbar: {
            el: '.swiper-scrollbar',
            draggable: true,
        },
    })
    gallerySwiper.slideTo(realIndex, 1000, false);
    checkslide()
}

function checkslide() {
    swiperStatus = def_Manual
    if (!def_Manual) { //不允许手动

        gallerySwiper.$wrapperEl.addClass('swiper-no-swiping')
        thumbsSwiper.$wrapperEl.addClass('swiper-no-swiping')

        $('.swiper-scrollbar').addClass('hide')

    } else {
        gallerySwiper.$wrapperEl.removeClass('swiper-no-swiping')
        thumbsSwiper.$wrapperEl.removeClass('swiper-no-swiping')
        $('.swiper-scrollbar').removeClass('hide')
    }
}

//引导页
function setguides() {
    createAPI("http://" + tomcatBase + "/ilive/appuser/getGuideUrl.jspx", {
            roomId: Params.roomId
        })
        .then(function (res) {
            var imgUrl = res.data.imgUrl || 'img/defultBg.jpg';
            _template.guide()
            $('.guide').css({
                'display': 'block',
                'backgroundImage': 'url(' + imgUrl + ')'
            })
            var times = 5;
            var t = setInterval(function () {
                $('.guide-num').text(times)
                if (1 === times) {
                    $(".guide").fadeOut()
                    clearInterval(t)
                }
                --times
            }, 1000);
            $('.guide-box').on('click', function () {
                $(".guide").fadeOut()
                clearInterval(t)
                // $('.page').css('display', 'block')
            })
        })


}

// var setguides = {
//     a:function () {
//
//     }
//     // init: function () {
//     //     // createAPI("http://" + tomcatBase + "/ilive/appuser/getGuideUrl.jspx", {roomId: Params.roomId})
//     //     //     .then(function (res) {
//     //     //         var imgUrl = res.data.imgUrl;
//     //     //         if (imgUrl != "") {
//     //     //             $('.guide').css({
//     //     //                 'display': 'block',
//     //     //                 'backgroundImage': 'url(' + imgUrl + ')'
//     //     //             })
//     //     //         }
//     //     //         //setguide.countDown(5)
//     //     //         var times = 5;
//     //     //         // var timer = setInterval(function () {
//     //     //         //     $('.guide-num').text(times)
//     //     //         //     if (1 === times){
//     //     //         //         setguide.hide()
//     //     //         //         clearInterval(timer)
//     //     //         //     }
//     //     //         //     --times
//     //     //         // },1000)
//     //     //
//     //     //
//     //     //     })
//     //     // $('.guide-box').on('click',function () {
//     //     //     setguides.hide()
//     //     // })
//     // },
//     // hide: function () {
//     //     $(".guide").fadeOut()
//     //     $('.page').css('display', 'block')
//     // }
//     // countDown: function (times) {
//     //     if (!times || isNaN(parseInt(times))) return;
//     //     var arg = arguments;
//     //     var self = this;
//     //     $('.guide-num').text(times)
//     //     if (1 === times) self.hide()
//     //     setTimeout(function () {
//     //         arg.callee.call(self, --times)
//     //     }, 1000);
//     //
//     // }
// }


// document.addEventListener('touchmove', function (e) {
//     if (swiperStatus) {
//         e.preventDefault();
//     }
// });


window.onbeforeunload = function () {
    if (ws) {
        ws.onclose();
    }
}
window.onunload = function (event) {
    if (ws) {
        ws.onclose();
    }
}