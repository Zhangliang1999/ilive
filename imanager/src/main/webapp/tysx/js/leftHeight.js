/**
 * Created by Administrator on 2018/3/15.
 */
$(document).ready(function(){
    var winH = $(window).height()-72;
    var wraH = $(".wrapper").height();
    $(".navbar-static-side").height(winH);
    //��ȡҳ��߶�
    var winHeight = $(window).height();
    //�������߶�
    var navHeight = $(".navbar").height();
    //�������߶ȼӱ���
    var titleHeight = $(".title-header").height()+2;
    var btnBox = $(".btn-box").height()+15;
    if(btnBox==15){
        btnBox=30
    }
    var thHeight = $("thead").height();
    var pageBox = $(".pageBox").height()+45;
    $("tbody").height(winHeight-navHeight-titleHeight-btnBox-thHeight-pageBox);
    $(".qiyetable>tbody").height(winHeight-navHeight-titleHeight-pageBox-thHeight-125-100-70);
    $(".newqiyetable>tbody").height(winHeight-navHeight-titleHeight-pageBox-thHeight-125-100-25);
    $("#contentSelected tbody").height(150);
    $(".listbox tbody").css("height","auto");
    $(".nav-left").slimScroll({
        height: winH
    });
    $(".wrapper").slimScroll({
        height: winH,
        opacity:0
    });


})


