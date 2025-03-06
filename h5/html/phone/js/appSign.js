/**
 * Created by Administrator on 2018/6/12.
 */
//$("body").css("cursor", "none");
var bridgeConstant;
window.onerror = function (err) {
    console.log('window.onerror: ' + err)
}

function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener('WebViewJavascriptBridgeReady', function () {
            callback(WebViewJavascriptBridge)
        }, false)
    }
}

connectWebViewJavascriptBridge(function (bridge) {
    bridgeConstant = bridge;
    var uniqueId = 1

    function log(message, data) {
        var log = document.getElementById('log')
        var el = document.createElement('div')
        el.className = 'logLine'
        el.innerHTML = uniqueId++ + '. ' + message + (data ? ':<br/>' + JSON.stringify(data) : '')
        if (log.children.length) {
            log.insertBefore(el, log.children[0])
        } else {
            log.appendChild(el)
        }
    }

    bridge.init(function (message, responseCallback) {
        log('JS got a message', message)
        var data = {
            'Javascript Responds': 'Wee!'
        }
        log('JS responding with', data)
        responseCallback(data)
    })

    bridge.registerHandler('testJavascriptHandler', function (data, responseCallback) {
        log('ObjC called testJavascriptHandler with', data)
        var responseData = {
            'Javascript Says': 'Right back atcha!'
        }
        log('JS responding with', responseData)
        responseCallback(responseData)
    })

    var button = document.getElementById('buttons').appendChild(document.createElement('button'))
    button.innerHTML = 'Send message to ObjC'
    button.onclick = function (e) {
        e.preventDefault()
        var data = 'Hello from JS button'
        log('JS sending message', data)
        bridge.send(data, function (responseData) {
            log('JS got response', responseData)
        })
    }

    document.body.appendChild(document.createElement('br'))

    var callbackButton = document.getElementById('buttons').appendChild(document.createElement('button'))
    callbackButton.innerHTML = 'Fire testObjcCallback'
    callbackButton.onclick = function (e) {
        e.preventDefault()
        log('JS calling handler "testObjcCallback"')
        bridge.callHandler('testObjcCallback', {
            'foo': 'bar'
        }, function (response) {
            log('JS got response', response)
        })
    }
})
// 点击签到按钮
$(document).ready(function () {
    $("body").delegate("#signIn","click",function(){
        selectQiandao()
    })
});
var num = 0;
var problemAnswers = "";
var voteId = 0;//投票活动ID
var isOpen = 0;//是否对用户公开结果    0不公开   1公开


function selectQiandao() {

    var roomId = $("input[name=roomId]").val();//直播间ID
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
            console.log(data);
            if (data.code == 1) {
                location.href = "http://" + h5Base + "/phone/sign.html?signId=" + data.signId + "&roomId=" + roomId;
            } else {
                $("#signIn").addClass('hide')
                $.DialogBySHF.Alert({
                    Width: 350,
                    Height: 200,
                    Title: "警告",
                    Content: data.message
                });
            }
        }
    });
}

// 投票结束
function endVode() {
    var HTML = "<div class=\"voteNostart\">";
    HTML += "	<div class=\"voteNostartPic\"></div>";
    HTML += "	<div class=\"voteText\">投票活动已结束</div>";
    HTML += "</div>";
    $("#vodeContentDiv").html(HTML);
    $(".voteMask").slideDown(500);
}


