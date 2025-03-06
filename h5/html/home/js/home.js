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
				console.log(enterprise)
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
						var status = '链接';
						var style="z_status z_link";
    					if(ele.contentType == 2){
    						status ="回看";
							style = "z_status z_review";
    					}else if(ele.contentType == 3){
    						status ="专题";
                            style = "z_status z_project";
    					}else if(ele.contentType == 1){
							// style = "z_status";
    						switch(ele.liveStatus){
    						case 0:status="预告";style = "z_status z_pre";break;
    						case 1:status="直播中";style = "z_status z_ing";break;
    						case 2:status="暂停中";style = "z_status z_ing";break;
    						case 3:status="已结束";style = "z_status z_end";break;
    						}
    					}
						var str = ele.contentUrl;
    					if(ele.contentType == 3){
    						var z_contentId = ele.contentId
							if(tomcatBase == 'http://apizb.tv189.com:80'){
    							str = 'http://zb.tv189.com/topic/topic.html?id='+ z_contentId
							}else{
                                str = "http://zbt.tv189.net/topic/topic.html?id=" + z_contentId
							}
						}
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
                        var status = '链接';
                        var style="z_status z_link";
    					if(ele.contentType == 2){
    						status ="回看";
                            style = "z_status z_review";
    					}else if(ele.contentType == 3){
    						status ="专题";
                            style = "z_status z_project";
    					}else if(ele.contentType == 1){
    						switch(ele.liveStatus){
    						case 0:status="预告";style = "z_status z_pre";break;
    						case 1:status="直播中";style = "z_status z_ing";break;
    						case 2:status="暂停中";style = "z_status z_ing";break;
    						case 3:status="已结束";style = "z_status z_end";;break;
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
						mobileList +="<span class='"+style+"'>"+status+"</span></div><p class='smallPicTit'>"+ele.contentName+"</p></div>";
                    // <p class='smallPicText'>"+ele.contentBrief+"</p>
						
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