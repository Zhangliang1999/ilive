$(function(){
	//var tomcatBase = "http://mp.zb.tv189.com:80";
	var tomcatBase = "http://apizb.tv189.com:80";
	//var tomcatBase = "http://127.0.0.1:8081";
	//获取企业信息
	$.ajax({
			type : "GET",//请求方式 get/post
			url : tomcatBase + "/ilive/homepage/homepageenterprise.jspx?enterpriseId="+GetQueryString("enterpriseId"),
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
			},
			success : function(data) {
				var enterprise = data.data;
				$("#enterprisename").html(enterprise.enterpriseName);
				$("#enterpriseimg").attr("src",enterprise.enterpriseImg);
				$("#fansnum").html(enterprise.fansNum);
				$("#desc").html(enterprise.enterpriseDesc);
				document.title=enterprise.enterpriseName;
				
				_shareTitle = enterprise.enterpriseName;
				_shareImg = enterprise.enterpriseImg;
				_shareDesc = enterprise.enterpriseDesc;
				_shareTitle2 = enterprise.enterpriseName;
				_shareImg2 = enterprise.enterpriseImg;
				_shareDesc2 = enterprise.enterpriseDesc;
			}
	});
	//获取内容信息
	$.ajax({
			type : "GET",//请求方式 get/post
			url : tomcatBase + "/ilive/homepage/homepagecontent.jspx?enterpriseId="+GetQueryString("enterpriseId"),
			dataType : "jsonp",
			jsonp : "callback",
			cache : false,
			data : {
			},
			success : function(data) {
				var res = data.data;
				console.log(data);
				$("#yugaocontent").empty();
				$("#mobileList").empty();
				$.each(res,function(index,ele){
					if(ele.structureId == 1){
						$("#backgroundimg").attr("src",ele.contentImg);
					}else if(ele.structureId == 3){
						$("#zhibo").html(ele.contentName);
						if(ele.contentUrl==""){
							$("#morezhibolink").remove();
						}else{
							$("#morezhibolink").attr("href",ele.contentUrl);
						}
					}else if(ele.structureId == 4){
						var yugaocontent = "";
						var status;
						var style="";
    					if(ele.contentType == 2){
    						status ="回看";
							style = "yugao";
    					}else if(ele.contentType == 3){
    						status ="专题";
    					}else if(ele.contentType == 1){
							style = "yugao1";
    						switch(ele.liveStatus){
    						case 0:status="预告";break;
    						case 1:status="直播中";break;
    						case 2:status="暂停中";break;
    						case 3:status="直播结束";break;
    						}
    					}
						var str = ele.contentUrl;
						yugaocontent += "<div class='bigPic col-lg-12 mobileActive' onclick='toLive(this)' data-href='"+str+"'>";
						if(ele.contentImg!=""){
							yugaocontent += "<img src='"+ele.contentImg+"'>";
						}
						yugaocontent += "<span class='"+style+"'>"+status+"</span><p class='bigPicMask'>"+ele.contentName+"</p></div>";
						$("#yugaocontent").append(yugaocontent);
					}else if(ele.structureId == 5){
						$("#huigutitle").html(ele.contentName);
						if(ele.contentUrl==""){
							$("#morelink").remove();
						}else{
							$("#morelink").attr("href",ele.contentUrl);
						}
					}else if(ele.structureId == 6){
						var mobileList = "";
						var status;
    					if(ele.contentType == 2){
    						status ="回看";
    					}else if(ele.contentType == 3){
    						status ="专题";
    					}else if(ele.contentType == 4){
    						status ="";
    					}else if(ele.contentType == 1){
    						switch(ele.liveStatus){
    						case 0:status="预告";break;
    						case 1:status="直播中";break;
    						case 2:status="暂停中";break;
    						case 3:status="直播结束";break;
    						}
    					}
						if(index%2==0){
							mobileList +="<div class='listBox'>";
						}
						var str = ele.contentUrl;
						mobileList +="<div class='smallPic pull-left' onclick='toLive(this)' data-href='"+str+"'><div class='smallPicBox'>";
						if(ele.contentImg!=""){
							mobileList +="<img src='"+ele.contentImg+"'>";
						}
						mobileList +="<span class='yugao'>"+status+"</span></div><p class='smallPicTit'>"+ele.contentName+"</p><p class='smallPicText'>"+ele.contentBrief+"</p></div>"; 
						
						if(index%2==0){
							mobileList +="</div>";
						}
						$("#mobileList").append(mobileList);
					}
				});
			}
	});
	
});

	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = decodeURI(window.location.search.substr(1)).match(reg);
		if (r != null)return unescape(r[2]);
			return null;
	}
	
	function toLive(e){
		var url = $(e).attr("data-href");
		console.log(url);
		window.location.href = url;
	}