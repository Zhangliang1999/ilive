$(function(){
	var tomcatBase = "http://apizb.tv189.com";
	//var tomcatBase = "http://apizbt.tv189.net";
	//var tomcatBase = "http://127.0.0.1:8081";
	//获取企业信息
	$.ajax({
			type : "GET",//请求方式 get/post
			url : tomcatBase + "/ilive/topic/getById.jspx?id="+GetQueryString("id"),
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
			},
			success : function(res) {
				console.log(res);
				if(res.code==1){
					var data = res.data;
					if(data=={}||data==""||data==null){
						
					}else{
						if(data.isPut == 0){
							location.href="http://zb.tv189.com/pc/404.html";
						}
						_shareTitle = data.name;
						_shareImg = data.logo;
						_shareDesc = data.descript;
						_shareTitle2 = data.name;
						_shareImg2 = data.logo;
						_shareDesc2 = data.descript;
						
						
						var show = "";
						$("#content").empty();
						$.each(data.listType,function(index,ele){
							var type = ele.type;
							
							switch(type){
								//普通文字
        						case 1:
        							var temp = ele.contentList;
        							if(temp.length>0){
										var div = $("<div></div>").addClass("text-line").text(temp[0].name);
										console.log(temp[0]);
        								if(temp[0].overStriking==1){
        									div.css("font-weight","700");
        								}
        								if(temp[0].tilt==1){
        									div.css("font-style","italic");
        								}
										console.log(temp[0].align+"位置");
        								switch(temp[0].align){
        								case 0:
        									div.css("text-align","left");break;
        								case 1:
        									div.css("text-align","center");break;
        								case 2:
        									div.css("text-align","right");break;
        								}
        								if(temp[0].fontSize==""){
        									div.css("font-size","12px");
        									div.css("line-height","12px");
        								}else{
        									div.css("font-size",temp[0].fontSize);
											div.css("line-height",temp[0].fontSize+"px");
        								}
										if(temp[0].paddingSize==0){
											div.css("padding-top","5px");
											div.css("padding-bottom","5px");
										}else{
											div.css("padding-top",temp[0].paddingSize+"px");
											div.css("padding-bottom",temp[0].paddingSize+"px");
										}
										
										$("#content").append(div);
        							}
        							break;
        						//单个图片
        						case 2:
        							var temp = ele.contentList;
        							if(temp.length>0){
										var div = $("<div></div>").addClass("one-pic").append("<a href='"+temp[0].contentUrl+"'><img src='"+temp[0].backgroundUrl+"'/></a>")
										$("#content").append(div);
        							}
        							break;
        						//图集
        						case 3:
									
									var div = $("<div></div>").addClass("swiper-wrapper");
									
									
									var con = "";
        							var temp = ele.contentList;
        							if(temp.length==0){
        								con+="<div class='swiper-slide'><img src='"+defaultImg+"'/></div>"
        							}else{
        								$.each(temp,function(i,el){
            								con+="<div class='swiper-slide'><a href='"+el.contentUrl+"'><img src='"+el.backgroundUrl+"'/></a></div>"
            							});
        							}
        							div.append(con);
									var container = $("<div></div>").addClass("swiper-container").append(div);
									
									var box = $("<div></div>").addClass("swipeBox").append(container);
									$("#content").append(box);
        							break;
        						//单列
        						case 4:
								
        							var temp = ele.contentList;
									var div = $("<div></div>")
        							con="";
        							$.each(temp,function(i,el){
										if(el.contentType==10){
											div.css("background","url("+el.backgroundUrl+")");
        									div.css("background-size","cover");
										}else{
											var t = "";
											if(el.contentType==1){
												
												console.log(el.status+"  状态");
												if(el.status==0){
													t += "<div class='condition'>预告</div>";
												}else if(el.status == 1){
													t += "<div class='condition status1'>正在直播</div>";
												}else if(el.status == 2){
													t += "<div class='condition status2'>暂停</div>";
												}else if(el.status == 3){
													t += "<div class='condition status3'>已结束</div>";
												}
											}else if(el.contentType==2){
												t += "<div class='condition status5'>精彩回看</div>";
											}else if(el.contentType==3){
												t += "<div class='condition status1'>专题</div>";
											}
											
											con+="<div class='bgc-fff'><div class='bigPic col-lg-12 mobileActive'><a href='"+el.contentUrl+"'><img src='"+el.backgroundUrl+"' alt=''/>"+t+"</a></div>";
											con+="<div class='oneColText'  style='text-align:center;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;'>"+el.name+"</div></div>";
										}
        								
        							});
									div.append(con);
        							$("#content").append(div);
        							break;
        						//双列
        						case 5:
        							var temp = ele.contentList;
									var div = $("<div></div>")
									
        							con="<div class='bgc-fff'><div class='listBox'>";
        							$.each(temp,function(i,el){
										if(el.contentType==10){
											div.css("background","url("+el.backgroundUrl+")");
        									div.css("background-size","cover");
										}else{
											var t = "";
											if(el.contentType==1){
												
												console.log(el.status+"  状态");
												if(el.status==0){
													t += "<div class='smallStatus'>预告</div>";
												}else if(el.status == 1){
													t += "<div class='smallStatus status1'>正在直播</div>";
												}else if(el.status == 2){
													t += "<div class='smallStatus status2'>暂停</div>";
												}else if(el.status == 3){
													t += "<div class='smallStatus status3'>已结束</div>";
												}
											}else if(el.contentType==2){
												t += "<div class='smallStatus status5'>精彩回看</div>";
											}else if(el.contentType==3){
												t += "<div class='smallStatus status1'>专题</div>";
											}
											
											con+="<div class='smallPic pull-left'><div class='smallPicBox'><a href='"+el.contentUrl+"'><img src='"+el.backgroundUrl+"' alt=''/>"+t+"</a>";
											con+="</div><p class='smallPicTit' style='text-align:center;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;'>"+el.name+"</p></div>";
										}
										
										
        							});
									con+="</div></div>"
									div.append(con);
        							$("#content").append(div);
        							break;
        						//标题
        						case 6:
        							var temp = ele.contentList;
        							if(temp.length>0){
										
										//var div = $("<div></div>").addClass("moblieHeader").text(temp[0].name);
										//$("#content").append(div);
										$("#content").css("background-color","#"+temp[0].backgroundColor);
										$("#content").css("background-color","#"+temp[0].backgroundColor).css("color","#"+temp[0].fontColor);;
										
										$("title").html(temp[0].name);
        							}
        							break;
							}
						});
						
						var swiper = new Swiper('.swiper-container', {
							pagination: '.swiper-pagination',
							loop: true,
							paginationClickable: true,
							nextButton: '.swiper-button-next',
							prevButton: '.swiper-button-prev',
							spaceBetween: 30,
							autoplay: 5000,
							effect: 'fade'
						});
						
						setTimeout(function(){
							$(".one-pic img").each(function(index,ele){
								var width = $(ele).css("width");
								console.log(width);
								var height = $(ele).css("height");
								console.log(height);
								var t = parseInt(width)/7.5;
								var h = parseInt(height)/t;
								console.log(t);
								console.log(h);
								$(ele).closest("div.one-pic").css("height",h+"rem");
							});
						},1000);
						
					}
				}else {
					location.href="http://zb.tv189.com/pc/404.html";
				}
			}
	});
	
});

	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = decodeURI(window.location.search.substr(1)).match(reg);
		if (r != null)return unescape(r[2]);
			return null;
	}
	

	
	var bridgeConstant;
	window.onerror = function(err) {
		log('window.onerror: ' + err)
	}
	
	function connectWebViewJavascriptBridge(callback) {
		if (window.WebViewJavascriptBridge) {
			callback(WebViewJavascriptBridge)
		} else {
			document.addEventListener('WebViewJavascriptBridgeReady', function() {
				callback(WebViewJavascriptBridge)
			}, false)
		}
	}
	
	connectWebViewJavascriptBridge(function(bridge) {
		bridgeConstant = bridge;
		var uniqueId = 1
		function log(message, data) {
			var log = document.getElementById('log')
			var el = document.createElement('div')
			el.className = 'logLine'
			el.innerHTML = uniqueId++ + '. ' + message + (data ? ':<br/>' + JSON.stringify(data) : '')
			if (log.children.length) { log.insertBefore(el, log.children[0]) }
			else { log.appendChild(el) }
		}
		bridge.init(function(message, responseCallback) {
			log('JS got a message', message)
			var data = { 'Javascript Responds':'Wee!' }
			log('JS responding with', data)
			responseCallback(data)
		})

		bridge.registerHandler('testJavascriptHandler', function(data, responseCallback) {
			log('ObjC called testJavascriptHandler with', data)
			var responseData = { 'Javascript Says':'Right back atcha!' }
			log('JS responding with', responseData)
			responseCallback(responseData)
		})

		var button = document.getElementById('buttons').appendChild(document.createElement('button'))
		button.innerHTML = 'Send message to ObjC'
		button.onclick = function(e) {
			e.preventDefault()
			var data = 'Hello from JS button'
			log('JS sending message', data)
			bridge.send(data, function(responseData) {
				log('JS got response', responseData)
			})
		}

		document.body.appendChild(document.createElement('br'))

		var callbackButton = document.getElementById('buttons').appendChild(document.createElement('button'))
		callbackButton.innerHTML = 'Fire testObjcCallback'
		callbackButton.onclick = function(e) {
			e.preventDefault()
			log('JS calling handler "testObjcCallback"')
			bridge.callHandler('testObjcCallback', {'foo': 'bar'}, function(response) {
				log('JS got response', response)
			})
		}
	})
