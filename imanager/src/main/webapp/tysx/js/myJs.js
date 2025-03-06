//�б��js
$(function () {
    if(document.body.clientWidth<=800){
        $("body").addClass("mini-navbar");
    }else{
        $("body").removeClass("mini-navbar");
    }
//  $(window).resize(function() {
//      if(document.body.clientWidth<=800){
//          $("body").addClass("mini-navbar");
//      }else{
//          $("body").removeClass("mini-navbar");
//      }
//  });
})
//���ڲ���ĳ������ĳ����
$(document).ready(function(){

    $('.data_5 .input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
});
//page�����б�
$(document).ready(function(){
    $(".btn-menu-list>li>a").click(function(){
        $(".btn-shows").text($(this).html());
    });
})
$(document).ready(function(){
    $(".btn-menu-list2>li>a").click(function(){
        $(".btn-shows2").text($(this).html());
    });
})
//ϵͳ�˺Ź��?��
$(document).ready(function(){
    $('.czrz-xx .group-down').click(function(){
        $('.czrz-xx').css("height","120px");
        $('.czrz-xx .group-box-c').css("height","120px") ;
        $('.czrz-xx .group-down').css("display","none")
        $('.czrz-xx .group-up').css("display","block")
    })
    $('.czrz-xx .group-up').click(function(){
        $('.czrz-xx').css("height","65px");
        $('.czrz-xx .group-box-c').css("height","65px") ;
        $('.czrz-xx .group-down').css("display","block")
        $('.czrz-xx .group-up').css("display","none")
    })
    $('.czrz-fy .group-down').click(function(){
        $('.czrz-fy').css("height","120px");
        $('.czrz-fy .group-box-c').css("height","120px") ;
        $('.czrz-fy .group-down').css("display","none")
        $('.czrz-fy .group-up').css("display","block")
    })
    $('.czrz-fy .group-up').click(function(){
        $('.czrz-fy').css("height","65px");
        $('.czrz-fy .group-box-c').css("height","65px") ;
        $('.czrz-fy .group-down').css("display","block")
        $('.czrz-fy .group-up').css("display","none")
    })
    $('.czrz-lx .group-down').click(function(){
        $('.czrz-lx').css("height","120px");
        $('.czrz-lx .group-box-c').css("height","120px") ;
        $('.czrz-lx .group-down').css("display","none")
        $('.czrz-lx .group-up').css("display","block")
    })
    $('.czrz-lx .group-up').click(function(){
        $('.czrz-lx').css("height","65px");
        $('.czrz-lx .group-box-c').css("height","65px") ;
        $('.czrz-lx .group-down').css("display","block")
        $('.czrz-lx .group-up').css("display","none")
    })
    $('.czrz-nj .group-down').click(function(){
        $('.czrz-nj').css("height","120px");
        $('.czrz-nj .group-box-c').css("height","120px") ;
        $('.czrz-nj .group-down').css("display","none")
        $('.czrz-nj .group-up').css("display","block")
    })
    $('.czrz-nj .group-up').click(function(){
        $('.czrz-nj').css("height","65px");
        $('.czrz-nj .group-box-c').css("height","65px") ;
        $('.czrz-nj .group-down').css("display","block")
        $('.czrz-nj .group-up').css("display","none")
    })

})
