var url = window.location.search.substr(1),
    searchParams = new URLSearchParams(url),
    Params = {};

Params.roomId = searchParams.has('roomId') ? searchParams.get('roomId') : '0';
Params.fileId = searchParams.has('fileId') ? searchParams.get('fileId') : '0';
Params.userId = searchParams.has('userId') ? searchParams.get('userId') : '0';
Params.liveEventId = searchParams.has('liveEventId') ? searchParams.get('liveEventId') : '0';
Params.commentsMsgId = searchParams.has('commentsMsgId') ? searchParams.get('commentsMsgId') : '0';
$("input[name=roomId]").val(Params.roomId);
$("input[name=userId]").val(Params.userId);
$("input[name=fileId]").val(Params.fileId);


function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = decodeURI(window.location.search.substr(1)).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

//判断是否是微信浏览器的函数
function isWeiXin() { 	//window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
    var ua = window.navigator.userAgent.toLowerCase();
    //通过正则表达式匹配ua中是否含有MicroMessenger字符串
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true;
    } else {
        return false;
    }
}

//弹框
function layerOpen(msg, setTime) {
    var time = setTime || 2
    layer.open({
        content: msg
        , skin: 'msg'
        , time: time
    });
}

function layerConfim(msg, callback) {
    layer.open({
        content: msg
        , btn: '确定'
        , yes: function (index) {
            callback()
        }
    });
}

function getReviewList(data) {
    var list = data.data;
    var recallingDiv = "";
    var authType = []
    var authTxt = ['公开观看', '密码观看', '付费观看', '白名单观看', '登录观看']
    if (list.length == 0) {
        recallingDiv += "<div class='nomoreIcon' style='text-align:center'><img width='120' height='120' src='img/nomore.png' alt='' /><p style='color:#999'>无相关视频...</p></div>";
    } else {
        for (var i = 0; i < list.length; i++) {
            var mediaFile = list[i];
            recallingDiv += "<div class=\"reviewList\" id=\"fileId_" + mediaFile.fileId + "\" onclick=\"selectMediaFile(" + mediaFile.fileId + ")\" >";
            recallingDiv += "   <div class=\"reviewLeft\">";
            recallingDiv += "       <img src=\"" + mediaFile.fileImg + "\" alt=\"\" />";
            recallingDiv += "       <div class=\"reviewMask\">";
            recallingDiv += "           <span>" + mediaFile.fileDuration + "</span>";
            recallingDiv += "       </div>";
            recallingDiv += "   </div>";
            recallingDiv += "   <div class=\"reviewRight\">";
            recallingDiv += "       <div class=\"reviewRightTop\">" + mediaFile.fileName + "</div>";
            recallingDiv += "       <div class=\"reviewRightBottom\">" + mediaFile.createTime + "</div>";
            recallingDiv += "       <div class=\"reviewIcon\">";
            recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont icon-video reviewplay\"><span>" + mediaFile.viewNum + "次</span></i></div>";

            switch (mediaFile.authType) {
                case 1:
                    authType = 'icon-yan'
                    break
                case 3:
                    authType = 'icon-qian'
                    break
                default:
                    authType = 'icon-mima'
                    break

            }
            recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont " + authType + " gongkai\"><span>" + authTxt[mediaFile.authType - 1] + "</span></i></div>";
            recallingDiv += "       </div>";
            recallingDiv += "   </div>";
            recallingDiv += "</div>";
        }
    }

    $("#review").html(recallingDiv);
}

function errorfunction() {
    window.location.href = "http://" + h5Base + "/phone/error.html";
}

//当前粉丝数
var concernTotalTemp;

//企业关注  取消
function updateConcernStatus(enterpriseId, concernStatus, e) {

    if (window.event) {
        e.cancelBubble = true;
    } else {
        e.stopPropagation();
    }
    var userId = $("input[name=userId]").val();//用户ID
    var fileId = $("input[name=fileId]").val() || 0 ;//用户ID
    if (userId == 0) {
        var roomId = $("input[name=roomId]").val();//直播间ID
        window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId+ "&fileId=" + fileId;
        return;
    }
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/app/room/enterprise/concern.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            enterpriseId: enterpriseId,
            userId: userId,
            type: concernStatus
        },
        success: function (data) {
            console.log(data);
            var status = data.code;
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (status == 401) {
                window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId ;
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
                if (concernStatus == 0) {
                    $("#concernStatus").removeClass('yiguanzhu').addClass('guanzhu icon-jia').attr("onclick", "updateConcernStatus(" + enterpriseId + ",1,event)");
                    //$("#concernStatus").attr("src", "img/guanzhu.png").attr("onclick", "updateConcernStatus(" + enterpriseId + ",1)");

                    //改变粉丝数
                    concernTotalTemp = concernTotalTemp - 1;

                    var concernTotal = concernTotalTemp
                    if (concernTotal > 9999) {
                        concernTotal = (concernTotal / 10000).toFixed(1) / 1 + 'w'
                    }
                    $(".smallText").html(concernTotal);

                    layerOpen(data.message)
                } else {

                    //企业关注设置
                    getConcern (enterpriseId,function (json) {
                        $("#concernStatus").addClass('yiguanzhu ').removeClass('guanzhu icon-jia').attr("onclick", "updateConcernStatus(" + enterpriseId + ",0,event)");
                        //改变粉丝数
                        concernTotalTemp = concernTotalTemp + 1;

                        var concernTotal = concernTotalTemp
                        if (concernTotal > 9999) {
                            concernTotal = (concernTotal / 10000).toFixed(1) / 1 + 'w'
                        }
                        $(".smallText").html(concernTotal);
                        var hide = ''
                        if (json.isImg == 0) hide = 'hide'
                        //赋值
                        var concernMsg='<div class="concernMsg" id="concernMsg">' +
                            '    <div class="heade">'+json.slogan+'</div>' +
                            '        <div class="body">' +
                            '            <img src="'+json.advertisementImg+'" alt="" class="'+hide+'">' +
                            '            <div class="custom-desc">'+json.prompt+'</div>' +
                            '        </div>' +
                            '    </div>' +
                            '    <div class="close" id="popclose"></div>'


                        var popConcen = layer.open({
                            type: 1
                            ,content: concernMsg
                            ,className: 'popuo-concernMsg',

                        });

                        $('#popclose').on('click',function () {
                            layer.close(popConcen)
                        })

                    })




                    // layer.open({
                    //     content: data.message
                    //     , btn: ['是', '否']
                    //     , yes: function () {
                    //         openConfirm()
                    //     }
                    //     , no: function (index) {
                    //         layer.close(index)
                    //         //temporaryCancelApp()
                    //     }
                    // });


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
                }
            } else {
                layerOpen('关注或取消关注失败')
            }
        }
    });
}
//企业关注设置
function getConcern(enterpriseId,callback) {
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/enterprise/enterpriseSetup.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            enterpriseId: enterpriseId,
        },
        success:function (res) {
            var jsons = {
                slogan: '感谢您的关注',
                prompt: '下载天翼直播app,了解更多直播内容',
                isImg :  1,
                advertisementImg:'http://zb.tv189.com/images/img-popup.png'
            }
            if (res.code == 1){
                jsons = {
                    slogan: res.data.slogan ,
                    prompt: res.data.prompt ,
                    isImg : res.data.isImg ==1  ? res.data.isImg : 0,
                    advertisementImg: res.data.advertisementImg !=undefined ? res.data.advertisementImg : 'http://zb.tv189.com/images/img-popup.png'
                }
            }

            callback(jsons)
        }

    })

}
//企业主页
function zbjcompany(enterpriseId) {
    return false
    //window.location.href = "http://" + h5Base + "/home/index.html?enterpriseId=" + enterpriseId;
}

//获取企业信息
function getEnterprise(enterpriseId) {
    var Clogo = getStore('Clogo')
    //var Clogo = !getStore('Clogo') || !getExpireStore('Clogo')
    if (!Clogo || enterpriseId != Clogo.v.enterpriseId || !getExpireStore('Clogo')) {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/homepage/homepageenterprise.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                enterpriseId: enterpriseId,
            },
            success: function (data) {
                Clogo = {
                    enterpriseId: enterpriseId,
                    img: data.data.enterpriseImg || 'img/ELOGO.png'
                }
                $('.companyimg').css('background-image', 'url(' + Clogo.img + ')');
                setExpireStore('Clogo', Clogo)

            }
        });
    } else {
        Clogo = getExpireStore('Clogo')
        $('.companyimg').css('background-image', 'url(' + Clogo.img + ')');
    }
}

//现在打开
function openConfirm() {
    console.log("你确认了查看直播方更多直播内容");
    window.location.href = "http://zb.tv189.com/QR.html";
}

//暂时不用
function temporaryCancel() {
    console.log("你放弃查看直播方更多直播内容");
}

//现在打开
function openConfirmApp() {
    console.log("你确认了查看直播方更多直播内容");
//		window.location.href="http://apizb.tv189.com/version/wap/download/ios"
    var u = navigator.userAgent;
    console.log(u)
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    if (isAndroid) {
//		     window.location.href = "openwjtr://com.tyrbl.wjtr"; /***打开app的协议，有安卓同事提供***/
//		     window.setTimeout(function(){
        window.location.href = "http://apizb.tv189.com/version/wap/download/android";
        /***打开app的协议，有安卓同事提供***/
//		     },2000);
    } else {
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isiOS) {
//				var ifr = document.createElement("iframe");
//		        ifr.src = "openwjtr://com.tyrbl.wjtr"; /***打开app的协议，有ios同事提供***/
//		        ifr.style.display = "none";
//		        document.body.appendChild(ifr);
//		        window.setTimeout(function(){
//		          document.body.removeChild(ifr);
            window.location.href = "http://apizb.tv189.com/version/wap/download/ios";
            /***下载app的地址***/
//		        },2000);
        } else {
            window.location.href = "http://apizb.tv189.com/version/wap/download/ios";
            /***下载app的地址***/
        }
    }
}

//绑定手机号
function saveMessagerPhoneNumber() {
    console.log("弹框进行手机号绑定");
    $(".passMask").fadeIn(500);
}

//获取验证码
function getAuthenticode(obj) {
    var phoneNumber = $("input[name=phoneNumber]").val() || $("input[name=phoneNum]").val();
    // var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    // if (!myreg.test(phoneNumber)) {
    //     layerOpen('请输入正确的手机号')
    // }
    type = 'reg';
    if ($(obj).parents().hasClass('passInput')) {
        type = 'bindMobile'
    }
    if (phoneNumber ==undefined || !isPoneAvailable(phoneNumber.trim())) {
        layerOpen('请输入正确的手机号')
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
            type: type
        },
        success: function (data) {
            var status = data.code;
            if (status == 1) {
                selectTimer(obj);
            } else {
                layerOpen(data.message)
            }
        }
    });
}

var countdown = 60;
// function selectTimer() {
//     if (countdown == 0) {
//         $("#spenTimer").text("重新发送").attr("onclick", "getAuthenticode()");
//         countdown = 60;
//     } else {
//         $("#spenTimer").text("重新发送(" + countdown + "s)").removeAttr("onclick");
//         countdown--;
//         setTimeout(function () {
//             selectTimer();
//         }, 1000);
//     }
// }

function selectTimer(obj) {
    if (countdown == 0) {
        $(obj).removeAttr("disabled");
        $(obj).text("获取验证码").attr("onclick", "getAuthenticode(this)");
        countdown = 60;
    } else {
        $(obj).attr("disabled", true);
        $(obj).text("重新发送(" + countdown + "s)").removeAttr("onclick");
        countdown--;
        setTimeout(function () {
            selectTimer(obj)
        }, 1000);
    }

}


function isPoneAvailable(str) {
    var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
    if (!myreg.test(str)) {
        return false;
    } else {
        return true;
    }
}

function isEmail(str) {
    var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if (!myreg.test(str)) {
        return false;
    } else {
        return true;
    }
}


//提交绑定手机账户
function savePhone() {
    wchatHackInput()
    var phoneNumber = $("input[name=phoneNumber]").val();
    var userId = $("input[name=userId]").val();

    if (!isPoneAvailable(phoneNumber.trim())) {
        layerOpen('请输入正确的手机号')
        return false
    }

    var vpassword = $("input[name=vpassword]").val();
    if (vpassword == null || vpassword == undefined || vpassword == "") {
        layerOpen('验证码不能为空!')
        return false
    }
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/app/personal/bindMobile.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            userId: userId,
            phoneNum: phoneNumber,
            vpassword: vpassword
        },
        success: function (data) {
            if (data.code == 1) {
                $(".passMask").fadeOut();
            } else {
                layerOpen(data.message)
            }
        }
    });
}

//激活码观看提交
function openRoomActivationCode() {
    var codeEwm = $("input[name=codeEwm]").val();
    if (codeEwm == null || codeEwm == undefined || codeEwm == "") {
        layerOpen('激活码不能为空');
        return false;
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "激活码不能为空"
        // });
    } else {
        var fileId = $("input[name=fileId]").val();//文件ID
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/app/room/userFCode.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                terminalType: "h5",
                fileId: fileId,
                code: codeEwm
            },
            success: function (data) {
                var code = data.code;
                if (code == 1) {
                    $(".ewmMask").addClass("hide");
                    $("#ewmDiv").addClass("hide");
                    window.location.href = window.location.href;
                } else {
                    layerOpen(data.message);
                    return false
                    // $.DialogBySHF.Alert({
                    //     Width: 350,
                    //     Height: 200,
                    //     Title: "警告",
                    //     Content: data.message
                    // });
                }
            }
        });
    }
}

//支付观看
function openPay() {
    $(".ewmMask").addClass("hide");
    $("#payDiv").removeClass("hide");
}

// //提交绑定手机账户
// function savePhone() {
//     var phoneNumber = $("input[name=phoneNumber]").val();
//     var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
//     if (!myreg.test(phoneNumber)) {
//         layerOpen('请输入正确的手机号')
//         return false;
//     }
//     var vpassword = $("input[name=password]").val();
//     if (vpassword == null || vpassword == undefined) {
//         layerOpen('验证码不能为空')
//         return false;
//     }
//     $.ajax({
//         type: "GET",//请求方式 get/post
//         url: "http://" + tomcatBase + "/ilive/app/personal/bindMobile.jspx",
//         dataType: "jsonp",
//         jsonp: "callback",
//         cache: false,
//         data: {
//             terminalType: "h5",
//             userId: userId,
//             phoneNum: phoneNumber,
//             vpassword: vpassword
//         },
//         success: function (data) {
//             if (data.code == 1) {
//                 window.location.href = "http://" + h5Base + "/phone/live.html?roomId=" + roomId + "&userId=" + userId;
//             } else {
//                 layerOpen(data.message)
//                 return false;
//             }
//         }
//     });
// }

//激活码观看
function openJhm() {
    $(".ewmMask").addClass("hide");
    $("#jhmDiv").removeClass("hide");
}

// 微信支付二维码   orderType: 1礼物  2打赏 3直播付费  4 点播      productDesc:描述     totalAmount：金额（分）
function wechatSweepUnifyTheOrder(orderType, productDesc, totalAmount, contentId) {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    var terminalOrderType = window.navigator.appName;
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/wx/sweep/order.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            productDesc: encodeURI(productDesc),
            totalAmount: totalAmount,
            userId: userId,
            orderType: orderType,
            terminalOrderType: terminalOrderType,
            roomId: roomId,
            contentId: contentId
        },
        success: function (data) {
            var code = data.code;
            if (code == 1) {
                //二维码创建成功
                var codeUrl = data.codeUrl;
                $('#ewmImage').qrcode({
                    render: "canvas", // 渲染方式有table方式和canvas方式
                    width: 276, //默认宽度
                    height: 250, //默认高度
                    text: codeUrl, //二维码内容
                    typeNumber: -1, //计算模式一般默认为-1
                    correctLevel: 2, //二维码纠错级别
                    background: "#ffffff", //背景颜色
                    foreground: "#000000" //二维码颜色
                });
                $(".ewmMask").addClass("hide");
                $("#ewmDiv").removeClass("hide");
                var orderId = data.orderId;
                selectOrder(orderId);
            } else {
                layerOpen(data.message)
            }
        }
    });
}

//查询订单详情
function selectOrder(orderId) {
    $.ajax({
        type: "GET",//请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/wx/select/getOrderId.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            terminalType: "h5",
            orderId: orderId
        },
        success: function (data) {
            var code = data.code;
            console.log(data);
            if (code == 1) {
                var payStatus = data.payStatus;
                if (payStatus == 0) {
                    $("#ewmImage").html("");
                    $(".ewmMask").addClass("hide");
                    $("#ewmDiv").addClass("hide");
                    var orderType = data.orderType;
                    if (orderType == 1) {
                        //礼物
                        //saveUserGift();
                    } else if (orderType == 2) {
                        //打赏
                        //saveUserPlayRewardsPay();
                    } else if (orderType == 3) {
                        //直播付费
                        console.log(orderType);
                        window.location.href = window.location.href;
                    } else if (orderType == 4) {
                        //点播付费
                        console.log(orderType);
                    }
                } else {
                    setTimeout(function () {
                        selectOrder(orderId);
                    }, 1000);
                }
            } else {
                console.log(data.message);
            }
        }
    });
}

function checkPhone() {
    //判断手机类型
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    // var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    return isAndroid
}

// function dcVideo() {
//     // var html = "<div class='videoPlay'><i class='iconfont icon-ziyuan'></i></div>";
//     // $(".videoBox").append(html);
//     var video = document.getElementById('video');
//     if (video.attributes.src.value == "") {
//         return
//     }
//     if (checkPhone()) {
//         var player = document.getElementById("video");
//         var video = document.getElementById("video");
//         $(".videoBtn").removeClass('hide');
//         //点击播放
//         $('.btnPlay').on('click', function () {
//             $(".videoPlay").addClass('hide');
//             if (video.paused) {
//                 $(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
//                 $(".videoPlay").addClass('hide');
//                 $("video").css("z-index", "1");
//                 video.play();
//             } else {
//                 $(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
//                 $(".videoPlay").removeClass('hide');
//                 $("video").css("z-index", "0");
//                 video.pause();
//             }
//         });
//         //var video = document.getElementById('video');
//         // $(".videoPlay").on('click', function() {
//         //     $(".videoPlay").addClass('hide');
//         //     if(video.paused) {
//         //         video.play();
//         //         $(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
//         //         $(".videoPlay").addClass('hide');
//         //         $("video").css("z-index","0");
//         //     } else {
//         //         video.pause();
//         //         $(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
//         //         $(".videoPlay").removeClass('hide');
//         //         $("video").css("z-index","-1");
//         //     }
//         // })
//         //全屏事件
//         var itemHeight = window.screen.height; //获取手机高度
//         var itemWidth = window.screen.width; //手机宽度
//         //手机全屏点击
//         $('#fullScreenBtn').on('click', function (event) {
//             event.stopPropagation();
//             event.preventDefault();
//             var aaa = $(this).attr("aaa");
//             if (aaa == 0) {
//                 henping();
//                 $('#fullScreenBtn').attr('aaa', '1');
//
//
//             } else {
//                 shuping();
//                 $('#fullScreenBtn').attr('aaa', '0');
//
//             }
//         });
//         player.addEventListener('x5videoenterfullscreen', function () {
//             //设为屏幕尺寸
//             var width = window.screen.width;
//             var height = window.screen.height;
//             if (width > height) {
//                 width = [height, height = width][0];
//             }
//             player.style.width = width + 'px';
//             player.style.height = height + 'px';
//             //在body下添加样式类以控制全屏下的展示
//             document.body.classList.add('fullscreen');
//             player.style["object-position"] = "0px 0px"
//             getHeight()
//             shuping()
//         });
//         player.addEventListener('x5videoexitfullscreen', function () {
//             document.body.classList.remove('fullscreen');
//             player.style["object-position"] = "0px 0px"
//             $('.btnPlay').removeClass('icon-zanting').addClass("icon-play");
//             getHeight()
//             shuping();
//             $("body").css({"width": itemWidth, "height": itemHeight, "overflow": "hidden"});
//         }, false);
//         player.addEventListener('ended', function (e) {
//             player.pause();
//             $(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
//         }, false);
//     } else {
//
//         $("#video").attr("controls", "controls");
//         //$("#video").attr("poster","http://pic.zb.tv189.com/img/livebg_default.png");
//         //var video = document.getElementById('video');
//         // $(".videoPlay").on('click', function () {
//         //     if ($(".videoPlay").find('i').hasClass('hide')) return false;
//         //     //$(".videoPlay").addClass('hide');
//         //     if (video.paused) {
//         //         video.play();
//         //         $(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
//         //         $(".videoPlay").addClass('hide');
//         //         $("video").css("z-index", "0");
//         //     } else {
//         //         video.pause();
//         //         $(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
//         //         $(".videoPlay").removeClass('hide');
//         //         $("video").css("z-index", "-1");
//         //     }
//         // })
//     }
//
//     // $(".videoPlay").on('click', function () {
//     //     if ($(".videoPlay").find('i').hasClass('hide')) return false;
//     //     // if (checkPhone()) {
//     //     //     $(".videoBtn").removeClass('hide');
//     //     // }
//     //     if (video.paused) {
//     //         $(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
//     //         $(".videoPlay").addClass('hide');
//     //         $("video").css("z-index", "1");
//     //         video.play();
//     //     } else {
//     //         $(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
//     //         $(".videoPlay").removeClass('hide');
//     //         $("video").css("z-index", "0");
//     //         video.pause();
//     //     }
//     // })
//
//     $(window).resize(function () {
//         getHeight();
//     });
//
//     //横屏时方法
//     function henping() {
//         //$("body").css({"width":itemHeight,"height":itemWidth,"overflow":"hidden"});
//         $('.videoBox').css({
//             '-webkit-transform': 'rotate(90deg)',
//             'width': itemHeight,
//             'height': itemWidth,
//             // 'background': "#FFF!important",
//             'overflow': 'hidden',
//             'position': 'fixed', // eidt by caibin
//             "z-index": 10000000000, //edit by caibin
//
//         });
//         $('video').css({
//             "width": itemHeight,
//             "height": itemWidth,
//             "object-fit": "fill"
//             // "z-index": -1, //edit by caibin
//             // "object-position": "center center"
//         });
//         $(".videoBtn").css("width", "90%"); //edit by caibin
//         $(".firstBox").css("z-index", "-1");
//         $(".videoBox").offset({
//             top: 0,
//             left: 0,
//         });
//         $(".playBox").css("margin-left", "1rem");
//         $(".expandBox").css("margin-right", "0.8rem");
//     };
//
//     //竖屏时方法
//     function shuping() {
//         $("body").css({"width": itemWidth, "height": itemHeight, "overflow": "hidden"});
//         var hei = $(window).width() * 9 / 16;
//         var hei1 = $(".videoBtn").height();
//         $('.videoBox').css({
//             '-webkit-transform': 'rotate(0deg)',
//             'width': "100%",
//             'height': hei + hei1,
//             'overflow': 'hidden',
//             'position': 'relative',
//             'top': '0',
//             'left': '0'
//         });
//         $('video').css({
//             "width": itemWidth,
//             "height": itemHeight,
//             "object-position": "center top"
//         });
//         $(".videoBtn").css("width", "100%"); //add by caibin
//         $(".firstBox").css("z-index", "0");
//         $(".mainBox").css({
//             "z-index": 9999
//         })
//         $(".playBox").css("margin-left", "0.3rem");
//         $(".expandBox").css("margin-right", "0");
//     };
//
//     //高度改变时高度
//
//
//     //判断手机横竖屏状态：
//     // function orient() {
//     //     if (window.orientation == 90 || window.orientation == -90) {
//     //         //ipad、iphone竖屏；Andriod横屏
//     //         $("body").attr("class", "landscape");
//     //         orientation = 'landscape';
//     //         getHeight();
//     //         return false;
//     //     }
//     //     else if (window.orientation == 0 || window.orientation == 180) {
//     //         //ipad、iphone横屏；Andriod竖屏
//     //         $("body").attr("class", "portrait");
//     //         orientation = 'portrait';
//     //         getHeight();
//     //         return false;
//     //     }
//     // }
//     //
//     // //用户变化屏幕方向时调用
//     // $(window).bind('orientationchange', function (e) {
//     //     orient();
//     // });
// }

var winHeight
var VideoBox
var menuBox
var talkCon
var replyHeader
var drawTit
var voteButton
var voteConBtn

function getHeight() {

    winHeight = $(window).height();
    $(".videoBox").height($(window).width() * 9 / 16);
    // VideoBox = $(".videoBox").height();
    VideoBox = $(".videoBox")[0].offsetHeight;
    menuBox = $(".menuBox").height();
    talkCon = $(".newtalkCon").height();
    replyHeader = $(".replyHeader").height();
    drawTit = $(".drawTitle").height() + 1;
    voteButton = $(".voteendButton").height();
    voteConBtn = $(".voteconButton").height();
    $(".contentBox").css("height", winHeight - VideoBox - menuBox);
    $(".livemask").css("height", winHeight - VideoBox);
    $(".giftMask").css("height", winHeight - VideoBox - menuBox);
    $(".rewardMask").css("height", winHeight - VideoBox);
    $(".giftmask").css("height", winHeight);
    $(".passMask").css("height", winHeight);
    $(".contentBox>ul>li").css("height", winHeight - VideoBox - menuBox);
    $(".replyBox").css("height", winHeight - VideoBox - menuBox);
    $(".replyCon").css("height", winHeight - VideoBox - menuBox - replyHeader);
    $(".talkLi").css("height", winHeight - VideoBox - menuBox - talkCon);
    $(".notheme").css("height", winHeight - VideoBox - menuBox);
    $(".videoMask").css("height", winHeight);
    $(".drawMask").css("height", winHeight - VideoBox);
    $(".drawContent").css("height", winHeight - VideoBox - drawTit);
    $(".hongbaoMask").css("height", winHeight - VideoBox);
    $(".hongbaoContent").css("height", winHeight - VideoBox - drawTit);
    $(".voteMask").css("height", winHeight - VideoBox);
    $(".voteContent").css("height", winHeight - VideoBox - drawTit);
    $(".rewardContent").css("height", winHeight - VideoBox - drawTit);
    $(".voteResult").css("height", winHeight - VideoBox - drawTit - voteButton);
    $(".voteMain").css("height", winHeight - VideoBox - drawTit - voteConBtn);
    $(".downloadMask").css("height", winHeight - VideoBox);
    $(".downloadContent").css("height", winHeight - VideoBox - drawTit);
    $(".downloadeResult").css("height", winHeight - VideoBox - drawTit - voteButton);
    $(".downloadMain").css("height", winHeight - VideoBox - drawTit - voteConBtn);
    $(".ewmMask").css("height", winHeight);
    $(".Mask").css("height", winHeight - VideoBox - menuBox);

}

function isObject(val) {
    return val !== null && typeof val === 'object'
}

function setExpireStore(key, val, ex) {
    ex = 60 * 60 * 1000;
    setStore(key, {v: val, t: new Date().getTime() + ex})
}

function getExpireStore(key) {
    var obj = getStore(key)
    if (obj.t > new Date().getTime()) return obj.v
    else return false
}

function setStore(key, val, type) {
    type = 'localStorage';
    if (isObject(val)) window[type].setItem(key, JSON.stringify(val))
    else window[type].setItem(key, val)
}

function getStore(key, type) {
    type = 'localStorage';
    var val = window[type].getItem(key)
    try {
        return JSON.parse(val)
    } catch (e) {
        return val
    }
}

function videoPlay() {
    if ($(".videoPlay").find('i').hasClass('hide')) return false;

    if (myPlayer.paused) {
        //$(".btnPlay").removeClass('icon-play').addClass("icon-zanting");
        $(".videoPlay").addClass('hide');
        //$("video").css("z-index", "1");
        landscapeVideoClick()
        myPlayer.play();
    } else {
        //$(".btnPlay").removeClass('icon-zanting').addClass("icon-play");
        $(".videoPlay").removeClass('hide');
        //$("video").css("z-index", "0");
        myPlayer.pause();
    }
}


// if (!!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
//     window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", onResize, false);
// }else{
//     window.addEventListener( "resize", onResize, false);
// }
function landscapeVideoClick() {
    if ($(document.body).hasClass('landscape')) {
        if (myPlayer != undefined) {
            myPlayer.ready(function () {
                if (!this.isFullscreen()) {
                    //alert('2')

                    if (this.paused()) { //暂停
                        //alert('3')
                        if (!$('.videoPlay').hasClass('hide')) {//第一次加载
                            return
                        }
                        setTimeout(function () {
                            $('.vjs-fullscreen-control').click();
                            $('.vjs-play-control').click();

                        }, 350)

                    } else { //未暂停
                        //alert('4')
                        $('.mui-switch').removeClass('hide')
                        setTimeout(function () {
                            $('.vjs-fullscreen-control').click();
                        }, 350)
                    }

                } else {
                    // alert(!this.paused())
                    if (this.paused()) {
                        setTimeout(function () {
                            //alert('5')
                            $('.vjs-fullscreen-control').click();
                            $('.vjs-play-control').click();
                        }, 350)
                    }

                }


            })

        }
    }

}

function onResize() {
    //alert(window['orientation'])
    if (isPortrait()) { //横屏
      setTimeout(function () {
            landscape()

        }, 0);
    } else {
       setTimeout(function () {
            portrait()

        }, 0);


    }

}

function isPortrait() {
    var orientation = window['orientation'];
    if (orientation || orientation == 0) {
        if (orientation == 90 || orientation == -90) { //横屏状态
            return true;

        } else {
            return false;

        }
    } else {
        if ($(window).width() > $(window).height()) {
            return true;

        } else {
            return false;

        }
    }
}

// function orient() {
//     if (window.orientation == 90 || window.orientation == -90) {
//         //ipad、iphone竖屏；Andriod横屏
//         $("body").attr("class", "landscape");
//         orientation = 'landscape';
//         getHeight();
//         return false;
//     }
//     else if (window.orientation == 0 || window.orientation == 180) {
//         //ipad、iphone横屏；Andriod竖屏
//         $("body").attr("class", "portrait");
//         orientation = 'portrait';
//         getHeight();
//         return false;
//     }
// }

//用户变化屏幕方向时调用
$(window).bind('orientationchange', function (e) {
    onResize();
});
onResize
$("input").on("onblur", function () {
    //$("video").show();
    wchatHackInput()
});
//横屏
function landscape() {
    $("body").attr("class", "landscape");
    orientation = 'landscape';
    setTimeout(function () {
        getHeight();
    },350)
    landscapeVideoClick()
    //$(".videoBox").height($(window).width() * 3 / 4);


}

function portrait() {
    $("body").attr("class", "portrait");
    orientation = 'portrait';
    setTimeout(function () {
        getHeight();
    },350)

}

function wchatHackInput() {
    if (isWeiXin()) {
        var scrTop = document.body.scrollTop;

        setTimeout(function () {
            document.body.scrollTop = scrTop;
        }, 0)
    }
}

function flyDanmu (msg){
    if (danmuSwitch) {
        var item  ={"msg": msg}
        $('canvas').barrager([item]);
    }
}