//页面操作============================================================
var liveStart = 0;
var mescroll;
var countdownTitle = null;
var minRewardsAmount = 0;
var maxRewardsAmount = 0;
var roomName = '';
var liveStartTime;
var hlsAddr;
//var video = document.getElementById('video');
var myPlayer
var swiperStatus;
var layerId;
var danmuSwitch

$(function () {
    var roomId = GetQueryString("roomId");

    if (!/Android|webOS|iPhone|iPod|iPad|BlackBerry/i.test(navigator.userAgent)) {
        window.location.href = "http://" + h5Base + "/pc/play.html?roomId=" + roomId;//PC
    }

    if (roomId == undefined || roomId == 'null') {
        roomId = 0;
    }
    $("input[name=roomId]").val(roomId);
    var userId = GetQueryString("userId");
    if (userId == undefined || userId == 'null') {
        userId = 0;
    }
    $("input[name=userId]").val(userId);


    if (roomId == 0) {
        errorfunction()
    } else {
        onResize()

        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/selectPageDecorate.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                roomId: roomId
            },
            success: function (data) {
                var code = data.code;
                if (code == 1) {
                    var menuBoxDIV = "";
                    var contentBoxDIV = "";
                    var pageDecorates = JSON.parse(data.pageDecorates);
                    var ret = false;
                    for (var i = 0; i < pageDecorates.length; i++) {
                        var Zindex = i
                        var pageDecorate = pageDecorates[i];
                        var liveMsgType = pageDecorate.menuType;
                        var menuName = pageDecorate.menuName;
                        /**
                         * menuType
                         * 1话题
                         * 2聊天互动
                         * 3在线问答
                         * 4详情
                         * 5往期回看
                         * 6文档
                         */
                        switch (liveMsgType) {
                            case 1:
                                menuBoxDIV += '<li ';
                                if (i == 0) {
                                    menuBoxDIV += ' class="liactive"';
                                    $(".wen").attr("msgValue", liveMsgType);
                                    $("textarea[name=msgContent]").attr("maxlength", "40");
                                    $("#sendMessageDIV").addClass('hide');
                                    //$(".yuyueBox ").css("position", "none");
                                }
                                menuBoxDIV += " liveMsgType=\"" + liveMsgType + "\"><span>" + menuName + "</span><i class=\"redDot\" style=\"display:none;\"></i></li>";
                                contentBoxDIV += "<li class=\"theme\" style='z-index:" + Zindex + "'>";
                                contentBoxDIV += "	<div id=\"mescroll\" class=\"mescroll\">";
                                contentBoxDIV += "		<div class=\"hintBox hide\" id=\"newTopic\">有一条新的内容，下拉刷新观看</div>";
                                contentBoxDIV += "		<div class=\"nomoreIcon hide\" id=\"topicNull\">";
                                contentBoxDIV += "		   <div class=\"nothemeBox\"></div>";
                                contentBoxDIV += "		   <p class=\"nothemeText\">暂无话题发布...</p >";
                                contentBoxDIV += "		</div>";
                                contentBoxDIV += "		<div id=\"topTalkBoxList1\"></div>";
                                contentBoxDIV += "		<div class=\"themeCon\" id=\"themeDiv\" ></div>";
                                contentBoxDIV += "	</div>";
                                contentBoxDIV += "</li>";
                                ret = true;
                                break
                            case 2:
                                menuBoxDIV += " <li ";
                                if (i == 0) {
                                    menuBoxDIV += "class=\"liactive\"";
                                    $(".wen").attr("msgValue", liveMsgType);
                                    $("textarea[name=msgContent]").attr("maxlength", "100");
                                    $("#sendMessageDIV").removeClass('hide');
                                    //$(".yuyueBox ").css("display", "none");
                                }
                                menuBoxDIV += " liveMsgType=\"" + liveMsgType + "\"><span>" + menuName + "</span></li>";
                                contentBoxDIV += "<li class=\"talkLi\" id=\"talkLi\" style='z-index:" + Zindex + "'>";
                                contentBoxDIV += "	<div id=\"topTalkBoxList2\"></div>";
                                contentBoxDIV += "	<div id=\"talkBoxList2\"></div>";
                                contentBoxDIV += "	<div class='dixian' style='display: none'><span>我是有底线的</span></div>";
                                contentBoxDIV += "</li>";
                                break
                            case 3:
                                break
                            case 4:
                                menuBoxDIV += " <li ";
                                if (i == 0) {
                                    menuBoxDIV += "class=\"liactive\"";
                                    $(".wen").attr("msgValue", liveMsgType);
                                    $("#sendMessageDIV").addClass('hide');
                                    // $(".yuyueBox").css("display", "block");
                                }
                                menuBoxDIV += " liveMsgType=\"" + liveMsgType + "\"><span>" + menuName + "</span></li>";
                                contentBoxDIV += "<li class=\"zbjj\" id=\"enterpriseList\" style='z-index:" + Zindex + ";'></li>";
                                break
                            case 5:
                                menuBoxDIV += " <li ";
                                if (i == 0) {
                                    menuBoxDIV += "class=\"liactive\"";
                                    $(".wen").attr("msgValue", liveMsgType);
                                    $("#sendMessageDIV").addClass('hide');
                                    //$(".yuyueBox").css("display", "none");
                                }
                                menuBoxDIV += " liveMsgType=\"" + liveMsgType + "\"><span>" + menuName + "</span></li>";
                                contentBoxDIV += "<li class=\"review\" id=\"review\" style='z-index:" + Zindex + ";'></li>";
                                break
                            case 6:
                                menuBoxDIV += " <li ";
                                if (i == 0) {
                                    menuBoxDIV += "class=\"liactive\"";
                                    $(".wen").attr("msgValue", liveMsgType);
                                    $("#sendMessageDIV").addClass('hide');
                                    // $(".yuyueBox").css("display", "none");
                                }
                                menuBoxDIV += " liveMsgType=\"" + liveMsgType + "\"><span>" + menuName + "</span></li>";
                                contentBoxDIV += "<li class=\"wendang\" liveMsgType=\"" + liveMsgType + "\" style='z-index:" + Zindex + ";'>";
                                contentBoxDIV += "</li>";
                                break

                        }

                    }
                    $("#menuBoxDIV").append(menuBoxDIV);
                    $("#contentBoxDIV").prepend(contentBoxDIV);
                    selectChoujiang(); //抽奖投票

                    var tableNumber = pageDecorates.length;
                    var getBox = function (className) {
                        return document.querySelector("." + className)
                    }
                    var tabNumber = tableNumber * 100;
                    var minwidth = tabNumber - 200;
                    $(".contentBox ul").css("width", tabNumber + "%");


                    var firstBox = getBox("firstBox");
                    var liList = $(".menuBox li");
                    var contentBox = document.querySelector(".contentBox");
                    var startX, disX;
                    var ul = document.querySelector(".contentBox ul");
                    ul.style.left = "0%";
                    if (ret) {
                        mescroll = new MeScroll("mescroll", {
                            down: {
                                hardwareClass: "mescroll-hardware",
                                callback: selectMessageLIVE
                            },
                            up: {
                                use: false,
                            }
                        });
                    }
                    for (var i = 0; i < liList.length; i++) {
                        liList[i].addEventListener("touchend", function (event, n) {
                            return function () {
                                menuController(n)
                                ul.style.left = "-" + n * 100 + "%";
                            }
                        }(event, i))
                    }
                    contentBox.addEventListener("touchstart", function (event) {
                        var touche = event.touches[0];
                        startX = touche.clientX;
                        event.stopPropagation()
                    })
                    contentBox.addEventListener("touchend", function (event) {
                        if (swiperStatus) return
                        var touche = event.changedTouches[0];
                        disX = touche.clientX - startX;
                        var leftNum = parseInt(ul.style.left);
                        if (disX > 0 && disX > 100) {
                            if (leftNum <= -100) {
                                ul.style.left = leftNum + 100 + "%";
                                var left = (~parseInt(ul.style.left) + 1) / 100;
                                menuController(left)
                            }
                        } else if (disX < 0 && disX < -100) {
                            if (leftNum >= -minwidth) {
                                ul.style.left = leftNum - 100 + "%";
                                var left = (~parseInt(ul.style.left) + 1) / 100;
                                menuController(left)
                            }
                        }
                    })

                    contentBox.addEventListener("touchmove", function (event) {
                        event.stopPropagation()
                    });

                    function menuController(order) {
                        swiperStatus = false;
                        for (var i = 0; i < liList.length; i++) {
                            liList[i].classList.remove("liactive");
                        }
                        liList[order].classList.add("liactive");

                        hideMask();
                        var liveMsgType = $(".liactive").attr("liveMsgType");
                        $('#liveStart').addClass('hide')
                        $("#sendMessageDIV").addClass('hide');
                        // $(".wendangBottom").addClass('hide');

                        // if (liveMsgType == 1) {
                        //
                        // }
                        if (liveMsgType == 2) {
                            $("#sendMessageDIV").removeClass('hide');
                        }
                        if (liveMsgType == 4) {
                            if (liveStart == 0) {
                                $('#liveStart').removeClass('hide') //开启预约按钮
                            }

                        }
                        // //文档直播禁止滑动
                        if (liveMsgType == 6) {
                            // $(".wendangBottom").removeClass('hide');
                            checkslide()
                        }
                        $(".wen").attr("msgValue", liveMsgType).css({
                            background: "url(\"img/graywen.png\") no-repeat",
                            backgroundSize: "100%"
                        });

                    }

                    // var winHeight = $(window).height();
                    // var VideoBox = $(".videoBox").height();
                    // var menuBox = $(".menuBox").height();
                    // var talkCon = $(".talkCon").height();
                    //$(".contentBox").css("height", winHeight - VideoBox - menuBox);
                    //$(".contentBox ul li").css("height", winHeight - VideoBox - menuBox);
                    //$(".talkLi").css("height", winHeight - VideoBox - menuBox - talkCon);
                    //图片点击放大
                    $(".imgBox").lightGallery({
                        loop: false,
                        auto: false,
                        closable: true
                    });
                    //var roomId = $("input[name=roomId]").val();
                    $.ajax({
                        type: "GET",//请求方式 get/post
                        url: "http://" + tomcatBase + "/ilive/app/room/checkRoomLogin.jspx",
                        dataType: "jsonp",
                        jsonp: "callback",
                        cache: false,
                        data: {
                            terminalType: "h5",
                            roomId: roomId
                        },
                        success: function (data) {
                            var status = data.code;
                            var userInfo
                            if (status == 404) {
                                window.location.href = "http://" + h5Base + "/phone/end.html";
                            } else if (status == 402) { //无手机号
                                userInfo = JSON.parse(data.data).userInfo;
                                if (userInfo != undefined || userInfo != null) {
                                    var userId = userInfo.userId;
                                    if (userId != undefined || userId != null) {
                                        $("input[name=userId]").val(userId);
                                    }
                                    window.location.href = "http://" + h5Base + "/phone/phoneNumber.html?roomId=" + roomId + "&userId=" + userId;
                                } else {
                                    layer.open({
                                        content: '绑定手机号，获取用户失败'
                                        , btn: '确定'
                                        , yes: function (index) {
                                            errorfunction()
                                        }
                                    });
                                }
                            } else if (status == 1) {
                                var datas = JSON.parse(data.data);
                                var repeateGuide = datas.repeateGuide;
                                //session 房间名字
                                window.sessionStorage.setItem('roomName', datas.roomTitle || '天翼直播平台')

                                if (repeateGuide == 0) {
                                    var openGuideSwitch = datas.openGuideSwitch;
                                    if (openGuideSwitch == 1) {

                                        window.location.replace('http://' + h5Base + '/phone/guide.html?roomId=' + roomId )
                                        //window.location.href = datas.guideAddr;
                                        return;
                                    }
                                }
                                /**  roomNeedLogin
                                 *   1.需要登录
                                 *   2不需要登录.
                                 */

                                var roomNeedLogin = datas.roomNeedLogin;
                                if (roomNeedLogin == 1) {
                                    //alert("进入登录页：    roomNeedLogin="+roomNeedLogin);
                                    window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId ;
                                    return;
                                } else if (roomNeedLogin == 2) {
                                    userInfo = datas.userInfo;
                                    if (userInfo != undefined || userInfo != null) {
                                        var userId = userInfo.userId;
                                        if (userId != undefined || userId != null) {
                                            $("input[name=userId]").val(userId);
                                        }
                                        var plinfo ={
                                            'userphone' :userInfo.mobile,
                                        }
                                        window.sessionStorage.setItem('plinfo',JSON.stringify(plinfo))
                                    }
                                    $.ajax({
                                        type: "GET",//请求方式 get/post
                                        url: "http://" + tomcatBase + "/ilive/app/room/getRoomInfo.jspx",
                                        dataType: "jsonp",
                                        jsonp: "callback",
                                        cache: false,
                                        data: {
                                            terminalType: "h5",
                                            roomId: roomId
                                        },
                                        success: function (data) {

                                            var status = data.code;
                                            if (status == 1) {
                                                var liveEvent = data.data.liveEvent;
                                                var liveEventId = liveEvent.liveEventId;
                                                $("input[name=liveEventId]").val(liveEventId);
                                                if (liveEvent == null || liveEvent == undefined) {
                                                    layer.open({
                                                        content: '直播间信息错误'
                                                        , btn: '确定'
                                                        , yes: function (index) {
                                                            errorfunction()
                                                        }
                                                    });
                                                    return false;
                                                }
                                                var liveStatus = liveEvent.liveStatus;
                                                // var playBgAddr = liveEvent.playBgAddr;
                                                roomName = liveEvent.liveTitle || "";
                                                document.title = roomName;
                                                liveStartTime = liveEvent.liveStartTime;
                                                if (liveStartTime == null || liveStartTime == undefined) {
                                                    liveStartTime = "";
                                                }
                                                //$("#videoImg").html("<img style=\"position: relative;z-index: 99;\"  src=\"" + playBgAddr + "\" alt=\"\"/>");
                                                // $("#endLive").addClass("hide");
                                                // $(".videoBtn").addClass("hide");
                                                // $(".videoPlay").addClass("hide");
                                                //$("#videoImg").css("background-image", "url(" + playBgAddr + ")").addClass('hide')
                                                $("#playBg").css("background-image", "url(" + liveEvent.converAddr + ")")
                                                $(".marqueeBox").css('bottom', '0');
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
                                                myPlayer = videojs('video', {
                                                    //bigPlayButton : false,
                                                    textTrackDisplay : false,
                                                    errorDisplay : false,
                                                    // controlBar: {
                                                    //     fullscreenToggle: false
                                                    // }
                                                })
                                                $('.vjs-big-play-button').addClass("hide");
                                                // $(video).append($('#canvas'))
                                                // $(video).append($('#mui-switch'))
                                                switch (liveStatus) {
                                                    case 0: //未直播
                                                        liveStart = 0;
                                                        //未开始直播
                                                        var openCountdownSwitch = liveEvent.openCountdownSwitch;
                                                        // $("#videoImg").html("<img style=\"position: relative;z-index: 99;\"  src=\"" + playBgAddr + "\" alt=\"\"/>");
                                                        // $("#endLive").addClass("hide");
                                                        // $(".videoBtn").addClass("hide");
                                                        // $(".videoPlay").addClass("hide");
                                                        if (openCountdownSwitch == 1) { //倒计时
                                                            countdownTitle = liveEvent.countdownTitle;
                                                            $("#customContent").text(countdownTitle);
                                                            $(".videoPlay").removeClass('hide')
                                                            $("#timeStart").removeClass("hide");
                                                            var ts = date.getTime();
                                                            //var newYear = true;
                                                            $('#countdown').countdown({
                                                                timestamp: ts,
                                                                callback: function (days, hours, minutes, seconds) {
                                                                }
                                                            });
                                                            //$(".no-timeing .timeone").css("margin-left", -$(".no-timeing .timeone").outerWidth() / 2);
                                                        } else {
                                                            // $("#videoImg").html("<img style=\"position: relative;z-index: 99;\"  src=\"" + playBgAddr + "\" alt=\"\"/>");
                                                            $("#endLive").removeClass("hide").find(".textMask").text('本场直播尚未开始');
                                                            //$("#endLive").addClass("hide");
                                                            // $(".videoBtn").addClass("hide");
                                                            // $(".videoPlay").hide();
                                                            //$(".no-strat .textMask").css("margin-left",-$(".no-strat .textMask").outerWidth()/2);
                                                        }
                                                        break;
                                                    case 1:
                                                        //开始直播
                                                        //$(".videoPlay").removeClass('hide')
                                                        $("#playBg").addClass('hide')
                                                        liveStart = 1;
                                                        // var newDate = new Date();   // 获取当前时间
                                                        // if (newDate.getTime() - date.getTime() > 13000) {
                                                        //     liveStart = 1;
                                                        // } else {
                                                        //     liveStart = 2;
                                                        // }
                                                        break;
                                                    case 2:
                                                        liveStart = 3
                                                        //暂停
                                                        $("#endLive").removeClass("hide").find(".textMask").text("当前暂无直播画面");
                                                        $("#playBg").addClass('hide')

                                                        break;
                                                    case 3:
                                                        liveStart = 4
                                                        //$("#stratLive").addClass("hide").find("#imageComtent").text("直播结束");
                                                        $("#endLive").removeClass("hide").find(".textMask").text("本场直播已结束");
                                                        //$(".videoPlay").removeClass("hide");
                                                        //$('.videoPlay').find("i").addClass("hide")
                                                        //直播结束

                                                        break;
                                                    default:
                                                    //liveStart = 0;
                                                }
                                                var pageDecorates = liveEvent.pageDecorate;
                                                if (pageDecorates == null || pageDecorates == undefined) {
                                                    layer.open({
                                                        content: '直播间错误'
                                                        , btn: '确定'
                                                        , yes: function (index) {
                                                            errorfunction()
                                                        }
                                                    });
                                                    return false;
                                                }
                                                var zbjj = 0, zbhk = 0, htzb = 0;
                                                /**
                                                 * menuType
                                                 * 1话题
                                                 * 2聊天互动
                                                 * 3在线问答
                                                 * 4详情
                                                 * 5往期回看
                                                 * 6文档
                                                 */
                                                for (var i = 0; i < pageDecorates.length; i++) {
                                                    var pageDecorate = pageDecorates[i];
                                                    if (pageDecorate.menuType == 4) {
                                                        zbjj = 1; //详情
                                                    } else if (pageDecorate.menuType == 5) {
                                                        zbhk = 1; //往期回看
                                                    } else if (pageDecorate.menuType == 1) {
                                                        htzb = 1; //话题
                                                    }
                                                }
                                                if (zbjj != 0) {
                                                    //var liveEventId = liveEvent.liveEventId;
                                                    var userId = $("input[name=userId]").val();
                                                    var enterprise = data.data.enterprise;
                                                    var isSign = liveEvent.isSign;
                                                    if (enterprise != null || enterprise != undefined) {
                                                        var enterpriseId = enterprise.enterpriseId;
                                                        var enterpriseImg = enterprise.enterpriseImg;
                                                        var enterpriseName = enterprise.enterpriseName;
                                                        var enterpriseDesc = enterprise.enterpriseDesc;
                                                        var applyTime = enterprise.applyTime;
                                                        var cbCount = 0
                                                        /**caibin**/
                                                        if (enterpriseId == 1969){ //李曰
                                                            cbCount = 15000
                                                        }
                                                        /**caibin**/
                                                        var concernTotal = cbCount + enterprise.concernTotal;//粉丝数
                                                        concernTotalTemp = concernTotal;
                                                        if (concernTotal > 9999) {
                                                            concernTotal = (concernTotal / 10000).toFixed(1) / 1 + 'w'
                                                        }

//																var desc = "";
//																if(enterpriseDesc.length>20){
//																	desc = enterpriseDesc.substring(0,20)+"...";
//																}else{
//																	desc = enterpriseDesc;
//																};
                                                        var enterpriseList = '<div class="zbjjHeader" id="enterpriseId_' + enterpriseId + '">';
                                                        enterpriseList += '<div class="zbjjLeft">';
                                                        enterpriseList += '		<div class="liveTitle"  title="' + roomName + '">' + roomName + '</div>';
                                                        // enterpriseList += "		<div class=\"small-liveTitle\">" + enterpriseName + "</div>";
                                                        enterpriseList += "		<div class=\"small-liveTitle \"><i class='iconfont icon-shijian'></i>" + liveStartTime.slice(0, liveStartTime.length - 3) + "</div>";
                                                        enterpriseList += "	</div>";
                                                        /**
                                                         * 企业认证状态 0未提交认证 1认证中 4认证通过 5认证失败
                                                         */
                                                        enterpriseList += "	<div class=\"zbjjHeaderCon\">";
                                                        enterpriseList += "		<div class=\"peopleNum\">";
                                                        enterpriseList += "			<i class=\"peopleIcon iconfont icon-mi\"><span class=\"onlineNumberSpan\">0</span></i>";
                                                        enterpriseList += "		</div>";
                                                        enterpriseList += "		<div class=\"shareCon\">";
//																	enterpriseList +="			<i class=\"shareIcon pull-left\" title=\"分享\"></i>";
                                                        enterpriseList += '<i id="dianZan" class="collIcon iconfont icon-dianzan" onclick="saveDianZan(' + roomId + ')" title="点赞"><span class="onlineDianZan">55.5w</span></i>';
                                                        // if (isSign == 1) {
                                                        enterpriseList += '<i id="qiandao" class="signIcon iconfont icon-qiandao hide" onclick="OpenQianDao()" title="签到"><span class="onlineQianDao">0</span></i>';
                                                        // }
                                                        //enterpriseList += ('<i id="signIn" class="signIcon iconfont icon-qiandao" title="签到"><span class="onlineQianDao">0</span></i>')
                                                        enterpriseList += "		</div>";
                                                        enterpriseList += "	</div>";
                                                        enterpriseList += "</div>";
                                                        // enterpriseList += "<div class=\"zbjjline\"></div>";
                                                        var certStatus = enterprise.certStatus;
                                                        var guanzhu = {
                                                            cerrs: false,
                                                            onclick: "",
                                                            type: 1,
                                                            className: 'guanzhu icon-jia',
                                                            icon: ''
                                                        }
                                                        if (certStatus == 4) {
                                                            guanzhu.cerrs = true;
                                                            guanzhu.onclick = 'onclick="zbjcompany(' + enterpriseId + ')"';
                                                            var concernStatus = enterprise.concernStatus;//是否关注
                                                            if (concernStatus != 0) {
                                                                guanzhu.type = 0;
                                                                guanzhu.className = 'yiguanzhu';
                                                                guanzhu.icon = 'icon-youjiantou'
                                                            }
                                                        }
                                                        enterpriseList += '<div class="zbjcompany"  ' + guanzhu.onclick + '  id="zbjcompany">';
                                                        enterpriseList += '<i class="iconfont ' + guanzhu.icon + '"></i>';
                                                        enterpriseList += '<div class="companyimg"></div>';
                                                        enterpriseList += '<div class="company">';
                                                        enterpriseList += '<div class="small-liveTitle">';
                                                        enterpriseList += '<span>' + enterpriseName + '</span>';
                                                        /**
                                                         * 企业认证状态 0未提交认证 1认证中 4认证通过 5认证失败
                                                         */

                                                        if (guanzhu.cerrs) {
                                                            enterpriseList += '<span class=" iconfont btn ' + guanzhu.className + ' " id="concernStatus" onclick="updateConcernStatus(' + enterpriseId + ',' + guanzhu.type + ',event)"></span>'
                                                            enterpriseList += '<span class="zhuye btn iconfont icon-jia1"></span>'
                                                        }
                                                        enterpriseList += '</div>';
                                                        enterpriseList += '<div class="small-fans" id="concernTotal">粉丝数: <span class="smallText">' + concernTotal + '</span> 人</div>';
                                                        enterpriseList += '</div>';
                                                        enterpriseList += '</div>';

                                                        enterpriseList += "<div class=\"zbjjMain\">";
                                                        enterpriseList += "	<div class=\"zbjjList\">";
                                                        enterpriseList += "		<div class=\"ListConTit\">内容介绍</div>";
                                                        enterpriseList += "		<div id=\"liveDesc_enterpriseId\" class=\"ListCon\"></div>";
                                                        enterpriseList += "	</div>";
                                                        enterpriseList += "</div>";
                                                        //$("#enterpriseList").html(enterpriseList);
                                                        enterpriseList += '<div class="yuyueBox hide" id="liveStart">';
                                                        enterpriseList += ' <button class="yuyue appointment" onclick="saveAppointment()">直播开始提醒</button>';
                                                        enterpriseList += '</div>'
                                                    } else {
                                                        var enterpriseList = "<div class=\"zbjjHeader\" id=\"enterpriseId_" + enterpriseId + "\">";
                                                        enterpriseList += "	<div class=\"zbjjHeaderCon\">";
                                                        enterpriseList += "		<div class=\"peopleNum\">";
                                                        enterpriseList += "			<i class=\"peopleIcon iconfont icon-mi\"><span class=\"onlineNumberSpan\">0</span></i>";
                                                        enterpriseList += "		</div>";
                                                        enterpriseList += "		<div class=\"shareCon\">";
//																	enterpriseList +="			<i class=\"shareIcon pull-left\" title=\"分享\"></i>";

                                                        enterpriseList += '<i id="dianZan" class="collIcon iconfont icon-dianzan" onclick="saveDianZan(' + roomId + ')" title="点赞"><span class="onlineDianZan">55.5w</span></i>';
                                                        // if (isSign == 1) {
                                                        enterpriseList += '<i id="qiandao" class="signIcon iconfont icon-qiandao hide" onclick="OpenQianDao()" title="签到"><span class="onlineQianDao">0</span></i>';
                                                        // }
                                                        //enterpriseList += ('<i id="signIn" class="signIcon iconfont icon-qiandao" title="签到"><span class="onlineQianDao">0</span></i>')
                                                        enterpriseList += "		</div>";
                                                        enterpriseList += "	</div>";
                                                        enterpriseList += "</div>";
                                                        enterpriseList += "<div class=\"zbjjMain\">";
                                                        enterpriseList += "	<div class=\"zbjjList\">";
                                                        enterpriseList += "		<div class=\"ListConTit\">内容介绍</div>";
                                                        enterpriseList += "		<div id=\"liveDesc_enterpriseId\" class=\"ListCon\"></div>";
                                                        enterpriseList += "	</div>";
                                                        enterpriseList += "</div>";
                                                        // $("#enterpriseList").html(enterpriseList);
                                                    }
                                                    $("#enterpriseList").html(enterpriseList);
                                                    var liveDesc = liveEvent.liveDesc;
                                                    $("#liveDesc_enterpriseId").html(liveDesc);
                                                    selectRoomNumber();
                                                    getEnterprise(enterpriseId)
                                                    window.setInterval("selectRoomNumber()", 60000);
                                                    $.ajax({
                                                        type: "GET",//请求方式 get/post
                                                        url: "http://" + tomcatBase + "/ilive/register/queryIsRegister.jspx", //是否签到点赞
                                                        dataType: "jsonp",
                                                        jsonp: "callback",
                                                        cache: false,
                                                        data: {
                                                            terminalType: "h5",
                                                            liveEventId: liveEventId,
                                                            userId: userId,
                                                        },
                                                        success: function (data) {
                                                            var code = data.code;
                                                            if (code == 1) {
                                                                var isQiandaoReg = data.data.isQiandaoReg;//签到
                                                                var isDianzanReg = data.data.isDianzanReg;//点赞
                                                                if (isQiandaoReg == 1) {
                                                                    $('#qiandao').addClass('checked').removeAttr("onclick");
                                                                }
                                                                if (isDianzanReg == 1) {
                                                                    $('#dianZan').addClass('checked').removeAttr("onclick");
                                                                }
                                                            } else {
                                                                layerOpen('获取签到、点赞失败')
                                                                return false
                                                            }
                                                        }
                                                    });

                                                }

                                                // var liveEventId = liveEvent.liveEventId;
                                                // $("input[name=liveEventId]").val(liveEventId);
                                                var openSignupSwitch = liveEvent.openSignupSwitch;
                                                if (openSignupSwitch == 1) {
                                                    var diyformAddr = liveEvent.diyformAddr;
                                                    window.location.href = "http://" + h5Base + "/phone/form.html?roomId=" + roomId;
                                                    return;
                                                }
                                                var viewAuthorized = liveEvent.viewAuthorized;
                                                if (viewAuthorized == 2) {
                                                    window.location.href = "http://" + h5Base + "/phone/password.html?roomId=" + roomId;
                                                    return;
                                                }else if(viewAuthorized == 6){
                                                	 window.location.href = "http://" + h5Base + "/phone/FCode.html?roomId=" + roomId;
                                                     return;
                                                } else if (viewAuthorized == 3) {
                                                    //付费观看
                                                    var viewMoney = liveEvent.viewMoney;
                                                    if (viewMoney == undefined || viewMoney == "" || viewMoney == null) {
                                                        viewMoney = 0.01;
                                                    }
                                                    var totalAmount = parseFloat(viewMoney) * 100;
                                                    var id = $("input[name=userId]").val();
                                                    var liveTitle = liveEvent.liveTitle || "";
                                                    var welcomeMsg = liveEvent.welcomeMsg;
                                                    $(".ewmTit").text(welcomeMsg);
                                                    $("#ewmMoneyDiv").text("￥" + viewMoney);
                                                    $("#checkbox-1-1").attr("wechatSweepUnifyTheOrder", "wechatSweepUnifyTheOrder(3,'" + liveTitle + "'," + totalAmount + ",0)");
                                                    $("#submitBtnMoney").attr("onclick", "wechatSweepUnifyTheOrder(3,'" + liveTitle + "'," + totalAmount + ",0)");
                                                    $("#ewmPayAgreement").attr("href", "http://" + h5Base + "/phone/agree.html?roomId=" + roomId + "&userId=" + id);
                                                    var openFCodeSwitch = liveEvent.openFCodeSwitch;
                                                    if (openFCodeSwitch == 1) {
                                                        $("#ewmCode").removeClass("hide");
                                                    } else {
                                                        $("#ewmCode").addClass("hide");
                                                    }
                                                    $("#payDiv").removeClass("hide");
                                                } else if (viewAuthorized == 1) {
                                                    tableContent(zbhk, roomId);
                                                } else {
                                                    console.log("观看权限管理值为：viewAuthorized：" + viewAuthorized);
                                                    tableContent(zbhk, roomId);
                                                }
                                                var documentId = liveEvent.documentId;
                                                if (documentId != 0) {
                                                    var documentDownload = liveEvent.documentDownload;
                                                    var documentPageNO = liveEvent.documentPageNO;
                                                    var documentManual = liveEvent.documentManual;
                                                    var enterpriseId = enterprise.enterpriseId;
                                                    selectDocument(documentId, documentDownload, documentPageNO, documentManual);
                                                    selectDocumentList(enterpriseId, documentManual);
                                                }
                                            } else {
                                                window.location.href = "http://" + h5Base + "/phone/nopermission.html?roomId=" + roomId;
                                            }
                                        }
                                    });
                                }
                            } else {
                                layerOpen(data.message)
                                return false
                            }
                        }
                    });

                }

            }
        });

    }
});
/*弹幕*/
function danmu(obj) {
    var _this = $(obj)
    if (_this.hasClass("active")) {
        danmuSwitch = false //关闭
        $('canvas').barrager("clear"); //清除/关闭弹幕
        $('canvas').addClass('hide');
        _this.removeClass("active");
        _this.find(".mui-switch-handle").css({"transition-duration": "0.2s", "transform": "translate(0px, 0px)"})
    } else {
        danmuSwitch = true
        $('canvas').removeClass('hide');
        _this.addClass("active");
        _this.find(".mui-switch-handle").css({"transition-duration": "0.2s", "transform": "translate(14px, 0px)"})
    }
}

//打赏准备
function saveUserPlayRewards() {
    var playRewardsAmount = $("input[name=playRewardsAmount]").val();
    if (playRewardsAmount == null || playRewardsAmount == "" || playRewardsAmount == undefined) {
        layerOpen('打赏金额不能为空')
    } else {
        if (parseFloat(playRewardsAmount) < parseFloat(minRewardsAmount) || parseFloat(playRewardsAmount) > parseFloat(maxRewardsAmount)) {
            layerOpen('打赏金额超出打赏范围')
        } else {
            //orderType: 1礼物  2打赏       productDesc:描述     totalAmount：金额（分）
            var totalAmount = parseFloat(playRewardsAmount) * 100;
            var productDesc = $("title").text();
            wechatSweepUnifyTheOrder(2, productDesc, totalAmount, 0);
        }
    }
}

//打赏
function saveUserPlayRewardsPay() {
    var playRewardsAmount = $("input[name=playRewardsAmount]").val();
    if (playRewardsAmount == null || playRewardsAmount == "" || playRewardsAmount == undefined) {
        layerOpen('打赏金额不能为空')
    } else {
        if (parseFloat(playRewardsAmount) < parseFloat(minRewardsAmount) || parseFloat(playRewardsAmount) > parseFloat(maxRewardsAmount)) {
            layerOpen('打赏金额超出打赏范围')
        } else {
            var roomId = $("input[name=roomId]").val();//直播间ID
            var userId = $("input[name=userId]").val();//用户ID
            $.ajax({
                type: "GET",//请求方式 get/post
                url: "http://" + tomcatBase + "/ilive/rewards/addPlayRewards.jspx",
                dataType: "jsonp",
                jsonp: "callback",
                cache: false,
                data: {
                    terminalType: "h5",
                    roomId: roomId,
                    playRewardsAmount: playRewardsAmount,
                    userId: userId
                },
                success: function (data) {
                    if (data.code == 1) {
                        console.log("打赏成功");
                        $(".rewardMask").slideUp(500);
                    } else {
                        layerOpen(data.message)
                    }
                }
            });
        }
    }
};
$('.btnIcons').on('click', function () {
    //getHeight()
    //$('.Mask').toggleClass('hide')
    // $(this).addClass('active').prev().addClass('active')
    $(this).toggleClass('active')
    $(this).prev().toggleClass('active ').find('.livemask').toggleClass('hide')

    // $(".Icons>div:not('.hide')").each(function (index, item) {
    //     $(this).parent().parent().height(1.2*(index+1)+'rem')
    // });


})

//判断抽奖投票是否启动
function selectChoujiang() {
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
            if (code == 401) {
                window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            } else if (code == 402) {
                saveMessagerPhoneNumber();
            } else if (code == 403) {

            } else if (code == 1) {
                //抽奖
                var isPlayRewards = data.data.isPlayRewards //打赏
                var isStartPrizr = data.data.isStartPrizr  //抽奖
                // var isStartQuestionAnswer= data.data.isStartQuestionAnswer //礼物
                var isStartRedPacket = data.data.isStartRedPacket //红包
                var isStartSign = data.data.isStartSign //签到
                var isStartVote = data.data.isStartVote //投票

                if (isStartSign) { //签到
                    setTimeout(function () {
                        $('#qiandao').removeClass('hide')

                    }, 350)
                } else {
                    setTimeout(function () {
                        $('#qiandao').addClass('hide')
                    }, 350)
                }

                if (isStartPrizr || isStartVote || isPlayRewards || isStartRedPacket) {
                    setTimeout(function () {
                        $('.btnIcons').removeClass('hide')

                    }, 350)

                } else {
                    setTimeout(function () {
                        $('.btnIcons').addClass('hide')
                    }, 350)
                }

                if (isStartPrizr == 1) { //抽奖
                    $(".drawIcon").removeClass("hide");
                }

                if (isStartVote == 1) {  //投票
                    $(".toupiaoIcon").removeClass("hide");
                }

                if (isStartRedPacket == 1) {  //红包
                    $(".hongbaoIcon").removeClass("hide");
                    hongbao()
                }

                if (isPlayRewards == 1) { //打赏
                    $(".rewardIcon").removeClass("hide");
                    playRewards()
                }


            } else {
                layerOpen(data.message)
            }
        }
    });
}

//打赏设置
function playRewards() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/rewards/selectPlayRewards.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId,
            terminalType: "h5"
        },
        success: function (data) {
            if (data.code == 1) {
                var iLivePlayRewards = data.data;
                if (iLivePlayRewards.rewardsSwitch == 0) {
                    //关闭
                    $(".rewardIcon").addClass("hide");
                    minRewardsAmount = 0;
                    maxRewardsAmount = 0;
                } else {
                    //开启
                    $(".rewardIcon").removeClass("hide");
                    if (iLivePlayRewards.rewardsTitleType == 0) {
                        //宣传语
                        $(".rewardPic").text(iLivePlayRewards.promotionalLanguage);
                    } else if (iLivePlayRewards.rewardsTitleType == 1) {
                        //宣传图
                        var html = "<img src=\"" + iLivePlayRewards.promotionalImage + "\" alt=\"\"/>";
                        $(".rewardPic").html(html);
                    }
                    minRewardsAmount = parseFloat(iLivePlayRewards.minRewardsAmount);
                    maxRewardsAmount = parseFloat(iLivePlayRewards.maxRewardsAmount);
                    $("input[name=playRewardsAmount]").attr("placeholder", minRewardsAmount + "~" + maxRewardsAmount + "元");
                }
                // $(".liveIcon>div:not('.hide')").each(function (index, item) {
                //     console.log('as'+index);
                //     // if (index == 0) {
                //     //     $(".liveIcon>div:not('.hide')").eq(0).css("bottom", 0);
                //     // } else if (index == 1) {
                //     //     $(".liveIcon>div:not('.hide')").eq(1).css("bottom", "1rem");
                //     // } else if (index == 2) {
                //     //     $(".liveIcon>div:not('.hide')").eq(2).css("bottom", "2rem");
                //     // } else if (index == 3) {
                //     //     $(".liveIcon>div:not('.hide')").eq(3).css("bottom", "3rem");
                //     // }
                // });
            } else {
                layerOpen(data.message)
            }
        }
    });
}

var hbflag = false

//红包
function hongbao() {
    $('.hongbaoIcon').on('click', function () {
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
                    $.DialogBySHF.Alert({
                        Width: 350,
                        Height: 200,
                        Title: "警告",
                        Content: "data.message"
                    });
                }
            }
        })

        $('.hongbaoClose').on('click', function () {
            $(".hongbaoMask").slideUp(500);
            $(".livemask").click()
        })

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


    })

}

function openhongbao(text) {
    $(".hongbaoMask").slideDown(500);
    if (hbflag) return
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

    img.src = 'img/hongbaokl.png'
    img.onload = function () {
        cxt.drawImage(img, 0, 0)
        cxt.textBaseline = 'middle'
        cxt.textAlign = 'center'

        var left = canvas.width / 2
        var top = canvas.height / 2


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

}

//礼物列表
function selectGiftList() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/gift/giftList.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            roomId: roomId,
            terminalType: "h5"
        },
        success: function (data) {
            if (data.code == 1) {
                var isSystemGift = data.isSystemGift;
                if (isSystemGift == 1) {
                    var giftList = data.data;
                    var HTML = "";
                    for (var i = 0; i < giftList.length; i++) {
                        var gift = giftList[i];
                        HTML += "<li id=\"gift_" + gift.giftId + "\" giftId=\"" + gift.giftId + "\" giftPrice=\"" + gift.giftPrice + "\" giftName=\"" + gift.giftName + "\">";
                        HTML += "	<div class=\"giftPic\"><img src=\"" + gift.giftImage + "\" alt=\"\"/></div>";
                        HTML += "	<div class=\"giftName\">" + gift.giftName + "</div>";
                        HTML += "	<div class=\"giftMoney\">" + gift.giftPrice + "元</div>";
                        HTML += "</li>";
                    }
                    $("#giftList").html(HTML);
                    $(".giftIcon").removeClass("hide");
                } else {
                    $(".giftIcon").addClass("hide");
                }
            } else {
                layerOpen(data.message)
            }
        }
    });
}

// //绑定手机号
function saveMessagerPhoneNumber() {
    console.log("弹框进行手机号绑定");
    $(".passMask").fadeIn(500);
}

$(".closepass .icon-close").click(function () {
    $(".passMask").fadeOut();
});

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
            terminalType: "h5",
            roomId: roomId
        },
        success: function (data) {
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (data.code == 401) {
                window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId ;
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {

            } else if (data.code == 1) {
                var iLiveRollingAdvertising = data.data;
                var startType = iLiveRollingAdvertising.startType;
                if (startType == 1) {
                    console.log("开启");
                    var content = iLiveRollingAdvertising.content;
                    $("#iLiveRollingAdvertising").removeClass("hide").text(content);
                    $('#iLiveRollingAdvertising').marquee({
                        duration: 5000
                    });
                } else {
                    console.log("关闭");
                    $("#iLiveRollingAdvertising").addClass("hide");
                }
            } else {
                console.log("获取滚动消息失败：" + data.message);
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
                var openLogoSwitch = liveEvent.openLogoSwitch;//logo显示开关 0为关闭 1为开启
                var liveTitle = liveEvent.liveTitle || "";
                // $(".logo").removeClass("logoleft logoright logobottomLeft logobottomRight");
                // $(".logo").addClass("hide");
                // if (openLogoSwitch == 1) {
                //     var logoUrl = liveEvent.logoUrl;// 直播场次LOGO
                //     var logoPosition = liveEvent.logoPosition;//LOGO位置 1:左上角 2:右上角 3:左下角 4:右下角
                //     $(".logo").removeClass("hide");
                //     $(".logo").html("<img src=\"" + logoUrl + "\">");
                //
                //     if (logoPosition == 1) {
                //
                //         $(".logo").addClass("logoleft");
                //
                //     } else if (logoPosition == 2) {
                //
                //         $(".logo").addClass("logoright");
                //
                //     } else if (logoPosition == 3) {
                //
                //         $(".logo").addClass("logobottomLeft");
                //
                //     } else if (logoPosition == 4) {
                //
                //         $(".logo").addClass("logobottomRight");
                //
                //     }
                // }
            } else {
                layer.open({
                    content: '获取直播间信息失败'
                    , btn: '确定'
                    , yes: function (index) {
                        errorfunction()
                    }
                });
            }
        }
    });
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
                        $(".appointment").addClass('select').text("取消提醒");
                    } else {
                        //未预约
                        $(".appointment").removeClass('select').text("直播开始提醒");
                    }
                } else {
                    layerOpen('预约信息查询失败!')
                }
            }
        });
    }

}

//预约和取消预约
function saveAppointment() {
    var userId = $("input[name=userId]").val();//用户ID
    var roomId = $("input[name=roomId]").val();//直播间ID
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId ;
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
                    window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                } else if (data.code == 402) {
                    saveMessagerPhoneNumber();
                } else if (data.code == 403) {

                } else if (data.code == 1) {
                    if (data.data.userid != undefined) {
                        var status = data.status
                        if (status == 1) {
                            //已预约
                            selectAppointment();
                            layer.open({
                                content: data.message
                                , btn: ['是', '否']
                                , yes: function () {
                                    openConfirm()
                                }
                                , no: function (index) {
                                    layer.close(index)
                                    //temporaryCancelApp()
                                }
                            });
                        } else if (status == 2) {
                            selectAppointment();
                            layerOpen(data.message)
                        }
                    } else {
                        //未预约
                        $(".appointment").text("直播开始提醒");
                        layerOpen(data.message)
                    }
                } else {
                    layerOpen('预约失败')
                }
            }
        });
    }
}

//现在打开
function openConfirm() {
    console.log("你确认了查看直播方更多直播内容");
    window.location.href = "http://apizb.tv189.com/version/wap/download/ios";
}

//暂时不用
function temporaryCancel() {
    console.log("你放弃查看直播方更多直播内容");
}

//签到
var signId,
    flag = true;

function OpenQianDao() {
    var Height = winHeight - VideoBox - menuBox
    // 1 拿到signid
    GetSignId(function () {
        $(document.body).addClass('modal-open')
        layerId = layer.open({
            type: 1,
            content: qiandaoHTML(),
            anim: 'up',
            style: 'position:absolute; left:0; bottom:0; width:100%; height:' + Height + 'px; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;'
        });
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
            if (data.code == 1) {
                signId = data.signId
                callback()
            } else {
                layerOpen(data.message)
                return false
            }
        }
    })
}

function qiandaoHTML() {
    html = '<div class="main-popup ">' +
        '<div class="Input">' +
        '<input type="tel" placeholder="手机号"   maxlength="11" name="phoneNum" id="phoneNum" onblur="validate(this)"/>' +
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

//验证手机号是否签到
function validate(obj) {
    wchatHackInput()
    var phoneNumber = obj.value;
    if (phoneNumber != null && phoneNumber != "") {
        if (!isPoneAvailable(phoneNumber.trim())) {
            layerOpen('请输入正确的手机号')
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
                    layerOpen('您已签到')
                    $('.QiandaoBtn').attr("disabled", "true").addClass('disableBtn');
                    return false
                } else {
                    $('.QiandaoBtn').removeAttr("disabled").removeClass('disableBtn');
                }
            }
        });
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
                layerOpen(data.msg)
                return false
            }
            if (data.status == 1000) {
                layerOpen(data.msg)
                layer.close(layerId)
                return false
            }
            if (data.status == 1001) {
                layerOpen(data.msg)
                return false

            }
            if (data.status == 1002) {
                layerOpen(data.msg)
                return false
            }
        }
    });

}

// function saveQianDao(liveEventId) {
//     if (liveEventId == 0) {
//         layerOpen('你已经签过到')
//     } else {
//         var userId = $("input[name=userId]").val();//用户ID
//         if (userId == 0) {
//             var roomId = $("input[name=roomId]").val();//直播间ID
//             window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
//             // if (confirm("是否进入登录？")) {
//             //     var roomId = $("input[name=roomId]").val();//直播间ID
//             //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
//             // } else {
//             //     $.DialogBySHF.Alert({
//             //         Width: 350,
//             //         Height: 200,
//             //         Title: "警告",
//             //         Content: "你没有登录、无法签到"
//             //     });
//             // }
//         } else {
//             $.ajax({
//                 type: "POST",//请求方式 get/post
//                 url: "http://" + tomcatBase + "/ilive/register/registerEvent.jspx",
//                 dataType: "jsonp",
//                 jsonp: "callback",
//                 cache: false,
//                 data: {
//                     terminalType: "h5",
//                     userId: userId,
//                     liveEventId: liveEventId
//                 },
//                 success: function (data) {
//                     //401 手机登录
//                     //402     绑定手机号
//                     //403    微信打开
//                     if (data.code == 401) {
//                         window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
//                         // if (confirm("是否进入登录？")) {
//                         //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
//                         // } else {
//                         //     $.DialogBySHF.Alert({
//                         //         Width: 350,
//                         //         Height: 200,
//                         //         Title: "警告",
//                         //         Content: data.message
//                         //     });
//                         // }
//                     } else if (data.code == 402) {
//                         saveMessagerPhoneNumber();
//                     } else if (data.code == 403) {
//
//                     } else if (data.code == 1) {
//                         $("#qiandao").removeAttr("onclick").addClass('checked');
//                         // $("#qiandao").css({
//                         //     "background": "url(img/redsignIcon.png) no-repeat",
//                         //     "background-size": "100% 100%"
//                         // });
//                     } else {
//                         layerOpen('签到失败')
//                     }
//                 }
//             });
//         }
//     }
// }

//点赞
function saveDianZan(roomId) {
    var userId = $("input[name=userId]").val();//用户ID
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/register/registerRoom.jspx",
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
            } else if (data.code == 402) {
                saveMessagerPhoneNumber();
            } else if (data.code == 403) {

            } else if (data.code == 1) {
                $("#dianZan").removeAttr("onclick").addClass('checked');
                // $("#dianZan").css({"background": "url(img/dianzan.png) no-repeat", "background-size": "100% 100%"});
            } else {
                layerOpen(data.message)
            }
        }
    });
    // var userId = $("input[name=userId]").val();//用户ID
    // if (userId == 0) {
    //     roomId = $("input[name=roomId]").val();//直播间ID
    //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
    //     return;
    // } else {
    //     $.ajax({
    //         type: "POST",//请求方式 get/post
    //         url: "http://" + tomcatBase + "/ilive/register/registerRoom.jspx",
    //         dataType: "jsonp",
    //         jsonp: "callback",
    //         cache: false,
    //         data: {
    //             terminalType: "h5",
    //             userId: userId,
    //             roomId: roomId
    //         },
    //         success: function (data) {
    //             //401 手机登录
    //             //402     绑定手机号
    //             //403    微信打开
    //             if (data.code == 401) {
    //                 window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
    //                 // if (confirm("是否进入登录？")) {
    //                 //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
    //                 // } else {
    //                 //     $.DialogBySHF.Alert({
    //                 //         Width: 350,
    //                 //         Height: 200,
    //                 //         Title: "警告",
    //                 //         Content: data.message
    //                 //     });
    //                 // }
    //             } else if (data.code == 402) {
    //                 saveMessagerPhoneNumber();
    //             } else if (data.code == 403) {
    //
    //             } else if (data.code == 1) {
    //                 $("#dianZan").removeAttr("onclick").addClass('checked');
    //                 // $("#dianZan").css({"background": "url(img/dianzan.png) no-repeat", "background-size": "100% 100%"});
    //             } else {
    //                 layerOpen('点赞失败')
    //             }
    //         }
    //     });
    // }
}

//隐藏话题全部评论
function hideMask() {
    $(".replymask").addClass("hide");
    $(".replyBox").addClass("hide");
    $(".newmask").addClass("hide");
}

//聊天输入框点击
$(".talkConRight").click(function () {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        return;
    } else {
        $(".wen").attr("msgValue", "2");
        //$(".wen").css({"background": "url(img/graywen.png) no-repeat", "background-size": "100% 100%"});
        $(".newmask").removeClass("hide");
        //$("video").hide();
        $('input[name="msgContent"]').focus();
        //$('input[type="text"],textarea').attr("maxlength", "40");
    }
});

//问答点击
$(".talkConLeft").click(function () {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        return false;
    } else {
        $(".wen").attr("msgValue", "3");
        $(".wen").css({"background": "url(img/question.png) no-repeat", "background-size": "100% 100%"});
        $(".newmask").removeClass("hide");
        //$("video").hide();
        $('input[type="text"],textarea').focus().attr("maxlength", "100");
    }
});
//关闭输入框
$(".livemask").click(function () {
    //icons
    if ($(this).parent().hasClass('liveIcon')) {
        $(this).addClass("hide")
        $(this).parent().removeClass('active').next().removeClass('active');
        return
    }
    $(this).parent().addClass("hide")
    $(".wen").attr("msgValue", "2");
    $('input[type="text"],textarea').attr("maxlength", "40");
});
//输入框下问答点击
$(".talkCon .wen").click(function () {
    if ($(this).attr("msgValue") == 3) {
        $(this).css({"background": "url(img/graywen.png) no-repeat", "background-size": "100% 100%"});
        $(this).attr("msgValue", "2");
        $('input[type="text"],textarea').attr("maxlength", "40");
    } else {
        $(this).css({"background": "url(img/question.png) no-repeat", "background-size": "100% 100%"});
        $(this).attr("msgValue", "3");
        $('input[type="text"],textarea').attr("maxlength", "100");
    }
});
//发送颜色
$('.textarea').bind('input propertychange', function () {
    if ($('.textarea').val().length != 0) {
        $(".sendMessage").removeAttr("disabled")
        $(".sendMessage").css("background", "#0084ff");
    } else {
        $(".sendMessage").css("background", "#b2daff");
    }
});
//监测输入框内有无文字输入变化发送按钮颜色
$('.replyInput').bind('input propertychange', function () {
    if ($('.replyInput').val().length != 0) {
        $(".replyMessage").removeAttr("disabled")
        $(".replyMessage").css("background", "#0084ff");
    } else {
        $(".replyMessage").css("background", "#b2daff");
        $(".replyMessage").attr("disabled", true)
    }
});
var commentsAllow = 0;

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
            terminalType: "h5",
            roomId: roomId
        },
        success: function (data) {
            var code = data.code;
            if (code == 1) {
                if (mescroll) {
                    mescroll.endSuccess();
                }
                var messageList = data.data;
                commentsAllow = data.commentsAllow;
                var themeDiv = "";
                var topTalkBoxList1 = "";
                if (messageList.length > 0) {
                    for (var i = 0; i < messageList.length; i++) {
                        var iLiveMessage = messageList[i];
                        if (iLiveMessage.top == 0) {
                            //未置顶
                            themeDiv += "<div class=\"themeBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                            themeDiv += "	<div class=\"themeHeader\">";
                            themeDiv += "		<div class=\"themeName\">";
                            themeDiv += "			<span class=\"inlineName\">" + iLiveMessage.senderName + "：</span>";
                            themeDiv += "			<span class=\"hottheme\"><i class=\"fireIcon\"></i> " + iLiveMessage.senderImage + "</span>";
                            themeDiv += "		</div>";
                            themeDiv += "		<div class=\"themeTime\">" + iLiveMessage.createTime + "</div>";
                            themeDiv += "	</div>";
                            themeDiv += "	<div class=\"themeBody\">";
                            themeDiv += "		<div class=\"themeText\">" + iLiveMessage.webContent + "</div>";
                            themeDiv += "		<div class=\"themeText thumb\" thumblist=\"list1\">";
                            if (iLiveMessage.msgType == 2 || iLiveMessage.msgType == 4) {
                                var images = iLiveMessage.images;
                                for (var j = 0; j < images.length; j++) {
                                    themeDiv += "<img class=\"toBig\" src=\"" + images[j] + "\"/>";
                                }

                            } else if (iLiveMessage.msgType == 5 || iLiveMessage.msgType == 3) {
                                themeDiv += "<video id=\"video_" + iLiveMessage.msgId + "\"  poster=\"http://pic.zb.tv189.com/img/defaultVedio.png\" class=\"toVideo\" src=\"" + iLiveMessage.video + "\" controls></video>";
                            }
                            themeDiv += "		</div>"
                            themeDiv += " 		<div class=\"themeBottom\">";
                            themeDiv += " 			<div class=\"reply\"  onclick=\"commentsList(" + iLiveMessage.msgId + ")\">";
                            themeDiv += "				<i class=\"replyIcon iconfont icon-huifu\"></i><span class=\"inlineName\">回复</span>";
                            themeDiv += "			</div>";
                            themeDiv += " 			<div class=\"comment\" id=\"messagePraise_" + iLiveMessage.msgId + "\">";
                            themeDiv += " 				<i class=\"heartIcon iconfont icon-xin\" onclick=\"saveILiveMessagePraise(" + iLiveMessage.msgId + ")\"/></i><span class=\"inlineName\" id=\"praiseNumber_" + iLiveMessage.msgId + "\" >" + iLiveMessage.praiseNumber + "</span>";
                            themeDiv += " 			</div>";
                            themeDiv += " 		</div>";
                            themeDiv += " 	</div>";
                            themeDiv += " 	<div class=\"themeFooter\" id=\"commentsList_" + iLiveMessage.msgId + "\"><div class=\"arrow-up\"></div><div class=\"tishi\">暂无评论</div></div>";
                            themeDiv += "</div>";
                        } else {
                            //置顶
                            topTalkBoxList1 += "<div class=\"themeBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                            topTalkBoxList1 += "	<div class=\"themeHeader\">";
                            topTalkBoxList1 += "		<div class=\"themeName\">";
                            topTalkBoxList1 += "			<span class=\"inlineName\">" + iLiveMessage.senderName + "：</span>";
                            topTalkBoxList1 += "			<span class=\"hottheme\"><i class=\"fireIcon\"></i> " + iLiveMessage.senderImage + "</span>";
                            topTalkBoxList1 += "			<span class=\"zdBox\">置顶</span>";
                            topTalkBoxList1 += "		</div>";
                            topTalkBoxList1 += "		<div class=\"themeTime\">" + iLiveMessage.createTime + "</div>";
                            topTalkBoxList1 += "	</div>";
                            topTalkBoxList1 += "	<div class=\"themeBody\">";
                            topTalkBoxList1 += "		<div class=\"themeText\">" + iLiveMessage.webContent + "</div>";
                            topTalkBoxList1 += "		<div class=\"themeText thumb\" thumblist=\"list1\">";
                            if (iLiveMessage.msgType == 2 || iLiveMessage.msgType == 4) {
                                var images = iLiveMessage.images;
                                for (var j = 0; j < images.length; j++) {
                                    topTalkBoxList1 += "<img class=\"toBig\" src=\"" + images[j] + "\"/>";
                                }

                            } else if (iLiveMessage.msgType == 5 || iLiveMessage.msgType == 3) {
                                topTalkBoxList1 += "<video id=\"video_" + iLiveMessage.msgId + "\"  class=\"toVideo\" poster=\"http://pic.zb.tv189.com/img/defaultVedio.png\"  src=\"" + iLiveMessage.video + "\" controls></video>";
                            }
                            topTalkBoxList1 += "		</div>"
                            topTalkBoxList1 += " 		<div class=\"themeBottom\">";
                            topTalkBoxList1 += " 			<div class=\"reply\"  onclick=\"commentsList(" + iLiveMessage.msgId + ")\">";
                            topTalkBoxList1 += "				<i class=\"replyIcon iconfont icon-huifu\"></i><span class=\"inlineName\">回复</span>";
                            topTalkBoxList1 += "			</div>";
                            topTalkBoxList1 += " 			<div class=\"comment\" id=\"messagePraise_" + iLiveMessage.msgId + "\">";
                            topTalkBoxList1 += " 				<i class=\"heartIcon iconfont icon-xin\" onclick=\"saveILiveMessagePraise(" + iLiveMessage.msgId + ")\"/></i><span class=\"inlineName\" id=\"praiseNumber_" + iLiveMessage.msgId + "\" >" + iLiveMessage.praiseNumber + "</span>";
                            topTalkBoxList1 += " 			</div>";
                            topTalkBoxList1 += " 		</div>";
                            topTalkBoxList1 += " 	</div>";
                            topTalkBoxList1 += " 	<div class=\"themeFooter\" id=\"commentsList_" + iLiveMessage.msgId + "\"><div class=\"arrow-up\"></div><div class=\"tishi\">暂无评论</div></div>";
                            topTalkBoxList1 += "</div>";
                        }
                    }
                }
                $("#topTalkBoxList1").html(topTalkBoxList1);
                $("#themeDiv").html(themeDiv);
                $(".redDot").addClass('hide');
                $("#newTopic").addClass("hide");
                if (messageList.length == 0) {
                    $("#topicNull").removeClass("hide");
                } else {
                    $("#topicNull").addClass("hide");
                    selectcommentsList(roomId);//页面加载5秒执行一次
                }
            } else {
                console.log("失败");
            }
        },
        error: function (data) {
            if (mescroll) {
                mescroll.endErr();
            }

        }
    });
}

//关闭评论
$(".closeReply").click(function () {
    $(".replyBox").addClass("hide");
    $(".replyCon").html("");
    $(".replyDiv").removeAttr("onclick");
})

//查看全部评论
function selectCommentAll(msgId) {
    var userId = $("input[name=userId]").val();
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/comments/selectCommentsAllByMsgId.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            msgId: msgId,
            userId: userId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                var HTMLTXT = "";
                var commentsList = JSON.parse(data.commentsList);
                for (var j = 0; j < commentsList.length; j++) {
                    var comments = commentsList[j];
                    HTMLTXT += "<div class=\"pinlun\">";
                    HTMLTXT += "		<span class=\"plname\">" + comments.commentsName + ":</span>" + comments.comments;
                    HTMLTXT += "	</div>";
                }
                $(".replyBox").removeClass("hide");
                $(".replyCon").html(HTMLTXT);
                $(".replyDiv").attr("onclick", "commentsList(" + msgId + ")");
            } else {
                console.log(data.message);
            }
        }
    });
}


//点赞
function saveILiveMessagePraise(msgId) {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // if (confirm("是否进入登录？")) {
        //     var roomId = $("input[name=roomId]").val();//直播间ID
        //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // } else {
        //     $.DialogBySHF.Alert({
        //         Width: 350,
        //         Height: 200,
        //         Title: "警告",
        //         Content: "你放弃了登录，无法进行点赞失败"
        //     });
        // }
        return;
    }
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/praise/savePraise.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            userId: userId,
            msgId: msgId
        },
        success: function (data) {
            var status = data.code;
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (status == 401) {
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
            } else if (status == 402) {
                saveMessagerPhoneNumber();
            } else if (status == 403) {

            }
            if (status == 1) {
                var praiseNumber = data.praiseNumber;
                if (praiseNumber == 0) {
                    var number = $("#praiseNumber_").text();
                    var HTML = "<i class=\"heartIcon iconfont icon-xin checked\"></i>";
                    HTML += "<span  class=\"inlineName\" id=\"praiseNumber_" + msgId + "\">" + number + "</span>";
                    $("#messagePraise_" + msgId).html(HTML);
                } else {
                    var HTML = "<i class=\"heartIcon iconfont icon-xin checked\"></i>";
                    HTML += "<span  class=\"inlineName\" id=\"praiseNumber_" + msgId + "\">" + praiseNumber + "</span>";
                    $("#messagePraise_" + msgId).html(HTML);
                }
                return;
            } else {
                layerOpen('点赞操作失败')
            }
        }
    });

}

function selectMediaFile(fileId) {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == "" || userId == undefined) {
        userId = 0;
    }
    window.location.href = "http://" + h5Base + "/phone/review.html?fileId=" + fileId + "&userId=" + userId + "&roomId=" + roomId;
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
        $("#openCountdownSwitch").text(dataTime);
        setTimeout(function () {
            selectTimer(liveStartTime);
        }, 1000);
    } else {
        $("#openCountdownSwitch").text("0 天 0 小时 0 分钟 0  秒");
    }

}

var ws;

function selectHTML(roomId) {
    window.setInterval(function () { //每隔5秒钟发送一次心跳，避免websocket连接因超时而自动断开
        var ping = {"p": "1"};
        if (ws) {
            try {
                ws.send(JSON.stringify(ping));
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
            terminalType: "h5",
            roomId: roomId,
            sessionType: 1,//WebSocket使用用户
            webType: 1
        },
        success: function (data) {
            if (data.code == 404) {
                window.location.href = "http://" + h5Base + "/phone/end.html";
            } else if (data.code == 402) {
                var userId = $("input[name=userId]").val();
                if (userId != 0) {
                    window.location.href = "http://" + h5Base + "/phone/phoneNumber.html?roomId=" + roomId + "&userId=" + userId;
                } else {
                    layer.open({
                        content: '绑定手机号，获取用户失败'
                        , btn: '确定'
                        , yes: function (index) {
                            errorfunction()
                        }
                    });
                    return false
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "警告",
                    //     Content: "绑定手机号，获取用户失败",
                    //     ConfirmFun: errorfunction
                    // });
                }
            } else if (data.code == 1) {
                //selectcommentsList(roomId);
                hlsAddr = data.hlsAddr;
                // if (liveStart != 0) {
                //     if (liveStart == 2) {
                //         liveStart = 1;
                //         setTimeout(function () {
                //             $("video").attr("src", hlsAddr);
                //             //进入全屏事件
                //             loadJsCss("js/dcVideo.js", "js");
                //         }, 13000);
                //     } else {
                //         $("video").attr("src", hlsAddr);
                //         //进入全屏事件
                //         loadJsCss("js/dcVideo.js", "js");
                //     }
                // }
                switch (liveStart) {
                    case 0:
                        $('#liveStart').removeClass('hide'); //预约
                        $('#enterpriseList').css('padding-bottom', '1.25rem');//预约
                        //dcVideo()
                        break
                    case 1:
                        $('.marqueeBox').css('bottom', '16%')
                        //$("video").attr("src", hlsAddr);
                        myPlayer.ready(function () {
                            //$(".videoPlay").removeClass("hide").find('i').removeClass('hide');
                            $('.vjs-big-play-button').removeClass("hide");
                            this.src({src: hlsAddr, type: 'application/x-mpegURL'})
                            this.poster(data.data.liveEvent.playBgAddr)
                            $('.vjs-live-control').text('实时直播')
                        })
                        //进入全屏事件
                        //dcVideo()
                        break
                    case 2:
                        liveStart = 1;
                        $('.videoBox').append('<div id="vLoad"></div>')
                        setTimeout(function () {
                            $('#vLoad').hide()
                            $('.marqueeBox').css('bottom', '16%')
                            myPlayer.ready(function () {
                                $('.vjs-big-play-button').removeClass("hide");
                                //$(".videoPlay").removeClass("hide").find('i').removeClass('hide');
                                this.src({src: hlsAddr, type: 'application/x-mpegURL'})
                                this.poster(data.data.liveEvent.playBgAddr)
                                $('.vjs-live-control').text('实时直播')
                            })
                            //进入全屏事件
                            //dcVideo()
                        }, 13000);
                        break
                    case 3:
                        setTimeout(function () {
                            myPlayer.ready(function () {
                                this.poster(data.data.liveEvent.playBgAddr)
                            })
                        }, 0)
                        //dcVideo()
                        break
                    case 4:
                        $('#liveStart').addClass('hide');
                        $('#enterpriseList').css('padding-bottom', '.5rem');
                        //dcVideo()
                        break
                }

                // if (liveStart != 0 && liveStart != 4) {
                //
                //
                // } else {
                //     //dcVideo()
                // }
                // if (liveStart == 2) {
                //     liveStart = 1;
                //     setTimeout(function () {
                //         $("video").attr("src", hlsAddr);
                //         //进入全屏事件
                //         dcVideo()
                //     }, 13000);
                // } else {
                //     //$("video").attr("src", hlsAddr);
                //     //进入全屏事件
                //     dcVideo()
                // }
                selectRoomLogo();//获取直播间信息Logo
                selectAdvertising();//滚屏消息
                selectGiftList();//初始化礼物列表
                // playRewards();//初始化打赏设置
                var token = data.token;
                var opType = 10;
                var estoppelType = data.estoppelType;
                if (estoppelType == 0) {
                    //是否全局禁言 0 禁言开启 1 关闭禁言
                    $(".jinyanBox").removeClass("hide");
                    $("#jinyanBoxImg").attr("src", "img/jinyan.png");
                } else {
                    var forbidTalk = data.forbidTalk;
                    console.log("禁言标示：" + forbidTalk);
                    if (forbidTalk == 1) {
                        opType = 11;
                        $(".jinyanBox").removeClass("hide");
                        $("#jinyanBoxImg").attr("src", "img/jinyan.png");
                    } else {
                        opType = 10;
                        $(".jinyanBox").addClass("hide");
                        $("#jinyanBoxImg").attr("src", "img/talking.png");
                    }
                }
                if (typeof (WebSocket) == "undefined") {
                    layer.open({
                        content: '你的浏览器不支持信息交流'
                        , btn: '确定'
                        , yes: function (index) {
                            errorfunction()
                        }
                    });
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "警告",
                    //     Content: "你的浏览器不支持信息交流",
                    //     ConfirmFun: errorfunction
                    // });
                    return;
                }
                var url = "ws://" + tomcatBase
                    + "/ilive/webSocketServer.jspx?username="
                    + token;
                ws = new WebSocket(url);
                ws.onopen = function (e) {
                    console.log("连接成功");
                }
                ws.onclose = function (e) {
                    console.log("关闭连接 ");
                }
                ws.onerror = function (e) {
                    console.log("建立连接异常");
                    window.location.href = window.location.href;
                }
                ws.onmessage = function (e) {
                    var iLiveMessage = JSON.parse(e.data);
                    var liveMsgType = iLiveMessage.liveMsgType;
                    switch (iLiveMessage.roomType) {
                        case 0:
                            var iLiveEventVo = iLiveMessage.iLiveEventVo;

                            estoppelType = iLiveEventVo.estoppleType;
                            if (estoppelType == 0) {
                                //是否全局禁言 0 禁言开启 1 关闭禁言
                                $(".jinyanBox").removeClass("hide");
                                $("#jinyanBoxImg").attr("src", "img/jinyan.png");
                            } else if (estoppelType == 1) {
                                if (opType == 11) {
                                    $(".jinyanBox").removeClass("hide");
                                    $("#jinyanBoxImg").attr("src", "img/jinyan.png");
                                } else {
                                    $(".jinyanBox").addClass("hide");
                                    $("#jinyanBoxImg").attr("src", "img/talking.png");
                                }
                            }
                            /**
                             * 直播状态：
                             * 0 直播未开始 1 直播中 2 暂停中 3 直播结束 4待审 5已审 6审核不过
                             */
                            var liveStatus = iLiveEventVo.liveStatus;
                            var playBgAddr = iLiveEventVo.playBgAddr;
                            /*if(liveStatus==0){
                                    //开始直播直播未开始
                                    liveStart=0;
                                    $("#videoImg").html("<img style=\"position: relative;z-index: 99;\"  src=\""+playBgAddr+"\" alt=\"\"/>");
                                    $("#imageComtent").removeClass("hide");
                                    $("#imageComtent").text(countdownTitle);
                                    $("#liveStart").removeClass("hide");
                                    $("#endLive").addClass("hide");
                                }else */
                           // $("#timeStart").addClass("hide");
                            //$('#liveStart').addClass('hide');
                            //$('#enterpriseList').css('padding-bottom', '.5rem');
                            switch (liveStatus) {
                                case 1:
                                    // if (liveStart == 0) {
                                    //     setTimeout(function () {
                                    //         window.location.reload();
                                    //     }, 13000);
                                    // }
                                    // video.src = hlsAddr
                                    setTimeout(function () {
                                        $("#endLive").addClass("hide");
                                        $("#playBg").addClass('hide')
                                        $(".videoPlay").addClass('hide')
                                        $('.marqueeBox').css('bottom', '16%')
                                        // $("#stratLive").addClass("hide");
                                        //$(".videoPlay").removeClass("hide").find('i').removeClass("hide");
                                        myPlayer.ready(function () {
                                            $('.vjs-big-play-button').removeClass("hide");
                                            this.pause()
                                            this.src({src: hlsAddr, type: 'application/x-mpegURL'})
                                            $('.vjs-live-control').text('实时直播')
                                        })
                                        //dcVideo();
                                        //video.play();
                                    }, 5000)
                                    break
                                case 2:
                                    //liveStart = 0;
                                    setTimeout(function () {
                                        //暂停
                                        //$("#videoImg").html("<img  style=\"position: relative;z-index: 99;\"  src=\"" + playBgAddr + "\" alt=\"\"/>");=

                                        $('.marqueeBox').css('bottom', '0')
                                        $("#endLive").removeClass("hide").find(".textMask").text("当前暂无直播画面");
                                        myPlayer.ready(function () {
                                            $('.vjs-big-play-button').addClass("hide");
                                            this.pause()
                                            this.src({src: 'none', type: 'application/x-mpegURL'})
                                        })
                                        //$('video').attr('src','')

                                        // myPlayer.ready(function () {
                                        //     this.src()
                                        // })

                                        //$("#liveStart").addClass("hide");
                                        // video.pause()
                                        //$(".no-strat .textMask").css("margin-left",-$(".no-strat .textMask").outerWidth()/2);
                                    }, 5000);
                                    break
                                case 3:
                                    //liveStart = 0;
                                    setTimeout(function () {
                                        // $("#stratLive").addClass("hide");
                                        $("#playBg").removeClass('hide')
                                        $("#endLive").removeClass("hide").find(".textMask").text("本场直播已结束");
                                        $('.marqueeBox').css('bottom', '0')
                                        myPlayer.ready(function () {
                                            $('.vjs-big-play-button').addClass("hide");
                                            this.pause()
                                            this.src({src: 'none', type: 'application/x-mpegURL'})
                                        })
                                        //window.location.reload()
                                        //直播结束
                                        // $("#videoImg").html("<img style=\"position: relative;z-index: 99;\"  src=\"" + liveStatus + "\" alt=\"\"/>");
                                        // $("#endLive").removeClass("hide");
                                        // $("#timeStart").addClass("hide");
                                        // $("#imageComtent").addClass("hide");
                                        // // $("#liveStart").addClass("hide");
                                        // $("#stratLive").addClass("hide");
                                        // $(".videoBtn").addClass("hide");
                                        // $(".videoPlay").addClass("hide");
                                        //$(".no-ended .textMask").css("margin-left",-$(".no-ended .textMask").outerWidth()/2);
                                    }, 23000);
                                    break
                            }

                            // if (liveStatus == 1) {
                            //
                            // } else if (liveStatus == 2) {
                            //
                            // } else if (liveStatus == 3) {
                            //
                            // }
                            break
                        case 1:
                            //消息管理
                            var deleteAll = iLiveMessage.deleteAll;
                            var HTMLTXT;
                            if (deleteAll == 1) {
                                $("#talkBoxList2").children().remove();
                                $("#topTalkBoxList2").children().remove();
                            } else {
                                if (iLiveMessage.deleted == 1) {
                                    $("#msgId_" + iLiveMessage.msgId).remove();
                                } else {
                                    var userId = $("input[name=userId]").val();
                                    if (iLiveMessage.senderId == userId) {
                                        if (estoppelType == 1) {
                                            opType = iLiveMessage.opType;
                                            if (opType == 11) {
                                                $(".jinyanBox").removeClass("hide");
                                                $("#jinyanBoxImg").attr("src", "img/jinyan.png");
                                                $(".giftIcon").addClass("hide");
                                            } else {
                                                $(".jinyanBox").addClass("hide");
                                                $("#jinyanBoxImg").attr("src", "img/talking.png");
                                                $(".giftIcon").removeClass("hide");
                                            }
                                        } else {
                                            opType = iLiveMessage.opType;
                                        }
                                    }
                                    if (liveMsgType == 2) {
                                        flyDanmu(iLiveMessage.webContent)
                                        if (iLiveMessage.checked == 1) {
                                            $("#msgId_" + iLiveMessage.msgId).remove();
                                            if (iLiveMessage.top == 0) {
                                                HTMLTXT = "<div class=\"talkline commentBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                                HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\"><span class=\"senderName\">" + iLiveMessage.senderName + "</span><span class='createTime'>" + iLiveMessage.createTime + "</span> </div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                                HTMLTXT += "</div>";
                                                //新增聊天互动
                                                $("#topTalkBoxList2 #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                $("#talkBoxList2").prepend(HTMLTXT);
                                            } else {
                                                HTMLTXT = "<div class=\"topBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                                HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\"><span class=\"senderName\">" + iLiveMessage.senderName + "</span><i class=\"questionIcon stickIcon\"></i><span class='createTime'>" + iLiveMessage.createTime + "</span></div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                                HTMLTXT += "</div>";
                                                //新增聊天互动
                                                $("#talkBoxList2 #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                $("#topTalkBoxList2").prepend(HTMLTXT);
                                            }
                                        } else {
                                            if (iLiveMessage.senderId == userId) {
                                                $("#msgId_" + iLiveMessage.msgId).remove();
                                                if (iLiveMessage.top == 0) {
                                                    HTMLTXT = "<div class=\"talkline commentBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                    HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                                    HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\"><span class=\"senderName\">" + iLiveMessage.senderName + "</span><span class='createTime'>" + iLiveMessage.createTime + "</span></div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                                    HTMLTXT += "</div>";
                                                    //新增聊天互动
                                                    $("#topTalkBoxList2 #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                    $("#talkBoxList2").prepend(HTMLTXT);
                                                } else {
                                                    HTMLTXT = "<div class=\"topBox\" id=\"msgId_" + iLiveMessage.msgId + "\">";
                                                    HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                                    HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\"><span class=\"senderName\">" + iLiveMessage.senderName + "</span><i class=\"questionIcon stickIcon\"></i><span class='createTime'>" + iLiveMessage.createTime + "</span></div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                                    HTMLTXT += "</div>";
                                                    //新增聊天互动
                                                    $("#talkBoxList2 #msgId_" + iLiveMessage.msgId).prepend(HTMLTXT);
                                                    $("#topTalkBoxList2").prepend(HTMLTXT);
                                                }
                                            }
                                        }
                                    } else if (liveMsgType == 3) {
                                        //问答
                                        if (iLiveMessage.replyType == 0) {
                                            //未回答
                                            var senderId = iLiveMessage.senderId;
                                            var userId = $("input[name=userId]").val();
                                            if (senderId == 0 || senderId == null || senderId == undefined) {
                                            } else {
                                                if (senderId == userId) {
                                                    HTMLTXT = "<div class=\"talkline questionBox\" id=\"msgId_" + iLiveMessage.msgId + "\" >";
                                                    HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                                    HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\">" + iLiveMessage.senderName + " <i class=\"questionIcon\"><img src=\"img/question.png\"></i><span class='createTime'>" + iLiveMessage.createTime + "</span></div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                                    HTMLTXT += "</div>";
                                                    $("#msgId_" + iLiveMessage.msgId).remove();
                                                    HTMLTXT += "</div>";
                                                    $("#talkBoxList2").prepend(HTMLTXT);
                                                }
                                            }
                                        } else {
                                            HTMLTXT = "<div class=\"talkline questionBox\" id=\"msgId_" + iLiveMessage.msgId + "\" >";
                                            HTMLTXT += "	<div class=\"userImg\"><img src=" + iLiveMessage.senderImage + "></div>";
                                            HTMLTXT += "	<div class=\"commentText\"><div class=\"inlineName\">" + iLiveMessage.senderName + " <i class=\"questionIcon\"><img src=\"img/question.png\"></i><span class='createTime'>" + iLiveMessage.createTime + "</span></div><div class=\"comment\">" + iLiveMessage.webContent + "</div> </div>";
                                            HTMLTXT += "	<div class=\"questionanswer\">";
                                            HTMLTXT += "		<div class=\"arrow-up\"></div>";
                                            HTMLTXT += "		<div class=\"questionName\"><i class=\"questionIcon\"><img src=\"img/answer.png\" alt=\"\"/></i><span class=\"inlineName\">" + iLiveMessage.replyName + "</span></div>";
                                            HTMLTXT += "		<div class=\"commentText\">" + iLiveMessage.replyContent + "</div>";
                                            HTMLTXT += "	</div></div>";
                                            HTMLTXT += "</div>";
                                            //回答
                                            HTMLTXT += "</div>";
                                            $("#msgId_" + iLiveMessage.msgId).remove();
                                            HTMLTXT += "</div>";
                                            $("#talkBoxList2").prepend(HTMLTXT);
                                        }
                                    }
                                }
                            }
                            //我是有底线的
                            var $topTalkBoxList2 = $('#topTalkBoxList2').height()
                            var $talkBoxList2 = $('#talkBoxList2').height()
                            if (($topTalkBoxList2 + $talkBoxList2) > $('#talkLi').height()) {
                                $('#talkLi').addClass('dixian')
                            }

                            break
                        case 2:
                            console.log("发现新话题");
                            $(".redDot").removeClass('hide');
                            $("#newTopic").removeClass("hide");
                            break
                        case 3:
                            window.location.href = "http://" + h5Base + "/phone/end.html";
                            break
                        case 4:
                            console.log("广告滚动");
                            selectAdvertising();
                            break
                        case 5:
                            console.log("抽奖");
                            selectChoujiang();
                            break
                        case 6:
                            console.log("投票");
                            selectChoujiang();
                            break
                        case 7:
                            console.log("赠送礼物");
                            //获取礼物数据
                            var iLiveUserGift = iLiveMessage.iLiveUserGift;
                            if (iLiveUserGift.giftOperation == 0) {
                                //送礼物
                                var userName = iLiveUserGift.userName;
                                var giftImage = iLiveUserGift.giftImage;
                                var giftNumber = iLiveUserGift.giftNumber;
                                arr.push({
                                    "code": 0,
                                    "userName": userName,
                                    'giftImage': giftImage,
                                    'giftNumber': giftNumber
                                })
                                giveGift();
                            } else {
                                //礼物列表更改
                                selectGiftList();
                            }
                            break
                        case 8:
                            break
                        case 9:
                            console.log("打赏");
                            var iLiveUserPlayRewards = iLiveMessage.iLiveUserPlayRewards;
                            if (iLiveUserPlayRewards.playRewardsOperation == 0) {
                                playRewards();
                            } else {
                                var rewardsPrice = iLiveUserPlayRewards.playRewardsAmount;
                                var userName = iLiveUserPlayRewards.userName;
                                arr.push({"code": 1, "userName": userName, 'rewardsPrice': rewardsPrice})
                                giveGift();
                            }
                            break
                        case 10:
                            var welcomeLanguage = iLiveMessage.welcomeLanguage;
                            arr.push({"code": 2, "welcomeLanguage": welcomeLanguage})
                            giveGift();

                            // var num=0
                            // setInterval(function () {
                            //     num++
                            //     var welcomeLanguage = iLiveMessage.welcomeLanguage;
                            //     //arr.push({"code": 2, "welcomeLanguage": welcomeLanguage})
                            //
                            //     arr.push({"code": 2, "welcomeLanguage": 'a'+ num +''})
                            //     giveGift();
                            // },1000)
                            break
                        case 11:
                            var documentId = iLiveMessage.documentId;
                            var documentPageNO = iLiveMessage.documentPageNO;
                            var documentManual = iLiveMessage.documentManual;
                            var documentDownload = iLiveMessage.documentDownload;
                            selectDocument(documentId, documentDownload, documentPageNO, documentManual);
                            break
                        case 15:
                            console.log("签到活动");
                            selectChoujiang();
                            break
                        case 16:
                            console.log("红包");
                            selectChoujiang();
                            break
                        default:
                            console.log("操作类型" + iLiveMessage.roomType);

                    }
                }
            } else if (data.code == 2) {

                //鉴权失败
                var permissionsjump = window.sessionStorage.getItem('permissionsjump')

                if (permissionsjump == null || permissionsjump == 'true') {
                    window.sessionStorage.setItem('permissionsjump',true)
                    window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                    return false
                }

                window.location.href = "http://" + h5Base + "/phone/permissions.html?roomId=" + roomId;

            } else if (data.code == 3) {
                //直播间人数已满
                window.location.href = "http://" + h5Base + "/phone/standby.html?roomId=" + roomId;
            } else {
                window.location.href = "http://" + h5Base + "/phone/error.html?roomId=" + roomId;
            }
        }
    });
}

//鉴权失败
function errorfunctionAuthentication() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    window.location.href = "http://" + h5Base + "/phone/permissions.html?roomId=" + roomId;
}

//直播间人数已满
function errorfunctionFull() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    window.location.href = "http://" + h5Base + "/phone/standby.html?roomId=" + roomId;
}

//发送消息
function sendMessageSubmitted() {
    wchatHackInput()
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // if (confirm("是否进入登录？")) {
        //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // } else {
        //     $.DialogBySHF.Alert({
        //         Width: 350,
        //         Height: 200,
        //         Title: "警告",
        //         Content: "你放弃了登录、无法发送消息"
        //     });
        // }
        return;
    }
    var liveMsgType = $(".wen").attr("msgValue");//发送消息类型
    console.log("消息类型：" + liveMsgType);
    var liveEventId = $.trim($("input[name=liveEventId]").val());//场次ID
    var msgContent = $.trim($("input[name=msgContent]").val());//发送消息内容


    if (msgContent.replace("/^s+|s+$/g+", "") == "" || msgContent == undefined) {
        layerOpen('请输入消息内容')
        return false
    }
    if (liveMsgType == 2 || liveMsgType == 3) {
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/sendMessage.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                userId: userId,
                roomId: roomId,
                liveMsgType: liveMsgType,
                content: encodeURI(msgContent),
                liveEventId: liveEventId,
                msgType: 1
            },
            success: function (data) {
                var status = data.code;
                //401 手机登录
                //402     绑定手机号
                //403    微信打开
                if (status == 401) {
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
                } else if (status == 402) {
                    saveMessagerPhoneNumber();
                } else if (status == 403) {

                } else if (status == 1) {
                    console.log("发送消息成功");
                    var iLiveMessage = data.iLiveMessage;
                    $(".wen").attr("msgValue", "2");
                    $(".newmask").addClass("hide");
                    $("input[name=msgContent]").val("");
                    var div = document.getElementById('talkLi');
                    div.scrollTop = 0;
                } else {
                    layerOpen('消息发送失败')
                }
            }
        });
    } else {
        layerOpen('发送消息类型错误')
    }
}

//评论话题
function saveTopicContent() {
    if (commentsAllow == 0) {
        $(".livemask").parent().addClass("hide");
        $(".replyBox").addClass("hide");
        $(".replyCon").html("");
        $("textarea[name=topicContent]").val("");
        $(".replyDiv").removeAttr("onclick");
        layerOpen('评论功能已关闭')
    } else {
        var userId = $("input[name=userId]").val();//用户ID
        var msgId = $("input[name=commentsMsgId]").val();
        if (userId == 0) {
            window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            // if (confirm("是否进入登录？")) {
            //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
            // } else {
            //     $(".livemask").parent().addClass("hide");
            //     $(".replyBox").addClass("hide");
            //     $(".replyCon").html("");
            //     $("input[name=topicContent]").val("");
            //     $(".replyDiv").removeAttr("onclick");
            //     $.DialogBySHF.Alert({
            //         Width: 350,
            //         Height: 200,
            //         Title: "警告",
            //         Content: "你放弃了登录、无法进行评论"
            //     });
            // }
            return false;
        }
        if (msgId == 0) {
            // $(".livemask").parent().addClass("hide");
            // $(".replyBox").addClass("hide");
            // $(".replyCon").html("");
            // $("textarea[name=topicContent]").val("");
            // $(".replyDiv").removeAttr("onclick");
            layerOpen('请选择评论话题')
            return false
        }
        var value = $.trim($("textarea[name=topicContent]").val());//发送消息内容
        var enterValue = value.replace(new RegExp(/\n/g), '<br>');
        var topicContent = enterValue.replace(new RegExp(/\s/g), '&nbsp;');


        if (topicContent.replace("/^s+|s+$/g+", "") == "" || topicContent == undefined) {
            // $(".livemask").parent().addClass("hide");
            // $(".replyBox").addClass("hide");
            // $(".replyCon").html("");
            // $("textarea[name=topicContent]").val("");
            // $(".replyDiv").removeAttr("onclick");
            layerOpen('评论内容不能为空')
            return false
        }
        $(".livemask").parent().addClass("hide");
        $(".replyBox").addClass("hide");
        $(".replyCon").html("");
        $("textarea[name=topicContent]").val("");
        $(".replyDiv").removeAttr("onclick");
        $.ajax({
            type: "POST",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/saveComments.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                userId: userId,
                msgId: msgId,
                comments: encodeURI(topicContent)
            },
            success: function (data) {
                var status = data.code;
                //401 手机登录
                //402     绑定手机号
                //403    微信打开
                if (status == 401) {
                    window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
                    return
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
                } else if (status == 402) {
                    saveMessagerPhoneNumber();
                } else if (status == 403) {

                }
                $(".livemask").parent().addClass("hide");
                $(".replyBox").addClass("hide");
                $(".replyCon").html("");
                $("textarea[name=topicContent]").val("");
                $(".replyDiv").removeAttr("onclick");
                if (status == 1) {
                    // $(".livemask").parent().addClass("hide");
                    // $(".replyBox").addClass("hide");
                    // $(".replyCon").html("");
                    // $("input[name=topicContent]").val("");
                    //$(".replyDiv").removeAttr("onclick");
                    selectMessageLIVE();
                } else {
                    // $(".livemask").parent().addClass("hide");
                    // $(".replyBox").addClass("hide");
                    // $(".replyCon").html("");
                    // $("input[name=topicContent]").val("");
                    //$(".replyDiv").removeAttr("onclick");
                    layerOpen('评论发送失败')
                }
            }
        });
    }
}

//选择评论话题
function commentsList(msgId) {
    var userId = $("input[name=userId]").val();
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // if (confirm("是否进入登录？")) {
        //     var roomId = $("input[name=roomId]").val();//直播间ID
        //     window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        // } else {
        //     $.DialogBySHF.Alert({
        //         Width: 350,
        //         Height: 200,
        //         Title: "警告",
        //         Content: "你放弃了登录、无法进行话题评论"
        //     });
        // }
    } else {
        $("input[name=commentsMsgId]").val(msgId);
        $(".replymask").removeClass("hide");
        $('textarea[name="topicContent"]').focus();
        //$('input[type="text"]').attr("maxlength", "40");
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
            terminalType: "h5",
            roomId: roomId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                var number = JSON.parse(data.data).number;
                if (number == undefined || number == null) {
                    number = 0;
                }
                checknumberlength($(".onlineNumberSpan"), number)
            }
        }
    });
}

//获取话题评论
function selectcommentsList(roomId) {
    var userId = $("input[name=userId]").val();
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/comments/selectList.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            roomId: roomId,
            userId: userId
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
//					var number = data.number;
//					if(number==undefined){
//						number=0;
//					}
//					$(".onlineNumberSpan").text(number);
                var iLiveCommentsMap = JSON.parse(data.iLiveCommentsMap);
                for (var i = 0; i < iLiveCommentsMap.length; i++) {
                    var vo = iLiveCommentsMap[i];
                    var list = vo.list;
                    var HTMLTXT = "<div class=\"arrow-up\"></div>";
                    for (var j = 0; j < list.length; j++) {
                        if (j < 5) {
                            var comments = list[j];
                            HTMLTXT += "<div class=\"pinlun\">";
                            HTMLTXT += "		<span class=\"plname\">" + comments.commentsName + ":</span>" + comments.comments;
                            HTMLTXT += "	</div>";
                        }
                    }
                    if ((list.length != 0) && (list.length > 5)) {
                        HTMLTXT += "<div class=\"tishi discuss\" onclick=\"selectCommentAll(" + vo.msgId + ")\"><i class='iconfont icon-laquo'></i> 查看全部" + list.length + "条评论</div>";
                    }
                    $("#commentsList_" + vo.msgId).html(HTMLTXT);
                }
                var praiseMap = JSON.parse(data.praiseMap);
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
                        var HTML = "<i class=\"heartIcon iconfont icon-xin\"  onclick=\"saveILiveMessagePraise(" + key + ")\"></i>";
                        HTML += "<span  class=\"inlineName\" id=\"praiseNumber_" + key + "\">" + praiseNumber + "</span>";
                        $("#messagePraise_" + key).html(HTML);
                    } else {
                        var HTML = "<i class=\"heartIcon iconfont icon-xin checked\"></i>";
                        HTML += "<span  class=\"inlineName\" id=\"praiseNumber_" + key + "\">" + praiseNumber + "</span>";
                        $("#messagePraise_" + key).html(HTML);
                    }
                }
            } else {
                layerOpen('获取话题评论失败')
            }
        }
    });
}


function tableContent(zbhk, roomId) {
    selectHTML(roomId);
    selectAppointment();
    if (zbhk != 0) {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/app/room/getrecordlist.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                roomId: roomId
            },
            success: function (data) {
                if (data.code == 1) {
                    getReviewList(data)
                } else {
                    layerOpen('直播回看获取失败')
                }
            }
        });
    }
}

function loginfunction() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
}

function loadJsCss(filename, filetype) {
    if (filetype == "js") {
        var fileref = document.createElement('script')//创建标签
        fileref.setAttribute("type", "text/javascript")//定义属性type的值为text/javascript
        fileref.setAttribute("src", filename)//文件的地址
    }
    else if (filetype == "css") {
        var fileref = document.createElement("link")
        fileref.setAttribute("rel", "stylesheet")
        fileref.setAttribute("type", "text/css")
        fileref.setAttribute("href", filename)
    }
    if (typeof fileref != "undefined") {
        document.getElementsByTagName("head")[0].appendChild(fileref)
    }
}

//获取当前直播中文档直播信息
var gallerySwiper, thumbsSwiper, def_Manual;

function selectDocument(documentId, documentDownload, documentPageNO, documentManual) {
    def_Manual = documentManual
    if (documentId == -1) {

        checkslide()
        //gallerySwiper
        // $(".pptBtn").removeClass("hide");
        // $(".left").attr("onclick", "leftPage()").css("color", "#666666");
        // $(".right").attr("onclick", "rightPage()").css("color", "#666666");

        if (documentDownload == 0) {
            $(".pptBtn").addClass("hide");
        }
        gallerySwiper.slideTo(documentPageNO - 1, 1000, false);
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
                    var documentPictures = documentManager.list;
                    var HTML = '';
                    // var type = documentManager.type
                    // var HTML = "<div class=\"wendangBox\">";
                    // HTML += "	<ol id=\"ul\">";
                    // for (var i = 0; i < documentPictures.length; i++) {
                    //     var url = documentPictures[i].url;
                    //     HTML += "		<li><img src=\"" + url + "\"></li>";
                    // }
                    // HTML += "	</ol>";
                    // HTML += "</div>";
                    // HTML += "<div class=\"wendangControl\">";
                    // if (documentManual == 0) {
                    //     HTML += "	<div class=\"left\" style=\"color: #ddd;\">上一页</div>";
                    //     HTML += "	<div class=\"center\"> <span class=\"num\">" + documentPageNO + "</span>/<span class=\"totle\">" + documentPictures.length + "</span></div>";
                    //     HTML += "	<div class=\"right\" style=\"color: #ddd;\">下一页</div>";
                    // } else {
                    //     HTML += "	<div class=\"left\" onclick=\"leftPage()\">上一页</div>";
                    //     HTML += "	<div class=\"center\"> <span class=\"num\">" + documentPageNO + "</span>/<span class=\"totle\">" + documentPictures.length + "</span></div>";
                    //     HTML += "	<div class=\"right\" onclick=\"rightPage()\">下一页</div>";
                    // }
                    //
                    // HTML += "</div>";
                    HTML += '<div class="swiper-container " id="gallery">';
                    HTML += '  <div class="swiper-wrapper">';
                    // for (var i = 0; i < documentPictures.length; i++) {
                    //     var url = documentPictures[i].url;
                    //     HTML += '<div class="swiper-slide"><img data-src="' + url + '"  class="swiper-lazy"><div class="swiper-lazy-preloader"></div></div>'
                    // }
                    HTML += '  </div>';
                    HTML += '</div>'

                    HTML += '<div class="swiper-container " id="thumbs">';
                    HTML += '  <div class="swiper-wrapper">';
                    for (var i = 0; i < documentPictures.length; i++) {
                        var url = documentPictures[i].url;
                        HTML += '<div class="swiper-slide"><img src="' + url + '"></div>'
                    }
                    HTML += '  </div>';
                    HTML += '<div class="swiper-scrollbar"></div>'
                    HTML += '</div>'


                    //下载选项
                    // HTML += "<div class=\"wendangBottom\">";
                    // HTML += "	<div class=\"pptImg " + type + " \">";
                    // if (type == "doc" || type == "docx") {
                    //     HTML += "			<img src=\"img/img-world.png\"/>";
                    // } else {
                    //     HTML += "			<img src=\"img/img-ppt.png\"/>";
                    // }
                    // HTML += "	</div>";
                    // HTML += "	<div class=\"pptCon\">";
                    // HTML += "		<p class=\"pptName\">" + documentManager.name + "</p>";
                    // HTML += "	</div>";
                    HTML += "</div>";
                    // var top = ($('.contentBox').height() / 2 - 50).toFixed(1)
                    if (documentDownload == 0) {
                        HTML += "	<div class=\"pptBtn hide\">";
                        HTML += "	</div>";
                    } else {
                        HTML += "	<div class=\"pptBtn\" style=\"top:40%\"  onclick=\"downloadMask()\">";
                        HTML += "	</div>";
                    }
                    $(".wendang").html(HTML);

                    wendangJS(documentPictures, documentPageNO - 1);

                    //openPage(documentPageNO);
                    if (documentDownload == 0) {
                        $(".pptBtn").addClass("hide");
                    } else {
                        $(".pptBtn").removeClass("hide");
                    }
                    // if (documentManual == 0) {
                    //     $(".left").removeAttr("onclick");
                    //     $(".right").removeAttr("onclick");
                    //     $(".left").css("color", "#ddd");
                    //     $(".right").css("color", "#ddd");
                    //     // openPage(documentPageNO);
                    // } else {
                    //     $(".left").attr("onclick", "leftPage()");
                    //     $(".right").attr("onclick", "rightPage()");
                    //     $(".left").css("color", "#666666");
                    //     $(".right").css("color", "#666666");
                    // }
                } else {
                    layerOpen(data.message)
                }
            }
        });
    }
}

var firstImg;
var num;
var imgW;
var timer;

function wendangJS(documentPictures,realIndex) {
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
    // // 获取第一张图片的节点对象
    // firstImg = $('#ul li').first().clone();
    // //获取第几张图片
    // $(".totle").html($('#ul li').length);
    // // 添加到最后的位置 并设置 ul 的宽度
    // $('#ul').width($('#ul li').length * $('#ul img').width());
    // num = 1;
    // imgW = $('#ul img').width();
    // $(".num").html(num);
}

// function rightPage() {
//     // 下一张
//     num = num + 1;
//     if (num >= $('#ul li').length + 1) {
//         num = 1
//     }
//     moveImg();
//     $(".num").html(num);
// }
//
// function leftPage() {
//     // 上一张
//     num = num - 1;
//     if (num <= 0) {
//         num = $('#ul li').length;
//     }
//     moveImg();
//     $(".num").html(num);
// }
//
// function openPage(documentPageNO) {
//     if (documentPageNO >= $('#ul li').length + 1) {
//         documentPageNO = 1
//     } else if (documentPageNO <= 0) {
//         documentPageNO = $('#ul li').length;
//     }
//     num = documentPageNO
//     moveImg();
//     $(".num").html(documentPageNO);
// }
//
// //移动到指定的图片
// function moveImg() {
//     // 移动图片动画
//     $('#ul').stop().animate({left: (num - 1) * imgW * -1}, 400);
// }
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

//下载层
function downloadMask() {
    $(".downloadMask").slideDown(500);
    //$(".downloadMask").css("display", "block");
}

$(document).delegate(".downloadClose", "click", function () {
    $(".downloadMask").slideUp(500);
});
//关闭
$(document).delegate(".leftCheck", "click", function () {
    $(".downloadMask").slideUp(500);
});
//下载
$('.rightCheck').on("click", function () {
    var fileUrl;
    $(".MainUl li").each(function (index, item) {
        if ($(item).find("input").attr("checked") == "checked") {
            fileUrl = $(item).find("input").attr("fileUrl");
        }
    });
    window.location.href = fileUrl;
});

//所有文档
function selectDocumentList(enterpriseId) {
    $.ajax({
        type: "POST",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/document/getByEnterpriseId.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            enterpriseId: enterpriseId
        },
        success: function (data) {
            if (data.code == 1) {
                var documentList = data.data;
                var HTML = "";
                for (var i = 0; i < documentList.length; i++) {
                    var document = documentList[i];
                    var url = document.url;
                    var name = document.name;
                    var type = document.type;
                    HTML += "<li>";
                    HTML += "	<label>";
                    HTML += "		<div class=\"way-choose\">";
                    HTML += "			<input fileUrl=\"" + url + "\" type=\"radio\" name=\"radio\" class=\"agreement-checkbox choose-way\">";
                    HTML += "		</div>";
                    HTML += "		<div class=\"pptImg\">";
                    if (type == "doc" || type == "docx") {
                        HTML += "			<img src=\"img/img-world.png\"/>";
                    } else {
                        HTML += "			<img src=\"img/img-ppt.png\"/>";
                    }

                    HTML += "		</div>";
                    HTML += "		<div class=\"pptCon\">";
                    HTML += "			<p class=\"pptName\">" + name + "</p>";
                    HTML += "		</div>";
                    HTML += "	</label>";
                    HTML += "</li>";
                }
                $(".MainUl").html(HTML);
            } else {
                layerOpen(data.message)
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

// function getHeight() {
//     var winHeight = $(window).height();
//     $(".videoBox").height($(window).width() * 9 / 16);
//     var VideoBox = $(".videoBox").height();
//     var menuBox = $(".menuBox").height();
//     var talkCon = $(".newtalkCon").height();
//     var replyHeader = $(".replyHeader").height();
//     var drawTit = $(".drawTitle").height() + 1;
//     var voteButton = $(".voteendButton").height();
//     var voteConBtn = $(".voteconButton").height();
//     $(".contentBox").css("height", winHeight - VideoBox - menuBox - 2);
//     $(".livemask").css("height", winHeight - VideoBox);
//     $(".giftMask").css("height", winHeight - VideoBox - menuBox - 2);
//     $(".rewardMask").css("height", winHeight - VideoBox - 2);
//     $(".giftmask").css("height", winHeight);
//     $(".passMask").css("height", winHeight);
//     $(".contentBox>ul>li").css("height", winHeight - VideoBox - menuBox);
//     $(".replyBox").css("height", winHeight - VideoBox - menuBox);
//     $(".replyCon").css("height", winHeight - VideoBox - menuBox - replyHeader);
//     $(".talkLi").css("height", winHeight - VideoBox - menuBox - talkCon);
//     $(".notheme").css("height", winHeight - VideoBox - menuBox);
//     $(".videoMask").css("height", winHeight);
//     $(".drawMask").css("height", winHeight - VideoBox);
//     $(".drawContent").css("height", winHeight - VideoBox - drawTit);
//     $(".voteMask").css("height", winHeight - VideoBox);
//     $(".voteContent").css("height", winHeight - VideoBox - drawTit);
//     $(".rewardContent").css("height", winHeight - VideoBox - drawTit);
//     $(".voteResult").css("height", winHeight - VideoBox - drawTit - voteButton);
//     $(".voteMain").css("height", winHeight - VideoBox - drawTit - voteConBtn);
//     $(".downloadMask").css("height", winHeight - VideoBox);
//     $(".downloadContent").css("height", winHeight - VideoBox - drawTit);
//     $(".downloadeResult").css("height", winHeight - VideoBox - drawTit - voteButton);
//     $(".downloadMain").css("height", winHeight - VideoBox - drawTit - voteConBtn);
//     $(".ewmMask").css("height", winHeight);
//     $(".Mask").css("height", winHeight - VideoBox - menuBox);
//
// }