	var minRewardsAmount =0;
	var maxRewardsAmount =0;
	function selectCK(){
		var checkbox = $("#checkbox-1-1").prop("checked");
		if($("#checkbox-1-1").prop("checked")){
			$("#payButtonClick").css("background","#86929c");
			$("#payButtonClick").removeAttr("onclick");
		}else{
			$("#payButtonClick").css("background","#0084ff");
			var wechatSweepUnifyTheOrder = $("#checkbox-1-1").attr("wechatSweepUnifyTheOrder");
			$("#payButtonClick").attr("onclick",wechatSweepUnifyTheOrder);
		}
	}
	//激活码观看提交
	function openRoomActivationCode(){
		var codeEwm = $("input[name=codeEwm]").val();
		if(codeEwm==null||codeEwm==undefined||codeEwm==""){
			$.DialogBySHF.Alert({
			      Width: 350,
			      Height: 200,
			      Title: "警告",
			      Content: "激活码不能为空"
			 });
		}else{
			var roomId = $("input[name=roomId]").val();//直播间ID
			$.ajax({
				type : "GET",//请求方式 get/post
				url : "http://" + tomcatBase + "/ilive/app/room/userFCode.jspx",
				dataType : "jsonp",
				jsonp : "callback",
				cache : false,
				data : {
					terminalType:"h5",
					roomId:roomId,
					code:codeEwm
				},
				success : function(data) {
					var code = data.code;
					if(code==1){
						$(".ewmMask").addClass("hide");
						$("#ewmDiv").addClass("hide");
						window.location.href=window.location.href;
					}else{
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
	}
	//支付观看
	function openPay(){
		$('#payModal').modal({backdrop: 'static', keyboard: false});
		$('#ewmModal').modal("hide");
		$('#codeModal').modal("hide");
	}
	//激活码观看
	function openJhm(){
		$('#payModal').modal("hide");
		$('#ewmModal').modal("hide");
		$('#codeModal').modal({backdrop: 'static', keyboard: false});
	}
	// 微信支付二维码   orderType: 1礼物  2打赏 3直播付费  4 点播      productDesc:描述     totalAmount：金额（分）
	function wechatSweepUnifyTheOrder(orderType,productDesc,totalAmount,contentId){
		var roomId = $("input[name=roomId]").val();//直播间ID
		var userId = $("input[name=userId]").val();//用户ID
		var terminalOrderType = window.navigator.appName;
		$.ajax({
			type : "GET",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/wx/sweep/order.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				terminalType:"pc",
				productDesc:encodeURI(productDesc),
				totalAmount:totalAmount,
				userId:userId,
				orderType:orderType,
				terminalOrderType:terminalOrderType,
				roomId:roomId,
				contentId:contentId
			},
			success : function(data) {
				var code = data.code;
				if(code==1){
					//二维码创建成功
					var codeUrl = data.codeUrl;
					$('#ewmImage').qrcode({
						render : "canvas", // 渲染方式有table方式和canvas方式
						width : 160, //默认宽度
						height : 160, //默认高度
						text : codeUrl, //二维码内容
						typeNumber : -1, //计算模式一般默认为-1
						correctLevel : 2, //二维码纠错级别
						background : "#ffffff", //背景颜色
						foreground : "#000000" //二维码颜色
					});
					$('#payModal').modal("hide");
					$('#ewmModal').modal({backdrop: 'static', keyboard: false});
					$('#codeModal').modal("hide");
					var orderId = data.orderId;
					selectOrder(orderId);
				}else{
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
	//查询订单详情
	function selectOrder(orderId){
		$.ajax({
			type : "GET",//请求方式 get/post
			url : "http://" + tomcatBase + "/ilive/wx/select/getOrderId.jspx",
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
				terminalType:"pc",
				orderId:orderId
			},
			success : function(data) {
				var code = data.code;
				if(code==1){
					var payStatus = data.payStatus;
					if(payStatus==0){
						$("#ewmImage").attr("src","");
						$(".ewmMask").addClass("hide");
						var orderType = data.orderType;
						if(orderType==1){
							//礼物
						}else if(orderType==2){
							//打赏
						}else if(orderType==3){
							window.location.href = window.location.href;
						}else{
							//其他订单
							console.log(orderType);
						}
					}else{
						setTimeout(function() {
							selectOrder(orderId);
						}, 2000);
					}
				}else{
					console.log(data.message);
				}
			}
		});
	}
