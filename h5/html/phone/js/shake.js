/**
 * Created by Administrator on 2018/6/11.
 */
$(document).ready(function() {
	var SHAKE_THRESHOLD = 5000;
	var last_update = 0;
	var x, y, z, last_x = 0, last_y = 0, last_z = 0;
	function deviceMotionHandler(eventData) {
		var drawMaskValue= $(".drawMask").attr("drawMask");
		if(drawMaskValue==1){
			 var acceleration =eventData.accelerationIncludingGravity;
			    var curTime = new Date().getTime();
			    if ((curTime-last_update)> 10) {
			        var diffTime = curTime -last_update;
			        last_update = curTime;
			        x = acceleration.x;
			        y = acceleration.y;
			        z = acceleration.z;
			        var speed = Math.abs(x +y + z - last_x - last_y - last_z) / diffTime * 10000;
			        if (speed > SHAKE_THRESHOLD) {
			            var choujiangNumber = $("#choujiangNumber").text();
		            	if(parseInt(choujiangNumber)>parseInt(0)){
		            		$("#choujiangNumber").text(choujiangNumber-1)
		            		//摇一摇中奖处理
		                    var userId = $("input[name=userId]").val();//用户ID
		                	var roomId = $("input[name=roomId]").val();//直播间ID
		                	var jj = 0;
		                	if(jj==0){
		                		jj=1;
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
		                	}
		            	}else{
		            		$.DialogBySHF.Alert({
		            		      Width: 350,
		            		      Height: 200,
		            		      Title: "提示",
		            		      Content: "抽奖次数已使用完毕"
		            		 });
		            	}

			        }
			        last_x = x;
			        last_y = y;
			        last_z = z;
			    }
		}
	}

	if (window.DeviceMotionEvent) {
		window.addEventListener('devicemotion',deviceMotionHandler,false);
	} else {
	    alert("你的设备不支持摇一摇功能")
	}

});