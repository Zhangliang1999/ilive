var shareUrl2Wx = window.location.href;
var appInterfaceBase = "http://apizb.tv189.com/ilive";
var statisticBase = "http://statistics.zb.tv189.com";
var _shareTitle,_shareImg,_shareDesc,_shareTitle2,_shareImg2,_shareDesc2;
$(function(){
	 $.ajax({  
            type : "post",  
            dataType : "jsonp",  
            url : appInterfaceBase +"/app/wx/share.jspx",
			jsonp: "callback",
            data : {  
                shareUrl : shareUrl2Wx
            },  
            complete : function() {
            },  
            success : function(msg) {
                    wx.config({  
                        debug : false, //  
                        appId : msg.appId, // 必填，公众号的唯一标识  
                        timestamp : msg.timestamp, // 必填，生成签名的时间戳  
                        nonceStr : msg.nonceStr, // 必填，生成签名的随机串  
                        signature : msg.signature,// 必填，签名，见附录1  
                        jsApiList : [ 'onMenuShareTimeline',  
                                'onMenuShareAppMessage', 'showOptionMenu' ]  
                    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2  
                    });  	
            },  
        });
		wx.ready(function() {
				wx.onMenuShareAppMessage({  
									title : _shareTitle2, // 分享标题  
									link : shareUrl2Wx, // 分享链接  
									imgUrl : _shareImg2,  
									desc : _shareDesc2, // 分享描述  
									success : function() {
									
									}									
				});
								
				wx.onMenuShareTimeline({
									title : _shareTitle, // 分享标题  
									link : shareUrl2Wx, // 分享链接  
									imgUrl : _shareImg,  
									desc : _shareDesc, // 分享描述  
									success : function() {  
										
									}   
				});
					
		});
});

 
    
   
