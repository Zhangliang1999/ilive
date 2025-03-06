var url = window.location.href;
var appInterfaceBase = webBaseUrl + "/ilive";
var statisticBase = "http://statistics.zb.tv189.com";
var _shareTitle,
    _shareImg,
    _shareDesc,
    _shareTitle2,
    _shareImg2,
    _shareDesc2,
    sessionId,
    userIdTemp,
    liveEventIdTemp;

var wxshare = {
    init: function () {
        createAPI(appInterfaceBase + "/app/wx/shareinfo.jspx", {
            roomId: Params.roomId
        }).then(function (res) {
            if (res.code !== 0) {
                //tysx
                var options = {
                    shareUrl: url,
                    cover: res.data.friendCircle.liveImg || '',
                    title: res.data.friendCircle.liveTitle || '',
                    content: res.data.friendCircle.liveDesc || ''
                };
                TysxJsSdk.invokeJsApi("set_shareInfo", options, function (data) {console.log(data); });

                //微信分享
                wxshare.share(res)
            } else {
                layerOpen('share-err')
            }

        })
    },
    share: function (data) {
        _shareTitle = data.data.friendCircle.liveTitle;
        _shareImg = data.data.friendCircle.liveImg;
        _shareDesc = data.data.friendCircle.liveDesc;

        _shareTitle2 = data.data.friendSingle.liveTitle;
        _shareImg2 = data.data.friendSingle.liveImg;
        _shareDesc2 = data.data.friendSingle.liveDesc;

        sessionId =data.sessionId 
        userIdTemp = $("input[name=userId]").val()
        liveEventIdTemp = $("input[name=liveEventId]").val()

        $("meta[name='description']").attr('content', data.data.friendCircle.liveDesc);


        createAPI(appInterfaceBase + "/app/wx/share.jspx", {
            shareUrl: url
        }).then(function (msg) {
            try {
                wx.config({
                    debug: false, //
                    appId: msg.appId, // 必填，公众号的唯一标识
                    timestamp: msg.timestamp, // 必填，生成签名的时间戳
                    nonceStr: msg.nonceStr, // 必填，生成签名的随机串
                    signature: msg.signature, // 必填，签名，见附录1
                    jsApiList: ['onMenuShareTimeline',
                        'onMenuShareAppMessage', 'showOptionMenu'
                    ]
                });
    
                wx.ready(function () {
                    wx.onMenuShareAppMessage({
                        title: _shareTitle2, // 分享标题
                        link: url, // 分享链接
                        imgUrl: _shareImg2,
                        desc: _shareDesc2, // 分享描述
                        success: function () {
                            $.ajax({
                                url: statisticBase + "/statistic/service/user/share",
                                type: "get",
                                data: {
                                    userId: userIdTemp + "_" + sessionId,
                                    roomId: Params.roomId,
                                    type: 4,
                                    liveEventId: liveEventIdTemp
                                },
                                async: false,
                                crossDomain: true,
                                dataType: "jsonp",
                                jsonp: 'jsoncallback',
                                success: function (data) {
                                    //
                                    // alert(data.code);
                                }
                            });
                        }
                    });
    
                    wx.onMenuShareTimeline({
                        title: _shareTitle, // 分享标题
                        link: url, // 分享链接
                        imgUrl: _shareImg,
                        desc: _shareDesc, // 分享描述
                        success: function () {
                            $.ajax({
                                url: statisticBase + "/statistic/service/user/share",
                                type: "get",
                                data: {
                                    userId: userIdTemp + "_" + sessionId,
                                    roomId: Params.roomId,
                                    type: 4,
                                    liveEventId: liveEventIdTemp
                                },
                                async: false,
                                crossDomain: true,
                                dataType: "jsonp",
                                jsonp: 'jsoncallback',
                                success: function (data) {
                                    // alert(data.code);
                                }
                            });
                            var userId = userIdTemp; //用户ID
                            if (userId != 0) {
                                $.ajax({
                                    type: "GET", //请求方式 get/post
                                    url: "http://" + tomcatBase + "/ilive/prize/share.jspx",
                                    dataType: "jsonp",
                                    jsonp: "callback",
                                    cache: false,
                                    data: {
                                        terminalType: "h5",
                                        userId: userId,
                                        roomId: Params.roomId
                                    },
                                    success: function (data) {
    
                                    }
                                });
                            }
                        }
                    });
                });
            } catch (error) {
                alert(error)
            }
        
        }).catch(function(err){
          
        })
    }
}

// var roomId = GetQueryString("roomId");

// $(function () {
//     $.ajax({
//         url: appInterfaceBase + "/app/wx/shareinfo.jspx",
//         type: "get",
//         data: {
//             roomId: Params.roomId1
//         },
//         async: false,
//         crossDomain: true,
//         dataType: "jsonp",
//         jsonp: 'callback',
//         success: function (data) {
//             if (data.code !== 0) {
//                 _shareTitle = data.data.friendCircle.liveTitle;
//                 _shareImg = data.data.friendCircle.liveImg;
//                 _shareDesc = data.data.friendCircle.liveDesc;
//                 _shareTitle2 = data.data.friendSingle.liveTitle;
//                 _shareImg2 = data.data.friendSingle.liveImg;
//                 _shareDesc2 = data.data.friendSingle.liveDesc;
//                 sessionId = data.sessionId;
//                 // alert("sessionId"+sessionId);
//                 userIdTemp = $("input[name=userId]").val(); //用户ID
//                 // alert("userIdTemp"+userIdTemp);
//                 liveEventIdTemp = $("input[name=liveEventId]").val();
//                 $("meta[name='description']").attr('content', data.data.friendCircle.liveDesc);
//                 //$('title').html(data.data.friendCircle.liveTitle);
//                 //  alert("liveEventIdTemp"+liveEventIdTemp);
//                 //场次ID
//                 // circle
//                 // friend

//                 //tysx
//                 var options = {
//                     shareUrl: url,
//                     cover: _shareImg,
//                     title: _shareTitle,
//                     content: _shareDesc
//                 };
//                 TysxJsSdk.invokeJsApi("set_shareInfo", options, function (data) {
//                     //回调的逻辑处理
//                     console.log(data);
//                 });
//             }

//         },
//         error: function (e) {
//             console(e);
//         }
//     });


//     $.ajax({
//         type: "post",
//         dataType: "jsonp",
//         url: appInterfaceBase + "/app/wx/share.jspx",
//         jsonp: "callback",
//         data: {
//             shareUrl: url
//         },
//         complete: function () {},
//         success: function (msg) {
//             wx.config({
//                 debug: false, //
//                 appId: msg.appId, // 必填，公众号的唯一标识
//                 timestamp: msg.timestamp, // 必填，生成签名的时间戳
//                 nonceStr: msg.nonceStr, // 必填，生成签名的随机串
//                 signature: msg.signature, // 必填，签名，见附录1
//                 jsApiList: ['onMenuShareTimeline',
//                     'onMenuShareAppMessage', 'showOptionMenu'
//                 ]
//                 // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
//             });
//         },
//     });


//     wx.ready(function () {
//         wx.onMenuShareAppMessage({
//             title: _shareTitle, // 分享标题
//             link: url, // 分享链接
//             imgUrl: _shareImg,
//             desc: _shareDesc, // 分享描述
//             success: function () {
//                 if (liveEventIdTemp == "0") liveEventIdTemp = $("input[name=liveEventId]").val();
//                 $.ajax({
//                     url: statisticBase + "/statistic/service/user/share",
//                     type: "get",
//                     data: {
//                         userId: userIdTemp + "_" + sessionId,
//                         roomId: roomId,
//                         type: 4,
//                         liveEventId: liveEventIdTemp
//                     },
//                     async: false,
//                     crossDomain: true,
//                     dataType: "jsonp",
//                     jsonp: 'jsoncallback',
//                     success: function (data) {
//                         //
//                         // alert(data.code);
//                     }
//                 });
//             }
//         });

//         wx.onMenuShareTimeline({
//             title: _shareTitle, // 分享标题
//             link: url, // 分享链接
//             imgUrl: _shareImg,
//             desc: _shareDesc, // 分享描述
//             success: function () {
//                 $.ajax({
//                     url: statisticBase + "/statistic/service/user/share",
//                     type: "get",
//                     data: {
//                         userId: userIdTemp + "_" + sessionId,
//                         roomId: roomId,
//                         type: 4,
//                         liveEventId: liveEventIdTemp
//                     },
//                     async: false,
//                     crossDomain: true,
//                     dataType: "jsonp",
//                     jsonp: 'jsoncallback',
//                     success: function (data) {
//                         // alert(data.code);
//                     }
//                 });
//                 var userId = userIdTemp; //用户ID
//                 if (userId != 0) {
//                     $.ajax({
//                         type: "GET", //请求方式 get/post
//                         url: "http://" + tomcatBase + "/ilive/prize/share.jspx",
//                         dataType: "jsonp",
//                         jsonp: "callback",
//                         cache: false,
//                         data: {
//                             terminalType: "h5",
//                             userId: userId,
//                             roomId: roomId
//                         },
//                         success: function (data) {

//                         }
//                     });
//                 }
//             }
//         });

//     });
// });