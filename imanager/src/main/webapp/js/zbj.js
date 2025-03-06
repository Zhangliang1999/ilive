var rightDiv = $("#rightTabContent").width();
//滚动条
jQuery(document).ready(function ($) {
    var winHeight;
    var comHeight;
    var comHeight1;
    var comHeight2;
    var leftHeight;
    //获取视频的宽度
    $(".videobox").css("width","100%");
    var videoWidth = $(".videobox").width();
    $(".videobox").css("height",videoWidth*9/16);
    //获取页面高度
    winHeight = $(window).height();
    //导航栏高度
    var navHeight = $(".navbar").height();
    //标题栏高度加边线
    var titleHeight = $(".title-header").height()+2;
    //tab切换的头部
    var tabHeight = $(".nav-tabs").height()+6;
    //tab1头部内容的高度
    var tabTop1 = 40;
    //tab1d底部高度
    var tabBottom1 = $(".setMessage").height()+20;
    //右侧滚动条区域高度
    comHeight = winHeight-navHeight-titleHeight-tabHeight-tabTop1-tabBottom1;
    comHeight1 = winHeight-navHeight-titleHeight-tabHeight-tabBottom1;
    comHeight2 = winHeight-navHeight-titleHeight-tabHeight-tabTop1-20;
    //左侧滚动条区域高度
    var videoTop = $(".videotop").height();
    var videoHeight = $(".videobox").height()+20;
    var myTabHeight = $("#myTab").height();
    leftHeight = winHeight-navHeight-titleHeight-10-videoTop-videoHeight-myTabHeight;
    //计算中间部分高度
    $(".commentbox").css("height",comHeight);//默认的高度
    var liveWidth = $("#livecom").width();
    $(".commentbox").css("width",liveWidth-30);
    $("#myTabContent").slimScroll({
        height: leftHeight
    });
    $("#liveDiv").slimScroll({
        height: comHeight
    });
    $("#picDiv").slimScroll({
        height: comHeight1-15
    });
    $("#wdbox").slimScroll({
        height: comHeight2
    });
    $("#wdDiv").slimScroll({
        height: comHeight1-40
    });
    $(".listbox").slimScroll({
        height: leftHeight-30-35
    });
    $(".toupiaobox ul").slimScroll({
        height: "178px"
    });
    $(".choujiangbox ul").slimScroll({
        height: "178px"
    });
    $("#rightTab li").each(function(i,v){
        $(v).click(function(){
            if(i==0){
                $(".commentbox").css("height",comHeight);
            }else if(i==1){
                $(".commentbox").css("height",comHeight1);
            }else if(i==2){
                setTimeout(function(){
                    var wdboxHeight = $(".wdcontent").height();
                    var wdbottom =  $(".wdbottom").height();
                    var zong = wdboxHeight+wdbottom;
                    if(comHeight2>zong){
                        $("#wdbox").css("margin-top",(comHeight2-wdboxHeight)/2)
                    }
                },300)

            }else if(i==3){
                $(".commentbox").css("height",comHeight1);
            }
        })
    })
    //图片点击放大
    $(".picbox").lightGallery({
        loop:false,
        auto:false,
        closable:true
    });
    $(".zbshare").hover(function(){
        $(".zhiboshare").stop(true).slideDown();
    },function(){
        $(".zhiboshare").stop(true).slideUp();
    });
    $(".toupiaobox li").hover(function(){
        $(this).css("background","#E4E4E4").siblings().css("background","#f2f2f2");
        $(".toupiaobox li").find("a").addClass("fabu");
        $(this).find("a").removeClass("fabu");
    });
    $(".choujiangbox li").hover(function(){
        $(this).css("background","#E4E4E4").siblings().css("background","#f2f2f2");
        $(".choujiangbox li").find("a").addClass("fabu");
        $(this).find("a").removeClass("fabu");
    })
    $(".toupiao").hover(function(){
        $(".toupiaobox").stop().fadeIn();
    },function(){
        $(".toupiaobox").stop().fadeOut();
    })
    $(".choujiang").hover(function(){
        $(".choujiangbox").stop().fadeIn();
    },function(){
        $(".choujiangbox").stop().fadeOut();
    })
    $(".choujiangbox .fabu").click(function(){
        $(this).parents(".choujiangbox").hide();
    })
    // 获取第一张图片的节点对象
    var firstImg = $('#ul li').first().clone();
    var firsttext = $('.ultext li').first().clone();
    //获取第几张图片
    $(".totle").html($('#ul li').length);
    // 添加到最后的位置 并设置 ul 的宽度
    $('#ul').append(firstImg).width($('#ul li').length * $('#ul img').width());
    $('.ultext').append(firsttext).width($('.ultext li').length * $('.ultext li').width());
    var i = 0;
    var imgW = $('#ul img').width();
    var timer;
    $(".num").html(i + 1);
// 下一张
    $('.right').click(function () {
        moveImg(++i);
        console.log(i)
        $(".num").html(i + 1);
        if (i == $('#ul li').length - 1) {
            $(".num").html(1);
        }
    });

// 上一张
    $('.left').click(function () {
        moveImg(--i);
        console.log(i)
        $(".num").html(i + 1);
    });

// 移动到指定的图片
    function moveImg() {
        // 最后一张
        if (i == $('#ul li').length) {
            $('#ul').css({
                left: 0
            })
            $('.ultext').css({
                left: 0
            })
            i = 1;
        }
        // 是第一张的时候
        if (i == -1) {
            i = $('#ul li').length - 2;
            $('#ul').css({
                left: ($('#ul li').length - 1) * -800
            });
            $('.ultext').css({
                left: ($('.ultext li').length - 1) * -800
            })
        }
        // 移动图片动画
        $('#ul').stop().animate({
            left: i * -imgW
        }, 400);
        $('.ultext').stop().animate({
            left: i * -$('.ultext li').width()
        })
        // 换一下每个图片的小标记
        if (i == ($('#ul li').length - 1)) {
            $('#ol li').eq(0).addClass('border-red').siblings().removeClass('border-red');
        } else {
            $('#ol li').eq(i).addClass('border-red').siblings().removeClass('border-red');
            $(".num").html(i + 1)
        }
    }
// 点击小图片，跳转到指定的图片
    $('#ol li').click(function () {
        i = $(this).index();
        moveImg();
    });
    $('#ol li').show()
// 鼠标移入幻灯片清除定时器
    $('.picobox').mouseover(function () {
        $('.button').show();
        clearInterval(timer)
    }).mouseout(function () {
        // 鼠标离开重新播放
//    autoPlay();
        $('.button').hide();
    })
    $("#ol").width($("#ol li").length * ($("#ol li").width() + 10 + 4));
    var a = 0;
    var flag = 1;
    var flog = 1;
    $('.rightarr').stop().click(function () {
        if (flag == 2) {
            return;
        }
        a++
        flag = 2
        if (parseInt($("#ol").css("left")) + parseInt($("#ol").css("width")) <= $(".olbox").width()) {
            a--
            $("#ol").css("left", $(".olbox").width() - $("#ol").width())
            flag = 1
            return false
        } else {
            $('#ol').animate({
                left: a * (-$("#ol li").width() - 10 - 4)
            }, function () {
                flag = 1
            });
        }
    });
    $('.leftarr').stop().click(function () {

        if (flog == 2) {
            return;
        }
        a = a - 1;
        flog = 2
        if (parseInt($("#ol").css("left")) >= 0) {
            a++
            $("#ol").css("left", "0")
            flog = 1
            return false
        } else {
            $('#ol').stop().animate({
                left: a * -($("#ol li").width() + 10 + 4)
            }, function () {
                flog = 1
            });
        }
    });
})