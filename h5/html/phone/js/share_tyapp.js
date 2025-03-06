var options = "";
var roomId = GetQueryString("roomId");

$(function () {

    $.ajax({
        url: appInterfaceBase + "/app/wx/shareinfo.jspx",
        type: "get",
        data: {
            roomId: roomId
        },
        async: false,
        crossDomain: true,
        dataType: "jsonp",
        jsonp: 'callback',
        success: function (data) {
            if (data.code != 0) {
                _shareTitle = data.data.friendCircle.liveTitle;
                _shareImg = data.data.friendCircle.liveImg;
                _shareDesc = data.data.friendCircle.liveDesc;
                _shareTitle2 = data.data.friendSingle.liveTitle;
                _shareImg2 = data.data.friendSingle.liveImg;
                _shareDesc2 = data.data.friendSingle.liveDesc;
                var sessionId = data.sessionId;
                // alert("sessionId"+sessionId);
                var userIdTemp = $("input[name=userId]").val();//用户ID
                // alert("userIdTemp"+userIdTemp);
                var liveEventIdTemp = $("input[name=liveEventId]").val();
                //  alert("liveEventIdTemp"+liveEventIdTemp);
                options = {
                    shareUrl: window.location.href,
                    cover: _shareImg,
                    title: _shareTitle,
                    content: _shareDesc
                };
                TysxJsSdk.invokeJsApi("set_shareInfo", options, function (data) {
                    //回调的逻辑处理
                    console.log(data);
                });
            }


        },
        error: function (e) {
            console(e);
        }
    });

});



