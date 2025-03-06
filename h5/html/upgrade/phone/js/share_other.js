var shareUrl2Wx = window.location.href;
var appInterfaceBase = "http://apizb.tv189.com/ilive";
var statisticBase = "http://statistics.zb.tv189.com";
// var _shareTitle,_shareImg,_shareDesc,_shareTitle2,_shareImg2,_shareDesc2;
var op = JSON.parse(sessionStorage.getItem('review'))
$(function () {
    // if (shareUrl2Wx.match(Params.userId)) {
    //     // 包含
    // } else {
    //     //var userId = $("input[name=userId]").val();
    //     //shareUrl2Wx = shareUrl2Wx+"&userId="+userId;
    // }
    $.ajax({
        type: "post",
        dataType: "jsonp",
        url: appInterfaceBase + "/app/wx/share.jspx",
        jsonp: "callback",
        data: {
            shareUrl: shareUrl2Wx
        },
        complete: function () {
        },
        success: function (msg) {
            wx.config({
                debug: false, //
                appId: msg.appId, // 必填，公众号的唯一标识
                timestamp: msg.timestamp, // 必填，生成签名的时间戳
                nonceStr: msg.nonceStr, // 必填，生成签名的随机串
                signature: msg.signature,// 必填，签名，见附录1
                jsApiList: ['onMenuShareTimeline',
                    'onMenuShareAppMessage', 'showOptionMenu']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
        },
    });
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: op.title, // 分享标题
            link: shareUrl2Wx, // 分享链接
            imgUrl: op.cover,
            desc: op.content, // 分享描述
            success: function () {

            }
        });

        wx.onMenuShareTimeline({
            title: op.title2, // 分享标题
            link: shareUrl2Wx, // 分享链接
            imgUrl: op.cover2,
            desc: op.content2, // 分享描述
            success: function () {

            }
        });

    });
    TysxJsSdk.invokeJsApi("set_shareInfo", op, function (data) {
        //回调的逻辑处理
        console.log(data);
    });
});




