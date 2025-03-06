/**
 * Created by Deng on 2018/6/8.
 */
$(document).ready(function(){
    //点击打赏
    $(document).delegate(".rewardIcon","click",function(){
    	var userId = $("input[name=userId]").val();//用户ID
		if(userId==0){
            window.location.href="http://"+h5Base+"/phone/login.html?roomId="+roomId;
		}else{
			$(".rewardMask").slideDown(500);
		}
    })
    //打赏遮罩层点击关闭
    $(".rewardClose").click(function(){
        $(".rewardMask").slideUp(500);
    })
});
