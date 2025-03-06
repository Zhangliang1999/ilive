/**
 * Created by Administrator on 2018/5/8.
 */
var winHeight;
//��ȡҳ��߶�
winHeight = $(window).height();
//�������߶�
var navHeight = $(".navbar").height();
//�������߶ȼӱ���
var titleHeight = $(".title-header").height()+2;
$(".tablebox").slimScroll({
    height: winHeight-navHeight-titleHeight-75
});


$(".picobox").slimScroll({
    height: winHeight-navHeight-titleHeight-30
});
$(".right").css("margin-top",(winHeight-navHeight-titleHeight-75)/2)
$(".left").css("margin-top",(winHeight-navHeight-titleHeight-75)/2)
var zbjkWid = $(".zbjkBox").width();
$(".picobox").width(zbjkWid);
$("#zbjkul").width(zbjkWid);
$("#zbjkul li").width(zbjkWid);
$(".zbjk").height($(".zbjk").width()*9/16)
// ��ȡ��һ��ͼƬ�Ľڵ����
var firstImg = $('#zbjkul li').first().clone();
// ��ӵ�����λ�� ������ ul �Ŀ��
$('#zbjkul').append(firstImg).width($('#zbjkul li').length * $('#zbjkul li').width());
var i = 0;
var sign ;
var imgW = $('#zbjkul li').width();
var timer;
// ��һ��
$('.right').click(function () {
	sign = 1;
    moveImg(++i);
    console.log($('#zbjkul li').length)
    console.log(i)

});
// ��һ��
$('.left').click(function () {
	sign = 0;
    moveImg(--i);
    console.log($('#zbjkul li').length)
    console.log(i)
});
// �ƶ���ָ����ͼƬ
function moveImg() {
    // ���һ��
    if (i == $('#zbjkul li').length) {
        $('#zbjkul').css({
            left: 0
        })
        i = 1;
    }
    // �ǵ�һ�ŵ�ʱ��
    if (i == -1) {
        i = $('#zbjkul li').length - 2;
        $('#zbjkul').css({
            left: ($('#zbjkul li').length - 1) * -800
        });
    }
    // �ƶ�ͼƬ����
    $('#zbjkul').stop().animate({
        left: i * -imgW
    }, 400);
    
    var pageNo = $("#pageNo").val();
    var maxPage = $("#maxPage").val();
    console.log(pageNo+"  "+maxPage);
    if(sign==1){
    	console.log("right");
    	if(pageNo<maxPage){
    		pageNo++;
    		$("#pageNo").val(pageNo)
    	}else{
    		alert("已经是最后一页！");
    	}
    	
    }else if(sign ==0){
    	console.log("left");
    	if(pageNo>1){
    		pageNo--;
    		$("#pageNo").val(pageNo)
    	}else{
    		alert("已经是第一页！");
    	}
    }
    $.ajax({
		url:"${base}/operator/managercontrol/getPage.do",
		method:"get",
		data:{
			pageNo:pageNo
		},
		success:function(res){
			console.log(res);
		}
	});
    
}