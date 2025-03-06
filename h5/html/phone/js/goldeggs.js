/**
 * Created by Deng on 2018/6/11.
 */
function  zadan(obj) {
	var choujiangNumber = $("#choujiangNumber").text();
	if(parseInt(choujiangNumber)>parseInt(0)){
		$(obj).children("span").hide();
	    eggClick($(obj));
	}else{
		layerOpen('抽奖次数已使用完毕')
		return false
		// $.DialogBySHF.Alert({
		//       Width: 350,
		//       Height: 200,
		//       Title: "提示",
		//       Content: ""
		//  });
	}
};

var dansui=1;
function eggClick(obj) {
    if(dansui<1){
    	layerOpen('蛋都碎了，请勿再敲，马上给你换一个！')
        //alert("蛋都碎了，请勿再敲，马上给你换一个！");
        location.reload();
        return;
    }
    var _this = obj;
    $("#hammer").stop(true);

    //clearInterval(dingshi);

    //$(".hammer").css({"top":"150px","left":"420px"});

    setTimeout(function(){
        _this.addClass("curr"); //蛋碎效果
        // _this.find("sup").show(); //金花四溅
        $(".hammer").hide();//隐藏锤子
        dansui=0;

        //砸金蛋奖项处理
        var userId = $("input[name=userId]").val();//用户ID
    	var roomId = $("input[name=roomId]").val();//直播间ID
        $.ajax({
    		type : "GET",//请求方式 get/post
    		url : "http://" + tomcatBase + "/ilive/prize/userLottery.jspx",
    		dataType : "jsonp",
    		jsonp : "callback",
    		cache : false,
    		data : {
    			terminalType:"h5",
    			userId : userId,
    			roomId : roomId
    		},
    		success : function(data) {
    			console.log(data);
    			if(data.code==1){
    				var id = data.data.prize;
    				winningPrize(id);
    			}else{
    				console.log(data.message);
    				notWinningPrize();
    			}
    		}
        });

    },300)




}