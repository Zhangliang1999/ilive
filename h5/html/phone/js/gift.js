/**
 * Created by Deng on 2018/6/15.
 */
var arr = [];
var num = 0;
var timer = 1;
$(document).ready(function () {
    $(document).delegate(".giftCon ul li", "click", function () {
        $(this).addClass("giftactive").siblings().removeClass("giftactive");
    });
    $(".giftmask").click(function () {
        $(".giftMask").fadeOut(500);
    });
});

function openGiftIcon() {
    var userId = $("input[name=userId]").val();//用户ID
    if (userId == 0) {
        if (confirm("是否进入登录？")) {
            var roomId = $("input[name=roomId]").val();//直播间ID
            window.location.href = "http://" + h5Base + "/phone/login.html?roomId=" + roomId;
        } else {
            layerOpen('不登录无法进行礼物操作')
            // $.DialogBySHF.Alert({
            //     Width: 350,
            //     Height: 200,
            //     Title: "警告",
            //     Content: "不登录无法进行礼物操作"
            // });
        }
    } else {
        $(".giftMask").fadeIn(500);
    }
}

//发送礼物支付准备
function saveUserGift() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    var giftId = $(".giftactive").attr("giftId");//礼物ID
    if (giftId == undefined || giftId == null) {
        layerOpen('请选择赠送礼物')
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "请选择赠送礼物"
        // });
    } else {
        var giftNumber = 1;
        //orderType: 1礼物  2打赏       productDesc:描述     totalAmount：金额（分）
        var giftPrice = $(".giftactive").attr("giftPrice")
        var totalAmount = parseFloat(giftPrice) * 100;
        var giftName = $(".giftactive").attr("giftName");
        var productDesc = giftName + "*" + giftNumber;
        wechatSweepUnifyTheOrder(1, productDesc, totalAmount, giftId);
    }
}

//发送礼物
function saveUserGiftPay() {
    var roomId = $("input[name=roomId]").val();//直播间ID
    var userId = $("input[name=userId]").val();//用户ID
    var giftId = $(".giftactive").attr("giftId");//礼物ID
    if (giftId == undefined || giftId == null) {
        layerOpen('请选择赠送礼物')
        // $.DialogBySHF.Alert({
        //     Width: 350,
        //     Height: 200,
        //     Title: "警告",
        //     Content: "请选择赠送礼物"
        // });
    } else {
        var giftNumber = 1;
        $.ajax({
            type: "GET",//请求方式 get/post
            url: "http://" + tomcatBase + "/ilive/gift/save/userGift.jspx",
            dataType: "jsonp",
            jsonp: "callback",
            cache: false,
            data: {
                roomId: roomId,
                terminalType: "h5",
                userId: userId,
                giftId: giftId,
                giftNumber: giftNumber
            },
            success: function (data) {
                if (data.code == 1) {
                    console.log("成功");
                    $(".giftMask").fadeOut(500);
                } else {
                    layerOpen(data.message)
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

//定时从数组中取值
function giveGift() {
    console.log("arr的长度==" + arr.length);
    if (closegift) return
    if (arr.length < 1) {
        setTimeout(function () {
            $(".giftLeft").eq(0).remove();
        }, timer)
    } else {
        num++
        if (arr[0].code == 0) {
            var html = "<div class='giftone'><span class='personname'>" + arr[0].userName + "</span>赠送了 <img src='" + arr[0].giftImage + "'/>x1</div>";
            // $(".giftLeft").append(html);
            // arr.splice(0, 1);
            // gift();
        } else if (arr[0].code == 1) {
            var html = "<div class='giftone'><span class='personname'>" + arr[0].userName + "</span>打赏了   <span>" + arr[0].rewardsPrice + "</span>元</div>";
            // $(".giftLeft").append(html);
            // arr.splice(0, 1);
            // gift();
        } else if (arr[0].code == 2) {

            var html = "<div class='giftone'><span class='personname'>" + arr[0].welcomeLanguage + "</span></div>";
            // $(".giftLeft").append(html);
            // arr.splice(0, 1);
            // gift();
        }
        $(".giftLeft").append(html);
        arr.splice(0, 1);
        gift();
    }
}

var margintop = -40;//上边距的偏移量
var time = null
var time1 = null
var closegift = false
//执行动画的操作
function gift() {
    // setTimeout(function(){
    //     $(".giftLeft ul li").eq(0).remove();
    // },timer)
    //console.log($(".giftLeft ul li").length)
    var $giftLeft = $('.giftLeft')
    var $giftone =$giftLeft.children('div')
    $giftLeft.show()
   for (var i = 0; i<$giftone.length; i++){
       clearInterval(time)
       time = setInterval(function(){

           $giftLeft.find('div').first().addClass('myopacityout').animate({marginTop: margintop}, 500, function () {
               $(this).remove()
               //$giftLeft.addClass('hide')
           }).next().addClass('myopacity')

       },1000);

   }
    clearInterval(time1)
    time1 = setInterval(function(){
        if ($giftLeft.children('div').length < 1){
            $giftLeft.hide()
            clearInterval(time1)
        }
    },10);
    // clearInterval(time)
    // console.log(time)
    // time = setInterval(function () {
    //     $giftLeft.find('li').eq(0).addClass('myopacityout')
    //     $giftLeft.find('li').first().animate({"margin-top": margintop}, 500, function () {
    //         $(this).remove()
    //         //$giftLeft.addClass('hide')
    //     });
    // },2000)
    // console.log(time)
    // if ($giftLeft.find('li').length > 3) {
    //     // $(".giftLeft ul li").eq(0).remove();\
    //     $giftLeft.find('li').eq(0).addClass('myopacityout')
    //     $giftLeft.find('li').first().animate({"margin-top": margintop}, 500, function () {
    //         $(this).remove()
    //     });
    // }else{
    //     setTimeout(function () {
    //         $giftLeft.find('li').eq(0).addClass('myopacityout')
    //         $giftLeft.find('li').first().animate({"margin-top": margintop}, 500, function () {
    //             $(this).remove()
    //             //$giftLeft.addClass('hide')
    //         });
    //     }, 2000)
    // }
    // if ($(".giftLeft ul li").length < 3) {
    //     // $(".giftLeft ul").children("li").first().animate({"margin-top": margintop}, 550, function () {
    //     //     $(this).remove()
    //     // });
    //     //$(".giftLeft ul li").eq(0).addClass('myopacityout')
    //
    // }
    // var len = $(".giftLeft ul li")
    // $(len).each(function (i) {
    //     $(this).css('top', i * 40)
    // })
    // var animateList = [
    //     function () {
    //         $('.giftone').animate({top: 0}, 2000, queueList);
    //     },
    // ];
    // $(document).queue('_queueList', animateList);
    // var queueList = function () {
    //     $(document).dequeue('_queueList');
    // };
    // queueList();

}

$('#giftLeftclose').on('click',function () {
    $('#giftLeft').css('display','none')
    closegift =true
})