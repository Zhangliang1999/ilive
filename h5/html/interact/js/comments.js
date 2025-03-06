/*

/ilive/prize/isOverComment.jspx?roomId=" + roomId

 */

$(function () {
    var result = [];
    var index = 1;
    var className = '';
    var $container = $('.container');
    var roomId = GetQueryString("roomId");
    var time = null
    var time1 = null
    var margintop = -105
    var index = 0
    if (roomId == undefined || roomId == 'null') {
        roomId = 0;
    }

    function init(callback) {

        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/prize/isOverComment.jspx?roomId=" + roomId,
            dataType: "jsonp",
            jsonp: "callback",
            success: function (data) {
                var code = data.code
                if (code) {
                    callback(data)
                } else {
                    alert('活动未开启')
                }
            }


        })

    }

    init(function (data) {
        var startTime = data.comment.startTime.time;
        var endTime = data.comment.endTime.time;

        if (checkTime(startTime, endTime)) return

        //设置
        GetALlSet(data)
        //评论
        Comment()
    })


    function Comment() {
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/app/room/roomenter.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            data: {
                roomId: roomId,
                sessionType: 1,//WebSocket使用用户
                webType: 2
            },
            success: function (data) {
                var token = data.token
                getComment(token)

            }


        })
    }

    function getComment(token) {
//ws
        var url = "ws://" + tomcatBase + "/ilive/webSocketServer.jspx?username=" + token;
        ws = new WebSocket(url);
        ws.onopen = function (e) {
            console.log("连接成功");
        }
        ws.onclose = function (e) {
            console.log("关闭连接 ");
        }
        ws.onerror = function (e) {
            console.log(e);
            console.log("建立连接异常" + e);
        }
        ws.onmessage = function (e) {
            var iLiveMessage = JSON.parse(e.data);
            var liveMsgType = iLiveMessage.liveMsgType;
            var deleteAll = iLiveMessage.deleteAll;
            if (deleteAll == 1) {
                $('.container').children().remove()
            }else if (iLiveMessage.deleted == 1) {
                $("#msgId_" + iLiveMessage.msgId).remove();

            }else if (liveMsgType == 2) { //评论
                if (iLiveMessage.checked == 1) { //通过
                    if ($("#msgId_" + iLiveMessage.msgId).length ==1) return false
                    //$container.append(template(iLiveMessage.senderImage, iLiveMessage.senderName, iLiveMessage.webContent, className))
                    var str = template(iLiveMessage.senderImage, iLiveMessage.senderName, iLiveMessage.webContent, iLiveMessage.msgId, className)
                    result.push(str)
                    // $('.container').append(result.join(""));
                    animate()
                }
            }

        }
        window.setInterval(function () { //每隔5秒钟发送一次心跳，避免websocket连接因超时而自动断开
            var ping = {"p": "1"};
            if (ws) {
                try {
                    ws.send(JSON.stringify(ping));
                    console.log(ping)
                } catch (error) {
                    console.log("websocket 连接已关闭")
                }
            }
        }, 10000);
    }

    function GetALlSet(info) {
        var qrcode = info.comment.ewImg;
        var imgUrl = info.comment.imgUrl;
        $('#code').append('<img src="' + qrcode + '">').parent().show();
        if (imgUrl!=''){
            $bg.css({
                'background-image': 'url('+imgUrl+')'
            })
        }
    }

    function template(images, name, content, msgId, className) {
        return html = '<div ' + className + ' id="msgId_' + msgId + '">' +
            '<dl>' +
            '<dt><img src="' + images + '"></dt>' +
            '<dd><p class="name">' + name + '</p>' +
            '<p class="body-area">' + content + '</p></dd>' +
            '</dl></div>'

    }

    function animate() {
        var len = result.length
        $container.append(result[len - 1]).find("div:last").hide().fadeIn(500)
        if (len >= 7) {
            clearInterval(time)
            time = setInterval(function () {
                if (result.length <= 6) {
                    clearInterval(time)
                    return
                }

                $container.find("div").eq(6 + index).hide().fadeIn(500)
                $container.animate({marginTop: margintop * (index + 1)}, 500, function () {
                    $container.find("div").eq(index).fadeIn(500)
                    result.splice(0, 1);
                    index++
                    //$giftLeft.addClass('hide')
                    //$container.append(tmpHtml).find("div:first").fadeOut(1000)
                });
            }, 1000);
        }

    }

    function checkTime(startTime, endTime) {
        var Now = new Date().getTime()

        if (startTime > Now || Now > endTime) {
            alert('活动未开始')
            return true
        }

    }


})