var //url = window.location.search.substr(1),
    //searchParams = new URLSearchParams(url),
    Params = {};
// var ua = window.navigator.userAgent.toLowerCase();
var myPlayer = null,
    userId = 0,
    _height;

var judgeDeviceType = function () {
    var ua = window.navigator.userAgent.toLocaleLowerCase();
    var isIOS = /iphone|ipad|ipod/.test(ua);
    var isAndroid = /android/.test(ua);
    var isWeiXin = ua.match(/MicroMessenger/i) == 'micromessenger';
    return {
        isIOS: isIOS,
        isAndroid: isAndroid,
        isWeiXin: isWeiXin
    }
}();
Params.roomId = GetQueryString('roomId');
Params.fileId = GetQueryString('fileId');
Params.userId = GetQueryString('userId');
Params.comment = GetQueryString('comment');
Params.liveEventId = GetQueryString('liveEventId');
Params.commentsMsgId = GetQueryString('commentsMsgId');
$("input[name=roomId]").val(Params.roomId);
$("input[name=userId]").val(Params.userId);
$("input[name=fileId]").val(Params.fileId);


function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = decodeURI(window.location.search.substr(1)).match(reg);
    if (r != null) return unescape(r[2]);
    return 0;
}

//判断是否是微信浏览器的函数
function isWeiXin() { //window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
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
        content: msg,
        skin: 'msg',
        time: time
    });
}

function layerConfim(msg, callback) {
    layer.open({
        content: msg,
        btn: '确定',
        yes: function (index) {
            callback()
        }
    });
}

// function getReviewList(data) {
//     var list = data.data;
//     var recallingDiv = "";
//     var authType = []
//     var authTxt = ['公开观看', '密码观看', '付费观看', '白名单观看', '登录观看']
//     if (list.length == 0) {
//         recallingDiv += "<div class='nomoreIcon' style='text-align:center'><img width='120' height='120' src='img/nomore.png' alt='' /><p style='color:#999'>无相关视频...</p></div>";
//     } else {
//         for (var i = 0; i < list.length; i++) {
//             var mediaFile = list[i];
//             recallingDiv += "<div class=\"reviewList\" id=\"fileId_" + mediaFile.fileId + "\" onclick=\"selectMediaFile(" + mediaFile.fileId + ")\" >";
//             recallingDiv += "   <div class=\"reviewLeft\">";
//             recallingDiv += "       <img src=\"" + mediaFile.fileImg + "\" alt=\"\" />";
//             recallingDiv += "       <div class=\"reviewMask\">";
//             recallingDiv += "           <span>" + mediaFile.fileDuration + "</span>";
//             recallingDiv += "       </div>";
//             recallingDiv += "   </div>";
//             recallingDiv += "   <div class=\"reviewRight\">";
//             recallingDiv += "       <div class=\"reviewRightTop\">" + mediaFile.fileName + "</div>";
//             recallingDiv += "       <div class=\"reviewRightBottom\">" + mediaFile.createTime + "</div>";
//             recallingDiv += "       <div class=\"reviewIcon\">";
//             recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont icon-video reviewplay\"><span>" + mediaFile.viewNum + "次</span></i></div>";
//
//             switch (mediaFile.authType) {
//                 case 1:
//                     authType = 'icon-yan'
//                     break
//                 case 3:
//                     authType = 'icon-qian'
//                     break
//                 default:
//                     authType = 'icon-mima'
//                     break
//
//             }
//             recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont " + authType + " gongkai\"><span>" + authTxt[mediaFile.authType - 1] + "</span></i></div>";
//             recallingDiv += "       </div>";
//             recallingDiv += "   </div>";
//             recallingDiv += "</div>";
//         }
//     }
//
//     $("#review").html(recallingDiv);
// }

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
    if (userId == 0) {
        setlogin()
        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId;
        return;
    }
    $.ajax({
        type: "GET", //请求方式 get/post
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
            var status = data.code;
            //401 手机登录
            //402     绑定手机号
            //403    微信打开
            if (status == 401) {
                setlogin()
                //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
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
                setlogin('bindPhone')
            } else if (status == 403) {

            } else if (status == 1) {
                if (concernStatus == 0) {
                    $("#concernStatus").removeClass('yiguanzhu').addClass('guanzhu icon-jia').attr("onclick", "updateConcernStatus(" + enterpriseId + ",1,event)");
                    //$("#concernStatus").attr("src", "img/guanzhu.png").attr("onclick", "updateConcernStatus(" + enterpriseId + ",1)");

                    //改变粉丝数
                    concernTotalTemp = concernTotalTemp - 1;

                    var concernTotal = concernTotalTemp;
                    concernTotal = setNum(concernTotal);
                    $(".smallText").html(concernTotal);

                    layerOpen(data.message)
                } else {

                    //企业关注设置
                    getConcern(enterpriseId, function (json) {
                        $("#concernStatus").addClass('yiguanzhu ').removeClass('guanzhu icon-jia').attr("onclick", "updateConcernStatus(" + enterpriseId + ",0,event)");
                        //改变粉丝数
                        concernTotalTemp = concernTotalTemp + 1;

                        var concernTotal = concernTotalTemp;
                        concernTotal = setNum(concernTotal);
                        $(".smallText").html(concernTotal);

                        var hide = '';
                        if (json.isImg == 0) hide = 'hide';
                        //赋值
                        var concernMsg = '<div class="concernMsg" id="concernMsg">' +
                            '    <div class="heade">' + json.slogan + '</div>' +
                            '        <div class="body">' +
                            '            <img src="' + json.advertisementImg + '" alt="" class="' + hide + '">' +
                            '            <div class="custom-desc">' + json.prompt + '</div>' +
                            '        </div>' +
                            '    </div>' +
                            '    <div class="close" id="popclose"></div>';


                        var popConcen = layer.open({
                            type: 1,
                            content: concernMsg,
                            className: 'popuo-concernMsg',

                        });
                        // $('.videoBox').css('z-index', 0);

                        $('#popclose').on('click', function () {
                            layer.close(popConcen)
                            //$('.videoBox').css('z-index', 19891015)
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
function getConcern(enterpriseId, callback) {
    $.ajax({
        type: "GET", //请求方式 get/post
        url: "http://" + tomcatBase + "/ilive/enterprise/enterpriseSetup.jspx",
        dataType: "jsonp",
        jsonp: "callback",
        cache: false,
        data: {
            enterpriseId: enterpriseId,
        },
        success: function (res) {
            var jsons = {
                slogan: '感谢您的关注',
                prompt: '下载天翼直播app,了解更多直播内容',
                isImg: 1,
                advertisementImg: 'http://zb.tv189.com/images/img-popup.png'
            }
            if (res.code == 1) {
                jsons = {
                    slogan: res.data.slogan,
                    prompt: res.data.prompt,
                    isImg: res.data.isImg == 1 ? res.data.isImg : 0,
                    advertisementImg: res.data.advertisementImg != undefined ? res.data.advertisementImg : 'http://zb.tv189.com/images/img-popup.png'
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
            type: "GET", //请求方式 get/post
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

// //现在打开
// function openConfirm() {
//     console.log("你确认了查看直播方更多直播内容");
//     window.location.href = "http://zb.tv189.com/QR.html";
// }
//
// //暂时不用
// function temporaryCancel() {
//     console.log("你放弃查看直播方更多直播内容");
// }

//现在打开
// function openConfirmApp() {
//     console.log("你确认了查看直播方更多直播内容");
// //		window.location.href="http://apizb.tv189.com/version/wap/download/ios"
//     var u = navigator.userAgent;
//     console.log(u)
//     var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
//     if (isAndroid) {
// //		     window.location.href = "openwjtr://com.tyrbl.wjtr"; /***打开app的协议，有安卓同事提供***/
// //		     window.setTimeout(function(){
//         window.location.href = "http://apizb.tv189.com/version/wap/download/android";
//         /***打开app的协议，有安卓同事提供***/
// //		     },2000);
//     } else {
//         var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
//         if (isiOS) {
// //				var ifr = document.createElement("iframe");
// //		        ifr.src = "openwjtr://com.tyrbl.wjtr"; /***打开app的协议，有ios同事提供***/
// //		        ifr.style.display = "none";
// //		        document.body.appendChild(ifr);
// //		        window.setTimeout(function(){
// //		          document.body.removeChild(ifr);
//             window.location.href = "http://apizb.tv189.com/version/wap/download/ios";
//             /***下载app的地址***/
// //		        },2000);
//         } else {
//             window.location.href = "http://apizb.tv189.com/version/wap/download/ios";
//             /***下载app的地址***/
//         }
//     }
// }

//绑定手机号
// function saveMessagerPhoneNumber() {
//     console.log("弹框进行手机号绑定");
//     $(".passMask").fadeIn(500);
// }

//获取验证码
var countdown = 60;

function getAuthenticode(obj, oType) {
    var phoneNumber = $("input[name=phoneNumber]").val() || $("input[name=phoneNum]").val();
    // var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    // if (!myreg.test(phoneNumber)) {
    //     layerOpen('请输入正确的手机号')
    // }
    var type = 'reg';
    if (oType) {
        type = oType
    }
    if ($(obj).parents().hasClass('bind')) {
        type = 'bindMobile'
    }
    if ($(obj).parents().hasClass('form')) {
        type = 'form'
    }
    if (phoneNumber == undefined || !isPoneAvailable(phoneNumber.trim())) {
        layerOpen('请输入正确的手机号')
        return false
    }
    createAPI("http://" + tomcatBase + "/ilive/appuser/sendmsg.jspx", {
            phoneNum: phoneNumber,
            type: type
        })
        .then(function (res) {
            var status = res.code;
            if (status == 1) {
                selectTimer(obj, type);
            } else {
                layerOpen(res.message)
            }
        })
}

function selectTimer(obj, type) {
    if (countdown == 0) {
        $(obj).removeAttr("disabled");
        $(obj).text("获取验证码").attr("onclick", "getAuthenticode(this , '" + type + "')");
        countdown = 60;
    } else {
        $(obj).attr("disabled", true);
        $(obj).text("重新发送(" + countdown + "s)").removeAttr("onclick");
        countdown--;
        setTimeout(function () {
            selectTimer(obj, type)
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
        type: "GET", //请求方式 get/post
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
        var fileId = $("input[name=fileId]").val(); //文件ID
        $.ajax({
            type: "GET", //请求方式 get/post
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
    var roomId = $("input[name=roomId]").val(); //直播间ID
    var userId = $("input[name=userId]").val(); //用户ID
    var terminalOrderType = window.navigator.appName;
    $.ajax({
        type: "GET", //请求方式 get/post
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
        type: "GET", //请求方式 get/post
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

function setNum(num) {
    if (num > 9999) {
        num = (num / 10000).toFixed(1) / 1 + 'w'
    }
    return num

}

function checkPhone() {
    //判断手机类型
    var u = navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
    // var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    return isAndroid
}

function isObject(val) {
    return val !== null && typeof val === 'object'
}

function setExpireStore(key, val, ex) {
    ex = 60 * 60 * 1000;
    setStore(key, {
        v: val,
        t: new Date().getTime() + ex
    })
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
                        if (!$('.videoPlay').hasClass('hide')) { //第一次加载
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

    // setTimeout(function(){
    //     var height = window.innerHeight
    //     || document.documentElement.clientHeight
    //     || document.body.innerHeight;
    //     $('body').attr("height",height+"px")
    // },1000)

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


//用户变化屏幕方向时调用
$(window).on('orientationchange', function (e) {
    onResize();
});

onResize

// $("input").on("onblur", function () {
//     //$("video").show();
//     wchatHackInput()
// });

//横屏
function landscape() {
    $("body").attr("class", "landscape");
    orientation = 'landscape';
    setTimeout(function () {
        //getHeight();
    }, 350)
    landscapeVideoClick()
    //$(".videoBox").height($(window).width() * 3 / 4);


}

function portrait() {
    $("body").attr("class", "portrait");
    orientation = 'portrait';
    setTimeout(function () {
        //getHeight();
    }, 350)

}


function wchatHackInput() {
    if (judgeDeviceType.isWeiXin) {
        var scrTop = document.body.scrollTop;

        setTimeout(function () {
            document.body.scrollTop = scrTop;
        }, 0)
    }
}


function flyDanmu(msg) {
    if (danmuSwitch) {
        var item = {
            "msg": msg
        }
        $('canvas').barrager([item]);
    }
}

/************模版模块************/

var _template = {
    page: function () {
        var dom = '<div class="videoBox">\n' +
            '        <video id="video" controls="controls" preload="auto" playsinline="playsinline"\n' +
            '               webkit-playsinline="true"  x5-playsinline=""  x5-video-player-fullscreen="true" x5-video-player-type="h5" x5-video-orientation="portrait"  x-webkit-airplay="true" \n' +
            '               class="video-js vjs-default-skin vjs-big-play-centered gu-media--video" style="object-fit: contain;"></video>\n' +
            '        <div class="no-ended" id="endLive" style="display: none">\n' +
            '            <div class="textMask"></div>\n' +
            '        </div>\n' +
            '        <div id="playBg"></div>\n' +
            '        <div class=\'videoPlay\' id="videoPlay" style="display: none">\n' +
            '            <div class="no-timeing" id="timeStart" style="display: none">\n' +
            '                <div class="timeone">\n' +
            '                    <div class="top">\n' +
            '                       <p id="customContent"></p>\n' +
            '                       <div>直播倒计时</div>\n' +
            '                    </div>\n' +
            '                    <div id="countdown"></div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '        <div id="iLiveRollingAdvertising" class="marqueeBox"></div>\n' +
            '        <!--<div id="mui-switch" class="mui-switch hide" onclick="danmu(this)" style="transition-duration: 0.2s">-->\n' +
            '        <!--<div class="mui-switch-handle" style="transition-duration: 0.2s; transform: translate(0px, 0px);"></div>-->\n' +
            '        <!--</div>-->\n' +
            '        <!--<canvas id="canvas" class=""></canvas>-->\n' +
            '    </div>\n' +
            '    <div class="main-content">\n' +
            '        <div class="menuBox">\n' +
            '            <div class="swiper-container" id="swiperMenu">\n' +
            '                <ul class="swiper-wrapper"></ul>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '        <div class="contentBox">\n' +
            '            <div class="swiper-container" id="swiperContent">\n' +
            '                <div class="swiper-wrapper"></div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>';
        $('.page').append(dom)
    },
    review_page: function () {
        var dom = ' <div class="videoBox">\n' +
            '        <video id="video" controls preload="metadata" x5-playsinline playsinline webkit-playsinline="true" x-webkit-airplay="allow"\n' +
            '               class="video-js vjs-default-skin vjs-big-play-centered gu-media--video" style="object-fit: contain;">\n' +
            '        </video>\n' +
            '    </div>\n' +
            '    <div class="main-content">\n' +
            '        <div class="menuBox">\n' +
            '            <ul>\n' +
            '                <li class="liactive zbjj"><span>简介</span></li>\n' +
            '                <li class="talkLi"><span>评论</span></li>\n' +
            '            </ul>\n' +
            '        </div>\n' +
            '        <div class="contentBox">\n' +
            '            <div class="mescroll" id="mescroll">\n' +
            '                <div class="contentBox-all">\n' +
            '                    <div class="cell showDesc" id="showDesc">\n' +
            '                        <div class="title ellipsis liveTitle">天翼直播平台</div>\n' +
            '                        <div class="play-num"><i class="iconfont icon-video"></i><span class="viewNum">0</span>次播放</div>\n' +
            '                        <div class="links">简介<i class="iconfont icon-youjiantou"></i></div>\n' +
            '                    </div>\n' +
            '                    <div class="cell zbjcompany" id="zbjcompany">\n' +
            '                        <div class="companyimg"></div>\n' +
            '                        <div class="company">\n' +
            '                            <div class="small-liveTitle"><span id="companyName">天翼直播平台</span>\n' +
            '                                <span class="iconfont btn" id="concernStatus" style="display: none"></span>\n' +
            '                                <span class="zhuye btn iconfont icon-jia1"></span>\n' +
            '                            </div>\n' +
            '                            <div class="small-fans" id="concernTotal">粉丝数: <span class="smallText">0</span> 人</div>\n' +
            '                        </div>\n' +
            '                        <div class="links" style="display: none">去主页<i class="iconfont icon-youjiantou"></i></div>\n' +
            '                    </div>\n' +
            '                    <div class="cell reviewsAll" id="reviewsAll" style="display: none">\n' +
            '                        <div class="title">相关</div>\n' +
            '                        <div class="links" id="showAllReview">全部<i class="iconfont icon-youjiantou"></i></div>\n' +
            '                        <div class="swiper-container" id="reviews">\n' +
            '                            <div class="swiper-wrapper"></div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="cell fileDoc" id="fileDoc" style="display: none">\n' +
            '                        <div class="title">文档</div>\n' +
            '                        <div class="" id="document"></div>\n' +
            '                    </div>\n' +
            '                    <div class="cell talkLi" id="talkLi">\n' +
            '                        <div class="title">评论</div>\n' +
            '                        <div class="no-comment" id="no_comment"></div>\n' +
            '                        <div class="comment" id="comment"></div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '            <div class="footer">\n' +
            '                <div class="flexbox">\n' +
            '                    <div class="flex flex5" id="chatipt">\n' +
            '                        <div class="chatipt">\n' +
            '                            <span>聊几句吧~</span>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="flex flex" id="dianzan">\n' +
            '                        <div class="dianzan">\n' +
            '                            <i class="iconfont icon-xin"></i>\n' +
            '                            <span class="tag" id="dianzan-tag">0</span>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="flex" id="fenxiang">\n' +
            '                        <div class="fenxiang">\n' +
            '                            <i class="iconfont icon-fenxiang"></i>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="flex" id="wendang" style="display: none">\n' +
            '                        <div class="">\n' +
            '                            <i class="iconfont icon-wendang" style="font-size: .4rem"></i>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>';
        $('.page').append(dom);
    },
    showDesc: function (value) {
        var dom = '  <div class="title ellipsis liveTitle" id="liveTitle" style="width: 100%">' + value.liveTitle + '</div>\n' +
            '                            <div class="play-num"><i class="iconfont icon-video"></i>直播时间：<span class="viewNum">' + value.liveStartTime + '</span>\n' +
            '                            </div>\n' +
            '                            <div class="play-peopleNum">\n' +
            '                                <i class="peopleIcon iconfont icon-mi"><span class="onlineNumberSpan"></span></i>\n' +
            '                            </div>';
        $('#showDesc').html(dom);
    },
    //企业信息
    zbjcompany: function (enterprise) {
        /**caibin**/
        var cbCount = OtherJs_CbCount(enterprise.enterpriseId); //李曰
        // var cbCount = 0;
        // if (enterprise.enterpriseId == 1969) { //李曰
        //     cbCount = 15000
        // }
        /**caibin**/
        var concernTotal = cbCount + enterprise.concernTotal; //粉丝数
        concernTotalTemp = concernTotal;
        concernTotal = setNum(concernTotal); //关注人数

        var dom = '<div class="companyimg" style="background-image: url("' + enterprise.enterpriseImg + '")"></div>\n' +
            '                            <div class="company">\n' +
            '                                <div class="small-liveTitle"><span id="companyName">' + enterprise.enterpriseName + '</span>\n' +
            '                                    <span class="iconfont btn" id="concernStatus" style="display: block;"></span>\n' +
            '                                    <span class="zhuye btn iconfont icon-jia1"></span>\n' +
            '                                </div>\n' +
            '                                <div class="small-fans" id="concernTotal">粉丝数: <span class="smallText">' + concernTotal + '</span> 人\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                            <div class="links" style="display: none">去主页<i class="iconfont icon-youjiantou"></i>\n' +
            '                            </div>';

        $('#zbjcompany').html(dom);

        /**
         * 企业认证状态 0未提交认证 1认证中 4认证通过 5认证失败
         */
        if (enterprise.certStatus == 4) { //4认证通过
            var guanzhu = {
                type: 1,
                className: 'guanzhu icon-jia',
            };
            if (enterprise.concernStatus != 0) {
                guanzhu = {
                    type: 0,
                    className: 'yiguanzhu',
                }
            };
            //关注设置
            $('#concernStatus').css('display', 'block')
                .addClass(guanzhu.className)
                .attr('onclick', 'updateConcernStatus(' + enterprise.enterpriseId + ',' + guanzhu.type + ',event)')

        };
        getEnterprise(enterprise.enterpriseId); //企业logo
    },
    //评论输入框
    talkCon: function (type) {
        return '<div id="talkCon">\n' +
            '    <div class="talkCon box">\n' +
            '        <input type="text"  class="textarea input"  placeholder="聊几句吧" id="saytext" name="msgContent">\n' +
            '        <div class="talkBottom">\n' +
            '            <div class="hiddenDiv" style="display: none;"></div>\n' +
            '            <div class="newhiddenDiv" style="display: none;"></div>\n' +
            '            <div class="wen " style="display: none;" msgValue="2" ></div>' +
            '            <div class="biaoqing emotion" data="emoji"></div>\n' +
            '            <button class="sendMessage" disabled data-type="' + type + '" onclick="saveComments(this)">发送</button>\n' +
            '            <div class="emojiBox" style="display: none;">\n' +
            '                <section class="emoji_container"></section>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>';
    },
    //评论
    commentDom: function (value) {
        var cusotm = {
            className: '',
            topIcon: '',
            wenIcon: '',
            wencss: 'none'
        };
        if (value.topFlagNum == 1) { //置顶
            cusotm.className = ' topBox';
            cusotm.topIcon = 'stickIcon'
        };
        if (value.liveMsgType == 3) {
            cusotm.wenIcon = 'wenIcon'
        };
        if (value.replyType == 1) {
            cusotm.wencss = 'blcok'
        };
        return str = '<div class="talkline' + cusotm.className + '" id="msgId_' + value.commentsId + '">\n' +
            '          <div class="userImg"><img src="' + value.userImg + '"></div>\n' +
            '          <div class="commentText">\n' +
            '               <div class="inlineName">\n' +
            '                   <span class="senderName">' + value.commentsUser + ':<i class="' + cusotm.topIcon + '"></i></span>\n' +
            '                   <span class="createTime">' + value.createTime + '</span>\n' +
            '               </div>\n' +
            '             <div class="comment"><i class="' + cusotm.wenIcon + '"></i><span>' + value.comments + '' +
            '                 <div class="questionanswer" style="display: ' + cusotm.wencss + '">' +
            '                           <div class="arrow-up"></div>' +
            '                           <div class="questionName">' +
            '                               <i class="questionIcon"></i>' +
            '                               <span class="inlineName">' + value.replyName + '</span>' +
            '                           </div>' +
            '                           <div class="commentText">' + value.replyContent + '</div>' +
            '                   </div>' +
            '               </div>' +
            '           </div>' +
            '      </div>';
    },
    //话题
    theme: function (value) {
        var str = '';
        var top = value.top;
        var isPraise = false; //可以点赞
        var isPraiseTxt = '赞';
        var praiseMap = value.praiseMap;
        for (i in praiseMap) {
            if (praiseMap[i].userId == userId) {
                isPraise = true
                isPraiseTxt = '已赞'

            }
        };

        str += ' <div class="themeBox" id="msgId_' + value.msgId + '">\n' +
            '                                <div class="themeImg">\n' +
            '                                    <img src="' + value.senderImg + '">\n' +
            '                                </div>\n' +
            '                                <div class="themebody">\n' +
            '                                    <div class="themeHeader">\n' +
            '                                        <div class="inlineName">\n' +
            '                                            <span class="senderName">' + value.senderName + '</span>\n' +
            '                                            <i class="hottheme">' + value.senderImage + '</i>\n'
        if (top) {
            str += '                                        <i class="zdBox">置顶</i>\n'
        }
        str += '                                        </div>\n' +
            '                                    </div>\n' +
            '                                    <div class="themecontent">\n' +
            '                                        <div class="themeText">\n' +
            '                                            ' + value.content + '\n' +
            '                                        </div>\n' +
            //视频图片显示
            '                                        ' + this.themes_img_video(value) + '\n' +
            //end
            '                                        <div class="themeBottom">\n' +
            '                                            <div class="themeTime" datetime = ""></div>\n' +
            '                                            <div class="replyIcon">\n' +
            '                                                <div class="replys">\n' +
            '                                                    <div class="replys-icon">\n' +
            '                                                        <div class="messagePraise" onclick="saveILiveMessagePraise(this,' + value.msgId + ', ' + isPraise + ')"><i class="iconfont icon-xin"></i><span>' + isPraiseTxt + '</span>\n' +
            '                                                        </div>\n' +
            '                                                        <div class="commentsList" onclick="commentsList(this,' + value.msgId + ')"><i class="iconfont icon-huifu"></i><span>回复</span>\n' +
            '                                                        </div>\n' +
            '                                                    </div>\n' +
            '                                                </div>\n' +
            '                                                <div class="reply-more iconfont icon-more" onclick="replyMore(this)"></div>\n' +
            '                                            </div>\n' +
            '                                        </div>\n' +
            //评论
            '                                        ' + this.themes_pinglun(value) + '\n' +
            //end
            '                                    </div>\n' +
            '                                </div>\n' +
            '                            </div>';
        return str;

    },
    themes_img_video: function (value) {
        var str = '';
        //视频 图片
        if (value.msgType == 2 || value.msgType == 4) {
            var images = value.images;
            var imglayout;
            switch (images.length) {
                case 1:
                    imglayout = 'one'
                    break
                case 2:
                    imglayout = 'two'
                    break
                case 3:
                    imglayout = 'three'
                    break
                case 4:
                    imglayout = 'four'
                    break
                default:
                    imglayout = 'all'
                    break

            };
            str += '<div class="thumb ' + imglayout + '">';
            for (var j = 0; j < images.length; j++) {
                str += "<img class=\"toBig\" src=\"" + images[j] + "\"/>";
            };
            str += '</div>';

        } else if (value.msgType == 5 || value.msgType == 3) {
            str += '<div class="thumb">\n';
            str += '<video id="video_' + value.msgId + '" x5-playsinline playsinline webkit-playsinline="true" x-webkit-airplay="allow"  class="video video-js vjs-default-skin vjs-big-play-centered gu-media--video"  controls="controls" preload="auto"></video>';
            str += '</div>';
        }

        return str
    },
    themes_pinglun: function (value) {

        var str = '';
        var list = value.comments.list;
        var praiseMap = value.praiseMap;
        var Footershow = list != undefined || praiseMap.length > 0 ? '' : 'hide';

        var praiseshow = praiseMap.length > 0 ? '' : 'hide';
        var pinlunshow = list != undefined && list.length >= 0 ? '' : 'hide';

        str += '<div class="themeFooter ' + Footershow + ' " id="commentsList">\n';

        str += '<div class="themeFooter-top ' + praiseshow + '">\n';
        if (praiseMap.length > 0) {
            str += '<i class="iconfont icon-xin"></i>';
            praiseMap.forEach(function (dz) {
                str += '<span class="dzName">' + dz.userName + '</span>';
            })
        }
        str += '</div>';

        str += '<div class="themeFooter-center ' + pinlunshow + '">\n';
        if (list != undefined && list.length >= 0) {
            list.forEach(function (pinlun, index) {
                var hide = index > 2 ? 'hide' : '';
                str += ' <div class="pinlun ' + hide + '"><span class="plname">' + pinlun.commentsName + '</span>' + pinlun.comments + '</div>\n';
            })
            str += '</div>\n';
            if (list.length > 3) {
                str += '<div class="tishi discuss" onclick="selectCommentAll(this)">\n' +
                    '             <i class="iconfont icon-laquo"></i> 查看全部' + list.length + '条评论\n' +
                    '    </div>\n';
            }
        }
        str += '   </div>\n';

        return str
    },
    //回看
    reviewsAll: function (res) {
        var arr = [];
        var reviews = res.data;
        var str = ' <div class="title">' + res.hkmc + '</div>\n' +
            '                            <div class="links" id="showAllReview">全部<i class="iconfont icon-youjiantou"></i></div>\n' +
            '                            <div class="swiper-container" id="reviews">\n' +
            '                                <div class="swiper-wrapper">\n';
        reviews.forEach(function (value) {
            str +=
                '<div class="swiper-slide">\n' +
                '  <div class="review-group" onclick="changeVideo(' + value.fileId + ')">\n' +
                '    <div class="review-img">\n' +
                '      <img src="' + value.fileImg + '" alt="">\n' +
                '      <div class="reviewMask"><span>' + value.fileDuration + '</span></div>\n' +
                '    </div>\n' +
                '    <div class="review-title">' + value.fileName + '</div>\n' +
                '  </div>\n' +
                '</div>\n';

        });
        str += '</div></div>';
        return str
    },
    //弹层回看
    show_all_review: function (data) {
        var arr = [];
        var authType = [];
        var authTxt = ['公开观看', '密码观看', '付费观看', '白名单观看', '登录观看'];
        data.forEach(function (value) {
            var recallingDiv = "<div class=\"reviewList\" id=\"fileId_" + value.fileId + "\" onclick=\"changeVideo(" + value.fileId + ")\" >";
            recallingDiv += "   <div class=\"reviewLeft\">";
            recallingDiv += "       <img src=\"" + value.fileImg + "\" alt=\"\" />";
            recallingDiv += "       <div class=\"reviewMask\">";
            recallingDiv += "           <span>" + value.fileDuration + "</span>";
            recallingDiv += "       </div>";
            recallingDiv += "   </div>";
            recallingDiv += "   <div class=\"reviewRight\">";
            recallingDiv += "       <div class=\"reviewRightTop\">" + value.fileName + "</div>";
            recallingDiv += "       <div class=\"reviewRightBottom\">" + value.createTime + "</div>";
            recallingDiv += "       <div class=\"reviewIcon\">";
            recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont icon-video reviewplay\"><span>" + setNum(value.viewNum) + "次</span></i></div>";

            switch (value.authType) {
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
            recallingDiv += "           <div class=\"reviewIcondiv\"><i class=\"reviewplayIcon iconfont " + authType + " gongkai\"><span>" + authTxt[value.authType - 1] + "</span></i></div>";
            recallingDiv += "       </div>";
            recallingDiv += "   </div>";
            recallingDiv += "</div>";

            arr.push(recallingDiv)
        });
        //$('#review').html()
        return arr.join('')
    },
    //投票
    tpdom: function () {
        return '<div class="voteContent" id="vodeContentDiv"></div>'
    },
    //抽奖
    drawdom: function () {
        return '<div id="prizrContentDiv"  ></div>'
    },
    //登陆弹层
    popDom: function () {
        if (window.popDom !== undefined && window.popDom) return;
        var dom = '<div class="container" id="mainContent">\n' +
            '    <div class="main">\n' +
            '        <div class="header">\n' +
            '            <div id="welcomeMsg" class="welcomeMsg"></div>\n' +
            '            <div class="" id="roomName"></div>\n' +
            '        </div>\n' +
            '        <div class="loginBox">\n' +
            '            <div class="login-input-item">\n' +
            '                <div class="login" style="display: none">' +
            '                    <div class="inputBox phoneNumber">\n' +
            '                        <label><span>+86</span></label>\n' +
            '                        <input type="tel" class="input" placeholder="请输入您的手机号" maxlength="11" name="phoneNumber">\n' +
            '                    </div>\n' +
            '                    <div class="inputBox">\n' +
            '                        <label><span>验证码</span></label>\n' +
            '                        <input type="tel" class="input"  placeholder="动态密码" maxlength="6" name="password">\n' +
            '                        <span class="yzmBtn input" onclick="getAuthenticode(this)" id="spenTimer">获取验证码</span>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '                <div class="login_passpord" style="display: none">' +
            '                    <div class="inputBox phoneNumber">\n' +
            '                        <input class="input" type="text" placeholder="请输入您的观看密码" name="roomPassword" maxlength="4">\n' +
            '                    </div>' +
            '                </div>' +
            '                <div class="FCode_passpord" style="display: none">' +
            '                    <div class="inputBox phoneNumber">\n' +
            '                        <input class="input" type="text" placeholder="请输入您的观看码" name="codeEwm" maxlength="6">\n' +
            '                    </div>' +
            '                </div>' +
            '            </div>\n' +
            '            <div class="text-center">\n' +
            '                <button type="button" class="livebtn" id="loginUser" onclick="loginUser(this)">进入观看</button>\n' +
            '                <a class="login-input-item third_login" style="display: none" href="javascript:;" onclick="loginSubmit()">手机号免密登录</a>\n' +
            '            </div>\n' +
            '            <div class="goback">我有已绑定观看的账户<br><a href="javascript:;" onclick="goback()">去登录</a></div>' +
            '        </div>\n' +
            '        <div class="permission" style="display: none;">\n' +
            '            <div class="welcomeMsg">您即将用以下手机号进行认证观看</div>\n' +
            '            <div class="edit-phone">\n' +
            '                <div class="userPhone"></div>\n' +
            '                <a href="javascript:;" onclick="changes()">修改</a>\n' +
            '            </div>\n' +
            '            <div class="text-center loginpermissions">\n' +
            '                <button type="button" class="livebtn" onclick="loginpermissions()">进入直播</button>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>\n' +
            '</div>';
        $(document.body).append(dom);
        allinput()
        window.popDom = true;
    },
    //引导页
    guide: function () {
        var dom = '<div class="guide">\n' +
            '    <div class="guide-box">\n' +
            '        <span class="guide-num">5</span><span>跳过</span>\n' +
            '    </div>\n' +
            '</div';
        $(document.body).append(dom);
    }

};


window.changeVideo = function (fileId) {
    window.location.href = "http://" + h5Base + "/phone/review.html?roomId=" + Params.roomId + "&fileId=" + fileId;
}

//发送评论
function saveComments(obj) {

    if (userId == 0) {
        setlogin()
        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId;
        return;
    }
    var Ty = $(obj).data('type');
    var msgContent = $.trim($("input[name=msgContent]").val()); //发送内容
    if (msgContent.replace("/^s+|s+$/g+", "") == "" || msgContent == undefined) {
        layerOpen('请输入消息内容')
        return false
    } else {
        switch (Ty) {
            case 'review': //回看评论
                layer.closeAll()
                createAPI("http://" + tomcatBase + "/ilive/app/room/vod/pushcomments.jspx", {
                    userId: userId,
                    fileId: Params.fileId,
                    comments: encodeURI(msgContent)
                }).then(function (res) {
                    if (res.code == -1 || res.code == 0) {
                        layerOpen(res.message)
                    } else if (res.code == 1) {
                        var str = _template.commentDom(res.data);
                        if ($('.mescroll-empty').length > 0) $('.mescroll-empty').hide()
                        //如果有置顶数据追加置顶下面
                        var topLen = $('#comment').find('.topBox').length;
                        if (topLen > 0) {
                            $('#comment').find('.topBox').eq(topLen - 1).after(str)
                        } else {
                            $('#comment').prepend(str)
                        }

                        $("textarea[name=msgContent]").val("");


                    } else {
                        layerConfim(res.message, function () {
                            setlogin()
                            //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId + "&userId=" + userId
                        })
                    }
                });
                break
            case 'theme':
                //图文评论
                const msgId = $("input[name=commentsMsgId]").val();

                if (msgId == 0) {
                    layerOpen('请选择评论话题')
                    return false
                }
                layer.closeAll()
                createAPI("http://" + tomcatBase + "/ilive/saveComments.jspx", {
                    userId: userId,
                    msgId: msgId,
                    comments: encodeURI(msgContent)
                }).then(function (res) {
                    if (res.code == 1) {
                        var pinlun = JSON.parse(res.comments);
                        var str = '<div class="pinlun"><span class="plname">' + pinlun.commentsName + '</span>' + pinlun.comments + '</div>\n';
                        $('#msgId_' + pinlun.msgId)
                            .find('.themeFooter').removeClass('hide')
                            .find('.themeFooter-center').removeClass('hide').prepend(str)

                    } else {
                        layerOpen(res.message)
                    }
                })
                break
            default:
                //直播评论
                const liveMsgType = $(".wen").attr("msgValue"); //发送消息类型
                if (liveMsgType == 2 || liveMsgType == 3) {
                    $(obj).attr("disabled", true);
                    createAPI("http://" + tomcatBase + "/ilive/sendMessage.jspx", {
                        userId: userId,
                        roomId: Params.roomId,
                        liveMsgType: liveMsgType,
                        content: encodeURI(msgContent),
                        liveEventId: liveEventId,
                        msgType: 1
                    }).then(function (res) {
                        if (res.code == 1) {
                            layer.closeAll()
                            $(".wen").attr("msgValue", "2");
                            $("input[name=msgContent]").val("");
                            $('.commentBox').scrollTop(0);
                        } else {
                            layerOpen(res.message)
                        }
                    })
                } else {
                    layerOpen('发送消息类型错误')
                }
        }

    }
}


//弹层
function popup(obj) {
  
    obj.id = obj.id || 0;
    var index = layer.open({
        type: 1,
        title: obj.title,
        className: 'CB',
        content: showPopupContent(obj),
        anim: 'up',
        shadeClose: false,
        style: ' height:' + _height + 'px; '
    });

    $('.layui-m-layershade').css('display', 'none')
    $('.CB h3').on('click', function () { //关闭按钮
        layer.close(index)
        popupDisable = false
    })
}

// function showPopupContent(obj) {
//     return '<div class="showPopupContent">' +
//         '  <div class="header"><span class="ellipsis">' + obj.title + '</span><i class="close iconfont icon-close"></i></div>' +
//         '  <div class="body mescroll" id="mescroll2">' + obj.content + '</div>' +
//         '</div>'
// }
function showPopupContent(obj) {

    var dom = '<div class="showPopupContent">'
    if (obj.id =='review'){
        dom += '<div class="body mescroll" style="" id="mescroll_' + obj.id + '">' + obj.content + '</div>';
    }else{
        dom += '<div class="body" style="height:100%" id="mescroll_' + obj.id + '">' + obj.content + '</div>';
    } dom +=  '</div>'
    return dom
}

//打开评论框
function chatiptOpen(type) {

    //如果是禁言
    // if (!gable_estoppelType) {
    //     return false
    // }

    if (userId == 0) {
        setlogin()
        //window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + Params.roomId + "&fileId=" + Params.fileId;
        return false;
    }
    layer.open({
        type: 1,
        className: 'CB',
        content: _template.talkCon(type),
        anim: 'up'
    });
    A_Emoji($('.box'));
    allinput()
    $('.textarea').focus().on('input propertychange', function () {
        if ($('.textarea').val().length != 0) {
            $(".sendMessage").removeAttr("disabled")
                .css("background", "#0084ff");
        } else {
            $(".sendMessage").css("background", "#b2daff");
        }
    });
    $(".talkCon .wen").on('click', function () {
        if ($(this).attr("msgValue") == 3) {
            $(this).css({
                "background": "url(img/graywen.png) no-repeat",
                "background-size": "100% 100%"
            });
            $(this).attr("msgValue", "2");
        } else {
            $(this).css({
                "background": "url(img/question.png) no-repeat",
                "background-size": "100% 100%"
            });
            $(this).attr("msgValue", "3");
        }
    });

    if (judgeDeviceType.isIOS) { //键盘收起页面空白问题
        document.body.scrollTop = 350;
    }

}

//登陆设置
function setlogin(type) {
    _template.popDom()
    $('.page').hide()
    var $mainContent = $('#mainContent');
    $mainContent.show();
    //鉴权进来
    permissionsjump = window.sessionStorage.getItem('permissionsjump')
    if (permissionsjump == 1) {
        var plinfo = JSON.parse(window.sessionStorage.getItem('plinfo'));
        $mainContent.find('.permission').show()
        $mainContent.find('.loginBox').hide()
        $mainContent.find('.userPhone').html(plinfo.userphone)
    }

    switch (type) {
        case 'password':
            $mainContent.find('.login').css('display', 'none')
            $mainContent.find('.third_login').css('display', 'none')
            $mainContent.find('.FCode_passpord').css('display', 'none')
            $mainContent.find('.goback').css('display', 'none')

            $mainContent.find('.login_passpord').css('display', 'block')
            $mainContent.find('#loginUser').data('type', 'password')

            break
        case 'FCode':
            $mainContent.find('.login').css('display', 'none')
            $mainContent.find('.third_login').css('display', 'none')
            $mainContent.find('.login_passpord').css('display', 'none')
            $mainContent.find('.FCode_passpord').css('display', 'block')
            $mainContent.find('.goback').css('display', 'block')
            $mainContent.find('#loginUser').data('type', 'FCode')
            break
        case 'bindPhone':
            $mainContent.find('.login_passpord').css('display', 'none')
            $mainContent.find('.FCode_passpord').css('display', 'none')
            $mainContent.find('.goback').css('display', 'none')
            $mainContent.find('.login').css('display', 'block')
            $mainContent.find('.third_login').css('display', 'block')
            $mainContent.find('#loginUser').data('type', 'bind')
            $mainContent.find('#spenTimer').attr("onclick", "getAuthenticode(this , 'bindMobile')");
            break
        default:
            $mainContent.find('.login_passpord').css('display', 'none')
            $mainContent.find('.FCode_passpord').css('display', 'none')
            $mainContent.find('.goback').css('display', 'none')
            $mainContent.find('.login').css('display', 'block')
            $mainContent.find('.third_login').css('display', 'block')
            $mainContent.find('#loginUser').data('type', 'login')
            break
    }

    if (Params.fileId != 0) {
        url = "http://" + tomcatBase + "/ilive/app/room/vod/getVodMsg.jspx";
        data = {
            'fileId': Params.fileId
        }
    } else {
        url = "http://" + tomcatBase + "/ilive/selectRoom.jspx";
        data = {
            'roomId': Params.roomId
        }

    }
    if (type == 'bindPhone') {
        $mainContent.find('#roomName').text('绑定手机号');
        return
    }
    createAPI(url, data).then(function (res) {
        if (res.code == 1) {
            var iLiveLiveRoom = JSON.parse(res.iLiveLiveRoom);
            var liveEvent = iLiveLiveRoom.liveEvent;
            var liveTitle = liveEvent.liveTitle;
            var welcomeMsg = liveEvent.welcomeMsg;
            if (welcomeMsg == undefined || welcomeMsg == '') {
                welcomeMsg = "欢迎观看";
            }
            document.title = liveTitle;
            $mainContent.find('#welcomeMsg').text(welcomeMsg);
            $mainContent.find("#roomName").text(liveTitle);
        }
    })

}

/**
 * 鉴权登陆
 * @param obj
 * @returns {boolean}
 */

function loginUser(obj) {
    var phoneNumber = $("input[name=phoneNumber]").val();
    var password = $("input[name=password]").val();
    var roomPassword = $("input[name=roomPassword]").val();
    var codeEwm = $("input[name=codeEwm]").val();
    var type = $(obj).data('type');
    $('.page').show();
    switch (type) {
        case 'login':
            if (phoneNumber == undefined || phoneNumber.replace("\s+", "") == "") {
                layerOpen('登录用户不能为空!')
                return false
            }
            if (password == undefined || password.replace("\s+", "") == "") {
                layerOpen('验证码不能为空!')
                return false
            }

            createAPI("http://" + tomcatBase + "/ilive/appuser/reg.jspx", {
                phoneNum: phoneNumber,
                vpassword: password,
                orginal: 1,
                type: 2
            }).then(function (res) {
                if (res.code == 1) {
                    if (Params.comment == 1) {
                        window.location.href = "http://" + h5Base + "/phone/comment.html?roomId=" + Params.roomId;
                        return false
                    }
                    $('#mainContent').hide();
                    clearLoginVale()
                    $('.page').children().length > 0 ? userId = res.data.userId : init();

                } else {
                    layerOpen(res.message)
                }
            })
            break
        case 'bind':
            if (phoneNumber == undefined || phoneNumber.replace("\s+", "") == "") {
                layerOpen('登录用户不能为空!')
                return false
            }
            if (password == undefined || password.replace("\s+", "") == "") {
                layerOpen('验证码不能为空!')
                return false
            }

            createAPI("http://" + tomcatBase + "/ilive/app/personal/bindMobile.jspx", {
                userId: userId,
                phoneNum: phoneNumber,
                vpassword: password
            }).then(function (res) {
                if (res.code == 1) {
                    $('#mainContent').hide();
                    clearLoginVale()
                    $('.page').children().length > 0 ? userId = res.data.userId : init();

                } else {
                    layerOpen(res.message)
                }
            })
            break
        case 'password':
            if (roomPassword == "" || roomPassword == undefined) {
                layerOpen('验证密码不能为空')
                return false;
            }

            if (Params.fileId != 0) {
                createAPI("http://" + tomcatBase + "/ilive/app/room/vod/checkFilePassword.jspx", {
                    fileId: Params.fileId,
                    passWord: roomPassword
                }).then(function (res) {
                    if (res.code == 0) {
                        layerOpen(res.message)
                        return false
                    }
                    $('#mainContent').hide()
                    clearLoginVale()
                    init()
                })
            } else {
                //直播
                createAPI("http://" + tomcatBase + "/ilive/app/room/checkRoomPassword.jspx", {
                    roomId: Params.roomId,
                    roomPassword: roomPassword
                }).then(function (res) {
                    if (res.code == 0) {
                        layerOpen(res.message)
                        return false
                    }
                    $('#mainContent').hide()
                    clearLoginVale()
                    init()
                })

            }


            break
        case 'FCode':
            if (codeEwm == "" || codeEwm == undefined) {
                layerOpen('观看码不能为空')
                return false
            }
            createAPI("http://" + tomcatBase + "/ilive/app/room/userFCode.jspx", {
                roomId: Params.roomId,
                fileId: Params.fileId,
                code: codeEwm
            }).then(function (res) {
                if (res.code == 1) {
                    // $('.page').html('')
                    $('#mainContent').hide()
                    clearLoginVale()
                    init()
                } else if (res.code == 404) { //已被绑定
                    window.location.href = "http://" + h5Base + "/phone/permissions2.html?roomId=" + Params.roomId;
                } else {
                    layerOpen('观看码验证失败');
                }

            })
            break
    }

}

function clearLoginVale() {
    $("input[name=phoneNumber]").val('');
    $("input[name=password]").val('');
    $("input[name=roomPassword]").val('');
    $("input[name=codeEwm]").val('');
}

//第三方登陆
function loginSubmit() {
    layer.open({
        type: 2,
        shadeClose: false
    });
    setTimeout(function () {
        createAPI("http://" + tomcatBase + "/ilive/appuser/getPhoneUrl.jspx", {
                roomId: Params.roomId,
                fileId: Params.fileId
            })
            .then(function (res) {
                var status = res.code;
                if (status == 1) {
                    var url = res.url;
                    window.location.replace(url)
                } else {
                    layerOpen(res.message)
                }
            })
    }, 2000)

}

//确认鉴权进来
function loginpermissions() {
    //鉴权进来
    if (permissionsjump == 1) {
        window.sessionStorage.setItem('permissionsjump', 0)
        $('#mainContent').hide()
        init()
    }
}

function changes() {
    window.sessionStorage.removeItem('permissionsjump')
    $('.permission').hide()
    $('.loginBox').show()
}

function goback() {
    var $mainContent = $('#mainContent');
    $mainContent.find('.login_passpord').css('display', 'none');
    $mainContent.find('.FCode_passpord').css('display', 'none');
    $mainContent.find('.goback').css('display', 'none');
    $mainContent.find('.login').css('display', 'block');
    $mainContent.find('.third_login').css('display', 'block');
    $mainContent.find('#loginUser').data('type', 'login');
}

window.onload = function () {
    //分享弹层
    $('#fenxiang').on('click', function () {
        $('#btnIcons').click()
        $('#customshade').css('display', 'block')
    })
    //弹层关闭
    $('#customshade').on('click', function () {
        $(this).css('display', 'none')
    })
    // //分享弹层
    // window.fenxiang.onclick = function (e) {
    //     $('#customshade').css('display', 'block')
    //
    // }
    //弹层关闭
    //     window.customshade.onclick = function () {
    //         $(this).css('display', 'none')
    //
    //     }
    $('#dianzan').on('click', function () {
        if ($(this).find('i').hasClass('checked')) return
        saveMediaFile()
    })
    //文档弹层
    $('#wendang').on('click', function () {
        var docId = $('.document').eq(0).data('docid');
        fileDocDetail(docId)
    })

}


// 监听输入框的软键盘弹起和收起事件
function listenKeybord($input) {
    if (judgeDeviceType.isIOS) {
        $input.addEventListener('focus', function () {
            console.log('IOS 键盘弹起啦！');
        }, false)

        $input.addEventListener('blur', function () {
            wchatHackInput()
            // var wechatInfo = window.navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i);
            // if (!wechatInfo) return;
            // var wechatVersion = wechatInfo[1];
            // var version = (navigator.appVersion).match(/OS (\d+)_(\d+)_?(\d+)?/);
            //
            // if (+wechatVersion.replace(/\./g, '') >= 674 && +version[1] >= 12) {
            //     window.scrollTo(0, Math.max(document.body.clientHeight, document.documentElement.clientHeight));
            // }
            console.log('IOS 键盘收起啦！');

        })
    }

    if (judgeDeviceType.isAndroid) {
        var originHeight = document.documentElement.clientHeight || document.body.clientHeight;
        window.addEventListener('resize', function () {
            var resizeHeight = document.documentElement.clientHeight || document.body.clientHeight;
            if (originHeight < resizeHeight) {
                console.log('Android 键盘收起啦！');

            } else {
                console.log('Android 键盘弹起啦！');
                wchatHackInput()
            }
            originHeight = resizeHeight;
        }, false)
    }
}

function allinput() {
    var $inputs = $('.input')
    for (var i = 0; i < $inputs.length; i++) {
        listenKeybord($inputs[i]);
    }
}

// function activeElementScrollIntoView(activeElement, delay) {
//     if (activeElement.tagName == 'INPUT' || activeElement.tagName == 'TEXTAREA' || editable === '' || editable) {
//         setTimeout(function () {
//                 activeElement.scrollIntoView();
//             }, delay
//         )
//     }
//
// }

// for (var i = 0; i < $inputs.length; i++) {
//     listenKeybord($inputs[i]);
// }
/*去掉iphone手机滑动默认行为*/
// $('body').on('touchmove', function (event) {
//     event.preventDefault();
// });